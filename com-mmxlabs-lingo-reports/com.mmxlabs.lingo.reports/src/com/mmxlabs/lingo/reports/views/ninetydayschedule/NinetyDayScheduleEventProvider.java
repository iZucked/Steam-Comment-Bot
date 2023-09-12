/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.services.EquivalentsManager;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;
import com.mmxlabs.widgets.schedulechart.EditableScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;

public class NinetyDayScheduleEventProvider implements IScheduleEventProvider<ScheduleModel> {
	
	private final VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
	private final EquivalentsManager equivalentsManager;
	private final IScheduleChartSettings settings;

	public NinetyDayScheduleEventProvider(EquivalentsManager equivalentsManager, IScheduleChartSettings settings) {
		this.equivalentsManager = equivalentsManager;
		this.settings = settings;
	}

	@Override
	public List<ScheduleEvent> getEvents(ScheduleModel input) {
		Schedule schedule = input.getSchedule();
		
		// contains Sequence, CombinedSequence and PositionsSequence
		List<Object> sequences = getSequences(schedule);
		List<Event> events = new ArrayList<>();
		
		for (Object o: sequences) {
			if (o instanceof Sequence s) {
				collectEvents(events, s);
			} else if (o instanceof CombinedSequence cs) {
				collectEvents(events, cs);
			} else if (o instanceof PositionsSequence ps) {
				collectEvents(events, ps);
			}
		}
		
		events.stream().forEach(e -> equivalentsManager.collectEquivalents(e, this::generateEquivalents));
		return events.stream().map(this::makeScheduleEvent).collect(Collectors.toList());
	}

	@Override
	public ScheduleChartRowKey getKeyForEvent(ScheduleEvent event) {
		if (event.getData() instanceof Event e) {
			var name = vesselAssignmentFormatter.render(e);
			if (name == null) {
				int i = 0;
			}
			return new ScheduleChartRowKey(name == null ? "" : name,  e.getSequence());
		}
		return null;
	}
	
	private List<Object> getSequences(Schedule schedule) {
		List<Object> res = new ArrayList<>();
		
		final EList<Sequence> sequences = schedule.getSequences();

		// find multiple charters
		final Map<Vessel, List<Sequence>> chartersMap = new HashMap<>();
		final List<Sequence> unassigned = sequences.stream() //
				.filter(s -> s.getSequenceType() == SequenceType.VESSEL).filter(s -> s.getVesselCharter() != null) //
				.sorted((a, b) -> a.getVesselCharter().getStartBy() == null ? -1
						: b.getVesselCharter().getStartBy() == null ? 1 : a.getVesselCharter().getStartBy().compareTo(b.getVesselCharter().getStartBy()))
				.collect(Collectors.toList());

		while (!unassigned.isEmpty()) {
			@NonNull final Sequence thisSequence = unassigned.get(0);
			final List<Sequence> matches = unassigned.stream().filter(s -> s.getVesselCharter().getVessel().equals(thisSequence.getVesselCharter().getVessel())).toList();
			chartersMap.put(thisSequence.getVesselCharter().getVessel(), matches);
			unassigned.removeAll(matches);
		}

		for (final Sequence seq : sequences) {
			// Skip nominal cargoes
			final var type = seq.getSequenceType();
			if (type == SequenceType.VESSEL || type == SequenceType.DES_PURCHASE || type == SequenceType.FOB_SALE) {
				continue;
			}
			res.add(seq);
		}

		for (final var entry : chartersMap.entrySet()) {
			final Vessel vessel = entry.getKey();
			final List<Sequence> combinedSequences = entry.getValue();
			if (combinedSequences.size() == 1) {
				res.add(combinedSequences.get(0));
			} else {
				final CombinedSequence cs = new CombinedSequence(vessel);
				cs.setSequences(combinedSequences);
				res.add(cs);
			}
		}
		
		addPositionsSequences(res, schedule);

		return res;
	}
	
	private void addPositionsSequences(List<Object> res, Schedule schedule) {
		final Collection<Slot<?>> nonShippedSlots = new HashSet<>();
		for (final NonShippedSequence seq : schedule.getNonShippedSequences()) {
			res.add(seq);
			for (final Event event : seq.getEvents()) {
				if (event instanceof NonShippedSlotVisit slotVisit) {
					nonShippedSlots.add(slotVisit.getSlot());
				}
			}
		}
		
		final @NonNull Collection<@NonNull SlotVisit> slotVisitsToIgnore;
		if (!nonShippedSlots.isEmpty()) {
			slotVisitsToIgnore = schedule.getCargoAllocations().stream() //
					.map(CargoAllocation::getSlotAllocations) //
					.flatMap(List::stream) //
					.filter(sa -> nonShippedSlots.contains(sa.getSlot())) //
					.map(SlotAllocation::getSlotVisit) //
					.collect(Collectors.toSet());
		} else {
			slotVisitsToIgnore = Collections.emptySet();
		}

		res.addAll(PositionsSequence.makeBuySellSequences(schedule, slotVisitsToIgnore, ""));
	}
	
