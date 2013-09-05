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
package gov.nasa.arc.mct.defaults.view;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.ViewProvider;
import gov.nasa.arc.mct.services.component.ViewInfo;
import gov.nasa.arc.mct.services.component.ViewType;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A view containing a drop-down of available view types 
 * for the represented component. Used by Inspector views 
 * for view switching.
 */
public class SwitcherView extends View {
    private static final long serialVersionUID = -7338842560419381410L;
    
    private ViewProvider managedView = null; // Where to send "switch" events
    @SuppressWarnings("rawtypes")
    private JComboBox comboBox;
    private JLabel    label; 
    private static final float FONT_SIZE = 10f; 
    
    /**
     * The view info used to instantiate this view.
     */
    public static final ViewInfo VIEW_INFO = 
            new ViewInfo(SwitcherView.class, "Switcher", ViewType.VIEW_SWITCHER);
    

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public SwitcherView(AbstractComponent ac, ViewInfo vi) {
        Set<ViewInfo> viewInfoSet = ac.getViewInfos(ViewType.OBJECT);
        ViewInfo[] viewInfos = viewInfoSet.toArray(new ViewInfo[viewInfoSet.size()]);

        if (viewInfos.length > 1) {
            // Only show combo box if there are multiple views
            comboBox = new JComboBox(viewInfos);
            comboBox.setRenderer(viewInfoRenderer);
            comboBox.addItemListener(itemListener);
            comboBox.setVisible(false);
            comboBox.setEnabled(false);
            comboBox.setFont(comboBox.getFont().deriveFont(FONT_SIZE));
            add(comboBox);
        } else if (viewInfos.length == 1) {
            // Otherwise, just show the one available view as a label
            label = new JLabel();
            label.setIcon(vi.getIcon());
            label.setText(vi.getViewName());
            setOpaque(false);
            add(label);
        } else {
            // No views to show
        }
    }
    
    @Override
    public <T> void addMonitoredGUI(T gui) {
        if (gui instanceof ViewProvider) {
            managedView = (ViewProvider) gui;
        }
        if (managedView != null) {
            resetSelection();
            if (comboBox != null) {
                comboBox.setVisible(true);
                comboBox.setEnabled(true);
            }
        }
        super.addMonitoredGUI(gui);
    }

    @Override
    public void setForeground(Color fg) {
        if (label != null) {
            label.setForeground(fg);
        }
        super.setForeground(fg);
    }
    
    private void resetSelection() {
        if (managedView != null && comboBox != null) {
            View housedView = managedView.getHousedViewManifestation();
            if (housedView != null) {
                ViewInfo vi = housedView.getInfo();
                if (comboBox != null) {
                    comboBox.removeItemListener(itemListener); // Avoid triggering listener
                    comboBox.setSelectedItem(vi);
                    comboBox.addItemListener(itemListener);
                }
                if (label != null) {
                    label.setIcon(vi.getIcon());
                    label.setText(vi.getViewName());
                }
            }
        }
    }
    
    private final ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (managedView != null) {       
                Object item = e.getItem();
                if (item != null && item instanceof ViewInfo) {
                    // Try to change the view
                    if (!managedView.setHousedViewManifestation((ViewInfo) item)) {
                        // View says it didn't change - restore previous selection
                        resetSelection();
                    }
                }
            }
        }        
    };

    // Used for providing components to represent ViewInfos in the drop-down
    @SuppressWarnings("rawtypes")
    private static final ListCellRenderer viewInfoRenderer = new ListCellRenderer() {
        private JLabel label = new JLabel(); // Reuse, since it's only used for rendering
                
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            label.setFont(label.getFont().deriveFont(FONT_SIZE));
            if (value instanceof ViewInfo) {
                ViewInfo vi = (ViewInfo) value;
                label.setIcon(vi.getIcon());
                label.setText(vi.getViewName());
            } else {
                label.setIcon(null);
                label.setText("Error");                
            }
            return label;
        }
        
    };
}
