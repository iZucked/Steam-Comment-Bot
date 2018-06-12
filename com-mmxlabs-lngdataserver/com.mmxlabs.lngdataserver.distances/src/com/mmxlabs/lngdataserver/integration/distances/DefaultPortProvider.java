/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.strings.StringDistance;
import com.mmxlabs.models.lng.port.Port;

public class DefaultPortProvider implements ILocationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPortProvider.class);

	private final @NonNull String version;
	private final @NonNull List<@NonNull Port> ports;

	public DefaultPortProvider(@NonNull final String version, @NonNull final List<Port> ports) {
		this.version = version;
		this.ports = ports;
	}

	@Override
	public @NonNull String getVersion() {
		return version;
	}

	@Override
	public Port getPortById(final String mmxId) {
		final Optional<Port> potential = ports.stream().filter(p -> p.getLocation().getMmxId().equals(mmxId)).findAny();
		if (!potential.isPresent()) {
			LOGGER.warn("No port found with id " + mmxId);
			throw new RuntimeException("No port found with id " + mmxId);
		}
		return potential.get();
	}

	@Override
	public Port getPortByExactName(final String name) {
		final Optional<Port> potential = ports.stream().filter(p -> p.getName().equals(name)).findAny();
		if (!potential.isPresent()) {
			LOGGER.warn("No port found with name " + name);
			throw new RuntimeException("No port found with name " + name);
		}
		return potential.get();
	}

	@Override
	public Port getPortByApproxName(final String approxName) {
		return staticGetPortByApproxName(ports, approxName);
	}

	/**
	 * For testability.
	 */
	public static Port staticGetPortByApproxName(final List<Port> ports, final String approxName) {
		final StringDistance stringDistance = StringDistance.defaultInstance();

		final List<Port> potential = ports.stream().sorted((final Port p1, final Port p2) -> {
			return Integer.valueOf(stringDistance.distance(approxName, p1.getName())).compareTo(stringDistance.distance(approxName, p2.getName()));
		}).collect(Collectors.toList());

		// TODO: add some magic number where distance is too highs
		return potential.get(0);
	}

	@Override
	public List<Port> getPorts() {
		return Collections.unmodifiableList(ports);
	}
}
