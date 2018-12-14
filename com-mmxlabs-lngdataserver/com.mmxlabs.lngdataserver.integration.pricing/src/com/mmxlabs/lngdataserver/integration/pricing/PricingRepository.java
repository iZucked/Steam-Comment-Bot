/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;

public class PricingRepository extends AbstractDataRepository<PricingVersion> {

	public static final PricingRepository INSTANCE = new PricingRepository();

	private static final Logger LOG = LoggerFactory.getLogger(PricingRepository.class);

	private PricingRepository() {
		super("Pricing", PricingVersion.class);
		doHandleUpstreamURLChange();
	}

	@Override
	protected String getVersionsURL() {
		return "/pricing/versions";
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return "/pricing/sync/versions/";
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/pricing/version_notification";
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
	protected SimpleVersion wrap(final PricingVersion version) {
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
