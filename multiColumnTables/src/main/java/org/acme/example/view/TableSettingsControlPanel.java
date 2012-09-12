package org.acme.example.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TableSettingsControlPanel extends JPanel {
	private static final String TEST_TAG = "test-tag";
	
	private TableControlPanelController controller;
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

	public TableSettingsControlPanel(TableControlPanelController controller, ViewSettings settings) {
		this.controller = controller;
		this.settings = settings;
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
		add(idBox);
		add(titleBox);
		add(fswnameBox);
		add(rawBox);
		add(valueBox);
		add(unitBox);
		add(ertBox);
		add(sclkBox);
		add(scetBox);
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
			}
		});
		titleBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(titleBox.isSelected()) { controller.addTableColumn(ColumnType.TITLE); }
				else { controller.removeTableColumn(ColumnType.TITLE); }
			}
		});
		fswnameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fswnameBox.isSelected()) { controller.addTableColumn(ColumnType.FSW_NAME); }
				else { controller.removeTableColumn(ColumnType.FSW_NAME); }
			}
		});
		rawBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rawBox.isSelected()) { controller.addTableColumn(ColumnType.RAW); }
				else { controller.removeTableColumn(ColumnType.RAW); }
			}
		});
		valueBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(valueBox.isSelected()) { controller.addTableColumn(ColumnType.VALUE); }
				else { controller.removeTableColumn(ColumnType.VALUE); }
			}
		});
		unitBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(unitBox.isSelected()) { controller.addTableColumn(ColumnType.UNIT); }
				else { controller.removeTableColumn(ColumnType.UNIT); }
			}
		});
		ertBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ertBox.isSelected()) { controller.addTableColumn(ColumnType.ERT); }
				else { controller.removeTableColumn(ColumnType.ERT); }
			}
		});
		sclkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sclkBox.isSelected()) { controller.addTableColumn(ColumnType.SCLK); }
				else { controller.removeTableColumn(ColumnType.SCLK); }
			}
		});
		scetBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(scetBox.isSelected()) { controller.addTableColumn(ColumnType.SCET); }
				else { controller.removeTableColumn(ColumnType.SCET); }
			}
		});
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
	
	
}
