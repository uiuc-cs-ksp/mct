package org.acme.example.view;

/**
 * Defines the types of tabular structures we can display.
 */
public enum TableType {
	
	//later, change the contents of this class to better suit multicolumntables+++

	/**
	 * Indicates that the table is displaying a single value.
	 */
	ZERO_DIMENSIONAL,
	
	/**
	 * Indicates that the table is displaying a 1-dimensional vector
	 * of values.
	 */
	ONE_DIMENSIONAL,
	
	/**
	 * Indicates that the table is displaying a nested, 2-dimensional
	 * array of values.
	 */
	TWO_DIMENSIONAL
	
}
