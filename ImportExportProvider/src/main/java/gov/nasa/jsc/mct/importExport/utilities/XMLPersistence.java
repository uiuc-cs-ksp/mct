/*******************************************************************************
 * Mission Control Technologies, Copyright (c) 2009-2012, United States Government
 * as represented by the Administrator of the National Aeronautics and Space 
 * Administration. All rights reserved.
 *
 * The MCT platform is licensed under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 *
 * MCT includes source code licensed under additional open source licenses. See 
 * the MCT Open Source Licenses file included with this distribution or the About 
 * MCT Licenses dialog available at runtime from the MCT Help menu for additional 
 * information. 
 *******************************************************************************/
package gov.nasa.jsc.mct.importExport.utilities;

import gov.nasa.arc.mct.components.ExtendedProperties;
import gov.nasa.arc.mct.importExport.provider.generated.ComponentListType;
import gov.nasa.arc.mct.importExport.provider.generated.ObjectFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Class to marshal and unmarshal data to/from an XML file or a string.
 */
public class XMLPersistence {
	
	/** Location and name of schema to be added to the XML file **/
	private static final String schemaLocation = "http://gov.nasa.arc.mct importExport.xsd";
	private static final Logger LOGGER = LoggerFactory
			                            .getLogger(XMLPersistence.class);

	/**
	 * Private unaccessible constructor
	 */
	private XMLPersistence() {
	}

	/**
	 * Given an object of type ComponentListType write it to an XML file
	 * @param primaryObject An object of type ComponentListType
	 * @param file File to write the data to
	 * @throws IOException
	 * @throws ValidationException 
	 */
	public static void marshal(ComponentListType primaryObject, File file) 
			           throws IOException, ValidationException{
		String errStr = null;
		
		if (primaryObject == null || primaryObject.getComponent() == null ||
				primaryObject.getComponent().size() == 0) {
			return;
		}
		
		// Convert the ComponentListType into a JAXBElement.
		ObjectFactory objFactory = new ObjectFactory();
		JAXBElement<ComponentListType> element = objFactory.createComponents(primaryObject);
		ValidationEventCollector validationEventCollector = 
                                             new ValidationEventCollector();

		FileOutputStream fileOut = null;

		// open file
		if (file == null || file.equals("")) {
			throw new IllegalArgumentException(
					"XMLPersistence: File name is null or empty string");
		}

		try {
			fileOut = new FileOutputStream(file);
			
			// create marshaller
			Marshaller marshaller = createNewMarshaller(primaryObject);
			
			// Get schema and set marshaller for validation
			Schema schema = getSchema();
            marshaller.setSchema(schema);
			marshaller.setEventHandler(validationEventCollector);
			
			// Marshal
			marshaller.marshal(element, fileOut);
			fileOut.close();
			
		} catch (JAXBException e) {
			errStr = String.format("JAXBException on file:%s due to: %s",
					file, e.getMessage());
		} catch (FileNotFoundException e) {
			errStr = String.format("Unable to find schema for xml file: %s Cause: %s. " +
					"Ensure Schema Path is set.", file, e.getMessage());
		} catch (IOException e) {
			errStr = String.format("IOException on file:%s due to: %s",
					file, e.getMessage());
		} catch (SAXException e) {
			errStr = String.format("SAXException on file:%s due to: %s",
					file, e.getMessage());
		} finally {
			handleError(errStr, validationEventCollector, file);
		}
	}

