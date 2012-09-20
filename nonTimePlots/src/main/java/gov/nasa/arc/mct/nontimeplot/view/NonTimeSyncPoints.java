package gov.nasa.arc.mct.nontimeplot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import plotter.xy.XYAxis;

public class NonTimeSyncPoints extends JComponent {
	private static final long serialVersionUID = 3482575621573373378L;

	private List<ColorPoint> colorPoints = new ArrayList<ColorPoint>();
	private XYAxis xAxis;
	private XYAxis yAxis;
	
	public NonTimeSyncPoints(XYAxis xAxis, XYAxis yAxis) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		setVisible(false);
	}
	
	public void addPoint(double x, double y, Color c) {
		setVisible(true);
		colorPoints.add(new ColorPoint(x,y,c));
	}
	
	public void clear() {
		setVisible(false);
		colorPoints.clear();
	}
	
	public void paint(Graphics g) {
		Color old = g.getColor();
		for (ColorPoint cp : colorPoints) cp.paint(g);
		g.setColor(old);
	}
	
	private class ColorPoint {		
		private double x;
		private double y;
		private Color  c;
		
		public ColorPoint(double x, double y, Color c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}
		
		public void paint(Graphics g) {
			int u = xAxis.toPhysical(x);
			int v = yAxis.toPhysical(y);
			g.setColor(c);
			g.fillOval(u-4, v-4, 8, 8);
		}
	}

}
