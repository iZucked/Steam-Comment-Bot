/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * This is used to calculate the load/return time mainly for divertable FOB Sale cargoes
 * 
 * @author achurchill
 * 
 */
public interface IDivertableFOBShippingTimesCalculator {

	/**
	 * Returns Load Time and Return time
	 * 
	 * @param buyOption
	 * @param sellOption
	 * @param nominatedVessel
	 * @param resource
	 * @return
	 */
	Pair<@NonNull Integer, @NonNull Integer> getDivertableFOBTimes(@NonNull ILoadOption buyOption, @NonNull IDischargeOption sellOption, @NonNull IVessel nominatedVessel, @NonNull IResource resource);
}
