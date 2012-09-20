package gov.nasa.arc.mct.nontimeplot.view.legend;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.gui.View;
import gov.nasa.arc.mct.services.component.ViewInfo;
import gov.nasa.arc.mct.services.component.ViewType;

import javax.swing.Icon;
import javax.swing.JLabel;

import plotter.xy.ScatterXYPlotLine;

public class LegendEntryView extends View {
	private static final long serialVersionUID = -2885137579175013142L;
	public static final ViewInfo VIEW_INFO = new ViewInfo(LegendEntryView.class, "Legend Entry", ViewType.EMBEDDED);
	
	
	private JLabel label = new JLabel();
	private ScatterXYPlotLine line = null; // TODO: Make more generic
	
	public LegendEntryView(AbstractComponent ac, ViewInfo vi) {
		super(ac, vi);
		
		setOpaque(false);
		
		label.setText(ac.getDisplayName());
		label.setFont(label.getFont().deriveFont(10f).deriveFont(Font.ITALIC));
		label.setForeground(Color.LIGHT_GRAY);
		
		add(label);
		
	}
	
	public void setPlotLine(ScatterXYPlotLine line) {
		this.line = line;
		label.setForeground(line.getForeground());
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		label.setIcon(new Icon() {

			@Override
			public int getIconHeight() {
				return 9; //TODO: if line has icon...
			}

			@Override
			public int getIconWidth() {
				return 9;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
			}
			
		});
		// TODO: Attach popup!
		
	}
	
	
}
