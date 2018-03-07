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

import com.mmxlabs.lngdataserver.integration.client.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.CurvePoint;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.CurveType;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class PricingFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingFromScenarioCopier.class);

	private static Function<DerivedIndex<?>, ExpressionCurve> derivedTransformer = (idx) -> {
		ExpressionCurve out = new ExpressionCurve();
		out.setExpression(idx.getExpression());
		return out;
	};
	private static Function<DataIndex<?>, DataCurve> dataTransformer = (idx) -> {
		DataCurve out = new DataCurve();
		List<CurvePoint> pts = new ArrayList<>(idx.getPoints().size());
		for (IndexPoint<?> pt : idx.getPoints()) {
			CurvePoint newPt = new CurvePoint();
			newPt.setDate(pt.getDate().atDay(1));
			newPt.setValue(BigDecimal.valueOf(((Number) pt.getValue()).doubleValue()));
			pts.add(newPt);
		}
		out.setCurve(pts);
		return out;
	};

	private static BiFunction<NamedIndexContainer<?>, CurveType, Curve> curveTransformer = (idx, type) -> {
		Index<?> data = idx.getData();
		Curve curve;
		if (data instanceof DerivedIndex<?>) {
			curve = derivedTransformer.apply((DerivedIndex<?>) data);
		} else if (data instanceof DataIndex<?>) {
			curve = dataTransformer.apply((DataIndex<?>) data);
		} else {
			return null;
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

	public static Version generateVersion(PricingModel pricingModel) {

		Version version = new Version();

		pricingModel.getCurrencyIndices().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.CURRENCY);
			version.getCurves().put(curve.getName(), curve);
		});

		pricingModel.getBaseFuelPrices().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.BASE_FUEL);
			version.getCurves().put(curve.getName(), curve);
		});

		pricingModel.getCharterIndices().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.CHARTER);
			version.getCurves().put(curve.getName(), curve);
		});

		pricingModel.getCommodityIndices().forEach(idx -> {
			Curve curve = curveTransformer.apply(idx, CurveType.COMMODITY);
			version.getCurves().put(curve.getName(), curve);
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
