package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Check the MMX port names matches the A to B via C name. While we often use a more common port name or strip out excess elements, it may indicate a bad mapping.
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortNames {

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

							String ours = l.getName();
							String theirs = upstreamPort.getName();
							if (!ours.equals(theirs)) {
								if (l.getAliases() == null || !l.getAliases().contains(theirs)) {
									String aliasStr = "";
									if (l.getAliases() != null && !l.getAliases().isEmpty()) {
										aliasStr = " Our aliases are " + l.getAliases().stream().collect(Collectors.joining(", "));
									}
									if (upstreamPort.getAliases() != null && !upstreamPort.getAliases().isEmpty()) {
										aliasStr += " Their aliases are " + upstreamPort.getAliases().stream().collect(Collectors.joining(", "));
									}
									System.out.println("Port name mismatch: " + ours + " <-> " + theirs + aliasStr);
								}
							}
							break;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
