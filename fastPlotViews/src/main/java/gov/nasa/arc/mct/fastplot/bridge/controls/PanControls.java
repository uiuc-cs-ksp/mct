package gov.nasa.arc.mct.fastplot.bridge.controls;

import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;
import gov.nasa.arc.mct.fastplot.bridge.PlotLocalControlsManager;
import gov.nasa.arc.mct.fastplot.bridge.PlotObserver;
import gov.nasa.arc.mct.fastplot.view.IconLoader.Icons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SpringLayout;

public class PanControls extends AbstractPlotLocalControl implements ActionListener {
	private static final long serialVersionUID = 3970100144412350694L;

	private static final List<AttachmentLocation> X_ATTACHMENTS = Arrays.asList(
				new AttachmentLocation(SpringLayout.NORTH, SpringLayout.SOUTH, 0),
				new AttachmentLocation(SpringLayout.HORIZONTAL_CENTER, SpringLayout.HORIZONTAL_CENTER, 0));
	private static final List<AttachmentLocation> Y_ATTACHMENTS = Arrays.asList(
				new AttachmentLocation(SpringLayout.EAST, SpringLayout.WEST, 0),
				new AttachmentLocation(SpringLayout.VERTICAL_CENTER, SpringLayout.VERTICAL_CENTER, 0));
	
	private AbstractAxis axis;
	private JButton      toStart, toEnd;
	private boolean      keyState = false;
	private boolean      mouseState = false;
	
	public PanControls(AbstractAxis axis) {
		super();
		this.axis = axis;
		toStart = makeButton(getIcon(true), this, getName(true));
		toEnd   = makeButton(getIcon(false), this, getName(false));
		toStart.setVisible(true);
		toEnd.setVisible(true);

		switch(axis.getVisibleOrientation()) {
		case HORIZONTAL:
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(toStart);
			add(toEnd);
			break;
		case VERTICAL:
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(toEnd);
			add(toStart);
			break;						
		}
		
		setVisible(false);
	}
	
	

	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.bridge.controls.AbstractPlotLocalControl#informKeyState(int, boolean)
	 */
	@Override
	public void informKeyState(int key, boolean pressed) {
		if (key == KeyEvent.VK_CONTROL) {
			keyState = pressed;
			setVisible(mouseState && keyState);
		}
	}



	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.bridge.controls.AbstractPlotLocalControl#informMouseHover(boolean)
	 */
	@Override
	public void informMouseHover(boolean inPlotArea) {
		mouseState = inPlotArea;
		setVisible(mouseState && keyState);
	}

	@Override
	public Collection<AttachmentLocation> getDesiredAttachmentLocations() {
		switch (axis.getVisibleOrientation()) {
		case HORIZONTAL: return X_ATTACHMENTS;
		case VERTICAL: return Y_ATTACHMENTS;
		}
		return Collections.<AttachmentLocation>emptyList();
	}

	@Override
	public PlotObserver getPlotObserver() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		double panAmount = (axis.getEnd() - axis.getStart()) * (PlotConstants.PANNING_PERCENTAGE / 100);	
		if (evt.getSource().equals(toStart)) {
			axis.shift(-panAmount);
		} else if (evt.getSource().equals(toEnd)) {
			axis.shift(panAmount);
		}		
		//TODO: Pin; maybe notify observers (or should axis be responsible?)
	}

	/**
	 * Utility method to determine appropriate icon for pan button
	 * @param less true if the pan button goes toward the bottom or left; otherwise, false
	 * @return
	 */
	private Icons getIcon(boolean less) {
		switch (axis.getVisibleOrientation()) {
		case HORIZONTAL: return less ? Icons.PLOT_PAN_LEFT_ARROW_ICON : Icons.PLOT_PAN_RIGHT_ARROW_ICON;
		case VERTICAL  : return less ? Icons.PLOT_PAN_DOWN_ARROW_ICON : Icons.PLOT_PAN_UP_ARROW_ICON;
		}
		return null;
	}
	
	/**
	 * Utility method to determine  an appropriate name for a pan button
	 * @param less true if the pan button goes toward the bottom or left; otherwise, false
	 * @return
	 */
	private String getName(boolean less) {
		switch (axis.getVisibleOrientation()) {
		case HORIZONTAL: return less ? "PanLeft" : "PanRight";
		case VERTICAL  : return less ? "PanDown" : "PanUp";
		}
		return null;
	}
		

}
