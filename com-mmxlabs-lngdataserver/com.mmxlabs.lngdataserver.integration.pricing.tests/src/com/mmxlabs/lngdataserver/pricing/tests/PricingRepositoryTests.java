/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.pricing.tests;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.pricing.IPricingProvider;
import com.mmxlabs.lngdataserver.integration.pricing.PricingRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class PricingRepositoryTests {

	static String BASE_URL = "http://localhost:8080/";

	@BeforeEach
	public void setup() {
		BackEndUrlProvider.INSTANCE.setPort(8080);
		BackEndUrlProvider.INSTANCE.setAvailable(true);
	}

	@AfterEach
	public void shutdown() {
		BackEndUrlProvider.INSTANCE.setAvailable(true);
		BackEndUrlProvider.INSTANCE.setPort(-1);
	}

	@Test
	public void getCurvesTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		Assertions.assertEquals(8, provider.getAvailableCurves().size());
	}

	@Test
	public void getCurveDataTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		List<Pair<LocalDate, Double>> data = provider.getData("HH");

		Assertions.assertEquals(6, data.size());
		Assertions.assertEquals(LocalDate.of(2017, 5, 1), data.get(0).getFirst());
		Assertions.assertEquals(10.905743478491804, data.get(0).getSecond(), 0.001);
	}

	@Test
	public void getExpressionTest() throws IOException {
		IPricingProvider provider = PricingRepository.INSTANCE.getLatestPrices();
		Assertions.assertEquals("95%HH", provider.getExpression("REL_HH"));
	}
}
