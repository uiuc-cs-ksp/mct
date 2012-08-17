package gov.nasa.arc.mct.fastplot.settings;


public interface PlotSettingsPopulator {
	public void populate(PlotConfiguration settings);
	public void reset(PlotConfiguration settings);
	public boolean isValid();
}
