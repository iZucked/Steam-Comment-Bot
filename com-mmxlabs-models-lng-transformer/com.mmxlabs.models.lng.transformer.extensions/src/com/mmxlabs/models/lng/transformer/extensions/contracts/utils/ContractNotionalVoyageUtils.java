/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.utils;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mmxlabs.models.lng.transformer.extensions.redirection.impl.FuelChoice;
import com.mmxlabs.models.lng.transformer.extensions.redirection.impl.FuelChoiceVoyageCostCalculator;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class ContractNotionalVoyageUtils {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Provider<FuelChoiceVoyageCostCalculator> voyageCostCalculatorProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Nullable
	public VoyagePlan createFOBPurchaseRealLadenNotionalBallastVoyagePlan(@NonNull final FuelChoice fuelChoice, @NonNull final ERouteOption routeOption, @NonNull final IVessel calculationVessel,
			@NonNull final IVesselAvailability resourceVesselAvailability, final int vesselCharterInRatePerDay, final long startHeelInM3, final long loadVolumeInMMBTu, @NonNull final ILoadOption buy,
			final int loadTime, @NonNull final IPort destinationPort, final int dischargeTime, final int dischargeDuration, final int salesPricePerMMBTu, final int baseFuelCostPerMT,
			@NonNull final VoyagePlan actualVoyagePlan, final int notionalSpeed, @Nullable final ShippingAnnotation shipAnnotation) {

		// Calculate the notional voyage
		final IResource resource = vesselProvider.getResource(resourceVesselAvailability);
		assert resource != null;
		final int loadDuration = durationProvider.getElementDuration(portSlotProvider.getElement(buy), resource);

		FuelChoiceVoyageCostCalculator voyageCostCalculator = voyageCostCalculatorProvider.get();

		voyageCostCalculator.setFuelChoice(fuelChoice);

		final VoyagePlan notionalVoyagePlan = voyageCostCalculator.calculateShippingCosts(buy.getPort(), destinationPort, loadTime, loadDuration, dischargeTime, dischargeDuration, calculationVessel,
				vesselCharterInRatePerDay, startHeelInM3, notionalSpeed, buy.getCargoCVValue(), routeOption, baseFuelCostPerMT, salesPricePerMMBTu);

		assert notionalVoyagePlan != null;

		return createFOBPurchaseRealLadenNotionalBallastVoyagePlan(fuelChoice, routeOption, calculationVessel, resourceVesselAvailability, vesselCharterInRatePerDay, startHeelInM3, loadVolumeInMMBTu,
				buy, loadTime, loadDuration, destinationPort, dischargeTime, dischargeDuration, salesPricePerMMBTu, baseFuelCostPerMT, actualVoyagePlan, notionalVoyagePlan, notionalSpeed,
				shipAnnotation);
	}

	@Nullable
	public VoyagePlan createFOBPurchaseRealLadenNotionalBallastVoyagePlan(@NonNull final FuelChoice fuelChoice, @NonNull final ERouteOption routeOption, @NonNull final IVessel calculationVessel,
			@NonNull final IVesselAvailability resourceVesselAvailability, final long vesselCharterInRatePerDay, final long startHeelInM3, final long loadVolumeInMMBTu, @NonNull final ILoadOption buy,
			final int loadTime, final int loadDuration, @NonNull final IPort destinationPort, final int dischargeTime, final int dischargeDuration, final int salesPricePerMMBTu,
			final int baseFuelCostPerMT, @NonNull final VoyagePlan actualVoyagePlan, @NonNull VoyagePlan notionalVoyagePlan, final int notionalSpeed,
			@Nullable final ShippingAnnotation shipAnnotation) {

		assert ((VoyageDetails) notionalVoyagePlan.getSequence()[3]).getSpeed() / 100 == notionalSpeed / 100;

		final IDetailsSequenceElement[] actualSequence = actualVoyagePlan.getSequence();
		final IDetailsSequenceElement[] notionalSequence = notionalVoyagePlan.getSequence();

		// Now splice the laden part and ballast parts together

		// Load
		notionalSequence[0] = ((PortDetails) actualSequence[0]).clone();
		// Laden Voyage
		notionalSequence[1] = ((VoyageDetails) actualSequence[1]).clone();
		// Discharge
		notionalSequence[2] = ((PortDetails) actualSequence[2]).clone();

		// Blend across LNG fuel components
		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents()) {
			for (final FuelUnit fu : FuelUnit.values()) {
				final long v = ((VoyageDetails) actualSequence[3]).getFuelConsumption(fc, fu);
				((VoyageDetails) notionalSequence[3]).setFuelConsumption(fc, fu, v);
			}
		}

		// Re-calculate the voyage plan with the spliced sequence

		final VoyagePlan newVP = new VoyagePlan();

		final int minBallastTime = distanceProvider.getTravelTime(routeOption, calculationVessel, destinationPort, buy.getPort(), dischargeTime + dischargeDuration, notionalSpeed);
		final int d2_inHours = (dischargeTime + dischargeDuration + minBallastTime) - loadTime;
		final int returnTime = loadTime + d2_inHours;

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecord.setSlotTime(buy, loadTime);

		final IPortSlot sell = ((PortDetails) notionalSequence[2]).getOptions().getPortSlot();
		final IPortSlot returnSlot = ((PortDetails) notionalSequence[4]).getOptions().getPortSlot();

		portTimesRecord.setSlotTime(sell, dischargeTime);
		portTimesRecord.setReturnSlotTime(returnSlot, returnTime);

		portTimesRecord.setSlotDuration(buy, loadDuration);
		portTimesRecord.setSlotDuration(sell, dischargeDuration);

		voyageCalculator.calculateVoyagePlan(newVP, calculationVessel, new long[] { startHeelInM3, startHeelInM3 }, baseFuelCostPerMT, portTimesRecord, notionalSequence);
		newVP.setCharterInRatePerDay(vesselCharterInRatePerDay);

		return newVP;
	}

	// Shipping cost array indices
	public static class RoundtripShippingCostBreakDown {
		public long ladenShippingHours = 0;
		public long ladenBunkerCosts = 0;
		public long ladenHireCosts = 0;
		public long portCosts = 0;
		public long routeCosts = 0;
		public long ladenShippingCosts = 0;
		public long ballastShippingHours = 0;
		public long ballastBunkerCosts = 0;
		public long ballastHireCosts = 0;
		public long ballastShippingCosts = 0;
		public FuelChoice fuelChoice = FuelChoice.Base;
		public long ladenRouteCost = 0;
		public long ballastRouteCost = 0;
		public long ballastNBOInMMBTu = 0;
		public long ladenNBOInMMBTu = 0;
	}

	private static final int IDX_LADEN = 0;
	private static final int IDX_BALLAST = 1;

	/**
	 * Populates a shipping cost array from the provided voyage plan object.
	 * 
	 * @param voyagePlan
	 * @param vessel
	 * @param buy
	 * @param loadTime
	 * @param destinationPort
	 * @param dischargeTime
	 * @param baseFuelCostPerMT
	 * @param shipAnnotation
	 * @return
	 */
	@Nullable
	public static RoundtripShippingCostBreakDown calculateRoundTrip(final IPortCostProvider portCostProvider, @Nullable final VoyagePlan voyagePlan, final IVessel vessel, final long loadVolumeInMMBTu,
			final ILoadOption buy, final int loadTime, final IPort destinationPort, final int dischargeTime, final int baseFuelCostPerMT, final int vesselHireCostsPerDay,
			final int dischargePriceInMMBTu, boolean isLadenMirrored) {

		if (voyagePlan == null) {
			return null;
		}

		final int[] distance = new int[2];
		final int[] travelTime = new int[2];
		final int[] portDuration = new int[2];
		final int[] idleTime = new int[2];
		final int[] speed = new int[2];

		final long[] portCosts = new long[2];
		final long[] routeCosts = new long[2];

		final long[] portBaseFuelInMT = new long[2];

		final long[] voyageNBOInM3 = new long[2];
		final long[] voyageIdleNBOInM3 = new long[2];
		final long[] voyageFBOInM3 = new long[2];
		final long[] voyageNBOInMMBTu = new long[2];
		final long[] voyageIdleNBOInMMBTu = new long[2];
		final long[] voyageFBOInMMBTu = new long[2];
		final long[] voyageBaseFuelInMT = new long[2];
		final long[] voyageIdleBaseFuelInMT = new long[2];

		FuelChoice fuelChoice = null;
		final Object[] sequence = voyagePlan.getSequence();
		// Skip last element
		for (int idx = 0; idx < sequence.length - 1; ++idx) {
			final Object obj = sequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails portDetails = (PortDetails) obj;
				final int arrayIdx;
				if (idx == 0) {
					// Load!
					arrayIdx = IDX_LADEN;
				} else {
					// Discharge!
					arrayIdx = IDX_BALLAST;
				}

				portDuration[arrayIdx] += portDetails.getOptions().getVisitDuration();
				portCosts[arrayIdx] += portDetails.getPortCosts();
				portBaseFuelInMT[arrayIdx] += portDetails.getFuelConsumption(FuelComponent.Base, FuelUnit.MT);
			} else if (obj instanceof VoyageDetails) {
				final int arrayIdx;
				final VoyageDetails voyageDetails = (VoyageDetails) obj;
				if (voyageDetails.getOptions().getVesselState() == VesselState.Laden) {
					// Laden!
					arrayIdx = IDX_LADEN;
				} else {
					// Ballast!
					arrayIdx = IDX_BALLAST;
				}
				distance[arrayIdx] += voyageDetails.getOptions().getDistance();
				travelTime[arrayIdx] += voyageDetails.getTravelTime();
				idleTime[arrayIdx] += voyageDetails.getIdleTime();

				voyageBaseFuelInMT[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.Base, FuelUnit.MT) + voyageDetails.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT)
						+ voyageDetails.getFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT);

				voyageBaseFuelInMT[arrayIdx] += voyageDetails.getRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT)
						+ voyageDetails.getRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT)
						+ voyageDetails.getRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT);

				voyageIdleBaseFuelInMT[arrayIdx] += +voyageDetails.getFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT)
						+ voyageDetails.getFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT);

				voyageNBOInM3[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3);
				voyageIdleNBOInM3[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3);
				voyageFBOInM3[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.FBO, FuelUnit.M3);

				voyageNBOInMMBTu[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.NBO, FuelUnit.MMBTu);
				voyageIdleNBOInMMBTu[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MMBTu);
				voyageFBOInMMBTu[arrayIdx] += voyageDetails.getFuelConsumption(FuelComponent.FBO, FuelUnit.MMBTu);

				voyageNBOInM3[arrayIdx] += voyageDetails.getRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3);
				voyageFBOInM3[arrayIdx] += voyageDetails.getRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3);

				voyageNBOInMMBTu[arrayIdx] += voyageDetails.getRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.MMBTu);
				voyageFBOInMMBTu[arrayIdx] += voyageDetails.getRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.MMBTu);

				// Note: Multiple load/discharges will not store correct speed!
				assert speed[arrayIdx] == 0;
				speed[arrayIdx] = voyageDetails.getSpeed();
				routeCosts[arrayIdx] = voyageDetails.getOptions().getRouteCost();
				if (fuelChoice == null) {
					fuelChoice = voyageDetails.getOptions().useFBOForSupplement() ? FuelChoice.FBO : FuelChoice.Base;
				}
			} else {
				throw new IllegalStateException("Unexpected element type in VoyagePlan sequence");
			}
		}

		final long ladenBunkersInMT = portBaseFuelInMT[0] + voyageBaseFuelInMT[0] + portBaseFuelInMT[1];
		final long ballastBunkersInMT = voyageBaseFuelInMT[1];
		final long ladenBunkerCost = Calculator.costFromConsumption(ladenBunkersInMT, baseFuelCostPerMT);

		final long totalPortCosts = portCosts[0] + portCosts[1];

		final long baseLadenBoilOffInM3 = voyageNBOInM3[IDX_LADEN] + voyageIdleNBOInM3[IDX_LADEN] + voyageFBOInM3[IDX_LADEN];
		final long baseBallastBoilOffInM3 = voyageNBOInM3[IDX_BALLAST] + voyageIdleNBOInM3[IDX_BALLAST] + voyageFBOInM3[IDX_BALLAST];
		final long baseBoilOffInM3 = baseLadenBoilOffInM3 + baseBallastBoilOffInM3;// + remainingHeelInM3;

		assert baseBoilOffInM3 == voyagePlan.getLNGFuelVolume();

		final RoundtripShippingCostBreakDown shippingCostArray = new RoundtripShippingCostBreakDown();
		shippingCostArray.ladenShippingHours = travelTime[IDX_LADEN] + idleTime[IDX_LADEN] + portDuration[IDX_LADEN] + portDuration[IDX_BALLAST];
		shippingCostArray.ladenBunkerCosts = ladenBunkerCost;
		shippingCostArray.ladenHireCosts = (shippingCostArray.ladenShippingHours * vesselHireCostsPerDay) / 24L;
		shippingCostArray.portCosts = totalPortCosts;
		shippingCostArray.ladenRouteCost = routeCosts[IDX_LADEN];
		shippingCostArray.ballastRouteCost = routeCosts[IDX_BALLAST];

		// NBO
		shippingCostArray.ladenNBOInMMBTu = voyageNBOInMMBTu[IDX_LADEN] + voyageIdleNBOInMMBTu[IDX_LADEN] + voyageFBOInMMBTu[IDX_LADEN];
		shippingCostArray.ballastNBOInMMBTu = voyageNBOInMMBTu[IDX_BALLAST] + voyageIdleNBOInMMBTu[IDX_BALLAST] + voyageFBOInMMBTu[IDX_BALLAST];

		shippingCostArray.ladenShippingCosts = shippingCostArray.ladenHireCosts + shippingCostArray.ladenBunkerCosts + shippingCostArray.portCosts;
		if (!isLadenMirrored) {
			shippingCostArray.ballastShippingHours = travelTime[IDX_BALLAST] + idleTime[IDX_BALLAST];
			shippingCostArray.ballastBunkerCosts = Calculator.costFromConsumption(voyageBaseFuelInMT[IDX_BALLAST], baseFuelCostPerMT);
			shippingCostArray.ballastHireCosts = (shippingCostArray.ballastShippingHours * vesselHireCostsPerDay) / 24L;
			shippingCostArray.ballastShippingCosts = shippingCostArray.ballastHireCosts + shippingCostArray.ballastBunkerCosts;
			shippingCostArray.ballastRouteCost = routeCosts[IDX_BALLAST];
		} else {
			shippingCostArray.ballastShippingHours = travelTime[IDX_LADEN] + idleTime[IDX_LADEN];
			shippingCostArray.ballastBunkerCosts = Calculator.costFromConsumption(voyageBaseFuelInMT[IDX_LADEN], baseFuelCostPerMT);
			shippingCostArray.ballastHireCosts = (shippingCostArray.ballastShippingHours * vesselHireCostsPerDay) / 24L;
			shippingCostArray.ballastShippingCosts = shippingCostArray.ballastHireCosts + shippingCostArray.ballastBunkerCosts;
			shippingCostArray.ballastRouteCost = routeCosts[IDX_LADEN];
		}
		shippingCostArray.fuelChoice = fuelChoice;
		shippingCostArray.routeCosts = shippingCostArray.ladenRouteCost + shippingCostArray.ballastRouteCost;
		return shippingCostArray;
	}

	private ERouteOption getLadenRoute(Object[] currentSequence) {
		VoyageOptions laden = getLadenVoyageOptions(currentSequence);
		if (laden != null) {
			return laden.getRoute();
		}
		return null;
	}

	private VoyageOptions getLadenVoyageOptions(Object[] currentSequence) {
		for (int idx = 0; idx < currentSequence.length; ++idx) {
			final Object obj = currentSequence[idx];
			if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				if (details.getOptions().getVesselState() == VesselState.Laden) {
					return details.getOptions();
				}
			}
		}
		return null;
	}

}
