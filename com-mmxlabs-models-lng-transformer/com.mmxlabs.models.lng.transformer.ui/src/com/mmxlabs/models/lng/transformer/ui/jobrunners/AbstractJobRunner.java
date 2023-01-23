/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.parameters.util.UserSettingsMixin;
import com.mmxlabs.models.lng.transformer.jobs.IJobRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public abstract class AbstractJobRunner implements IJobRunner {

	protected int subJobId = 0;
	protected boolean enableLogging = false;
	protected @Nullable Meta meta;

	protected @Nullable IScenarioDataProvider sdp;

	public static ObjectMapper createObjectMapper() {

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
		objectMapper.addMixIn(UserSettings.class, UserSettingsMixin.class);

		return objectMapper;
	}

	@Override
	public void withLogging(final Object object) {
		enableLogging = true;
		if (object instanceof Meta m) {
			this.meta = m;
		}
	}

	@Override
	public void withParams(final File file) throws IOException {

		try (FileInputStream is = new FileInputStream(file)) {
			final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			withParams(text);
		}
	}

	@Override
	public void withParams(final InputStream is) throws IOException {
		final String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
		withParams(text);
	}

	@Override
	public void withScenario(final IScenarioDataProvider sdp) {
		this.sdp = sdp;
	}

	@Override
	public void withSubJobId(int subJobId) {
		this.subJobId = subJobId;
	}

}
