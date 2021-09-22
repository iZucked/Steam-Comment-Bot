/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.OptionalLong;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.InsertionOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultiobjectiveSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelHillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelLocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelMultiobjectiveSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelMultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.ReduceSequencesStage;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.actionplan.LNGActionSetTransformerUnit;
import com.mmxlabs.models.lng.transformer.breakeven.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGHillClimbOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGReduceToBestSolutionUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.ResetInitialSequencesUnit;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerOptimiserUnit;
import com.mmxlabs.models.lng.transformer.multisimilarity.LNGMultiObjectiveOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser.LNGParallelHillClimbingOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser.LNGParallelMultiObjectiveOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.ui.parallellocalsearchoptimiser.LNGParallelOptimiserTransformerUnit;

public class LNGScenarioChainUnitFactory {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";
	public static final String PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN = "MMX_DISABLE_SECOND_ACTION_SET_RUN";
	public static final String PROPERTY_MMX_DISABLE_SECOND_LSO_RUN = "MMX_DISABLE_SECOND_LSO_RUN";

	private static final int PROGRESS_CLEAN_STATE = 25;
	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	public static @Nullable BiConsumer<LNGScenarioToOptimiserBridge, String> chainUp(final @NonNull ChainBuilder builder, @NonNull LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, final @NonNull JobExecutorFactory jobExecutorFactory,
			final @NonNull OptimisationStage template, final int jobCount, final @NonNull UserSettings userSettings) {
		if (template instanceof CleanStateOptimisationStage) {
			final CleanStateOptimisationStage stage = (CleanStateOptimisationStage) template;
			if (stage.getAnnealingSettings().getIterations() > 0) {
				LightWeightSchedulerOptimiserUnit.chain(builder, scenarioToOptimiserBridge, stage.getName(), userSettings, stage, PROGRESS_CLEAN_STATE, jobExecutorFactory, stage.getSeed());
			}
			return null;
		} else if (template instanceof MultiobjectiveSimilarityOptimisationStage) {
			final MultiobjectiveSimilarityOptimisationStage stage = (MultiobjectiveSimilarityOptimisationStage) template;
			if (stage.getAnnealingSettings().getIterations() > 0) {
				final boolean doSecondRun = doSecondLSORun(userSettings);
				final int[] seeds = new int[jobCount];
				for (int i = 0; i < jobCount; ++i) {
					seeds[i] = stage.getSeed() + i;
				}
				if (template instanceof ParallelMultiobjectiveSimilarityOptimisationStage) {
					if (doSecondRun) {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGParallelMultiObjectiveOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION, true);
					} else {
						LNGParallelMultiObjectiveOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION, true);
					}
				} else {
					if (doSecondRun) {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGMultiObjectiveOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION - (PROGRESS_OPTIMISATION / 3), jobExecutorFactory, seeds);
					} else {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION, jobExecutorFactory, seeds);
					}
				}
			}
		} else if (template instanceof MultipleSolutionSimilarityOptimisationStage) {
			final MultipleSolutionSimilarityOptimisationStage stage = (MultipleSolutionSimilarityOptimisationStage) template;
			if (stage.getAnnealingSettings().getIterations() > 0) {
				final boolean doSecondRun = doSecondLSORun(userSettings);
				final int[] seeds = new int[jobCount];
				for (int i = 0; i < jobCount; ++i) {
					seeds[i] = stage.getSeed() + i;
				}
				if (template instanceof ParallelMultipleSolutionSimilarityOptimisationStage) {
					if (doSecondRun) {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGParallelMultiObjectiveOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION, false);
					} else {
						LNGParallelMultiObjectiveOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION, false);
					}
				} else {
					if (doSecondRun) {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGMultiObjectiveOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION - (PROGRESS_OPTIMISATION / 3), jobExecutorFactory, seeds);
					} else {
						LNGMultiObjectiveOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION, jobExecutorFactory, seeds);
					}
				}
				return (bridge, name) -> {
					SolutionSetExporterUnit.exportMultipleSolutions(builder, PROGRESS_ACTION_SET_SAVE, bridge, () -> {
						OptimisationResult options = AnalyticsFactory.eINSTANCE.createOptimisationResult();
						options.setName(name);
						options.setUserSettings(EcoreUtil.copy(userSettings));
						return options;
					},  false, OptionalLong.empty());
				};
			}
		} else if (template instanceof LocalSearchOptimisationStage) {
			final LocalSearchOptimisationStage stage = (LocalSearchOptimisationStage) template;
			if (stage.getAnnealingSettings().getIterations() > 0) {
				final int[] seeds = new int[jobCount];
				for (int i = 0; i < jobCount; ++i) {
					seeds[i] = stage.getSeed() + i;
				}
				final boolean doSecondRun = doSecondLSORun(userSettings);
				if (template instanceof ParallelLocalSearchOptimisationStage) {
					if (doSecondRun) {
						LNGLSOOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGParallelOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION);
					} else {
						LNGParallelOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_OPTIMISATION);
					}
				} else {
					if (doSecondRun) {
						LNGLSOOptimiserTransformerUnit.chainPoolFake(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION / 3, jobExecutorFactory, seeds);
						LNGLSOOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION - (PROGRESS_OPTIMISATION / 3), jobExecutorFactory, seeds);
					} else {
						LNGLSOOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_OPTIMISATION, jobExecutorFactory, seeds);
					}
				}
			}
			return null;
		} else if (template instanceof BreakEvenOptimisationStage) {
			final BreakEvenOptimisationStage stage = (BreakEvenOptimisationStage) template;
			BreakEvenTransformerUnit.chain(builder, userSettings, stage, 100);
			return null;
		} else if (template instanceof HillClimbOptimisationStage) {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_HILLCLIMB)) {
				final HillClimbOptimisationStage stage = (HillClimbOptimisationStage) template;
				if (stage.getAnnealingSettings().getIterations() > 0) {
					if (template instanceof ParallelHillClimbOptimisationStage) {
						LNGParallelHillClimbingOptimiserTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_HILLCLIMBING_OPTIMISATION);
					} else {
						if (jobCount > 1) {
							LNGHillClimbOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_HILLCLIMBING_OPTIMISATION, jobExecutorFactory, true);
						} else {
							LNGHillClimbOptimiserTransformerUnit.chainPool(builder, stage.getName(), userSettings, stage, PROGRESS_HILLCLIMBING_OPTIMISATION, jobExecutorFactory, false);
						}
					}
				}
			}
			return null;
		} else if (template instanceof ActionPlanOptimisationStage) {
			final ActionPlanOptimisationStage stage = (ActionPlanOptimisationStage) template;
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPTIMISATION_ACTIONSET)) {
				if (stage.getTotalEvaluations() > 0) {
					// Run the action set post optimisation
					final boolean doSecondRun = doSecondActionSetRun(userSettings);
					if (doSecondRun) {
						LNGActionSetTransformerUnit.chainFake(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_ACTION_SET_OPTIMISATION / 2);
						LNGActionSetTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_ACTION_SET_OPTIMISATION / 2);
					} else {
						LNGActionSetTransformerUnit.chain(builder, stage.getName(), userSettings, stage, jobExecutorFactory, PROGRESS_ACTION_SET_OPTIMISATION);
					}
				}
				return (bridge, name) -> {
					SolutionSetExporterUnit.exportMultipleSolutions(builder, PROGRESS_ACTION_SET_SAVE, bridge, () -> {
						ActionableSetPlan options = AnalyticsFactory.eINSTANCE.createActionableSetPlan();
						options.setUserSettings(EcoreUtil.copy(userSettings));
						options.setName(name);
						return options;
					}, false, OptionalLong.empty());
				};
			}
		} else if (template instanceof ResetInitialSequencesStage) {
			ResetInitialSequencesStage stageSettings = (ResetInitialSequencesStage) template;
			ResetInitialSequencesUnit.chain(builder, stageSettings.getName(), userSettings, stageSettings, 1);
		} else if (template instanceof ReduceSequencesStage) {
			final ReduceSequencesStage stageSettings = (ReduceSequencesStage) template;
			LNGReduceToBestSolutionUnit.chain(builder, stageSettings.getName(), 1);
		} else if (template instanceof InsertionOptimisationStage) {
			// Currently we directly construct the chain up code, particularly due to the extra inputs required.
			 return null;
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
		if (!userSettings.isSetPeriodStartDate() || !userSettings.isSetPeriodEnd()) {
			over3Months = true;
		} else {
			final LocalDate after = userSettings.getPeriodStartDate();
			final YearMonth before = userSettings.getPeriodEnd();
			if (after == null || before == null) {
				over3Months = true;

			} else if (Months.between(after, before) > 3) {
				over3Months = true;
			}
		}
		return over3Months;
	}

	protected static boolean doSecondLSORun(@NonNull final UserSettings userSettings) {

		if (System.getProperty(PROPERTY_MMX_DISABLE_SECOND_LSO_RUN) != null) {
			return false;
		}
		return LicenseFeatures.isPermitted("features:optimiser-second-lso-stage");
	}
}
