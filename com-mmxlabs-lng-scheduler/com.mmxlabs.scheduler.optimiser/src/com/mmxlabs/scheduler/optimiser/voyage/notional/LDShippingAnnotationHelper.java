/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.notional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
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
		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
			shippingAnnotation.loadBunkersInMT += loadDetails.getFuelConsumption(fc);
		}
		shippingAnnotation.loadHireHours = loadDetails.getOptions().getVisitDuration();
		shippingAnnotation.loadPortCosts = loadDetails.getPortCosts();
		shippingAnnotation.startOfLoading = allocationAnnotation.getSlotTime(loadDetails.getOptions().getPortSlot());
		shippingAnnotation.completionOfLoading = shippingAnnotation.startOfLoading + allocationAnnotation.getSlotDuration(loadDetails.getOptions().getPortSlot());
	}

	public static void extractLadenDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final VoyageDetails ladenLeg = (VoyageDetails) realPlan.getSequence()[1];

		shippingAnnotation.ladenRoute = ladenLeg.getOptions().getRoute();
		shippingAnnotation.ladenDistance = ladenLeg.getOptions().getDistance();
		shippingAnnotation.ladenHireHours = ladenLeg.getTravelTime() + ladenLeg.getIdleTime();
		shippingAnnotation.ladenSpeed = ladenLeg.getSpeed();
		shippingAnnotation.ladenCanalCosts = ladenLeg.getOptions().getRouteCost();

		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
			shippingAnnotation.ladenBunkersInMT += ladenLeg.getFuelConsumption(fc, FuelUnit.MT);
			shippingAnnotation.ladenBunkersInMT += ladenLeg.getRouteAdditionalConsumption(fc, FuelUnit.MT);
		}
		// lng
		long ladenBOGInM3 = 0L;
		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
			ladenBOGInM3 += ladenLeg.getFuelConsumption(fc, FuelUnit.M3);
			ladenBOGInM3 += ladenLeg.getRouteAdditionalConsumption(fc, FuelUnit.M3);
		}
		shippingAnnotation.ladenBOInMMBTu = Calculator.convertM3ToMMBTu(ladenBOGInM3, ladenLeg.getOptions().getCargoCVValue());
	}

	public static void extractDischargeDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final PortDetails dischargeDetails = (PortDetails) realPlan.getSequence()[2];

		// Discharge Details
		shippingAnnotation.dischargePort = dischargeDetails.getOptions().getPortSlot().getPort();

		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
			shippingAnnotation.dischargeBunkersInMT += dischargeDetails.getFuelConsumption(fc);
		}
		shippingAnnotation.dischargeHireHours = dischargeDetails.getOptions().getVisitDuration();
		shippingAnnotation.dischargePortCosts = dischargeDetails.getPortCosts();
		shippingAnnotation.startOfDischarge = allocationAnnotation.getSlotTime(dischargeDetails.getOptions().getPortSlot());
		shippingAnnotation.completionOfDischarge = shippingAnnotation.startOfDischarge + allocationAnnotation.getSlotDuration(dischargeDetails.getOptions().getPortSlot());
	}

	public static void extractBallastDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final VoyageDetails ballastLeg = (VoyageDetails) realPlan.getSequence()[3];

		shippingAnnotation.ballastRoute = ballastLeg.getOptions().getRoute();
		shippingAnnotation.ballastDistance = ballastLeg.getOptions().getDistance();
		shippingAnnotation.ballastHireHours = ballastLeg.getTravelTime() + ballastLeg.getIdleTime();
		shippingAnnotation.ballastSpeed = ballastLeg.getSpeed();
		shippingAnnotation.ballastCanalCosts = ballastLeg.getOptions().getRouteCost();

		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents()) {
			shippingAnnotation.ballastBunkersInMT += ballastLeg.getFuelConsumption(fc, FuelUnit.MT);
			shippingAnnotation.ballastBunkersInMT += ballastLeg.getRouteAdditionalConsumption(fc, FuelUnit.MT);
		}
		// lng
		long ballastBOGInM3 = 0L;
		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
			ballastBOGInM3 += ballastLeg.getFuelConsumption(fc, FuelUnit.M3);
			ballastBOGInM3 += ballastLeg.getRouteAdditionalConsumption(fc, FuelUnit.M3);
		}
		shippingAnnotation.ballastBOInMMBTu = Calculator.convertM3ToMMBTu(ballastBOGInM3, ballastLeg.getOptions().getCargoCVValue());
	}

	public static void extractReturnDetails(final @NonNull VoyagePlan realPlan, final @NonNull IAllocationAnnotation allocationAnnotation, final @NonNull LDShippingAnnotation shippingAnnotation) {
		final PortDetails returnDetails = (PortDetails) realPlan.getSequence()[5];

		// Discharge Details
		shippingAnnotation.returnPort = returnDetails.getOptions().getPortSlot().getPort();
		shippingAnnotation.nextPortDate = allocationAnnotation.getSlotTime(allocationAnnotation.getReturnSlot());
	}

	public static void updateBunkerCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final int bunkerPricePerMT) {

		shippingAnnotation.loadBunkersCost = Calculator.costFromConsumption(shippingAnnotation.loadBunkersInMT, bunkerPricePerMT);
		shippingAnnotation.ladenBunkersCost = Calculator.costFromConsumption(shippingAnnotation.ladenBunkersInMT, bunkerPricePerMT);
		shippingAnnotation.dischargeBunkersCost = Calculator.costFromConsumption(shippingAnnotation.dischargeBunkersInMT, bunkerPricePerMT);
		shippingAnnotation.ballastBunkersCost = Calculator.costFromConsumption(shippingAnnotation.ballastBunkersInMT, bunkerPricePerMT);
	}

	public static void updateLNGCosts(final @NonNull LDShippingAnnotation shippingAnnotation, int lngCostPerMMBTu) {

		shippingAnnotation.ladenBOCost = Calculator.costFromConsumption(shippingAnnotation.ladenBOInMMBTu, lngCostPerMMBTu);
		shippingAnnotation.ballastBOCost = Calculator.costFromConsumption(shippingAnnotation.ballastBOInMMBTu, lngCostPerMMBTu);
	}

	public static void updateCharterCosts(final @NonNull LDShippingAnnotation shippingAnnotation, final int charterCostPerDay) {
		shippingAnnotation.loadCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.loadHireHours) / 24L;
		shippingAnnotation.ladenCharterCost = Calculator.getTimeFromRateQuantity(charterCostPerDay, shippingAnnotation.ladenHireHours) / 24L;
		shippingAnnotation.dischargeCharterCost = Calculator.quantityFromRateTime(charterCostPerDay, shippingAnnotation.dischargeHireHours) / 24L;
		shippingAnnotation.ballastCharterCost = Calculator.getTimeFromRateQuantity(charterCostPerDay, shippingAnnotation.ballastHireHours) / 24L;
	}

	public static long getTotalShippingCost(final @NonNull LDShippingAnnotation shippingAnnotation, boolean includeBOG) {

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