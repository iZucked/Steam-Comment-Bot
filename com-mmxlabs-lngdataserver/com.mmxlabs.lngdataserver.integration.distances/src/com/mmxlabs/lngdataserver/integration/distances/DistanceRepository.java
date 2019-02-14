/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.time.LocalDateTime;

import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository extends AbstractDataRepository<DistancesVersion> {

	public static final DistanceRepository INSTANCE = new DistanceRepository();

	private DistanceRepository() {
		super("Distances", DistancesVersion.class);
		doHandleUpstreamURLChange();
	}

	protected String getVersionsURL() {
		return "/distances/versions";
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return "/distances/sync/versions/";
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/distances/version_notification";
	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return true;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return true;
	}

	@Override
	protected SimpleVersion wrap(DistancesVersion version) {
		return new SimpleVersion() {

			@Override
			public String getIdentifier() {
				return version.getIdentifier();
			}

			@Override
			public LocalDateTime getCreatedAt() {
				return version.getCreatedAt();
			}
		};
	}
}