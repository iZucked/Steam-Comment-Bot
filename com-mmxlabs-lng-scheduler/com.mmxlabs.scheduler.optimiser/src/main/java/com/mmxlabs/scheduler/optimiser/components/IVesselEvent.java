/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a vessel event such as a charter out or dry-dock visit
 * @author Tom Hinton
 *
 */
public interface IVesselEvent {
	/**
	 * This is the time window in which the vessel must arrive at the start port
	 * @return
	 */
	public ITimeWindow getTimeWindow();
	/**
	 * The duration in hours of the event
	 * @return
	 */
	public int getDurationHours();
	/**
	 * The port at which this vessel event starts
	 * @return
	 */
	public IPort getStartPort();
	
	/**
	 * The port at which the vessel will be after the event
	 * @return
	 */
	public IPort getEndPort();
	
	/**
	 * The maximum volume of LNG available for travel after the event
	 * @return
	 */
	public long getMaxHeelOut();
	/**
	 * The CV Value of any LNG available for travel after the event.
	 * @return
	 */
	public int getHeelCVValue();
}
