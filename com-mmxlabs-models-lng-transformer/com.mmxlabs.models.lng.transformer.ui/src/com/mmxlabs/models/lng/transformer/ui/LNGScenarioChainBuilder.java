/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.shiro.SecurityUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainRunner;
import com.mmxlabs.models.lng.transformer.chain.MultiChainRunner;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGEvaluationTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGHillClimbOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGLSOOptimiserTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGNoNominalInPromptTransformerUnit;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_OptimiserSettingsModule;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.LNGActionSetTransformerUnit;

public class LNGScenarioChainBuilder {

	public static final String PROPERTY_MMX_NUMBER_OF_CORES = "MMX_NUMBER_OF_CORES";
	public static final String PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN = "MMX_DISABLE_SECOND_ACTION_SET_RUN";

	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	// TODO: (Alex) - Does not work as class is not injected. See system property.
	@Inject
	@Named(LNGParameters_OptimiserSettingsModule.PROPERTY_MMX_HALF_SPEED_ACTION_SETS)
	private static boolean HALF_SPEED_ACTION_SETS;

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
			@NonNull final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge, @NonNull final OptimiserSettings optimiserSettings, @NonNull final ExecutorService executorService,
			@NonNull final String @Nullable... initialHints) {

		boolean createOptimiser = false;
		boolean doHillClimb = false;
		boolean doActionSetPostOptimisation = false;

		final Set<String> hints = LNGTransformerHelper.getHints(optimiserSettings, initialHints);
		for (final String hint : hints) {
			if (LNGTransformerHelper.HINT_OPTIMISE_LSO.equals(hint)) {
				createOptimiser = true;
			}
		}
		// Check for break down optimisation here.
		if (optimiserSettings.isBuildActionSets()) {
			if (SecurityUtils.getSubject().isPermitted("features:optimisation-actionset")) {
				doActionSetPostOptimisation = true;
			}
		}
		doHillClimb = optimiserSettings.getSolutionImprovementSettings().isImprovingSolutions() && SecurityUtils.getSubject().isPermitted("features:optimisation-hillclimb");

		final ChainBuilder builder = new ChainBuilder(dataTransformer);
		if (createOptimiser) {

			if (LicenseFeatures.isPermitted("features:no-nominal-in-prompt")) {
				LNGNoNominalInPromptTransformerUnit.chain(builder, optimiserSettings, 1);
			}

			// Run the standard LSO optimisation
			LNGLSOOptimiserTransformerUnit.chain(builder, optimiserSettings, PROGRESS_OPTIMISATION);
			if (doHillClimb) {
				// Run a hill climb opt on the LSO result
				LNGHillClimbOptimiserTransformerUnit.chainPool(builder, optimiserSettings, PROGRESS_HILLCLIMBING_OPTIMISATION, executorService);
			}

			if (doActionSetPostOptimisation) {
				// Run the action set post optimisation
				final boolean doSecondRun = doSecondActionSetRun(optimiserSettings);
				if (doSecondRun) {
					LNGActionSetTransformerUnit.chainFake(builder, optimiserSettings, executorService, PROGRESS_ACTION_SET_OPTIMISATION / 2);
					LNGActionSetTransformerUnit.chain(builder, optimiserSettings, executorService, PROGRESS_ACTION_SET_OPTIMISATION / 2);
				} else {
					LNGActionSetTransformerUnit.chain(builder, optimiserSettings, executorService, PROGRESS_ACTION_SET_OPTIMISATION);

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
					LNGExporterUnit.export(builder, 1, scenarioToOptimiserBridge, childName, containerProvider, resultProvider);
				} else {
					// No child scenario, so export action sets under the original scenario
					resultProvider = new ContainerProvider(scenarioToOptimiserBridge.getScenarioInstance());
				}
				// Export action sets.
				LNGActionSetTransformerUnit.export(builder, PROGRESS_ACTION_SET_SAVE, scenarioToOptimiserBridge, resultProvider);
			} else {
				// No action sets - then just export the result as a child if required. Ignore the results
				if (childName != null) {
					final ContainerProvider containerProvider = new ContainerProvider(scenarioToOptimiserBridge.getScenarioInstance());
					LNGExporterUnit.export(builder, 1, scenarioToOptimiserBridge, childName, containerProvider, new ContainerProvider());
				}
			}
		} else {
			// Just evaluate the current scenario.
			// TODO: Remove this step if we can get rid of the IAnnotatedSolutions.
			LNGEvaluationTransformerUnit.chain(builder, 1);
		}
		return builder.build();
	}

	protected static boolean doSecondActionSetRun(@NonNull final OptimiserSettings optimiserSettings) {
		if (System.getProperty(PROPERTY_MMX_DISABLE_SECOND_ACTION_SET_RUN) != null) {
			return false;
		}
		boolean over3Months = false;
		final OptimisationRange range = optimiserSettings.getRange();
		if (range != null) {
			if (!range.isSetOptimiseAfter() || !range.isSetOptimiseBefore()) {
				over3Months = true;
			} else {
				final YearMonth after = range.getOptimiseAfter();
				final YearMonth before = range.getOptimiseBefore();
				if (after == null || before == null) {
					over3Months = true;

				} else if (Months.between(after, before) > 3) {
					over3Months = true;
				}
			}
		}
		return over3Months;
	}

	/**
	 * WIP: Needs UI link up. Generates a run-all similarity mode chain.
	 * 
	 * @param dataTransformer
	 * @param dataExporter
	 * @param optimiserSettings
	 * @param initialHints
	 * @return
	 */
	public static IChainRunner createRunAllSimilarityOptimisationChain(@NonNull final LNGDataTransformer dataTransformer, @NonNull final LNGScenarioToOptimiserBridge dataExporter,
			@NonNull final OptimiserSettings optimiserSettings, @NonNull final ExecutorService executorService, @Nullable final String... initialHints) {

		final UserSettings basicSettings = ParametersFactory.eINSTANCE.createUserSettings();
		if (optimiserSettings.getRange() != null) {
			final OptimisationRange range = optimiserSettings.getRange();
			if (range.isSetOptimiseAfter()) {
				basicSettings.setPeriodStart(range.getOptimiseAfter());
			}
			if (range.isSetOptimiseBefore()) {
				basicSettings.setPeriodEnd(range.getOptimiseBefore());
			}
		}
		basicSettings.setShippingOnly(optimiserSettings.isShippingOnly());
		basicSettings.setGenerateCharterOuts(optimiserSettings.isGenerateCharterOuts());
		basicSettings.setBuildActionSets(optimiserSettings.isBuildActionSets());
		final List<IChainRunner> runners = new ArrayList<>(SimilarityMode.values().length - 1);
		for (final SimilarityMode mode : SimilarityMode.values()) {
			if (mode == SimilarityMode.ALL) {
				continue;
			}
			final UserSettings copy = EcoreUtil.copy(basicSettings);
			copy.setSimilarityMode(mode);

			OptimisationHelper.checkUserSettings(copy, true);

			final OptimiserSettings settings = OptimisationHelper.transformUserSettings(copy, null, dataExporter.getOptimiserScenario());
			if (settings != null) {
				runners.add(createStandardOptimisationChain("Similarity-" + mode.toString(), dataTransformer, dataExporter, settings, executorService, initialHints));
			}
		}
		final MultiChainRunner runner = new MultiChainRunner(dataTransformer, runners, executorService);

		return runner;
	}

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
