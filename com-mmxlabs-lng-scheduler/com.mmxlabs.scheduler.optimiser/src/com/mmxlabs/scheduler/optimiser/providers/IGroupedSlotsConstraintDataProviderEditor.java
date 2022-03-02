/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

/**
 * Provides groups of slots to apply count restrictions to.
 * @author miten
 *
 */
@NonNullByDefault
public interface IGroupedSlotsConstraintDataProviderEditor extends IGroupedSlotsConstraintDataProvider {
	void addMinDischargeSlots(List<IDischargeOption> slots, int limit);
}
