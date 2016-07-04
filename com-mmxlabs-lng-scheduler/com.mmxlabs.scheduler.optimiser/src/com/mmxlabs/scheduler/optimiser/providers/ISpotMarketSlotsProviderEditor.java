/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
/**
 * Provider for determining whether or not an element is a spot market slot.
 * @author achurchill
 *
 */
public interface ISpotMarketSlotsProviderEditor extends ISpotMarketSlotsProvider {

	void setSpotMarketSlot(ISequenceElement element, boolean isSpotMarketSlot);
}
