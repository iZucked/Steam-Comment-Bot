/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;

@NonNullByDefault
public interface IAdpFrozenAssignmentProvider {

	boolean isPaired(IPortSlot slot);

	boolean isCargoPair(ILoadOption slot1, IDischargeOption slot2);

	boolean isLockedToVessel(ILoadOption slot1, IDischargeOption slot2);

	IVesselCharter getVesselCharter(ILoadOption slot1, IDischargeOption slot2);
	
	Map<ILoadOption, IDischargeOption> getCargoPairs();
	
	List<IVesselCharter> getLockedVesselCharters();
	
	List<ICargo> getCargoesFromVesselCharter(IVesselCharter v);
}
