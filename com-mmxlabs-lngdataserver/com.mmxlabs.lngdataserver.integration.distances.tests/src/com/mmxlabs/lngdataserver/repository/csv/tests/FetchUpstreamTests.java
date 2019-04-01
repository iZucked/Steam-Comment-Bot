/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.repository.csv.tests;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.mmxlabs.lngdataserver.integration.distances.UpstreamDistancesFetcher;
import com.mmxlabs.lngdataserver.integration.distances.UpstreamPortFetcher;
import com.mmxlabs.lngdataserver.integration.distances.Via;
import com.mmxlabs.models.lng.port.Port;

public class FetchUpstreamTests {

	private WireMockServer wireMockServer;

	@BeforeEach
	void configureSystemUnderTest() {
		this.wireMockServer = new WireMockServer(options() //
				.bindAddress("127.0.0.1") //
				.port(8089) //
				.usingFilesUnderClasspath("data"));
		this.wireMockServer.start();
	}

	@AfterEach
	void stopWireMockServer() {
		this.wireMockServer.stop();
	}

	private static final String SERVICE_URL = "http://localhost:8089";

	private boolean initialized = false;

	@Test
	public void fetchDistancesTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {

		Map<Via, Map<String, Map<String, Integer>>> distances = UpstreamDistancesFetcher.getDistances(SERVICE_URL, "user", "pw");
		Assertions.assertEquals(Integer.valueOf(15847), distances.get(Via.Direct).get("L_US_Sabin").get("L_JP_Himej"));
		Assertions.assertEquals(Integer.valueOf(9631), distances.get(Via.PanamaCanal).get("L_US_Sabin").get("L_JP_Himej"));
		Assertions.assertEquals(Integer.valueOf(14957), distances.get(Via.SuezCanal).get("L_US_Sabin").get("L_JP_Himej"));
	}

	@Test
	public void fetchPortsTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		List<Port> ports = UpstreamPortFetcher.getPorts(SERVICE_URL, "user", "pw");
		Optional<Port> potential = ports.stream().filter(e -> "Soyo".equals(e.getName())).findAny();
		Assertions.assertTrue(potential.isPresent());
		Assertions.assertEquals("Soyo Terminal", potential.get().getLocation().getOtherNames().get(0));
	}

}
