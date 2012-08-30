package org.acme.example.view;
import java.util.EventListener;

/**
 * Defines a callback for listeners interested in when the
 * visual layout of a table has been changed by the user.
 * Visual layout changes include modifying the column order
 * and changing the width of a column.
 */
public interface TableLayoutListener extends EventListener {

	/**
	 * Notify listeners that the table visual layout has changed.
	 * 
	 * @param source the component that is the source of the event
	 */
	void tableChanged(Object source);
	
}
