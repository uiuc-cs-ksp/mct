package org.acme.example.view;

import javax.swing.SwingConstants;

/**
 * The content alignment of cell or header data.
 */
public enum ContentAlignment {
	/** Content should be left aligned. */
	LEFT(SwingConstants.LEFT),
	/** Content should be centered. */
	CENTER(SwingConstants.CENTER),
	/** Content should be right aligned. */
	RIGHT(SwingConstants.RIGHT),
	/** Content should be decimal aligned. */
	DECIMAL(SwingConstants.RIGHT);
	
	private int componentAlignment = SwingConstants.LEFT;
	
	private ContentAlignment(int componentAlignment) {
		this.componentAlignment = componentAlignment;
	}
	
	/**
	 * Returns the component alignment value needed to match this
	 * alignment enumerated value.
	 * 
	 * @return the component alignment corresponding to the alignment enumeration constant
	 */
	public int getComponentAlignment() {
		return componentAlignment;
	}
	
}