/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * {@link IScheduledEvent} defining a generated charter out opportunity starting and ending at a {@link IPort}
 * 
 * l@author Simon Goodall
 * @since 2.0
 * 
 */
public interface IGeneratedCharterOutEvent extends IScheduledEvent {

	/**
	 * Returns the {@link IPort} at which this idle event occurs.
	 * 
	 * @return
	 */
	IPort getPort();

	long getCharterOutRevenue();
}
