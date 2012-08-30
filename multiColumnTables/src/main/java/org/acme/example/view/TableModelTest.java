package org.acme.example.view;

import gov.nasa.arc.mct.components.AbstractComponent;

import java.util.List;

import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;

class TableModelTest extends AbstractTableModel {
	private String[] colTitles = {"ID", "Owner", "Original Owner", "Creator",
			"Creation Date", "Display Name"};
	private AbstractComponent selectedComponent; //is this name confusing because it's the same as in AlexView?
	List<AbstractComponent> childrenList;
	
	public TableModelTest(AbstractComponent componentin) {
		selectedComponent = componentin;
		childrenList = selectedComponent.getComponents();
		if(childrenList.size()==0) {
//			childrenList = new ArrayList<AbstractComponent>();
			childrenList.add(selectedComponent);
		}
		//this might only refresh each time it's opened -- naive?+++
	}
					
	public int getColumnCount() { return colTitles.length; }
	public int getRowCount()    { return childrenList.size(); }
	public String getColumnName(int i) { return colTitles[i]; }
	public Object getValueAt(int r, int c) {
		AbstractComponent cellComponent = childrenList.get(r);
		Object cellDatum = null;
		switch(c) {
		case 0:
			cellDatum = cellComponent.getId(); break;				
		case 1:
			cellDatum = cellComponent.getOwner(); break;
		case 2:
			cellDatum = cellComponent.getOriginalOwner(); break;
		case 3:
			cellDatum = cellComponent.getCreator(); break;
		case 4:
			cellDatum = cellComponent.getCreationDate(); break;
		case 5:
			cellDatum = cellComponent.getDisplayName(); break;
		}
		if(cellDatum==null) { return "(no data)"; }
		else                { return cellDatum; }
	}
	public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	
	//copied somewhat blindly from labeledtablemodel.java:
	/**
	 * Adds a listener for events fired when the labels have been updated.
	 * 
	 * @param listener the label change listener.
	 */
	public void addLabelChangeListener(LabelChangeListener listener) {
		listenerManager.addListener(LabelChangeListener.class, listener);
	}
	
	/**
	 * Removes the listener for events.
	 * @param listener the label change listener.
	 */
	public void removeLabelChangeListener(LabelChangeListener listener) {
		listenerManager.removeListener(LabelChangeListener.class, listener);
	}
	
	/**
	 * Gets the table type.
	 * @return 2-D TableType 
	 */
	public TableType getTableType() {
		return TableType.TWO_DIMENSIONAL;
	}

	protected Object getStoredObjectAt(int objectIndex, int attributeIndex) {
		return getValueAt(objectIndex, attributeIndex);
	}
	
	/**
	 * Handles an event where the row, column, or cell labels have changed.
	 * Triggers a structure changed event so that the table will be redrawn
	 * completely.
	 */
	public void fireLabelsChanged() {
		rowLabelModel.fireLabelsChanged();
		columnLabelModel.fireLabelsChanged();
		listenerManager.fireEvent(LabelChangeListener.class, new ListenerNotifier<LabelChangeListener>() {
			@Override
			public void notifyEvent(LabelChangeListener listener) {
				listener.labelsChanged();
			}
		});
	}

	@Override
	public void fireTableCellUpdated(int row, int column) {
		super.fireTableCellUpdated(row, column);
	}

	/**
	 * Recalculates all row, column, and cell labels. This method is executed
	 * automatically when the table model changes. However, the user can
	 * call this method at other times, if desired.
	 */
	public void updateLabels() {
		rowLabels = new String[getRowCount()];
		columnLabels = new String[getColumnCount()];
		cellLabels = new String[getRowCount()][getColumnCount()];
		
		algorithm.computeLabels(this);
		rowLabelModel.fireLabelsChanged();
		columnLabelModel.fireLabelsChanged();
	}
	
