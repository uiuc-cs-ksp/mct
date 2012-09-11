package org.acme.example.view;

import java.util.ArrayList;

public class ViewSettings {
	
	//which columns are currently being displayed.
	private ArrayList<ColumnType> columnTypes;
	
	public ViewSettings() {
		setAllToDefaults();
		
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
	
	/*public String getColumnDisplayName(ColumnType colType) {
		switch(colType) {
		case ID:
			return "ID";
		case VALUE: 
			return "Value";
		case TITLE: 
			return "Title";
		case ALARM_STATE: 
			return "Alarm State";
		case ERT:
			return "ERT";
		default:
			return "(unknown type)";
		}
	}*/
	
	public int getNumberOfColumns() { return columnTypes.size(); }
	
	public ColumnType getColumnAtIndex(int colIndex) {
		return columnTypes.get(colIndex);
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


