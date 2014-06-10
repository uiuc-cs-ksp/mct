package gov.nasa.arc.mct.gui;


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataRetentionUtil {
    
    public static final String DATA_RETENTION_KEY = "data.retention.eviction";
    public static final String DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE = "appReceiveTime";
    public static final String DATA_RETENTION_TIMESTAMP_VALUE = "timestamp";
    private static final Logger logger = LoggerFactory.getLogger(DataRetentionUtil.class);
    private static final DataRetentionUtil instance = new DataRetentionUtil();
    
    private boolean isApplicationTimeOption;
    
    private DataRetentionUtil(){
        String defaultRetentionValue = getDataRetentionConfiguration();
        isApplicationTimeOption = (defaultRetentionValue == null || defaultRetentionValue.isEmpty()) 
                        ? true : defaultRetentionValue.equals(DATA_RETENTION_APPLICATION_RECEIVE_TIME_PROPERTY_VALUE);
    }
    
    public static final synchronized DataRetentionUtil getInstance(){
         return instance;
    }
    
    public boolean isApplicationTimeOption() {
         return isApplicationTimeOption;
    }

    public void setApplicationTimeOption(boolean isEnabled) {
         this.isApplicationTimeOption = isEnabled;
   }
    
    public static String getDataRetentionConfiguration() {
        String evictionProcedure = "";
        try {
            Properties p = new Properties();
            p.load(ClassLoader.getSystemResourceAsStream("properties/feed.properties"));
            evictionProcedure = p.getProperty(DATA_RETENTION_KEY);
        } catch (Exception e) {
            logger.error("Could not load Data Rentention preference.", e);
            // if not found, just ignore any exceptions - it's not critical...
        }
        return evictionProcedure;
    }
    
}
