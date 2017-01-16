/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.apache.shiro.SecurityUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGNoNominalInPromptTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.BreakEvenTransformerUnit;

public class LNGScenarioChainBuilder {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";
	public static final String PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN = "MMX_DISABLE_SECOND_ACTION_SET_RUN";

	private static final int PROGRESS_CLEAN_STATE = 25;
	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	/**
	 * Creates a {@link IChainRunner} for the "standard" optimisation process (as of 2015/11)
	 * 
	 * @param childName
	 * @param dataTransformer
	 * @param scenarioToOptimiserBridge
	 * @param optimiserSettings
	 * @param executorService
	 *            Optional (for now) {@link ExecutorService} for parallelisation
	 * @param initialHints
	 * @return
	 */
	public static IChainRunner createStandardOptimisationChain(@Nullable final String childName, @NonNull final LNGDataTransformer dataTransformer,
			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, @NonNull final OptimisationPlan optimisationPlan, @NonNull final ExecutorService executorService,
			@NonNull final String @Nullable... initialHints) {

		boolean createOptimiser = false;
		boolean doCleanStateOptimiser = false;
		boolean doHillClimb = false;
		boolean doActionSetPostOptimisation = false;

		final Set<String> hints = LNGTransformerHelper.getHints(optimisationPlan.getUserSettings(), initialHints);
		for (final String hint : hints) {
			if (LNGTransformerHelper.HINT_OPTIMISE_LSO.equals(hint)) {
				createOptimiser = true;
			}
			if (LNGTransformerHelper.HINT_CLEAN_STATE_EVALUATOR.equals(hint)) {
				doCleanStateOptimiser = true;
			}
		}
		// Check for break down optimisation here.
		if (optimisationPlan.getUserSettings().isBuildActionSets()) {
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-actionset")) {
				doActionSetPostOptimisation = true;
			}
		}

		final ChainBuilder builder = new ChainBuilder(dataTransformer);
		if (createOptimiser) {

			// TODO: Make a stage
			if (LicenseFeatures.isPermitted("features:no-nominal-in-prompt")) {
				LNGNoNominalInPromptTransformerUnit.chain(builder, optimisationPlan.getUserSettings(), 1);
			}

			BiConsumer<LNGScenarioToOptimiserBridge, ContainerProvider> exportCallback = null;
			if (!optimisationPlan.getStages().isEmpty()) {

				UserSettings userSettings = optimisationPlan.getUserSettings();
				for (final OptimisationStage stage : optimisationPlan.getStages()) {
					if (stage instanceof ParallelOptimisationStage<?>) {
						final ParallelOptimisationStage<? extends OptimisationStage> parallelOptimisationStage = (ParallelOptimisationStage<? extends OptimisationStage>) stage;
						final OptimisationStage template = parallelOptimisationStage.getTemplate();
						assert template != null;
						exportCallback = LNGScenarioChainUnitFactory.chainUp(builder, executorService, template, parallelOptimisationStage.getJobCount(), userSettings);
					} else {
						exportCallback = LNGScenarioChainUnitFactory.chainUp(builder, executorService, stage, 1, userSettings);
					}
				}
			} 
			
			final ContainerProvider resultProvider;
			if (childName != null) {
				// We have finished all optimisation steps. If we are saving the result in a child scenario rather than overwriting - do that here. Otherwise the result will be returned and the
				// LNGScenarioRunner will do the save.
				
				// Create a ContainerProvider on the original instance.
				final ContainerProvider containerProvider = new ContainerProvider(scenarioToOptimiserBridge.getScenarioInstance());
				// Create an empty container to store the child scenario result into so we can then pass it into the action set export
				resultProvider = new ContainerProvider();
				// Export a copy of the best result
				LNGExporterUnit.exportSingle(builder, 1, scenarioToOptimiserBridge, childName, containerProvider, resultProvider);
			} else {
				// No child scenario, so export action sets under the original scenario
				resultProvider = new ContainerProvider(scenarioToOptimiserBridge.getScenarioInstance());
			}
			
			if (exportCallback != null) {
				exportCallback.accept(scenarioToOptimiserBridge, resultProvider);
			}
		} else {
			// Just evaluate the current scenario.
			// TODO: Remove this step if we can get rid of the IAnnotatedSolutions.
			boolean breakeven = false;
			BreakEvenOptimisationStage beStage = null;
			for (OptimisationStage stage : optimisationPlan.getStages()) {
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

	// /**
	// * WIP: Needs UI link up. Generates a run-all similarity mode chain.
	// *
	// * @param dataTransformer
	// * @param dataExporter
	// * @param optimiserSettings
	// * @param initialHints
	// * @return
	// */
	// public static IChainRunner createRunAllSimilarityOptimisationChain(@NonNull final LNGDataTransformer dataTransformer, @NonNull final LNGScenarioToOptimiserBridge dataExporter,
	// @NonNull final OptimisationPlan optimisationPlan, @NonNull final ExecutorService executorService, @Nullable final String... initialHints) {
	//
	// final UserSettings basicSettings = ParametersFactory.eINSTANCE.createUserSettings();
	// if (optimiserSettings.getRange() != null) {
	// final OptimisationRange range = optimiserSettings.getRange();
	// if (range.isSetOptimiseAfter()) {
	// basicSettings.setPeriodStart(range.getOptimiseAfter());
	// }
	// if (range.isSetOptimiseBefore()) {
	// basicSettings.setPeriodEnd(range.getOptimiseBefore());
	// }
	// }
	// basicSettings.setShippingOnly(optimiserSettings.isShippingOnly());
	// basicSettings.setGenerateCharterOuts(optimiserSettings.isGenerateCharterOuts());
	// basicSettings.setBuildActionSets(optimiserSettings.isBuildActionSets());
	// final List<IChainRunner> runners = new ArrayList<>(SimilarityMode.values().length - 1);
	// for (final SimilarityMode mode : SimilarityMode.values()) {
	// if (mode == SimilarityMode.ALL) {
	// continue;
	// }
	// final UserSettings copy = EcoreUtil.copy(basicSettings);
	// copy.setSimilarityMode(mode);
	//
	// OptimisationHelper.checkUserSettings(copy, true);
	//
	// final OptimiserSettings settings = OptimisationHelper.transformUserSettings(copy, null, dataExporter.getOptimiserScenario());
	// if (settings != null) {
	// runners.add(createStandardOptimisationChain("Similarity-" + mode.toString(), dataTransformer, dataExporter, settings, executorService, initialHints));
	// }
	// }
	// final MultiChainRunner runner = new MultiChainRunner(dataTransformer, runners, executorService);
	//
	// return runner;
	// }

	@NonNull
	public static ExecutorService createExecutorService() {
		final int cores = getNumberOfAvailableCores();
		return createExecutorService(cores);
	}

	@NonNull
	public static ExecutorService createExecutorService(final int nThreads) {
		return Executors.newFixedThreadPool(nThreads);
	}

	public static int getNumberOfAvailableCores() {
		final int cores;

		if (System.getProperty(PROPERTY_MMX_NUMBER_OF_CORES) != null) {
			cores = Integer.valueOf(System.getProperty(PROPERTY_MMX_NUMBER_OF_CORES));
		} else if (LicenseFeatures.isPermitted("features:module-parallelisation")) {
			cores = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
		} else {
			cores = 1;
		}
		return cores;
	}
}
