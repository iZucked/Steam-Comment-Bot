/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManager;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerContainer;

public class LNGScenarioRunner {

	@NonNull
	private static final Logger LOG = LoggerFactory.getLogger(LNGScenarioRunner.class);

	@NonNull
	private final IChainRunner chainRunner;

	private long startTimeMillis;

	@Nullable
	private Schedule schedule;

	@NonNull
	private final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	public @NonNull LNGScenarioToOptimiserBridge getScenarioToOptimiserBridge() {
		return scenarioToOptimiserBridge;
	}

	@NonNull
	private final IScenarioDataProvider scenarioDataProvider;

	@Nullable
	private final ScenarioInstance scenarioInstance;

	private final @NonNull JobExecutorFactory jobExecutorFactory;

	public LNGScenarioRunner(@NonNull final JobExecutorFactory executorService, //
			@NonNull final IScenarioDataProvider scenarioDataProvider, //
			@Nullable final ScenarioInstance scenarioInstance, //
			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, //
			@NonNull final IChainRunner chainRunner, //
			@Nullable final IRunnerHook runnerHook) {

		this.jobExecutorFactory = executorService;
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenarioInstance = scenarioInstance;
		this.scenarioToOptimiserBridge = scenarioToOptimiserBridge;
		this.chainRunner = chainRunner;

		setRunnerHook(runnerHook);
	}

	/**
	 * Evaluates the initial state
	 */
	@Nullable
	public Schedule evaluateInitialState() {
		startTimeMillis = System.currentTimeMillis();

		final IMultiStateResult result = chainRunner.getInitialState();

		final ISequences startRawSequences = result.getBestSolution().getFirst();
		final Map<String, Object> extraAnnotations = result.getBestSolution().getSecond();
		final Schedule[] v = new Schedule[1];
		RunnerHelper.syncExecDisplayOptional(() -> {
			final Injector injector = scenarioToOptimiserBridge.getInjector();
			final ILazyExpressionManagerContainer container = injector.getInstance(ILazyExpressionManagerContainer.class);
			try (ILazyExpressionManager manager = container.getExpressionManager()) {
				manager.initialiseAllPricingData();
				v[0] = scenarioToOptimiserBridge.overwrite(0, startRawSequences, extraAnnotations);
			} catch (Exception e) {
				
			}
		});
		schedule = v[0];
		// need to undo this.chainRunner..
		return schedule;
	}

	public IMultiStateResult runAndApplyBest() {
		final IMultiStateResult result = runWithProgress(new NullProgressMonitor());
		final NonNullPair<ISequences, Map<String, Object>> bestSolution = result.getBestSolution();
		RunnerHelper.syncExecDisplayOptional(() -> {
			this.schedule = getScenarioToOptimiserBridge().overwrite(100, bestSolution.getFirst(), bestSolution.getSecond());
		});

		return result;
	}

	/**
	 * used by {@link AbstractEclipseJobControl} / {@link LNGSchedulerOptimiserJobControl}
	 * 
	 * @param progressMonitor
	 */

	@NonNull
	public IMultiStateResult runWithProgress(final @NonNull IProgressMonitor progressMonitor) {

		final IMultiStateResult result = chainRunner.run(progressMonitor);
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
		}
		return result;
	}

	@Nullable
	public final Schedule getSchedule() {
		return schedule;
	}

	@NonNull
	public IScenarioDataProvider getScenarioDataProvider() {
		return scenarioDataProvider;
	}

	@Nullable
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public IRunnerHook getRunnerHook() {
		return scenarioToOptimiserBridge.getDataTransformer().getRunnerHook();
	}

	public void setRunnerHook(final @Nullable IRunnerHook runnerHook) {
		scenarioToOptimiserBridge.getDataTransformer().setRunnerHook(runnerHook);
	}

	public JobExecutorFactory getJobExecutorFactory() {
		return jobExecutorFactory;
	}
}
