/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;

/**
 * Provider for determining whether or not an element is a spot market slot.
 * 
 * @author achurchill
 *
 */
@NonNullByDefault
public interface ISpotMarketSlotsProviderEditor extends ISpotMarketSlotsProvider {

	void setSpotMarketSlot(ISequenceElement element, IPortSlot portSlot, ISpotMarket spotMarket, String marketDateKey);
}
