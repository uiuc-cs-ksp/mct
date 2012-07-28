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

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.importExport.access.ComponentRegistryAccess;
import gov.nasa.arc.mct.services.component.ComponentRegistry;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);
	
	private Utilities() {
	}

	/**
	 * Use the registry isCreatable method to determine if a component is createable
	 * @param component
	 * @return
	 */
	public static boolean isCreateable(AbstractComponent component) {
		//get the registry
		ComponentRegistry registry = (new ComponentRegistryAccess()).getComponentRegistry();
		return registry.isCreatable(component.getClass());
	}
	
	/**
     * Convert from Date to XMLGregorianCalendar. If date is null, return current date.
     * @param date Date date
     * @return XMLGregorianCalendar date
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
    	XMLGregorianCalendar xmlGCDate = null;
    	
    	try {
			GregorianCalendar gcDate = new GregorianCalendar();
			if (date != null) {
			  gcDate.setTime(date);
			} else {
			  gcDate.setTime(new Date());
			}
			xmlGCDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcDate);
			
		} catch (DatatypeConfigurationException ex) {
			
		}
    	return xmlGCDate;
    }

    /**
     * Applies checks to the user selected file. If user selected file is a
     * directory and directory is OK, its children are added to files. If user
     * selected file is a file and file is OK, it is added to files.
     * 
     * @param rootFileOrDir user selected file
     * @return list of files that are OK
     */
    public static List<File> filterSelectedFiles(List<File> rootFileOrDir) {
    	assert rootFileOrDir != null;
    	List<File> files = new ArrayList<File>();
    	// Check for directories

    	// Loop through the array
    	for (File tempFile : rootFileOrDir) {
    		if (tempFile.isDirectory()) {
    			FilenameFilter fileNameFilter = new FilenameFilter() {
    				public boolean accept(File dir, String name) {
    					return name.endsWith(".xml");
    				}
    			};
    			String[] children = tempFile.list(fileNameFilter);
    			for (int i = 0; i < children.length; i++) {
    				File file = new File(tempFile
    						+ System.getProperty("file.separator")
    						+ children[i]);
    				if (verifyExists(file)) {
    					files.add(file);
    				} else {
    					LOGGER.error(file
    							+ " is not a valid file. Loader will skip.");
    				}
    			}
    		} else {
    			if (verifyExists(tempFile)) {
    				files.add(tempFile);
    			} else {
    				LOGGER.error(rootFileOrDir
    						+ " is not a valid file. Loader will skip.");
    			}
    		}

    	}
    	return files;
    }

    /**
     * Tests a File can be read, exists and is a file, not a directory.
     * 
     * @param aFile
     * @return boolean indicating if the file passes all tests
     */
    private static boolean verifyExists(File aFile) {
    	assert aFile != null;
    	boolean validFile = true;

    	if (!aFile.canRead()) {
    		throw new IllegalArgumentException("File cannot be read: " + aFile);
    	}
    	return validFile;
    }

}
