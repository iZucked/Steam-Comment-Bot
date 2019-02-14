/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.util;

import com.mmxlabs.scheduler.optimiser.Calculator;
//import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;

public class ApproximateVoyageCalculatorHelper {
	public static final class ApproximateFuelCostLegData {
		public int salesPrice;
		public long boiloffRateM3;
		public IVessel vessel;
		public int cv;
		public int[] times;
		public long distance;
		public int equivalenceFactor;
		public int[] baseFuelPricesPerMT;
		public int canalTransitTime;
		public int durationAtPort;
		public boolean isLaden;
		public boolean forceBaseFuel;
		public boolean includeIdleBunkerCosts;
	}
	
	public static ApproximateFuelCosts getLegFuelCosts(ApproximateFuelCostLegData data) {
		return getLegFuelCosts(data.salesPrice, data.boiloffRateM3, data.vessel, data.cv, data.times, data.distance, data.equivalenceFactor,
				data.baseFuelPricesPerMT, data.canalTransitTime, data.durationAtPort, data.isLaden, data.forceBaseFuel, data.includeIdleBunkerCosts);
	}
	
	public static ApproximateFuelCosts getLegFuelCosts(final int salesPrice, final long boiloffRateM3, final IVessel vessel, final int cv, final int[] times, final long distance, final int equivalenceFactor,
			final int[] baseFuelPricesPerMT, final int canalTransitTime, final int durationAtPort, final boolean isLaden) {
		return getLegFuelCosts(salesPrice, boiloffRateM3, vessel, cv, times, distance, equivalenceFactor, baseFuelPricesPerMT, canalTransitTime, durationAtPort, isLaden, false, false);
	}
	
	/**
	 * Calculates approximate fuel costs. Assumptions: does not calculate the in port costs, route costs
	 * 
	 * @param salesPrice
	 * @param boiloffRateM3
	 * @param vesselClass
	 * @param cv
	 * @param times
	 * @param distance
	 * @param equivalenceFactor
	 * @param baseFuelPricesPerMT
	 * @param canalTransitTime
	 * @param durationAtPort
	 * @param isLaden
	 * @return
	 */
	public static ApproximateFuelCosts getLegFuelCosts(final int salesPrice, final long boiloffRateM3, final IVessel vessel, final int cv, final int[] times, final long distance, final int equivalenceFactor,
			final int[] baseFuelPricesPerMT, final int canalTransitTime, final int durationAtPort, final boolean isLaden, final boolean forceBaseFuel, final boolean includeIdleBunkerCosts) {
		final VesselState vesselState;
		if (isLaden) {
			vesselState = VesselState.Laden;
		} else {
			vesselState = VesselState.Ballast;
		}
		final int totalLegLengthInHours = (times[1] - times[0] - durationAtPort - canalTransitTime);

		if (distance == 0 || totalLegLengthInHours == 0) {		
				return new ApproximateFuelCosts(0, 0);
		}
		// estimate speed and rate
		final int nboSpeed = vessel.getConsumptionRate(vesselState).getSpeed(Calculator.convertM3ToMT(boiloffRateM3, cv, equivalenceFactor));
		final int naturalSpeed = Calculator.speedFromDistanceTime(distance, totalLegLengthInHours);
		final int speed = Math.min(Math.max(nboSpeed, naturalSpeed), vessel.getMaxSpeed()); // the speed bounded by NBO and Max
		final long rateVoyage = vessel.getConsumptionRate(vesselState).getRate(speed);
		final long vesselTravelTimeInHours = Calculator.getTimeFromSpeedDistance(speed, distance);
		final long idleTimeInHours = Math.max(totalLegLengthInHours - vesselTravelTimeInHours, 0) + canalTransitTime; // note: modelling canal time as idle

		final long requiredTotalFuelMT = (rateVoyage * vesselTravelTimeInHours) / 24L;
		final long requiredTotalFuelM3 = Calculator.convertMTToM3(requiredTotalFuelMT, cv, equivalenceFactor);
		final long requiredTotalFuelMMBTu = Calculator.convertM3ToMMBTu(requiredTotalFuelM3, cv);

		long idleBunkerTotalFuelMT = includeIdleBunkerCosts ? (vessel.getIdleConsumptionRate(vesselState) * idleTimeInHours) / 24L : 0L;
		final long idleBoiloffMMBTU = Calculator.convertM3ToMMBTu(vessel.getIdleNBORate(vesselState) * (int) idleTimeInHours, cv) / 24L;
		
		final long idleBoiloffCost = Calculator.costFromVolume(idleBoiloffMMBTU, salesPrice);

		final ApproximateFuelCosts fuelCosts;
		final long totalBoilOffCost;
		final long totalBunkerCost;
		
		final long idleBunkerCost = Calculator.costFromConsumption(idleBunkerTotalFuelMT,
				baseFuelPricesPerMT[vessel.getIdleBaseFuel().getIndex()]);

		if (forceBaseFuel) {
			totalBoilOffCost = 0;
			totalBunkerCost = Calculator.costFromConsumption(requiredTotalFuelMT, baseFuelPricesPerMT[vessel.getTravelBaseFuel().getIndex()]);
		} else {
			if (vessel.hasReliqCapability()) {
				totalBoilOffCost = Calculator.costFromVolume(requiredTotalFuelMMBTu, salesPrice);
				totalBunkerCost = 0L;
			} else {
				final long boiloffM3 = (vesselTravelTimeInHours * boiloffRateM3) / 24L;
				final long boiloffMMBTU = Calculator.convertM3ToMMBTu((vesselTravelTimeInHours * boiloffRateM3) / 24, cv);
				final long boiloffMT = Calculator.convertM3ToMT(boiloffM3, cv, equivalenceFactor);
				final long boiloffCost = Calculator.costFromVolume(boiloffMMBTU, salesPrice);
				final long boiloffCostMMBTU = boiloffCost;
	
				final long bunkersNeededMT = Math.max(0, requiredTotalFuelMT - boiloffMT);
				final long bunkersCost = Calculator.costFromConsumption(bunkersNeededMT, baseFuelPricesPerMT[vessel.getTravelBaseFuel().getIndex()]);
	
				final long fboInMMBTU = Math.max(0, requiredTotalFuelMMBTu - boiloffMMBTU);
				final long fboCost = Calculator.costFromVolume(fboInMMBTU, salesPrice);
	
				if (fboCost > bunkersCost) {
					totalBoilOffCost = boiloffCostMMBTU;
					totalBunkerCost = bunkersCost;
				} else {
					totalBoilOffCost = Calculator.costFromVolume(requiredTotalFuelMMBTu, salesPrice);
					totalBunkerCost = 0L;
				}
			}
		}
		// note converting units so same scale as charter rate
		fuelCosts = new ApproximateFuelCosts(
				totalBoilOffCost + idleBoiloffCost,
				totalBunkerCost + idleBunkerCost
			);
		
		return fuelCosts;
	}

}
