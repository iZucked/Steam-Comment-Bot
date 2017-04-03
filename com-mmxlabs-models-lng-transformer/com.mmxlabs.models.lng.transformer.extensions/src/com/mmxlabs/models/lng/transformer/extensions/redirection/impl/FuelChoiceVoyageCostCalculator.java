/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.extensions.redirection.IVoyageCostCalculator;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.NotionalEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.FBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IdleNBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.LinkedFBOVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.NBOTravelVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.ReliqVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider.CostType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private Provider<VoyagePlanOptimiser> vpoProvider;

	private FuelChoice fuelChoice = null;

	@Override
	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue,
			@NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator) {

		final int ladenDistance = distanceProvider.getDistance(route, loadPort, dischargePort, loadTime + loadDuration, vessel);
		if (ladenDistance == Integer.MAX_VALUE) {
			return null;
		}

		final int ballastDistance = distanceProvider.getDistance(route, dischargePort, loadPort, dischargeTime + dischargeDuration, vessel);
		if (ballastDistance == Integer.MAX_VALUE) {
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
	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator) {
		final int ladenDistance = distanceProvider.getDistance(route, loadPort, dischargePort, loadTime + loadDuration, vessel);
		if (ladenDistance == Integer.MAX_VALUE) {
			return null;
		}

		final int ballastDistance = distanceProvider.getDistance(route, dischargePort, loadPort, dischargeTime + dischargeDuration, vessel);
		if (ballastDistance == Integer.MAX_VALUE) {
			return null;
		}

		return calculateShippingCosts(loadPort, dischargePort, loadTime, ladenDistance, loadDuration, dischargeTime, ballastDistance, dischargeDuration, notionalReturnTime, vessel,
				vesselCharterInRatePerDay, startHeelInM3, cargoCVValue, route, baseFuelPricePerMT, salesPriceCalculator);

	}

	@Override
	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDistance, final int loadDuration,
			final int dischargeTime, final int dischargeDistance, final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay,
			final long startHeelInM3, final int cargoCVValue, @NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator) {

		// MUST CALL setFuelChoice!
		assert fuelChoice != null;
		// When assert is not enabled, default to FuelChoice#Optimal
		if (fuelChoice == null) {
			fuelChoice = FuelChoice.Optimal;
		}

		final LoadSlot notionalLoadSlot = makeNotionalLoad(loadPort, loadTime, vessel, cargoCVValue);
		// notionalLoadSlot.setPort(loadPort);
		// notionalLoadSlot.setTimeWindow(new TimeWindow(loadTime, loadTime));
		// notionalLoadSlot.setCargoCVValue(cargoCVValue);
		// notionalLoadSlot.setCooldownForbidden(true);
		// notionalLoadSlot.setMaxLoadVolume(vessel.getCargoCapacity());
		// notionalLoadSlot.setMinLoadVolume(vessel.getCargoCapacity());

		final DischargeSlot notionalDischargeSlot = makeNotionalDischarge(dischargePort, dischargeTime, salesPriceCalculator);

		final HeelOptionConsumer heelOptions = new HeelOptionConsumer(vessel.getVesselClass().getSafetyHeel(), vessel.getVesselClass().getSafetyHeel(), VesselTankState.MUST_BE_COLD,
				ConstantHeelPriceCalculator.ZERO);

		final PortSlot notionalReturnSlot = new NotionalEndPortSlot("notional-return", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), heelOptions);

		final PortTimesRecord portTimesRecord = getPortTimesRecord(loadTime, loadDuration, dischargeTime, dischargeDuration, notionalReturnTime, notionalLoadSlot, notionalDischargeSlot,
				notionalReturnSlot);

		final VoyagePlanOptimiser vpo = vpoProvider.get();

		return applyVPOAndGetVP(loadTime, loadDistance, loadDuration, dischargeTime, dischargeDistance, dischargeDuration, notionalReturnTime, vessel, vesselCharterInRatePerDay, startHeelInM3,
				cargoCVValue, route, baseFuelPricePerMT, notionalLoadSlot, notionalDischargeSlot, notionalReturnSlot, portTimesRecord, vpo);
	}

	public @Nullable Pair<VoyagePlan, IPortTimesRecord> calculateShippingCostsAndGetPTR(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDistance,
			final int loadDuration, final int dischargeTime, final int dischargeDistance, final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel,
			final int vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue, @NonNull final ERouteOption route, final int baseFuelPricePerMT, final int salesPricePerMMBTu) {

		// MUST CALL setFuelChoice!
		assert fuelChoice != null;
		// When assert is not enabled, default to FuelChoice#Optimal
		if (fuelChoice == null) {
			fuelChoice = FuelChoice.Optimal;
		}

		final LoadSlot notionalLoadSlot = makeNotionalLoad(loadPort, loadTime, vessel, cargoCVValue);

		final DischargeSlot notionalDischargeSlot = makeNotionalDischarge(dischargePort, dischargeTime, salesPricePerMMBTu);

		final HeelOptionConsumer heelOptions = new HeelOptionConsumer(vessel.getVesselClass().getSafetyHeel(), vessel.getVesselClass().getSafetyHeel(), VesselTankState.MUST_BE_COLD,
				new ConstantHeelPriceCalculator(0));

		final PortSlot notionalReturnSlot = new NotionalEndPortSlot("notional-return", loadPort, new TimeWindow(notionalReturnTime, notionalReturnTime), heelOptions);

		final PortTimesRecord portTimesRecord = getPortTimesRecord(loadTime, loadDuration, dischargeTime, dischargeDuration, notionalReturnTime, notionalLoadSlot, notionalDischargeSlot,
				notionalReturnSlot);

		final VoyagePlanOptimiser vpo = vpoProvider.get();

		return new Pair<>(applyVPOAndGetVP(loadTime, loadDistance, loadDuration, dischargeTime, dischargeDistance, dischargeDuration, notionalReturnTime, vessel, vesselCharterInRatePerDay,
				startHeelInM3, cargoCVValue, route, baseFuelPricePerMT, notionalLoadSlot, notionalDischargeSlot, notionalReturnSlot, portTimesRecord, vpo), portTimesRecord);
	}

	private PortTimesRecord getPortTimesRecord(final int loadTime, final int loadDuration, final int dischargeTime, final int dischargeDuration, final int notionalReturnTime,
			final LoadSlot notionalLoadSlot, final DischargeSlot notionalDischargeSlot, final PortSlot notionalReturnSlot) {
		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(notionalLoadSlot, loadTime);
		portTimesRecord.setSlotTime(notionalDischargeSlot, dischargeTime);
		portTimesRecord.setReturnSlotTime(notionalReturnSlot, notionalReturnTime);

		portTimesRecord.setSlotDuration(notionalLoadSlot, loadDuration);
		portTimesRecord.setSlotDuration(notionalDischargeSlot, dischargeDuration);
		return portTimesRecord;
	}

	private VoyagePlan applyVPOAndGetVP(final int loadTime, final int loadDistance, final int loadDuration, final int dischargeTime, final int dischargeDistance, final int dischargeDuration,
			final int notionalReturnTime, final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue, final ERouteOption route,
			final int baseFuelPricePerMT, final LoadSlot notionalLoadSlot, final DischargeSlot notionalDischargeSlot, final PortSlot notionalReturnSlot, final PortTimesRecord portTimesRecord,
			final VoyagePlanOptimiser vpo) {
		@NonNull
		final List<@NonNull IVoyagePlanChoice> vpoChoices = new LinkedList<>();

		// Calculate new voyage requirements
		{
			final long ladenRouteCosts = routeCostProvider.getRouteCost(route, vessel, loadTime + loadDuration, CostType.Laden);
			final long ballastRouteCosts = routeCostProvider.getRouteCost(route, vessel, dischargeTime + dischargeDuration, CostType.RoundTripBallast);

			final VoyageOptions ladenOptions = createVoyageOptions(VesselState.Laden, vessel, route, loadDistance, ladenRouteCosts, dischargeTime - loadDuration - loadTime, notionalLoadSlot,
					notionalDischargeSlot, cargoCVValue);

			final VoyageOptions ballastOptions = createVoyageOptions(VesselState.Ballast, vessel, route, dischargeDistance, ballastRouteCosts, notionalReturnTime - dischargeDuration - dischargeTime,
					notionalDischargeSlot, notionalReturnSlot, cargoCVValue);

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
				vpoChoices.add(new LinkedFBOVoyagePlanChoice(ladenOptions, ballastOptions));
				// vpo.addChoice(new NBOTravelVoyagePlanChoice(ladenOptions, ballastOptions));
				// vpo.addChoice(new FBOVoyagePlanChoice(ballastOptions));
				// vpo.addChoice(new FBOVoyagePlanChoice(ladenOptions));
				// vpo.addChoice(new IdleNBOVoyagePlanChoice(ballastOptions));
				break;
			case LadenBaseBallastOptimal:
				ladenOptions.setUseFBOForSupplement(false);
				if (!vessel.getVesselClass().hasReliqCapability()) {
					vpoChoices.add(new NBOTravelVoyagePlanChoice(ladenOptions, ballastOptions));
					vpoChoices.add(new FBOVoyagePlanChoice(ballastOptions));
					vpoChoices.add(new IdleNBOVoyagePlanChoice(ballastOptions));
				} else {
					vpoChoices.add(new ReliqVoyagePlanChoice(ladenOptions, ballastOptions));
				}
				break;
			case LadenFBOBallastOptimal:
				ladenOptions.setUseFBOForSupplement(true);
				if (!vessel.getVesselClass().hasReliqCapability()) {
					vpoChoices.add(new NBOTravelVoyagePlanChoice(ladenOptions, ballastOptions));
					vpoChoices.add(new FBOVoyagePlanChoice(ballastOptions));
					vpoChoices.add(new IdleNBOVoyagePlanChoice(ballastOptions));
				} else {
					vpoChoices.add(new ReliqVoyagePlanChoice(ladenOptions, ballastOptions));
				}
				break;
			default:
				break;
			}

			final PortOptions loadOptions = new PortOptions(notionalLoadSlot);
			loadOptions.setVisitDuration(loadDuration);
			loadOptions.setVessel(vessel);
			loadOptions.setCargoCVValue(cargoCVValue);
			
			final PortOptions dischargeOptions = new PortOptions(notionalDischargeSlot);
			dischargeOptions.setVisitDuration(dischargeDuration);
			dischargeOptions.setVessel(vessel);
			dischargeOptions.setCargoCVValue(cargoCVValue);
			
			final PortOptions returnOptions = new PortOptions(notionalReturnSlot);
			returnOptions.setVisitDuration(0);
			returnOptions.setVessel(vessel);
			returnOptions.setCargoCVValue(cargoCVValue);

			final List<@NonNull IOptionsSequenceElement> basicSequence = Lists.<@NonNull IOptionsSequenceElement> newArrayList(loadOptions, ladenOptions, dischargeOptions, ballastOptions,
					returnOptions);

			final VoyagePlan notionalPlan = vpo.optimise(null, vessel, new long[] { startHeelInM3, startHeelInM3 }, baseFuelPricePerMT, vesselCharterInRatePerDay, portTimesRecord, basicSequence,
					vpoChoices);

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
