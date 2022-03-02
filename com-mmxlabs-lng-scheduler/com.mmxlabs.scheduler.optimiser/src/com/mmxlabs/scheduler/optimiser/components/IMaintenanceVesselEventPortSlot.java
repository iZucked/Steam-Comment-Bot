/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

public interface IMaintenanceVesselEventPortSlot extends IVesselEventPortSlot, IHeelOptionConsumerPortSlot, IHeelOptionSupplierPortSlot {
	public IVesselEventPortSlot getFormerPortSlot();
}
