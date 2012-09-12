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
		
		//testing how arraylists behave
		List<String> arrayListTest = new ArrayList<String>();
		arrayListTest.add("one");
		arrayListTest.remove("one");
		arrayListTest.remove("one");
		arrayListTest.remove("two");
		
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
		idBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//updateColumnList();
				//move to controller TCPC?
				if(idBox.isSelected()) {  }
				else { controller.removeTableColumn(ColumnType.ID); }
				
				/*if(idBox.isSelected()) { settings.removeColumnType(ColumnType.ID); }
				else { settings.addColumnType(ColumnType.ID); }*/
			}
		});
		//didn't work. I'll try a changelistener:
		/*titleBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(titleBox.isSelected()) { settings.removeColumnType(ColumnType.TITLE); }
				else { settings.addColumnType(ColumnType.TITLE); }
			}
		});*/
		//...
	}
	
	private void updateColumnList() {
		if(idBox.isSelected()) { settings.addColumnType(ColumnType.ID); }
		else { settings.removeColumnType(ColumnType.ID); }
		//...
	}
	
	
}
