/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.schedule.CanalBookingEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
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

	private final WeakHashMap<Slot<?>, SlotVisit> cachedElements = new WeakHashMap<>();

	@Override
	public Object[] getElements(final Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parent) {

		if (parent instanceof Collection<?>) {
			final Collection<?> collection = (Collection<?>) parent;
			final List<Object> result = new ArrayList<>();
			for (final Object o : collection) {
				if (o instanceof Schedule) {
					Schedule schedule = (Schedule) o;
					final EList<Sequence> sequences = schedule.getSequences();
					// find multiple availabilities
					final Map<Vessel, List<Sequence>> availabilityMap = new HashMap<>();
					final List<Sequence> unassigned = sequences.stream() //
							.filter(s -> s.getSequenceType() == SequenceType.VESSEL).filter(s -> s.getVesselAvailability() != null) //
							.sorted((a, b) -> a.getVesselAvailability().getStartBy() == null ? -1
									: b.getVesselAvailability().getStartBy() == null ? 1 : a.getVesselAvailability().getStartBy().compareTo(b.getVesselAvailability().getStartBy()))
							.collect(Collectors.toList());
					while (!unassigned.isEmpty()) {
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
					// for (InventoryEvents inventory : schedule.getInventoryLevels()) {
					// result.add(inventory);
					// }
				}
			}
			return result.toArray();
		} else if (parent instanceof Schedule) {
			Schedule schedule = (Schedule) parent;
			final EList<Sequence> sequences = schedule.getSequences();
			final List<Object> seqs = new ArrayList<>(sequences.size());
			for (final Sequence seq : sequences) {
				// Skip nominal cargoes
				if (seq.getSequenceType() == SequenceType.ROUND_TRIP) {
					// continue;
				}
				seqs.add(seq);
			}
			// for (InventoryEvents inventory : schedule.getInventoryLevels()) {
			// seqs.add(inventory);
			// }
			return seqs.toArray();
		} else if (parent instanceof Sequence) {
			final Sequence sequence = (Sequence) parent;
			final EList<Event> events = sequence.getEvents();

			final List<Event> newEvents = new LinkedList<>();

			VesselAvailability va = sequence.getVesselAvailability();
			if (va != null) {
				if (va.getStartAfter() != null) {
					final CharterAvailableFromEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableFromEvent();
					evt.setLinkedSequence(sequence);
					evt.setStart(va.getStartAfterAsDateTime());
					evt.setEnd(evt.getStart().plusDays(1));
					// canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
					newEvents.add(evt);
				}
			}

			for (final Event event : events) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
				}
				newEvents.add(event);
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (journey.getCanalDateTime() != null) {
						if (journey.getRouteOption() == RouteOption.PANAMA) {
							final IScenarioDataProvider scenarioDataProviderFor = getScenarioDataProviderFor(journey);
							if (scenarioDataProviderFor != null) {
								final @NonNull ModelDistanceProvider modelDistanceProvider = scenarioDataProviderFor.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES,
										ModelDistanceProvider.class);
								if (sequence.getCharterInMarket() == null || sequence.getSpotIndex() != -1) {

									final CanalBookingEvent canal = ScheduleFactory.eINSTANCE.createCanalBookingEvent();
									canal.setLinkedSequence(journey.getSequence());
									canal.setLinkedJourney(journey);
									canal.setStart(journey.getCanalDateTime().atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME))));
									canal.setEnd(canal.getStart().plusDays(1));
									if (journey.getCanalBooking() == null) {
										if (journey.getLatestPossibleCanalDateTime() != null) {
											ZonedDateTime atStartOfDay = journey.getLatestPossibleCanalDateTime()
													.atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME)));
											int hours = Math.max(1, Hours.between(canal.getStart(), atStartOfDay));
											canal.setEnd(canal.getStart().plusHours(hours));
										}
									}
									canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
									newEvents.add(canal);
								}
							}
						}
					}
				}
			}

			if (va != null) {
				if (va.getEndBy() != null) {
					final CharterAvailableToEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableToEvent();
					evt.setLinkedSequence(sequence);
					evt.setStart(va.getEndByAsDateTime());
					evt.setEnd(evt.getStart().plusDays(1));
					// canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
					newEvents.add(evt);
				}
			}
			return newEvents.toArray();
		} else if (parent instanceof CombinedSequence) {
			final CombinedSequence combinedSequence = (CombinedSequence) parent;
			final List<Event> newEvents = new LinkedList<>();
			for (final Sequence sequence : combinedSequence.getSequences()) {

				VesselAvailability va = sequence.getVesselAvailability();
				if (va != null) {
					if (va.getStartAfter() != null) {
						final CharterAvailableFromEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableFromEvent();
						evt.setLinkedSequence(sequence);
						evt.setStart(va.getStartAfterAsDateTime());
						evt.setEnd(evt.getStart().plusDays(1));
						// canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
						newEvents.add(evt);
					}
				}

				// events.addAll(sequence.getEvents());
				for (final Event event : sequence.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						cachedElements.put(slotVisit.getSlotAllocation().getSlot(), slotVisit);
					}
					newEvents.add(event);
					if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						if (journey.getCanalDateTime() != null) {
							if (journey.getRouteOption() == RouteOption.PANAMA) {
								if (sequence.getCharterInMarket() == null || sequence.getSpotIndex() != -1) {
									IScenarioDataProvider scenarioDataProvider = getScenarioDataProviderFor(journey);
									if (scenarioDataProvider == null) {
										continue;
									}
									final @NonNull ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES,
											ModelDistanceProvider.class);

									final CanalBookingEvent canal = ScheduleFactory.eINSTANCE.createCanalBookingEvent();
									canal.setLinkedSequence(journey.getSequence());
									canal.setLinkedJourney(journey);
									canal.setStart(journey.getCanalDateTime().atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME))));
									canal.setEnd(canal.getStart().plusDays(1));
									if (journey.getCanalBooking() == null) {
										if (journey.getLatestPossibleCanalDateTime() != null) {
											ZonedDateTime atStartOfDay = journey.getLatestPossibleCanalDateTime()
													.atZone(ZoneId.of(journey.getTimeZone(SchedulePackage.Literals.JOURNEY__CANAL_DATE_TIME)));
											int hours = Math.max(1, Hours.between(canal.getStart(), atStartOfDay));
											canal.setEnd(canal.getStart().plusHours(hours));

										}
									}
									canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
									newEvents.add(canal);
								}
							}
						}
					}
				}

				if (va != null) {
					if (va.getEndBy() != null) {
						final CharterAvailableToEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableToEvent();
						evt.setLinkedSequence(sequence);
						evt.setStart(va.getEndByAsDateTime());
						evt.setEnd(evt.getStart().plusDays(1));
						// canal.setPort(modelDistanceProvider.getCanalPort(journey.getRouteOption(), journey.getCanalEntrance()));
						newEvents.add(evt);
					}
				}

			}
			return newEvents.toArray();
		} else if (parent instanceof InventoryEvents) {
			// InventoryEvents inventoryEvents = (InventoryEvents) parent;
			// return inventoryEvents.getEvents().stream() //
			// .filter(evt -> evt.isBreachedMin() || evt.isBreachedMax()) //
			// .toArray();

		}
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		return (element instanceof Sequence) || (element instanceof Schedule) || (element instanceof CombinedSequence) || (element instanceof InventoryEvents);
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
		} else if (element instanceof InventoryChangeEvent) {
			InventoryChangeEvent event = (InventoryChangeEvent) element;
			return GregorianCalendar.from(event.getDate().atZone(ZoneId.of("UTC")));
		}
		return null;
	}

	@Override
	public Calendar getElementEndTime(final Object element) {
		if (element instanceof Event) {
			final Event event = (Event) element;

			return GregorianCalendar.from(event.getEnd());
		} else if (element instanceof InventoryChangeEvent) {
			InventoryChangeEvent event = (InventoryChangeEvent) element;
			return GregorianCalendar.from(event.getDate().plusDays(1).atZone(ZoneId.of("UTC")));
		}
		return null;
	}

	@Override
	public Calendar getElementPlannedStartTime(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			final Slot<?> slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStart();
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
			final Slot<?> slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStart();
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
			} else if (event instanceof CharterAvailableFromEvent) {
				sequence = ((CharterAvailableFromEvent) event).getLinkedSequence();
			} else if (event instanceof CharterAvailableToEvent) {
				sequence = ((CharterAvailableToEvent) event).getLinkedSequence();
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
				final Slot<?> slot = slotAllocation.getSlot();
				Slot<?> transferSlot = null;
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
