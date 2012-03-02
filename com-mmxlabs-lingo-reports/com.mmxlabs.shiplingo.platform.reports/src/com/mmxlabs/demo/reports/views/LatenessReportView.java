/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;

/**
 * @author hinton
 * 
 */
public class LatenessReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.demo.reports.views.LatenessReportView";

	public LatenessReportView() {
		super();
		addColumn("Schedule", containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		final EventsPackage ep = EventsPackage.eINSTANCE;

		addColumn("Type", objectFormatter, ep.getScheduledEvent__GetDisplayTypeName());

		addColumn("ID", objectFormatter, ep.getScheduledEvent__GetName());

		final ColumnHandler dateColumn = addColumn("Start Date", datePartFormatter, ep.getScheduledEvent__GetLocalStartTime());
		addColumn("Start Time", timePartFormatter, ep.getScheduledEvent__GetLocalStartTime());

		addColumn("End Date", datePartFormatter, ep.getScheduledEvent__GetLocalEndTime());
		addColumn("End Time", timePartFormatter, ep.getScheduledEvent__GetLocalEndTime());
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object object) {
				final ArrayList<ScheduledEvent> allEvents = new ArrayList<ScheduledEvent>();
				clearInputEquivalents();
				if (object instanceof Iterable) {
					for (final Object o : ((Iterable<?>) object)) {
						if (o instanceof Schedule) {
							for (final Sequence seq : ((Schedule) o).getSequences()) {
								for (final ScheduledEvent e : seq.getEvents()) {
									if (e instanceof SlotVisit) {
										final SlotVisit visit = (SlotVisit) e;

										if (visit.getStartTime().after(visit.getSlot().getWindowEnd())) {
											allEvents.add(e);
										}
										setInputEquivalents(visit, Collections.singleton((Object) visit.getCargoAllocation()));
									} else if (e instanceof VesselEventVisit) {
										final VesselEventVisit vev = (VesselEventVisit) e;
										if (vev.getStartTime().after(vev.getVesselEvent().getEndDate())) {
											allEvents.add(e);
										}
									}
								}
							}
						}
					}
				}
				return allEvents.toArray();
			}
		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}
}
