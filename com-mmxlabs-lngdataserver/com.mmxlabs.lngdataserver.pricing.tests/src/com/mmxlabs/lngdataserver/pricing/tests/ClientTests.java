package com.mmxlabs.lngdataserver.pricing.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mmxlabs.lngdataserver.integration.pricing.PricingClient;
import com.mmxlabs.lngdataserver.integration.pricing.PricingVersion;
import com.mmxlabs.lngdataservice.pricing.model.Curve;
import com.mmxlabs.lngdataservice.pricing.model.DataCurve;
import com.mmxlabs.lngdataservice.pricing.model.ExpressionCurve;

public class ClientTests extends AbstractTest {

	private int port = getPort();
	private String BASE_URL = "http://localhost:" + port + "/";
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(port).usingFilesUnderClasspath("data"));
	
	@Test
	public void getVersionTest() throws IOException {
		List<PricingVersion> result = PricingClient.getVersions(BASE_URL);
		assertEquals("ba38bae0-4849-4a99-9449-54685b4c832d", result.get(0).getIdentifier());
	}
	
	@Test
	public void getCurvesTest() throws IOException {
		String latest = PricingClient.getVersions(BASE_URL).get(0).getIdentifier();
		List<Curve> charterCurves = PricingClient.getCharterCurves(BASE_URL, latest);
		List<Curve> currencyCurves = PricingClient.getCurrencyCurves(BASE_URL, latest);
		List<Curve> baseFuelCurves = PricingClient.getFuelCurves(BASE_URL, latest);
		List<Curve> commodityCurves = PricingClient.getCommodityCurves(BASE_URL, latest);
		
		assertEquals(1, charterCurves.size());
		assertEquals("BUNKER_GLOBAL_HSFO", charterCurves.get(0).getName());
		
		assertEquals(5, commodityCurves.size());
		
		assertEquals(1, baseFuelCurves.size());
		
		assertEquals(2, currencyCurves.size());
	}
	
	@Test
	public void polymorphismTest() throws IOException {
		String latest = PricingClient.getVersions(BASE_URL).get(0).getIdentifier();
		DataCurve dataCurve = PricingClient.getCurve(BASE_URL, latest, "HH", DataCurve.class);
		ExpressionCurve expressionCurve = PricingClient.getCurve(BASE_URL, latest, "REL_HH", ExpressionCurve.class);
		List<Curve> commodityCurves = PricingClient.getCommodityCurves(BASE_URL, latest);
		assertEquals(true, commodityCurves.stream().filter(e -> e.getName().equals("HH")).findFirst().get() instanceof DataCurve);
		assertEquals(true, commodityCurves.stream().filter(e -> e.getName().equals("REL_HH")).findFirst().get() instanceof ExpressionCurve);
	}
}
