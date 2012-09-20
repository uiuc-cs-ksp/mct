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

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.FeedProvider;
import gov.nasa.arc.mct.components.FeedProvider.RenderingInfo;
import gov.nasa.arc.mct.gui.FeedView;
import gov.nasa.arc.mct.nontimeplot.view.legend.LegendEntryView;
import gov.nasa.arc.mct.nontimeplot.view.legend.LegendManager;
import gov.nasa.arc.mct.services.component.ViewInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class NonTimePlotView extends FeedView {
	private static final long serialVersionUID = -8332691253144683655L;

	private static final String SEPARATOR = "\n";
	private static final long   EXPIRATION_AGE = 3600;
	
	private List<FeedProvider> feedProviders = new ArrayList<FeedProvider>();

	private NonTimePlot plot = new NonTimePlot();
	private long        cycle = 0;
	
	public NonTimePlotView (AbstractComponent ac, ViewInfo vi) {
		super (ac, vi);
		
		List<AbstractComponent> feedComponents = new ArrayList<AbstractComponent>();
		for (AbstractComponent child : ac.getComponents()) {
			FeedProvider fp = child.getCapability(FeedProvider.class);
			if (fp != null) {
				feedComponents.add(child);
				feedProviders.add(fp);
			}
		}
		
		LegendManager legendManager = new LegendManager(feedComponents);
		
		for (int i = 0; i < feedProviders.size(); i++) {
			FeedProvider fp = feedProviders.get(i);
			LegendEntryView v = legendManager.getLegendEntry(feedComponents.get(i));
			if (i > 0) {
				v.setPlotLine(
					plot.addDataset(key(fp.getSubscriptionId()), NonTimeColorPalette.getColor(i-1))
					);
			}
		}
		
		setLayout(new BorderLayout());
		JPanel legendArea = new JPanel(new BorderLayout());
		legendArea.add(legendManager, BorderLayout.NORTH);
		add(legendArea, BorderLayout.WEST);
		add (plot, BorderLayout.CENTER);
		legendArea.setBackground(Color.DARK_GRAY.darker());
	}
	
	@Override
	public void updateFromFeed(Map<String, List<Map<String, String>>> data) {
		if (feedProviders.size() < 2) return;
		plot.clearSyncPoints();
		Double x = getData(feedProviders.get(0), data);
		for (int i = 1; i < feedProviders.size(); i++) {
			FeedProvider fp = feedProviders.get(i);
			Double y = getData(fp, data);
			if (x != null && y != null) {
				plot.addPoint(key(fp.getSubscriptionId()), x, y);
			}	
		}
		plot.repaint();
		cycle++;
	}

	@Override
	public void synchronizeTime(Map<String, List<Map<String, String>>> data,
			long syncTime) {		
		if (feedProviders.size() < 2) return;
		plot.clearSyncPoints();
		Double x = getData(feedProviders.get(0), data);
		for (int i = 1; i < feedProviders.size(); i++) {
			FeedProvider fp = feedProviders.get(i);
			Double y = getData(fp, data);
			if (x != null && y != null) {
				plot.addSyncPoint(key(fp.getSubscriptionId()), x, y);
			}	
		}	
		plot.repaint();
	}

	@Override
	public Collection<FeedProvider> getVisibleFeedProviders() {
		return feedProviders;
	}
	
	private Double getData(FeedProvider fp, Map<String, List<Map<String,String>>> data) {
		String id = fp.getSubscriptionId();
		if (data.containsKey(id)) {
			List<Map<String, String>> series = data.get(id);
			if (series.size() > 0) {
				Map<String, String> point = series.get(series.size() - 1);
				RenderingInfo ri = fp.getRenderingInfo(point);
				if (ri != null && ri.isPlottable()) {
					String value = ri.getValueText();					
					try {
						return Double.parseDouble(value);
					} catch (Exception e) {
						return null;
					}
				} else {
					return Double.NaN; // LOS or bad data
				}
			}
		}
		return null;
	}
	
	private String key (String feedId) {
		return (feedProviders.size() >= 2) ?
				(feedProviders.get(0).getSubscriptionId() +
				SEPARATOR +
				feedId) :
				"";
	}
	
	private Long getTimestamp(Map<String, String> dataPoint) {
		if (dataPoint.containsKey(FeedProvider.NORMALIZED_TIME_KEY)) {
			String timestamp = dataPoint.get(FeedProvider.NORMALIZED_TIME_KEY);
			try {
				return Long.parseLong(timestamp);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	private class BiFeed { // A pair of feeds
		//private ???NonTimeDataSeries??? dataseries
		private String xFeedId;
		private String yFeedId;
		private List   pair;
		
		private Map<Long, Pair> pairs = new HashMap<Long, Pair>();
		
		public BiFeed(String xFeedId, String yFeedId) {
			this.xFeedId = xFeedId;
			this.yFeedId = yFeedId;
		}
		
		public void addData(FeedProvider fp, Map<String, String> data) {
			Long time = getTimestamp(data);
			if (time != null) {
				RenderingInfo ri = fp.getRenderingInfo(data);
				if (ri != null) {
					try {
						Double v = Double.parseDouble(ri.getValueText());
						String id = fp.getSubscriptionId();
						if (!pairs.containsKey(time)) {
							pairs.put(time, new Pair(time));
						}
						if (id == xFeedId) {
							pairs.get(time).setX(v);
						}
						if (id == yFeedId) {
							pairs.get(time).setY(v);
						}
					} catch (NumberFormatException e) {
						//TODO: Log?
					}
				}
			}
		}
		
		public void clearExpiredPairs() {
			//TODO: This allocation may be too expensive for this context
			List<Pair> expired = new ArrayList<Pair>();
			for (Pair p : pairs.values()) {
				if (p.expired()) {
					expired.add(p);
				}				
			}
			for (Pair p : expired) {
				p.remove();
			}
		}
		
		private class Pair {
			private long   creationCycle;
			private Double x = null;
			private Double y = null;
			private long   t;
			public Pair(Long time) {
				t = time;
				creationCycle = cycle;
			}
			public boolean expired() {
				return (cycle - creationCycle) > EXPIRATION_AGE;
			}
			public void setX(Double v) {
				x = v;
				promoteIfComplete();
			}
			public void setY(Double v) {
				y = v;
				promoteIfComplete();
			}
			
			public void remove() {
				pairs.remove(t); // Concurrent modification exception?
			}
			
			private void promoteIfComplete() {
				if (x != null && x != null) {
					//dataseries.update(x,y,t);
					remove();
				}
			}			
		}
	}
	
	
}
