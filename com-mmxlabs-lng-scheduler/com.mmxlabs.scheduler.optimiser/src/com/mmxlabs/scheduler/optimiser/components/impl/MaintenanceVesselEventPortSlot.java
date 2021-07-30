/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class MaintenanceVesselEventPortSlot extends VesselEventPortSlot implements IMaintenanceVesselEventPortSlot {

	@NonNull
	private final IVesselEventPortSlot formerPortSlot;

	public MaintenanceVesselEventPortSlot(@NonNull final String id, @Nullable final ITimeWindow timeWindow, @NonNull final IPort port, final int durationHours, @NonNull final IHeelOptionConsumer heelConsumer, @NonNull final IHeelOptionSupplier heelSupplier, @NonNull final IVesselEventPortSlot formerPortSlot) {
		super(id, PortType.Maintenance, port, timeWindow, new MaintenanceVesselEvent(timeWindow, port, durationHours, heelConsumer, heelSupplier), heelConsumer);
		this.formerPortSlot = formerPortSlot;
	}

	@Override
	public @NonNull IHeelOptionSupplier getHeelOptionsSupplier() {
		return ((IMaintenanceVesselEvent) super.getVesselEvent()).getHeelOptionSupplier();
	}

	@Override
	public @NonNull IVesselEventPortSlot getFormerPortSlot() {
		return this.formerPortSlot;
	}

	@Override
	public @NonNull List<@NonNull ISequenceElement> getEventSequenceElements() {
		return Collections.singletonList(new WrappedSequenceElement(this));
	}
}
