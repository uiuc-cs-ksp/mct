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
package gov.nasa.arc.mct.gui.actions;

import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.DataRetentionUtil;
import gov.nasa.arc.mct.gui.GroupAction;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class DataRetentionAction extends GroupAction {
    
    private static final Logger logger = LoggerFactory.getLogger(DataRetentionAction.class);
    
    private static String dataRetentionOption;
    
    private EvictByApplicationReceivedAction appReceivedAction;
    private EvictByTimestampAction timestampAction;

    public DataRetentionAction() {
        super("Data Retention");
        
        if(dataRetentionOption == null || dataRetentionOption.isEmpty()) {
            String defaultRetentionValue = DataRetentionUtil.getDataRetentionConfiguration();
            dataRetentionOption = (defaultRetentionValue == null || defaultRetentionValue.isEmpty()) 
                    ? DataRetentionUtil.DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE
                    : defaultRetentionValue;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Do nothing
    }

    @Override
    public boolean canHandle(ActionContext context) {
        List<GroupAction.RadioAction> actions = new ArrayList<RadioAction>();
        appReceivedAction = new EvictByApplicationReceivedAction(
                dataRetentionOption.equals(DataRetentionUtil.DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE));
        timestampAction = new EvictByTimestampAction(dataRetentionOption.equals(DataRetentionUtil.DATA_RETENTION_TIMESTAMP_VALUE));
        
        actions.add(appReceivedAction);
        actions.add(timestampAction);
        
        // Setting radio button actions
        setActions(actions.toArray(new RadioAction[2]));
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    
    public static String getDataRetentionConfiguration() {
        String evictionProcedure = "";
        try {
            Properties p = new Properties();
            p.load(ClassLoader.getSystemResourceAsStream("properties/feed.properties"));
            evictionProcedure = p.getProperty(DataRetentionUtil.DATA_RETENTION_KEY);
        } catch (Exception e) {
            logger.error("Could not load Data Rentention preference.", e);
            // if not found, just ignore any exceptions - it's not critical...
        }
        return evictionProcedure;
    }
    
    private final class EvictByApplicationReceivedAction extends GroupAction.RadioAction {
        
        private boolean isSelected = false;
        
        @SuppressWarnings("unused")
        public EvictByApplicationReceivedAction() {
            this(false);
        }
        
        public EvictByApplicationReceivedAction(boolean isSelected) {
            this.isSelected = isSelected;
            putValue(Action.NAME, "Evict by Application Received Time");
        }
        
        @Override
        public boolean isMixed() {
            return false;
        }
        
        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dataRetentionOption = DataRetentionUtil.DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE;
            DataRetentionUtil.getInstance().setApplicationTimeOption(true);
        }
    }
    
    private final class EvictByTimestampAction extends GroupAction.RadioAction {
        
        private boolean isSelected = false;

        @SuppressWarnings("unused")
        public EvictByTimestampAction() {
            this(false);
        }
        
        public EvictByTimestampAction(boolean isSelected) {
            this.isSelected = isSelected;
            putValue(Action.NAME, "Evict by Timestamp");
        }
        
        @Override
        public boolean isMixed() {
            return false;
        }
        
        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dataRetentionOption = DataRetentionUtil.DATA_RETENTION_TIMESTAMP_VALUE;
            DataRetentionUtil.getInstance().setApplicationTimeOption(false);
        }
    }

    
}
