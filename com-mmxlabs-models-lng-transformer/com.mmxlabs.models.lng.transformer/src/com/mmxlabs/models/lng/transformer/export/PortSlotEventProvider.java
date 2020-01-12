/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class PortSlotEventProvider implements IPortSlotEventProvider {

	private final Map<Pair<IPortSlot, Class<?>>, Object> portSlotToEventMap = new HashMap<>();

	@Override
	public <T> void addEventToPortSlot(final IPortSlot portSlot, final Class<T> clazz, final T o) {
		portSlotToEventMap.put(new Pair<>(portSlot, clazz), o);
	}

	@Override
	public <T> T getEventFromPortSlot(final IPortSlot portSlot, final Class<T> clazz) {
		return clazz.cast(portSlotToEventMap.get(new Pair<>(portSlot, clazz)));
	}
}
