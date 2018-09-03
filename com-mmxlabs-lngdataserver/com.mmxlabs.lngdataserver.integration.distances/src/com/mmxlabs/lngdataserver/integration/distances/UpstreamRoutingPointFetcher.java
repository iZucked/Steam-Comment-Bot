/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
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

public class UpstreamRoutingPointFetcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamRoutingPointFetcher.class);

	private static final String CANALS_URL = "/canals";

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
	public static List<CanalInfo> getRoutingPoints(final String baseUrl, final String username, final String password) throws IOException, ParseException, AuthenticationException {
		return getRoutingPoints(baseUrl, null, username, password);
	}

	public static final class CanalInfo {

		public final String rpName;
		public final String northernEntryId;;
		public final String southernEntryId;
		public final String virtualLocationId;

		public CanalInfo(final String rpName, final String northernEntryId, final String southernEntryId, final String virtualLocationId) {
			this.rpName = rpName;
			this.northernEntryId = northernEntryId;
			this.southernEntryId = southernEntryId;
			this.virtualLocationId = virtualLocationId;

		}
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
	public static List<CanalInfo> getRoutingPoints(final String baseUrl, final String version, final String username, final String password)
			throws IOException, ParseException, AuthenticationException {
		final List<CanalInfo> result = new ArrayList<>();

		String url = baseUrl + CANALS_URL;
		if (version != null) {
			url = url + "?v=" + version;
		}
		final String rawJSON = UrlFetcher.fetchURLContent(url, username, password);
		final JSONParser parser = new JSONParser();
		final Object portsJSON = parser.parse(rawJSON);

		if (!(portsJSON instanceof JSONArray)) {
			LOGGER.error("Error parsing canals");
			throw new RuntimeException("Error parsing canals");
		}
		// look at JSON, convert into triple!
		for (final Object current : JSONConverter.toList((JSONArray) portsJSON)) {
			if (!(current instanceof Map)) {
				// skip
				LOGGER.info("Received invalid canal");
				continue;
			}

			final Map<String, Object> currentMap = (Map<String, Object>) current;
			final String rpName = (String) currentMap.get("identifier");
			final String north = (String) currentMap.get("northernEntry");
			final String south = (String) currentMap.get("southernEntry");
			final String virtualId = (String) ((Map) currentMap.get("virtualLocation")).get("mmxId");

			result.add(new CanalInfo(rpName, north, south, virtualId));
		}

		return result;
	}

}
