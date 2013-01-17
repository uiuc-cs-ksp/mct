package gov.nasa.arc.mct.fastplot.bridge.controls;

import gov.nasa.arc.mct.fastplot.bridge.AbstractPlottingPackage;
import gov.nasa.arc.mct.fastplot.bridge.PlotAbstraction;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class LocalControlKeyEventDispatcher implements KeyEventDispatcher, AncestorListener {
	private PlotAbstraction abstraction;

	public LocalControlKeyEventDispatcher(PlotAbstraction abstraction) {
		super();
		this.abstraction = abstraction;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int id = event.getID();
		if (id == KeyEvent.KEY_PRESSED || id == KeyEvent.KEY_RELEASED) {
			boolean pressed = id == KeyEvent.KEY_PRESSED;
			for (AbstractPlottingPackage p : abstraction.getSubPlots()) {
				// Report all key released events, or any event in the plot area
				if (!pressed || !p.getPlotActionListener().isMouseOutsideOfPlotArea()) {
					p.getLocalControlsManager().informKeyState(event.getKeyCode(), pressed);
				}
			}
		}
		return false;
	}



	@Override
	public void ancestorRemoved(AncestorEvent e) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
	}

	@Override
	public void ancestorAdded(AncestorEvent e) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);			
	}


	@Override
	public void ancestorMoved(AncestorEvent arg0) {
		// TODO Auto-generated method stub

	}	

}
