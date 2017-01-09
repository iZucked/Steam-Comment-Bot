/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.common.detailtree.impl.UnitPriceDetailElement;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.DefaultVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Sample Redirection contract. A combination of both the {@link NetbackContract} and {@link ProfitSharingContract}.
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionContract implements ILoadPriceCalculator {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IVoyageCostCalculator redirVCC;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IPortVisitDurationProvider portVisitDurationProvider;

	@Inject
	private ICharterRateCalculator charterRateCalculator;

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Inject
	private ShippingCostHelper shippingCostHelper;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IVesselProvider vesselProvider;

	private final ICurve purchasePriceCurve;
	// private final ICurve salesPriceCurve;
	private final int notionalSpeed;
	private final IPort baseSalesMarketPort;
	private final IPort sourcePurchasePort;

	public RedirectionContract(final ICurve purchasePriceCurve, final ICurve salesPriceCurve, final int notionalSpeed, final IPort baseSalesMarketPort, final IPort sourcePurchasePort,
			final IVesselClass templateClass, final ICurve charterInCurve) {
		super();
		this.purchasePriceCurve = purchasePriceCurve;

		this.notionalSpeed = notionalSpeed;
		this.baseSalesMarketPort = baseSalesMarketPort;
		this.sourcePurchasePort = sourcePurchasePort;
	}

	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IVesselAvailability vesselAvailability, final int vesselStartTime, final VoyagePlan originalPlan, @Nullable VolumeAllocatedSequences volumeAllocatedSequences,
			final IDetailTree annotation) {

		final int loadTime = allocationAnnotation.getSlotTime(loadSlot);
		final int dischargeTime = allocationAnnotation.getSlotTime(dischargeSlot);

		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(loadTime, loadSlot));
		final int cargoCVValue = loadSlot.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeSlot);
		final ITimeWindow baseTimeWindow = shippingHoursRestrictionProvider.getBaseTime(loadElement);

		if (baseSalesMarketPort.equals(dischargeSlot.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {

			final long startHeelInM3 = originalPlan.getStartingHeelInM3();

			final int originalLoadTime = shippingHoursRestrictionProvider.getBaseTime(loadElement).getInclusiveStart();

			long baseShippingCosts = Long.MAX_VALUE;
			ERouteOption baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotation != null) {
				baseRouteAnnotation = annotation.addChild("Base Route", "");
				currentRouteAnnotation = annotation.addChild("Redirect Route", "");
			}

			// TODO: E.g. Model based on distance @ max speed?
			final int baseDischargeTime = dischargeTime;
			{
				final ERouteOption route = ERouteOption.DIRECT;
				final int loadDuration = durationProvider.getElementDuration(loadElement, vesselProvider.getResource(vesselAvailability));
				final int dischargeDuration = portVisitDurationProvider.getVisitDuration(baseSalesMarketPort, PortType.Discharge);

				final long vesselCharterInRatePerDay;
				if (actualsDataProvider.hasActuals(loadSlot)) {
					vesselCharterInRatePerDay = actualsDataProvider.getCharterRatePerDay(loadSlot);
				} else {
					vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, /** FIXME: not utc */
							vesselStartTime, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot));
				}

				final int utcLoadTime = timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot);
				final int baseFuelPriceInMT = vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), utcLoadTime);
				// final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), baseSalesMarketPort, originalLoadTime, loadDuration, baseDischargeTime, dischargeDuration,
				// vesselAvailability.getVessel(), vesselCharterInRatePerDay, startHeelInM3, vesselAvailability.getVessel().getVesselClass().getMaxSpeed(), loadSlot.getCargoCVValue(), route,
				// vesselAvailability.getVessel().getVesselClass().getBaseFuelUnitPrice(), dischargePricePerMMBTu);
				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), baseSalesMarketPort, originalLoadTime, loadDuration, baseDischargeTime, dischargeDuration,
						vesselAvailability.getVessel(), vesselCharterInRatePerDay, startHeelInM3, vesselAvailability.getVessel().getVesselClass().getMaxSpeed(), loadSlot.getCargoCVValue(), route,
						baseFuelPriceInMT, dischargePricePerMMBTu);
				if (plan == null) {
					return Integer.MAX_VALUE;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vesselAvailability, false, true);
				if (baseRouteAnnotation != null && details != null) {
					baseRouteAnnotation.addChild(route.name(), "").addChild(details[0]);
				}

				if (costs < baseShippingCosts) {
					baseShippingCosts = costs;
					baseRoute = route;
				}
			}

			if (baseShippingCosts == Long.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}

			// Current voyage plan
			long notionalShippingCosts = Long.MAX_VALUE;
			ERouteOption notionalRoute = null;
			{
				final ERouteOption route = ERouteOption.DIRECT;
				final int loadDuration = durationProvider.getElementDuration(loadElement, vesselProvider.getResource(vesselAvailability));
				final int dischargeDuration = durationProvider.getElementDuration(dischargeElement, vesselProvider.getResource(vesselAvailability));
				final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vesselAvailability, vesselStartTime, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot));

				final int utcLoadTime = timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot);
				final int baseFuelPriceInMT = vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), utcLoadTime);

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), dischargeSlot.getPort(), originalLoadTime, loadDuration, dischargeTime, dischargeDuration,
						vesselAvailability.getVessel(), vesselCharterInRatePerDay, startHeelInM3, vesselAvailability.getVessel().getVesselClass().getMaxSpeed(), loadSlot.getCargoCVValue(), route,
						baseFuelPriceInMT, dischargePricePerMMBTu);

				if (plan == null) {
					return Integer.MAX_VALUE;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vesselAvailability, false, true);
				if (currentRouteAnnotation != null && details != null) {
					currentRouteAnnotation.addChild(route.name(), "").addChild(details[0]);
				}
				if (costs < notionalShippingCosts) {
					notionalShippingCosts = costs;
					notionalRoute = route;
				}
			}
			if (notionalShippingCosts == Long.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}

			final long incrementalShipping = notionalShippingCosts - baseShippingCosts;

			// Profit Share
			int profitSharePerMMBTu = 0;
			// Clamp to [0, infinity)
			final long diff = incrementalShipping;
			if (diff > 0) {
				profitSharePerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(diff, allocationAnnotation.getCommercialSlotVolumeInMMBTu(dischargeSlot));
			}

			if (annotation != null) {
				annotation.addChild("Total Incremental Shipping", new TotalCostDetailElement(incrementalShipping));
				annotation.addChild("Incremental Shipping", new UnitPriceDetailElement(profitSharePerMMBTu / 1000l));
			}
			// TODO: Apply profit share factor
			return marketPurchasePricePerMMBTu + profitSharePerMMBTu;

		}
	}

	private int calculateLoadPricePerMMBTu(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final IPort pricingPort, final int actualSalesPricePerMMBTu,
			final long transferVolumeInM3, final IDetailTree annotations) {

		// FIXME: Safety Heel
		final long startHeelInM3 = 0;
		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(transferTime, pricingPort));
		final int cargoCVValue = loadOption.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadOption);
		final ISequenceElement dischargeElement = portSlotProvider.getElement(dischargeOption);
		final ITimeWindow baseTimeWindow = shippingHoursRestrictionProvider.getBaseTime(loadElement);
		final IVessel vessel = nominatedVesselProvider.getNominatedVessel(loadElement);
		assert vessel != null;
		if (baseSalesMarketPort.equals(dischargeOption.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {
			final DefaultVesselAvailability notionalVesselAvailability = new DefaultVesselAvailability(vessel, VesselInstanceType.FLEET);
			final int originalLoadTime = shippingHoursRestrictionProvider.getBaseTime(loadElement).getInclusiveStart();

			long baseShippingCosts = Long.MAX_VALUE;
			ERouteOption baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotations != null) {
				baseRouteAnnotation = annotations.addChild("Base Route", "");
				currentRouteAnnotation = annotations.addChild("Redirect Route", "");
			}

			// TODO: E.g. Model based on distance @ max speed?
			final int baseDischargeTime = transferTime;
			final int utcLoadTime = timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption);
			final int baseFuelPriceInMT = vesselBaseFuelCalculator.getBaseFuelPrice(vessel, utcLoadTime);
			{
				final ERouteOption route = ERouteOption.DIRECT;
				// Notional Costs - Purchase port to base destination
				final int loadDuration = portVisitDurationProvider.getVisitDuration(loadOption.getPort(), PortType.Load);
				final int dischargeDuration = portVisitDurationProvider.getVisitDuration(baseSalesMarketPort, PortType.Discharge);

				final long vesselCharterInRatePerDay;
				if (actualsDataProvider.hasActuals(loadOption)) {
					vesselCharterInRatePerDay = actualsDataProvider.getCharterRatePerDay(loadOption);
				} else {
					vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(notionalVesselAvailability, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption),
							timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption));
				}

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), baseSalesMarketPort, originalLoadTime, loadDuration, baseDischargeTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, vessel.getVesselClass().getMaxSpeed(), loadOption.getCargoCVValue(), route, baseFuelPriceInMT, actualSalesPricePerMMBTu);
				if (plan == null) {
					return Integer.MAX_VALUE;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, notionalVesselAvailability, false, true);
				if (baseRouteAnnotation != null && details != null) {
					baseRouteAnnotation.addChild(route.name(), "").addChild(details[0]);
				}

				if (costs < baseShippingCosts) {
					baseShippingCosts = costs;
					baseRoute = route;
				}
			}

			if (baseShippingCosts == Long.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}

			// Current voyage plan
			long notionalShippingCosts = Long.MAX_VALUE;
			ERouteOption notionalRoute = null;
			{
				final ERouteOption route = ERouteOption.DIRECT;

				final int loadDuration = portVisitDurationProvider.getVisitDuration(loadOption.getPort(), PortType.Load);
				final int dischargeDuration = portVisitDurationProvider.getVisitDuration(dischargeOption.getPort(), PortType.Discharge);

				final long vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(notionalVesselAvailability, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption),
						timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption));

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), dischargeOption.getPort(), originalLoadTime, loadDuration, transferTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, notionalVesselAvailability.getVessel().getVesselClass().getMaxSpeed(), loadOption.getCargoCVValue(), route, baseFuelPriceInMT,
						actualSalesPricePerMMBTu);

				if (plan == null) {
					return Integer.MAX_VALUE;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, notionalVesselAvailability, false, true);
				if (currentRouteAnnotation != null && details != null) {
					currentRouteAnnotation.addChild(route.name(), "").addChild(details[0]);
				}
				if (costs < notionalShippingCosts) {
					notionalShippingCosts = costs;
					notionalRoute = route;
				}
			}
			if (notionalShippingCosts == Long.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}

			final long incrementalShipping = notionalShippingCosts - baseShippingCosts;

			// Profit Share
			int profitSharePerMMBTu = 0;
			// Clamp to [0, infinity)
			final long diff = incrementalShipping;
			if (diff > 0) {
				profitSharePerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInM3(diff, transferVolumeInM3, cargoCVValue);
			}

			if (annotations != null) {
				annotations.addChild("Total Incremental Shipping", new TotalCostDetailElement(incrementalShipping));
				annotations.addChild("Incremental Shipping", new UnitPriceDetailElement(profitSharePerMMBTu / 1000l));
			}
			// TODO: Apply profit share factor
			return marketPurchasePricePerMMBTu + profitSharePerMMBTu;
		}
	}

	public int getNotionalSpeed() {
		return notionalSpeed;
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		final int transferTime = allocationAnnotation.getSlotTime(dischargeSlot);
		final long transferVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(dischargeSlot);
		return calculateLoadPricePerMMBTu(loadOption, dischargeSlot, transferTime, dischargeSlot.getPort(), dischargePricePerMMBTu, transferVolumeInM3, annotations);
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			@Nullable VolumeAllocatedSequences volumeAllocatedSequences, final IDetailTree annotations) {
		final int transferTime = allocationAnnotation.getSlotTime(loadSlot);
		final long transferVolumeInM3 = allocationAnnotation.getCommercialSlotVolumeInM3(loadSlot);
		return calculateLoadPricePerMMBTu(loadSlot, dischargeOption, transferTime, loadSlot.getPort(), dischargePricePerMMBTu, transferVolumeInM3, annotations);
	}

	@Override
	public PricingEventType getCalculatorPricingEventType(ILoadOption loadOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEstimatedPurchasePrice(ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCalculatorPricingDate(ILoadOption loadOption, IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO Auto-generated method stub
		return 0;
	}

}
