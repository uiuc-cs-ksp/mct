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
package gov.nasa.arc.mct.importExport.provider;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.gui.ActionContext;
import gov.nasa.arc.mct.gui.ContextAwareAction;
import gov.nasa.arc.mct.gui.FileChooser;
import gov.nasa.arc.mct.gui.FrameUtil;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.jsc.mct.importExport.utilities.Utilities;
import gov.nasa.jsc.mct.importExport.utilities.XMLFileFilter;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 * The <code>ExportAction</code> allows user to select a component to be serialized
 * into an XML file. This menu item is located under the The "This" menu in the 
 * menubar.
 * 
 */

public class ExportThisAction extends ContextAwareAction implements
		PropertyChangeListener {

	private static final long serialVersionUID = 5839600208723859922L;
	private static ResourceBundle bundle = ResourceBundle
			                               .getBundle("ImportExportProvider");
	private JProgressBar progressBar;
	private JDialog jd;
	private View windowView;

	public ExportThisAction() {
		super(bundle.getString("export_text"));
	}

	/**
	 * Callback for the export menu item.
	 * 
	 * Presents a FileChooser to user, then serializes all components into selected 
	 * XML file.
	 * 
	 * @param e The {@link ActionEvent}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get user selected file
		File file = selectFile(windowView);

		if (file != null) {
			// find the selected component
			AbstractComponent selectedComponent = windowView.getManifestedComponent();
			List<AbstractComponent> selectedComponents = new ArrayList<AbstractComponent>();
			selectedComponents.add(selectedComponent);

			buildProgressBar();

			// create the JAXB handler for marshal of data
			Exporter jbHandler = new Exporter(file, selectedComponents, 
					                          progressBar, jd);
			
			// ImportAction on EDT listens to Task's bound property progress
			jbHandler.addPropertyChangeListener(this);
			jbHandler.execute();

		}
	}
	
	/**
	 * Build the progress bar
	 */
	private void buildProgressBar() {
		Window activeWindow = FrameUtil.getFrameForComponent(windowView);
		jd = new JDialog();
		jd.setModal(false);
		jd.setTitle(bundle.getString("export_text")
				+ " progress");
		jd.setLocationRelativeTo(activeWindow);
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(5, 100, 5,
				100));
		
		jd.getContentPane().add(progressBar);
		jd.pack();
		jd.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	/**
	 * Opens FileChooser with the passed in component as parent and returns a
	 * File object specified by the user through the FileChooser.
	 * 
	 * @param source
	 *            parent component into which the FileChooser is to be opened
	 * @return File object returned by the FileChooser, which may be null if user
	 *         canceled out of the FileChooser
	 */

	private File selectFile(Component source) {
		// create a save as dialog
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setDialogTitle(bundle.getString("export_message"));
		fileChooser.setApproveButtonText(bundle.getString("export_button"));
		fileChooser.setFileSelectionMode(FileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new XMLFileFilter());
		if (source == null)
			return null;
		int returnVal = fileChooser.showOpenDialog(source);

		if (returnVal == FileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			// check if the selected file ends with the extension
			// if not then add it
			String path = file.getAbsolutePath();
			// TODO add .xml to bundle?
			if (!path.endsWith(".xml")) {
				file = new File(path + ".xml");
			}
			return file;
		}
		return null;
	}

	/**
	 * Given the {@link ActionContext}, this method determines if we have a
	 * single selection, which determines the availability (i.e., either shown
	 * or hidden) of this action when appeared as a menu item.
	 * 
	 * @param context
	 *            the {@link ActionContext}
	 * @return a boolean that indicates the availability of the action
	 */
	@Override
	public boolean canHandle(ActionContext context) {
		windowView = context.getWindowManifestation();
		return true;
	}

	/**
	 * Controls whether menu item is gray or not, based on policy.
	 * 
	 * @return boolean indicating if menu item is enabled
	 */
	@Override
	public boolean isEnabled() {
		return Utilities.isCreateable(windowView.getManifestedComponent());
	}
}
