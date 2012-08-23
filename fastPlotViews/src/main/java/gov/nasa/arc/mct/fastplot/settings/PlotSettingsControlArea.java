package gov.nasa.arc.mct.fastplot.settings;

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

		//add(new SectionPanel("Plot Setup", new PlotSetupPanel()));
		add(new SectionPanel("Plot Setup", new PlotSetupControlPanel(managedView)));
		//add(new SectionPanel("Plot Behavior When Space Runs Out", new PlotBehaviorPanel(managedView)));
		//add(new SectionPanel("Line Setup", new LineSetupPanel(managedView)));
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
