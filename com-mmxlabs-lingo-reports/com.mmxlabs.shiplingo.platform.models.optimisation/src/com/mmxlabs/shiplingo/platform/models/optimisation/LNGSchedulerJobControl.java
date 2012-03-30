/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation;

import javax.management.timer.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
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

	private ModelEntityMap entities ;

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

		LNGTransformer transformer = new LNGTransformer(scenario);

		IOptimisationData data = transformer.getOptimisationData();
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

		saveInitialSolution(startSolution);
	}

	private Schedule saveInitialSolution(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		exporter.addPlatformExporterExtensions();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);

		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		scheduleModel.setInitialSchedule(schedule);
		
		return schedule;
	}

	private Schedule saveOptimisedSolution(final IAnnotatedSolution solution) {
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		exporter.addPlatformExporterExtensions();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);

		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		scheduleModel.setOptimisedSchedule(schedule);
		
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
			intermediateSchedule = saveOptimisedSolution(optimiser.getBestSolution(false));
		}

		// System.err.println("current fitness " +
		// optimiser.getFitnessEvaluator().getCurrentFitness() + ", best " +
		// optimiser.getFitnessEvaluator().getBestFitness());

		super.setProgress(currentProgress);
		if (optimiser.isFinished()) {
			// export final state
			intermediateSchedule = saveOptimisedSolution(optimiser.getBestSolution(true));
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
}
