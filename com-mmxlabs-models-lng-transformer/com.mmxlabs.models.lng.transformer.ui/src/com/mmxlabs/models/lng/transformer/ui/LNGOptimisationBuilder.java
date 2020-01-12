/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Module;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.adp.ADPScenarioModuleHelper;
import com.mmxlabs.models.lng.transformer.ui.strategic.StrategicScenarioModuleHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@NonNullByDefault
public class LNGOptimisationBuilder {

	public static LNGOptimisationBuilder begin(final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance) {
		return new LNGOptimisationBuilder(scenarioDataProvider, scenarioInstance);
	}

	public static LNGOptimisationBuilder begin(final IScenarioDataProvider scenarioDataProvider) {
		return new LNGOptimisationBuilder(scenarioDataProvider, null);
	}

	private final IScenarioDataProvider scenarioDataProvider;
	private final @Nullable ScenarioInstance scenarioInstance;
	private @Nullable ExtraDataProvider extraDataProvider;

	private @Nullable Consumer<OptimisationPlan> optimisationPlanCustomiser;
	private @Nullable Consumer<UserSettings> userSettingsCustomiser;
	private @Nullable IOptimiserInjectorService optimiserInjectorService;
	private @Nullable Module extraModule;
	private @Nullable Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory;
	private @Nullable IRunnerHook runnerHook;
	private @Nullable UserSettings userSettings;
	private @Nullable OptimisationPlan optimisationPlan;
	private @Nullable Integer threadCount;
	private boolean optimiseHint = false;
	private final List<String> extraHints = new LinkedList<>();
	private boolean includeDefaultExportStage = true;

	private LNGOptimisationBuilder(final IScenarioDataProvider scenarioDataProvider, @Nullable final ScenarioInstance scenarioInstance) {
		this.scenarioDataProvider = scenarioDataProvider;
		this.scenarioInstance = scenarioInstance;
	}

	public LNGOptimisationBuilder withExtraDataProvider(@Nullable ExtraDataProvider extraDataProvider) {
		this.extraDataProvider = extraDataProvider;
		return this;
	}

	public LNGOptimisationBuilder withHints(final String... hints) {
		for (final String hint : hints) {
			this.extraHints.add(hint);
		}
		return this;
	}

	public LNGOptimisationBuilder withRunnerHookFactory(final @Nullable Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory) {
		this.runnerHookFactory = runnerHookFactory;
		return this;
	}

	public LNGOptimisationBuilder withRunnerHook(final @Nullable IRunnerHook runnerHook) {
		this.runnerHook = runnerHook;
		return this;
	}

	public LNGOptimisationBuilder withOptimiserInjectorService(final @Nullable IOptimiserInjectorService optimiserInjectorService) {
		this.optimiserInjectorService = optimiserInjectorService;
		return this;
	}

	public LNGOptimisationBuilder withOptimisationPlanCustomiser(final @Nullable Consumer<OptimisationPlan> optimisationPlanCustomiser) {
		this.optimisationPlanCustomiser = optimisationPlanCustomiser;
		return this;
	}

	public LNGOptimisationBuilder withUserSettingsCustomiser(final @Nullable Consumer<UserSettings> userSettingsCustomiser) {
		this.userSettingsCustomiser = userSettingsCustomiser;
		return this;
	}

	public LNGOptimisationBuilder withUserSettings(final @Nullable UserSettings userSettings) {
		this.userSettings = userSettings;
		return this;
	}

	public LNGOptimisationBuilder withExtraModule(final @Nullable Module extraModule) {
		this.extraModule = extraModule;
		return this;
	}

	public LNGOptimisationBuilder withOptimisationPlan(final @Nullable OptimisationPlan optimisationPlan) {
		this.optimisationPlan = optimisationPlan;
		return this;
	}

	/**
	 * Add the {@link LNGTransformerHelper#HINT_OPTIMISE_LSO} hint and sets
	 * evaluationOnly to false.
	 * 
	 * @return
	 */
	public LNGOptimisationBuilder withOptimiseHint() {
		this.optimiseHint = true;
		return this;
	}

	/**
	 * Flag indicating whether to run the default export stages or just return the
	 * {@link IMultiStateResult} without further processing.
	 * 
	 * @return
	 */
	public LNGOptimisationBuilder withIncludeDefaultExportStage(boolean enabled) {
		this.includeDefaultExportStage = enabled;
		return this;
	}

