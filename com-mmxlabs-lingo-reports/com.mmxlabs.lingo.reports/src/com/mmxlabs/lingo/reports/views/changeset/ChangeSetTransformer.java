/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleGroupUtils;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.EventGroupingOverlapProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.GCOCycleGroupingProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.IDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.LadenVoyageProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.StructuralDifferencesProcessor;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
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
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ChangeSetTransformer {

	/**
	 * Per scenario (index in order added, but with reference/pinned scenario at index 0) map of element key to elements
	 */

	private final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers;

	// private final List<LNGScenarioModel> rootObjects = new LinkedList<>();

	/**
	 * All the reference elements when in pin/diff mode.
	 */
	private final List<EObject> referenceElements = new LinkedList<>();
	private final List<Map<String, List<EObject>>> perScenarioElementsByKeyMap = new LinkedList<>();
	private final EquivalanceGroupBuilder equivalanceGroupBuilder = new EquivalanceGroupBuilder();
	/**
	 * Map between {@link Schedule} model elements and {@link Row}s
	 */
	private final Map<EObject, Row> elementToRowMap = new HashMap<>();

	private final ScheduleDiffUtils scheduleDiffUtils;

	public ChangeSetTransformer(final ScheduleDiffUtils scheduleDiffUtils, final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers) {
		this.scheduleDiffUtils = scheduleDiffUtils;
		this.customRelatedSlotHandlers = customRelatedSlotHandlers;
	}

	public ChangeSetRoot createDataModel(@NonNull final ScenarioInstance from, @NonNull final ScenarioInstance to, final IProgressMonitor monitor) {
		monitor.beginTask("Opening change sets", 1);
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		assert root != null;

		final List<IDiffProcessor> diffProcessors = new LinkedList<>();

		// Also if number of scenarios is 0 or 1
		final boolean enableDiffTools = LicenseFeatures.isPermitted("features:difftools");
		if (enableDiffTools) {
		}
		diffProcessors.add(new CycleDiffProcessor(customRelatedSlotHandlers));
		diffProcessors.add(new StructuralDifferencesProcessor(scheduleDiffUtils));
		// if (enableDiffTools) {
		diffProcessors.add(new GCOCycleGroupingProcessor());
		diffProcessors.add(new LadenVoyageProcessor());
		diffProcessors.add(new EventGroupingOverlapProcessor());
		// }

		final Schedule toSchedule = getSchedule(to);
		if (toSchedule == null) {
			return root;
		}
		for (final IDiffProcessor diffProcessor : diffProcessors) {
			diffProcessor.processSchedule(toSchedule, true);
		}

		final Schedule fromSchedule = getSchedule(from);
		if (fromSchedule == null) {
			return root;
		}
		for (final IDiffProcessor diffProcessor : diffProcessors) {
			diffProcessor.processSchedule(fromSchedule, false);
		}

		// rootObjects.add((LNGScenarioModel) to.getInstance());
		// rootObjects.add((LNGScenarioModel) from.getInstance());

		final Table table = ScheduleReportFactory.eINSTANCE.createTable();

		final Set<EObject> fromInterestingElements = new LinkedHashSet<>();
		final Set<EObject> toInterestingElements = new LinkedHashSet<>();
		final Set<EObject> fromAllElements = new LinkedHashSet<>();
		final Set<EObject> toAllElements = new LinkedHashSet<>();
		assert toSchedule != fromSchedule;
		extractElements(toSchedule, toInterestingElements, toAllElements);
		extractElements(fromSchedule, fromInterestingElements, fromAllElements);

		generateRows(table, to, toSchedule, toInterestingElements, toAllElements, true);
		generateRows(table, from, fromSchedule, fromInterestingElements, fromAllElements, false);

		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
		final List<EObject> uniqueElements = new LinkedList<>();
		equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, elementToRowMap);

		// Display unique rows.
		for (final EObject element : uniqueElements) {
			final Row row = elementToRowMap.get(element);
			if (row != null) {
				if (row.getCycleGroup() == null) {
					// Create a default group
					CycleGroupUtils.createOrReturnCycleGroup(table, row);
				}
			}
		}

		// Create basic groups based on reference/referencing relationship.
		for (final EObject referenceElement : referenceElements) {
			final Row referenceRow = elementToRowMap.get(referenceElement);
			if (referenceRow != null) {
				final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
				for (final Row row : referenceRow.getReferringRows()) {
					CycleGroupUtils.addToOrMergeCycleGroup(table, row, cycleGroup);
				}
			}
		}

		// // Run diff processes.
		for (final IDiffProcessor diffProcessor : diffProcessors) {
			diffProcessor.runDiffProcess(table, referenceElements, uniqueElements, equivalancesMap, elementToRowMap);
		}

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		List<ChangeSet> changeSets = new LinkedList<>();

		// Convert into new data model.
		for (final UserGroup g : table.getUserGroups()) {
			// final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
			// final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

			final ChangeSet changeSet = createChangeSet(root, from, to);
			final List<ChangeSetRow> rows = new LinkedList<>();

			for (final CycleGroup cycleGroup : g.getGroups()) {
				processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, true);
				processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, false);
			}
			processRows(toSchedule, fromSchedule, rows, changeSet);
			if (!changeSet.getChangeSetRowsToPrevious().isEmpty()) {
				changeSets.add(changeSet);
			}
		}
		for (final CycleGroup cycleGroup : table.getCycleGroups()) {
			final List<ChangeSetRow> rows = new LinkedList<>();

			final ChangeSet changeSet = createChangeSet(root, from, to);

			processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, true);
			processCycleGroup(cycleGroup, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, equivalancesMap, false);

			processRows(toSchedule, fromSchedule, rows, changeSet);
			if (!changeSet.getChangeSetRowsToPrevious().isEmpty()) {
				changeSets.add(changeSet);
			}
		}
		Collections.sort(changeSets, new Comparator<ChangeSet>() {

			@Override
			public int compare(ChangeSet o1, ChangeSet o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o2.getMetricsToPrevious().getPnlDelta(), o1.getMetricsToPrevious().getPnlDelta());
			}
		});
		root.getChangeSets().addAll(changeSets);
		return root;

	}

	@NonNull
	private ChangeSet createChangeSet(@NonNull final ChangeSetRoot root, @NonNull final ScenarioInstance prev, @NonNull final ScenarioInstance current) {
		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();

		// final ModelReference baseReference = base.getReference();
		final ModelReference prevReference = prev.getReference();
		final ModelReference currentReference = current.getReference();

		// Pre-Load
		// baseReference.getInstance();
		prevReference.getInstance();
		currentReference.getInstance();

		// final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		// changeSet.setBaseScenario(base);
		// changeSet.setBaseScenarioRef(baseReference);
		changeSet.setPrevScenario(prev);
		changeSet.setPrevScenarioRef(prevReference);
		changeSet.setCurrentScenario(current);
		changeSet.setCurrentScenarioRef(currentReference);

		return changeSet;
	}

	private void processRows(@NonNull final Schedule toSchedule, @NonNull final Schedule fromSchedule, @NonNull final List<ChangeSetRow> rows, @NonNull final ChangeSet changeSet) {
		ChangeSetTransformerUtil.setRowFlags(rows);
		ChangeSetTransformerUtil.filterRows(rows);
		ChangeSetTransformerUtil.sortRows(rows);
		changeSet.getChangeSetRowsToPrevious().addAll(rows);
		calculateMetrics(changeSet, fromSchedule, toSchedule);
	}

	public List<Row> generateRows(final Table tableModelInstance, final ScenarioInstance scenarioInstance, final Schedule schedule, final Collection<EObject> interestingElements,
			final Collection<EObject> allElements, final boolean isReferenceSchedule) {
		final List<Row> rows = new ArrayList<Row>(interestingElements.size());

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
					row.setSequence(cargoAllocation.getSequence());
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
					} // else
					{
						row.setName(slot.getName());
					}
				}
				addInputEquivalents(row, openSlotAllocation);
				rows.add(row);
			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final Row row = ScheduleReportFactory.eINSTANCE.createRow();
				row.setSequence(event.getSequence());
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
		} else if (a instanceof GeneratedCharterOut) {
			final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) a;
			row.getInputEquivalents().add(generatedCharterOut);
			row.getInputEquivalents().addAll(generatedCharterOut.getEvents());
		} else if (a instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) a;
			row.getInputEquivalents().addAll(startEvent.getEvents());
			final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
			if (vesselAvailability != null) {
				row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(startEvent, vesselAvailability.getVessel()));
			}
		} else if (a instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) a;
			row.getInputEquivalents().add(endEvent);
		} else if (a instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		} else {
			row.getInputEquivalents().addAll(Lists.<EObject> newArrayList(a));
		}
		// FIXME: Added to vessel report, but this causing any event on a sequence to be "equivalent" to any other
		if (row.getSequence() != null) {
			// row.getInputEquivalents().add(row.getSequence());
		}
	}

	// private ChangeSet buildChangeSet(final ScenarioInstance base, final ScenarioInstance prev, final ScenarioInstance current) {
	// final ModelReference baseReference = base.getReference();
	// final ModelReference prevReference = prev.getReference();
	// final ModelReference currentReference = current.getReference();
	//
	// // Pre-Load
	// baseReference.getInstance();
	// prevReference.getInstance();
	// currentReference.getInstance();
	//
	// final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
	// changeSet.setBaseScenario(base);
	// changeSet.setBaseScenarioRef(baseReference);
	// changeSet.setPrevScenario(prev);
	// changeSet.setPrevScenarioRef(prevReference);
	// changeSet.setCurrentScenario(current);
	// changeSet.setCurrentScenarioRef(currentReference);
	//
	// generateDifferences(base, current, changeSet, true);
	// generateDifferences(prev, current, changeSet, false);
	//
	// return changeSet;
	// }

	/**
	 * Find elements that we are interested in showing in the view.
	 *
	 * @param schedule
	 * @param interestingEvents
	 * @param allEvents
	 */
	private void extractElements(final Schedule schedule, final Collection<EObject> interestingEvents, final Collection<EObject> allEvents) {
		if (schedule == null) {
			return;
		}

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				boolean includeEvent = false;
				if (event instanceof SlotVisit) {
					includeEvent = true;
				} else if (event instanceof VesselEventVisit) {
					includeEvent = true;

				} else if (event instanceof StartEvent) {
					includeEvent = true;
				} else if (event instanceof EndEvent) {
					includeEvent = true;

				}
				if (includeEvent) {
					interestingEvents.add(event);
				}
				allEvents.add(event);
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			interestingEvents.add(openSlotAllocation);
			allEvents.add(openSlotAllocation);
		}
	}

	private void processCycleGroup(final CycleGroup cycleGroup, @NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap,
			Map<String, List<ChangeSetRow>> lhsRowMarketMap, Map<String, List<ChangeSetRow>> rhsRowMarketMap, @NonNull final List<ChangeSetRow> rows, final Map<EObject, Set<EObject>> equivalancesMap,
			final boolean firstPass) {
		for (final Row r : cycleGroup.getRows()) {

			boolean isBase = true;
			if (firstPass && !r.isReference()) {
				// continue;
			}

			if (firstPass && !r.isReference()) {
				if (r.getReferenceRow() == null) {// || r.getReferringRows().isEmpty()) {
					// && !r.isReference() && r.getReferenceRow() != null && !r.getReferringRows().isEmpty()) {
					isBase = false;
				} else {
					continue;
				}
			}

			EObject element = r.getTarget();
			if (element instanceof CargoAllocation) {
				CargoAllocation cargoAllocation = (CargoAllocation) element;
				for (SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					if (slotAllocation.getSlot() instanceof LoadSlot) {
						SlotVisit slotVisit = slotAllocation.getSlotVisit();
						element = slotVisit;
						break;
					}
				}
			}
			assert element != null;

			if (firstPass) {
				ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, isBase, false);
			} else {

				final Set<EObject> equivalents = equivalancesMap.get(element);
				if (equivalents == null) {
					continue;
				}

				if (element instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) element;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();

						for (final EObject e : equivalents) {
							if (e instanceof SlotVisit) {
								final SlotVisit slotVisit2 = (SlotVisit) e;
								final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
								if (cargoAllocation.getSlotAllocations().size() != 2) {
									throw new RuntimeException("Complex cargoes are not supported");
								}
								ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit2,
										(LoadSlot) slotVisit2.getSlotAllocation().getSlot(), false, false);
							}
						}
					}
				} else if (element instanceof OpenSlotAllocation) {
					final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
					for (final EObject e : equivalents) {
						if (e instanceof OpenSlotAllocation) {
							final OpenSlotAllocation openSlotAllocation2 = (OpenSlotAllocation) e;
							ChangeSetTransformerUtil.createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation2, false);
						}
					}
				} else if (element instanceof Event) {
					final Event event = (Event) element;
					ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, false);
				}
			}
		}
	}

	@Nullable
	private Schedule getSchedule(@NonNull final ScenarioInstance scenarioInstance) {
		final LNGScenarioModel scenarioModel = findScenarioModel(scenarioInstance);
		return ScenarioModelUtil.findSchedule(scenarioModel);
	}

	@NonNull
	private static LNGScenarioModel findScenarioModel(@NonNull final ScenarioInstance scenarioInstance) {
		return (LNGScenarioModel) scenarioInstance.getInstance();
	}

	public static void calculateMetrics(@NonNull final ChangeSet changeSet, @NonNull final Schedule fromSchedule, @NonNull final Schedule toSchedule) {
		final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
		final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();

		{
			long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(toSchedule);
			int lateness = ScheduleModelKPIUtils.getScheduleLateness(toSchedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
			int violations = ScheduleModelKPIUtils.getScheduleViolationCount(toSchedule);

			currentMetrics.setPnl((int) pnl);
			currentMetrics.setCapacity((int) violations);
			currentMetrics.setLateness((int) lateness);
		}

		long pnl = 0L;
		long violations = 0L;
		long lateness = 0L;
		{
			for (ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				{
					ProfitAndLossContainer newGroupProfitAndLoss = row.getNewGroupProfitAndLoss();
					if (newGroupProfitAndLoss != null) {
						final GroupProfitAndLoss groupProfitAndLoss = newGroupProfitAndLoss.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl += groupProfitAndLoss.getProfitAndLoss();
						}
						if (groupProfitAndLoss instanceof CargoAllocation) {
							CargoAllocation cargoAllocation = (CargoAllocation) groupProfitAndLoss;
							lateness += LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
							violations += ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
						}
					}

					ProfitAndLossContainer originalGroupProfitAndLoss = row.getOriginalGroupProfitAndLoss();
					if (originalGroupProfitAndLoss != null) {
						final GroupProfitAndLoss groupProfitAndLoss = originalGroupProfitAndLoss.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl -= groupProfitAndLoss.getProfitAndLoss();
						}
						if (groupProfitAndLoss instanceof CargoAllocation) {
							CargoAllocation cargoAllocation = (CargoAllocation) groupProfitAndLoss;
							lateness -= LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
							violations -= ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
						}
					}
				}
			}
		}
		deltaMetrics.setPnlDelta((int) pnl);
		deltaMetrics.setLatenessDelta((int) lateness);
		deltaMetrics.setCapacityDelta((int) violations);
		changeSet.setMetricsToPrevious(deltaMetrics);

		changeSet.setCurrentMetrics(currentMetrics);
	}
}
