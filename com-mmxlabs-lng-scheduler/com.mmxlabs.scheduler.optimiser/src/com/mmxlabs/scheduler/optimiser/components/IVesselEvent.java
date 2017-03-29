/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a vessel event such as a charter out or dry-dock visit
 * 
 * @author Tom Hinton
 * 
 */
public interface IVesselEvent {
	/**
	 * This is the time window in which the vessel must arrive at the start port
	 * 
	 * @return
	 */
	@Nullable ITimeWindow getTimeWindow();

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
	@NonNull
	IPort getStartPort();

	/**
	 * The port at which the vessel will be after the event
	 * 
	 * @return
	 */
	@NonNull
	IPort getEndPort();

}
