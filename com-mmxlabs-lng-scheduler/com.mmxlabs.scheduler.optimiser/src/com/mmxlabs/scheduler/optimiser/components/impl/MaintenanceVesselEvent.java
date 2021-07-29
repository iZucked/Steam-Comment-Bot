package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class MaintenanceVesselEvent extends VesselEvent implements IMaintenanceVesselEvent {
	@NonNull
	private IHeelOptionConsumer heelConsumer;
	@NonNull
	private IHeelOptionSupplier heelSupplier;

	public MaintenanceVesselEvent(final @Nullable ITimeWindow timeWindow, @NonNull final IPort port, final int durationHours, @NonNull final IHeelOptionConsumer heelConsumer,
			@NonNull final IHeelOptionSupplier heelSupplier) {
		super(timeWindow, port);
		this.setDurationHours(durationHours);
		this.heelConsumer = heelConsumer;
		this.heelSupplier = heelSupplier;
	}

	@Override
	public void setHeelConsumer(@NonNull final IHeelOptionConsumer heelConsumer) {
		this.heelConsumer = heelConsumer;
	}

	@Override
	public void setHeelSupplier(@NonNull final IHeelOptionSupplier heelSupplier) {
		this.heelSupplier = heelSupplier;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return this.heelConsumer;
	}

	@Override
	public @NonNull IHeelOptionSupplier getHeelOptionSupplier() {
		return this.heelSupplier;
	}
}
