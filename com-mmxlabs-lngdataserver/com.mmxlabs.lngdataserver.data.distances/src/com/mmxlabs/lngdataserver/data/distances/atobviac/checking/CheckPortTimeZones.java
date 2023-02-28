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
 * Check the MMX port timezone y matches the mapping from A to B via C. Note - the A to B via C mapping is usually less exact (it uses a city in the same timezone rather than the nearest city)
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortTimeZones {

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

							String ours = l.getGeographicPoint().getTimeZone();
							String theirs = upstreamPort.getTimezone().getTimezoneId();
							if (!ours.equals(theirs)) {
								System.out.println("Timezone mismatch for " + l.getName() + ": " + ours + " <-> " + theirs);
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
