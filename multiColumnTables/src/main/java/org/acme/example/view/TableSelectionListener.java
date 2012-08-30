package org.acme.example.view;
import java.util.EventListener;

/**
 * Defines a callback interface for listening for selection changes
 * in a table.
 */
public interface TableSelectionListener extends EventListener {

	/**
	 * Notify the listener that the selection has changed.
	 * 
	 * @param selectedRows the row indices in the new selection
	 * @param selectedColumns the column indices in the new selection
	 */
	void selectionChanged(int[] selectedRows, int[] selectedColumns);
	
}
