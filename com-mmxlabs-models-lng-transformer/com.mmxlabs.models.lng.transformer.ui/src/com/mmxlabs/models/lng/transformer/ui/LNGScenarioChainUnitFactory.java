/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.OptionalLong;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultiobjectiveSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ReduceSequencesStage;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.StrategicLocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.breakeven.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGParallelHillClimbingOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGParallelMultiObjectiveOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGParallelOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGReduceToBestSolutionUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.ResetInitialSequencesUnit;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerOptimiserUnit;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;

public class LNGScenarioChainUnitFactory {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";

	private static final int PROGRESS_CLEAN_STATE = 25;
	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	public static @Nullable BiConsumer<LNGScenarioToOptimiserBridge, String> chainUp(final @NonNull ChainBuilder builder, @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge,
			final @NonNull JobExecutorFactory jobExecutorFactory, final @NonNull OptimisationStage template, final @NonNull UserSettings userSettings) {
		if (template instanceof StrategicLocalSearchOptimisationStage stage) {

			if (stage.getAnnealingSettings().getIterations() > 0) {
				builder.addLink(new LNGLSOOptimiserTransformerUnit(stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION));
			}

			return null;
		} else if (template instanceof CleanStateOptimisationStage stage) {
			if (stage.getAnnealingSettings().getIterations() > 0) {
				LightWeightSchedulerOptimiserUnit.chain(builder, scenarioToOptimiserBridge, stage.getName(), userSettings, stage, PROGRESS_CLEAN_STATE, jobExecutorFactory, stage.getSeed());
			}
			return null;
		} else if (template instanceof MultiobjectiveSimilarityOptimisationStage stage) {
			if (stage.getAnnealingSettings().getIterations() > 0) {
				builder.addLink(new LNGParallelMultiObjectiveOptimiserTransformerUnit(stage.getName(), userSettings, stage, jobExecutorFactory, true, PROGRESS_OPTIMISATION));
			}
		} else if (template instanceof MultipleSolutionSimilarityOptimisationStage stage) {
			if (stage.getAnnealingSettings().getIterations() > 0) {
				builder.addLink(new LNGParallelMultiObjectiveOptimiserTransformerUnit(stage.getName(), userSettings, stage, jobExecutorFactory, false, PROGRESS_OPTIMISATION));
				return (bridge, name) -> {
					SolutionSetExporterUnit.exportMultipleSolutions(builder, 5, bridge, () -> {
						OptimisationResult options = AnalyticsFactory.eINSTANCE.createOptimisationResult();
						options.setName(name);
						options.setUserSettings(EcoreUtil.copy(userSettings));
						return options;
					}, false, OptionalLong.empty(), true);
				};
			}
		} else if (template instanceof LocalSearchOptimisationStage stage) {
			if (stage.getAnnealingSettings().getIterations() > 0) {
				builder.addLink(new LNGParallelOptimiserTransformerUnit(stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION));
			}
			return null;
		} else if (template instanceof BreakEvenOptimisationStage stage) {
			BreakEvenTransformerUnit.chain(builder, userSettings, stage, 100);
			return null;
		} else if (template instanceof HillClimbOptimisationStage stage) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_HILLCLIMB)) {
				if (stage.getAnnealingSettings().getIterations() > 0) {
					builder.addLink(new LNGParallelHillClimbingOptimiserTransformerUnit(stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_HILLCLIMBING_OPTIMISATION));
				}
			}
			return null;
		} else if (template instanceof ResetInitialSequencesStage stageSettings) {
			ResetInitialSequencesUnit.chain(builder, stageSettings.getName(), userSettings, stageSettings, 1);
		} else if (template instanceof ReduceSequencesStage stageSettings) {
			LNGReduceToBestSolutionUnit.chain(builder, stageSettings.getName(), 1);
		} else if (template instanceof InsertionOptimisationStage) {
			// Currently we directly construct the chain up code, particularly due to the
			// extra inputs required.
			return null;
		} else {
			throw new IllegalArgumentException("Unknown stage type");
		}
		return null;
	}
}
