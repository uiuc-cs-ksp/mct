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
package gov.nasa.arc.mct.importExport.provider;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Implements a map of key, value pairs representing the
 * metadata attributes of a component.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportExportData {
    /**
     * Constant for getting the meta data.
     */
    public final static String META_DATA_PROPERTY_KEY = "COMPONENT_META_DATA";

    private final Map<String,String> attributes = new HashMap<String,String>();

    /**
     * Adds a metadata attribute for a component.
     * 
     * @param key the metadata key
     * @param value the metadata value
     * @return the object previously associated with this key, else null
     */

    public String addMetaData(String key, String value) {
        return attributes.put(key, value);
    }

    /**
     * Gets the metadata attribute with a given key.
     * 
     * @param key the metadata key
     * @return the metadata value for the key
     */
    public String getMetaData(String key) {
        return attributes.get(key);
    }
    
    /**
     * Gets the metadata element.
     * @return metadata
     */
    public Map<String,String> getAll() {
        return attributes;
    }
}
