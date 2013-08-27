package gov.nasa.arc.mct.gui.actions;

import gov.nasa.arc.mct.defaults.view.MCTHousingViewManifestation;
import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.OptionBox;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.housing.MCTContentArea;
import gov.nasa.arc.mct.gui.impl.WindowManagerImpl;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.services.component.ViewInfo;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * The "Refresh Now" menu option. Causes the view in the 
 * center pane to be re-created with the latest version 
 * of the object in the housing.
 * 
 * @author vwoeltje
 *
 */
public class RefreshAction extends ContextAwareAction {
    private static final long serialVersionUID = -224000420281170561L;

    private MCTHousingViewManifestation housing;
    
    /**
     * Create the refresh action.
     */
    public RefreshAction() {
        super("Refresh Now");
    }
    
    @Override
    public boolean canHandle(ActionContext context) {
        // Store reference to housing for "actionPerformed"
        this.housing = (MCTHousingViewManifestation) context.getWindowManifestation();
        
        // Only valid if we have a center pane
        return housing != null && housing.getContentArea() != null;
    }

    @Override
    public boolean isEnabled() {
        return true;
        // Ideally, we want something like:
        // return housing.getContentArea().getHousedViewManifestation().getManifestedComponent().isStale();
        // But staleness in this view is not detected for some reason
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MCTContentArea contentArea = housing.getContentArea();
        
        // Should not be null per canHandle, but check for safety
        if (contentArea != null) {
            View housedView = contentArea.getHousedViewManifestation();
            boolean doRefresh = true;
            
            // Give the user an opportunity to cancel the refresh if it would
            // overwrite unsaved changes.
            if (housedView.getManifestedComponent().isDirty()) {
                Map<String, Object> hints = new HashMap<String, Object>();
                hints.put(WindowManagerImpl.MESSAGE_TYPE, OptionBox.WARNING_MESSAGE);
                hints.put(WindowManagerImpl.OPTION_TYPE, OptionBox.YES_NO_OPTION);
                hints.put(WindowManagerImpl.PARENT_COMPONENT, housing);

                String input = PlatformAccess.getPlatform().getWindowManager().showInputDialog(
                        "Refresh view", 
                        "<html>This view contains unsaved changes.<br/>Refreshing will cause these to be lost.</html>", 
                        new String[]{"Refresh", "Cancel"}, 
                        "Refresh", 
                        hints);
                doRefresh = input.equals("Refresh");
            }

            // Perform the refresh by re-creating view
            if (doRefresh) {
                ViewInfo vi = contentArea.getHousedViewManifestation().getInfo();
                View newView = vi.createView(housing.getManifestedComponent());
                contentArea.setOwnerComponentCanvasManifestation(newView);
            }
        }
    }

}
