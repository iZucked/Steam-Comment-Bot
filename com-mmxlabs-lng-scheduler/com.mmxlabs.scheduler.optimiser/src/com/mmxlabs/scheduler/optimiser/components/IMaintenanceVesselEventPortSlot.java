package com.mmxlabs.scheduler.optimiser.components;

public interface IMaintenanceVesselEventPortSlot extends IVesselEventPortSlot, IHeelOptionConsumerPortSlot, IHeelOptionSupplierPortSlot {
	public IVesselEventPortSlot getFormerPortSlot();
}
