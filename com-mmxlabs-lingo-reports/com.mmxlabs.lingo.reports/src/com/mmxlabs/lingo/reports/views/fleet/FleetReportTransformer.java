/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class FleetReportTransformer {

	/**
	 * Map between {@link Schedule} model elements and {@link Row}s
	 */
	private final Map<EObject, Row> elementToRowMap = new HashMap<>();

	/**
	 * All the reference elements when in pin/diff mode.
	 */
	private List<EObject> referenceElements;

	/**
	 * Per scenario (index in order added, but with reference/pinned scenario at index 0) map of element key to elements
	 */
	private final List<Map<String, List<EObject>>> perScenarioElementsByKeyMap = new LinkedList<>();

	private final FleetEquivalanceGroupBuilder equivalanceGroupBuilder = new FleetEquivalanceGroupBuilder();

	private final FleetBasedReportBuilder builder;

	private final Table table;

	public FleetReportTransformer(final Table table, final FleetBasedReportBuilder builder) {
		this.table = table;
		this.builder = builder;
	}

	public IScenarioInstanceElementCollector getElementCollector(final ConfigurableFleetReportView viewer) {
		return new ScheduleElementCollector() {

			private boolean isPinned = false;
			private int numberOfSchedules;
			private final List<LNGScenarioModel> rootObjects = new LinkedList<>();

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);
				numberOfSchedules = 0;
				isPinned = false;
				perScenarioElementsByKeyMap.clear();
				elementToRowMap.clear();
				rootObjects.clear();

				table.getRowGroups().clear();
				table.getRows().clear();
				table.getCycleGroups().clear();
				table.getUserGroups().clear();
				table.getScenarios().clear();
				table.setPinnedScenario(null);

			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean isPinned) {
				this.isPinned |= isPinned;

				numberOfSchedules++;

				rootObjects.add(scenarioModel);

				if (isPinned) {
					table.setPinnedScenario(scenarioModel);
				}
				table.getScenarios().add(scenarioModel);

				return generateRows(table, scenarioInstance, schedule, isPinned);
			}

			@Override
			public void endCollecting() {
				// In Pin/Diff mode

				final boolean pinDiffMode = numberOfSchedules > 1 && isPinned;
				for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
					if (handler != null) {
						handler.setViewState(numberOfSchedules > 1, pinDiffMode);
					}
				}

				// Show all rows
				for (final Row row : table.getRows()) {
					row.setVisible(true);
				}
				if (!isPinned || numberOfSchedules == 1) {
				} else {

					// Pin Mode

					// Hide all rows by default
					final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
					final List<EObject> uniqueElements = new LinkedList<>();
					equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, elementToRowMap);
				}
				super.endCollecting();

				viewer.processInputs(table.getRows());
			}

		};
	}

	public List<Row> generateRows(final Table dataModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean isPinned) {

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		final Set<EObject> allEvents = new HashSet<EObject>();
		final Set<Vessel> seenVessels = new HashSet<Vessel>();
		for (final Sequence sequence : schedule.getSequences()) {
			if (builder.showEvent(sequence)) {
				final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
				if (vesselAvailability != null) {
					if (!seenVessels.contains(vesselAvailability.getVessel())) {
						interestingEvents.add(sequence);
						seenVessels.add(vesselAvailability.getVessel());
					}
				}
			}
			allEvents.add(sequence);
		}

		return generateRows(dataModelInstance, scenarioInstance, schedule, interestingEvents, allEvents, isPinned);
	}

	public List<Row> generateRows(final Table tableModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final List<EObject> interestingElements,
			final Set<EObject> allElements, final boolean isReferenceSchedule) {
		final List<Row> rows = new ArrayList<>(interestingElements.size());

		if (isReferenceSchedule) {
			this.referenceElements = new LinkedList<>();
		}
		for (final Object element : interestingElements) {

			if (element instanceof Sequence) {
				final Sequence sequence = (Sequence) element;
				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setTarget(sequence);
				row.setName(sequence.getName());
				row.setSequence(sequence);
				final EList<Sequence> linkedSequences = row.getLinkedSequences();
				final List<Sequence> foundSequences = schedule.getSequences().stream().filter(s -> (s.getVesselAvailability() != null && s.getVesselAvailability().getVessel() != null
						&& s.getVesselAvailability().getVessel().equals(sequence.getVesselAvailability().getVessel()))).collect(Collectors.toList());
				linkedSequences.addAll(foundSequences);
				rows.add(row);

				elementToRowMap.put(sequence, row);
				allElements.add(sequence);
				if (isReferenceSchedule) {
					this.referenceElements.add(sequence);
				}
				addInputEquivalents(row, sequence);
			}

		}
		// If this is the reference schedule, then mark all rows as reference
		if (isReferenceSchedule) {
			for (final Row row : rows) {
				row.setReference(true);
			}
		}

		for (final Row row : rows) {
			row.setScenario(scenarioInstance);
			row.setSchedule(schedule);
		}

		// Generate the element by key map
		final Map<String, List<EObject>> map = equivalanceGroupBuilder.generateElementNameGroups(allElements);
		if (isReferenceSchedule) {
			perScenarioElementsByKeyMap.add(0, map);
		} else {
			perScenarioElementsByKeyMap.add(map);
		}

		// Add rows to the table!
		tableModelInstance.getRows().addAll(rows);

		return rows;
	}

	public void addInputEquivalents(@NonNull final Row row, @NonNull final EObject a) {

		// map to events
		if (a instanceof CargoAllocation) {
			final CargoAllocation allocation = (CargoAllocation) a;

			final List<Object> equivalents = new LinkedList<Object>();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			row.getInputEquivalents().addAll(allocation.getEvents());
			row.getInputEquivalents().add(allocation.getInputCargo());
		} else if (a instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) a;

			final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				row.getInputEquivalents().add(slotAllocation.getSlot());
				row.getInputEquivalents().add(slotAllocation.getSlotVisit());
			}
			row.getInputEquivalents().addAll(allocation.getEvents());
			row.getInputEquivalents().add(allocation.getInputCargo());
		} else if (a instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
			row.getInputEquivalents().addAll(vesselEventVisit.getEvents());
		} else if (a instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) a;
			row.getInputEquivalents().addAll(startEvent.getEvents());
			final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
			if (vesselAvailability != null) {
				row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(startEvent, vesselAvailability.getVessel()));
			}
		} else if (a instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		} else {
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(a));
		}
	}
}
