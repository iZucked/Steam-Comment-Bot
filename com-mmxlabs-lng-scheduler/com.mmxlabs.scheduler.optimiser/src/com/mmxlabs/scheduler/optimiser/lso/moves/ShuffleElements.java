/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link ShuffleElements} is a {@link IMove} to manipulate multiple {@link ISequenceElement}s in various places in the {@link ISequences}. Typically it will be used to combine two
 * {@link ISequenceElement}s from different places and insert them into another part of the {@link ISequences}. A helper class, {@link ShuffleElementsBuilder} can be used to build up the individual
 * steps. The move consists of a set of {@link From} instructions and a set of {@link To} instructions. A {@link From} instruction references a single {@link ISequenceElement}. A {@link To} element
 * references an insertion point and an expected number of elements. The {@link From} and {@link To} lists are ordered - the {@link From} elements are consumed by the {@link To} elements in list
 * order. For example if we have three {@link From} elements and two {@link To} elements with a size of 2 and 1 respectively, then the first {@link To} will use the first two {@link From} elements and
 * the second {@link To} will take the third {@link From} element. The total number of {@link From} elements must equal the sum of the size attributes of all the {@link To} elements.
 * 
 * @author Simon Goodall
 * 
 */
public class ShuffleElements implements IMove {

	/**
	 * A helper class to build up the inputs for the {@link ShuffleElements} move.
	 * 
	 * 
	 */
	public static final class ShuffleElementsBuilder {

		private final List<From> froms = new LinkedList<From>();
		private final List<To> tos = new LinkedList<To>();

		/**
		 * Add a new {@link From} instruction. Use the {@link ISequenceElement} in the {@link ISequence} for the given {@link IResource} at the specified index position.
		 * 
		 * @param resource
		 * @param index
		 */
		public void addFrom(final IResource resource, final int index) {
			froms.add(new From(resource, index));
		}

		/**
		 * Add a new {@link From} instruction. Use the {@link ISequenceElement} in the {@link ISequence} for the given {@link IResource} at the specified index position. This variant also stores the
		 * expected {@link ISequenceElement} used for debugging. This variation takes an alternative element to use in the corresponding To.
		 * 
		 * @param resource
		 * @param index
		 */
		public void addFrom(final @Nullable IResource resource, final int index, final @NonNull ISequenceElement element) {
			froms.add(new From(resource, index, element));
		}

		/**
		 * Add a new {@link From} instruction. Use the {@link ISequenceElement} in the {@link ISequence} for the given {@link IResource} at the specified index position. This variant also stores the
		 * expected {@link ISequenceElement} used for debugging.
		 * 
		 * @param resource
		 * @param index
		 */
		public void addFrom(final @Nullable IResource resource, final int index, final @NonNull ISequenceElement element, final @Nullable ISequenceElement alternative) {
			froms.add(new From(resource, index, element, alternative));
		}

		/**
		 * Add a new {@link To} instruction. Insert <i>size</i> elements into the {@link ISequence} for the given {@link IResource} at the given index.
		 * 
		 * @param resource
		 * @param index
		 * @param size
		 */
		public void addTo(final @Nullable IResource resource, final int index, final int size) {
			tos.add(new To(resource, index, size));
		}

		/**
		 * Construct and return the new {@link ShuffleElements} move. Do not use this {@link ShuffleElementsBuilder} instance again.
		 * 
		 * @return
		 */
		public ShuffleElements getMove() {
			return new ShuffleElements(froms.toArray(new From[0]), tos.toArray(new To[0]));
		}
	};

	/**
	 * The {@link From} class specifies an instruction to the {@link ShuffleElements} move as to where to find an element.
	 * 
	 */
	public static final class From {
		public final @Nullable IResource resource;
		public final int index;
		public final @Nullable ISequenceElement element;
		/**
		 */
		public final @Nullable ISequenceElement alternativeElement;

		public From(final @Nullable IResource resource, final int index) {
			this.resource = resource;
			this.index = index;
			this.element = null;
			this.alternativeElement = null;
		}

		public From(final @Nullable IResource resource, final int index, final @NonNull ISequenceElement element) {
			this.resource = resource;
			this.index = index;
			this.element = element;
			this.alternativeElement = null;
		}

		/**
		 */
		public From(final @Nullable IResource resource, final int index, final @NonNull ISequenceElement element, final @Nullable ISequenceElement alternativeElement) {
			this.resource = resource;
			this.index = index;
			this.element = element;
			this.alternativeElement = alternativeElement;
		}
	}

