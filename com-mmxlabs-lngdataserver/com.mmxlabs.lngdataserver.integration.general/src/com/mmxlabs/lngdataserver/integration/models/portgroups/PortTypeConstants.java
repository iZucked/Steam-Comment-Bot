/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.models.portgroups;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.types.APortSet;

public final class PortTypeConstants {
	private PortTypeConstants() {
	}

	public static final String PORT_PREFIX = "Port:";
	public static final String PORT_GROUP_PREFIX = "PortGroup:";
	public static final String COUNTRY_GROUP_PREFIX = "PortCountry:";

	public static String encode(APortSet<Port> entry) {
		if (entry instanceof Port) {
			Port port = (Port) entry;
			return PortTypeConstants.PORT_PREFIX + port.getLocation().getMmxId();
		} else if (entry instanceof PortGroup) {
			return PortTypeConstants.PORT_GROUP_PREFIX + entry.getName();
		} else if (entry instanceof PortCountryGroup) {
			return PortTypeConstants.COUNTRY_GROUP_PREFIX + entry.getName();
		} else {
			throw new IllegalStateException("Unknown portset type");
		}
	}
}
