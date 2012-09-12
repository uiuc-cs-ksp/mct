package org.acme.example.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableColumn;

public class ViewSettings {
	
	//which columns are currently being displayed.
	private ArrayList<ColumnType> columnTypes;
	
	private Map<String,TableColumn> hiddenColumns;
	
	public ViewSettings() {
		setAllToDefaults();
		hiddenColumns = new HashMap<String,TableColumn>();
		
	}
	
	public void setAllToDefaults() {
		columnTypes = new ArrayList<ColumnType>();
		columnTypes.add(ColumnType.ID);
		columnTypes.add(ColumnType.TITLE);
		columnTypes.add(ColumnType.FSW_NAME);
		columnTypes.add(ColumnType.RAW);
		columnTypes.add(ColumnType.VALUE);		
		columnTypes.add(ColumnType.UNIT);
		columnTypes.add(ColumnType.ERT);
		columnTypes.add(ColumnType.SCLK);
		columnTypes.add(ColumnType.SCET);
	}
	
	public void hideColumn(TableColumn column, String identifier) {
		hiddenColumns.put(identifier, column);
	}
	
	public TableColumn retrieveColumn(String identifier) {
		TableColumn column = hiddenColumns.get(identifier);
		hiddenColumns.remove(identifier);
		return column;
	}
	
	public int getNumberOfColumns() { return columnTypes.size(); }// - hiddenColumns.size(); }
	
	public ColumnType getColumnAtIndex(int colIndex) {
		return columnTypes.get(colIndex);
	}
	
	public int getIndexForColumn(ColumnType colValue) {
		return columnTypes.indexOf(colValue);
	}

	public boolean isDisplayingColumn(ColumnType colValue) {
		return columnTypes.contains(colValue);
	}
		
	public void addColumnType(ColumnType colValue) {
		columnTypes.add(colValue);
	}
	
	public boolean removeColumnType(ColumnType colValue) {
		return columnTypes.remove(colValue);
	}
	
	public ArrayList<ColumnType> getColumnTypes() {
		return columnTypes;
	}
	
}


