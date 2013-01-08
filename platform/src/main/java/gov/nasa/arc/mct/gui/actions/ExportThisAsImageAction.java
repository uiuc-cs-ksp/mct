package gov.nasa.arc.mct.gui.actions;

import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ExportAsImageAction;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dcberrio
 * Class to export MCT Views as Images.
 */
@SuppressWarnings("serial")
public class ExportThisAsImageAction  extends ExportAsImageAction {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("gov/nasa/arc/mct/gui/actions/Bundle"); 
    private static final String TEXT = bundle.getString("ExportAsImageCommand");
    private ActionContextImpl actionContext = null;
    private static final Logger logger = LoggerFactory.getLogger(ExportThisAsImageAction.class);
    
    /**
     * Constructor.
     */
    public ExportThisAsImageAction() {
        super(TEXT);
    }

    
    @Override
    public boolean canHandle(ActionContext context) {
        if (context == null) throw new IllegalStateException();
        actionContext = (ActionContextImpl) context;
        if (actionContext.getTargetHousing() == null) {
            return false;
        }
        jComponentToExport = actionContext.getWindowManifestation(); 
        return (jComponentToExport != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileNameExtensionFilter extFilter = new FileNameExtensionFilter(bundle.getString("ExportViewFormat"), bundle.getString("ExportViewFormat"));
        getFileChooser().setFileFilter(extFilter);
        getFileChooser().addChoosableFileFilter(extFilter);
        getFileChooser().setDialogTitle(bundle.getString("ExportThisMessage"));
        int returnVal = getFileChooser().showSaveDialog(jComponentToExport);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!getFileChooser().getSelectedFile().getName().endsWith("."+bundle.getString("ExportViewFormat"))) {
                if (!(new File(getFileChooser().getSelectedFile().getAbsolutePath()  + "."+bundle.getString("ExportViewFormat")).exists())) {
                    getFileChooser().setSelectedFile(new File(getFileChooser().getSelectedFile().getAbsolutePath()  + "."+bundle.getString("ExportViewFormat")));
                }
            }
            BufferedImage bi = new BufferedImage(jComponentToExport.getWidth(), jComponentToExport.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            jComponentToExport.paint(g);
            g.dispose();
            try{
                ImageIO.write(bi,bundle.getString("ExportViewFormat"),new FileOutputStream(getFileChooser().getSelectedFile()));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                logger.error(ex.getMessage());
            }
        }
    }

    
}
