
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
import org.joda.time.LocalDate;

import com.mmxlabs.lingo.reports.utils.CargoAllocationUtils;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.EventVesselChange;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.changeset.model.VesselChange;
import com.mmxlabs.lingo.reports.views.changeset.model.WiringChange;
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
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(fromInterestingElements));
		perScenarioElementsByKeyMap.add(equivalanceGroupBuilder.generateElementNameGroups(toInterestingElements));

		final Map<EObject, Set<EObject>> equivalancesMap = new HashMap<>();
		final List<EObject> uniqueElements = new LinkedList<>();

		equivalanceGroupBuilder.populateEquivalenceGroups(perScenarioElementsByKeyMap, equivalancesMap, uniqueElements, null);

		final List<WiringChange> wiringChanges = new LinkedList<>();
		final List<VesselChange> vesselChanges = new LinkedList<>();
		final List<WiringChange> otherChanges = new LinkedList<>();
		final List<EventVesselChange> eventChanges = new LinkedList<>();
		// Now generate the diff structures.
		for (final EObject element : fromInterestingElements) {
			final Set<EObject> equivalents = equivalancesMap.get(element);
			if (equivalents == null) {
				continue;
			}
			// Is it a cargo?
			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {

					String oldVesselName = null;
					String newVesselName = null;

					final WiringChange wiringChange = ChangesetFactory.eINSTANCE.createWiringChange();
					final VesselChange vc = ChangesetFactory.eINSTANCE.createVesselChange();

					wiringChange.setOriginalLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					vc.setOriginalLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					wiringChange.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
					vc.setOriginalLoadAllocation(slotVisit.getSlotAllocation());

					wiringChange.setOriginalGroupProfitAndLoss(slotVisit.getSlotAllocation().getCargoAllocation());
					vc.setOriginalGroupProfitAndLoss(slotVisit.getSlotAllocation().getCargoAllocation());
					wiringChange.setOriginalEventGrouping(slotVisit.getSlotAllocation().getCargoAllocation());
					vc.setOriginalEventGrouping(slotVisit.getSlotAllocation().getCargoAllocation());

					// Get original discharge
					{
						final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
						final Sequence sequence = cargoAllocation.getSequence();
						oldVesselName = sequence.getName();

						if (sequence.isSetCharterInMarket()) {
							vc.setOriginalVessel(sequence.getCharterInMarket());
						} else {
							vc.setOriginalVessel(sequence.getVesselAvailability());
						}
						if (cargoAllocation.getSlotAllocations().size() != 2) {
							throw new RuntimeException("Complex cargoes are not supported");
						}

						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation == slotVisit.getSlotAllocation()) {
								continue;
							}
							// Otherwise it is the original discharge
							// TODO: Maybe store the slot allocation? Problematic with the open slot allocation....
							wiringChange.setOriginalDischargeSlot((DischargeSlot) slotAllocation.getSlot());
							vc.setOriginalDischargeSlot((DischargeSlot) slotAllocation.getSlot());

							wiringChange.setOriginalDischargeAllocation(slotAllocation);
							vc.setOriginalDischargeAllocation(slotAllocation);
						}
					}
					for (final EObject e : equivalents) {
						if (e instanceof SlotVisit) {
							final SlotVisit slotVisit2 = (SlotVisit) e;

							wiringChange.setNewLoadSlot((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
							vc.setNewLoadSlot((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
							wiringChange.setNewLoadAllocation(slotVisit2.getSlotAllocation());
							vc.setNewLoadAllocation(slotVisit2.getSlotAllocation());

							final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
							final Sequence sequence = cargoAllocation.getSequence();
							newVesselName = sequence.getName();
							vc.setNewVessel(sequence.getVesselAvailability());
							if (sequence.isSetCharterInMarket()) {
								vc.setNewVessel(sequence.getCharterInMarket());
							} else {
								vc.setNewVessel(sequence.getVesselAvailability());
							}
							wiringChange.setNewGroupProfitAndLoss(slotVisit2.getSlotAllocation().getCargoAllocation());
							vc.setNewGroupProfitAndLoss(slotVisit2.getSlotAllocation().getCargoAllocation());
							wiringChange.setNewEventGrouping(slotVisit2.getSlotAllocation().getCargoAllocation());
							vc.setNewEventGrouping(slotVisit2.getSlotAllocation().getCargoAllocation());

							if (cargoAllocation.getSlotAllocations().size() != 2) {
								throw new RuntimeException("Complex cargoes are not supported");
							}
							for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
								if (slotAllocation == slotVisit2.getSlotAllocation()) {
									continue;
								}
								// Otherwise it is the original discharge
								// TODO: Maybe store the slot allocation? Problematic with the open slot allocation....
								wiringChange.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								vc.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								wiringChange.setNewDischargeAllocation(slotAllocation);
								vc.setNewDischargeAllocation(slotAllocation);
							}
						} else if (e instanceof OpenSlotAllocation) {
							final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) e;
							wiringChange.setNewDischargeSlot((DischargeSlot) openSlotAllocation.getSlot());
							wiringChange.setNewGroupProfitAndLoss(openSlotAllocation);
						} else {
							assert false;
						}
					}

					if (wiringChange.getOriginalDischargeSlot() != null && wiringChange.getNewDischargeSlot() != null) {
						// TODO: Use this?
						final String referenceStr = CargoAllocationUtils.getSalesWiringAsString(wiringChange.getOriginalLoadAllocation().getCargoAllocation());
						final String otherStr = CargoAllocationUtils.getSalesWiringAsString(wiringChange.getNewLoadAllocation().getCargoAllocation());
						if (!referenceStr.equals(otherStr)) {
							// if (!equivalanceGroupBuilder.getElementKey(wiringChange.getOriginalDischargeSlot()).equals(equivalanceGroupBuilder.getElementKey(wiringChange.getNewDischargeSlot()))) {
							wiringChanges.add(wiringChange);
							// Wiring change
							// Record change
						} else {
							// wiringChanges.add(wiringChange);
							// no change in wiring, discard the wiring change (but use for vessel change?)
						}
						// TODO: Check vessel change
					} else {
						wiringChanges.add(wiringChange);
					}
					if (oldVesselName != null && newVesselName != null) {
						vesselChanges.add(vc);
					}

					if (!wiringChanges.contains(wiringChange)) {

						final WiringChange change = wiringChange;

						Number f = null;
						{
							final ProfitAndLossContainer originalPNL = change.getOriginalGroupProfitAndLoss();
							if (originalPNL != null) {
								f = ChangeSetUtils.getGroupProfitAndLoss(originalPNL);
							}
						}
						Number t = null;
						{
							final ProfitAndLossContainer newPNL = change.getNewGroupProfitAndLoss();
							if (newPNL != null) {
								t = ChangeSetUtils.getGroupProfitAndLoss(newPNL);
							}
						}
						double delta = 0;
						if (f != null) {
							delta -= f.intValue();
						}
						if (t != null) {
							delta += t.intValue();
						}
						delta = delta / 1000000.0;
						if (delta != 0) {
							otherChanges.add(wiringChange);
						}
					}

					// Is is a wiring difference?
					// final String referenceStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
					// final String otherStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
					//
					// -- use existing code to check wiring string.
					// // Make sure we include "unique" elements

					// Is it a vessel difference?

					// Else is another difference?
				}
			} else if (element instanceof OpenSlotAllocation) {
				for (final Object e : equivalents) {
					if (e instanceof OpenSlotAllocation) {
						// No change?
					} else if (e instanceof SlotVisit) {
						// Create wiring change stuff as above
					} else {
						assert false;
					}
				}
			} else {
				if (element instanceof Event) {
					Event event = (Event) element;
					EventVesselChange vc = ChangesetFactory.eINSTANCE.createEventVesselChange();
					vc.setEventName(event.name());
					vc.setOriginalEvent(event);
					if (event instanceof ProfitAndLossContainer) {
						vc.setOriginalGroupProfitAndLoss((ProfitAndLossContainer) event);

					}
					if (event instanceof EventGrouping) {
						vc.setOriginalEventGrouping((EventGrouping) event);
					}
					{
						Sequence sequence = event.getSequence();
						if (sequence.isSetCharterInMarket()) {
							vc.setOriginalVessel(sequence.getCharterInMarket());
						} else {
							vc.setOriginalVessel(sequence.getVesselAvailability());
						}
					}

					for (final EObject e : equivalents) {
						if (e instanceof Event) {
							final Event equivalentEvent = (Event) e;
							vc.setNewEvent(equivalentEvent);
							Sequence sequence = equivalentEvent.getSequence();
							if (sequence.isSetCharterInMarket()) {
								vc.setNewVessel(sequence.getCharterInMarket());
							} else {
								vc.setNewVessel(sequence.getVesselAvailability());
							}
							if (equivalentEvent instanceof ProfitAndLossContainer) {
								vc.setNewGroupProfitAndLoss((ProfitAndLossContainer) equivalentEvent);
							}
							if (equivalentEvent instanceof EventGrouping) {
								vc.setNewEventGrouping((EventGrouping) equivalentEvent);
							}
						}
					}
					eventChanges.add(vc);
				}
				// it an event difference

				// Is it a vessel difference?

				// Else is another diffe

			}
		}
		for (

		final EObject element : uniqueElements)

		{
			// Is it a cargo?
			final boolean isBaseElement = fromAllElements.contains(element);
			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {

					String oldVesselName = null;
					final String newVesselName = null;

					final WiringChange wiringChange = ChangesetFactory.eINSTANCE.createWiringChange();
					final VesselChange vc = ChangesetFactory.eINSTANCE.createVesselChange();

					// wiringChanges.add(wiringChange);
					if (isBaseElement) {
						wiringChange.setOriginalLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						vc.setOriginalLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						wiringChange.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
						vc.setOriginalLoadAllocation(slotVisit.getSlotAllocation());

					} else {
						wiringChange.setNewLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						vc.setNewLoadSlot((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						wiringChange.setNewLoadAllocation(slotVisit.getSlotAllocation());
						vc.setNewLoadAllocation(slotVisit.getSlotAllocation());
					}
					// Get original discharge
					{
						final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
						final Sequence sequence = cargoAllocation.getSequence();
						oldVesselName = sequence.getName();
						if (isBaseElement) {
							if (sequence.isSetCharterInMarket()) {
								vc.setOriginalVessel(sequence.getCharterInMarket());
							} else {
								vc.setOriginalVessel(sequence.getVesselAvailability());
							}
							if (sequence.isSetCharterInMarket()) {
								vc.setNewVessel(sequence.getCharterInMarket());
							} else {
								vc.setNewVessel(sequence.getVesselAvailability());
							}
						}
						if (cargoAllocation.getSlotAllocations().size() != 2) {
							throw new RuntimeException("Complex cargoes are not supported");
						}
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation == slotVisit.getSlotAllocation()) {
								continue;
							}
							// Otherwise it is the original discharge
							// TODO: Maybe store the slot allocation? Problematic with the open slot allocation....
							if (isBaseElement) {
								wiringChange.setOriginalDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								vc.setOriginalDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								wiringChange.setOriginalDischargeAllocation(slotAllocation);
								vc.setOriginalDischargeAllocation(slotAllocation);

							} else {
								wiringChange.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								vc.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								wiringChange.setNewDischargeAllocation(slotAllocation);
								vc.setNewDischargeAllocation(slotAllocation);
								final Set<EObject> localEquivs = equivalancesMap.get(slotAllocation.getSlotVisit());// .getSlotAllocation().getSlot());

							}
						}
					}
					wiringChanges.add(wiringChange);
					vesselChanges.add(vc);
				}
			} else if (element instanceof OpenSlotAllocation) {
				final int ii = 0;
				// for (final Object e : equivalents) {
				// if (e instanceof OpenSlotAllocation) {
				// // No change?
				// } else if (e instanceof SlotVisit) {
				// // Create wiring change stuff as above
				// } else {
				// assert false;
				// }
				// }
			} else {

				// it an event difference

				// Is it a vessel difference?

				// Else is another diffe

			}
		}
		// Stage 2, generate the row data structures from the wiring and vessel changes

		// Do this later#? Part of viewer comparator?
		// SOrt wiring change by load ID, then by discharge ID. (or sort all by date?)
		// APpend vessel changes if required.

		final Map<String, ChangeSetRow> rhsToRowMap = new HashMap<>();
		final Map<String, ChangeSetRow> lhsToRowMap = new HashMap<>();

		final Collection<ChangeSetRow> rows = new LinkedHashSet<>();

		// First pass of wiring changes, create all the required rows.
		for (final WiringChange wc : wiringChanges)

		{
			LoadSlot loadSlot = wc.getOriginalLoadSlot();
			String lhsName = getRowName(wc.getOriginalLoadSlot());
			if (lhsName == null) {
				loadSlot = wc.getNewLoadSlot();
				lhsName = getRowName(wc.getNewLoadSlot());
			}
			final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
			rows.add(row);
			row.setWiringChange(true);

			if (lhsName != null) {
				final String lhsKeyName = getKeyName(loadSlot);
				lhsToRowMap.put(lhsKeyName, row);
				row.setLhsName(lhsName);
				lhsToRowMap.put(lhsKeyName, row);
				row.setLoadSlot(loadSlot);
				row.setOriginalLoadAllocation(wc.getOriginalLoadAllocation());
				row.setNewLoadAllocation(wc.getNewLoadAllocation());

				row.setOriginalDischargeAllocation(wc.getOriginalDischargeAllocation());
				row.setNewDischargeAllocation(wc.getNewDischargeAllocation());

				row.setOriginalGroupProfitAndLoss(wc.getOriginalGroupProfitAndLoss());
				row.setNewGroupProfitAndLoss(wc.getNewGroupProfitAndLoss());
				row.setOriginalEventGrouping(wc.getOriginalEventGrouping());
				row.setNewEventGrouping(wc.getNewEventGrouping());
			}

			final String rhsName = getRowName(wc.getNewDischargeSlot());
			if (rhsName != null) {
				row.setDischargeSlot(wc.getNewDischargeSlot());
				row.setRhsName(rhsName);
				final String rhsKeyName = getKeyName(wc.getNewDischargeSlot());
				rhsToRowMap.put(rhsKeyName, row);
			}
		}

		// Second pass, generate the wiring links
		for (

		final WiringChange wc : wiringChanges)

		{
			ChangeSetRow lhsRow = null;

			final LoadSlot loadSlot = wc.getOriginalLoadSlot();
			final String lhsRowKey = getKeyName(loadSlot);

			if (lhsToRowMap.containsKey(lhsRowKey)) {
				lhsRow = lhsToRowMap.get(lhsRowKey);
			}
			ChangeSetRow otherRhsRow = null;
			final String rhsNewName = getRowName(wc.getOriginalDischargeSlot());
			final String rhsKeyName = getKeyName(wc.getOriginalDischargeSlot());
			if (rhsToRowMap.containsKey(rhsKeyName)) {
				otherRhsRow = rhsToRowMap.get(rhsKeyName);
			} else {
				if (wc.getOriginalDischargeSlot() != null) {
					otherRhsRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(otherRhsRow);
					otherRhsRow.setWiringChange(true);

					otherRhsRow.setRhsName(rhsNewName);
					otherRhsRow.setDischargeSlot(wc.getOriginalDischargeSlot());

					rhsToRowMap.put(rhsKeyName, otherRhsRow);

					otherRhsRow.setOriginalGroupProfitAndLoss(wc.getOriginalGroupProfitAndLoss());
					otherRhsRow.setNewGroupProfitAndLoss(wc.getNewGroupProfitAndLoss());
					otherRhsRow.setOriginalEventGrouping(wc.getOriginalEventGrouping());
					otherRhsRow.setNewEventGrouping(wc.getNewEventGrouping());
				}
			}
			if (lhsRow == null && otherRhsRow != null) {
				final String lhsName2 = getRowName(wc.getNewLoadSlot());
				otherRhsRow.setLhsName(lhsName2);
				otherRhsRow.setLoadSlot(wc.getNewLoadSlot());
			}

			// Bind the rows for wirings
			if (lhsRow != null && otherRhsRow != null && lhsRow != otherRhsRow) {
				lhsRow.setRhsWiringLink(otherRhsRow);
			}
		}
		if (false) {
			for (final WiringChange vc : otherChanges) {
				ChangeSetRow row = null;
				final String lhsName = getRowName(vc.getOriginalLoadSlot());
				final String rhsName = getRowName(vc.getOriginalDischargeSlot());

				if (lhsToRowMap.containsKey(getKeyName(vc.getOriginalLoadSlot()))) {
					row = lhsToRowMap.get(getKeyName(vc.getOriginalLoadSlot()));
				} else {
					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					row.setLhsName(lhsName);
					row.setLoadSlot(vc.getOriginalLoadSlot());
					row.setOriginalLoadAllocation(vc.getOriginalLoadAllocation());
					row.setNewLoadAllocation(vc.getNewLoadAllocation());
					row.setOriginalDischargeAllocation(vc.getOriginalDischargeAllocation());
					row.setNewDischargeAllocation(vc.getNewDischargeAllocation());
					row.setRhsName(rhsName);
					// row.setLhsVesselChange(vc);
					// lhsRow.setLhsVesselChange(vc);
					lhsToRowMap.put(getKeyName(vc.getOriginalLoadSlot()), row);

					row.setOriginalGroupProfitAndLoss(vc.getOriginalGroupProfitAndLoss());
					row.setNewGroupProfitAndLoss(vc.getNewGroupProfitAndLoss());
					row.setOriginalEventGrouping(vc.getOriginalEventGrouping());
					row.setNewEventGrouping(vc.getNewEventGrouping());

					// if (vc.getOriginalVessel() != null && !getName(vc.getOriginalVessel()).equals(getName(vc.getNewVessel()))) {
					rows.add(row);
					// row.setVesselChange(true);
					// }

				}

				// row.setLhsVesselName(getName(vc.getOriginalVessel()));
				// row.setRhsVesselName(getName(vc.getNewVessel()));
			}
		}
		for (

		final VesselChange vc : vesselChanges)

		{
			ChangeSetRow row = null;
			final String lhsName = getRowName(vc.getOriginalLoadSlot());
			final String rhsName = getRowName(vc.getOriginalDischargeSlot());

			if (lhsToRowMap.containsKey(getKeyName(vc.getOriginalLoadSlot()))) {
				row = lhsToRowMap.get(getKeyName(vc.getOriginalLoadSlot()));
			} else {
				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				row.setLhsName(lhsName);
				row.setLoadSlot(vc.getOriginalLoadSlot());
				row.setOriginalLoadAllocation(vc.getOriginalLoadAllocation());
				row.setNewLoadAllocation(vc.getNewLoadAllocation());
				row.setOriginalDischargeAllocation(vc.getOriginalDischargeAllocation());
				row.setNewDischargeAllocation(vc.getNewDischargeAllocation());
				row.setRhsName(rhsName);

				row.setOriginalGroupProfitAndLoss(vc.getOriginalGroupProfitAndLoss());
				row.setNewGroupProfitAndLoss(vc.getNewGroupProfitAndLoss());
				row.setOriginalEventGrouping(vc.getOriginalEventGrouping());
				row.setNewEventGrouping(vc.getNewEventGrouping());

				// row.setLhsVesselChange(vc);
				// lhsRow.setLhsVesselChange(vc);
				lhsToRowMap.put(getKeyName(vc.getOriginalLoadSlot()), row);

				if (vc.getOriginalVessel() != null && !getName(vc.getOriginalVessel()).equals(getName(vc.getNewVessel()))) {
					rows.add(row);
					row.setVesselChange(true);
				}

			}

			row.setLhsVesselName(getName(vc.getOriginalVessel()));
			row.setRhsVesselName(getName(vc.getNewVessel()));
		}
		if (false) {
			for (final EventVesselChange vc : eventChanges) {
				ChangeSetRow row = null;
				final String lhsName = vc.getEventName();// (vc.getOriginalLoadSlot());
				// final String rhsName = getRowName(vc.getOriginalDischargeSlot());

				// if (lhsToRowMap.containsKey(getKeyName(vc.getOriginalLoadSlot()))) {
				// row = lhsToRowMap.get(getKeyName(vc.getOriginalLoadSlot()));
				// } else {
				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				row.setLhsName(lhsName);
				// row.setLoadSlot(vc.getOriginalLoadSlot());
				// row.setOriginalLoadAllocation(vc.getOriginalLoadAllocation());
				// row.setNewLoadAllocation(vc.getNewLoadAllocation());
				// row.setOriginalDischargeAllocation(vc.getOriginalDischargeAllocation());
				// row.setNewDischargeAllocation(vc.getNewDischargeAllocation());
				// row.setRhsName(rhsName);

				row.setOriginalGroupProfitAndLoss(vc.getOriginalGroupProfitAndLoss());
				row.setNewGroupProfitAndLoss(vc.getNewGroupProfitAndLoss());
				row.setOriginalEventGrouping(vc.getOriginalEventGrouping());
				row.setNewEventGrouping(vc.getNewEventGrouping());

				// row.setLhsVesselChange(vc);
				// lhsRow.setLhsVesselChange(vc);
				// lhsToRowMap.put(getKeyName(vc.getOriginalLoadSlot()), row);

				// if (vc.getOriginalVessel() != null && !getName(vc.getOriginalVessel()).equals(getName(vc.getNewVessel()))) {
				rows.add(row);
				// row.setVesselChange(true);
				// }

				// }

				row.setLhsVesselName(getName(vc.getOriginalVessel()));
				row.setRhsVesselName(getName(vc.getNewVessel()));
			}
		}
		// TODO
		// for (OtherChabnge : otherChabnges) {
		//
		// }

		final List<ChangeSetRow> sortedRows = new LinkedList<>();

		if (!rows.isEmpty())

		{
			ChangeSetRow lastRow = null;
			int idx = 0;
			while (!rows.isEmpty()) {
				if (lastRow == null) {
					lastRow = rows.iterator().next();

					// Some code to find the end of the line
					ChangeSetRow t = lastRow;
					while (t != null && t != lastRow) {
						if (t.getLhsWiringLink() == null) {
							break;
						}
						t = t.getLhsWiringLink();
					}
					lastRow = t;
					idx = sortedRows.size();
					rows.remove(lastRow);
					sortedRows.add(lastRow);
				} else {
					final ChangeSetRow link = lastRow.getRhsWiringLink();
					lastRow = null;
					if (link != null) {
						if (!sortedRows.contains(link)) {
							sortedRows.add(idx, link);
							lastRow = link;
						}
						rows.remove(link);
					}
				}
			}
		}
		if (isBase)

		{
			changeSet.getChangesToBase().addAll(wiringChanges);
			changeSet.getChangesToBase().addAll(vesselChanges);
			changeSet.getChangeSetRowsToBase().addAll(sortedRows);
		} else

		{
			changeSet.getChangesToPrevious().addAll(wiringChanges);
			changeSet.getChangesToPrevious().addAll(vesselChanges);
			changeSet.getChangeSetRowsToPrevious().addAll(sortedRows);
		}

		// Build metrics
		{
			final Metrics metrics = ChangesetFactory.eINSTANCE.createMetrics();

			long pnl = 0;
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
								pnl += slotVisit.getSlotAllocation().getCargoAllocation().getGroupProfitAndLoss().getProfitAndLoss();
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
								pnl -= slotVisit.getSlotAllocation().getCargoAllocation().getGroupProfitAndLoss().getProfitAndLoss();
							}
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : toSchedule.getOpenSlotAllocations()) {
					pnl -= openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}
			metrics.setPnlDelta((int) -pnl);
			if (isBase) {
				changeSet.setMetricsToBase(metrics);
			} else {
				changeSet.setMetricsToPrevious(metrics);

			}
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