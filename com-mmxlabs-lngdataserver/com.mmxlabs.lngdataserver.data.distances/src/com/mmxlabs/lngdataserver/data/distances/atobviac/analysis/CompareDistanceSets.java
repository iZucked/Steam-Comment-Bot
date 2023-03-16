/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.analysis;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;

public class CompareDistanceSets {

	public static void main(String[] args) throws Exception {
		compare("2023b", "2023c");
	}

	private static TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void compare(String beforeSet, String afterSet) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		String fmt = "/%s/distances.json";
		String beforeFilename = String.format(fmt, beforeSet);
		String afterFilename = String.format(fmt, afterSet);

		try (InputStream beforeStream = CompareDistanceSets.class.getResourceAsStream(beforeFilename); InputStream afterStream = CompareDistanceSets.class.getResourceAsStream(afterFilename)) {

			List<AtoBviaCLookupRecord> oldRecords = mapper.readValue(beforeStream, DISTANCE_TYPE);
			List<AtoBviaCLookupRecord> newRecords = mapper.readValue(afterStream, DISTANCE_TYPE);

			Map<Pair<String, String>, AtoBviaCLookupRecord> oldMap = oldRecords.stream()
					.filter(AtoBviaCLookupRecord::getAntiPiracy)
					.collect(Collectors.toMap(r -> Pair.of(mapName(r.getFrom()), mapName(r.getTo())), Function.identity()));
			Map<Pair<String, String>, AtoBviaCLookupRecord> newMap = newRecords.stream()
					.filter(AtoBviaCLookupRecord::getAntiPiracy)
					.collect(Collectors.toMap(r -> Pair.of(mapName(r.getFrom()), mapName(r.getTo())), Function.identity()));

			int newDistance = 0;
			int stillMissing = 0;
			int fixedDistance = 0;
			int similarDistance = 0;
			int newErrorDistance = 0;
			// int updatedDistanceSmall = 0;
			// int updatedDistanceLarge = 0;

			double[][] updatedCount = { { 0.01, 0 }, { 2, 0 }, { 5, 0 }, { 10, 0 }, { 12, 0 }, { Double.MAX_VALUE, 0 }

			};

			for (Pair<String, String> key : newMap.keySet()) {
				if (oldMap.containsKey(key)) {
					AtoBviaCLookupRecord oldRecord = oldMap.get(key);
					AtoBviaCLookupRecord newRecord = newMap.get(key);

					if (oldRecord.getDistance() < 0.0 && newRecord.getDistance() < 0.0) {
						stillMissing++;
					} else if (oldRecord.getDistance() < 0.0) {
						fixedDistance++;
						System.out.println(key + " F  " + newRecord.getFrom() + " -- " + oldRecord.getTo() + " --- " + newRecord.getDistance());

					} else if (newRecord.getDistance() < 0.0) {
						System.out.println(key + "  " + newRecord.getErrorCode() + " -- " + +oldRecord.getDistance());

						newErrorDistance++;
					} else {
						double delta = Math.abs(oldRecord.getDistance() - newRecord.getDistance());
						for (double[] b : updatedCount) {
							if (delta < b[0]) {
								b[1]++;
								break;
							}
						}

						// if ( < 0.01) {
						// }
						// similarDistance++;
						// } else if (Math.abs(oldRecord.getDistance() - newRecord.getDistance()) <
						// 12.0) {
						// updatedDistanceSmall++;
						// } else {
						// updatedDistanceLarge++;
					}

				} else {
					newDistance++;
					AtoBviaCLookupRecord newRecord = newMap.get(key);

					if (newRecord.getDistance() < 0.0) {
						System.out.println(key + " New Missing " + newRecord.getFrom() + " -- " + newRecord.getTo() + " --- " + newRecord.getDistance());

						newErrorDistance++;
					}
				}
			}
			int lostDistance = 0;

			for (Pair<String, String> key : oldMap.keySet()) {
				if (!newMap.containsKey(key)) {
					// System.out.println(key);
					lostDistance++;
				}
			}

			System.out.println("==Summary==");
			System.out.printf("Total records: %d %d\n", oldMap.entrySet().stream().count(), newMap.entrySet().stream().count());
			System.out.printf("Total distances: %d %d\n", oldMap.entrySet().stream().filter(e -> e.getValue().getDistance() >= 0).count(),
					newMap.entrySet().stream().filter(e -> e.getValue().getDistance() >= 0).count());
			// System.out.printf("Missing both ways: %d\n", bothMissing);
			// System.out.printf("Missing one way: %d\n", oneMissing);
			System.out.printf("Added distance record: %d\n", newDistance);
			// System.out.printf("Updated distance record < 10nm: %d\n",
			// updatedDistanceSmall);
			// System.out.printf("Updated distance record: %d\n", updatedDistanceLarge);
			for (double[] b : updatedCount) {
				System.out.printf("Updated distance record < %.2G nm: %.0f\n", b[0], b[1]);

				// System.out.printf("Updated distance record: %d\n", updatedDistanceLarge);
			}
			System.out.printf("Similar records: %d\n", similarDistance);
			System.out.printf("Still missing: %d\n", stillMissing);
			System.out.printf("Fixed: %d\n", fixedDistance);
			System.out.printf("Lost: %d\n", lostDistance);
			System.out.printf("New missing: %d\n", newErrorDistance);

		}
	}

	public static String mapName(String n) {
		// if ("KR0010".equals(n)) {
		// return "KR0058";
		// }
		return n;
	}
}
