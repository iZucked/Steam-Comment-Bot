/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunner {

	@SuppressWarnings("null")
	@NonNull
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioRunner.class);

	@NonNull
	private final IChainRunner chainRunner;

	private long startTimeMillis;

	@Nullable
	private Schedule schedule;

	@NonNull
	private LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	public LNGScenarioToOptimiserBridge getScenarioToOptimiserBridge() {
		return scenarioToOptimiserBridge;
	}

	@NonNull
	private LNGScenarioModel scenarioModel;

	@Nullable
	private ScenarioInstance scenarioInstance;

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings,
			@Nullable final IRunnerHook runnerHook, final String... initialHints) {
		this(exectorService, scenario, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), runnerHook, initialHints);

	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @NonNull final OptimiserSettings optimiserSettings, @Nullable Module extraModule,
			@Nullable final IRunnerHook runnerHook, final String... initialHints) {
		this(exectorService, scenarioModel, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), extraModule, null, runnerHook, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, @Nullable final IRunnerHook runnerHook, final String... initialHints) {
		this(exectorService, scenarioModel, scenarioInstance, optimiserSettings, editingDomain, null, null, runnerHook, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService executorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, @Nullable final Module extraModule,
			@Nullable final IOptimiserInjectorService localOverrides, @Nullable final IRunnerHook runnerHook, final String... initialHints) {

		this.scenarioModel = scenarioModel;
		this.scenarioInstance = scenarioInstance;

		// TODO: initial hints should specify LSO!

		// here we want to take user settings and generate initial state settings
		scenarioToOptimiserBridge = new LNGScenarioToOptimiserBridge(scenarioModel, scenarioInstance, optimiserSettings, editingDomain, extraModule, localOverrides,
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		setRunnerHook(runnerHook);

		// FB: 1712 Switch for enabling run-all similarity optimisation. Needs better UI hook ups.
		if (false) {
			chainRunner = LNGScenarioChainBuilder.createRunAllSimilarityOptimisationChain(scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings, executorService,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		} else {
			// chainRunner = LNGScenarioChainBuilder.createStandardOptimisationChain(null, scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings,
			// executorService,
			// LNGTransformerHelper.HINT_OPTIMISE_LSO);
			chainRunner = LNGScenarioChainBuilder.createStandardOptimisationChain(null, scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings, executorService,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		}

	}

	public void dispose() {

	}

	/**
	 * Evaluates the initial state
	 */
	@Nullable
	public Schedule evaluateInitialState() {
		startTimeMillis = System.currentTimeMillis();

		// TODO: The API would be *much* cleaner if we did not need to return the annotated solution to get the fitness trace. This would avoid the need here to re-calculate after doing an initial
		// export in the constructor
		// TODO: It is also pretty keyed to the first run LSO state and not any other stage in the process.
		// TODO: Fitness traces needed for ITS run. Additional data also needed for headless app runs - e.g. move analysis logger.
		final IMultiStateResult result = chainRunner.getInitialState();

		final ISequences startRawSequences = result.getBestSolution().getFirst();
		final Map<String, Object> extraAnnotations = result.getBestSolution().getSecond();
		schedule = scenarioToOptimiserBridge.overwrite(0, startRawSequences, extraAnnotations);

		// initialSchedule= ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule();

		// need to undo this.chainRunner..
		return schedule;
	}

	public IMultiStateResult run() {
		return runWithProgress(new NullProgressMonitor());
	}

	/**
	 * used by {@link AbstractEclipseJobControl} / {@link LNGSchedulerOptimiserJobControl}
	 * 
	 * @param progressMonitor
	 */

	@NonNull
	public IMultiStateResult runWithProgress(final @NonNull IProgressMonitor progressMonitor) {
		// assert createOptimiser;

		// TODO: Replace with originalScenario.getScheduleModel().getSchedule()
		if (schedule == null) {
			evaluateInitialState();
		}

		final IMultiStateResult result = chainRunner.run(progressMonitor);

		schedule = scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());
		log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));

		return result;
	}

	@Nullable
	public final Schedule getSchedule() {
		return schedule;
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return scenarioModel;
	}

	@Nullable
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;

	}

	public IRunnerHook getRunnerHook() {
		return scenarioToOptimiserBridge.getDataTransformer().getRunnerHook();
	}

	private void setRunnerHook(final @Nullable IRunnerHook runnerHook) {

		scenarioToOptimiserBridge.getDataTransformer().setRunnerHook(runnerHook);
	}

}
