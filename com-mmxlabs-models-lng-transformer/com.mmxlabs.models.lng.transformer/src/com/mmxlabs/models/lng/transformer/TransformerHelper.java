/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.TreeMap;

import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * @since 2.0
 */
public class TransformerHelper {

	public static IVesselClass buildIVesselClass(ISchedulerBuilder builder, VesselClass eVc, double baseFuelPrice) {
		final IVesselClass vc = builder.createVesselClass(eVc.getName(), OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMinSpeed()),
				OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMaxSpeed()), OptimiserUnitConvertor.convertToInternalVolume((int) (eVc.getFillCapacity() * eVc.getCapacity())),
				OptimiserUnitConvertor.convertToInternalVolume(eVc.getMinHeel()), OptimiserUnitConvertor.convertToInternalPrice(baseFuelPrice),
				OptimiserUnitConvertor.convertToInternalConversionFactor(eVc.getBaseFuel().getEquivalenceFactor()), OptimiserUnitConvertor.convertToInternalHourlyRate(eVc.getPilotLightRate()),
				eVc.getWarmingTime(), OptimiserUnitConvertor.convertToInternalVolume(eVc.getCoolingVolume()));

		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVc.getLadenAttributes());
		buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVc.getBallastAttributes());

		//
		// TODO: we don't have port type attributes in the model yet
		// so we kludge them by pulling VesselState-dependent out of the vessel class
		//

		builder.setVesselClassPortTypeParameters(vc, PortType.Load, OptimiserUnitConvertor.convertToInternalHourlyRate(eVc.getLadenAttributes().getInPortBaseRate()));
		builder.setVesselClassPortTypeParameters(vc, PortType.Discharge, OptimiserUnitConvertor.convertToInternalHourlyRate(eVc.getBallastAttributes().getInPortBaseRate()));

		return vc;
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
	public static void buildVesselStateAttributes(final ISchedulerBuilder builder, final IVesselClass vc, final com.mmxlabs.scheduler.optimiser.components.VesselState state,
			final VesselStateAttributes attrs) {
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (final FuelConsumption line : attrs.getFuelConsumption()) {
			keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(line.getSpeed()), (long) OptimiserUnitConvertor.convertToInternalHourlyRate(line.getConsumption()));
		}

		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);

		builder.setVesselClassStateParameters(vc, state, OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getNboRate()),
				OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getIdleNBORate()), OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getIdleBaseRate()), cc);
	}
}
