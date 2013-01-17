package gov.nasa.arc.mct.fastplot.bridge.controls;

import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis;
import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis.AxisVisibleOrientation;
import gov.nasa.arc.mct.fastplot.bridge.PlotObserver;
import gov.nasa.arc.mct.fastplot.bridge.PlotSubject;
import gov.nasa.arc.mct.fastplot.view.IconLoader.Icons;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.SpringLayout;

public class CornerResetButton extends AbstractPlotLocalControl implements PlotObserver, ActionListener {
	private static final long serialVersionUID = -348727120749498680L;
	
	private Collection<ObservableAxis> managedAxes = new ArrayList<ObservableAxis>();
	
	public CornerResetButton(ObservableAxis... axes) {
		super();
		setLayout(new GridLayout());
		Collections.addAll(managedAxes, axes);	
		add (makeButton(chooseIcon(), this, chooseName()));
		setVisible(false);
		setBorder(null);
	}

	@Override
	public Collection<AttachmentLocation> getDesiredAttachmentLocations() {
		String verticalEdge = isManaged(AxisVisibleOrientation.VERTICAL) ?
				SpringLayout.WEST : SpringLayout.EAST;
		String horizontalEdge = isManaged(AxisVisibleOrientation.HORIZONTAL) ?
				SpringLayout.SOUTH : SpringLayout.NORTH;
		return Arrays.asList(
				new AttachmentLocation(verticalEdge,   verticalEdge,   0),
				new AttachmentLocation(horizontalEdge, horizontalEdge, 0)
		);			
	}

	@Override
	public PlotObserver getPlotObserver() {
		return this;
	}

	@Override
	public void updateTimeAxis(PlotSubject subject, long startTime, long endTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void plotAxisChanged(PlotSubject subject, AbstractAxis axis) {
		boolean dirty = true;
		for (ObservableAxis a : managedAxes) {
			dirty &= a.isDirty();
		}
		setVisible(dirty);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (ObservableAxis a : managedAxes) {
			a.reset();
		}
	}

	private boolean isManaged(AxisVisibleOrientation o) {
		for (ObservableAxis axis : managedAxes) {
			if (axis.getVisibleOrientation() == o) {
				return true;
			}
		}
		return false;
	}
	
	private Icons chooseIcon() {
		Icons[] icons = { Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, Icons.PLOT_CORNER_RESET_BUTTON_TOP_RIGHT_GREY,
				          Icons.PLOT_CORNER_RESET_BUTTON_BOTTOM_LEFT_GREY, Icons.PLOT_CORNER_RESET_BUTTON_BOTTOM_RIGHT_GREY };
		int index = (isManaged(AxisVisibleOrientation.HORIZONTAL) ? 2 : 0) +
		         (isManaged(AxisVisibleOrientation.VERTICAL  ) ? 0 : 1);
		return icons[index];		
	}
	
	private String chooseName() {
		String[] names = { "TopLeftCornerButton", "TopRightCornerButton",
				           "BottomLeftCornerButton", "BottomRightCornerButton" };
		int index = (isManaged(AxisVisibleOrientation.HORIZONTAL) ? 2 : 0) +
		            (isManaged(AxisVisibleOrientation.VERTICAL  ) ? 0 : 1);
		return names[index];
	}
}
