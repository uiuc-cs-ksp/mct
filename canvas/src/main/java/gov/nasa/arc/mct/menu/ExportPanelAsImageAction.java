package gov.nasa.arc.mct.menu;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nasa.arc.mct.canvas.panel.Panel;
import gov.nasa.arc.mct.canvas.view.CanvasManifestation;
import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ExportAsImageAction;
import gov.nasa.arc.mct.gui.View;

public class ExportPanelAsImageAction extends ExportAsImageAction {

    /**
     * 
     */
    private static final long serialVersionUID = 5215272311432363269L;
    private static final ResourceBundle BUNDLE = 
                    ResourceBundle.getBundle("CanvasResourceBundle");
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPanelAsImageAction.class);

    private Collection<Container> selectedPanels = new HashSet<Container>();
    private Collection<View> selectedManifestations;
    
    public ExportPanelAsImageAction() {
        super(BUNDLE.getString("ExportPanelMessage"));
    }

    @Override
    public boolean canHandle(ActionContext context) {
//        selectedManifestations = getCanvasManifestations(getSelectedManifestations(context));
        selectedManifestations = context.getSelectedManifestations();
        for (View viewManifestation : selectedManifestations) {
            Container p = SwingUtilities.getAncestorOfClass(Panel.class, viewManifestation);
            if (p != null) {
                selectedPanels.add(p);
            }
        }
        return !selectedPanels.isEmpty();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (View v : selectedManifestations) {
            Container p = SwingUtilities.getAncestorOfClass(Panel.class, v);
            FileNameExtensionFilter extFilter = new FileNameExtensionFilter(BUNDLE.getString("ExportViewFormat"), BUNDLE.getString("ExportViewFormat"));
            getFileChooser().setFileFilter(extFilter);
            getFileChooser().addChoosableFileFilter(extFilter);
            getFileChooser().setDialogTitle(BUNDLE.getString("ExportPanelMessage"));
            getFileChooser().setApproveButtonToolTipText(BUNDLE.getString("ExportPanelDialog") + " " + ((Panel) p).getTitle());
            int returnVal = getFileChooser().showSaveDialog(p);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (!getFileChooser().getSelectedFile().getName().endsWith("."+ BUNDLE.getString("ExportViewFormat"))) {
                    if (!(new File(getFileChooser().getSelectedFile().getAbsolutePath()  + "."+ BUNDLE.getString("ExportViewFormat")).exists())) {
                        getFileChooser().setSelectedFile(new File(getFileChooser().getSelectedFile().getAbsolutePath()  + "."+ BUNDLE.getString("ExportViewFormat")));
                    }
                }
                BufferedImage bi = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                p.paint(g);
                g.dispose();
                FileOutputStream fos = null;
                try{
                    fos = new FileOutputStream(getFileChooser().getSelectedFile());
                    ImageIO.write(bi,BUNDLE.getString("ExportViewFormat"),fos);
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage());
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e1) {
                            LOGGER.error("Unable to close file " + getFileChooser().getSelectedFile().getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
//        return !selectedPanels.isEmpty();
        for (View viewManifestation : selectedManifestations) {
            CanvasManifestation canvas = (CanvasManifestation) SwingUtilities.getAncestorOfClass(CanvasManifestation.class, viewManifestation);
            if (canvas != null) {
                return !canvas.getSelectedManifestations().isEmpty();
            }
        }
        return false;
    }
    
    protected Collection<View> getSelectedManifestations(ActionContext actionContext) {
        return actionContext.getSelectedManifestations();
    }
    
    private Collection<CanvasManifestation> getCanvasManifestations(
                    Collection<View> selectedManifestations) {
        List<CanvasManifestation> selectedCanvasManifestations = new LinkedList<CanvasManifestation>();

        for (View viewManifestation : selectedManifestations) {
            if (viewManifestation instanceof CanvasManifestation) {
                selectedCanvasManifestations.add((CanvasManifestation) viewManifestation);
            }
        }
        return selectedCanvasManifestations;
    }
    
}
