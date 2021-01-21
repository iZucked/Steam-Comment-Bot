/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotMarket;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProviderEditor;

@NonNullByDefault
public final class HashMapSpotMarketSlotsEditor implements ISpotMarketSlotsProviderEditor {

	private static class Record {
		ISpotMarket spotMarket;
		String dateKey;

		public Record(final ISpotMarket spotMarket, final String dateKey) {
			this.spotMarket = spotMarket;
			this.dateKey = dateKey;

		}
	}

	private final Map<ISequenceElement, Record> elementMap = new HashMap<>();
	private final Map<IPortSlot, Record> portSlotMap = new HashMap<>();
	private final Map<ISpotMarket, List<ISequenceElement>> marketToElementsMap = new HashMap<>();
	private final Map<ISpotMarket, List<IPortSlot>> marketToPortSlotsMap = new HashMap<>();

	@Override
	public void setSpotMarketSlot(final ISequenceElement sequenceElement, final IPortSlot portSlot, final ISpotMarket spotMarket, final String dateKey) {
		final Record record = new Record(spotMarket, dateKey);

		elementMap.put(sequenceElement, record);
		portSlotMap.put(portSlot, record);

		marketToElementsMap.computeIfAbsent(spotMarket, m -> new LinkedList<>()).add(sequenceElement);
		marketToPortSlotsMap.computeIfAbsent(spotMarket, m -> new LinkedList<>()).add(portSlot);
	}

	@Override
	public boolean isSpotMarketSlot(@NonNull final ISequenceElement element) {
		return elementMap.containsKey(element);
	}

	@Override
	public boolean isSpotMarketSlot(@NonNull final IPortSlot portSlot) {
		return portSlotMap.containsKey(portSlot);
	}

	@Override
	public @NonNull String getMarketDateKey(@NonNull final ISequenceElement element) {
		return elementMap.get(element).dateKey;
	}

	@Override
	public @NonNull String getMarketDateKey(@NonNull final IPortSlot portSlot) {
		return portSlotMap.get(portSlot).dateKey;
	}

	@Override
	public @NonNull ISpotMarket getSpotMarket(@NonNull final ISequenceElement element) {
		return elementMap.get(element).spotMarket;

	}

	@Override
	public @NonNull ISpotMarket getSpotMarket(@NonNull final IPortSlot portSlot) {
		return portSlotMap.get(portSlot).spotMarket;
	}

	@Override
	public List<ISequenceElement> getElementsFor(final ISpotMarket market) {
		return Collections.unmodifiableList(marketToElementsMap.getOrDefault(market, Collections.emptyList()));
	}

	@Override
	public List<IPortSlot> getPortSlotsFor(final ISpotMarket market) {
		return Collections.unmodifiableList(marketToPortSlotsMap.getOrDefault(market, Collections.emptyList()));
	}

	@Override
	public boolean isEquivalentOption(final ISequenceElement a, final ISequenceElement b) {

		if (getSpotMarket(a) == getSpotMarket(b)) {
			final String key_a = getMarketDateKey(a);
			final String key_b = getMarketDateKey(b);
			return Objects.equals(key_a, key_b);
		}

		return false;
	}
}
