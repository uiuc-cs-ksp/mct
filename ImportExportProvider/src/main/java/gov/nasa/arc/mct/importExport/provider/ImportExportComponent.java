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

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.JAXBModelStatePersistence;
import gov.nasa.arc.mct.components.ModelStatePersistence;

import java.util.concurrent.atomic.AtomicReference;

public class ImportExportComponent extends AbstractComponent {
	private AtomicReference<ImportExportModel> model = 
			           new AtomicReference<ImportExportModel>(new ImportExportModel());
	
	
	public ImportExportModel getModel() {
		return model.get();
	}
	
	@Override
	protected <T> T handleGetCapability(Class<T> capability) {
		if (ModelStatePersistence.class.isAssignableFrom(capability)) {
		    JAXBModelStatePersistence<ImportExportModel> persistence = 
		    		               new JAXBModelStatePersistence<ImportExportModel>() {

				@Override
				protected ImportExportModel getStateToPersist() {
					return model.get();
				}

				@Override
				protected void setPersistentState(ImportExportModel modelState) {
					model.set(modelState);
				}

				@Override
				protected Class<ImportExportModel> getJAXBClass() {
					return ImportExportModel.class;
				}
		        
			};
			
			return capability.cast(persistence);
		}
		
		return null;
	}
}
