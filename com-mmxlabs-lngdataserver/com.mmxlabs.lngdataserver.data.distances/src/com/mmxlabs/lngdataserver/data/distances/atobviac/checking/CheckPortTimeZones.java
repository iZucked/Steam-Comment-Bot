/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Check the MMX port timezone y matches the mapping from A to B via C. Note - the A to B via C mapping is usually less exact (it uses a city in the same timezone rather than the nearest city)
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortTimeZones {

	private static final String API_KEY = "39XADZRH0R2R"; // API Key from Ryan?

	public static void main(final String[] args) {

		boolean use_atobvaic = false;
		boolean use_timezonedb_com = true; // External service, rate limited

		final String sourceData = "2023d";

		Util.withService(service -> {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {
				Set<String> newPorts = new HashSet<>();

				final PortDistanceVersion version = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);

				for (var l : version.getLocations()) {
					if (!newPorts.contains(l.getUpstreamID())) {
						newPorts.add(l.getUpstreamID());
					}
				}

				final List<AtoBviaCPort> upstreamPorts;
				File portCache = new File("port-cache.json");
				if (portCache.exists()) {
					upstreamPorts = mapper.readerForListOf(AtoBviaCPort.class).readValue(portCache);
				} else {
					upstreamPorts = service.getAtoBviaCAdapter().getPortsBlocking();
					mapper.writerWithDefaultPrettyPrinter().writeValue(portCache, upstreamPorts);
				}

				try (CloseableHttpClient client = HttpClients.createDefault()) {
					for (var l : version.getLocations()) {
						for (final var upstreamPort : upstreamPorts) {
							if (l.getUpstreamID().equals(upstreamPort.getCode())) {

								String ours = l.getGeographicPoint().getTimeZone();
								if (use_atobvaic) {
									String theirs = upstreamPort.getTimezone().getTimezoneId();
									if (!ours.equals(theirs)) {
										System.out.println("Timezone mismatch for " + l.getName() + ": " + ours + " <-> " + theirs);
									}
								}

								if (use_timezonedb_com) {
									HttpGet request = new HttpGet(String.format("https://api.timezonedb.com/2.1/get-time-zone?key=%s&format=json&by=position&lat=%f&lng=%f", //
											API_KEY, 
											l.getGeographicPoint().getLat(), //
											l.getGeographicPoint().getLon()));

									try (var response = client.execute(request)) {
										// Limited to one request a second
										Thread.sleep(2_000);
										// Threas.sleep(1_000)
										String str = EntityUtils.toString(response.getEntity());
										try {
											var respo = mapper.readValue(str, TZResponse.class);
											String theirs = respo.zoneName;
											if (!ours.equals(theirs)) {
												System.out.println("DB Timezone mismatch for " + l.getName() + ": " + ours + " <-> " + theirs);
											}
										} catch (Exception e) {
											int ii = 0;
										}
									}

								}

								break;
							}
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	static class TZResponse {
		public String status;
		public String message;
		public String countryCode;
		public String countryName;
		public String regionName;
		public String cityName;
		public String zoneName;
		public String abbreviation;
		public int gmtOffset;
		public int dst;
		public Long zoneStart;
		public Long zoneEnd;
		public String nextAbbreviation;
		public long timestamp;
		public String formatted;
	}
}
