package com.mmxlabs.models.lng.transformer.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.stochasticactionsets.LNGActionSetTransformerUnit;

public class LNGScenarioChainBuilder {

	private static final int PROGRESS_OPTIMISATION = 100;
	private static final int PROGRESS_HILLCLIMBING_OPTIMISATION = 10;
	private static final int PROGRESS_ACTION_SET_OPTIMISATION = 20;
	private static final int PROGRESS_ACTION_SET_SAVE = 5;

	public static IChainRunner createStandardOptimisationChain(@Nullable String childName, @NonNull LNGDataTransformer dataTransformer, @NonNull LNGScenarioDataTransformer dataExporter,
			@NonNull OptimiserSettings optimiserSettings, @Nullable final String... initialHints) {

		boolean createOptimiser = false;
		boolean doHillClimb = false;
		boolean doActionSetPostOptimisation = false;

		Set<String> hints = LNGTransformerHelper.getHints(optimiserSettings, initialHints);
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
			LNGLSOOptimiserTransformerUnit.chain(builder, optimiserSettings, PROGRESS_OPTIMISATION);
			if (doHillClimb) {
				LNGHillClimbOptimiserTransformerUnit.chain(builder, optimiserSettings, PROGRESS_HILLCLIMBING_OPTIMISATION);
			}
			final ContainerProvider resultProvider;
			if (childName != null) {
				final ContainerProvider containerProvider = new ContainerProvider(dataExporter.getScenarioInstance());
				resultProvider = new ContainerProvider();
				LNGExporterUnit.export(builder, 1, dataExporter, childName, containerProvider, resultProvider);
			} else {
				resultProvider = new ContainerProvider(dataExporter.getScenarioInstance());
			}
			if (doActionSetPostOptimisation) {
				LNGActionSetTransformerUnit.chain(builder, optimiserSettings, PROGRESS_ACTION_SET_OPTIMISATION);
				LNGActionSetTransformerUnit.export(builder, PROGRESS_ACTION_SET_SAVE, dataExporter, resultProvider);
			}
		} else {
			LNGEvaluationTransformerUnit.chain(builder, 1);
		}
		return builder.build();
	}

	public static IChainRunner createRunAllSimilarityOptimisationChain(@NonNull LNGDataTransformer dataTransformer, @NonNull LNGScenarioDataTransformer dataExporter,
			@NonNull OptimiserSettings optimiserSettings, @Nullable final String... initialHints) {

		UserSettings basicSettings = ParametersFactory.eINSTANCE.createUserSettings();
		if (optimiserSettings.getRange() != null) {
			OptimisationRange range = optimiserSettings.getRange();
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
		List<IChainRunner> runners = new ArrayList<>(SimilarityMode.values().length - 1);
		for (SimilarityMode mode : SimilarityMode.values()) {
			if (mode == SimilarityMode.ALL) {
				continue;
			}
			UserSettings copy = EcoreUtil.copy(basicSettings);
			copy.setSimilarityMode(mode);

			OptimisationHelper.checkUserSettings(copy, true);

			OptimiserSettings settings = OptimisationHelper.transformUserSettings(copy, null);
			if (settings != null) {
				runners.add(createStandardOptimisationChain("Similarity-" + mode.toString(), dataTransformer, dataExporter, settings, initialHints));
			}
		}
		MultiChainRunner runner = new MultiChainRunner(dataTransformer, runners, 4);

		return runner;
	}

}
