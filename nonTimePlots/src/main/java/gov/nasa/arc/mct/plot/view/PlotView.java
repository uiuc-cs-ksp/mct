package gov.nasa.arc.mct.plot.view;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.FeedProvider;
import gov.nasa.arc.mct.gui.FeedView;
import gov.nasa.arc.mct.plot.data.DataHandler;
import gov.nasa.arc.mct.services.component.ViewInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlotView extends FeedView {
	private DataHandler dataHandler;
	
	public PlotView(AbstractComponent component, ViewInfo info) {
		super(component, info);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateFromFeed(Map<String, List<Map<String, String>>> data) {
		if (dataHandler != null) dataHandler.updateFromFeed(data);
	}

	@Override
	public void synchronizeTime(Map<String, List<Map<String, String>>> data,
			long syncTime) {
		if (dataHandler != null) dataHandler.synchronizeTime(data, syncTime);
	}

	@Override
	public Collection<FeedProvider> getVisibleFeedProviders() {
		// TODO Auto-generated method stub
		return null;
	}

}
