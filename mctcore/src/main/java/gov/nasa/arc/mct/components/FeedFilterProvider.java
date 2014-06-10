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
package gov.nasa.arc.mct.components;

import java.text.ParseException;
import java.util.Map;

/**
 * Provides information necessary to support the filtering of feed data 
 * before display (to support filtering of plotted data.) Components 
 * can expose this capability in order to inject such filtering; views 
 * can probe for this capability in order to support such filtering.
 */
public interface FeedFilterProvider {

    /**
     * Create an editor for setting filter parameters, as appropriate 
     * for the object which exposed this capability.
     * @return an editor for filter parameters
     */
    public FeedFilterEditor createEditor();
        
    /**
     * Create a filter for feed data, according to the provided definition; 
     * typically some rules or parameters which the filter should enforce.
     * The format of the definition is the component's responsibility; 
     * the provided definition should be as produced by a corresponding 
     * editor.
     * @param definition the rules which the filter will enforce
     * @return a filter which enforces the specified rules 
     * @throws ParseException should be thrown when definition is unrecognized
     */
    public FeedFilter createFilter(String definition) throws ParseException;
    
    /**
     * A filter for accepting or removing data points from a feed (e.g. 
     * before they are displayed.)
     */
    public interface FeedFilter {
        /**
         * Filter a given data point. Return true if the data point passes 
         * the filter (e.g. it should be plotted); otherwise, false (e.g. 
         * it should not be displayed.)
         * @param datum the data point
         * @return true if the filter is passed; otherwise false
         */
        public boolean accept(Map<String, String> datum);
    }    
    
    /**
     * An editor for getting and setting filter definitions, used to support 
     * user specification of filter parameters.
     */
    public interface FeedFilterEditor {
        /**
         * Set the current filter definition as a plain string. This 
         * internal format of this string is unique to the filter; 
         * views will not be responsible for interpreting this definition, 
         * and will typically simply store it as part of their view 
         * properties.
         * @return a string defining the current filter state
         * @throws ParseException should be thrown when definition is unrecognized
         */
        public String setFilterDefinition(String definition) throws ParseException;

        /**
         * Get the current filter definition as a plain string. This 
         * internal format of this string is unique to the filter; 
         * views will not be responsible for interpreting this definition, 
         * and will typically simply store it as part of their view 
         * properties.
         * @return a string defining the current filter state
         */
        public String getFilterDefinition();        
        
        /**
         * Get a UI component to display to the user. The get and set methods 
         * for filter definition should match with what the user has entered. 
         * If this filter editor can not be expressed using the desired UI 
         * component, this method should return null.
         * @param uiComponentClass the class of UI component expected (typically JComponent)
         * @param listener a callback to be invoked when user changes have occurred
         * @return the user interface component for editing filter parameters
         */
        public <T> T getUI(Class<T> uiComponentClass, Runnable listener); 
    }
    
}
