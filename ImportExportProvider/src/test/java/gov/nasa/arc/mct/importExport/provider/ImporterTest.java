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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.JAXBModelStatePersistence;
import gov.nasa.arc.mct.components.ModelStatePersistence;
import gov.nasa.arc.mct.importExport.access.ComponentRegistryAccess;
import gov.nasa.arc.mct.platform.spi.PersistenceProvider;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.platform.spi.Platform;
import gov.nasa.arc.mct.policy.ExecutionResult;
import gov.nasa.arc.mct.policy.PolicyContext;
import gov.nasa.arc.mct.services.component.ComponentRegistry;
import gov.nasa.arc.mct.services.component.PolicyManager;
import gov.nasa.arc.mct.services.internal.component.ComponentInitializer;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class ImporterTest {
	
	private ComponentRegistryAccess access;
	private TestAbstractComponent componentA;
	private TestAbstractComponent componentB;
	private TestAbstractComponent componentC;
	private TestAbstractComponent componentD;
	private TestAbstractComponent componentE;
	private TestAbstractComponent componentF;
	private TestAbstractComponent componentG;
	private TestIEComponent ieComponentDate;
	private TestIEComponent ieComponentFileName1;
	private TestIEComponent ieComponentFileName2;
	private TestIEComponent ieComponentFileName3;
	private TestIEComponent ieComponentFileName4;
	private TestIEComponent ieComponentFileName5;
	private TestIEComponent ieComponentFileName6;
	private int count = 0;
	
	@Mock private ComponentRegistry mockRegistry;
	@Mock private Platform mockPlatform;
	@Mock private PolicyManager mockPolicyManager;
	@Mock private PersistenceProvider mockPersistenceProvider;
	@Mock private ComponentRegistryAccess mockRegistryAccess;
	
	@DataProvider(name="validData")
	public Object[][] validData() {
		return new Object[][] {
			new Object[] {"src/test/resources/testData/testBadAssocCompID.xml", 
					"My Collection", false, 1 },
			new Object[] {"src/test/resources/testData/testBadClassType.xml", 
					"BadClassType", true, 1 },
			new Object[] {"src/test/resources/testData/testCanvas.xml", 
					"My Collection", false, 1 },
//			new Object[] {"src/test/resources/testData/testCanvasInCanvas.xml", 
//			        "Fred", false, 1 },
	        new Object[] {"src/test/resources/testData/testCyclic.xml", 
                    "Cyclic", true, 1 },
            new Object[] {"src/test/resources/testData/testLotsOfPUIs.xml", 
                    "X", false, 1 },
            new Object[] {"src/test/resources/testData/testMultTopLevelComps.xml", 
                    "My Collection", false, 2 },
            new Object[] {"src/test/resources/testData/testUnknownRef.xml", 
                    "My Collection", false, 1 },
		};
	}
	
	@DataProvider(name="invalidData")
	public Object[][] invalidData() {
		return new Object[][] {
			new Object[] {"src/test/resources/testData/testInvalid_MissingData.xml" },
			new Object[] {"src/test/resources/testData/testInvalid_WrongVersion.xml" },
			new Object[] {"src/test/resources/testData/testInvalid_NoSuchFile.xml" },
		};
	}
	
	@BeforeClass
	public void beforeClass() {
		
	}

  @BeforeMethod
  public void beforeMethod() {
	  MockitoAnnotations.initMocks(this);
	  int base = count * 12;
	  
	  access = new ComponentRegistryAccess();
	  
	  ieComponentDate = new TestIEComponent();
	  ieComponentDate.setId(String.valueOf(base + 1));
	  ieComponentFileName1 = new TestIEComponent();
	  ieComponentFileName1.setId(String.valueOf(base + 2));
	  ieComponentFileName2 = new TestIEComponent();
	  ieComponentFileName2.setId(String.valueOf(base + 3));
	  ieComponentFileName3 = new TestIEComponent();
	  ieComponentFileName3.setId(String.valueOf(base + 4));
	  ieComponentFileName4 = new TestIEComponent();
	  ieComponentFileName4.setId(String.valueOf(base + 5));
	  ieComponentFileName5 = new TestIEComponent();
	  ieComponentFileName5.setId(String.valueOf(base + 6));
	  ieComponentFileName6 = new TestIEComponent();
	  ieComponentFileName6.setId(String.valueOf(base + 7));
	  
	  componentB = createAbstractComponent("compB", String.valueOf(base + 8));
	  componentC = createAbstractComponent("compC", String.valueOf(base + 9));
	  componentD = createAbstractComponent("compD", String.valueOf(base + 10));
	  componentE = createAbstractComponent("compE", String.valueOf(base + 11));
	  componentF = createAbstractComponent("compF", String.valueOf(base + 12));
	  componentG = createAbstractComponent("compG", String.valueOf(base + 13));
	  
	  Mockito.when(mockRegistry
			 .newInstance(Mockito.eq(ImportExportComponent.class), 
					       Mockito.any(AbstractComponent.class)))
			 .thenReturn(ieComponentDate)
			 .thenReturn(ieComponentFileName1)
			 .thenReturn(ieComponentFileName2)
			 .thenReturn(ieComponentFileName3)
			 .thenReturn(ieComponentFileName4)
			 .thenReturn(ieComponentFileName5)
			 .thenReturn(ieComponentFileName6);
	  Mockito.when(mockRegistry.newInstance(Mockito.anyString()))
	         .thenReturn(componentB)
	         .thenReturn(componentC)
	         .thenReturn(componentD)
	         .thenReturn(componentE)
	         .thenReturn(componentF)
	         .thenReturn(componentG);
	  Mockito.when(mockRegistry.isCreatable(Mockito.any(Class.class)))
	         .thenReturn(true);
	  
	  Mockito.when(mockPlatform.getPolicyManager()).thenReturn(mockPolicyManager);
	  Mockito.when(mockRegistryAccess.getComponentRegistry()).thenReturn(mockRegistry);
	  Mockito.when(mockPlatform.getPersistenceProvider())
	         .thenReturn(mockPersistenceProvider);
	  (new PlatformAccess()).setPlatform(mockPlatform);
	  
	  Mockito.when(mockPolicyManager.execute(Mockito.anyString(), 
                   Mockito.any(PolicyContext.class)))
             .thenReturn(new ExecutionResult(null,true,""));
	  
	  access.setRegistry(mockRegistry);
	  count++;
  }
  
  @AfterMethod
  public void tearDown() {
      access.releaseRegistry(mockRegistry);
  }
  
  
  @Test (enabled=true, dataProvider="validData")
  public void testImport(String inputFilename, String expectedRootName, boolean cyclical,
		                 int numTopLevel) 
		  throws Exception {

	  File file = new File(inputFilename);
	  ArrayList<File> files = new ArrayList<File>();
	  files.add(file);

	  componentA = createAbstractComponent("compA", "0");
	  componentA.save();
	  Assert.assertEquals(0, componentA.getComponents().size());

      Importer importer = new Importer(files, "TheOwner", componentA);
	  importer.doInBackground();

	  Assert.assertEquals(componentA.getComponents().size(), 1);
	  Assert.assertEquals(componentA.getComponents().get(0)
			  .getComponents().size(), 1);
	  Assert.assertEquals(componentA.getComponents().get(0)
			  .getComponents().get(0)
			  .getComponents().size(), numTopLevel);
	  AbstractComponent importedComp = componentA.getComponents().get(0)
			  .getComponents().get(0)
			  .getComponents().get(0);

	  Assert.assertEquals(importedComp.getDisplayName(), expectedRootName);
	  // Shows that save has been called on all the components
	  if (cyclical) {
	      verifyCyclicalSave(componentA);
	  } else {
		  verifySave(componentA);
	  }
  }
  
  @Test
  public void testMultipleXMLFiles() {
	  List<File> files = new ArrayList<File>();
	  File file = new File("src/test/resources/testData/testCanvas.xml");
	  files.add(file);
	  file = new File("src/test/resources/testData/testLotsOfPUIs.xml");
	  files.add(file);
	  file = new File("src/test/resources/testData/testBadAssocCompID.xml");
	  files.add(file);

	  componentA = createAbstractComponent("compA", "0");
	  componentA.save();

	  Importer importer = new Importer(files, "TheOwner", componentA);
	  importer.doInBackground();

	  Assert.assertEquals(componentA.getComponents().size(), 1);
	  Assert.assertEquals(componentA.getComponents().get(0)
			  .getComponents().size(), 3);
	  
	  // Verify all 3 file component have 1 child (which is the top level comp in the file
	  for (AbstractComponent comp : componentA.getComponents().get(0).getComponents()) {
		  Assert.assertEquals(comp.getComponents().size(), 1);
	  }
	  
	  // Shows that save has been called on all the components
	  verifyCyclicalSave(componentA);
  }
  
  /**
   * Verify an invalid XML file does not throw an exception.  It should not create the
   * "Imported on date" component.
   */
  @Test(enabled=true, dataProvider="invalidData")
  public void testInvalid(String invalidFile) {
	  File file = new File(invalidFile);
	  ArrayList<File> files = new ArrayList<File>();
	  files.add(file);

	  componentA = createAbstractComponent("compA", "0");
	  componentA.save();

	  Importer importer = new Importer(files, "TheOwner", componentA);
	  importer.doInBackground();

	  Assert.assertEquals(componentA.getComponents().size(), 0);
  }
  
 

  // Do not use this on cyclical component trees!
  private void verifySave(AbstractComponent comp) {
	  Assert.assertTrue(comp.isDirty());
	  for (AbstractComponent child : comp.getComponents()) {
		  verifySave(child);
	  }
  }
  
  int depth = 0;
  private void verifyCyclicalSave(AbstractComponent comp) {
	  depth++;
	  Assert.assertTrue(comp.isDirty());
	  for (AbstractComponent child : comp.getComponents()) {
		  if (depth < 6) {
		      verifyCyclicalSave(child);
		  }
	  }
  }

  private TestAbstractComponent createAbstractComponent(String componentName, String id) {
	  TestAbstractComponent comp = new TestAbstractComponent();
	  ComponentInitializer ci = comp.getCapability(ComponentInitializer.class);
	  ci.setId(id);
	  return comp;
  }

  public static class TestAbstractComponent extends AbstractComponent {
	  private AtomicReference<TestModel> model = 
			  new AtomicReference<TestModel>(new TestModel());

	  public void setModelValue(String v) {
		  model.get().setValue(v);
	  }

	  public String getModelValue() {
		  return model.get().getValue();
	  }

	  @Override
	  protected <T> T handleGetCapability(Class<T> capability) {
		  if (ModelStatePersistence.class.isAssignableFrom(capability)) {
			  JAXBModelStatePersistence<TestModel> persistence = 
					  new JAXBModelStatePersistence<TestModel>() {

				  @Override
				  protected TestModel getStateToPersist() {
					  return model.get();
				  }

				  @Override
				  protected void setPersistentState(TestModel modelState) {
					  model.set(modelState);
				  }

				  @Override
				  protected Class<TestModel> getJAXBClass() {
					  return TestModel.class;
				  }

			  };

			  return capability.cast(persistence);
		  }
		  return super.handleGetCapability(capability);
	  }
  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class TestModel {
	  private String value;

	  public String getValue()  {
		  return value;
	  }

	  public void setValue(String v) {
		  value = v;
	  }
  }

  public static class TestIEComponent extends ImportExportComponent {
	  private AtomicReference<TestModel> model = 
			  new AtomicReference<TestModel>(new TestModel());

	  public void setModelValue(String v) {
		  model.get().setValue(v);
	  }

	  public String getModelValue() {
		  return model.get().getValue();
	  }

	  @Override
	  protected <T> T handleGetCapability(Class<T> capability) {
		  if (ModelStatePersistence.class.isAssignableFrom(capability)) {
			  JAXBModelStatePersistence<TestModel> persistence = 
					  new JAXBModelStatePersistence<TestModel>() {

				  @Override
				  protected TestModel getStateToPersist() {
					  return model.get();
				  }

				  @Override
				  protected void setPersistentState(TestModel modelState) {
					  model.set(modelState);
				  }

				  @Override
				  protected Class<TestModel> getJAXBClass() {
					  return TestModel.class;
				  }

			  };

			  return capability.cast(persistence);
		  }
		  return super.handleGetCapability(capability);
	  }
  }

}
