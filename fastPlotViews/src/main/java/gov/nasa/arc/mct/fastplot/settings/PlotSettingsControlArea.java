package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsCheckBox;
import gov.nasa.arc.mct.fastplot.view.PlotViewManifestation;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PlotSettingsControlArea extends PlotSettingsPanel {
	private static final long serialVersionUID = -7171700637608907253L;

	private PlotViewManifestation managedView;	
	
	public PlotSettingsControlArea(PlotViewManifestation managedView) {
		this.managedView = managedView;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//TODO: Add the subpanels here
		add(new SectionPanel("test", new PlotSettingsCheckBox("test") {

			@Override
			public void populate(PlotSettings settings) {
				// TODO Auto-generated method stub
			}

			@Override
			public boolean getFrom(PlotSettings settings) {
				return false;
			}
			
		}));
		
		add(new SectionPanel("Plot Setup", new PlotSetupPanel()));
	}
	
	private class SectionPanel extends JPanel {
		private static final long serialVersionUID = -3867465087891823132L;

		public SectionPanel(String titleText, PlotSettingsSubPanel subPanel) {
			addSubPanel(subPanel);			
			
			setLayout  (new BorderLayout());
			setBorder(BorderFactory.createTitledBorder(titleText));
			add(subPanel, BorderLayout.WEST);//, gbc);
		}

	}

}
