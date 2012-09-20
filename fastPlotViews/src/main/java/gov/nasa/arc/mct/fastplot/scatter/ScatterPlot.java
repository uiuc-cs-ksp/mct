package gov.nasa.arc.mct.fastplot.scatter;

import gov.nasa.arc.mct.components.FeedProvider.RenderingInfo;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataManager;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlotDataSeries;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlottingPackage;
import gov.nasa.arc.mct.fastplot.bridge.LegendManager;
import gov.nasa.arc.mct.fastplot.bridge.PlotAbstraction;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.LimitAlarmState;
import gov.nasa.arc.mct.fastplot.bridge.PlotLocalControlsManager;
import gov.nasa.arc.mct.fastplot.bridge.PlotObserver;
import gov.nasa.arc.mct.fastplot.bridge.PlotViewActionListener;
import gov.nasa.arc.mct.fastplot.settings.PlotConfiguration;
import gov.nasa.arc.mct.fastplot.settings.PlotConfigurationDelegator;
import gov.nasa.arc.mct.fastplot.settings.PlotSettings;
import gov.nasa.arc.mct.fastplot.utils.AbbreviatingPlotLabelingAlgorithm;
import gov.nasa.arc.mct.fastplot.view.Axis;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JComponent;

public class ScatterPlot extends PlotConfigurationDelegator implements AbstractPlottingPackage {
	private AbstractPlotDataManager plotDataManager = new ScatterPlotDataManager(this);
	private ArrayList<PlotObserver> observers = new ArrayList<PlotObserver>();
	private Set<String> knownDataSeries = new HashSet<String>();
	private PlotAbstraction abstraction;
	
	private long minTime;
	private long maxTime;
	
	private JComponent plotPanel;
	
	private ImplicitTimeAxis timeAxis = new ImplicitTimeAxis();
	private AbbreviatingPlotLabelingAlgorithm plotLabelingAlgorithm = new AbbreviatingPlotLabelingAlgorithm();
	private LegendManager legendManager = new LegendManager(plotLabelingAlgorithm);
	
	public ScatterPlot() {
		this (new PlotSettings());
	}
	
	public ScatterPlot(PlotConfiguration delegate) {
		super(delegate);
		if (delegate instanceof PlotAbstraction) {
			setPlotAbstraction((PlotAbstraction) delegate);
		}
		minTime = delegate.getMinTime();
		maxTime = delegate.getMaxTime();

	}
	
	@Override
	public void registerObservor(PlotObserver o) {
		observers.add(o);

	}

	@Override
	public void removeObserver(PlotObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObserversTimeChange() {
		//TODO need to implement for non-time?
	}

	@Override
	public void createChart(Font timeAxisFont, int plotLineThickness,
			Color plotBackgroundFrameColor, Color plotAreaBackgroundColor,
			int timeAxisIntercept, Color timeAxisColor,
			Color timeAxisLabelColor, Color nonTimeAxisLabelColor,
			String timeAxisDataFormat, Color nonTimeAxisColor,
			Color gridLineColor, int minSamplesForAutoScale,
			boolean isCompressionEnabled, boolean isTimeLabelsEnabled,
			boolean isLocalControlEnabled, PlotAbstraction thePlotAbstraction,
			AbbreviatingPlotLabelingAlgorithm thePlotLabelingAlgorithm) {

		
		ScatterPlotObjects objects = new ScatterPlotObjects(this);
		objects.setAxisRepresentation(timeAxisFont, nonTimeAxisColor);
		plotPanel = objects.getXYPlot();

		
	}
	

	@Override
	public JComponent getPlotPanel() {
		return plotPanel;
	}
	
	private boolean begun = false; //TODO: Need better organization of this

	@Override
	public void addDataSet(String dataSetName, Color plottingColor) {
		plotDataManager.addDataSet(dataSetName, plottingColor);
		knownDataSeries.add(dataSetName);
		AbstractPlotDataSeries series = plotDataManager.getDataSeries(dataSetName);
		if (series != null) {
			series.getLegendEntry().setDataSetName(dataSetName);
		}
	}

	@Override
	public void addDataSet(String lowerCase, Color plottingColor,
			String displayName) {
		addDataSet(lowerCase, plottingColor);
		AbstractPlotDataSeries series = plotDataManager.getDataSeries(lowerCase);
		if (series != null) {
			series.getLegendEntry().setDataSetName(displayName);
		}	
	}

	@Override
	public boolean isKnownDataSet(String setName) {
		return plotDataManager.getDataSeries(setName) != null;
	}

	@Override
	public void updateLegend(String dataSetName, RenderingInfo info) {

	}

	@Override
	public void refreshDisplay() {
		
	}

	@Override
	public int getDataSetSize() {
		return knownDataSeries.size();
	}

	@Override
	public LimitAlarmState getDependentMaxAlarmState() {
		return LimitAlarmState.NO_ALARM; // TODO - need limit alarm for scatterplot
	}

	@Override
	public LimitAlarmState getDependentMinAlarmState() {
		return LimitAlarmState.NO_ALARM; // TODO - need limit alarm for scatterplot
	}

	@Override
	public GregorianCalendar getCurrentTimeAxisMin() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(super.getMinTime());
		return gc;
	}

