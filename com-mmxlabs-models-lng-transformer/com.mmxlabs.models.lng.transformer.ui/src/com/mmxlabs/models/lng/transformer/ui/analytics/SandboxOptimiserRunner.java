/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class SandboxOptimiserRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(LNGSchedulerInsertSlotJobRunner.class);

	private final IScenarioDataProvider originalScenarioDataProvider;

	private final LNGScenarioRunner scenarioRunner;

	private final EditingDomain originalEditingDomain;

	private static final String[] hint_with_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			LNGTransformerHelper.HINT_DISABLE_CACHES, //
			LNGTransformerHelper.HINT_KEEP_NOMINALS_IN_PROMPT, //
			LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN };

	private static final String[] hint_without_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			//LNGTransformerHelper.HINT_KEEP_NOMINALS_IN_PROMPT 
			};

	private final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private final LNGDataTransformer dataTransformer;

	private final UserSettings userSettings;

	private OptimisationPlan plan;

	public SandboxOptimiserRunner(@Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider, final EditingDomain editingDomain,
			final UserSettings userSettings, @Nullable ExtraDataProvider extraDataProvider, @Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider) {

		this.originalScenarioDataProvider = scenarioDataProvider;
		this.originalEditingDomain = editingDomain;
		this.userSettings = userSettings;

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		plan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		plan = LNGScenarioRunnerUtils.createExtendedSettings(plan);
		final IOptimiserInjectorService extraService = buildExtraModules(extraDataProvider, initialSolutionProvider);

		// TODO: Only disable caches if we do a break-even (caches *should* be ok otherwise?)
		final String[] hints = hint_without_breakeven;
		final LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(originalScenarioDataProvider, scenarioInstance) //
				.withOptimisationPlan(plan) //
				.withExtraDataProvider(extraDataProvider) //
				.withOptimiserInjectorService(extraService) //
				.withOptimiseHint() //
				.withHints(hints) //
				.withIncludeDefaultExportStage(false) //
				.buildDefaultRunner();

		scenarioRunner = runner.getScenarioRunner();

		scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
	}

	private IOptimiserInjectorService buildExtraModules(ExtraDataProvider extraDataProvider, @Nullable TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider) {
		return new IOptimiserInjectorService() {

			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

				if (moduleType == ModuleType.Module_InitialSolution) {
					if (!hints.contains(LNGTransformerHelper.HINT_PERIOD_SCENARIO) && initialSolutionProvider != null) {
						return Collections.singletonList(new PrivateModule() {

							@Override
							protected void configure() {
								// Nothing to do here - see provides methods
							}

							@Provides
							@Singleton
							@Named("EXTERNAL_SOLUTION")
							private ISequences provideSequences(final Injector injector, ModelEntityMap mem, IOptimisationData data) {
								ISequences sequences = initialSolutionProvider.apply(mem, data, injector);
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)
							@Exposed
							private ISequences provideInitialSequences(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
							@Exposed
							private IMultiStateResult provideSolutionPair(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {

								return new MultiStateResult(sequences, new HashMap<>());
							}

						});
					}
				}

				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					List<Module> modules = new LinkedList<>();
					return modules;
				}
				return null;
			}

		};
	}

	public void prepare() {
		scenarioRunner.evaluateInitialState();
	}

	 

	public IMultiStateResult runOptimiser(final IProgressMonitor progressMonitor) {
		return scenarioRunner.runWithProgress(progressMonitor);
	}

	 

	public LNGScenarioRunner getLNGScenarioRunner() {
		return scenarioRunner;
	}

	public void dispose() {
		if (scenarioRunner != null) {
			scenarioRunner.getExecutorService().shutdownNow();
		}
	}
}
