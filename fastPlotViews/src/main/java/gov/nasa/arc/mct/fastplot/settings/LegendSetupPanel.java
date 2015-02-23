package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsCheckBox;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JPanel;


/**
 * This class defines the UI for the Plot Configuration Panel
 */
public class LegendSetupPanel extends PlotSettingsPanel {
	private static final long serialVersionUID = -521875198747937122L;
	
	// Access bundle file where externalized strings are defined.
	private static final ResourceBundle BUNDLE = ResourceBundle
			.getBundle("gov.nasa.arc.mct.fastplot.view.Bundle");
	
	private static final int TITLE_SPACING = 0;
	
	private PlotSettingsCheckBox useLongNamesCheckBox;
	
	public LegendSetupPanel() {
		setLayout(new GridBagLayout());
		
		JPanel legendSetupPanel = new JPanel();
    	legendSetupPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, TITLE_SPACING));
    	useLongNamesCheckBox = new PlotSettingsCheckBox(BUNDLE.getString("UseLongNames.label")) {
			private static final long serialVersionUID = 2725784642673926438L;

			@Override
			public boolean getFrom(PlotConfiguration settings) {
				Boolean result = settings.getExtension(PlotConstants.LEGEND_USE_LONG_NAMES, Boolean.class);				
				return result != null ? result : false;
			}

			@Override
			public void populate(PlotConfiguration settings) {
				settings.setExtension(PlotConstants.LEGEND_USE_LONG_NAMES, isSelected());
			}
    	};
    	legendSetupPanel.add(useLongNamesCheckBox);
    	addSubPanel(useLongNamesCheckBox);
    	add(legendSetupPanel);
		
		for (Component c : getComponents()) {
			if (c instanceof AbstractButton) {
				((AbstractButton) c).addActionListener(this); // Trigger callbacks for any action
			}
		}
	}
	
	@Override
	public void populate(PlotConfiguration settings) {
		boolean enabled = useLongNamesCheckBox.isSelected();
		settings.setExtension(PlotConstants.LEGEND_USE_LONG_NAMES, enabled);
	}

	@Override
	public void reset(PlotConfiguration settings, boolean hard) {
		if (hard) {
			useLongNamesCheckBox.reset(settings, hard);		
		}
	}

	@Override
	public boolean isValidated() {
		return true;
	}
}
