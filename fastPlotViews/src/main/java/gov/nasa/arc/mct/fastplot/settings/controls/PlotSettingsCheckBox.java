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

import gov.nasa.arc.mct.fastplot.settings.PlotConfiguration;
import gov.nasa.arc.mct.fastplot.settings.PlotSettingsSubPanel;

import javax.swing.JCheckBox;

public abstract class PlotSettingsCheckBox extends PlotSettingsSubPanel {
	private static final long serialVersionUID = 5485293797086854968L;
	
	private JCheckBox checkbox = new JCheckBox();
	private boolean   initial  = false;
	
	public PlotSettingsCheckBox(String text) {
		checkbox.setText(text);
		add(checkbox);
		checkbox.addActionListener(this);
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.settings.PlotSettingsPopulator#reset(gov.nasa.arc.mct.fastplot.settings.PlotSettings)
	 */
	@Override
	public void reset(PlotConfiguration settings, boolean hard) {
		if (hard) checkbox.setSelected(initial = getFrom(settings));
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.settings.PlotSettingsSubPanel#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return initial != checkbox.isSelected();
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.settings.PlotSettingsSubPanel#isValidated()
	 */
	@Override
	public boolean isValidated() {
		return true;
	}

	public boolean isSelected() {
		return checkbox.isSelected();
	}
	
	public abstract boolean getFrom(PlotConfiguration settings);
}
