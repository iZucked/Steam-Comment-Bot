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
 * Check Search for nearby ports and print the locodes out
 * 
 * @author Simon Goodall
 *
 */
public class CheckLocode {

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

				LOOP_LOCATIONS: for (var l : version.getLocations()) {

					Set<String> locodes = new HashSet<>();
					String countryCode = null;
					for (final var upstreamPort : upstreamPorts) {
						if (l.getUpstreamID().equals(upstreamPort.getCode())) {
							// Do the codes already match?
							if (l.getLocode() != null && !l.getLocode().isBlank() && upstreamPort.getLocode() != null && !upstreamPort.getLocode().isBlank()) {
								if (l.getLocode().replaceAll(" ", "").equals(upstreamPort.getLocode().replaceAll(" ", ""))) {
									continue LOOP_LOCATIONS;
								}
							}
							if (upstreamPort.getLocode() != null && !upstreamPort.getLocode().isBlank()) {
								locodes.add(upstreamPort.getLocode().replaceAll(" ", ""));
							}
							countryCode = upstreamPort.getCountryCode();
							break;
						}
					}
					for (final var upstreamPort : upstreamPorts) {
						if (!l.getUpstreamID().equals(upstreamPort.getCode())) {
							// Make sure we are looking in the same country
							if (countryCode.equals(upstreamPort.getCountryCode())) {
								var d = Util.distance(l.getGeographicPoint().getLon(), l.getGeographicPoint().getLat(), upstreamPort.getLon(), upstreamPort.getLatGeodetic(), 'M');
								if (d < threshold) {
									locodes.add(upstreamPort.getLocode().replaceAll(" ", ""));
								}
							}
						}
					}

					locodes.remove(null);
					locodes.remove("");
					if (l.getLocode() != null && !l.getLocode().isBlank()) {
						String lc = l.getLocode().replaceAll(" ", "");
						locodes.remove(lc);
						if (!locodes.isEmpty()) {
							String s = locodes.stream().map(ll -> String.format("%s  %s", ll.substring(0, 2), ll.substring(2))).collect(Collectors.joining(", "));
							System.out.printf("%s has %s. Alternatives are %s%n", l.getName(), l.getLocode(), s);
						}
					} else {
						if (!locodes.isEmpty()) {
							String s = locodes.stream().map(ll -> String.format("%s  %s", ll.substring(0, 2), ll.substring(2))).collect(Collectors.joining(", "));
							System.out.printf("%s locode possible options are %s%n", l.getName(), s);
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}
}
