/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Check the MMX port Locode matches the expected format (2 chars country, two spaces, then up to three characters or numbers)
 * 
 * @author Simon Goodall
 *
 */
public class CheckNearbyPorts {

	public static void main(final String[] args) {

		final String sourceData = "2023d";
		float threshold = 5.0f; // Miles

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

				for (var l : version.getLocations()) {

					String upstreamName = null;
					for (final var upstreamPort : upstreamPorts) {
						if (l.getUpstreamID().equals(upstreamPort.getCode())) {
							upstreamName = upstreamPort.getName();
							break;
						}
					}
					boolean printed = false;
					for (final var upstreamPort : upstreamPorts) {
						if (!l.getUpstreamID().equals(upstreamPort.getCode())) {
							var d = Util.distance(l.getGeographicPoint().getLon(), l.getGeographicPoint().getLat(), upstreamPort.getLon(), upstreamPort.getLatGeodetic(), 'M');
							if (d < threshold) {
								if (!printed) {
									System.out.println("--------------------------------"); // Separator
								}
								System.out.printf("%s (%s) has %s nearby (%.1fm)%n", l.getName(), upstreamName, upstreamPort.getName(), d);
								printed = true;
							}
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}
}
