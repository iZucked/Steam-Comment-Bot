/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * This interface defines an immutable sequence of elements. It is assumed that each element appears only once in a given sequence.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequence extends Iterable<@NonNull ISequenceElement> {

	/**
	 * Returns the number of elements in this sequence.
	 * 
	 * @return
	 */
	int size();

	/**
	 * Return the element at the specified index. Throws IndexOutOfBoundsException if the index does not point to a member.
	 * 
	 * @param index
	 * @return
	 */
	@NonNull
	ISequenceElement get(int index);

	/**
	 * Return a {@link ISegment} instance from a subset of the sequence using the specified range. The segment will be independent from the sequence. Throws IndexOutOfBoundsException if the start or
	 * end does is not a valid value.
	 * 
	 * @param start
	 *            Range start (inclusive)
	 * @param end
	 *            Range end (exclusive)
	 * @return
	 */
	@NonNull
	ISegment getSegment(int start, int end);

	/**
	 * Get the element at the end of this sequence
	 * 
	 * @return last element
	 */
	@Nullable
	ISequenceElement last();

	/**
	 * Get the element at the start of this sequence
	 * 
	 * @return first element
	 */
	@Nullable
	ISequenceElement first();
}
