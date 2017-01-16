/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.Set;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * A total volume limit; specifies a time window, a set of candidate {@link IPortSlot}s, and a volume limit. For the subset of portslots visited in the given time window, the total volume
 * loaded/discharged may not exceed the volume limit.
 * 
 * @author hinton
 * 
 */
public interface ITotalVolumeLimit {
	/**
	 * The set of slots which this <em>might</em> apply to
	 */
	public Set<IPortSlot> getPossibleSlots();

	/**
	 * The maximum volume allowed
	 * 
	 * @return
	 */
	public long getVolumeLimit();

	/**
	 * The arrival time window for which the constraint applies; window is <em>inclusive</em> at <em>both ends</em>.
	 * 
	 * @return
	 */
	public ITimeWindow getTimeWindow();
}
