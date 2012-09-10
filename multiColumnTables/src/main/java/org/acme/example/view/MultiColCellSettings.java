package org.acme.example.view;

import gov.nasa.arc.mct.components.AbstractComponent;

import java.awt.Color;
import java.text.SimpleDateFormat;

/**
 * Implements a holder of cell formatting settings.
 */

public class MultiColCellSettings {
	
	/** The default number of decimals to use when displaying numeric values. */
	public static final int DEFAULT_DECIMALS = 2;

	private ContentAlignment alignment = ContentAlignment.LEFT;
	private int numberOfDecimals = DEFAULT_DECIMALS;
	private AbstractComponent evaluator;
	private int fontStyle = TableFormattingConstants.defaultFontStyle;
	private int fontSize = TableFormattingConstants.defaultFontSize;
	private Color foregroundColor;
	private Color backgroundColor;
	private int textAttributeUnderline = TableFormattingConstants.UNDERLINE_OFF;
	
	/**
	 * Gets the cell content alignment.
	 * 
	 * @return the content alignment
	 */
	public ContentAlignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Sets the cell content alignment.
	 * 
	 * @param alignment the new alignment
	 */
	public void setAlignment(ContentAlignment alignment) {
		this.alignment = alignment;
	}
	
	/**
	 * Gets the number of decimal places to display.
	 * 
	 * @return the number of decimals to show
	 */
	public int getNumberOfDecimals() {
		return numberOfDecimals;
	}
	
	/**
	 * Sets the number of decimals to display.
	 * 
	 * @param numberOfDecimals the number of decimals to show
	 */
	public void setNumberOfDecimals(int numberOfDecimals) {
		this.numberOfDecimals = numberOfDecimals;
	}
		
	/**
	 * Gets the evaluator for the cell value, if any.
	 * 
	 * @return the evaluator to use, or null for no evaulator
	 */
	public AbstractComponent getEvaluator() {
		return evaluator;
	}
	
	/**
	 * Sets the evaluator for the cell value.
	 * 
	 * @param evaluator the evaluator, or null for no evaluator
	 */
	public void setEvaluator(AbstractComponent evaluator) {
		this.evaluator = evaluator;
	}

	/**Get the font style for this cell
	 * @return fontStyle the font style for this cell
	 */
	public int getFontStyle() {
		return fontStyle;
	}
	
	/**Get the font textAttribute for this cell
	 * @return textAttributeUnderline the font text attribute for this cell
	 */
	public int getTextAttributeUnderline() {
		return textAttributeUnderline;
	}

	/** Set the font style for this cell
	 * @param fontStyle the font style for this cell
	 */
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	
	/** Set the font style for this cell
	 * @param textAttribute the font style for this cell
	 */
	public void setTextAttributeUnderline(int textAttribute) {
		this.textAttributeUnderline = textAttribute;
	}

	/**Get the foreground color for this cell
	 * @return the foreground color for this cell
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**Set the foreground color for this cell
	 * @param foregroundColor
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	
	/**Get the background color for this cell
	 * @return the background color for this cell
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**Set the background color for this cell
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**Get the font size for this cell
	 * @return the font size for this cell
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**Set the font size for this cell
	 * @param fontSize font size for this cell
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	/**Get the font color for this cell
	 * Same as getForegroundColor, for font objects
	 * @return the font color for this cell
	 */
	public Color getFontColor() {
		return foregroundColor;
	}

	/**Set the font color for this cell
	 * @param afontColor font size for this cell
	 */
	public void setFontColor(Color afontColor) {
		this.foregroundColor = afontColor;
	}
	
}
