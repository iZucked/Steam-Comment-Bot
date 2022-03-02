/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.BallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.NotionalJourneyBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class DefaultNotionalJourneyBallastBonusContractTerm extends BallastBonusContractTerm {
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

	public DefaultNotionalJourneyBallastBonusContractTerm(final Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve, final @NonNull ICurve fuelPriceCurve,
			final @NonNull ILongCurve charterRateCurve, final @NonNull Set<IPort> returnPorts, final boolean includeCanalFees, final boolean includeCanalTime, final int speedInKnots) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.returnPorts = returnPorts;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;
	}

	private long computeLumpSum(final int time) {
		final long lumpSum = (lumpSumCurve != null ? lumpSumCurve.getValueAtPoint(time) : 0);
		return lumpSum;
	}

	@Override
	public boolean match(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPort) {
		IPortSlot slot = portTimesRecord.getFirstSlot();
		return( slot.getPortType() == PortType.End || slot.getPortType() == PortType.Round_Trip_Cargo_End)
				
				&& (getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty());
	}

	public Set<IPort> getReturnPorts() {
		return returnPorts;
	}

	@Override
	public long calculateCost(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPort) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		final long lumpSum = computeLumpSum(vesselEndTime);
		long minCost = Long.MAX_VALUE;
		for (final IPort returnPort : getReturnPorts()) {
			@NonNull
			final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), slot.getPort(), returnPort,
					speedInKnots, AvailableRouteChoices.OPTIMAL);
			final ERouteOption route = quickestTravelTime.getFirst();
			final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel());
			final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;
			int hireTime = quickestTravelTime.getSecond();
			final long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;
			long fuelUsedCanal = 0L;
			if (this.includeCanalTime) {
				fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
			} else { // canal time not included.
				hireTime = journeyTravelTime;
			}
			final long canalCost = routeCostProvider.getRouteCost(route, slot.getPort(), returnPort, vesselAvailability.getVessel(), vesselEndTime, CostType.Ballast);
			final long hireCost = (charterRateCurve.getValueAtPoint(vesselEndTime) * hireTime) / 24L;
			final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(vesselEndTime));
			final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			minCost = Math.min(minCost, cost);
		}
		return minCost;
	}

	@Override
	public ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPorte) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		final long lumpSum = computeLumpSum(vesselEndTime);
		long minTotalCost = Long.MAX_VALUE;
		Pair<@NonNull ERouteOption, @NonNull Integer> minTravel = null;
		long minfuelUsed = Long.MAX_VALUE;
		long minCanalCost = Long.MAX_VALUE;
		long minHireCost = Long.MAX_VALUE;
		long minFuelCost = Long.MAX_VALUE;
		IPort minReturnPort = null;
		for (final IPort returnPort : getReturnPorts()) {
			@NonNull
			final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), slot.getPort(), returnPort,
					speedInKnots, AvailableRouteChoices.OPTIMAL);
			final ERouteOption route = quickestTravelTime.getFirst();
			final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselAvailability.getVessel());
			final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;
			int hireTime = quickestTravelTime.getSecond();
			final long fuelUsedJourney = Calculator.quantityFromRateTime(vesselAvailability.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;
			long fuelUsedCanal = 0L;

			if (this.includeCanalTime) {
				fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselAvailability.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
			} else {
				hireTime = journeyTravelTime;
			}
			final long canalCost = routeCostProvider.getRouteCost(route, slot.getPort(), returnPort, vesselAvailability.getVessel(), vesselEndTime, CostType.Ballast);
			final long hireCost = (charterRateCurve.getValueAtPoint(vesselEndTime) * hireTime) / 24L;
			final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(vesselEndTime));
			final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			if (cost < minTotalCost) {
				minTotalCost = cost;
				minfuelUsed = fuelUsedJourney + fuelUsedCanal;
				if (!this.includeCanalTime) {
					minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
				} else {
					minTravel = quickestTravelTime;
				}
				minCanalCost = canalCost;
				minHireCost = hireCost;
				minFuelCost = fuelCost;
				minReturnPort = returnPort;
			}
		}
		NotionalJourneyBallastBonusTermAnnotation notionalJourneyBallastBonusRuleAnnotation = null;
		if (minReturnPort != null) {
			notionalJourneyBallastBonusRuleAnnotation = new NotionalJourneyBallastBonusTermAnnotation();
			notionalJourneyBallastBonusRuleAnnotation.lumpSum = lumpSum;
			notionalJourneyBallastBonusRuleAnnotation.returnPort = minReturnPort;
			notionalJourneyBallastBonusRuleAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), slot.getPort(), minReturnPort, vesselAvailability.getVessel());
			notionalJourneyBallastBonusRuleAnnotation.totalTimeInHours = minTravel.getSecond();
			notionalJourneyBallastBonusRuleAnnotation.totalFuelUsed = minfuelUsed;
			notionalJourneyBallastBonusRuleAnnotation.fuelPrice = fuelPriceCurve.getValueAtPoint(vesselEndTime);
			notionalJourneyBallastBonusRuleAnnotation.totalFuelCost = minFuelCost;
			notionalJourneyBallastBonusRuleAnnotation.hireRate = charterRateCurve.getValueAtPoint(vesselEndTime);
			notionalJourneyBallastBonusRuleAnnotation.totalHireCost = minHireCost;
			notionalJourneyBallastBonusRuleAnnotation.route = minTravel.getFirst();
			notionalJourneyBallastBonusRuleAnnotation.canalCost = minCanalCost;
		}
		return notionalJourneyBallastBonusRuleAnnotation;
	}
}
