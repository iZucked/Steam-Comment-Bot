/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

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
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * The {@link LNGDataTransformer} is the main entry point for the {@link ILNGStateTransformerUnit} and {@link IChainRunner} APIs.
 * 
 * @author Simon Goodall
 *
 */
public class LNGDataTransformer {

	@NonNull
	private final Collection<@NonNull IOptimiserInjectorService> services;

	@NonNull
	private final Collection<@NonNull String> hints;

	@NonNull
	private final Injector injector;

	private final OptimiserSettings settings;

	@Nullable
	private IRunnerHook runnerHook;

	@SuppressWarnings("null")
	public LNGDataTransformer(@NonNull final LNGScenarioModel scenarioModel, @NonNull final OptimiserSettings settings, @NonNull final Collection<@NonNull String> hints,
			@NonNull final Collection<@NonNull IOptimiserInjectorService> services) {

		this.settings = settings;
		this.hints = hints;
		this.services = services;

		final List<@NonNull Module> modules = new LinkedList<>();

		// Prepare the main modules with the re-usable data for any further work.
		modules.add(new PerChainUnitScopeModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new DataComponentProviderModule(), services, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGTransformerModule(scenarioModel, hints), services, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, hints));

		final Injector parentInjector = Guice.createInjector(modules);

		// Create temporary child injector to compute the initial solution and then pass result back into the main injector (as new child). This avoid polluting the injector with evaluation state.
		{
			final List<Module> modules2 = new LinkedList<>();
			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(settings), services,
					IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			modules2.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGInitialSequencesModule(), services, IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));

			final Injector initialSolutionInjector = parentInjector.createChildInjector(modules2);
			final PerChainUnitScopeImpl scope = initialSolutionInjector.getInstance(PerChainUnitScopeImpl.class);
			try {
				scope.enter();
				final ISequences initialSequences = initialSolutionInjector.getInstance(Key.get(ISequences.class, Names.named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)));
				// Create a new child injector from the parent (i.e. without the modules2 list) with the initial sequences added
				injector = parentInjector.createChildInjector(new InitialSequencesModule(initialSequences));
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

	//
	// @SuppressWarnings("null")
	// @NonNull
	// public LNGScenarioModel getScenarioModel() {
	// return injector.getInstance(LNGScenarioModel.class);
	// }
	//
	@SuppressWarnings("null")
	@NonNull
	public OptimiserSettings getOptimiserSettings() {
		return settings;
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
		return injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)));
	}

	@Nullable
	public IRunnerHook getRunnerHook() {
		return runnerHook;
	}

	public void setRunnerHook(@Nullable IRunnerHook runnerHook) {
		this.runnerHook = runnerHook;
	}
}
