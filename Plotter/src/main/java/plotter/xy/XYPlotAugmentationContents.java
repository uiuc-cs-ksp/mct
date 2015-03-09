package plotter.xy;

import java.awt.Component;

/**
 * Contains the plot augmentation.
 */
public class XYPlotAugmentationContents extends XYPlotContents {
	private static final long serialVersionUID = -5212982470127330415L;
	
	private XYPlot xyPlot;

	/**
	 * 
	 * @param xyPlot the Plots points
	 */
	public XYPlotAugmentationContents(XYPlot xyPlot) {
		super();
		this.xyPlot = xyPlot;
	}
	
	/**
	 * Converts to physical value
	 * @param d The logical value to be converted
	 * @param isXAxisAsTime flag if the X-Axis is Time
	 * @return the physical value
	 */
	public int toPhysical(double d, boolean isXAxisAsTime) {

		if(isXAxisAsTime) {
			int yOffset = xyPlot.getContents().getY();
			XYAxis yAxis = xyPlot.getYAxis();
			double min = yAxis.getStart();
			double max = yAxis.getEnd();
			int endMargin = yAxis.getEndMargin();
			int height = yAxis.getHeight() - yAxis.getStartMargin();
			return height - (int) ((d - min) / (max - min) * (height - endMargin) + .5) - yOffset;

		} else {
			XYAxis xAxis = xyPlot.getXAxis();
			double minX = xAxis.getEnd();
			double maxX = xAxis.getStart();
			int endMarginX = xAxis.getEndMargin();
			int height = xAxis.getWidth() - xAxis.getStartMargin();
			return height - (int) ((d - minX) / (maxX - minX) * (height - endMarginX) + .5);
		}
	}
	
	@Override
	public void doLayout() {
		int width = getWidth();
		int height = getHeight();
		for (Component c : getComponents()) {
			c.setBounds(0, 0, width, height);
		}
	}

	@Override
	public boolean isValidateRoot() {
		return true;
	}
}
