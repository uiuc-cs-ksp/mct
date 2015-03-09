package gov.nasa.arc.mct.fastplot.component;

import gov.nasa.arc.mct.components.FeedProvider;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

import plotter.xy.XYPlotAugmentationContents;

/**
 * Represents a viewed object to provide additional drawings on top of a plot.
 * 
 * This should be exposed via the getCapability method of a component.
 * 
 */
public interface PlotAugmentationCapability {
	/**
	 * Method to draw on top of the plot.
	 * @param xyPlotContents
	 * @param dataBounds
	 */
	public void draw(XYPlotAugmentationContents xyPlotContents, Rectangle2D dataBounds);

	/**
	 * Method to obtain the FeedProviders associated to the plot.
	 * @param keySet
	 */
	public void setFeedProviders(Collection<FeedProvider> feedProviders);
	
	/**
	 * Sets the minimum non-time value of the graph.
	 * @param minNonTime minimum non time value
	 */
	public void setMinNonTime(double minNonTime); 

	/**
	 * Sets the maximum non-time value of the graph.
	 * @param maxNonTime maximum non time value
	 */
	public void setMaxNonTime(double maxNonTime);

	/**
	 * Sets if the plot X-Axis is configured as Time. Otherwise it is configured as Y-Axis.
	 * @param b true if X-Axis is configured as time.
	 */
	public void setXAxisAsTime(boolean b);
}
