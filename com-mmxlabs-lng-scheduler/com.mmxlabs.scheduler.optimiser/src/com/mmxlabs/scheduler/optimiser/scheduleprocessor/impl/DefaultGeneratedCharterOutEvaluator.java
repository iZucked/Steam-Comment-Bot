/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.FBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.NBOTravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.RouteVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 */
public class DefaultGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IVolumeAllocator cargoAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IVoyagePlanOptimiser vpo;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final int vesselStartTime, final IVessel vessel, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {

		if (!(vessel.getVesselInstanceType() == VesselInstanceType.FLEET || vessel.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null; // continue;
		}

		// TODO: Extract out further for custom base fuel pricing logic?
		// Use forecast BF, but check for actuals later
		int baseFuelUnitPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, vesselStartTime, portTimesRecord.getFirstSlotTime());

		final long startingHeelInM3 = vp.getStartingHeelInM3();
		// First step, find a ballast leg which is long enough to charter-out
		boolean isCargoPlan = false;
		// Grab the current list of arrival times and update the rolling currentTime
		final Object[] currentSequence = vp.getSequence();
		int ladenIdx = -1;
		int ballastIdx = -1;
		int ballastStartTime = -1;

		int currentTime = 0;// arrivalTimes.get(0);
		final int aTimeIdx = 0;
		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails details = (PortDetails) obj;
				if (idx != (currentSequence.length - 1)) {
					currentTime = portTimesRecord.getSlotTime(details.getOptions().getPortSlot());
					assert details.getOptions().getVisitDuration() == portTimesRecord.getSlotDuration(details.getOptions().getPortSlot());
					currentTime += portTimesRecord.getSlotDuration(details.getOptions().getPortSlot());

					if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
						isCargoPlan = true;
					}

					if (actualsDataProvider.hasActuals(details.getOptions().getPortSlot())) {
						baseFuelUnitPricePerMT = actualsDataProvider.getBaseFuelPricePerMT(details.getOptions().getPortSlot());
						vesselCharterInRatePerDay = actualsDataProvider.getCharterRatePerDay(details.getOptions().getPortSlot());
					}
				}
			} else if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;

				// record last ballast leg
				if (details.getOptions().getVesselState() == VesselState.Ballast) {
					ballastIdx = idx;
					ballastStartTime = currentTime;
				} else {
					ladenIdx = idx;
				}
				currentTime += details.getOptions().getAvailableTime();
			}
		}

		assert ((ballastIdx < 0) == (ballastStartTime < 0));
		if (ballastIdx == -1 || ballastStartTime == -1) {

			// no ballast leg?
			return null;
		}

		// Found a ballast leg, now look at the markets to see if it is long enough
		final VoyageDetails ladenDetails = ladenIdx == -1 ? null : (VoyageDetails) currentSequence[ladenIdx];
		final VoyageDetails ballastDetails = (VoyageDetails) currentSequence[ballastIdx];

		boolean foundMarketPrice = false;
		int bestDailyPrice = 0;

		final int availableTime = ballastDetails.getOptions().getAvailableTime();
		final int distance = ballastDetails.getOptions().getDistance();
		final int maxSpeed = ballastDetails.getOptions().getVessel().getVesselClass().getMaxSpeed();

		final int travelTime = Calculator.getTimeFromSpeedDistance(maxSpeed, distance);

		final int availableCharteringTime = availableTime - travelTime;
		final int time = ballastStartTime + travelTime;
		if (time < charterMarketProvider.getCharterOutStartTime()) {
			// Charter starts too early
			return null;
		}

		// Scan all the markets for a match
		for (final CharterMarketOptions option : charterMarketProvider.getCharterOutOptions(vessel.getVesselClass(), time)) {
			if (availableCharteringTime >= option.getMinDuration() && option.getCharterPrice() > bestDailyPrice) {
				foundMarketPrice = true;
				bestDailyPrice = option.getCharterPrice();
			}
		}
		// Have we found a market?
		if (!foundMarketPrice) {
			return null;
		}

		// We will use the VPO to optimise fuel and route choices
		final List<IOptionsSequenceElement> newRawSequence = new ArrayList<IOptionsSequenceElement>(currentSequence.length);
		for (final Object o : currentSequence) {
			if (o instanceof PortDetails) {
				newRawSequence.add(((PortDetails) o).getOptions());
			} else if (o instanceof VoyageDetails) {
				newRawSequence.add(((VoyageDetails) o).getOptions());
			}
		}

		// Need to reproduce P&L calculations here, switching the charter flag on/off on ballast idle.
		// Duplicate all the relevant objects and replay calcs

		// We replace the ballastDetails as the VPO will manipulate this, and we want to turn on the charter flag.
		final VoyageOptions options = ballastDetails.getOptions().clone();
		newRawSequence.set(ballastIdx, options);
		// Turn on chartering
		options.setCharterOutIdleTime(true);
		options.setCharterOutDailyRate(bestDailyPrice);

		// Construct a new VPO instance (TODO - use injection provider)
		// TODO: Use the central caching VPO?
		// final VoyagePlanOptimiser vpo = new VoyagePlanOptimiser(voyageCalculator);

		vpo.reset();

		vpo.setVessel(vessel, baseFuelUnitPricePerMT);
		vpo.setVesselCharterInRatePerDay(vesselCharterInRatePerDay);
		vpo.setStartHeel(startingHeelInM3);
		// Install our new alternative sequence
		vpo.setBasicSequence(newRawSequence);

		vpo.setPortTimesRecord(portTimesRecord);

		// Add in the route choice
		{
			final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(options.getFromPortSlot().getPort(), options.getToPortSlot()
					.getPort()));
			// Only add route choice if there is one
			if (distances.size() == 1) {
				final MatrixEntry<IPort, Integer> d = distances.get(0);
				options.setDistance(d.getValue());
				options.setRoute(d.getKey());
			} else {
				vpo.addChoice(new RouteVoyagePlanChoice(options, distances));
			}
		}

		// Add in NBO etc choices
		vpo.addChoice(new NBOTravelVoyagePlanChoice(ladenDetails == null ? null : ladenDetails.getOptions(), options));
		vpo.addChoice(new FBOVoyagePlanChoice(options));
		vpo.addChoice(new IdleNBOVoyagePlanChoice(options));

		// Calculate our new plan
		final VoyagePlan newVoyagePlan = vpo.optimise();

		// Calculate the P&L of both the original and the new option
		final long originalOption;
		final long newOption;

		final IAllocationAnnotation newAllocation;
		if (isCargoPlan) {
			// Get the new cargo allocation.

			final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vesselStartTime, vp, portTimesRecord);
			newAllocation = cargoAllocator.allocate(vessel, vesselStartTime, newVoyagePlan, portTimesRecord);

			originalOption = entityValueCalculator.evaluate(vp, currentAllocation, vessel, vesselStartTime, null);
			newOption = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, vesselStartTime, null);

		} else {
			originalOption = entityValueCalculator.evaluate(vp, vessel, portTimesRecord.getFirstSlotTime(), vesselStartTime, null);
			newOption = entityValueCalculator.evaluate(newVoyagePlan, vessel, portTimesRecord.getFirstSlotTime(), vesselStartTime, null);
			newAllocation = null;
		}
		// TODO: This should be recorded based on market availability groups and then processed.
		if (originalOption >= newOption) {
			// Keep
			return null;
		} else {
			// Overwrite details
			return new Pair<>(newVoyagePlan, newAllocation);
		}
	}
}
