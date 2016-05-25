/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.util;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.APortSet;

public class SpotMarketsModelBuilder {

	private final @NonNull SpotMarketsModel spotMarketsModel;

	public SpotMarketsModelBuilder(@NonNull final SpotMarketsModel spotMarketsModel) {
		this.spotMarketsModel = spotMarketsModel;
	}

	@NonNull
	public SpotMarketsModel getSpotMarketsModel() {
		return spotMarketsModel;
	}

	@NonNull
	public CharterInMarket createCharterInMarket(@NonNull final String name, @NonNull final VesselClass vesselClass, @NonNull final String charterInRate, final int charterInCount) {

		final CharterInMarket charterInMarket = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
		charterInMarket.setName(name);
		charterInMarket.setVesselClass(vesselClass);
		charterInMarket.setCharterInRate(charterInRate);
		charterInMarket.setSpotCharterCount(charterInCount);

		spotMarketsModel.getCharterInMarkets().add(charterInMarket);
		return charterInMarket;
	}

	@NonNull
	public SpotMarketMaker makeDESPurchaseMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression,
			@Nullable final Double cv) {
		final SpotMarketMaker maker = new SpotMarketMaker(this);
		return maker.withDESPurchaseMarket(name, ports, entity, priceExpression, cv);
	}

	@NonNull
	public SpotMarketMaker makeDESSaleMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression) {
		final SpotMarketMaker maker = new SpotMarketMaker(this);
		return maker.withDESSaleMarket(name, notionalPort, entity, priceExpression);
	}

	@NonNull
	public SpotMarketMaker makeFOBPurchaseMarket(@NonNull final String name, @NonNull final Port notionalPort, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression,
			@Nullable final Double cv) {
		final SpotMarketMaker maker = new SpotMarketMaker(this);
		return maker.withFOBPurchaseMarket(name, notionalPort, entity, priceExpression, cv);
	}

	@NonNull
	public SpotMarketMaker makeFOBSaleMarket(@NonNull final String name, @NonNull final List<APortSet<Port>> ports, @Nullable final BaseLegalEntity entity, @NonNull final String priceExpression) {
		final SpotMarketMaker maker = new SpotMarketMaker(this);
		return maker.withFOBSaleMarket(name, ports, entity, priceExpression);
	}
}
