/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;

public class PortModelFinder {
	private final @NonNull PortModel portModel;

	public PortModelFinder(final @NonNull PortModel portModel) {
		this.portModel = portModel;
	}

	@NonNull
	public PortModel getPortModel() {
		return portModel;
	}

	@NonNull
	public Port findPort(@NonNull final String portName) {
		for (final Port port : getPortModel().getPorts()) {
			if (portName.equals(port.getName())) {
				return port;
			}
		}
		throw new IllegalArgumentException("Unknown port " + portName);
	}

	@NonNull
	public Route findCanal(@NonNull final String canalName) {
		for (final Route route : getPortModel().getRoutes()) {
			if (canalName.equals(route.getName())) {
				return route;
			}
		}
		throw new IllegalArgumentException("Unknown route");
	}

	public APortSet<Port> getCapabilityPortsGroup(PortCapability capabilityGroup) {
		String groupName = "All " + capabilityGroup.getName() + " Ports";
		for (CapabilityGroup g : portModel.getSpecialPortGroups()) {
			if (groupName.contentEquals(g.getName())) {
				return g;
			}
		}
		throw new IllegalStateException("Special " + capabilityGroup.getName() + " port group does not exist");
	}

	public Route findCanal(final @NonNull RouteOption option) {
		for (final Route route : getPortModel().getRoutes()) {
			if (option == route.getRouteOption()) {
				return route;
			}
		}
		throw new IllegalArgumentException("Unknown route");
	}
}
