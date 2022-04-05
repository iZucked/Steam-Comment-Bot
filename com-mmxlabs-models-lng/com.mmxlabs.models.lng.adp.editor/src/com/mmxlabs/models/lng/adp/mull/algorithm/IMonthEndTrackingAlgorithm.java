/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.YearMonth;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public interface IMonthEndTrackingAlgorithm extends IMullAlgorithm {
	public Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Long>>> getMonthEndEntitlements();
}
