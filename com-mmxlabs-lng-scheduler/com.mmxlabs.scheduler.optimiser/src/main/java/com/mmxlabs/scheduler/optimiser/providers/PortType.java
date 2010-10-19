/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Enum specifying different types of port. These are intended to be linked to a
 * sequence element rather than directly to a {@link IPort} as a port may
 * provide more than one service. However a sequence element is a particular
 * service at a particular time.
 * 
 * @author Simon Goodall
 * 
 */
public enum PortType {
	/**
	 * Unknown or unspecified port type.
	 */
	Unknown,

	/**
	 * Port type is a start port. I.e. the first element in the sequence.
	 */
	Start,

	/**
	 * Port type is an end port. I.e. the last element in the sequence.
	 */
	End,

	/**
	 * Port type is a Load event.
	 */
	Load,

	/**
	 * Port type is a discharge event
	 */
	Discharge,

	/**
	 * Port type is a dry dock event.
	 */
	DryDock,

	/**
	 * Port type is a waypoint, i.e. a routing point optionally associated with
	 * a {@link ITimeWindow}
	 */
	Waypoint,

	/**
	 * Port type is a charter out, which will have a start time window
	 * and duration.
	 */
	CharterOut,
	
	/**
	 * Generic port type.
	 */
	Other;
}