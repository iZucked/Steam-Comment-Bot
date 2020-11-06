/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.concurrent.NamedExecutorService;
import com.mmxlabs.common.concurrent.SimpleCleanableExecutorService;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.breakeven.BreakEvenTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGNoNominalInPromptTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LNGCheckForViolatedConstraintsUnit;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;

public class LNGScenarioChainBuilder {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";
	public static final String PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN = "MMX_DISABLE_SECOND_ACTION_SET_RUN";

	/**
	 * Creates a {@link IChainRunner} for the "standard" optimisation process (as of
	 * 2015/11)
	 * 
	 * @param childName
	 * @param dataTransformer
	 * @param scenarioToOptimiserBridge
	 * @param optimiserSettings
	 * @param executorService           Optional (for now)
	 *                                  {@link CleanableExecutorService} for
	 *                                  parallelisation
	 * @param initialHints
	 * @return
	 */
	public static @NonNull IChainRunner createStandardOptimisationChain(@NonNull final String resultName, @NonNull final LNGDataTransformer dataTransformer,
			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, @NonNull final OptimisationPlan optimisationPlan, @NonNull final CleanableExecutorService executorService,
			boolean includeDefaultExport, @NonNull final String @Nullable... initialHints) {
		boolean createOptimiser = false;

		final Set<String> hints = LNGTransformerHelper.getHints(optimisationPlan.getUserSettings(), initialHints);
		for (final String hint : hints) {
			if (LNGTransformerHelper.HINT_OPTIMISE_LSO.equals(hint)) {
				createOptimiser = true;
			}
		}

		final ChainBuilder builder = new ChainBuilder(dataTransformer);
		if (createOptimiser) {
			// Disable no nominals in prompt rules for ADP scenarios.
			if (optimisationPlan.getUserSettings().getMode() != OptimisationMode.ADP && hints.contains(LNGTransformerHelper.HINT_NO_NOMINALS_IN_PROMPT)) {
				LNGNoNominalInPromptTransformerUnit.chain(builder, optimisationPlan.getUserSettings(), 1);
			}

			if (optimisationPlan.getUserSettings().getMode() == OptimisationMode.ADP && !optimisationPlan.getUserSettings().isCleanSlateOptimisation()) {
				LNGCheckForViolatedConstraintsUnit.chain(builder, optimisationPlan.getUserSettings(), 1);
			}
			
			BiConsumer<LNGScenarioToOptimiserBridge, String> exportCallback = (bridge, name) -> SolutionSetExporterUnit.exportMultipleSolutions(builder, 1, bridge, () -> {
				final OptimisationResult options = AnalyticsFactory.eINSTANCE.createOptimisationResult();
				options.setName(name);
				options.setUserSettings(EcoreUtil.copy(dataTransformer.getUserSettings()));
				return options;
			}, false, OptionalLong.empty());

			if (!optimisationPlan.getStages().isEmpty()) {

				final UserSettings userSettings = optimisationPlan.getUserSettings();
				for (final OptimisationStage stage : optimisationPlan.getStages()) {
					BiConsumer<LNGScenarioToOptimiserBridge, String> callback;
					if (stage instanceof ParallelOptimisationStage<?>) {
						final ParallelOptimisationStage<? extends OptimisationStage> parallelOptimisationStage = (ParallelOptimisationStage<? extends OptimisationStage>) stage;
						final OptimisationStage template = parallelOptimisationStage.getTemplate();
						assert template != null;
						callback = LNGScenarioChainUnitFactory.chainUp(builder, scenarioToOptimiserBridge, executorService, template, parallelOptimisationStage.getJobCount(), userSettings);

					} else {
						callback = LNGScenarioChainUnitFactory.chainUp(builder, scenarioToOptimiserBridge, executorService, stage, 1, userSettings);
					}
					if (callback != null) {
						exportCallback = callback;
					}
				}
			}

			if (includeDefaultExport && exportCallback != null) {
				exportCallback.accept(scenarioToOptimiserBridge, resultName);
			}
		} else {
			// Just evaluate the current scenario.
			// TODO: Remove this step if we can get rid of the IAnnotatedSolutions.
			boolean breakeven = false;
			BreakEvenOptimisationStage beStage = null;
			for (final OptimisationStage stage : optimisationPlan.getStages()) {
				if (stage instanceof BreakEvenOptimisationStage) {
					breakeven = true;
					beStage = (BreakEvenOptimisationStage) stage;
					break;
				}
			}
			if (breakeven && beStage != null) {
				BreakEvenTransformerUnit.chain(builder, optimisationPlan.getUserSettings(), beStage, 100);
			} else {
				LNGEvaluationTransformerUnit.chain(builder, 1);
			}
		}

		return builder.build();
	}

	@NonNull
	public static CleanableExecutorService createExecutorService() {
		final int cores = getNumberOfAvailableCores();
		return createExecutorService(cores);
	}

	@NonNull
	public static CleanableExecutorService createExecutorService(final int nThreads) {
		return new SimpleCleanableExecutorService(NamedExecutorService.createFixedPool(nThreads), nThreads);
	}

	public static int getNumberOfAvailableCores() {
		final int cores;

		if (System.getProperty(PROPERTY_MMX_NUMBER_OF_CORES) != null) {
			cores = Integer.valueOf(System.getProperty(PROPERTY_MMX_NUMBER_OF_CORES));
		} else if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MODULE_PARALLELISATION)) {
			cores = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
		} else {
			cores = 1;
		}
		return cores;
	}
}
