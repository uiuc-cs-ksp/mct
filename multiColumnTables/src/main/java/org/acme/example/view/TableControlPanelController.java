package org.acme.example.view;

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
	
	
	public TableControlPanelController(MultiColView viewManifestation, 
			MultiColTable table, MultiColTableModel model) {
		this.viewManifestation = viewManifestation;
		this.table = table;
		this.model = model;
		
	}
	
	//TableColumnModel indexes never change
	public void removeTableColumn(ColumnType type) {
		TableColumnModel columnModel = table.getTable().getColumnModel();
		int colIndex = columnModel.getColumnIndex(type.name());		
		columnModel.removeColumn(columnModel.getColumn(colIndex));		
	}
	
	
	
}
