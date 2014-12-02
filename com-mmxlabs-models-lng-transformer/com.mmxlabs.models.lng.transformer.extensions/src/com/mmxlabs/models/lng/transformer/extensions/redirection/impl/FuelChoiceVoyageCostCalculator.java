/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mmxlabs.models.lng.transformer.extensions.redirection.IVoyageCostCalculator;
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
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.LinkedFBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A {@link IVoyageCostCalculator} allowing control over fuel choices. This implementation links the FBO choice between laden and ballast legs (assumes NBO all through the ballast). The fuel choice be
 * be set by the user by calling {@link #setFuelChoice(FuelChoice)} - and indeed must be called prior to calling
 * {@link #calculateShippingCosts(IPort, IPort, int, int, int, int, int, IVessel, long, int, String, int, int)}. If {@link FuelChoice#Optimal} is used, a {@link IVoyagePlanOptimiser} is used to pick
 * the optimal choice and this can be returned via {@link #getFuelChoice()}.
 * 
 * Note: This class is *NOT* thread safe
 */
public class FuelChoiceVoyageCostCalculator extends AbstractVoyageCostCalculator {

	public enum FuelChoice {
		FBO, Base, Optimal
	}

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private Provider<VoyagePlanOptimiser> vpoProvider;

	private FuelChoice fuelChoice = null;

	@Override
	public @Nullable
	VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue,
			@NonNull final String route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator) {

		final Integer ladenDistance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (ladenDistance == null || ladenDistance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		final Integer ballastDistance = distanceProvider.get(route).get(dischargePort, loadPort);
		if (ballastDistance == null || ballastDistance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		// Generate a notional voyage plan
		final int ballastTravelTime = Calculator.getTimeFromSpeedDistance(notionalBallastSpeed, ballastDistance);

		// Determine notional port visit times.
		final int notionalReturnTime = dischargeTime + dischargeDuration + ballastTravelTime;

		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDuration, dischargeTime, dischargeDuration, notionalReturnTime, vessel, vesselCharterInRatePerDay, startHeelInM3,
				cargoCVValue, route, baseFuelPricePerMT, salesPriceCalculator);
	}

	@Override
	public @Nullable
	VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final String route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator) {

		// MUST CALL setFuelChoice!
		assert fuelChoice != null;
		// When assert is not enabled, default to FuelChoice#Optimal
		if (fuelChoice == null) {
			fuelChoice = FuelChoice.Optimal;
		}

		final Integer ladenDistance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (ladenDistance == null || ladenDistance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		final Integer ballastDistance = distanceProvider.get(route).get(dischargePort, loadPort);
		if (ballastDistance == null || ballastDistance.intValue() == Integer.MAX_VALUE) {
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
		notionalDischargeSlot.setDischargePriceCalculator(salesPriceCalculator);

		final PortSlot notionalReturnSlot = new EndPortSlot("notional-return", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), true, vessel.getVesselClass().getSafetyHeel());

		final VoyagePlanOptimiser vpo = vpoProvider.get();

		// Calculate new voyage requirements
		{
			final VoyageOptions ladenOptions = createVoyageOptions(VesselState.Laden, vessel, route, ladenDistance, dischargeTime - loadDuration - loadTime, notionalLoadSlot, notionalDischargeSlot);
			final VoyageOptions ballastOptions = createVoyageOptions(VesselState.Ballast, vessel, route, ballastDistance, notionalReturnTime - dischargeDuration - dischargeTime,
					notionalDischargeSlot, notionalReturnSlot);

			switch (fuelChoice) {
			case Base:
				ladenOptions.setUseFBOForSupplement(false);
				ballastOptions.setUseFBOForSupplement(false);
				break;
			case FBO:
				ladenOptions.setUseFBOForSupplement(true);
				ballastOptions.setUseFBOForSupplement(true);
				break;
			case Optimal:
				vpo.addChoice(new LinkedFBOVoyagePlanChoice(ladenOptions, ballastOptions));
				break;
			default:
				break;
			}

			final PortOptions loadOptions = new PortOptions();
			loadOptions.setPortSlot(notionalLoadSlot);
			loadOptions.setVisitDuration(loadDuration);
			loadOptions.setVessel(vessel);

			final PortOptions dischargeOptions = new PortOptions();
			dischargeOptions.setPortSlot(notionalDischargeSlot);
			dischargeOptions.setVisitDuration(dischargeDuration);
			dischargeOptions.setVessel(vessel);

			final PortOptions returnOptions = new PortOptions();
			returnOptions.setPortSlot(notionalReturnSlot);
			returnOptions.setVisitDuration(0);
			returnOptions.setVessel(vessel);

			final List<IOptionsSequenceElement> basicSequence = Lists.<IOptionsSequenceElement> newArrayList(loadOptions, ladenOptions, dischargeOptions, ballastOptions, returnOptions);

			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
			portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
			portTimesRecord.setReturnSlotTime(notionalReturnSlot, notionalReturnTime);

			portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
			portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);

			vpo.setBasicSequence(basicSequence);
			vpo.setPortTimesRecord(portTimesRecord);
			vpo.setVessel(vessel, null, baseFuelPricePerMT);
			vpo.setVesselCharterInRatePerDay(vesselCharterInRatePerDay);
			vpo.setStartHeel(startHeelInM3);

			final VoyagePlan notionalPlan = vpo.optimise();

			// Set to null to trigger assert at start of method if API is not being correctly used.
			fuelChoice = null;

			return notionalPlan;
		}
	}

	public FuelChoice getFuelChoice() {
		return fuelChoice;
	}

	public void setFuelChoice(final FuelChoice fuelChoice) {
		this.fuelChoice = fuelChoice;
	}
}
