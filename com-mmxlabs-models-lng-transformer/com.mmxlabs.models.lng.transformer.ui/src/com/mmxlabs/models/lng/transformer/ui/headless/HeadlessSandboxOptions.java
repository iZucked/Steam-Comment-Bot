/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonInclude(Include.NON_NULL)
public class HeadlessSandboxOptions {
	public String sandboxUUID;

	public String sandboxName;

	@JsonDeserialize(as = UserSettingsImpl.class)
	public UserSettings userSettings;

}
