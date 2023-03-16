/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Check the MMX port Locode matches the expected format (2 chars country, two spaces, then up to three characters or numbers)
 * 
 * @author Simon Goodall
 *
 */
public class CheckLocodeFormat {

	public static void main(final String[] args) {

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

				for (var l : version.getLocations()) {
					if (l.getLocode() != null) {
						if (!l.getLocode().matches("[A-Z][A-Z]  [A-Z0-9]{2,3}")) {
							System.out.printf("%s locode of %s does not match expected format%n", l.getName(), l.getLocode());
						}
					}

				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
