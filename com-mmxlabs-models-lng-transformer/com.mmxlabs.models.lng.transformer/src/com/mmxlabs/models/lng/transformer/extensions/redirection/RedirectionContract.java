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
import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.models.lng.transformer.extensions.redirection.providers.IOriginalDataProvider;
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
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
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

	private RedirectionVoyageCostCalculator redirVCC;
	
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
	private IOriginalDataProvider originalDateProvider;

	/**
	 * A dummy vessel for purposes of notional shipping cost calculations.
	 */
	private Vessel vessel;

	private final ICurve purchasePriceCurve;
//	private final ICurve salesPriceCurve;
	private final int notionalSpeed;
	private final IPort baseSalesMarketPort;
	private final IPort sourcePurchasePort;

	public RedirectionContract(final ICurve purchasePriceCurve, final ICurve salesPriceCurve, final int notionalSpeed, final IPort baseSalesMarketPort, final IPort sourcePurchasePort,
			final IVesselClass templateClass, final ICurve charterInCurve) {
		super();
		this.purchasePriceCurve = purchasePriceCurve;
//		this.salesPriceCurve = salesPriceCurve;
		
		redirVCC = new RedirectionVoyageCostCalculator(salesPriceCurve);
		
		this.notionalSpeed = notionalSpeed;
		this.baseSalesMarketPort = baseSalesMarketPort;
		this.sourcePurchasePort = sourcePurchasePort;

		// Create vessel with dummy indexing context.
		this.vessel = new Vessel(new IIndexingContext() {

			@Override
			public void registerType(final Class<? extends Object> type) {

			}

			@Override
			public int assignIndex(final Object indexedObject) {
				return 0;
			}
		});
		vessel.setVesselInstanceType(VesselInstanceType.SPOT_CHARTER);
		vessel.setVesselClass(templateClass);
		vessel.setHourlyCharterInPrice(charterInCurve);
	}

	@Override
	public int calculateFOBPricePerMMBTu(final ILoadSlot loadSlot, final IDischargeSlot dischargeSlot, final int loadTime, final int dischargeTime, final int dischargePricePerMMBTu,
			final long loadVolumeInM3, final long dischargeVolumeInM3, final IVessel vessel, final VoyagePlan originalPlan, final IDetailTree annotation) {

		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(loadTime);
		final int cargoCVValue = loadSlot.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadSlot);
		final int originalLoadTime = originalDateProvider.getOriginalDate(loadElement);

		if (baseSalesMarketPort.equals(dischargeSlot.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {
			long baseShippingCosts = Long.MAX_VALUE;
			String baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotation != null) {
				baseRouteAnnotation = annotation.addChild("Base Route", "");
				currentRouteAnnotation = annotation.addChild("Redirect Route", "");
			}

			for (final String route : distanceProvider.getKeys()) {
				// Notional Costs - Hammerfest -> Dragon

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getLoadPriceCalculator(), sourcePurchasePort, baseSalesMarketPort, originalLoadTime, loadVolumeInM3, vessel, cargoCVValue,
						route);
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

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadSlot.getLoadPriceCalculator(), sourcePurchasePort, dischargeSlot.getPort(), originalLoadTime, loadVolumeInM3, vessel, cargoCVValue,
						route);
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
	public int calculateLoadPricePerMMBTu(final ILoadOption loadOption, final IDischargeOption dischargeOption, final int transferTime, final int actualSalesPricePerMMBTu, final long transferVolumeInM3,
			final IDetailTree annotations) {

		final int marketPurchasePricePerMMBTu = purchasePriceCurve.getValueAtPoint(transferTime);
		final int cargoCVValue = loadOption.getCargoCVValue();

		final ISequenceElement loadElement = portSlotProvider.getElement(loadOption);
		final int originalLoadTime = originalDateProvider.getOriginalDate(loadElement);

		if (baseSalesMarketPort.equals(dischargeOption.getPort())) {
			return marketPurchasePricePerMMBTu;
		} else {
			long baseShippingCosts = Long.MAX_VALUE;
			String baseRoute = null;

			IDetailTree baseRouteAnnotation = null;
			IDetailTree currentRouteAnnotation = null;
			if (annotations != null) {
				baseRouteAnnotation = annotations.addChild("Base Route", "");
				currentRouteAnnotation = annotations.addChild("Redirect Route", "");
			}

			for (final String route : distanceProvider.getKeys()) {
				// Notional Costs - Hammerfest -> Dragon

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getLoadPriceCalculator(), sourcePurchasePort, baseSalesMarketPort, originalLoadTime, transferVolumeInM3, vessel,
						cargoCVValue, route);
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

				final VoyagePlan plan = redirVCC.calculateShippingCosts(loadOption.getLoadPriceCalculator(), sourcePurchasePort, dischargeOption.getPort(), originalLoadTime, transferVolumeInM3, vessel,
						cargoCVValue, route);
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
	public void prepareEvaluation(final ISequences sequences) {

	}

	public int getNotionalSpeed() {
		return notionalSpeed;
	}

	@Override
	public long calculateAdditionalProfitAndLoss(ILoadOption loadOption, List<IPortSlot> slots, int[] arrivalTimes, long[] volumes, int[] dischargePricesPerMMBTu, IVessel vessel, VoyagePlan plan,
			IDetailTree annotations) {
		// TODO Auto-generated method stub
		return 0;
	}
}
