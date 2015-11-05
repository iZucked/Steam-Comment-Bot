/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Map;

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
	private LNGScenarioDataTransformer scenarioDataTransformer;

	@NonNull
	private LNGScenarioModel scenarioModel;

	@Nullable
	private ScenarioInstance scenarioInstance;

	public LNGScenarioRunner(@NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, final String... initialHints) {
		this(scenario, null, optimiserSettings, LNGScenarioRunnerUtils.createLocalEditingDomain(), initialHints);
	}

	public LNGScenarioRunner(@NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			final String... initialHints) {
		this(scenarioModel, scenarioInstance, optimiserSettings, LNGScenarioRunnerUtils.createLocalEditingDomain(), null, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull final LNGScenarioModel scenarioModel, @NonNull final OptimiserSettings optimiserSettings, @Nullable Module extraModule, final String... initialHints) {
		this(scenarioModel, null, optimiserSettings, LNGScenarioRunnerUtils.createLocalEditingDomain(), extraModule, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			final EditingDomain editingDomain, final String... initialHints) {
		this(scenarioModel, scenarioInstance, optimiserSettings, editingDomain, null, null, initialHints);
	}

	public LNGScenarioRunner(@NonNull final LNGScenarioModel scenarioModel, @Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimiserSettings optimiserSettings,
			@NonNull final EditingDomain editingDomain, @Nullable final Module extraModule, @Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {

		this.scenarioModel = scenarioModel;
		this.scenarioInstance = scenarioInstance;

		// here we want to take user settings and generate initial state settings
		scenarioDataTransformer = new LNGScenarioDataTransformer(scenarioModel, scenarioInstance, optimiserSettings, editingDomain, extraModule, localOverrides,
				LNGTransformerHelper.HINT_OPTIMISE_LSO);

		if (false) {
			chainRunner = LNGScenarioChainBuilder.createRunAllSimilarityOptimisationChain(scenarioDataTransformer.getDataTransformer(), scenarioDataTransformer, optimiserSettings,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		} else {
			chainRunner = LNGScenarioChainBuilder.createStandardOptimisationChain(null, scenarioDataTransformer.getDataTransformer(), scenarioDataTransformer, optimiserSettings,
					LNGTransformerHelper.HINT_OPTIMISE_LSO);
		}

	}

	/**
	 * Evaluates the initial state
	 */
	@Nullable
	public Schedule evaluateInitialState() {
		startTimeMillis = System.currentTimeMillis();

		final IMultiStateResult result = chainRunner.getInitialState();

		final ISequences startRawSequences = result.getBestSolution().getFirst();
		final Map<String, Object> extraAnnotations = scenarioDataTransformer.extractOptimisationAnnotations(result.getBestSolution().getSecond());
		initialSchedule = scenarioDataTransformer.exportSchedule(0, startRawSequences, extraAnnotations);

		return initialSchedule;
	}

	public void run() {
		runWithProgress(new NullProgressMonitor());
	}

	/**
	 * used by {@link AbstractEclipseJobControl} / {@link LNGSchedulerOptimiserJobControl}
	 * 
	 * @param progressMonitor
	 */
	public void runWithProgress(final @NonNull IProgressMonitor progressMonitor) {
		// assert createOptimiser;

		if (initialSchedule == null) {
			evaluateInitialState();
		}

		final IMultiStateResult p = chainRunner.run(progressMonitor);

		finalSchedule = scenarioDataTransformer.exportSchedule(100, p.getBestSolution().getFirst(), scenarioDataTransformer.extractOptimisationAnnotations(p.getBestSolution().getSecond()));
		log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
	}

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

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

}
