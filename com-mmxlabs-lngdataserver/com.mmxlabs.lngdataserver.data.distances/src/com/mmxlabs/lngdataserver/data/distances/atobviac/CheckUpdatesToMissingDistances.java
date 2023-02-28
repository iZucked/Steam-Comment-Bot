package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
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

/**
 * Queries upstream for any distances we do not have (e.g. typically because the distance didn't exist last time we checked)
 * 
 * @author Simon Goodall
 *
 */
public class CheckUpdatesToMissingDistances {

	public static void main(final String[] args) throws Exception {

		Util.PERFORM_UPSTREAM_QUERIES = true;

		final String sourceData = "2023b"; // Original data set
		final String destData = "2023c"; // New data set (assumes ports.json exists here already)

		gatherMissing(sourceData, destData);

		CompareDistanceSets.compare(sourceData, destData);
	}

	private static final TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void gatherMissing(final String sourceData, final String destData) throws Exception {

		final URL destPortJsonURL = CheckUpdatesToMissingDistances.class.getResource("/" + destData + "/ports.json");

		final URL cacheFileURL = new URL(FileLocator.toFileURL(new URL(destPortJsonURL.toString())).toString().replace(" ", "%20").replace("ports.json", "distance-cache.txt"));
		final File cacheFile = new File(cacheFileURL.toURI());
		final URL newDistancesFileURL = new URL(FileLocator.toFileURL(new URL(destPortJsonURL.toString())).toString().replace(" ", "%20").replace("ports.json", "distances.json"));
		final File newDistancesFile = new File(newDistancesFileURL.toURI());
		final File newDistancesVersionFile = new File(newDistancesFile.getParent(), "distances-version.json");

		Util.withService(service -> {
			try {
				final ObjectMapper mapper = new ObjectMapper();

				// Load in existing data set
				final List<AtoBviaCLookupRecord> existingRecords = mapper.readValue(CheckUpdatesToMissingDistances.class.getResourceAsStream("/" + sourceData + "/distances.json"), DISTANCE_TYPE);

				// Configure datastore with existing distance records
				final LocalDataStore localDataStore = new LocalDataStore();
				localDataStore.loadFrom(existingRecords);

				// Configure bean
				service.setDistanceLookupEnabled(true);
				service.setDataStore(localDataStore);

				final List<Pair<String, String>> lookups = new LinkedList<>();
				//
				for (final AtoBviaCLookupRecord r : existingRecords) {
					if (r.getErrorCode() != null) {
						lookups.add(Pair.of(r.getFrom(), r.getTo()));
					}
				}

				// TODO: Cache data load into function....

				System.out.println("New queries " + lookups.size());
				if (cacheFile.exists()) {
					// Load the between version cache - stored as many separate JSON documents
					// concatenated together
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

								final AtoBviaCLookupRecord r = jp.readValueAs(AtoBviaCLookupRecord.class);
								lookups.remove(Pair.of(r.getFrom(), r.getTo()));

								// Add to local store if there is a valid distance, otherwise only add if the
								// record was not previously there or also an error.
								final AtoBviaCLookupRecord existingRecord = localDataStore.get(r.getKey());
								if (r.getErrorCode() == null || (existingRecord == null || existingRecord.getErrorCode() != null)) {
									localDataStore.store(r);
								}

								jp.nextToken();

							}
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("Reduced queries " + lookups.size());

				// Run the distance query
				try (FileOutputStream fos = new FileOutputStream(cacheFile, true)) {
					if (Util.PERFORM_UPSTREAM_QUERIES) {
						localDataStore.setLogStream(fos);
						final Future<Void> batchRefresh = service.batchRefresh(lookups);
						// block until finished
						batchRefresh.get();
					}
				}
				// Write the new distance data file out
				mapper.writerWithDefaultPrettyPrinter().writeValue(newDistancesFile, localDataStore.getRecords());

				final DistanceDataVersion dv = new DistanceDataVersion();
				dv.setVersion(destData);
				mapper.writerWithDefaultPrettyPrinter().writeValue(newDistancesVersionFile, dv);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
