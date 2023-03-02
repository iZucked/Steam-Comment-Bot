package com.mmxlabs.lngdataserver.data.distances.atobviac.checking;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lngdataserver.data.distances.atobviac.AddDistanceForNewPorts;
import com.mmxlabs.lngdataserver.data.distances.atobviac.Util;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

/**
 * Check the MMX port that have notes or fallback ids (typically meaning they need periodic review)
 * 
 * @author Simon Goodall
 *
 */
public class CheckPortsWithNotes {

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
					 if (l.getNotes() != null) {
						 System.out.printf("%s: %s%n%n", l.getName(), l.getNotes());
					 }
					 if (l.getFallbackUpstreamID() != null) {
						 System.out.printf("%s: has a fallback id of %s%n%n", l.getName(), l.getFallbackUpstreamID());
					 }
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
