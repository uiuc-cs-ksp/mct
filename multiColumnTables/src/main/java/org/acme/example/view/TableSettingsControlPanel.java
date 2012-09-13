package org.acme.example.view;

import gov.nasa.arc.mct.components.ExtendedProperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TableSettingsControlPanel extends JPanel {
	
	private TableControlPanelController controller;
	private MultiColView tableView;
	/** The settings object */
	private ViewSettings settings; 
	/** The resource bundle we should use for getting strings. */
	private static final ResourceBundle bundle = ResourceBundle.getBundle("MultiColResourceBundle"); //NOI18N

	//private TaggedComponentManager mgr;
	
	private JCheckBox idBox;
	private JCheckBox titleBox;
	private JCheckBox fswnameBox;
	private JCheckBox rawBox;
	private JCheckBox valueBox;
	private JCheckBox unitBox;
	private JCheckBox ertBox;
	private JCheckBox sclkBox;
	private JCheckBox scetBox;

	public TableSettingsControlPanel(
			MultiColView tableView, 
			TableControlPanelController controller, 
			ViewSettings settings) {
		this.tableView = tableView;
		this.controller = controller;
		this.settings = settings;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Columns to Show"));
		
		//mgr = new TaggedComponentManager();

		idBox = new JCheckBox(bundle.getString("ID"));
		titleBox = new JCheckBox(bundle.getString("TITLE"));
		fswnameBox = new JCheckBox(bundle.getString("FSW_NAME"));
		rawBox = new JCheckBox(bundle.getString("RAW"));
		valueBox = new JCheckBox(bundle.getString("VALUE"));
		unitBox = new JCheckBox(bundle.getString("UNIT"));
		ertBox = new JCheckBox(bundle.getString("ERT"));
		sclkBox = new JCheckBox(bundle.getString("SCLK"));
		scetBox = new JCheckBox(bundle.getString("SCET"));
		
		updateCheckBoxes();
		addCheckBoxListeners();
		
		//setAccessibleName(testCheckBox, "testing checkboxes");
		//mgr.tagComponents(TEST_TAG, testCheckBox);
		
		GridBagConstraints ch = new GridBagConstraints();
		ch.fill = GridBagConstraints.HORIZONTAL;
		ch.weightx = 1;
		add(Box.createHorizontalGlue(),ch);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		int y = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = y++;
		add(idBox,c);
		c.gridy = y++;
		add(titleBox,c);
		c.gridy = y++;
		add(fswnameBox,c);
		c.gridy = y++;
		add(rawBox,c);
		c.gridy = y++;
		add(valueBox,c);
		c.gridy = y++;
		add(unitBox,c);
		c.gridy = y++;
		add(ertBox,c);
		c.gridy = y++;
		add(sclkBox,c);
		c.gridy = y++;
		c.weighty = 1;
		add(scetBox,c);
	}
	
	//move to TCPC?
	private void updateCheckBoxes() {
		if(settings.isDisplayingColumn(ColumnType.ID))       { idBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.TITLE))    { titleBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.FSW_NAME)) { fswnameBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.RAW))      { rawBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.VALUE))    { valueBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.UNIT))     { unitBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.ERT))      { ertBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.SCLK))     { sclkBox.setSelected(true); }
		if(settings.isDisplayingColumn(ColumnType.SCET))     { scetBox.setSelected(true); }
	}
	
	//private final ChangeListener titleChangeListener;
	private void addCheckBoxListeners() {
		/*addActionListenerToCheckBox(idBox, ColumnType.ID);
		addActionListenerToCheckBox(titleBox, ColumnType.TITLE);
		addActionListenerToCheckBox(fswnameBox, ColumnType.FSW_NAME);
		addActionListenerToCheckBox(rawBox, ColumnType.RAW);
		addActionListenerToCheckBox(valueBox, ColumnType.VALUE);
		addActionListenerToCheckBox(unitBox, ColumnType.UNIT);
		addActionListenerToCheckBox(ertBox, ColumnType.ERT);
		addActionListenerToCheckBox(sclkBox, ColumnType.SCLK);
		addActionListenerToCheckBox(scetBox, ColumnType.SCET);*/
		idBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(idBox.isSelected()) { controller.addTableColumn(ColumnType.ID); }
				else { controller.removeTableColumn(ColumnType.ID); }
				saveColumnVisibilityStates();
			}
		});
		titleBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(titleBox.isSelected()) { controller.addTableColumn(ColumnType.TITLE); }
				else { controller.removeTableColumn(ColumnType.TITLE); }
				saveColumnVisibilityStates();
			}
		});
		fswnameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fswnameBox.isSelected()) { controller.addTableColumn(ColumnType.FSW_NAME); }
				else { controller.removeTableColumn(ColumnType.FSW_NAME); }
				saveColumnVisibilityStates();
			}
		});
		rawBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rawBox.isSelected()) { controller.addTableColumn(ColumnType.RAW); }
				else { controller.removeTableColumn(ColumnType.RAW); }
				saveColumnVisibilityStates();
			}
		});
		valueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(valueBox.isSelected()) { controller.addTableColumn(ColumnType.VALUE); }
				else { controller.removeTableColumn(ColumnType.VALUE); }
				saveColumnVisibilityStates();
			}
		});
		unitBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(unitBox.isSelected()) { controller.addTableColumn(ColumnType.UNIT); }
				else { controller.removeTableColumn(ColumnType.UNIT); }
				saveColumnVisibilityStates();
			}
		});
		ertBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ertBox.isSelected()) { controller.addTableColumn(ColumnType.ERT); }
				else { controller.removeTableColumn(ColumnType.ERT); }
				saveColumnVisibilityStates();
			}
		});
		sclkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sclkBox.isSelected()) { controller.addTableColumn(ColumnType.SCLK); }
				else { controller.removeTableColumn(ColumnType.SCLK); }
				saveColumnVisibilityStates();
			}
		});
		scetBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(scetBox.isSelected()) { controller.addTableColumn(ColumnType.SCET); }
				else { controller.removeTableColumn(ColumnType.SCET); }
				saveColumnVisibilityStates();
			}
		});
	}
	
	private void saveColumnVisibilityStates() {		
		ExtendedProperties viewProperties = tableView.getViewProperties();
		Set<Object> p = viewProperties.getProperty(MultiColView.HIDDEN_COLUMNS_PROP);
		if (p == null)
			viewProperties.addProperty(MultiColView.HIDDEN_COLUMNS_PROP, "");
		p = viewProperties.getProperty(MultiColView.HIDDEN_COLUMNS_PROP);
		p.clear();
		for (String id : settings.getHiddenColumnIds())
			p.add(id);
		tableView.getManifestedComponent().save();
	}
	private void addActionListenerToCheckBox(final JCheckBox checkBox, final ColumnType colType) {
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox.isSelected()) {  }
				else { controller.removeTableColumn(colType); }
			}
		});
	}
	
	private void updateColumnList() {
		if(idBox.isSelected()) { settings.addColumnType(ColumnType.ID); }
		else { settings.removeColumnType(ColumnType.ID); }
		//...
	}
	
	public void updateColumnVisibilityStates(Collection<String> columnIdentifiers) {
		for (String id : columnIdentifiers) {
			if (id.equals(ColumnType.ID.name()))
				idBox.setSelected(false);
			else if (id.equals(ColumnType.TITLE.name())) 
				titleBox.setSelected(false);
			else if (id.equals(ColumnType.FSW_NAME.name())) 
				fswnameBox.setSelected(false);
			else if (id.equals(ColumnType.UNIT.name())) 
				unitBox.setSelected(false);
			else if (id.equals(ColumnType.RAW.name())) 
				rawBox.setSelected(false);
			else if (id.equals(ColumnType.VALUE.name())) 
				valueBox.setSelected(false);
			else if (id.equals(ColumnType.ERT.name())) 
				ertBox.setSelected(false);
			else if (id.equals(ColumnType.SCLK.name())) 
				sclkBox.setSelected(false);			
			else if (id.equals(ColumnType.SCET.name())) 
				scetBox.setSelected(false);
		}
	}
}
