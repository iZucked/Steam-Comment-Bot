package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * {@link IScheduledEvent} defining a journey or travel between ports.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IJourneyEvent<T> extends IScheduledEvent<T> {

	/**
	 * Returns the originating {@link IPort} for this journey.
	 * 
	 * @return
	 */
	IPort getFromPort();

	/**
	 * Returns the destination {@link IPort} for this journey.
	 * 
	 * @return
	 */
	IPort getToPort();

	/**
	 * Returns the distance of this journey.
	 * 
	 * @return
	 */
	int getDistance();
}
