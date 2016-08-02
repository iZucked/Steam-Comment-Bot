/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class FBOOnlyVoyageCostCalculator extends AbstractVoyageCostCalculator {

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Override
	public VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int notionalSpeed, final int cargoCVValue,
			@NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPrice) {

		final VoyagePlan notionalPlan = new VoyagePlan();
		notionalPlan.setCharterInRatePerDay(vesselCharterInRatePerDay);

		final int distance = distanceProvider.getDistance(route, loadPort, dischargePort, loadTime + loadDuration, vessel);
		if (distance == Integer.MAX_VALUE) {
			return null;
		}

		// Generate a notional voyage plan
		final int travelTime = Calculator.getTimeFromSpeedDistance(notionalSpeed, distance);

		// Determine notional port visit times.
		final int notionalReturnTime = dischargeTime + dischargeDuration + travelTime;

		final LoadSlot notionalLoadSlot = makeNotionalLoad(loadPort, loadTime, vessel, cargoCVValue);

		final DischargeSlot notionalDischargeSlot = makeNotionalDischarge(dischargePort, dischargeTime, salesPrice);

		final PortSlot notionalReturnSlot = new EndPortSlot("notional-end", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), true, vessel.getVesselClass().getSafetyHeel());

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
		portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
		portTimesRecord.setReturnSlotTime(notionalReturnSlot, notionalReturnTime);

		portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
		portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);

		// Calculate new voyage requirements
		{
			final long ladenRouteCosts = routeCostProvider.getRouteCost(route, vessel, CostType.Laden);
			final long ballastRouteCosts = routeCostProvider.getRouteCost(route, vessel, CostType.RoundTripBallast);

			final VoyageDetails ladenDetails = calculateVoyageDetails(VesselState.Laden, vessel, route, distance, ladenRouteCosts, dischargeTime - loadDuration - loadTime, notionalLoadSlot,
					notionalDischargeSlot, cargoCVValue);

			final VoyageDetails ballastDetails = calculateVoyageDetails(VesselState.Ballast, vessel, route, distance, ballastRouteCosts, notionalReturnTime - dischargeDuration - dischargeTime,
					notionalDischargeSlot, notionalReturnSlot, cargoCVValue);

			final PortDetails loadDetails = new PortDetails(new PortOptions(notionalLoadSlot));
			loadDetails.getOptions().setVisitDuration(loadDuration);

			final PortDetails dischargeDetails = new PortDetails(new PortOptions(notionalDischargeSlot));
			dischargeDetails.getOptions().setVisitDuration(dischargeDuration);

			final PortDetails returnDetails = new PortDetails(new PortOptions(notionalReturnSlot));
			returnDetails.getOptions().setVisitDuration(0);

			final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, ladenDetails, dischargeDetails, ballastDetails, returnDetails };
			notionalPlan.setSequence(sequence);
			voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, startHeelInM3, baseFuelPricePerMT, portTimesRecord, sequence);

			return notionalPlan;
		}
	}

	@Override
	@Nullable
	public VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, final int returnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPrice) {

		final VoyagePlan notionalPlan = new VoyagePlan();
		notionalPlan.setCharterInRatePerDay(vesselCharterInRatePerDay);

		final int distance = distanceProvider.getDistance(route, loadPort, dischargePort, loadTime + loadDuration, vessel);
		if (distance == Integer.MAX_VALUE) {
			return null;
		}

		return calculateShippingCosts(loadPort, dischargePort, loadTime, distance, loadDuration, dischargeTime, distance, dischargeDuration, returnTime, vessel, vesselCharterInRatePerDay,
				startHeelInM3, cargoCVValue, route, baseFuelPricePerMT, salesPrice);
	}

	@Override
	public VoyagePlan calculateShippingCosts(IPort loadPort, IPort dischargePort, int loadTime, int loadDistance, int loadDuration, int dischargeTime, int dischargeDistance, int dischargeDuration,
			int notionalReturnTime, IVessel vessel, long vesselCharterInRatePerDay, long startHeelInM3, int cargoCVValue, ERouteOption route, int baseFuelPricePerMT,
			ISalesPriceCalculator salesPriceCalculator) {
		final VoyagePlan notionalPlan = new VoyagePlan();
		notionalPlan.setCharterInRatePerDay(vesselCharterInRatePerDay);

		final LoadSlot notionalLoadSlot = makeNotionalLoad(loadPort, loadTime, vessel, cargoCVValue);

		final DischargeSlot notionalDischargeSlot = makeNotionalDischarge(dischargePort, dischargeTime, salesPriceCalculator);

		final PortSlot notionalReturnSlot = new EndPortSlot("notional-end", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), true, vessel.getVesselClass().getSafetyHeel());

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
		portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
		portTimesRecord.setReturnSlotTime(notionalReturnSlot, notionalReturnTime);

		portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
		portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);

		// Calculate new voyage requirements
		{
			final long ladenRouteCosts = routeCostProvider.getRouteCost(route, vessel, CostType.Laden);
			final long ballastRouteCosts = routeCostProvider.getRouteCost(route, vessel, CostType.RoundTripBallast);

			final VoyageDetails ladenDetails = calculateVoyageDetails(VesselState.Laden, vessel, route, loadDistance, ladenRouteCosts, dischargeTime - loadDuration - loadTime, notionalLoadSlot,
					notionalDischargeSlot, cargoCVValue);

			final VoyageDetails ballastDetails = calculateVoyageDetails(VesselState.Ballast, vessel, route, dischargeDistance, ballastRouteCosts,
					notionalReturnTime - dischargeDuration - dischargeTime, notionalDischargeSlot, notionalReturnSlot, cargoCVValue);

			final PortDetails loadDetails = new PortDetails(new PortOptions(notionalLoadSlot));
			loadDetails.getOptions().setVisitDuration(loadDuration);

			final PortDetails dischargeDetails = new PortDetails(new PortOptions(notionalDischargeSlot));
			dischargeDetails.getOptions().setVisitDuration(dischargeDuration);

			final PortDetails returnDetails = new PortDetails(new PortOptions(notionalReturnSlot));
			returnDetails.getOptions().setVisitDuration(0);

			final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, ladenDetails, dischargeDetails, ballastDetails, returnDetails };
			notionalPlan.setSequence(sequence);
			voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, startHeelInM3, baseFuelPricePerMT, portTimesRecord, sequence);

			return notionalPlan;
		}
	}
}
