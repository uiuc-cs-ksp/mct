package org.acme.example.view;

import gov.nasa.arc.mct.components.FeedProvider;

import java.awt.Component;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class TimeCellRender extends DefaultTableCellRenderer {

	private Format TIMEFormatter = new SimpleDateFormat("HH:mm:ss.S");

	public TimeCellRender() {
		setHorizontalAlignment(JLabel.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		assert value instanceof FeedProvider;
		FeedProvider feedProvider = (FeedProvider) value;
		if (column == ColumnType.TIME.ordinal()) {
			label.setText(TIMEFormatter.format(feedProvider.getTimeService().getCurrentTime()));
		} else {
			label.setText(Long.toString(feedProvider.getTimeService().getCurrentTime()));
		}
		return label;

	}
}