	/**
	 * Tests whether there are row labels. If any row label is nonempty,
	 * then we have row labels.
	 * 
	 * @return true, if at least one row label is nonempty
	 */
	public boolean hasRowLabels() {
		if (rowLabels == null) {
			return true; // Don't yet have labels.
		}
		
		for (String label : rowLabels) {
			if (!label.isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Tests whether there are column labels. If any column label is nonempty,
	 * then we have column labels.
	 * 
	 * @return true, if at least one column label is nonempty
	 */
	public boolean hasColumnLabels() {
		if (columnLabels == null) {
			return true; // Don't yet have defined labels.
		}
		
		for (String label : columnLabels) {
			if (!label.isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets a row label, possibly abbreviated.
	 * 
	 * @param rowIndex the index of the row for which to get the label
	 * @return the row label
	 */
	public String getRowName(int rowIndex) {
		return getFullRowName(rowIndex);
	}
	
	/**
	 * Gets the unabbrevaited label for a row.
	 * 
	 * @param rowIndex the index of the row for which to get the label
	 * @return the row label
	 */
	public String getFullRowName(int rowIndex) {
		if (rowLabels==null || rowIndex >= rowLabels.length) {
			return ""; // Must not have updated labels yet.
		} else {
			return rowLabels[rowIndex];
		}
	}
	
	/**
	 * Sets a row label. This is designed to be called by the labeling
	 * algorithm.
	 * 
	 * @param rowIndex the index of the row label to set
	 * @param label the new row label
	 */
	void setRowName(int rowIndex, String label) {
		rowLabels[rowIndex] = label;
	}
	
	/**
	 * Gets the unabbreviated label for a column.
	 * 
	 * @param columnIndex the index of the column for which to get the label
	 * @return the column label
	 */
	public String getFullColumnName(int columnIndex) {
		if (columnLabels==null || columnIndex >= columnLabels.length) {
			return ""; // Must not have updated labels yet.
		} else {
			return columnLabels[columnIndex];
		}
	}
	
	/**
	 * Sets a column label. This is designed to be called by the labeling
	 * algorithm.
	 * 
	 * @param columnIndex the index of the column label to set
	 * @param label the new column label
	 */
	void setColumnName(int columnIndex, String label) {
		columnLabels[columnIndex] = label;
	}
	
	/**
	 * Gets a cell label, possibly abbreviated.
	 * 
	 * @param rowIndex the index of the row containing the cell
	 * @param columnIndex the index of the column containing the cell
	 * @return the cell label
	 */
	public String getCellName(int rowIndex, int columnIndex) {
		return getFullCellName(rowIndex, columnIndex);
	}

	/**
	 * Gets the unabbreviated label for a cell.
	 * 
	 * @param rowIndex the index of the row containing the cell
	 * @param columnIndex the index of the column containing the cell
	 * @return the cell label
	 */
	public final String getFullCellName(int rowIndex, int columnIndex) {
		if (rowIndex >= cellLabels.length || columnIndex >= cellLabels[rowIndex].length) {
			return "";
		} else {
			return cellLabels[rowIndex][columnIndex];
		}
	}

	/**
	 * Gets a label for an object in a cell. This method takes into consideration
	 * the orientation of the table.
	 * 
	 * @param objectIndex the index of the object within the table
	 * @param attributeIndex the index of the attribute object
	 * @return the label for the object
	 */
	String getObjectName(int objectIndex, int attributeIndex) {
			return getCellName(objectIndex, attributeIndex);
	}
	
	/**
	 * Sets a cell label. This is designed to be called by the labeling
	 * algorithm.
	 * 
	 * @param rowIndex the index of the row containing the cell
	 * @param columnIndex the index of the column containing the cell
	 * @param label the new cell label
	 */
	void setCellName(int rowIndex, int columnIndex, String label) {
		cellLabels[rowIndex][columnIndex] = label;
	}

	/**
	 * Gets a list model for the row labels.
	 * 
	 * @return the list model
	 */
	public ListModel getRowLabelModel() {
		return rowLabelModel;
	}

	/**
	 * Gets a list model for the column labels.
	 * 
	 * @return the list model
	 */
	public ListModel getColumnLabelModel() {
		return columnLabelModel;
	}
	
	/**
	 * Gets the unique identifier for the data in the indicated cell.
	 * The unique identifier is often used with a table labeling
	 * algorithm to determine abbreviated row, column, and cell
	 * labels.
	 * 
	 * @param rowIndex the row containing the cell
	 * @param columnIndex the column containing the cell
	 * @return the unique identifier for the cell
	 */
	public final String getIdentifierAt(int rowIndex, int columnIndex) {
		return getObjectIdentifierAt(rowIndex, columnIndex);			
	}
	
	/**
	 * Tests whether a value can be placed into a position within a table. The
	 * new position may cause a row or column to be inserted.
	 * 
	 * @param rowIndex the row at which to place the new item
	 * @param columnIndex the column at which to place the new item
	 * @param isInsertRow true, if a new row should be inserted above the position
	 * @param isInsertColumn true, if a new column should be inserted to the left of the position
	 * @return true, if a value can be placed or inserted at the position
	 */
	public final boolean canSetValueAt(int rowIndex, int columnIndex, boolean isInsertRow, boolean isInsertColumn) {
		return canSetObjectAt(rowIndex, columnIndex, isInsertRow, isInsertColumn);			
	}
	
	/**
	 * Sets the object value at specific row and column indices along with isInsertRow and isInsertColumn boolean flags.
	 * @param aValue the object value.
	 * @param rowIndex the row index.
	 * @param columnIndex the column index.
	 * @param isInsertRow boolean flag to check for whether can insert row.
	 * @param isInsertColumn boolean flag to check for whether can insert column.
	 */
	public final void setValueAt(Object aValue, int rowIndex, int columnIndex, boolean isInsertRow, boolean isInsertColumn) {
		setObjectAt(aValue, rowIndex, columnIndex, isInsertRow, isInsertColumn);			
		updateLabels();
	}

}