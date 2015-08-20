
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ChangeSetViewTransformer {

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
		final Set<EObject> fromAllElements = new LinkedHashSet<>();
		final Set<EObject> toInterestingElements = new LinkedHashSet<>();
		final Set<EObject> toAllElements = new LinkedHashSet<>();

		final Schedule fromSchedule = ((LNGScenarioModel) from.getInstance()).getPortfolioModel().getScheduleModel().getSchedule();
		final Schedule toSchedule = ((LNGScenarioModel) to.getInstance()).getPortfolioModel().getScheduleModel().getSchedule();

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

		final List<ChangeSetRow> rows = new LinkedList<>();

		// Pass one, construct current wiring.
		for (final EObject element : toInterestingElements) {

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();
					createOrUpdateRow(lhsRowMap, rhsRowMap, rows, slotVisit, loadSlot, true);
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				createOrUpdateRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation, true);
			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				rows.add(row);

				// TODO: Unique name?
				lhsRowMap.put(event.name(), row);

				row.setLhsName(event.name());
				if (event instanceof ProfitAndLossContainer) {
					row.setNewGroupProfitAndLoss((ProfitAndLossContainer) event);
				}
				if (event instanceof EventGrouping) {
					row.setNewEventGrouping((EventGrouping) event);
				}
				row.setLhsVesselName(getName(event.getSequence()));
			}
		}

		for (final EObject element : uniqueElements) {
			// Is it a cargo?
			final boolean isBaseElement = toAllElements.contains(element);

			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final Slot slot = slotVisit.getSlotAllocation().getSlot();
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					createOrUpdateRow(lhsRowMap, rhsRowMap, rows, slotVisit, loadSlot, isBaseElement);
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				createOrUpdateRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation, isBaseElement);
			} else if (element instanceof Event) {
				assert false;
			}
		}

		// Second pass, create the wiring links.

		for (final EObject element : toInterestingElements) {
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
							createOrUpdateRow(lhsRowMap, rhsRowMap, rows, slotVisit2, (LoadSlot) slotVisit2.getSlotAllocation().getSlot(), false);
						}
					}
				}
			} else if (element instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
				for (final EObject e : equivalents) {
					if (e instanceof OpenSlotAllocation) {
						final OpenSlotAllocation openSlotAllocation2 = (OpenSlotAllocation) e;
						createOrUpdateRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation2, false);
					}
				}
			} else if (element instanceof Event) {
				final Event event = (Event) element;
				final ChangeSetRow row = lhsRowMap.get(event.name());

				if (event instanceof ProfitAndLossContainer) {
					row.setOriginalGroupProfitAndLoss((ProfitAndLossContainer) event);
				}
				if (event instanceof EventGrouping) {
					row.setOriginalEventGrouping((EventGrouping) event);
				}
				row.setRhsVesselName(getName(event.getSequence()));
			}
		}

		// Update with vessel and wiring changes
		for (final ChangeSetRow row : rows) {
			if (!Objects.equal(row.getLhsVesselName(), row.getRhsVesselName())) {
				row.setVesselChange(true);
			}
			if (row.getLhsWiringLink() != null || row.getRhsWiringLink() != null) {
				row.setWiringChange(true);
			}

		}

		// Filter out zero P&L changes
		final Iterator<ChangeSetRow> itr = rows.iterator();
		while (itr.hasNext()) {
			final ChangeSetRow row = itr.next();
			if (row.isWiringChange()) {
				continue;
			}
			if (row.isVesselChange()) {
				continue;
			}
			long a = ChangeSetUtils.getGroupProfitAndLoss(row.getOriginalGroupProfitAndLoss());
			long b = ChangeSetUtils.getGroupProfitAndLoss(row.getNewGroupProfitAndLoss());
			if (a == b) {
				a = ChangeSetUtils.getCapacityViolationCount(row.getOriginalEventGrouping());
				b = ChangeSetUtils.getCapacityViolationCount(row.getNewEventGrouping());
				if (a == b) {
					a = ChangeSetUtils.getLateness(row.getOriginalEventGrouping());
					b = ChangeSetUtils.getLateness(row.getNewEventGrouping());
					if (a == b) {
						itr.remove();
						continue;
					}
				}
			}
		}

		// Sort into wiring groups.
		final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToRowGroup = new HashMap<>();
		for (ChangeSetRow row : rows) {
			updateRowGroups(row, rowToRowGroup);
		}

		convertToSortedGroups(rowToRowGroup);
		for (ChangeSetRow row : rows) {
			assert rowToRowGroup.containsKey(row);
			// updateRowGroups(row, rowToRowGroup);
		}
		Collections.sort(rows, new Comparator<ChangeSetRow>() {

			@Override
			public int compare(final ChangeSetRow o1, final ChangeSetRow o2) {
				// Sort wiring changes first
				if (o1.isWiringChange() != o2.isWiringChange()) {
					if (o1.isWiringChange()) {
						return -1;
					} else {
						return 1;
					}
				} else if (o1.isWiringChange() && o2.isWiringChange()) {
					// If wiring change, sort related blocks together

					if (rowToRowGroup.containsKey(o1) && rowToRowGroup.get(o1).contains(o2)) {
						// Related elements, sort together.

						// Are these elements related?
						// ChangeSetRow link = o1.getRhsWiringLink();
						final Collection<ChangeSetRow> group = rowToRowGroup.get(o1);
						// group.add(o1);
						// while (link != null && !group.contains(link)) {
						// group.add(link);
						// link = link.getRhsWiringLink();
						// }
						// link = o1.getLhsWiringLink();
						// while (link != null && !group.contains(link)) {
						// group.add(link);
						// link = link.getLhsWiringLink();
						// }

						// Are the rows related?
						// if (group.contains(o2)) {
						// Start from the first element in the sorted group
						ChangeSetRow link = group.iterator().next();// get(0);
						while (link != null) {
							if (link == o1) {
								return -1;
							}
							if (link == o2) {
								return 1;
							}
							link = link.getRhsWiringLink();
						}
						assert false;
					} else {
						final Collection<ChangeSetRow> group1 = rowToRowGroup.get(o1);
						final Collection<ChangeSetRow> group2 = rowToRowGroup.get(o2);

						// Sort on head element ID
						return ("" + group1.iterator().next().getLhsName()).compareTo("" + group2.iterator().next().getLhsName());
					}
				}
				// Sort vessel changes above anything else.
				if (o1.isVesselChange() != o2.isVesselChange()) {
					if (o1.isVesselChange()) {
						return -1;
					} else {
						return 1;
					}
				}

				// Compare name
				return ("" + o1.getLhsName()).compareTo("" + o2.getLhsName());
			}
		});

		// Add to data model
		if (isBase) {
			changeSet.getChangeSetRowsToBase().addAll(rows);
		} else {
			changeSet.getChangeSetRowsToPrevious().addAll(rows);
		}

		// Build metrics
		{
			final Metrics metrics = ChangesetFactory.eINSTANCE.createMetrics();

			long pnl = 0;
			long lateness = 0;
			long violations = 0;
			{
				for (final Sequence sequence : fromSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl += groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl += cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness += ChangeSetUtils.getLateness(cargoAllocation);
								violations += ChangeSetUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : fromSchedule.getOpenSlotAllocations()) {
					pnl += openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}
			{
				for (final Sequence sequence : toSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl -= groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl -= cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness -= ChangeSetUtils.getLateness(cargoAllocation);
								violations -= ChangeSetUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : toSchedule.getOpenSlotAllocations()) {
					pnl -= openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}
			metrics.setPnlDelta((int) -pnl);
			metrics.setLatenessDelta((int) -lateness);
			metrics.setCapacityDelta((int) -violations);
			if (isBase) {
				changeSet.setMetricsToBase(metrics);
			} else {
				changeSet.setMetricsToPrevious(metrics);

			}
		}
	}

	private void convertToSortedGroups(final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToGroups) {

		for (Map.Entry<ChangeSetRow, Collection<ChangeSetRow>> entry : rowToGroups.entrySet()) {
			Collection<ChangeSetRow> value = entry.getValue();
			if (value.size() < 2) {
				continue;
			}
			if (value instanceof Set<?>) {
				if (true) {
					// Convert to list and sort.
					List<ChangeSetRow> newGroup = new ArrayList<>(value.size());

					ChangeSetRow firstAlphabetically = null;
					ChangeSetRow head = null;
					for (ChangeSetRow r : value) {
						if (r.getLhsWiringLink() == null) {
							head = r;
							break;
						}
						if (firstAlphabetically == null && r.getLhsName() != null) {
							firstAlphabetically = r;
						} else if (firstAlphabetically != null && r.getLhsName() != null) {
							if (firstAlphabetically.getLhsName().compareTo(r.getLhsName()) > 0) {
								firstAlphabetically = r;
							}
						}
					}
					if (head == null) {
						head = firstAlphabetically;
					}
					while (head != null && !newGroup.contains(head)) {
						newGroup.add(head);
						head = head.getRhsWiringLink();
					}

					entry.setValue(newGroup);
				} else {
					List<ChangeSetRow> newGroup = new LinkedList<>(value);

					// Sort the group with head, tail elements fixed then alphabetically
					// This creates a stable sort even with cyclic wiring groups
					Collections.sort(newGroup, new Comparator<ChangeSetRow>() {

						@Override
						public int compare(final ChangeSetRow o1, final ChangeSetRow o2) {

							if (o1.getLhsWiringLink() == null) {
								return -1;
							}
							if (o2.getLhsWiringLink() == null) {
								return 1;
							}
							if (o1.getRhsWiringLink() == null) {
								return 1;
							}
							if (o2.getRhsWiringLink() == null) {
								return -1;
							}

							if (o1.getLhsWiringLink() == o2) {
								return -1;
							}

							if (o1.getRhsWiringLink() == o2) {
								return 1;
							}

							String n1 = o1.getLhsName();
							String n2 = o2.getLhsName();

							if (n1 != null && n2 == null) {
								return -1;
							}
							if (n1 == null && n2 != null) {
								return 1;
							}
							if (n1 != null && n2 != null) {
								return n1.compareTo(n2);
							}

							n1 = o1.getRhsName();
							n2 = o2.getRhsName();
							if (n1 != null && n2 == null) {
								return -1;
							}
							if (n1 == null && n2 != null) {
								return 1;
							}
							if (n1 != null && n2 != null) {
								return n1.compareTo(n2);
							}

							return 0;
						}
					});
					entry.setValue(newGroup);
				}
			}
		}

	}

	private void updateRowGroups(final ChangeSetRow row, final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToGroups) {
		if (rowToGroups.containsKey(row)) {
			return;
		}
		// Try and find an existing group.
		Collection<ChangeSetRow> group = null;
		if (row.getLhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getLhsWiringLink())) {
				group = rowToGroups.get(row.getLhsWiringLink());
			}
		} else if (row.getRhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getRhsWiringLink())) {
				group = rowToGroups.get(row.getRhsWiringLink());
			}
		}
		// Create new group if required.
		if (group == null) {
			group = new LinkedHashSet<>();
		}
		rowToGroups.put(row, group);
		// Add to group
		group.add(row);
		// Merge existing groups if required.
		if (row.getLhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getLhsWiringLink())) {
				final Collection<ChangeSetRow> otherGroup = rowToGroups.get(row.getLhsWiringLink());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetRow r : otherGroup) {
						rowToGroups.put(r, group);
					}
				}
			}
		}
		if (row.getRhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getRhsWiringLink())) {
				final Collection<ChangeSetRow> otherGroup = rowToGroups.get(row.getRhsWiringLink());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetRow r : otherGroup) {
						rowToGroups.put(r, group);
					}
				}
			}
		}
		assert rowToGroups.containsKey(row);
	}

	protected void createOrUpdateRow(final Map<String, ChangeSetRow> lhsRowMap, final Map<String, ChangeSetRow> rhsRowMap, final List<ChangeSetRow> rows, final OpenSlotAllocation openSlotAllocation,
			final boolean isBase) {
		final Slot slot = openSlotAllocation.getSlot();

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			final ChangeSetRow row;
			{
				final String rowKey = getKeyName(loadSlot);
				if (lhsRowMap.containsKey(rowKey)) {
					row = lhsRowMap.get(rowKey);
				} else {
					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(row);
					row.setLhsName(getRowName(loadSlot));
					row.setLoadSlot(loadSlot);
					lhsRowMap.put(getKeyName(loadSlot), row);
				}
			}
			if (isBase) {
				row.setNewGroupProfitAndLoss(openSlotAllocation);
			} else {
				row.setOriginalGroupProfitAndLoss(openSlotAllocation);
				if (openSlotAllocation.getSlot() != null) {
					final ChangeSetRow otherRow = lhsRowMap.get(getKeyName(openSlotAllocation.getSlot()));
					if (otherRow != null) {
						if (row != otherRow) {
							row.setLhsWiringLink(otherRow);
						}
					}
				}

			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			final ChangeSetRow row;
			{
				final String rowKey = getKeyName(dischargeSlot);
				if (rhsRowMap.containsKey(rowKey)) {
					row = rhsRowMap.get(rowKey);
				} else {
					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(row);
					row.setRhsName(getRowName(dischargeSlot));
					row.setDischargeSlot(dischargeSlot);
					rhsRowMap.put(getKeyName(dischargeSlot), row);
				}
			}
			if (isBase) {
				row.setNewGroupProfitAndLoss(openSlotAllocation);
			} else {
				row.setOriginalGroupProfitAndLoss(openSlotAllocation);
				if (openSlotAllocation.getSlot() != null) {
					final ChangeSetRow otherRow = rhsRowMap.get(getKeyName(openSlotAllocation.getSlot()));
					if (otherRow != null) {
						if (row != otherRow) {
							row.setRhsWiringLink(otherRow);
						}
					}
				}

			}
		} else {
			//
			assert false;
		}
	}

	protected void createOrUpdateRow(final Map<String, ChangeSetRow> lhsRowMap, final Map<String, ChangeSetRow> rhsRowMap, final List<ChangeSetRow> rows, final SlotVisit slotVisit,
			final LoadSlot loadSlot, final boolean isBase) {
		final ChangeSetRow row;
		{
			final String rowKey = getKeyName(loadSlot);
			if (lhsRowMap.containsKey(rowKey)) {
				row = lhsRowMap.get(rowKey);
			} else {
				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				rows.add(row);
				row.setLhsName(getRowName(loadSlot));
				row.setLoadSlot(loadSlot);
				lhsRowMap.put(getKeyName(loadSlot), row);
			}
		}

		final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
		if (cargoAllocation.getSlotAllocations().size() != 2) {
			throw new RuntimeException("Complex cargoes are not supported");
		}
		if (isBase) {
			row.setNewGroupProfitAndLoss(cargoAllocation);
			row.setNewEventGrouping(cargoAllocation);
			row.setLhsVesselName(getName(slotVisit.getSequence()));
			row.setNewLoadAllocation(slotVisit.getSlotAllocation());
		} else {
			row.setOriginalGroupProfitAndLoss(cargoAllocation);
			row.setOriginalEventGrouping(cargoAllocation);
			row.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
			row.setRhsVesselName(getName(slotVisit.getSequence()));
		}
		// Get discharge data
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation == slotVisit.getSlotAllocation()) {
				continue;
			}

			if (isBase) {
				row.setRhsName(getRowName(slotAllocation.getSlot()));
				row.setNewDischargeAllocation(slotAllocation);
				row.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());
				rhsRowMap.put(getKeyName(slotAllocation.getSlot()), row);
			} else {
				row.setOriginalDischargeAllocation(slotAllocation);
			}
			if (!isBase) {
				if (slotAllocation.getSlot() != null) {
					ChangeSetRow otherRow = rhsRowMap.get(getKeyName(slotAllocation.getSlot()));
					if (otherRow == null) {
						// Special case, a spot slot will have no "OpenSlotAllocation" to pair up to, so create a new row here.
						assert slotAllocation.getSlot() instanceof SpotSlot;
						otherRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
						rows.add(otherRow);
						otherRow.setRhsName(getRowName(slotAllocation.getSlot()));
						otherRow.setOriginalDischargeAllocation(slotAllocation);
						otherRow.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());
						// otherRow.setWiringChange(true);
						rhsRowMap.put(getKeyName(slotAllocation.getSlot()), row);
					}
					if (row != otherRow) {
						row.setRhsWiringLink(otherRow);
					}
				}
			}
		}

	}

	private String getName(final Sequence sequence) {
		if (sequence.isSetCharterInMarket()) {
			return getName(sequence.getCharterInMarket());
		} else {
			return getName(sequence.getVesselAvailability());
		}
	}

	private String getName(final VesselAssignmentType t) {
		if (t instanceof VesselAvailability) {
			return ((VesselAvailability) t).getVessel().getName();
		} else if (t instanceof CharterInMarket) {
			return ((CharterInMarket) t).getName();
		}
		return null;
	}

	private String getRowName(final Slot slot) {
		if (true) {
			// return getKeyName(slot);
		}
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			if (slot instanceof SpotSlot) {
				final SpotMarket market = ((SpotSlot) slot).getMarket();
				return String.format("%s-%s", market.getName(), format(slot.getWindowStart()));
			}
		}
		return slot.getName();
	}

	private String getKeyName(final Slot slot) {
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			return EquivalanceGroupBuilder.getElementKey(slot);
		}
		return slot.getName();
	}

	private static String format(final LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthOfYear());

	}
}