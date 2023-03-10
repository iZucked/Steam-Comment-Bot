/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.Location;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class AddNewPort {

	public static void main(final String[] args) {

		// Input data
		final List<String> newPorts = Lists.newArrayList("NL0017");
		final String sourceData = "2023b";

		// Output
		final String destData = "2023d";

		Util.withService(service -> {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			try {

				Set<String> existingMMXIDs = new HashSet<>();

				final PortDistanceVersion version = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);
				// upstreamPorts.
				for (var l : version.getLocations()) {
					existingMMXIDs.add(l.getMmxId());
				}

				final URL sourceVersionURL = AddDistanceForNewPorts.class.getResource("/" + sourceData + "/ports.json");

				final URL destFileURL = new URL(FileLocator.toFileURL(new URL(sourceVersionURL.toString())).toString().replace(" ", "%20").replace(sourceData, destData));
				final File destFile = new File(destFileURL.toURI());
				destFile.getParentFile().mkdirs();

				final List<AtoBviaCPort> upstreamPorts;
				File portCache = new File(destFile.getParentFile(), "port-cache.json");
				if (portCache.exists()) {
					upstreamPorts = mapper.readerForListOf(AtoBviaCPort.class).readValue(portCache);
				} else {
					upstreamPorts = service.getAtoBviaCAdapter().getPortsBlocking();
					// ObjectMapper mapper = new ObjectMapper();
					mapper.writerWithDefaultPrettyPrinter().writeValue(portCache, upstreamPorts);
				}

				for (final var upstreamPort : upstreamPorts) {
					if (newPorts.contains(upstreamPort.getCode())) {

						final Location newLocation = new Location();
						newLocation.setName(upstreamPort.getName());
						if (upstreamPort.getAliases() != null && !upstreamPort.getAliases().isEmpty()) {
							newLocation.setAliases(upstreamPort.getAliases());
						}
						newLocation.setUpstreamID(upstreamPort.getCode());
						newLocation.getGeographicPoint().setLat(upstreamPort.getLatGeodetic());
						newLocation.getGeographicPoint().setLon(upstreamPort.getLon());
						newLocation.getGeographicPoint().setTimeZone(upstreamPort.getTimezone().getTimezoneId());
						if (upstreamPort.getLocode() != null && !upstreamPort.getLocode().isEmpty()) {
							// Most, but not all locodes have no spacing after the country code. Strip out spaces in case of noisy data
							String lc = upstreamPort.getLocode().replaceAll(" ", "");
							newLocation.setLocode(String.format("%s  %s", lc.substring(0, 2), lc.substring(2)));
						}
						if (!Util.COUNTRY_CODE_MAP.containsKey(upstreamPort.getCountryCode())) {
							throw new IllegalStateException("No country code mapping for " + upstreamPort.getCountryCode());
						}
						newLocation.getGeographicPoint().setCountry(Util.COUNTRY_CODE_MAP.get(upstreamPort.getCountryCode()));

						// find a unique MMX id to use
						String name = upstreamPort.getName();
						String prefix = name.length() < 5 ? name : name.substring(0, 5);
						String basemmxId = String.format("L_%s_%s", upstreamPort.getCountryCode(), prefix);
						String mmxId = basemmxId;
						int counter = 2;
						while (existingMMXIDs.contains(mmxId)) {
							mmxId = basemmxId + counter++;
						}
						existingMMXIDs.add(mmxId);

						newLocation.setMmxId(mmxId);

						version.getLocations().add(newLocation);
					}
				}
				version.setCreatedAt(Instant.now());

				destFile.getParentFile().mkdirs();
				mapper.writerWithDefaultPrettyPrinter().writeValue(destFile, version);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}
}
