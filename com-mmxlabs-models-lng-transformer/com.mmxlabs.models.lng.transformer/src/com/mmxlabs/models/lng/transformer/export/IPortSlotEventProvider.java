/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

@NonNullByDefault
public interface IPortSlotEventProvider {

	<T> void addEventToPortSlot(IPortSlot portSlot, Class<T> clazz, T o);

	<T> @Nullable T getEventFromPortSlot(IPortSlot portSlot, Class<T> clazz);

}