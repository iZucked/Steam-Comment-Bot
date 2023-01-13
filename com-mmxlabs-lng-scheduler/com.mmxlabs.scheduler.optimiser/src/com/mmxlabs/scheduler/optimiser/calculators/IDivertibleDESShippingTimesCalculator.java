/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
 * This is used to calculate the discharge/return time mainly for divertible DES cargoes
 * 
 * @author achurchill
 * 
 */
public interface IDivertibleDESShippingTimesCalculator {

	Pair<@NonNull Integer, @NonNull Integer> getDivertibleDESTimes(@NonNull ILoadOption buyOption, @NonNull IDischargeOption sellOption, @NonNull IVessel nominatedVessel, @NonNull IResource resource);
}
