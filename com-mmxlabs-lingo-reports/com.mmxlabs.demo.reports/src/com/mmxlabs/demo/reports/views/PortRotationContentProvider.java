/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleet.AllocatedVessel;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class PortRotationContentProvider implements IStructuredContentProvider {
	public class RowData {
		public final PortVisit visit;
		public final AllocatedVessel vessel;
		public RowData(PortVisit visit, AllocatedVessel vessel) {
			super();
			this.visit = visit;
			this.vessel = vessel;
		}
	}
	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof Schedule) {
			final Schedule schedule = (Schedule) inputElement;
			
			final LinkedList<Object> result = new LinkedList<Object>();
			for (final Sequence seq : schedule.getSequences()) {
				for (final ScheduledEvent evt : seq.getEvents()) {
					if (evt instanceof PortVisit) {
						result.add(new RowData((PortVisit) evt, seq.getVessel()));
					}
				}
			}
			return result.toArray();
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
