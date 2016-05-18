/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ActionSetTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioInstance> stages = new LinkedList<>();

		// Try forks
		{
			final Container c = (ScenarioInstance) instance;
			boolean foundBase = false;
			int i = 1;
			while (true) {
				boolean found = false;
				for (final Container cc : c.getElements()) {
					if (!foundBase && (cc.getName().equals("base") || cc.getName().equalsIgnoreCase(String.format("ActionSet-base")))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(0, (ScenarioInstance) cc);
							foundBase = true;
						}
					}
					if (cc.getName().equals(Integer.toString(i)) || cc.getName().equalsIgnoreCase(String.format("ActionSet-%d", i))) {
						if (cc instanceof ScenarioInstance) {
							stages.add((ScenarioInstance) cc);
							found = true;
							i++;
						}
					}
				}
				if (!found) {
					break;
				}
			}
		}
		if (stages.isEmpty()) {
			// Try parent folder
			final Container c = ((ScenarioInstance) instance).getParent();
			int i = 1;
			boolean foundBase = false;
			while (true) {
				boolean found = false;
				for (final Container cc : c.getElements()) {
					if (!foundBase && (cc.getName().equals("base") || cc.getName().equalsIgnoreCase(String.format("ActionSet-base")))) {
						if (cc instanceof ScenarioInstance) {
							stages.add(0, (ScenarioInstance) cc);
							foundBase = true;
						}
					}

					if (cc.getName().equals(Integer.toString(i)) || cc.getName().equalsIgnoreCase(String.format("ActionSet-%d", i))) {
						if (cc instanceof ScenarioInstance) {
							stages.add((ScenarioInstance) cc);
							found = true;
							i++;
						}
					}
				}
				if (!found) {
					break;
				}
			}
		}

		try {
			monitor.beginTask("Opening action sets", stages.size());
			ScenarioInstance prev = null;
			for (final ScenarioInstance current : stages) {
				if (prev != null) {
					root.getChangeSets().add(buildChangeSet(stages.get(0), prev, current));
				}
				prev = current;
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

	private ChangeSet buildChangeSet(final ScenarioInstance base, final ScenarioInstance prev, final ScenarioInstance current) {
		final ModelReference baseReference = base.getReference();
		final ModelReference prevReference = prev.getReference();
		final ModelReference currentReference = current.getReference();

		// Pre-Load
		baseReference.getInstance();
		prevReference.getInstance();
		currentReference.getInstance();

		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setBaseScenario(base);
		changeSet.setBaseScenarioRef(baseReference);
		changeSet.setPrevScenario(prev);
		changeSet.setPrevScenarioRef(prevReference);
		changeSet.setCurrentScenario(current);
		changeSet.setCurrentScenarioRef(currentReference);

		generateDifferences(base, current, changeSet, true);
		generateDifferences(prev, current, changeSet, false);

		return changeSet;
	}

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

	private void generateDifferences(final ScenarioInstance from, final ScenarioInstance to, final ChangeSet changeSet, final boolean isBase) {
		final EquivalanceGroupBuilder equivalanceGroupBuilder = new EquivalanceGroupBuilder();

		final Set<EObject> fromInterestingElements = new LinkedHashSet<>();
		final Set<EObject> toInterestingElements = new LinkedHashSet<>();
		final Set<EObject> fromAllElements = new LinkedHashSet<>();
		final Set<EObject> toAllElements = new LinkedHashSet<>();

		final Schedule fromSchedule = ((LNGScenarioModel) from.getInstance()).getScheduleModel().getSchedule();
		final Schedule toSchedule = ((LNGScenarioModel) to.getInstance()).getScheduleModel().getSchedule();

		extractElements(fromSchedule, fromInterestingElements, fromAllElements);
		extractElements(toSchedule, toInterestingElements, toAllElements);

		// Generate the element by key map
		final List<Map<String, List<EObject>>> perScenarioElementsByKeyMap = new LinkedList<>();
		// Make the "to" scenario the reference scenario
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(toInterestingElements));
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(fromInterestingElements));

		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
		final List<EObject> uniqueElements = new LinkedList<>();

		equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, null);

		final Map<String, ChangeSetRow> lhsRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> rhsRowMap = new HashMap<>();

		final Map<String, List<ChangeSetRow>> lhsRowMarketMap = new HashMap<>();
		final Map<String, List<ChangeSetRow>> rhsRowMarketMap = new HashMap<>();

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Pass one, construct current wiring.
		for (final EObject element : toInterestingElements) {
			assert element != null;
			ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, true, false);
		}

		for (final EObject element : uniqueElements) {
			assert element != null;
			final boolean isBaseElement = toAllElements.contains(element);
			if (!isBaseElement) {
				ChangeSetTransformerUtil.createOrUpdateRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, element, isBaseElement, false);
			}
		}

		// Second pass, create the wiring links.
		final List<EObject> deferredElements = processElementsForWiringPass(toInterestingElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, true);
		// Process deferred elements - i.e. those with multiple spot market options
		processElementsForWiringPass(deferredElements, equivalancesMap, lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, false);

		ChangeSetTransformerUtil.mergeSpots(rows);
		ChangeSetTransformerUtil.setRowFlags(rows);
		ChangeSetTransformerUtil.filterRows(rows);
		ChangeSetTransformerUtil.sortRows(rows);

		// Add to data model
		if (isBase) {
			changeSet.getChangeSetRowsToBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToPrevious().addAll(rows);
		}

		// Build metrics
		ChangeSetTransformerUtil.calculateMetrics(changeSet, fromSchedule, toSchedule, isBase);
	}

	private List<EObject> processElementsForWiringPass(final Collection<EObject> elements, final Map<EObject, Set<EObject>> equivalancesMap, final Map<String, ChangeSetRow> lhsRowMap,
			final Map<String, ChangeSetRow> rhsRowMap, final Map<String, List<ChangeSetRow>> lhsRowMarketMap, final Map<String, List<ChangeSetRow>> rhsRowMarketMap, final List<ChangeSetRow> rows,
			final boolean canDefer) {
		final List<EObject> deferredElements = new LinkedList<>();

		for (final EObject element : elements) {
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
							if (ChangeSetTransformerUtil.createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit2,
									(LoadSlot) slotVisit2.getSlotAllocation().getSlot(), false, canDefer)) {
								deferredElements.add(element);
							}
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
				for (final EObject e : equivalents) {
					if (e instanceof Event) {
						final Event event2 = (Event) e;
						ChangeSetTransformerUtil.createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event2, false);
					}
				}
			}
		}
		return deferredElements;

	}

}