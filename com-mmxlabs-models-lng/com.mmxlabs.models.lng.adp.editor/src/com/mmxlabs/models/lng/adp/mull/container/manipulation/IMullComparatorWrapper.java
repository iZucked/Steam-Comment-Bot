/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;

@NonNullByDefault
public interface IMullComparatorWrapper {
	public Comparator<IMudContainer> getComparator(final LocalDateTime currentDateTime);
}
