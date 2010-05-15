package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.IPort;

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
	 * Returns the {@link IPort} being visited.
	 * 
	 * @return
	 */
	IPort getPort();
}
