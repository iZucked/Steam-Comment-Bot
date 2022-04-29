/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.parameters.util.UserSettingsMixin;
import com.mmxlabs.models.lng.transformer.jobs.IJobRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;

@NonNullByDefault
public abstract class AbstractJobRunner implements IJobRunner {

	protected boolean enableLogging = false;
	protected @Nullable Meta meta;

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
}
