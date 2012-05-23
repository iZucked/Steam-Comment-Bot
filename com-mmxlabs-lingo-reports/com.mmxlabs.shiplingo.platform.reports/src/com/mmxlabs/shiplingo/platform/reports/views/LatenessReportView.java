/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduledEventCollector;

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

		addColumn("Start Date", datePartFormatter, sp.getEvent__GetLocalStart());
		addColumn("Start Time", timePartFormatter, sp.getEvent__GetLocalStart());

		addColumn("End Date", datePartFormatter, sp.getEvent__GetLocalEnd());
		addColumn("End Time", timePartFormatter, sp.getEvent__GetLocalEnd());

		addColumn("Window Start Date", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				return datePartFormatter.format(getWindowStartDate(object));
			}
		});

		addColumn("Window Start Time", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				return timePartFormatter.format(getWindowStartDate(object));
			}
		});

		addColumn("Window End Date", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				return datePartFormatter.format(getWindowEndDate(object));
			}
		});

		addColumn("Window End Time", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				return timePartFormatter.format(getWindowEndDate(object));
			}
		});

		addColumn("Late By", new BaseFormatter() {
			@Override
			public String format(final Object object) {

				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					final Calendar localStart = slotVisit.getLocalStart();
					final Calendar windowEndDate = getWindowEndDate(object);

					long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

					// Strip milliseconds
					diff /= 1000;
					// Strip seconds;
					diff /= 60;
					// Strip
					diff /= 60;
					return String.format("%02d:%02d", diff / 24, diff % 24);
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					final Calendar localStart = slotVisit.getLocalStart();
					final Calendar windowEndDate = getWindowEndDate(object);

					long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

					// Strip milliseconds
					diff /= 1000;
					// Strip seconds;
					diff /= 60;
					// Strip
					diff /= 60;
					return Long.valueOf(diff);
				}

				return super.getComparable(object);
			}
		});
	}

	private Calendar getWindowStartDate(final Object object) {
		if (object instanceof SlotVisit) {
			final Date date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			final Date date = ((VesselEventVisit) object).getVesselEvent().getStartAfter();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(FleetPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		}
		return null;
	}

	private Calendar getWindowEndDate(final Object object) {
		final Date date;
		if (object instanceof SlotVisit) {
			date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			date = ((VesselEventVisit) object).getVesselEvent().getStartBy();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(FleetPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else {
			return null;
		}
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {

				clearInputEquivalents();
				final Object[] result = superProvider.getElements(object);
				for (final Object e : result) {
					if (e instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) e;

						setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
					}
				}

				return result;
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector() {
			@Override
			protected boolean filter(final Event e) {
				if (e instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) e;

					if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
						return true;
					}

					setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
				} else if (e instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) e;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						return true;
					}
				}
				return false;
			}

			@Override
			protected boolean filter() {
				return true;
			}

		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}
}
