/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 * This is used to calculate the discharge/return time mainly for divertable DES cargoes
 * 
 * @author achurchill
 * 
 */
public interface IDivertableDESShippingTimesCalculator {

	Pair<@NonNull Integer, @NonNull Integer> getDivertableDESTimes(@NonNull ILoadOption buyOption, @NonNull IDischargeOption sellOption, @NonNull IVessel nominatedVessel, @NonNull IResource resource);
}
