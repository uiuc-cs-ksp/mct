package gov.nasa.arc.mct.gui.actions;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;

import javax.swing.JFileChooser;

import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.housing.InspectionArea;
import gov.nasa.arc.mct.gui.housing.MCTHousing;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExportAsImageActionsTest {
    private ExportViewAsImageAction exportViewAction;
    private ExportThisAsImageAction exportWindowAction;
    
    @Mock
    MCTHousing housing;
    @Mock
    InspectionArea inspectionArea;
    @Mock
    View aView;
    @Mock JFileChooser fileChooser;
    
    @BeforeMethod
    protected void postSetup() {
        MockitoAnnotations.initMocks(this);
        
        exportViewAction = new ExportViewAsImageAction();
        exportWindowAction = new ExportThisAsImageAction();
    }

    @Test
    public void testConstructor() {
        assertNotNull(exportViewAction);
    }

    @Test (dependsOnMethods = { "testConstructor" }, expectedExceptions={IllegalStateException.class})
    public void testNullContexts() {
        exportViewAction.canHandle(null);
        exportWindowAction.canHandle(null);
    }

    @Test (dependsOnMethods = { "testConstructor" })
    public void testNonNullContexts() {
        File testOutputFile = new File("testImageExport.png");
        File testOutputFile2 = new File("testImageExport2.png");
        ActionContextImpl context = Mockito.mock(ActionContextImpl.class);
        Mockito.when(context.getTargetHousing()).thenReturn(housing);
        Mockito.when(context.getSelectedManifestations()).thenReturn(Collections.singleton(aView));
        Mockito.when(fileChooser.showSaveDialog(aView)).thenReturn(JFileChooser.APPROVE_OPTION);
        Mockito.when(fileChooser.getSelectedFile()).thenReturn(testOutputFile);
        exportViewAction.setFileChooser(fileChooser);
        exportWindowAction.setFileChooser(fileChooser);
        Mockito.when(housing.getInspectionArea()).thenReturn(inspectionArea);
        Mockito.when(inspectionArea.getHousedViewManifestation()).thenReturn(aView);
        Mockito.when(aView.getWidth()).thenReturn(10);
        Mockito.when(aView.getHeight()).thenReturn(10);
        
        context.setTargetHousing(housing);
        Mockito.when(context.getSelectedManifestations()).thenReturn(Collections.singleton(aView));
        ActionContextImpl context2 = Mockito.mock(ActionContextImpl.class);
        Mockito.when(context2.getTargetHousing()).thenReturn(housing);
        Mockito.when(context2.getWindowManifestation()).thenReturn(aView);

        
        assertTrue(exportViewAction.canHandle(context));
        assertTrue(exportViewAction.isEnabled());
        
        exportViewAction.actionPerformed(Mockito.mock(ActionEvent.class));
        Assert.assertTrue(testOutputFile.length() > 0L);
        testOutputFile.delete();
        
        Mockito.when(fileChooser.getSelectedFile()).thenReturn(testOutputFile2);
        assertTrue(exportWindowAction.canHandle(context2));
        assertTrue(exportWindowAction.isEnabled());
        
        exportWindowAction.actionPerformed(Mockito.mock(ActionEvent.class));
        Assert.assertTrue(testOutputFile2.length() > 0L);
        testOutputFile2.delete();
    }
    
    
}
