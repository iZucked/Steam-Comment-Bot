/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.notional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A annotation class for single load, single discharge voyages.
 * 
 */
public class LDShippingAnnotationHelper {

	public static void extractLoadDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {

		final PortDetails loadDetails = (PortDetails) realPlan.getSequence()[0];
		// Load Details
		shippingAnnotation.loadPort = loadDetails.getOptions().getPortSlot().getPort();
		final IVessel vessel = loadDetails.getOptions().getVessel();

		shippingAnnotation.loadBunkersInMT += loadDetails.getFuelConsumption(vessel.getInPortBaseFuelInMT());

		shippingAnnotation.loadHireHours = loadDetails.getOptions().getVisitDuration();
		shippingAnnotation.loadPortCosts = loadDetails.getPortCosts();
		shippingAnnotation.startOfLoading = allocationAnnotation.getSlotTime(loadDetails.getOptions().getPortSlot());
		shippingAnnotation.completionOfLoading = shippingAnnotation.startOfLoading + allocationAnnotation.getSlotDuration(loadDetails.getOptions().getPortSlot());
	}

	public static void extractLoadDetails(final @NonNull VoyagePlan realPlan, final int startOfLoadingTime, final int loadDuration, final @NonNull LDShippingAnnotation shippingAnnotation) {

		final PortDetails loadDetails = (PortDetails) realPlan.getSequence()[0];
		// Load Details
		shippingAnnotation.loadPort = loadDetails.getOptions().getPortSlot().getPort();
		final IVessel vessel = loadDetails.getOptions().getVessel();

		shippingAnnotation.loadBunkersInMT += loadDetails.getFuelConsumption(vessel.getInPortBaseFuelInMT());
		shippingAnnotation.loadHireHours = loadDetails.getOptions().getVisitDuration();
		shippingAnnotation.loadPortCosts = loadDetails.getPortCosts();
		shippingAnnotation.startOfLoading = startOfLoadingTime;
		shippingAnnotation.completionOfLoading = shippingAnnotation.startOfLoading + loadDuration;
	}

