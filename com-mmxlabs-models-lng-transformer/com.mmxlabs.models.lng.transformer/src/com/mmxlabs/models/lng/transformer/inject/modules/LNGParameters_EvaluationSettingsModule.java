/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluatedStateConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.SimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;

/**
 * The {@link LNGParameters_EvaluationSettingsModule} provides user-definable parameters derived from the {@link OptimiserSettings} object such as the random seed and number of iterations
 * 
 */
public class LNGParameters_EvaluationSettingsModule extends AbstractModule {

	public static final String OPTIMISER_REEVALUATE = "LNGParameters_EvaluationSettingsModule_OPTIMISER_REEVALUATE";
	
	@NonNull
	private final UserSettings userSettings;

	private ConstraintAndFitnessSettings constraintAndFitnessSettings;

	public LNGParameters_EvaluationSettingsModule(@NonNull UserSettings userSettings, @NonNull ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		this.userSettings = userSettings;
		this.constraintAndFitnessSettings = constraintAndFitnessSettings;
	}

	@Override
	protected void configure() {

	}

	//

	@Provides
	@Singleton
	@Named(EvaluatedStateConstraintCheckerInstantiatorModule.ENABLED_EVALUATED_STATE_CONSTRAINT_NAMES)
	private List<String> provideEnabledEvaluatedStateConstraintNames(final IEvaluatedStateConstraintCheckerRegistry registry) {
		// settings.getConstraints().stream().filter(c -> c.isEnabled()).map(Constraint::getName()).collect(Collectors.toList());

		final List<String> result = new ArrayList<String>();
		for (final IEvaluatedStateConstraintCheckerFactory f : registry.getConstraintCheckerFactories()) {
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
	@Named(EvaluationProcessInstantiatorModule.ENABLED_EVALUATION_PROCESS_NAMES)
	private List<String> provideEnabledEvaluationProcessNames(final IEvaluationProcessRegistry registry) {
		final List<String> result = new ArrayList<String>();

		// registry.getEvaluationProcessNames().stream()//.filter(c->c.isEnabled())
		// .map(IEvaluationProcessFactory::getName()).collect(Collectors.toList());

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
	@Named(ConstraintCheckerInstantiatorModule.ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames() {
		// settings.getConstraints().stream().filter(c -> c.isEnabled()).map(Constraint::getName()).collect(Collectors.toList());

		final List<String> result = new ArrayList<String>();

		for (final Constraint c : constraintAndFitnessSettings.getConstraints()) {
			if (c.isEnabled()) {
				result.add(c.getName());
			}
		}

		return result;
	}

	@Provides
	@Singleton
	@Named(FitnessFunctionInstantiatorModule.ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames() {
		final List<String> result = new ArrayList<String>();

		for (final Objective o : constraintAndFitnessSettings.getObjectives()) {
			if (o.isEnabled() && o.getWeight() > 0) {
				result.add(o.getName());
			}
		}

		return result;
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

		for (final Objective objective : constraintAndFitnessSettings.getObjectives()) {
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
		final SimilaritySettings similaritySettings = constraintAndFitnessSettings.getSimilaritySettings();

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
	private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
		final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
		int highPeriodInDays = constraintAndFitnessSettings.getFloatingDaysLimit();
		int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
		idleParams.setEndWeight(10_000);

		return idleParams;
	}

	@Provides
	@Named(OPTIMISER_REEVALUATE)
	private boolean isOptimiserReevaluating() {
		return true;
	}

	@Provides
	@Named(ConstrainedMoveGenerator.LSO_MOVES_SCMG)
	private boolean isSCMGLooping() {
		return false;
	}
	

}