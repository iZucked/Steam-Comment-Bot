/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

public interface IPairingMaxSlotCountConstraintProvider {
	Map<Set<IDischargeOption>, Integer> getAllMaxDischargeGroupCounts();
	Map<Set<IDischargeOption>, Integer> getAllMinDischargeGroupCounts();
}