	public LNGOptimisationBuilder withThreadCount(final int threadCount) {
		this.threadCount = Integer.valueOf(threadCount);
		return this;
	}

	public LNGOptimisationRunnerBuilder buildDefaultRunner() {

		@NonNull
		OptimisationPlan localOptimisationPlan;
		if (this.optimisationPlan != null) {
			localOptimisationPlan = optimisationPlan;
		} else {
			final @NonNull UserSettings localUserSettings = buildUserSettings();

			final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			localOptimisationPlan = OptimisationHelper.transformUserSettings(localUserSettings, null, lngScenarioModel);
			localOptimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(localOptimisationPlan);

			if (optimisationPlanCustomiser != null) {
				optimisationPlanCustomiser.accept(localOptimisationPlan);
			}
		}

		final CleanableExecutorService executorService = createExecutorService();

		try {
			final String[] hints;
			final boolean evaluationOnly = !optimiseHint;
			final List<String> listHints = new LinkedList<>(extraHints);
			if (optimiseHint) {
				listHints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);
			}
			hints = listHints.toArray(new String[listHints.size()]);

			if (localOptimisationPlan.getUserSettings().getMode() == OptimisationMode.ADP) {
				final ADPModel adpModel = ScenarioModelUtil.getADPModel(scenarioDataProvider);
				if (adpModel == null) {
					throw new IllegalStateException("No ADP Model for ADP optimisation");
				}
				final OptimiserInjectorServiceMaker serviceMaker = OptimiserInjectorServiceMaker.begin()//
						.withModuleBindInstance(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPModel.class, adpModel)//
						.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, ADPScenarioModuleHelper.createExtraDataModule(adpModel))//
				;
				if (!evaluationOnly && localOptimisationPlan.getUserSettings().isCleanSlateOptimisation()) {
					serviceMaker.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_InitialSolution, ADPScenarioModuleHelper.createEmptySolutionModule());
				}
				// FIXME: This replaces any local overrides!
				optimiserInjectorService = serviceMaker.make(optimiserInjectorService);
			} else if (localOptimisationPlan.getUserSettings().getMode() == OptimisationMode.STRATEGIC) {
				final OptimiserInjectorServiceMaker serviceMaker = OptimiserInjectorServiceMaker.begin()//
						.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, StrategicScenarioModuleHelper.createExtraDataModule())//
				;
				
				// IS this needed?
