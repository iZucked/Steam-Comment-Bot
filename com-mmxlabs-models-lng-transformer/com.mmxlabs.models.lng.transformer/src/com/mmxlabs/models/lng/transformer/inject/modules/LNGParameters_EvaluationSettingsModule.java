/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluatedStateConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;

/**
 * The {@link LNGParameters_EvaluationSettingsModule} provides user-definable parameters derived from the {@link OptimiserSettings} object such as the random seed and number of iterations
 * 
 */
public class LNGParameters_EvaluationSettingsModule extends AbstractModule {

	public static final String OPTIMISER_REEVALUATE = "LNGParameters_EvaluationSettingsModule_OPTIMISER_REEVALUATE";

	@NonNull
	private final OptimiserSettings settings;

	public LNGParameters_EvaluationSettingsModule(@NonNull OptimiserSettings settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named(ConstraintCheckerInstantiatorModule.ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames() {
		// settings.getConstraints().stream().filter(c -> c.isEnabled()).map(Constraint::getName()).collect(Collectors.toList());

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
	@Named(OPTIMISER_REEVALUATE)
	private boolean isOptimiserReevaluating() {
		return true;
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
		int highPeriodInDays = settings.getFloatingDaysLimit();
		int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
		idleParams.setEndWeight(10_000);

		return idleParams;
	}

}