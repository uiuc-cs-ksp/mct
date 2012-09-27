package gov.nasa.arc.mct.plot.adapter;

import gov.nasa.arc.mct.plot.settings.PlotConfiguration;

import java.util.List;

import javax.swing.JComponent;

/** 
 * A single instance of a plot; may contain one or more lines.
 * @author vwoeltje
 */
public interface Plot {
	public void           configure(PlotConfiguration configuration);
	public JComponent     getRepresentation();
	public PlotLine       createPlotLine();
	public List<PlotLine> getPlotLines();
	public PlotAppearance getAppearance();
	public void           setAppearance(PlotAppearance appearance);
	
	//TODO: groupWith 
	
	public static enum PlotAxis {
		DEPENDENT , INDEPENDENT
	}
}
