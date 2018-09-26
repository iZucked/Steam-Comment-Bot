/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Collection;
import java.util.HashMap;
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
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.jobmanager.eclipse.jobs.impl.AbstractEclipseJobControl;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.ui.adp.ADPScenarioModuleHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

public class LNGScenarioRunner {

	@NonNull
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioRunner.class);

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

	private final @NonNull CleanableExecutorService executorService;

	public static @NonNull LNGScenarioRunner make(@NonNull final CleanableExecutorService exectorService, @NonNull final IScenarioDataProvider scenarioDataProvider,
			@NonNull final OptimisationPlan optimisationPlan, @Nullable final IRunnerHook runnerHook, final boolean evaluationOnly, final String... initialHints) {
		return make(exectorService, scenarioDataProvider, null, optimisationPlan, scenarioDataProvider.getEditingDomain(), runnerHook, evaluationOnly, initialHints);

	}

	public static @NonNull LNGScenarioRunner make(@NonNull final CleanableExecutorService exectorService, @NonNull final IScenarioDataProvider scenarioDataProvider,
			@NonNull final OptimisationPlan optimisationPlan, @Nullable final Module extraModule, @Nullable final IRunnerHook runnerHook, final boolean evaluationOnly, final String... initialHints) {
		return make(exectorService, scenarioDataProvider, null, optimisationPlan, scenarioDataProvider.getEditingDomain(), extraModule, null, runnerHook, evaluationOnly, initialHints);
	}

	public static @NonNull LNGScenarioRunner make(@NonNull final CleanableExecutorService exectorService, @NonNull final IScenarioDataProvider scenarioDataProvider,
			@Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimisationPlan optimisationPlan, @NonNull final EditingDomain editingDomain, @Nullable final IRunnerHook runnerHook,
			final boolean evaluationOnly, final String... initialHints) {
		return make(exectorService, scenarioDataProvider, scenarioInstance, optimisationPlan, editingDomain, null, null, runnerHook, evaluationOnly, initialHints);
	}

	public static @NonNull LNGScenarioRunner make(@NonNull final CleanableExecutorService executorService, @NonNull final IScenarioDataProvider scenarioDataProvider,
			@Nullable final ScenarioInstance scenarioInstance, @NonNull final OptimisationPlan optimisationPlan, @NonNull final EditingDomain editingDomain, @Nullable final Module extraModule,
			@Nullable final IOptimiserInjectorService localOverrides, @Nullable final IRunnerHook runnerHook, final boolean evaluationOnly, final String... initialHints) {

		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;
		UserSettings userSettings = optimisationPlan.getUserSettings();
		if (userSettings.isAdpOptimisation()) {
			ADPModel adpModel = ScenarioModelUtil.getADPModel(scenarioDataProvider);
			if (adpModel == null) {
				throw new IllegalStateException("No ADP Model for ADP optimisation");
			}
			OptimiserInjectorServiceMaker serviceMaker = OptimiserInjectorServiceMaker.begin()//
					.withModuleBindInstance(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPModel.class, adpModel)//
					.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPScenarioModuleHelper.createExtraDataModule(adpModel))//
			;
			if (userSettings.isCleanStateOptimisation()) {
				serviceMaker.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_InitialSolution, ADPScenarioModuleHelper.createEmptySolutionModule());
			}
			scenarioToOptimiserBridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, //
					scenarioInstance, //
					userSettings, //
					optimisationPlan.getSolutionBuilderSettings(), //
					editingDomain, //
					extraModule, // Bootstrap module
					serviceMaker.make(), //
					evaluationOnly, true, //
					initialHints // Hints? No Caching?
			);
		} else {
			scenarioToOptimiserBridge = new LNGScenarioToOptimiserBridge(scenarioDataProvider, scenarioInstance, userSettings, optimisationPlan.getSolutionBuilderSettings(), editingDomain,
					extraModule, localOverrides, evaluationOnly, true, initialHints);
		}

		// Probably need to bring in the evaluation modules
		final Collection<IOptimiserInjectorService> services = scenarioToOptimiserBridge.getDataTransformer().getModuleServices();

		// here we want to take user settings and generate initial state settings
		final IChainRunner chainRunner = LNGScenarioChainBuilder.createStandardOptimisationChain(optimisationPlan.getResultName(), scenarioToOptimiserBridge.getDataTransformer(),
				scenarioToOptimiserBridge, optimisationPlan, executorService, initialHints);

		return new LNGScenarioRunner(executorService, scenarioDataProvider, scenarioInstance, scenarioToOptimiserBridge, chainRunner, runnerHook);

	}

	public LNGScenarioRunner(@NonNull final CleanableExecutorService executorService, //
			@NonNull final IScenarioDataProvider scenarioDataProvider, //
			@Nullable final ScenarioInstance scenarioInstance, //
			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, //
			@NonNull final IChainRunner chainRunner, //
			@Nullable final IRunnerHook runnerHook) {

		this.executorService = executorService;
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenarioInstance = scenarioInstance;
		this.scenarioToOptimiserBridge = scenarioToOptimiserBridge;
		this.chainRunner = chainRunner;

		setRunnerHook(runnerHook);
	}

	public void dispose() {

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
			v[0] = scenarioToOptimiserBridge.overwrite(0, startRawSequences, extraAnnotations);
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
		// assert createOptimiser;

		// TODO: Replace with originalScenario.getScheduleModel().getSchedule()
		if (schedule == null) {
			evaluateInitialState();
		}

		final IMultiStateResult result = chainRunner.run(progressMonitor);

		if (log.isDebugEnabled()) {
			log.debug(String.format("Job finished in %.2f minutes", (System.currentTimeMillis() - startTimeMillis) / (double) Timer.ONE_MINUTE));
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

	public CleanableExecutorService getExecutorService() {
		return executorService;
	}
}
