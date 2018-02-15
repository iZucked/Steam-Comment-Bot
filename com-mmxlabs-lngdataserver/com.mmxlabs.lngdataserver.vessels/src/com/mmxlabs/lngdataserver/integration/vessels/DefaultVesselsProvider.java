package com.mmxlabs.lngdataserver.integration.vessels;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.strings.StringDistance;
import com.mmxlabs.lngdataserver.vessel.model.Vessel;
import com.mmxlabs.models.lng.port.Port;

public class DefaultVesselsProvider implements IVesselsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultVesselsProvider.class);

	private final @NonNull String version;
	private final @NonNull List<@NonNull Vessel> vessels;

	public DefaultVesselsProvider(@NonNull final String version, @NonNull final List<Vessel> vessels) {
		this.version = version;
		this.vessels = vessels;
	}

	@Override
	public @NonNull String getVersion() {
		return version;
	}

	@Override
	public Vessel getVesselById(final String mmxId) {
		final Optional<Vessel> potential = vessels.stream().filter(v -> v.getMmxId().equals(mmxId)).findAny();
		if (!potential.isPresent()) {
			LOGGER.warn("No port found with id " + mmxId);
			throw new RuntimeException("No port found with id " + mmxId);
		}
		return potential.get();
	}


	@Override
	public List<Vessel> getVessels() {
		return Collections.unmodifiableList(vessels);
	}
}
