package com.mmxlabs.lngdataserver.lng.exporters.port;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataservice.ports.model.Port;
import com.mmxlabs.lngdataservice.ports.model.PortCapability;
import com.mmxlabs.lngdataservice.ports.model.Version;
import com.mmxlabs.models.lng.port.PortModel;

public class PortFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortFromScenarioCopier.class);

	public static Version generateVersion(PortModel portModel) {

		Version version = new Version();

		for (com.mmxlabs.models.lng.port.Port lingo_port : portModel.getPorts()) {
			List<Port> ports = version.getPorts();
			Port port = new Port();
			port.setLocationMmxId(lingo_port.getMmxId());
			port.setAllowCooldown(lingo_port.isAllowCooldown());
			port.setBerths(lingo_port.getBerths());
			port.setCvValue(lingo_port.getCvValue());
			port.setLoadDuration(lingo_port.getLoadDuration());
			port.setDischargeDuration(lingo_port.getDischargeDuration());
			port.setMinCvValue(lingo_port.getMinCvValue());
			port.setMaxCvValue(lingo_port.getMaxCvValue());

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
	 
		String portDataVersion = portModel.getPortDataVersion();
		if (portDataVersion == null) {
			portDataVersion = EcoreUtil.generateUUID();
			portModel.setPortDataVersion(portDataVersion);
		}
		version.setIdentifier(portDataVersion);
		version.setCreatedAt(LocalDateTime.now());

		return version;
	}
}
