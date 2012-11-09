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
package gov.nasa.arc.mct.fastplot.view;

import gov.nasa.arc.mct.fastplot.settings.PlotSettings;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge between the PlostSettingsControlPanel which provides the GUI widgets and callbacks and
 * MCT. 
 * 
 */
public class PlotSettingController extends PlotSettings {
	
	
	private static Logger logger = LoggerFactory.getLogger(PlotSettingController.class);
    
	    // Panel controller is controlling
		private PlotSettingsControlPanel panel;
	        
	    /**
	     * Construct controller defining the panel to connect to. 
	     * @param panel the panel to control.
	     * @throws IllegalArgumentExcpetion if panel is null.
	     */
	    PlotSettingController(PlotSettingsControlPanel inputPanel) {
	        if (inputPanel == null) {
	            throw new IllegalArgumentException();
	        }
	        panel = inputPanel;
	    }

		public void setTimeMinMaxValues(GregorianCalendar timeMin,
				GregorianCalendar timeMax) {
			this.setMinTime(timeMin.getTimeInMillis());
			this.setMaxTime(timeMax.getTimeInMillis());
		}
		
		public void setNonTimeMinMaxValues(double nonTimeMin, double nonTimeMax) {
			this.setMinNonTime(nonTimeMin);
			this.setMaxNonTime(nonTimeMax);
		}
		
	    /**
	     * Run tests to check that the plot settings panel has feed a valid state for a plot to be created.
	     * @return null if state is valid. False otherwise. 
	     */
	    private String getInvalidMessage() {	    	
	    	if (getMinTime() > getMaxTime()) {
	    		return "PlotSettingsPanel passed a nonTimeMin  (" + getMinTime() + 
	    				      ") >=  nonTimeMax (" + getMaxTime() + ") to the PlotSettingsController. Panel needs to validate this.";
	     	}
	    		    	
	    	if (getTimePadding() > 1.0 || getTimePadding() < 0.0) {
	    		return "PlotSettingsPanel of "+ getTimePadding() + " passed a timePadding outside the range 0.0 .. 1.0 to PlotSettingsController. Panel needs to validate this.";
	    	}
	    	
	    	if (getNonTimeMaxPadding() > 1.0 || getNonTimeMaxPadding() < 0.0) {
	    		return "PlotSettingsPanel of "+ getNonTimeMaxPadding() + " passed a nonTimeMinPadding outside the range 0.0 .. 1.0 to PlotSettingsController. Panel needs to validate this.";
	    	}
	    	
	    	if (getNonTimeMinPadding() + getNonTimeMaxPadding() >= 1.0) {
	    		return "The minimum and maximum non-Time axis padding must total less than 1";
	    	}
	    	
	    	if (getNonTimeMaxPadding() > 1.0 || getNonTimeMaxPadding() < 0.0) {
	    		return "PlotSettingsPanel of "+ getNonTimeMaxPadding() + " passed a nonTimeMaxPadding outside the range 0.0 .. 1.0 to PlotSettingsController. Panel needs to validate this.";
	    	}	    	
	    	
	    	if (getMaxNonTime() < getMinNonTime()) {
	    		return "Maximum non-time is less than minimum non-time.";
	    	}
	    	
	    	if (getMaxTime() < getMinTime()) {
	    		return "Maximum time is less than minimum time.";
	    	}
	    	
	    	if (this.getAxisOrientationSetting() == null) {
	    		return "PlotSettingsPanel passed a null timeAxisSetting to the PlotSettingsController. Panel needs to validate this.";
	      	}
	    	
	    	if (this.getXAxisMaximumLocation() == null) {
	    		return "PlotSettingsPanel passed a null xAxisMaximumLocation to the PlotSettingsController. Panel needs to validate this.";
	    	}
	    	
	    	if (this.getTimeAxisSubsequentSetting() == null) {
	    		return "PlotSettingsPanel passed a null timeAxisSubsequentSetting to the PlotSettingsController. Panel needs to validate this.";
	    	}
	    	
	    	if (this.getNonTimeAxisSubsequentMinSetting() == null) {
	    		return "PlotSettingsPanel passed a null nonTimeAxisSubsequentMinSetting to the PlotSettingsController. Panel needs to validate this.";
	    	}
	    	
	    	if (this.getNonTimeAxisSubsequentMaxSetting() == null) {
	    		return "PlotSettingsPanel passed a null nonTimeAxisSubsequentMaxSetting to the PlotSettingsController. Panel needs to validate this.";
	    		
	    	}
	    	
	    	return null;
	    }
	    
	    /*
	     * Call when user presses create chart button
	     */
	    public void createPlot() {
	    	// Only create a new plot if the state passed from plot settings panel is valid.
	    	String badStateMessage = getInvalidMessage();

	    	// Cause a hard assertion failure when running in development environment. 
	    	assert (badStateMessage == null) : "Plot setting panel passed a bad state to the plot " + badStateMessage; 

	    	// Display an error message in production environment. 
	    	if (badStateMessage != null) {
	    		logger.error(badStateMessage);
	    	} else {
	    		// The state is good so that we can create the plot. 		
	    		panel.getPlot().setupPlot(this);
	    	}
	    }

}
