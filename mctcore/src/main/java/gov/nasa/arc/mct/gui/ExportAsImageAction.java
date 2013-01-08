package gov.nasa.arc.mct.gui;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 * @author dcberrio
 * Abstract action superclass for exporting things as images.
 */
public abstract class ExportAsImageAction extends ContextAwareAction {
    
    private static final long serialVersionUID = -855367390085242730L;
    /**
     * FileChooser for save dialog.
     */
    protected JFileChooser fileChooser;
    /**
     * JComponent to be exported.
     */
    protected JComponent jComponentToExport;
    
    /**
     * Constructor.
     * @param name The name of the action.
     */
    protected ExportAsImageAction(String name) {
        super(name);
        setFileChooser(new JFileChooser(){
            /**
             * 
             */
            private static final long serialVersionUID = 7528769046690472284L;

            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this,
                            "The file exists, overwrite?", "Existing file",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                    default:
                        return;
                    }
                }
                super.approveSelection();
            } 
        });
    }
    
    @Override
    public boolean isEnabled() {
        return (jComponentToExport != null &&  
                jComponentToExport.getWidth() > 0 && jComponentToExport.getHeight() > 0);
    }

    /**
     * Get the JFileChooser component.
     * @return JFileChooser
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    /** 
     * Set the JFileChooser component.
     * @param fileChooser the JFileChooser to set.
     */
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
}
