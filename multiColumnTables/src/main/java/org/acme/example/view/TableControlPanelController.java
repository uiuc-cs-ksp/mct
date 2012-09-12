package org.acme.example.view;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//rename to MultiColControlPanelController? Just ControlPanelController?
/**
 * Implements a controller that mediates between a multi-column table
 * and a control panel for that table.
 */
public class TableControlPanelController {
	private MultiColView viewManifestation;
	private MultiColTable table;
	private MultiColTableModel model;
	private ViewSettings settings;
	
	
	public TableControlPanelController(MultiColView viewManifestation, 
			MultiColTable table, MultiColTableModel model, ViewSettings settings) {
		this.viewManifestation = viewManifestation;
		this.table = table;
		this.model = model;
		this.settings = settings;
	}
	
	//haven't decided what index to add into yet
	public void addTableColumn(ColumnType colType) {
		TableColumn retrievedColumn = settings.retrieveColumn(colType.name());
		TableColumnModel columnModel = table.getTable().getColumnModel();  
		columnModel.addColumn(retrievedColumn);
	}
	
	public void removeTableColumn(ColumnType colType) {
		TableColumnModel columnModel = table.getTable().getColumnModel();
		int colIndex = columnModel.getColumnIndex(colType.name());		
		TableColumn column = columnModel.getColumn(colIndex);
		settings.hideColumn(column, colType.name());
		columnModel.removeColumn(column);		
	}
	
	
	
}
