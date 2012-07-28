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

import java.awt.EventQueue;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import gov.nasa.arc.mct.gui.OptionBox;


/**
 * Unit test enabler.
 * Provides a way for unit tests to throw up a modal dialog, which is disposable.
 * 
 */
public class DialogMgr extends JComponent {

	private static final long serialVersionUID = 1L;
	private Window parent = null;
	
	public DialogMgr(JFrame parent) {
		this.parent = parent;
	}

	Window showMessageDialog(final String message) { 
		return showMessageDialog(message, "Message", OptionBox.INFORMATION_MESSAGE);
	}
	
	/**
	 * Create a dialog to display a string. If parent is not null, as is the case for unit
	 * tests, the dialog will be disposed.
	 * @param s string to display
	 * @return the parent of the dialog
	 */
	 
	Window showMessageDialog(final String message, final String title, final int messageType){
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					OptionBox.showMessageDialog(parent, message, title, messageType);
				}
			});
			//for unit tests, parent will be killable
			if (parent != null) {
				killWindow(parent);
			}

		} catch (InterruptedException e) {
			;
		} catch (InvocationTargetException e) {
			;
		}	
		return parent;
	}

	private static void killWindow(final Window frame) throws InterruptedException,
	                    InvocationTargetException {
		Thread.sleep(2000);
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				frame.dispose();
			}
		});
	}
}
