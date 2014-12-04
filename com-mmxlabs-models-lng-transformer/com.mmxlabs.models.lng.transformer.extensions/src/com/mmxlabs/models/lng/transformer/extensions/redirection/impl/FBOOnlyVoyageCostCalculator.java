/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class FBOOnlyVoyageCostCalculator extends AbstractVoyageCostCalculator {

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Override
	public VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int notionalSpeed, final int cargoCVValue,
			@NonNull final String route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPrice) {

		final VoyagePlan notionalPlan = new VoyagePlan();
		notionalPlan.setCharterInRatePerDay(vesselCharterInRatePerDay);

		final Integer distance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (distance == null || distance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		// Generate a notional voyage plan
		final int travelTime = Calculator.getTimeFromSpeedDistance(notionalSpeed, distance);

		// Determine notional port visit times.
		final int notionalReturnTime = dischargeTime + dischargeDuration + travelTime;

		final LoadSlot notionalLoadSlot = new LoadSlot();
		notionalLoadSlot.setPort(loadPort);
		notionalLoadSlot.setTimeWindow(new TimeWindow(loadTime, loadTime));
		notionalLoadSlot.setCargoCVValue(cargoCVValue);
		notionalLoadSlot.setCooldownForbidden(true);
		notionalLoadSlot.setMaxLoadVolume(vessel.getCargoCapacity());
		notionalLoadSlot.setMinLoadVolume(vessel.getCargoCapacity());

		final DischargeSlot notionalDischargeSlot = new DischargeSlot();
		notionalDischargeSlot.setPort(dischargePort);
		notionalDischargeSlot.setTimeWindow(new TimeWindow(dischargeTime, dischargeTime));
		notionalDischargeSlot.setDischargePriceCalculator(salesPrice);

		final PortSlot notionalReturnSlot = new EndPortSlot("notional-end", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), true, vessel.getVesselClass().getSafetyHeel());

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
		portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
		portTimesRecord.setReturnSlotTime(notionalReturnSlot, notionalReturnTime);

		portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
		portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);

		// Calculate new voyage requirements
		{
			final VoyageDetails ladenDetails = calculateVoyageDetails(VesselState.Laden, vessel, route, distance, dischargeTime - loadDuration - loadTime, notionalLoadSlot, notionalDischargeSlot,
					cargoCVValue);

			final VoyageDetails ballastDetails = calculateVoyageDetails(VesselState.Ballast, vessel, route, distance, notionalReturnTime - dischargeDuration - dischargeTime, notionalDischargeSlot,
					notionalReturnSlot, cargoCVValue);

			final PortDetails loadDetails = new PortDetails();
			loadDetails.setOptions(new PortOptions());
			loadDetails.getOptions().setPortSlot(notionalLoadSlot);
			loadDetails.getOptions().setVisitDuration(loadDuration);

			final PortDetails dischargeDetails = new PortDetails();
			dischargeDetails.setOptions(new PortOptions());
			dischargeDetails.getOptions().setPortSlot(notionalDischargeSlot);
			dischargeDetails.getOptions().setVisitDuration(dischargeDuration);

			final PortDetails returnDetails = new PortDetails();
			returnDetails.setOptions(new PortOptions());
			returnDetails.getOptions().setPortSlot(notionalReturnSlot);
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
			final int dischargeDuration, final int returnTime, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final String route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPrice) {

		final VoyagePlan notionalPlan = new VoyagePlan();
		notionalPlan.setCharterInRatePerDay(vesselCharterInRatePerDay);

		final Integer distance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (distance == null || distance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		final LoadSlot notionalLoadSlot = new LoadSlot();
		notionalLoadSlot.setPort(loadPort);
		notionalLoadSlot.setTimeWindow(new TimeWindow(loadTime, loadTime));
		notionalLoadSlot.setCargoCVValue(cargoCVValue);
		notionalLoadSlot.setCooldownForbidden(true);
		notionalLoadSlot.setMaxLoadVolume(vessel.getCargoCapacity());
		notionalLoadSlot.setMinLoadVolume(vessel.getCargoCapacity());

		final DischargeSlot notionalDischargeSlot = new DischargeSlot();
		notionalDischargeSlot.setPort(dischargePort);
		notionalDischargeSlot.setTimeWindow(new TimeWindow(dischargeTime, dischargeTime));
		notionalDischargeSlot.setDischargePriceCalculator(salesPrice);

		final PortSlot notionalReturnSlot = new EndPortSlot("notional-end", loadPort, new TimeWindow(returnTime, returnTime), true, vessel.getVesselClass().getSafetyHeel());

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
		portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
		portTimesRecord.setReturnSlotTime(notionalReturnSlot, returnTime);

		portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
		portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);

		// Calculate new voyage requirements
		{
			final VoyageDetails ladenDetails = calculateVoyageDetails(VesselState.Laden, vessel, route, distance, dischargeTime - loadDuration - loadTime, notionalLoadSlot, notionalDischargeSlot,
					cargoCVValue);

			final VoyageDetails ballastDetails = calculateVoyageDetails(VesselState.Ballast, vessel, route, distance, returnTime - dischargeDuration - dischargeTime, notionalDischargeSlot,
					notionalReturnSlot, cargoCVValue);

			final PortDetails loadDetails = new PortDetails();
			loadDetails.setOptions(new PortOptions());
			loadDetails.getOptions().setPortSlot(notionalLoadSlot);
			loadDetails.getOptions().setVisitDuration(loadDuration);

			final PortDetails dischargeDetails = new PortDetails();
			dischargeDetails.setOptions(new PortOptions());
			dischargeDetails.getOptions().setPortSlot(notionalDischargeSlot);
			dischargeDetails.getOptions().setVisitDuration(dischargeDuration);

			final PortDetails returnDetails = new PortDetails();
			returnDetails.setOptions(new PortOptions());
			returnDetails.getOptions().setPortSlot(notionalReturnSlot);
			returnDetails.getOptions().setVisitDuration(0);

			final IDetailsSequenceElement[] sequence = new IDetailsSequenceElement[] { loadDetails, ladenDetails, dischargeDetails, ballastDetails, returnDetails };
			notionalPlan.setSequence(sequence);
			voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, startHeelInM3, baseFuelPricePerMT, portTimesRecord, sequence);

			return notionalPlan;
		}

	}
}
