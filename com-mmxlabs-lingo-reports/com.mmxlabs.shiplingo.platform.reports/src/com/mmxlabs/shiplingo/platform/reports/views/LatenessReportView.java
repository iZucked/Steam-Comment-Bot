/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * @author hinton
 * 
 */
public class LatenessReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView";

	public LatenessReportView() {
		super();
		addColumn("Schedule", containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("Type", objectFormatter, sp.getEvent__Type());

		addColumn("ID", objectFormatter, sp.getEvent__Name());

		final ColumnHandler dateColumn = addColumn("Start Date", datePartFormatter, sp.getEvent__GetLocalStart());
		addColumn("Start Time", timePartFormatter, sp.getEvent__GetLocalStart());

		addColumn("End Date", datePartFormatter, sp.getEvent__GetLocalEnd());
		addColumn("End Time", timePartFormatter,sp.getEvent__GetLocalEnd());
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
				final ArrayList<Event> allEvents = new ArrayList<Event>();
				clearInputEquivalents();
				if (object instanceof Iterable) {
					for (final Object o : ((Iterable<?>) object)) {
						if (o instanceof Schedule) {
							for (final Sequence seq : ((Schedule) o).getSequences()) {
								for (final Event e : seq.getEvents()) {
									if (e instanceof SlotVisit) {
										final SlotVisit visit = (SlotVisit) e;

										if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
											allEvents.add(e);
										}
										setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
									} else if (e instanceof VesselEventVisit) {
										final VesselEventVisit vev = (VesselEventVisit) e;
										if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
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
