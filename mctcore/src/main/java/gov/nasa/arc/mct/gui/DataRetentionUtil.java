package gov.nasa.arc.mct.gui;
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

import java.util.Properties;

/**
 * Data Retention Utility to check the default Data Retention option.
 *
 */
public class DataRetentionUtil {
    
    /**
     * The Data Retention Properties file key.
     */
    public static final String DATA_RETENTION_KEY = "data.retention.eviction";
    /**
     * The value for the Data Retention option of evicting data by Application Received Time.
     */
    public static final String DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE = "appReceiveTime";
    /**
     * The value for the Data Retention option of evicting data by data timestamp.
     */
    public static final String DATA_RETENTION_TIMESTAMP_VALUE = "timestamp";
    private static final DataRetentionUtil instance = new DataRetentionUtil();
    
    private boolean isApplicationTimeOption;
    
    /**
     * The singleton constructor.
     */
    private DataRetentionUtil() {
        String defaultRetentionValue = getDataRetentionConfiguration();
        isApplicationTimeOption = (defaultRetentionValue == null || defaultRetentionValue.isEmpty()) 
                        ? true : defaultRetentionValue.equals(DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE);
    }
    
    /**
     * Gets the singleton instance of this class.
     * @return Instance of this class
     */
    public static final synchronized DataRetentionUtil getInstance(){
         return instance;
    }
    
    /**
     * Checks to see if the current Data Retention option is by Application Received Time.
     * @return Returns true if current option is by Application Received Time
     */
    public boolean isApplicationTimeOption() {
         return isApplicationTimeOption;
    }

    /**
     * Setter method to indicate Data Retention option is by Application Received Time.
     * @param isEnabled Boolean value to set the data Retention option to application received time.
     */
    public void setApplicationTimeOption(boolean isEnabled) {
         this.isApplicationTimeOption = isEnabled;
    }
    
    /**
     * Retrieves the default Data Retention option from the properties file.
     * @return The default Data Retention option of either appReceiveTime/timestamp
     */
    public static String getDataRetentionConfiguration() {
        String evictionProcedure = "";
        try {
            Properties p = new Properties();
            p.load(ClassLoader.getSystemResourceAsStream("properties/feed.properties"));
            evictionProcedure = p.getProperty(DATA_RETENTION_KEY);
        } catch (Exception e) {
            // if not found, just ignore any exceptions - it's not critical...
        }
        return evictionProcedure;
    }
    
}
