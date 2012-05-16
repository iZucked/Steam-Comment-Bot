/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.properties;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;

/**
 * Properties display for unit cost lines.
 * 
 * @author hinton
 *
 */
public class UnitCostLinePropertySource implements IPropertySource {
	/**
	 * 
	 */
	private static final String OVERVIEW = "1. Overview";
	private final UnitCostLine line;
	private IPropertyDescriptor[] descriptors;
	
	private final ILabelProvider doubleLabelProvider = new LabelProvider() {
		@Override
		public String getText(Object element) {
			if (element instanceof Double) {
				return String.format("%.2f", element);
			}
			return super.getText(element);
		}
	};
	
	private final ILabelProvider integerLabelProvider = new LabelProvider() {
		@Override
		public String getText(Object element) {
			if (element instanceof Integer) {
				return String.format("%,d", (Integer) element);
			}
			return element + "";
		}
	};
	
	private final ILabelProvider costLabelProvider = new LabelProvider() {
		@Override
		public String getText(Object element) {
			if (element instanceof Integer) {
				return String.format("$%,d", (Integer) element);
			}
			return element + "";
		}
	};
	
	private final ILabelProvider timeLabelProvider = new LabelProvider() {
		@Override
		public String getText(Object element) {
			if (element instanceof Integer) {
				final int hours = (Integer) element;
				if (hours >= 24) {
					return String.format("%dd %dh", hours/24, hours%24);
				} else {
					return String.format("%dh", hours);
				}
			}
			return "" + element;
		}
	};

	private interface IPropertyGetter {
		public Object get(final UnitCostLine line);
	}
	
	public UnitCostLinePropertySource(final UnitCostLine line) {
		this.line = line;
		createDescriptors();
	}

	private void createDescriptors() {
		final ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getTotalCost();
			}
		}, "Total Cost", OVERVIEW, costLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getDuration();
			}
		}, "Total Duration", OVERVIEW, timeLabelProvider));
		
		if (line.getFuelCost() > 0)
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getFuelCost();
			}
		}, "Fuel Cost", OVERVIEW, costLabelProvider));
		
		if (line.getHireCost() > 0)
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getHireCost();
			}
		}, "Charter Cost", OVERVIEW, costLabelProvider));
		
		if (line.getCanalCost() > 0)
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getCanalCost();
			}
		}, "Canal Cost", OVERVIEW, costLabelProvider));
		
		if (line.getPortCost() > 0)
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getPortCost();
			}
		}, "Port Cost", OVERVIEW, costLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getMmbtuDelivered();
			}
		}, "MMBTU", OVERVIEW, integerLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getVolumeLoaded();
			}
		}, "m3 Loaded", OVERVIEW, integerLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return line.getVolumeDischarged();
			}
		}, "m3 Discharged", OVERVIEW, integerLabelProvider));
		
		addVisitDescriptors(descriptors, "2. Loading", (Visit) line.getCostComponents().get(0));
		addVoyageDescriptors(descriptors, "3. Laden Voyage", (Voyage) line.getCostComponents().get(1));
		addVisitDescriptors(descriptors, "4. Discharge", (Visit) line.getCostComponents().get(2));
		addVoyageDescriptors(descriptors, "5. Ballast Voyage", (Voyage) line.getCostComponents().get(3));
		
		this.descriptors = descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	/**
	 * @param descriptors2
	 * @param string
	 * @param visit
	 */
	private void addVisitDescriptors(ArrayList<IPropertyDescriptor> descriptors, String category, final Visit visit) {
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return visit.getDuration();
			}
		}, "Duration", category, timeLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return visit.getHireCost();
			}
		}, "Hire cost", category, costLabelProvider));
		
		if (visit.getPortCost() > 0)
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return visit.getPortCost();
			}
		}, "Port cost", category, costLabelProvider));
	}

	/**
	 * @param descriptors2
	 * @param string 
	 * @param voyage
	 */
	private void addVoyageDescriptors(ArrayList<IPropertyDescriptor> descriptors, String category, final Voyage voyage) {
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getDuration();
			}
		}, "Duration", category, timeLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getHireCost();
			}
		}, "Hire cost", category, costLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getFuelCost();
			}
		}, "Fuel cost", category, costLabelProvider));
		
		if (voyage.getRouteCost() > 0) {
			descriptors.add(makeDescriptor(new IPropertyGetter() {
				@Override
				public Object get(UnitCostLine line) {
					return voyage.getRouteCost();
				}
			}, "Route cost", category, costLabelProvider));
			
		}
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getRoute();
			}
		}, "Route", category, null));
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getSpeed();
			}
		}, "Speed (kts)", category, doubleLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getDistance();
			}
		}, "Distance (kts)", category, integerLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getTravelTime();
			}
		}, "Travel time", category, timeLabelProvider));
		
		descriptors.add(makeDescriptor(new IPropertyGetter() {
			@Override
			public Object get(UnitCostLine line) {
				return voyage.getIdleTime();
			}
		}, "Idle time", category, timeLabelProvider));
	}

	/**
	 * @param iPropertyGetter
	 * @param string
	 * @param string2
	 * @param costLabelProvider2 
	 * @return
	 */
	private IPropertyDescriptor makeDescriptor(IPropertyGetter iPropertyGetter, String string, String string2, ILabelProvider costLabelProvider2) {
		final PropertyDescriptor result = new PropertyDescriptor(iPropertyGetter, string);
		if (string2 != null) result.setCategory(string2);
		if (costLabelProvider2 != null) result.setLabelProvider(costLabelProvider2);
		return result;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id instanceof IPropertyGetter) {
			return ((IPropertyGetter) id).get(line);
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

}
