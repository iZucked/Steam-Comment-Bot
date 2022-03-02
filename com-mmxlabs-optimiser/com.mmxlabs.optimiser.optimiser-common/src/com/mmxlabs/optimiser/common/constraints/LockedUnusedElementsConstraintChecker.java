/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
	private ILockedElementsProvider lockedElementsProvider;

	@Inject(optional = true) // Marked as optional as this constraint checker is active in the initial sequence builder where we do not have an existing initial solution.
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@Nullable
	private ISequences initialSequences;

	public LockedUnusedElementsConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
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
					if (!checkElement(element, messages)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		if (isInitialised()) {
			return checkElement(first, messages) && checkElement(second, messages);
		} else {
			return true;
		}
	}

	private final boolean checkElement(@NonNull final ISequenceElement element, final List<String> messages) {

		if (lockedElementsProvider.isElementLocked(element) && checkElementUnusedInitially(element, messages)) {
			return false;
		}
		return true;
	}

	private boolean checkElementUnusedInitially(final ISequenceElement element, final List<String> messages) {
		final boolean result = getInitialSequences().getUnusedElements().contains(element);
		if (!result && messages != null) {
			messages.add(String.format("%s: Element %s is not in the list of unused elements in the Initial Sequence!", this.name, element.getName()));
		}
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
	public void sequencesAccepted(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, final List<String> messages) {
		setInitialSequences(fullSequences);
	}
}
