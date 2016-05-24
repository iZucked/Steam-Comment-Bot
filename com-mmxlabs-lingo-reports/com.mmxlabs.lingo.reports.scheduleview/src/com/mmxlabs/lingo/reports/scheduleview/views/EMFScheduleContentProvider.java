/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.Viewer;

import com.google.inject.Stage;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;

/**
 * A gantt chart content provider which provides content for a selected EMF Schedule object.
 * 
 * @author hinton
 * 
 */
public class EMFScheduleContentProvider implements IGanttChartContentProvider {

	private final WeakHashMap<Slot, SlotVisit> cachedElements = new WeakHashMap<Slot, SlotVisit>();

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parent) {

		if (parent instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) parent;
			final List<Object> result = new ArrayList<Object>();
			for (final Object o : collection) {
				if (o instanceof Schedule) {
					final EList<Sequence> sequences = ((Schedule) o).getSequences();

					for (final Sequence seq : sequences) {
						// Skip nominal cargoes
						if (seq.getSequenceType() == SequenceType.ROUND_TRIP) {
							continue;
						}
						result.add(seq);

					}
				}
			}
			return result.toArray();
		} else if (parent instanceof Schedule) {
			final EList<Sequence> sequences = ((Schedule) parent).getSequences();
			final List<Sequence> seqs = new ArrayList<Sequence>(sequences.size());
			for (final Sequence seq : sequences) {
				// Skip nominal cargoes
				if (seq.getSequenceType() == SequenceType.ROUND_TRIP) {
					continue;
				}
				seqs.add(seq);
			}
			return seqs.toArray();
		} else if (parent instanceof Sequence) {
			final Sequence sequence = (Sequence) parent;
			final EList<Event> events = sequence.getEvents();
			for (final Event event : events) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
				}
			}
			return events.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return (element instanceof Sequence) || (element instanceof Schedule);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		cachedElements.clear();
	}

	@Override
	public Calendar getElementStartTime(final Object element) {
		if (element instanceof Event) {
			final Event event = (Event) element;
			return GregorianCalendar.from(event.getStart());
		}
		return null;
	}

	@Override
	public Calendar getElementEndTime(final Object element) {
		if (element instanceof Event) {
			final Event event = (Event) element;

			return GregorianCalendar.from(event.getEnd());
		}
		return null;
	}

	@Override
	public Calendar getElementPlannedStartTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			final Slot slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				ZonedDateTime windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
				if (windowStartWithSlotOrPortTime != null) {
					return GregorianCalendar.from(windowStartWithSlotOrPortTime);
				}
			}
		}

		return null;
	}

	@Override
	public Calendar getElementPlannedEndTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			final Slot slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				ZonedDateTime windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
				if (windowStartWithSlotOrPortTime != null) {
					return GregorianCalendar.from(windowStartWithSlotOrPortTime);
				}
			}
		}

		return null;
	}

	/**
	 */
	@Override
	public String getGroupIdentifier(final Object element) {

		if (element instanceof Event) {
			final Event event = (Event) element;
			// Special case for cargo shorts - group items separately
			if (event.getSequence().getSequenceType() == SequenceType.ROUND_TRIP) {
				Event start = ScheduleModelUtils.getSegmentStart(event);

				if (start != null) {
					return start.name();
				}

			}

		}
		return null;
	}

	/**
	 */
	@Override
	public Object getElementDependency(final Object element) {

		if (element instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) element;
			final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
			if (slotAllocation != null) {
				final Slot slot = slotAllocation.getSlot();
				Slot transferSlot = null;
				if (slot instanceof LoadSlot) {
					// final LoadSlot loadSlot = (LoadSlot) slot;
					// transferSlot = loadSlot.getTransferFrom();
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					transferSlot = dischargeSlot.getTransferTo();
				}
				if (transferSlot != null) {
					return cachedElements.get(transferSlot);
				}
			}
		}

		return null;
	}
}
