package org.acme.example.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class MultiColColumnRenderer extends DefaultTableCellRenderer {

	public MultiColColumnRenderer() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
		label.setText(ColumnType.getDisplayName((String) value));
		return label;
	}
}
