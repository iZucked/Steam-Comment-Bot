/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.Calendar;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.ScheduledEvent;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(final Object object) {
		if (object instanceof Schedule) {
			final Schedule schedule = (Schedule) object;
			return schedule.getCargoAllocations().toArray();
		}
		return new Object[]{};
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		
	}

	
	
	public Calendar getElementStartTime(final Object element) {
		if (element instanceof ScheduledEvent) {
			Calendar c = Calendar.getInstance();
			c.setTime(((ScheduledEvent)element).getStartTime());
			return c;
		} else if (element instanceof CargoAllocation) {
			Calendar c = Calendar.getInstance();
			c.setTime(((CargoAllocation)element).getLoadDate());
			return c;
		}

		return null;
	}

	public Calendar getElementEndTime(final Object element) {
		if (element instanceof ScheduledEvent) {
			Calendar c = Calendar.getInstance();
			c.setTime(((ScheduledEvent)element).getEndTime());
			return c;
		}else if (element instanceof CargoAllocation) {
			Calendar c = Calendar.getInstance();
			c.setTime(((CargoAllocation)element).getDischargeDate());
			return c;
		}

		return null;
	}

	@Override
	public void dispose() {

	}
}
