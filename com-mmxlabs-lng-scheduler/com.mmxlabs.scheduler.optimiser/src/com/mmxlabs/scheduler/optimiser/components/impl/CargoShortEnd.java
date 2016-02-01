/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 */
public class CargoShortEnd extends PortSlot {

	public CargoShortEnd(final String id, final IPort port) {
		setPortType(PortType.Short_Cargo_End);
		setPort(port);
		setId(id);
	}
}