	/**
     * Marshals the data and return the marshalled String. The object is
     * converted between byte and Unicode characters using the named encoding
     * "UTF-8".
     * 
     * @param toBeMarshalled
     *            the object whose data is to be marshalled.
     * @throws JAXBException - JAXB XML exception handling.
     * @throws UnsupportedEncodingException - Unsupported encoding exception handling.
     * @return out marshalled string.
     * @param <T> string
     */
    public static <T> String marshal(T toBeMarshalled) throws JAXBException, 
                                                         UnsupportedEncodingException {
    	Map<Class<?>, JAXBContext> marshalCache = 
    			                          new ConcurrentHashMap<Class<?>, JAXBContext>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Class<?> clazz = toBeMarshalled.getClass();
        JAXBContext ctxt = marshalCache.get(clazz);
        if (ctxt == null) {
            ctxt = JAXBContext.newInstance(clazz);
            marshalCache.put(clazz, ctxt);
        }
        Marshaller marshaller = ctxt.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "ASCII");
        marshaller.marshal(toBeMarshalled, out);
        return out.toString("ASCII");
    }
	
	/**
	 * Create a new marshaller to write data to an XML file
	 * @return a new marshaller
	 * @throws JAXBException 
	 */
	private static Marshaller createNewMarshaller(ComponentListType primaryObject) 
			throws JAXBException {
		Marshaller marshaller = null;
		// Package location of object
		String objectPackage = primaryObject.getClass().getPackage().getName();

		JAXBContext jaxbContext = JAXBContext.newInstance(objectPackage, 
				XMLPersistence.class.getClassLoader());

		marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		// Add schema location and name to XML file
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
		
		return marshaller;
	}
	
	/**
	 * Validates that the XML file conforms to the ImportExport.xsd schema and
	 * unmarshalls it into a JAXBElement. Uses a JABXContext created from the
	 * package of classes generated with the xjc binding compiler applied to the
	 * schema. Must read the ImportExport.xsd schema file for validation.
	 * 
	 * @param xmlFile to be unmarshalled
	 * @return null upon error, else ComponentListType
	 * @throws ValidationException 
	 */
	public static ComponentListType unmarshal(File xmlFile) throws IOException, ValidationException {
		
		if (xmlFile == null || xmlFile.equals("") || !xmlFile.exists()) {
			throw new IllegalArgumentException(
					"XMLPersistence: File name is null, the empty string, or does not" +
					" exist.");
		}
		
		ValidationEventCollector validationEventCollector = 
				                               new ValidationEventCollector();
		Schema schema;
		JAXBElement<ComponentListType> root = null;
		String errStr = null;
		ComponentListType componentListType = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(
					                ComponentListType.class.getPackage().getName(),
					                XMLPersistence.class.getClassLoader());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// Get schema and add to unmarshaller
			schema = getSchema();
			unmarshaller.setSchema(schema);
			
			unmarshaller.setEventHandler(validationEventCollector);
			// Unmarshal the file
			root = unmarshaller.unmarshal(new StreamSource(xmlFile),
					ComponentListType.class);
			
			componentListType = root.getValue();

		} catch (SAXException saxe) {
			errStr = String.format("SAXException on file:%s due to: %s ",
					xmlFile, saxe.getMessage());
		} catch (JAXBException jaxbe) {
			errStr = String.format("JAXBException on file:%s due to: %s",
					xmlFile, jaxbe.toString());
		} catch (FileNotFoundException e) {
			errStr = String.format("Unable to find schema for xml file: %s Cause: %s. " +
					"Ensure Schema Path is set.", xmlFile, e.getMessage());
		} finally {
			handleError(errStr, validationEventCollector, xmlFile);
		}
		
		return componentListType;
	}
	
	/**
	 * Unmarshal the string and return it as an ExtendedProperties object
	 * @param props String to unmarshal
	 * @return An ExtendedProperties object
	 */
	public static ExtendedProperties unmarshal(String props) {
		Unmarshaller unmarshaller;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ExtendedProperties.class);
			unmarshaller = jaxbContext.createUnmarshaller();
			InputStream is = new ByteArrayInputStream(props.getBytes("ASCII"));
			return ExtendedProperties.class.cast(unmarshaller.unmarshal(is));
		} catch (JAXBException je) {
			throw new RuntimeException(je);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get the Schema
	 * @return
	 * @throws FileNotFoundException If schema can't be found
	 * @throws SAXException
	 */
	private static Schema getSchema() throws FileNotFoundException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		StreamSource streamSource = getSchemaSource();
		return schemaFactory.newSchema(streamSource);
	}
	
	/**
	 * Get the StreamSource attached to the ImportExport.xsd schema file. The
	 * schema file can be found in two mutually exclusive ways. If property
	 * mccAppSchemaPath is set, then the file location will be opened.
	 * Alternatively, if mccAppSchemaPath is not set, this will attempt to get
	 * the schema as a resource from the classpath defined by
	 * xsdSchemaResourceClasspath. Note that the file method does not fallback
	 * to the resource method.
	 * 
	 * @return The schema's StreamSource
	 * @throws FileNotFoundException
	 */
	private static StreamSource getSchemaSource() throws FileNotFoundException {
		String schemaPath = System.getProperty("importExportSchemaPath");
		StreamSource streamSource = null;
		if (schemaPath != null) {
			streamSource = new StreamSource(new FileInputStream(schemaPath));
		} else {
			ResourceBundle bundle = ResourceBundle.getBundle("ImportExportProvider");
			String xsdSchemaResourceClasspath = bundle
					.getString("xsdSchemaResourceClasspath");
			assert xsdSchemaResourceClasspath != null;

			InputStream is = XMLPersistence.class.getClassLoader()
					               .getResourceAsStream(xsdSchemaResourceClasspath);
			if (is == null) {
				is = ClassLoader.getSystemClassLoader().getResourceAsStream(
						xsdSchemaResourceClasspath);
			}
			if (is == null) {
				throw new FileNotFoundException();
			} else {
				streamSource = new StreamSource(is);
			}
		}

		return streamSource;
	}
	
	/**
	 * If an exception was thrown or if the validation event collector has an error, log
	 * the error and throw an IOException
	 * @param errStr Error message created when/if an exception was thrown
	 * @param validationEventCollector
	 * @param file XML file that was attempted to be processed
	 * @throws ValidationException 
	 */
	private static void handleError(String errStr,
			                ValidationEventCollector validationEventCollector, File file)
	                    throws IOException, ValidationException {
		
		if (validationEventCollector.hasEvents()) {
			for (ValidationEvent validationEvent : validationEventCollector
					.getEvents()) {
				String msg = validationEvent.getMessage();
				String m = String.format(
						"xml file: %s reports validation event: %s", file,
						msg);
				LOGGER.error(m);
				throw new ValidationException(m);
			}
		} else {
			if (errStr != null) {
				LOGGER.error(errStr);
				throw new IOException(errStr);
			}
		}
	}

}
