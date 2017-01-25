package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class Hints {

	private final List<ISequenceElement> problemElements = new LinkedList<>();

	private final List<ISequenceElement> usedElements = new LinkedList<>();

	private final List<ISequenceElement> suggestedElements = new LinkedList<>();

	private final List<Pair<ISequenceElement, MoveTypes>> suggestedElementMoves = new LinkedList<>();

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

	public List<ISequenceElement> getProblemElements() {
		return problemElements;
	}

	public List<ISequenceElement> getSuggestedElements() {
		return suggestedElements;
	}

	public List<ISequenceElement> getUsedElements() {
		return usedElements;
	}

	public void addSuggestedElement(@NonNull final ISequenceElement element) {
		this.suggestedElements.add(element);
	}

	public void addSuggestedElementMove(@NonNull final ISequenceElement element, MoveTypes moveType) {
		this.suggestedElementMoves.add(new Pair<>(element, moveType));
		this.suggestedElements.add(element);
	}

	public void addSuggestedElements(final ISequenceElement... elements) {
		for (final ISequenceElement element : elements) {
			this.suggestedElements.add(element);
		}
	}

	public void addSuggestedElements(final @NonNull Iterable<ISequenceElement> elements) {
		for (final ISequenceElement element : elements) {
			this.suggestedElements.add(element);
		}
	}
}
