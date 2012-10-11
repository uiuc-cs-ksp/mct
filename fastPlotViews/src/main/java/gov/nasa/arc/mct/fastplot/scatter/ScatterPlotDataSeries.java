package gov.nasa.arc.mct.fastplot.scatter;

import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataSeries;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotLine;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlottingPackage;
import gov.nasa.arc.mct.fastplot.bridge.LegendEntry;
import gov.nasa.arc.mct.fastplot.view.legend.AbstractLegendEntry;

import java.util.Map.Entry;
import java.util.SortedMap;

public class ScatterPlotDataSeries implements AbstractPlotDataSeries {
	private AbstractPlotLine plotLine;
	private SortedMap<Long, Double> dependent;
	private SortedMap<Long, Double> independent;
	private long        lastUpdate = Long.MIN_VALUE;
	private AbstractLegendEntry legend;
	
	public ScatterPlotDataSeries(AbstractPlottingPackage plot, SortedMap<Long, Double> independent, SortedMap<Long, Double> dependent, LegendEntry legend) {
		super();
		this.dependent = dependent;
		this.independent = independent;		
		this.legend = legend;
		this.plotLine = plot.createPlotLine();
		// TODO: Setup plot line, etc!
	}

	@Override
	public AbstractLegendEntry getLegendEntry() {
		return legend;
	}
	
	public AbstractPlotLine getPlotLine() {
		return plotLine;
	}
	
	public void updatePlotLine() {
		for (Entry<Long, Double> entry : dependent.entrySet()) {
			long t = entry.getKey();
			if (t > lastUpdate) {
				if (independent.containsKey(t)) {
					double depValue = entry.getValue();
					double indValue = independent.get(t);
					plotLine.addData(indValue, depValue);
				}
				lastUpdate = t;
			}
		}
	}

	@Override
	public void setLegendEntry(AbstractLegendEntry entry) {
		legend = entry;
		entry.attachPlotLine(plotLine);
	}

}
