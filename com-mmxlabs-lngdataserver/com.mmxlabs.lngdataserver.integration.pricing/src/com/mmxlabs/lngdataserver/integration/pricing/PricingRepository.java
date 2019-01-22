/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.rcp.common.json.EMFDeserializer;
import com.mmxlabs.rcp.common.json.EMFSerializer;

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
