/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.analysis;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class PortCount {

	public static void main(String[] args) throws Exception {

		// Input data
		final String sourceData = "2023d";

		Util.withService(service -> {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {

				final PortDistanceVersion version = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);
				System.out.println("Ports: " + version.getLocations().size());
				final Set<String> upstreamPorts = new HashSet<>();
				// upstreamPorts.
				for (var l : version.getLocations()) {
					upstreamPorts.add(l.getUpstreamID());
					upstreamPorts.add(l.getFallbackUpstreamID());
				}
				upstreamPorts.remove(null);
				System.out.println("Upstream Is: " + upstreamPorts.size()); 
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}
}
