package org.acme.example.view;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * Implements a skeleton table column model listener that ignores all
 * the events. Subclasses can override the specific methods for events
 * that need processing.
 */
public class TableColumnModelAdapter implements TableColumnModelListener {

	@Override
	public void columnAdded(TableColumnModelEvent e) {
		// do nothing
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
		// do nothing
	}

	@Override
	public void columnMoved(TableColumnModelEvent e) {
		// do nothing
	}

	@Override
	public void columnRemoved(TableColumnModelEvent e) {
		// do nothing
	}

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
		// do nothing
	}

}
