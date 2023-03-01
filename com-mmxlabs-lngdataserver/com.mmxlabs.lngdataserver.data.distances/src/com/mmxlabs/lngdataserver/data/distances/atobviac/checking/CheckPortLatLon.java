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
 * Check the Lat/lon we have is close to the lat/lon for the specified upstream port.
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortLatLon {

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
							if (Math.abs(l.getGeographicPoint().getLat() - upstreamPort.getLatGeodetic()) > 0.001 //
									|| Math.abs(l.getGeographicPoint().getLon() - upstreamPort.getLon()) > 0.001) {

								var d = Util.distance(l.getGeographicPoint().getLon(), l.getGeographicPoint().getLat(), upstreamPort.getLon(), upstreamPort.getLatGeodetic(), 'M');
								System.out.printf("%s lat/lon diffes by %f miles %n", l.getName(), d);
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
