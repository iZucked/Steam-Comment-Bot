/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * This is used to calculate the load/return time mainly for divertible FOB Sale cargoes
 * 
 * @author achurchill
 * 
 */
public interface IDivertibleFOBShippingTimesCalculator {

	/**
	 * Returns Load Time, disscharge time and return time
	 * 
	 * @param buyOption
	 * @param sellOption
	 * @param nominatedVessel
	 * @param resource
	 * @return
	 */
	Triple<@NonNull Integer, @NonNull Integer, @NonNull Integer> getDivertibleFOBTimes(@NonNull ILoadOption buyOption, @NonNull IDischargeOption sellOption, @NonNull IVessel nominatedVessel,
			@NonNull IResource resource);
}
