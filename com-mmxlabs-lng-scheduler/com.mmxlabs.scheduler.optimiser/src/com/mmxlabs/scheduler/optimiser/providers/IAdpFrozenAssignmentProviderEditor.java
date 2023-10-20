package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
@NonNullByDefault
public interface IAdpFrozenAssignmentProviderEditor extends IAdpFrozenAssignmentProvider {
	public void setVesselAssignment(IVesselCharter charter, ILoadOption load, IDischargeOption discharge);
	public void setCargoPair(ILoadOption load, IDischargeOption discharge);
}
