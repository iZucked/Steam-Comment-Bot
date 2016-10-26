/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 */
public class TransformerHelper {

	/**
	 */
	@NonNull
	public static IVesselClass buildIVesselClass(@NonNull final ISchedulerBuilder builder, @NonNull final VesselClass eVc, @NonNull final IBaseFuel baseFuel) {
		final IVesselClass vc = builder.createVesselClass(eVc.getName(), OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMinSpeed()),
				OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMaxSpeed()), OptimiserUnitConvertor.convertToInternalVolume((int) (eVc.getFillCapacity() * eVc.getCapacity())),
				OptimiserUnitConvertor.convertToInternalVolume(eVc.getMinHeel()), baseFuel, OptimiserUnitConvertor.convertToInternalDailyRate(eVc.getPilotLightRate()), eVc.getWarmingTime(),
				OptimiserUnitConvertor.convertToInternalVolume(eVc.getCoolingVolume()), OptimiserUnitConvertor.convertToInternalDailyRate(eVc.getMinBaseFuelConsumption()), eVc.isHasReliqCapability());
		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVc.getLadenAttributes());
		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVc.getBallastAttributes());

		//
		// TODO: we don't have port type attributes in the model yet
		// so we kludge them by pulling VesselState-dependent out of the vessel class
		//

		builder.setVesselClassPortTypeParameters(vc, PortType.Load, OptimiserUnitConvertor.convertToInternalDailyRate(eVc.getLadenAttributes().getInPortBaseRate()));
		builder.setVesselClassPortTypeParameters(vc, PortType.Discharge, OptimiserUnitConvertor.convertToInternalDailyRate(eVc.getBallastAttributes().getInPortBaseRate()));

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
	public static void buildVesselStateAttributes(@NonNull final ISchedulerBuilder builder, @NonNull final IVesselClass vc, final com.mmxlabs.scheduler.optimiser.components.@NonNull VesselState state,
			final VesselStateAttributes attrs) {
		final TreeMap<Integer, Long> keypoints = new TreeMap<>();

		int minSpeed = Integer.MAX_VALUE;
		int maxSpeed = Integer.MIN_VALUE;
		for (final FuelConsumption line : attrs.getFuelConsumption()) {
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

		builder.setVesselClassStateParameters(vc, state, OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getNboRate()),
				OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getIdleNBORate()), OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getIdleBaseRate()), cc,
				OptimiserUnitConvertor.convertToInternalSpeed(attrs.getServiceSpeed()), OptimiserUnitConvertor.convertToInternalDailyRate(attrs.getInPortNBORate()) );
	}
}
