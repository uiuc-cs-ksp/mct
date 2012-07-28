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
package gov.nasa.jsc.mct.importExport.utilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Custom file choser that can have an owner field
 * 
 * @author dotsonu
 * 
 */

@SuppressWarnings("serial")
public class CustomFileChooser extends JFileChooser {
	private JComboBox ownerCombo;
	private boolean hasOwnerField;

	public CustomFileChooser() {
		super();
		this.ownerCombo = new JComboBox();
	}

	public String getOwner() {
		return (String) ownerCombo.getSelectedItem();
	}

	public void addOwners(List<String> ownerList) {
		// empty the list
		this.ownerCombo.removeAllItems();
		// add list to the combo
		for (String owner : ownerList) {
			this.ownerCombo.addItem(owner);
		}
		
		hasOwnerField = true;
	}

	public void setOwner(String owner) {
		this.ownerCombo.setSelectedItem(owner);
	}

	/**
	 * Override the create dialog only if this file chooser needs an owner field
	 */
	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException {

		JDialog dialog = super.createDialog(parent);
		if (hasOwnerField) {

			Container rootPane = dialog.getRootPane();

			// build horizontal panel
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

			// add label
			JLabel label = new JLabel("Owner After Import:  ");
			label.setDisplayedMnemonic('O');
			panel.add(label);

			// add owner text field
			panel.add(ownerCombo);
			panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

			// walk the component tree down to position we want
			Component[] components = rootPane.getComponents();
			Container component = (Container) components[1];
			component = (Container) component.getComponent(0);
			component = (Container) component.getComponent(0);
			component = (Container) component.getComponent(3);

			component.add(panel, 3);
		}
		return dialog;

	}

}
