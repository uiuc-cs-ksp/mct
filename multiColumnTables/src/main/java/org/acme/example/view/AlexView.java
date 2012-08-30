package org.acme.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.io.*;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.ExtendedProperties;
import gov.nasa.arc.mct.components.FeedProvider;
import gov.nasa.arc.mct.components.TimeConversion;
import gov.nasa.arc.mct.components.FeedProvider.FeedType;
import gov.nasa.arc.mct.components.FeedProvider.RenderingInfo;
import gov.nasa.arc.mct.gui.FeedView;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.services.component.ViewInfo;

public class AlexView extends FeedView {
	private AlexTable table;
	private TableModelTest model;

	public AlexView(AbstractComponent ac, ViewInfo vi) {
		super(ac,vi);

		JPanel view = new JPanel(); //rename 'panel'?
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

		// Add the header for this view manifestation. remove later.
		view.add(createHeaderRow("Multicolumn Table", Color.black, 19));

		// Add the content for this view manifestation.
		AbstractComponent component = getManifestedComponent();
		table = new AlexTable(component); //misleading var name?+++
		table.setOpaque(true);
		view.add(table);
		
		//init model
		model = new TableModelTest(component);
		//deal with label stuff
		
		setLayout(new BorderLayout());
		add(view, BorderLayout.NORTH);
	}
	
	private MultiColumnSettings getCurrentTableSettings(AlexTable table) {
		MultiColumnSettings settings = new MultiColumnSettings();
		settings.setColumnWidths(table.getColumnWidths());
		settings.setColumnOrder(table.getColumnOrder());
		settings.setShowGrid(table.getShowGrid());
		settings.setRowHeights(integerToIntArray(table.getRowHeights()));
		settings.setRowHeaderAlignments(table.getRowHeaderAlignments());
		settings.setColumnHeaderAlignments(table
				.getColummnHeaderAlignments());
		settings.setRowFontNames(table.getRowHeaderFontNames());
		settings.setRowFontColors(colorToIntArray(table.getRowHeaderFontColors()));
		settings.setRowHeaderBorderColors(colorToIntArray(table.getRowHeaderBorderColors()));
		settings.setRowBackgroundColors(colorToIntArray(table.getRowHeaderBackgroundColors()));
		settings.setRowFontSizes(integerToIntArray(table.getRowHeaderFontSizes()));
		settings.setRowFontStyles(integerToIntArray(table.getRowHeaderFontStyles()));
		settings.setRowTextAttributes(integerToIntArray(table.getRowHeaderTextAttributes()));
		settings.setColumnFontNames(table.getColumnHeaderFontNames());
		settings.setColumnFontColors(colorToIntArray(table.getColumnHeaderFontColors()));
		settings.setColumnBackgroundColors(colorToIntArray(table.getColumnHeaderBackgroundColors()));
		settings.setColumnHeaderBorderColors(colorToIntArray(table.getColumnHeaderBorderColors()));
		settings.setColumnFontSizes(integerToIntArray(table.getColumnHeaderFontSizes()));
		settings.setColumnFontStyles(integerToIntArray(table.getColumnHeaderFontStyles()));
		settings.setColumnTextAttributes(integerToIntArray(table.getColumnHeaderTextAttributes()));
		settings.setRowHeaderBorderStates(table.getRowHeaderBorderStates());
		settings.setColumnHeaderBorderStates(table.getColumnHeaderBorderStates());

		return settings;
	}

	
	private void saveSettingsToPersistence() {
		boolean settingsChanged = false;
		MultiColumnSettings settings = getCurrentTableSettings(table);
		ExtendedProperties viewProperties = getViewProperties();
		for (MultiColumnSettings.AvailableSettings setting : MultiColumnSettings.AvailableSettings
				.values()) {
			String currentValue = getViewProperties().getProperty(
					setting.name(), String.class);
			String newValue = settings.getValue(setting);
			assert (newValue != null) : "Table setting for "
					+ setting.toString() + " has null value";
			if (!newValue.equals(currentValue)) {
				viewProperties.setProperty(setting.name(),
						newValue);
				settingsChanged = true;
			}
		}

		// save all cell settings.
		for (int row = 0; row < model.getRowCount(); ++row) {
			for (int col = 0; col < model.getColumnCount(); ++col) {
				AbstractComponent component = (AbstractComponent) model
						.getStoredObjectAt(row, col);
				if (component != null) {
					settingsChanged = (saveCellSettings(component) || settingsChanged);
				}
			}
		}

		if (settingsChanged) {
		    try {
		        updating = true;
		        table.updateColumnsHeaderValuesOnly();
		    	getManifestedComponent().save();
		    } finally {
		        updating = false;
		    }
		}
	}
	
	
	
	//useless for multicolumn? prolly not actually
	// Creates a formatted JPanel that contains the header in a JLabel
	private JPanel createHeaderRow(String title, Color color, float size) {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(title);
		label.setFont(label.getFont().deriveFont(Font.BOLD, size));
		label.setForeground(color);
		panel.add(label, BorderLayout.WEST);
		return panel;
	}
	
	private int[] colorToIntArray(Color[] a) {
		int[] newArray = new int[a.length];
		for (int i = 0; i < a.length ; i++) {
			newArray[i] = a[i].getRGB();
		}
		return newArray;
	}
	
	private int[] integerToIntArray(Integer[] a) {
		int[] newArray = new int[a.length];
		for (int i = 0; i < a.length ; i++) {
			newArray[i] = a[i].intValue();
		}
		return newArray;
	}

	@Override
	public void updateFromFeed(Map<String, List<Map<String, String>>> data) {
		// TODO Auto-generated method stub
		//not sure where i store & access everything, but:
		//this is called, giving me a ptr to the huge Map,
		//and i update the telemetry object, etc. (but what if more than one?)
		//I should look at other feedview descendants for advice. 
		//Now for a /really/ naive copy paste from TableViewManifestation:++++
		if (data != null) {
			Collection<FeedProvider> feeds = getVisibleFeedProviders();
			for (FeedProvider provider : feeds) {
				String feedId = provider.getSubscriptionId();
				List<Map<String, String>> dataForThisFeed = data
						.get(feedId);
				if (dataForThisFeed != null && !dataForThisFeed.isEmpty()) {
					// Process the first value for this feed.
					Map<String, String> entry = dataForThisFeed
							.get(dataForThisFeed.size() - 1);
					try {
						Object value = entry
								.get(FeedProvider.NORMALIZED_VALUE_KEY);
						RenderingInfo ri = provider.getRenderingInfo(entry);
					} catch (ClassCastException ex) {
						System.err.println("Feed data entry of unexpected type");
					} catch (NumberFormatException ex) {
						System.err.println("Feed data entry does not contain parsable value");
					}
				}
			}
		} else {
			System.err.println("Data was null");
		}	
	}

	@Override
	public void synchronizeTime(Map<String, List<Map<String, String>>> data,
			long syncTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<FeedProvider> getVisibleFeedProviders() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected JComponent initializeControlManifestation() {
//		copied from tableview version
		TableControlPanelController controller = new TableControlPanelController(
				this, table, model);
		return new TableSettingsControlPanel(controller);
	}
}
