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

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.MCTMutableTreeNode;
import gov.nasa.arc.mct.gui.SelectionProvider;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.housing.MCTDirectoryArea;
import gov.nasa.arc.mct.gui.housing.MCTHousing;
import gov.nasa.arc.mct.gui.impl.ActionContextImpl;
import gov.nasa.arc.mct.platform.spi.PersistenceProvider;
import gov.nasa.arc.mct.platform.spi.Platform;
import gov.nasa.arc.mct.platform.spi.PlatformAccess;
import gov.nasa.arc.mct.platform.spi.WindowManager;
import gov.nasa.arc.mct.services.component.ViewInfo;
import gov.nasa.arc.mct.services.component.ViewType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDeleteAll {
    @Mock ActionContextImpl mockContext;
    @Mock MCTHousing mockHousing;
    @Mock SelectionProvider mockSelection;
    @Mock MCTDirectoryArea mockDirectory;
    @Mock Platform mockPlatform;
    @Mock PersistenceProvider mockPersistence;
    @Mock WindowManager mockWindowing;
    @Mock MCTMutableTreeNode mockNode;
    
    private TreePath[] selectedTreePaths; // Since JTree can't be mocked
    
    private Platform oldPlatform;
    
    private ContextAwareAction deleteAll;
    
    @BeforeClass
    public void cachePlatform() {
        oldPlatform = PlatformAccess.getPlatform();
    }
    
    @AfterClass
    public void restorePlatform() {
        new PlatformAccess().setPlatform(oldPlatform);
    }
    
    @BeforeMethod    
    public void setup() {
        MockitoAnnotations.initMocks(this);
        new PlatformAccess().setPlatform(mockPlatform);
        
        // Common setup
        Mockito.when(mockNode.getParentTree())
            .thenReturn(new PseudoJTree());
        Mockito.when(mockHousing.getSelectionProvider())
            .thenReturn(mockSelection);
        
        deleteAll = new DeleteAllAction();
    }
    
    @Test (dataProvider="canHandleTestCases")
    public void testCanHandle(
            boolean hasHousing,
            Collection<View> selection,
            boolean hasDirectory,
            boolean hasFirstNode,
            TreePath[] treePaths,
            boolean expected            
            ) {
        // Setup inputs
        Mockito.when(mockContext.getTargetHousing())
            .thenReturn(hasHousing ? mockHousing : null);
        Mockito.when(mockContext.getSelectedManifestations())
            .thenReturn(selection);
        Mockito.when(mockHousing.getDirectoryArea())
            .thenReturn(hasDirectory ? mockDirectory : null);
        Mockito.when(mockDirectory.getSelectedDirectoryNode())
            .thenReturn(hasFirstNode ? mockNode : null);
        Mockito.when(mockSelection.getSelectedManifestations())
            .thenReturn(selection);
            
        selectedTreePaths = treePaths;
        
        // Test can handle
        Assert.assertEquals(
                deleteAll.canHandle(mockContext), 
                expected);
    }
    
    @DataProvider
    public Object[][] canHandleTestCases() {
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        boolean truths[] = {false, true};
        
        Collection<View> validSelection =
                Arrays.asList(mockView(ViewType.NODE));
        
        Collection<?>[] selections = {
                Collections.emptyList(),
                Arrays.asList(mockView(ViewType.OBJECT)),
                validSelection
        };
        
        TreePath[][] treePaths = {
                null,
                {},
                { Mockito.mock(TreePath.class) } 
        };
        
        // Permute possible 
        for (boolean hasHousing : truths) {
            for (Collection<?> selection : selections) {
                for (boolean hasDirectory : truths) {
                    for (boolean hasNode : truths) {
                        for (TreePath[] treePath : treePaths) {
                            // Based expectation on conditions
                            boolean expected =
                                    hasHousing &&
                                    (selection == validSelection) &&
                                    hasDirectory &&
                                    hasNode &&
                                    (treePath != null) &&
                                    (treePath.length != 0);
                            
                            testCases.add(new Object[]{
                                    hasHousing,
                                    selection,
                                    hasDirectory,
                                    hasNode,
                                    treePath,
                                    expected
                            });                            
                        }
                    }
                }
            }
        }
        
        Object[][] result = new Object[testCases.size()][];
        int i = 0;
        Iterator<Object[]> iter = testCases.iterator();
        while (iter.hasNext()) {
            result[i++] = iter.next();
        }
        return result;
    }
    
    private View mockView(ViewType vt) {
        return mockView(
                Mockito.mock(AbstractComponent.class),
                vt
                );
    }
    
    private View mockView(AbstractComponent ac, ViewType vt) {
        View view = Mockito.mock(View.class);
        ViewInfo viewInfo = Mockito.mock(ViewInfo.class);
        Mockito.when(viewInfo.getViewType()).thenReturn(vt);
        Mockito.when(view.getInfo()).thenReturn(viewInfo);
        Mockito.when(view.getManifestedComponent()).thenReturn(ac);
        return view;
    }
    
    private class PseudoJTree extends JTree {
        private static final long serialVersionUID = 1L;

        @Override
        public TreePath[] getSelectionPaths() {
            return selectedTreePaths;
        }
    }
    
}
