/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;

public class VesselEventPortSlot extends PortSlot implements IVesselEventPortSlot {
	protected IVesselEvent charterOut;
	private @NonNull List<@NonNull ISequenceElement> eventSequenceElements = Collections.emptyList();
	private @NonNull List<@NonNull IPortSlot> eventPortSlots = Collections.emptyList();

	public VesselEventPortSlot(@NonNull final String id, @NonNull final IPort port, final ITimeWindow timeWindow, @NonNull final IVesselEvent charterOut) {
		super(id, port, timeWindow);
		this.charterOut = charterOut;
	}

	@Override
	public IVesselEvent getVesselEvent() {
		return charterOut;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return getVesselEvent();
	}

	public void setEventPortSlots(final @NonNull List<@NonNull IPortSlot> eventPortSlots) {
		this.eventPortSlots = eventPortSlots;

	}

	public void setEventSequenceElements(final @NonNull List<@NonNull ISequenceElement> eventSequenceElements) {
		this.eventSequenceElements = eventSequenceElements;

	}

	@Override
	public @NonNull List<@NonNull ISequenceElement> getEventSequenceElements() {
		return eventSequenceElements;
	}

	@Override
	public @NonNull List<@NonNull IPortSlot> getEventPortSlots() {
		return eventPortSlots;
	}
}
