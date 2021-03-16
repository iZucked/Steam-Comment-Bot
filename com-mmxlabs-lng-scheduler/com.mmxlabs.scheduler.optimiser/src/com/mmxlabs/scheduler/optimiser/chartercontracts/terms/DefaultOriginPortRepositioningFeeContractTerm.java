/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.RepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.OriginPortRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class DefaultOriginPortRepositioningFeeContractTerm extends RepositioningFeeContractTerm {
	private final @NonNull ILongCurve lumpSumCurve;
	private final @NonNull ICurve fuelPriceCurve;
	private final @NonNull ILongCurve charterRateCurve;
	private final boolean includeCanalFees;
	private final boolean includeCanalTime;
	private final int speedInKnots;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	public DefaultOriginPortRepositioningFeeContractTerm(final @NonNull IPort originPort, final @NonNull ILongCurve lumpSumCurve, final @NonNull ICurve fuelPriceCurve,
			final @NonNull ILongCurve charterRateCurve, final boolean includeCanalFees, final boolean includeCanalTime, final int speedInKnots) {
		super(originPort);
		this.lumpSumCurve = lumpSumCurve;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;
	}

	private long computeLumpSum(final int time) {
		final long lumpSum = (lumpSumCurve != null ? lumpSumCurve.getValueAtPoint(time) : 0);
		return lumpSum;
	}

	@Override
	public ICharterContractTermAnnotation annotate(final IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {

		final int voyageStartTime = vesselEndTime;

		final long lumpSum = computeLumpSum(voyageStartTime);
		Pair<@NonNull ERouteOption, @NonNull Integer> minTravel = null;
		long fuelUsed = Long.MAX_VALUE;

		@NonNull
		final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), getOriginPort(), firstLoad,
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
		final long canalCost = routeCostProvider.getRouteCost(route,  getOriginPort(), firstLoad,vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
		final long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
		final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));

		fuelUsed = fuelUsedJourney + fuelUsedCanal;
		if (!this.includeCanalTime) {
			minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
		} else {
			minTravel = quickestTravelTime;
		}

		OriginPortRepositioningFeeTermAnnotation termAnnotation = new OriginPortRepositioningFeeTermAnnotation();
		termAnnotation.lumpSum = lumpSum;
		termAnnotation.originPort = getOriginPort();
		termAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), getOriginPort() ,firstLoad, vesselAvailability.getVessel());
		termAnnotation.totalTimeInHours = minTravel.getSecond();
		termAnnotation.totalFuelUsed = fuelUsed;
		termAnnotation.fuelPrice = fuelPriceCurve.getValueAtPoint(voyageStartTime);
		termAnnotation.totalFuelCost = fuelCost;
		termAnnotation.hireRate = charterRateCurve.getValueAtPoint(voyageStartTime);
		termAnnotation.totalHireCost = hireCost;
		termAnnotation.route = minTravel.getFirst();
		termAnnotation.canalCost = canalCost;
		
		return termAnnotation;
	}

	@Override
	public boolean match(final IPort loadPort, IPortSlot slot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return true; //getOriginPort() == loadPort;
	}

	@Override
	public long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		final int voyageStartTime = vesselEndTime;

		final long lumpSum = computeLumpSum(voyageStartTime);

		@NonNull
		final Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), getOriginPort(), firstLoad,
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
		final long canalCost = routeCostProvider.getRouteCost(route, getOriginPort(), firstLoad, vesselAvailability.getVessel(), voyageStartTime, CostType.Ballast);
		final long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
		final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));
		final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;

		return cost;
	}
}
