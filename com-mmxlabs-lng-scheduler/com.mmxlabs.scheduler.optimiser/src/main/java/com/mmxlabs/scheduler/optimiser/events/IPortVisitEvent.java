/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * {@link IScheduledEvent} defining a visit to a {@link IPort}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IPortVisitEvent<T> extends IScheduledEvent<T> {

	/**
	 * Returns the {@link IPortSlot} being visited.
	 * 
	 * @return
	 */
	IPortSlot getPortSlot();
}
