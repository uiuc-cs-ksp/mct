package gov.nasa.arc.mct.fastplot.bridge.controls;

public interface AbstractPlotLocalControlsManager {
	public void informKeyState(int key, boolean pressed);
	public void informMouseHover(boolean inPlotArea);
}
