/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataservice.client.ports.model.Port;

public class DefaultPortsProvider implements IPortsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPortsProvider.class);

	private final @NonNull String version;
	private final @NonNull List<@NonNull Port> ports;

	public DefaultPortsProvider(@NonNull final String version, @NonNull final List<Port> ports) {
		this.version = version;
		this.ports = ports;
	}

	@Override
	public @NonNull String getVersion() {
		return version;
	}

	@Override
	public Port getPortById(final String mmxId) {
		final Optional<Port> potential = ports.stream().filter(v -> v.getMmxId().equals(mmxId)).findAny();
		if (!potential.isPresent()) {
			LOGGER.warn("No port found with id " + mmxId);
			throw new RuntimeException("No port found with id " + mmxId);
		}
		return potential.get();
	}


	@Override
	public List<Port> getPorts() {
		return Collections.unmodifiableList(ports);
	}
}
