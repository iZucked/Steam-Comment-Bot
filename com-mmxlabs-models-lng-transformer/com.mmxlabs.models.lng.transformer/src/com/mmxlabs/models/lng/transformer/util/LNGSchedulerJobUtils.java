/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PostExportProcessorModule;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;

/**
 * @author Simon Goodall
 * @noextend This class is not intended to be subclassed by clients.
 * @since 3.0
 * 
 */
public class LNGSchedulerJobUtils {

	/**
	 * Label used to prefix optimisation update commands so they can be undone later if possible to avoid a history of commands for each report interval.
	 */
	private static final String LABEL_PREFIX = "Optimised: ";

	/**
	 * Given the input scenario and am {@link IAnnotatedSolution}, create a new {@link Schedule} and update the related models ( the {@link CargoModel} and {@link InputModel})
	 * 
	 * @param injector
	 * @param scenario
	 * @param editingDomain
	 * @param entities
	 * @param solution
	 * @param lockKey
	 * @param solutionCurrentProgress
	 * @param LABEL_PREFIX
	 * @return
	 */
	public static Schedule exportSolution(final Injector injector, final MMXRootObject scenario, final EditingDomain editingDomain, final ModelEntityMap entities, final IAnnotatedSolution solution,
			final int solutionCurrentProgress) {

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		{
			final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
			childInjector.injectMembers(exporter);
		}
		final Schedule schedule = exporter.exportAnnotatedSolution(entities, solution);
		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		final InputModel inputModel = scenario.getSubModel(InputModel.class);
		final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);

		final String label = (solutionCurrentProgress != 0) ? (LABEL_PREFIX + solutionCurrentProgress + "%") : ("Evaluate");
		final CompoundCommand command = new CompoundCommand(label);

