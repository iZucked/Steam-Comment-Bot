/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components;

/**
 * A requirement that a vessel's journey start or end in a particular location at a particular time.
 * 
 * @author hinton
 *
 */
public interface IStartEndRequirement {
	/**
	 * If the vessel must start/end at a given location this is true; otherwise false
	 * @return
	 */
	public boolean hasPortRequirement();
	/**
	 * If and only if there is an earliest start time or latest arrival time this is true.
	 * @return
	 */
	public boolean hasTimeRequirement();
	/**
	 * The earliest start time or latest arrival time for this vessel.
	 * @return
	 */
	public int getTime();
	/**
	 * The location from which this vessel must start or to which it must return
	 * @return
	 */
	public IPort getLocation();
}
