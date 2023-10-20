/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.FullLightWeightConstraintCheckerRegistry;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightConstraintCheckerRegistry;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightFitnessFunctionRegistry;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightSchedulerOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLongTermSequenceElementFilter;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.metaheuristic.tabu.TabuLightWeightSequenceOptimiser;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IAdpFrozenAssignmentProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class LightWeightSchedulerStage2Module extends AbstractModule {
	public static final String LIGHTWEIGHT_FITNESS_FUNCTION_NAMES = "LIGHTWEIGHT_FITNESS_FUNCTION_NAMES";
	public static final String LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	public static final String FULL_LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "FULL_LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	public static final String LIGHTWEIGHT_VESSELS = "LIGHTWEIGHT_VESSELS";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT";
	public static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT";
	public static final String LIGHTWEIGHT_INTIAL_SEQUENCES = "LIGHTWEIGHT_INITIAL_SEQUENCES";
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
	@Named(LIGHTWEIGHT_INTIAL_SEQUENCES)
	@Singleton
	private List<List<Integer>> getLightWeightInitialSequences(@NonNull final ILightWeightOptimisationData lightWeightOptimisationData, final IVesselProvider vesselProvider,
			final IPortSlotProvider portSlotProvider, @NonNull final IAdpFrozenAssignmentProvider frozenAssignmentProvider,
			@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences) {
		List<List<Integer>> result = new ArrayList<>();
		List<IVesselCharter> vessels = lightWeightOptimisationData.getVessels();
		for (int i = 0; i < vessels.size(); i++) {
			List<Integer> cargoes = new ArrayList<>();
			IResource resource = vesselProvider.getResource(vessels.get(i));
			ISequence sequence = initialSequences.getSequence(resource);
			for (int j = 0; j < sequence.size(); j++) {
				ISequenceElement element = sequence.get(j);
				IPortSlot portSlot = portSlotProvider.getPortSlot(element);
				if (portSlot.getPortType() != PortType.Load && portSlot.getPortType() != PortType.Discharge) {
					continue;
				}
				for (int cargoIndex = 0; cargoIndex < lightWeightOptimisationData.getShippedCargoes().size(); cargoIndex++) {
					List<IPortSlot> cargo = lightWeightOptimisationData.getShippedCargoes().get(cargoIndex);
					if (cargo.contains(portSlot)) {
						if (cargo.get(0) instanceof ILoadOption load && cargo.get(1) instanceof IDischargeOption discharge && frozenAssignmentProvider.isLockedToVessel(load, discharge))
							cargoes.add(cargoIndex);
						j += cargo.size() - 1;
						break;
					}
				}
			}
			result.add(cargoes);
		}
		return result;
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
	private List<IFullLightWeightConstraintChecker> getFullConstraintCheckers(Injector injector, FullLightWeightConstraintCheckerRegistry registry,
			@Named(FULL_LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES) List<String> names) {
		List<IFullLightWeightConstraintChecker> constraintCheckers = new LinkedList<>();
		Collection<IFullLightWeightConstraintCheckerFactory> constraintCheckerFactories = registry.getConstraintCheckerFactories();
		for (String name : names) {
			for (IFullLightWeightConstraintCheckerFactory lightWeightConstraintCheckerFactory : constraintCheckerFactories) {
				if (lightWeightConstraintCheckerFactory.getName().equals(name)) {
					IFullLightWeightConstraintChecker constraintChecker = lightWeightConstraintCheckerFactory.createConstraintChecker();
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
