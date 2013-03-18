/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.inject.Injector;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;

/**
 * A simple {@link IJobControl} to evaluate a schedule.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGSchedulerEvaluationJobControl implements IJobControl {

	private final LinkedList<IJobControlListener> listeners = new LinkedList<IJobControlListener>();

	private LNGSchedulerJobDescriptor jobDescriptor;

	private EJobState currentState = EJobState.UNKNOWN;

	public LNGSchedulerEvaluationJobControl(final LNGSchedulerJobDescriptor descriptor) {
		this.jobDescriptor = descriptor;
	}

	@Override
	public IJobDescriptor getJobDescriptor() {
		return jobDescriptor;
	}

	@Override
	public void prepare() {
		setJobState(EJobState.INITIALISED);
	}

	@Override
	public void start() {
		setJobState(EJobState.RUNNING);
		final ScenarioInstance scenarioInstance = jobDescriptor.getJobContext();

		scenarioInstance.getLock(jobDescriptor.getLockKey()).awaitClaim();
		try {
			final MMXRootObject scenario = (MMXRootObject) scenarioInstance.getInstance();
			final EditingDomain editingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

			// Create the transformer and object reference to the data objects
			final LNGTransformer transformer = new LNGTransformer(scenario);

			final Injector injector = transformer.getInjector();
			final ModelEntityMap entities = transformer.getEntities();
			final IOptimisationData data = transformer.getOptimisationData();

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

			// Pass annotated solution to utils to create a new Schedule object and update the other data models as required.
			LNGSchedulerJobUtils.saveInitialSolution(injector, scenario, editingDomain, transformer.getEntities(), solution, 0);

			setJobState(EJobState.COMPLETED);
		} catch (final Exception e) {
			setJobState(EJobState.CANCELLED);
			throw new RuntimeException(e);
		} finally {
			scenarioInstance.getLock(jobDescriptor.getLockKey()).release();
		}
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isPauseable() {
		return false;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public EJobState getJobState() {
		return currentState;
	}

	@Override
	public int getProgress() {
		return 0;
	}

	@Override
	public Object getJobOutput() {
		return jobDescriptor.getJobContext().getInstance();
	}

	@Override
	public void addListener(final IJobControlListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(final IJobControlListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void dispose() {
		jobDescriptor = null;
		this.currentState = EJobState.UNKNOWN;
	}

	private synchronized void setJobState(final EJobState newState) {
		final EJobState oldState = currentState;
		if (oldState != newState) {

			currentState = newState;

			// Take copy to avoid concurrent modification exceptions.
			final List<IJobControlListener> copy = new ArrayList<IJobControlListener>(listeners);

			for (final IJobControlListener mjl : copy) {
				if (!mjl.jobStateChanged(this, oldState, newState)) {
					listeners.remove(mjl);
				}
			}
		}
	}
}
