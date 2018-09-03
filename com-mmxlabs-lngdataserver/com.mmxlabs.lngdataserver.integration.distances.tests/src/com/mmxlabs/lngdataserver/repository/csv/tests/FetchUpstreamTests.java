/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.repository.csv.tests;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mmxlabs.lngdataserver.integration.distances.UpstreamDistancesFetcher;
import com.mmxlabs.lngdataserver.integration.distances.UpstreamPortFetcher;
import com.mmxlabs.lngdataserver.integration.distances.Via;
import com.mmxlabs.models.lng.port.Port;

public class FetchUpstreamTests {

	private static final String SERVICE_URL = "http://localhost:8089";

	private boolean initialized = false;
	// private final CsvDistanceRepository repo = new CsvDistanceRepository();

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(8089).usingFilesUnderClasspath("data"));
	//
	// @Before
	// public void loadDataTest() throws URISyntaxException {
	// if (initialized) {
	// return;
	// }
	//
	// File f = new File(getClass().getResource("/distances/tests/").toURI());
	// repo.initRepository(f);
	// initialized = true;
	//
	// // enable to test writing locally
	// // repo.initRepository(new File("C:/temp/repo/"));
	// repo.initRepository(new File("D:/workspace/lingo-ws/lingo-master/runtime-g.product/distance-repository"));
	// }


	@Test
	public void fetchDistancesTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		Map<Via, Map<String, Map<String, Double>>> distances = UpstreamDistancesFetcher.getDistances(SERVICE_URL, "user", "pw");
		assertEquals(Double.valueOf(15847.0), distances.get(Via.Direct).get("L_US_Sabin").get("L_JP_Himej"));
		assertEquals(Double.valueOf(9631.0), distances.get(Via.PanamaCanal).get("L_US_Sabin").get("L_JP_Himej"));
		assertEquals(Double.valueOf(14957.0), distances.get(Via.SuezCanal).get("L_US_Sabin").get("L_JP_Himej"));
	}

	@Test
	public void fetchPortsTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		List<Port> ports = UpstreamPortFetcher.getPorts(SERVICE_URL, "user", "pw");
		Optional<Port> potential = ports.stream().filter(e -> "Soyo".equals(e.getName())).findAny();
		assertTrue(potential.isPresent());
		assertEquals("Soyo Terminal", potential.get().getLocation().getOtherNames().get(0));
	}

}
