/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProviderEditor;

@NonNullByDefault
public class DefaultExtraIdleTimeProviderEditor implements IExtraIdleTimeProviderEditor {

	private static final Integer ZERO = Integer.valueOf(0);

	private final Map<Pair<IPort, IPort>, Integer> portPairSlotToIdleTime = new HashMap<>();
	private final Map<IPortSlot, Integer> portSlotToIdleTimeBeforeVisit = new HashMap<>();

	@Override
	public int getContingencyIdleTimeInHours(final IPortSlot fromPortSlot, final IPortSlot toPortSlot) {
		int portPairSlotIdleTime = portPairSlotToIdleTime.getOrDefault(new Pair<>(fromPortSlot.getPort(), toPortSlot.getPort()), ZERO);
		return portPairSlotIdleTime;
	}

	@Override
	public int getBufferIdleTimeInHours(final IPortSlot toPortSlot) {
		int toPortSlotIdleTimeBeforeVisit = portSlotToIdleTimeBeforeVisit.getOrDefault(toPortSlot, ZERO);
		return toPortSlotIdleTimeBeforeVisit;
	}

	@Override
	public void setExtraIdleTimeOnVoyage(final IPort fromPort, final IPort toPort, final int extraIdleTimeInHours) {
		portPairSlotToIdleTime.put(new Pair<>(fromPort, toPort), extraIdleTimeInHours);
	}

	@Override
	public void setExtraIdleTimeBeforeVisit(final ISequenceElement element, final IPortSlot toPortSlot, final int extraIdleTimeInHours) {
		// TODO add mapping from sequence element to extraIdleTime if getter added for
		// this.
		portSlotToIdleTimeBeforeVisit.put(toPortSlot, extraIdleTimeInHours);
	}
}
