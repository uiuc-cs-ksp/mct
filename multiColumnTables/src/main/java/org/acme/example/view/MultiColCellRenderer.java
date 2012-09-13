package org.acme.example.view;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

public class MultiColCellRenderer extends DefaultTableCellRenderer {
	public MultiColCellRenderer() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
}
