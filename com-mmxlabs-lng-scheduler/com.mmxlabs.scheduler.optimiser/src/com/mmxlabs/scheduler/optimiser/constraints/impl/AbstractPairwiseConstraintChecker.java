/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;

/**
 * An implementation of {@link IPairwiseConstraintChecker} to forbid certain {@link ISequenceElement} pairings
 * 
 */
public abstract class AbstractPairwiseConstraintChecker implements IPairwiseConstraintChecker {

	protected final @NonNull String name;

	public AbstractPairwiseConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, @NonNull final List<@NonNull String> messages) {
		boolean valid = true;
		ISequenceElement prev = null;
		for (final ISequenceElement current : sequence) {
			if (prev != null && !checkPairwiseConstraint(prev, current, resource, messages)) {
				return false;
			}
			prev = current;
		}
		return valid;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @NonNull final List<@NonNull String> messages) {
		boolean valid = true;

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				if (messages == null) {
					return false;
				} else {
					valid = false;
				}
			}
		}
		
		if (valid) {
			final String message = String.format("%s: %s", this.name, "all sequences have passed the constraint.");
			messages.add(message);
		}

		return valid;
	}
}
