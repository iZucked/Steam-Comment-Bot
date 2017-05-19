/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Map;
import java.util.SortedSet;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * An editor interface for {@link IPanamaSlotsProvider}
 * 
 * @author R
 */
public interface IPanamaSlotsProviderEditor extends IPanamaSlotsProvider {
	/**
	 * Sets the slots overwriting existing ones.
	 */
	void setSlots(Map<IPort, SortedSet<Integer>> slots);
}
