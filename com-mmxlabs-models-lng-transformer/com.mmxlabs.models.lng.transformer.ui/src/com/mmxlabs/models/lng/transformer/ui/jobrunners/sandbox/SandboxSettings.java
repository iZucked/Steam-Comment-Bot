/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonInclude(Include.NON_NULL)
public class SandboxSettings {

	private String sandboxUUID;

	private String sandboxName;

	@JsonDeserialize(as = UserSettingsImpl.class)
	private UserSettings userSettings;

	public String getSandboxUUID() {
		return sandboxUUID;
	}

	public void setSandboxUUID(final String sandboxUUID) {
		this.sandboxUUID = sandboxUUID;
	}

	public String getSandboxName() {
		return sandboxName;
	}

	public void setSandboxName(final String sandboxName) {
		this.sandboxName = sandboxName;
	}

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(final UserSettings userSettings) {
		this.userSettings = userSettings;
	}
}
