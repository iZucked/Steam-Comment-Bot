/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.pricesensitivity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonInclude(Include.NON_NULL)
public class PriceSensitivitySettings {

	private String sensitivityUUID;

	private String sensitivityName;

	@JsonDeserialize(as = UserSettingsImpl.class)
	private UserSettings userSettings;

	public String getSensitivityUUID() {
		return sensitivityUUID;
	}

	public void setSensitivityUUID(final String sensitivityUUID) {
		this.sensitivityUUID = sensitivityUUID;
	}

	public String getSensitivityName() {
		return sensitivityName;
	}

	public void setSensitivityName(final String sensitivityName) {
		this.sensitivityName = sensitivityName;
	}

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public void setUserSettings(final UserSettings userSettings) {
		this.userSettings = userSettings;
	}
}
