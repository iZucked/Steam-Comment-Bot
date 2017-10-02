/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.common.time.Days;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.schedule.CanalBookingEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * A gantt chart content provider which provides content for a selected EMF Schedule object.
 * 
 * @author hinton
 * 
 */
public abstract class EMFScheduleContentProvider implements IGanttChartContentProvider {

	private final WeakHashMap<Slot, SlotVisit> cachedElements = new WeakHashMap<Slot, SlotVisit>();

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parent) {

		if (parent instanceof Collection<?>) {
			final Collection<?> collection = (Collection<?>) parent;
			final List<Object> result = new ArrayList<Object>();
			for (final Object o : collection) {
				if (o instanceof Schedule) {
					final EList<Sequence> sequences = ((Schedule) o).getSequences();
					// find multiple availabilities
					final Map<Vessel, List<Sequence>> availabilityMap = new HashMap<>();
					final List<Sequence> unassigned = sequences.stream() //
							.filter(s -> s.getSequenceType() == SequenceType.VESSEL).filter(s -> s.getVesselAvailability() != null) //
							.sorted((a, b) -> a.getVesselAvailability().getStartBy() == null ? -1
									: b.getVesselAvailability().getStartBy() == null ? 1 : a.getVesselAvailability().getStartBy().compareTo(b.getVesselAvailability().getStartBy()))
							.collect(Collectors.toList());
					while (unassigned.size() > 0) {
						@NonNull
						final Sequence thisSequence = unassigned.get(0);
						final List<Sequence> matches = unassigned.stream().filter(s -> s.getVesselAvailability().getVessel().equals(thisSequence.getVesselAvailability().getVessel()))
								.collect(Collectors.toList());
						availabilityMap.put(thisSequence.getVesselAvailability().getVessel(), matches);
						unassigned.removeAll(matches);
					}
					// find
					for (final Sequence seq : sequences) {
						// Skip nominal cargoes
						if (
						// seq.getSequenceType() == SequenceType.ROUND_TRIP ||
						seq.getSequenceType() == SequenceType.VESSEL) {
							continue;
						}
						result.add(seq);
					}
					for (final Vessel vessel : availabilityMap.keySet()) {
						final List<Sequence> combinedSequences = availabilityMap.get(vessel);
						if (combinedSequences.size() == 1) {
							result.add(combinedSequences.get(0));
						} else {
							final CombinedSequence cs = new CombinedSequence(vessel);
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
					// continue;
				}
				seqs.add(seq);
			}
			return seqs.toArray();
		} else if (parent instanceof Sequence) {
			final Sequence sequence = (Sequence) parent;
			final EList<Event> events = sequence.getEvents();

			final List<Event> newEvents = new LinkedList<>();
			for (final Event event : events) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
				}
				newEvents.add(event);
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (journey.getCanalDate() != null) {
						if (journey.getRouteOption() == RouteOption.PANAMA) {
							final IScenarioDataProvider scenarioDataProviderFor = getScenarioDataProviderFor(journey);
							if (scenarioDataProviderFor != null) {
								final @NonNull ModelDistanceProvider modelDistanceProvider = scenarioDataProviderFor.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES,
										ModelDistanceProvider.class);

								final CanalBookingEvent canal = ScheduleFactory.eINSTANCE.createCanalBookingEvent();
								canal.setLinkedSequence(journey.getSequence());
								canal.setLinkedJourney(journey);
								canal.setStart(journey.getCanalDate().atStartOfDay(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE))));
								canal.setEnd(canal.getStart().plusDays(1));
								if (journey.getCanalBooking() == null) {
									if (journey.getLatestPossibleCanalDate() != null) {
										ZonedDateTime atStartOfDay = journey.getLatestPossibleCanalDate().atStartOfDay(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE)));
										int days = Math.max(1, Days.between(canal.getStart(), atStartOfDay));
										canal.setEnd(canal.getStart().plusDays(days));
									}
								}
								canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
								newEvents.add(canal);
							}
						}
					}
				}
			}
			return newEvents.toArray();
		} else if (parent instanceof CombinedSequence) {
			final CombinedSequence combinedSequence = (CombinedSequence) parent;
			final List<Event> newEvents = new LinkedList<>();
			for (final Sequence sequence : combinedSequence.getSequences()) {
				// events.addAll(sequence.getEvents());
				for (final Event event : sequence.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
					}
					newEvents.add(event);
					if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						if (journey.getCanalDate() != null) {
							if (journey.getRouteOption() == RouteOption.PANAMA) {
								final @NonNull ModelDistanceProvider modelDistanceProvider = getScenarioDataProviderFor(journey).getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES,
										ModelDistanceProvider.class);

								final CanalBookingEvent canal = ScheduleFactory.eINSTANCE.createCanalBookingEvent();
								canal.setLinkedSequence(journey.getSequence());
								canal.setLinkedJourney(journey);
								canal.setStart(journey.getCanalDate().atStartOfDay(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE))));
								canal.setEnd(canal.getStart().plusDays(1));
								if (journey.getCanalBooking() == null) {
									if (journey.getLatestPossibleCanalDate() != null) {
										ZonedDateTime atStartOfDay = journey.getLatestPossibleCanalDate().atStartOfDay(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE)));
										int days = Math.max(1, Days.between(canal.getStart(), atStartOfDay));
										canal.setEnd(canal.getStart().plusDays(days));

									}
								}
								canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
								newEvents.add(canal);
							}
						}
					}
				}
			}
			return newEvents.toArray();
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
				final ZonedDateTime windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
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
				final ZonedDateTime windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
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
			final Sequence sequence;
			if (event instanceof CanalBookingEvent) {
				sequence = ((CanalBookingEvent) event).getLinkedSequence();
			} else {
				sequence = event.getSequence();
			}
			// Special case for cargo shorts - group items separately
			if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
				final Event start = ScheduleModelUtils.getSegmentStart(event);

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

	@Override
	public boolean isVisibleByDefault(final Object resource) {
		if (resource instanceof Sequence) {
			final Sequence sequence = (Sequence) resource;
			return sequence.getSequenceType() != SequenceType.ROUND_TRIP;
		}
		return true;
	}

	public abstract IScenarioDataProvider getScenarioDataProviderFor(Object obj);
}
