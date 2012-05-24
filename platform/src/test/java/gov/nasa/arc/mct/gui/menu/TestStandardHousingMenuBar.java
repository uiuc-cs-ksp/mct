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
package gov.nasa.arc.mct.gui.menu;

import static org.mockito.Mockito.when;
import gov.nasa.arc.mct.component.MockComponent;
import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.dao.specifications.MCTUser;
import gov.nasa.arc.mct.defaults.view.MCTHousingViewManifestation;
import gov.nasa.arc.mct.gui.ActionContextImpl;
import gov.nasa.arc.mct.gui.ActionManager;
import gov.nasa.arc.mct.gui.ContextAwareMenu;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.gui.housing.MCTHousingFactory;
import gov.nasa.arc.mct.gui.housing.MCTStandardHousing;
import gov.nasa.arc.mct.gui.menu.housing.ConveniencesMenu;
import gov.nasa.arc.mct.gui.menu.housing.EditMenu;
import gov.nasa.arc.mct.gui.menu.housing.HelpMenu;
import gov.nasa.arc.mct.gui.menu.housing.ObjectsMenu;
import gov.nasa.arc.mct.gui.menu.housing.ThisMenu;
import gov.nasa.arc.mct.gui.menu.housing.ViewMenu;
import gov.nasa.arc.mct.gui.menu.housing.WindowsMenu;
import gov.nasa.arc.mct.gui.util.TestSetupUtilities;
import gov.nasa.arc.mct.persistence.PersistenceUnitTest;
import gov.nasa.arc.mct.services.component.ViewInfo;
import gov.nasa.arc.mct.services.component.ViewType;
import gov.nasa.arc.mct.services.internal.component.ComponentInitializer;
import gov.nasa.arc.mct.services.internal.component.User;

import java.awt.GraphicsEnvironment;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class TestStandardHousingMenuBar extends PersistenceUnitTest {

    private AbstractComponent componentA;
    private AbstractComponent componentB;
    private AbstractComponent componentC;
    @Mock
    private MCTUser user;
    
    @Override
    protected User getUser() {
        MockitoAnnotations.initMocks(this);

        when(user.getUserId()).thenReturn("asi");
        when(user.getDisciplineId()).thenReturn("CATO");
        return user;
    }

    @Override
    protected void postSetup() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        TestSetupUtilities.setupForMockPolicyManager();
        TestSetupUtilities.setupForMenuBar();
        
        componentA = new MockComposite();
        componentA.setShared(false);
        componentA.getCapability(ComponentInitializer.class).initialize();

        
        componentB = new MockComposite();
        componentB.setShared(false);
        componentB.getCapability(ComponentInitializer.class).initialize();

        
        componentC = new MockComponent();
        componentC.setShared(false);
        componentC.getCapability(ComponentInitializer.class).initialize();

        // Register menus in the menubar
        ActionManager.registerMenu(ThisMenu.class, "THIS_MENU");
        ActionManager.registerMenu(ObjectsMenu.class, "OBJECTS_MENU");
        ActionManager.registerMenu(ViewMenu.class, "VIEW_MENU");
        ActionManager.registerMenu(WindowsMenu.class, "WINDOWS_MENU");
        ActionManager.registerMenu(ConveniencesMenu.class, "CONVENIENCES_MENU");
        ActionManager.registerMenu(HelpMenu.class, "HELP_MENU");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testNullHousing() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        new MCTStandardHousingMenuBar(null);
    }

    @Test
    public void testActiveHousingComponentNullPermission() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        
        MockHousing housing = new MockHousing(0, 0, 0, MCTHousingFactory.DIRECTORY_AREA_ENABLE, new MCTHousingViewManifestation(componentA,new ViewInfo(MCTHousingViewManifestation.class,"",ViewType.LAYOUT)));
        MCTStandardHousingMenuBar standardMenuBar = new MCTStandardHousingMenuBar(housing);
        Assert.assertEquals(standardMenuBar.getMenuCount(), 6);

        for (int i = 0; i < standardMenuBar.getMenuCount(); i++) {
            Assert.assertFalse(standardMenuBar.getMenu(i) instanceof EditMenu);
        }

    }

    @Test
    public void testActiveHousingComponentRWPermission() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        
        MockHousing housing = new MockHousing(0, 0, 0, MCTHousingFactory.DIRECTORY_AREA_ENABLE, new MCTHousingViewManifestation(componentB,new ViewInfo(MCTHousingViewManifestation.class,"",ViewType.LAYOUT)));
        MCTStandardHousingMenuBar standardMenuBar = new MCTStandardHousingMenuBar(housing);
        Assert.assertEquals(standardMenuBar.getMenuCount(), 6);

        for (int i = 0; i < standardMenuBar.getMenuCount(); i++) {
            Assert.assertFalse(standardMenuBar.getMenu(i) instanceof EditMenu);
        }

        ActionContextImpl context = new ActionContextImpl();
        context.setTargetHousing(housing);
        context.addTargetViewComponent(housing.getHousedViewManifestation());
        List<ContextAwareMenu> userObjectMenus = standardMenuBar.getUserObjectMenus(context);
        Assert.assertEquals(userObjectMenus.size(), 1);
        Assert.assertTrue(userObjectMenus.get(0) instanceof ObjectsMenu);
        
    }

    @Test
    public void testActiveHousingComponent() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        MockHousing housing = new MockHousing(0, 0, 0, MCTHousingFactory.CONTENT_AREA_ENABLE, new MCTHousingViewManifestation(componentC,new ViewInfo(MCTHousingViewManifestation.class,"",ViewType.LAYOUT)));
        MCTStandardHousingMenuBar standardMenuBar = new MCTStandardHousingMenuBar(housing);
        Assert.assertEquals(standardMenuBar.getMenuCount(), 6);

        ActionContextImpl context = new ActionContextImpl();
        context.setTargetHousing(housing);
        context.addTargetViewComponent(housing.getHousedViewManifestation());
        List<ContextAwareMenu> userObjectMenus = standardMenuBar.getUserObjectMenus(context);
        Assert.assertEquals(userObjectMenus.size(), 1);
        Assert.assertTrue(userObjectMenus.get(0) instanceof ObjectsMenu);
    }
    
    @AfterClass
    public void tearDown() {
        TestSetupUtilities.tearDownMockPolicyManager();
    }

    @SuppressWarnings("serial")
    private class MockHousing extends MCTStandardHousing {

        public MockHousing(int width, int height, int closeAction, byte areaSelection, View housingView) {
            super(width, height, closeAction, housingView);
        }

    }

    private class MockComposite extends MockComponent {

        public MockComposite() {
            super();
        }

    }
}