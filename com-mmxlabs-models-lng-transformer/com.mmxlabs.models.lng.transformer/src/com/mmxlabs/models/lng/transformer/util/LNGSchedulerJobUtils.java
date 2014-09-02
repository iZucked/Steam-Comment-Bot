/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

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
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
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

/**
 * @author Simon Goodall
 * @noextend This class is not intended to be subclassed by clients.
 * 
 */
public class LNGSchedulerJobUtils {

	/**
	 * Label used to prefix optimisation update commands so they can be undone later if possible to avoid a history of commands for each report interval.
	 */
	private static final String LABEL_PREFIX = "Optimised: ";

	/**
	 * Given the input scenario and am {@link IAnnotatedSolution}, create a new {@link Schedule} and update the related models ( the {@link CargoModel} and {@link AssignmentModel})
	 * 
	 * @param injector
	 * @param scenario
	 * @param editingDomain
	 * @param modelEntityMap
	 * @param solution
	 * @param lockKey
	 * @param solutionCurrentProgress
	 * @param LABEL_PREFIX
	 * @return
	 */
	public static Schedule exportSolution(final Injector injector, final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final EditingDomain editingDomain,
			final ModelEntityMap modelEntityMap, final IAnnotatedSolution solution, final int solutionCurrentProgress) {

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		{
			final Injector childInjector = injector.createChildInjector(new ExporterExtensionsModule());
			childInjector.injectMembers(exporter);
		}
		final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();

		final Schedule schedule = exporter.exportAnnotatedSolution(modelEntityMap, solution);
		final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
		final CargoModel cargoModel = portfolioModel.getCargoModel();

		final CompoundCommand command = createBlankCommand(solutionCurrentProgress);

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

			command.append(derive(editingDomain, scenario, schedule, cargoModel, postExportProcessors));
			// command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Dirty(), false));
			command.append(SetCommand.create(editingDomain, portfolioModel, LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel_Parameters(), optimiserSettings));
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

	public static CompoundCommand createBlankCommand(final int solutionCurrentProgress) {
		final String label = (solutionCurrentProgress != 0) ? (LABEL_PREFIX + solutionCurrentProgress + "%") : ("Evaluate");
		final CompoundCommand command = new CompoundCommand(label);
		return command;
	}

	public static void undoPreviousOptimsationStep(final EditingDomain editingDomain, final int solutionCurrentProgress) {
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
							((MMXAdaptersAwareCommandStack) stack).undo(null);
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
	 * Given a {@link Schedule}, update the {@link CargoModel} for rewirings and the {@link AssignmentModel} for assignment changes. Back link the {@link CargoModel} to the {@link Schedule}
	 * 
	 * @param domain
	 * @param scenario
	 * @param schedule
	 * @param assignmentModel
	 * @param cargoModel
	 * @param postExportProcessors
	 * @return
	 */
	public static Command derive(final EditingDomain domain, final MMXRootObject scenario, final Schedule schedule, final CargoModel cargoModel,
			final Iterable<IPostExportProcessor> postExportProcessors) {
		final CompoundCommand cmd = new CompoundCommand("Update Vessel Assignments");

		// The ElementAssignment needs a cargo linked to a slot. Not all slots will have a cargo until after the command generated here is executed. Therefore build up a map to perform the link. Fall
		// back to slot.getCargo() is there is no mapping.
		final Map<LoadSlot, Cargo> slotToCargoMap = new HashMap<LoadSlot, Cargo>();

		// Maintain a two lists of commands. The null commands are the commands which unset (or set to null) references between cargoes and lots.. The set commands are the commands which then re-set
		// the new slot/cargo references. They are kept separate to avoid issues where opposite references changes can lead to unexpected results.
		// final List<Command> nullCommands = new LinkedList<Command>();
		final List<Command> setCommands = new LinkedList<Command>();

		// Set of slots which may not be linked to a cargo
		final Set<Slot> unsetCargoSlots = new HashSet<Slot>();
		// Set of slots which really are linked to a cargo. This will later be taken out of the unsetCargoSlots
		final Set<Slot> setCargoSlots = new HashSet<Slot>();

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
							loadCargo = c;
							slotToCargoMap.put(loadSlot, c);
						} else {
							// use existing cargo ref
							loadCargo = loadSlot.getCargo();
						}

						firstLoad = false;
					} else {
						// Record different cargoes as possibly unused cargoes and remove the reference
						if (slot.getCargo() != loadCargo) {
							possibleUnusedCargoes.add(slot.getCargo());
							unsetCargoSlots.addAll(slot.getCargo().getSlots());
							unsetCargoSlots.remove(slot);
							// nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
						}

					}
					if (loadSlot.isDESPurchase()) {
						desPurchaseSlot = loadSlot;
					}
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					// Record different cargoes as possibly unused cargoes and remove the reference
					if (dischargeSlot.getCargo() != loadCargo) {
						possibleUnusedCargoes.add(dischargeSlot.getCargo());
						unsetCargoSlots.add(slot);
						// nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));

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

				// Spot slots may not have a port set, so copy from other half
				if (allocation.getSlotAllocations().size() == 2) {
					if (desPurchaseSlot instanceof SpotSlot) {
						cmd.append(SetCommand.create(domain, desPurchaseSlot, CargoPackage.eINSTANCE.getSlot_Port(), allocation.getSlotAllocations().get(1).getPort()));
						// Adjust port times to be local to new port
						updateSpotSlotDate(domain, cmd, desPurchaseSlot, allocation.getSlotAllocations().get(1).getPort());
					}

					if (fobSaleSlot instanceof SpotSlot) {
						cmd.append(SetCommand.create(domain, fobSaleSlot, CargoPackage.eINSTANCE.getSlot_Port(), allocation.getSlotAllocations().get(0).getPort()));
						// Adjust port times to be local to new port
						updateSpotSlotDate(domain, cmd, fobSaleSlot, allocation.getSlotAllocations().get(0).getPort());
					}
				}
			}

			// // If the cargo is to become a FOB Sale - then we remove the vessel assignment.
			// if (fobSaleSlot != null) {
			// // Slot discharge = allocation.getSlotAllocations().get(1).getSlot();
			// final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, loadCargo);
			// if (elementAssignment !=null) {
			// cmd.append(DeleteCommand.create(domain, elementAssignment));
			// }
			// }

			// Remove references to slots no longer in the cargo
			final Set<Slot> oldSlots = new HashSet<Slot>();
			oldSlots.addAll(loadCargo.getSlots());
			oldSlots.removeAll(cargoSlots);
			for (final Slot slot : oldSlots) {
				unsetCargoSlots.add(slot);
				// nullCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
			}

			// Add in the slots which are currently not in the cargo
			cargoSlots.removeAll(loadCargo.getSlots());
			for (final Slot slot : cargoSlots) {
				setCommands.add(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), loadCargo));
				setCargoSlots.add(slot);
			}
			// Finally match the CargoAllocation to the Cargo object
			cmd.append(SetCommand.create(domain, allocation, SchedulePackage.eINSTANCE.getCargoAllocation_InputCargo(), loadCargo));
		}

		// Add the unset commands first so they do not overwrite the set commands
		unsetCargoSlots.removeAll(setCargoSlots);
		for (final Slot slot : unsetCargoSlots) {
			cmd.append(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
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
					cmd.append(SetCommand.create(domain, c, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE));
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

		// Create all the new vessel assignment objects.
		for (final Sequence sequence : schedule.getSequences()) {

			final AVesselSet<Vessel> assignment = sequence.isSpotVessel() ? sequence.getVesselClass() : (sequence.isSetVesselAvailability() ? sequence.getVesselAvailability().getVessel() : null);
			int index = 0;
			for (final Event event : sequence.getEvents()) {
				AssignableElement object = null;
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
					cmd.append(SetCommand.create(domain, object, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, assignment));
					cmd.append(SetCommand.create(domain, object, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, index++));
					if (sequence.isSetSpotIndex()) {
						cmd.append(SetCommand.create(domain, object, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, sequence.getSpotIndex()));
					} else {
						cmd.append(SetCommand.create(domain, object, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, SetCommand.UNSET_VALUE));
					}
				}
			}
		}

		if (postExportProcessors != null) {
			for (final IPostExportProcessor processor : postExportProcessors) {
				processor.postProcess(domain, scenario, schedule, cmd);
			}
		}
		return cmd;

	}

	public static IAnnotatedSolution evaluateCurrentState(final LNGTransformer transformer) {
		final ModelEntityMap modelEntityMap = transformer.getModelEntityMap();
		final IOptimisationData data = transformer.getOptimisationData();
		final Injector injector = transformer.getInjector();
		/**
		 * Start the full evaluation process.
		 */

		// Step 1. Get or derive the initial sequences from the input scenario data
		final IModifiableSequences sequences = new ModifiableSequences(transformer.getOptimisationTransformer().createInitialSequences(data, modelEntityMap));

		// Run through the sequences manipulator of things such as start/end port replacement

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		manipulator.init(data);
		// this will set the return elements to the right places, and remove the start elements.
		manipulator.manipulate(sequences);

		// run a scheduler on the sequences - there is no SchedulerFitnessEvaluator to guide it!
		final ISequenceScheduler scheduler = injector.getInstance(DirectRandomSequenceScheduler.class);

		// The output data structured, a solution with all the output data as annotations
		final AnnotatedSolution solution = new AnnotatedSolution();
		solution.setSequences(sequences);
		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, solution);

		// Make sure a schedule was created.
		if (scheduledSequences == null) {
			// Error scheduling
			throw new RuntimeException("Unable to evaluate Scenario. Check schedule level inputs (e.g. distances, vessel capacities, restrictions)");
		}

		// Create a fake context
		solution.setContext(new OptimisationContext(data, null, null, null, null, null, null, null));
		return solution;
	}

	private static void updateSpotSlotDate(final EditingDomain domain, final CompoundCommand cmd, final Slot slot, final Port port) {

		if (slot.getPort() != port) {
			final String oldZone;
			if (slot.getPort() == null) {
				// Assume current date is UTC
				oldZone = "UTC";
			} else {
				oldZone = slot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			}
			final String newZone = ((Port) port).getTimeZone();
			// remap from old zone to new zone
			if ((newZone == null && !(oldZone == null)) || (newZone != null && !newZone.equals(oldZone))) {
				final Calendar oldCalendar = Calendar.getInstance(getZone(oldZone));
				final Calendar newCalendar = Calendar.getInstance(getZone(newZone));
				// Prime with current date in old tz
				oldCalendar.setTime(slot.getWindowStart());
				// Clear all components (specifically time)
				newCalendar.clear();
				// Replicate the date components in new TZ
				newCalendar.set(Calendar.YEAR, oldCalendar.get(Calendar.YEAR));
				newCalendar.set(Calendar.MONTH, oldCalendar.get(Calendar.MONTH));
				newCalendar.set(Calendar.DAY_OF_MONTH, oldCalendar.get(Calendar.DAY_OF_MONTH));

				final Date newDate = newCalendar.getTime();
				cmd.append(SetCommand.create(domain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), newDate));
			}
		}

	}

	private static TimeZone getZone(final String zone) {
		if (zone == null || zone.isEmpty())
			return TimeZone.getTimeZone("UTC");
		else
			return TimeZone.getTimeZone(zone);
	}

}
