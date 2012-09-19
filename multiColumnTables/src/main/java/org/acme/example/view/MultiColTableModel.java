package org.acme.example.view;

import gov.nasa.arc.mct.components.AbstractComponent;
import gov.nasa.arc.mct.components.FeedProvider;
import gov.nasa.arc.mct.components.Placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class MultiColTableModel extends AbstractTableModel {
	//mockup:
	private static final String UNIT_BASE = "deq";
	private static final String FSW_BASE = "ADC RPAM A REU PRT";
	private static final String TITLE_BASE = "THRM-T-BITBOX";
	private static final String ID_BASE = "ABCD-000";

	private List<AbstractComponent> childrenList;
	private ViewSettings settings;
	
	private Map<String, Object> values = new HashMap<String, Object>();
	private Map<String,List<Integer>> componentLocations;
	
	public MultiColTableModel(List<AbstractComponent> childrenList, ViewSettings settings) {
		this.settings = settings;
		this.childrenList = childrenList;
		updateLocations();
	}
	
	@Override
	public int getColumnCount() { return settings.getNumberOfColumns(); }
	@Override
	public int getRowCount()    { return childrenList.size(); }
	
	public AbstractComponent getComponentOfRow(int rowIndex) {
		return childrenList.get(rowIndex);
	}

	@Override
	public String getColumnName(int colIndex) {
		ColumnType colType = settings.getColumnAtIndex(colIndex);
		return colType.name();
	}
	
	private void updateLocations() {
		componentLocations = new HashMap<String,List<Integer>>();
		for (int row=0; row < getRowCount(); ++row) {
			AbstractComponent component = childrenList.get(row);
			if (component != null) {
				List<Integer> locations = componentLocations.get(getKey(component));
				if (locations == null) {
					locations = new ArrayList<Integer>();
					componentLocations.put(getKey(component), locations);
				}
				locations.add(row);
			}
		}
	}
	
	/**
	 * Returns a unique key for a given component. This key is used by the
	 * code that responds to a feed update to pass along changes to the
	 * component's value.
	 * 
	 * @param component the component for which to determine the key
	 * @return a unique key for the component
	 */
	public String getKey(AbstractComponent component) {
		AbstractComponent delegate = component;
		FeedProvider fp = component.getCapability(FeedProvider.class);
		if (fp != null) {
			return fp.getSubscriptionId();
		}		
		return delegate.getComponentId();
	}
	
	@Override
	public Object getValueAt(int r, int c) {
		ColumnType colType = settings.getColumnAtIndex(c);
		Object cellDatum = null;
		AbstractComponent cellComponent = childrenList.get(r);
		switch(colType) {
		case ID:
			cellDatum = ID_BASE + r; break;
		case TITLE:
			cellDatum = TITLE_BASE + r; break;
		case FSW_NAME: 
			cellDatum = FSW_BASE + r; break;
		case RAW:
			cellDatum = getValueForComponent(cellComponent); break;
		case VALUE:
			cellDatum = getValueForComponent(cellComponent); break;
		case UNIT:
			cellDatum = UNIT_BASE + r; break;
		case ERT:
			cellDatum = (FeedProvider) cellComponent; break;
		case SCLK:
			cellDatum = (FeedProvider) cellComponent; break;
		case SCET:
			cellDatum = (FeedProvider) cellComponent; break;
		}
		if(cellDatum==null) { return "(no data)"; }
		else                { return cellDatum; }
	}
	
	/**
	 * Sets the value of an object updated by a data feed. This change
	 * is propagated to all table cells displaying that object.
	 * 
	 * @param id the identifier for the object updated
	 * @param value the new value to display
	 */
	public void setValue(String id, Object value) {
		values.put(id, value);
		List<Integer> locations = componentLocations.get(id);
		if (locations != null) {
			for (Integer row : locations) {
				fireTableCellUpdated(row, settings.getIndexForColumn(ColumnType.VALUE)); 
				fireTableCellUpdated(row, settings.getIndexForColumn(ColumnType.RAW)); 
				fireTableCellUpdated(row, settings.getIndexForColumn(ColumnType.ERT)); 
				fireTableCellUpdated(row, settings.getIndexForColumn(ColumnType.SCET)); 
				fireTableCellUpdated(row, settings.getIndexForColumn(ColumnType.SCLK)); 
			}
		}
	}
	
	private Object getValueForComponent(AbstractComponent component) {
		Object value = values.get(getKey(component));
		if (value == null) {
			DisplayedValue displayedValue = new DisplayedValue();
			if (component.getCapability(Placeholder.class) != null) {
			    displayedValue.setValue(component.getCapability(Placeholder.class).getPlaceholderValue());
			} else {
				displayedValue.setValue(component.getDisplayName());
			}
			return displayedValue;
		} else {
			return value;
		}
	}
}