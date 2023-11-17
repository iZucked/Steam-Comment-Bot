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
import org.eclipse.jface.dialogs.MessageDialog;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.lingo.reports.services.EquivalentsManager;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSeqenceElements;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceElement;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.BuySellSplit;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.EnabledPositionsSequenceProviderTracker;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.ISchedulePositionsSequenceProvider;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.ISchedulePositionsSequenceProviderExtension;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.PositionsSequenceProviderException;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.ENinteyDayNonShippedRotationSelection;
import com.mmxlabs.widgets.schedulechart.EditableScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.IScheduleChartRowsDataProvider;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.providers.IScheduleEventProvider;

public class NinetyDayScheduleEventProvider implements IScheduleEventProvider<NinetyDayScheduleInput> {

	private final VesselAssignmentFormatter vesselAssignmentFormatter = new VesselAssignmentFormatter();
	private final EquivalentsManager equivalentsManager;
	private final IScheduleChartSettings settings;
	private final EnabledPositionsSequenceProviderTracker enabledPSPTracker = new EnabledPositionsSequenceProviderTracker();
	private final IScheduleChartRowsDataProvider scheduleCharRowsDataProvider;
	@Inject
	private Iterable<ISchedulePositionsSequenceProviderExtension> positionsSequenceProviderExtensions;
	private final NinetyDayScheduleReport parent;

	public NinetyDayScheduleEventProvider(EquivalentsManager equivalentsManager, IScheduleChartSettings settings, IScheduleChartRowsDataProvider scheduleCharRowsDataProvider,
			NinetyDayScheduleReport parent) {
		this.equivalentsManager = equivalentsManager;
		this.settings = settings;
		this.scheduleCharRowsDataProvider = scheduleCharRowsDataProvider;
		this.parent = parent;

	}

	@Override
	public List<ScheduleEvent> getEvents(NinetyDayScheduleInput input) {
		List<ScheduleEvent> res = new ArrayList<>();
		res.addAll(getEventForScenarioResult(input.pinned(), true));
		for (ScenarioResult sr : input.other()) {
			res.addAll(getEventForScenarioResult(sr, false));
		}
		return res;
	}

	public void injectMembers(Injector injector) {
		injector.injectMembers(this);
	}

	private List<ScheduleEvent> getEventForScenarioResult(ScenarioResult sr, boolean isPinned) {
		if (sr == null) {
			return Collections.emptyList();
		}
		final LNGScenarioModel scenarioModel = sr.getTypedRoot(LNGScenarioModel.class);
		if (scenarioModel == null) {
			return Collections.emptyList();
		}
		final ScheduleModel schedule = sr.getTypedResult(ScheduleModel.class);
		final Schedule lastScheduleFromScenario = schedule == null ? null : schedule.getSchedule();
		if (lastScheduleFromScenario == null) {
			return Collections.emptyList();
		}

		// contains Sequence, CombinedSequence and PositionsSequence
		final List<Object> sequences = getSequences(lastScheduleFromScenario);
		final List<Event> events = new ArrayList<>();
		final List<Object> elements = new ArrayList<>();
		final List<ScheduleEvent> positionSequenceScheduleEvents = new ArrayList<>();

		for (final Object sequenceObject : sequences) {
			if (sequenceObject instanceof Sequence sequence) {
				collectEvents(events, sequence);
			} else if (sequenceObject instanceof NonShippedSequence nonShippedSequence) {
				collectEvents(events, nonShippedSequence);
			} else if (sequenceObject instanceof CombinedSequence combinedSequence) {
				collectEvents(events, combinedSequence);
			} else if (sequenceObject instanceof PositionsSequence positionSequence) {
				collectEvents(elements, positionSequenceScheduleEvents, positionSequence, sr, isPinned);
			}
		}

		elements.stream().forEach(e -> equivalentsManager.collectEquivalents(e, this::generateEquivalents));
		events.stream().forEach(e -> equivalentsManager.collectEquivalents(e, this::generateEquivalents));
		final List<ScheduleEvent> scheduleEvents = events.stream().map(e -> makeScheduleEvent(e, sr, isPinned)).collect(Collectors.toList());
		scheduleEvents.addAll(positionSequenceScheduleEvents);
		return scheduleEvents;

	}

