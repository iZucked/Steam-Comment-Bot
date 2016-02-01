/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface IPortSlotEventProvider {

	<T> void addEventToPortSlot(IPortSlot portSlot, Class<T> clazz, T o);

	<T> T getEventFromPortSlot(IPortSlot portSlot, Class<T> clazz);

}