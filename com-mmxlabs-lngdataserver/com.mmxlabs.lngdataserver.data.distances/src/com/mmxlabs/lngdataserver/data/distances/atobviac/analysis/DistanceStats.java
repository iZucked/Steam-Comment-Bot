/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.analysis;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;

public class DistanceStats {

	public static void main(String[] args) throws Exception {
		boolean rawDistances = true;
		stats("2023f", rawDistances);
	}

	private static TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void stats(String beforeSet, boolean rawDistances) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		String fmt = rawDistances ? "/%s/distances-raw.json" : "/%s/distances.json";
		String beforeFilename = String.format(fmt, beforeSet);

		try (InputStream beforeStream = DistanceStats.class.getResourceAsStream(beforeFilename);) {

			List<AtoBviaCLookupRecord> oldRecords = mapper.readValue(beforeStream, DISTANCE_TYPE);

			Map<Pair<String, String>, AtoBviaCLookupRecord> recordMap = oldRecords.stream().filter(AtoBviaCLookupRecord::getAntiPiracy).filter(r -> r.getErrorCode() == null).map(r -> {
				double d = r.getDistance();
				d = Math.floor(d);
				r.setDistance(d);
				return r;
			}).collect(Collectors.toMap(r -> Pair.of(mapName(r.getFrom()), mapName(r.getTo())), Function.identity()));

			int totalRecords = oldRecords.size();

			int newDistanceRecord = 0;
			int newDistance = 0;
			int stillMissing = 0;
			int fixedDistance = 0;
			int similarDistance = 0;
			int changedDistance = 0;
			int newErrorDistance = 0;
			// int updatedDistanceSmall = 0;
			// int updatedDistanceLarge = 0;

			double[][] updatedCount = { { 0.01, 0 }, { 2, 0 }, { 5, 0 }, { 10, 0 }, { 12, 0 }, { Double.MAX_VALUE, 0 }

			};

			int missingReverseDistance = 0;
			int[][] reverseDistanceDeltas = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 5, 0 }, { 10, 0 }, { Integer.MAX_VALUE, 0 }

			};
			Set<Pair<String, String>> seenReverseKeys = new HashSet<>();
			for (Pair<String, String> key : recordMap.keySet()) {
				final var reverseKey = Pair.of(key.getSecond(), key.getFirst());

				if (!recordMap.containsKey(reverseKey)) {
					missingReverseDistance++;
				} else

				if (seenReverseKeys.contains(key) || seenReverseKeys.contains(reverseKey)) {
					continue;
				}

				if (recordMap.containsKey(reverseKey)) {
					seenReverseKeys.add(key);
					seenReverseKeys.add(reverseKey);
					int delta = (int) Math.floor(Math.abs(recordMap.get(key).getDistance() - recordMap.get(reverseKey).getDistance()));
					for (var e : reverseDistanceDeltas) {
						if (delta <= e[0]) {
							e[1]++;
							break;
						}
					}
				}
			}

			System.out.printf("Total records: %d\n", totalRecords);
			System.out.printf("Total usable: %d\n", recordMap.size());
			System.out.printf("Total invalid: %d\n", totalRecords - recordMap.size());
			for (var e : reverseDistanceDeltas) {
				System.out.printf("Delta %d: %d\n", e[0], e[1]);
			}

		}
	}

	public static String mapName(String n) {
		// if ("KR0010".equals(n)) {
		// return "KR0058";
		// }
		return n;
	}
}
