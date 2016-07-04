/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProviderEditor;

/**
 * Simple implementation of {@link IMarkToMarketProviderEditor}.
 * 
 * @auther Simon Goodall
 */
public class HashMapMarkToMarketProviderEditor implements IMarkToMarketProviderEditor {

	private final Map<ISequenceElement, IMarkToMarket> marketMap = new HashMap<ISequenceElement, IMarkToMarket>();

	@Override
	@Nullable
	public IMarkToMarket getMarketForElement(@NonNull final ISequenceElement element) {
		return marketMap.get(element);
	}

	@Override
	public void setMarkToMarketForElement(@NonNull final ISequenceElement element, @NonNull final IMarkToMarket markToMarket) {
		marketMap.put(element, markToMarket);
	}
}
