/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class PricingModelBuilder {

	private final @NonNull PricingModel pricingModel;

	public PricingModelBuilder(@NonNull final PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@NonNull
	public CharterIndex createCharterIndex(@NonNull final String name, @Nullable final String currencyUnit, @Nullable final String volumeUnit, final int fixedPricePerDay) {

		final CharterIndex charterIndex = PricingFactory.eINSTANCE.createCharterIndex();
		charterIndex.setName(name);
		if (currencyUnit != null) {
			charterIndex.setCurrencyUnit(currencyUnit);
		}
		if (volumeUnit != null) {
			charterIndex.setVolumeUnit(volumeUnit);
		}

		final DerivedIndex<Integer> derivedIndex = PricingFactory.eINSTANCE.createDerivedIndex();
		derivedIndex.setExpression(Integer.toString(fixedPricePerDay));

		charterIndex.setData(derivedIndex);

		pricingModel.getCharterIndices().add(charterIndex);

		return charterIndex;
	}

	public @NonNull BaseFuelIndex createBaseFuelExpressionIndex(@NonNull final String name, final double baseFuelUnitPrice) {

		final DerivedIndex<Double> indexData = PricingFactory.eINSTANCE.createDerivedIndex();
		indexData.setExpression(Double.toString(baseFuelUnitPrice));

		final BaseFuelIndex baseFuelIndex = PricingFactory.eINSTANCE.createBaseFuelIndex();
		baseFuelIndex.setName(name);
		baseFuelIndex.setData(indexData);

		pricingModel.getBaseFuelPrices().add(baseFuelIndex);

		return baseFuelIndex;
	}
	
	public @NonNull CommodityIndex createCommodityIndex(@NonNull final String name, final double commodityIndexPrice) {

		final DerivedIndex<Double> indexData = PricingFactory.eINSTANCE.createDerivedIndex();
		indexData.setExpression(Double.toString(commodityIndexPrice));

		final CommodityIndex commodityIndex = PricingFactory.eINSTANCE.createCommodityIndex();
		commodityIndex.setName(name);
		commodityIndex.setData(indexData);

		pricingModel.getCommodityIndices().add(commodityIndex);

		return commodityIndex;
	}
	
	public void clearPointsOnCommodityIndex(CommodityIndex commodityIndex) {
		DataIndex<Double> data = (DataIndex<Double>) commodityIndex.getData();
		data.getPoints().clear();
	}

	public void addDataToCommodityIndex(final CommodityIndex ci, final YearMonth date, final double value) {
		final DataIndex<Double> di = (DataIndex<Double>) ci.getData();
		final List<IndexPoint<Double>> points = di.getPoints();
		final IndexPoint<Double> ip = PricingFactory.eINSTANCE.createIndexPoint();
		ip.setDate(date);
		ip.setValue(value);
		points.add(ip);
	}

}
