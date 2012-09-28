/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a vessel event such as a charter out or dry-dock visit
 * 
 * @author Tom Hinton
 * 
 */
public interface IVesselEvent extends IHeelOptions {
	/**
	 * This is the time window in which the vessel must arrive at the start port
	 * 
	 * @return
	 */
	public ITimeWindow getTimeWindow();

	/**
	 * The duration in hours of the event
	 * 
	 * @return
	 */
	public int getDurationHours();

	/**
	 * The port at which this vessel event starts
	 * 
	 * @return
	 */
	public IPort getStartPort();

	/**
	 * The port at which the vessel will be after the event
	 * 
	 * @return
	 */
	public IPort getEndPort();

	// /**
	// * The maximum volume of LNG available for travel after the event
	// * @return
	// */
	// public long getMaxHeelOut();
	// /**
	// * The CV Value of any LNG available for travel after the event.
	// * @return
	// */
	// public int getHeelCVValue();

	/**
	 * Returns the repositioning fee for charter out events.
	 * 
	 * @return
	 * @since 2.0
	 */
	long getRepositioning();

	/**
	 * Returns the total hire cost for charter out events.
	 * 
	 * @return
	 * @since 2.0
	 */
	long getHireCost();

	/**
	 * Sets the repositioning fee for charter out events.
	 * 
	 * @since 2.0
	 */
	void setRepositioning(long repositioning);

	/**
	 * Set the total hire cost for charter out events.
	 * 
	 * @since 2.0
	 */
	void setHireCost(long hireCost);
}
