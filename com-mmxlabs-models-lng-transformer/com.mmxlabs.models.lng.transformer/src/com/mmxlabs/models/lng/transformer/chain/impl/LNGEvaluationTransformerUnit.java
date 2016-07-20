/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.IMultiStateResult;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IEvaluationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGEvaluationTransformerUnit implements ILNGStateTransformerUnit {

	@NonNull
	public static IChainLink chain(final ChainBuilder chainBuilder, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				LNGEvaluationTransformerUnit t = new LNGEvaluationTransformerUnit(dt, initialSequences.getSequences(), inputState.getBestSolution().getFirst(), dt.getHints());
				return t.run(monitor);
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}

		};
		chainBuilder.addLink(link);
		return link;
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final IMultiStateResult inputState;

	@SuppressWarnings("null")
	public LNGEvaluationTransformerUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull ISequences initialSequences, @NonNull final ISequences inputSequences,
			/* Evaluation Parameters */ @NonNull final Collection<String> hints) {
		this.dataTransformer = dataTransformer;

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();
		List<Module> modules = new LinkedList<>();
		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
				new LNGParameters_EvaluationSettingsModule(dataTransformer.getUserSettings(), dataTransformer.getSolutionBuilderSettings().getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		injector = dataTransformer.getInjector().createChildInjector(modules);

		try (PerChainUnitScopeImpl scope = injector.getInstance(PerChainUnitScopeImpl.class)) {
			scope.enter();

			final IAnnotatedSolution annotatedSolution = LNGSchedulerJobUtils.evaluateCurrentState(injector, injector.getInstance(IOptimisationData.class), inputSequences).getFirst();
			assert annotatedSolution != null;
			inputState = new MultiStateResult(inputSequences, LNGSchedulerJobUtils.extractOptimisationAnnotations(annotatedSolution));
		}
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		return inputState;
	}
}
