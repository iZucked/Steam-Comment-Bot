/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.moves;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveGeneratorModule;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Tests for the {@link GuidedMoveGenerator}
 *
 */
public abstract class AbstractMoveHandlerTest extends AbstractMicroTestCase {

	protected void runTest(final BiConsumer<Injector, LNGScenarioRunner> action) {
		evaluateWithLSOTest(false, plan -> plan.getStages().clear(), null, scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			final Injector parentInjector = dataTransformer.getInjector();
			final Collection<@NonNull String> hints = scenarioToOptimiserBridge.getDataTransformer().getHints();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = dataTransformer.getInitialSequences();

			final Collection<@NonNull IOptimiserInjectorService> services = dataTransformer.getModuleServices();

			@NonNull

			final List<Module> modules = new LinkedList<>();

			modules.add(new InitialSequencesModule(initialRawSequences));
			modules.add(new InputSequencesModule(initialRawSequences));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
					new LNGParameters_EvaluationSettingsModule(ScenarioUtils.createDefaultUserSettings(), ScenarioUtils.createDefaultConstraintAndFitnessSettings()), services,
					IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			modules.add(new AbstractModule() {

				@Override
				protected void configure() {

					install(new MoveGeneratorModule());

					bind(ILookupManager.class).to(LookupManager.class);
				}
			});

			final Injector injector = parentInjector.createChildInjector(modules);

			try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
				scope.enter();

				action.accept(injector, scenarioRunner);

			}

		}, null);
	}
}