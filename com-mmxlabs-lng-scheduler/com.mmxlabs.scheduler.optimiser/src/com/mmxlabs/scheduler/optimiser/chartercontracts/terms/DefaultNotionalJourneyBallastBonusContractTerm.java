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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
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
	public boolean match(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, final VesselStartState vesselStartState) {
		IPortSlot slot = portTimesRecord.getFirstSlot();
		return (slot.getPortType() == PortType.End || slot.getPortType() == PortType.Round_Trip_Cargo_End)

				&& (getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty());
	}

	public Set<IPort> getReturnPorts() {
		return returnPorts;
	}

	@Override
	public long calculateCost(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, final VesselStartState vesselStartState) {
		var result = calculateCost(portTimesRecord, vesselCharter, vesselStartState, false);
		return result == null ? 0L : result.totalCost;
	}

	@Override
	public ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, final VesselStartState vesselStartState) {

		return calculateCost(portTimesRecord, vesselCharter, vesselStartState, true);
	}

	private NotionalJourneyBallastBonusTermAnnotation calculateCost(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, final VesselStartState vesselStartState, boolean fullDetails) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		final long lumpSum = computeLumpSum(vesselEndTime);

		NotionalJourneyBallastBonusTermAnnotation bestResult = null;
		for (final IPort returnPort : getReturnPorts()) {

			final @NonNull Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselCharter.getVessel(), slot.getPort(), returnPort,
					speedInKnots, AvailableRouteChoices.OPTIMAL);
			final ERouteOption route = quickestTravelTime.getFirst();
			final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselCharter.getVessel());
			final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;
			int hireTime = quickestTravelTime.getSecond();
			final long fuelUsedJourney = Calculator.quantityFromRateTime(vesselCharter.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;
			long fuelUsedCanal = 0L;

			if (this.includeCanalTime) {
				fuelUsedCanal = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselCharter.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
			} else { // canal time not included.
				hireTime = journeyTravelTime;
			}

			final long canalCost = routeCostProvider.getRouteCost(route, slot.getPort(), returnPort, vesselCharter.getVessel(), vesselEndTime, CostType.Ballast);
			final long hireCost = (charterRateCurve.getValueAtPoint(vesselEndTime) * hireTime) / 24L;
			final long fuelCost = Calculator.costFromConsumption(fuelUsedJourney + fuelUsedCanal, fuelPriceCurve.getValueAtPoint(vesselEndTime));
			final long cost = lumpSum + fuelCost + (includeCanalFees ? canalCost : 0L) + hireCost;

			NotionalJourneyBallastBonusTermAnnotation annotation = new NotionalJourneyBallastBonusTermAnnotation();
			annotation.totalCost = cost;

			if (fullDetails) {

				Pair<@NonNull ERouteOption, @NonNull Integer> minTravel;
				if (!this.includeCanalTime) {
					minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
				} else {
					minTravel = quickestTravelTime;
				}

				annotation.lumpSum = lumpSum;
				annotation.returnPort = returnPort;
				annotation.distance = distanceProvider.getDistance(minTravel.getFirst(), slot.getPort(), returnPort, vesselCharter.getVessel());
				annotation.totalTimeInHours = minTravel.getSecond();
				annotation.totalFuelUsed = fuelUsedJourney + fuelUsedCanal;
				annotation.fuelPrice = fuelPriceCurve.getValueAtPoint(vesselEndTime);
				annotation.totalFuelCost = fuelCost;
				annotation.hireRate = charterRateCurve.getValueAtPoint(vesselEndTime);
				annotation.totalHireCost = hireCost;
				annotation.route = minTravel.getFirst();
				annotation.canalCost = canalCost;
			}

			if (bestResult == null || annotation.totalCost < bestResult.totalCost) {
				bestResult = annotation;
			}
		}
		return bestResult;
	}

}
