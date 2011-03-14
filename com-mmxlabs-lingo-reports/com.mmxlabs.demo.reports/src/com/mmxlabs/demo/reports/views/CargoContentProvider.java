/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.demo.reports.views.TotalsContentProvider.RowData;

import scenario.schedule.Schedule;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(final Object object) {
		Schedule schedule = null;
		if (object instanceof Schedule) {
			schedule = (Schedule) object;
		}
		else if (object instanceof IAdaptable) {
			schedule = (Schedule) ((IAdaptable) object).getAdapter(Schedule.class);
		}
		
		if (schedule != null) {
			return schedule.getCargoAllocations().toArray();
		}
		
		return new Object[]{};
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		
	}

	@Override
	public void dispose() {

	}
}
