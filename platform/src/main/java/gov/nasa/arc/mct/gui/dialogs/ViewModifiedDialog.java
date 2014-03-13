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
package gov.nasa.arc.mct.gui.dialogs;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.ObjectManager;
import gov.nasa.arc.mct.gui.OptionBox;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.housing.Inspector;
import gov.nasa.arc.mct.gui.impl.WindowManagerImpl;
import gov.nasa.arc.mct.platform.spi.Platform;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.policy.PolicyContext;
import gov.nasa.arc.mct.policy.PolicyInfo;


public class ViewModifiedDialog  {
    private static final ResourceBundle BUNDLE = 
            ResourceBundle.getBundle(
                    Inspector.class.getName().substring(0, 
                            Inspector.class.getName().lastIndexOf("."))+".Bundle");
    
    private View view;

    public ViewModifiedDialog(View view) {
        super();
        this.view = view;
    }
    
    /**
     * Prompt the user to commit or abort pending changes, 
     * if there are any. Note that this may not be possible 
     * (component may not be writeable, for instance). 
     * 
     * This only returns false when the action is explicitly 
     * aborted (so if there is no prompt because the user 
     * chooses Save, this still returns true.)
     * 
     * @return false if change was aborted
     */
    public boolean commitOrAbortPendingChanges() {
        AbstractComponent committedComponent = PlatformAccess.getPlatform().getPersistenceProvider().getComponent(view.getManifestedComponent().getComponentId());
        if (committedComponent == null)
            return true;
        
        AbstractComponent comp = view.getManifestedComponent();
        if (!isComponentWriteableByUser(comp))
            return true;
        
        String save = BUNDLE.getString("view.modified.alert.save");
        String abort = BUNDLE.getString("view.modified.alert.abort");
        
        // Show options - Save, Abort, or maybe Save All
        ObjectManager om = comp.getCapability(ObjectManager.class);
        Set<AbstractComponent> modified = om != null ? 
                om.getAllModifiedObjects() : Collections.<AbstractComponent>emptySet();
        String[] options = new String[]{ save, abort };
    
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put(WindowManagerImpl.MESSAGE_TYPE, OptionBox.WARNING_MESSAGE);
        hints.put(WindowManagerImpl.OPTION_TYPE, OptionBox.YES_NO_OPTION);
        hints.put(WindowManagerImpl.PARENT_COMPONENT, view);

        String answer = PlatformAccess.getPlatform().getWindowManager().showInputDialog(
                BUNDLE.getString("view.modified.alert.title"), 
                MessageFormat.format(BUNDLE.getString("view.modified.alert.text"), view.getInfo().getViewName(), view.getManifestedComponent().getDisplayName()), 
                options, 
                options[0], 
                hints);
        
        if (save.equals(answer)) {
            Set<AbstractComponent> allModifiedObjects;
            if (comp.isDirty()) {
                // Create a new set including the object if it's dirty
                allModifiedObjects = new HashSet<AbstractComponent>();
                allModifiedObjects.addAll(modified);
                allModifiedObjects.add(comp);
            } else {
                // Just use the same set returned by the comp's ObjectManager capability
                allModifiedObjects = modified; 
            }
            PlatformAccess.getPlatform().getPersistenceProvider().persist(allModifiedObjects);
            
            // Notify the object manager so it can clear things out
            if (om != null) {
                om.notifySaved(modified);
            }
        }
        
        return true;
    }
    
    private boolean isComponentWriteableByUser(AbstractComponent component) {
        Platform p = PlatformAccess.getPlatform();
        PolicyContext policyContext = new PolicyContext();
        policyContext.setProperty(PolicyContext.PropertyName.TARGET_COMPONENT.getName(), component);
        policyContext.setProperty(PolicyContext.PropertyName.ACTION.getName(), 'w');
        String inspectionKey = PolicyInfo.CategoryType.OBJECT_INSPECTION_POLICY_CATEGORY.getKey();
        return p.getPolicyManager().execute(inspectionKey, policyContext).getStatus();
    }
    
}
