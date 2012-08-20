package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsCheckBox;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsComboBox;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsRadioButtonGroup;
import gov.nasa.arc.mct.fastplot.view.IconLoader;
import gov.nasa.arc.mct.services.activity.TimeService;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class PlotSetupPanel extends PlotSettingsPanel {
	private static final long serialVersionUID = -7353890937891359595L;
	
	public PlotSetupPanel() {
		setLayout(new GridBagLayout());
		
        add (new AxisOrientationSetupPanel(),           0, 0,       GridBagConstraints.NONE);
        add (new JSeparator(SwingConstants.VERTICAL),   1, 0,       GridBagConstraints.VERTICAL);
        add (new AxisMinMaxPanel("X"),                  2, 0,       GridBagConstraints.NONE);
        add (new JSeparator(SwingConstants.VERTICAL),   3, 0,       GridBagConstraints.VERTICAL);
        add (new AxisMinMaxPanel("Y"),                  4, 0,       GridBagConstraints.NONE);
        add (new JSeparator(SwingConstants.VERTICAL),   5, 0,       GridBagConstraints.VERTICAL);
        add (new SubPlotGroupingPanel(),                6, 0,       GridBagConstraints.NONE);
        add (new JSeparator(SwingConstants.HORIZONTAL), 0, 1, 7, 1, GridBagConstraints.HORIZONTAL);        
		add (new AxisRangeSetupPanel(),                 0, 2, 7, 1, GridBagConstraints.NONE);
	}
	
	private GridBagConstraints gbc = new GridBagConstraints();
	private void add (JComponent component, int x, int y, int fill) {
		add(component, x, y, 1, 1, fill);		
	}
	private void add (JComponent component, int x, int y, int w, int h, int fill) {		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.fill = fill;
		gbc.anchor = GridBagConstraints.NORTH;
		add(component, gbc);
		component.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		if (component instanceof PlotSettingsSubPanel) {
			addSubPanel((PlotSettingsSubPanel) component);
		}
	}
	
	private class AxisOrientationSetupPanel extends PlotSettingsPanel {
		private static final long serialVersionUID = -6683940104093335939L;
		
		public AxisOrientationSetupPanel() {
			// Time Systems and Formats			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			
			JPanel timeSystemPanel = new JPanel();
			timeSystemPanel.setLayout(new BoxLayout(timeSystemPanel, BoxLayout.X_AXIS)); 
			timeSystemPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0));

			JPanel timeFormatsPanel = new JPanel();
			timeFormatsPanel.setLayout(new BoxLayout(timeFormatsPanel, BoxLayout.X_AXIS)); 
			timeFormatsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0));
					
			timeSystemPanel.add(new JLabel("Time System:"));
			timeSystemPanel.add(new PlotSettingsComboBox<String>("GMT") {
				@Override
				public void populate(PlotConfiguration settings) {}

				@Override
				public void reset(PlotConfiguration settings) {}				
			});
			
			timeFormatsPanel.add(new JLabel("Time Format:"));
			timeFormatsPanel.add(new PlotSettingsComboBox<String>(TimeService.DEFAULT_TIME_FORMAT) {
				@Override
				public void populate(PlotConfiguration settings) {}
				@Override
				public void reset(PlotConfiguration settings) {}				
			}); //TODO: getComponentSpecifiedTimeFormatChoices
			
			add(timeSystemPanel);
			add(timeFormatsPanel);			
			add(new PlotSettingsRadioButtonGroup<String>("X-Axis as Time", "Y-Axis as Time") {
				@Override
				public void populate(PlotConfiguration settings) {}
				@Override
				public void reset(PlotConfiguration settings) {}
			});					
		}
	}
	
	private class AxisMinMaxPanel extends PlotSettingsPanel {
		public AxisMinMaxPanel(String axis) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			add (new JLabel(axis + "-Axis:"), gbc);
			gbc.gridy ++;
			add (Box.createVerticalStrut(20), gbc);
			gbc.gridy ++;
			gbc.gridheight = 2;
			add (new PlotSettingsRadioButtonGroup<String>("Max at left", "Max at right") {

				@Override
				public void populate(PlotConfiguration settings) {}

				@Override
				public void reset(PlotConfiguration settings) {}
				
			}, gbc);
			
		}
	}
	
	private class SubPlotGroupingPanel extends PlotSettingsPanel {
		public SubPlotGroupingPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			add (new JLabel("Stacked Plot Grouping:"), gbc);
			gbc.gridy++;
			add (new PlotSettingsCheckBox("Separate collections in separate plots") {

				@Override
				public void populate(PlotConfiguration settings) {
					settings.setOrdinalPositionForStackedPlots(!isSelected());
				}

				@Override
				public boolean getFrom(PlotConfiguration settings) {
					return !settings.getOrdinalPositionForStackedPlots();
				}
				
			}, gbc);
			
		}
	}
	
	private class AxisRangeSetupPanel extends PlotSettingsPanel {
		private static final long serialVersionUID = 7462800421417982477L;

		private StillPlotImagePanel    imagePanel;
		private JPanel                 xPanel, yPanel, zPanel;
		private AxisRangeSetupSubPanel axisRangeSetupPanel[] = new AxisRangeSetupSubPanel[AxisType.values().length];
		private JLabel                 xLabel[] = { new JLabel("Min"), new JLabel("Max") };
		private JLabel                 yLabel[] = { new JLabel("Min"), new JLabel("Max") };
		
		public AxisRangeSetupPanel() {
			imagePanel = new StillPlotImagePanel();
			xPanel     = new JPanel(); //AxisRangeSetupSubPanel(Axis.X, AxisType.TIME);
			yPanel     = new JPanel(); //new AxisRangeSetupSubPanel(Axis.Y, AxisType.DEPENDENT);
			zPanel     = new JPanel(); //new AxisRangeSetupSubPanel(Axis.Z, AxisType.INDEPENDENT);
						
			//addSubPanel(imagePanel);
			Axis[] axes = Axis.values();
			for (AxisType axisType : AxisType.values()) {
				axisRangeSetupPanel[axisType.ordinal()] = 
					new AxisRangeSetupSubPanel(axes[axisType.ordinal() % axes.length], axisType);
				addSubPanel(axisRangeSetupPanel[axisType.ordinal()]);
			}
			
			add(imagePanel);
			add(xPanel);
			add(yPanel);
			add(zPanel);
			add(xLabel[0]);
			add(xLabel[1]);
			add(yLabel[0]);
			add(yLabel[1]);
			
			xPanel.add(axisRangeSetupPanel[0]);
			yPanel.add(axisRangeSetupPanel[1]);
			zPanel.add(axisRangeSetupPanel[2]);
			
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			layout.putConstraint(SpringLayout.WEST, yPanel, 0, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, yPanel, 0, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, imagePanel, 0, SpringLayout.EAST, yPanel);
			layout.putConstraint(SpringLayout.NORTH, imagePanel, 0, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, zPanel, 0, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.EAST, zPanel, 0, SpringLayout.WEST, imagePanel);
			layout.putConstraint(SpringLayout.SOUTH, zPanel, 0, SpringLayout.SOUTH, this);
			layout.putConstraint(SpringLayout.NORTH, zPanel, 0, SpringLayout.SOUTH, imagePanel);

			layout.putConstraint(SpringLayout.WEST, xPanel, 0, SpringLayout.WEST, imagePanel);
			layout.putConstraint(SpringLayout.NORTH, xPanel, 0, SpringLayout.SOUTH, imagePanel);
			layout.putConstraint(SpringLayout.EAST, xPanel, 0, SpringLayout.EAST, imagePanel);
			
			layout.putConstraint(SpringLayout.WEST, xLabel[0], 0, SpringLayout.WEST, imagePanel);
			layout.putConstraint(SpringLayout.NORTH, xLabel[0], 0, SpringLayout.SOUTH, imagePanel);
			layout.putConstraint(SpringLayout.EAST, xLabel[1], 0, SpringLayout.EAST, imagePanel);
			layout.putConstraint(SpringLayout.NORTH, xLabel[1], 0, SpringLayout.SOUTH, imagePanel);

			layout.putConstraint(SpringLayout.EAST, yLabel[0], 0, SpringLayout.WEST, imagePanel);
			layout.putConstraint(SpringLayout.SOUTH, yLabel[0], 0, SpringLayout.SOUTH, imagePanel);
			layout.putConstraint(SpringLayout.EAST, yLabel[1], 0, SpringLayout.WEST, imagePanel);
			layout.putConstraint(SpringLayout.NORTH, yLabel[1], 0, SpringLayout.NORTH, imagePanel);		
			
			layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, imagePanel);
			layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, xPanel);
		}		
		
		private void setupMinMaxLabels(PlotConfiguration settings) {
			int xMin = 0;
			int yMin = 0;
			//TODO: Respond to settings
		}
	}
	
	
	private enum BoundMode { CURRENT, MANUAL, RELATIVE }
	
	private class AxisRangeSetupSubPanel extends PlotSettingsPanel {
		
		
		private Axis axis;
		private AxisType axisType;
		private JPanel subPanel[] = { new JPanel(), new JPanel() }; // To swap min/max
		private PlotSettingsSubPanel minPanel;
		private PlotSettingsSubPanel maxPanel;
		private JPanel spanPanel = new JPanel();
		private JLabel axisLabel = new JLabel();
		
		public AxisRangeSetupSubPanel(Axis axis, AxisType axisType) {
			this.axis = axis;
			setOpaque(false);
			
			minPanel = new PlotSettingsRadioButtonGroup<String>("Min A", "Min B", "Min C") {
				@Override
				public void populate(PlotConfiguration settings) {}
				@Override
				public void reset(PlotConfiguration settings) {}				
			};
			
			maxPanel = new PlotSettingsRadioButtonGroup<String>("Max A", "Max B", "Max C") {
				@Override
				public void populate(PlotConfiguration settings) {}
				@Override
				public void reset(PlotConfiguration settings) {}				
			};
			
			
			add(subPanel[0]);
			add(spanPanel);
			add(subPanel[1]);
			//add(axisLabel);
			
			addSubPanel(minPanel);
			addSubPanel(maxPanel);
			
			subPanel[0].add(minPanel);
			subPanel[1].add(maxPanel);
			spanPanel  .add(new JLabel("Span panel"));
			
			setupLayout();
		}
		
		private void setupLayout() {
			SimpleSpringLayout layout = new SimpleSpringLayout();
			setLayout(layout);
			
			switch (axis) {
			case X:
			case Y:
			case Z:
				layout.putConstraint(SpringLayout.WEST, subPanel[0], this);
				layout.putConstraint(SpringLayout.SOUTH, subPanel[0], this);
				
				layout.putConstraint(SpringLayout.WEST, subPanel[1], subPanel[0]);
				layout.putConstraint(SpringLayout.SOUTH, subPanel[1], subPanel[0]);				
				layout.putConstraint(SpringLayout.NORTH, subPanel[1], subPanel[0]);
				
				layout.putConstraint(SpringLayout.SOUTH, spanPanel, subPanel[0]);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spanPanel, this);
				
				layout.putConstraint(SpringLayout.EAST, this, subPanel[1]);
				layout.putConstraint(SpringLayout.NORTH, this, spanPanel);
				break;
//			case Y:
//				layout.putConstraint(SpringLayout.WEST, subPanel[0], this);
//				layout.putConstraint(SpringLayout.NORTH, subPanel[0], this);
//				
//				layout.putConstraint(SpringLayout.WEST, subPanel[1], this);
//				layout.putConstraint(SpringLayout.SOUTH, subPanel[1], this);				
//				
//				layout.putConstraint(SpringLayout.VERTICAL_CENTER, spanPanel, this);
//				layout.putConstraint(SpringLayout.EAST, spanPanel, this);
//				
//				break;
//			case Z:
//				layout.putConstraint(SpringLayout.WEST, subPanel[0], this);
//				layout.putConstraint(SpringLayout.SOUTH, subPanel[0], this);
//				
//				layout.putConstraint(SpringLayout.WEST, subPanel[1], subPanel[0]);
//				layout.putConstraint(SpringLayout.SOUTH, subPanel[1], subPanel[0]);				
//				layout.putConstraint(SpringLayout.NORTH, subPanel[1], subPanel[0]);
//				
//				layout.putConstraint(SpringLayout.SOUTH, spanPanel, subPanel[0]);
//				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spanPanel, this);
//				
//				layout.putConstraint(SpringLayout.EAST, this, subPanel[1]);
//				layout.putConstraint(SpringLayout.NORTH, this, spanPanel);
//				break;				
			}
		}
		
		private boolean isTime(PlotConfiguration settings) {
			//TODO implement logic!
			return axis == Axis.X;
		}
		
		private class SimpleSpringLayout extends SpringLayout {
			public void putConstraint(String edge, JComponent comp, JComponent ref) {
				String otherEdge = edge;
				if (ref != AxisRangeSetupSubPanel.this && comp != AxisRangeSetupSubPanel.this) {
					if (edge.equals(SpringLayout.WEST )) otherEdge = SpringLayout.EAST;
					if (edge.equals(SpringLayout.EAST )) otherEdge = SpringLayout.WEST;
					if (edge.equals(SpringLayout.NORTH)) otherEdge = SpringLayout.SOUTH;
					if (edge.equals(SpringLayout.SOUTH)) otherEdge = SpringLayout.NORTH;
				}
				putConstraint(edge, comp, 0, otherEdge, ref);
			}
		}
	}
	
	
	
	// Panel that holds the still image of a plot in the Initial Settings area
    private class StillPlotImagePanel extends JPanel {
		private static final long serialVersionUID = 8645833372400367908L;
		private JLabel timeOnXAxisNormalPicture;
		private JLabel timeOnYAxisNormalPicture;
		private JLabel timeOnXAxisReversedPicture;
		private JLabel timeOnYAxisReversedPicture;

		public StillPlotImagePanel() {
			timeOnXAxisNormalPicture = new JLabel("", IconLoader.INSTANCE.getIcon(IconLoader.Icons.PLOT_TIME_ON_X_NORMAL), JLabel.CENTER);
			timeOnYAxisNormalPicture = new JLabel("", IconLoader.INSTANCE.getIcon(IconLoader.Icons.PLOT_TIME_ON_Y_NORMAL), JLabel.CENTER);
			timeOnXAxisReversedPicture = new JLabel("", IconLoader.INSTANCE.getIcon(IconLoader.Icons.PLOT_TIME_ON_X_REVERSED), JLabel.CENTER);
			timeOnYAxisReversedPicture = new JLabel("", IconLoader.INSTANCE.getIcon(IconLoader.Icons.PLOT_TIME_ON_Y_REVERSED), JLabel.CENTER);
			add(timeOnXAxisNormalPicture); // default

			// Instrument
			timeOnXAxisNormalPicture.setName("timeOnXAxisNormalPicture");
			timeOnYAxisNormalPicture.setName("timeOnYAxisNormalPicture");
			timeOnXAxisReversedPicture.setName("timeOnXAxisReversedPicture");
			timeOnYAxisReversedPicture.setName("timeOnYAxisReversedPicture");
		}

		public void setImageToTimeOnXAxis(boolean normalDirection) {
			removeAll();
			if (normalDirection) {
				add(timeOnXAxisNormalPicture);
			} else {
				add(timeOnXAxisReversedPicture);
			}
			revalidate();
		}

		public void setImageToTimeOnYAxis(boolean normalDirection) {
			removeAll();
			if (normalDirection) {
				add(timeOnYAxisNormalPicture);
			} else {
				add(timeOnYAxisReversedPicture);
			}
			revalidate();
		}
	}

    private enum Axis     { X, Y, Z };
    private enum AxisType { TIME, INDEPENDENT, DEPENDENT };
}
