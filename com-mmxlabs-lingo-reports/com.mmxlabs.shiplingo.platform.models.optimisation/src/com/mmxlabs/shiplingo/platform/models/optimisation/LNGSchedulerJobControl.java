/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import java.util.LinkedList;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.progress.IProgressConstants2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class LNGSchedulerJobControl extends AbstractEclipseJobControl {
	private static final String LABEL_PREFIX = "Optimised: ";

	private static final Logger log = LoggerFactory.getLogger(LNGSchedulerJobControl.class);

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private final ScenarioInstance scenarioInstance;

	private final MMXRootObject scenario;

	private Schedule intermediateSchedule = null;

	private ModelEntityMap entities;

	private LocalSearchOptimiser optimiser;

	private long startTimeMillis;

	private final EditingDomain editingDomain;

	private static final ImageDescriptor imgOpti = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/elcl16/resume_co.gif");
	private static final ImageDescriptor imgEval = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/evaluate_schedule.gif");

	public LNGSchedulerJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super((jobDescriptor.isOptimising() ? "Optimising " : "Evaluating ") + jobDescriptor.getJobName(), CollectionsUtil.<QualifiedName, Object> makeHashMap(
				IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, true, IProgressConstants.ICON_PROPERTY, (jobDescriptor.isOptimising() ? imgOpti : imgEval)));
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

		final IOptimisationData data = transformer.getOptimisationData();
		entities = transformer.getEntities();

		final OptimisationTransformer ot = transformer.getOptimisationTransformer();
		final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, entities);

		final IOptimisationContext context = optAndContext.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		saveInitialSolution(startSolution, 0);
	}

	private Schedule saveInitialSolution(final IAnnotatedSolution solution, int currentProgress) {

		EditingDomain domain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
		try {

			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setAdaptersEnabled(false);
			}

			// Rollback last "save" and re-apply to avoid long history of undos
			if (currentProgress != 0) {
				Command mostRecentCommand = editingDomain.getCommandStack().getMostRecentCommand();
				if (mostRecentCommand != null) {
					if (mostRecentCommand.getLabel().startsWith(LABEL_PREFIX)) {
						editingDomain.getCommandStack().undo();
					}
				}
			}

		} finally {
			if (domain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) domain).setAdaptersEnabled(true, true);
			}
		}
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		exporter.addPlatformExporterExtensions();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);
		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		final InputModel inputModel = scenario.getSubModel(InputModel.class);
		final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);

		String label = (currentProgress != 0) ? (LABEL_PREFIX + currentProgress + "%") : ("Evaluate");
		final CompoundCommand command = new CompoundCommand(label);

		command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_InitialSchedule(), schedule));
		command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_OptimisedSchedule(), null));
		command.append(derive(editingDomain, schedule, inputModel, cargoModel));
		// command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Dirty(), false));

		if (!command.canExecute()) {
			throw new RuntimeException("Unable to execute save schedule command");
		}

		editingDomain.getCommandStack().execute(command);

		// Hmm, should this be done here or as part of a command - it is a persisted item.
		// However the dirty adapter sets dirty to true outside of a command...
		//
		//
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
			return false; // if we are not optimising, finish.
		}
		optimiser.step(REPORT_PERCENTAGE);
		currentProgress += REPORT_PERCENTAGE;

		if ((currentProgress % 5) == 0) {
			if (intermediateSchedule != null) {
				// ((EList<EObject>) intermediateSchedule.eContainer().eGet(
				// intermediateSchedule.eContainingFeature()))
				// .remove(intermediateSchedule);
			}
			intermediateSchedule = saveInitialSolution(optimiser.getBestSolution(false), currentProgress);
		}

		// System.err.println("current fitness " +
		// optimiser.getFitnessEvaluator().getCurrentFitness() + ", best " +
		// optimiser.getFitnessEvaluator().getBestFitness());

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state
			intermediateSchedule = saveInitialSolution(optimiser.getBestSolution(true), 100);
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

	private Command derive(EditingDomain domain, final Schedule schedule, final InputModel inputModel, final CargoModel cargoModel) {
		CompoundCommand cmd = new CompoundCommand("Update Vessel Assignments");

		List<Assignment> newAssignments = new LinkedList<Assignment>();

		for (final Sequence sequence : schedule.getSequences()) {
			final Assignment a = InputFactory.eINSTANCE.createAssignment();

			if (sequence.getVessel() == null) {
				continue;
			}

			if (sequence.isSpotVessel()) {
				a.setAssignToSpot(true);
				a.getVessels().add(sequence.getVesselClass());
			} else {
				a.setAssignToSpot(false);
				a.getVessels().add(sequence.getVessel());
			}

			for (final Event event : sequence.getEvents()) {
				if (event instanceof SlotVisit) {
					final Slot slot = ((SlotVisit) event).getSlotAllocation().getSlot();

					if (slot instanceof LoadSlot) {
						a.getAssignedObjects().add(((LoadSlot) slot).getCargo());
					}
				} else if (event instanceof VesselEventVisit) {
					a.getAssignedObjects().add(((VesselEventVisit) event).getVesselEvent());
				}
			}

			if (!a.getAssignedObjects().isEmpty() || sequence.isSpotVessel() == false) {
				newAssignments.add(a);
			}
		}

		cmd.append(SetCommand.create(domain, inputModel, InputPackage.eINSTANCE.getInputModel_Assignments(), newAssignments));

		// rewire any cargos which require it
		// TODO handle spot market cases, and free slots
		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {
			if (allocation.getInputCargo() == null) {
				// this does not correspond directly to an input cargo;
				// get the slots, find their cargos, and adjust them?
				final LoadSlot load = (LoadSlot) allocation.getLoadAllocation().getSlot();
				final DischargeSlot discharge = (DischargeSlot) allocation.getDischargeAllocation().getSlot();

				final Cargo loadCargo = load.getCargo();
				final Cargo dischargeCargo = discharge.getCargo();

				// the cargo "belongs" to the load slot
				// loadCargo.setDischargeSlot(discharge);
				cmd.append(SetCommand.create(domain, loadCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), discharge));
				dischargeCargo.setDischargeSlot(null);
				cmd.append(SetCommand.create(domain, dischargeCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), null));
			}
		}

		return cmd;

	}
}
