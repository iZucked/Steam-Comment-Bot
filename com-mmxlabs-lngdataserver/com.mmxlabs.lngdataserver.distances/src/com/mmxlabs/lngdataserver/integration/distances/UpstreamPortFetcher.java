package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.http.UrlFetcher;
import com.mmxlabs.common.json.JSONConverter;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class UpstreamPortFetcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamPortFetcher.class);

	private static final String PORTS_URL = "/locations";

	/**
	 * Returns the latest version of the ports
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 * @throws AuthenticationException
	 */
	public static List<Port> getPorts(String baseUrl, String username, String password) throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		return getPorts(baseUrl, null, username, password);
	}

	/**
	 * Returns a specific version of the ports. <b>Ports contain only name, aliases and MmxId</b>.
	 * 
	 * @param baseUrl
	 * @param version
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 * @throws AuthenticationException
	 */
	public static List<Port> getPorts(String baseUrl, String version, String username, String password) throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		List<Port> result = new ArrayList<>();

		String url = baseUrl + PORTS_URL;
		if (version != null) {
			url = url + "?v=" + version;
		}
		String rawJSON = UrlFetcher.fetchURLContent(url, username, password);
		JSONParser parser = new JSONParser();
		Object portsJSON = parser.parse(rawJSON);

		if (!(portsJSON instanceof JSONArray)) {
			LOGGER.error("Error parsing ports");
			throw new RuntimeException("Error parsing ports");
		}

		for (Object current : JSONConverter.toList((JSONArray) portsJSON)) {
			if (!(current instanceof Map)) {
				// skip
				LOGGER.info("Received invalid port");
				continue;
			}

			Map<String, Object> currentMap = (Map<String, Object>) current;

			if (!currentMap.containsKey("mmxId") || !(currentMap.get("mmxId") instanceof String)) {
				LOGGER.info("Port does not contain a mmxId");
				continue;
			}

			if (!currentMap.containsKey("name") || !(currentMap.get("name") instanceof String)) {
				LOGGER.info("Port does not contain a name");
				continue;
			}

			Port p = PortFactory.eINSTANCE.createPort();
			Location l = PortFactory.eINSTANCE.createLocation();
			p.setLocation(l);
			result.add(p);

			p.setName((String) currentMap.get("name"));
			l.setName((String) currentMap.get("name"));
			l.setMmxId((String) currentMap.get("mmxId"));
			Object geographicPoint = currentMap.get("geographicPoint");
			if (geographicPoint instanceof Map) {
				l.setCountry((String) ((Map) geographicPoint).get("country"));
				l.setLat((Double) ((Map) geographicPoint).get("lat"));
				l.setLon((Double) ((Map) geographicPoint).get("lon"));
				l.setTimeZone((String) ((Map) geographicPoint).get("timeZone"));
			}
			if (currentMap.containsKey("aliases") && currentMap.get("aliases") instanceof JSONArray) {
				((JSONArray) currentMap.get("aliases")).forEach(a -> l.getOtherNames().add((String) a));
			}
		}

		return result;
	}

}
