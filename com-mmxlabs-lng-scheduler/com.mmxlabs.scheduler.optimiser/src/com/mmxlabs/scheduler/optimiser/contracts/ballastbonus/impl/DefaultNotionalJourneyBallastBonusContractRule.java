/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

public class DefaultNotionalJourneyBallastBonusContractRule extends BallastBonusContractRule implements INotionalJourneyBallastBonusContractRule{
	private final @NonNull ICurve fuelPriceCurve;
	private final @NonNull ILongCurve charterRateCurve;
	private final @NonNull Set<IPort> returnPorts;
	private final boolean includeCanalFees;
	private final int speedInKnots;

	@Inject
	private IDistanceProvider distanceProvider;
	
	@Inject
	private IRouteCostProvider routeCostProvider;

	public DefaultNotionalJourneyBallastBonusContractRule(Set<IPort> redeliveryPorts, final @NonNull ICurve fuelPriceCurve, final @NonNull ILongCurve charterRateCurve, final @NonNull Set<IPort> returnPorts, final boolean includeCanalFees, final int speedInKnots) {
		super(redeliveryPorts);
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.returnPorts = returnPorts;
		this.includeCanalFees = includeCanalFees;
		this.speedInKnots = speedInKnots;
	}
	
	@Override
	public long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		long minCost = Long.MAX_VALUE;
		for (IPort returnPort : getReturnPorts()) {
			@NonNull
			Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort, time, speedInKnots);
			ERouteOption route = quickestTravelTime.getFirst();
			long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getVesselClass().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots),
					(quickestTravelTime.getSecond() - routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel()))) / 24L;
			long fuelUsedCanal = routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast);
			long canalCost = routeCostProvider.getRouteCost(route, vesselAvailability.getVessel(), time, CostType.Ballast);
			long hireCost = (charterRateCurve.getValueAtPoint(time) * quickestTravelTime.getSecond())/24L;
			long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(time)) ;
			long cost = fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			minCost = Math.min(minCost, cost);
		}
		return minCost;
	}
	
	@Override
	public NotionalJourneyBallastBonusRuleAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		long minTotalCost = Long.MAX_VALUE;
		Pair<@NonNull ERouteOption, @NonNull Integer> minTravel = null;
		long minfuelUsed = Long.MAX_VALUE;
		long minCanalCost = Long.MAX_VALUE;
		long minHireCost = Long.MAX_VALUE;
		long minFuelCost = Long.MAX_VALUE;
		IPort minReturnPort = null;
		for (IPort returnPort : getReturnPorts()) {
			@NonNull
			Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort, time, speedInKnots);
			ERouteOption route = quickestTravelTime.getFirst();
			long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getVesselClass().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots),
					(quickestTravelTime.getSecond() - routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel()))) / 24L;
			long fuelUsedCanal = routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast);
			long canalCost = routeCostProvider.getRouteCost(route, vesselAvailability.getVessel(), time, CostType.Ballast);
			long hireCost = (charterRateCurve.getValueAtPoint(time) * quickestTravelTime.getSecond())/24L;
			long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(time)) ;
			long cost = fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			if (cost < minTotalCost) {
				minTotalCost = cost;
				minfuelUsed = fuelUsedJourney + fuelUsedCanal;
				minTravel = quickestTravelTime;
				minCanalCost = canalCost;
				minHireCost = hireCost;
				minFuelCost = fuelCost;
				minReturnPort = returnPort;
			}
		}
		NotionalJourneyBallastBonusRuleAnnotation notionalJourneyBallastBonusRuleAnnotation = null;
		if (minReturnPort != null) {
			notionalJourneyBallastBonusRuleAnnotation = new NotionalJourneyBallastBonusRuleAnnotation();
			notionalJourneyBallastBonusRuleAnnotation.returnPort = minReturnPort;
			notionalJourneyBallastBonusRuleAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), lastSlot.getPort(), minReturnPort, time, vesselAvailability.getVessel());
			notionalJourneyBallastBonusRuleAnnotation.totalTimeInHours = minTravel.getSecond();
			notionalJourneyBallastBonusRuleAnnotation.totalFuelUsed = minfuelUsed;
			notionalJourneyBallastBonusRuleAnnotation.fuelPrice = fuelPriceCurve.getValueAtPoint(time);
			notionalJourneyBallastBonusRuleAnnotation.totalFuelCost = minFuelCost;
			notionalJourneyBallastBonusRuleAnnotation.hireRate = charterRateCurve.getValueAtPoint(time);
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
