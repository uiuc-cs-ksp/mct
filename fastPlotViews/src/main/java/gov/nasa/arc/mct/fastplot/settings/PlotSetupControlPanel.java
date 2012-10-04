/*******************************************************************************
 * Mission Control Technologies, Copyright (c) 2009-2012, United States Government
 * as represented by the Administrator of the National Aeronautics and Space 
 * Administration. All rights reserved.
 *
 * The MCT platform is licensed under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 *
 * MCT includes source code licensed under additional open source licenses. See 
 * the MCT Open Source Licenses file included with this distribution or the About 
 * MCT Licenses dialog available at runtime from the MCT Help menu for additional 
 * information. 
 *******************************************************************************/
package gov.nasa.arc.mct.fastplot.settings;

import gov.nasa.arc.mct.fastplot.bridge.PlotConstants;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.AxisOrientationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.XAxisMaximumLocationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.YAxisMaximumLocationSetting;
import gov.nasa.arc.mct.fastplot.bridge.PlotterPlot;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsCheckBox;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsComboBox;
import gov.nasa.arc.mct.fastplot.settings.controls.PlotSettingsRadioButtonGroup;
import gov.nasa.arc.mct.fastplot.view.IconLoader;
import gov.nasa.arc.mct.fastplot.view.NumericTextField;
import gov.nasa.arc.mct.fastplot.view.PlotViewManifestation;
import gov.nasa.arc.mct.fastplot.view.TimeDuration;
import gov.nasa.arc.mct.fastplot.view.TimeSpanTextField;
import gov.nasa.arc.mct.fastplot.view.TimeTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines the UI for the Plot Configuration Panel
 */

public class PlotSetupControlPanel extends PlotSettingsPanel {
	private static final long serialVersionUID = 6158825155688815494L;
	
	// Access bundle file where externalized strings are defined.
	private static final ResourceBundle BUNDLE = 
                               ResourceBundle.getBundle("gov.nasa.arc.mct.fastplot.view.Bundle");
    
    private final static Logger logger = LoggerFactory.getLogger(PlotSetupControlPanel.class);

	private static final String MANUAL_LABEL = BUNDLE.getString("Manual.label");

    private static final int INTERCONTROL_HORIZONTAL_SPACING = 0; 
	private static final int Y_SPAN_SPACING = 100;


    private static final int INNER_PADDING = 5;
    private static final int INNER_PADDING_TOP = 5;

    private static final int X_AXIS_TYPE_VERTICAL_SPACING = 2;
	private static final Border TOP_PADDED_MARGINS = BorderFactory.createEmptyBorder(5, 0, 0, 0);
	private static final Border BOTTOM_PADDED_MARGINS = BorderFactory.createEmptyBorder(0, 0, 2, 0);

	private static final Border CONTROL_PANEL_MARGINS = BorderFactory.createEmptyBorder(INNER_PADDING_TOP, INNER_PADDING, INNER_PADDING, INNER_PADDING);
	private static final Border SETUP_AND_BEHAVIOR_MARGINS = BorderFactory.createEmptyBorder(0, INNER_PADDING, INNER_PADDING, INNER_PADDING);

	// Stabilize width of panel on left of the static plot image
	private static final Dimension Y_AXIS_BUTTONS_PANEL_PREFERRED_SIZE = new Dimension(350, 0);

	private static final Double NONTIME_AXIS_SPAN_INIT_VALUE = Double.valueOf(30);

	private static final int JTEXTFIELD_COLS = 8;
	private static final int NUMERIC_TEXTFIELD_COLS1 = 12;

	private static final DecimalFormat PARENTHESIZED_LABEL_FORMAT = new DecimalFormat("###.###");
	
	private static final String DATE_FORMAT = "D/HH:mm:ss";

	private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	static GregorianCalendar ZERO_TIME_SPAN_CALENDAR = new GregorianCalendar();
	static {
		ZERO_TIME_SPAN_CALENDAR.set(Calendar.DAY_OF_YEAR, 1);
		ZERO_TIME_SPAN_CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
		ZERO_TIME_SPAN_CALENDAR.set(Calendar.MINUTE, 0);
		ZERO_TIME_SPAN_CALENDAR.set(Calendar.SECOND, 0);
	}
	private static Date ZERO_TIME_SPAN_DATE = new Date();
	static {
		// Sets value to current Year and time zone
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		ZERO_TIME_SPAN_DATE.setTime(cal.getTimeInMillis());
	}

	// Space between paired controls (e.g., label followed by text field)
	private static final int SPACE_BETWEEN_ROW_ITEMS = 3;


	// Maintain link to the plot view this panel is supporting.
	private PlotViewManifestation plotViewManifestation;
	
	// Non-Time Axis Maximums panel
	NonTimeAxisMaximumsPanel nonTimeAxisMaximumsPanel;

	// Non-Time Axis Minimums panel
	NonTimeAxisMinimumsPanel nonTimeAxisMinimumsPanel;

	//Time Axis Minimums panel
	public TimeAxisMinimumsPanel timeAxisMinimumsPanel;

	//Time Axis Maximums panel
	public TimeAxisMaximumsPanel timeAxisMaximumsPanel;

	// Top panel controls: Manipulate controls around static plot image

	JRadioButton xAxisAsTimeRadioButton;
    JRadioButton yAxisAsTimeRadioButton;
    JRadioButton zAxisAsTimeRadioButton;

   
	//=========================================================================
	/*
	 * X Axis panels
	 */
	XAxisSpanCluster xAxisSpanCluster;

	XAxisAdjacentPanel xAxisAdjacentPanel;



    JLabel xMinLabel;
    JLabel xMaxLabel;


	//=========================================================================
	/*
	 * Y Axis panels
	 */
    YMaximumsPlusPanel yMaximumsPlusPanel;
    YAxisSpanPanel yAxisSpanPanel;
    YMinimumsPlusPanel yMinimumsPlusPanel;

	/*
	 * Time Axis fields
	 */
    JRadioButton timeAxisMaxAuto;
    ParenthesizedTimeLabel timeAxisMaxAutoValue;
    JRadioButton timeAxisMaxManual;
    TimeTextField timeAxisMaxManualValue;

    JRadioButton timeAxisMinAuto;
    ParenthesizedTimeLabel timeAxisMinAutoValue;
    JRadioButton timeAxisMinManual;
    TimeTextField timeAxisMinManualValue;
    
    TimeSpanTextField timeSpanValue;

	public JRadioButton timeAxisMinCurrent;
	public JRadioButton timeAxisMaxCurrent;
	public ParenthesizedTimeLabel timeAxisMinCurrentValue;
	public ParenthesizedTimeLabel timeAxisMaxCurrentValue;
	
	JComboBox timeAxisMinYear;
	JComboBox timeAxisMaxYear;

    /*
     * Non-time Axis fields
     */
    JRadioButton nonTimeAxisMaxCurrent;
    ParenthesizedNumericLabel nonTimeAxisMaxCurrentValue;
    JRadioButton nonTimeAxisMaxManual;
    NumericTextField nonTimeAxisMaxManualValue;
    JRadioButton nonTimeAxisMaxAutoAdjust;
    ParenthesizedNumericLabel nonTimeAxisMaxAutoAdjustValue;

    JRadioButton nonTimeAxisMinCurrent;
    ParenthesizedNumericLabel nonTimeAxisMinCurrentValue;
    JRadioButton nonTimeAxisMinManual;
    NumericTextField nonTimeAxisMinManualValue;
    JRadioButton nonTimeAxisMinAutoAdjust;
	ParenthesizedNumericLabel nonTimeAxisMinAutoAdjustValue;

    NumericTextField nonTimeSpanValue;
	
	private StillPlotImagePanel imagePanel;

	private boolean timeMinManualHasBeenSelected = false;
	private boolean timeMaxManualHasBeenSelected = false;
	
	class ParenthesizedTimeLabel extends JLabel {
		private static final long serialVersionUID = -6004293775277749905L;

		private GregorianCalendar timeInMillis;

		private JRadioButton companionButton;
		
