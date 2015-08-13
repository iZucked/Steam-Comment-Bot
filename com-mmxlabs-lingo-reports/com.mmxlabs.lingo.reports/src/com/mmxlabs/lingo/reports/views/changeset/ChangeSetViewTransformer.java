
package com.mmxlabs.lingo.reports.views.changeset;

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

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.changeset.model.VesselChange;
import com.mmxlabs.lingo.reports.views.changeset.model.WiringChange;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
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
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ChangeSetViewTransformer {

	ChangeSetRoot createDataModel(final ScenarioInstance instance, IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		if (!(instance.eContainer() instanceof ScenarioInstance)) {
			return null;
		}
		final ScenarioInstance base = (ScenarioInstance) instance.eContainer();
		final ScenarioInstance optimised = instance;

		final List<ScenarioInstance> stages = new LinkedList<>();
		for (final Container child : instance.getElements()) {
			if (child instanceof ScenarioInstance) {
				ScenarioInstance scenarioInstance = (ScenarioInstance) child;
				stages.add(scenarioInstance);
			}
		}
		Collections.sort(stages, new Comparator<ScenarioInstance>() {
			@Override
			public int compare(final ScenarioInstance o1, final ScenarioInstance o2) {

				// Extract name and sort by index

				return 0;
			}
		});

		// Make sure these are in the correct place.
		// stages.add(0, base);
		stages.add(optimised);
		try {
			monitor.beginTask("Analysing Solutions", stages.size());
			ScenarioInstance prev = base;
			for (final ScenarioInstance current : stages) {

				root.getChangeSets().add(buildChangeSet(base, prev, current));
				prev = current;
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

	ChangeSet buildChangeSet(final ScenarioInstance base, final ScenarioInstance prev, final ScenarioInstance current) {
		final ModelReference baseReference = base.getReference();
		final ModelReference prevReference = prev.getReference();
		final ModelReference currentReference = current.getReference();

		// Load
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

	void extractElements(final Schedule schedule, final Collection<EObject> interestingEvents, final Collection<EObject> allEvents) {
		// final List<EObject> interestingEvents = new LinkedList<EObject>();
		// final Set<EObject> allEvents = new HashSet<EObject>();
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
			// if (builder.showOpenSlot(openSlotAllocation)) {
			interestingEvents.add(openSlotAllocation);
			// }
			allEvents.add(openSlotAllocation);
		}

	}

	void generateDifferences(final ScenarioInstance from, final ScenarioInstance to, final ChangeSet changeSet, final boolean isBase) {
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

		// Build up element groupers

		final List<WiringChange> wiringChanges = new LinkedList<>();
		final List<VesselChange> vesselChanges = new LinkedList<>();
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
					VesselChange vc = ChangesetFactory.eINSTANCE.createVesselChange();

					// wiringChanges.add(wiringChange);
					wiringChange.setLoadSlot_base((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					vc.setLoadSlot_base((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					wiringChange.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
					vc.setOriginalLoadAllocation(slotVisit.getSlotAllocation());

					// Get original discharge
					{
						final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
						Sequence sequence = cargoAllocation.getSequence();
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
							vc.setDischargeSlot_base((DischargeSlot) slotAllocation.getSlot());

							wiringChange.setOriginalDischargeAllocation(slotAllocation);
							vc.setOriginalDischargeAllocation(slotAllocation);
						}
					}
					for (final EObject e : equivalents) {
						if (e instanceof SlotVisit) {
							final SlotVisit slotVisit2 = (SlotVisit) e;

							wiringChange.setLoadSlot_target((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
							vc.setLoadSlot_target((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
							wiringChange.setNewLoadAllocation(slotVisit2.getSlotAllocation());
							vc.setNewLoadAllocation(slotVisit2.getSlotAllocation());

							final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
							Sequence sequence = cargoAllocation.getSequence();
							newVesselName = sequence.getName();
							vc.setNewVessel(sequence.getVesselAvailability());
							if (sequence.isSetCharterInMarket()) {
								vc.setNewVessel(sequence.getCharterInMarket());
							} else {
								vc.setNewVessel(sequence.getVesselAvailability());
							}

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
								vc.setDischargeSlot_target((DischargeSlot) slotAllocation.getSlot());
								wiringChange.setNewDischargeAllocation(slotAllocation);
								vc.setNewDischargeAllocation(slotAllocation);
							}
						} else if (e instanceof OpenSlotAllocation) {
							final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) e;
							wiringChange.setNewDischargeSlot((DischargeSlot) openSlotAllocation.getSlot());

						} else {
							assert false;
						}
					}

					if (wiringChange.getOriginalDischargeSlot() != null && wiringChange.getNewDischargeSlot() != null) {
						// TODO: Use this?
						// final String referenceStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
						// final String otherStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
						//
						if (!equivalanceGroupBuilder.getElementKey(wiringChange.getOriginalDischargeSlot()).equals(equivalanceGroupBuilder.getElementKey(wiringChange.getNewDischargeSlot()))) {
							wiringChanges.add(wiringChange);
							// Wiring change
							// Record change
						} else {
							// no change in wiring, discard the wiring change (but use for vessel change?)
						}
						// TODO: Check vessel change
					}
					if (oldVesselName != null && newVesselName != null) {
						if (!oldVesselName.equals(newVesselName)) {
							vesselChanges.add(vc);
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

				// it an event difference

				// Is it a vessel difference?

				// Else is another diffe

			}
		}
		for (final EObject element : uniqueElements) {
			// Is it a cargo?
			boolean isBaseElement = fromAllElements.contains(element);
			if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {

					String oldVesselName = null;
					String newVesselName = null;

					final WiringChange wiringChange = ChangesetFactory.eINSTANCE.createWiringChange();
					VesselChange vc = ChangesetFactory.eINSTANCE.createVesselChange();

					// wiringChanges.add(wiringChange);
					if (isBaseElement) {

						wiringChange.setLoadSlot_base((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						vc.setLoadSlot_base((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					} else {
						wiringChange.setLoadSlot_target((LoadSlot) slotVisit.getSlotAllocation().getSlot());
						vc.setLoadSlot_target((LoadSlot) slotVisit.getSlotAllocation().getSlot());
					}
					// Get original discharge
					{
						final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
						Sequence sequence = cargoAllocation.getSequence();
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
								vc.setDischargeSlot_base((DischargeSlot) slotAllocation.getSlot());
							} else {
								wiringChange.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
								vc.setDischargeSlot_target((DischargeSlot) slotAllocation.getSlot());
							}
						}
					}
					// for (final EObject e : equivalents) {
					// if (e instanceof SlotVisit) {
					// final SlotVisit slotVisit2 = (SlotVisit) e;
					// wiringChange.setLoadSlot_target((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
					// vc.setLoadSlot_target((LoadSlot) slotVisit2.getSlotAllocation().getSlot());
					// final CargoAllocation cargoAllocation = slotVisit2.getSlotAllocation().getCargoAllocation();
					// Sequence sequence = cargoAllocation.getSequence();
					// newVesselName = sequence.getName();
					// vc.setNewVessel(sequence.getVesselAvailability());
					// if (sequence.isSetCharterInMarket()) {
					// vc.setNewVessel(sequence.getCharterInMarket());
					// } else {
					// vc.setNewVessel(sequence.getVesselAvailability());
					// }
					//
					// if (cargoAllocation.getSlotAllocations().size() != 2) {
					// throw new RuntimeException("Complex cargoes are not supported");
					// }
					// for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					// if (slotAllocation == slotVisit2.getSlotAllocation()) {
					// continue;
					// }
					// // Otherwise it is the original discharge
					// // TODO: Maybe store the slot allocation? Problematic with the open slot allocation....
					// wiringChange.setNewDischargeSlot((DischargeSlot) slotAllocation.getSlot());
					// vc.setDischargeSlot_target((DischargeSlot) slotAllocation.getSlot());
					// }
					// } else if (e instanceof OpenSlotAllocation) {
					// final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) e;
					// wiringChange.setNewDischargeSlot((DischargeSlot) openSlotAllocation.getSlot());
					//
					// } else {
					// assert false;
					// }
					// }

					// if (wiringChange.getOriginalDischargeSlot() != null && wiringChange.getNewDischargeSlot() != null) {
					// // TODO: Use this?
					// // final String referenceStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
					// // final String otherStr = CargoAllocationUtils.getSalesWiringAsString(pinnedCargoAllocation);
					// //
					// if (!equivalanceGroupBuilder.getElementKey(wiringChange.getOriginalDischargeSlot()).equals(equivalanceGroupBuilder.getElementKey(wiringChange.getNewDischargeSlot()))) {
					wiringChanges.add(wiringChange);
					// Wiring change
					// Record change
					//// } else {
					//// // no change in wiring, discard the wiring change (but use for vessel change?)
					//// }
					//// // TODO: Check vessel change
					//// }
					// if (oldVesselName != null && newVesselName != null) {
					// if (!oldVesselName.equals(newVesselName)) {
					vesselChanges.add(vc);
					// }
					// }
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
				int ii = 0;
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

		for (final WiringChange wc : wiringChanges) {
			// ChangeSetRow row = null;
			// ChangeSetRow rhsRow = null;

			String lhsName = wc.getLoadSlot_base() != null ? wc.getLoadSlot_base().getName() : null;
			if (lhsName == null) {
				lhsName = wc.getLoadSlot_target() != null ? wc.getLoadSlot_target().getName() : null;
			}
			String rhsName = wc.getNewDischargeSlot() != null ? wc.getNewDischargeSlot().getName() : null;
			if (rhsName == null) {
				// rhsName = wc.getNewDischargeSlot() != null ? wc.getNewDischargeSlot().getName() : null;
			}
			ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
			rows.add(row);

			if (lhsName != null) {
				// if (lhsToRowMap.containsKey(lhsName)) {
				// lhsRow = lhsToRowMap.get(lhsName);
				// } else if (rhsToRowMap.containsKey(rhsName)) {
				// lhsRow = rhsToRowMap.get(rhsName);
				// row.setLhsName(lhsName);
				// row.setLhsWiringChange(wc);
				lhsToRowMap.put(lhsName, row);
				// row.setLoadSlot(wc.getLoadSlot_base());
				// } else {
				row.setLhsName(lhsName);
				row.setLhsWiringChange(wc);
				lhsToRowMap.put(lhsName, row);
				row.setLoadSlot(wc.getLoadSlot_base());
				row.setOriginalLoadAllocation(wc.getOriginalLoadAllocation());
				row.setNewLoadAllocation(wc.getNewLoadAllocation());

				row.setOriginalDischargeAllocation(wc.getOriginalDischargeAllocation());
				row.setNewDischargeAllocation(wc.getNewDischargeAllocation());
				// }
			}
			// rhsRow = lhsRow;
			if (rhsName != null) {
				// if (rhsToRowMap.containsKey(rhsName)) {
				// rhsRow = rhsToRowMap.get(rhsName);
				// } else {
				// rhsRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
				row.setDischargeSlot(wc.getNewDischargeSlot());
				row.setRhsName(rhsName);
				row.setRhsWiringChange(wc);
				rhsToRowMap.put(rhsName, row);
				// rows.add(rhsRow);
				// }
			}
		}
		for (final WiringChange wc : wiringChanges) {

			ChangeSetRow lhsRow = null;
			// ChangeSetRow rhsRow = null;

			String lhsName = wc.getLoadSlot_base() != null ? wc.getLoadSlot_base().getName() : null;
			if (lhsName == null) {
				// lhsName = wc.getLoadSlot_target() != null ? wc.getLoadSlot_target().getName() : null;
			} // final String rhsName = wc.getOriginalDischargeSlot() != null ? wc.getOriginalDischargeSlot().getName() : null;
			if (lhsName != null) {
				if (lhsToRowMap.containsKey(lhsName)) {
					lhsRow = lhsToRowMap.get(lhsName);
				}
			}
			ChangeSetRow otherRhsRow = null;
			final String rhsNewName = wc.getOriginalDischargeSlot() != null ? wc.getOriginalDischargeSlot().getName() : null;
			if (rhsToRowMap.containsKey(rhsNewName)) {
				otherRhsRow = rhsToRowMap.get(rhsNewName);
			} else {
				otherRhsRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
				rows.add(otherRhsRow);
				otherRhsRow.setRhsName(rhsNewName);
				otherRhsRow.setRhsWiringChange(wc);
				rhsToRowMap.put(rhsNewName, otherRhsRow);
				otherRhsRow.setDischargeSlot(wc.getOriginalDischargeSlot());
			}
			if (lhsRow == null) {
				String lhsName2 = wc.getLoadSlot_target() != null ? wc.getLoadSlot_target().getName() : null;
				if (lhsName2 == null) {
					// lhsName2 = wc.getLoadSlot_base() != null ? wc.getLoadSlot_base().getName() : null;
				}
				otherRhsRow.setLhsName(lhsName2);
				otherRhsRow.setLoadSlot(wc.getLoadSlot_target());
				if (otherRhsRow.getLoadSlot() == null) {
					// otherRhsRow.setLoadSlot(wc.getLoadSlot_base());
				}
			}

			if (lhsRow != null && "B2".equals(lhsRow.getLhsName())) {
				int ii = 0;
			}
			// Bind the rows for wirings
			if (lhsRow != null && otherRhsRow != null) {
				if (lhsRow.getRhsWiringLink() != null) {
					int ii = 0;
				}
				lhsRow.setRhsWiringLink(otherRhsRow);
			}
		}
		//
		for (VesselChange vc : vesselChanges) {
			ChangeSetRow row = null;
			String lhsName = vc.getLoadSlot_base() != null ? vc.getLoadSlot_base().getName() : null;
			if (lhsName == null) {
				lhsName = vc.getLoadSlot_target() != null ? vc.getLoadSlot_target().getName() : null;
			}
			String rhsName = vc.getDischargeSlot_base() != null ? vc.getDischargeSlot_base().getName() : null;

			if (lhsToRowMap.containsKey(lhsName)) {
				row = lhsToRowMap.get(lhsName);
			} else {
				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				row.setLhsName(lhsName);
				row.setLoadSlot(vc.getLoadSlot_base());
				row.setOriginalLoadAllocation(vc.getOriginalLoadAllocation());
				row.setNewLoadAllocation(vc.getNewLoadAllocation());
				row.setOriginalDischargeAllocation(vc.getOriginalDischargeAllocation());
				row.setNewDischargeAllocation(vc.getNewDischargeAllocation());
				row.setRhsName(rhsName);
				// row.setLhsVesselChange(vc);
				// lhsRow.setLhsVesselChange(vc);
				lhsToRowMap.put(lhsName, row);
				rows.add(row);
			}

			row.setLhsVesselName(getName(vc.getOriginalVessel()));
			row.setRhsVesselName(getName(vc.getNewVessel()));
		}

		// TODO
		// for (OtherChabnge : otherChabnges) {
		//
		// }

		List<ChangeSetRow> sortedRows = new LinkedList<>();

		if (!rows.isEmpty()) {
			ChangeSetRow lastRow = null;// rows.iterator().next();
			// rows.remove(lastRow);
			// sortedRows.add(lastRow);
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

					rows.remove(lastRow);
					sortedRows.add(lastRow);
				} else {
					ChangeSetRow link = lastRow.getRhsWiringLink();
					lastRow = null;
					if (link != null) {
						if (!sortedRows.contains(link)) {// && rows.contains(link)) {
							sortedRows.add(link);
							lastRow = link;
						} else {
							lastRow = null;
						}
						rows.remove(link);
					}
					// if (link == lastRow) {
					// lastRow = null;
					// } else {
					// }
				}
			}
		}

		if (isBase) {
			changeSet.getChangesToBase().addAll(wiringChanges);
			changeSet.getChangesToBase().addAll(vesselChanges);
			changeSet.getChangeSetRowsToBase().addAll(sortedRows);
		} else {
			changeSet.getChangesToPrevious().addAll(wiringChanges);
			changeSet.getChangesToPrevious().addAll(vesselChanges);
			changeSet.getChangeSetRowsToPrevious().addAll(sortedRows);
		}

		// Build metrics
		{
			Metrics metrics = ChangesetFactory.eINSTANCE.createMetrics();

			long pnl = 0;
			{
				for (final Sequence sequence : fromSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl += groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							SlotVisit slotVisit = (SlotVisit) event;
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
							ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl -= groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							SlotVisit slotVisit = (SlotVisit) event;
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

	String getName(VesselAssignmentType t) {
		if (t instanceof VesselAvailability) {
			return ((VesselAvailability) t).getVessel().getName();
		} else if (t instanceof CharterInMarket) {
			return ((CharterInMarket) t).getName();
		}
		return null;
	}
}