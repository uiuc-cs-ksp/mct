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

public interface FeedFilterProvider {

    public FeedFilterEditor createEditor();
        
    public FeedFilter createFilter(String definition) throws ParseException;
    
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
    
    public interface FeedFilterEditor {
        /**
         * Get the current filter definition as a plain string. This 
         * internal format of this string is unique to the filter.
         * @return
         */
        public String setFilterDefinition(String definition) throws ParseException;

        public String getFilterDefinition();        
        
        public <T> T getUI(Class<T> uiComponentClass, Runnable listener); 
    }
    
}
