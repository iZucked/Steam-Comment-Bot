/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonInclude(Include.NON_NULL)
public class OptioniserSettings {

	public List<String> loadIds = new LinkedList<>();
	public List<String> dischargeIds = new LinkedList<>();
	public List<String> eventsIds = new LinkedList<>();

	@JsonDeserialize(as = UserSettingsImpl.class)
	public UserSettings userSettings;
}
