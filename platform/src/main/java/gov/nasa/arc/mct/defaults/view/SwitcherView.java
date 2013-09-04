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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class SwitcherView extends View {
    private static final long serialVersionUID = -7338842560419381410L;
    
    private ViewProvider managedView = null; // Where to send "switch" events
    private JComboBox comboBox;
    
    public static final ViewInfo VIEW_INFO = 
            new ViewInfo(SwitcherView.class, "Switcher", ViewType.VIEW_SWITCHER);
    
    
    
    public SwitcherView(AbstractComponent ac, ViewInfo vi) {
        comboBox = new JComboBox(ac.getViewInfos(ViewType.OBJECT).toArray());
        comboBox.setRenderer(viewInfoRenderer);
        comboBox.addItemListener(itemListener);
        comboBox.setVisible(false);
        comboBox.setEnabled(false);
        add(comboBox);
    }
    
    @Override
    public <T> void addMonitoredGUI(T gui) {
        if (gui instanceof ViewProvider) {
            managedView = (ViewProvider) gui;
        }
        if (managedView != null) {
            resetSelection();
            comboBox.setVisible(true);
            comboBox.setEnabled(true);
        }
        super.addMonitoredGUI(gui);
    }

    private void resetSelection() {
        if (managedView != null && comboBox != null) {
            View housedView = managedView.getHousedViewManifestation();
            if (housedView != null) {
                comboBox.removeItemListener(itemListener); // Avoid triggering listener
                comboBox.setSelectedItem(housedView.getInfo());
                comboBox.addItemListener(itemListener);
            }
        }
    }
    
    private final ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (managedView != null) {
                Object source = e.getSource();            
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

    private static final ListCellRenderer viewInfoRenderer = new ListCellRenderer() {
        private JLabel label = new JLabel(); // Reuse, since it's only used for rendering
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            label.setIcon(null);
            label.setText("Error");
            if (value instanceof ViewInfo) {
                ViewInfo vi = (ViewInfo) value;
                label.setIcon(vi.getIcon());
                label.setText(vi.getViewName());
            }
            return label;
        }
        
    };
}
