/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.impl.ProcessorAgnosticParallelLSO;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * This unit creates an optimiser and triggers first stage of constraint running
 * etc. It does more than just evaluating the scenario, but doesn't run an
 * optimisation. Note - could we replace this with the {@link EvaluationHelper}?
 * 
 * @author sg
 *
 */
@NonNullByDefault
public class LNGEvaluatingLSOTransformerUnit extends AbstractLNGOptimiserTransformerUnit<ProcessorAgnosticParallelLSO, LocalSearchOptimisationStage> {

	public LNGEvaluatingLSOTransformerUnit(final String stage, final UserSettings userSettings, final LocalSearchOptimisationStage stageSettings, final JobExecutorFactory jobExecutorFactory,
			int progressTicks) {
		super(stage, userSettings, stageSettings, jobExecutorFactory, progressTicks, ProcessorAgnosticParallelLSO.class);
	}

	@Override
	protected List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final LocalSearchOptimisationStage stageSettings,
			final ISequences initialSequences, final ISequences inputSequences, final Collection<@NonNull String> hints) {
		final List<Module> modules = new LinkedList<>();

		final Collection<IOptimiserInjectorService> services = dataTransformer.getModuleServices();

		addDefaultModules(modules, dataTransformer, stage, userSettings, stageSettings, initialSequences, inputSequences, hints);

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGParameters_AnnealingSettingsModule(stageSettings.getSeed(), stageSettings.getAnnealingSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_OptimisationParametersModule, hints));

		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(CollectionsUtil.makeLinkedList(new LNGOptimisationModule(), new LocalSearchOptimiserModule()), services,
				IOptimiserInjectorService.ModuleType.Module_Optimisation, hints));

		return modules;
	}

	protected IMultiStateResult doRunOptimisation(final LNGDataTransformer dataTransformer, final SequencesContainer initialSequences, final IMultiStateResult inputState,
			final IProgressMonitor monitor, final @Nullable IRunnerHook runnerHook) {

		final int jobId = 0;
		monitor.beginTask("", 100);
		try {
			// Construct the optimiser instance
			final List<Module> modules = createModules(dataTransformer, stage, userSettings, stageSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(),
					dataTransformer.getHints());
			final Injector injector = dataTransformer.getInjector().createChildInjector(modules);

			createOptimiser(dataTransformer, injector, stage, jobId, initialSequences.getSequences(), inputState.getBestSolution().getFirst());

			return inputState;
		} finally {
			monitor.done();
		}

	}
}
