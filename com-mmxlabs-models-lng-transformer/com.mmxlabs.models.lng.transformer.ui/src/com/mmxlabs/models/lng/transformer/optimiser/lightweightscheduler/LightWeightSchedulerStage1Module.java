/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import static com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightSchedulerStage2Module.LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES;
import static com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT;
import static com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT;
import static com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightSchedulerStage2Module.LIGHTWEIGHT_FITNESS_FUNCTION_NAMES;
import static com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightSchedulerStage2Module.LIGHTWEIGHT_VESSELS;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints.LightWeightShippingRestrictionsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.DefaultPNLLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.VesselCargoCountLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.CargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.GoogleORToolsLongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermOptimisationData;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LightWeightSchedulerStage1Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ICargoToCargoCostCalculator.class).to(SimpleCargoToCargoCostCalculator.class);
		bind(ICargoVesselRestrictionsMatrixProducer.class).to(CargoVesselRestrictionsMatrixProducer.class);

		bind(ILongTermMatrixOptimiser.class).to(GoogleORToolsLongTermMatrixOptimiser.class);

		bind(LongTermOptimisationData.class);

		bind(HashSetLongTermSlotsEditor.class).in(Singleton.class);
		bind(ILongTermSlotsProvider.class).to(HashSetLongTermSlotsEditor.class);
		bind(ILongTermSlotsProviderEditor.class).to(HashSetLongTermSlotsEditor.class);
	}

	@Provides
	@Named(LIGHTWEIGHT_VESSELS)
	private List<@NonNull IVesselAvailability> getVessels(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences, IVesselProvider vesselProvider) {
		return initialSequences.getResources().stream().sorted((a, b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)).filter(v -> LightWeightOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());
	}

	@Provides
	@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT)
	private int[] getDesiredNumberSlotsPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels, IVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
		int[] desiredVesselCargoCount = new int[vessels.size()];
		if (vesselSlotCountFitnessProvider != null) {
			for (int i = 0; i < vessels.size(); i++) {
				desiredVesselCargoCount[i] = vesselSlotCountFitnessProvider.getCountForVessel(vessels.get(i));
			}
		}
		return desiredVesselCargoCount;
	}

	@Provides
	@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT)
	private long[] getDesiredNumberSlotsWeightingPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels,
			IVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
		long[] vesselReward = new long[vessels.size()];
		if (vesselSlotCountFitnessProvider != null) {
			for (int i = 0; i < vessels.size(); i++) {
				vesselReward[i] = vesselSlotCountFitnessProvider.getWeightForVessel(vessels.get(i));
			}
		}
		return vesselReward;
	}

	@Provides
	@Singleton
	LightWeightConstraintCheckerRegistry getLightweightConstraintCheckerRegistry() {
		LightWeightConstraintCheckerRegistry registry = new LightWeightConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new LightWeightShippingRestrictionsConstraintCheckerFactory());
		return registry;
	}

	@Provides
	@Singleton
	LightWeightFitnessFunctionRegistry getLightweightFitnessFunctionRegistry() {
		LightWeightFitnessFunctionRegistry registry = new LightWeightFitnessFunctionRegistry();
		registry.registerFitnessFunctionFactory(new DefaultPNLLightWeightFitnessFunctionFactory());
		registry.registerFitnessFunctionFactory(new VesselCargoCountLightWeightFitnessFunctionFactory());
		return registry;
	}

	@Provides
	@Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES)
	List<String> getFitnessFunctionNames() {
		return CollectionsUtil.makeLinkedList("DefaultPNLLightWeightFitnessFunction", "VesselCargoCountLightWeightFitnessFunction");
	}

	@Provides
	@Named(LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES)
	List<String> getConstraintCheckerNames() {
		return CollectionsUtil.makeLinkedList("LightWeightShippingRestrictionsConstraintChecker");
	}
}
