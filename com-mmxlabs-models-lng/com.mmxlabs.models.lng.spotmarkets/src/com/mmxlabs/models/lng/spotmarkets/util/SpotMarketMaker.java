/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import java.time.YearMonth;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.port.Port;
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

	private SpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder, @NonNull final SpotMarket spotMarket) {
		this.spotMarketsModelBuilder = spotMarketsModelBuilder;
		this.spotMarket = spotMarket;
	}

	public class FOBPurchaseSpotMarketMaker extends SpotMarketMaker {

		private FOBPurchaseSpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder, @NonNull final SpotMarket spotMarket) {
			super(spotMarketsModelBuilder, spotMarket);
		}

		@Override
		public FOBPurchasesMarket build() {
			return (FOBPurchasesMarket) super.build();
		}
	}

	public class FOBSaleSpotMarketMaker extends SpotMarketMaker {

		private FOBSaleSpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder, @NonNull final SpotMarket spotMarket) {
			super(spotMarketsModelBuilder, spotMarket);
		}

		@Override
		public FOBSalesMarket build() {
			return (FOBSalesMarket) super.build();
		}
	}

	public class DESPurchaseSpotMarketMaker extends SpotMarketMaker {

		private DESPurchaseSpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder, @NonNull final SpotMarket spotMarket) {
			super(spotMarketsModelBuilder, spotMarket);
		}

		@Override
		public DESPurchaseMarket build() {
			return (DESPurchaseMarket) super.build();
		}
	}

	public class DESSaleSpotMarketMaker extends SpotMarketMaker {
		private int daysBuffer = 0;
		
		private DESSaleSpotMarketMaker(@NonNull final SpotMarketsModelBuilder spotMarketsModelBuilder, @NonNull final SpotMarket spotMarket) {
			super(spotMarketsModelBuilder, spotMarket);
		}

		@Override
		public DESSalesMarket build() {
			DESSalesMarket dsm = (DESSalesMarket) super.build();
			dsm.setDaysBuffer(daysBuffer);
			return dsm;
		}
		
		public DESSaleSpotMarketMaker withDaysBuffer(int daysBuffer) {
			this.daysBuffer = daysBuffer;
			return this;
		}
	}

	@NonNull
	public FOBPurchaseSpotMarketMaker withFOBPurchaseMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression,
			@Nullable final Double cv) {

		final FOBPurchasesMarket market = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
		market.setName(name);
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.setNotionalPort(notionalPort);
		market.setEntity(entity);

		final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		if (cv != null) {
			market.setCv(cv);
		}

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());

		return new FOBPurchaseSpotMarketMaker(this.spotMarketsModelBuilder, this.spotMarket);
	}

	@NonNull
	public DESPurchaseSpotMarketMaker withDESPurchaseMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity,
			@NonNull final String priceExpression, @Nullable final Double cv) {

		final DESPurchaseMarket market = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		market.setName(name);
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.getDestinationPorts().addAll(ports);
		market.setEntity(entity);

		final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		if (cv != null) {
			market.setCv(cv);
		}

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());

		return new DESPurchaseSpotMarketMaker(this.spotMarketsModelBuilder, this.spotMarket);
	}

	@NonNull
	public FOBSaleSpotMarketMaker withFOBSaleMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity,
			@NonNull final String priceExpression) {

		final FOBSalesMarket market = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
		market.setName(name);
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.getOriginPorts().addAll(ports);
		market.setEntity(entity);

		final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());
		return new FOBSaleSpotMarketMaker(this.spotMarketsModelBuilder, this.spotMarket);
	}

	@NonNull
	public DESSaleSpotMarketMaker withDESSaleMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression) {

		final DESSalesMarket market = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
		market.setName(name);
		market.setPricingEvent(PricingEvent.START_LOAD);
		market.setNotionalPort(notionalPort);
		market.setEntity(entity);

		final DateShiftExpressionPriceParameters params = CommercialFactory.eINSTANCE.createDateShiftExpressionPriceParameters();
		params.setPriceExpression(priceExpression);
		market.setPriceInfo(params);

		this.spotMarket = market;
		this.spotMarket.setAvailability(SpotMarketsFactory.eINSTANCE.createSpotAvailability());
		this.spotMarket.getAvailability().setCurve(PricingFactory.eINSTANCE.createDataIndex());
		return new DESSaleSpotMarketMaker(this.spotMarketsModelBuilder, this.spotMarket);
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
	public SpotMarketMaker with(Consumer<SpotMarket> action) {
		action.accept(spotMarket);
		return this;
	}

	@NonNull
	public <T extends SpotMarket> T build() {
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

		return (T) spotMarket;
	}

}
