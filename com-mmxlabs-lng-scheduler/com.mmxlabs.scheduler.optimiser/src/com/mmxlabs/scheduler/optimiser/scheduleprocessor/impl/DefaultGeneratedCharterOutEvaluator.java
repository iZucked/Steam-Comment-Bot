/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.FBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.NBOTravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.RouteVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public class DefaultGeneratedCharterOutEvaluator implements IGeneratedCharterOutEvaluator {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IVolumeAllocator cargoAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Override
	public void processSchedule(final ScheduledSequences scheduledSequences) {
		// Charter Out Optimisation... Detect potential charter out opportunities.
		for (final ScheduledSequence seq : scheduledSequences) {

			final IVessel vessel = vesselProvider.getVessel(seq.getResource());
			if (!(vessel.getVesselInstanceType() == VesselInstanceType.FLEET || vessel.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
				continue;
			}

			int currentTime = seq.getStartTime();

			for (final VoyagePlan vp : seq.getVoyagePlans()) {

				// First step, find a ballast leg which is long enough to charter-out

				boolean isCargoPlan = false;
				// Grab the current list of arrival times and update the rolling currentTime
				// 5 as we know that is the max we need (currently - a single cargo)
				final int[] arrivalTimes = new int[5];
				int idx = -1;
				arrivalTimes[++idx] = currentTime;
				final Object[] currentSequence = vp.getSequence();
				int ladenIdx = -1;
				int ballastIdx = -1;
				for (final Object obj : currentSequence) {
					if (obj instanceof PortDetails) {
						final PortDetails details = (PortDetails) obj;
						if (idx != (currentSequence.length - 1)) {
							currentTime += details.getOptions().getVisitDuration();
							arrivalTimes[++idx] = currentTime;

							if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
								isCargoPlan = true;
							}

						}
					} else if (obj instanceof VoyageDetails) {
						final VoyageDetails details = (VoyageDetails) obj;
						currentTime += details.getOptions().getAvailableTime();
						arrivalTimes[++idx] = currentTime;

						// record last ballast leg
						if (details.getOptions().getVesselState() == VesselState.Ballast) {
							ballastIdx = idx - 1;
						} else {
							ladenIdx = idx - 1;
						}
					}
				}

				if (ballastIdx == -1) {
					// no ballast leg?
					continue;
				}

				// Found a ballast leg, now look at the markets to see if it is long enough
				final VoyageDetails ladenDetails = ladenIdx == -1 ? null : (VoyageDetails) currentSequence[ladenIdx];
				final VoyageDetails ballastDetails = (VoyageDetails) currentSequence[ballastIdx];

				boolean foundMarketPrice = false;
				int bestPrice = 0;
				final int time = arrivalTimes[ballastIdx] + ballastDetails.getTravelTime();

				final int availableTime = ballastDetails.getOptions().getAvailableTime();
				final int distance = ballastDetails.getOptions().getDistance();
				final int maxSpeed = ballastDetails.getOptions().getVessel().getVesselClass().getMaxSpeed();

				final int travelTime = Calculator.getTimeFromSpeedDistance(maxSpeed, distance);

				final int availableCharteringTime = availableTime - travelTime;

				// Scan all the markets for a match
				for (final CharterMarketOptions option : charterMarketProvider.getCharterOutOptions(vessel.getVesselClass(), time)) {
					if (availableCharteringTime >= option.getMinDuration() && option.getCharterPrice() > bestPrice) {
						foundMarketPrice = true;
						bestPrice = option.getCharterPrice();
					}
				}
				// Have we found a market?
				if (!foundMarketPrice) {
					continue;
				}

				// We will use the VPO to optimise fuel and route choices
				final List<Object> newRawSequence = new ArrayList<Object>(currentSequence.length);
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
				VoyageOptions options;
				try {
					options = ballastDetails.getOptions().clone();
				} catch (final CloneNotSupportedException e) {
					// Do not expect this, VoyageOptions implements Cloneable
					throw new RuntimeException(e);
				}
				newRawSequence.set(ballastIdx, options);
				// Turn on chartering
				options.setCharterOutIdleTime(true);
				options.setCharterOutHourlyRate(bestPrice);

				// Construct a new VPO instance (TODO - use injection provider)
				final VoyagePlanOptimiser vpo = new VoyagePlanOptimiser(voyageCalculator);
				vpo.setVessel(vessel, seq.getStartTime());

				// Install our new alternative sequence
				vpo.setBasicSequence(newRawSequence);

				// Rebuilt the arrival times list
				final List<Integer> currentTimes = new ArrayList<Integer>(3);
				for (final int t : arrivalTimes) {
					currentTimes.add(t);
				}
				vpo.setArrivalTimes(currentTimes);

				// Add in the route choice
				{
					final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(options.getFromPortSlot().getPort(), options
							.getToPortSlot().getPort()));
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
				if (isCargoPlan) {
					// Get the new cargo allocation.
					final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vp, arrivalTimes);
					final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, newVoyagePlan, arrivalTimes);

					originalOption = entityValueCalculator.evaluate(vp, currentAllocation, vessel, seq.getStartTime(), null);
					newOption = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, seq.getStartTime(), null);

				} else {
					originalOption = entityValueCalculator.evaluate(vp, vessel, arrivalTimes[0], seq.getStartTime(), null);
					newOption = entityValueCalculator.evaluate(newVoyagePlan, vessel, arrivalTimes[0], seq.getStartTime(), null);

				}
				// TODO: This should be recorded based on market availability groups and then processed.
				if (originalOption >= newOption) {
					// Keep
				} else {
					// Overwrite details
					voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newVoyagePlan.getSequence());
				}
			}
		}
	}
}
