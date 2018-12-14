/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.time.LocalDateTime;

import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;

public class PortsRepository extends AbstractDataRepository<PortsVersion> {

	public static final PortsRepository INSTANCE = new PortsRepository();

	private PortsRepository() {
		super("Ports", PortsVersion.class);
		doHandleUpstreamURLChange();
	}

	@Override
	protected String getVersionsURL() {
		return "/ports/versions";
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return "/ports/sync/versions/";
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/ports/version_notification";
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
	protected SimpleVersion wrap(final PortsVersion version) {
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
