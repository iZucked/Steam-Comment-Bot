package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class GenerateLiNGODataSet {
	public static void main(final String[] args) throws Exception {

		final String compareData = "2023f"; // Older set to compare to.
		final String sourceData = "2023f"; // Original data set

		postProcess(sourceData);

		// CompareDistanceSets.compare(compareData, sourceData, false);
	}

	private static final TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void postProcess(final String sourceData) throws Exception {

		try {
			final ObjectMapper mapper = new ObjectMapper();

			// Load in existing data set
			final PortDistanceVersion version = mapper.readValue(AddDistanceForNewPorts.class.getResourceAsStream("/" + sourceData + "/ports.json"), PortDistanceVersion.class);
			// Filter out invalid distances and round the valid ones.
			// We also generate the look up and filter out bi-directional distances.
			final Map<Pair<String, String>, AtoBviaCLookupRecord> recordsMap = new HashMap<>();

			// First pass, filter out invalid and identical bi-directional distances, round down distance values and record remaining in a lookup table.
			final List<AtoBviaCLookupRecord> baseExistingRecords = mapper.readValue(CheckUpdatesToMissingDistances.class.getResourceAsStream("/" + sourceData + "/distances-raw.json"), DISTANCE_TYPE)
					.stream() // .
					.filter(AtoBviaCLookupRecord::getAntiPiracy)
					.filter(r -> r.getErrorCode() == null) //
					.map(r -> {
						if (r.getDistance() > 0.0) {
							r.setDistance(Math.floor(r.getDistance()));
						}
						return r;
					}) //

					.filter(r -> {
						final var p1 = Pair.of(r.getFrom(), r.getTo());
						final var p2 = Pair.of(r.getTo(), r.getFrom());

						final var v2 = recordsMap.get(p2);
						if (v2 != null && v2.getDistance() == r.getDistance()) {
							return false;
						}
						recordsMap.put(p1, r);
						return true;
					})
					// Record distance
					.map(r -> {
						final var p1 = Pair.of(r.getFrom(), r.getTo());
						recordsMap.put(p1, r);
						return r;
					}) //
					.toList(); //
			// Second pass, filter out similar bi-directional distances
			final Range<Double> similarRange = Range.closed(0.0, 2.0);
			final List<AtoBviaCLookupRecord> existingRecords = baseExistingRecords.stream() //
					.filter(r -> {
						final var p2 = Pair.of(r.getTo(), r.getFrom());
						final var v2 = recordsMap.get(p2);
						// For distances over 1000m, if the bi-directional distance is with 2nm, retain the lower distance
						// Check the other distance is over 1000 and this distance-other distance is in the positive range (this ensure that this distance is greater than the other distance and so
						// both are over 1000.0
						if (v2 != null && v2.getDistance() > 1_000.0 && similarRange.contains(r.getDistance() - v2.getDistance())) {
							return false;
						} else {
							return true;
						}
					})
					.toList();

			// Next - we want to determine which distances will be used. Some ports have a fallback id and thus some fallback port entries will be unused.

			// Generate port and fallback port mapping
			final Map<String, String> fallbackUpstreaIDMapping = new HashMap<>();
			final Map<String, String> mmxIdToUpstreaIDMapping = new HashMap<>();
			for (final var bl : version.getLocations()) {
				final String fallbackId = bl.getFallbackUpstreamID();
				if (fallbackId != null) {
					fallbackUpstreaIDMapping.put(bl.getUpstreamID(), bl.getFallbackUpstreamID());
				}
				mmxIdToUpstreaIDMapping.put(bl.getMmxId(), bl.getUpstreamID());
			}

			final BiFunction<String, String, @Nullable AtoBviaCLookupRecord> lookupFunction = makeLookupFunction(recordsMap, fallbackUpstreaIDMapping);

			for (final var from : version.getLocations()) {
				final String fromID = from.getUpstreamID();
				if (fromID == null) {
					assert false;
				}
				for (final var to : version.getLocations()) {
					final String toID = to.getUpstreamID();

					if (toID == null) {
						assert false;
					}
					// Check identity
					if (from == to) {
						continue;
					}
					// Check primary id match
					if (Objects.equals(fromID, toID)) {
						continue;
					}

					final AtoBviaCLookupRecord distanceRecord = lookupFunction.apply(fromID, toID);
					if (distanceRecord == null) {
						continue;
					}
					// Mark this record used
					distanceRecord.used = true;
				}
			}

			// Finally filter out unused record.
			final List<AtoBviaCLookupRecord> retainedRecords = existingRecords.stream() // .
					.filter(r -> r.used)
					.toList();

			final URL destPortJsonURL = AddDistanceForNewPorts.class.getResource("/" + sourceData + "/distances-raw.json");
			final URL newDistancesFileURL = new URL(FileLocator.toFileURL(new URL(destPortJsonURL.toString())).toString().replace(" ", "%20").replace("distances-raw.json", "distances.json"));
			final File newDistancesFile = new File(newDistancesFileURL.toURI());
			mapper.writerWithDefaultPrettyPrinter().writeValue(newDistancesFile, retainedRecords);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static BiFunction<String, String, @Nullable AtoBviaCLookupRecord> makeLookupFunction(final Map<Pair<String, String>, AtoBviaCLookupRecord> recordsMap,
			// final //
			// Map<Pair<String, String>, AtoBviaCLookupRecord> manualRecordsMap,
			final Map<String, String> fallbackIDMapping) {

		// Test for a valid and usable distance record
		final Predicate<AtoBviaCLookupRecord> isValidRecord = record -> record != null && record.getErrorCode() == null && record.getDistance() >= 0.0;

		// Returns the record if usable, otherwise null
		final BiFunction<String, String, AtoBviaCLookupRecord> getIfValid = (fromID, toID) -> {
			// Try primary ID pair first of all.
			AtoBviaCLookupRecord record = recordsMap.get(Pair.of(fromID, toID));
			if (isValidRecord.test(record)) {
				return record;
			}
			// Try reverse distance
			record = recordsMap.get(Pair.of(toID, fromID));
			if (isValidRecord.test(record)) {
				return record;
			}

			return null;
		};

		return (fromID, toID) -> {

			// Try primary ID pair first of all.
			AtoBviaCLookupRecord record = getIfValid.apply(fromID, toID);
			if (record != null) {
				return record;
			}
			if (fallbackIDMapping.containsKey(fromID)) {
				final String fallbackFromID = fallbackIDMapping.get(fromID);
				record = getIfValid.apply(fallbackFromID, toID);
				if (record != null) {
					return record;
				}
			}
			if (fallbackIDMapping.containsKey(toID)) {
				final String fallbackToID = fallbackIDMapping.get(toID);
				record = getIfValid.apply(fromID, fallbackToID);
				if (record != null) {
					return record;
				}
			}
			if (fallbackIDMapping.containsKey(fromID) && fallbackIDMapping.containsKey(toID)) {
				final String fallbackFromID = fallbackIDMapping.get(fromID);
				final String fallbackToID = fallbackIDMapping.get(toID);

				record = getIfValid.apply(fallbackFromID, fallbackToID);
				if (record != null) {
					return record;
				}
			}
			return null;
		};
	}
}
