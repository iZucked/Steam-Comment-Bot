/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerModule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;

public class LNGSchedulerJobControl extends AbstractEclipseJobControl {
	private static final Logger log = LoggerFactory.getLogger(LNGSchedulerJobControl.class);

	private static final int REPORT_PERCENTAGE = 1;
	private int currentProgress = 0;

	private final LNGSchedulerJobDescriptor jobDescriptor;

	private MMXRootObject scenario;

	private Schedule intermediateSchedule = null;

	private final ModelEntityMap entities = new ResourcelessModelEntityMap();

	private LocalSearchOptimiser optimiser;

	private long startTimeMillis;

	public LNGSchedulerJobControl(final LNGSchedulerJobDescriptor jobDescriptor) {
		super("Optimising " + jobDescriptor.getJobName());
		this.jobDescriptor = jobDescriptor;
		this.scenario = jobDescriptor.getJobContext();
	}

	@Override
	protected void reallyPrepare() {
		startTimeMillis = System.currentTimeMillis();
		scenario = EcoreUtil.copy(scenario);

		entities.setScenario(scenario);

		final LNGScenarioTransformer lst = LNGTransformerModule.createLNGScenarioTransformer(scenario);

		final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

		IOptimisationData data;
		try {
			lst.addPlatformTransformerExtensions();
			data = lst.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			// Wrap up exception
			throw new RuntimeException(e);
		}

		final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, entities);

		final IOptimisationContext context = optAndContext.getFirst();
		optimiser = optAndContext.getSecond();

		// because we are driving the optimiser ourself, so we can be paused, we
		// don't actually get progress callbacks.
		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		saveSolution("start state", startSolution);
	}

	private Schedule saveSolution(final String name, final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		exporter.addPlatformExporterExtensions();
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
			optimiser = null;
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
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
