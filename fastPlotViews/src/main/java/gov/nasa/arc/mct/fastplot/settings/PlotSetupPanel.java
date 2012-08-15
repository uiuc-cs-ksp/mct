package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.view.IconLoader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;

public class PlotSetupPanel extends PlotSettingsPanel {
	private static final long serialVersionUID = -7353890937891359595L;
	
	public PlotSetupPanel() {

		JPanel initialSetup = new JPanel();
		initialSetup.setLayout(new BoxLayout(initialSetup, BoxLayout.Y_AXIS));
		//initialSetup.setBorder(SETUP_AND_BEHAVIOR_MARGINS);

//		yAxisType = new JLabel("(" + BUNDLE.getString("NonTime.label") + ")");
        JPanel yAxisTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//        yAxisTypePanel.add(yAxisType);


        // Start defining the top panel
//		JPanel initTopPanel = createTopPanel();

        // Assemble the bottom panel
        JPanel initBottomPanel = new JPanel();
        initBottomPanel.setLayout(new GridBagLayout());
//		initBottomPanel.setBorder(TOP_PADDED_MARGINS);

//		JPanel yAxisPanelSet = createYAxisPanelSet();

//        JPanel xAxisPanelSet = createXAxisPanelSet();
        
        JPanel yAxisControlsPanel = new JPanel();
        yAxisControlsPanel.setLayout(new BoxLayout(yAxisControlsPanel, BoxLayout.Y_AXIS));

//        yAxisPanelSet.setAlignmentX(Component.CENTER_ALIGNMENT);
//        yAxisControlsPanel.add(yAxisPanelSet);

        JPanel xAxisControlsPanel = new JPanel(new GridLayout(1, 1));
//        xAxisControlsPanel.add(xAxisPanelSet);

        // The title label for (TIME) or (NON-TIME)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        initBottomPanel.add(yAxisTypePanel, gbc);

        // The Y Axis controls panel
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.gridwidth = 1;
        gbc1.gridheight = 3;
        gbc1.fill = GridBagConstraints.BOTH;
        // To align the "Min" or "Max" label with the bottom of the static plot image,
        // add a vertical shim under the Y Axis bottom button set and "Min"/"Max" label.
        gbc1.insets = new Insets(2, 0, 10, 2); 
        initBottomPanel.add(yAxisControlsPanel, gbc1);

        // The static plot image
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 1;
        gbc2.gridwidth = 3;
        gbc2.gridheight = 3;
        //initBottomPanel.add(imagePanel, gbc2);

        // The X Axis controls panel
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 1;
        gbc3.gridy = 4;
        gbc3.gridwidth = 3;
        gbc3.gridheight = 1;
        gbc3.fill = GridBagConstraints.BOTH;
        gbc3.insets = new Insets(0, 8, 0, 0);
        initBottomPanel.add(xAxisControlsPanel, gbc3);

		// Assemble the major panel: Initial Settings
        JPanel topClamp = new JPanel(new BorderLayout());
//        topClamp.add(initTopPanel, BorderLayout.NORTH);
        JPanel bottomClamp = new JPanel(new BorderLayout());
        bottomClamp.add(initBottomPanel, BorderLayout.NORTH);
        JPanel sideClamp = new JPanel(new BorderLayout());
        sideClamp.add(bottomClamp, BorderLayout.WEST);

//        initialSetup.add(Box.createRigidArea(new Dimension(0, INNER_PADDING)));
		initialSetup.add(topClamp);
//		initialSetup.add(Box.createRigidArea(new Dimension(0, INNER_PADDING)));
		initialSetup.add(new JSeparator());
        initialSetup.add(sideClamp);

        // Instrument
        initialSetup.setName("initialSetup");
//        initTopPanel.setName("initTopPanel");
        initBottomPanel.setName("initBottomPanel");
//        yAxisPanelSet.setName("yAxisPanelSet");
//        xAxisPanelSet.setName("xAxisPanelSet");
        yAxisControlsPanel.setName("yAxisInnerPanel");
        xAxisControlsPanel.setName("nontimeSidePanel");
        topClamp.setName("topClamp");
        bottomClamp.setName("bottomClamp");

        add(initialSetup);
        
		add (new AxisRangeSetupPanel());
	}
	
	private class AxisOrientationSetupPanel extends PlotSettingsPanel {
		private static final long serialVersionUID = -6683940104093335939L;
		
		public AxisOrientationSetupPanel() {
			
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
			xPanel     = new AxisRangeSetupSubPanel(Axis.X, AxisType.TIME);
			yPanel     = new AxisRangeSetupSubPanel(Axis.Y, AxisType.DEPENDENT);
			zPanel     = new AxisRangeSetupSubPanel(Axis.Z, AxisType.INDEPENDENT);
						
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
		
		private void setupMinMaxLabels(PlotSettings settings) {
			int xMin = 0;
			int yMin = 0;
			//TODO: Respond to settings
		}
	}
	
	
	
	private class AxisRangeSetupSubPanel extends PlotSettingsPanel {
		private Axis axis;
		private AxisType axisType;
		private JPanel minPanel;
		private JPanel maxPanel;
		private JPanel spanPanel;
		
		public AxisRangeSetupSubPanel(Axis axis, AxisType axisType) {
			this.axis = axis;
			add (new JLabel(axis.name()));
			
			setOpaque(false);
			
//		    yMaximumsPlusPanel = new YMaximumsPlusPanel();
//		    yAxisSpanPanel = new YAxisSpanPanel();
//		    yMinimumsPlusPanel = new YMinimumsPlusPanel();
//
//	        yAxisButtonsPanel = new YAxisButtonsPanel();
//	        yAxisButtonsPanel.insertMinMaxPanels(nonTimeAxisMinimumsPanel, nonTimeAxisMaximumsPanel);
//	        yAxisButtonsPanel.setNormalOrder(true);
//
//	        return yAxisButtonsPanel;
		}
		
		private boolean isTime(PlotSettings settings) {
			//TODO implement logic!
			return axis == Axis.X;
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
