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
package gov.nasa.arc.mct.gui.actions;

import gov.nasa.arc.mct.gui.SelectionProvider;
import gov.nasa.arc.mct.gui.housing.MCTDirectoryArea;
import gov.nasa.arc.mct.gui.housing.MCTHousing;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;
import gov.nasa.arc.mct.platform.spi.PersistenceProvider;
import gov.nasa.arc.mct.platform.spi.Platform;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.platform.spi.WindowManager;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class TestDeleteAll {
    @Mock ActionContextImpl mockContext;
    @Mock MCTHousing mockHousing;
    @Mock SelectionProvider mockSelection;
    @Mock MCTDirectoryArea mockDirectory;
    @Mock Platform mockPlatform;
    @Mock PersistenceProvider mockPersistence;
    @Mock WindowManager mockWindowing;
    
    private Platform oldPlatform;
    
    @BeforeClass
    public void cachePlatform() {
        oldPlatform = PlatformAccess.getPlatform();
    }
    
    @AfterClass
    public void restorePlatform() {
        new PlatformAccess().setPlatform(oldPlatform);
    }
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        new PlatformAccess().setPlatform(mockPlatform);
    }
    
}
