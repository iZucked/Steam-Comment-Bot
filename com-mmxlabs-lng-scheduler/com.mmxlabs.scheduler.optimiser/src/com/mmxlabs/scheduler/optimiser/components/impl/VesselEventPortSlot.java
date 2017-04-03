/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumerPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class VesselEventPortSlot extends AbstractVesselEventPortSlot implements IHeelOptionConsumerPortSlot {
	private final IHeelOptionConsumer heelConsumer;

	public VesselEventPortSlot(final String id, final PortType portType, final IPort port, final @Nullable ITimeWindow timeWindow, final IVesselEvent vesselEvent) {
		super(id, portType, port, timeWindow, vesselEvent);
		heelConsumer = new HeelOptionConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0));
	}

	public VesselEventPortSlot(final String id, final PortType portType, final IPort port, final @Nullable ITimeWindow timeWindow, final IVesselEvent vesselEvent,
			final IHeelOptionConsumer heelConsumer) {
		super(id, portType, port, timeWindow, vesselEvent);
		this.heelConsumer = heelConsumer;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return heelConsumer;
	}
}
