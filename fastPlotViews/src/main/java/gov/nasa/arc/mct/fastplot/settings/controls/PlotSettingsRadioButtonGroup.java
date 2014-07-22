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
package gov.nasa.arc.mct.fastplot.settings.controls;

import gov.nasa.arc.mct.fastplot.settings.PlotSettingsSubPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public abstract class PlotSettingsRadioButtonGroup<T> extends PlotSettingsSubPanel {
	private static final long serialVersionUID = -3064217413423344296L;

	private Map<T, JRadioButton> buttons = new HashMap<T, JRadioButton>();
	
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private JRadioButton selection;
	
	public PlotSettingsRadioButtonGroup(T... options) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
				
		for (T option : options) {				
			JRadioButton button = new JRadioButton(option.toString());
			buttonGroup.add(button);			
			button.addActionListener(this); // Hook into apply/reset callbacks
			add(button, gbc);		
			gbc.gridy++;
			
			buttons.put(option, button);
		}
		
		if (options.length > 0) {
			setSelection(options[0]);
		}
			
	}
	
	public JRadioButton getButton(T object) {
		return buttons.get(object);
	}
	
	/**
	 * Set the selection for this group. Note that this will be considered 
	 * the "clean" selection - user changes will flag this as dirty.
	 * @param object
	 */
	public void setSelection(T object) {
		JRadioButton selection = buttons.get(object);
		if (selection != null) {
			buttonGroup.setSelected(selection.getModel(), true);
			this.selection = selection;
		}
	}
	
	public T getSelection() {
		for (Entry<T, JRadioButton> button : buttons.entrySet()) {
			if (button.getValue().isSelected()) {
				return button.getKey();
			}
		}
		return null;
	}
	
	public void setText(T object, String text) {
		JRadioButton button = buttons.get(object);
		if (button != null) {
			button.setText(text);
		}
	}

	@Override
	public boolean isDirty() {
		if (selection == null) return false;
		return !buttonGroup.isSelected(selection.getModel());
	}

	@Override
	public boolean isValidated() {
		return true;
	}
}
