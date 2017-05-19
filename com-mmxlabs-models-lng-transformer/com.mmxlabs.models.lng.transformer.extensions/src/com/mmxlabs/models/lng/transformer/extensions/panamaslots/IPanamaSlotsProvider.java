/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * 
 * @author Robert Erdin
 */
public interface IPanamaSlotsProvider extends IDataComponentProvider {
	
	ImmutableMap<IPort, ImmutableSortedSet<Integer>> getSlots();
}
