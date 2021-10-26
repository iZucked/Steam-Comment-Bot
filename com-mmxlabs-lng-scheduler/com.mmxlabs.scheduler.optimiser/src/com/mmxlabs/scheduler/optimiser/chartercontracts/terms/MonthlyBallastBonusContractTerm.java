/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import java.util.Collections;
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
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class MonthlyBallastBonusContractTerm extends BallastBonusContractTerm{
	private final @NonNull ILongCurve lumpSumCurve;
	private final @NonNull ICurve fuelPriceCurve;
	private final @NonNull ILongCurve charterRateCurve;
	private final @NonNull Set<IPort> returnPorts;
	private final boolean includeCanalFees;
	private final boolean includeCanalTime;
	private final int speedInKnots;

	int monthStartInclusive;
	int monthEndExclusive;
	long pctCharterRate;
	long pctFuelRate;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	public MonthlyBallastBonusContractTerm(int oYMStartInclusive, int oYMEndExclusive, long pctCharterRate, long pctFuelRate, Set<IPort> redeliveryPorts, ILongCurve lumpSumCurve,
			ICurve fuelPriceCurve, ILongCurve charterRateCurve, Set<IPort> returnPorts, boolean includeCanalFees, boolean includeCanalTime, int speedInKnots) {
		super(redeliveryPorts);

		this.lumpSumCurve = lumpSumCurve;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.returnPorts = returnPorts;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;

		this.monthStartInclusive = oYMStartInclusive;
		this.monthEndExclusive = oYMEndExclusive;
		this.pctCharterRate = pctCharterRate;
		this.pctFuelRate = pctFuelRate;
	}

	@Override
	public long calculateCost(final IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {

		final int voyageStartTime = vesselEndTime;

		final long lumpSum = computeLumpSum(voyageStartTime);
		long minCost = Long.MAX_VALUE;
		for (final IPort returnPort : getReturnPorts(firstLoad)) {
			@NonNull
			final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort,
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
			final long canalCost = routeCostProvider.getRouteCost(route, lastSlot.getPort(), returnPort, vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
			final long hireCost = Calculator.percentageLow(this.pctCharterRate, (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L);
			final long fuelCost = Calculator.percentageLow(this.pctFuelRate, Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime)));
			final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;
			minCost = Math.min(minCost, cost);
		}
		return minCost;
	}

	private long computeLumpSum(final int time) {
		final long lumpSum = (lumpSumCurve != null ? lumpSumCurve.getValueAtPoint(time) : 0);
		return lumpSum;
	}

	@Override
	public ICharterContractTermAnnotation annotate(final IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime,
			final int vesselEndTime) {

		final int voyageStartTime = vesselEndTime;

		final long lumpSum = computeLumpSum(voyageStartTime);
		long minTotalCost = Long.MAX_VALUE;
		Pair<@NonNull ERouteOption, @NonNull Integer> minTravel = null;
		long minfuelUsed = Long.MAX_VALUE;
		long minCanalCost = Long.MAX_VALUE;
		long minHireCost = Long.MAX_VALUE;
		long minFuelCost = Long.MAX_VALUE;
		IPort minReturnPort = null;
		for (final IPort returnPort : getReturnPorts(firstLoad)) {
			@NonNull
			final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), lastSlot.getPort(), returnPort,
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
			final long canalCost = routeCostProvider.getRouteCost(route, lastSlot.getPort(), returnPort, vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
			final long hireCost = Calculator.percentageLow(this.pctCharterRate, (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L);
			final long fuelCost = Calculator.percentageLow(this.pctFuelRate, Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime)));
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
			notionalJourneyBallastBonusRuleAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), lastSlot.getPort(), minReturnPort, vesselAvailability.getVessel());
			notionalJourneyBallastBonusRuleAnnotation.totalTimeInHours = minTravel.getSecond();
			notionalJourneyBallastBonusRuleAnnotation.totalFuelUsed = minfuelUsed;
			notionalJourneyBallastBonusRuleAnnotation.fuelPrice = (int) Calculator.percentageLow(this.pctFuelRate, (long) fuelPriceCurve.getValueAtPoint(voyageStartTime));
			notionalJourneyBallastBonusRuleAnnotation.totalFuelCost = minFuelCost;
			notionalJourneyBallastBonusRuleAnnotation.hireRate = Calculator.percentageLow(this.pctCharterRate, charterRateCurve.getValueAtPoint(voyageStartTime));
			notionalJourneyBallastBonusRuleAnnotation.totalHireCost = minHireCost;
			notionalJourneyBallastBonusRuleAnnotation.route = minTravel.getFirst();
			notionalJourneyBallastBonusRuleAnnotation.canalCost = minCanalCost;
		}
		return notionalJourneyBallastBonusRuleAnnotation;
	}

	private Set<IPort> getReturnPorts(IPort firstLoad) {
		if (this.getReturnPorts() == null) {
			return Collections.singleton(firstLoad);
		} else {
			return getReturnPorts();
		}
	}

	@Override
	public boolean match(final IPort loadPort, final IPortSlot slot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {	
		if (monthStartInclusive <= vesselEndTime && vesselEndTime < monthEndExclusive) {
			return getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty();
		}
		return false;
	}

	public boolean matchWithoutDates(IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return getRedeliveryPorts().contains(lastSlot.getPort()) || getRedeliveryPorts().isEmpty();
	}

	public int getMonthStartInclusive() {
		return this.monthStartInclusive;
	}

	public int getMonthEndExclusive() {
		return this.monthEndExclusive;
	}

	public Set<IPort> getReturnPorts() {
		return returnPorts;
	}
}
