/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;

public interface ISlotGroupCountProviderEditor extends ISlotGroupCountProvider {

	SlotGroup createSlotGroup(Collection<ISequenceElement> elements, int count);

}
