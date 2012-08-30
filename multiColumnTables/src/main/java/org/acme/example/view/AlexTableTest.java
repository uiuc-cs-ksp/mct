package org.acme.example.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

//This class is copyright oracle corporation! For testing & debug only. 
public class AlexTableTest extends JPanel {
	public AlexTableTest() {
		super(new GridLayout(1,0));

		JTable table = new JTable(new MyTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(500, 90));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);
	}

	class MyTableModel extends AbstractTableModel {
		private String[] columnNames = {"First Name",
				"Last Name",
				"Sport",
				"# of Years",
	        	"Vegetarian"};
		private Object[][] data = {
				{"Kathy", "Smith",
					"Snowboarding", new Integer(5), new Boolean(false)},
				{"John", "Doe",
					"Rowing", new Integer(3), new Boolean(true)},
				{"Sue", "Black",
					"Knitting", new Integer(2), new Boolean(false)},
				{"Jane", "White",
					"Speed reading", new Integer(20), new Boolean(true)},
				{"Joe", "Brown",
					"Pool", new Integer(10), new Boolean(false)}
		};

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}
	}
}
