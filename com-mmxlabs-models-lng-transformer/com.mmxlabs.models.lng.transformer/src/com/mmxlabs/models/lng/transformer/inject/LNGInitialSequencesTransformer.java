package com.mmxlabs.models.lng.transformer.inject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

public class LNGInitialSequencesTransformer {

	@NonNull
	private final Injector injector;

	@SuppressWarnings("null")
	public LNGInitialSequencesTransformer(@NonNull final LNGDataTransformer dataTransformer, @NonNull final OptimiserSettings settings, @NonNull final Collection<String> hints) {

		final List<Module> modules = new LinkedList<>();
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_EvaluationSettingsModule(settings), dataTransformer.getModuleServices(),
				IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		modules.addAll(
				LNGTransformerHelper.getModulesWithOverrides(new LNGInitialSequencesModule(), dataTransformer.getModuleServices(), IOptimiserInjectorService.ModuleType.Module_InitialSolution, hints));

		injector = Guice.createInjector(modules);
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@NonNull
	public ISequences getInitialSequences() {
		return injector.getInstance(Key.get(ISequences.class, Names.named("Initial")));
	}
}
