/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;

/**
 * 
 */
public interface IMarkToMarketProvider extends IDataComponentProvider {

	@Nullable
	IMarkToMarket getMarketForElement(@NonNull ISequenceElement element);

}
