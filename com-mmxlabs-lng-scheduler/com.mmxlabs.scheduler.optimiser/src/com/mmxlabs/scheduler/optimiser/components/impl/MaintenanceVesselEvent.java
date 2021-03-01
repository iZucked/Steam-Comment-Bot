package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class MaintenanceVesselEvent extends VesselEvent implements IMaintenanceVesselEvent {

	private IHeelOptionConsumer heelConsumer;
	private IHeelOptionSupplier heelSupplier;

	public MaintenanceVesselEvent(final @Nullable ITimeWindow timeWindow, @NonNull final IPort port, final int durationHours, @NonNull final IHeelOptionConsumer heelConsumer, @NonNull final IHeelOptionSupplier heelSupplier) {
		super(timeWindow, port);
		this.setDurationHours(durationHours);
		this.heelConsumer = heelConsumer;
		this.heelSupplier = heelSupplier;
	}

	@Override
	public void setHeelConsumer(final IHeelOptionConsumer heelConsumer) {
		this.heelConsumer = heelConsumer;
	}

	@Override
	public void setHeelSupplier(final IHeelOptionSupplier heelSupplier) {
		this.heelSupplier = heelSupplier;
	}

	@Override
	public IHeelOptionConsumer getHeelOptionsConsumer() {
		return this.heelConsumer;
	}

	@Override
	public IHeelOptionSupplier getHeelOptionSupplier() {
		return this.heelSupplier;
	}
}
