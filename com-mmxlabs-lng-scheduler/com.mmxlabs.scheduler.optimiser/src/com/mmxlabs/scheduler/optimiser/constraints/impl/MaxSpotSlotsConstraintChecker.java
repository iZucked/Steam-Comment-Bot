/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;

/**
 * {@link IPairwiseConstraintChecker} to prevent the optimiser from wiring spot loads to spot discharges.
 * 
 * @author achurchill
 */
public class MaxSpotSlotsConstraintChecker implements IConstraintChecker {

	private static final int MAX_SPOT_COUNT = 4;

	@Inject
	private ISpotMarketSlotsProvider spotMarketSlots;

	private final @NonNull String name;

	public MaxSpotSlotsConstraintChecker(final @NonNull String name) {
		this.name = name;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources, List<String> messages) {
		int spots = 0;
		for (IResource resource : sequences.getResources()) {
			for (ISequenceElement element : sequences.getSequence(resource)) {
				if (spotMarketSlots.isSpotMarketSlot(element)) {
					spots++;
				}
			}
		}
		if (spots > MAX_SPOT_COUNT) {
			if (messages != null)
				messages.add(String.format("%s: It seems that there are spot loads wired to spot discharges. Spot count %d.", this.name, spots));
			return false;
		} else {
			return true;
		}
	}

}
