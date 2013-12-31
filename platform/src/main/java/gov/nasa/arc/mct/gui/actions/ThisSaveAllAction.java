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

import gov.nasa.arc.mct.api.persistence.OptimisticLockException;
import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.ObjectManager;
import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.housing.MCTContentArea;
import gov.nasa.arc.mct.gui.housing.MCTHousing;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;
import gov.nasa.arc.mct.platform.spi.Platform;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.policy.PolicyContext;
import gov.nasa.arc.mct.policy.PolicyInfo;
import gov.nasa.arc.mct.services.internal.component.Updatable;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A Save action which includes any managed components of 
 * the component being saved (for instance, components modified 
 * in nested views.)
 */
public class ThisSaveAllAction extends ContextAwareAction{
    private static final long serialVersionUID = 3940626077815919451L;
    private static final ResourceBundle BUNDLE = 
            ResourceBundle.getBundle(
                    ThisSaveAllAction.class.getName().substring(0, 
                            ThisSaveAllAction.class.getName().lastIndexOf("."))+".Bundle");
    private ActionContextImpl actionContext;
        

    public ThisSaveAllAction() {
        super(BUNDLE.getString("SaveAllAction.label"));
    }
    
    @Override
    public boolean canHandle(ActionContext context) {
        actionContext = (ActionContextImpl) context;
        return getCenterPaneComponent() != null;
    }

    private boolean isComponentWriteableByUser(AbstractComponent component) {
        Platform p = PlatformAccess.getPlatform();
        PolicyContext policyContext = new PolicyContext();
        policyContext.setProperty(PolicyContext.PropertyName.TARGET_COMPONENT.getName(), component);
        policyContext.setProperty(PolicyContext.PropertyName.ACTION.getName(), 'w');
        String inspectionKey = PolicyInfo.CategoryType.OBJECT_INSPECTION_POLICY_CATEGORY.getKey();
        return p.getPolicyManager().execute(inspectionKey, policyContext).getStatus();
    }
    
    private AbstractComponent getCenterPaneComponent() {
        MCTHousing housing = actionContext.getTargetHousing();
        MCTContentArea contentArea = housing.getContentArea();
        return contentArea == null ? null : contentArea.getHousedViewManifestation().getManifestedComponent();
    }
    
    @Override
    public boolean isEnabled() {
        AbstractComponent ac = getCenterPaneComponent();
        ObjectManager om = ac.getCapability(ObjectManager.class);
        Set<AbstractComponent> modified = om != null ?                
                om.getAllModifiedObjects() : 
                Collections.<AbstractComponent>emptySet();
        
        // Should enable if at least one object can be saved
        boolean hasWriteableComponents = 
                !ac.isStale() && 
                ac.isDirty() &&
                isComponentWriteableByUser(ac);
        if (!hasWriteableComponents) {
            for (AbstractComponent mod : modified) {
                if (isComponentWriteableByUser(mod)) {
                    hasWriteableComponents = true;
                    break;
                }
            }
        }
                
        return hasWriteableComponents;
    }

    /**
     * This method is invoked when the client side object is stale. This can occur when another client 
     * even another window in the same application instance has saved the component after it has been loaded. 
     * This implementation will try again, which will overwrite the previous change; however, this is where 
     * configuration could be added to display a message instead. 
     */
    private void handleStaleObject(AbstractComponent ac) {
        overwritePreviousChanges(ac);
    }
    
    private void overwritePreviousChanges(AbstractComponent ac) {
        AbstractComponent updatedComp = PlatformAccess.getPlatform().getPersistenceProvider().getComponentFromStore(ac.getComponentId());
        ac.getCapability(Updatable.class).setVersion(updatedComp.getVersion());
        actionPerformed(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractComponent ac = getCenterPaneComponent();
        ObjectManager om = ac.getCapability(ObjectManager.class);
        Collection<AbstractComponent> modified = om != null ?
                om.getAllModifiedObjects() :
                Collections.<AbstractComponent>emptySet();

        // Assemble objects to save.
        // Make sure they pass the writeable-by-user test.
        Set<AbstractComponent> allModifiedObjects = new HashSet<AbstractComponent>();
        for (AbstractComponent mod : modified) {
            if (isComponentWriteableByUser(mod)) {
                allModifiedObjects.add(mod);
            }
        }
        if (ac.isDirty() && !ac.isStale() && isComponentWriteableByUser(ac)) {
            allModifiedObjects.add(ac);
        }

        try {
            PlatformAccess.getPlatform().getPersistenceProvider().persist(allModifiedObjects);
        } catch (OptimisticLockException ole) {
            handleStaleObject(ac);
        }
        
        if (om != null) {
            om.notifySaved(allModifiedObjects);
        }
    }

}
