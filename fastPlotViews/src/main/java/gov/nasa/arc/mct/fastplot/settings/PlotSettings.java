package gov.nasa.arc.mct.fastplot.settings;

import java.util.GregorianCalendar;

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
	
	public boolean isNull() {
		return getTimeAxisSetting() == null;
	}
	
	private void createDefaults() {
		long now = new GregorianCalendar().getTimeInMillis();
		this.create(PlotConstants.TIME_AXIS_SETTING, AxisOrientationSetting.X_AXIS_AS_TIME, AxisOrientationSetting.class);
		this.create(PlotConstants.TIME_AXIS_SUBSEQUENT_SETTING, TimeAxisSubsequentBoundsSetting.JUMP, TimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.TIME_SYSTEM_SETTING, PlotConstants.DEFAULT_TIME_SYSTEM, String.class);
		this.create(PlotConstants.TIME_FORMAT_SETTING, PlotConstants.DEFAULT_TIME_FORMAT, String.class);
		this.create(PlotConstants.X_AXIS_MAXIMUM_LOCATION_SETTING, XAxisMaximumLocationSetting.MAXIMUM_AT_RIGHT, XAxisMaximumLocationSetting.class);
		this.create(PlotConstants.Y_AXIS_MAXIMUM_LOCATION_SETTING, PlotConstants.DEFAULT_Y_AXIS_MAX_LOCATION_SETTING, YAxisMaximumLocationSetting.class);
		this.create(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MIN_SETTING, PlotConstants.DEFAULT_NON_TIME_AXIS_MIN_SUBSEQUENT_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MAX_SETTING, PlotConstants.DEFAULT_NON_TIME_AXIS_MAX_SUBSEQUENT_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
		this.create(PlotConstants.TIME_MAX, now, Long.class);
		this.create(PlotConstants.TIME_MIN, now - PlotConstants.DEFAULT_PLOT_SPAN, Long.class);
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
		this.set(PlotConstants.TIME_SYSTEM_SETTING, timeSystemSetting);
	}

	public String getTimeSystemSetting() {
		return this.get(PlotConstants.TIME_FORMAT_SETTING, String.class);
	}

	public void setTimeFormatSetting(String timeFormatSetting) {
		this.set(PlotConstants.TIME_FORMAT_SETTING, timeFormatSetting);
	}

	public String getTimeFormatSetting() {
		return this.get(PlotConstants.TIME_FORMAT_SETTING, String.class);
	}

	public void setXAxisMaximumLocation(XAxisMaximumLocationSetting xAxisMaximumLocation) {
		this.set(PlotConstants.X_AXIS_MAXIMUM_LOCATION_SETTING, xAxisMaximumLocation);
	}

	public XAxisMaximumLocationSetting getXAxisMaximumLocation() {
		return this.get(PlotConstants.X_AXIS_MAXIMUM_LOCATION_SETTING, XAxisMaximumLocationSetting.class);
	}

	public void setYAxisMaximumLocation(YAxisMaximumLocationSetting yAxisMaximumLocation) {
		this.set(PlotConstants.Y_AXIS_MAXIMUM_LOCATION_SETTING, yAxisMaximumLocation);
	}

	public YAxisMaximumLocationSetting getYAxisMaximumLocation() {
		return this.get(PlotConstants.Y_AXIS_MAXIMUM_LOCATION_SETTING, YAxisMaximumLocationSetting.class);
	}

	public void setTimeAxisSubsequent(TimeAxisSubsequentBoundsSetting timeAxisSubsequent) {
		this.set(PlotConstants.TIME_AXIS_SUBSEQUENT_SETTING, timeAxisSubsequent);
	}

	public TimeAxisSubsequentBoundsSetting getTimeAxisSubsequent() {
		return this.get(PlotConstants.TIME_AXIS_SUBSEQUENT_SETTING, TimeAxisSubsequentBoundsSetting.class);
	}

	public void setNonTimeAxisSubsequentMinSetting(
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMinSetting) {
		this.set(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MIN_SETTING, nonTimeAxisSubsequentMinSetting);
	}

	public NonTimeAxisSubsequentBoundsSetting getNonTimeAxisSubsequentMinSetting() {
		return this.get(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MIN_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
	}

	public void setNonTimeAxisSubsequentMaxSetting(
			NonTimeAxisSubsequentBoundsSetting nonTimeAxisSubsequentMaxSetting) {
		this.set(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MAX_SETTING, nonTimeAxisSubsequentMaxSetting);
	}

	public NonTimeAxisSubsequentBoundsSetting getNonTimeAxisSubsequentMaxSetting() {
		return this.get(PlotConstants.NON_TIME_AXIS_SUBSEQUENT_MAX_SETTING, NonTimeAxisSubsequentBoundsSetting.class);
	}

	public void setMaxTime(long maxTime) {
		this.set(PlotConstants.TIME_MAX, (Long) maxTime);
	}

	public long getMaxTime() {
		return this.get(PlotConstants.TIME_MAX, Long.class);
	}

	public void setMinTime(long minTime) {
		this.set(PlotConstants.TIME_MIN, (Long) minTime);
	}

	public long getMinTime() {
		return this.get(PlotConstants.TIME_MIN, Long.class);
	}

	public void setMaxNonTime(double maxNonTime) {
		this.set(PlotConstants.NON_TIME_MAX, (Double) maxNonTime);
	}

	public double getMaxNonTime() {
		return this.get(PlotConstants.NON_TIME_MAX, Double.class);
	}

	public void setMinNonTime(double minNonTime) {
		this.set(PlotConstants.NON_TIME_MIN, (Double) minNonTime);
	}

	public double getMinNonTime() {
		return this.get(PlotConstants.NON_TIME_MIN, Double.class);
	}

	public void setTimePadding(double timePadding) {
		this.set(PlotConstants.TIME_PADDING, (Double) timePadding);
	}

	public double getTimePadding() {
		return this.get(PlotConstants.TIME_PADDING, Double.class);
	}

	public void setNonTimeMaxPadding(double nonTimeMaxPadding) {
		this.set(PlotConstants.NON_TIME_MAX_PADDING, (Double) nonTimeMaxPadding);
	}

	public double getNonTimeMaxPadding() {
		return this.get(PlotConstants.NON_TIME_MAX_PADDING, Double.class);
	}

	public void setNonTimeMinPadding(double nonTimeMinPadding) {
		this.set(PlotConstants.NON_TIME_MIN_PADDING, (Double) nonTimeMinPadding);
	}

	public double getNonTimeMinPadding() {
		return this.get(PlotConstants.NON_TIME_MIN_PADDING, Double.class);
	}

	public void setOrdinalPositionForStackedPlots(
			boolean ordinalPositionForStackedPlots) {
		this.set(PlotConstants.GROUP_BY_ORDINAL_POSITION, (Boolean) ordinalPositionForStackedPlots);
	}

	public boolean getOrdinalPositionForStackedPlots() {
		return this.get(PlotConstants.GROUP_BY_ORDINAL_POSITION, Boolean.class);
	}

	public void setPinTimeAxis(boolean pinTimeAxis) {
		this.set(PlotConstants.PIN_TIME_AXIS, (Boolean) pinTimeAxis);
	}

	public boolean getPinTimeAxis() {
		return this.get(PlotConstants.PIN_TIME_AXIS, Boolean.class);
	}

	public void setPlotLineDraw(PlotLineDrawingFlags plotLineDraw) {
		this.set(PlotConstants.DRAW_LINES, plotLineDraw.drawLine());
		this.set(PlotConstants.DRAW_MARKERS, plotLineDraw.drawMarkers());
	}

	public PlotLineDrawingFlags getPlotLineDraw() {
		return new PlotLineDrawingFlags(this.get(PlotConstants.DRAW_LINES,   Boolean.class), 
				                        this.get(PlotConstants.DRAW_MARKERS, Boolean.class)); 
	}

	public void setPlotLineConnectionType(PlotLineConnectionType plotLineConnectionType) {
		this.set(PlotConstants.CONNECTION_TYPE, plotLineConnectionType);
	}

	public PlotLineConnectionType getPlotLineConnectionType() {
		return this.get(PlotConstants.CONNECTION_TYPE, PlotLineConnectionType.class);
	}
}