	@Override
	public ScheduleChartRowKey getKeyForEvent(ScheduleEvent event) {
		if (event.getData() instanceof final PositionsSequenceElement positionsSequenceElement) {

			return new ScheduleChartRowKey(positionsSequenceElement.getPositionsSequence().toString(), positionsSequenceElement.getPositionsSequence());
		}
		if (event.getData() instanceof final Event e) {
			// Check if event is a non-shipped event
			if (e.eContainer() instanceof NonShippedSequence nss) {
				var name = nss.getVessel() == null ? "" : nss.getVessel().getName();
				return new ScheduleChartRowKey(name, nss);
			}

			var name = vesselAssignmentFormatter.render(e);
			return new ScheduleChartRowKey(name == null ? "" : name, e.getSequence());
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
			@NonNull
			final Sequence thisSequence = unassigned.get(0);
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

	private List<@NonNull PositionsSequence> addBuySellExtensions(Schedule schedule, Collection<SlotVisit> slotVisitsToIgnore) {
		List<@NonNull PositionsSequence> result = new ArrayList<>();

		if (enabledPSPTracker.hasInputChanged()) {
			enabledPSPTracker.clearErrors();
			enabledPSPTracker.collectErrors(positionsSequenceProviderExtensions, schedule, slotVisitsToIgnore);
			enabledPSPTracker.setInputChanged(false);
		}

		if (positionsSequenceProviderExtensions.iterator().hasNext()) {
			for (var ext : positionsSequenceProviderExtensions) {
				ISchedulePositionsSequenceProvider provider = ext.createInstance();
				try {
					if (enabledPSPTracker.isEnabledWithNoError(provider.getId())) {
						result.addAll(provider.provide(schedule, slotVisitsToIgnore));
					}
				} catch (PositionsSequenceProviderException e) {
					enabledPSPTracker.addError(provider.getId(), e);
					MessageDialog dialog = new MessageDialog(parent.getViewer().getControl().getShell(), e.getTitle(), null, e.getDescription(), 0, 0, "OK");
					dialog.create();
					dialog.open();
				}
			}
		}

		if (result.isEmpty()) {
			try {
				return new BuySellSplit().provide(schedule, slotVisitsToIgnore);
			} catch (PositionsSequenceProviderException e) {
				// BuySellSplit should never throw this exception
			}
		}

		return result;
	}

	private void addPositionsSequences(List<Object> res, Schedule schedule) {
		final Collection<Slot<?>> nonShippedSlots = new HashSet<>();
		for (final NonShippedSequence seq : schedule.getNonShippedSequences()) {
			if (parent.getFobRotationsToShow().stream().anyMatch(p -> p.test(seq))) {
				res.add(seq);
				for (final Event event : seq.getEvents()) {
					if (event instanceof NonShippedSlotVisit slotVisit) {
						nonShippedSlots.add(slotVisit.getSlot());
					}
				}
			}
		}

		final @NonNull Collection<@NonNull SlotVisit> slotVisitsToIgnore;
		if (parent.getFobRotationOptionSelection() == ENinteyDayNonShippedRotationSelection.OFF) {
			slotVisitsToIgnore = Collections.emptySet();
		} else {
			slotVisitsToIgnore = schedule.getCargoAllocations().stream() //
					.filter(ca -> !parent.getSelectedContracts().contains(ca.getSlotAllocations().get(0).getSlot().getContract())).map(CargoAllocation::getSlotAllocations) //
					.flatMap(List::stream) //
					.filter(sa -> nonShippedSlots.contains(sa.getSlot())) //
					.map(SlotAllocation::getSlotVisit).collect(Collectors.toSet());
		}
		res.addAll(addBuySellExtensions(schedule, slotVisitsToIgnore));
		// res.addAll(PositionsSequence.makeBuySellSequences(schedule, slotVisitsToIgnore, ""));
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
		for (final Sequence s : cs.getSequences()) {
			collectEvents(res, s);
		}
	}

	private void collectEvents(List<Event> res, NonShippedSequence nss) {
		for (final Event e : nss.getEvents()) {
			res.add(e);
		}
	}

	private void collectEvents(final List<Object> elements, final List<ScheduleEvent> scheduleEvents, final PositionsSequence positionSequence, ScenarioResult scenarioResult, boolean isPinned) {
		for (final Object element : positionSequence.getElements()) {
			final LocalDateTime startTime = PositionsSeqenceElements.getEventTime(element);
			final ScheduleEvent scheduleEvent = new ScheduleEvent(startTime, startTime, PositionsSequenceElement.of(element, positionSequence.isBuy(), positionSequence), element, scenarioResult,
					isPinned, List.of(), false);
			scheduleEvent.forceVisible();

			elements.add(element);
			scheduleEvents.add(scheduleEvent);
		}
	}

	private ScheduleEvent makeScheduleEvent(Event event, ScenarioResult scenarioResult, boolean isPinned) {
		ScheduleEvent se = new ScheduleEvent(event.getStart().toLocalDateTime(), event.getEnd().toLocalDateTime(), event, null, scenarioResult, isPinned, makeEventAnnotations(event));
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
			final var canalJourneyAnnotation = new ScheduleEventAnnotation(List.of(j.getCanalDateTime()), NinetyDayScheduleEventAnnotationType.CANAL_JOURNEY);
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

	@Override
	public List<ScheduleChartRow> classifyEventsIntoRows(final List<ScheduleEvent> events) {
		final List<ScheduleChartRow> classified = IScheduleEventProvider.super.classifyEventsIntoRows(events);
		return classified;
	}

	@Override
	public IScheduleChartRowsDataProvider getRowsDataProvider() {
		return scheduleCharRowsDataProvider;
	}

	public EnabledPositionsSequenceProviderTracker getEnabledPositionsSequenceTracker() {
		return enabledPSPTracker;
	}
}
