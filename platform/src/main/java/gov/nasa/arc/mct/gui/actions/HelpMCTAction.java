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
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.OptionBox;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class HelpMCTAction extends ContextAwareAction {
    
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("AboutResource");
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpMCTAction.class);
    
    public static final String TEXT = "MCT User Documents";
    private boolean browserReturned = false;
    private URI uri = null; 
    
    public HelpMCTAction() {
        super(TEXT);
        try {
            uri = new URL(BUNDLE.getString("UserDocumentURL")).toURI();
        } catch (Exception e) {
            LOGGER.warn("Unable to interpret URL to user documents", e);
            uri = null;
        }
    }

    @Override
    public boolean isEnabled() {
        return uri != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (Desktop.isDesktopSupported()) {
           try {
        
                if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(uri);
                        
                } else {
                        OptionBox.showMessageDialog(null, "A Browser is not supported for this Desktop", 
                                "Error opening HTML File!", OptionBox.ERROR_MESSAGE);
                        return;
                } 
                
            } catch (Exception exp) {
                
                OptionBox.showMessageDialog(null, "<HTML>MCT Help files do not exist for Generic Platform. <BR>Unable to open MCT Help file.  <BR>" +
                        "Error as follows: " + exp.toString(), "Error opening Help File!",
                        OptionBox.ERROR_MESSAGE);
            }
            
        } else {
            OptionBox.showMessageDialog(null, "Cannot find a supported Desktop - aborting...", 
                    "Error opening user documents!", OptionBox.ERROR_MESSAGE);
            return;

        }
        browserReturned = true;
    }

    @Override
    public boolean canHandle(ActionContext context) {
        return isEnabled();
    }

    public boolean getBrowserReturned() {
        return browserReturned;
    }
    
}
