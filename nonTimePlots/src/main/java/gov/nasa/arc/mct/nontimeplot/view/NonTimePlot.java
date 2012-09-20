/*******************************************************************************
 * Mission Control Technologies, Copyright (c) 2009-2012, United States Government
 * as represented by the Administrator of the National Aeronautics and Space 
 * Administration. All rights reserved.
 *
 * The MCT platform is licensed under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 *
 * MCT includes source code licensed under additional open source licenses. See 
 * the MCT Open Source Licenses file included with this distribution or the About 
 * MCT Licenses dialog available at runtime from the MCT Help menu for additional 
 * information. 
 *******************************************************************************/
package gov.nasa.arc.mct.nontimeplot.view;

import gov.nasa.arc.mct.nontimeplot.view.legend.LegendManager;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SpringLayout;

import plotter.xy.LinearXYAxis;
import plotter.xy.ScatterXYPlotLine;
import plotter.xy.SimpleXYDataset;
import plotter.xy.XYDimension;
import plotter.xy.XYGrid;
import plotter.xy.XYPlot;
import plotter.xy.XYPlotContents;

public class NonTimePlot extends XYPlot {
	private static final long serialVersionUID = 7711105789250549245L;
	
	private static final int PLOT_MARGIN = 32;
	
	private NonTimeSyncPoints sync;
	private XYPlotContents contents = new XYPlotContents();
	private Map<String, SimpleXYDataset> dataSet = new HashMap<String, SimpleXYDataset>();
	private Map<String, ScatterXYPlotLine> plotLines = new HashMap<String, ScatterXYPlotLine>();
	private int maxPoints = 600;
	
		
	public NonTimePlot() {
		setXBounds(-1.5, 1.5);
		setYBounds(-1.5, 1.5);
		
		setBackground(Color.DARK_GRAY.darker());
		contents.setBackground(Color.BLACK);
		
		add (getXAxis());
		add (getYAxis());

		sync = new NonTimeSyncPoints(getXAxis(), getYAxis());
		XYGrid grid = new XYGrid(getXAxis(), getYAxis());
		grid.setForeground(Color.WHITE);
		contents.add(grid);
		contents.add(sync);
		
		add (contents);		
		
		setupLayout();
	}
	
	public void setXBounds(double minimum, double maximum) {
		LinearXYAxis axis = new LinearXYAxis(XYDimension.X);
		axis.setStart(minimum);
		axis.setEnd(maximum);
		setXAxis(axis);
		colorize(axis);
	}

	public void setYBounds(double minimum, double maximum) {
		LinearXYAxis axis = new LinearXYAxis(XYDimension.Y);
		axis.setStart(minimum);
		axis.setEnd(maximum);
		setYAxis(axis);
		colorize(axis);
	}
	
	public void addPoint (String key, double x, double y) {
		if (dataSet.containsKey(key)) { 
			dataSet.get(key).add(x, y);
			int pointCount = dataSet.get(key).getPointCount();
			if (pointCount > maxPoints) dataSet.get(key).removeFirst(pointCount - maxPoints);
		}
	}
	
	public ScatterXYPlotLine addDataset (String key, Color c) {
		ScatterXYPlotLine plotLine = new ScatterXYPlotLine(getXAxis(), getYAxis());
		SimpleXYDataset data = new SimpleXYDataset(plotLine);
		plotLines.put(key, plotLine);
		dataSet.put(key, data);
		contents.add(plotLine);
		plotLine.setForeground(c);
		return plotLine;
	}	
	
	private Color getColor(String key) {
		return plotLines.get(key).getForeground();
	}
	
	private void colorize(LinearXYAxis comp) {
		comp.setBackground(Color.DARK_GRAY.darker());
		comp.setForeground(Color.WHITE);
		comp.setTextMargin(12);		
		comp.setFont(Font.decode(Font.SANS_SERIF).deriveFont(10f));
	}

	private void setupLayout() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		layout.putConstraint(SpringLayout.WEST, getXAxis(), PLOT_MARGIN*2, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, getXAxis(), 0, SpringLayout.EAST, this);
		
		layout.putConstraint(SpringLayout.SOUTH, getYAxis(), -PLOT_MARGIN, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, getYAxis(), 0, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.SOUTH, getXAxis(), 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.NORTH, getXAxis(), 0, SpringLayout.SOUTH, getYAxis());
		
		layout.putConstraint(SpringLayout.WEST, getYAxis(), 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, getYAxis(), 0, SpringLayout.WEST, getXAxis());
		
		layout.putConstraint(SpringLayout.SOUTH, contents, 0, SpringLayout.NORTH, getXAxis());
		layout.putConstraint(SpringLayout.NORTH, contents, 0, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, contents, 0, SpringLayout.EAST, getYAxis());
		layout.putConstraint(SpringLayout.EAST, contents, 0, SpringLayout.EAST, this);		
	}

	public void addSyncPoint(String key, Double x, Double y) {
		sync.addPoint(x, y, getColor(key));
	}

	public void clearSyncPoints() {
		sync.clear();
	}
}
