/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.port;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ports.model.Port;
import com.mmxlabs.lngdataserver.integration.ports.model.PortCapability;
import com.mmxlabs.lngdataserver.integration.ports.model.PortsVersion;
import com.mmxlabs.lngdataserver.integration.ports.model.TimePeriod;
import com.mmxlabs.models.lng.port.PortModel;

public class PortFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortFromScenarioCopier.class);

	public static @NonNull PortsVersion generateVersion(@NonNull PortModel portModel) {

		PortsVersion version = new PortsVersion();

		List<Port> ports = new LinkedList<>();
		for (com.mmxlabs.models.lng.port.Port lingo_port : portModel.getPorts()) {
			Port port = new Port();
			port.setLocationMmxId(lingo_port.getLocation().getMmxId());
			port.setAllowCooldown(lingo_port.isAllowCooldown());
			port.setDefaultStartTime(LocalTime.of(lingo_port.getDefaultStartTime(), 0));
			port.setBerths(lingo_port.getBerths());
			port.setCvValue(lingo_port.getCvValue());
			port.setLoadDuration(lingo_port.getLoadDuration());
			port.setDischargeDuration(lingo_port.getDischargeDuration());
			port.setDefaultWindowSize(lingo_port.getDefaultWindowSize());
			port.setDefaultWindowSizeUnits(map(lingo_port.getDefaultWindowSizeUnits()));
			if (lingo_port.isSetMinCvValue()) {
				port.setMinCvValue(lingo_port.getMinCvValue());
			}
			if (lingo_port.isSetMaxCvValue()) {
				port.setMaxCvValue(lingo_port.getMaxCvValue());
			}

			port.setCapabilities(new LinkedHashSet<>());
			for (com.mmxlabs.models.lng.types.PortCapability lingo_pc : lingo_port.getCapabilities()) {
				switch (lingo_pc) {
				case DISCHARGE:
					port.getCapabilities().add(PortCapability.DISCHARGE);
					break;
				case DRYDOCK:
					port.getCapabilities().add(PortCapability.DRYDOCK);
					break;
				case LOAD:
					port.getCapabilities().add(PortCapability.LOAD);
					break;
				case MAINTENANCE:
					port.getCapabilities().add(PortCapability.MAINTENANCE);
					break;
				case TRANSFER:
					break;
				default:
					break;

				}
			}
			ports.add(port);

		}

		version.setIdentifier(portModel.getPortVersionRecord().getVersion());
		version.setCreatedBy(portModel.getPortVersionRecord().getCreatedBy());
		version.setCreatedAt(portModel.getPortVersionRecord().getCreatedAt());
	 
		version.setPorts(ports);

		return version;
	}

	private static TimePeriod map(com.mmxlabs.models.lng.types.TimePeriod period) {
		switch (period) {
		case DAYS:
			return TimePeriod.DAYS;
		case HOURS:
			return TimePeriod.HOURS;
		case MONTHS:
			return TimePeriod.MONTHS;
		}
		throw new IllegalArgumentException();
	}
}
