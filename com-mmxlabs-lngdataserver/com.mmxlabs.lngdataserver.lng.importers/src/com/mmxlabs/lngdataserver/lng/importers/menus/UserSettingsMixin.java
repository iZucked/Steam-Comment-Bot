package com.mmxlabs.lngdataserver.lng.importers.menus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;

@JsonIgnoreProperties({ "setPeriodEnd", "setPeriodStartDate" })
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(as = UserSettingsImpl.class)
public class UserSettingsMixin {

}
