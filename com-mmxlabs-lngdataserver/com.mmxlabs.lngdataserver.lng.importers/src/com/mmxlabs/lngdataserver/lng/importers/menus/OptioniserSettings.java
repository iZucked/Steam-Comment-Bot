package com.mmxlabs.lngdataserver.lng.importers.menus;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

// TODO: Replace with HeadlessOptioniserOptions when merged with master
public class OptioniserSettings {

	public List<String> loadIds = new LinkedList<>();
	public List<String> dischargeIds = new LinkedList<>();
	public List<String> eventsIds = new LinkedList<>();

	@JsonDeserialize(as = UserSettingsImpl.class)
	public UserSettings userSettings;
}
