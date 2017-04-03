/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class SplitCharterOutVesselEventStartPortSlot extends AbstractVesselEventPortSlot implements ICharterOutVesselEventPortSlot, IHeelOptionConsumerPortSlot {
	private ICharterOutVesselEvent charterOutEvent;

	public SplitCharterOutVesselEventStartPortSlot(final String id, final PortType portType, final IPort port, final @Nullable ITimeWindow timeWindow, final ICharterOutVesselEvent vesselEvent) {
		super(id, portType, port, timeWindow, vesselEvent);
		this.charterOutEvent = vesselEvent;
	}

	@Override
	public ICharterOutVesselEvent getVesselEvent() {
		return charterOutEvent;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return charterOutEvent.getHeelOptionsConsumer();
	}
}
