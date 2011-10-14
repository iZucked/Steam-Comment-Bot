/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ResourcelessModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

public class LNGSchedulerJobControl extends AbstractEclipseJobControl {

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private Scenario scenario;

	private Schedule intermediateSchedule = null;

	private final ModelEntityMap entities = new ResourcelessModelEntityMap();

	private LocalSearchOptimiser<ISequenceElement> optimiser;

	public LNGSchedulerJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super("Optimising " + jobDescriptor.getJobName());
		this.jobDescriptor = jobDescriptor;
		this.scenario = jobDescriptor.getJobContext();
	}

	@Override
	protected void reallyPrepare() {

		scenario = EcoreUtil.copy(scenario);
		// scenario.setName(resource.getName().replaceAll(resource.getFileExtension(),
		// ""));

		entities.setScenario(scenario);

		final LNGScenarioTransformer lst = new LNGScenarioTransformer(scenario);

		final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

		IOptimisationData<ISequenceElement> data;
		try {
			data = lst.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			// Wrap up exception
			throw new RuntimeException(e);
		}

		final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot.createOptimiserAndContext(data, entities);

		final IOptimisationContext<ISequenceElement> context = optAndContext.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor<ISequenceElement>());

		optimiser.init();
		final IAnnotatedSolution<ISequenceElement> startSolution = optimiser.start(context);

		saveSolution("start state", startSolution);
	}

	private Schedule saveSolution(final String name, final IAnnotatedSolution<ISequenceElement> solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);

		schedule.setName(scenario.getName() + " " + name);

		scenario.getOrCreateScheduleModel().getSchedules().add(schedule);

		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.jobcontroller.core.AbstractManagedJob#step()
	 */
	@Override
	protected boolean step() {
		optimiser.step(REPORT_PERCENTAGE);
		currentProgress += REPORT_PERCENTAGE;

		if ((currentProgress % 5) == 0) {
			if (intermediateSchedule != null) {
				// ((EList<EObject>) intermediateSchedule.eContainer().eGet(
				// intermediateSchedule.eContainingFeature()))
				// .remove(intermediateSchedule);
			}
			intermediateSchedule = saveSolution(currentProgress + "%", optimiser.getBestSolution(false));
		}

		// System.err.println("current fitness " +
		// optimiser.getFitnessEvaluator().getCurrentFitness() + ", best " +
		// optimiser.getFitnessEvaluator().getBestFitness());

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state
			if (intermediateSchedule != null) {
				@SuppressWarnings("unchecked")
				final EList<EObject> schedules = ((EList<EObject>) intermediateSchedule.eContainer().eGet(intermediateSchedule.eContainingFeature()));
				schedules.remove(intermediateSchedule);
			}
			intermediateSchedule = null;
			saveSolution("optimised", optimiser.getBestSolution(true));
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
		}
	}

	@Override
	public void dispose() {

		kill();

		this.entities.dispose();

		// TODO: this.scenario = null;
		this.optimiser = null;

		super.dispose();
	}

	@Override
	public final SerializableScenario getJobOutput() {
		return new SerializableScenario(scenario);
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public boolean isPauseable() {
		return true;
	}
}
