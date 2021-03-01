package com.mmxlabs.scheduler.optimiser.components;

public interface IMaintenanceVesselEvent extends IVesselEvent {
	void setHeelConsumer(final IHeelOptionConsumer heelConsumer);

	void setHeelSupplier(final IHeelOptionSupplier heelSupplier);

	IHeelOptionConsumer getHeelOptionsConsumer();

	IHeelOptionSupplier getHeelOptionSupplier();
}
