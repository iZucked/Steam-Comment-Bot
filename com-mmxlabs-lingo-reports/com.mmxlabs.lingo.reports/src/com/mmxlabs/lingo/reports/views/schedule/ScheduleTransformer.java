/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.GCOCycleGroupingProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.IDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.StructuralDifferencesProcessor;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
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
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
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
			private final List<LNGScenarioModel> rootObjects = new LinkedList<>();
			private final List<IDiffProcessor> diffProcessors = new LinkedList<>();

			@Override
			public void beginCollecting() {
				super.beginCollecting();
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

				diffProcessors.clear();
				diffProcessors.add(new CycleDiffProcessor(customRelatedSlotHandlers));
				diffProcessors.add(new StructuralDifferencesProcessor(builder.getScheduleDiffUtils()));
				diffProcessors.add(new GCOCycleGroupingProcessor());
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean isPinned) {
				this.isPinned |= isPinned;

				numberOfSchedules++;

				for (final IDiffProcessor diffProcessor : diffProcessors) {
					diffProcessor.processSchedule(schedule, isPinned);
				}

				rootObjects.add(findScenarioModel(schedule));

				if (isPinned) {
					table.setPinnedScenario(scenarioInstance.getInstance());
				}
				table.getScenarios().add(scenarioInstance.getInstance());

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
						final Row row = elementToRowMap.get(element);
						if (row != null) {
							row.setVisible(true);
						}
					}
					// // Run diff processes.
					for (final IDiffProcessor diffProcessor : diffProcessors) {
						diffProcessor.runDiffProcess(table, referenceElements, uniqueElements, equivalancesMap, elementToRowMap);
					}
					renumberCycleGroups(table);
				}
				super.endCollecting();

				viewer.processInputs(table.getRows());

				builder.refreshPNLColumns(rootObjects);
			}

		};
	}

	public List<Row> generateRows(final Table dataModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean isPinned) {

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		final Set<EObject> allEvents = new HashSet<EObject>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (builder.showEvent(event)) {
					interestingEvents.add(event);
				}
				allEvents.add(event);
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			if (builder.showOpenSlot(openSlotAllocation)) {
				interestingEvents.add(openSlotAllocation);
			}
			allEvents.add(openSlotAllocation);
		}

		return generateRows(dataModelInstance, scenarioInstance, schedule, interestingEvents, allEvents, isPinned);
	}

	public List<Row> generateRows(final Table tableModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final List<EObject> interestingElements,
			final Set<EObject> allElements, final boolean isReferenceSchedule) {
		final List<Row> rows = new ArrayList<Row>(interestingElements.size());

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
						allElements.add(slot);

						Event evt = slot.getSlotVisit().getNextEvent();
						while (evt != null && !(evt instanceof SlotVisit)) {
							elementToRowMap.put(evt, row);
							evt = evt.getNextEvent();
							if (!cargoAllocation.getEvents().contains(evt)) {
								break;
							}
						}
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
						allElements.add(slot);

						Event evt = slot.getSlotVisit().getNextEvent();
						while (evt != null && !(evt instanceof SlotVisit)) {
							elementToRowMap.put(evt, row);
							evt = evt.getNextEvent();
							if (!cargoAllocation.getEvents().contains(evt)) {
								break;
							}
						}
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
				allElements.add(openSlotAllocation);
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
				allElements.add(event);
				if (isReferenceSchedule) {
					this.referenceElements.add(event);
				}
				addInputEquivalents(row, event);
				if (element instanceof EventGrouping) {
					final EventGrouping eventGrouping = (EventGrouping) element;
					for (final Event ge : eventGrouping.getEvents()) {
						elementToRowMap.put(ge, row);
					}
				}
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

	private void renumberCycleGroups(final Table table) {

		// For cargo based cycle groups, construct a particular diff message
		final Set<Pair<CycleGroup, Integer>> orderedCycleGroup = new TreeSet<>(new Comparator<Pair<CycleGroup, Integer>>() {

			@Override
			public int compare(final Pair<CycleGroup, Integer> o1, final Pair<CycleGroup, Integer> o2) {

				if (o1.getFirst() == o2.getFirst()) {
					return 0;
				}
				//
				int c = o2.getSecond() - o1.getSecond();
				if (c == 0) {
					final String desc2 = o2.getFirst().getDescription();
					final String desc1 = o1.getFirst().getDescription();
					if (desc2 == null) {
						c = 1;
					} else if (desc1 == null) {
						c = -1;
					} else {
						c = desc2.compareToIgnoreCase(desc1);
					}
				}
				if (c == 0) {
					c = o2.hashCode() - o1.hashCode();
				}
				return c;
			}
		});

		for (final CycleGroup group : table.getCycleGroups()) {
			// Skip empty groups
			if (group.getRows().isEmpty()) {
				continue;
			}

			final int pnlDelta = getPNLDelta(table, group);
			if (pnlDelta != 0) {
				orderedCycleGroup.add(new Pair<>(group, pnlDelta));
				// non-zero P&L, show group
				for (final Row row2 : group.getRows()) {
					row2.setVisible(true);
				}
			} else {
				// Zero P&L, hide group
				for (final Row row2 : group.getRows()) {
					row2.setVisible(false);
				}
			}
		}
		int goupCounter = 1;
		table.getCycleGroups().clear();
		for (final Pair<CycleGroup, Integer> p : orderedCycleGroup) {
			final CycleGroup group = p.getFirst();
			table.getCycleGroups().add(group);
			group.setIndex(goupCounter++);
		}
	}

	/**
	 * TODO: Also in {@link StandardScheduleColumnFactory}
	 * 
	 * @param object
	 * @return
	 */

	private int getPNLDelta(final Table tbl, final CycleGroup group) {
		// if (object instanceof Row) {
		// final Row row = (Row) object;
		//
		// // In simplePinDiff mode, we can activate row spanning, thus we always show total P&L delta. Otherwise show p&l delta only on the non-reference rows.
		// final Table tbl = row.getTable();
		final boolean simplePinDiff = tbl.getScenarios().size() == 2 && tbl.getPinnedScenario() != null;
		if (!simplePinDiff) {
			return 0;
		}
		//
		// // Disabled, see comment above.
		// if (!simplePinDiff && row.isReference()) {
		// return null;
		// }
		//
		// final CycleGroup group = row.getCycleGroup();
		if (group != null) {
			int delta = 0;
			for (final Row groupRow : group.getRows()) {
				final Integer pnl = getElementProfitAndLoss(groupRow.getTarget());
				if (pnl == null) {
					continue;
				}
				if (groupRow.isReference()) {
					delta -= pnl.intValue();
				} else {
					// // Exclude rows from other scenarios. (disabled, see comment above)
					// if (!simplePinDiff && groupRow.getSchedule() != row.getSchedule()) {
					// continue;
					// }
					delta += pnl.intValue();
				}
			}
			return delta;
		}
		// }

		return 0;
	}

	/**
	 * TODO: Also in {@link StandardScheduleColumnFactory}
	 * 
	 * @param object
	 * @return
	 */

	private Integer getElementProfitAndLoss(final Object object) {
		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut || object instanceof OpenSlotAllocation) {
			container = (ProfitAndLossContainer) object;
		}
		if (object instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) object;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}
		return null;
	}
}
