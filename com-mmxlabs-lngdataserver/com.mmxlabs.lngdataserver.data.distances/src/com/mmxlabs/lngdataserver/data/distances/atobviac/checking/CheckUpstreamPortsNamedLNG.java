package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Looks for upstream ports with LNG in the name and report ones which we are not using.
 * 
 * @author Simon Goodall
 *
 */
public class CheckUpstreamPortsNamedLNG {

	public static void main(final String[] args) {

		final String sourceData = "2023d";

		Util.withService(service -> {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {

				Map<String, AtoBviaCPort> codes = new HashMap<>();

				final List<AtoBviaCPort> upstreamPorts;
				File portCache = new File("port-cache.json");
				if (portCache.exists()) {
					upstreamPorts = mapper.readerForListOf(AtoBviaCPort.class).readValue(portCache);
				} else {
					upstreamPorts = service.getAtoBviaCAdapter().getPortsBlocking();
					mapper.writerWithDefaultPrettyPrinter().writeValue(portCache, upstreamPorts);
				}

				for (var upstreamPort : upstreamPorts) {
					if (upstreamPort.getName().contains("LNG")) {
						// System.out.printf("%s (%s)%n", upstreamPort.getName(), upstreamPort.getCode());
						codes.put(upstreamPort.getCode(), upstreamPort);
					} else if (upstreamPort.getAliases() != null) {
						for (String alias : upstreamPort.getAliases()) {
							if (alias.contains("LNG")) {
								// System.out.printf("%s (%s - %s)%n", alias, upstreamPort.getName(), upstreamPort.getCode());
								codes.put(upstreamPort.getCode(), upstreamPort);
								break;
							}
						}
					}
				}

				final PortDistanceVersion version = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);

				for (var l : version.getLocations()) {
					codes.remove(l.getUpstreamID());
				}

				for (var upstreamPort : codes.values()) {
					if (upstreamPort.getName().contains("LNG")) {
						System.out.printf("%s (%s)%n", upstreamPort.getName(), upstreamPort.getCode());
					} else if (upstreamPort.getAliases() != null) {
						for (String alias : upstreamPort.getAliases()) {
							if (alias.contains("LNG")) {
								System.out.printf("%s (%s - %s)%n", alias, upstreamPort.getName(), upstreamPort.getCode());
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
}
