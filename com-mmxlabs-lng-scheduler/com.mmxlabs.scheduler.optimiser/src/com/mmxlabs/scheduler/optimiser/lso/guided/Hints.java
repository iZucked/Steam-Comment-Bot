/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class Hints {

	private final @NonNull List<@NonNull ISequenceElement> problemElements = new LinkedList<>();

	private final @NonNull List<@NonNull ISequenceElement> usedElements = new LinkedList<>();

	private final @NonNull List<@NonNull ISequenceElement> suggestedElements = new LinkedList<>();

	private final List<Pair<ISequenceElement, GuidedMoveTypes>> suggestedElementMoves = new LinkedList<>();

	public void addProblemElement(@NonNull final ISequenceElement element) {
		this.problemElements.add(element);
	}

	public void addShippingLength(@NonNull final IResource resource, final int start) {

	}

	public void fixedElement(@NonNull final ISequenceElement element) {

	}

	public void usedElement(@NonNull final ISequenceElement element) {
		usedElements.add(element);
	}

	public @NonNull List<@NonNull ISequenceElement> getProblemElements() {
		return problemElements;
	}

	public @NonNull List<@NonNull ISequenceElement> getSuggestedElements() {
		return suggestedElements;
	}

	public @NonNull List<@NonNull ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public void addSuggestedElement(@NonNull final ISequenceElement element) {
		this.suggestedElements.add(element);
	}

	public void addSuggestedElementMove(@NonNull final ISequenceElement element, GuidedMoveTypes moveType) {
		this.suggestedElementMoves.add(new Pair<>(element, moveType));
		this.suggestedElements.add(element);
	}

	public void addSuggestedElements(final @NonNull ISequenceElement... elements) {
		for (final ISequenceElement element : elements) {
			this.suggestedElements.add(element);
		}
	}

	public void addSuggestedElements(final @NonNull Iterable<@NonNull ISequenceElement> elements) {
		for (final ISequenceElement element : elements) {
			this.suggestedElements.add(element);
		}
	}
}