		public ParenthesizedTimeLabel(JRadioButton button) {
						
			companionButton = button;
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					companionButton.setSelected(true);
					companionButton.requestFocusInWindow();
					//TODO : Fire callbacks!
				}
			});
		}

		public void setTime(GregorianCalendar inputTime) {
			timeInMillis = inputTime;
			setText("(" + "???" + " "
					+ inputTime.get(Calendar.YEAR) + ")");		
		}

		public GregorianCalendar getCalendar() {
			return timeInMillis;
		}

		public long getTimeInMillis() {
			return timeInMillis.getTimeInMillis();
		}

		public int getSecond() {
			return timeInMillis.get(Calendar.SECOND);
		}

		public int getMinute() {
			return timeInMillis.get(Calendar.MINUTE);
		}

		public int getHourOfDay() {
			return timeInMillis.get(Calendar.HOUR_OF_DAY);
		}
	}

	class ParenthesizedNumericLabel extends JLabel {
		private static final long serialVersionUID = 3403375470853249483L;
		private JRadioButton companionButton;

		public ParenthesizedNumericLabel(JRadioButton button) {
			
			companionButton = button;
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					companionButton.setSelected(true);
					companionButton.requestFocusInWindow();
					//TODO: Fire callbacks!
				}
			});
		}

		public Double getValue() {
			String data = getText();
			if (data == null) {
				return null;
			}
			if (data.length() < 3) {
				logger.error("Numeric label in plot settings contained invalid content [" + data + "]");
				return null;
			}
			Double result = null;
			try {
				result = Double.parseDouble(data.substring(1, data.length() - 1));
				
			} catch(NumberFormatException e) {
				logger.error("Could not parse numeric value from ["+ data.substring(1, data.length() - 1) + "]");
			}
			return result;
		}

		public void setValue(Double input) {
			
			String formatNum = PARENTHESIZED_LABEL_FORMAT.format(input);
			
			if ( (input.doubleValue() >= PlotConstants.MILLION_VALUES) || (input.doubleValue() <= PlotConstants.NEGATIVE_MILLION_VALUES) ) {
                formatNum = PlotterPlot.getNumberFormatter(input.doubleValue()).format(input.doubleValue());
			}

			if (formatNum.equals("0"))
				formatNum = "0.0";
			
			if (formatNum.equals("1"))
				formatNum = "1.0";
			
			setText("(" + formatNum + ")");
		}
	}

	/*
	 * Focus listener for the Time axis Manual and Span fields
	 */
	class TimeFieldFocusListener extends FocusAdapter {
		// This class can be used with a null button
		private JRadioButton companionButton;

		public TimeFieldFocusListener(JRadioButton assocButton) {			
			companionButton = assocButton;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (e.isTemporary())
				return;
			if (companionButton != null) {
				companionButton.setSelected(true);
			}
			PlotSetupControlPanel.this.focusGained(e);
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (e.isTemporary())
				return;
			try {
				timeSpanValue.commitEdit(); 
			} catch (ParseException exception) {
				exception.printStackTrace();
			}

			//updateTimeAxisControls();
		}
	}

	/*
	 * Common action listener for the Time axis Mode buttons
	 */
	class TimeAxisModeListener implements ActionListener {
		private JTextComponent companionField;

		public TimeAxisModeListener(JTextComponent field) {
			companionField = field;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.actionPerformed(e);
			String content = companionField.getText();
			companionField.requestFocusInWindow();
			companionField.setSelectionStart(0);
			if (content != null) {
				companionField.setSelectionEnd(content.length());
			}
		}
	}


	class NonTimeFieldFocusListener extends FocusAdapter {
		private JRadioButton companionButton;

		public NonTimeFieldFocusListener(JRadioButton assocButton) {
			companionButton = assocButton;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (e.isTemporary())
				return;
			companionButton.setSelected(true);
			updateNonTimeAxisControls();
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (e.isTemporary())
				return;

			updateNonTimeAxisControls();
		}
	}

	/*
	 * Guide to the inner classes implementing the movable panels next to the static plot image
	 * 
	 * XAxisAdjacentPanel - Narrow panel just below X axis
	 *     XAxisSpanCluster - child panel
	 * XAxisButtonsPanel - Main panel below X axis
	 *     minimumsPanel - child
	 *     maximumsPanel - child
	 * 
	 * YAxisButtonsPanel - Main panel to left of Y axis
	 *     YMaximumsPlusPanel - child panel
	 *     YSpanPanel - child
	 *     YMinimumsPlusPanel - child
	 *
	 */


	// Panel holding the Y Axis Span controls
	class YAxisSpanPanel extends JPanel {
		private static final long serialVersionUID = 6888092349514542052L;
		private JLabel ySpanTag;
		private JComponent spanValue;
		private Component boxGlue;
		private Component boxOnRight;

		public YAxisSpanPanel() {
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			
			nonTimeSpanValue = new NumericTextField(NUMERIC_TEXTFIELD_COLS1, PARENTHESIZED_LABEL_FORMAT);
			nonTimeSpanValue.setColumns(JTEXTFIELD_COLS);
			nonTimeSpanValue.setValue(NONTIME_AXIS_SPAN_INIT_VALUE);

	        spanValue = nonTimeSpanValue;

	        ySpanTag = new JLabel(BUNDLE.getString("SpanTextField.label"));
	        boxGlue = Box.createHorizontalGlue();
	        boxOnRight = Box.createRigidArea(new Dimension(Y_SPAN_SPACING, 0));

	        layoutPanel();

	        // Instrument
	        ySpanTag.setName("ySpanTag");
		}

		void layoutPanel() {
			removeAll();
			add(boxGlue);
	        add(ySpanTag);
            add(spanValue);
            if (spanValue instanceof TimeSpanTextField) {
	            add(Box.createHorizontalStrut(INTERCONTROL_HORIZONTAL_SPACING + 3));
	            add(new JLabel(BUNDLE.getString("YearSpan")));
	            add(Box.createHorizontalStrut(INTERCONTROL_HORIZONTAL_SPACING + 3));
	            add(timeSpanValue.getYearSpanValue());
            } else {
    	        //add(boxOnRight);
            }
		}

		void setSpanField(JComponent field) {
			spanValue = field;
			layoutPanel();
		}
	}

	// Panel holding the combined "Min" label and Y Axis minimums panel
	class YMinimumsPlusPanel extends JPanel {
		private static final long serialVersionUID = 2995723041366974233L;

		private JPanel coreMinimumsPanel;
		private JLabel minJLabel = new JLabel("Min");

		public YMinimumsPlusPanel() {
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        minJLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            minJLabel.setFont(minJLabel.getFont().deriveFont(Font.BOLD));

//            nonTimeAxisMinimumsPanel = new NonTimeAxisMinimumsPanel();
//            nonTimeAxisMinimumsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            minJLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

            add(nonTimeAxisMinimumsPanel);
	        add(minJLabel);

	        // Instrument
	        minJLabel.setName("minJLabel");
		}

		public void setPanel(JPanel minPanel) {
			coreMinimumsPanel = minPanel;
			removeAll();
	        add(coreMinimumsPanel);
	        add(minJLabel);
	        revalidate();
		}

		public void setAxisTagAlignment(float componentAlignment) {
			coreMinimumsPanel.setAlignmentY(componentAlignment);
			minJLabel.setAlignmentY(componentAlignment);
		}
	}

	// Panel holding the combined "Max" label and Y Axis maximums panel
	class YMaximumsPlusPanel extends JPanel {
		private static final long serialVersionUID = -7611052255395258026L;

		private JPanel coreMaximumsPanel;
		private JLabel maxJLabel = new JLabel("Max");

		public YMaximumsPlusPanel() {
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        maxJLabel.setAlignmentY(Component.TOP_ALIGNMENT);
	        maxJLabel.setFont(maxJLabel.getFont().deriveFont(Font.BOLD));

	        nonTimeAxisMaximumsPanel = new NonTimeAxisMaximumsPanel();
	        nonTimeAxisMaximumsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
	        maxJLabel.setAlignmentY(Component.TOP_ALIGNMENT);
	        add(nonTimeAxisMaximumsPanel);
	        add(maxJLabel);

	        // Instrument
	        maxJLabel.setName("maxJLabel");
		}

		public void setPanel(JPanel maxPanel) {
			coreMaximumsPanel = maxPanel;
			removeAll();
	        add(coreMaximumsPanel);
	        add(maxJLabel);
	        revalidate();
		}

		public void setAxisTagAlignment(float componentAlignment) {
			coreMaximumsPanel.setAlignmentY(componentAlignment);
			maxJLabel.setAlignmentY(componentAlignment);
			revalidate();
		}
	}

	// Panel holding the Time Axis minimum controls
	class TimeAxisMinimumsPanel extends JPanel {
		private static final long serialVersionUID = 3651502189841560982L;

		public TimeAxisMinimumsPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			timeAxisMinCurrent = new JRadioButton(BUNDLE.getString("Currentmin.label"));
			timeAxisMinCurrentValue = new ParenthesizedTimeLabel(timeAxisMinCurrent);
			GregorianCalendar calendar = new GregorianCalendar();
			timeAxisMinCurrentValue.setTime(calendar);
			Integer[] years = new Integer[10];
			for (int i = 0 ; i < 10; i++ ) {
				years[i] = new Integer(calendar.get(Calendar.YEAR) - i);
			}
			timeAxisMinYear = new JComboBox(years);
			timeAxisMinYear.setEditable(true);

		    timeAxisMinManual = new JRadioButton(MANUAL_LABEL);
	        MaskFormatter formatter = null;
			try {
				formatter = new MaskFormatter("###/##:##:##");
				formatter.setPlaceholderCharacter('0');
			} catch (ParseException e) {
				logger.error("Parse error in creating time field", e);
			}

		    timeAxisMinManualValue = new TimeTextField(formatter, calendar.get(Calendar.YEAR));
		    timeAxisMinYear.setPreferredSize(new Dimension(60,timeAxisMinManualValue.getPreferredSize().height - 1));
		    timeAxisMinYear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timeAxisMinManualValue.setYear(((Integer) ((JComboBox) e.getSource()).getSelectedItem()).intValue());
				}
				
			});

		    timeAxisMinAuto = new JRadioButton(BUNDLE.getString("Now.label"));
		    timeAxisMinAutoValue = new ParenthesizedTimeLabel(timeAxisMinAuto);
			timeAxisMinAutoValue.setTime(new GregorianCalendar());
		    timeAxisMinAutoValue.setText("should update every second");

		    timeAxisMinAuto.setSelected(true);

		    JPanel yAxisMinRow1 = createMultiItemRow(timeAxisMinCurrent, timeAxisMinCurrentValue);
	        JPanel yAxisMinRow2 = createMultiItemRow(timeAxisMinManual, timeAxisMinManualValue);
		    JPanel yAxisMinRow3 = createMultiItemRow(timeAxisMinAuto, timeAxisMinAutoValue);
		    JPanel yAxisMinRow4 = createMultiItemRow(timeAxisMinYear, null);
		    
		    JPanel manualMinPanel = new JPanel();
		    manualMinPanel.setLayout(new BoxLayout(manualMinPanel, BoxLayout.X_AXIS));
		    manualMinPanel.add(yAxisMinRow2);
		    manualMinPanel.add(yAxisMinRow4);

	        add(yAxisMinRow1);
	        add(manualMinPanel);
	        add(yAxisMinRow3);
	        add(Box.createVerticalGlue());

	        ButtonGroup minButtonGroup = new ButtonGroup();
	        minButtonGroup.add(timeAxisMinCurrent);
	        minButtonGroup.add(timeAxisMinManual);
	        minButtonGroup.add(timeAxisMinAuto);

	        timeAxisMinAuto.setToolTipText(BUNDLE.getString("TimeAxisMins.label"));

	        setBackground(Color.ORANGE);
		}
	}

	// Panel holding the Time Axis maximum controls
	class TimeAxisMaximumsPanel extends JPanel {
		private static final long serialVersionUID = 6105026690366452860L;

		public TimeAxisMaximumsPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			timeAxisMaxCurrent = new JRadioButton(BUNDLE.getString("CurrentMax.label"));
			timeAxisMaxCurrentValue = new ParenthesizedTimeLabel(timeAxisMaxCurrent);
			GregorianCalendar initCalendar = new GregorianCalendar();
			initCalendar.add(Calendar.MINUTE, 10);
			timeAxisMaxCurrentValue.setTime(initCalendar);
			Integer[] years = new Integer[10];
			for (int i = 0 ; i < 10; i++ ) {
				years[i] = new Integer(initCalendar.get(Calendar.YEAR) - i);
			}
			timeAxisMaxYear = new JComboBox(years);
			timeAxisMaxYear.setEditable(true);
			

			timeAxisMaxManual = new JRadioButton(MANUAL_LABEL);

			MaskFormatter formatter = null;
			try {
				formatter = new MaskFormatter("###/##:##:##");
				formatter.setPlaceholderCharacter('0');
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    timeAxisMaxManualValue = new TimeTextField(formatter, initCalendar.get(Calendar.YEAR));
		    initCalendar.setTimeInMillis(timeAxisMaxManualValue.getValueInMillis() + 1000);
		    timeAxisMaxManualValue.setTime(initCalendar);
		    timeAxisMaxYear.setSelectedItem(Integer.valueOf(timeAxisMaxManualValue.getYear()));
		    timeAxisMaxYear.setPreferredSize(new Dimension(60,timeAxisMaxManualValue.getPreferredSize().height - 1));
			timeAxisMaxYear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timeAxisMaxManualValue.setYear(((Integer) ((JComboBox) e.getSource()).getSelectedItem()).intValue());
				}
				
			});
		    
			timeAxisMaxAuto = new JRadioButton(BUNDLE.getString("MinPlusSpan.label"));
			timeAxisMaxAutoValue = new ParenthesizedTimeLabel(timeAxisMaxAuto);
            timeAxisMaxAutoValue.setTime(initCalendar);
            
			JPanel yAxisMaxRow1 = createMultiItemRow(timeAxisMaxCurrent, timeAxisMaxCurrentValue);
	        JPanel yAxisMaxRow2 = createMultiItemRow(timeAxisMaxManual, timeAxisMaxManualValue);
			JPanel yAxisMaxRow3 = createMultiItemRow(timeAxisMaxAuto, timeAxisMaxAutoValue);
			JPanel yAxisMaxRow4 = createMultiItemRow(timeAxisMaxYear, null);
			
	        timeAxisMaxAuto.setSelected(true);

		    JPanel manualMaxPanel = new JPanel();
		    manualMaxPanel.setLayout(new BoxLayout(manualMaxPanel, BoxLayout.X_AXIS));
		    manualMaxPanel.add(yAxisMaxRow2);
		    manualMaxPanel.add(yAxisMaxRow4);
		    
	        add(yAxisMaxRow1);
	        add(manualMaxPanel);
	        add(yAxisMaxRow3);
	        add(Box.createVerticalGlue());

	        ButtonGroup maxButtonGroup = new ButtonGroup();
	        maxButtonGroup.add(timeAxisMaxCurrent);
	        maxButtonGroup.add(timeAxisMaxManual);
	        maxButtonGroup.add(timeAxisMaxAuto);

	        timeAxisMaxAuto.setToolTipText(BUNDLE.getString("TimeAxisMins.label"));
		}
	}

	private enum GenericMinMaxSetting {
		CURRENT, MANUAL, AUTO
	}
	
	// Non-time axis Minimums panel
	private abstract class AxisRadioButtonPanel extends JPanel {
		private static final long serialVersionUID = -1971142961447566207L;

		private JRadioButton[] buttons = new JRadioButton[3];
		private ButtonGroup    group   = new ButtonGroup(); 
		
		public AxisRadioButtonPanel(String currentName, JComponent currentPanel, 
							   String manualName, JComponent manualPanel, 
							   String autoName, JComponent autoPanel) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			String[]     names  = { currentName , manualName , autoName  };
			JComponent[] panels = { currentPanel, manualPanel, autoPanel };
			
			for (int i = 0; i < 3; i++) {
				buttons[i] = new JRadioButton(names[i], false);
				group.add(buttons[i]);
				add(createMultiItemRow(buttons[i], panels[i]));
			}
		}

		public GenericMinMaxSetting getSelected() {
			for (GenericMinMaxSetting setting : GenericMinMaxSetting.values()) {
				if (buttons[setting.ordinal()].isSelected()) {
					return setting;
				}
			}
			return null;
		}

		public void setSelected(GenericMinMaxSetting setting) {
			if (setting != null) {
				group.setSelected(buttons[setting.ordinal()].getModel(), true);
			}
		}

		public void addActionListener(ActionListener listener) {
			for (JRadioButton button : buttons) {
				button.addActionListener ( listener );
			}
		}

		public void setEnabled(GenericMinMaxSetting setting, boolean state) {
			buttons[setting.ordinal()].setEnabled(state);
		}
	}
	
	// Non-time axis Minimums panel
	class NonTimeAxisMinimumsPanel extends JPanel {
		private static final long serialVersionUID = -2619634570876465687L;

		public NonTimeAxisMinimumsPanel(boolean isMaximum) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			nonTimeAxisMinCurrent = new JRadioButton(BUNDLE.getString(
					isMaximum ? "CurrentLargestDartum.label" : "CurrentSmallestDatum.label"), true);
			
			nonTimeAxisMinCurrentValue = new ParenthesizedNumericLabel(
					isMaximum ? nonTimeAxisMaxCurrent : nonTimeAxisMinCurrent);
			nonTimeAxisMinCurrentValue.setValue(isMaximum ? 1.0 : 0.0);

			nonTimeAxisMinManual = new JRadioButton(MANUAL_LABEL, false);
			
			nonTimeAxisMinManualValue = new NumericTextField(NUMERIC_TEXTFIELD_COLS1, PARENTHESIZED_LABEL_FORMAT);
			nonTimeAxisMinManualValue.setColumns(JTEXTFIELD_COLS);
			
			nonTimeAxisMinManualValue.setText(isMaximum ? nonTimeAxisMaxCurrentValue.getValue().toString() :
				                                          nonTimeAxisMinCurrentValue.getValue().toString()); 
			
			nonTimeAxisMinAutoAdjust = new JRadioButton(
					BUNDLE.getString(isMaximum ? "MinPlusSpan.label" : "MaxMinusSpan.label"), false);
			nonTimeAxisMinAutoAdjustValue = new ParenthesizedNumericLabel(nonTimeAxisMinAutoAdjust);
			nonTimeAxisMinAutoAdjustValue.setValue(isMaximum ? 1.0 : 0.0); 

			
			JPanel xAxisMinRow1 = createMultiItemRow(nonTimeAxisMinCurrent, nonTimeAxisMinCurrentValue);
			JPanel xAxisMinRow2 = createMultiItemRow(nonTimeAxisMinManual, nonTimeAxisMinManualValue);
			JPanel xAxisMinRow3 = createMultiItemRow(nonTimeAxisMinAutoAdjust, nonTimeAxisMinAutoAdjustValue);

			ButtonGroup minimumsGroup = new ButtonGroup();
			minimumsGroup.add(nonTimeAxisMinCurrent);
			minimumsGroup.add(nonTimeAxisMinManual);
			minimumsGroup.add(nonTimeAxisMinAutoAdjust);

			// Layout
			add(xAxisMinRow1);
			add(xAxisMinRow2);
			add(xAxisMinRow3);
			nonTimeAxisMinCurrent.setToolTipText(BUNDLE.getString("NonTimeAxisMin.label"));

			// Instrument
			xAxisMinRow1.setName("xAxisMinRow1");
			xAxisMinRow2.setName("xAxisMinRow2");
			xAxisMinRow3.setName("xAxisMinRow3");
		}
	}

	// Non-time axis Maximums panel
	class NonTimeAxisMaximumsPanel extends JPanel {
		private static final long serialVersionUID = -768623994853270825L;

		public NonTimeAxisMaximumsPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			nonTimeAxisMaxCurrent = new JRadioButton(BUNDLE.getString("CurrentLargestDatum.label"), true);
			
			nonTimeAxisMaxCurrentValue = new ParenthesizedNumericLabel(nonTimeAxisMaxCurrent);
			nonTimeAxisMaxCurrentValue.setValue(1.0);

			nonTimeAxisMaxManual = new JRadioButton(MANUAL_LABEL, false);
			
			DecimalFormat format = new DecimalFormat("###.######");
			format.setParseIntegerOnly(false);
			nonTimeAxisMaxManualValue = new NumericTextField(NUMERIC_TEXTFIELD_COLS1, format);
			nonTimeAxisMaxManualValue.setColumns(JTEXTFIELD_COLS);
			
			nonTimeAxisMaxManualValue.setText(nonTimeAxisMaxCurrentValue.getValue().toString());

			nonTimeAxisMaxAutoAdjust = new JRadioButton(BUNDLE.getString("MinPlusSpan.label"), false);
			nonTimeAxisMaxAutoAdjustValue = new ParenthesizedNumericLabel(nonTimeAxisMaxAutoAdjust);
			nonTimeAxisMaxAutoAdjustValue.setValue(1.0);

			JPanel xAxisMaxRow1 = createMultiItemRow(nonTimeAxisMaxCurrent, nonTimeAxisMaxCurrentValue);
			JPanel xAxisMaxRow2 = createMultiItemRow(nonTimeAxisMaxManual, nonTimeAxisMaxManualValue);
			JPanel xAxisMaxRow3 = createMultiItemRow(nonTimeAxisMaxAutoAdjust, nonTimeAxisMaxAutoAdjustValue);

			ButtonGroup maximumsGroup = new ButtonGroup();
			maximumsGroup.add(nonTimeAxisMaxManual);
			maximumsGroup.add(nonTimeAxisMaxCurrent);
			maximumsGroup.add(nonTimeAxisMaxAutoAdjust);

			// Layout
			add(xAxisMaxRow1);
			add(xAxisMaxRow2);
			add(xAxisMaxRow3);
			nonTimeAxisMaxCurrent.setToolTipText(BUNDLE.getString("NonTimeAxisMax.label"));

			// Instrument
			xAxisMaxRow1.setName("xAxisMaxRow1");
			xAxisMaxRow2.setName("xAxisMaxRow2");
			xAxisMaxRow3.setName("xAxisMaxRow3");
		}
	}

	// Panel holding X axis tags for Min and Max, and the Span field
	class XAxisAdjacentPanel extends JPanel {
		private static final long serialVersionUID = 4160271246055659710L;
        GridBagConstraints gbcLeft = new GridBagConstraints();
        GridBagConstraints gbcRight = new GridBagConstraints();
        GridBagConstraints gbcCenter = new GridBagConstraints();

		public XAxisAdjacentPanel() {
		    this.setLayout(new GridBagLayout());
			xMinLabel = new JLabel(BUNDLE.getString("Min.label"));
			xMaxLabel = new JLabel(BUNDLE.getString("Max.label"));
            xMinLabel.setFont(xMinLabel.getFont().deriveFont(Font.BOLD));
            xMaxLabel.setFont(xMaxLabel.getFont().deriveFont(Font.BOLD));

			setBorder(BOTTOM_PADDED_MARGINS);

            gbcLeft.anchor = GridBagConstraints.WEST;
            gbcLeft.gridx = 0;
            gbcLeft.gridy = 0;
            gbcLeft.weightx = 0.5;
            gbcCenter.anchor = GridBagConstraints.NORTH;
            gbcCenter.gridx = 1;
            gbcCenter.gridy = 0;
            gbcRight.anchor = GridBagConstraints.EAST;
            gbcRight.gridx = 2;
            gbcRight.gridy = 0;
            gbcRight.weightx = 0.5;
		}

		public void setNormalOrder(boolean normalDirection) {
			removeAll();
			if (normalDirection) {
			    add(xMinLabel, gbcLeft);
			    add(xAxisSpanCluster, gbcCenter);
			    add(xMaxLabel, gbcRight);
			} else {
                add(xMaxLabel, gbcLeft);
                add(xAxisSpanCluster, gbcCenter);
                add(xMinLabel, gbcRight);
			}
			revalidate();
		}
	}

	// Panel holding X axis Span controls
	class XAxisSpanCluster extends JPanel {
		private static final long serialVersionUID = -3947426156383446643L;
		private JComponent spanValue;
		private JLabel spanTag;
		private Component boxStrut;

		public XAxisSpanCluster() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	        spanTag = new JLabel(BUNDLE.getString("SpanTextField.label"));

	        // Create a text field with a ddd/hh:mm:ss format for time
	        timeSpanValue = getTimeSpanTextFieldFormat();
	        spanValue = timeSpanValue;
	        boxStrut = Box.createHorizontalStrut(INTERCONTROL_HORIZONTAL_SPACING);

			layoutPanel();

            // Instrument
            spanTag.setName("spanTag");
		}

		void layoutPanel() {
			removeAll();
			add(spanTag);
            add(boxStrut);
            add(spanValue);
            if (spanValue instanceof TimeSpanTextField) {
	            add(Box.createHorizontalStrut(INTERCONTROL_HORIZONTAL_SPACING + 3));
	            add(new JLabel(BUNDLE.getString("YearSpan")));
	            add(Box.createHorizontalStrut(INTERCONTROL_HORIZONTAL_SPACING + 3));
	            add(timeSpanValue.getYearSpanValue());
            }
		}

		void setSpanField(JComponent field) {
			spanValue = field;
			layoutPanel();
		}
		
		TimeSpanTextField getTimeSpanTextFieldFormat() {
			MaskFormatter formatter = null;
			try {
				formatter = new MaskFormatter("###/##:##:##");
				formatter.setPlaceholderCharacter('0');
			} catch (ParseException e) {
				logger.error("Error in creating a mask formatter", e);
			}
	        return new TimeSpanTextField(formatter);
		}
	}

	// Panel that holds the still image of a plot in the Initial Settings area
    public class StillPlotImagePanel extends PlotSettingsSubPanel {
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

		@Override
		public void populate(PlotConfiguration settings) {
			// Passive - only respond to settings
		}

		@Override
		public void reset(PlotConfiguration settings, boolean hard) {
			switch (settings.getAxisOrientationSetting()) {
			case X_AXIS_AS_TIME:
				setImageToTimeOnXAxis(settings.getXAxisMaximumLocation() == XAxisMaximumLocationSetting.MAXIMUM_AT_RIGHT);
				break;
			case Y_AXIS_AS_TIME:
				setImageToTimeOnYAxis(settings.getYAxisMaximumLocation() == YAxisMaximumLocationSetting.MAXIMUM_AT_TOP);
				break;
			}
		}

		@Override
		public boolean isDirty() {
			return false;
		}

		@Override
		public boolean isValidated() {
			return true;
		}
	}


    /* ================================================
	 * Main Constructor for Plot Settings Control panel
	 * ================================================
	 */
	public PlotSetupControlPanel(PlotViewManifestation plotMan) {
		// This sets the display of date/time fields that use this format object to use GMT
		dateFormat.setTimeZone(TimeZone.getTimeZone(PlotConstants.DEFAULT_TIME_ZONE));
		
		// store reference to the plot
		plotViewManifestation = plotMan;	
		// Create controller for this panel.

		setLayout(new BorderLayout());
		setBorder(CONTROL_PANEL_MARGINS);		

		add(getInitialSetupPanel(), BorderLayout.WEST);

		// Set the initial value of the Time Min Auto value ("Now")
		GregorianCalendar nextTime = new GregorianCalendar();
		nextTime.setTimeInMillis(plotViewManifestation.getCurrentMCTTime());
		nextTime.setTimeZone(TimeZone.getTimeZone(PlotConstants.DEFAULT_TIME_ZONE));
		if (nextTime.getTimeInMillis() == 0.0) {
			nextTime.setTimeInMillis(plotViewManifestation.getPlot().getMinTime());
			nextTime.setTimeZone(TimeZone.getTimeZone(PlotConstants.DEFAULT_TIME_ZONE));
		}
		timeAxisMinAutoValue.setTime(nextTime);

		instrumentNames();

//		// Initialize state of control panel to match that of the plot.
//		PlotAbstraction plot = plotViewManifestion.getPlot();
//		
//		if (plot != null) {
//			reset(plot);
//		}
		
		refreshDisplay();
	}



	/**
	 * Return true if tw
	 * @param buttonSet1
	 * @param buttonSet2
	 * @return
	 */
	static boolean buttonStateMatch(List<JToggleButton> buttonSet1, List<JToggleButton> buttonSet2) {
	   return buttonSet1.size() == buttonSet2.size() &&
		    buttonSet1.containsAll(buttonSet2);
	}
	


	// Assign internal names to the top level class variables
	private void instrumentNames() {
//        timeAxisMinimumsPanel.setName("timeAxisMinimumsPanel");
//        timeAxisMaximumsPanel.setName("timeAxisMaximumsPanel");
//		nonTimeAxisMaximumsPanel.setName("nonTimeAxisMaximumsPanel");
//		nonTimeAxisMinimumsPanel.setName("nonTimeAxisMinimumsPanel");
//
//        timeAxisMinAuto.setName("timeAxisMinAuto");
//        timeAxisMinAutoValue.setName("timeAxisMinAutoValue");
//        timeAxisMinManual.setName("timeAxisMinManual");
//        timeAxisMinManualValue.setName("timeAxisMinManualValue");
//
//        timeAxisMaxAuto.setName("timeAxisMaxAuto");
//        timeAxisMaxAutoValue.setName("timeAxisMaxAutoValue");
//        timeAxisMaxManual.setName("timeAxisMaxManual");
//        timeAxisMaxManualValue.setName("timeAxisMaxManualValue");
//
//		nonTimeAxisMinCurrent.setName("nonTimeAxisMinCurrent");
//		nonTimeAxisMinCurrentValue.setName("nonTimeAxisMinCurrentValue");
//		nonTimeAxisMinManual.setName("nonTimeAxisMinManual");
//		nonTimeAxisMinManualValue.setName("nonTimeAxisMinManualValue");
//		nonTimeAxisMinAutoAdjust.setName("nonTimeAxisMinAutoAdjust");
//
//		nonTimeAxisMaxCurrent.setName("nonTimeAxisMaxCurrent");
//		nonTimeAxisMaxCurrentValue.setName("nonTimeAxisMaxCurrentValue");
//		nonTimeAxisMaxManual.setName("nonTimeAxisMaxManual");
//		nonTimeAxisMaxManualValue.setName("nonTimeAxisMaxManualValue");
//		nonTimeAxisMaxAutoAdjust.setName("nonTimeAxisMaxAutoAdjust");
//
//        imagePanel.setName("imagePanel");
//        timeSystemDropdown.setName("timeSystemDropdown");
//        timeFormatDropdown.setName("timeFormatDropdown");
//
//        timeSpanValue.setName("timeSpanValue");
//        nonTimeSpanValue.setName("nonTimeSpanValue");
//		
//        xMinLabel.setName("xMinLabel");
//        xMaxLabel.setName("xMaxLabel");
//
//        xAxisAsTimeRadioButton.setName("xAxisAsTimeRadioButton");
//        yAxisAsTimeRadioButton.setName("yAxisAsTimeRadioButton");
//        xMaxAtRight.setName("xMaxAtRight");
//        xMaxAtLeft.setName("xMaxAtLeft");
//        yMaxAtTop.setName("yMaxAtTop");
//        yMaxAtBottom.setName("yMaxAtBottom");
//
//		xAxisSpanCluster.setName("xAxisSpanCluster");
//		xAxisAdjacentPanel.setName("xAxisAdjacentPanel");
//		xAxisButtonsPanel.setName("xAxisButtonsPanel");
//
//        yAxisSpanPanel.setName("ySpanPanel");
//        yMaximumsPlusPanel.setName("yMaximumsPlusPanel");
//        yMinimumsPlusPanel.setName("yMinimumsPlusPanel");
//        yAxisButtonsPanel.setName("yAxisButtonsPanel");
	}

	/**
	 * This method scans and sets the Time Axis controls next to the static plot image.
	 * Triggered by time axis button selection.
	 */
	GregorianCalendar scratchCalendar = new GregorianCalendar();
	GregorianCalendar workCalendar = new GregorianCalendar();
	{
		workCalendar.setTimeZone(TimeZone.getTimeZone(PlotConstants.DEFAULT_TIME_ZONE));
	}
	

	void updateTimeAxisControls() {
		// Enable/disable the Span control
		timeSpanValue.setEnabled(timeAxisMaxAuto.isSelected());
		timeSpanValue.getYearSpanValue().setEnabled(timeAxisMaxAuto.isSelected());

		// Set the value of the Time Span field and the various Min and Max Time fields
		if (timeAxisMinAuto.isSelected() && timeAxisMaxAuto.isSelected()) {
			// If both Auto buttons are selected, Span value is used
			scratchCalendar.setTimeInMillis(timeAxisMinAutoValue.getTimeInMillis());
			scratchCalendar.add(Calendar.SECOND, timeSpanValue.getSecond());
			scratchCalendar.add(Calendar.MINUTE, timeSpanValue.getMinute());
			scratchCalendar.add(Calendar.HOUR_OF_DAY, timeSpanValue.getHourOfDay());
			scratchCalendar.add(Calendar.DAY_OF_YEAR, timeSpanValue.getDayOfYear());
			scratchCalendar.add(Calendar.YEAR, Integer.parseInt(timeSpanValue.getYearSpanValue().getText()));
			timeAxisMaxAutoValue.setTime(scratchCalendar);
		} else if (timeAxisMinAuto.isSelected() && timeAxisMaxManual.isSelected()) {
			/*
			 * Min Auto ("Now"), and Max Manual
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinAutoValue.getTimeInMillis(),
					timeAxisMaxManualValue.getValueInMillis()));
		} else if (timeAxisMinAuto.isSelected() && timeAxisMaxCurrent.isSelected()) {
			/*
			 * Min Auto ("Now"), and Current Max
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinAutoValue.getTimeInMillis(),
					timeAxisMaxCurrentValue.getTimeInMillis()));
		} else if (timeAxisMinManual.isSelected() && timeAxisMaxAuto.isSelected()) {
			/*
			 * Min Manual, and Max Auto ("Min+Span")
			 */
			scratchCalendar.setTimeInMillis(timeAxisMinManualValue.getValueInMillis());
			scratchCalendar.add(Calendar.SECOND, timeSpanValue.getSecond());
			scratchCalendar.add(Calendar.MINUTE, timeSpanValue.getMinute());
			scratchCalendar.add(Calendar.HOUR_OF_DAY, timeSpanValue.getHourOfDay());
			scratchCalendar.add(Calendar.DAY_OF_YEAR, timeSpanValue.getDayOfYear());
			timeAxisMaxAutoValue.setTime(scratchCalendar);
		} else if (timeAxisMinManual.isSelected() && timeAxisMaxManual.isSelected()) {
			/*
			 * Min Manual, and Max Manual
			 * - subtract the Min Manual from the Max Manual to get the new Span value
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinManualValue.getValueInMillis(),
					timeAxisMaxManualValue.getValueInMillis()));
		} else if (timeAxisMinManual.isSelected() && timeAxisMaxCurrent.isSelected()) {
			/*
			 * Min Manual, and Current Max
			 * - subtract the Min Manual from the Current Max to get the new Span value
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinManualValue.getValueInMillis(),
					timeAxisMaxCurrentValue.getTimeInMillis()));
		} else if (timeAxisMinCurrent.isSelected() && timeAxisMaxAuto.isSelected()) {
			/*
			 * Current Min, and Max Auto ("Min+Span")
			 * - set the Max Auto value to the sum of Current Min and the Span value
			 */
			scratchCalendar.setTimeInMillis(timeAxisMinCurrentValue.getTimeInMillis());
			scratchCalendar.add(Calendar.SECOND, timeSpanValue.getSecond());
			scratchCalendar.add(Calendar.MINUTE, timeSpanValue.getMinute());
			scratchCalendar.add(Calendar.HOUR_OF_DAY, timeSpanValue.getHourOfDay());
			scratchCalendar.add(Calendar.DAY_OF_YEAR, timeSpanValue.getDayOfYear());
			scratchCalendar.add(Calendar.YEAR, Integer.parseInt(timeSpanValue.getYearSpanValue().getText()));
			timeAxisMaxAutoValue.setTime(scratchCalendar);
		} else if (timeAxisMinCurrent.isSelected() && timeAxisMaxManual.isSelected()) {
			/*
			 * Current Min, and Max Manual
			 * - subtract the Current Min from Max Manual to get the new Span value 
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinCurrentValue.getTimeInMillis(),
					timeAxisMaxManualValue.getValueInMillis()));
		} else if (timeAxisMinCurrent.isSelected() && timeAxisMaxCurrent.isSelected()) {
			/*
			 * Current Min, and Current Max
			 * - subtract the Current Min from the Current Max to get the new Span value
			 */
			timeSpanValue.setTime(subtractTimes(timeAxisMinCurrentValue.getTimeInMillis(),
					timeAxisMaxCurrentValue.getTimeInMillis()));
		} else {
			logger.error("Program issue: if-else cases are missing one use case.");
		}

		if (timeAxisMinCurrent.isSelected()) {
			scratchCalendar.setTimeInMillis(timeAxisMinCurrentValue.getTimeInMillis());
		} else
			if (timeAxisMinManual.isSelected()) {
				scratchCalendar.setTimeInMillis(timeAxisMinManualValue.getValueInMillis());
			} else
				if (timeAxisMinAuto.isSelected()) {
					scratchCalendar.setTimeInMillis(timeAxisMinAutoValue.getTimeInMillis());
				}

		scratchCalendar.add(Calendar.SECOND, timeSpanValue.getSecond());
		scratchCalendar.add(Calendar.MINUTE, timeSpanValue.getMinute());
		scratchCalendar.add(Calendar.HOUR_OF_DAY, timeSpanValue.getHourOfDay());
		scratchCalendar.add(Calendar.DAY_OF_YEAR, timeSpanValue.getDayOfYear());
		scratchCalendar.add(Calendar.YEAR, Integer.parseInt(timeSpanValue.getYearSpanValue().getValue().toString()));
		timeAxisMaxAutoValue.setTime(scratchCalendar);

		// Update the Time axis Current Min and Max values
		scratchCalendar.setTimeInMillis(plotViewManifestation.getPlot().getMinTime());
		timeAxisMinCurrentValue.setTime(scratchCalendar);
		scratchCalendar.setTimeInMillis(plotViewManifestation.getPlot().getMaxTime());
		timeAxisMaxCurrentValue.setTime(scratchCalendar);

		// If the Manual (Min and Max) fields have NOT been selected up to now, update them with the
		// plot's current Min and Max values
		if (! timeMinManualHasBeenSelected) {
    		workCalendar.setTimeInMillis(plotViewManifestation.getPlot().getMinTime());
			timeAxisMinManualValue.setTime(workCalendar);
			timeAxisMinYear.setSelectedItem(timeAxisMinManualValue.getYear());
		}
		if (! timeMaxManualHasBeenSelected) {
    		workCalendar.setTimeInMillis(plotViewManifestation.getPlot().getMaxTime());
			timeAxisMaxManualValue.setTime(workCalendar);
			timeAxisMaxYear.setSelectedItem(timeAxisMaxManualValue.getYear());
		}

	}

	/**
	 * Returns the difference between two times as a time Duration
	 * @param begin
	 * @param end
	 * @return
	 */
	private TimeDuration subtractTimes(long begin, long end) {
	    if (begin < end) {
			long difference = end - begin;
			long denominator = 365L * 24L * 60L * 60L * 1000L;
			long years = difference / denominator;
			long remainder = difference - (long) (years * 365L * 24L * 60L * 60L * 1000L);
			long days = remainder / (24 * 60 * 60 * 1000);
			remainder = remainder - (days * 24 * 60 * 60 * 1000);
			long hours = remainder / (60 * 60 * 1000);
			remainder = remainder - (hours * 60 * 60 * 1000);
			long minutes = remainder / (60 * 1000);
			remainder = remainder - (minutes * 60 * 1000);
			long seconds = remainder / (1000);
			return new TimeDuration((int) years, (int) days, (int) hours, (int) minutes, (int) seconds);
		} else {
			return new TimeDuration(0, 0, 0, 0, 0);
		}
	}

	/**
	 * This method scans and sets the Non-Time Axis controls next to the static plot image.
	 * Triggered when a non-time radio button is selected or on update tick
	 */
	void updateNonTimeAxisControls() {
		assert !(nonTimeAxisMinAutoAdjust.isSelected() && nonTimeAxisMaxAutoAdjust.isSelected()) : "Illegal condition: Both span radio buttons are selected.";
		// Enable/disable the non-time Span value
		if (nonTimeAxisMinAutoAdjust.isSelected() || nonTimeAxisMaxAutoAdjust.isSelected()) {
			nonTimeSpanValue.setEnabled(true);
		} else {
			nonTimeSpanValue.setEnabled(false);
		}
		// Enable/disable the non-time Auto-Adjust (Span-dependent) controls
		if (nonTimeAxisMaxAutoAdjust.isSelected()) {
			nonTimeAxisMinAutoAdjust.setEnabled(false);
			nonTimeAxisMinAutoAdjustValue.setEnabled(false);
		} else
		if (nonTimeAxisMinAutoAdjust.isSelected()) {
			nonTimeAxisMaxAutoAdjust.setEnabled(false);
			nonTimeAxisMaxAutoAdjustValue.setEnabled(false);
		} else
			// If neither of the buttons using Span is selected, enable both
			if (!nonTimeAxisMinAutoAdjust.isSelected() && !nonTimeAxisMaxAutoAdjust.isSelected()) {
			nonTimeAxisMinAutoAdjust.setEnabled(true);
			nonTimeAxisMaxAutoAdjust.setEnabled(true);					
			nonTimeAxisMinAutoAdjustValue.setEnabled(true);
			nonTimeAxisMaxAutoAdjustValue.setEnabled(true);					
		}

		// Update the Span-dependent controls
		//     nonTimeAxisMinAutoAdjustValue: (Max - Span)
		double maxValue = getNonTimeMaxValue();
		//     nonTimeAxisMaxAutoAdjustValue: (Min + Span)
		double minValue = getNonTimeMinValue();
		
	
		try {
			String span = nonTimeSpanValue.getText();
			if (! span.isEmpty()) {
				double spanValue = nonTimeSpanValue.getDoubleValue();
				nonTimeAxisMinAutoAdjustValue.setValue(maxValue - spanValue);
				nonTimeAxisMaxAutoAdjustValue.setValue(minValue + spanValue);
			}
		} catch (ParseException e) {
			logger.error("Plot control panel: Could not parse non-time span value.");
		}

		if (!(nonTimeAxisMinAutoAdjust.isSelected() || nonTimeAxisMaxAutoAdjust.isSelected())) {
			double difference = getNonTimeMaxValue() - getNonTimeMinValue();
			nonTimeSpanValue.setValue(difference);
		}

	}


	/**
	 * Update the label representing the time axis' Min + Span value
	 * Selections are: Min Manual button, Max Auto ("Min + Span") button
	 */
	public void refreshDisplay() {
		// Update the MCT time ("Now")
//		GregorianCalendar gc = new GregorianCalendar();
//		gc.setTimeInMillis(plotViewManifestion.getCurrentMCTTime());
//		timeAxisMinAutoValue.setTime(gc);
//
//		// Update the time min/max values			
//		nonTimeAxisMinCurrentValue.setValue(plotViewManifestion.getMinFeedValue());
//		nonTimeAxisMaxCurrentValue.setValue(plotViewManifestion.getMaxFeedValue());
//		
		//updateTimeAxisControls();
		//updateNonTimeAxisControls();
	}
	
	// Initially returns float; but shouldn't this be double precision (?)
	double getNonTimeMaxValue() {
		if (nonTimeAxisMaxCurrent.isSelected()) {
			return nonTimeAxisMaxCurrentValue.getValue().floatValue();
		} else if (nonTimeAxisMaxManual.isSelected()) {
			//float result = 0;
			double result = 1.0;
			try {
				result = nonTimeAxisMaxManualValue.getDoubleValue().floatValue();
			} catch (ParseException e) {
				return 1.0;
			}
			
			return result;
		} else if (nonTimeAxisMaxAutoAdjust.isSelected()) {
			
			return nonTimeAxisMaxAutoAdjustValue.getValue().floatValue();
		}
		return 1.0;
	}

	double getNonTimeMinValue() {
		if (nonTimeAxisMinCurrent.isSelected()) {
			return nonTimeAxisMinCurrentValue.getValue().floatValue();
		} else if (nonTimeAxisMinManual.isSelected()) {
			double result = 0.0;
			try {
				result = nonTimeAxisMinManualValue.getDoubleValue().floatValue();
				
			} catch (ParseException e) {
				return 0.0;
			}
			return result;
		} else if (nonTimeAxisMinAutoAdjust.isSelected()) {
			
			return nonTimeAxisMinAutoAdjustValue.getValue().floatValue();
		}
		return 0.0;
	}

	// The Initial Settings panel
	// Name change: Initial Settings is now labeled Plot Setup
	private PlotSettingsPanel getInitialSetupPanel() {
		PlotSettingsPanel initialSetup = new PlotSettingsPanel();
		initialSetup.setLayout(new BoxLayout(initialSetup, BoxLayout.Y_AXIS));
		initialSetup.setBorder(SETUP_AND_BEHAVIOR_MARGINS);


        imagePanel = new StillPlotImagePanel();

        // Start defining the top panel
		PlotSettingsSubPanel initTopPanel = getAlternateTopPanel();
        PlotSettingsSubPanel initBottomPanel = getAlternateBottomPanel();

		// Assemble the major panel: Initial Settings
        JPanel topClamp = new JPanel(new BorderLayout());
        topClamp.add(initTopPanel, BorderLayout.NORTH);
        JPanel bottomClamp = new JPanel(new BorderLayout());
        bottomClamp.add(initBottomPanel, BorderLayout.NORTH);
        JPanel sideClamp = new JPanel(new BorderLayout());
        sideClamp.add(bottomClamp, BorderLayout.WEST);

        initialSetup.add(Box.createRigidArea(new Dimension(0, INNER_PADDING)));
		initialSetup.add(topClamp);
		initialSetup.add(Box.createRigidArea(new Dimension(0, INNER_PADDING)));
		initialSetup.add(new JSeparator());
        initialSetup.add(sideClamp);

        // Instrument
        initialSetup.setName("initialSetup");
        initTopPanel.setName("initTopPanel");
        initBottomPanel.setName("initBottomPanel");
        
        topClamp.setName("topClamp");
        bottomClamp.setName("bottomClamp");

        initialSetup.addSubPanel(initTopPanel);
        initialSetup.addSubPanel(initBottomPanel);
        
		return initialSetup;
	}

	
	private PlotSettingsSubPanel getAlternateBottomPanel() {

		final AxisPanel xAxisPanel = new AxisPanel() {			
			private static final long serialVersionUID = 7880175726915727283L;

			@Override
			public void initialLayout() {
				layout.putConstraint(SpringLayout.WEST, minMaxPanel[0], 0, SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.NORTH, minMaxPanel[0], 0, SpringLayout.SOUTH, spanPanel);
				
				layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, minMaxPanel[1]);
				layout.putConstraint(SpringLayout.WEST, minMaxPanel[1], 0, SpringLayout.EAST, minMaxPanel[0]);
				layout.putConstraint(SpringLayout.NORTH, minMaxPanel[1], 0, SpringLayout.SOUTH, spanPanel);
				
				layout.putConstraint(SpringLayout.WEST, minMaxLabel[0], 0, SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.NORTH, minMaxLabel[0], 0, SpringLayout.NORTH, this);
				
				layout.putConstraint(SpringLayout.EAST, minMaxLabel[1], 0, SpringLayout.EAST, this);
				layout.putConstraint(SpringLayout.NORTH, minMaxLabel[1], 0, SpringLayout.NORTH, this);
				
				layout.putConstraint(SpringLayout.SOUTH, minMaxPanel[1], 0, SpringLayout.SOUTH, minMaxPanel[0]);
				layout.putConstraint(SpringLayout.NORTH, axisTitle, 0, SpringLayout.SOUTH, minMaxPanel[0]);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, axisTitle, 0, SpringLayout.EAST, minMaxPanel[0]);
				
				layout.putConstraint(SpringLayout.NORTH, spanPanel, 0, SpringLayout.NORTH, this);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, spanPanel, 0, SpringLayout.EAST, minMaxPanel[0]);
				
				layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, axisTitle);
			}
		};
		
		final AxisPanel yAxisPanel = new AxisPanel() {			
			private static final long serialVersionUID = 7880175726915727283L;

			@Override
			public void initialLayout() {
				layout.putConstraint(SpringLayout.WEST, minMaxPanel[0], 0, SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.WEST, minMaxLabel[0], 0, SpringLayout.EAST, minMaxPanel[0]);

				layout.putConstraint(SpringLayout.WEST, minMaxPanel[1], 0, SpringLayout.WEST, minMaxPanel[0]);
				layout.putConstraint(SpringLayout.WEST, minMaxLabel[1], 0, SpringLayout.EAST, minMaxPanel[1]);
				
				layout.putConstraint(SpringLayout.EAST, spanPanel, 0, SpringLayout.EAST, this);
				layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, minMaxLabel[0]);
				
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, axisTitle, 0, SpringLayout.HORIZONTAL_CENTER, minMaxPanel[0]);

				layout.putConstraint(SpringLayout.NORTH, axisTitle, 0, SpringLayout.NORTH, this);
				layout.putConstraint(SpringLayout.NORTH, minMaxPanel[0], 0, SpringLayout.SOUTH, axisTitle);				
				layout.putConstraint(SpringLayout.NORTH, spanPanel, 0, SpringLayout.SOUTH, minMaxPanel[0]);
				layout.putConstraint(SpringLayout.NORTH, minMaxPanel[1], 0, SpringLayout.SOUTH, spanPanel);
				layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, minMaxPanel[1]);
				
				layout.putConstraint(SpringLayout.SOUTH, minMaxLabel[1], 0, SpringLayout.SOUTH, this);
				
			}
		};
		JPanel p1 = new JPanel(); p1.add(new JLabel("Min panel"));
		JPanel p2 = new JPanel(); p2.add(new JLabel("Max panel"));
		JPanel p3 = new JPanel(); //p3.add(new JLabel("Span panel"));

		
		final AxisGroup timeGroup = new AxisGroup(new TimeAxisMinimumsPanel(), new TimeAxisMaximumsPanel(), new XAxisSpanCluster());
		final AxisGroup nonTimeGroup = new AxisGroup(new NonTimeAxisMinimumsPanel(false), new NonTimeAxisMaximumsPanel(), new YAxisSpanPanel());
		
		PlotSettingsPanel panel = new PlotSettingsPanel() {
			private static final long serialVersionUID = 1730870211575829997L;			
			
			@Override			
			public void reset(PlotConfiguration settings, boolean hard) {
				boolean xInverted = settings.getXAxisMaximumLocation() == XAxisMaximumLocationSetting.MAXIMUM_AT_LEFT;
				boolean yInverted = settings.getYAxisMaximumLocation() == YAxisMaximumLocationSetting.MAXIMUM_AT_BOTTOM;
				switch (settings.getAxisOrientationSetting()) {
				case X_AXIS_AS_TIME:
					xAxisPanel.setFrom(timeGroup, xInverted);
					yAxisPanel.setFrom(nonTimeGroup, yInverted);
					break;
				case Y_AXIS_AS_TIME:
					yAxisPanel.setFrom(timeGroup, yInverted);
					xAxisPanel.setFrom(nonTimeGroup, xInverted);					
					break;
				}
				timeGroup.setTitle("TIME");
				nonTimeGroup.setTitle("NON-TIME");
				super.reset(settings, hard);
			}			
		};
		panel.setLayout(new GridBagLayout());
		panel.setBorder(TOP_PADDED_MARGINS);
		        

        // The title label for (TIME) or (NON-TIME)
        GridBagConstraints gbc = new GridBagConstraints();        

        // The Y Axis controls panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        // To align the "Min" or "Max" label with the bottom of the static plot image,
        // add a vertical shim under the Y Axis bottom button set and "Min"/"Max" label.
        gbc.insets = new Insets(2, 0, 10, 2); 
        panel.add(yAxisPanel, gbc);

        // The static plot image
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        panel.add(imagePanel, gbc);

        // The X Axis controls panel
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 8, 0, 0);
        panel.add(xAxisPanel, gbc);
        
        // Instrument
        yAxisPanel.setName("yAxisPanelSet");
        xAxisPanel.setName("xAxisPanelSet");
		
		return panel;
	}
	
	private PlotSettingsSubPanel getAlternateTopPanel() {
		PlotSettingsPanel panel = new PlotSettingsPanel();
		
		PlotSettingsSubPanel subPanels[] = {
			new TimeSetupPanel(),
			new XYSetupPanel(false),
			new XYSetupPanel(true),
			new GroupingSetupPanel()			
		};
				
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 0.0;
        
		for (JComponent c : subPanels) {
			gbc.insets = new Insets(0, 0, 0, 0);			
			gbc.fill = GridBagConstraints.NONE;	
			if (c == subPanels[subPanels.length - 1]) gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			panel.add(c, gbc);
			gbc.gridx++;
			
			if (c == subPanels[subPanels.length - 1]) break; // No final separator
			gbc.insets = new Insets(0, 5, 0, 5);
			gbc.fill = GridBagConstraints.VERTICAL;
			panel.add(new JSeparator(JSeparator.VERTICAL), gbc);
			gbc.gridx++;
		}
		
		return panel;
	}
	
	private class TimeSetupPanel extends PlotSettingsPanel {
		private static final long serialVersionUID = -2628686516154128194L;
		
		public TimeSetupPanel() {
			// Time Systems and Formats
			JPanel timePanel = new PlotSettingsPanel();
			timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS)); 
			timePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			
			JPanel timeSystemPanel = new PlotSettingsPanel();
			timeSystemPanel.setLayout(new BoxLayout(timeSystemPanel, BoxLayout.X_AXIS)); 
			timeSystemPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0));
			
			JPanel timeFormatsPanel = new PlotSettingsPanel();
			timeFormatsPanel.setLayout(new BoxLayout(timeFormatsPanel, BoxLayout.X_AXIS)); 
			timeFormatsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 0));
			
			timePanel.add(timeSystemPanel);
			timePanel.add(timeFormatsPanel);
			
			String [] choices = getComponentSpecifiedTimeSystemChoices();
			PlotSettingsComboBox<String> timeSystemComboBox = new PlotSettingsComboBox<String>(choices) {
				private static final long serialVersionUID = 1765748658974789785L;

				@Override
				public void populate(PlotConfiguration settings) {
					settings.setTimeSystemSetting(getSelection());
				}

				@Override
				public void reset(PlotConfiguration settings, boolean hard) {
					if (hard) setSelection(settings.getTimeSystemSetting());					
				}				
			};
			if (choices.length < 1) {
				timeSystemComboBox.setFocusable(false);
				timeSystemComboBox.setEnabled(false);
			}
			
			timeSystemPanel.add(new JLabel(BUNDLE.getString("TimeSystem.label")));
			timeSystemPanel.add(timeSystemComboBox);
			addSubPanel(timeSystemComboBox);
						
			choices = getComponentSpecifiedTimeFormatChoices();
			PlotSettingsComboBox<String> timeFormatComboBox = new PlotSettingsComboBox<String>(choices) {				
				private static final long serialVersionUID = 4893624658379045632L;

				@Override
				public void populate(PlotConfiguration settings) {
					settings.setTimeFormatSetting(getSelection());
				}

				@Override
				public void reset(PlotConfiguration settings, boolean hard) {
					if (hard) setSelection(settings.getTimeFormatSetting());					
				}				
			};
			if (choices.length < 1) {
				timeFormatComboBox.setFocusable(false);
				timeFormatComboBox.setEnabled(false);
			}
			timeFormatComboBox.setSelection(plotViewManifestation.getPlot().getTimeFormatSetting());
			timeFormatsPanel.add(new JLabel(BUNDLE.getString("TimeFormat.label")));
			timeFormatsPanel.add(timeFormatComboBox);
			addSubPanel(timeFormatComboBox);
			
	        
	    	PlotSettingsRadioButtonGroup<AxisOrientationSetting> timeAxisButtons = 
	    		new PlotSettingsRadioButtonGroup<AxisOrientationSetting>(AxisOrientationSetting.values()) {
	    		private static final long serialVersionUID = 1L;

				@Override
				public void populate(PlotConfiguration settings) {
					settings.setAxisOrientationSetting(getSelection());					
				}

				@Override
				public void reset(PlotConfiguration settings, boolean hard) {
					if (hard) setSelection(settings.getAxisOrientationSetting());
				}	    		
	    	};
	    	timeAxisButtons.setText(AxisOrientationSetting.X_AXIS_AS_TIME, BUNDLE.getString("XAxisAsTime.label"));
	    	timeAxisButtons.setText(AxisOrientationSetting.Y_AXIS_AS_TIME, BUNDLE.getString("YAxisAsTime.label"));
	    	
	    	setLayout(new GridBagLayout());
	    	GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets = new Insets(0,0,2,0);
			gbc.gridy = 0;
			add(timeSystemPanel, gbc);
			gbc.gridy = 1;			
			add(timeFormatsPanel, gbc);
			gbc.gridy = 2;
			add(timeAxisButtons, gbc);	
		}
	}
	
	/**
	 * Panel for selecting "Max at Top", "Max at Bottom", et cetera
	 * Used for both X and Y axes; boolean value in constructor call distinguishes
	 */
	private class XYSetupPanel extends PlotSettingsSubPanel {
		private static final long serialVersionUID = -4105387384633330667L;

		private boolean yAxis; //Otherwise X!		
		private JRadioButton topOption    = new JRadioButton();
		private JRadioButton bottomOption = new JRadioButton();
		private ButtonGroup  group = new ButtonGroup();
		private JRadioButton cachedSelection = topOption;
		
		public XYSetupPanel(boolean isYAxis) {
			yAxis = isYAxis;
			
			JLabel xDirTitle = new JLabel(BUNDLE.getString(yAxis ? "YAxis.label" : "XAxis.label"));
			topOption.setText(BUNDLE.getString(yAxis ? "MaxAtTop.label" : "MaxAtRight.label"));
			bottomOption.setText(BUNDLE.getString(yAxis ? "MaxAtBottom.label" : "MaxAtLeft.label"));

			topOption.addActionListener(this);
			bottomOption.addActionListener(this);
			
			group.add(topOption);
			group.add(bottomOption);
			
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets = new Insets(8,0,16,0);
			gbc.gridy = 0;
			add(xDirTitle, gbc);
			gbc.insets = new Insets(0,0,2,0);
			gbc.gridy = 1;			
			add(topOption, gbc);
			gbc.gridy = 2;
			add(bottomOption, gbc);			
		}
		
		@Override
		public void populate(PlotConfiguration settings) {
			if (yAxis) {
				settings.setYAxisMaximumLocation(topOption.isSelected() ? 
						YAxisMaximumLocationSetting.MAXIMUM_AT_TOP : 
						YAxisMaximumLocationSetting.MAXIMUM_AT_BOTTOM);
			} else {
				settings.setXAxisMaximumLocation(bottomOption.isSelected() ? 
						XAxisMaximumLocationSetting.MAXIMUM_AT_LEFT : 
						XAxisMaximumLocationSetting.MAXIMUM_AT_RIGHT);				
			}
		}

		@Override
		public void reset(PlotConfiguration settings, boolean hard) {
			boolean normal = yAxis ? (settings.getYAxisMaximumLocation() == YAxisMaximumLocationSetting.MAXIMUM_AT_TOP) :
				                     (settings.getXAxisMaximumLocation() == XAxisMaximumLocationSetting.MAXIMUM_AT_RIGHT);
			if (hard) {
				cachedSelection = normal ? topOption : bottomOption;			
				group.setSelected( cachedSelection.getModel(), true );
			}
		}

		@Override
		public boolean isDirty() {
			return cachedSelection.isSelected();
		}

		@Override
		public boolean isValidated() {
			return true;
		}
	}
	
	private class GroupingSetupPanel extends PlotSettingsPanel {
		private static final long serialVersionUID = 4465726647984136821L;

		public GroupingSetupPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridy = 0;
	        gbc.anchor = GridBagConstraints.NORTHWEST;
	        gbc.insets = new Insets(8,0,14,0);
	        add(new JLabel(BUNDLE.getString("StackedPlotGroping.label")),gbc);
	        gbc.gridy = 1;
	        gbc.insets = new Insets(0,0,0,0);
	        JPanel groupCheckBox = new PlotSettingsCheckBox(BUNDLE.getString("GroupByCollection.label")) {
				private static final long serialVersionUID = 3172215382592976671L;
				@Override
				public void populate(PlotConfiguration settings) {
					settings.setOrdinalPositionForStackedPlots(isSelected());
				}
				@Override
				public boolean getFrom(PlotConfiguration settings) {
					return settings.getOrdinalPositionForStackedPlots();
				}	        	
	        };
	        add(groupCheckBox, gbc);
		}
	}
	
	String[] getComponentSpecifiedTimeSystemChoices() {
		return (plotViewManifestation != null) ?
				plotViewManifestation.getTimeSystemChoices() :  new String[] { PlotConstants.DEFAULT_TIME_SYSTEM };
	}

	String[] getComponentSpecifiedTimeFormatChoices() {
		return (plotViewManifestation != null) ?
				plotViewManifestation.getTimeFormatChoices() : new String[] { PlotConstants.DEFAULT_TIME_FORMAT };
	}


			
	private abstract class AxisPanel extends JPanel {
		protected JPanel minMaxPanel[] = { new JPanel(), new JPanel() };
		protected JLabel minMaxLabel[] = { new JLabel(), new JLabel() };		
		protected JPanel spanPanel     = new JPanel();
		protected JLabel axisTitle     = new JLabel();
		
		protected SpringLayout layout  = new SpringLayout();
		
		public AxisPanel() {
			for (JLabel l : minMaxLabel) l.setFont(l.getFont().deriveFont(Font.BOLD));
			setLayout(layout);
			add(minMaxPanel[0]);
			add(minMaxPanel[1]);
			add(minMaxLabel[0]);
			add(minMaxLabel[1]);
			add(spanPanel);
			add(axisTitle);

		}
		
		public void clear() {
			for (JPanel p : minMaxPanel) p.removeAll();
			for (JLabel l : minMaxLabel) l.setText("");
			spanPanel.removeAll();
			axisTitle.setText("");
		}
		
		public void setFrom(AxisGroup group, boolean inverted) {
			clear();
			int min = inverted ? 1 : 0;
			int max = inverted ? 0 : 1;
			minMaxPanel[min].add(group.getMinControls());
			minMaxPanel[max].add(group.getMaxControls());
			minMaxLabel[min].setText(group.getMinText());
			minMaxLabel[max].setText(group.getMaxText());
			spanPanel.add(group.getSpanControls());
			axisTitle.setText("(" + group.getTitle() + ")");
			layout = new SpringLayout();
			setLayout(layout);
			initialLayout();
			revalidate();
		}
		
		public abstract void initialLayout();
	}
	
	private class AxisGroup {
		private JPanel minControls;
		private JPanel maxControls;
		private JPanel spanControls;
		private String minText = "Min";
		private String maxText = "Max";
		private String title = "Axis";

		public AxisGroup(JPanel minControls, JPanel maxControls, JPanel spanControls) {
			this.minControls = minControls;
			this.maxControls = maxControls;
			this.spanControls = spanControls;
		}
		
//		public double getMinimum() {
//			return getValue(true);
//		}
//		
//		public double getMaximum() {
//			return getValue(false);
//		}
		
//		private double getValue(boolean min) {
//			AxisRadioButtonPanel focus = min ? minControls : maxControls;
//			AxisRadioButtonPanel other = min ? maxControls : minControls;
//			
//			switch (focus.getSelected()) {
//			case CURRENT: return 0.0;
//			case MANUAL:  return 1.0; // field...
//			case AUTO:
//				if (isTime && min) return 0; // Now!
//				return getValue(!min) + (min ? -1 : 1) * 2.0; //Span! 
//			}
//			return 0;
//		}
		
		public JPanel getMinControls() {
			return minControls;
		}

		public JPanel getMaxControls() {
			return maxControls;
		}

		public JPanel getSpanControls() {
			return spanControls;
		}

		public String getMinText() {
			return minText;
		}

		public String getMaxText() {
			return maxText;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}
	
	// Convenience method for populating and applying a standard layout for multi-item rows
    private JPanel createMultiItemRow(final JComponent button, JComponent secondItem) {
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridx = 0;
    	gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    	if (button != null && button instanceof JRadioButton) {
    		((AbstractButton) button).setSelected(false);
    		panel.add(button, gbc);
    	} else if (button != null && button instanceof JComboBox){
    		panel.add(button, gbc);
    	}
    	if (secondItem != null) {
    		gbc.gridx = 1;
    		gbc.insets = new Insets(0, SPACE_BETWEEN_ROW_ITEMS, 0, 0);
    		gbc.weightx = 1;
    		panel.add(secondItem, gbc);
    	}
    	panel.setName("multiItemRow");
		return panel;
	}

}
