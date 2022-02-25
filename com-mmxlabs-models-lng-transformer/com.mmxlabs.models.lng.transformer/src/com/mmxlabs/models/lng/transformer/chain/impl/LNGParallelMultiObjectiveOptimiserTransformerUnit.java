/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.lso.multiobjective.impl.NonDominatedSolution;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveLSOMover;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveOptimiser;
import com.mmxlabs.optimiser.lso.multiobjective.modules.MultiObjectiveOptimiserModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

@NonNullByDefault
public class LNGParallelMultiObjectiveOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<SimpleMultiObjectiveOptimiser, MultipleSolutionSimilarityOptimisationStage> {

	private boolean singleSolution;

	public LNGParallelMultiObjectiveOptimiserTransformerUnit(final String stage, final UserSettings userSettings, final MultipleSolutionSimilarityOptimisationStage stageSettings,
			JobExecutorFactory jobExecutorFactory, final boolean singleSolution, int progressTicks) {
		super(stage, userSettings, stageSettings, jobExecutorFactory, progressTicks, SimpleMultiObjectiveOptimiser.class);
		this.singleSolution = singleSolution;
	}

	@Override
	protected List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final MultipleSolutionSimilarityOptimisationStage stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<String> hints) {
		final List<Module> modules = new LinkedList<>();

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		addDefaultModules(modules, dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints);

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(
				LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule()), services, IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		modules.add(new MultiObjectiveOptimiserModule());

		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				bind(SimpleMultiObjectiveLSOMover.class).in(ThreadLocalScope.class);
			}

		});

		return modules;
	}

	@Override
	protected IMultiStateResult returnBestSolution(SimpleMultiObjectiveOptimiser optimiser, final @Nullable IAnnotatedSolution bestSolution, final @Nullable ISequences bestRawSequences) {
		if (singleSolution) {
			return super.returnBestSolution(optimiser, bestSolution, bestRawSequences);
		} else {
			List<NonDominatedSolution> sortedArchive = optimiser.getSortedArchive(true); // TODO: make generic

			final List<NonNullPair<ISequences, Map<String, Object>>> solutions = sortedArchive.stream() //
					.distinct() //
					.map(r -> new NonNullPair<ISequences, Map<String, Object>>(r.getSequences(), new HashMap<>())) //
					.toList();

			if (bestRawSequences != null && bestSolution != null) {
				return new MultiStateResult(new NonNullPair<ISequences, Map<String, Object>>(bestRawSequences, new HashMap<>()), solutions);
			} else {
				throw new RuntimeException("Unable to optimise");
			}
		}
	}
}
