/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

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
	 * 
	 * @param port
	 * @param vessel
	 * @param type
	 * @param from
	 */
	public void setPortCost(final @NonNull IPort port, final @NonNull IVessel vessel, final @NonNull PortType type, final long cost);
}
