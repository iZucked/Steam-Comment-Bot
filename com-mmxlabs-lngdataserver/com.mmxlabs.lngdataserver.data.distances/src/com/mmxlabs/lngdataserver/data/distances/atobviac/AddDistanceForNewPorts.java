/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.FileLocator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.data.distances.atobviac.analysis.CompareDistanceSets;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.DistanceDataVersion;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.LocalDataStore;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class AddDistanceForNewPorts {

	public static void main(String[] args) throws Exception {

		Util.PERFORM_UPSTREAM_QUERIES = true;

		String sourceData = "2023b";
		String destData = "2023c";

		ObjectMapper mapper = new ObjectMapper();

		List<String> newPorts = new LinkedList<>();
		{
			PortDistanceVersion sourceVersion = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);
			PortDistanceVersion destVersion = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + destData + "/ports.json"), PortDistanceVersion.class);

			Set<String> names = new HashSet<>();
			destVersion.getLocations().forEach(l -> names.add(l.getUpstreamID()));
			destVersion.getLocations().forEach(l -> names.add(l.getFallbackUpstreamID()));

			sourceVersion.getLocations().forEach(l -> names.remove(l.getUpstreamID()));
			sourceVersion.getLocations().forEach(l -> names.remove(l.getFallbackUpstreamID()));

			newPorts.addAll(names);
		}

		System.out.println("New upstream port IDs:");
		newPorts.forEach(System.out::println);

		Collections.sort(newPorts);
		gatherNew(sourceData, destData, newPorts);

		CompareDistanceSets.compare(sourceData, destData);
	}

	private static TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void gatherNew(String sourceData, String destData, List<String> newUpstreamIDs) throws Exception {

		URL destPortJsonURL = AddDistanceForNewPorts.class.getResource("/" + destData + "/ports.json");

		final URL cacheFileURL = new URL(FileLocator.toFileURL(new URL(destPortJsonURL.toString())).toString().replace(" ", "%20").replace("ports.json", "distance-cache.txt"));
		final File cacheFile = new File(cacheFileURL.toURI());
		final URL newDistancesFileURL = new URL(FileLocator.toFileURL(new URL(destPortJsonURL.toString())).toString().replace(" ", "%20").replace("ports.json", "distances.json"));
		final File newDistancesFile = new File(newDistancesFileURL.toURI());
		final File newDistancesVersionFile = new File(newDistancesFile.getParent(), "distances-version.json");

		Util.withService(service -> {
			try {
				ObjectMapper mapper = new ObjectMapper();

				// Load in existing data set
				List<AtoBviaCLookupRecord> existingRecords = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/distances.json"), DISTANCE_TYPE);

				// Load in the list of (existing) ports
				PortDistanceVersion portsConfig = mapper.readValue(destPortJsonURL.openStream(), PortDistanceVersion.class);

				Set<String> existingPortsSet = new LinkedHashSet<>();

				portsConfig.getLocations().forEach(l -> {
					// Some ports are legacy "virtual" ports, ignore them
					if (l.getUpstreamID() != null) {
						existingPortsSet.add(l.getUpstreamID());
					}
				});

				// Remove new ports from existing ports list
				existingPortsSet.removeAll(newUpstreamIDs);

				List<String> existingPorts = new LinkedList<>(existingPortsSet);

				// Configure datastore with existing distance records
				LocalDataStore localDataStore = new LocalDataStore();
				localDataStore.loadFrom(existingRecords);

				// Configure bean
				service.setDistanceLookupEnabled(false);
				service.setDataStore(localDataStore);

				List<Pair<String, String>> lookups = new LinkedList<>();

				// Add new port to new port lookup
				for (String from : newUpstreamIDs) {
					for (String to : newUpstreamIDs) {
						// == is fine here as it really is the same String instance
						if (from == to) {
							continue;
						}
						lookups.add(Pair.of(from, to));
					}
				}
				// Add bi-directional lookup for all existing ports to new port
				for (String existing : existingPorts) {
					for (String newPort : newUpstreamIDs) {
						lookups.add(Pair.of(newPort, existing));
						lookups.add(Pair.of(existing, newPort));
					}
				}

				System.out.println("New queries " + lookups.size());

				// Load the between version cache - stored as many separate JSON documents
				// concatenated together
				if (cacheFile.exists()) {
					try (InputStream fis = new BufferedInputStream(new FileInputStream(cacheFile))) {
						final JsonFactory jsonFactory = new JsonFactory();
						jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
						jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

						final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

						final JsonFactory jf = new JsonFactory();
						try (JsonParser jp = jf.createParser(fis)) {
							jp.setCodec(objectMapper);

							jp.nextToken();
							while (jp.hasCurrentToken()) {

								AtoBviaCLookupRecord r = jp.readValueAs(AtoBviaCLookupRecord.class);
								lookups.remove(Pair.of(r.getFrom(), r.getTo()));

								localDataStore.store(r);

								jp.nextToken();

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					System.out.println("Reduced queries " + lookups.size());
				}
				// Run the distance query
				try (FileOutputStream fos = new FileOutputStream(cacheFile, true)) {
					if (Util.PERFORM_UPSTREAM_QUERIES) {
						localDataStore.setLogStream(fos);
						Future<Void> batchRefresh = service.batchRefresh(lookups);
						// block until finished
						batchRefresh.get();
					} else {
						System.out.println("Upstream querying disabled");
					}
				}
				// Write the new distance data file out
				mapper.writerWithDefaultPrettyPrinter().writeValue(newDistancesFile, localDataStore.getRecords());

				DistanceDataVersion dv = new DistanceDataVersion();
				dv.setVersion(destData);
				mapper.writerWithDefaultPrettyPrinter().writeValue(newDistancesVersionFile, dv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
