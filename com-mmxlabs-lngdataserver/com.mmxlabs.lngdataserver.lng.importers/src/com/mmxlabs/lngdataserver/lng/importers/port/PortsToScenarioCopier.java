/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.port;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ports.IPortsProvider;
import com.mmxlabs.lngdataservice.client.ports.model.Port.CapabilitiesEnum;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;

public class PortsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortsToScenarioCopier.class);

	public static Command getUpdatePortsCommand(@NonNull final EditingDomain editingDomain, IPortsProvider portsProvider, @NonNull final PortModel portModel) {

		final CompoundCommand cc = new CompoundCommand("Update Port");
		Map<String, Port> portsMap = new HashMap<String, Port>();

		Map<String, List<Port>> locationToPort = portModel.getPorts().stream() //
				.filter(p -> p.getLocation() != null) //
				.filter(p -> p.getLocation().getMmxId() != null) //
				.collect(Collectors.groupingBy(p -> p.getLocation().getMmxId()));

		//
		// // Ignore port in current schedule without mmxId
		// for (final Port port: portModel.getPorts()) {
		// String mmxId = port.getMmxId();
		// if (mmxId != null && !mmxId.isEmpty()) {
		// portsMap.put(mmxId, port);
		// }
		// }

		// // New port
		// // i.e no mmxId or mmxId that can't be found in the current ports list
		// List<com.mmxlabs.lngdataserver.port.model.@NonNull Port> newPorts = portsProvider.getPorts().stream().filter((v) -> {
		// if (v.getMmxId() == null || v.getMmxId().isEmpty()) {
		// return true;
		// }
		//
		// if (v.getMmxId() != null && !v.getMmxId().isEmpty()) {
		// if(!portsMap.containsKey(v.getMmxId())) {
		// return true;
		// }
		// }
		//
		// return false;
		// }).collect(Collectors.toList());
		//
		// // Port to update
		// Collection<com.mmxlabs.lngdataserver.port.model.@NonNull Port> toUpdatePorts = portsProvider.getPorts();
		// toUpdatePorts.removeAll(newPorts);

		// // Create new ports commands
		// for(com.mmxlabs.lngdataserver.port.model.@NonNull Port port: newPorts) {
		// Port newPort = PortFactory.eINSTANCE.createPort();
		// cc.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, newPort));
		// portsMap.put(port.getMmxId(), newPort);
		// }
		//
		// Create all port update
		for (com.mmxlabs.lngdataservice.client.ports.model.@NonNull Port port : portsProvider.getPorts()) {

			String mmxID = port.getLocationMmxId();
			List<Port> lingo_ports = locationToPort.get(mmxID);
			if (lingo_ports != null) {
				for (Port lingo_port : lingo_ports) {

					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__ALLOW_COOLDOWN, port.isAllowCooldown()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__BERTHS, port.getBerths()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__CV_VALUE, port.getCvValue()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__LOAD_DURATION, port.getLoadDuration()));
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DISCHARGE_DURATION, port.getDischargeDuration()));
					if (port.getMaxCvValue() == null|| port.getMaxCvValue() == 0.0) {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MAX_CV_VALUE, SetCommand.UNSET_VALUE));
					} else {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MAX_CV_VALUE, port.getMaxCvValue()));
					}
					if (port.getMinCvValue() == null  || port.getMinCvValue() == 0.0) {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MIN_CV_VALUE, SetCommand.UNSET_VALUE));
					} else {
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MIN_CV_VALUE, port.getMinCvValue()));
					}
					{
						LocalTime t = LocalTime.parse(port.getDefaultStartTime(), DateTimeFormatter.ofPattern("H:mm:ss"));
						cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__DEFAULT_START_TIME, t.getHour()));
					}
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__MMX_ID, port.getMmxId()));

					Set<com.mmxlabs.models.lng.types.PortCapability> caps = new HashSet<>();
					if (port.getCapabilities() != null) {
						for (CapabilitiesEnum lingo_pc : port.getCapabilities()) {
							switch (lingo_pc) {
							case DISCHARGE:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.DISCHARGE);
								break;
							case DRYDOCK:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.DRYDOCK);
								break;
							case LOAD:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.LOAD);
								break;
							case MAINTENANCE:
								caps.add(com.mmxlabs.models.lng.types.PortCapability.MAINTENANCE);
								break;
							default:
								break;

							}
						}
					}
					cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__CAPABILITIES, caps));
					// cc.append(SetCommand.create(editingDomain, lingo_port, PortPackage.Literals.PORT__BERTHS, port.getDefaultStartTime()));
				}

			}

		}
		cc.append(SetCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_DATA_VERSION, portsProvider.getVersion()));

		return cc;
	}
}
