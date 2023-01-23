/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGSharedDataTransformerModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.inject.modules.SequencesEvaluatorModule;
import com.mmxlabs.models.lng.transformer.inject.modules.SequencesEvaluatorModule.ISequenceEvaluator;
import com.mmxlabs.models.lng.transformer.shared.impl.SharedDataTransformerService;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.common.events.OptimisationLifecycleManager;
import com.mmxlabs.optimiser.common.events.OptimisationLifecycleModule;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.NotInjectedScopeModule;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * The {@link LNGDataTransformer} is the main entry point for the {@link ILNGStateTransformerUnit} and {@link IChainRunner} APIs.
 * 
 * @author Simon Goodall
 *
 */
public class LNGDataTransformer {

	private final @NonNull Collection<@NonNull IOptimiserInjectorService> services;

	private final @NonNull Collection<@NonNull String> hints;

	private final @NonNull Injector injector;

	private @Nullable IRunnerHook runnerHook;

	private final @NonNull UserSettings userSettings;

	private final @NonNull SolutionBuilderSettings solutionBuilderSettings;

	private IMultiStateResult solutionBuilderResult;

	@SuppressWarnings("null")
	public LNGDataTransformer(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable ExtraDataProvider extraDataProvider, @NonNull final UserSettings userSettings,
			@NonNull final SolutionBuilderSettings solutionBuilderSettings, int concurrencyLevel, @NonNull final Collection<@NonNull String> hints,
			@NonNull final Collection<@NonNull IOptimiserInjectorService> services) {

		this.userSettings = userSettings;
		this.solutionBuilderSettings = solutionBuilderSettings;
		this.hints = hints;
		this.services = services;

		final List<@NonNull Module> modules = new LinkedList<>();

		// Prepare the main modules with the re-usable data for any further work.
		modules.add(new ThreadLocalScopeModule());
		modules.add(new NotInjectedScopeModule());
		modules.add(new OptimisationLifecycleModule());
		modules.add(new LNGSharedDataTransformerModule(scenarioDataProvider, new SharedDataTransformerService()));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new DataComponentProviderModule(), services, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule, hints));

		List<Module> transformerModules = new ArrayList<>(2);
		transformerModules.add(new LNGTransformerModule(scenarioDataProvider, userSettings, concurrencyLevel, hints));
		if (extraDataProvider != null) {
			transformerModules.add(extraDataProvider.asModule());
		} else {
			transformerModules.add(ExtraDataProvider.createDefaultModule());
		}
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(transformerModules, services, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, hints));

		final Injector parentInjector = Guice.createInjector(modules);

		// Create temporary child injector to compute the initial solution and then pass result back into the main injector (as new child). This avoid polluting the injector with evaluation state.
		{
			final List<Module> modules2 = new LinkedList<>();
			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, solutionBuilderSettings.getConstraintAndFitnessSettings()), services,
					IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));

			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGInitialSequencesModule(), services, IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));
			modules2.add(new InitialPhaseOptimisationDataModule());
			final Injector initialSolutionInjector = parentInjector.createChildInjector(modules2);
			final ThreadLocalScopeImpl scope = initialSolutionInjector.getInstance(ThreadLocalScopeImpl.class);
			try {
				scope.enter();
				solutionBuilderResult = initialSolutionInjector.getInstance(Key.get(IMultiStateResult.class, Names.named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)));
				// Create a new child injector from the parent (i.e. without the modules2 list) with the initial sequences added
				injector = parentInjector.createChildInjector(new SequenceBuilderSequencesModule(solutionBuilderResult.getBestSolution().getFirst()));
			} finally {
				scope.exit();
			}
		}
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@NonNull
	public Collection<@NonNull IOptimiserInjectorService> getModuleServices() {
		return services;
	}

	@NonNull
	public UserSettings getUserSettings() {
		return userSettings;
	}

	@NonNull
	public ModelEntityMap getModelEntityMap() {
		return injector.getInstance(ModelEntityMap.class);
	}

	@NonNull
	public IOptimisationData getOptimisationData() {
		return injector.getInstance(IOptimisationData.class);
	}

	@NonNull
	public Collection<@NonNull String> getHints() {
		return hints;
	}

	@NonNull
	public ISequences getInitialSequences() {
		return injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_SEQUENCE_BUILDER)));
	}

	@Nullable
	public IRunnerHook getRunnerHook() {
		return runnerHook;
	}

	public void setRunnerHook(@Nullable final IRunnerHook runnerHook) {
		this.runnerHook = runnerHook;
	}

	public SolutionBuilderSettings getSolutionBuilderSettings() {
		return solutionBuilderSettings;
	}

	public @NonNull IMultiStateResult getInitialResult() {
		return solutionBuilderResult;
	}

	/**
	 * Used to evaluate the given raw sequences against the fitness components defined in the initial sequence builder.
	 * 
	 * Intended for use in ITS cases
	 * 
	 * @param rawSequences
	 * @return
	 */
	public IMultiStateResult evalWithFitness(final ISequences rawSequences) {
		final List<Module> modules2 = new LinkedList<>();
		modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(userSettings, solutionBuilderSettings.getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules2.add(new InitialPhaseOptimisationDataModule());
		modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new SequencesEvaluatorModule(), services, IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));
		final Injector initialSolutionInjector = injector.createChildInjector(modules2);

		final ISequenceEvaluator evaluator = initialSolutionInjector.getInstance(SequencesEvaluatorModule.ISequenceEvaluator.class);
		return evaluator.eval(rawSequences);
	}

	public OptimisationLifecycleManager getLifecyleManager() {
		return injector.getInstance(OptimisationLifecycleManager.class);
	}
}
