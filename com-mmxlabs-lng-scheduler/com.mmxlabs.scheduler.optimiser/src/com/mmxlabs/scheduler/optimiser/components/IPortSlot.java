/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Represents a single time slot at a given port.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortSlot {

	String getId();

	IPort getPort();

	ITimeWindow getTimeWindow();

	PortType getPortType();
}
