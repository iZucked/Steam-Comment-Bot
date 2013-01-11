/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;


/**
 * DCP editor for setting port costs.
 * 
 * @author hinton
 *
 */
public interface IPortCostProviderEditor extends IPortCostProvider {
	/**
	 * Set the port cost for visiting this port in a vessel of this class etc. from the given time onward
	 * @param port
	 * @param vesselClass
	 * @param type
	 * @param from
	 */
	public void setPortCost(final IPort port, final IVessel vessel, final PortType type, final long cost);
}
