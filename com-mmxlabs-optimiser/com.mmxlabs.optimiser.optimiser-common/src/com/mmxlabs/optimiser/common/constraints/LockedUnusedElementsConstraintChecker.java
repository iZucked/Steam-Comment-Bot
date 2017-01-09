/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * A {@link IConstraintChecker} implementation which enforces elements that are locked and initially unlocked can never be used in the final solution. This implements an early break-out, meaning only
 * the first violation of this constraint is logged.
 * 
 * @author achurchill
 * 
 */
public final class LockedUnusedElementsConstraintChecker implements IPairwiseConstraintChecker, IInitialSequencesConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	@NonNull
	private ILockedElementsProvider lockedElementsProvider;

	@Inject(optional = true) // Marked as optional as this constraint checker is active in the initial sequence builder where we do not have an existing initial solution.
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@Nullable
	private ISequences initialSequences;

	public LockedUnusedElementsConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		if (isInitialised()) {
			final Collection<@NonNull IResource> loopResources;
			if (changedResources == null) {
				loopResources = sequences.getResources();
			} else {
				loopResources = changedResources;
			}

			for (final IResource resource : loopResources) {
				assert resource != null;
				for (final ISequenceElement element : sequences.getSequence(resource)) {
					assert element != null;
					if (!checkElement(element)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		if (isInitialised()) {
			return checkElement(first) && checkElement(second);
		} else {
			return true;
		}
	}

	private final boolean checkElement(@NonNull final ISequenceElement element) {

		if (lockedElementsProvider.isElementLocked(element)) {
			if (checkElementUnusedInitially(element)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkElementUnusedInitially(final ISequenceElement element) {
		return getInitialSequences().getUnusedElements().contains(element);
	}

	private boolean isInitialised() {
		return getInitialSequences() != null;
	}

	public ISequences getInitialSequences() {
		return initialSequences;
	}

	public void setInitialSequences(final ISequences initialSequences) {
		this.initialSequences = initialSequences;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return String.format("%s --> %s, %s --> %s", first.getName(), checkElement(first), second.getName(), checkElement(second));
	}

	@Override
	public void sequencesAccepted(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences) {
		setInitialSequences(fullSequences);
	}
}
