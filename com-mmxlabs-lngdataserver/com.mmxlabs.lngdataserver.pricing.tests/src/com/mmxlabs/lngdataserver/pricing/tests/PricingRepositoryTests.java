package com.mmxlabs.lngdataserver.pricing.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.pricing.IPricingProvider;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class PricingRepositoryTests {

	static String BASE_URL = "http://localhost:8080/";

	@Before
	public void setup() {
		BackEndUrlProvider.INSTANCE.setPort(8080);
		BackEndUrlProvider.INSTANCE.setAvailable(true);
	}

	@After
	public void shutdown() {
		BackEndUrlProvider.INSTANCE.setAvailable(true);
		BackEndUrlProvider.INSTANCE.setPort(-1);
	}

	@Test
	public void getCurvesTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		assertEquals(8, provider.getAvailableCurves().size());
	}

	@Test
	public void getCurveDataTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		List<Pair<LocalDate, Double>> data = provider.getData("HH");

		assertEquals(6, data.size());
		assertEquals(LocalDate.of(2017, 5, 1), data.get(0).getFirst());
		assertEquals(10.905743478491804, data.get(0).getSecond(), 0.001);
	}

	@Test
	public void getExpressionTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		assertEquals("95%HH", provider.getExpression("REL_HH"));
	}
}
