/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.portgroups;

import com.mmxlabs.lngdataserver.integration.general.model.portgroups.PortGroupDefinition;
import com.mmxlabs.lngdataserver.integration.general.model.portgroups.PortGroupsVersion;
import com.mmxlabs.lngdataserver.integration.general.model.portgroups.PortTypeConstants;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.APortSet;

public class PortGroupsFromScenarioCopier {
	private PortGroupsFromScenarioCopier() {

	}

	public static PortGroupsVersion generateVersion(final PortModel portModel) {

		final PortGroupsVersion version = new PortGroupsVersion();
		version.setIdentifier(portModel.getPortGroupVersionRecord().getVersion());
		version.setCreatedBy(portModel.getPortGroupVersionRecord().getCreatedBy());
		version.setCreatedAt(portModel.getPortGroupVersionRecord().getCreatedAt());
	 

		for (final PortGroup portGroup : portModel.getPortGroups()) {
			final PortGroupDefinition def = new PortGroupDefinition();
			def.setName(portGroup.getName());

			for (APortSet<Port> entry : portGroup.getContents()) {
				def.getEntries().add(PortTypeConstants.encode(entry));
			}

			version.getGroups().add(def);
		}

		return version;
	}
}
