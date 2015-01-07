package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.IDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.StructuralDifferencesProcessor;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScheduleTransformer {

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

	private final EquivalanceGroupBuilder equivalanceGroupBuilder = new EquivalanceGroupBuilder();

	private final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers;

	private final ScheduleBasedReportBuilder builder;

	private final Table table;

	public ScheduleTransformer(final Table table, final ScheduleBasedReportBuilder builder, final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers) {
		this.table = table;
		this.builder = builder;
		this.customRelatedSlotHandlers = customRelatedSlotHandlers;
	}

	public IScenarioInstanceElementCollector getElementCollector(final ConfigurableScheduleReportView viewer) {
		return new ScheduleElementCollector() {

			private boolean isPinned = false;
			private int numberOfSchedules;
			private List<LNGScenarioModel> rootObjects = new LinkedList<>();
			private final List<IDiffProcessor> diffProcessors = new LinkedList<>();

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				numberOfSchedules = 0;
				isPinned = false;
				perScenarioElementsByKeyMap.clear();
				elementToRowMap.clear();
				rootObjects.clear();
				// tableDataModel = ScheduleReportFactory.eINSTANCE.createTable();

				table.getRowGroups().clear();
				table.getRows().clear();
				table.getCycleGroups().clear();
				
				diffProcessors.clear();
				 diffProcessors.add(new CycleDiffProcessor(customRelatedSlotHandlers));
				diffProcessors.add(new StructuralDifferencesProcessor(builder.getScheduleDiffUtils()));
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean isPinned) {
				this.isPinned |= isPinned;

				numberOfSchedules++;

				for (final IDiffProcessor diffProcessor : diffProcessors) {
					diffProcessor.processSchedule(schedule, isPinned);
				}

				rootObjects.add(findScenarioModel(schedule));

				return generateRows(table, scenarioInstance, schedule, isPinned);
			}

			LNGScenarioModel findScenarioModel(final Schedule schedule) {
				EObject container = schedule.eContainer();
				while (container != null && !(container instanceof LNGScenarioModel)) {
					container = container.eContainer();
				}
				return (LNGScenarioModel) container;
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

				// Also if number of scenarios is 0 or 1
				if (!isPinned) {
					// Show all rows
					for (final Row row : table.getRows()) {
						row.setVisible(true);
					}
				} else {

					// Pin Mode

					// Hide all rows by default
					for (final Row row : table.getRows()) {
						row.setVisible(false);
					}

					final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
					final List<EObject> uniqueElements = new LinkedList<>();
					equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, elementToRowMap);

					// Display unique rows.
					for (final EObject element : uniqueElements) {
						elementToRowMap.get(element).setVisible(true);
					}
					// // Run diff processes.
					for (final IDiffProcessor diffProcessor : diffProcessors) {
						diffProcessor.runDiffProcess(table, referenceElements, uniqueElements, equivalancesMap, elementToRowMap);
					}
				}
				super.endCollecting();

				viewer.processInputs(table.getRows());

				builder.refreshPNLColumns(rootObjects);
			}

		};
	}

	public List<Row> generateRows(final Table dataModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean isPinned) {

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (builder.showEvent(event)) {
					interestingEvents.add(event);
				}
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			if (builder.showOpenSlot(openSlotAllocation)) {
				interestingEvents.add(openSlotAllocation);
			}
		}

		return generateRows(dataModelInstance, scenarioInstance, schedule, interestingEvents, isPinned);
	}

	public List<Row> generateRows(final Table tableModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final List<EObject> interestingElements,
			final boolean isReferenceSchedule) {
		final List<Row> rows = new ArrayList<Row>(interestingElements.size());

		final List<EObject> scheduleElements = new LinkedList<>();
		if (isReferenceSchedule) {
			this.referenceElements = new LinkedList<>();
		}
		for (final Object element : interestingElements) {

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();

				// Build up list of slots assigned to cargo, sorting into loads and discharges
				final List<SlotAllocation> loadSlots = new ArrayList<SlotAllocation>();
				final List<SlotAllocation> dischargeSlots = new ArrayList<SlotAllocation>();
				for (final SlotAllocation slot : cargoAllocation.getSlotAllocations()) {
					if (slot.getSlot() instanceof LoadSlot) {
						loadSlots.add(slot);
					} else if (slot.getSlot() instanceof DischargeSlot) {
						dischargeSlots.add(slot);
					} else {
						// Assume some kind of discharge?
						// dischargeSlots.add((Slot) slot);
					}

				}

				final RowGroup group = ScheduleReportFactory.eINSTANCE.createRowGroup();
				tableModelInstance.getRowGroups().add(group);
				// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
				for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {

					final Row row = ScheduleReportFactory.eINSTANCE.createRow();
					row.setTarget(cargoAllocation);
					row.setCargoAllocation(cargoAllocation);
					row.setRowGroup(group);
					if (i < loadSlots.size()) {
						final SlotAllocation slot = loadSlots.get(i);
						row.setLoadAllocation(slot);
						row.setName(slot.getName());
						elementToRowMap.put(slot, row);
						elementToRowMap.put(slot.getSlotVisit(), row);
						if (isReferenceSchedule) {

							this.referenceElements.add(slot);
						}
						scheduleElements.add(slot);
					}
					if (i < dischargeSlots.size()) {
						final SlotAllocation slot = dischargeSlots.get(i);
						row.setDischargeAllocation(slot);
						row.setName2(slot.getName());
						elementToRowMap.put(slot, row);
						elementToRowMap.put(slot.getSlotVisit(), row);
						if (isReferenceSchedule) {

							this.referenceElements.add(slot);
						}
						scheduleElements.add(slot);
					}
					rows.add(row);
					addInputEquivalents(row, cargoAllocation);
				}
			} else if (element instanceof OpenSlotAllocation) {

				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;

				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setTarget(openSlotAllocation);
				row.setOpenSlotAllocation(openSlotAllocation);
				elementToRowMap.put(openSlotAllocation, row);
				scheduleElements.add(openSlotAllocation);
				if (isReferenceSchedule) {
					this.referenceElements.add(openSlotAllocation);
				}
				final Slot slot = openSlotAllocation.getSlot();
				if (slot == null) {
					row.setName("??");
				} else {
					if (slot instanceof DischargeSlot) {
						row.setName2(slot.getName());
					} else {
						row.setName(slot.getName());

					}
				}
				addInputEquivalents(row, openSlotAllocation);
				rows.add(row);

			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setTarget(event);
				row.setName(event.name());
				rows.add(row);

				elementToRowMap.put(event, row);
				scheduleElements.add(event);
				if (isReferenceSchedule) {
					this.referenceElements.add(event);
				}
				addInputEquivalents(row, event);
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
		final Map<String, List<EObject>> map = equivalanceGroupBuilder.generateElementNameGroups(scheduleElements);
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
		} else if (a instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) a;
			final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
			if (vesselAvailability != null) {
				row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(startEvent, vesselAvailability.getVessel()));
			}
		} else if (a instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		}
	}
}
