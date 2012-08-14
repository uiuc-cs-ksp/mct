package gov.nasa.arc.mct.fastplot.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public abstract class PlotSettingsSubPanel extends JPanel 
	implements PlotSettingsPopulator, ActionListener, FocusListener {

	private List<Runnable> callbacks = new ArrayList<Runnable>();
	
	
	
	public void addCallback(Runnable callback) {
		callbacks.add(callback);
	}
	
	public void removeCallback(Runnable callback) {
		callbacks.remove(callback);
	}
	
	private void fireCallbacks() {
		for (Runnable callback : callbacks) {
			callback.run();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		fireCallbacks();
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