	private void collectEvents(List<Event> res, Sequence s) {
		final VesselCharter va = s.getVesselCharter();
		if (va != null && va.getStartAfter() != null) {
				final CharterAvailableFromEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableFromEvent();
				evt.setLinkedSequence(s);
				evt.setStart(va.getStartAfterAsDateTime());
				evt.setEnd(evt.getStart().plusDays(1));
				res.add(evt);
		}

		for (final Event event : s.getEvents()) {
			res.add(event);
			/* Note: We don't add CanalJourneyEvent here since it is now treated as an annotation */
		}

		if (va != null && va.getEndBy() != null) {
			final CharterAvailableToEvent evt = ScheduleFactory.eINSTANCE.createCharterAvailableToEvent();
			evt.setLinkedSequence(s);
			evt.setStart(va.getEndByAsDateTime());
			evt.setEnd(evt.getStart().plusDays(1));
			res.add(evt);
		}
	}
	
	private void collectEvents(List<Event> res, CombinedSequence cs) {
		for (final Sequence s: cs.getSequences()) {
			collectEvents(res, s);
		}
	}
	
	private void collectEvents(List<Event> events, PositionsSequence ps) {
		// TODO: fill this
	}

	private ScheduleEvent makeScheduleEvent(Event event) {
		ScheduleEvent se = new ScheduleEvent(event.getStart().toLocalDateTime(), event.getEnd().toLocalDateTime(), event, makeEventAnnotations(event));
		se.setVisible(true);
		return se;
	}
	
	private List<ScheduleEventAnnotation> makeEventAnnotations(Event event) {
		List<ScheduleEventAnnotation> res = new ArrayList<>();
		if (event instanceof final SlotVisit sv && sv.getSlotAllocation().getSlot() != null) {
			// Get window start and end dates
			LocalDateTime windowStart = null;
			LocalDateTime windowEnd = null;

			final Slot<?> slot = sv.getSlotAllocation().getSlot();
			final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStart();
			final ZonedDateTime windowEndWithSlotOrPortTime = slot.getSchedulingTimeWindow().getEnd();

			if (windowStartWithSlotOrPortTime != null) {
				windowStart = windowStartWithSlotOrPortTime.toLocalDateTime();
			}
			if (windowEndWithSlotOrPortTime != null) {
				windowEnd = windowEndWithSlotOrPortTime.toLocalDateTime();
			}

			final var slotWindowAnnotation = new EditableScheduleEventAnnotation(List.of(windowStart, windowEnd), NinetyDayScheduleEventAnnotationType.SLOT_WINDOW);
			slotWindowAnnotation.setVisible(true);
			res.add(slotWindowAnnotation);
			
			PortVisitLateness pvl = sv.getLateness();
			if (pvl != null && pvl.getLatenessInHours() > 0) {
				LocalDateTime start = sv.getStart().toLocalDateTime();
				final var latenessAnnotation = new ScheduleEventAnnotation(List.of(start.minusHours(pvl.getLatenessInHours()), start), NinetyDayScheduleEventAnnotationType.LATENESS_BAR);
				latenessAnnotation.setVisible(true);
				res.add(latenessAnnotation);
			}
		} else if (event instanceof NonShippedSlotVisit sv) {
			PortVisitLateness pvl = sv.getLateness();
			if (pvl != null && pvl.getLatenessInHours() > 0) {
				LocalDateTime start = sv.getStart().toLocalDateTime();
				final var latenessAnnotation = new ScheduleEventAnnotation(List.of(start.minusHours(pvl.getLatenessInHours()), start), NinetyDayScheduleEventAnnotationType.LATENESS_BAR);
				latenessAnnotation.setVisible(true);
				res.add(latenessAnnotation);
			}
		} else if (event instanceof Journey j && j.getCanalJourneyEvent() != null) {
			final var canalJourneyAnnotation = new ScheduleEventAnnotation(List.of(j.getCanalDateTime()),  NinetyDayScheduleEventAnnotationType.CANAL_JOURNEY);
			canalJourneyAnnotation.setVisible(true);
			res.add(canalJourneyAnnotation);
		}
		
		return res;
	}

	private List<Object> generateEquivalents(Object event) {
		if (event instanceof final SlotVisit slotVisit) {
			return Lists.newArrayList(slotVisit.getSlotAllocation(), slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation().getCargoAllocation());
		} else if (event instanceof final CargoAllocation allocation) {

			final List<Object> localEquivalents = new ArrayList<>();
			for (final SlotAllocation sa : allocation.getSlotAllocations()) {
				localEquivalents.add(sa.getSlotVisit());
				localEquivalents.add(sa.getSlot());
			}
			localEquivalents.addAll(allocation.getEvents());

			return localEquivalents;

		} else if (event instanceof final VesselEventVisit vev) {
			return Collections.singletonList(vev.getVesselEvent());
		} else if (event instanceof final OpenSlotAllocation sa) {
			return Collections.singletonList(sa.getSlot());
		} else if (event instanceof StartEvent se) {
			if (se.getSequence() != null && se.getSequence().getVesselCharter() != null) {
				return Collections.singletonList(se.getSequence().getVesselCharter());
			}
		} else if (event instanceof EndEvent ee) {
			if (ee.getSequence() != null && ee.getSequence().getVesselCharter() != null) {
				return Collections.singletonList(ee.getSequence().getVesselCharter());
			}
		} else if (event instanceof final NonShippedSlotVisit slotVisit) {
			return Collections.singletonList(slotVisit.getSlot());
		}

		return Collections.emptyList();
		
	}

}
