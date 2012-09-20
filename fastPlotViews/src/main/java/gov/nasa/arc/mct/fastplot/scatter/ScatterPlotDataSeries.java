package gov.nasa.arc.mct.fastplot.scatter;

import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataSeries;
import gov.nasa.arc.mct.fastplot.bridge.LegendEntry;

import java.util.SortedMap;

import plotter.xy.SimpleXYDataset;

public class ScatterPlotDataSeries implements AbstractPlotDataSeries {
	private SimpleXYDataset dataset;
	private SortedMap<Long, Double> dependent;
	private SortedMap<Long, Double> independent;
	private LegendEntry legend;
	
	public ScatterPlotDataSeries(SortedMap<Long, Double> independent, SortedMap<Long, Double> dependent, LegendEntry legend) {
		super();
		this.dependent = dependent;
		this.independent = independent;		
		this.legend = legend;
		
		//ScatterXYPlotLine plotLine = new ScatterXYPlotLine
		// TODO: Setup plot line, etc!
	}

	@Override
	public LegendEntry getLegendEntry() {
		return legend;
	}

}
