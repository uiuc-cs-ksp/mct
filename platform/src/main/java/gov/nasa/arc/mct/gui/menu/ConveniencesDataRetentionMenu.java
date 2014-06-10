package gov.nasa.arc.mct.gui.menu;

import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ContextAwareMenu;
import gov.nasa.arc.mct.gui.MenuItemInfo;
import gov.nasa.arc.mct.gui.MenuItemInfo.MenuItemType;

import java.util.Arrays;

/**
 * Export submenu under "Conveniences"
 */
@SuppressWarnings("serial")
public abstract class ConveniencesDataRetentionMenu extends ContextAwareMenu {    
    
    private static final String DATA_RETENTION_ITEMS_EXT = "/conveniences/dataretention.ext";
    
    public ConveniencesDataRetentionMenu(String extension) {
        super("Data Retention", new String[]{ extension });
    }
    
    @Override
    public boolean canHandle(ActionContext context) {
        return true;
    }

    /**
     * Data Retention submenu items
     */
    public static class ConveniencesDataRetentionItemsMenu extends ConveniencesDataRetentionMenu {    

        public ConveniencesDataRetentionItemsMenu() {
            super(DATA_RETENTION_ITEMS_EXT);
        }

        @Override
        protected void populate() {
            addMenuItemInfos(DATA_RETENTION_ITEMS_EXT, Arrays.asList(
                            new MenuItemInfo("DATA_RETENTION_ACTION", MenuItemType.RADIO_GROUP)
                        ));
        }
    }
}

