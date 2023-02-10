/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class DistanceUpdaterTests {

	/**
	 * Constant used for "current" dataset as null doesn't look nice in the test name
	 */
	private static final String CURRENT = "Current";

	public static Iterable<Object[]> distanceTransitions() {
		return Arrays.asList(new Object[][] { //
				{ "2021a", "2021b", 0 }, //
				{ "2021b", "2021c", 2 }, // Expect two lost distances here
				{ "2021c", "2021d", 0 }, //
				{ "2021d", "2022a", 0 }, //
				{ "2022a", "2022b", 0 }, //
				{ "2022b", "2023a", 0 }, //
				{ "2023a", "2023b", 0 }, //
				{ "2023a", CURRENT, 0 }, //
				{ "2023b", CURRENT, 0 }, //

		});
	}

	@ParameterizedTest(name = "test {0} To {1}")
	@MethodSource("distanceTransitions")
	void performTest(String from, String to, int tolerance) throws Exception {

		// Load in the scenario
		ScenarioBuilder builder = ScenarioBuilder.initialiseBasicScenario();
		IScenarioDataProvider sdp = builder.getScenarioDataProvider();
		PortModel portModel = ScenarioModelUtil.getPortModel(sdp);

		// Clear any existing distances
		for (Route r : portModel.getRoutes()) {
			r.getLines().clear();
		}

		// Import our before data
		ScenarioBuilder.reloadPortsAndDistances(sdp, DataLoader.class.getResourceAsStream("/" + from + "/ports.json"), //
				DataLoader.class.getResourceAsStream("/" + from + "/distances.json"), //
				null, // DataLoader.class.getResourceAsStream("/" + from + "/distances-manual.json"), //
				null);

		// Compute basic stats
		EnumMap<RouteOption, Integer> before = new EnumMap<>(RouteOption.class);
		Map<Triple<RouteOption, Port, Port>, Integer> counter = new HashMap<>();
		for (var r : portModel.getRoutes()) {
			before.put(r.getRouteOption(), r.getLines().size());
			for (var l : r.getLines()) {
				if (l.getDistance() >= 0.0) {
					Triple<RouteOption, Port, Port> key = Triple.of(r.getRouteOption(), l.getFrom(), l.getTo());
					counter.merge(key, -1, Math::addExact);
				}
			}
		}

		// Replace with after data
		if (to == null || to == CURRENT) {
			ScenarioBuilder.reloadPortsAndDistances(sdp);
		} else {
			ScenarioBuilder.reloadPortsAndDistances(sdp, DataLoader.class.getResourceAsStream("/" + to + "/ports.json"), //
					DataLoader.class.getResourceAsStream("/" + to + "/distances.json"), //
					null, // DataLoader.class.getResourceAsStream("/" + to + "/distances-manual.json"), //
					null);
		}
		// Compute stats
		EnumMap<RouteOption, Integer> after = new EnumMap<>(RouteOption.class);
		for (var r : portModel.getRoutes()) {
			after.put(r.getRouteOption(), r.getLines().size());
			for (var l : r.getLines()) {
				if (l.getDistance() >= 0.0) {
					Triple<RouteOption, Port, Port> key = Triple.of(r.getRouteOption(), l.getFrom(), l.getTo());
					counter.merge(key, 1, Math::addExact);
				}
			}
		}

		// Make sure we have not lost any distance lines from the totals count
		for (var ro : RouteOption.values()) {
			// System.out.printf("%s %,d -> %,d%n", ro.getName(), before.get(ro), after.get(ro));
			 Assertions.assertTrue(after.get(ro) >= before.get(ro));
		}
		int lost = 0;
		for (var e : counter.entrySet()) {
			if (e.getValue() < 0) {
				System.out.printf("Lost %s -> %s (%s)%n", e.getKey().getSecond().getName(), e.getKey().getThird().getName(), e.getKey().getFirst().getName());
				lost++;
			}
		}
		Assertions.assertEquals(tolerance, lost);
	}

}
