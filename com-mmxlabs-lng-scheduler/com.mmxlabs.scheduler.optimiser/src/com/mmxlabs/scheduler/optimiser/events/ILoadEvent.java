/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * A {@link ILoadEvent} represents a {@link IPortVisitEvent} where LNG is loaded onto a {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILoadEvent extends IPortVisitEvent {

	/**
	 * Returns the volume loaded at this event.
	 * 
	 * @return
	 */
	long getLoadVolume();

	/**
	 * Returns the unit purchase price for this event.
	 * 
	 * @return
	 */
	long getPurchasePrice();

}
