/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

import org.apache.shiro.SecurityUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGCleanStateOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGHillClimbOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.ResetInitialSequencesUnit;
import com.mmxlabs.models.lng.transformer.ui.breakdown.chain.LNGActionSetTransformerUnit;

public class LNGScenarioChainUnitFactory {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";
	public static final String PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN = "MMX_DISABLE_SECOND_ACTION_SET_RUN";

	private static final int PROGRESS_CLEAN_STATE = 25;
	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	public static @Nullable BiConsumer<LNGScenarioToOptimiserBridge, ContainerProvider> chainUp(final @NonNull ChainBuilder builder, final @NonNull ExecutorService executorService,
			final @NonNull OptimisationStage template, final int jobCount, final @NonNull UserSettings userSettings) {

		if (template instanceof CleanStateOptimisationStage) {
			final CleanStateOptimisationStage stage = (CleanStateOptimisationStage) template;
			if (stage.getAnnealingSettings().getIterations() > 0) {

				final int[] seeds = new int[jobCount];
				for (int i = 0; i < jobCount; ++i) {
					seeds[i] = stage.getSeed() + i;
				}

				LNGCleanStateOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_CLEAN_STATE, executorService, seeds);
			}
			return null;
		} else if (template instanceof LocalSearchOptimisationStage) {
			final LocalSearchOptimisationStage stage = (LocalSearchOptimisationStage) template;

			if (stage.getAnnealingSettings().getIterations() > 0) {

				final int[] seeds = new int[jobCount];
				for (int i = 0; i < jobCount; ++i) {
					seeds[i] = stage.getSeed() + i;
				}
				LNGLSOOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION, executorService, seeds);
			}
			return null;
		} else if (template instanceof HillClimbOptimisationStage) {
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-hillclimb")) {
				final HillClimbOptimisationStage stage = (HillClimbOptimisationStage) template;
				if (stage.getAnnealingSettings().getIterations() > 0) {
					if (jobCount > 1) {
						LNGHillClimbOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_HILLCLIMBING_OPTIMISATION, executorService);
					} else {
						LNGHillClimbOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, PROGRESS_HILLCLIMBING_OPTIMISATION);
					}
				}
			}
			return null;
		} else if (template instanceof ActionPlanOptimisationStage) {
			final ActionPlanOptimisationStage stage = (ActionPlanOptimisationStage) template;
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-actionset")) {
				if (stage.getTotalEvaluations() > 0) {
					// Run the action set post optimisation
					final boolean doSecondRun = doSecondActionSetRun(userSettings);
					if (doSecondRun) {
						LNGActionSetTransformerUnit.chainFake(builder, stage.getName(), userSettings, stage, executorService, PROGRESS_ACTION_SET_OPTIMISATION / 2);
						LNGActionSetTransformerUnit.chain(builder, stage.getName(), userSettings, stage, executorService, PROGRESS_ACTION_SET_OPTIMISATION / 2);
					} else {
						LNGActionSetTransformerUnit.chain(builder, stage.getName(), userSettings, stage, executorService, PROGRESS_ACTION_SET_OPTIMISATION);
					}
				}
				return (bridge, resultProvider) -> {
					LNGActionSetTransformerUnit.export(builder, PROGRESS_ACTION_SET_SAVE, bridge, resultProvider);
				};
			}
		} else if (template instanceof ResetInitialSequencesStage) {
			ResetInitialSequencesStage stageSettings = (ResetInitialSequencesStage) template;
			ResetInitialSequencesUnit.chain(builder, stageSettings.getName(), userSettings, stageSettings, 1);
		} else {
			throw new IllegalArgumentException("Unknown stage type");
		}
		return null;
	}

	protected static boolean doSecondActionSetRun(@NonNull final UserSettings userSettings) {
		if (System.getProperty(PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN) != null) {
			return false;
		}
		boolean over3Months = false;
		if (!userSettings.isSetPeriodStart() || !userSettings.isSetPeriodEnd()) {
			over3Months = true;
		} else {
			final YearMonth after = userSettings.getPeriodStart();
			final YearMonth before = userSettings.getPeriodEnd();
			if (after == null || before == null) {
				over3Months = true;

			} else if (Months.between(after, before) > 3) {
				over3Months = true;
			}
		}
		return over3Months;
	}
}
