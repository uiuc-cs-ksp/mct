package gov.nasa.arc.mct.identitymgr.impl;

import gov.nasa.arc.mct.platform.spi.PersistenceProvider;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.services.internal.component.User;

import java.util.Properties;

import javax.swing.JOptionPane;

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
        
        // If PersistenceProvider is available, we can create a dialog with available users
        PersistenceProvider persistence = PlatformAccess.getPlatform().getPersistenceProvider();
        
        if (username == null && persistence != null) {
            Object[] users = persistence.getAllUsers().toArray();
            // TODO: Abstract out this reference to Swing
            username = users.length < 1 ?
                    "testUser1" :
                    (String) JOptionPane.showInputDialog(null, "Log in as user...", "Log in as user...", JOptionPane.QUESTION_MESSAGE, null, users, users[0]);
        }
        
        this.currentUser = username;
        
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
