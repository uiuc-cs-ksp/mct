package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.AxisOrientationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.NonTimeAxisSubsequentBoundsSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.PlotLineConnectionType;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.PlotLineDrawingFlags;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.TimeAxisSubsequentBoundsSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.XAxisMaximumLocationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.YAxisMaximumLocationSetting;

public class PlotSettings {
	/** Time axis orientation setting. */
	public AxisOrientationSetting timeAxisSetting;
	/** Time System setting. */
	public String timeSystemSetting;
	/** Time Format setting. */
	public String timeFormatSetting;
	/** X-axis maximum location setting. */
	public XAxisMaximumLocationSetting xAxisMaximumLocation;
	/** Y-axis maximum location setting. */
	public YAxisMaximumLocationSetting yAxisMaximumLocation;
	/** Time axis subsequent bounds settings. */
	public TimeAxisSubsequentBoundsSetting timeAxisSubsequent;
	/** Non-time axis minimal subsequent bounds setting. */
	public NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMinSetting;
	/** Non-time axis maximum subsequent bounds setting. */
	public NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMaxSetting;
	/** Max time in millisecs. */
	public long maxTime;
	/** Min time in millisecs. */
	public long minTime;
	/** Max non-time value. */
	public double maxNonTime;
	/** Min non-time value. */
	public double minNonTime;
	/** Time padding value. */
	public double timePadding;
	/** Non-time max padding. */
	public double nonTimeMaxPadding;
	/** Non-time min padding. */
	public double nonTimeMinPadding;
	/** Ordinal position for stacked plots. Defaults to true. */
	public boolean ordinalPositionForStackedPlots;
	/** Pin time axis. Defaults to false. */
	public boolean pinTimeAxis;
	/** Plot line drawing type; line, markers, or both. */
	public PlotLineDrawingFlags plotLineDraw;
	/** Plot line connection style; direct or step. */
	public PlotLineConnectionType plotLineConnectionType;

	public boolean isNull() {
		return timeAxisSetting == null;
	}
	
	public PlotSettings() {}
	
	public PlotSettings(AxisOrientationSetting timeAxisSetting,
			String timeSystemSetting, String timeFormatSetting,
			XAxisMaximumLocationSetting xAxisMaximumLocation,
			YAxisMaximumLocationSetting yAxisMaximumLocation,
			TimeAxisSubsequentBoundsSetting timeAxisSubsequent,
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMinSetting,
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMaxSetting,
			long maxTime, long minTime, double maxNonTime, double minNonTime,
			double timePadding, double nonTimeMaxPadding,
			double nonTimeMinPadding, boolean ordinalPositionForStackedPlots,
			boolean pinTimeAxis, PlotLineDrawingFlags plotLineDraw,
			PlotLineConnectionType plotLineConnectionType) {
		this.timeAxisSetting = timeAxisSetting;
		this.timeSystemSetting = timeSystemSetting;
		this.timeFormatSetting = timeFormatSetting;
		this.xAxisMaximumLocation = xAxisMaximumLocation;
		this.yAxisMaximumLocation = yAxisMaximumLocation;
		this.timeAxisSubsequent = timeAxisSubsequent;
		this.nonTimeAxisSubsequentMinSetting = nonTimeAxisSubsequentMinSetting;
		this.nonTimeAxisSubsequentMaxSetting = nonTimeAxisSubsequentMaxSetting;
		this.maxTime = maxTime;
		this.minTime = minTime;
		this.maxNonTime = maxNonTime;
		this.minNonTime = minNonTime;
		this.timePadding = timePadding;
		this.nonTimeMaxPadding = nonTimeMaxPadding;
		this.nonTimeMinPadding = nonTimeMinPadding;
		this.ordinalPositionForStackedPlots = ordinalPositionForStackedPlots;
		this.pinTimeAxis = pinTimeAxis;
		this.plotLineDraw = plotLineDraw;
		this.plotLineConnectionType = plotLineConnectionType;
	}
}