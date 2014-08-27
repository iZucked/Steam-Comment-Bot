/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.transformer.extensions.redirection.IVoyageCostCalculator;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;
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
	public @Nullable
	VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue,
			@NonNull final String route, final int basePricePerMT, final int salesPricePerMMBTu) {
		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDuration, dischargeTime, dischargeDuration, vessel, vesselCharterInRatePerDay, startHeelInM3, notionalBallastSpeed,
				cargoCVValue, route, basePricePerMT, createSalesPriceCalculator(salesPricePerMMBTu));
	}

	public @Nullable
	VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int loadDuration, final int dischargeTime,
			final int dischargeDuration, final int returnTime, @NonNull final IVessel vessel, final int vesselCharterInRatePerDay, final long startHeelInM3, final int cargoCVValue,
			@NonNull final String route, final int basePricePerMT, final int salesPricePerMMBTu) {
		return calculateShippingCosts(loadPort, dischargePort, loadTime, loadDuration, dischargeTime, dischargeDuration, returnTime, vessel, vesselCharterInRatePerDay, startHeelInM3, cargoCVValue,
				route, basePricePerMT, createSalesPriceCalculator(salesPricePerMMBTu));
	}

	@Nullable
	protected abstract VoyagePlan calculateShippingCosts(@NonNull IPort loadPort, @NonNull IPort dischargePort, int loadTime, int loadDuration, int dischargeTime, int dischargeDuration,
			int returnTime, @NonNull IVessel vessel, final int vesselCharterInRatePerDay, long startHeelInM3, int cargoCVValue, @NonNull String route, int baseFuelPricePerMT,
			@NonNull ISalesPriceCalculator salesPrice);

	public abstract @Nullable
	VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, int loadDuration, final int dischargeTime, final int dischargeDuration,
			@NonNull final IVessel vessel, final int vesselCharterInRatePerDay, long startHeelInM3, final int notionalBallastSpeed, final int cargoCVValue, @NonNull final String route,
			final int basePricePerMT, @NonNull final ISalesPriceCalculator salesPriceCalculator);

	protected @NonNull
	VoyageDetails calculateVoyageDetails(@NonNull final VesselState vesselState, @NonNull final IVessel vessel, @NonNull final String route, final int distance, final int availableTime,
			@NonNull final PortSlot from, @NonNull final PortSlot to, final int cargoCV) {
		final VoyageDetails voyageDetails = new VoyageDetails();
		{
			final VoyageOptions voyageOptions = createVoyageOptions(vesselState, vessel, route, distance, availableTime, from, to, cargoCV);

			voyageCalculator.calculateVoyageFuelRequirements(voyageOptions, voyageDetails);
		}
		return voyageDetails;
	}

	protected @NonNull
	VoyageOptions createVoyageOptions(final VesselState vesselState, final IVessel vessel, final String route, final int distance, final int availableTime, final PortSlot from, final PortSlot to,
			final int cargoCV) {
		final VoyageOptions voyageOptions = new VoyageOptions();
		voyageOptions.setAvailableTime(availableTime);
		voyageOptions.setAllowCooldown(false);
		voyageOptions.setDistance(distance);
		voyageOptions.setFromPortSlot(from);
		voyageOptions.setRoute(route);
		voyageOptions.setToPortSlot(to);
		voyageOptions.setShouldBeCold(true);
		voyageOptions.setUseFBOForSupplement(true);
		voyageOptions.setUseNBOForIdle(true);
		voyageOptions.setUseNBOForTravel(true);
		voyageOptions.setVessel(vessel);
		voyageOptions.setVesselState(vesselState);
		voyageOptions.setWarm(false);

		final IVesselClass vesselClass = vessel.getVesselClass();
		// Convert rate to MT equivalent per day
		final int nboRateInMTPerDay = (int) Calculator.convertM3ToMT(vesselClass.getNBORate(vesselState), cargoCV, vesselClass.getBaseFuelConversionFactor());
		final int nboSpeed = vesselClass.getConsumptionRate(vesselState).getSpeed(nboRateInMTPerDay);
		voyageOptions.setNBOSpeed(nboSpeed);
		return voyageOptions;
	}

	protected @NonNull
	ISalesPriceCalculator createSalesPriceCalculator(final int salesPricePerMMBTu) {
		final SimpleContract contract = new SimpleContract() {

			@Override
			protected int calculateSimpleUnitPrice(final int loadTime, final IPort port) {
				return salesPricePerMMBTu;
			}
		};

		injector.injectMembers(contract);
		return contract;
	}
}
