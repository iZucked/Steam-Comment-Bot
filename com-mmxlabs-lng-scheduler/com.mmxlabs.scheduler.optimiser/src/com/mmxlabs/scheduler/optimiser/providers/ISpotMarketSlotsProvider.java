/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;

/**
 * Provider for spot market slot information.
 * 
 * @author Alex Churchill
 *
 */
@NonNullByDefault
public interface ISpotMarketSlotsProvider extends IDataComponentProvider {

	boolean isSpotMarketSlot(ISequenceElement element);

	boolean isSpotMarketSlot(IPortSlot element);

	String getMarketDateKey(ISequenceElement element);

	String getMarketDateKey(IPortSlot portSlot);

	ISpotMarket getSpotMarket(ISequenceElement element);

	ISpotMarket getSpotMarket(IPortSlot portSlot);
}
