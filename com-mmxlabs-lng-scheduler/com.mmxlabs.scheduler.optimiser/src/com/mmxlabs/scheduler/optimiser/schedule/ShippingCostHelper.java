/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.chartercontracts.CharterContractConstants;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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

		return plan.getBaseFuelCost() + plan.getCooldownCost();
	}

	public long getPortCosts(final @NonNull VoyagePlan plan) {

		long portCosts = 0;
		final Object[] sequence = plan.getSequence();
		final int offset = plan.isIgnoreEnd() ? 1 : 0;
		for (int i = 0; i < sequence.length - offset; ++i) {
			final Object obj = sequence[i];
			if (obj instanceof final PortDetails portDetails) {
				portCosts += portDetails.getPortCosts();
			}

		}
		return portCosts;
	}

	public long getRouteExtraCosts(final @NonNull VoyagePlan plan) {

		return plan.getTotalRouteCost();
	}

	public long getHireCosts(final @NonNull VoyagePlan plan) {
		return plan.getCharterCost();
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
			if (o instanceof final VoyageDetails voyageDetails) {
				planDuration += voyageDetails.getTravelTime();
				planDuration += voyageDetails.getIdleTime();
				planDuration += voyageDetails.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE);
			} else {
				planDuration += ((PortDetails) o).getOptions().getVisitDuration();
			}
		}
		return planDuration;
	}

	/**
	 * Check if a plan has a generated charter out event at the start
	 * 
	 * @param plan
	 * @return
	 */
	public boolean hasGeneratedCharterOut(final @NonNull VoyagePlan plan) {
		final Object obj = plan.getSequence()[0];
		if (obj instanceof final PortDetails portDetails) {
			if (portDetails.getOptions().getPortSlot().getPortType() == PortType.GeneratedCharterOut) {
				return true;
			}
		}
		return false;
	}

	public long getShippingCosts(final @NonNull VoyagePlan plan, final @NonNull IVesselCharter vesselCharter, final boolean includeCharterInCosts) {

		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
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
			if (obj instanceof final PortDetails portDetails) {
				final @NonNull IPortSlot slot = portDetails.getOptions().getPortSlot();
				if (actualsDataProvider != null && actualsDataProvider.hasActuals(slot)) {
					capacityCosts += actualsDataProvider.getCapacityCosts(slot);
					crewBonusCosts += actualsDataProvider.getCrewBonusCosts(slot);
					insuranceCosts += actualsDataProvider.getInsuranceCosts(slot);
				}
			}
		}

		final long shippingCosts = getRouteExtraCosts(plan) + getFuelCosts(plan);
		final long portCosts = getPortCosts(plan);
		final long hireCosts = includeCharterInCosts ? plan.getCharterCost() : 0L;

		return shippingCosts + portCosts + hireCosts + capacityCosts + crewBonusCosts + insuranceCosts;
	}

	public long calculateBBCost(final IPortTimesRecord portTimesRecord, final @NonNull IPortSlot portSlot, final @NonNull IVesselCharter vesselCharter, final VesselStartState vesselStartState, final PreviousHeelRecord heelRecord) {

		boolean applyContractCosts;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			applyContractCosts = portSlot.getPortType() == PortType.Discharge || portSlot.getPortType() == PortType.Round_Trip_Cargo_End;
		} else {
			applyContractCosts = portSlot.getPortType() == PortType.End;
		}

		if (applyContractCosts) {
			final @Nullable ICharterContract charterContract = vesselCharter.getCharterContract();
			if (charterContract != null) {
				return charterContract.calculateBBCost(portTimesRecord, portSlot, vesselCharter, vesselStartState, heelRecord);
			}
		}

		return 0L;

	}

	public long calculateRFRevenue(final IPortTimesRecord portTimesRecord, final @NonNull IPortSlot portSlot, final @NonNull IVesselCharter vesselCharter) {

		boolean applyContractCosts;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			applyContractCosts = portSlot.getPortType() == PortType.Load;
		} else {
			applyContractCosts = portSlot.getPortType() == PortType.Start;
		}

		if (applyContractCosts) {
			final @Nullable ICharterContract charterContract = vesselCharter.getCharterContract();
			if (charterContract != null) {
				return charterContract.calculateRFRevenue(portTimesRecord, vesselCharter);
			}
		}

		return 0L;

	}

	public void annotateBB(final IPortTimesRecord portTimesRecord, final DetailTree shippingDetails, final IPortSlot portSlot, @NonNull final IVesselCharter vesselCharter, final VesselStartState vesselStartState, final PreviousHeelRecord heelRecord) {
		boolean applyContractCosts;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			applyContractCosts = portSlot.getPortType() == PortType.Discharge || portSlot.getPortType() == PortType.Round_Trip_Cargo_End;
		} else {
			applyContractCosts = portSlot.getPortType() == PortType.End;
		}

		if (applyContractCosts) {
			final @Nullable ICharterContract charterContract = vesselCharter.getCharterContract();
			if (charterContract != null) {
				final ICharterContractAnnotation annotation = charterContract.annotateBB(portTimesRecord, portSlot, vesselCharter, vesselStartState, heelRecord);
				shippingDetails.addChild(CharterContractConstants.BALLAST_BONUS_KEY, annotation);
			}
		}
	}

	public void annotateRF(final IPortTimesRecord portTimesRecord, final DetailTree shippingDetails, final IPortSlot portSlot, @NonNull final IVesselCharter vesselCharter) {
		boolean applyContractCosts;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			applyContractCosts = portSlot.getPortType() == PortType.Load;
		} else {
			applyContractCosts = portSlot.getPortType() == PortType.Start;
		}

		if (applyContractCosts) {
			final @Nullable ICharterContract charterContract = vesselCharter.getCharterContract();
			if (charterContract != null) {
				final ICharterContractAnnotation annotation = charterContract.annotateRF(portTimesRecord, vesselCharter);
				shippingDetails.addChild(CharterContractConstants.REPOSITIONING_FEE_KEY, annotation);
			}
		}
	}

	public long @NonNull [] getSeperatedShippingCosts(final @NonNull VoyagePlan plan, final @NonNull IVesselCharter vesselCharter, final boolean includeCharterInCosts) {
		final long @NonNull [] costs = new long[4];
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return costs;
		}

		final long shippingCosts = getRouteExtraCosts(plan) + getFuelCosts(plan);
		final long portCosts = getPortCosts(plan);
		final long hireCosts = includeCharterInCosts ? plan.getCharterCost() : 0L;
		final long shippingDays = includeCharterInCosts ? getDurationInDays(plan) : 0L;

		costs[0] = shippingCosts;
		costs[1] = portCosts;
		costs[2] = hireCosts;
		costs[3] = shippingDays;

		return costs;
	}
}
