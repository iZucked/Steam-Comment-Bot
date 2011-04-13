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
	public ITimeWindow getTimeWindow();
	public int getDurationHours();
	public IPort getPort();
}
