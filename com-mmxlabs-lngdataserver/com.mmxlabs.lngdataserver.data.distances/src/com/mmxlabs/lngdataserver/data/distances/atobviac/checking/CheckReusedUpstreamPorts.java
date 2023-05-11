/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * This tool checks to see if we have re-used the upstream ports for multiple ports definitions. This may indicate checking for a better upstream alternative
 * 
 * @author Simon Goodall
 *
 */
public class CheckReusedUpstreamPorts {

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

				Map<String, List<String>> upstreamID = new HashMap<>();

				for (var l : version.getLocations()) {
					upstreamID.computeIfAbsent(l.getUpstreamID(), k -> new LinkedList<>()).add(l.getName());
				}
				for (var e : upstreamID.entrySet()) {
					if (e.getValue().size() > 1) {
						System.out.printf("%s - used for %s%n", e.getKey(), e.getValue().stream().collect(Collectors.joining(", ")));
					}
				}

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
