/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
	private @NonNull String id;

	public MaintenanceVesselEventPortSlot(@NonNull final String id, @Nullable final ITimeWindow timeWindow, @NonNull final IPort port, final int durationHours, @NonNull final IHeelOptionConsumer heelConsumer, @NonNull final IHeelOptionSupplier heelSupplier, @NonNull final IVesselEventPortSlot formerPortSlot) {
		super(id, PortType.Maintenance, port, timeWindow, new MaintenanceVesselEvent(timeWindow, port, durationHours, heelConsumer, heelSupplier), heelConsumer);
		this.id = id;
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

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof MaintenanceVesselEventPortSlot other) {
			return Objects.equals(id, other.id) 
					&& Objects.equals(formerPortSlot, other.formerPortSlot)
					&& Objects.equals(getEventPortSlots(), other.getEventPortSlots())
					
					;
		}
		return false;
	}

}
