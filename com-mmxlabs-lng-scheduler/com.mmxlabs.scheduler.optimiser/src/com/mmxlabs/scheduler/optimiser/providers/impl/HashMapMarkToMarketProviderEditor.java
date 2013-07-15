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
 * @since 6.0
 */
public class HashMapMarkToMarketProviderEditor implements IMarkToMarketProviderEditor {

	private final String name;

	private final Map<ISequenceElement, IMarkToMarket> marketMap = new HashMap<ISequenceElement, IMarkToMarket>();

	public HashMapMarkToMarketProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	@Nullable
	public IMarkToMarket getMarketForElement(@NonNull final ISequenceElement element) {
		return marketMap.get(element);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		marketMap.clear();
	}

	@Override
	public void setMarkToMarketForElement(@NonNull final ISequenceElement element, @NonNull final IMarkToMarket markToMarket) {
		marketMap.put(element, markToMarket);
	}
}
