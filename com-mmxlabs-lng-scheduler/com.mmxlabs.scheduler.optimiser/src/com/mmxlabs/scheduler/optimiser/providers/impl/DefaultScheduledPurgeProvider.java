/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IScheduledPurgeProviderEditor;

@NonNullByDefault
public class DefaultScheduledPurgeProvider implements IScheduledPurgeProviderEditor {

	private final Set<ISequenceElement> scheduledElements = new HashSet<>();
	private final Set<IPortSlot> scheduledPortSlots = new HashSet<>();

	@Override
	public boolean isPurgeScheduled(final ISequenceElement element) {
		return scheduledElements.contains(element);
	}

	@Override
	public boolean isPurgeScheduled(final IPortSlot portSlot) {
		return scheduledPortSlots.contains(portSlot);
	}

	@Override
	public void setPurgeScheduled(final ISequenceElement element, final ILoadSlot slot) {
		scheduledElements.add(element);
		scheduledPortSlots.add(slot);
	}
}
