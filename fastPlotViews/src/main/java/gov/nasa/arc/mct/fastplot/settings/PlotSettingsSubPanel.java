package gov.nasa.arc.mct.fastplot.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public abstract class PlotSettingsSubPanel extends JPanel 
	implements ActionListener, FocusListener {

	private static final long serialVersionUID = -8067855151177076767L;

	private List<Runnable> callbacks = new ArrayList<Runnable>();
	
	/**
	 * Populate the settings object with the value entered into the 
	 * control.
	 */
	public abstract void populate(PlotConfiguration settings);
	
	/**
	 * Update the state of this control. If this is a hard reset, 
	 * value in the control should be changed to match settings; 
	 * otherwise, state of the control should simply be updated 
	 * to understand its context (useful if the control's 
	 * valid/invalid state is dependent on some other control's 
	 * value.)
	 * @param settings the plot settings
	 * @param hard true if the control itself should be updated
	 */
	public abstract void reset(PlotConfiguration settings, boolean hard);
	
	/**
	 * Check if the control's value has changed since the last hard 
	 * reset.
	 * @return true if the control has been changed; otherwise false
	 */
	public abstract boolean isDirty();
	
	/**
	 * Check if the contents of the control are valid (that is, if
	 * the user should be able to apply these settings.)
	 * @return true if the setting can be applied
	 */
	public abstract boolean isValidated();
	
	
	/**
	 * Add a callback to be triggered when a change in the editor 
	 * occurs.
	 * @param callback the callback to run
	 */
	public void addCallback(Runnable callback) {
		callbacks.add(callback);
	}
	
	/**
	 * Remove a previously added editor change callback.
	 * @param callback the callback removed
	 */
	public void removeCallback(Runnable callback) {
		callbacks.remove(callback);
	}
	
	/**
	 * Cause all callbacks attached to this panel to be 
	 * invoke (for instance, because a contained control's 
	 * state has changed.)
	 */
	protected void fireCallbacks() {
		for (Runnable callback : callbacks) {
			callback.run();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		fireCallbacks();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		fireCallbacks();
	}
	
}
