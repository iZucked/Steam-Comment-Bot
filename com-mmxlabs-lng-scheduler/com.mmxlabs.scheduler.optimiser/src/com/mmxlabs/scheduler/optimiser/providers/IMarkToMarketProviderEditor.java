/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;

/**
 */
public interface IMarkToMarketProviderEditor extends IMarkToMarketProvider {

	void setMarkToMarketForElement(@NonNull ISequenceElement element, @NonNull IMarkToMarket markToMarket);
}
