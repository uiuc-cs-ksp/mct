package gov.nasa.arc.mct.fastplot.settings;


public interface PlotSettingsPopulator {
	public void populate(PlotSettings settings);
	public void reset(PlotSettings settings);
	public boolean isValid();
}
