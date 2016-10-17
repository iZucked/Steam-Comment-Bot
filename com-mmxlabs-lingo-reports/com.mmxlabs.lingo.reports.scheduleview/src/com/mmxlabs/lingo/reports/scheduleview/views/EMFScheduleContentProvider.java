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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;

import com.google.inject.Stage;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
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
					// find multiple availabilities
					Map<Vessel, List<Sequence>> availabilityMap = new HashMap<>();
					List<Sequence> unassigned = sequences.stream().filter(s -> s.getSequenceType() == SequenceType.VESSEL).sorted(
							(a, b) -> a.getVesselAvailability().getStartBy() == null ? - 1 : b.getVesselAvailability().getStartBy() == null ? 1 : a.getVesselAvailability().getStartBy().compareTo(b.getVesselAvailability().getStartBy())).collect(Collectors.toList());
					while (unassigned.size() > 0) {
						@NonNull
						Sequence thisSequence = unassigned.get(0);
						List<Sequence> matches = unassigned.stream().filter(s -> s.getVesselAvailability().getVessel().equals(thisSequence.getVesselAvailability().getVessel())).collect(Collectors.toList());
						availabilityMap.put(thisSequence.getVesselAvailability().getVessel(), matches);
						unassigned.removeAll(matches);
					}
					// find 
					for (final Sequence seq : sequences) {
						// Skip nominal cargoes
						if (seq.getSequenceType() == SequenceType.ROUND_TRIP || seq.getSequenceType() == SequenceType.VESSEL) {
							continue;
						}
						result.add(seq);
					}
					for (Vessel vessel : availabilityMap.keySet()) {
						List<Sequence> combinedSequences = availabilityMap.get(vessel);
						if (combinedSequences.size() == 1) {
							result.add(combinedSequences.get(0));
						} else {
							CombinedSequence cs = new CombinedSequence(vessel);
							cs.setSequences(combinedSequences);
							result.add(cs);
						}
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
		} else if (parent instanceof CombinedSequence) {
			CombinedSequence combinedSequence = (CombinedSequence) parent;
			List<Event> events = new LinkedList<>();
			for (Sequence sequence : combinedSequence.getSequences()) {
				events.addAll(sequence.getEvents());
				for (final Event event : sequence.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
					}
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
		return (element instanceof Sequence) || (element instanceof Schedule) || (element instanceof CombinedSequence);
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
