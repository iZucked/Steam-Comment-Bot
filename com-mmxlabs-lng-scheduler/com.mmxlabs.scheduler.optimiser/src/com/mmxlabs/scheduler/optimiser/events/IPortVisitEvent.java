/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IScheduledEvent} defining a visit to a {@link IPort}.
 * 
 * @author Simon Goodall
 */
public interface IPortVisitEvent extends IScheduledEvent, IFuelUsingEvent {

	/**
	 * Returns the {@link IPortSlot} being visited.
	 * 
	 * @return
	 */
	IPortSlot getPortSlot();
}
