/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.extensions.redirection.IVoyageCostCalculator;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public abstract class AbstractVoyageCostCalculator implements IVoyageCostCalculator {

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private Injector injector;

	@Override
	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue,
			@NonNull final ERouteOption route, final int basePricePerMT, final int salesPricePerMMBTu) {
		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDuration, dischargeTime, dischargeDuration, vessel, vesselCharterInRatePerDay, startHeelInM3, notionalBallastSpeed,
				cargoCVValue, route, basePricePerMT, createSalesPriceCalculator(salesPricePerMMBTu));
	}

	@Override
	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, final int returnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final ERouteOption route, final int basePricePerMT, final int salesPricePerMMBTu) {
		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDuration, dischargeTime, dischargeDuration, returnTime, vessel, vesselCharterInRatePerDay, startHeelInM3, cargoCVValue,
				route, basePricePerMT, createSalesPriceCalculator(salesPricePerMMBTu));
	}

	public @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDistance, final int loadDuration,
			final int dischargeTime, final int dischargeDistance, final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay,
			final long startHeelInM3, final int cargoCVValue, @NonNull final ERouteOption route, final int baseFuelPricePerMT, final int dischargePriceInMMBTU) {
		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDistance, loadDuration, dischargeTime, dischargeDistance, dischargeDuration, notionalReturnTime, vessel,
				vesselCharterInRatePerDay, startHeelInM3, cargoCVValue, route, baseFuelPricePerMT, createSalesPriceCalculator(dischargePriceInMMBTU));
	}

	@Nullable
	protected abstract VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration,
			int returnTime, @NonNull IVessel vessel, final long vesselCharterInRatePerDay, long startHeelInM3, int cargoCVValue, @NonNull ERouteOption route, int baseFuelPricePerMT,
			@NonNull ISalesPriceCalculator salesPrice);

	public abstract @Nullable VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay, long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue,
			@NonNull final ERouteOption route, final int basePricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator);

	public @Nullable abstract VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDistance, final int loadDuration,
			final int dischargeTime, final int dischargeDistance, final int dischargeDuration, final int notionalReturnTime, @NonNull final IVessel vessel, final long vesselCharterInRatePerDay,
			final long startHeelInM3, final int cargoCVValue, @NonNull final ERouteOption route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator);

	protected @NonNull VoyageDetails calculateVoyageDetails(@NonNull final VesselState vesselState, @NonNull final IVessel vessel, @NonNull final ERouteOption route, final int distance,
			final long routeCost, final int availableTime, @NonNull final PortSlot from, @NonNull final PortSlot to, final int cargoCV) {

		final VoyageOptions voyageOptions = createVoyageOptions(vesselState, vessel, route, distance, routeCost, availableTime, from, to, cargoCV);
		final VoyageDetails voyageDetails = new VoyageDetails(voyageOptions);

		voyageCalculator.calculateVoyageFuelRequirements(voyageOptions, voyageDetails);

		return voyageDetails;
	}

	protected @NonNull VoyageOptions createVoyageOptions(final VesselState vesselState, final IVessel vessel, final @NonNull ERouteOption route, final int distance, final long routeCost,
			final int availableTime, final @NonNull PortSlot from, final @NonNull PortSlot to, final int cargoCV) {
		final VoyageOptions voyageOptions = new VoyageOptions(from, to);
		voyageOptions.setAvailableTime(availableTime);
		voyageOptions.setAllowCooldown(false);
		voyageOptions.setRoute(route, distance, routeCost);
		voyageOptions.setShouldBeCold(VesselTankState.MUST_BE_COLD);
		voyageOptions.setUseFBOForSupplement(true);
		voyageOptions.setUseNBOForIdle(true);
		voyageOptions.setUseNBOForTravel(true);
		voyageOptions.setVessel(vessel);
		voyageOptions.setVesselState(vesselState);
		voyageOptions.setWarm(false);
		voyageOptions.setCargoCVValue(cargoCV);

		final IVesselClass vesselClass = vessel.getVesselClass();
		// Convert rate to MT equivalent per day
		final int nboRateInMTPerDay = (int) Calculator.convertM3ToMT(vesselClass.getNBORate(vesselState), cargoCV, vesselClass.getBaseFuel().getEquivalenceFactor());
		final int nboSpeed = vesselClass.getConsumptionRate(vesselState).getSpeed(nboRateInMTPerDay);
		voyageOptions.setNBOSpeed(nboSpeed);
		return voyageOptions;
	}

	protected @NonNull ISalesPriceCalculator createSalesPriceCalculator(final int salesPricePerMMBTu) {
		final SimpleContract contract = new SimpleContract() {

			@Override
			protected int calculateSimpleUnitPrice(final int loadTime, final IPort port) {
				return salesPricePerMMBTu;
			}
		};

		injector.injectMembers(contract);
		return contract;
	}

	protected @NonNull DischargeSlot makeNotionalDischarge(@NonNull IPort dischargePort, int dischargeTime, @NonNull ISalesPriceCalculator salesPriceCalculator) {
		DischargeSlot dischargeSlot = new DischargeSlot("notional-discharge", dischargePort, new TimeWindow(dischargeTime, dischargeTime), true, 0L, Long.MAX_VALUE, salesPriceCalculator, 0, 0);

		return dischargeSlot;
	}

	protected @NonNull DischargeSlot makeNotionalDischarge(final @NonNull IPort dischargePort, final int dischargeTime, final int salesPricePerMMBTu) {
		return makeNotionalDischarge(dischargePort, dischargeTime, createSalesPriceCalculator(salesPricePerMMBTu));
	}

	protected @NonNull LoadSlot makeNotionalLoad(final @NonNull IPort loadPort, final int loadTime, final IVessel vessel, final int cargoCVValue) {
		return new LoadSlot("notional-load", loadPort, new TimeWindow(loadTime, loadTime), true, vessel.getCargoCapacity(), vessel.getCargoCapacity(), new FixedPriceContract(0), cargoCVValue, false,
				true);
	}
}
