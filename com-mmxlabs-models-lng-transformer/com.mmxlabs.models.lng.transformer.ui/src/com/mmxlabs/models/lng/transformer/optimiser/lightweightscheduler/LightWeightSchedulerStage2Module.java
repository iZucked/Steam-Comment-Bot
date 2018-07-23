/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

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
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLongTermSequenceElementFilter;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic.TabuLightWeightSequenceOptimiser;

public class LightWeightSchedulerStage2Module extends AbstractModule {
	public static final String LIGHTWEIGHT_FITNESS_FUNCTION_NAMES = "LIGHTWEIGHT_FITNESS_FUNCTION_NAMES";
	public static final String LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	public static final String LIGHTWEIGHT_VESSELS = "LIGHTWEIGHT_VESSELS";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT";
	public static final boolean DEBUG = false;

	private Map<Thread, LightWeightSchedulerOptimiser> threadCache;

	public LightWeightSchedulerStage2Module(Map<Thread, LightWeightSchedulerOptimiser> threadCache) {
		this.threadCache = threadCache;
	}

	@Override
	protected void configure() {

		bind(ILightWeightSequenceOptimiser.class).to(TabuLightWeightSequenceOptimiser.class);
		bind(ILightWeightPostOptimisationStateModifier.class).to(DefaultLightWeightPostOptimisationStateModifier.class);
		bind(ISequenceElementFilter.class).to(DefaultLongTermSequenceElementFilter.class);
	}

	@Provides
	private LightWeightSchedulerOptimiser providePerThreadBagMover(@NonNull final Injector injector) {

		LightWeightSchedulerOptimiser lightweightSchedulerOptimiser = threadCache.get(Thread.currentThread());
		if (lightweightSchedulerOptimiser == null) {
			lightweightSchedulerOptimiser = new LightWeightSchedulerOptimiser();
			injector.injectMembers(lightweightSchedulerOptimiser);
			threadCache.put(Thread.currentThread(), lightweightSchedulerOptimiser);
		}
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
