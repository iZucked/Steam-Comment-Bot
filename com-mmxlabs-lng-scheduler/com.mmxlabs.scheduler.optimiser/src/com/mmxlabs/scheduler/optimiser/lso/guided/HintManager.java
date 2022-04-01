/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;

public class HintManager {

	@Inject
	private IPortTypeProvider portTypeProvider;

	private final @NonNull Collection<@NonNull ISequenceElement> problemElements = new LinkedHashSet<>();

	private final @NonNull Collection<@NonNull ISequenceElement> suggestedElements = new LinkedHashSet<>();

	private final @NonNull Collection<@NonNull ISequenceElement> usedElements = new LinkedHashSet<>();

	public @NonNull Collection<@NonNull ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public @NonNull Collection<@NonNull ISequenceElement> getProblemElements() {
		return problemElements;
	}

	public @NonNull Collection<@NonNull ISequenceElement> getSuggestedElements() {
		return suggestedElements;
	}

	public void chain(@NonNull final Hints hints) {
		problemElements.addAll(hints.getProblemElements());
		suggestedElements.addAll(hints.getSuggestedElements());
		usedElements.addAll(hints.getUsedElements());

		// Filter
		{
			final Iterator<ISequenceElement> itr = problemElements.iterator();
			while (itr.hasNext()) {
				final ISequenceElement e = itr.next();
				if (!GuidedMoveGenerator.validElementTypes.contains(portTypeProvider.getPortType(e))) {
					itr.remove();
				}
			}
		}
		{
			final Iterator<@NonNull ISequenceElement> itr = suggestedElements.iterator();
			while (itr.hasNext()) {
				final ISequenceElement e = itr.next();
				if (!GuidedMoveGenerator.validElementTypes.contains(portTypeProvider.getPortType(e))) {
					itr.remove();
				}
			}
		}

	}

	public void reset() {
		problemElements.clear();
		suggestedElements.clear();
		usedElements.clear();
	}
}
