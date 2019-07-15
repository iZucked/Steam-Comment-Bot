/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

public class PricingModelBuilder {

	private final @NonNull PricingModel pricingModel;

	public PricingModelBuilder(@NonNull final PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@NonNull
	public CharterCurve createCharterCurve(@NonNull final String name, @Nullable final String currencyUnit, @Nullable final String volumeUnit, final int fixedPricePerDay) {

		final CharterCurve curve = PricingFactory.eINSTANCE.createCharterCurve();
		curve.setName(name);
		if (currencyUnit != null) {
			curve.setCurrencyUnit(currencyUnit);
		}
		if (volumeUnit != null) {
			curve.setVolumeUnit(volumeUnit);
		}

		curve.setExpression(Integer.toString(fixedPricePerDay));

		return curve;
	}

	public @NonNull BunkerFuelCurve createBaseFuelExpressionIndex(@NonNull final String name, final double baseFuelUnitPrice) {

		final BunkerFuelCurve curve = PricingFactory.eINSTANCE.createBunkerFuelCurve();
		curve.setName(name);
		curve.setExpression(Double.toString(baseFuelUnitPrice));

		pricingModel.getBunkerFuelCurves().add(curve);

		return curve;
	}

	public @NonNull CommodityCurve createCommodityIndex(@NonNull final String name, final double commodityIndexPrice) {

		final CommodityCurve curve = PricingFactory.eINSTANCE.createCommodityCurve();
		curve.setName(name);
		curve.setExpression(Double.toString(commodityIndexPrice));

		pricingModel.getCommodityCurves().add(curve);

		return curve;
	}

	public void clearPointsOnCommodityIndex(CommodityCurve curve) {
		curve.getPoints().clear();
	}

	public void addDataToCommodityIndex(final AbstractYearMonthCurve curve, final YearMonth date, final double value) {
		final YearMonthPoint pt = PricingFactory.eINSTANCE.createYearMonthPoint();
		pt.setDate(date);
		pt.setValue(value);
		curve.getPoints().add(pt);
	}

	public DataCurveBuilder<CommodityCurve> makeCommodityDataCurve(String name, String currencyUnit, String volumeUnit) {
		final CommodityCurve curve = PricingFactory.eINSTANCE.createCommodityCurve();
		curve.setName(name);
		curve.setCurrencyUnit(currencyUnit);
		curve.setVolumeUnit(volumeUnit);

		return new DataCurveBuilder<CommodityCurve>(curve, idx -> {
			pricingModel.getCommodityCurves().add(idx);
		});
	}
	
	public DataCurveBuilder<CharterCurve> makeCharterDataCurve(String name, String currencyUnit, String volumeUnit) {
		final CharterCurve curve = PricingFactory.eINSTANCE.createCharterCurve();
		curve.setName(name);
		curve.setCurrencyUnit(currencyUnit);
		curve.setVolumeUnit(volumeUnit);

		return new DataCurveBuilder<CharterCurve>(curve, idx -> {
			pricingModel.getCharterCurves().add(idx);
		});
	}

}
