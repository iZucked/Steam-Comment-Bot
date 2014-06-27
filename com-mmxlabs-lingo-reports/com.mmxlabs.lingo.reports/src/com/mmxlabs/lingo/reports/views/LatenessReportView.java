/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
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
		addScheduleColumn("Schedule", containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("ID", ColumnType.NORMAL, objectFormatter, sp.getEvent__Name());

		addColumn("Type", ColumnType.NORMAL, objectFormatter, sp.getEvent__Type());
		addColumn("Lateness", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {

				if (object instanceof PortVisit) {
					final PortVisit slotVisit = (PortVisit) object;
					final Calendar localStart = slotVisit.getLocalStart();
					final Calendar windowEndDate = getWindowEndDate(object);

					long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

					// Strip milliseconds
					diff /= 1000;
					// Strip seconds;
					diff /= 60;
					// Strip
					diff /= 60;
					if (diff / 24 == 0) {
						return String.format("%2dh", diff % 24);
					} else {
						return String.format("%2dd, %2dh", diff / 24, diff % 24);
					}
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof PortVisit) {
					final PortVisit slotVisit = (PortVisit) object;
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

		addColumn("Start by", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				return calendarFormatterNoTZ.format(getWindowEndDate(object));
			}
		});
		addColumn("Scheduled time", ColumnType.NORMAL, calendarFormatterNoTZ, sp.getEvent__GetLocalStart());

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
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy());
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
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof PortVisit) {
			final PortVisit visit = (PortVisit) object;
			final Sequence seq = visit.getSequence();
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability == null) {
				return null;
			}
			if (seq.getEvents().indexOf(visit) == 0) {
				final Date startBy = vesselAvailability.getStartBy();
				if (startBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(startBy);
					return c;
				}
			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final Date endBy = vesselAvailability.getEndBy();
				if (endBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(endBy);
					return c;
				}
			}
		}
		return null;
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(Object inputElement) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(inputElement);
				for (final Object e : result) {
					if (e instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) e;

						setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
					} else if (e instanceof VesselEventVisit) {
						final VesselEventVisit visit = (VesselEventVisit) e;

						setInputEquivalents(visit, Collections.singleton((Object) visit.getVesselEvent()));
					} else if (e instanceof PortVisit) {
						final PortVisit visit = (PortVisit) e;

						//
					}
				}

				return result;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return superProvider.getChildren(parentElement);
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
					// Exclude DES Purchase and fob sales
					final Sequence seq = visit.getSequence();
					if (seq.getSequenceType() == SequenceType.DES_PURCHASE || seq.getSequenceType() == SequenceType.FOB_SALE) {
						return false;
					}
					if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
						return true;
					}

					setInputEquivalents(
							visit,
							Lists.<Object> newArrayList(visit.getSlotAllocation().getCargoAllocation(), visit.getSlotAllocation().getSlot(), visit.getSlotAllocation().getCargoAllocation()
									.getInputCargo()));
				} else if (e instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) e;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						return true;
					}
				} else if (e instanceof PortVisit) {
					final PortVisit visit = (PortVisit) e;
					final Sequence seq = visit.getSequence();

					final VesselAvailability availability = seq.getVesselAvailability();
					if (availability == null) {
						return false;
					}
					if (seq.getEvents().indexOf(visit) == 0) {

						final Date startBy = availability.getStartBy();
						if (startBy != null && visit.getStart().after(startBy)) {
							return true;
						}

					} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
						final Date endBy = availability.getEndBy();
						if (endBy != null && visit.getStart().after(endBy)) {
							return true;
						}
					}
					// setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
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