//				serviceMaker.withModuleOverride(IOptimiserInjectorService.ModuleType.Module_InitialSolution, StrategicScenarioModuleHelper.createEmptySolutionModule());

				optimiserInjectorService = serviceMaker.make(optimiserInjectorService);
			}

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = buildStandardBridge(localOptimisationPlan, evaluationOnly, executorService.getNumThreads(), hints);

			final IChainRunner chainRunner = buildStandardChain(scenarioToOptimiserBridge, localOptimisationPlan, includeDefaultExportStage, executorService, hints);

			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, scenarioDataProvider, scenarioInstance, scenarioToOptimiserBridge, chainRunner, null);

			return new LNGOptimisationRunnerBuilder(this, scenarioRunner);
		} catch (final Exception e) {
			executorService.shutdownNow();
			throw e;
		}
	}

	private CleanableExecutorService createExecutorService() {
		final CleanableExecutorService executorService;
		if (threadCount != null) {
			executorService = LNGScenarioChainBuilder.createExecutorService(threadCount);
		} else {
			executorService = LNGScenarioChainBuilder.createExecutorService();
		}
		return executorService;
	}

	private UserSettings buildUserSettings() {
		final @NonNull UserSettings localUserSettings;
		if (this.userSettings == null) {
			localUserSettings = ScenarioUtils.createDefaultUserSettings();
		} else {
			localUserSettings = EcoreUtil.copy(this.userSettings);
		}
		if (userSettingsCustomiser != null) {
			userSettingsCustomiser.accept(localUserSettings);
		}
		return localUserSettings;
	}

	public LNGScenarioToOptimiserBridge buildStandardBridge(final OptimisationPlan optimisationPlan, final boolean evaluationOnly, int concurrencyLevel, final String... initialHints) {

		return new LNGScenarioToOptimiserBridge(scenarioDataProvider, scenarioInstance, extraDataProvider, optimisationPlan.getUserSettings(), optimisationPlan.getSolutionBuilderSettings(),
				scenarioDataProvider.getEditingDomain(), concurrencyLevel, extraModule, optimiserInjectorService, evaluationOnly, initialHints);
	}

	public IChainRunner buildStandardChain(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final OptimisationPlan optimisationPlan, boolean includeDefaultExportStage,
			final CleanableExecutorService executorService, final String... initialHints) {
		return LNGScenarioChainBuilder.createStandardOptimisationChain(optimisationPlan.getResultName(), scenarioToOptimiserBridge.getDataTransformer(), scenarioToOptimiserBridge, optimisationPlan,
				executorService, includeDefaultExportStage, initialHints);
	}

	public static class LNGOptimisationRunnerBuilder implements AutoCloseable {
		private final LNGOptimisationBuilder builder;
		private final LNGScenarioRunner scenarioRunner;

		private LNGOptimisationRunnerBuilder(final LNGOptimisationBuilder builder, final LNGScenarioRunner scenarioRunner) {
			this.builder = builder;
			this.scenarioRunner = scenarioRunner;

			init();
		}

		private void init() {

			if (builder.runnerHook != null) {
				scenarioRunner.setRunnerHook(builder.runnerHook);
			} else if (builder.runnerHookFactory != null) {
				final IRunnerHook runnerHook = builder.runnerHookFactory.apply(scenarioRunner);
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
			}

		}

		public void run(final boolean runOptimisation) {
			run(runOptimisation, null);
		}

		public void run(final boolean runOptimisation, @Nullable final Consumer<LNGScenarioRunner> resultChecker) {
			final CleanableExecutorService executorService = scenarioRunner.getExecutorService();
			try {

				if (runOptimisation) {
					scenarioRunner.runAndApplyBest();
				}
				if (resultChecker != null) {
					resultChecker.accept(scenarioRunner);
				}
			} finally {
				executorService.shutdownNow();
			}
		}

		/**
		 * Run the optimisation and return the exported Schedule
		 */
		public Schedule runAndReturnSchedule() {
			final CleanableExecutorService executorService = scenarioRunner.getExecutorService();
			try {

				IMultiStateResult result = scenarioRunner.runWithProgress(new NullProgressMonitor());
				if (result != null) {
					return scenarioRunner.getScenarioToOptimiserBridge().createSchedule(result.getBestSolution().getFirst(), result.getBestSolution().getSecond());
				}
			} finally {
				executorService.shutdownNow();
			}
			return null;
		}

		@Override
		public void close() throws Exception {
			dispose();
		}

		public void dispose() {
			scenarioRunner.getExecutorService().shutdownNow();
		}

		public LNGScenarioRunner getScenarioRunner() {
			return scenarioRunner;
		}

		public LNGOptimisationRunnerBuilder evaluateInitialState() {
			scenarioRunner.evaluateInitialState();
			return this;
		}
	}

	public static void quickEvaluation(final IScenarioDataProvider scenarioDataProvider, final UserSettings userSettings, final @Nullable Module extraModule) {

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		final LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withExtraModule(extraModule) //
				.withUserSettings(userSettings) //
				.withOptimisationPlan(optimisationPlan) //
				.buildDefaultRunner();

		try {
			runner.evaluateInitialState();
		} finally {
			runner.dispose();
		}
	}

	public static void quickEvaluation(final IScenarioDataProvider scenarioDataProvider, final OptimisationPlan optimisationPlan, final @Nullable Module extraModule) {

		final LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withExtraModule(extraModule) //
				.withOptimisationPlan(optimisationPlan) //
				.buildDefaultRunner();

		try {
			runner.evaluateInitialState();
		} finally {
			runner.dispose();
		}
	}

	public static void quickEvaluationWithUpdateSettings(final IScenarioDataProvider scenarioDataProvider) {
		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

		final OptimisationPlan p = OptimisationHelper.getOptimiserSettings(lngScenarioModel, true, null, false, false, null);

		if (p == null) {
			return;
		}
		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();
		final CompoundCommand cmd = new CompoundCommand("Update settings");
		cmd.append(SetCommand.create(editingDomain, lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(), EcoreUtil.copy(p.getUserSettings())));
		RunnerHelper.syncExecDisplayOptional(() -> {
			editingDomain.getCommandStack().execute(cmd);
		});
		scenarioDataProvider.setLastEvaluationFailed(true);
		final LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(p) //
				.buildDefaultRunner();

		try {
			scenarioDataProvider.setLastEvaluationFailed(true);
			runner.evaluateInitialState();
			scenarioDataProvider.setLastEvaluationFailed(false);
		} finally {
			runner.dispose();
		}
	}
}