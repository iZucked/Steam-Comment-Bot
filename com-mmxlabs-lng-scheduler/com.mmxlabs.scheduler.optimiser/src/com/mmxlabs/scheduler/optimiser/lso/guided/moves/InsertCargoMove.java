package com.mmxlabs.scheduler.optimiser.lso.guided.moves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.DisconnectedSegment;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.AfterElementFinder;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.BeforeElementFinder;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.IFinder;
import com.mmxlabs.scheduler.optimiser.lso.guided.finders.IndexFinder;

public class InsertCargoMove implements IMove {

	public static class Builder {

		private final @NonNull List<ISequenceElement> orderedElements = new LinkedList<>();
		private IResource fromResource;

		private ISequenceElement afterElement;
		private ISequenceElement beforeElement;
		private int index = -1;
		private IResource targetResource;

		public static Builder newMove() {
			return new Builder();
		}

		public Builder withElements(@Nullable final IResource fromResource, final List<ISequenceElement> elements) {
			this.fromResource = fromResource;
			this.orderedElements.clear();
			this.orderedElements.addAll(elements);
			return this;
		}

		public Builder withInsertBefore(@NonNull final IResource targetResource, @NonNull final ISequenceElement element) {
			this.targetResource = targetResource;
			this.index = -1;
			this.afterElement = null;
			this.beforeElement = element;
			return this;
		}

		public Builder withInsertAfter(@NonNull final IResource targetResource, @NonNull final ISequenceElement element) {
			this.targetResource = targetResource;
			this.index = -1;
			this.afterElement = element;
			this.beforeElement = null;
			return this;
		}

		public Builder withInsertAt(@NonNull final IResource targetResource, final int insertionIndex) {
			this.targetResource = targetResource;
			this.index = insertionIndex;
			this.afterElement = null;
			this.beforeElement = null;
			return this;
		}

		public InsertCargoMove create() {
			IFinder insertionPosition;
			if (index != -1) {
				insertionPosition = new IndexFinder(index);
			} else if (afterElement != null) {
				insertionPosition = new AfterElementFinder(afterElement);
			} else if (beforeElement != null) {
				insertionPosition = new BeforeElementFinder(beforeElement);
			} else {
				throw new IllegalStateException();
			}

			if (targetResource == null) {
				throw new IllegalStateException();
			}
			return new InsertCargoMove(fromResource, orderedElements, targetResource, insertionPosition);
		}
	}

	private final @NonNull List<IResource> affectedResources = new ArrayList<>(2);
	private final @NonNull List<ISequenceElement> elements;
	private final @NonNull IFinder insertionPosition;
	private final @Nullable IResource fromResource;
	private final @NonNull IResource targetResource;

	public InsertCargoMove(@Nullable final IResource fromResource, @NonNull final List<ISequenceElement> elements, @NonNull final IResource targetResource, @NonNull final IFinder insertionPosition) {

		this.fromResource = fromResource;
		this.elements = elements;
		this.targetResource = targetResource;
		this.insertionPosition = insertionPosition;

		if (fromResource != null) {
			this.affectedResources.add(fromResource);
		}
		this.affectedResources.add(targetResource);
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		return affectedResources;
	}

	@Override
	public void apply(final IModifiableSequences sequences) {
		final IResource pFromResource = fromResource;
		if (pFromResource == null) {
			final List<ISequenceElement> unusedElements = sequences.getModifiableUnusedElements();
			elements.forEach(e -> unusedElements.remove(e));
		} else {
			final IModifiableSequence fromSequence = sequences.getModifiableSequence(pFromResource);
			elements.forEach(e -> fromSequence.remove(e));
		}

		final IModifiableSequence targetSequence = sequences.getModifiableSequence(targetResource);
		final int insertionIndex = insertionPosition.findInsertionIndex(targetSequence);
		assert insertionIndex != -1;

		targetSequence.insert(insertionIndex, new DisconnectedSegment(elements));
	}

	@Override
	public boolean validate(final ISequences sequences) {
		return true;
	}
}
