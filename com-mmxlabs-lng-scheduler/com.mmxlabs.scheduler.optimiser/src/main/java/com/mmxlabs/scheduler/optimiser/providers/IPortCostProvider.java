/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * DCP which gives you the cost of visiting a given port in a given vessel class at a given time
 * to perform a given activity
 * 
 * @author hinton
 *
 */
public interface IPortCostProvider extends IDataComponentProvider {
	/**
	 * Returns the cost (in scaled dollars) of visiting the given port in the given vessel
	 * to do an activity of the given type
	 * 
	 * @param port
	 * @param vessel
	 * @param portType
	 * @return
	 */
	public long getPortCost(final IPort port, final IVessel vessel, final PortType portType);
}
