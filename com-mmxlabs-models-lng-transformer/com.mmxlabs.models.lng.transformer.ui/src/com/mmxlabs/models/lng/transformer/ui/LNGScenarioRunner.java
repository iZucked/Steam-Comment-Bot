/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.management.timer.Timer;

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
	private Schedule initialSchedule;

	@Nullable
	private Schedule finalSchedule;

	@NonNull
	private LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	@NonNull
	private LNGScenarioModel scenarioModel;

	@Nullable
	private ScenarioInstance scenarioInstance;

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, final String... initialHints) {
		this(exectorService, scenario, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @NonNull final OptimiserSettings optimiserSettings, @Nullable Module extraModule,
			final String... initialHints) {
		this(exectorService, scenarioModel, null, optimiserSettings, LNGSchedulerJobUtils.createLocalEditingDomain(), extraModule, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService exectorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, final String... initialHints) {
		this(exectorService, scenarioModel, scenarioInstance, optimiserSettings, editingDomain, null, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull ExecutorService executorService, @NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final EditingDomain editingDomain, @Nullable final Module extraModule,
			@Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {

		this.scenarioModel = scenarioModel;
		this.scenarioInstance = scenarioInstance;

		// here we want to take user settings and generate initial state settings
		scenarioToOptimiserBridge = new LNGScenarioToOptimiserBridge(scenarioModel, scenarioInstance, optimiserSettings, editingDomain, extraModule, localOverrides,
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		// FB: 1712 Switch for enabling run-all similarity optimisation. Needs better UI hook ups.

		if (false) {
			chainRunner = LNGScenarioChainBuilder.createRunAllSimilarityOptimisationChain(scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimiserSettings, executorService,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		} else {
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
		initialSchedule = scenarioToOptimiserBridge.overwrite(0, startRawSequences, extraAnnotations);

		// initialSchedule= ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule();

		// need to undo this.chainRunner..
		return initialSchedule;
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
		if (initialSchedule == null) {
			evaluateInitialState();
		}

		final IMultiStateResult result = chainRunner.run(progressMonitor);

		finalSchedule = scenarioToOptimiserBridge.overwrite(100, result.getBestSolution().getFirst(), result.getBestSolution().getSecond());
		log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));

		return result;
	}

	// TODO: There should only be one schedule, not initial and final. Not sure what state the initial schedule will be in.
	@Nullable
	public final Schedule getFinalSchedule() {
		return finalSchedule;
	}

	@Nullable
	public final Schedule getIntialSchedule() {
		return initialSchedule;
	}

	@NonNull
	public LNGScenarioModel getScenario() {
		return scenarioModel;
	}

	@Nullable
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}
}
