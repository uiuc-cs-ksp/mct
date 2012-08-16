package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.AxisOrientationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.NonTimeAxisSubsequentBoundsSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.PlotLineConnectionType;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.PlotLineDrawingFlags;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.TimeAxisSubsequentBoundsSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.XAxisMaximumLocationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.YAxisMaximumLocationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotLineGlobalConfiguration;

public class PlotSettings extends GenericSettings {
	/** Time axis orientation setting. */
	private AxisOrientationSetting timeAxisSetting;
	/** Time System setting. */
	private String timeSystemSetting;
	/** Time Format setting. */
	private String timeFormatSetting;
	/** X-axis maximum location setting. */
	private XAxisMaximumLocationSetting xAxisMaximumLocation;
	/** Y-axis maximum location setting. */
	private YAxisMaximumLocationSetting yAxisMaximumLocation;
	/** Time axis subsequent bounds settings. */
	private TimeAxisSubsequentBoundsSetting timeAxisSubsequent;
	/** Non-time axis minimal subsequent bounds setting. */
	private NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMinSetting;
	/** Non-time axis maximum subsequent bounds setting. */
	private NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMaxSetting;
	/** Max time in millisecs. */
	private long maxTime;
	/** Min time in millisecs. */
	private long minTime;
	/** Max non-time value. */
	private double maxNonTime;
	/** Min non-time value. */
	private double minNonTime;
	/** Time padding value. */
	private double timePadding;
	/** Non-time max padding. */
	private double nonTimeMaxPadding;
	/** Non-time min padding. */
	private double nonTimeMinPadding;
	/** Ordinal position for stacked plots. Defaults to true. */
	private boolean ordinalPositionForStackedPlots;
	/** Pin time axis. Defaults to false. */
	private boolean pinTimeAxis;
	/** Plot line drawing type; line, markers, or both. */
	private PlotLineDrawingFlags plotLineDraw;
	/** Plot line connection style; direct or step. */
	private PlotLineConnectionType plotLineConnectionType;

	public boolean isNull() {
		return getTimeAxisSetting() == null;
	}
	
	private void createDefaults() {
		this.create(PlotConstants.TIME_AXIS_SETTING, AxisOrientationSetting.X_AXIS_AS_TIME, AxisOrientationSetting.class);
		this.create(PlotConstants.TIME_AXIS_SUBSEQUENT_SETTING, TimeAxisSubsequentBoundsSetting.JUMP, TimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.TIME_SYSTEM_SETTING, PlotConstants.DEFAULT_TIME_SYSTEM, String.class);
		this.create(PlotConstants.TIME_FORMAT_SETTING, PlotConstants.DEFAULT_TIME_FORMAT, String.class);
		this.create(PlotConstants.X_AXIS_MAXIMUM_LOCATION_SETTING, XAxisMaximumLocationSetting.MAXIMUM_AT_RIGHT, XAxisMaximumLocationSetting.class);
		this.create(PlotConstants.Y_AXIS_MAXIMUM_LOCATION_SETTING, PlotConstants.DEFAULT_Y_AXIS_MAX_LOCATION_SETTING, YAxisMaximumLocationSetting.class);
		this.create(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MIN_SETTING, PlotConstants.DEFAULT_NON_TIME_AXIS_MIN_SUBSEQUENT_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MAX_SETTING, PlotConstants.DEFAULT_NON_TIME_AXIS_MAX_SUBSEQUENT_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.TIME_MAX, null, Long.class);
		this.create(PlotConstants.TIME_MIN, null, Long.class);
		this.create(PlotConstants.NON_TIME_MAX, PlotConstants.DEFAULT_NON_TIME_AXIS_MAX_VALUE, Double.class);
		this.create(PlotConstants.NON_TIME_MIN, PlotConstants.DEFAULT_NON_TIME_AXIS_MIN_VALUE, Double.class);
		this.create(PlotConstants.TIME_PADDING, PlotConstants.DEFAULT_TIME_AXIS_PADDING, Double.class);
		this.create(PlotConstants.NON_TIME_MAX_PADDING, PlotConstants.DEFAULT_NON_TIME_AXIS_PADDING_MAX, Double.class);
		this.create(PlotConstants.NON_TIME_MIN_PADDING, PlotConstants.DEFAULT_NON_TIME_AXIS_PADDING_MIN, Double.class);
		this.create(PlotConstants.GROUP_BY_ORDINAL_POSITION, true, Boolean.class);
		this.create(PlotConstants.PIN_TIME_AXIS, false, Boolean.class);
		this.create(PlotConstants.DRAW_LINES, PlotConstants.DEFAULT_PLOT_LINE_DRAW.drawLine(), Boolean.class);
		this.create(PlotConstants.DRAW_MARKERS, PlotConstants.DEFAULT_PLOT_LINE_DRAW.drawMarkers(), Boolean.class);
		this.create(PlotConstants.CONNECTION_TYPE, PlotLineGlobalConfiguration.getDefaultConnectionType(), PlotLineConnectionType.class);
	}
	
