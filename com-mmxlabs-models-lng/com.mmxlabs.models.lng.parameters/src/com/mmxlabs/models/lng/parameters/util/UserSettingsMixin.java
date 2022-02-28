/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonIgnoreProperties({ "setPeriodEnd", "setPeriodStartDate", "buildActionSets" })
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(as = UserSettingsImpl.class)
public class UserSettingsMixin {

}
