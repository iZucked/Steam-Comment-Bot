/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Provider for determining whether or not an element is a spot market slot.
 * @author achurchill
 *
 */
public interface ISpotMarketSlotsProvider extends IDataComponentProvider {

	boolean isSpotMarketSlot(ISequenceElement element);
}
