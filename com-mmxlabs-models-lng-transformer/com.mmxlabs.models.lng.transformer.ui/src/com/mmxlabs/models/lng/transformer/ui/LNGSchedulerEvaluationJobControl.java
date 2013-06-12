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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

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

		final ScenarioLock lock = scenarioInstance.getLock(jobDescriptor.getLockKey());
		lock.awaitClaim();
		try {
			final LNGScenarioModel scenario = (LNGScenarioModel) scenarioInstance.getInstance();
			final EditingDomain editingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

			// Create the transformer and object reference to the data objects
			final LNGTransformer transformer = new LNGTransformer(scenario);

			final Injector injector = transformer.getInjector();
			final IAnnotatedSolution solution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);

			// Pass annotated solution to utils to create a new Schedule object and update the other data models as required.
			LNGSchedulerJobUtils.exportSolution(injector, scenario, editingDomain, transformer.getEntities(), solution, 0);

			setJobState(EJobState.COMPLETED);
		} catch (final Throwable e) {
			setJobState(EJobState.CANCELLED);
			throw new RuntimeException(e);
		} finally {
			lock.release();
		}
	}

	@Override
	public void cancel() {
		setJobState(EJobState.CANCELLED);
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

		if (jobDescriptor != null) {
			// Release the lock should it still be claimed at this point
			final ScenarioInstance scenarioInstance = jobDescriptor.getJobContext();
			final ScenarioLock lock = scenarioInstance.getLock(jobDescriptor.getLockKey());
			lock.release();
		}

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
