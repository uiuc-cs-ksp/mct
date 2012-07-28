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
package gov.nasa.arc.mct.importExport.provider;

import gov.nasa.arc.mct.importExport.provider.ImportAction;
import gov.nasa.jsc.mct.importExport.utilities.Utilities;
import gov.nasa.jsc.mct.importExport.utilities.XMLFileFilter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class ImportActionTest {
	ImportAction importAction;
	List<File> okFiles;
	
	@BeforeMethod
	void setup()  { 
	}
	
	@Test 
	public void testActionPerformedFilter(){
   	    okFiles = Utilities.filterSelectedFiles(
   	    		   Arrays.asList(new File("src/test/resources/testOutput/multComp.xml")));
		Assert.assertEquals(okFiles.size(), 1);
		
		okFiles = Utilities.filterSelectedFiles(
				             Arrays.asList(new File("src/test/resources/testOutput/")));
		Assert.assertEquals(okFiles.size(), 5);
	}
	
	@Test 
	public void testXMLFilter(){
		XMLFileFilter xmlfilter = new XMLFileFilter();
		Assert.assertFalse(xmlfilter.accept(new File("")));		
		Assert.assertTrue(xmlfilter.accept(new File("src/test/resources/testOutput/")));
		Assert.assertTrue(xmlfilter.accept(new File("src/test/resources/testOutput/multComp.xml")));
	}
	
	@Test (expectedExceptions = IllegalArgumentException.class)
	public void testActionPerformedFilterNotafile(){
		okFiles = Utilities.filterSelectedFiles(Arrays.asList(new File("UNIT-Test-not-a-file")));
	}

	@Test 
	public void testTop(){
		importAction = new ImportAction();
		//importAction.selectFiles(null);
	}
}

