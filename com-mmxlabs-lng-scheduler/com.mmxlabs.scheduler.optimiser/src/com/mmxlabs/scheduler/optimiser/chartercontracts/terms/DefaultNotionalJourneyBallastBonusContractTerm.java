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
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;

public class DefaultNotionalJourneyBallastBonusContractTerm extends BallastBonusContractTerm {

	public enum FuelCalculationMode {
		BUNKERS_ONLY_WITH_CURVE_PRICE, LNG_ONLY_WITH_LAST_PRICE,
	}

	private final @NonNull ILongCurve lumpSumCurve;
	private final @NonNull ICurve fuelPriceCurve;
	private final @NonNull ILongCurve charterRateCurve;
	private final @NonNull Set<IPort> returnPorts;
	private final boolean includeCanalFees;
	private final boolean includeCanalTime;
	private final int speedInKnots;

	private final FuelCalculationMode fuelCalculationMode;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;
	
	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public DefaultNotionalJourneyBallastBonusContractTerm(final Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve, final @NonNull ICurve fuelPriceCurve, FuelCalculationMode fuelCalculationMode,
			final @NonNull ILongCurve charterRateCurve, final @NonNull Set<IPort> returnPorts, final boolean includeCanalFees, final boolean includeCanalTime, final int speedInKnots) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
		this.fuelPriceCurve = fuelPriceCurve;
		this.charterRateCurve = charterRateCurve;
		this.returnPorts = returnPorts;
		this.includeCanalFees = includeCanalFees;
		this.includeCanalTime = includeCanalTime;
		this.speedInKnots = speedInKnots;
		this.fuelCalculationMode = fuelCalculationMode;
	}

	private long computeLumpSum(final int time) {
		final long lumpSum = (lumpSumCurve != null ? lumpSumCurve.getValueAtPoint(time) : 0);
		return lumpSum;
	}

	@Override
	public boolean match(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState) {
		final IPortSlot slot = portTimesRecord.getFirstSlot();
		return (slot.getPortType() == PortType.End || slot.getPortType() == PortType.Round_Trip_Cargo_End)

				&& (getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty());
	}

	public Set<IPort> getReturnPorts() {
		return returnPorts;
	}

	@Override
	public long calculateCost(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState, final PreviousHeelRecord heelRecord) {
		final var result = calculateCost(portTimesRecord, vesselCharter, vesselStartState, heelRecord, false);
		return result == null ? 0L : result.totalCost;
	}

	@Override
	public ICharterContractTermAnnotation annotate(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState, final PreviousHeelRecord heelRecord) {

		return calculateCost(portTimesRecord, vesselCharter, vesselStartState, heelRecord, true);
	}

	private NotionalJourneyBallastBonusTermAnnotation calculateCost(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState,
			final PreviousHeelRecord heelRecord, final boolean fullDetails) {
		final IPortSlot slot = portTimesRecord.getFirstSlot();

		final int ballastBonusStartTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);
		final int ballastBonusStartTimeUTC = timeZoneToUtcOffsetProvider.UTC(ballastBonusStartTime, slot);
		final long lumpSum = computeLumpSum(ballastBonusStartTimeUTC);

		NotionalJourneyBallastBonusTermAnnotation bestResult = null;
		for (final IPort returnPort : getReturnPorts()) {

			final NotionalJourneyBallastBonusTermAnnotation annotation = new NotionalJourneyBallastBonusTermAnnotation();
			annotation.returnPort = returnPort;
			annotation.lumpSum = lumpSum;

			final @NonNull Pair<@NonNull ERouteOption, @NonNull Integer> quickestTravelTime = distanceProvider.getQuickestTravelTime(vesselCharter.getVessel(), slot.getPort(), returnPort,
					speedInKnots, AvailableRouteChoices.OPTIMAL);
			final ERouteOption route = quickestTravelTime.getFirst();
			final int routeTransitTime = routeCostProvider.getRouteTransitTime(route, vesselCharter.getVessel());
			final int journeyTravelTime = quickestTravelTime.getSecond() - routeTransitTime;

			final int hireTime = this.includeCanalTime ? quickestTravelTime.getSecond() : journeyTravelTime;
			annotation.hireRate = charterRateCurve.getValueAtPoint(ballastBonusStartTimeUTC);
			annotation.totalHireCost = (annotation.hireRate * hireTime) / 24L;

			
			if (fuelCalculationMode == FuelCalculationMode.BUNKERS_ONLY_WITH_CURVE_PRICE) {
				annotation.totalFuelUsed = Calculator.quantityFromRateTime(vesselCharter.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;
				if (this.includeCanalTime) {
					annotation.totalFuelUsed += Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselCharter.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
				}
				annotation.fuelPrice = fuelPriceCurve.getValueAtPoint(ballastBonusStartTimeUTC);

				annotation.totalFuelCost = Calculator.costFromConsumption(annotation.totalFuelUsed, annotation.fuelPrice);

			} else if (fuelCalculationMode == FuelCalculationMode.LNG_ONLY_WITH_LAST_PRICE) {

				final IBaseFuel baseFuel = vesselCharter.getVessel().getTravelBaseFuel();
				final int equivalenceFactorMMBTuToMT = baseFuel.getEquivalenceFactor();

				final long bunkersRequiredInMT = Calculator.quantityFromRateTime(vesselCharter.getVessel().getConsumptionRate(VesselState.Ballast).getRate(speedInKnots), journeyTravelTime) / 24L;
				annotation.totalLNGUsed = Calculator.convertMTtoMMBTu(bunkersRequiredInMT, equivalenceFactorMMBTuToMT);
				if (this.includeCanalTime) {
					final long canalBunkersInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(route, vesselCharter.getVessel(), VesselState.Ballast), routeTransitTime) / 24L;
					annotation.totalLNGUsed += Calculator.convertMTtoMMBTu(canalBunkersInMT, equivalenceFactorMMBTuToMT);
				}
				annotation.lngPrice = heelRecord.lastHeelPricePerMMBTU;
				annotation.totalLNGCost = Calculator.costFromConsumption(annotation.totalLNGUsed, heelRecord.lastHeelPricePerMMBTU);
			} else {
				assert false;
			}

			annotation.canalCost = includeCanalFees ? routeCostProvider.getRouteCost(route, slot.getPort(), returnPort, vesselCharter.getVessel(), ballastBonusStartTimeUTC, CostType.Ballast) : 0L;

			final long cost = annotation.lumpSum + annotation.totalFuelCost + annotation.totalLNGCost + annotation.canalCost + annotation.totalHireCost;

			annotation.totalCost = cost;

			if (fullDetails) {
				// Fill in remaining options
				Pair<@NonNull ERouteOption, @NonNull Integer> minTravel;
				if (!this.includeCanalTime) {
					minTravel = Pair.of(quickestTravelTime.getFirst(), journeyTravelTime);
				} else {
					minTravel = quickestTravelTime;
				}

				annotation.distance = distanceProvider.getDistance(minTravel.getFirst(), slot.getPort(), returnPort, vesselCharter.getVessel());
				annotation.totalTimeInHours = minTravel.getSecond();
				annotation.route = minTravel.getFirst();
			}

			if (bestResult == null || annotation.totalCost < bestResult.totalCost) {
				bestResult = annotation;
			}
		}
		return bestResult;
	}

}
