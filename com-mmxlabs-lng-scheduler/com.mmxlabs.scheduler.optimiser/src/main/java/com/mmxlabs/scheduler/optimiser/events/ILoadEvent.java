/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.events;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * A {@link ILoadEvent} represents a {@link IPortVisitEvent} where LNG is loaded
 * onto a {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ILoadEvent<T> extends IPortVisitEvent<T> {

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
