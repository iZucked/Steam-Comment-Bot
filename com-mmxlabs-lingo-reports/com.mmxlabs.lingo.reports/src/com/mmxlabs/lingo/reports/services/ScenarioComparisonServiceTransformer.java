/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.views.framelist.GoIntoAction;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.CycleGroupUtils;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.EventGroupingOverlapProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.GCOCycleGroupingProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.IDiffProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.LadenVoyageProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.StartEventProcessor;
import com.mmxlabs.lingo.reports.views.schedule.diffprocessors.StructuralDifferencesProcessor;
import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ScenarioComparisonServiceTransformer {

	private final class UserGroupPNLDeltaComparator implements Comparator<Pair<UserGroup, Integer>> {
		@Override
		public int compare(final Pair<UserGroup, Integer> o1, final Pair<UserGroup, Integer> o2) {

			if (o1.getFirst() == o2.getFirst()) {
				return 0;
			}
			//
			int c = o2.getSecond() - o1.getSecond();
			if (c == 0) {
				final String desc2 = o2.getFirst().getComment();
				final String desc1 = o1.getFirst().getComment();
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
	}

	private final class CycleGroupPNLComparator implements Comparator<Pair<CycleGroup, Integer>> {
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
	}

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

	private final ScheduleDiffUtils scheduleDiffUtils;

	private final Table table;
	private Map<EObject, Set<EObject>> equivalancesMap;
	private final List<LNGScenarioModel> rootObjects = new LinkedList<>();

	public ScenarioComparisonServiceTransformer(final Table table, final ScheduleDiffUtils scheduleDiffUtils, final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers) {
		this.table = table;
		this.scheduleDiffUtils = scheduleDiffUtils;
		this.customRelatedSlotHandlers = customRelatedSlotHandlers;
	}

	public static class TransformResult {
		public final Table table;
		public final List<LNGScenarioModel> rootObjects;
		public final Map<EObject, Set<EObject>> equivalancesMap;
		ISelectedDataProvider selectedDataProvider;
		final ScenarioResult pinned;
		final Collection<ScenarioResult> others;

		public TransformResult(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final List<LNGScenarioModel> rootObjects, final ScenarioResult pinned,
				final Collection<ScenarioResult> others) {
			this.table = table;
			this.equivalancesMap = equivalancesMap;
			this.rootObjects = rootObjects;
			this.pinned = pinned;
			this.others = others;
		}
	}

	@NonNull
	public static TransformResult transform(final ScenarioResult pinned, final Collection<ScenarioResult> others, @NonNull ISelectedDataProvider selectedDataProvider,
			final ScheduleDiffUtils scheduleDiffUtils, final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers) {

		final Table table = ScheduleReportFactory.eINSTANCE.createTable();
		final ScenarioComparisonServiceTransformer transformer = new ScenarioComparisonServiceTransformer(table, scheduleDiffUtils, customRelatedSlotHandlers);
		final IScenarioInstanceElementCollector elementCollector = transformer.getElementCollector();

		elementCollector.beginCollecting(pinned != null);
		if (pinned != null) {
			elementCollector.collectElements(pinned, true);
		}
		for (final ScenarioResult other : others) {
			elementCollector.collectElements(other, false);
		}
		elementCollector.endCollecting();
		final TransformResult result = new TransformResult(table, transformer.equivalancesMap, transformer.rootObjects, pinned, others);
		return result;
	}

	public IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private boolean isPinned = false;
			private int numberOfSchedules;
			private final List<IDiffProcessor> diffProcessors = new LinkedList<>();

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

				diffProcessors.clear();

				// Also if number of scenarios is 0 or 1
				final boolean enableDiffTools = LicenseFeatures.isPermitted("features:difftools");
				if (enableDiffTools) {
					diffProcessors.add(new CycleDiffProcessor(customRelatedSlotHandlers));
				}
				diffProcessors.add(new StructuralDifferencesProcessor(scheduleDiffUtils));
				if (enableDiffTools) {
					diffProcessors.add(new GCOCycleGroupingProcessor());
					diffProcessors.add(new LadenVoyageProcessor());
					diffProcessors.add(new EventGroupingOverlapProcessor());
					diffProcessors.add(new StartEventProcessor());
				}
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, LNGScenarioModel scenarioModel, final Schedule schedule, final boolean isPinned) {
				this.isPinned |= isPinned;

				numberOfSchedules++;

				for (final IDiffProcessor diffProcessor : diffProcessors) {
					diffProcessor.processSchedule(schedule, isPinned);
				}

				rootObjects.add(findScenarioModel(schedule));

				if (isPinned) {
					table.setPinnedScenario(scenarioModel);
				}
				table.getScenarios().add(scenarioModel);

				return generateRows(table, scenarioResult, schedule, isPinned);
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

				if (!isPinned || numberOfSchedules == 1) {
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

					equivalancesMap = new HashMap<>();
					final List<EObject> uniqueElements = new LinkedList<>();
					equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, elementToRowMap);

					// Display unique rows.
					for (final EObject element : uniqueElements) {
						final Row row = elementToRowMap.get(element);
						if (row != null) {
							row.setVisible(true);
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
							referenceRow.setVisible(true);

							// Bind (non-spot market) slots based on ID - slot ID must by unique!
							{
								if (referenceRow.getLhsLink() == null && referenceRow.getName() != null && !referenceRow.getName().isEmpty()) {

									for (Row row : table.getRows()) {
										if (!row.isReference()) {
											if (Objects.equal(referenceRow.getName(), row.getName())) {

												if (referenceRow.getOpenLoadSlotAllocation() != null
														|| (referenceRow.getLoadAllocation() != null && !(referenceRow.getLoadAllocation().getSlot() instanceof SpotSlot))) {

													assert row.getLhsLink() == null;
													row.setLhsLink(referenceRow);
													referenceRow.setLhsLink(row);

													final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
													CycleGroupUtils.addToOrMergeCycleGroup(table, row, cycleGroup);

													break;
												}
											}
										}
									}
								}
								if (referenceRow.getRhsLink() == null && referenceRow.getName2() != null && !referenceRow.getName2().isEmpty()) {

									for (Row row : table.getRows()) {
										if (!row.isReference()) {
											if (Objects.equal(referenceRow.getName2(), row.getName2())) {
												if (referenceRow.getOpenDischargeSlotAllocation() != null
														|| (referenceRow.getDischargeAllocation() != null && !(referenceRow.getDischargeAllocation().getSlot() instanceof SpotSlot))) {
													assert row.getRhsLink() == null;
													row.setRhsLink(referenceRow);
													referenceRow.setRhsLink(row);
													break;
												}
											}
										}
									}

								}
							}

							// Bind all rows linked by LHS/RHS definitions.
							final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
							{
								Row row = referenceRow.getLhsLink();
								if (row != null) {
									row.setVisible(true);
									CycleGroupUtils.addToOrMergeCycleGroup(table, row, cycleGroup);

									RowGroup rowGroup = row.getRowGroup();
									if (rowGroup != null) {
										for (Row r : rowGroup.getRows()) {
											r.setVisible(true);
											CycleGroupUtils.addToOrMergeCycleGroup(table, r, cycleGroup);
										}
									}
								}
							}
							{
								Row row = referenceRow.getRhsLink();
								if (row != null) {
									row.setVisible(true);
									CycleGroupUtils.addToOrMergeCycleGroup(table, row, cycleGroup);

									RowGroup rowGroup = row.getRowGroup();
									if (rowGroup != null) {
										for (Row r : rowGroup.getRows()) {
											r.setVisible(true);
											CycleGroupUtils.addToOrMergeCycleGroup(table, r, cycleGroup);
										}
									}
								}
							}
							// Bind all rows in group
							{

							}
						}
					}

					// // Run diff processes.
					for (final IDiffProcessor diffProcessor : diffProcessors) {
						diffProcessor.runDiffProcess(table, referenceElements, uniqueElements, equivalancesMap, elementToRowMap);
					}
					renumberCycleGroups(table);
				}
				super.endCollecting();
			}

		};
	}

	public List<Row> generateRows(final Table dataModelInstance, final ScenarioResult scenarioResult, final Schedule schedule, final boolean isPinned) {

		final List<EObject> interestingEvents = new LinkedList<EObject>();
		final Set<EObject> allEvents = new HashSet<EObject>();
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (showEvent(event)) {
					interestingEvents.add(event);
				}
				allEvents.add(event);
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			interestingEvents.add(openSlotAllocation);
			allEvents.add(openSlotAllocation);
		}

		return generateRows(dataModelInstance, scenarioResult, schedule, interestingEvents, allEvents, isPinned);
	}

	public List<Row> generateRows(final Table tableModelInstance, final ScenarioResult scenarioResult, final Schedule schedule, final List<EObject> interestingElements, final Set<EObject> allElements,
			final boolean isReferenceSchedule) {
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
				if (openSlotAllocation.getSlot() instanceof LoadSlot) {
					row.setOpenLoadSlotAllocation(openSlotAllocation);
				} else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
					row.setOpenDischargeSlotAllocation(openSlotAllocation);
				} else {
					assert false;
				}
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
			row.setScenarioName(scenarioResult.getModelRecord().getName());
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
		final Set<EObject> equivalents = new LinkedHashSet<>();
		if (a instanceof CargoAllocation) {
			final CargoAllocation allocation = (CargoAllocation) a;

			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			equivalents.addAll(allocation.getEvents());
			// Cargo inputCargo = allocation.getInputCargo();
			// if (inputCargo != null) {
			// equivalents.add(inputCargo);
			// }
		} else if (a instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) a;

			final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
				equivalents.add(slotAllocation.getSlot());
				equivalents.add(slotAllocation.getSlotVisit());
			}
			equivalents.addAll(allocation.getEvents());
			// equivalents.add(allocation.getInputCargo());
		} else if (a instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
			equivalents.addAll(Lists.<EObject> newArrayList(vesselEventVisit, vesselEventVisit.getVesselEvent()));
			equivalents.addAll(vesselEventVisit.getEvents());
		} else if (a instanceof GeneratedCharterOut) {
			final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) a;
			equivalents.add(generatedCharterOut);
			equivalents.addAll(generatedCharterOut.getEvents());
		} else if (a instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) a;
			equivalents.addAll(startEvent.getEvents());
			final VesselAvailability vesselAvailability = startEvent.getSequence().getVesselAvailability();
			if (vesselAvailability != null) {
				equivalents.addAll(Lists.<EObject> newArrayList(startEvent, vesselAvailability.getVessel()));
			}
		} else if (a instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) a;
			equivalents.add(endEvent);
		} else if (a instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) a;
			equivalents.addAll(Lists.<EObject> newArrayList(openSlotAllocation, openSlotAllocation.getSlot()));
		} else {
			equivalents.addAll(Lists.<EObject> newArrayList(a));
		}
		// FIXME: Added to vessel report, but this causing any event on a sequence to be "equivalent" to any other
		if (row.getSequence() != null) {
			// row.getInputEquivalents().add(row.getSequence());
		}

		equivalents.stream()//
				.filter(e -> e != null)// Filter out nulls
				.forEach(e -> row.getInputEquivalents().add(e));
	}

	private void renumberCycleGroups(final Table table) {

		// For cargo based cycle groups, construct a particular diff message
		final Set<Pair<UserGroup, Integer>> orderedUserGroup = new TreeSet<>(new UserGroupPNLDeltaComparator());

		for (final UserGroup group : table.getUserGroups()) {
			// Skip empty groups
			if (group.getGroups().isEmpty()) {
				continue;
			}

			// Filter out single element groups
			if (group.getGroups().size() == 1) {
				table.getCycleGroups().addAll(group.getGroups());
			} else {
				final int pnlDelta = PNLDeltaUtils.getPNLDelta(group);
				orderedUserGroup.add(new Pair<>(group, pnlDelta));
			}
		}
		int userGoupCounter = 1;
		int cycleGoupCounter = 1;
		table.getUserGroups().clear();
		for (final Pair<UserGroup, Integer> p : orderedUserGroup) {
			final UserGroup userGroup = p.getFirst();
			// Zero sum user group, remove it.
			// if (p.getSecond() == 0) {
			// table.getCycleGroups().addAll(userGroup.getGroups());
			// continue;
			// }

			final Set<Pair<CycleGroup, Integer>> orderedCycleGroup = new TreeSet<>(new CycleGroupPNLComparator());
			for (final CycleGroup cycleGroup : userGroup.getGroups()) {
				// Skip empty groups
				if (cycleGroup.getRows().isEmpty()) {
					continue;
				}

				if (filterCycleGroup(cycleGroup)) {
					for (final Row row2 : cycleGroup.getRows()) {
						row2.setVisible(true);
					}
					final int pnlDelta = PNLDeltaUtils.getPNLDelta(cycleGroup);
					orderedCycleGroup.add(new Pair<>(cycleGroup, pnlDelta));
				}
			}

			userGroup.getGroups().clear();
			for (final Pair<CycleGroup, Integer> p2 : orderedCycleGroup) {
				final CycleGroup group = p2.getFirst();
				userGroup.getGroups().add(group);
				group.setIndex(cycleGoupCounter++);
			}

			if (!userGroup.getGroups().isEmpty()) {
				table.getUserGroups().add(userGroup);
				userGroup.setComment(String.format("Group %d", userGoupCounter++));
			}
		}

		// For cargo based cycle groups, construct a particular diff message
		final Set<Pair<CycleGroup, Integer>> orderedCycleGroup = new TreeSet<>(new CycleGroupPNLComparator());

		for (final CycleGroup group : table.getCycleGroups()) {
			// Skip empty groups
			if (group.getRows().isEmpty()) {
				continue;
			}

			if (filterCycleGroup(group)) {
				final int pnlDelta = PNLDeltaUtils.getPNLDelta(group);
				orderedCycleGroup.add(new Pair<>(group, pnlDelta));
			}
		}

		table.getCycleGroups().clear();
		for (final Pair<CycleGroup, Integer> p : orderedCycleGroup) {
			final CycleGroup group = p.getFirst();
			table.getCycleGroups().add(group);
			group.setIndex(cycleGoupCounter++);
		}
	}

	public boolean showEvent(final Event event) {
		if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}

	public static boolean filterCycleGroup(@NonNull final CycleGroup group) {

		if (group.getChangeType() == ChangeType.WIRING) {
			return true;
		}
		if (group.getChangeType() == ChangeType.VESSEL) {
			return true;
		}

		long delta = PNLDeltaUtils.getPNLDelta(group);
		if (delta != 0) {
			return true;
		}
		delta = PNLDeltaUtils.getLatenessDelta(group);
		if (delta != 0) {
			return true;
		}
		delta = PNLDeltaUtils.getCapacityDelta(group);
		if (delta != 0) {
			return true;
		}
		return false;
	}
}
