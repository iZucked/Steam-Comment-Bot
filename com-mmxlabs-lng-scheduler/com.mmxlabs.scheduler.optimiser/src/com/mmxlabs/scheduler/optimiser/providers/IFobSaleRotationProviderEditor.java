/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IFobSaleRotationProviderEditor extends IFobSaleRotationProvider {
	void addMapping(IVesselCharter vesselCharter, IDischargeOption dischargeOption);
}
