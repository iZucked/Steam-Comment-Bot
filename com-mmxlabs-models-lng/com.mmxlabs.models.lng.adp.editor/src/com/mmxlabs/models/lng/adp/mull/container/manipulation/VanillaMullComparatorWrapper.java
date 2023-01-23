/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;


@NonNullByDefault
public class VanillaMullComparatorWrapper implements IMullComparatorWrapper {

	final VanillaComparator comparator = new VanillaComparator();

	private class VanillaComparator implements Comparator<IMudContainer> {

		@Override
		public int compare(final IMudContainer mc0, final IMudContainer mc1) {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			return Long.compare(allocation0, allocation1);
		}
		
	}
	
	@Override
	public Comparator<IMudContainer> getComparator(LocalDateTime currentDateTime) {
		return comparator;
	}



}
