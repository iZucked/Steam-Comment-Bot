/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Sample Redirection contract. A combination of both the {@link NetbackContract} and {@link ProfitSharingContract}.
 * 
 * @author Simon Goodall
 * 
 */
public class RedirectionContract implements ILoadPriceCalculator {

	@Inject
	private IVoyageCostCalculator redirVCC;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

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
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan originalPlan, final IDetailTree annotation) {

		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(loadTime);
		final int cargoCVValue = loadSlot.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final ITimeWindow baseTimeWindow = shippingHoursRestrictionProvider.getBaseTime(loadElement);

		if (baseSalesMarketPort.equals(dischargeSlot.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {

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

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), baseSalesMarketPort, originalLoadTime, baseDischargeTime, vessel, vessel.getVesselClass().getMaxSpeed(),
						loadSlot.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(), dischargePricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = entityValueCalculator.getShippingCosts(plan, vessel, false, true, originalLoadTime, details);
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

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getPort(), dischargeSlot.getPort(), originalLoadTime, dischargeTime, vessel, vessel.getVesselClass().getMaxSpeed(),
						loadSlot.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(), dischargePricePerMMBTu);

				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotation == null ? null : new IDetailTree[2];
				final long costs = entityValueCalculator.getShippingCosts(plan, vessel, false, true, originalLoadTime, details);
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
				profitSharePerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInM3(diff, dischargeVolumeInM3, cargoCVValue);
			}

			if (annotation != null) {
				annotation.addChild("Total Incremental Shipping", new TotalCostDetailElement(incrementalShipping));
				annotation.addChild("Incremental Shipping", new UnitPriceDetailElement(profitSharePerMMBTu / 1000l));
			}
			// TODO: Apply profit share factor
			return marketPurchasePricePerMMBTu + profitSharePerMMBTu;

		}
	}

	@Override
	public int calculateLoadPricePerMMBTu(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int actualSalesPricePerMMBTu,
			final long transferVolumeInM3, final IDetailTree annotations) {

		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(transferTime);
		final int cargoCVValue = loadOption.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadOption);
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
				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), baseSalesMarketPort, originalLoadTime, baseDischargeTime, vessel, vessel.getVesselClass().getMaxSpeed(),
						loadOption.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(), actualSalesPricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = entityValueCalculator.getShippingCosts(plan, vessel, false, true, originalLoadTime, details);
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

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getPort(), dischargeOption.getPort(), originalLoadTime, transferTime, vessel, vessel.getVesselClass().getMaxSpeed(),
						loadOption.getCargoCVValue(), route, vessel.getVesselClass().getBaseFuelUnitPrice(), actualSalesPricePerMMBTu);
				if (plan == null) {
					continue;
				}
				final IDetailTree details[] = annotations == null ? null : new IDetailTree[2];
				final long costs = entityValueCalculator.getShippingCosts(plan, vessel, false, true, originalLoadTime, details);
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
	public void prepareEvaluation(final ISequences sequences, final ScheduledSequences scheduledSequences) {

	}

	public int getNotionalSpeed() {
		return notionalSpeed;
	}

	@Override
	public long calculateAdditionalProfitAndLoss(final ILoadOption loadOption, final List<IPortSlot> slots, final int[] arrivalTimes, final long[] volumes, final int[] dischargePricesPerMMBTu,
			final IVessel vessel, final VoyagePlan plan, final IDetailTree annotations) {
		// TODO Auto-generated method stub
		return 0;
	}
}
