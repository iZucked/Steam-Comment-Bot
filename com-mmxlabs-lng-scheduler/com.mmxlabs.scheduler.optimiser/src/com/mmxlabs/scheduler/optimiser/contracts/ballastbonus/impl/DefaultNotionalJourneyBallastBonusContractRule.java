/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.INotionalJourneyBallastBonusContractRule;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class DefaultNotionalJourneyBallastBonusContractRule extends BallastBonusContractRule implements INotionalJourneyBallastBonusContractRule {
	private final @NonNull ILongCurve lumpSumCurve;
	private final @NonNull ICurve fuelPriceCurve;
	private final @NonNull ILongCurve charterRateCurve;
	private final @NonNull Set<IPort> returnPorts;
	private final boolean includeCanalFees;
	private final boolean includeCanalTime;
	private final int speedInKnots;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	public DefaultNotionalJourneyBallastBonusContractRule(Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve, final @NonNull ICurve fuelPriceCurve, final @NonNull ILongCurve charterRateCurve,
			final @NonNull Set<IPort> returnPorts, final boolean includeCanalFees, final boolean includeCanalTime, final int speedInKnots) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.returnPorts = returnPorts;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;
	}
	
	@Override
	public long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int voyageStartTime) {
		long lumpSum = computeLumpSum(voyageStartTime);
		long minCost = Long.MAX_VALUE;
		for (IPort returnPort : getReturnPorts()) {
			@NonNull
			Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort, speedInKnots,
					AvailableRouteChoices.OPTIMAL);
			ERouteOption route = quickestTravelTime.getFirst();
			int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel());
			int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime; 
			int hireTime = quickestTravelTime.getSecond();
			long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots),
					journeyTravelTime) / 24L;
			long fuelUsedCanal = 0L;
			if (this.includeCanalTime) {
				fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast),
					routeTransitTime) / 24L;
			}
			else { //canal time not included.
				hireTime = journeyTravelTime;
			}
			long canalCost = routeCostProvider.getRouteCost(route, vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
			long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
			long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));
			long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			minCost = Math.min(minCost, cost);
		}
		return minCost;
	}

	private long computeLumpSum(int time) {
		long lumpSum = (lumpSumCurve != null ? lumpSumCurve.getValueAtPoint(time) : 0);
		return lumpSum;
	}
	
	@Override
	public NotionalJourneyBallastBonusRuleAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int voyageStartTime) {
		long lumpSum = computeLumpSum(voyageStartTime);
		long minTotalCost = Long.MAX_VALUE;
		Pair<@NonNull ERouteOption, @NonNull Integer> minTravel = null;
		long minfuelUsed = Long.MAX_VALUE;
		long minCanalCost = Long.MAX_VALUE;
		long minHireCost = Long.MAX_VALUE;
		long minFuelCost = Long.MAX_VALUE;
		IPort minReturnPort = null;
		for (IPort returnPort : getReturnPorts()) {
			@NonNull
			Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort, speedInKnots,
					AvailableRouteChoices.OPTIMAL);
			ERouteOption route = quickestTravelTime.getFirst();
			int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel());
			int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime; 
			int hireTime = quickestTravelTime.getSecond();
			long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots),
					journeyTravelTime) / 24L;
			long fuelUsedCanal = 0L;

			if (this.includeCanalTime) {
				fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast),
					routeTransitTime) / 24L;
			}
			else {
				hireTime = journeyTravelTime;
			}
			long canalCost = routeCostProvider.getRouteCost(route, vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
			long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
			long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));
			long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			if (cost < minTotalCost) {
				minTotalCost = cost;
				minfuelUsed = fuelUsedJourney + fuelUsedCanal;
				if (!this.includeCanalTime) {
					minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
				}
				else {
					minTravel = quickestTravelTime;
				}
				minCanalCost = canalCost;
				minHireCost = hireCost;
				minFuelCost = fuelCost;
				minReturnPort = returnPort;
			}
		}
		NotionalJourneyBallastBonusRuleAnnotation notionalJourneyBallastBonusRuleAnnotation = null;
		if (minReturnPort != null) {
			notionalJourneyBallastBonusRuleAnnotation = new NotionalJourneyBallastBonusRuleAnnotation();
			notionalJourneyBallastBonusRuleAnnotation.lumpSum = lumpSum;
			notionalJourneyBallastBonusRuleAnnotation.returnPort = minReturnPort;
			notionalJourneyBallastBonusRuleAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), lastSlot.getPort(), minReturnPort, vesselAvailability.getVessel());
			notionalJourneyBallastBonusRuleAnnotation.totalTimeInHours = minTravel.getSecond();
			notionalJourneyBallastBonusRuleAnnotation.totalFuelUsed = minfuelUsed;
			notionalJourneyBallastBonusRuleAnnotation.fuelPrice = fuelPriceCurve.getValueAtPoint(voyageStartTime);
			notionalJourneyBallastBonusRuleAnnotation.totalFuelCost = minFuelCost;
			notionalJourneyBallastBonusRuleAnnotation.hireRate = charterRateCurve.getValueAtPoint(voyageStartTime);
			notionalJourneyBallastBonusRuleAnnotation.totalHireCost = minHireCost;
			notionalJourneyBallastBonusRuleAnnotation.route = minTravel.getFirst();
			notionalJourneyBallastBonusRuleAnnotation.canalCost = minCanalCost;
		}
		return notionalJourneyBallastBonusRuleAnnotation;
	}

	@Override
	public boolean match(IPortSlot slot, IVesselAvailability vesselAvailability, int time) {
		return getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty();
	}

	public Set<IPort> getReturnPorts() {
		return returnPorts;
	}
}
