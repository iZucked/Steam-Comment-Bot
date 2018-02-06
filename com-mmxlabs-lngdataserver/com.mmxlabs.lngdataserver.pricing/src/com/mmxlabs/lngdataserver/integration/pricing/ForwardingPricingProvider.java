package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.pricing.exceptions.CurveNotFoundException;
import com.mmxlabs.lngdataserver.integration.pricing.exceptions.DateNotFoundException;
import com.mmxlabs.lngdataserver.integration.pricing.exceptions.InvalidCurveTypeException;
import com.mmxlabs.lngdataservice.pricing.model.Curve;
import com.mmxlabs.lngdataservice.pricing.model.CurvePoint;
import com.mmxlabs.lngdataservice.pricing.model.DataCurve;
import com.mmxlabs.lngdataservice.pricing.model.ExpressionCurve;

public class ForwardingPricingProvider implements IPricingProvider{
	
	private final String backendUrl;
	
	private final String version;
	private final List<Curve> commodityCurves;
	private final List<Curve> charterCurves;
	private final List<Curve> baseFuelCurves;
	private final List<Curve> currencyCurves;
	
	private final Map<String, Curve> curveIndex = new HashMap<String, Curve>();
	
	public ForwardingPricingProvider(String backendUrl, String version, List<Curve> commodityCurves, List<Curve> charterCurves, List<Curve> baseFuelCurves, List<Curve> currencyCurves) {
		
		this.backendUrl = backendUrl;
		
		this.version = version;
		this.commodityCurves = commodityCurves;
		this.commodityCurves.forEach(c -> curveIndex.put(c.getName(), c));
		
		this.charterCurves = charterCurves;
		this.charterCurves.forEach(c -> curveIndex.put(c.getName(), c));
		
		this.baseFuelCurves = baseFuelCurves;
		this.baseFuelCurves.forEach(c -> curveIndex.put(c.getName(), c));
		
		this.currencyCurves = currencyCurves;
		this.currencyCurves.forEach(c -> curveIndex.put(c.getName(), c));
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public List<String> getAvailableCurves() {
		return curveIndex.values().stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	@Override
	public List<LocalDate> getAvailableDates(String curve) {
		check(curve);
		if (curveIndex.get(curve) instanceof ExpressionCurve) {
			throw new InvalidCurveTypeException("Given curve is an expression curve, not a data curve"); 
		}
		return ((DataCurve)curveIndex.get(curve)).getCurve().stream().map(p -> p.getDate()).collect(Collectors.toList());
	}

	@Override
	public String getCurrencyUnit(String curve) {
		check(curve);
		return curveIndex.get(curve).getCurrency();
	}

	@Override
	public String getVolumeUnit(String curve) {
		check(curve);
		return curveIndex.get(curve).getUnit();
	}

	@Override
	public List<String> getCommodityCurves() {
		return commodityCurves.stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	@Override
	public List<String> getCurrencyCurves() {
		return currencyCurves.stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	@Override
	public List<String> getBaseFuelCurves() {
		return baseFuelCurves.stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	@Override
	public List<String> getCharterCurves() {
		return charterCurves.stream().map(c -> c.getName()).collect(Collectors.toList());
	}

	@Override
	public CurveType getCurveType(String curve) {
		check(curve);
		if (curveIndex.get(curve) instanceof DataCurve) {
			return CurveType.DataCurve;
		}else {
			return CurveType.ExpressionCurve;
		}
	}

	@Override
	public String getExpression(String curve) throws IOException {
		requireExpressionCurve(curve);
		
		ExpressionCurve expressionCurve = PricingClient.getCurve(backendUrl, version, curve, ExpressionCurve.class);
		return expressionCurve.getExpression();
	}

	@Override
	public double getValue(String curve, LocalDate date) throws IOException {
		requireDataCurve(curve);
		
		DataCurve dataCurve = PricingClient.getCurve(backendUrl, version, curve, DataCurve.class);
		
		Optional<CurvePoint> potential = dataCurve.getCurve().stream().filter(p -> (p.getDate().compareTo(date) == 0)).findFirst();
		if (!potential.isPresent()) {
			throw new DateNotFoundException("Date not found in curve");
		}else {
			return potential.get().getValue().doubleValue();
		}
	}
	
	@Override
	public List<Pair<LocalDate, Double>> getData(String curve) throws IOException {
		requireDataCurve(curve);
		
		DataCurve dataCurve = PricingClient.getCurve(backendUrl, version, curve, DataCurve.class);
		
		return dataCurve.getCurve().stream()
				.map(curvePoint -> new Pair(curvePoint.getDate(), curvePoint.getValue().doubleValue()))
				.collect(Collectors.toList());
	}
	
	private void requireDataCurve(String curve) {
		check(curve);
		if (!(curveIndex.get(curve) instanceof DataCurve)) {
			throw new InvalidCurveTypeException("Given curve not a data curve"); 
		}
	}
	
	private void requireExpressionCurve(String curve) {
		check(curve);
		if (!(curveIndex.get(curve) instanceof ExpressionCurve)) {
			throw new InvalidCurveTypeException("Given curve not an expression curve"); 
		}
	}
	
	private void check(String curve) {
		if (!curveIndex.containsKey(curve)) {
			throw new CurveNotFoundException("No curve found with name " + curve);
		}
	}


}
