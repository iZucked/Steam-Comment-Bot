/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

public class DefaultDistanceProvider implements IDistanceProvider {
	private final @NonNull String version;
	private final @NonNull Map<Via, Map<String, Map<String, Double>>> distances;
	private final @NonNull Set<@NonNull String> knownPorts;

	public DefaultDistanceProvider(@NonNull final String version, @NonNull final Map<Via, Map<String, Map<String, Double>>> distances) {
		this.version = version;
		this.distances = distances;
		final Set<String> tmpKnownPorts = new HashSet<>();
		distances.forEach((k, viaMap) -> {
			viaMap.forEach((from, toMap) -> {
				tmpKnownPorts.add(from);
				tmpKnownPorts.addAll(toMap.keySet());
			});
		});
		this.knownPorts = Collections.unmodifiableSet(new TreeSet<>(tmpKnownPorts));
	}

	@Override
	public @NonNull String getVersion() {
		return version;
	}

	@Override
	public double getDistance(@NonNull final String from, @NonNull final String to, @NonNull final Via route) {

		if (distances.containsKey(route)) {
			final Map<String, Map<String, Double>> map = distances.get(route);
			if (map.containsKey(from)) {
				final Map<String, Double> map2 = map.get(from);
				return map2.getOrDefault(to, Double.MAX_VALUE);
			}
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public @NonNull Collection<@NonNull String> getKnownPorts() {
		return knownPorts;
	}
}
