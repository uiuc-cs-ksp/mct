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
package gov.nasa.arc.mct.fastplot.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Explicitly re-dispatches mouse events from one component 
 * to another target component. This is useful for the specific 
 * case of supporting tool tips without interfering with 
 * propagation of mouse events (specifically, showing tool tips 
 * on axis labels without breaking the time sync feature.)
 * 
 * See {@link http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4413412}
 * and {@link http://stackoverflow.com/questions/14931323}
 * for root cause & solution rationale.
 */
public class MouseRedispatcher extends MouseAdapter {
	private JComponent target;

	/**
	 * Create a MouseListener/MouseMotionListener which will 
	 * re-dispatch all mouse events to the specified component.
	 * @param target the new receiver for these events
	 */
	public MouseRedispatcher(JComponent target) {
		super();
		this.target = target;
	}

	private void redispatch(MouseEvent e) {
		e = SwingUtilities.convertMouseEvent(e.getComponent(), e, target);
		target.dispatchEvent(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		redispatch(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		redispatch(e);
	}
}
