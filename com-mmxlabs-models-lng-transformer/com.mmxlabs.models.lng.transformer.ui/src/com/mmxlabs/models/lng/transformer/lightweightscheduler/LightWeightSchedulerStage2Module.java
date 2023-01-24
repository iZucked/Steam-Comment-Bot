/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightConstraintCheckerRegistry;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightFitnessFunctionRegistry;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightSchedulerOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLongTermSequenceElementFilter;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu.TabuLightWeightSequenceOptimiser;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;

public class LightWeightSchedulerStage2Module extends AbstractModule {
	public static final String LIGHTWEIGHT_FITNESS_FUNCTION_NAMES = "LIGHTWEIGHT_FITNESS_FUNCTION_NAMES";
	public static final String LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	public static final String LIGHTWEIGHT_VESSELS = "LIGHTWEIGHT_VESSELS";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT";
	public static final boolean DEBUG = false;

	@Override
	protected void configure() {

		bind(ILightWeightSequenceOptimiser.class).to(TabuLightWeightSequenceOptimiser.class);
		bind(ILightWeightPostOptimisationStateModifier.class).to(DefaultLightWeightPostOptimisationStateModifier.class);
		bind(ISequenceElementFilter.class).to(DefaultLongTermSequenceElementFilter.class);
	}

	@Provides
	@ThreadLocalScope
	private LightWeightSchedulerOptimiser providePerThreadBagMover(@NonNull final Injector injector) {

		LightWeightSchedulerOptimiser lightweightSchedulerOptimiser = new LightWeightSchedulerOptimiser();
		injector.injectMembers(lightweightSchedulerOptimiser);
		return lightweightSchedulerOptimiser;
	}

	@Provides
	@Singleton
	private List<ILightWeightConstraintChecker> getConstraintCheckers(Injector injector, LightWeightConstraintCheckerRegistry registry,
			@Named(LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES) List<String> names) {
		List<ILightWeightConstraintChecker> constraintCheckers = new LinkedList<>();
		Collection<ILightWeightConstraintCheckerFactory> constraintCheckerFactories = registry.getConstraintCheckerFactories();
		for (String name : names) {
			for (ILightWeightConstraintCheckerFactory lightWeightConstraintCheckerFactory : constraintCheckerFactories) {
				if (lightWeightConstraintCheckerFactory.getName().equals(name)) {
					ILightWeightConstraintChecker constraintChecker = lightWeightConstraintCheckerFactory.createConstraintChecker();
					injector.injectMembers(constraintChecker);
					constraintCheckers.add(constraintChecker);
				}
			}
		}
		return constraintCheckers;
	}

	@Provides
	@Singleton
	private List<ILightWeightFitnessFunction> getFitnessFunctions(Injector injector, LightWeightFitnessFunctionRegistry registry, @Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES) List<String> names) {
		List<ILightWeightFitnessFunction> fitnessFunctions = new LinkedList<>();
		Collection<ILightWeightFitnessFunctionFactory> fitnessFunctionFactories = registry.getFitnessFunctionFactories();
		for (String name : names) {
			for (ILightWeightFitnessFunctionFactory lightWeightFitnessFunctionFactory : fitnessFunctionFactories) {
				if (lightWeightFitnessFunctionFactory.getName().equals(name)) {
					ILightWeightFitnessFunction fitnessFunction = lightWeightFitnessFunctionFactory.createFitnessFunction();
					injector.injectMembers(fitnessFunction);
					fitnessFunction.init();
					fitnessFunctions.add(fitnessFunction);
				}
			}
		}
		return fitnessFunctions;
	}
}
