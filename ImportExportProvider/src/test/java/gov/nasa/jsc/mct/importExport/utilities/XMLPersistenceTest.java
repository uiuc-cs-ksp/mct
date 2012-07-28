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

import gov.nasa.arc.mct.importExport.provider.generated.ComponentListType;
import gov.nasa.arc.mct.importExport.provider.generated.ComponentType;
import gov.nasa.arc.mct.importExport.provider.generated.ObjectFactory;
import gov.nasa.jsc.mct.importExport.utilities.XMLPersistence;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class XMLPersistenceTest {
	
	@DataProvider(name="increasingingComplextInputData")
	public Object[][] increasingingComplextInputData() {
		return new Object[][] {
			new Object[] {"src/test/resources/testData/testCanvas.xml", 
					"54680f8d439a4e2a872b9319e9ca3e67" },
			new Object[] {"src/test/resources/testData/testCyclic.xml", 
					"e0bc743521aa46de95b8d533905577a0" },
			new Object[] {"src/test/resources/testData/testMultTopLevelComps.xml", 
					"54680f8d439a4e2a872b9319e9ca3e67" },
			new Object[] {"src/test/resources/testData/testUnknownRef.xml", 
			        "54680f8d439a4e2a872b9319e9ca3e67" },

		};
	}
    
    private final String schemaFile = "src/main/resources/importExportSchema/importExport.xsd";
	
    @BeforeMethod
    void setup() { 
        System.setProperty("importExportSchemaPath", schemaFile); 
    }
    
    /////////////// Import tests ///////////////////////

    /**
     * Should throw an exception when it can't find the schema
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=IOException.class)
    public void importCannotFindSchema() throws IOException, ValidationException{ 
    	System.setProperty("importExportSchemaPath", "xxxxxxxxxxxxxxx-UNIT-TEST.xsd"); 
    	XMLPersistence.unmarshal(
    			              new File("src/test/resources/testData/doesntValidate.xml"));
    }
    
    /**
     * Should throw an exception when the file to unmarshal does not exist
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=IllegalArgumentException.class)
    public void importBadFile() throws IOException, ValidationException{ 
    	XMLPersistence.unmarshal(new File("bad.xml"));
    }
    
    /**
     * Should throw an exception when the XML file doesn't validate against the schema
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=ValidationException.class)
    public void importDoesntValidate() throws IOException, ValidationException{ 
        XMLPersistence
        		.unmarshal(new File("src/test/resources/testData/doesntValidate.xml"));
    }
    
    /**
     * Test of unmarshalling good data
     * @param inputFilename
     * @param expectedRootExternalKey
     * @throws Exception
     */
    @Test (enabled=true, dataProvider="increasingingComplextInputData")
    public void importTest(String inputFilename, String expectedRootID) 
  		  throws Exception {
  	
  	  File file = new File(inputFilename);
  	  
  	  ComponentListType components = XMLPersistence.unmarshal(file);
  	  ComponentType component = components.getComponent().get(0);
  	  
  	  Assert.assertNotNull(component);
  	  Assert.assertEquals(component.getComponentId(), expectedRootID);
  	  
    }
    
    ////////////// Export tests ///////////////////////
    
    /**
     * File which does not exist should throw exception
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=IOException.class)
    public void exportCannotFindSchema() throws IOException, ValidationException{ 
          System.setProperty("importExportSchemaPath", "xxxxxxxxxxxxxxx-UNIT-TEST.xsd");
          XMLPersistence.marshal(newCompWOChildren(), new File(""));
    }
    
    /**
     * Null file should throw exception
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=IllegalArgumentException.class)
    public void exportBadFile() throws IOException, ValidationException{ 
          XMLPersistence.marshal(newCompWOChildren(), null);
    }
    
    /**
     * Should throw an exception when the data to marshal doesn't validate against the
     * schema
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true, expectedExceptions=ValidationException.class)
    public void exportDoesntValidate() throws IOException, ValidationException{ 
        XMLPersistence.marshal(illegalComp(), 
        		               new File("testExportDoesntValidate.xml"));
    }
    
    /**
     * Null object should not throw an exception. It should do nothing.
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true)
    public void exportNullOject() throws IOException, ValidationException{
          XMLPersistence.marshal(null, new File("test.xml"));
    }
    
    /**
     * Test of marshalling of good data
     * @throws IOException
     * @throws ValidationException 
     */
    @Test  (enabled=true)
    public void exportTest() throws IOException, ValidationException{
    	File file = new File("testExportTest.xml");
    	file.delete();
    	XMLPersistence.marshal(newCompWOChildren(), file);
    	if (!file.exists()) {
    		Assert.fail("XML file was not generated.");
    	}
    	
    	ComponentListType components = XMLPersistence.unmarshal(file);
    	ComponentType component = components.getComponent().get(0);
    	Assert.assertNotNull(component);
    	Assert.assertEquals(component.getExternalKey(), "TheExternalKey");
    	  
    	file.delete();
    }
    
    
    private ComponentListType illegalComp() {
    	ObjectFactory objFactory = new ObjectFactory();
    	ComponentListType compList = objFactory.createComponentListType();
    	compList.setSchemaVersion(new BigDecimal(1.0));
    	ComponentType comp = objFactory.createComponentType();
    	comp.setComponentId("askjdfskdjfk");
    	compList.getComponent().add(comp);
    	return compList;
    }
    
    private ComponentListType newCompWOChildren() {
    	
    	ObjectFactory objFactory = new ObjectFactory();
    	ComponentListType compList = objFactory.createComponentListType();
    	
    	compList.setSchemaVersion(new BigDecimal(1.0));
    	ComponentType comp = objFactory.createComponentType();
    	comp.setComponentId("askjdfskdjfk");
    	comp.setComponentType("TheCompType");
    	comp.setCreationDate(Utilities.convertToXMLGregorianCalendar(null));
    	comp.setCreator("TheCreator");
    	comp.setExternalKey("TheExternalKey");
    	comp.setName("TheName");
    	comp.setOwner("TheOwner");
    	comp.setToplevel(true);
    	
    	compList.getComponent().add(comp);
    	
    	return compList;
    }
}
