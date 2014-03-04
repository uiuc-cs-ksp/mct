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
import gov.nasa.arc.mct.services.internal.component.ComponentInitializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    private final DragDropMode[] modes;
    
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
        Collections.reverse(droppedComponents);
        
        modes = new DragDropMode[] {
                new DragDropMove(),
                new DragDropLink(),
                new DragDropCopy()
        };
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
            } else {
                message = null;
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
    
    private boolean consultPolicy(PolicyInfo.CategoryType policyType) {
        return consultPolicy(policyType, makePolicyContext(droppedComponents, dropView.getManifestedComponent()));
    }
    
    private boolean consultPolicy(PolicyInfo.CategoryType policyType, PolicyContext context) {
        ExecutionResult result = PlatformAccess.getPlatform().getPolicyManager().execute(
                policyType.getKey(), 
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
            return consultPolicy(PolicyInfo.CategoryType.COMPOSITION_POLICY_CATEGORY);
        }

        @Override
        public void perform() {
            dropView.getManifestedComponent().addDelegateComponents(index, droppedComponents);
            dropView.getManifestedComponent().save();
        }        
    }
    
    private class DragDropMove extends DragDropLink {
        private Map<String, AbstractComponent> parents =
                new HashMap<String, AbstractComponent>();
        private Map<String, List<AbstractComponent>> toRemove = 
                new HashMap<String, List<AbstractComponent>>();
        private boolean valid = true; 
        
        public DragDropMove() {
            for (View v : draggedViews) {
                View parent = v.getParentView();
                if (parent != null) {
                    AbstractComponent parentComponent = parent.getManifestedComponent();
                    String id = parentComponent.getId();
                    if (!id.equals(dropView.getManifestedComponent().getComponentId())) {
                        parents.put(id, parentComponent);
                        if (!toRemove.containsKey(id)) {
                            toRemove.put(id, new ArrayList<AbstractComponent>());
                        }
                        toRemove.get(id).add(v.getManifestedComponent());
                    }
                } else {
                    valid = false;
                    break;
                }
            }
        }
        
        @Override
        public String getName() {
            return "Move";
        }

        @Override
        public boolean canPerform() {
            for (String id : parents.keySet()) {
                PolicyContext context = makePolicyContext(toRemove.get(id), parents.get(id));
                if (!consultPolicy(PolicyInfo.CategoryType.CAN_REMOVE_MANIFESTATION_CATEGORY, context)) {
                    return false;
                }
            }
            return valid && 
                   !toRemove.isEmpty() &&
                   super.canPerform();
        }

        @Override
        public void perform() {
            for (String id : parents.keySet()) {
                parents.get(id).removeDelegateComponents(toRemove.get(id));
                parents.get(id).save();
            }
            super.perform();
        }        
    }
    
    private class DragDropCopy extends DragDropLink {
        @Override
        public String getName() {
            return "Copy";
        }

        @Override
        public boolean canPerform() {
            for (AbstractComponent ac : droppedComponents) {
                if (ac.getExternalKey() != null) {
                    return false;
                }
                if (!PlatformAccess.getPlatform().getComponentRegistry().isCreatable(ac.getClass())) {
                    return false;
                }
            }
            return super.canPerform();
        }

        @Override
        public void perform() {
            droppedComponents = clone(droppedComponents);
            super.perform();
        }        
        
        private List<AbstractComponent> clone(List<AbstractComponent> droppedComponents) {
            List<AbstractComponent> clones = new ArrayList<AbstractComponent>();
            
            for (AbstractComponent component : droppedComponents) {
                clones.add(clone(component));
            }
            
            return clones;
        }
        
        private AbstractComponent clone(AbstractComponent component) {
            AbstractComponent duplicate = component.clone();
            ComponentInitializer ci = duplicate.getCapability(ComponentInitializer.class);
            ci.setCreator(PlatformAccess.getPlatform().getCurrentUser().getUserId());
            ci.setOwner(PlatformAccess.getPlatform().getCurrentUser().getUserId());
            duplicate.setDisplayName("copy of " + component.getDisplayName());
            duplicate.save();
            return duplicate;
        }
    }
}
