/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class AbstractVesselEventPortSlot extends PortSlot implements IVesselEventPortSlot {
	protected IVesselEvent vesselEvent;
	private List<ISequenceElement> eventSequenceElements = Collections.emptyList();
	private List<IPortSlot> eventPortSlots = Collections.emptyList();

	public AbstractVesselEventPortSlot(final String id, final PortType portType, final IPort port, final @Nullable ITimeWindow timeWindow, final IVesselEvent vesselEvent) {
		super(id, portType, port, timeWindow);
		this.vesselEvent = vesselEvent;
	}

	@Override
	public IVesselEvent getVesselEvent() {
		return vesselEvent;
	}

	public void setEventPortSlots(final List<IPortSlot> eventPortSlots) {
		this.eventPortSlots = eventPortSlots;

	}

	public void setEventSequenceElements(final List<ISequenceElement> eventSequenceElements) {
		this.eventSequenceElements = eventSequenceElements;

	}

	@Override
	public List<ISequenceElement> getEventSequenceElements() {
		return eventSequenceElements;
	}

	@Override
	public List<IPortSlot> getEventPortSlots() {
		return eventPortSlots;
	}
}
