/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.common.detailtree.impl.UnitPriceDetailElement;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
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
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

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
	private ShippingCostHelper shippingCostHelper;

	@Inject
	private IActualsDataProvider actualsDataProvider;

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
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, IAllocationAnnotation allocationAnnotation,
			final IVessel vessel, final int vesselStartTime, final VoyagePlan originalPlan, final IDetailTree annotation) {

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

			final int originalLoadTime = shippingHoursRestrictionProvider.getBaseTime(loadElement).getStart();

			long baseShippingCosts = Long.MAX_VALUE;
			String baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotation != null) {
				baseRouteAnnotation = annotation.addChild("Base Route", "");
				currentRouteAnnotation = annotation.addChild("Redirect Route", "");
			}

			// TODO: E.g. Model based on distance @ max speed?
			final int baseDischargeTime = dischargeTime;
			for (final String route : distanceProvider.getKeys()) {

				final int loadDuration = durationProvider.getElementDuration(loadElement, vessel);
				final int dischargeDuration = portVisitDurationProvider.getVisitDuration(baseSalesMarketPort, PortType.Discharge);

				final int vesselCharterInRatePerDay;
				if (actualsDataProvider.hasActuals(loadSlot)) {
					vesselCharterInRatePerDay = actualsDataProvider.getCharterRatePerDay(loadSlot);
				} else {
					vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, vesselStartTime, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot));
				}

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), baseSalesMarketPort, originalLoadTime, loadDuration, baseDischargeTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, vessel.getVesselClass().getMaxSpeed(), loadSlot.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(),
						dischargePricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vessel, false, true);
				if (baseRouteAnnotation != null && details != null) {
					baseRouteAnnotation.addChild(route, "").addChild(details[0]);
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
			String notionalRoute = null;
			for (final String route : distanceProvider.getKeys()) {
				final int loadDuration = durationProvider.getElementDuration(loadElement, vessel);
				final int dischargeDuration = durationProvider.getElementDuration(dischargeElement, vessel);
				final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, vesselStartTime, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadSlot));

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), dischargeSlot.getPort(), originalLoadTime, loadDuration, dischargeTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, vessel.getVesselClass().getMaxSpeed(), loadSlot.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(),
						dischargePricePerMMBTu);

				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vessel, false, true);
				if (currentRouteAnnotation != null && details != null) {
					currentRouteAnnotation.addChild(route, "").addChild(details[0]);
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
				profitSharePerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(diff, allocationAnnotation.getSlotVolumeInMMBTu(dischargeSlot));
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
		if (baseSalesMarketPort.equals(dischargeOption.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {
			final int originalLoadTime = shippingHoursRestrictionProvider.getBaseTime(loadElement).getStart();

			long baseShippingCosts = Long.MAX_VALUE;
			String baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotations != null) {
				baseRouteAnnotation = annotations.addChild("Base Route", "");
				currentRouteAnnotation = annotations.addChild("Redirect Route", "");
			}

			// TODO: E.g. Model based on distance @ max speed?
			final int baseDischargeTime = transferTime;
			for (final String route : distanceProvider.getKeys()) {
				// Notional Costs - Purchase port to base destination
				final int loadDuration = durationProvider.getElementDuration(loadElement, vessel);
				final int dischargeDuration = portVisitDurationProvider.getVisitDuration(baseSalesMarketPort, PortType.Discharge);

				final int vesselCharterInRatePerDay;
				if (actualsDataProvider.hasActuals(loadOption)) {
					vesselCharterInRatePerDay = actualsDataProvider.getCharterRatePerDay(loadOption);
				} else {
					vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption),
							timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption));
				}

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), baseSalesMarketPort, originalLoadTime, loadDuration, baseDischargeTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, vessel.getVesselClass().getMaxSpeed(), loadOption.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(),
						actualSalesPricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vessel, false, true);
				if (baseRouteAnnotation != null && details != null) {
					baseRouteAnnotation.addChild(route, "").addChild(details[0]);
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
			String notionalRoute = null;
			for (final String route : distanceProvider.getKeys()) {

				final int loadDuration = durationProvider.getElementDuration(loadElement, vessel);
				final int dischargeDuration = durationProvider.getElementDuration(dischargeElement, vessel);
				final int vesselCharterInRatePerDay = charterRateCalculator.getCharterRatePerDay(vessel, timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption),
						timeZoneToUtcOffsetProvider.UTC(originalLoadTime, loadOption));

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), dischargeOption.getPort(), originalLoadTime, loadDuration, transferTime, dischargeDuration, vessel,
						vesselCharterInRatePerDay, startHeelInM3, vessel.getVesselClass().getMaxSpeed(), loadOption.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(),
						actualSalesPricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = shippingCostHelper.getShippingCosts(plan, vessel, false, true);
				if (currentRouteAnnotation != null && details != null) {
					currentRouteAnnotation.addChild(route, "").addChild(details[0]);
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

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	@Override
	public void prepareRealPNL() {

	}

	public int getNotionalSpeed() {
		return notionalSpeed;
	}

	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final IAllocationAnnotation allocationAnnotation, final int[] dischargePricesPerMMBTu, final IVessel vessel,
			final int vesselStartTime, final VoyagePlan plan, final IDetailTree annotations) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int calculateDESPurchasePricePerMMBTu(final ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		final int transferTime = allocationAnnotation.getSlotTime(dischargeSlot);
		final long transferVolumeInM3 = allocationAnnotation.getSlotVolumeInM3(dischargeSlot);
		return calculateLoadPricePerMMBTu(loadOption, dischargeSlot, transferTime, dischargeSlot.getPort(), dischargePricePerMMBTu, transferVolumeInM3, annotations);
	}

	@Override
	public int calculatePriceForFOBSalePerMMBTu(final ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int dischargePricePerMMBTu, final IAllocationAnnotation allocationAnnotation,
			final IDetailTree annotations) {
		final int transferTime = allocationAnnotation.getSlotTime(loadSlot);
		final long transferVolumeInM3 = allocationAnnotation.getSlotVolumeInM3(loadSlot);
		return calculateLoadPricePerMMBTu(loadSlot, dischargeOption, transferTime, loadSlot.getPort(), dischargePricePerMMBTu, transferVolumeInM3, annotations);
	}
}
