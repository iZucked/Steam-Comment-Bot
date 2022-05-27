/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.OriginPortRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

@NonNullByDefault
public class DefaultOriginPortRepositioningFeeContractTerm extends DefaultLumpSumRepositioningFeeContractTerm {

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	private final ICurve fuelPriceCurve;
	private final ILongCurve charterRateCurve;
	private final boolean includeCanalFees;
	private final boolean includeCanalTime;
	private final int speedInKnots;

	private final IPort originPort;

	public DefaultOriginPortRepositioningFeeContractTerm(final IPort originPort, final Set<IPort> startPorts, final ILongCurve lumpSumCurve, final ICurve fuelPriceCurve,
			final ILongCurve charterRateCurve, final boolean includeCanalFees, final boolean includeCanalTime, final int speedInKnots) {
		super(startPorts, lumpSumCurve);
		this.originPort = originPort;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;
	}

	@Override
	public long calculateCost(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter) {
		final int voyageStartTime = portTimesRecord.getFirstSlotTime();

		final long lumpSum = super.calculateCost(portTimesRecord, vesselCharter);

		final IPort startPort = getFirstPort(vesselCharter, portTimesRecord);
		assert startPort != null;

		final Pair<ERouteOption, Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselCharter.getVessel(), originPort, startPort, speedInKnots,
				AvailableRouteChoices.OPTIMAL);

		final ERouteOption route = quickestTravelTime.getFirst();
		final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselCharter.getVessel());
		final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;
		final long fuelUsedJourney = Calculator.quantityFromRateTime(vesselCharter.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;

		final int hireTime;
		final long fuelUsedCanal;
		if (this.includeCanalTime) {
			fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselCharter.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
			hireTime = quickestTravelTime.getSecond();
		} else { // canal time not included.
			hireTime = journeyTravelTime;
			fuelUsedCanal = 0L;
		}
		final long canalCost = routeCostProvider.getRouteCost(route, originPort, startPort, vesselCharter.getVessel(), voyageStartTime, CostType.Ballast);
		final long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
		final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));
		final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;

		return cost;
	}

	@Override
	public ICharterContractTermAnnotation annotate(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter) {

		final int voyageStartTime = portTimesRecord.getFirstSlotTime();

		final long lumpSum = super.calculateCost(portTimesRecord, vesselCharter);

		final IPort startPort = getFirstPort(vesselCharter, portTimesRecord);
		assert startPort != null;

		final Pair<ERouteOption, Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselCharter.getVessel(), originPort, startPort, speedInKnots,
				AvailableRouteChoices.OPTIMAL);
		final ERouteOption route = quickestTravelTime.getFirst();
		final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselCharter.getVessel());
		final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;
		final long fuelUsedJourney = Calculator.quantityFromRateTime(vesselCharter.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;

		final long fuelUsedCanal;
		final int hireTime;
		if (this.includeCanalTime) {
			fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselCharter.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
			// Journey + canal time
			hireTime = quickestTravelTime.getSecond();
		} else {
			// Just journey time, ignore canal
			hireTime = journeyTravelTime;
			fuelUsedCanal = 0L;
		}
		final long canalCost = routeCostProvider.getRouteCost(route, originPort, startPort, vesselCharter.getVessel(), voyageStartTime, CostType.Ballast);
		final long hireCost = (charterRateCurve.getValueAtPoint(voyageStartTime) * hireTime) / 24L;
		final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(voyageStartTime));

		final long fuelUsed = fuelUsedJourney + fuelUsedCanal;

		final Pair<@NonNull ERouteOption, @NonNull Integer> minTravel;
		if (!this.includeCanalTime) {
			minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
		} else {
			minTravel = quickestTravelTime;
		}

		final OriginPortRepositioningFeeTermAnnotation termAnnotation = new OriginPortRepositioningFeeTermAnnotation();
		termAnnotation.lumpSum = lumpSum;
		termAnnotation.originPort = originPort;
		termAnnotation.distance = distanceProvider.getDistance(minTravel.getFirst(), originPort, startPort, vesselCharter.getVessel());
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
}
