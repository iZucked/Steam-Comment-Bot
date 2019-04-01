/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.pricing.tests;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.pricing.PricingClient;
import com.mmxlabs.lngdataserver.integration.pricing.PricingVersion;

public class ClientTests extends AbstractTest {

	private int port = getPort();
	private String BASE_URL = "http://localhost:" + port + "/";

	private WireMockServer wireMockServer;

	@BeforeEach
	void configureSystemUnderTest() {
		this.wireMockServer = new WireMockServer(options() //
				.bindAddress("127.0.0.1") //
				.port(getPort()) //
				.usingFilesUnderClasspath("data")

		);
		this.wireMockServer.start();
	}

	@AfterEach
	void stopWireMockServer() {
		this.wireMockServer.stop();
	}

	@Test
	public void getVersionTest() throws IOException {
		List<PricingVersion> result = PricingClient.getVersions(BASE_URL);
		Assertions.assertEquals("ba38bae0-4849-4a99-9449-54685b4c832d", result.get(0).getIdentifier());
	}

	@Test
	public void getCurvesTest() throws IOException {
		String latest = PricingClient.getVersions(BASE_URL).get(0).getIdentifier();
		List<Curve> charterCurves = PricingClient.getCharterCurves(BASE_URL, latest);
		List<Curve> currencyCurves = PricingClient.getCurrencyCurves(BASE_URL, latest);
		List<Curve> baseFuelCurves = PricingClient.getFuelCurves(BASE_URL, latest);
		List<Curve> commodityCurves = PricingClient.getCommodityCurves(BASE_URL, latest);

		Assertions.assertEquals(1, charterCurves.size());
		Assertions.assertEquals("BUNKER_GLOBAL_HSFO", charterCurves.get(0).getName());

		Assertions.assertEquals(5, commodityCurves.size());

		Assertions.assertEquals(1, baseFuelCurves.size());

		Assertions.assertEquals(2, currencyCurves.size());
	}

	@Test
	public void polymorphismTest() throws IOException {
		String latest = PricingClient.getVersions(BASE_URL).get(0).getIdentifier();
		DataCurve dataCurve = PricingClient.getCurve(BASE_URL, latest, "HH", DataCurve.class);
		ExpressionCurve expressionCurve = PricingClient.getCurve(BASE_URL, latest, "REL_HH", ExpressionCurve.class);
		List<Curve> commodityCurves = PricingClient.getCommodityCurves(BASE_URL, latest);
		Assertions.assertEquals(true, commodityCurves.stream().filter(e -> e.getName().equals("HH")).findFirst().get() instanceof DataCurve);
		Assertions.assertEquals(true, commodityCurves.stream().filter(e -> e.getName().equals("REL_HH")).findFirst().get() instanceof ExpressionCurve);
	}
}
