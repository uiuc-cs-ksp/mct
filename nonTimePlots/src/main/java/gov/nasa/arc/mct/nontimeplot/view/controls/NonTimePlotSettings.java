package gov.nasa.arc.mct.nontimeplot.view.controls;

import gov.nasa.arc.mct.components.ExtendedProperties;
import gov.nasa.arc.mct.nontimeplot.view.NonTimePlotView;

public class NonTimePlotSettings {
	private double independentBounds[] = { -1.0, 1.0 };
	private double dependentBounds[] = { -1.0, 1.0 };
	private int    dataPoints = 6000;
	
	private NonTimePlotView view;
	
	public NonTimePlotSettings(NonTimePlotView v) {
		view = v;
		readSettings();
		applySettings();
	}
	
	private void applySettings() {
		view.applySettings(this);
	}
	
	private void readSettings() {
		ExtendedProperties props = view.getViewProperties();
		independentBounds[0] = read(props.getProperty("NonTimeIndependentMin", String.class), independentBounds[0]);
		independentBounds[1] = read(props.getProperty("NonTimeIndependentMax", String.class), independentBounds[1]);
		dependentBounds  [0] = read(props.getProperty("NonTimeDependentMin", String.class), dependentBounds[0]);
		dependentBounds  [1] = read(props.getProperty("NonTimeDependentMax", String.class), dependentBounds[1]);
		dataPoints           = (int) read(props.getProperty("NonTimeDataPoints", String.class), dataPoints);
		
	}
	
	private void persistSettings() {
		ExtendedProperties props = view.getViewProperties();
		props.addProperty("NonTimeIndependentMin", "" + independentBounds[0]);
		props.addProperty("NonTimeIndependentMax", "" + independentBounds[1]);
		props.addProperty("NonTimeDependentMin",   "" + dependentBounds  [0]);
		props.addProperty("NonTimeDependentMax",   "" + dependentBounds  [1]);
		props.addProperty("NonTimeDataPoints",     "" + dataPoints);
		view.getManifestedComponent().save();
	}
	
	private double read(String value, double current) {
		if (value == null) return current;
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			return current;
		}
	}
	
	private void update() {
		applySettings();
		persistSettings();
	}

	/**
	 * @return the independentBounds
	 */
	public double[] getIndependentBounds() {
		return independentBounds;
	}

	/**
	 * @param independentBounds the independentBounds to set
	 */
	public void setIndependentBounds(double min, double max) {
		this.independentBounds = new double[]{ min, max };
		update();
	}

	/**
	 * @return the dependentBounds
	 */
	public double[] getDependentBounds() {
		return dependentBounds;
	}

	/**
	 * @param dependentBounds the dependentBounds to set
	 */
	public void setDependentBounds(double min, double max) {
		this.dependentBounds = new double[]{ min, max };		
		update();
	}

	/**
	 * @return the dataPoints
	 */
	public int getDataPoints() {
		return dataPoints;
	}

	/**
	 * @param dataPoints the dataPoints to set
	 */
	public void setDataPoints(int dataPoints) {
		this.dataPoints = dataPoints;
		update();
	}
	
}