	/**
	 * The {@link To} class specifies and instruction to the {@link ShuffleElements} move as to where to insert a sequence of elements.
	 * 
	 * @author sg
	 * 
	 */
	public static final class To {

		public final IResource resource;
		public final int index;

		/**
		 * The number of elements to insert.
		 */
		public final int size;

		public To(final IResource resource, final int index, final int size) {
			this.resource = resource;
			this.index = index;
			this.size = size;
		}
	}

	private final From[] froms;
	private final To[] tos;

	private final @NonNull Collection<@NonNull IResource> resources = new LinkedHashSet<>();

	public ShuffleElements(final From[] froms, final To[] tos) {
		super();

		this.froms = froms;
		this.tos = tos;

		int fromSize = 0;
		int toSize = 0;
		for (final From from : froms) {
			@Nullable
			IResource fromResource = from.resource;
			if (fromResource != null) {
				resources.add(fromResource);
			}
			fromSize++;
		}
		for (final To to_ : tos) {
			IResource toResource = to_.resource;
			if (toResource != null) {
				resources.add(toResource);
			}
			toSize += to_.size;
		}
		if (fromSize != toSize) {
			throw new IllegalArgumentException("Number of from elements should equal number of to elements sizes");
		}
	}

	@Override
	public @NonNull Collection<@NonNull IResource> getAffectedResources() {
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#apply(com.mmxlabs.optimiser.core.IModifiableSequences)
	 */
	@Override
	public void apply(final IModifiableSequences sequences) {

		final List<@NonNull ISequenceElement> spare = sequences.getModifiableUnusedElements();

		// Remove the various elements first
		final List<From> seenFroms = new ArrayList<>(froms.length);
		final List<ISequenceElement> insertionElements = new ArrayList<>(froms.length);
		for (final From from : froms) {
			// The offset - if we have already interacted with this ISequence, then the indicies may need adjusting
			int offset = 0;
			@Nullable
			IResource fromResource = from.resource;
			for (final From seenFrom : seenFroms) {
				if (fromResource == seenFrom.resource && from.index > seenFrom.index) {
					++offset;
				}
			}
			final IModifiableSequence seq = fromResource == null ? null : sequences.getModifiableSequence(fromResource);

			final ISequenceElement e = removeElement(seq, spare, from.index - offset);
			if (from.element != null) {
				if (from.element != e) {
					throw new IllegalStateException("Removing unexpected element");
				}

			}
			// If we have an alternative element, instead of inserting the element we have just removed, we insert the alternative element.
			if (from.alternativeElement != null) {
				insertionElements.add(from.alternativeElement);
			} else {
				insertionElements.add(e);
			}
			seenFroms.add(from);
		}
		// Add the new elements

		final List<To> seenTos = new ArrayList<To>(tos.length);
		for (final To to_ : tos) {
			// The offset - if we have already interacted with this ISequence, then the indicies may need adjusting
			int offset = 0;

			IResource toResource = to_.resource;
			for (final From seenFrom : seenFroms) {
				if (toResource == seenFrom.resource && to_.index > seenFrom.index) {
					--offset;
				}
			}

			for (final To seenTo : seenTos) {
				if (toResource == seenTo.resource && to_.index > seenTo.index) {
					offset += seenTo.size;
				}
			}
			final IModifiableSequence seq = toResource == null ? null : sequences.getModifiableSequence(toResource);
			for (int i = 0; i < to_.size; ++i) {
				addElement(seq, spare, to_.index + offset++, insertionElements.remove(0));
			}
			seenTos.add(to_);
		}

	}

	private void addElement(final @Nullable IModifiableSequence seq, final List<@NonNull ISequenceElement> spare, final int index, final @NonNull ISequenceElement element) {
		if (seq == null) {
			spare.add(index, element);
		} else {
			seq.insert(index, element);
		}
	}

	private ISequenceElement removeElement(final @Nullable IModifiableSequence seq, final List<@NonNull ISequenceElement> spares, final int index) {
		if (seq == null) {
			return spares.remove(index);
		} else {
			return seq.remove(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.lso.IMove#validate(com.mmxlabs.optimiser.core.ISequences)
	 */
	@Override
	public boolean validate(final ISequences sequences) {

		return true;
	}
}
