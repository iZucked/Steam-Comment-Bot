/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui;

import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.ui.dnd.IChangeSource;

public class ChangeDescriptionSource implements IChangeSource {

	private final ChangeDescription changeDescription;
	private final UserSettings userSettings;
	private final String name;

	public ChangeDescriptionSource(final String name, final ChangeDescription changeDescription, final UserSettings userSettings) {
		this.name = name;
		this.changeDescription = changeDescription;
		this.userSettings = userSettings;
	}

	public ChangeDescription getChangeDescription() {
		return changeDescription;
	}

	public UserSettings getUserSettings() {
		return userSettings;
	}

	public String getName() {
		return name;
	}

}