	public PlotSettings() {
		createDefaults();
	}
	
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
		createDefaults();
		this.setTimeAxisSetting(timeAxisSetting);
		this.setTimeSystemSetting(timeSystemSetting);
		this.setTimeFormatSetting(timeFormatSetting);
		this.setXAxisMaximumLocation(xAxisMaximumLocation);
		this.setYAxisMaximumLocation(yAxisMaximumLocation);
		this.setTimeAxisSubsequent(timeAxisSubsequent);
		this.setNonTimeAxisSubsequentMinSetting(nonTimeAxisSubsequentMinSetting);
		this.setNonTimeAxisSubsequentMaxSetting(nonTimeAxisSubsequentMaxSetting);
		this.setMaxTime(maxTime);
		this.setMinTime(minTime);
		this.setMaxNonTime(maxNonTime);
		this.setMinNonTime(minNonTime);
		this.setTimePadding(timePadding);
		this.setNonTimeMaxPadding(nonTimeMaxPadding);
		this.setNonTimeMinPadding(nonTimeMinPadding);
		this.setOrdinalPositionForStackedPlots(ordinalPositionForStackedPlots);
		this.setPinTimeAxis(pinTimeAxis);
		this.setPlotLineDraw(plotLineDraw);
		this.setPlotLineConnectionType(plotLineConnectionType);
	}
	
	public boolean matches(PlotSettings settings) {
		return
			settings.getTimeAxisSetting() == getTimeAxisSetting() &&
			settings.getTimeSystemSetting() == getTimeSystemSetting() &&
			settings.getTimeFormatSetting() == getTimeFormatSetting() &&
			settings.getXAxisMaximumLocation() == getXAxisMaximumLocation() &&
			settings.getYAxisMaximumLocation() == getYAxisMaximumLocation() &&
			settings.getTimeAxisSubsequent() == getTimeAxisSubsequent() &&
			settings.getNonTimeAxisSubsequentMinSetting() == getNonTimeAxisSubsequentMinSetting() &&
			settings.getNonTimeAxisSubsequentMaxSetting() == getNonTimeAxisSubsequentMaxSetting() &&
			settings.getMaxTime() == getMaxTime() &&
			settings.getMinTime() == getMinTime() &&
			settings.getMaxNonTime() == getMaxNonTime() &&
			settings.getMinNonTime() == getMinNonTime() &&
			settings.getTimePadding() == getTimePadding() &&
			settings.getNonTimeMaxPadding() == getNonTimeMaxPadding() &&
			settings.getNonTimeMinPadding() == getNonTimeMinPadding() &&
			settings.getOrdinalPositionForStackedPlots() == getOrdinalPositionForStackedPlots() &&
			settings.getPinTimeAxis() == getPinTimeAxis() &&
			settings.getPlotLineDraw() == getPlotLineDraw() &&
			settings.getPlotLineConnectionType() == getPlotLineConnectionType();
	}

	public void setTimeAxisSetting(AxisOrientationSetting timeAxisSetting) {
		this.set(PlotConstants.TIME_AXIS_SETTING, timeAxisSetting);
	}

	public AxisOrientationSetting getTimeAxisSetting() {
		return this.get(PlotConstants.TIME_AXIS_SETTING, AxisOrientationSetting.class);
	}

	public void setTimeSystemSetting(String timeSystemSetting) {
		this.timeSystemSetting = timeSystemSetting;
	}

	public String getTimeSystemSetting() {
		return timeSystemSetting;
	}

	public void setTimeFormatSetting(String timeFormatSetting) {
		this.timeFormatSetting = timeFormatSetting;
	}

	public String getTimeFormatSetting() {
		return timeFormatSetting;
	}

	public void setXAxisMaximumLocation(XAxisMaximumLocationSetting xAxisMaximumLocation) {
		this.xAxisMaximumLocation = xAxisMaximumLocation;
	}

	public XAxisMaximumLocationSetting getXAxisMaximumLocation() {
		return xAxisMaximumLocation;
	}

	public void setYAxisMaximumLocation(YAxisMaximumLocationSetting yAxisMaximumLocation) {
		this.yAxisMaximumLocation = yAxisMaximumLocation;
	}

	public YAxisMaximumLocationSetting getYAxisMaximumLocation() {
		return yAxisMaximumLocation;
	}

	public void setTimeAxisSubsequent(TimeAxisSubsequentBoundsSetting timeAxisSubsequent) {
		this.timeAxisSubsequent = timeAxisSubsequent;
	}

	public TimeAxisSubsequentBoundsSetting getTimeAxisSubsequent() {
		return timeAxisSubsequent;
	}

	public void setNonTimeAxisSubsequentMinSetting(
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMinSetting) {
		this.nonTimeAxisSubsequentMinSetting = nonTimeAxisSubsequentMinSetting;
	}

	public NonTimeAxisSubsequentBoundsSetting getNonTimeAxisSubsequentMinSetting() {
		return nonTimeAxisSubsequentMinSetting;
	}

	public void setNonTimeAxisSubsequentMaxSetting(
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMaxSetting) {
		this.nonTimeAxisSubsequentMaxSetting = nonTimeAxisSubsequentMaxSetting;
	}

	public NonTimeAxisSubsequentBoundsSetting getNonTimeAxisSubsequentMaxSetting() {
		return nonTimeAxisSubsequentMaxSetting;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMaxNonTime(double maxNonTime) {
		this.maxNonTime = maxNonTime;
	}

	public double getMaxNonTime() {
		return maxNonTime;
	}

	public void setMinNonTime(double minNonTime) {
		this.minNonTime = minNonTime;
	}

	public double getMinNonTime() {
		return minNonTime;
	}

	public void setTimePadding(double timePadding) {
		this.timePadding = timePadding;
	}

	public double getTimePadding() {
		return timePadding;
	}

	public void setNonTimeMaxPadding(double nonTimeMaxPadding) {
		this.nonTimeMaxPadding = nonTimeMaxPadding;
	}

	public double getNonTimeMaxPadding() {
		return nonTimeMaxPadding;
	}

	public void setNonTimeMinPadding(double nonTimeMinPadding) {
		this.nonTimeMinPadding = nonTimeMinPadding;
	}

	public double getNonTimeMinPadding() {
		return nonTimeMinPadding;
	}

	public void setOrdinalPositionForStackedPlots(
			boolean ordinalPositionForStackedPlots) {
		this.ordinalPositionForStackedPlots = ordinalPositionForStackedPlots;
	}

	public boolean getOrdinalPositionForStackedPlots() {
		return ordinalPositionForStackedPlots;
	}

	public void setPinTimeAxis(boolean pinTimeAxis) {
		this.pinTimeAxis = pinTimeAxis;
	}

	public boolean getPinTimeAxis() {
		return pinTimeAxis;
	}

	public void setPlotLineDraw(PlotLineDrawingFlags plotLineDraw) {
		this.plotLineDraw = plotLineDraw;
	}

	public PlotLineDrawingFlags getPlotLineDraw() {
		return plotLineDraw;
	}

	public void setPlotLineConnectionType(PlotLineConnectionType plotLineConnectionType) {
		this.plotLineConnectionType = plotLineConnectionType;
	}

	public PlotLineConnectionType getPlotLineConnectionType() {
		return plotLineConnectionType;
	}
}