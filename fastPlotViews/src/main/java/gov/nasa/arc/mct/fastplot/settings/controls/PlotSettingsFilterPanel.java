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

import gov.nasa.arc.mct.components.FeedFilterProvider.FeedFilterEditor;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;
import gov.nasa.arc.mct.fastplot.settings.PlotConfiguration;
import gov.nasa.arc.mct.fastplot.settings.PlotSettingsSubPanel;

import java.text.ParseException;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlotSettingsFilterPanel extends PlotSettingsSubPanel {
	private static final long serialVersionUID = 5275936589696055080L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlotSettingsSubPanel.class);
	
	private FeedFilterEditor editor;
	private String initial;
	
	
	public PlotSettingsFilterPanel(FeedFilterEditor editor) {
		super();
		this.editor = editor;
		this.initial = editor.getFilterDefinition();
		JComponent ui = editor.getUI(JComponent.class, new Runnable() {
			@Override
			public void run() {
				fireCallbacks();
			}			
		});
		add(ui);
	}

	@Override
	public void populate(PlotConfiguration settings) {
		settings.setExtension(PlotConstants.FILTER_VALUE, editor.getFilterDefinition());
	}

	@Override
	public void reset(PlotConfiguration settings, boolean hard) {
		if (hard) {
			String def = settings.getExtension(PlotConstants.FILTER_VALUE, String.class);
			try {
				editor.setFilterDefinition(def);
			} catch (ParseException pe) {
				LOGGER.warn("Could not parse plot filter settings from persistence; using defaults.");			
			}		
		}
	}

	@Override
	public boolean isDirty() {
		String current = editor.getFilterDefinition();
		return !((current == null) ? (initial == null) : (current.equals(initial)));
	}

	@Override
	public boolean isValidated() {		
		return true;
	}

}
