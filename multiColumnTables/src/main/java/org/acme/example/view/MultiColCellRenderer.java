package org.acme.example.view;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

//This cell renderer is for cells that do not update from a feed. 

@SuppressWarnings("serial")
public class MultiColCellRenderer extends DefaultTableCellRenderer {
	public MultiColCellRenderer() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
}
