/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.exporters.port;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataservice.client.ports.model.Port;
import com.mmxlabs.lngdataservice.client.ports.model.Version;
import com.mmxlabs.models.lng.port.PortModel;

public class PortFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortFromScenarioCopier.class);

	public static @NonNull Version generateVersion(@NonNull PortModel portModel) {

		Version version = new Version();

		List<Port> ports = new LinkedList<>();
		for (com.mmxlabs.models.lng.port.Port lingo_port : portModel.getPorts()) {
			Port port = new Port();
			port.setLocationMmxId(lingo_port.getLocation().getMmxId());
			port.setAllowCooldown(lingo_port.isAllowCooldown());
			port.setDefaultStartTime(String.format("%02d:00:00", lingo_port.getDefaultStartTime()));
			port.setBerths(lingo_port.getBerths());
			port.setCvValue(lingo_port.getCvValue());
			port.setLoadDuration(lingo_port.getLoadDuration());
			port.setDischargeDuration(lingo_port.getDischargeDuration());
			if (lingo_port.isSetMinCvValue()) {
				port.setMinCvValue(lingo_port.getMinCvValue());
			}
			if (lingo_port.isSetMaxCvValue()) {
				port.setMaxCvValue(lingo_port.getMaxCvValue());
			}

			port.setCapabilities(new ArrayList<Port.CapabilitiesEnum>());
			for (com.mmxlabs.models.lng.types.PortCapability lingo_pc : lingo_port.getCapabilities()) {
				switch (lingo_pc) {
				case DISCHARGE:
					port.getCapabilities().add(Port.CapabilitiesEnum.DISCHARGE);
					break;
				case DRYDOCK:
					port.getCapabilities().add(Port.CapabilitiesEnum.DRYDOCK);
					break;
				case LOAD:
					port.getCapabilities().add(Port.CapabilitiesEnum.LOAD);
					break;
				case MAINTENANCE:
					port.getCapabilities().add(Port.CapabilitiesEnum.MAINTENANCE);
					break;
				case TRANSFER:
					break;
				default:
					break;

				}
			}
			ports.add(port);

		}

		String portDataVersion = portModel.getPortDataVersion();
		if (portDataVersion == null) {
			portDataVersion = "private-" + EcoreUtil.generateUUID();
			portModel.setPortDataVersion(portDataVersion);
		}
		version.setIdentifier(portDataVersion);
		version.setCreatedAt(AbstractDataRepository.DATE_FORMATTER.format(LocalDateTime.now()));
		version.setLocationVersion(portModel.getDistanceDataVersion());
		version.setPorts(ports);

		return version;
	}
}
