/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGOptimisationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_AnnealingSettingsModule;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.impl.ProcessorAgnosticParallelLSO;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

@NonNullByDefault
public class LNGParallelHillClimbingOptimiserTransformerUnit extends AbstractLNGOptimiserTransformerUnit<ProcessorAgnosticParallelLSO, HillClimbOptimisationStage> {

	public LNGParallelHillClimbingOptimiserTransformerUnit(final String stage, final UserSettings userSettings, final HillClimbOptimisationStage stageSettings,
			final JobExecutorFactory jobExecutorFactory, int progressTicks) {
		super(stage, userSettings, stageSettings, jobExecutorFactory, progressTicks, ProcessorAgnosticParallelLSO.class);
	}

	protected Key<ProcessorAgnosticParallelLSO> getOptimiserTypeKey() {
		return Key.get(ProcessorAgnosticParallelLSO.class, Names.named(LocalSearchOptimiserModule.GREEDY_THRESHOLDER));
	}

	@Override
	protected List<Module> createModules(final LNGDataTransformer dataTransformer, final String stage, final UserSettings userSettings, final HillClimbOptimisationStage stageSettings,
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
}
