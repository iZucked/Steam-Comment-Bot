package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;

/**
 * The {@link OptimiserSettingsModule} provides user-definable parameters derived from the {@link OptimiserSettings} object such as the random seed and number of iterations
 * 
 * @since 2.0
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

		return new GeometricThresholder(new Random(seed), settings.getAnnealingSettings().getEpochLength(), settings.getAnnealingSettings().getInitialTemperature(), settings.getAnnealingSettings()
				.getCooling());
		// return new MovingAverageThresholder(getRandom(), ts.getInitialAcceptanceRate(), ts.getAlpha(), ts.getEpochLength(), 3000);
		// return new CalibratingGeometricThresholder(getRandom(), ts.getEpochLength(), ts.getInitialAcceptanceRate(), ts.getAlpha());
	}

	@Provides
	@Named(LocalSearchOptimiserModule.RANDOM_SEED)
	private long getRandomSeed(final OptimiserSettings settings) {
		return settings.getSeed();
	}

	@Provides
	@Named(LocalSearchOptimiserModule.LSO_NUMBER_OF_ITERATIONS)
	private int getNumberOfIterations(final OptimiserSettings settings) {
		return settings.getAnnealingSettings().getIterations();
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
}
