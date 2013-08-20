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

import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.housing.MCTAbstractHousing;
import gov.nasa.arc.mct.gui.housing.MCTStandardHousing;
import gov.nasa.arc.mct.gui.housing.registry.UserEnvironmentRegistry;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;
import gov.nasa.arc.mct.osgi.platform.OSGIRuntime;
import gov.nasa.arc.mct.osgi.platform.OSGIRuntimeImpl;

import java.awt.event.ActionEvent;
import java.util.Collection;

import org.slf4j.LoggerFactory;

/**
 * This action closes all open windows, thus quitting MCT
 */
@SuppressWarnings("serial")
public class QuitAction extends ContextAwareAction {

    private static String TEXT = "Quit MCT";

    private Collection<MCTAbstractHousing> housings;
    
    public QuitAction() {
        super(TEXT);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Close every window
        for (MCTAbstractHousing housing : housings) {
            housing.closeHousing();
        }
        
        // User may have cancelled some window closings, so verify housing count
        if (UserEnvironmentRegistry.getHousingCount() == 0) {
            OSGIRuntime osgiRuntime = OSGIRuntimeImpl.getOSGIRuntime();
            try {
                osgiRuntime.stopOSGI();
            } catch (Exception e1) {
                LoggerFactory.getLogger(MCTStandardHousing.class).warn(e1.getMessage(), e1);
            }
            System.exit(0);
        }
    }

    @Override
    public boolean canHandle(ActionContext context) {
        housings = getAllHousings();
        if (housings == null || housings.isEmpty())
            return false;
        
        return (MCTAbstractHousing) ((ActionContextImpl) context).getTargetHousing() != null;
    }

    @Override
    public boolean isEnabled() {
        return housings != null && housings.size() > 0;
    }

    Collection<MCTAbstractHousing> getAllHousings() {
        return UserEnvironmentRegistry.getAllHousings();
    }
}