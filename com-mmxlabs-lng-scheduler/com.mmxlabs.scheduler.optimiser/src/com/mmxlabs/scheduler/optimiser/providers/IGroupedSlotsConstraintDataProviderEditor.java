/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

/**
 * Provides groups of slots to apply count restrictions to.
 * @author alex
 *
 */
public interface IGroupedSlotsConstraintDataProviderEditor extends IGroupedSlotsConstraintDataProvider {
	void addMinDischargeSlots(List<Pair<IDischargeOption, Integer>> slots, int limit);
}
