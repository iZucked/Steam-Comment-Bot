/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.calculators;

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

	public Pair<Integer, Integer> getDivertableDESTimes(final ILoadOption buyOption, final IDischargeOption sellOption, final IVessel nominatedVessel, final IResource resource);
}
