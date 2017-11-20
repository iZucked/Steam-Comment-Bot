package com.mmxlabs.lngdataserver.pricing.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import com.mmxlabs.lngdataserver.pricing.PricingRepository;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.pricing.IPricingProvider;

public class PricingRepositoryTests {
	
	static String BASE_URL = "http://localhost:8080/";
	
	@Test
	public void getCurvesTest() throws IOException {
		IPricingProvider provider = new PricingRepository(BASE_URL).getLatestPrices();
		assertEquals(8, provider.getAvailableCurves().size());
	}
	
	@Test
	public void getCurveDataTest() throws IOException {
		IPricingProvider provider = new PricingRepository(BASE_URL).getLatestPrices();
		List<Pair<LocalDate, Double>> data = provider.getData("HH");
		
		assertEquals(6, data.size());
		assertEquals(LocalDate.of(2017, 5, 1), data.get(0).getFirst());
		assertEquals(10.905743478491804, data.get(0).getSecond(), 0.001);
	}
	
	@Test
	public void getExpressionTest() throws IOException {
		IPricingProvider provider = new PricingRepository(BASE_URL).getLatestPrices();
		assertEquals("95%HH", provider.getExpression("REL_HH"));
	}
}
