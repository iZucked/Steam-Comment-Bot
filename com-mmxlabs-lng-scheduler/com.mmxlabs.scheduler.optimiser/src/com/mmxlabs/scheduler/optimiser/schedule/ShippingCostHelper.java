/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
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
	private IActualsDataProvider actualsDataProvider;

	public long getFuelCosts(final @NonNull VoyagePlan plan) {

		// @formatter:off
		final long fuelCost = plan.getTotalFuelCost(FuelComponent.Base)
				+ plan.getTotalFuelCost(FuelComponent.Base_Supplemental)
				+ plan.getTotalFuelCost(FuelComponent.Cooldown)
				+ plan.getTotalFuelCost(FuelComponent.IdleBase)
				+ plan.getTotalFuelCost(FuelComponent.IdlePilotLight) 
				+ plan.getTotalFuelCost(FuelComponent.PilotLight);
		// @formatter:on
		return fuelCost;
	}

	public long getPortCosts(final @NonNull IVessel vessel, final @NonNull VoyagePlan plan) {

		long portCosts = 0;
		final Object[] sequence = plan.getSequence();
		final int offset = plan.isIgnoreEnd() ? 1 : 0;
		for (int i = 0; i < sequence.length - offset; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof PortDetails) {

				final PortDetails portDetails = (PortDetails) obj;
				portCosts += portDetails.getPortCosts();
			}

		}
		return portCosts;
	}

	public long getRouteExtraCosts(final @NonNull VoyagePlan plan) {

		return plan.getTotalRouteCost();
	}

	public long getHireCosts(final @NonNull VoyagePlan plan) {
		final long planDuration = getPlanDurationInHours(plan);

		final long hireRatePerDay = plan.getCharterInRatePerDay();
		final long hireCosts = hireRatePerDay * planDuration / 24L;
		return hireCosts;
	}

	private long getDurationInDays(final @NonNull VoyagePlan plan) {
		final long planDuration = getPlanDurationInHours(plan);
		return Calculator.ScaleFactor * planDuration / 24L;
	}

	public int getPlanDurationInHours(final @NonNull VoyagePlan plan) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int offset = plan.isIgnoreEnd() ? 1 : 0;
		final int k = sequence.length - offset;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				planDuration += voyageDetails.getTravelTime();
				planDuration += voyageDetails.getIdleTime();
			} else {
				planDuration += ((PortDetails) o).getOptions().getVisitDuration();
			}
		}
		return planDuration;
	}

	/**
	 * Calculate revenue for the idle time method of generating charter outs
	 * 
	 * @param plan
	 * @param vesselAvailability
	 * @return
	 */
	public long getIdleTimeGeneratedCharterOutRevenue(final @NonNull VoyagePlan plan, final @NonNull IVesselAvailability vesselAvailability) {
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

	/**
	 * Check if plan contains an idle time generated charter out
	 * 
	 * @param plan
	 * @return
	 */
	public boolean hasIdleTimeGeneratedCharterOut(final @NonNull VoyagePlan plan) {
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

	/**
	 * Calculate costs for the idle time method of generating charter outs
	 * 
	 * @param plan
	 * @return
	 */
	public long getIdleTimeGeneratedCharterOutCosts(final @NonNull VoyagePlan plan) {
		int planDuration = 0;
		for (final Object obj : plan.getSequence()) {

			if (obj instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().isCharterOutIdleTime()) {
					planDuration += voyageDetails.getIdleTime();
				}
			}
		}
		final long hireRatePerDay = plan.getCharterInRatePerDay();
		final long hireCosts = hireRatePerDay * (long) planDuration / 24L;

		return hireCosts;
	}

	/**
	 * Check if a plan has a generated charter out event at the start
	 * 
	 * @param plan
	 * @return
	 */
	public boolean hasGeneratedCharterOut(final @NonNull VoyagePlan plan) {
		final Object obj = plan.getSequence()[0];
		if (obj instanceof PortDetails) {
			final PortDetails portDetails = (PortDetails) obj;
			if (portDetails.getOptions().getPortSlot().getPortType() == PortType.GeneratedCharterOut) {
				return true;
			}
		}
		return false;
	}

	public long getShippingCosts(final @NonNull VoyagePlan plan, final @NonNull IVesselAvailability vesselAvailability, final boolean includeCharterInCosts) {

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return 0L;
		}
		long capacityCosts = 0;
		long crewBonusCosts = 0;
		long insuranceCosts = 0;
		// Add in actuals
		final Object[] sequence = plan.getSequence();
		final int offset = plan.isIgnoreEnd() ? 1 : 0;
		for (int i = 0; i < sequence.length - offset; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof PortDetails) {

				final PortDetails portDetails = (PortDetails) obj;
				@NonNull
				final IPortSlot slot = portDetails.getOptions().getPortSlot();
				if (actualsDataProvider.hasActuals(slot)) {
					capacityCosts += actualsDataProvider.getCapacityCosts(slot);
					crewBonusCosts += actualsDataProvider.getCrewBonusCosts(slot);
					insuranceCosts += actualsDataProvider.getInsuranceCosts(slot);
				}
			}
		}

		final long shippingCosts = getRouteExtraCosts(plan) + getFuelCosts(plan);
		final long portCosts = getPortCosts(vesselAvailability.getVessel(), plan);
		final long hireCosts = includeCharterInCosts ? getHireCosts(plan) : 0L;

		return shippingCosts + portCosts + hireCosts + capacityCosts + crewBonusCosts + insuranceCosts;
	}

	public long getShippingRepositioningCost(final @NonNull IPortSlot portSlot, final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime) {
		if (portSlot.getPortType() == PortType.Start) {
			return vesselAvailability.getRepositioningFee().getValueAtPoint(vesselStartTime);
		}
		return 0L;
	}

	public long getShippingBallastBonusCost(final @NonNull IPortSlot portSlot, final @NonNull IVesselAvailability vesselAvailability, final int vesselEndTime) {
		if (portSlot.getPortType() == PortType.End) {
			return vesselAvailability.getBallastBonus().getValueAtPoint(vesselEndTime);
		}
		return 0L;
	}

	public long @NonNull [] getSeperatedShippingCosts(final @NonNull VoyagePlan plan, final @NonNull IVesselAvailability vesselAvailability, final boolean includeCharterInCosts) {
		final long @NonNull [] costs = new long[4];
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return costs;
		}

		final long shippingCosts = getRouteExtraCosts(plan) + getFuelCosts(plan);
		final long portCosts = getPortCosts(vesselAvailability.getVessel(), plan);
		final long hireCosts = includeCharterInCosts ? getHireCosts(plan) : 0L;
		final long shippingDays = includeCharterInCosts ? getDurationInDays(plan) : 0L;

		costs[0] = shippingCosts;
		costs[1] = portCosts;
		costs[2] = hireCosts;
		costs[3] = shippingDays;

		return costs;
	}
}
