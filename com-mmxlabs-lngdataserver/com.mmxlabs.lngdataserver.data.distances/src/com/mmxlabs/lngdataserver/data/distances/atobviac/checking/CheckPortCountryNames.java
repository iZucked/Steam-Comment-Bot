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
 * Check the MMX port country matches the country name mapping from A to B via C.
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortCountryNames {

	public static void main(final String[] args) {

		final String sourceData = "2023b";

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
					for (final var upstreamPort : upstreamPorts) {
						if (l.getUpstreamID().equals(upstreamPort.getCode())) {
							if (!Util.COUNTRY_CODE_MAP.get(upstreamPort.getCountryCode()).equals(l.getGeographicPoint().getCountry())) {
								System.out.println(
										"Country mismatch for " + l.getName() + ": " + Util.COUNTRY_CODE_MAP.get(upstreamPort.getCountryCode()) + " <-> " + l.getGeographicPoint().getCountry());
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
