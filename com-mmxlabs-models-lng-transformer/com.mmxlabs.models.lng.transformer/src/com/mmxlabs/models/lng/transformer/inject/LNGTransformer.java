/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.OptimisationTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

/**
 * Helper class to create {@link LNGScenarioTransformer}, {@link OptimisationTransformer}, {@link IOptimisationData} and {@link ModelEntityMap} instances using Guice to inject components as necessary.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGTransformer {

	/**
	 */
	public static final String HINT_OPTIMISE_LSO = "hint-lngtransformer-optimise-lso";
	/**
	 */
	public static final String HINT_OPTIMISE_BREAKDOWN = "hint-lngtransformer-optimise-breakdown";

	public static final String HINT_GENERATE_CHARTER_OUTS = "hint-lngtransformer-generate-charter-outs";

	private final LNGScenarioModel scenario;

	private final Injector injector;

	private Iterable<IOptimiserInjectorService> extraModules;

	private final OptimiserSettings optimiserSettings;
	private Collection<IOptimiserInjectorService> services;
	private Set<String> hints;

	/**
	 * @param scenario
	 * @param hints
	 */
	public LNGTransformer(final LNGScenarioModel scenario, final OptimiserSettings optimiserSettings, final String... hints) {
		this(scenario, optimiserSettings, null, null, hints);
	}

	/**
	 */
	public LNGTransformer(@NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, @Nullable final IOptimiserInjectorService localOverrides,
			final String... hints) {
		this(scenario, optimiserSettings, null, localOverrides, hints);
	}

	/**
	 * 
	 * @param scenario
	 * @param module
	 * @param hints
	 */
	public LNGTransformer(@NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, @Nullable final Module module, final String... hints) {
		this(scenario, optimiserSettings, module, null, hints);
	}

	/**
	 */
	public LNGTransformer(@NonNull final LNGScenarioModel scenario, @NonNull final OptimiserSettings optimiserSettings, @Nullable final Module bootstrapModule,
			@Nullable final IOptimiserInjectorService localOverrides, final String... initialHints) {
		this.scenario = scenario;
		this.optimiserSettings = optimiserSettings;

		boolean performOptimisation = false;

		hints = new HashSet<String>();
		// Check hints
		if (initialHints != null) {
			for (final String hint : initialHints) {
				hints.add(hint);
				if (HINT_OPTIMISE_LSO.equals(hint)) {
					performOptimisation = true;
				}
			}
		}
		if (optimiserSettings.isGenerateCharterOuts()) {
			if (LicenseFeatures.isPermitted("features:optimisation-charter-out-generation")) {
				hints.add(HINT_GENERATE_CHARTER_OUTS);
			}
		}

		// Too late for LNGScenarioRunner, but add to hints for modules in case it is needed in the future.
		if (optimiserSettings.isBuildActionSets()) {
			if (LicenseFeatures.isPermitted("features:optimisation-actionset")) {
				hints.add(HINT_OPTIMISE_BREAKDOWN);
			}
		}

		services = LNGTransformerHelper.getOptimiserInjectorServices(bootstrapModule, localOverrides);

		Injector parentInjector;
		{
			final List<Module> modules = new LinkedList<>();

			// Prepare the main modules with the re-usable data for any further work.
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new DataComponentProviderModule(), services, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGTransformerModule(scenario, optimiserSettings), services,
					IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, hints));

			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserSettings), services,
					IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
//			parentInjector = Guice.createInjector(modules);
//		}
//		// Create temporary child injector to compute the initial solution and then pass result back into the main injector (as new child). This avoid polluting the injector with evaluation state.
//		final ISequences initialSequences;
//		{
//			final List<Module> modules2 = new LinkedList<>();
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGInitialSequencesModule(), services, IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));

//			final Injector initialSolutionInjector = parentInjector.createChildInjector(modules2);
//			{
//				initialSequences = initialSolutionInjector.getInstance(Key.get(ISequences.class, Names.named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)));
//				// Create a new child injector from the parent (i.e. without the modules2 list) with the initial sequences added
//				parentInjector = parentInjector.createChildInjector(new InitialSequencesModule(initialSequences));
//			}
//		}
//		{
//			final List<Module> modules = new LinkedList<>();

//			modules.add(new InputSequencesModule(initialSequences));
//			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(optimiserSettings), services,
//					IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_OptimiserSettingsModule(optimiserSettings), services,
					IOptimiserInjectorService.ModuleType.Module_ParametersModule, hints));
//			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGOptimisationModule(), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));
			parentInjector = Guice.createInjector(modules);

//			parentInjector = parentInjector.createChildInjector(modules);

		}

		this.injector = parentInjector;

		injector.injectMembers(this);
	}

	public MMXRootObject getScenario() {
		return scenario;
	}

	/**
	 */
	public ModelEntityMap getModelEntityMap() {
		return injector.getInstance(ModelEntityMap.class);
	}

	public LNGScenarioTransformer getLngScenarioTransformer() {
		return injector.getInstance(LNGScenarioTransformer.class);
	}

	@NonNull
	public IOptimisationData getOptimisationData() {
		return injector.getInstance(IOptimisationData.class);
	}

	/**
	 */
	@NonNull
	public IEvaluationContext getEvaluationContext() {
		return injector.getInstance(IEvaluationContext.class);
	}

	@NonNull
	public IOptimisationContext getOptimisationContext() {
		return injector.getInstance(IOptimisationContext.class);
	}

	/**
	 */
	public LocalSearchOptimiser getOptimiser() {
		return injector.getInstance(LocalSearchOptimiser.class);
	}

	/**
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 */
	public OptimiserSettings getOptimiserSettings() {
		return optimiserSettings;
	}

	public Collection<IOptimiserInjectorService> getOptimiserServices() {
		return services;
	}

	public Collection<String> getHints() {
		return hints;
	}
}