		try {

			if (editingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(true);
			}
			command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Schedule(), schedule));

			final Injector childInjector = injector.createChildInjector(new PostExportProcessorModule());

			final Key<List<IPostExportProcessor>> key = Key.get(new TypeLiteral<List<IPostExportProcessor>>() {
			});

			Iterable<IPostExportProcessor> postExportProcessors;
			try {
				postExportProcessors = childInjector.getInstance(key);
				//
			} catch (final ConfigurationException e) {
				postExportProcessors = null;
			}

			command.append(derive(editingDomain, scenario, schedule, inputModel, cargoModel, postExportProcessors));
			// command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Dirty(), false));
		} finally {
			if (editingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(false);
			}
		}
		editingDomain.getCommandStack().execute(command);

		// Hmm, should this be done here or as part of a command - it is a persisted item.
		// However the dirty adapter sets dirty to true outside of a command...
		scheduleModel.setDirty(false);
		return schedule;
	}

	public static void undoPreviousOptimsationStep(final EditingDomain editingDomain, final String lockKey, final int solutionCurrentProgress) {
		// Undo previous optimisation step if possible
		try {

			if (editingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) editingDomain).setAdaptersEnabled(false);
			}

			// Rollback last "save" and re-apply to avoid long history of undos
			if (solutionCurrentProgress != 0) {
				final Command mostRecentCommand = editingDomain.getCommandStack().getMostRecentCommand();
				if (mostRecentCommand != null) {
					if (mostRecentCommand.getLabel().startsWith(LABEL_PREFIX)) {
						final CommandStack stack = editingDomain.getCommandStack();
						if (stack instanceof MMXAdaptersAwareCommandStack) {
							// this is needed because we have claimed the lock under the given key
							// so if we undo using EDITORS as the lock, we spin forever.
							((MMXAdaptersAwareCommandStack) stack).undo(lockKey);
						} else {
							stack.undo();
						}
					}
				}
			}

		} finally {
			if (editingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) editingDomain).setAdaptersEnabled(true, true);
			}
		}
	}

	/**
	 * Given a {@link Schedule}, update the {@link CargoModel} for rewirings and the {@link InputModel} for assignment changes. Back link the {@link CargoModel} to the {@link Schedule}
	 * 
	 * @param domain
	 * @param scenario
	 * @param schedule
	 * @param inputModel
	 * @param cargoModel
	 * @param postExportProcessors
	 * @return
	 */
	public static Command derive(final EditingDomain domain, final MMXRootObject scenario, final Schedule schedule, final InputModel inputModel, final CargoModel cargoModel,
			final Iterable<IPostExportProcessor> postExportProcessors) {
		final CompoundCommand cmd = new CompoundCommand("Update Vessel Assignments");

		final HashSet<UUIDObject> previouslyLocked = new HashSet<UUIDObject>();

		final HashSet<UUIDObject> reassigned = new HashSet<UUIDObject>();

		// The ElementAssignment needs a cargo linked to a slot. Not all slots will have a cargo until after the command generated here is executed. Therefore build up a map to perform the link. Fall
		// back to slot.getCargo() is there is no mapping.
		final Map<LoadSlot, Cargo> slotToCargoMap = new HashMap<LoadSlot, Cargo>();

		// Maintain a two lists of commands. The null commands are the commands which unset (or set to null) references between cargoes and lots.. The set commands are the commands which then re-set
		// the new slot/cargo references. They are kept separate to avoid issues where oppposite references changes can lead to unexpected results.
		final List<Command> nullCommands = new LinkedList<Command>();
		final List<Command> setCommands = new LinkedList<Command>();

		// First pass - add in generated slots to the slot containers
		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {

			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {

				final Slot slot = slotAllocation.getSlot();
				// Slots created in the builder have no container so add it the container now as it is used.
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (slot.eContainer() == null) {
						cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlot));
					}
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.eContainer() == null) {
						cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), dischargeSlot));
					}

				} else {
					throw new IllegalStateException("Unsupported slot type");
				}
			}
		}

		// Next step: Find the first load in the allocation sequence and use it as the Cargo definition slot.
		// We may reuse the existing slot, or create a new one. For non-first load slots, remove the cargo definition

		// Maintain a list of used cargo objects.
		final Set<Cargo> usedCargoes = new HashSet<Cargo>();
		// Maintain a list of potentially unused cargoes - once this next step is complete, we need to remove the used cargoes to get the real list
		final Set<Cargo> possibleUnusedCargoes = new HashSet<Cargo>();

		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {

			// Keep reference to a des or fob slot if found during scan
			LoadSlot desPurchaseSlot = null;
			DischargeSlot fobSaleSlot = null;
			Cargo loadCargo = null;

			// The list of slots this cargo now uses
			final List<Slot> cargoSlots = new ArrayList<Slot>(allocation.getSlotAllocations().size());

			// Treat the first load slot as the cargo defining slot
			boolean firstLoad = true;
			for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {

				final Slot slot = slotAllocation.getSlot();
				cargoSlots.add(slot);

				// Slots created in the builder have no container so add it the container now as it is used.
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;

					if (firstLoad) {
						// Found our first load slot to define the cargo
						if (loadSlot.getCargo() == null) {
							// Slot has no existing cargo, so create a new one
							final Cargo c = CargoFactory.eINSTANCE.createCargo();
							c.setAllowRewiring(true);
							c.setName(slot.getName());
							cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), c));
							cmd.append(AddCommand.create(domain, c, CargoPackage.eINSTANCE.getCargo_Slots(), loadSlot));
							loadCargo = c;
							slotToCargoMap.put(loadSlot, c);
						} else {
							// use existing cargo ref
							loadCargo = loadSlot.getCargo();
						}

						firstLoad = false;
					} else {
						// Record different cargoes as possibly unused cargoes and remove the refernce
						if (slot.getCargo() != loadCargo) {
							possibleUnusedCargoes.add(slot.getCargo());
							nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
						}

					}
					if (loadSlot.isDESPurchase()) {
						desPurchaseSlot = loadSlot;
					}
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					// Record different cargoes as possibly unused cargoes and remove the refernce
					if (dischargeSlot.getCargo() != loadCargo) {
						possibleUnusedCargoes.add(dischargeSlot.getCargo());
						nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));

					}
					if (dischargeSlot.isFOBSale()) {
						fobSaleSlot = dischargeSlot;
					}
				} else {
					throw new IllegalStateException("Unsupported slot type");
				}
			}
			// Record the cargo defining cargo as a definitly used cargo
			usedCargoes.add(loadCargo);
			// Sanity check the FOB/DES cargoes
			if (desPurchaseSlot != null || fobSaleSlot != null) {
				if (allocation.getSlotAllocations().size() > 2) {
					throw new IllegalStateException("Multiple Load/Discharges for a DES Purchase or FOB Sale is not permitted");
				}
			}

			// "Load port" is the discharge port for DES purchases
			if (desPurchaseSlot != null) {
				// if (load.isDESPurchase()) {
				final Slot discharge = allocation.getSlotAllocations().get(1).getSlot();
				cmd.append(SetCommand.create(domain, desPurchaseSlot, CargoPackage.eINSTANCE.getSlot_Port(), discharge.getPort()));
			}
			// If the cargo is to become a FOB Sale - then we remove the vessel assignment.
			if (fobSaleSlot != null) {
				// Slot discharge = allocation.getSlotAllocations().get(1).getSlot();
				cmd.append(AssignmentEditorHelper.unassignElement(domain, inputModel, loadCargo));
			}

			// Remove references to slots no longer in the cargo
			final Set<Slot> oldSlots = new HashSet<Slot>();
			oldSlots.addAll(loadCargo.getSlots());
			oldSlots.removeAll(cargoSlots);
			for (final Slot slot : oldSlots) {
				nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			}

			// Add in the slots which are currently not in the cargo
			cargoSlots.removeAll(loadCargo.getSlots());
			for (final Slot slot : cargoSlots) {
				setCommands.add(AddCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), loadCargo));
			}
			// Finally match the CargoAllocation to the Cargo object
			cmd.append(SetCommand.create(domain, allocation, SchedulePackage.eINSTANCE.getCargoAllocation_InputCargo(), loadCargo));
		}

		// Add the null commands first so they do not overwrite the set commands
		for (final Command c : nullCommands) {
			cmd.append(c);
		}
		// Then add in the set commands
		for (final Command c : setCommands) {
			cmd.append(c);
		}

		// Remove any used cargoes from the possibly unused list
		possibleUnusedCargoes.removeAll(usedCargoes);
		// Make sure there is no null reference
		possibleUnusedCargoes.remove(null);

		// For slots which are no longer used, remove the cargo
		for (final EObject eObj : schedule.getUnusedElements()) {
			if (eObj instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) eObj;
				if (loadSlot.getCargo() != null) {
					final Cargo c = loadSlot.getCargo();
					possibleUnusedCargoes.remove(c);
					// Sanity check
					// Unused non-optional slots now handled by optimiser
					// if (!loadSlot.isOptional()) {
					// throw new RuntimeException("Non-optional cargo/load is not linked to a cargo");
					// }
					cmd.append(AssignmentEditorHelper.unassignElement(domain, inputModel, c));
					cmd.append(DeleteCommand.create(domain, c));
				}
			}
			if (eObj instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) eObj;
				// Market slot, we can remove it.
				if (spotSlot.getMarket() != null && eObj.eContainer() != null) {
					// Remove rather than full delete as we may wish to re-use the object later
					// Note ensure this is also removed from the unused elements list as Remove does not delete other references.
					cmd.append(RemoveCommand.create(domain, eObj));

				}
			}
			// Remove from the unused elements list
			cmd.append(RemoveCommand.create(domain, schedule, SchedulePackage.eINSTANCE.getSchedule_UnusedElements(), eObj));

		}

		// TODO: We do not expect to get here!
		if (!possibleUnusedCargoes.isEmpty()) {
			for (final Cargo c : possibleUnusedCargoes) {
				// Sanity check
				// Unused non-optional slots now handled by optimiser
				// if (!c.getLoadSlot().isOptional()) {
				// throw new RuntimeException("Non-optional cargo/load is not linked to a cargo");
				// }
				cmd.append(AssignmentEditorHelper.unassignElement(domain, inputModel, c));
				cmd.append(DeleteCommand.create(domain, c));
			}
		}

		for (final ElementAssignment ai : inputModel.getElementAssignments()) {
			if (ai.isLocked()) {
				previouslyLocked.add(ai.getAssignedObject());
			}
		}

		final List<ElementAssignment> newElementAssignments = new LinkedList<ElementAssignment>();

		int spotIndex = 0;
		for (final Sequence sequence : schedule.getSequences()) {
			int thisIndex = 0;
			if (sequence.isSpotVessel()) {
				thisIndex = spotIndex++;
			}

			final AVesselSet assignment = sequence.isSpotVessel() ? sequence.getVesselClass() : sequence.getVessel();
			int index = 0;
			for (final Event event : sequence.getEvents()) {
				UUIDObject object = null;
				if (event instanceof SlotVisit) {
					final Slot slot = ((SlotVisit) event).getSlotAllocation().getSlot();

					if (slot instanceof LoadSlot) {

						final LoadSlot loadSlot = (LoadSlot) slot;
						object = slotToCargoMap.get(loadSlot);
						if (object == null) {
							object = loadSlot.getCargo();
						}
					}
				} else if (event instanceof VesselEventVisit) {
					object = ((VesselEventVisit) event).getVesselEvent();
				}

				if (object != null) {
					final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
					ea.setAssignedObject(object);
					ea.setAssignment(assignment);
					ea.setSequence(index++);
					ea.setSpotIndex(thisIndex);
					ea.setLocked(previouslyLocked.contains(object));
					reassigned.add(object);
					newElementAssignments.add(ea);
				}
			}
		}

		// copy through assignments which were not present in the schedule
		// TODO this will probably need some thought around optional elements
		for (final ElementAssignment ea : inputModel.getElementAssignments()) {
			if (ea.getAssignedObject() != null && !reassigned.contains(ea.getAssignedObject())) {
				newElementAssignments.add(ea);
			}
		}

		cmd.append(SetCommand.create(domain, inputModel, InputPackage.eINSTANCE.getInputModel_ElementAssignments(), newElementAssignments));

		if (postExportProcessors != null) {
			for (final IPostExportProcessor processor : postExportProcessors) {
				processor.postProcess(domain, scenario, schedule, inputModel, cmd);
			}
		}
		return cmd;

	}

	public static IAnnotatedSolution evaluateCurrentState(final LNGTransformer transformer) {
		final ModelEntityMap entities = transformer.getEntities();
		final IOptimisationData data = transformer.getOptimisationData();
		final Injector injector = transformer.getInjector();
		/**
		 * Start the full evaluation process.
		 */

		// Step 1. Get or derive the initial sequences from the input scenario data
		final IModifiableSequences sequences = new ModifiableSequences(transformer.getOptimisationTransformer().createInitialSequences(data, entities));

		// Run through the sequences manipulator of things such as start/end port replacement

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		manipulator.init(data);
		// this will set the return elements to the right places, and remove the start elements.
		manipulator.manipulate(sequences);

		// run a scheduler on the sequences - there is no SchedulerFitnessEvaluator to guide it!
		final ISequenceScheduler scheduler = injector.getInstance(DirectRandomSequenceScheduler.class);
		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, null);

		// Make sure a schedule was created.
		if (scheduledSequences == null) {
			// Error scheduling
			throw new RuntimeException("Unable to evaluate Scenario. Check schedule level inputs (e.g. distances, vessel capacities, restrictions)");
		}

		// Get a ScheduleCalculator to process the schedule and obtain output data
		final ScheduleCalculator scheduleCalculator = injector.getInstance(ScheduleCalculator.class);

		// The output data structured, a solution with all the output data as annotations
		final AnnotatedSolution solution = (AnnotatedSolution) scheduleCalculator.calculateSchedule(sequences, scheduledSequences);
		// Create a fake context
		solution.setContext(new OptimisationContext(data, null, null, null, null, null, null, null));
		return solution;
	}

}
