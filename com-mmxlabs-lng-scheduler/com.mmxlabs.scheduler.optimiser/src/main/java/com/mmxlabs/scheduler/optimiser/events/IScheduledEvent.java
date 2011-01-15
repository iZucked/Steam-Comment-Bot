/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;

/**
 * Interface defining an event in the schedule generated by a
 * {@link ISequenceScheduler}. Each element is bound to a sequence element and
 * defines events at or before the element.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public interface IScheduledEvent<T> {

	/**
	 * Return the sequence element this event is bound to.
	 * 
	 * @return
	 */
	T getSequenceElement();

	/**
	 * Returns the start time of this event.
	 * 
	 * @return
	 */
	int getStartTime();

	/**
	 * Returns the end time of this event.
	 * 
	 * @return
	 */
	int getEndTime();

	/**
	 * Returns the duration of this event. This should be the same as
	 * {@link #getEndTime()} - {@link #getStartTime()}
	 * 
	 * @return
	 */
	int getDuration();

	/**
	 * Returns the name of this event.
	 * 
	 * @return
	 */
	String getName();
}
