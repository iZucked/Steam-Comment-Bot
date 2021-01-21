/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Interface defining an event in the schedul. Each element is bound to a sequence element and defines events at or before the element.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScheduledEvent extends IElementAnnotation {

	/**
	 * Return the sequence element this event is bound to.
	 * 
	 * @return
	 */
	ISequenceElement getSequenceElement();

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
	 * Returns the duration of this event. This should be the same as {@link #getEndTime()} - {@link #getStartTime()}
	 * 
	 * @return
	 */
	int getDuration();

	/**
	 * Returns the chartering cost for this event.
	 * 
	 * @return
	 */
	long getCharterCost();

	/**
	 * Returns the name of this event.
	 * 
	 * @return
	 */
	String getName();
}
