/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.RestartingLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;

/**
 * The {@link OptimiserSettingsModule} provides user-definable parameters derived from the {@link OptimiserSettings} object such as the random seed and number of iterations
 * 
 */
public class OptimiserSettingsModule extends AbstractModule {

	@Override
	protected void configure() {
	};

	@Provides
	@Singleton
	@Named(OptimiserCoreModule.ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames(final OptimiserSettings settings) {
		final List<String> result = new ArrayList<String>();

		for (final Constraint c : settings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}

		return result;
	}

	@Provides
	@Singleton
	@Named(OptimiserCoreModule.ENABLED_EVALUATION_PROCESS_NAMES)
	private List<String> provideEnabledEvaluationProcessNames(final OptimiserSettings settings, final IEvaluationProcessRegistry registry) {
		final List<String> result = new ArrayList<String>();

		// Enable all processes.
		for (final IEvaluationProcessFactory f : registry.getEvaluationProcessFactories()) {
			result.add(f.getName());
		}
		// for (final Constraint c : settings.getConstraints()) {
		// if (c.isEnabled()) {
		// result.add(c.getName());
		// }
		// }

		return result;
	}

	@Provides
	@Singleton
	@Named(OptimiserCoreModule.ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames(final OptimiserSettings settings) {
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
	private IThresholder provideThresholder(final OptimiserSettings settings, @Named(LocalSearchOptimiserModule.RANDOM_SEED) final long seed) {
		// For now we are just going to generate a self-calibrating thresholder

		final IThresholder thresholder = new GeometricThresholder(new Random(seed), settings.getAnnealingSettings().getEpochLength(), settings.getAnnealingSettings().getInitialTemperature(), settings
				.getAnnealingSettings().getCooling());
		return thresholder;

		// return new MovingAverageThresholder(getRandom(), ts.getInitialAcceptanceRate(), ts.getAlpha(), ts.getEpochLength(), 3000);
		// return new CalibratingGeometricThresholder(getRandom(), ts.getEpochLength(), ts.getInitialAcceptanceRate(), ts.getAlpha());
	}

	@Provides
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long getRandomSeed(final OptimiserSettings settings) {
		return settings.getSeed();
	}

	@Provides
	@Named(EnumeratingSequenceScheduler.OPTIMISER_REEVALUATE)
	private boolean isOptimiserReevaluating() {
		return false;
	}

	@Provides
	@Named(SequencesConstrainedMoveGeneratorUnit.OPTIMISER_ENABLE_FOUR_OPT_2)
	private boolean enableFourOpt2() {
		return true;
	}

	@Provides
	@Named(TravelTimeConstraintChecker.OPTIMISER_START_ELEMENT_FIX)
	private boolean enableStartOfSequenceFix() {
		return true;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)
	private int getNumberOfIterations(final OptimiserSettings settings) {
		return settings.getAnnealingSettings().getIterations();
	}
	
	@Provides
	@Named(LocalSearchOptimiserModule.SOLUTION_IMPROVER_NUMBER_OF_ITERATIONS)
	private int getNumberOfSolutionImprovementIterations(final OptimiserSettings settings) {
		return settings.getSolutionImprovementSettings() != null ? settings.getSolutionImprovementSettings().getIterations() : 0;
	}

	@Provides
	@Named(LocalSearchOptimiserModule.USE_RESTARTING_OPTIMISER)
	private boolean isLSORestarting(final OptimiserSettings settings) {
		return settings.getAnnealingSettings().isRestarting();
	}
	
	@Provides
	@Named(RestartingLocalSearchOptimiser.RESTART_ITERATIONS_THRESHOLD)
	private int getRestartIterationsThreshold(final OptimiserSettings settings) {
		return settings.getAnnealingSettings().getRestartIterationsThreshold();
	}

	@Provides
	@Named(LinearFitnessEvaluatorModule.LINEAR_FITNESS_WEIGHTS_MAP)
	Map<String, Double> provideLSOFitnessWeights(final OptimiserSettings settings, final List<IFitnessComponent> fitnessComponents) {
		// Initialise to zero, then take optimiser settings
		final Map<String, Double> weightsMap = new HashMap<String, Double>();
		for (final IFitnessComponent component : fitnessComponents) {
			if (component != null) {
				weightsMap.put(component.getName(), 0.0);
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
	private ILatenessComponentParameters provideLatenessComponentParameters() {
		final LatenessComponentParameters lcp = new LatenessComponentParameters();

		lcp.setThreshold(Interval.PROMPT, 48);
		lcp.setLowWeight(Interval.PROMPT, 0);
		lcp.setHighWeight(Interval.PROMPT, 1000000);

		lcp.setThreshold(Interval.MID_TERM, 72);
		lcp.setLowWeight(Interval.MID_TERM, 0);
		lcp.setHighWeight(Interval.MID_TERM, 1000000);

		lcp.setThreshold(Interval.BEYOND, 72);
		lcp.setLowWeight(Interval.BEYOND, 0);
		lcp.setHighWeight(Interval.BEYOND, 1000000);

		return lcp;
	}

	@Provides
	@Singleton
	private ISimilarityComponentParameters provideSimilarityComponentParameters(@NonNull OptimiserSettings settings) {

		final SimilarityComponentParameters scp = new SimilarityComponentParameters();

		// Replace with settings.
		SimilaritySettings similaritySettings = settings.getSimilaritySettings();
		
		scp.setThreshold(ISimilarityComponentParameters.Interval.LOW, similaritySettings.getLowInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.LOW, similaritySettings.getLowInterval().getWeight());
		scp.setThreshold(ISimilarityComponentParameters.Interval.MEDIUM, similaritySettings.getMedInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.MEDIUM, similaritySettings.getMedInterval().getWeight());
		scp.setThreshold(ISimilarityComponentParameters.Interval.HIGH, similaritySettings.getHighInterval().getThreshold());
		scp.setWeight(ISimilarityComponentParameters.Interval.HIGH, similaritySettings.getHighInterval().getWeight());
		scp.setOutOfBoundsWeight(similaritySettings.getOutOfBoundsWeight());

		return scp;
	}

}