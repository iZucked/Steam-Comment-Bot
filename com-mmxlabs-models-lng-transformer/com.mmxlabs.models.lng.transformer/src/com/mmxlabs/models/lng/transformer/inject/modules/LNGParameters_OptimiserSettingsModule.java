/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.RestartingLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;

/**
 * The {@link LNGParameters_OptimiserSettingsModule} provides user-definable parameters derived from the {@link OptimiserSettings} object such as the random seed and number of iterations
 * 
 */
public class LNGParameters_OptimiserSettingsModule extends AbstractModule {

	public static final String PROPERTY_MMX_HALF_SPEED_ACTION_SETS = "MMX_HALF_SPEED_ACTION_SETS";

	@NonNull
	private final OptimiserSettings settings;

	public LNGParameters_OptimiserSettingsModule(@NonNull final OptimiserSettings settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named(FitnessFunctionInstantiatorModule.ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames() {
		final List<String> result = new ArrayList<String>();

		for (final Objective o : settings.getObjectives()) {
			if (o.isEnabled() && o.getWeight() > 0) {
				result.add(o.getName());
			}
		}

		return result;
	}

	@Provides
	@Singleton
	private IThresholder provideThresholder(@Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {
		// For now we are just going to generate a self-calibrating thresholder

		final IThresholder thresholder = new GeometricThresholder(new Random(seed), settings.getAnnealingSettings().getEpochLength(), settings.getAnnealingSettings().getInitialTemperature(),
				settings.getAnnealingSettings().getCooling());
		return thresholder;

		// return new MovingAverageThresholder(getRandom(), ts.getInitialAcceptanceRate(), ts.getAlpha(), ts.getEpochLength(), 3000);
		// return new CalibratingGeometricThresholder(getRandom(), ts.getEpochLength(), ts.getInitialAcceptanceRate(), ts.getAlpha());
	}

	@Provides
	@Named(PROPERTY_MMX_HALF_SPEED_ACTION_SETS)
	private boolean isActionSetsRevaluating() {
		return true;
	}
	
	@Provides
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long getRandomSeed() {
		return settings.getSeed();
	}

	@Provides
	@Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)
	private boolean enableFourOpt2() {
		return true;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)
	private int getNumberOfIterations() {
		return settings.getAnnealingSettings().getIterations();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS)
	private int getNumberOfSolutionImprovementIterations() {
		return settings.getSolutionImprovementSettings() != null ? settings.getSolutionImprovementSettings().getIterations() : 0;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)
	private boolean isLSORestarting() {
		return settings.getAnnealingSettings().isRestarting();
	}

	@Provides
	@Named(RestartingLocalSearchOptimiser.RESTART_ITERATIONS_THRESHOLD)
	private int getRestartIterationsThreshold() {
		return settings.getAnnealingSettings().getRestartIterationsThreshold();
	}

	@Provides
	@Named(LinearFitnessEvaluatorModule.LINEAR_FITNESS_WEIGHTS_MAP)
	Map<String, Double> provideLSOFitnessWeights(@Named(FitnessFunctionInstantiatorModule.ENABLED_FITNESS_NAMES) @NonNull final List<String> enabledFitnessNames) {

		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final String component : enabledFitnessNames) {
			if (component != null) {
				weightsMap.put(component, 0.0);
			}
		}

		for (final Objective objective : settings.getObjectives()) {
			if (objective.isEnabled()) {
				if (weightsMap.containsKey(objective.getName())) {
					weightsMap.put(objective.getName(), objective.getWeight());
				}
			}
		}
		return weightsMap;
	}

	@Provides
	@Singleton
	private ISimilarityComponentParameters provideSimilarityComponentParameters() {

		final SimilarityComponentParameters scp = new SimilarityComponentParameters();

		// Replace with settings.
		final SimilaritySettings similaritySettings = settings.getSimilaritySettings();

		scp.setThreshold(ISimilarityComponentParameters.Interval.LOW, similaritySettings.getLowInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.LOW, similaritySettings.getLowInterval().getWeight());
		scp.setThreshold(ISimilarityComponentParameters.Interval.MEDIUM, similaritySettings.getMedInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.MEDIUM, similaritySettings.getMedInterval().getWeight());
		scp.setThreshold(ISimilarityComponentParameters.Interval.HIGH, similaritySettings.getHighInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.HIGH, similaritySettings.getHighInterval().getWeight());
		scp.setOutOfBoundsWeight(similaritySettings.getOutOfBoundsWeight());

		return scp;
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_TOTAL_EVALUATIONS)
	private int actionPlanTotalEvals() {
		final ActionPlanSettings actionPlanSettings = settings.getActionPlanSettings();
		assert actionPlanSettings != null;
		return actionPlanSettings.getTotalEvaluations();
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_IN_RUN_EVALUATIONS)
	private int actionPlanInRunEvals() {
		final ActionPlanSettings actionPlanSettings = settings.getActionPlanSettings();
		assert actionPlanSettings != null;
		return actionPlanSettings.getInRunEvaluations();
	}

	@Provides
	@Named(ActionPlanModule.ACTION_PLAN_MAX_SEARCH_DEPTH)
	private int actionPlanInRunSearchDepth() {
		final ActionPlanSettings actionPlanSettings = settings.getActionPlanSettings();
		assert actionPlanSettings != null;
		return actionPlanSettings.getSearchDepth();
	}

}