/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IMaintenanceVesselEvent extends IVesselEvent {
	void setHeelConsumer(@NonNull final IHeelOptionConsumer heelConsumer);

	void setHeelSupplier(@NonNull final IHeelOptionSupplier heelSupplier);

	@NonNull IHeelOptionConsumer getHeelOptionsConsumer();

	@NonNull IHeelOptionSupplier getHeelOptionSupplier();
}
