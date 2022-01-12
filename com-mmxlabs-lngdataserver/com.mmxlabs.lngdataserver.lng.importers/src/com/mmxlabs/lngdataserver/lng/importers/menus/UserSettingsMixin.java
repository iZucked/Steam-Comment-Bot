package com.mmxlabs.lngdataserver.lng.importers.menus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({"setPeriodEnd", "setPeriodStartDate"})
@JsonInclude(Include.NON_NULL)
public class UserSettingsMixin {

}
