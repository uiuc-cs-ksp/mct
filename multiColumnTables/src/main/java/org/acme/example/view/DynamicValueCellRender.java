package org.acme.example.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class DynamicValueCellRender extends DefaultTableCellRenderer {
	
	public DynamicValueCellRender() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		assert value instanceof DisplayedValue;
		DisplayedValue displayedValue = (DisplayedValue) value;
		label.setForeground(displayedValue.getColor());
		
		return label;
		
	}
}
