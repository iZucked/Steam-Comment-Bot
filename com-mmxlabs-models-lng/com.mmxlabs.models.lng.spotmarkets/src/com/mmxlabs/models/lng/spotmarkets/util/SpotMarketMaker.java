/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class SpotMarketMaker {
	@NonNull
	protected final SpotMarketsModelBuilder spotMarketsModelBuilder;

	protected SpotMarket spotMarket = null;

	public SpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder) {
		this.spotMarketsModelBuilder = spotMarketsModelBuilder;
	}

	@NonNull
	public SpotMarketMaker withFOBPurchaseMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression,
			@Nullable final Double cv) {

		final FOBPurchasesMarket market = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.setNotionalPort(notionalPort);
		market.setEntity(entity);

		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		if (cv != null) {
			market.setCv(cv);
		}

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());

		return this;
	}

	@NonNull
	public SpotMarketMaker withDESPurchaseMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression,
			@Nullable final Double cv) {

		final DESPurchaseMarket market = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.getDestinationPorts().addAll(ports);
		market.setEntity(entity);

		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		if (cv != null) {
			market.setCv(cv);
		}

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());
		return this;
	}

	@NonNull
	public SpotMarketMaker withFOBSaleMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression) {

		final FOBSalesMarket market = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.getOriginPorts().addAll(ports);
		market.setEntity(entity);

		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());
		return this;
	}

	@NonNull
	public SpotMarketMaker withDESSaleMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression) {

		final DESSalesMarket market = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.setNotionalPort(notionalPort);
		market.setEntity(entity);

		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());
		return this;
	}

	@NonNull
	public SpotMarketMaker withVolumeLimits(final int minVolume, final int maxVolume, @NonNull final VolumeUnits volumeUnit) {
		spotMarket.setVolumeLimitsUnit(volumeUnit);
		spotMarket.setMinQuantity(minVolume);
		spotMarket.setMaxQuantity(maxVolume);

		return this;
	}

	@NonNull
	public SpotMarketMaker withEnabled(final boolean enabled) {
		spotMarket.setEnabled(enabled);
		return this;
	}

	@NonNull
	public SpotMarketMaker withAvailabilityConstant(@Nullable final Integer constant) {
		if (constant != null) {
			spotMarket.getAvailability().setConstant(constant);
		} else {
			spotMarket.getAvailability().unsetConstant();
		}
		return this;
	}

	@NonNull
	public SpotMarketMaker withAvailabilityDate(@NonNull final YearMonth date, final int count) {
		final IndexPoint<Integer> entry = PricingFactory.eINSTANCE.createIndexPoint();
		entry.setDate(date);
		entry.setValue(count);
		spotMarket.getAvailability().getCurve().getPoints().add(entry);
		return this;
	}

	@NonNull
	public SpotMarketMaker withPricingEvent(@NonNull final PricingEvent pricingEvent) {
		spotMarket.setPricingEvent(pricingEvent);
		return this;
	}

	@NonNull
	public SpotMarket build() {

		if (spotMarket instanceof DESPurchaseMarket) {
			spotMarketsModelBuilder.getSpotMarketsModel().getDesPurchaseSpotMarket().getMarkets().add(spotMarket);
		} else if (spotMarket instanceof FOBPurchasesMarket) {
			spotMarketsModelBuilder.getSpotMarketsModel().getFobPurchasesSpotMarket().getMarkets().add(spotMarket);
		} else if (spotMarket instanceof DESSalesMarket) {
			spotMarketsModelBuilder.getSpotMarketsModel().getDesSalesSpotMarket().getMarkets().add(spotMarket);
		} else if (spotMarket instanceof FOBSalesMarket) {
			spotMarketsModelBuilder.getSpotMarketsModel().getFobSalesSpotMarket().getMarkets().add(spotMarket);
		} else {
			throw new IllegalStateException("Unknown subtype");
		}

		return spotMarket;
	}

}