	public static void extractLadenDetails(final @NonNull VoyagePlan realPlan, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final VoyageDetails ladenLeg = (VoyageDetails) realPlan.getSequence()[1];

		shippingAnnotation.ladenRoute = ladenLeg.getOptions().getRoute();
		shippingAnnotation.ladenDistance = ladenLeg.getOptions().getDistance();
		shippingAnnotation.ladenHireHours = ladenLeg.getTravelTime() + ladenLeg.getIdleTime();
		shippingAnnotation.ladenSpeed = ladenLeg.getSpeed();
		shippingAnnotation.ladenCanalCosts = ladenLeg.getOptions().getRouteCost();
		final IVessel vessel = ladenLeg.getOptions().getVessel();

		for (FuelKey fk : vessel.getVoyageFuelKeys()) {
			shippingAnnotation.ladenBunkersInMT += ladenLeg.getFuelConsumption(fk);
			shippingAnnotation.ladenBunkersInMT += ladenLeg.getRouteAdditionalConsumption(fk);
		}
		// lng
		long ladenBOGInM3 = 0L;

		for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
			ladenBOGInM3 += ladenLeg.getFuelConsumption(fk);
			ladenBOGInM3 += ladenLeg.getRouteAdditionalConsumption(fk);
		}
		shippingAnnotation.ladenBOInM3 = ladenBOGInM3;
		shippingAnnotation.ladenBOInMMBTu = Calculator.convertM3ToMMBTu(ladenBOGInM3, ladenLeg.getOptions().getCargoCVValue());
	}

	public static void extractDischargeDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final PortDetails dischargeDetails = (PortDetails) realPlan.getSequence()[2];

		// Discharge Details
		shippingAnnotation.dischargePort = dischargeDetails.getOptions().getPortSlot().getPort();
		final IVessel vessel = dischargeDetails.getOptions().getVessel();
		shippingAnnotation.dischargeBunkersInMT += dischargeDetails.getFuelConsumption(vessel.getInPortBaseFuelInMT());
		shippingAnnotation.dischargeHireHours = dischargeDetails.getOptions().getVisitDuration();
		shippingAnnotation.dischargePortCosts = dischargeDetails.getPortCosts();
		shippingAnnotation.startOfDischarge = allocationAnnotation.getSlotTime(dischargeDetails.getOptions().getPortSlot());
		shippingAnnotation.completionOfDischarge = shippingAnnotation.startOfDischarge + allocationAnnotation.getSlotDuration(dischargeDetails.getOptions().getPortSlot());
	}

	public static void extractDischargeDetails(final @NonNull VoyagePlan realPlan, final int startOfDischargeTime, final int dischargeDuration,
			final @NonNull LDShippingAnnotation shippingAnnotation) {
		final PortDetails dischargeDetails = (PortDetails) realPlan.getSequence()[2];

		// Discharge Details
		shippingAnnotation.dischargePort = dischargeDetails.getOptions().getPortSlot().getPort();
		final IVessel vessel = dischargeDetails.getOptions().getVessel();
		shippingAnnotation.dischargeBunkersInMT += dischargeDetails.getFuelConsumption(vessel.getInPortBaseFuelInMT());
		shippingAnnotation.dischargeHireHours = dischargeDetails.getOptions().getVisitDuration();
		shippingAnnotation.dischargePortCosts = dischargeDetails.getPortCosts();
		shippingAnnotation.startOfDischarge = startOfDischargeTime;
		shippingAnnotation.completionOfDischarge = shippingAnnotation.startOfDischarge + dischargeDuration;
	}

	public static void extractBallastDetails(final @NonNull VoyagePlan realPlan, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final VoyageDetails ballastLeg = (VoyageDetails) realPlan.getSequence()[3];

		shippingAnnotation.ballastRoute = ballastLeg.getOptions().getRoute();
		shippingAnnotation.ballastDistance = ballastLeg.getOptions().getDistance();
		shippingAnnotation.ballastHireHours = ballastLeg.getTravelTime() + ballastLeg.getIdleTime() + ballastLeg.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE);
		shippingAnnotation.ballastSpeed = ballastLeg.getSpeed();
		shippingAnnotation.ballastCanalCosts = ballastLeg.getOptions().getRouteCost();

		final IVessel vessel = ballastLeg.getOptions().getVessel();

		for (FuelKey fk : vessel.getVoyageFuelKeys()) {
			shippingAnnotation.ballastBunkersInMT += ballastLeg.getFuelConsumption(fk);
			shippingAnnotation.ballastBunkersInMT += ballastLeg.getRouteAdditionalConsumption(fk);
		}

		// lng
		long ballastBOGInM3 = 0L;
		for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
			ballastBOGInM3 += ballastLeg.getFuelConsumption(fk);
			ballastBOGInM3 += ballastLeg.getRouteAdditionalConsumption(fk);
		}
		shippingAnnotation.ballastBOInM3 = ballastBOGInM3;
		shippingAnnotation.ballastBOInMMBTu = Calculator.convertM3ToMMBTu(ballastBOGInM3, ballastLeg.getOptions().getCargoCVValue());
	}

	public static void extractReturnDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final PortDetails returnDetails = (PortDetails) realPlan.getSequence()[4];

		// Discharge Details
		shippingAnnotation.returnPort = returnDetails.getOptions().getPortSlot().getPort();
		IPortSlot returnSlot = allocationAnnotation.getReturnSlot();
		if (returnSlot != null) {
			shippingAnnotation.nextPortDate = allocationAnnotation.getSlotTime(returnSlot);
		}
	}

	public static void updateBunkerCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final int refBaseFuelPrice) {

		shippingAnnotation.loadBunkersCost += Calculator.costFromConsumption(shippingAnnotation.loadBunkersInMT, refBaseFuelPrice);
		shippingAnnotation.ladenBunkersCost += Calculator.costFromConsumption(shippingAnnotation.ladenBunkersInMT, refBaseFuelPrice);
		shippingAnnotation.dischargeBunkersCost += Calculator.costFromConsumption(shippingAnnotation.dischargeBunkersInMT, refBaseFuelPrice);
		shippingAnnotation.ballastBunkersCost += Calculator.costFromConsumption(shippingAnnotation.ballastBunkersInMT, refBaseFuelPrice);
	}

	public static void updateBunkerCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final int voyageBunkerPricePerMT, final int portBunkerPricePerMT) {

		shippingAnnotation.loadBunkersCost = Calculator.costFromConsumption(shippingAnnotation.loadBunkersInMT, portBunkerPricePerMT);
		shippingAnnotation.ladenBunkersCost = Calculator.costFromConsumption(shippingAnnotation.ladenBunkersInMT, voyageBunkerPricePerMT);
		shippingAnnotation.dischargeBunkersCost = Calculator.costFromConsumption(shippingAnnotation.dischargeBunkersInMT, portBunkerPricePerMT);
		shippingAnnotation.ballastBunkersCost = Calculator.costFromConsumption(shippingAnnotation.ballastBunkersInMT, voyageBunkerPricePerMT);
	}

	public static void updateLNGCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final int lngCostPerMMBTu) {

		shippingAnnotation.ladenBOCost = Calculator.costFromConsumption(shippingAnnotation.ladenBOInMMBTu, lngCostPerMMBTu);
		shippingAnnotation.ballastBOCost = Calculator.costFromConsumption(shippingAnnotation.ballastBOInMMBTu, lngCostPerMMBTu);
	}

	public static void updateCharterCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final long charterCostPerDay) {
		shippingAnnotation.loadCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.loadHireHours) / 24L;
		shippingAnnotation.ladenCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.ladenHireHours) / 24L;
		shippingAnnotation.dischargeCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.dischargeHireHours) / 24L;
		shippingAnnotation.ballastCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.ballastHireHours) / 24L;
	}

	public static void updateCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final long charterCostPerDay, final int lngCostPerMMBTu, final int referenceBaseFuelRate) {
		updateCharterCosts(shippingAnnotation, charterCostPerDay);
		updateBunkerCosts(shippingAnnotation, referenceBaseFuelRate);
		updateLNGCosts(shippingAnnotation, lngCostPerMMBTu);
		// TODO: neccessary?
		shippingAnnotation.ladenCostsExcludingBOG = shippingAnnotation.ladenBunkersCost + shippingAnnotation.ladenCanalCosts + shippingAnnotation.ladenCharterCost + shippingAnnotation.ladenMiscCosts;
		shippingAnnotation.ladenCostsIncludingBOG = shippingAnnotation.ladenCostsExcludingBOG + shippingAnnotation.ladenBOCost;

		shippingAnnotation.ballastCostsExcludingBOG = shippingAnnotation.ballastBunkersCost + shippingAnnotation.ballastCanalCosts + shippingAnnotation.ballastCharterCost
				+ shippingAnnotation.ballastMiscCosts;
		shippingAnnotation.ballastCostsIncludingBOG = shippingAnnotation.ballastCostsExcludingBOG + shippingAnnotation.ballastBOCost;
	}

	public static void updateTotalCosts(final @NonNull LDShippingAnnotation shippingAnnotation) {
		shippingAnnotation.ladenCostsExcludingBOG = 0;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.loadPortCosts;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.loadCharterCost;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.loadBunkersCost;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.loadMiscCosts;

		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.ladenCharterCost;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.ladenBunkersCost;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.ladenCanalCosts;
		shippingAnnotation.ladenCostsExcludingBOG += shippingAnnotation.ladenMiscCosts;

		shippingAnnotation.ladenCostsIncludingBOG = shippingAnnotation.ladenCostsExcludingBOG + shippingAnnotation.ladenBOCost;

		shippingAnnotation.ballastCostsExcludingBOG = 0;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.dischargePortCosts;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.dischargeCharterCost;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.dischargeBunkersCost;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.dischargeMiscCosts;

		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.ballastCharterCost;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.ballastBunkersCost;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.ballastCanalCosts;
		shippingAnnotation.ballastCostsExcludingBOG += shippingAnnotation.ballastMiscCosts;

		shippingAnnotation.ballastCostsIncludingBOG = shippingAnnotation.ballastBOCost + shippingAnnotation.ballastCostsExcludingBOG;

	}

	public static long getTotalShippingCost(final @NonNull LDShippingAnnotation shippingAnnotation, final boolean includeBOG) {

		long totalCosts = 0L;

		totalCosts += shippingAnnotation.loadPortCosts;
		totalCosts += shippingAnnotation.loadCharterCost;
		totalCosts += shippingAnnotation.loadBunkersCost;
		totalCosts += shippingAnnotation.loadMiscCosts;

		totalCosts += shippingAnnotation.ladenCharterCost;
		totalCosts += shippingAnnotation.ladenBunkersCost;
		totalCosts += shippingAnnotation.ladenCanalCosts;
		totalCosts += shippingAnnotation.ladenMiscCosts;
		if (includeBOG) {
			totalCosts += shippingAnnotation.ladenBOCost;
		}

		totalCosts += shippingAnnotation.dischargePortCosts;
		totalCosts += shippingAnnotation.dischargeCharterCost;
		totalCosts += shippingAnnotation.dischargeBunkersCost;
		totalCosts += shippingAnnotation.dischargeMiscCosts;

		totalCosts += shippingAnnotation.ballastCharterCost;
		totalCosts += shippingAnnotation.ballastBunkersCost;
		totalCosts += shippingAnnotation.ballastCanalCosts;
		totalCosts += shippingAnnotation.ballastMiscCosts;

		if (includeBOG) {
			totalCosts += shippingAnnotation.ballastBOCost;
		}

		totalCosts += shippingAnnotation.cooldownCost;

		return totalCosts;
	}
}