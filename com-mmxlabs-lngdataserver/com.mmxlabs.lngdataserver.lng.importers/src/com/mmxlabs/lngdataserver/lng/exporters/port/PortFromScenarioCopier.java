package com.mmxlabs.lngdataserver.lng.exporters.port;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.port.model.Port.CapabilitiesEnum;
import com.mmxlabs.lngdataservice.ports.model.Port;
import com.mmxlabs.lngdataservice.ports.model.Version;
import com.mmxlabs.models.lng.port.PortModel;

public class PortFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortFromScenarioCopier.class);

	public static List<com.mmxlabs.lngdataserver.port.model. Port> generateVersion(PortModel portModel) {

		Version version = new Version();

		 List<com.mmxlabs.lngdataserver.port.model. Port>  ports = new LinkedList<>();
		for (com.mmxlabs.models.lng.port.Port lingo_port : portModel.getPorts()) {
			com.mmxlabs.lngdataserver.port.model.  Port port = new  com.mmxlabs.lngdataserver.port.model.  Port();
			port.setLocationMmxId(lingo_port.getLocation().getMmxId());
			port.setAllowCooldown(lingo_port.isAllowCooldown());
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

			port.setCapabilities(new LinkedList<>());
			for (com.mmxlabs.models.lng.types.PortCapability lingo_pc : lingo_port.getCapabilities()) {
				switch (lingo_pc) {
				case DISCHARGE:
					port.getCapabilities().add(CapabilitiesEnum.DISCHARGE);
					break;
				case DRYDOCK:
					port.getCapabilities().add(CapabilitiesEnum.DRYDOCK);
					break;
				case LOAD:
					port.getCapabilities().add(CapabilitiesEnum.LOAD);
					break;
				case MAINTENANCE:
					port.getCapabilities().add(CapabilitiesEnum.MAINTENANCE);
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

		return ports;
	}
}
