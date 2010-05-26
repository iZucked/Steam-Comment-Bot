package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * {@link IScheduledEvent} defining a period of idle time at a {@link IPort}
 * 
 * l@author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IIdleEvent<T> extends IScheduledEvent<T> {

	/**
	 * Returns the {@link IPort} at which this idle event occurs.
	 * 
	 * @return
	 */
	IPort getPort();
}
