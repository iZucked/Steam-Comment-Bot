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
import com.mmxlabs.scheduler.optimiser.providers.IBaseFuelProvider;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterLengthEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
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
 * Convert idle voyages over a certain number of days into a separate P&L event.
 * We construct a new zero duration event and pass through heel info etc so that
 * the new split voyage plans are equivalent. During export to EMF the
 * PortDetails and VoyageDetails are combined into a single event.
 * 
 * Note: Currently we run the VPO again on the data to evaluate the data however
 * we do not change any choices. This can however lead to some small changes in
 * fuel consumptions due to the re-evaluation. We could instead manually clone
 * and populate the voyage details.
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

	@Inject
	private IBaseFuelProvider baseFuelProvider;

	// See default binding in LNGEvaluationModule - typically 2 weeks
	@Inject
	@Named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)
	private int minIdleTimeInHours;

	/**
	 * If false, split any idle that is large enough. If true, split the idle at the
	 * point we run out of NBO and the remaining time is large enough.
	 */
	private static final boolean SPLIT_ON_RUNDRY = false;

	@Override
	public @Nullable List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final long[] startHeelVolumeRangeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp,
			final IPortTimesRecord portTimesRecord, @Nullable final IAnnotatedSolution annotatedSolution) {

		// Only apply to "real" vessels. Exclude nominal/round-trip vessels.
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find the final ballast leg and then see if it is long enough to
		// convert to a charter length event.
		final Object[] currentSequence = vp.getSequence();
		int ballastIdx = -1;
		int ballastStartTime = -1;
		PortDetails firstDetails = null;
		VoyageDetails ballastDetails = null;

		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof final PortDetails portDetails && firstDetails == null) {
				firstDetails = portDetails;
			} else if (obj instanceof final VoyageDetails details) {
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

		// Check idle length. Note: This is idle based on current speed. While we could
		// speed up the vessel to get more idle time, this is not what we want to do.
		if (SPLIT_ON_RUNDRY) {
			if ((ballastDetails.getIdleTime() - ballastDetails.getIdleNBOHours()) < minIdleTimeInHours) {
				return null;
			}
		} else {
			// Exclude panama idle from charter length time
			if (ballastDetails.getIdleTime() - ballastDetails.getOptions().getPanamaIdleHours() < minIdleTimeInHours) {
				return null;
			}
		}

		// We have a candidate voyage plan with enough time, pass into the method to
		// split into two plans and recompute values.
		final List<Pair<VoyagePlan, IPortTimesRecord>> r = generateNewVoyagePlansWithCharterLength(vesselAvailability, vp, portTimesRecord, currentSequence, ballastIdx, ballastStartTime,
				annotatedSolution);

		// Sanity checking of heel info
		if (r.size() > 1) {
			// Assuming two plans
			final VoyagePlan p1 = r.get(0).getFirst();
			final VoyagePlan p2 = r.get(1).getFirst();

			{
				final long startHeel = p1.getStartingHeelInM3();
				final long bog = p1.getLNGFuelVolume();
				final long endHeel = p1.getRemainingHeelInM3();

				// assert endHeel == startHeel - bog;
			}
			// Check the start heel - bog == end heel is correct
			{
				final long startHeel = p2.getStartingHeelInM3();
				final long bog = p2.getLNGFuelVolume();
				final long endHeel = p2.getRemainingHeelInM3();

				assert endHeel == startHeel - bog;
			}
			// Check heel between events matches.
			assert p1.getRemainingHeelInM3() == p2.getStartingHeelInM3();
			final IGeneratedCharterLengthEventPortSlot eventSlot = (IGeneratedCharterLengthEventPortSlot) ((PortDetails) p2.getSequence()[0]).getOptions().getPortSlot();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsConsumer().getMinimumHeelAcceptedInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsConsumer().getMaximumHeelAcceptedInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsSupplier().getMinimumHeelAvailableInM3();
			assert p2.getStartingHeelInM3() == eventSlot.getHeelOptionsSupplier().getMaximumHeelAvailableInM3();
		}

		return r;
	}

	/**
	 * Produces a new large sequence with the original voyage and a charter length
	 * event inserted after the final ballast travel to replace the idle time.
	 * 
	 */
	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlansWithCharterLength(final IVesselAvailability vesselAvailability, final VoyagePlan originalPlan,
			final IPortTimesRecord originalPortTimesRecord, final Object[] currentSequence, final int ballastIdx, final int ballastStartTime, @Nullable final IAnnotatedSolution annotatedSolution) {

		final IVessel vessel = vesselAvailability.getVessel();
		final int[] baseFuelPricesPerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, originalPortTimesRecord);

		// Grab the target ballast leg we want to convert.
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];

		// Determine when we split off the charter length. The simple case is to just
		// convert the whole idle leg. However any panama waiting days need to be kept
		// with the original event. Finally we may also split when we run dry. If both
		// panama and run-dry are to be considered, we pick the later of the two times.
		int splitPoint;
		if (SPLIT_ON_RUNDRY) {
			if (originalBallast.getOptions().getPanamaIdleHours() > 0) {
				// Split when we ran-dry or finished panama waiting days - whichever is longest
				splitPoint = Math.max(originalBallast.getOptions().getPanamaIdleHours(), originalBallast.getIdleNBOHours());
			} else {
				// Split when we ran dry
				splitPoint = originalBallast.getIdleNBOHours();
			}
		} else if (originalBallast.getOptions().getPanamaIdleHours() > 0) {
			splitPoint = originalBallast.getOptions().getPanamaIdleHours();
		} else {
			// Split immediately - i.e. the whole idle leg
			splitPoint = 0;
		}

		// If we split the idle up the the intermediateResult is the idle leg portion
		// for the event. We re-calculate costs for period then update the event idle
		// with these values and subtract from the original for the charter length part.
		VoyageDetails intermediateResult = null;
		if (splitPoint > 0 && splitPoint < originalBallast.getIdleTime()) {
			// We are splitting up the idle leg.

			// With run-dry we NBO until we run out then switch to bunkers. Due to e.g. min
			// base fuel we can't easily split the original details so we re-compute just
			// the NBO part of the voyage. We
			// can then apply this later to the original when we do the split.

			final VoyageOptions intermediateOptions = originalBallast.getOptions().copy();
			intermediateOptions.setAvailableTime(splitPoint);
			intermediateOptions.setFromPortSlot(originalBallast.getOptions().getToPortSlot());
			intermediateOptions.setRoute(ERouteOption.DIRECT, 0, 0, 0);

			for (final var type : ExplicitIdleTime.values()) {
				intermediateOptions.setExtraIdleTime(type, 0);
			}

			intermediateResult = new VoyageDetails(intermediateOptions);
			// Use existing BOG value as the heel so that we can run-dry if needed.
			final long idleBOGInM3 = originalBallast.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
			voyageCalculator.calculateVoyageFuelRequirements(intermediateOptions, intermediateResult, idleBOGInM3);

		}

		// Calculate how much heel we need to keep to cover charter length event idle
		// and heel for next event. This is the original end heel + idle BOG.
		long idleBOGInM3 = originalBallast.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
		if (intermediateResult != null) {
			idleBOGInM3 -= intermediateResult.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
		}
		final long charterLengthHeelInM3 = originalPlan.getRemainingHeelInM3() + idleBOGInM3;

		// Set explicit values as we already know what the heel should be at the switch
		// over point.
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
		// Reset purge, contingency matrix etc. These will be set in the charter length
		// options instead.
		for (final var type : ExplicitIdleTime.values()) {
			ballastStartToCharterLengthPortVoyageOptions.setExtraIdleTime(type, 0);
		}
		// Change the available time to just include travel time. Idle time will be
		// moved into a new voyage.
		ballastStartToCharterLengthPortVoyageOptions.setAvailableTime(originalBallast.getTravelTime() + splitPoint);
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

		// (3) New charter length IDLE to return port
		final VoyageOptions charterToReturnPortVoyageOptions = originalBallast.getOptions().copy();
		// Exclude the original travel time and just include the idle/purge time.
		charterToReturnPortVoyageOptions.setAvailableTime((originalBallast.getIdleTime() - splitPoint) + originalBallast.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE));
		for (final var type : ExplicitIdleTime.values()) {
			charterToReturnPortVoyageOptions.setExtraIdleTime(type, originalBallast.getOptions().getExtraIdleTime(type));
		}

		// Update the origin port to the charter length
		charterToReturnPortVoyageOptions.setFromPortSlot(charterLengthPortSlot);
		// No distance or costs to apply
		charterToReturnPortVoyageOptions.setRoute(originalBallast.getOptions().getRoute(), 0, 0, 0);

		// This leg is just the travel values, so clear out any idle portion.
		final VoyageDetails newTravelToCharterLength = originalBallast.copy();
		{
			newTravelToCharterLength.setOptions(ballastStartToCharterLengthPortVoyageOptions);
			// Any cooldown or purge details go on the charter length
			newTravelToCharterLength.setCooldownPerformed(false);
			newTravelToCharterLength.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 0);
			newTravelToCharterLength.setPurgePerformed(false);

			if (intermediateResult != null) {
				// We have split the voyage, so there is some idle values to set here. The
				// intermediateResult should be the relevant values to use

				// Set Idle time values.
				newTravelToCharterLength.setIdleTime(intermediateResult.getIdleTime());
				newTravelToCharterLength.setIdleNBOHours(intermediateResult.getIdleNBOHours());

				// Set idle NBO values
				for (final FuelKey fk : LNGFuelKeys.Idle_LNG) {
					newTravelToCharterLength.setFuelConsumption(fk, intermediateResult.getFuelConsumption(fk));
				}

				// Set bunker fuels use
				newTravelToCharterLength.setFuelConsumption(vessel.getIdleBaseFuelInMT(), intermediateResult.getFuelConsumption(vessel.getIdleBaseFuelInMT()));
				newTravelToCharterLength.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), intermediateResult.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));
			} else {
				// No split, so no idle

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

		// This leg is the idle time that will become the charter length, so clear out
		// any travel elements
		final VoyageDetails newIdleFromCharterLength = originalBallast.copy();
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

			if (intermediateResult != null) {
				// The new idle time is the remaining portion. Could also do idletime -
				// idleNBOhours
				newIdleFromCharterLength.setIdleTime(newIdleFromCharterLength.getIdleTime() - intermediateResult.getIdleTime());
				newIdleFromCharterLength.setIdleNBOHours(newIdleFromCharterLength.getIdleNBOHours() - intermediateResult.getIdleNBOHours());

				// Subtract the bunker values from the NBO part of the idle time
				newIdleFromCharterLength.setFuelConsumption(vessel.getIdleBaseFuelInMT(),
						newIdleFromCharterLength.getFuelConsumption(vessel.getIdleBaseFuelInMT()) - intermediateResult.getFuelConsumption(vessel.getIdleBaseFuelInMT()));

				newIdleFromCharterLength.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(),
						newIdleFromCharterLength.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()) - intermediateResult.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()));

				assert newIdleFromCharterLength.getFuelConsumption(vessel.getIdleBaseFuelInMT()) >= 0;
				assert newIdleFromCharterLength.getFuelConsumption(vessel.getIdlePilotLightFuelInMT()) >= 0;
				// Likewise take off the values
				for (final FuelKey fk : LNGFuelKeys.Idle_LNG) {
					newIdleFromCharterLength.setFuelConsumption(fk, newIdleFromCharterLength.getFuelConsumption(fk) - intermediateResult.getFuelConsumption(fk));
					assert newIdleFromCharterLength.getFuelConsumption(fk) >= 0;
				}
			}
		}

		// Sanity checks
		assert newIdleFromCharterLength.isCooldownPerformed() == originalBallast.isCooldownPerformed();
		assert newIdleFromCharterLength.isPurgePerformed() == originalBallast.isPurgePerformed();

		final List<IDetailsSequenceElement> partialSequenceUpToCharterLength = new LinkedList<>();

		// build new sequence up to and not including last ballast leg from original
		// data.
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof final PortDetails portDetails) {
				partialSequenceUpToCharterLength.add(portDetails.copy());
			} else if (o instanceof final VoyageDetails voyageDetails) {
				partialSequenceUpToCharterLength.add(voyageDetails.copy());
			}
		}
		// Now add in new travel leg and final charter length event
		final PortDetails portDetails = new PortDetails(generatedCharterPortOptions);
		partialSequenceUpToCharterLength.add(newTravelToCharterLength);
		partialSequenceUpToCharterLength.add(portDetails);

		// build new sequence from the new charter length event to the return port using
		// the newly created port and voyage details.
		final List<IDetailsSequenceElement> partialSequenceFromCharterLength = new LinkedList<>();
		partialSequenceFromCharterLength.add(portDetails);
		partialSequenceFromCharterLength.add(newIdleFromCharterLength);
		// Include any pre-existing data from the original sequence that happened after
		// the target ballast leg (including final port details)
		for (int i = ballastIdx + 1; i < currentSequence.length; i++) {
			final Object o = currentSequence[i];
			if (o instanceof final PortDetails portDetails2) {
				partialSequenceFromCharterLength.add(portDetails2.copy());
			} else if (o instanceof final VoyageDetails voyageDetails) {
				partialSequenceFromCharterLength.add(voyageDetails.copy());
			}
		}

		final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<>();

		// Calculate pre-charter length voyage plan

		// Use the original heel value as the range.
		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalPlan.getStartingHeelInM3();
		startHeelRangeInM3[1] = originalPlan.getStartingHeelInM3();

		IPortSlot lastSlotInFirstRecord = null;
		{
			final PortTimesRecord portTimesRecord1 = new PortTimesRecord();

			// Clone all the existing data
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); i++) {
				final IPortSlot portSlot = originalPortTimesRecord.getSlots().get(i);
				portTimesRecord1.setSlotTime(portSlot, originalPortTimesRecord.getSlotTime(portSlot));
				portTimesRecord1.setSlotDuration(portSlot, originalPortTimesRecord.getSlotDuration(portSlot));
				{
					// Copy the extra idle time across. If this is the last leg, then it will be set
					// to zero later and copied into the second PTR
					// Note: Market Buffer should never appear on the same leg as a charter length.
					// Contingency matrix and purge time may do.
					for (final var type : ExplicitIdleTime.values()) {
						portTimesRecord1.setSlotExtraIdleTime(portSlot, type, originalPortTimesRecord.getSlotExtraIdleTime(portSlot, type));
					}
				}
				portTimesRecord1.setSlotNextVoyageOptions(portSlot, originalPortTimesRecord.getSlotNextVoyageOptions(portSlot));

				portTimesRecord1.setRouteOptionBooking(portSlot, originalPortTimesRecord.getRouteOptionBooking(portSlot));
				portTimesRecord1.setSlotMaxAvailablePanamaIdleHours(portSlot, originalPortTimesRecord.getSlotMaxAdditionaPanamaIdleHours(portSlot));
				portTimesRecord1.setSlotAdditionalPanamaIdleHours(portSlot, originalPortTimesRecord.getSlotAdditionaPanamaIdleHours(portSlot));

				// Rolling variable so we get the last one used
				lastSlotInFirstRecord = portSlot;
			}
			portTimesRecord1.setReturnSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime() + splitPoint);

			if (lastSlotInFirstRecord != null) {
				// Rest values to zero as they become part of the charter length
				for (final var type : ExplicitIdleTime.values()) {
					portTimesRecord1.setSlotExtraIdleTime(lastSlotInFirstRecord, type, 0);
				}
			}

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

			// Validation
			assert validatePortTimesRecord1(originalPortTimesRecord, portTimesRecord1, currentPlan);
		}

		// New voyage plan representing the charter length (here the zero length event
		// plus idle time to roll in during export).
		{
			final PortTimesRecord portTimesRecord2 = new PortTimesRecord();

			portTimesRecord2.setSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime() + splitPoint);
			portTimesRecord2.setSlotDuration(charterLengthPortSlot, 0);

			if (lastSlotInFirstRecord != null) {
				// Rest values to zero as they become part of the charter length
				for (final var type : ExplicitIdleTime.values()) {
					portTimesRecord2.setSlotExtraIdleTime(charterLengthPortSlot, type, originalPortTimesRecord.getSlotExtraIdleTime(lastSlotInFirstRecord, type));
				}
				assert portTimesRecord2.getSlotExtraIdleTime(charterLengthPortSlot, ExplicitIdleTime.MARKET_BUFFER) == 0;
			}

			final IPortSlot returnSlot = originalPortTimesRecord.getReturnSlot();
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

		}

		assert validatePlans(vessel, originalPlan, charterPlans, charterLengthHeelInM3);
		return charterPlans;

	}

	private boolean validatePortTimesRecord1(final IPortTimesRecord originalPortTimesRecord, final IPortTimesRecord portTimesRecord1, final VoyagePlan currentPlan) {

		// Part 1 - validate the times all add up correctly
		int time = originalPortTimesRecord.getFirstSlotTime();
		for (int i = 0; i < portTimesRecord1.getSlots().size(); i++) {
			final IPortSlot slot = portTimesRecord1.getSlots().get(i);
			assert time == portTimesRecord1.getSlotTime(slot);
			time += portTimesRecord1.getSlotDuration(slot);
			final VoyageDetails details = (VoyageDetails) currentPlan.getSequence()[1 + 2 * i];
			final int availTime = details.getOptions().getAvailableTime();
			final int componentSum = details.getTravelTime() + details.getIdleTime() + details.getOptions().getExtraIdleTime(ExplicitIdleTime.PURGE);
			assert availTime == componentSum;
			time += availTime;
		}
		final IPortSlot returnSlot = portTimesRecord1.getReturnSlot();
		assert returnSlot != null;
		assert time == portTimesRecord1.getSlotTime(returnSlot);

		return true;
	}

	private boolean validatePlans(final IVessel vessel, final VoyagePlan originalPlan, final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans, final long charterLengthHeelInM3) {


		long originalLNGUse = 0;
		final long[] bunkers = new long[baseFuelProvider.getNumberOfBaseFuels()];
		for (final var o : originalPlan.getSequence()) {
			if (o instanceof final VoyageDetails voyageDetails) {
				for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
					originalLNGUse += voyageDetails.getFuelConsumption(fk);
				}
				for (final var fk : vessel.getAllFuelKeys()) {
					bunkers[fk.getBaseFuel().getIndex()] += voyageDetails.getFuelConsumption(fk);
				}

			}
			if (o instanceof final PortDetails voyageDetails) {
				for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
					originalLNGUse += voyageDetails.getFuelConsumption(fk);
				}
				for (final var fk : vessel.getAllFuelKeys()) {
					bunkers[fk.getBaseFuel().getIndex()] += voyageDetails.getFuelConsumption(fk);
				}

			}
		}

		long newLNGUse = 0;
		final long[] newbunkers = new long[baseFuelProvider.getNumberOfBaseFuels()];
		for (final var p : charterPlans) {
			final VoyagePlan vp = p.getFirst();
			for (final var o : vp.getSequence()) {
				if (o instanceof final VoyageDetails voyageDetails) {
					for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
						newLNGUse += voyageDetails.getFuelConsumption(fk);
					}
					for (final var fk : vessel.getAllFuelKeys()) {
						newbunkers[fk.getBaseFuel().getIndex()] += voyageDetails.getFuelConsumption(fk);
					}
				}
				if (o instanceof final PortDetails voyageDetails) {
					for (final FuelKey fk : LNGFuelKeys.LNG_In_m3) {
						newLNGUse += voyageDetails.getFuelConsumption(fk);
					}
					for (final var fk : vessel.getAllFuelKeys()) {
						newbunkers[fk.getBaseFuel().getIndex()] += voyageDetails.getFuelConsumption(fk);
					}
				}
			}
		}

		for (int i = 0; i < bunkers.length; ++i) {
			assert bunkers[i] == newbunkers[i];

		}

		assert originalLNGUse == newLNGUse;

		assert charterPlans.get(0).getFirst().getStartingHeelInM3() == originalPlan.getStartingHeelInM3();
		assert charterPlans.get(0).getFirst().getRemainingHeelInM3() == charterLengthHeelInM3;

		assert charterPlans.get(1).getFirst().getStartingHeelInM3() == charterLengthHeelInM3;
		assert charterPlans.get(1).getFirst().getRemainingHeelInM3() == originalPlan.getRemainingHeelInM3();

		return true;
	}
}