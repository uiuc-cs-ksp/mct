package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.view.PlotViewManifestation;

import java.util.ArrayList;
import java.util.List;

public class PlotSettingsPanel extends PlotSettingsSubPanel {
	private static final long serialVersionUID = -7171700637608907253L;

	private List<PlotSettingsSubPanel> subPanels = new ArrayList<PlotSettingsSubPanel>();
	private PlotViewManifestation managedView;	
	
	private boolean dirty = false;
	private boolean valid = true;
	
	private final Runnable callback = new Runnable() {
		@Override
		public void run() {
			dirty = false;
			valid = true;
			for (PlotSettingsSubPanel subPanel : subPanels) {
				dirty |= subPanel.isDirty();
				valid &= subPanel.isValidated();
			}			
			fireCallbacks();
		}		
	};
	
	/**
	 * Add a sub panel which is used to formulate settings.
	 * Note that this does not add the subPanel visually
	 * @param subPanel
	 */
	public void addSubPanel(PlotSettingsSubPanel subPanel) {
		if (!subPanels.contains(subPanel)) {
			subPanels.add(subPanel);		
			subPanel.addCallback(callback);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.settings.PlotSettingsPopulator#populate(gov.nasa.arc.mct.fastplot.settings.PlotSettings)
	 */
	@Override
	public void populate(PlotSettings settings) {
		for (PlotSettingsSubPanel subPanel : subPanels) {
			subPanel.populate(settings);
		}
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.mct.fastplot.settings.PlotSettingsPopulator#reset(gov.nasa.arc.mct.fastplot.settings.PlotSettings)
	 */
	@Override
	public void reset(PlotSettings settings) {
		for (PlotSettingsSubPanel subPanel : subPanels) {
			subPanel.reset(settings);
		}
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	@Override
	public boolean isValidated() {
		return valid;
	}

 
}
