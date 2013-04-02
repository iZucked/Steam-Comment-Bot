/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.properties;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class ScheduledEventPropertySourceProvider implements IPropertySourceProvider {

	static class SimpleLabelProvider extends ColumnLabelProvider {
		public SimpleLabelProvider() {

		}

		@Override
		public String getText(final Object element) {
			if ((element instanceof Integer) || (element instanceof Long)) {
				return String.format("%,d", element);
			} else if ((element instanceof Float) || (element instanceof Double)) {
				return String.format("%.1f", element);
			} else if (element instanceof Port) {
				return ((Port) element).getName();
			} else if (element instanceof Calendar) {
				final Calendar cal = (Calendar) element;
				final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
				df.setCalendar(cal);
				return df.format(cal.getTime()) + " (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")";
			}
			return super.getText(element);
		}
	}

	@Override
	public IPropertySource getPropertySource(final Object object) {

		if (object instanceof Event) {
			final Event event = (Event) object;
			return new EventGroupPropertySource(event);
		} else if (object instanceof CargoAllocation) {
			final CargoAllocation alloc = (CargoAllocation) object;
			final EList<SlotAllocation> slotAllocations = alloc.getSlotAllocations();
			if (!slotAllocations.isEmpty()) {
				return new EventGroupPropertySource(slotAllocations.get(0).getSlotVisit());
			}
		} else if (object instanceof SlotAllocation) {
			final SlotAllocation alloc = (SlotAllocation) object;
			return new EventGroupPropertySource(alloc.getSlotVisit());
		} else if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		}
		return null;
	}

}
