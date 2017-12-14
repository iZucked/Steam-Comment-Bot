package com.mmxlabs.lngdataserver.lng.importers.ports;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.distances.IPortProvider;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class PortsToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortsToScenarioCopier.class);

	public static Pair<Command, Collection<Port>> getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final IPortProvider portProvider, @NonNull final PortModel portModel) {

		final CompoundCommand cmd = new CompoundCommand("Update ports");

		Map<String, Port> portMap = new HashMap<>();
		List<Port> unlinkedPorts = new LinkedList<>();
		for (final Port port : portModel.getPorts()) {
			Location l = port.getLocation();
			if (l != null) {
				String mmxId = l.getMmxId();
				if (mmxId != null && !mmxId.isEmpty()) {
					portMap.put(l.getMmxId(), port);
					continue;
				}
			}
			System.out.println("Unlinked port " + port.getName());
			unlinkedPorts.add(port);
		}

		List<Port> portsAdded = new LinkedList<>();
		List<Port> portsModified = new LinkedList<>();

		List<Port> newPorts = portProvider.getPorts();
		for (Port newPort : newPorts) {
			Location newLocation = newPort.getLocation();
			String mmxId = newLocation.getMmxId();

			final Port oldPort;
			if (portMap.containsKey(mmxId)) {
				oldPort = portMap.get(mmxId);
				portsModified.add(oldPort);
			} else {
				oldPort = PortFactory.eINSTANCE.createPort();
				oldPort.setLocation(PortFactory.eINSTANCE.createLocation());

				oldPort.setDefaultWindowSize(1);
				oldPort.setDefaultWindowSizeUnits(TimePeriod.DAYS);

				portsAdded.add(oldPort);
				cmd.append(AddCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORTS, oldPort));
			}
			cmd.append(SetCommand.create(editingDomain, oldPort, MMXCorePackage.Literals.NAMED_OBJECT__NAME, newLocation.getName()));

			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.NAMED_OBJECT__NAME, newLocation.getName()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), MMXCorePackage.Literals.OTHER_NAMES_OBJECT__OTHER_NAMES, newLocation.getOtherNames()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__COUNTRY, newLocation.getCountry()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__TIME_ZONE, newLocation.getTimeZone()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LAT, newLocation.getLat()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__LON, newLocation.getLon()));
			cmd.append(SetCommand.create(editingDomain, oldPort.getLocation(), PortPackage.Literals.LOCATION__MMX_ID, newLocation.getMmxId()));

		}

		Set<Port> portsToRemove = new HashSet<>(portModel.getPorts());
		portsToRemove.removeAll(portsModified);
		// portsToRemove.removeAll(portsadded);
		cmd.append(DeleteCommand.create(editingDomain, portsToRemove));

		cmd.append(SetCommand.create(editingDomain, portModel, PortPackage.Literals.PORT_MODEL__PORT_DATA_VERSION, portProvider.getVersion()));
		return new Pair<>(cmd, portsToRemove);

	}

}
