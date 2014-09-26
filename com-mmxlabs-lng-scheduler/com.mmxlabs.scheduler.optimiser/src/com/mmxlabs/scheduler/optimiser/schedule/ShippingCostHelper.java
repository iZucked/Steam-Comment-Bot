/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import javax.inject.Inject;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Helper class to get various raw costs from a {@link VoyagePlan}
 * 
 * @author Simon Goodall
 * 
 */
public class ShippingCostHelper {

	@Inject
	private IPortCostProvider portCostProvider;

	public long getFuelCosts(final VoyagePlan plan, boolean includeLNG) {
		// @formatter:off
		long fuelCost = plan.getTotalFuelCost(FuelComponent.Base)
				+ plan.getTotalFuelCost(FuelComponent.Base_Supplemental)
				+ plan.getTotalFuelCost(FuelComponent.Cooldown)
				+ plan.getTotalFuelCost(FuelComponent.IdleBase)
				+ plan.getTotalFuelCost(FuelComponent.IdlePilotLight) 
				+ plan.getTotalFuelCost(FuelComponent.PilotLight)
				+ (includeLNG ? plan.getTotalFuelCost(FuelComponent.NBO) 
						+ plan.getTotalFuelCost(FuelComponent.FBO)
						+ plan.getTotalFuelCost(FuelComponent.IdleNBO) : 0);
		// @formatter:on
		return fuelCost;
	}

	public long getPortCosts(final IVessel vessel, final VoyagePlan plan) {

		long portCosts = 0;
		final Object[] sequence = plan.getSequence();
		for (int i = 0; i < sequence.length - 1; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof PortDetails) {

				final PortDetails portDetails = (PortDetails) obj;
				portCosts += portDetails.getPortCosts();
			}

		}
		return portCosts;
	}

	public long getRouteExtraCosts(final VoyagePlan plan) {

		return plan.getTotalRouteCost();
	}

	public long getHireCosts(final VoyagePlan plan) {

		final long planDuration = getPlanDurationInHours(plan, false);

		final int hireRatePerDay = plan.getCharterInRatePerDay();
		long hireCosts = (long) hireRatePerDay * (long) planDuration / 24L;
		return hireCosts;
	}

	public int getPlanDurationInHours(final VoyagePlan plan, boolean includeCharterOutTime) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int k = sequence.length - 1;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				planDuration += voyageDetails.getTravelTime();
				if (includeCharterOutTime || !voyageDetails.getOptions().isCharterOutIdleTime()) {
					planDuration += voyageDetails.getIdleTime();
				}
			} else {
				planDuration += ((PortDetails) o).getOptions().getVisitDuration();
			}
		}
		return planDuration;
	}

	public long getGeneratedCharterOutRevenue(final VoyagePlan plan, final IVessel vessel) {
		long charterRevenue = 0;

		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					final long hourlyCharterOutPrice = voyageDetails.getOptions().getCharterOutDailyRate();
					charterRevenue += Calculator.quantityFromRateTime(hourlyCharterOutPrice, voyageDetails.getIdleTime()) / 24L;
				}
			}
		}
		return charterRevenue;
	}

	public boolean hasGeneratedCharterOut(final VoyagePlan plan) {
		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					return true;
				}
			}
		}
		return false;
	}

	public long getGeneratedCharterOutCosts(final VoyagePlan plan) {

		int planDuration = 0;
		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					planDuration += voyageDetails.getIdleTime();
				}
			}
		}

		final int hireRatePerDay = plan.getCharterInRatePerDay();
		final long hireCosts = (long) hireRatePerDay * (long) planDuration / 24L;

		return hireCosts;
	}

	public long getShippingCosts(final VoyagePlan plan, final IVessel vessel, final boolean includeLNG, final boolean includeCharterCosts) {

		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return 0L;
		}

		final long shippingCosts = getRouteExtraCosts(plan) + getFuelCosts(plan, includeLNG);
		final long portCosts = getPortCosts(vessel, plan);
		final long hireCosts = includeCharterCosts ? getHireCosts(plan) : 0L;

		return shippingCosts + portCosts + hireCosts;
	}
}
