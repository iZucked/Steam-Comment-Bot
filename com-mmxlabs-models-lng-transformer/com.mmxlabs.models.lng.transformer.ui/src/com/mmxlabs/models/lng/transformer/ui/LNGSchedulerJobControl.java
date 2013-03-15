/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
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
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.ExporterExtensionsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.PostExportProcessorModule;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;

public class LNGSchedulerJobControl extends AbstractEclipseJobControl {
	private static final String LABEL_PREFIX = "Optimised: ";

	private static final Logger log = LoggerFactory.getLogger(LNGSchedulerJobControl.class);

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final MMXRootObject scenario;

	private ModelEntityMap entities;

	private LocalSearchOptimiser optimiser;

	private long startTimeMillis;

	private final EditingDomain editingDomain;

	private static final ImageDescriptor imgOpti = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	private Injector injector = null;

	public LNGSchedulerJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimise " : "Evaluate ") + jobDescriptor.getJobName(), CollectionsUtil.<QualifiedName, Object> makeHashMap(IProgressConstants.ICON_PROPERTY,
				(jobDescriptor.isOptimising() ? imgOpti : imgEval)));
		this.jobDescriptor = jobDescriptor;
		this.scenarioInstance = jobDescriptor.getJobContext();
		this.scenario = (MMXRootObject) scenarioInstance.getInstance();
		editingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
	}

	@Override
	protected void reallyPrepare() {
		scenarioInstance.getLock(jobDescriptor.getLockKey()).awaitClaim();
		startTimeMillis = System.currentTimeMillis();

		final LNGTransformer transformer = new LNGTransformer(scenario);

		injector = transformer.getInjector();

		// final IOptimisationData data = transformer.getOptimisationData();
		entities = transformer.getEntities();

		final IOptimisationContext context = transformer.getOptimisationContext();
		optimiser = transformer.getOptimiser();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		saveInitialSolution(startSolution, 0);
	}

	private Schedule saveInitialSolution(final IAnnotatedSolution solution, final int solutionCurrentProgress) {

		final EditingDomain domain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
		try {

			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setAdaptersEnabled(false);
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
							((MMXAdaptersAwareCommandStack) stack).undo(jobDescriptor.getLockKey());
						} else {
							stack.undo();
						}
					}
				}
			}

		} finally {
			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setAdaptersEnabled(true, true);
			}
		}

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

			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setCommandProvidersDisabled(true);
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

			command.append(derive(editingDomain, schedule, inputModel, cargoModel, postExportProcessors));
			// command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Dirty(), false));
		} finally {
			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setCommandProvidersDisabled(false);
			}
		}
		editingDomain.getCommandStack().execute(command);

		// Hmm, should this be done here or as part of a command - it is a persisted item.
		// However the dirty adapter sets dirty to true outside of a command...
		scheduleModel.setDirty(false);
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#step()
	 */
	@Override
	protected boolean step() {
		// final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		if (jobDescriptor.isOptimising() == false) {
			// scheduleModel.setDirty(false);
			// log.debug("Cleared dirty bit on " + scheduleModel);
			// clear lock
			scenarioInstance.getLock(jobDescriptor.getLockKey()).release();
			return false; // if we are not optimising, finish.
		}
		optimiser.step(REPORT_PERCENTAGE);
		currentProgress += REPORT_PERCENTAGE;

		// if ((currentProgress % 5) == 0) {
		// Disable intermediate solution exporting. This is to avoid various concurrency issues caused by UI updating at the same time as a new solution is exported.
		// See e.g. FogBugz: 830, 838, 847, 848

		// saveInitialSolution(optimiser.getBestSolution(false), currentProgress);
		// }

		// System.err.println("current fitness " +
		// optimiser.getFitnessEvaluator().getCurrentFitness() + ", best " +
		// optimiser.getFitnessEvaluator().getBestFitness());

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state
			saveInitialSolution(optimiser.getBestSolution(true), 100);
			optimiser = null;
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
			// scheduleModel.setDirty(false);
			// log.debug("Cleared dirty bit on " + scheduleModel);
			super.setProgress(100);
			scenarioInstance.getLock(jobDescriptor.getLockKey()).release();
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#kill()
	 */
	@Override
	protected void kill() {
		if (optimiser != null) {
			optimiser.dispose();
			optimiser = null;
		}
		scenarioInstance.getLock(jobDescriptor.getLockKey()).release();
	}

	@Override
	public void dispose() {

		kill();
		if (this.entities != null) {
			this.entities.dispose();
			this.entities = null;
		}

		// TODO: this.scenario = null;
		this.optimiser = null;

		super.dispose();
	}

	@Override
	public final MMXRootObject getJobOutput() {
		return scenario;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public boolean isPauseable() {
		return true;
	}

	private Command derive(final EditingDomain domain, final Schedule schedule, final InputModel inputModel, final CargoModel cargoModel, final Iterable<IPostExportProcessor> postExportProcessors) {
		final CompoundCommand cmd = new CompoundCommand("Update Vessel Assignments");

		final HashSet<UUIDObject> previouslyLocked = new HashSet<UUIDObject>();

		final HashSet<UUIDObject> reassigned = new HashSet<UUIDObject>();

		// The ElementAssignment needs a cargo linked to a slot. Not all slots will have a cargo until after the command generated here is executed. Therefore build up a map to perform the link. Fall
		// back to slot.getCargo() is there is no mapping.
		final Map<LoadSlot, Cargo> slotToCargoMap = new HashMap<LoadSlot, Cargo>();

		// rewire any cargos which require it
		// TODO handle spot market cases, and free slots
		final List<Command> nullCommands = new LinkedList<Command>();
		final List<Command> setCommands = new LinkedList<Command>();

		// Maintain the set of cargoes which we a) remove the discharge slot and b) and one to.
		// If the subtract b from a what is left should be the cargoes removed from the solution .. hopefully optional..
		final Set<Cargo> nullCargoes = new HashSet<Cargo>();
		final Set<Cargo> setCargoes = new HashSet<Cargo>();

		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {
			if (allocation.getInputCargo() == null) {
				// this does not correspond directly to an input cargo;
				// get the slots, find their cargos, and adjust them?
				final LoadSlot load = (LoadSlot) allocation.getLoadAllocation().getSlot();
				final DischargeSlot discharge = (DischargeSlot) allocation.getDischargeAllocation().getSlot();

				// Spot market options

				// Slots created in the builder have no container so add it the container now as it is used.
				if (load.eContainer() == null) {
					cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), load));
				}
				if (discharge.eContainer() == null) {
					cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), discharge));
				}

				// Optional loads may not have an original cargo, so create one now.
				final Cargo loadCargo;
				if (load.getCargo() == null) {
					final Cargo c = CargoFactory.eINSTANCE.createCargo();
					c.setAllowRewiring(true);
					c.setName(load.getName());
					cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), c));
					cmd.append(SetCommand.create(domain, c, CargoPackage.eINSTANCE.getCargo_LoadSlot(), load));
					loadCargo = c;
					slotToCargoMap.put(load, c);
				} else {
					loadCargo = load.getCargo();
				}
				// "Load port" is the discharge port for DES purchases
				if (load.isDESPurchase()) {
					cmd.append(SetCommand.create(domain, load, CargoPackage.eINSTANCE.getSlot_Port(), discharge.getPort()));
				}
				// If the cargo is to become a FOB Sale - then we remove the vessel assignment.
				if (discharge.isFOBSale()) {
					cmd.append(AssignmentEditorHelper.unassignElement(domain, inputModel, loadCargo));
				}

				final Cargo dischargeCargo = discharge.getCargo();

				// the cargo "belongs" to the load slot
				// loadCargo.setDischargeSlot(discharge);
				setCommands.add(SetCommand.create(domain, loadCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), discharge));
				setCargoes.add(loadCargo);

				if (dischargeCargo != null) {
					nullCommands.add(SetCommand.create(domain, dischargeCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), null));
					nullCargoes.add(dischargeCargo);
				}

				cmd.append(SetCommand.create(domain, allocation, SchedulePackage.eINSTANCE.getCargoAllocation_InputCargo(), loadCargo));
			}
		}

		// Add the null commands first so they do not overwrite the set commands
		for (final Command c : nullCommands) {
			cmd.append(c);
		}
		for (final Command c : setCommands) {
			cmd.append(c);
		}

		nullCargoes.removeAll(setCargoes);
		// For slots which are no longer used, remove the cargo
		for (final EObject eObj : schedule.getUnusedElements()) {
			if (eObj instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) eObj;
				if (loadSlot.getCargo() != null) {
					final Cargo c = loadSlot.getCargo();
					nullCargoes.remove(c);
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
		if (!nullCargoes.isEmpty()) {
			for (final Cargo c : nullCargoes) {
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
}
