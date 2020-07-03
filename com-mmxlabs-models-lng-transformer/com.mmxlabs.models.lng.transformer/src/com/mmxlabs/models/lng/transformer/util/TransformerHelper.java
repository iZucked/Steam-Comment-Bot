/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 */
public class TransformerHelper {

	/**
	 */
	@NonNull
	public static IVessel buildIVessel(@NonNull final ISchedulerBuilder builder, Vessel eVessel, @NonNull final IBaseFuel baseFuel, @NonNull final IBaseFuel idleBaseFuel,
			@NonNull final IBaseFuel inPortBaseFuel, @NonNull final IBaseFuel pilotLightBaseFuel) {

		@NonNull
		final IVessel vc = builder.createVessel(eVessel.getName(), //
				OptimiserUnitConvertor.convertToInternalSpeed(eVessel.getVesselOrDelegateMinSpeed()), //
				OptimiserUnitConvertor.convertToInternalSpeed(eVessel.getVesselOrDelegateMaxSpeed()), //
				OptimiserUnitConvertor.convertToInternalVolume((int) (eVessel.getVesselOrDelegateCapacity() * eVessel.getVesselOrDelegateFillCapacity())), //
				OptimiserUnitConvertor.convertToInternalVolume(eVessel.getVesselOrDelegateSafetyHeel()), //
				baseFuel, idleBaseFuel, inPortBaseFuel, pilotLightBaseFuel, //
				OptimiserUnitConvertor.convertToInternalDailyRate(eVessel.getVesselOrDelegatePilotLightRate()), // 
				eVessel.getVesselOrDelegateWarmingTime(),
				eVessel.getVesselOrDelegatePurgeTime(),
				OptimiserUnitConvertor.convertToInternalVolume(eVessel.getVesselOrDelegateCoolingVolume()),
				OptimiserUnitConvertor.convertToInternalDailyRate(eVessel.getVesselOrDelegateMinBaseFuelConsumption()), //
				eVessel.getVesselOrDelegateHasReliqCapability());

		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVessel.getLadenAttributes());
		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVessel.getBallastAttributes());

		//
		// TODO: we don't have port type attributes in the model yet
		// so we kludge them by pulling VesselState-dependent out of the vessel
		//

		builder.setVesselPortTypeParameters(vc, PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(eVessel.getLadenAttributes().getVesselOrDelegateInPortBaseRate()));
		builder.setVesselPortTypeParameters(vc, PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(eVessel.getBallastAttributes().getVesselOrDelegateInPortBaseRate()));

		return vc;
	}

	@NonNull
	public static IBaseFuel buildBaseFuel(@NonNull final ISchedulerBuilder builder, @NonNull final BaseFuel eBF) {
		final IBaseFuel bf = builder.createBaseFuel(eBF.getName(), OptimiserUnitConvertor.convertToInternalConversionFactor(eBF.getEquivalenceFactor()));
		return bf;
	}

	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet model
	 * 
	 * @param builder
	 *            the builder which is currently in use
	 * @param vc
	 *            the {@link IVesselClass} which the builder has constructed whose attributes we are setting
	 * @param laden
	 *            the {@link com.mmxlabs.scheduler.optimiser.components.VesselState} we are setting attributes for
	 * @param ladenAttributes
	 *            the {@link VesselStateAttributes} from the EMF model
	 */
	public static void buildVesselStateAttributes(@NonNull final ISchedulerBuilder builder, @NonNull final IVessel vc, final com.mmxlabs.scheduler.optimiser.components.@NonNull VesselState state,
			final VesselStateAttributes attrs) {
		final TreeMap<Integer, Long> keypoints = new TreeMap<>();

		int minSpeed = Integer.MAX_VALUE;
		int maxSpeed = Integer.MIN_VALUE;
		for (final FuelConsumption line : attrs.getVesselOrDelegateFuelConsumption()) {
			final int speed = OptimiserUnitConvertor.convertToInternalSpeed(line.getSpeed());
			keypoints.put(speed, (long) OptimiserUnitConvertor.convertToInternalDailyRate(line.getConsumption()));
			if (speed > maxSpeed) {
				maxSpeed = speed;
			}
			if (speed < minSpeed) {
				minSpeed = speed;
			}
		}

		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(minSpeed, maxSpeed, consumptionCalculator);

		builder.setVesselStateParameters(vc, state, OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getVesselOrDelegateNBORate()),
				OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getVesselOrDelegateIdleNBORate()), OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getVesselOrDelegateIdleBaseRate()),
				cc, OptimiserUnitConvertor.convertToInternalSpeed(attrs.getVesselOrDelegateServiceSpeed()),
				OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getVesselOrDelegateInPortNBORate()));
	}
}
