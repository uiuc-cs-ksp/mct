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
package gov.nasa.arc.mct.gui.housing;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.policy.ExecutionResult;
import gov.nasa.arc.mct.policy.PolicyContext;
import gov.nasa.arc.mct.policy.PolicyInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MCTDragDropHandler {
    
    // Currently selected nodes, in their context
    // Keys: Containing views
    // Values: Selected views.
    private Collection<View> draggedViews;
    
    // The view onto which components were dropped
    private View dropView;
    
    // The index of the drop
    private int index;

    private String message = "";
    
    private List<AbstractComponent> droppedComponents = new ArrayList<AbstractComponent>();
    
    private final DragDropMode[] modes = {
            new DragDropLink()
    };
    
    /**
     * 
     * @param draggedViews
     * @param dropView
     * @param index index of the drop, or -1 if unspecified
     */
    public MCTDragDropHandler(Collection<View> draggedViews, View dropView, int index) {
        super();
        this.draggedViews = draggedViews;
        this.dropView = dropView;
        this.index = index;
        
        for (View v : draggedViews) {
            droppedComponents.add(v.getManifestedComponent());
        }        
    }
    
    /**
     * 
     * @return true if completed; otherwise false
     */
    public boolean perform() {
        boolean complete = false;
        
        List<DragDropMode> options = new ArrayList<DragDropMode>();
        
        for (DragDropMode mode : modes) {
            if (mode.canPerform()) {
                options.add(mode);
            }
        }
        
        if (options.size() > 0) {
            DragDropMode choice = PlatformAccess.getPlatform().getWindowManager().showInputDialog(
                    "", 
                    "", 
                    options.toArray(new DragDropMode[options.size()]), 
                    options.get(0), 
                    null);
            
            if (choice != null) {
                try {
                    PlatformAccess.getPlatform().getPersistenceProvider().startRelatedOperations();
                    choice.perform();    
                    complete = true; // Will not be set if there is an exception
                } finally {
                    PlatformAccess.getPlatform().getPersistenceProvider().completeRelatedOperations(complete);
                }
            }        
        }
        
        return complete;
    }
    
    public Collection<AbstractComponent> getDroppedComponents() {
        return droppedComponents;
    }
    
    public String getMessage() {
        return message;
    }
    
    private boolean consultPolicy(String policyKey) {
        return consultPolicy(policyKey, makePolicyContext(droppedComponents, dropView.getManifestedComponent()));
    }
    
    private boolean consultPolicy(String policyKey, PolicyContext context) {
        ExecutionResult result = PlatformAccess.getPlatform().getPolicyManager().execute(
                policyKey, 
                context);
        
        if (!result.getStatus()) {
            message = result.getMessage();
        }
        
        return result.getStatus();
    }
    
    private PolicyContext makePolicyContext(List<AbstractComponent> sourceComponents, AbstractComponent targetComponent) {
        PolicyContext context = new PolicyContext();
        context.setProperty(PolicyContext.PropertyName.TARGET_COMPONENT.getName(), targetComponent);
        context.setProperty(PolicyContext.PropertyName.SOURCE_COMPONENTS.getName(), sourceComponents);
        context.setProperty(PolicyContext.PropertyName.ACTION.getName(), Character.valueOf( 'w' ));
        return context;
    }
    
    private abstract class DragDropMode {
        public abstract String getName();
        public abstract boolean canPerform();
        public abstract void perform();
        
        @Override
        public String toString() {
            return getName();
        }
    }
    
    private class DragDropLink extends DragDropMode {
        @Override
        public String getName() {
            return "Link";
        }

        @Override
        public boolean canPerform() {
            return consultPolicy(PolicyInfo.CategoryType.COMPOSITION_POLICY_CATEGORY.getKey());
        }

        @Override
        public void perform() {
            dropView.getManifestedComponent().addDelegateComponents(index, droppedComponents);
            dropView.getManifestedComponent().save();
        }
        
    }
}
