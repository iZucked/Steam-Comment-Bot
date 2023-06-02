package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IFobSaleRotationProvider {

	@Nullable
	IVesselCharter getVesselCharter(IDischargeOption dischargeOption);
}
