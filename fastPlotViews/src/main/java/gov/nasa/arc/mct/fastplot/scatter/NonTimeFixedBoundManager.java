package gov.nasa.arc.mct.fastplot.scatter;

import gov.nasa.arc.mct.fastplot.bridge.AbstractAxis;
import gov.nasa.arc.mct.fastplot.bridge.AbstractAxisBoundManager;
import gov.nasa.arc.mct.fastplot.bridge.AbstractPlottingPackage;
import gov.nasa.arc.mct.fastplot.bridge.PlotConstants.LimitAlarmState;

public class NonTimeFixedBoundManager implements AbstractAxisBoundManager {
	private AbstractAxis axis;
	private AbstractPlottingPackage plot;
	private boolean isMaximal;
	private double extremum;
	private double priorValue;
	private long   mostRecentAlarm;
	private LimitAlarmState state;
	
	
	public NonTimeFixedBoundManager(AbstractPlottingPackage plot, AbstractAxis axis, boolean isMaximal) {
		super();
		this.axis = axis;
		this.plot = plot;
		this.isMaximal = isMaximal;
		this.extremum = isMaximal ? Double.MIN_VALUE : Double.MAX_VALUE;
		this.priorValue = getCurrent();
		this.state = LimitAlarmState.NO_ALARM;
	}

	@Override
	public void informPointPlottedAtTime(long timestamp, double value) {
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			return; 
		}
		if (meetsOrExceedsExtremum(value)) {
			extremum = value;
		}
		if (!meetsOrExceedsExtremum(getCurrent())) {
			state = LimitAlarmState.ALARM_RAISED;
			mostRecentAlarm = timestamp;
		}
		if (state != LimitAlarmState.ALARM_RAISED && mostRecentAlarm < plot.getMinTime()) {
			state = LimitAlarmState.NO_ALARM;
		}
	}

	@Override
	public LimitAlarmState getState() {
		return state;
	}

	@Override
	public void expand() {
		if (state == LimitAlarmState.NO_ALARM || state == LimitAlarmState.ALARM_CLOSED_BY_USER) {
			priorValue = getCurrent();
		}
		if (!meetsOrExceedsExtremum(priorValue)) {
			if (isStart()) {
				axis.setStart(extremum);
			} else {
				axis.setEnd(extremum);
			}
			plot.notifyObserversAxisChanged(getAxis());
			state = LimitAlarmState.ALARM_OPENED_BY_USER;
		}
	}

	@Override
	public void collapse() {
		if (isStart()) {
			axis.setStart(priorValue);
		} else {
			axis.setEnd(priorValue);
		}
		plot.notifyObserversAxisChanged(getAxis());
		state = LimitAlarmState.ALARM_CLOSED_BY_USER;
	}

	@Override
	public AbstractAxis getAxis() {
		return axis;
	}

	@Override
	public boolean isStart() {
		return isMaximal ^ (axis.getEnd() > axis.getStart());
	}
	
	private double getCurrent() {
		return isStart() ? axis.getStart() : axis.getEnd();
	}
	
	private boolean meetsOrExceedsExtremum(double value) {
		return (Math.signum(value - extremum) != (isMaximal ? -1 : 1));		
	}
}
