package gov.nasa.arc.mct.fastplot.bridge.controls;

import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlottingPackage;
import gov.nasa.arc.mct.fastplot.bridge.PlotObserver;
import gov.nasa.arc.mct.fastplot.bridge.PlotSubject;
import gov.nasa.arc.mct.fastplot.view.IconLoader.Icons;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CornerResetButtons extends AbstractPlotLocalControl implements PlotObserver {
	private static final long serialVersionUID = -3648127305459678924L;
	
	private ActionListener topLeft, topRight, bottomLeft, bottomRight;
	
	public CornerResetButtons() {
		//...make buttons...
//		topRight     = makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, this, "TopRightcornerButton");
//		topLeft      = makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, this, "TopRightcornerButton");
//		bottomRight  = makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, this, "TopRightcornerButton");
//		bottomLeft   = makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, this, "TopRightcornerButton");
		
		
		setLayout (new BorderLayout());
		JComponent top, bottom;
		top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		bottom = new JPanel(new BorderLayout());
		bottom.setOpaque(false);
		
		top.add(makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_LEFT_GREY, topLeft, "TopLeftCornerButton"), 
				BorderLayout.WEST);
		top.add(makeButton(Icons.PLOT_CORNER_RESET_BUTTON_TOP_RIGHT_GREY, topRight, "TopRightCornerButton"),
				BorderLayout.EAST);
		bottom.add(makeButton(Icons.PLOT_CORNER_RESET_BUTTON_BOTTOM_LEFT_GREY, bottomLeft, "BottomLeftCornerButton"),
				BorderLayout.WEST);
		bottom.add(makeButton(Icons.PLOT_CORNER_RESET_BUTTON_BOTTOM_RIGHT_GREY, bottomRight, "BottomRightCornerButton"),
				BorderLayout.EAST);
		
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);
	}
	
	@Override
	public Collection<AttachmentLocation> getDesiredAttachmentLocations() {
		return FILL_ATTACHMENT;
	}

	@Override
	public PlotObserver getPlotObserver() {
		return this;
	}

	@Override
	public void updateTimeAxis(PlotSubject subject, long startTime, long endTime) {

	}

	@Override
	public void plotAxisChanged(PlotSubject subject, AbstractAxis axis) {
		// TODO Auto-generated method stub
		//updateResetButtons
	}

}
