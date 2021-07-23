/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.GeneratedCharterLengthPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterLengthEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Convert idle voyages over a certain number of days into a separate P&L event. We construct a new zero duration event and pass through heel info etc so that the new split voyage plans are
 * equivalent. During export to EMF the PortDetails and VoyageDetails are combined into a single event.
 * 
 * Note: Currently we run the VPO again on the data to evaluate the data however we do not change any choices. This can however lead to some small changes in fuel consumptions due to the
 * re-evaluation. We could instead manually clone and populate the voyage details.
 * 
 */
@NonNullByDefault
public class CharterLengthEvaluator implements IGeneratedCharterLengthEvaluator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	// See default binding in LNGEvaluationModule - typically 2 weeks
	@Inject
	@Named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)
	private int minIdleTimeInHours;

	/**
	 * If false, split any idle that is large enough. If true, split the idle at the point we run out of NBO and the remaining time is large enough.
	 */
	private static final boolean SPLIT_ON_RUNDRY = false;

	@Override
	public @Nullable List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final long[] startHeelVolumeRangeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp,
			final IPortTimesRecord portTimesRecord, @Nullable IAnnotatedSolution annotatedSolution) {

		// Only apply to "real" vessels. Exclude nominal/round-trip vessels.
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find the final ballast leg and then see if it is long enough to convert to a charter length event.
		final Object[] currentSequence = vp.getSequence();
		int ballastIdx = -1;
		int ballastStartTime = -1;
		PortDetails firstDetails = null;
		VoyageDetails ballastDetails = null;

		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails && firstDetails == null) {
				firstDetails = (PortDetails) obj;
			} else if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				// record last ballast leg
				if (details.getOptions().getVesselState() == VesselState.Ballast) {
					ballastIdx = idx;
					ballastStartTime = portTimesRecord.getSlotTime(details.getOptions().getFromPortSlot()) + portTimesRecord.getSlotDuration(details.getOptions().getFromPortSlot());
					ballastDetails = details;
				}
			}
		}

		assert ((ballastIdx < 0) == (ballastStartTime < 0));
		if (firstDetails == null || ballastIdx == -1 || ballastStartTime == -1 || ballastDetails == null) {
			// no ballast leg?
			return null;
		}

		// Check idle length. Note: This is idle based on current speed. While we could speed up the vessel to get more idle time, this is not what we want to do.
		if (SPLIT_ON_RUNDRY) {
			if ((ballastDetails.getIdleTime() - ballastDetails.getIdleNBOHours()) < minIdleTimeInHours) {
				return null;
			}
		} else {
			if (ballastDetails.getIdleTime() < minIdleTimeInHours) {
				return null;
			}
		}

		// We have a candidate voyage plan with enough time, pass into the method to split into two plans and recompute values.
		List<Pair<VoyagePlan, IPortTimesRecord>> r = generateNewVoyagePlansWithCharterLength(vesselAvailability, vp, portTimesRecord, currentSequence, ballastIdx, ballastStartTime, annotatedSolution);

		// Sanity checking of heel info
		if (r.size() > 1) {
			// Assuming two plans
			VoyagePlan p1 = r.get(0).getFirst();
			VoyagePlan p2 = r.get(1).getFirst();

			{
				long startHeel = p1.getStartingHeelInM3();
				long bog = p1.getLNGFuelVolume();
				long endHeel = p1.getRemainingHeelInM3();

				// assert endHeel == startHeel - bog;
			}
			// Check the start heel - bog == end heel is correct
			{
				long startHeel = p2.getStartingHeelInM3();
				long bog = p2.getLNGFuelVolume();
				long endHeel = p2.getRemainingHeelInM3();

				assert endHeel == startHeel - bog;
			}
			// Check heel between events matches.
			assert p1.getRemainingHeelInM3() == p2.getStartingHeelInM3();
			IGeneratedCharterLengthEventPortSlot eventSlot = (IGeneratedCharterLengthEventPortSlot) ((PortDetails) p2.getSequence()[0]).getOptions().getPortSlot();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsSupplier().getMinimumHeelAvailableInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
		}

		return r;
	}

	/**
	 * Produces a new large sequence with the original voyage and a charter length event inserted after the final ballast travel to replace the idle time.
	 * 
	 */
	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlansWithCharterLength(final IVesselAvailability vesselAvailability, final VoyagePlan originalPlan,
			final IPortTimesRecord originalPortTimesRecord, final Object[] currentSequence, final int ballastIdx, final int ballastStartTime, @Nullable IAnnotatedSolution annotatedSolution) {

		final IVessel vessel = vesselAvailability.getVessel();
		final int[] baseFuelPricesPerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, originalPortTimesRecord);

		// Grab the target ballast leg we want to convert.
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];

		// Manually construct the voyage details based on previous data.
		VoyageDetails intermediateResult = null;
		if (SPLIT_ON_RUNDRY && originalBallast.getIdleNBOHours() > 0 && originalBallast.getIdleNBOHours() < originalBallast.getIdleTime()) {
			// With run-dry we NBO until we run out then switch to bunkers. Due to e.g. min base fuel we can't easily split the original details so we re-compute just the NBO part of the voyage. We
			// can then apply this later to the original when we do the split.

			VoyageOptions intermediateOptions = originalBallast.getOptions().copy();
			intermediateOptions.setAvailableTime(originalBallast.getIdleNBOHours());
			intermediateOptions.setFromPortSlot(originalBallast.getOptions().getToPortSlot());
			intermediateOptions.setRoute(ERouteOption.DIRECT, 0, 0);

			intermediateResult = new VoyageDetails(intermediateOptions);
			voyageCalculator.calculateVoyageFuelRequirements(intermediateOptions, intermediateResult, Long.MAX_VALUE);

		}

		// Calculate how much heel we need to keep to cover charter length event idle and heel for next event. This is the original end heel + idle BOG.
		long idleBOGInM3 = originalBallast.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
		long charterLengthHeelInM3 = originalPlan.getRemainingHeelInM3() + (SPLIT_ON_RUNDRY ? 0 : idleBOGInM3);

		// Set explicit values as we already know what the heel should be at the switch over point.
		final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(charterLengthHeelInM3, charterLengthHeelInM3, idleBOGInM3 > 0 ? VesselTankState.MUST_BE_COLD : VesselTankState.EITHER,
				new ConstantHeelPriceCalculator(0), false);
		final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(charterLengthHeelInM3, charterLengthHeelInM3, originalBallast.getOptions().getCargoCVValue(),
				new ConstantHeelPriceCalculator(0));

		// Dynamically create the charter length port slot.
		final GeneratedCharterLengthPortSlot charterLengthPortSlot = new GeneratedCharterLengthPortSlot(
				/* ID based on from-to ids */ String.format("charterlength-%s-%s", originalBallast.getOptions().getFromPortSlot().getId(), originalBallast.getOptions().getToPortSlot().getId()), //
				/* Time window - set later */ null, //
				/* Charter length will be at destination port */ originalBallast.getOptions().getToPortSlot().getPort(), //
				heelOptionConsumer, //
				heelOptionSupplier //
		);

		// Now we start to construct the new voyage plans

		// (1) ballast to charter length event
		final VoyageOptions ballastStartToCharterLengthPortVoyageOptions = originalBallast.getOptions().copy();
		// Change the available time to just include travel time. Idle time will be moved into a new voyage.
		ballastStartToCharterLengthPortVoyageOptions.setAvailableTime(originalBallast.getTravelTime());
		// Replace the return port with the charget length
		ballastStartToCharterLengthPortVoyageOptions.setToPortSlot(charterLengthPortSlot);

		if (originalBallast.isCooldownPerformed()) {
			// May need to tweak these
			ballastStartToCharterLengthPortVoyageOptions.setShouldBeCold(VesselTankState.EITHER);
			// dischargeToCharterPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
			// dischargeToCharterPortVoyageOptions.setAllowCooldown(false);
		}

		// (2) New charter length port visit
		final PortOptions generatedCharterPortOptions = new PortOptions(charterLengthPortSlot);
		generatedCharterPortOptions.setVessel(originalBallast.getOptions().getVessel());
		generatedCharterPortOptions.setVisitDuration(0);
		generatedCharterPortOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());

		// (3) New charter lenth IDLE to return port
		final VoyageOptions charterToReturnPortVoyageOptions = originalBallast.getOptions().copy();
		// Exclude the orignal travel time and just include the idle/purge time.
		charterToReturnPortVoyageOptions.setAvailableTime(originalBallast.getIdleTime() + originalBallast.getPurgeDuration());
		// Update the origin port to the charter length
		charterToReturnPortVoyageOptions.setFromPortSlot(charterLengthPortSlot);
		// No distance or costs to apply
		charterToReturnPortVoyageOptions.setRoute(originalBallast.getOptions().getRoute(), 0, 0);

		// This leg is just the travel values, so clear out any idle portion.
		final VoyageDetails newTravelToCharterLength = originalBallast.clone();
		{
			newTravelToCharterLength.setOptions(ballastStartToCharterLengthPortVoyageOptions);
			// Any cooldown or purge details go on the charter length
			newTravelToCharterLength.setCooldownPerformed(false);
			newTravelToCharterLength.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 0);
			newTravelToCharterLength.setPurgePerformed(false);
			newTravelToCharterLength.setPurgeDuration(0);

			if (SPLIT_ON_RUNDRY && intermediateResult != null) {
				// Copy Idle time values. These should be identical
				newTravelToCharterLength.setIdleTime(intermediateResult.getIdleTime());
				newTravelToCharterLength.setIdleNBOHours(intermediateResult.getIdleNBOHours());

				// Update the base fuel as it only covers part of the voyage now. We do not update the LNG as this should already have been correct as we split when we run out.
				newTravelToCharterLength.setFuelConsumption(vessel.getIdleBaseFuelInMT(), intermediateResult.getFuelConsumption(vessel.getIdleBaseFuelInMT()));
				newTravelToCharterLength.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), intermediateResult.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));
			} else {

				// Clear Idle values
				newTravelToCharterLength.setIdleTime(0);
				newTravelToCharterLength.setIdleNBOHours(0);

				newTravelToCharterLength.setFuelConsumption(vessel.getIdleBaseFuelInMT(), 0);
				newTravelToCharterLength.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), 0);
				newTravelToCharterLength.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 0);
				for (final FuelKey fk : LNGFuelKeys.Idle_LNG) {
					newTravelToCharterLength.setFuelConsumption(fk, 0);
				}
			}
		}

		// This leg is the idle time that will become the charter length, so clear out any travel elements
		final VoyageDetails newIdleFromCharterLength = originalBallast.clone();
		{
			newIdleFromCharterLength.setOptions(charterToReturnPortVoyageOptions);
			// Clear Travel values
			newIdleFromCharterLength.setTravelTime(0);
			newIdleFromCharterLength.setTravelNBOHours(0);
			newIdleFromCharterLength.setSpeed(0);
			for (final FuelKey fk : vessel.getTravelFuelKeys()) {
				newIdleFromCharterLength.setFuelConsumption(fk, 0);
				newIdleFromCharterLength.setRouteAdditionalConsumption(fk, 0);
			}
			for (final FuelKey fk : LNGFuelKeys.Travel_LNG) {
				newIdleFromCharterLength.setFuelConsumption(fk, 0);
				newIdleFromCharterLength.setRouteAdditionalConsumption(fk, 0);
			}

			if (SPLIT_ON_RUNDRY && intermediateResult != null) {
				// The new idle time is the remaining portion. Could also do idletime - idleNBOhours
				newIdleFromCharterLength.setIdleTime(newIdleFromCharterLength.getIdleTime() - intermediateResult.getIdleTime());
				newIdleFromCharterLength.setIdleNBOHours(0);

				// Subtract the bunker values from the NBo part of the idle time
				newIdleFromCharterLength.setFuelConsumption(vessel.getIdleBaseFuelInMT(),
						newIdleFromCharterLength.getFuelConsumption(vessel.getIdleBaseFuelInMT()) - intermediateResult.getFuelConsumption(vessel.getIdleBaseFuelInMT()));
				newIdleFromCharterLength.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(),
						newIdleFromCharterLength.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()) - intermediateResult.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));

				// By definition all LNG is in the non-charter length portion
				for (final FuelKey fk : LNGFuelKeys.Idle_LNG) {
					newIdleFromCharterLength.setFuelConsumption(fk, 0);
				}
			}
		}

		// Sanity checks
		assert newIdleFromCharterLength.isCooldownPerformed() == originalBallast.isCooldownPerformed();
		assert newIdleFromCharterLength.isPurgePerformed() == originalBallast.isPurgePerformed();

		final List<IDetailsSequenceElement> partialSequenceUpToCharterLength = new LinkedList<>();

		// build new sequence up to and not including last ballast leg from original data.
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				partialSequenceUpToCharterLength.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				partialSequenceUpToCharterLength.add(voyageDetails.clone());
			}
		}
		// Now add in new travel leg and final charter length event
		final PortDetails portDetails = new PortDetails(generatedCharterPortOptions);
		partialSequenceUpToCharterLength.add(newTravelToCharterLength);
		partialSequenceUpToCharterLength.add(portDetails);

		// build new sequence from the new charter length event to the return port using the newly created port and voyage details.
		final List<IDetailsSequenceElement> partialSequenceFromCharterLength = new LinkedList<>();
		partialSequenceFromCharterLength.add(portDetails);
		partialSequenceFromCharterLength.add(newIdleFromCharterLength);
		// Include any pre-existing data from the original sequence that happened after the target ballast leg (including final port details)
		for (int i = ballastIdx + 1; i < currentSequence.length; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				partialSequenceFromCharterLength.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				partialSequenceFromCharterLength.add(voyageDetails.clone());
			}
		}

		final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<>();

		// Calculate pre-charter length voyage plan

		// Use the original heel value as the range.
		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalPlan.getStartingHeelInM3();
		startHeelRangeInM3[1] = originalPlan.getStartingHeelInM3();

		{
			final PortTimesRecord portTimesRecord1 = new PortTimesRecord();

			// Clone all the existing data
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); i++) {
				final IPortSlot portSlot = originalPortTimesRecord.getSlots().get(i);
				portTimesRecord1.setSlotTime(portSlot, originalPortTimesRecord.getSlotTime(portSlot));
				portTimesRecord1.setSlotDuration(portSlot, originalPortTimesRecord.getSlotDuration(portSlot));
				portTimesRecord1.setSlotExtraIdleTime(portSlot, originalPortTimesRecord.getSlotExtraIdleTime(portSlot));
				portTimesRecord1.setSlotNextVoyageOptions(portSlot, originalPortTimesRecord.getSlotNextVoyageOptions(portSlot));
			}
			portTimesRecord1.setReturnSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime() + (SPLIT_ON_RUNDRY ? originalBallast.getIdleNBOHours() : 0));

			//
			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(true);

			// Calculate voyage plan
			final long[] violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricesPerMT,
					portTimesRecord1, partialSequenceUpToCharterLength.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != null;

			// Make sure this is still the same
			assert currentPlan.getStartingHeelInM3() == originalPlan.getStartingHeelInM3();

			currentPlan.setRemainingHeelInM3(charterLengthHeelInM3);

			// Recompute the volume (and P&L) for the reduced voyage plan
			final IAllocationAnnotation allocation = volumeAllocator.get().allocate(vesselAvailability, currentPlan, portTimesRecord1, annotatedSolution);
			if (allocation != null) {
				charterPlans.add(Pair.of(currentPlan, allocation));

				// We don't expect this to change....
				assert allocation.getRemainingHeelVolumeInM3() == charterLengthHeelInM3;

				// Update the starting heel range ready for second voyage plan
				startHeelRangeInM3[0] = allocation.getRemainingHeelVolumeInM3();
				startHeelRangeInM3[1] = allocation.getRemainingHeelVolumeInM3();
			} else {
				// Null because the voyage plan was not a cargo.
				charterPlans.add(Pair.of(currentPlan, portTimesRecord1));

				// Update the starting heel range ready for second voyage plan
				startHeelRangeInM3[0] = currentPlan.getRemainingHeelInM3();
				startHeelRangeInM3[1] = currentPlan.getRemainingHeelInM3();

			}
			// Ensure this flag is still set correctly
			currentPlan.setIgnoreEnd(true);
		}

		// New voyage plan representing the charter length (here the zero length event plus idle time to roll in during export).
		{

			final PortTimesRecord portTimesRecord2 = new PortTimesRecord();

			portTimesRecord2.setSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime() + (SPLIT_ON_RUNDRY ? originalBallast.getIdleNBOHours() : 0));
			portTimesRecord2.setSlotDuration(charterLengthPortSlot, 0);
			portTimesRecord2.setSlotExtraIdleTime(charterLengthPortSlot, 0);

			IPortSlot returnSlot = originalPortTimesRecord.getReturnSlot();
			if (returnSlot != null) {
				portTimesRecord2.setReturnSlotTime(returnSlot, originalPortTimesRecord.getSlotTime(returnSlot));
			}

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			// Calculate voyage plan
			final long[] violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, vesselAvailability.getCharterCostCalculator(), startHeelRangeInM3, baseFuelPricesPerMT,
					portTimesRecord2, partialSequenceFromCharterLength.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != null;

			charterPlans.add(Pair.of(currentPlan, portTimesRecord2));

			// Make sure we retain the original plan value
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			// These should match
			assert currentPlan.getStartingHeelInM3() == charterLengthHeelInM3;
			assert currentPlan.getRemainingHeelInM3() == originalPlan.getRemainingHeelInM3();

		}
		return charterPlans;

	}

}