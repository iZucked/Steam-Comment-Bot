/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;

public class VesselsRepository extends AbstractDataRepository<VesselsVersion> {

	private static final Logger LOG = LoggerFactory.getLogger(VesselsRepository.class);

	public static final VesselsRepository INSTANCE = new VesselsRepository();

	public VesselsRepository() {
		super("Vessels", VesselsVersion.class);
		doHandleUpstreamURLChange();
	}

	@Override
	protected String getVersionsURL() {
		return "/vessels/versions";
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return "/vessels/sync/versions/";
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/vessels/version_notification";
	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	@Override
	protected SimpleVersion wrap(final VesselsVersion version) {
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
