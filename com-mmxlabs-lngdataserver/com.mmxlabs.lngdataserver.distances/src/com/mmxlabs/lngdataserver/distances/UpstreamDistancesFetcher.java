package com.mmxlabs.lngdataserver.distances;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Joiner;
import com.mmxlabs.common.http.UrlFetcher;
import com.mmxlabs.common.json.JSONConverter;

public class UpstreamDistancesFetcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamDistancesFetcher.class);

	private static Map<Via, String> routeOpt;
	private static final String DISTANCES_URL = "/distances";
	private static final String VERSIONS_URL = "/distances/versions";
	static {
		Map<Via, String> map = new HashMap<>();

		map.put(Via.Direct, "");
		map.put(Via.PanamaCanal, "open=PAN");
		map.put(Via.SuezCanal, "open=SUZ");

		routeOpt = Collections.unmodifiableMap(map);
	}

	/**
	 * Returns the latest version of the distances
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 * @throws AuthenticationException
	 */
	public static Map<Via, Map<String, Map<String, Integer>>> getDistances(String baseUrl, String username, String password) throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		return getDistances(baseUrl, null, username, password);
	}
	
	
	/**
	 * Returns a specific version of the distances
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
	public static Map<Via, Map<String, Map<String, Integer>>> getDistances(String baseUrl, String version, String username, String password) throws ClientProtocolException, IOException, ParseException, AuthenticationException {
		Map<Via, Map<String, Map<String, Integer>>> result = new EnumMap<>(Via.class);

		
		for (Entry<Via, String> ro : routeOpt.entrySet()) {
			List<String> requestArgs = new LinkedList<>();
			if (!ro.getValue().isEmpty()) {
				requestArgs.add(ro.getValue());
			}
			requestArgs.add("v=" + version);
			String url = baseUrl + DISTANCES_URL + "?" + Joiner.on("&").join(requestArgs) ;
			
			String rawJSON = UrlFetcher.fetchURLContent(url, username, password);
			JSONParser parser = new JSONParser();

			Object distances = parser.parse(rawJSON);

			if (!(distances instanceof JSONObject)) {
				throw new RuntimeException("Response from server was not an object");
			}
			result.put(ro.getKey(), createMatrix(JSONConverter.toMap((JSONObject) distances)));
		}
		return result;
	}

	private static Map<String, Map<String, Integer>> createMatrix(Map<String, Object> distancesMap) {
		Map<String, Map<String, Integer>> routeMap = new HashMap<String, Map<String, Integer>>();
		for (Entry<String, Object> src : distancesMap.entrySet()) {
			Map<String, Object> destinations = (Map<String, Object>) src.getValue();
			routeMap.put(src.getKey(), new HashMap<String, Integer>());

			for (Entry<String, Object> dst : destinations.entrySet()) {
				String distStr = (String) dst.getValue();
				try {
					float dist = Float.parseFloat(distStr);
					routeMap.get(src.getKey()).put(dst.getKey(), Math.round(dist));
				} catch (NumberFormatException e) {
					// too many occurrences for logging
					// LOGGER.info("Could not parse distance" + src.getKey() + " > " + dst.getKey() + ": " + distStr);
				}
			}
		}
		return routeMap;
	}
}
