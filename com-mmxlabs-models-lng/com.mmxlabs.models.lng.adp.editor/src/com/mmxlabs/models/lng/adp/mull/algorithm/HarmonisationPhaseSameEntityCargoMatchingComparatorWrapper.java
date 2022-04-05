/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;

@NonNullByDefault
public class HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper {

	private final HarmonisationPhaseSameEntityCargoMatchingComparator comparator;

	private class HarmonisationPhaseSameEntityCargoMatchingComparator implements Comparator<ICargoBlueprint> {
		private int allocatedVolumeToMatch;

		public void setAllocatedVolumeToMatch(final int allocatedVolumeToMatch) {
			this.allocatedVolumeToMatch = allocatedVolumeToMatch;
		}

		@Override
		public int compare(final ICargoBlueprint cb1, final ICargoBlueprint cb2) {
			final int cb1Difference = Math.abs(this.allocatedVolumeToMatch - cb1.getAllocatedVolume());
			final int cb2Difference = Math.abs(this.allocatedVolumeToMatch - cb2.getAllocatedVolume());
			return Integer.compare(cb1Difference, cb2Difference);
		}
	}

	public HarmonisationPhaseSameEntityCargoMatchingComparatorWrapper() {
		this.comparator = new HarmonisationPhaseSameEntityCargoMatchingComparator();
	}

	public Comparator<ICargoBlueprint> getComparator(final ICargoBlueprint cargoBlueprintToMatch) {
		this.comparator.setAllocatedVolumeToMatch(cargoBlueprintToMatch.getAllocatedVolume());
		return this.comparator;
	}
}
