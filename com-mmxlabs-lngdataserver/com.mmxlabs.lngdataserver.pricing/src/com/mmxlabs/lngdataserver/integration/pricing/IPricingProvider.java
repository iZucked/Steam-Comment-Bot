package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.mmxlabs.common.Pair;

public interface IPricingProvider {
	String getVersion();
	List<String> getAvailableCurves();
	List<String> getCommodityCurves();
	List<String> getCurrencyCurves();
	List<String> getBaseFuelCurves();
	List<String> getCharterCurves();
	CurveType getCurveType(String curve);
	String getExpression(String curve) throws IOException;
	List<LocalDate> getAvailableDates(String curve);
	List<Pair<LocalDate, Double>> getData(String curve) throws IOException;
	double getValue(String curve, LocalDate date) throws IOException;
	String getCurrencyUnit(String curve);
	String getVolumeUnit(String curve);
}
