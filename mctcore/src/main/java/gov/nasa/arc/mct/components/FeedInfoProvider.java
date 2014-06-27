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

/**
 * Provides additional data about FeedProvider instances. 
 * Intended to be exposed as a capability via  
 * {@link gov.nasa.arc.mct.components.AbstractComponent#getCapability(Class)} 
 * to provide additional, optional information about 
 * FeedProvider instances exposed by that object's 
 * FeedProvider capabilities.   
 * 
 * This allows other features, such as plots, to distinguish 
 * relevant features among multiple such exposed FeedProvider 
 * objects, without requiring extension of the FeedProvider API.
 */
public interface FeedInfoProvider {
    
    /**
     * Get a description of the feed being provided. 
     * May return null if no information is known 
     * for this feed (for instance, if it is 
     * associated with a different component.)
     * @param fp the feed provider for which info is needed
     * @return information about the feed, or null
     */
    public FeedInfo getFeedInfo(FeedProvider fp);
    
    /**
     * Provides information about a data feed which can 
     * be used to distinguish multiple FeedProvider 
     * capabilities provided by the same component.
     */
    public interface FeedInfo {
        /**
         * A machine-readable unique identifier for 
         * this type of feed.
         * @return
         */
        public String getTypeId();
     
        /**
         * A human-readable name for this type of 
         * feed.
         * @return
         */
        public String getTypeName();
    }
}
