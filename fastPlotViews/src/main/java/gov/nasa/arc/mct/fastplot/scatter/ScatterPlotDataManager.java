package gov.nasa.arc.mct.fastplot.scatter;

import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataManager;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataSeries;
import gov.nasa.arc.mct.fastplot.bridge.LegendEntry;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class ScatterPlotDataManager implements AbstractPlotDataManager {
	private ScatterPlot scatterPlot;
	private Map<String, SortedMap<Long, Double>> dataPoints = new HashMap<String, SortedMap<Long, Double>>();
	private Map<String, Map<String, ScatterPlotDataSeries>> dataSeriesMap = new HashMap<String, Map<String, ScatterPlotDataSeries>>();
	private String activeIndependent = null;
	
	public ScatterPlotDataManager(ScatterPlot scatterPlot) {
		super();
		this.scatterPlot = scatterPlot;
	}
	
	public void beginGroup(String dataSetName) {
		activeIndependent = dataSetName;
		if (!dataSeriesMap.containsKey(dataSetName)) {
			dataSeriesMap.put(dataSetName, new HashMap<String, ScatterPlotDataSeries>());
		}
	}
	
	public void endGroup() {
		activeIndependent = null;
	}
	
	private SortedMap<Long, Double> getDataMap(String key) {
		if (!dataPoints.containsKey(key)) {
			dataPoints.put(key, new TreeMap<Long,Double>());
		}
		return dataPoints.get(key);
	}

	@Override
	public void addDataSet(String dataSetName, Color plottingColor) {
		if (activeIndependent == null) {
			beginGroup(dataSetName);
		} else {
			LegendEntry legendEntry =
				new LegendEntry(PlotConstants.LEGEND_BACKGROUND_COLOR, plottingColor,
						PlotConstants.DEFAULT_TIME_AXIS_FONT, scatterPlot.getPlotLabelingAlgorithm());
			scatterPlot.getLegendManager().addLegendEntry(legendEntry);
			ScatterPlotDataSeries dataSeries = 
				new ScatterPlotDataSeries(getDataMap(activeIndependent), getDataMap(dataSetName), legendEntry);
			dataSeriesMap.get(activeIndependent).put(dataSetName, dataSeries);
		}
	}

	@Override
	public void addData(String feed, SortedMap<Long, Double> points) {
		SortedMap<Long, Double> target = dataPoints.get(feed);
		for (Entry<Long, Double> point : points.entrySet()) {
			target.put(point.getKey(), point.getValue());
		}
		// TODO: We also need to discard old data at some point!
	}

	@Override
	public void informUpdateCacheDataStreamStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void informResizeEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resizeAndReloadPlotBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPlotDataSeries getDataSeries(String name) {
		if (activeIndependent == null || !dataSeriesMap.containsKey(activeIndependent)) {
			return null;
		}
		return dataSeriesMap.get(activeIndependent).get(name);
	}

}