	@Override
	public GregorianCalendar getCurrentTimeAxisMax() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(super.getMaxTime());
		return gc;	}

	@Override
	public void showTimeSyncLine(GregorianCalendar time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTimeSyncLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getInitialNonTimeMinSetting() {
		return super.getMinNonTime();
	}

	@Override
	public double getInitialNonTimeMaxSetting() {
		return super.getMaxNonTime();
	}

	@Override
	public long getInitialTimeMinSetting() {
		return super.getMinTime();
	}

	@Override
	public long getInitialTimeMaxSetting() {
		return super.getMaxTime();
	}

	@Override
	public boolean isTimeSyncLineVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPlotAbstraction(PlotAbstraction plotView) {
		abstraction = plotView;
		abstraction.setPlotTimeAxis(timeAxis);
	}

	@Override
	public void notifyGlobalTimeSyncFinished() {
		
	}

	@Override
	public boolean inTimeSyncMode() {
		return false;
	}

	@Override
	public double getNonTimeMaxDataValueCurrentlyDisplayed() {
		// TODO Auto-generated method stub
		return 1.5;
	}

	@Override
	public double getNonTimeMinDataValueCurrentlyDisplayed() {
		// TODO Auto-generated method stub
		return -1.5;
	}

	@Override
	public void setCompressionEnabled(boolean state) {
		// Ignore compression! Makes no sense on scatter plot
	}

	@Override
	public boolean isCompressionEnabled() {
		return false;
	}

	@Override
	public void informUpdateCachedDataStreamStarted() {
		
	}

	@Override
	public void informUpdateCacheDataStreamCompleted() {
		
	}

	@Override
	public void informUpdateFromLiveDataStreamStarted() {
		
	}

	@Override
	public void informUpdateFromLiveDataStreamCompleted() {
		
	}

	@Override
	public void setTimeAxisStartAndStop(long startTime, long endTime) {
		minTime = startTime;
		maxTime = endTime;
	}

	@Override
	public void clearAllDataFromPlot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPaused() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Axis getNonTimeAxis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateResetButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlotLabelingAlgorithm(
			AbbreviatingPlotLabelingAlgorithm thePlotLabelingAlgorithm) {
		plotLabelingAlgorithm = thePlotLabelingAlgorithm;
	}

	@Override
	public AbbreviatingPlotLabelingAlgorithm getPlotLabelingAlgorithm() {
		return this.plotLabelingAlgorithm;
	}

	@Override
	public void addData(String feedID, SortedMap<Long, Double> points) {
		plotDataManager.addData(feedID, points);
	}

	@Override
	public void addData(String feed, long time, double value) {
		SortedMap<Long, Double> points = new TreeMap<Long, Double>();
		points.put(time, value);
		addData(feed, points);
	}

	@Override
	public void setTruncationPoint(double min) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCompressionRatio() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlotAbstraction getPlotAbstraction() {
		return abstraction;
	}

	@Override
	public LegendManager getLegendManager() {
		return legendManager;
	}

	@Override
	public AbstractPlotDataManager getPlotDataManager() {
		return plotDataManager;
	}

	@Override
	public PlotLocalControlsManager getLocalControlsManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlotViewActionListener getPlotActionListener() {
		// TODO Auto-generated method stub
		return null;
	}



}
