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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

public abstract class PlotSettingsComboBox<T> extends PlotSettingsSubPanel {
	private static final long serialVersionUID = -5666070801819602174L;

	private T selection;
	
	private Map<T, ItemWrapper<T>> items = new HashMap<T, ItemWrapper<T>>();
	private JComboBox comboBox;
	
	public PlotSettingsComboBox(T... choices) {
		List<ItemWrapper<T>> wrappedChoices = new ArrayList<ItemWrapper<T>>();
		for (T item : choices) {
			ItemWrapper<T> wrapper = new ItemWrapper<T>(item);
			wrappedChoices.add(wrapper);
			items.put(item, wrapper);
		}
		comboBox = new JComboBox(wrappedChoices.toArray());
		if (choices.length > 0) setSelection(choices[0]);
		add(comboBox);
		comboBox.addActionListener(this);
	}

	/**
	 * Set the selection for this group. Note that this will be considered 
	 * the "clean" selection - user changes will flag this as dirty.
	 * @param object
	 */
	public void setSelection(T object) {
		this.selection = object;
		ItemWrapper<T> w = items.get(object);
		if (w == null) return;
		
		// Note: setSelectedItem triggers ActionListeners, but we do not want to 
		//       trigger Apply/Reset checks with this. So, temporarily stop listening.
		comboBox.removeActionListener(this);
		comboBox.setSelectedItem(w);
		comboBox.addActionListener(this);
	}
	
	@SuppressWarnings("unchecked")
	public T getSelection() {
		ItemWrapper<T> wrapper = ((ItemWrapper<T>) comboBox.getSelectedItem());
		return wrapper == null ? null : wrapper.item;
	}

	@Override
	public boolean isDirty() {
		return selection != null && !selection.equals(getSelection());
	}

	@Override
	public boolean isValidated() {
		return true;
	}

	public void setText(T object, String text) {
		ItemWrapper<T> item = items.get(object);
		if (item != null) {
			item.text = text;
		}
	}
	
	private static class ItemWrapper<T> {
		private T item;
		private String text;
		
		public ItemWrapper(T item) {
			this.item = item;
			this.text = item.toString();
		}
		
		public String toString() {
			return text;
		}
	}
}
