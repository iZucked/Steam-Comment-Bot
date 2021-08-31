/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * A simple {@link IJobControl} to evaluate a schedule.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGSchedulerEvaluationJobControl implements IJobControl {

	private final LinkedList<IJobControlListener> listeners = new LinkedList<>();

	private @NonNull LNGSchedulerJobDescriptor jobDescriptor;

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

		final @NonNull ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		try (final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("LNGSchedulerEvaluationJobControl")) {

			scenarioDataProvider.setLastEvaluationFailed(true);
			LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider, scenarioInstance) //
					.withThreadCount(1) //
					.withOptimisationPlan(jobDescriptor.getOptimisationPlan()) //
					// Hack: Add on shipping only hint to avoid generating spot markets during eval.
					.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY, LNGTransformerHelper.HINT_EVALUATION_ONLY) //
					.buildDefaultRunner();
			try {
				runner.evaluateInitialState();
				scenarioDataProvider.setLastEvaluationFailed(false);
			} finally {
				runner.dispose();
			}

			setJobState(EJobState.COMPLETED);
		} catch (final Throwable e) {
			setJobState(EJobState.CANCELLED);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancel() {
		if (getJobState() != EJobState.COMPLETED) {
			setJobState(EJobState.CANCELLED);
		}

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
