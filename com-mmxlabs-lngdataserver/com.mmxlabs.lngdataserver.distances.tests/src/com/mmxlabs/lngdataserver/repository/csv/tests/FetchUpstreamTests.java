package com.mmxlabs.lngdataserver.repository.csv.tests;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mmxlabs.lngdataserver.distances.UpstreamDistancesFetcher;
import com.mmxlabs.lngdataserver.distances.UpstreamPortFetcher;
import com.mmxlabs.lngdataserver.distances.Via;
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
	public void updateAvailableTest() throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		List<String> localVersions = new ArrayList(2);
		localVersions.add("foo");
		localVersions.add("bar");

		assertEquals(true, UpstreamDistancesFetcher.checkForUpdates(localVersions, SERVICE_URL, "user", "pw"));
	}

	@Test
	public void updateUnavailableTest() throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		List<String> localVersions = new ArrayList(3);
		localVersions.add("foo");
		localVersions.add("v1.0.5.153_1");
		localVersions.add("bar");

		assertEquals(false, UpstreamDistancesFetcher.checkForUpdates(localVersions, SERVICE_URL, "user", "pw"));
	}

	@Test
	public void fetchDistancesTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		Map<Via, Map<String, Map<String, Integer>>> distances = UpstreamDistancesFetcher.getDistances(SERVICE_URL, "user", "pw");
		assertEquals(Integer.valueOf(15847), distances.get(Via.Direct).get("L_US_Sabin").get("L_JP_Himej"));
		assertEquals(Integer.valueOf(9631), distances.get(Via.PanamaCanal).get("L_US_Sabin").get("L_JP_Himej"));
		assertEquals(Integer.valueOf(14957), distances.get(Via.SuezCanal).get("L_US_Sabin").get("L_JP_Himej"));
	}

	@Test
	public void fetchPortsTest() throws AuthenticationException, ClientProtocolException, IOException, ParseException {
		List<Port> ports = UpstreamPortFetcher.getPorts(SERVICE_URL, "user", "pw");
		Optional<Port> potential = ports.stream().filter(e -> "Soyo".equals(e.getName())).findAny();
		assertTrue(potential.isPresent());
		assertEquals("Soyo Terminal", potential.get().getLocation().getOtherNames().get(0));
	}

}
