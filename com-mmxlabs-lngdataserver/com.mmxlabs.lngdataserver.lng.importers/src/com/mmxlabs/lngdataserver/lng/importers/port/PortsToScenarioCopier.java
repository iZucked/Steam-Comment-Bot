package com.mmxlabs.lngdataserver.lng.importers.port;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.serviceintegration.ports.internal.IPortsProvider;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

public class PortsToScenarioCopier {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PortsToScenarioCopier.class);
	
	public static Command getUpdatePortsCommand(@NonNull final EditingDomain editingDomain, IPortsProvider portsProvider, @NonNull final PortModel portModel) {
		
		final CompoundCommand cc = new CompoundCommand("Update Port");
		Map<String, Port> portsMap = new HashMap<String, Port>();
		
		// Ignore port in current schedule without mmxId
		for (final Port port: portModel.getPorts()) {
				String mmxId = port.getMmxId();
				if (mmxId != null && !mmxId.isEmpty()) {
					portsMap.put(mmxId, port);
				}
		}
		
		// New port
		// i.e no mmxId or mmxId that can't be found in the current ports list 
		List<com.mmxlabs.lngdataserver.port.model.@NonNull Port> newPorts = portsProvider.getPorts().stream().filter((v) -> { 
			if (v.getMmxId() == null || v.getMmxId().isEmpty()) {
				return true;
			}
			
			if (v.getMmxId() != null && !v.getMmxId().isEmpty()) {
				if(!portsMap.containsKey(v.getMmxId())) {
					return true;
				}
			}
			
			return false;	
		}).collect(Collectors.toList());
	
		// Port to update
		Collection<com.mmxlabs.lngdataserver.port.model.@NonNull Port> toUpdatePorts = portsProvider.getPorts();
		toUpdatePorts.removeAll(newPorts);
		
		// Create new ports commands
		for(com.mmxlabs.lngdataserver.port.model.@NonNull Port port: newPorts) {
			Port newPort = PortFactory.eINSTANCE.createPort();
			cc.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, newPort));
			portsMap.put(port.getMmxId(), newPort);
		}
		
		// Create all port update
		for(com.mmxlabs.lngdataserver.port.model.@NonNull Port port: portsProvider.getPorts()) {
		}

		return cc;
	}
}
