/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.pricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurvePoint;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

public class PricingFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingFromScenarioCopier.class);

	private static Function<AbstractYearMonthCurve, ExpressionCurve> derivedTransformer = (idx) -> {
		ExpressionCurve out = new ExpressionCurve();
		out.setExpression(idx.getExpression());
		return out;
	};
	private static Function<AbstractYearMonthCurve, DataCurve> dataTransformer = (idx) -> {
		DataCurve out = new DataCurve();
		List<CurvePoint> pts = new ArrayList<>(idx.getPoints().size());
		for (YearMonthPoint pt : idx.getPoints()) {
			CurvePoint newPt = new CurvePoint();
			newPt.setDate(pt.getDate().atDay(1));
			newPt.setValue(BigDecimal.valueOf(pt.getValue()));
			pts.add(newPt);
		}
		out.setCurve(pts);
		return out;
	};

	private static BiFunction<AbstractYearMonthCurve, CurveType, Curve> curveTransformer = (idx, type) -> {
		Curve curve;
		if (idx.getExpression() != null) {
			curve = derivedTransformer.apply(idx);
		} else {
			curve = dataTransformer.apply(idx);
		}
		curve.setName(idx.getName());
		curve.setCurrency(idx.getCurrencyUnit());
		curve.setUnit(idx.getVolumeUnit());

		// The service needs a non-null value (actually null is fine, but jackson removes the parameter from the JSON and that is the problem);
		if (curve.getUnit() == null) {
			curve.setUnit("");
		}
		if (curve.getCurrency() == null) {
			curve.setCurrency("");
		}
		curve.setType(type);

		return curve;
	};

	public static PricingVersion generateVersion(PricingModel pricingModel) {

		PricingVersion version = new PricingVersion();

		pricingModel.getCurrencyCurves().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.CURRENCY);
			version.getCurves().put(Curve.encodedName(curve.getName()), curve);
		});

		pricingModel.getBunkerFuelCurves().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.BASE_FUEL);
			version.getCurves().put(Curve.encodedName(curve.getName()), curve);
		});

		pricingModel.getCharterCurves().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.CHARTER);
			version.getCurves().put(Curve.encodedName(curve.getName()), curve);
		});

		pricingModel.getCommodityCurves().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.COMMODITY);
			version.getCurves().put(Curve.encodedName(curve.getName()), curve);
		});

		String marketCurveDataVersion = pricingModel.getMarketCurveDataVersion();
		if (marketCurveDataVersion == null) {
			marketCurveDataVersion = EcoreUtil.generateUUID();
			pricingModel.setMarketCurveDataVersion(marketCurveDataVersion);
		}
		version.setIdentifier(marketCurveDataVersion);
		version.setCreatedAt(LocalDateTime.now());

		return version;
	}
}
