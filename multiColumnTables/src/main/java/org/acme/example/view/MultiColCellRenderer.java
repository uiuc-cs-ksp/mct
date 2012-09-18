package org.acme.example.view;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class MultiColCellRenderer extends DefaultTableCellRenderer {
	public MultiColCellRenderer() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
}
