package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

public class RemoveElementsMove implements IMove {

	public static class Builder {

		private @NonNull final List<Pair<IResource, ISequenceElement>> elementsToRemove = new LinkedList<>();

		public static Builder newMove() {
			return new Builder();
		}

		public Builder removeElement(@NonNull IResource resource, @NonNull ISequenceElement sequenceElement) {
			elementsToRemove.add(new Pair<>(resource, sequenceElement));
			return this;
		}

		public RemoveElementsMove create() {
			return new RemoveElementsMove(elementsToRemove);
		}

	}

	private @NonNull final List<Pair<IResource, ISequenceElement>> elementsToRemove;

	private @NonNull final List<IResource> affectedResources;

	public RemoveElementsMove(List<Pair<IResource, ISequenceElement>> elementsToRemove) {
		this.elementsToRemove = new ArrayList<>(elementsToRemove);
		this.affectedResources = elementsToRemove.stream().map(Pair::getFirst).collect(Collectors.toList());
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {

		for (final Pair<IResource, ISequenceElement> p : elementsToRemove) {
			final ISequenceElement e = p.getSecond();
			sequences.getModifiableSequence(p.getFirst()).remove(e);
			sequences.getModifiableUnusedElements().add(e);
		}
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}
}
