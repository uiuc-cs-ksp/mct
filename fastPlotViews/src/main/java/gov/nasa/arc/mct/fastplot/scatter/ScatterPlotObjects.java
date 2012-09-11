package gov.nasa.arc.mct.fastplot.scatter;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import plotter.xy.LinearXYAxis;
import plotter.xy.XYDimension;
import plotter.xy.XYPlot;
import plotter.xy.XYPlotContents;

public class ScatterPlotObjects extends JPanel {
	private ScatterPlot plotPackage;
	
	private XYPlot         plot     = new XYPlot();
	private XYPlotContents contents = new XYPlotContents();
	private LinearXYAxis   xAxis = new LinearXYAxis(XYDimension.X);
	private LinearXYAxis   yAxis = new LinearXYAxis(XYDimension.Y);
	
	
	public ScatterPlotObjects (ScatterPlot scatterPlot) {
		this.plotPackage = scatterPlot;
		
		contents.setBackground(Color.BLACK);
		
		plot.add(contents);
		plot.add(xAxis);
		plot.add(yAxis);		
	}

	private void setupLayout() {
		SpringLayout layout = new SpringLayout();
		plot.setLayout(layout);
		
		layout.putConstraint(SpringLayout.WEST, xAxis, 24, SpringLayout.WEST, plot);
		layout.putConstraint(SpringLayout.EAST, xAxis, 0, SpringLayout.EAST, plot);
		
		layout.putConstraint(SpringLayout.SOUTH, yAxis, -24, SpringLayout.SOUTH, plot);
		layout.putConstraint(SpringLayout.NORTH, yAxis, 0, SpringLayout.NORTH, plot);
		
		layout.putConstraint(SpringLayout.SOUTH, xAxis, 0, SpringLayout.SOUTH, plot);
		layout.putConstraint(SpringLayout.NORTH, xAxis, 0, SpringLayout.SOUTH, yAxis);
		
		layout.putConstraint(SpringLayout.WEST, yAxis, 0, SpringLayout.WEST, plot);
		layout.putConstraint(SpringLayout.EAST, yAxis, 0, SpringLayout.WEST, xAxis);
		
		layout.putConstraint(SpringLayout.SOUTH, contents, 0, SpringLayout.NORTH, xAxis);
		layout.putConstraint(SpringLayout.NORTH, contents, 0, SpringLayout.NORTH, plot);
		
		layout.putConstraint(SpringLayout.WEST, contents, 0, SpringLayout.EAST, yAxis);
		layout.putConstraint(SpringLayout.EAST, contents, 0, SpringLayout.EAST, plot);		
	}
}
