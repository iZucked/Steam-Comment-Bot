/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class DefaultExtraIdleTimeProviderEditor implements IExtraIdleTimeProviderEditor {

	private static final Integer ZERO = Integer.valueOf(0);

	private final Map<Pair<IPort, IPort>, Integer> portPairSlotToIdleTime = new HashMap<>();
	// private final Map<IPortSlot, Integer> portSlotToIdleTime = new HashMap<>();
	// private final Map<ISequenceElement, Integer> elementToIdleTime = new HashMap<>();

	@Override
	public int getExtraIdleTimeInHours(final IPortSlot fromPortSlot, final IPortSlot toPortSlot) {
		return portPairSlotToIdleTime.getOrDefault(new Pair<>(fromPortSlot.getPort(), toPortSlot.getPort()), ZERO);
	}
//
//	@Override
//	public int getExtraIdleTimeInHours(final ISequenceElement fromElement, final ISequenceElement toElement) {
////		 final TODO Auto-final generated method stub
//		return 0;
//	}

	@Override
	public void setExtraIdleTimeOnVoyage(final IPort fromPort, final IPort toPort, final int extraIdleTimeInHours) {
		portPairSlotToIdleTime.put(new Pair<>(fromPort, toPort), extraIdleTimeInHours);

	}

	@Override
	public void setExtraIdleTimeAfterVisit(final IPort port, final PortType portType, final int extraIdleTimeInHours) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public int getExtraIdleTimeInHours(final IPortSlot portSlot) {
	// return 48;// portSlotToIdleTime.getOrDefault(portSlot, ZERO);
	// }
	//
	// @Override
	// public int getExtraIdleTimeInHours(final ISequenceElement element) {
	// return 48;// elementToIdleTime.getOrDefault(element, ZERO);
	// }
	//
	// @Override
	// public void setExtraIdleTime(final ISequenceElement element, final IPortSlot portSlot, final int extraIdleTimeInHours) {
	// elementToIdleTime.put(element, extraIdleTimeInHours);
	// portSlotToIdleTime.put(portSlot, extraIdleTimeInHours);
	// }

}
