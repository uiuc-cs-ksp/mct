package gov.nasa.arc.mct.identitymgr.impl;

import gov.nasa.arc.mct.platform.spi.PersistenceProvider;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.platform.spi.WindowManager;
import gov.nasa.arc.mct.services.internal.component.User;

import java.util.Arrays;
import java.util.Properties;

/**
 * A minimal implementation of an IdentityManager suitable for demonstrations or 
 * evaluation environments. Prompts the user to enter their name if no user name 
 * (mct.user) has been specified.
 * 
 * @author vwoeltje
 *
 */
public class DefaultIdentityManager extends IdentityManager {

    /**
     * Create a default identity manager
     * @param properties input properties
     */
    public DefaultIdentityManager(Properties properties) {
        String username = properties.getProperty("mct.user");
        
        // If PersistenceProvider & WindowManager are available, we can create a dialog with available users
        PersistenceProvider persistence = PlatformAccess.getPlatform().getPersistenceProvider();
        WindowManager windowing = PlatformAccess.getPlatform().getWindowManager();
        
        if (username == null && persistence != null && windowing != null) {
            Object[] users = persistence.getAllUsers().toArray();
            // TODO: Abstract out this reference to Swing
            if (users.length > 0) {
                Arrays.sort(users);
                username = windowing.showInputDialog("Mission Control Technologies", "Log in as user...", users, users[0]).toString();
            }
        }        
        
        this.currentUser = username;
        
        // If a user was selected, initialize with group info as well from Persistence
        if (persistence != null && username != null) {
            User user = persistence.getUser(username);
            if (user != null) {        
                this.currentGroup = user.getDisciplineId();
            }
        }
    }
    
    @Override
    public boolean isMonitorRunning() {
        // There is no need for a monitor here.
        return false;
    }

}
