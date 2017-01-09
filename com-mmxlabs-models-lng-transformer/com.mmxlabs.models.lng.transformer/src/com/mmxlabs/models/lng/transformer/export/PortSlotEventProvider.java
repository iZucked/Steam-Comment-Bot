/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class PortSlotEventProvider implements IPortSlotEventProvider {
	Map<Pair<IPortSlot, Class>, Object> portSlotToEventMap = new HashMap<Pair<IPortSlot, Class>, Object>();
	
	@Override
	public <T> void addEventToPortSlot(IPortSlot portSlot, Class<T> clazz, T o) {
		portSlotToEventMap.put(new Pair<IPortSlot, Class>(portSlot, clazz), o);
	}
	
	@Override
	public <T> T getEventFromPortSlot(IPortSlot portSlot, Class<T> clazz) {
		T o = (T) portSlotToEventMap.get(new Pair<IPortSlot, Class>(portSlot, clazz));
		return o;
	}
}
