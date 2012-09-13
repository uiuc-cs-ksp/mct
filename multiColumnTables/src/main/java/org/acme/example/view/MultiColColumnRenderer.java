package org.acme.example.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MultiColColumnRenderer extends DefaultTableCellRenderer {

	public MultiColColumnRenderer() {
		setHorizontalAlignment(JLabel.CENTER);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);
		label.setText(ColumnType.getDisplayName((String) value));
		//label.setBackground(Color.DARK_GRAY);
		//label.setForeground(Color.BLACK);
		return label;
	}
}
