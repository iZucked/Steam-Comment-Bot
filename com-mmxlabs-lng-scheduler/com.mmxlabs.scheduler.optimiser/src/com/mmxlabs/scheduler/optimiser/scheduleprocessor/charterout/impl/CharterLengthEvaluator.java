/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
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
public class CharterLengthEvaluator implements IGeneratedCharterLengthEvaluator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	// See default binding in LNGEvaluationModule
	@Inject
	@Named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)
	private int minIdleTimeInHours;

	@Override
	public List<Pair<VoyagePlan, IPortTimesRecord>> processSchedule(final int vesselStartTime, final long[] startHeelVolumeRangeInM3, final IVesselAvailability vesselAvailability, final VoyagePlan vp,
			final IPortTimesRecord portTimesRecord) {
		if (!(vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER)) {
			return null;
		}

		// First step, find a ballast leg which is long enough to split out.
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

		// Check idle length
		// TODO: May based on notional speed.
		if (ballastDetails.getIdleTime() < minIdleTimeInHours) {
			return null;
		}

		return generateNewVoyagePlans(vesselAvailability, vp, portTimesRecord, vesselStartTime, currentSequence, ballastIdx, ballastStartTime);
	}

	/**
	 * Produces a new large sequence including a generated charter out and a ballast to and from the charter out port
	 * 
	 * @param currentSequence
	 * @param charterOutOption
	 * @param portTimesRecord
	 * @param ballastIdx
	 * @param ballastStartTime
	 * @return
	 */
	private List<Pair<VoyagePlan, IPortTimesRecord>> generateNewVoyagePlans(final @NonNull IVesselAvailability vesselAvailability, final VoyagePlan originalPlan,
			final IPortTimesRecord originalPortTimesRecord, final int vesselStartTime,

			final Object[] currentSequence, final int ballastIdx, final int ballastStartTime) {

		// Now insert elements from the charter out option
		final VoyageDetails originalBallast = (VoyageDetails) currentSequence[ballastIdx];

		// These will be updated later on
		final HeelOptionConsumer heelOptionConsumer = new HeelOptionConsumer(0, Long.MAX_VALUE, VesselTankState.MUST_BE_COLD, new ConstantHeelPriceCalculator(0), false);
		final HeelOptionSupplier heelOptionSupplier = new HeelOptionSupplier(0, 0, originalBallast.getOptions().getCargoCVValue(), new ConstantHeelPriceCalculator(0));

		// now update port slot
		final GeneratedCharterLengthPortSlot charterLengthPortSlot = new GeneratedCharterLengthPortSlot(
				/* ID */ String.format("charterlength-%s-%s", originalBallast.getOptions().getFromPortSlot().getId(), originalBallast.getOptions().getToPortSlot().getId()), //
				/* Time window - set later */ null, /* Start / End Port */ originalBallast.getOptions().getToPortSlot().getPort(), //
				heelOptionConsumer, heelOptionSupplier //
		);

		// (1) ballast to charter out

		final VoyageOptions dischargeToCharterPortVoyageOptions = originalBallast.getOptions().clone();
		dischargeToCharterPortVoyageOptions.setAvailableTime(originalBallast.getTravelTime());
		dischargeToCharterPortVoyageOptions.setToPortSlot(charterLengthPortSlot);

		if (originalBallast.isCooldownPerformed()) {
			// May need to tweak these
			dischargeToCharterPortVoyageOptions.setShouldBeCold(VesselTankState.EITHER);
			// dischargeToCharterPortVoyageOptions.setWarm(originalBallast.getOptions().isWarm());
			// dischargeToCharterPortVoyageOptions.setAllowCooldown(false);
		}
		// (2) charter out

		final PortOptions generatedCharterPortOptions = new PortOptions(charterLengthPortSlot);
		generatedCharterPortOptions.setVessel(originalBallast.getOptions().getVessel());
		generatedCharterPortOptions.setVisitDuration(0);
		generatedCharterPortOptions.setCargoCVValue(originalBallast.getOptions().getCargoCVValue());

		// (3) ballast to return port
		final VoyageOptions charterToReturnPortVoyageOptions = originalBallast.getOptions().clone();
		charterToReturnPortVoyageOptions.setAvailableTime(originalBallast.getIdleTime()+ originalBallast.getPurgeDuration());
		charterToReturnPortVoyageOptions.setFromPortSlot(charterLengthPortSlot);
		charterToReturnPortVoyageOptions.setRoute(originalBallast.getOptions().getRoute(), 0, 0);

		final IVessel vessel = vesselAvailability.getVessel();

		final VoyageDetails newTravel = originalBallast.clone();

		{
			newTravel.setOptions(dischargeToCharterPortVoyageOptions);
			newTravel.setCooldownPerformed(false);
			newTravel.setPurgePerformed(false);
			newTravel.setPurgeDuration(0);
			// Clear Idle values
			newTravel.setIdleTime(0);
			newTravel.setIdleNBOHours(0);

			newTravel.setFuelConsumption(vessel.getIdleBaseFuelInMT(), 0);
			newTravel.setFuelConsumption(vessel.getIdlePilotLightFuelInMT(), 0);
			newTravel.setFuelConsumption(LNGFuelKeys.Cooldown_In_m3, 0);
			for (final FuelKey fk : LNGFuelKeys.Idle_LNG) {
				newTravel.setFuelConsumption(fk, 0);
			}
		}

		final VoyageDetails newIdle = originalBallast.clone();
		{
			newIdle.setOptions(charterToReturnPortVoyageOptions);
			// Clear Travel values
			newIdle.setTravelTime(0);
			newIdle.setTravelNBOHours(0);
			newIdle.setSpeed(0);
			for (final FuelKey fk : vessel.getTravelFuelKeys()) {
				newIdle.setFuelConsumption(fk, 0);
				newIdle.setRouteAdditionalConsumption(fk, 0);
			}
			for (final FuelKey fk : LNGFuelKeys.Travel_LNG) {
				newIdle.setFuelConsumption(fk, 0);
				newIdle.setRouteAdditionalConsumption(fk, 0);
			}
		}

		final List<IDetailsSequenceElement> newRawSequence1 = new LinkedList<>();

		// build new sequence up to and not including last ballast leg
		for (int i = 0; i < ballastIdx; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				newRawSequence1.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				newRawSequence1.add(voyageDetails.clone());
			}
		}
		final PortDetails portDetails = new PortDetails(generatedCharterPortOptions);
		newRawSequence1.add(newTravel);
		newRawSequence1.add(portDetails);

		final List<IDetailsSequenceElement> newRawSequence2 = new LinkedList<>();
		newRawSequence2.add(portDetails);
		newRawSequence2.add(newIdle);
		for (int i = ballastIdx + 1; i < currentSequence.length; i++) {
			final Object o = currentSequence[i];
			if (o instanceof PortDetails) {
				newRawSequence2.add(((PortDetails) o).clone());
			} else if (o instanceof VoyageDetails) {
				final VoyageDetails voyageDetails = (VoyageDetails) o;
				newRawSequence2.add(voyageDetails.clone());
			}
		}

		final List<Pair<VoyagePlan, IPortTimesRecord>> charterPlans = new LinkedList<>();

		final int[] baseFuelPricesPerMT = vesselBaseFuelCalculator.getBaseFuelPrices(vessel, originalPortTimesRecord);

		// calculate pre-charter plan
		final long[] startHeelRangeInM3 = new long[2];
		startHeelRangeInM3[0] = originalPlan.getStartingHeelInM3();
		startHeelRangeInM3[1] = originalPlan.getStartingHeelInM3();

		{
			final PortTimesRecord portTimesRecord1 = new PortTimesRecord();

			// existing
			for (int i = 0; i < originalPortTimesRecord.getSlots().size(); i++) {
				portTimesRecord1.setSlotTime(originalPortTimesRecord.getSlots().get(i), originalPortTimesRecord.getSlotTime(originalPortTimesRecord.getSlots().get(i)));
				portTimesRecord1.setSlotDuration(originalPortTimesRecord.getSlots().get(i), originalPortTimesRecord.getSlotDuration(originalPortTimesRecord.getSlots().get(i)));
				portTimesRecord1.setSlotExtraIdleTime(originalPortTimesRecord.getSlots().get(i), originalPortTimesRecord.getSlotExtraIdleTime(originalPortTimesRecord.getSlots().get(i)));
			}
			portTimesRecord1.setReturnSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime());

			//
			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setCharterInRatePerDay(originalPlan.getCharterInRatePerDay());
			currentPlan.setIgnoreEnd(true);

			// Calculate voyage plan
			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, startHeelRangeInM3, baseFuelPricesPerMT, portTimesRecord1,
					newRawSequence1.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != Integer.MAX_VALUE;

			// Re-add the idle BOG
			final long idleBOGInM3 = originalBallast.getFuelConsumption(LNGFuelKeys.IdleNBO_In_m3);
			long remainingHeelInM3 = originalPlan.getRemainingHeelInM3();
			long remainingHeelInM32 = remainingHeelInM3 + idleBOGInM3;
			currentPlan.setRemainingHeelInM3(remainingHeelInM32);

			final IAllocationAnnotation allocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, currentPlan, portTimesRecord1);
			if (allocation == null) {
				charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(currentPlan, portTimesRecord1));
				startHeelRangeInM3[0] = remainingHeelInM3;
				startHeelRangeInM3[1] = remainingHeelInM3;
				currentPlan.setIgnoreEnd(true);

				setHeelOptions(charterLengthPortSlot.getVesselEvent(), portTimesRecord1, currentPlan.getSequence(), remainingHeelInM3);
			} else {
				charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(currentPlan, allocation));
				startHeelRangeInM3[0] = allocation.getRemainingHeelVolumeInM3();
				startHeelRangeInM3[1] = allocation.getRemainingHeelVolumeInM3();
				currentPlan.setIgnoreEnd(true);

				setHeelOptions(charterLengthPortSlot.getVesselEvent(), portTimesRecord1, currentPlan.getSequence(), allocation.getRemainingHeelVolumeInM3());
			}
		}
		{

			final PortTimesRecord portTimesRecord2 = new PortTimesRecord();

			portTimesRecord2.setSlotTime(charterLengthPortSlot, ballastStartTime + originalBallast.getTravelTime());
			portTimesRecord2.setSlotDuration(charterLengthPortSlot, 0);
			portTimesRecord2.setSlotExtraIdleTime(charterLengthPortSlot, 0);

			portTimesRecord2.setReturnSlotTime(originalPortTimesRecord.getReturnSlot(), originalPortTimesRecord.getSlotTime(originalPortTimesRecord.getReturnSlot()));

			final VoyagePlan currentPlan = new VoyagePlan();
			currentPlan.setCharterInRatePerDay(originalPlan.getCharterInRatePerDay());
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());

			// Calculate voyage plan
			final int violationCount = voyageCalculator.calculateVoyagePlan(currentPlan, vessel, startHeelRangeInM3, baseFuelPricesPerMT, portTimesRecord2,
					newRawSequence2.toArray(new IDetailsSequenceElement[0]));
			assert violationCount != Integer.MAX_VALUE;
			charterPlans.add(new Pair<VoyagePlan, IPortTimesRecord>(currentPlan, portTimesRecord2));
			currentPlan.setIgnoreEnd(originalPlan.isIgnoreEnd());
		}
		return charterPlans;

	}

	/**
	 * Loop through the first voyage plan (up to the GCO) and find an element to extract heel options information
	 * 
	 * @param generatedEvent
	 * @param sequence
	 * @param heelVolume
	 * @param portTimesRecord
	 * @return
	 */
	private void setHeelOptions(final IGeneratedCharterLengthEvent generatedEvent, final IPortTimesRecord portTimesRecord, final IDetailsSequenceElement[] sequence, final long heelVolume) {
		IHeelPriceCalculator heelPriceCalculator = null;
		int cv = 0;

		final IDetailsSequenceElement startOfSequence = sequence[0];
		if (startOfSequence instanceof PortDetails) {
			final IPortSlot portSlot = ((PortDetails) startOfSequence).getOptions().getPortSlot();
			if (portSlot instanceof IHeelOptionSupplierPortSlot) {
				heelPriceCalculator = ((IHeelOptionSupplierPortSlot) portSlot).getHeelOptionsSupplier().getHeelPriceCalculator();
				cv = ((IHeelOptionSupplierPortSlot) portSlot).getHeelOptionsSupplier().getHeelCVValue();
			} else if (portSlot instanceof ILoadSlot) {
				IDischargeSlot discharge = null;
				for (int i = sequence.length - 1; i >= 0; i--) {
					if (sequence[i] instanceof PortDetails) {
						if (((PortDetails) sequence[i]).getOptions().getPortSlot() instanceof IDischargeSlot) {
							discharge = (IDischargeSlot) ((PortDetails) sequence[i]).getOptions().getPortSlot();
							break;
						}
					}
				}
				if (discharge != null) {
					heelPriceCalculator = new ConstantHeelPriceCalculator(discharge.getDischargePriceCalculator().estimateSalesUnitPrice(discharge, portTimesRecord, null));
				}
				cv = ((ILoadSlot) portSlot).getCargoCVValue();
			} else {
				heelPriceCalculator = new ConstantHeelPriceCalculator(0);
			}
		}
		assert heelPriceCalculator != null;
		final HeelOptionConsumer heelConsumer = new HeelOptionConsumer(heelVolume, heelVolume, heelVolume > 0 ? VesselTankState.MUST_BE_COLD : VesselTankState.MUST_BE_WARM, heelPriceCalculator,
				false);
		final HeelOptionSupplier heelSupplier = new HeelOptionSupplier(heelVolume, heelVolume, cv, heelPriceCalculator);
		generatedEvent.setHeelConsumer(heelConsumer);
		generatedEvent.setHeelSupplier(heelSupplier);
	}
}