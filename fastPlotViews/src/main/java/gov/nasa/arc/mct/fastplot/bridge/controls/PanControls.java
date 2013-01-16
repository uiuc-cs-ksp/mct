package gov.nasa.arc.mct.fastplot.bridge.controls;

import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis;
import gov.nasa.arc.mct.fastplot.bridge.PlotObserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SpringLayout;

public class PanControls extends AbstractPlotLocalControl {
	private static final long serialVersionUID = 3970100144412350694L;

	private static final List<AttachmentLocation> X_ATTACHMENTS = Arrays.asList(
				new AttachmentLocation(SpringLayout.NORTH, SpringLayout.SOUTH, 0),
				new AttachmentLocation(SpringLayout.HORIZONTAL_CENTER, SpringLayout.HORIZONTAL_CENTER, 0));
	private static final List<AttachmentLocation> Y_ATTACHMENTS = Arrays.asList(
				new AttachmentLocation(SpringLayout.EAST, SpringLayout.WEST, 0),
				new AttachmentLocation(SpringLayout.VERTICAL_CENTER, SpringLayout.VERTICAL_CENTER, 0));
	
	private AbstractAxis axis;

	public PanControls(AbstractAxis axis) {
		super();
		this.axis = axis;
		add(new JButton("<"));
		add(new JButton(">"));
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
}
