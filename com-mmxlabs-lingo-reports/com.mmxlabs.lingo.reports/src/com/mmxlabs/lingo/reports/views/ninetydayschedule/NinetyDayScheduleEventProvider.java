/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
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
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;

public class NinetyDayScheduleEventProvider implements IScheduleEventProvider<ScheduleModel> {
	
	private final VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
	private final EquivalentsManager equivalentsManager;

	public NinetyDayScheduleEventProvider(EquivalentsManager equivalentsManager) {
		this.equivalentsManager = equivalentsManager;
	}

	@Override
	public List<ScheduleEvent> getEvents(ScheduleModel input) {
		Schedule schedule = input.getSchedule();
		
		// contains Sequence and CombinedSequence
		List<Object> sequences = getSequences(schedule);
		List<Event> events = new ArrayList<>();
		
		for (Object o: sequences) {
			if (o instanceof Sequence s) {
				collectEvents(events, s);
			} else if (o instanceof CombinedSequence cs) {
				collectEvents(events, cs);
			}
		}
		
		events.stream().forEach(e -> equivalentsManager.collectEquivalents(e, this::generateEquivalents));
		return events.stream().map(this::makeScheduleEvent).collect(Collectors.toList());
	}

	@Override
	public String getKeyForEvent(ScheduleEvent event) {
		if (event.getData() instanceof Event e) {
			var key = vesselAssignmentFormatter.render(e);
			return key == null ? "" : key;
		}
		return "";
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

		return res;
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
			if (event instanceof final Journey journey 
					&& journey.getCanalDateTime() != null 
					&& journey.getRouteOption() == RouteOption.PANAMA 
					&& (s.getCharterInMarket() == null || s.getSpotIndex() != -1) 
					&& journey.getCanalJourneyEvent() != null) {
						journey.getCanalJourneyEvent().setLinkedSequence(s);
						res.add(journey.getCanalJourneyEvent());
			}
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
	
	private ScheduleEvent makeScheduleEvent(Event event) {

		// Get planned start and end dates
		LocalDateTime plannedStartDate = null;
		LocalDateTime plannedEndDate = null;

		if (event instanceof final SlotVisit visit) {
			final Slot<?> slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStart();
				final ZonedDateTime windowEndWithSlotOrPortTime = slot.getSchedulingTimeWindow().getEnd();
				if (windowStartWithSlotOrPortTime != null) {
					plannedStartDate = windowStartWithSlotOrPortTime.toLocalDateTime();
				}
				if (windowEndWithSlotOrPortTime != null) {
					plannedEndDate = windowEndWithSlotOrPortTime.toLocalDateTime();
				}
			}
		}
		
		ScheduleEvent se = new ScheduleEvent(event.getStart().toLocalDateTime(), event.getEnd().toLocalDateTime(), plannedStartDate, plannedEndDate, event);

		boolean visible = !(event instanceof StartEvent || event instanceof EndEvent);
		se.setVisible(visible);
		
		if (event instanceof final SlotVisit sv) {
			if (sv.getSlotAllocation().getSlot() != null) {
				se.setWindowStartDate(plannedStartDate);
				se.setWindowEndDate(plannedEndDate);
				se.setResizable(true);
			}
		}
		
		return se;
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
