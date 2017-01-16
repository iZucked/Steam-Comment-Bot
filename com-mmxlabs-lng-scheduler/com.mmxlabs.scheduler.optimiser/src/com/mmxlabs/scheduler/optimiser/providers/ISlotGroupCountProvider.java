/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

public interface ISlotGroupCountProvider extends IDataComponentProvider {

	/**
	 * Returns a {@link Collection} of all the {@link SlotGroup}s contained within this provider.
	 * 
	 * @return
	 */
	Collection<SlotGroup> getGroups();

	/**
	 * Returns all the {@link SlotGroup}s this element is part of, or an empty list if there are none.
	 * 
	 * @param element
	 * @return
	 */
	Collection<SlotGroup> getGroupsForElement(ISequenceElement element);

}
