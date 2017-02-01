package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;

public class SwapElementsMove implements IMove {

	public static class Builder {

		private Pair<IResource, ISequenceElement> elementA;
		private Pair<IResource, ISequenceElement> elementB;

		public static Builder newMove() {
			return new Builder();
		}

		public Builder withElementA(@Nullable final IResource resource, @NonNull final ISequenceElement sequenceElement) {
			this.elementA = new Pair<>(resource, sequenceElement);
			return this;
		}

		public Builder withElementB(@Nullable final IResource resource, @NonNull final ISequenceElement sequenceElement) {
			this.elementB = new Pair<>(resource, sequenceElement);
			return this;
		}

		public SwapElementsMove create() {
			if (elementA == null || elementB == null) {
				throw new IllegalStateException();
			}
			return new SwapElementsMove(elementA, elementB);
		}

		public ISequenceElement getElementA() {
			return elementA.getSecond();
		}
		public ISequenceElement getElementB() {
			return elementB.getSecond();
		}

	}

	private @NonNull
	final Pair<IResource, ISequenceElement> elementA;
	private @NonNull
	final Pair<IResource, ISequenceElement> elementB;

	private @NonNull final Set<IResource> affectedResources = new LinkedHashSet<>();

	public SwapElementsMove(@NonNull final Pair<IResource, ISequenceElement> elementA, @NonNull final Pair<IResource, ISequenceElement> elementB) {
		this.elementA = elementA;
		this.elementB = elementB;
		if (elementA.getFirst() != null) {
			this.affectedResources.add(elementA.getFirst());
		}
		if (elementB.getFirst() != null) {
			this.affectedResources.add(elementB.getFirst());
		}
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {

		// Find element A index
		final IResource resourceA = elementA.getFirst();
		final int indexA;
		if (resourceA == null) {
			indexA = sequences.getUnusedElements().indexOf(elementA.getSecond());
		} else {
			final ISequence seq = sequences.getSequence(elementA.getFirst());
			int index = -1;
			for (int i = 0; i < seq.size(); ++i) {
				if (seq.get(i) == elementA.getSecond()) {
					index = i;
					break;
				}
			}
			indexA = index;
		}
		assert indexA != -1;

		// Replace element B with element A
		final IResource resourceB = elementB.getFirst();
		if (resourceB == null) {
			final int indexB = sequences.getUnusedElements().indexOf(elementB.getSecond());
			sequences.getModifiableUnusedElements().set(indexB, elementA.getSecond());
		} else {
			final IModifiableSequence seq = sequences.getModifiableSequence(elementB.getFirst());
			for (int i = 0; i < seq.size(); ++i) {
				if (seq.get(i) == elementB.getSecond()) {
					seq.set(i, elementA.getSecond());
					break;
				}
			}
		}

		// Now replace element A with element B
		if (resourceA == null) {
			sequences.getModifiableUnusedElements().set(indexA, elementB.getSecond());
		} else {
			sequences.getModifiableSequence(elementA.getFirst()).set(indexA, elementB.getSecond());
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}
}
