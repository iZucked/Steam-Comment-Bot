/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A simple {@link IJobControl} to evaluate a schedule.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGSchedulerEvaluationJobControl implements IJobControl {

	private final LinkedList<IJobControlListener> listeners = new LinkedList<>();

	private LNGSchedulerJobDescriptor jobDescriptor;

	private EJobState currentState = EJobState.UNKNOWN;

	public LNGSchedulerEvaluationJobControl(@NonNull final LNGSchedulerJobDescriptor descriptor) {
		this.jobDescriptor = descriptor;
	}

	@Override
	public @NonNull IJobDescriptor getJobDescriptor() {
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

		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		try (final ModelReference modelReference = scenarioInstance.getReference()) {
			final LNGScenarioModel scenario = (LNGScenarioModel) modelReference.getInstance();
			final EditingDomain editingDomain = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);

			final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, scenario, scenarioInstance, jobDescriptor.getOptimisationPlan(), editingDomain, null, true);
			try {
				runner.evaluateInitialState();
			} finally {
				// runner.dispose();
			}

			setJobState(EJobState.COMPLETED);
		} catch (final Throwable e) {
			setJobState(EJobState.CANCELLED);
			throw new RuntimeException(e);
		} finally {
			executorService.shutdownNow();
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
		// Method not actually used and it would require hanging on to the model reference longer...
		// return jobDescriptor.getJobContext().getInstance();
		return null;
	}

	@Override
	public void addListener(@NonNull final IJobControlListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(@NonNull final IJobControlListener listener) {
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
			final List<IJobControlListener> copy = new ArrayList<>(listeners);

			for (final IJobControlListener mjl : copy) {
				if (!mjl.jobStateChanged(this, oldState, newState)) {
					listeners.remove(mjl);
				}
			}
		}
	}
}
