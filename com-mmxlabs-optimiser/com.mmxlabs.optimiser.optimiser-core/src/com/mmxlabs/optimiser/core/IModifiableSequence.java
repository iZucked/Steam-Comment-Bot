/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link IModifiableSequence} interface extends the {@link ISequence} interface to allow sequence contents to be modified. It is expected that the main users of this interface will be
 * {@link ISequencesManipulator} instances and optimiser operators.
 * 
 * @author Simon Goodall
 * 
 */
public interface IModifiableSequence extends ISequence {

	/**
	 * Replace the element at the specified index with the given element.
	 * 
	 * @param index
	 * @param element
	 */
	void set(int index, @NonNull ISequenceElement element);

	/**
	 * Remove the element at the specified index
	 * 
	 * @param index
	 */
	@Nullable
	ISequenceElement remove(int index);

	/**
	 * Remove the given element from the sequence. Returns false if the element did not exist or true on success.
	 * 
	 * @param element
	 */
	boolean remove(@NonNull ISequenceElement element);

	/**
	 * Insert the given element at the specified index.
	 * 
	 * @param index
	 * @param element
	 */
	void insert(int index, @NonNull ISequenceElement element);

	/**
	 * Add the given element to the end of the sequence.
	 * 
	 * @param element
	 */
	void add(@NonNull ISequenceElement element);

	/**
	 * Remove a range of elements specified in the given {@link ISegment}
	 * 
	 * @param segment
	 */
	void remove(@NonNull ISegment segment);

	/**
	 * Remove a range of elements from the start index to the end index.
	 * 
	 * @param start
	 *            Range start (inclusive)
	 * @param end
	 *            Range end (Exclusive)
	 */
	void remove(int start, int end);

	/**
	 * Insert a range of elements defined be the given {@link ISegment} at the specified index. The new element at index is the first element of the segment, the new element at index+1 the second of
	 * the segment, and so on, with the previous elements being shifted right.
	 * 
	 * @param index
	 * @param segment
	 */
	void insert(int index, @NonNull ISegment segment);

	/**
	 * Replaces all entries in the current sequence with those in the specified sequence.
	 * 
	 * @param sequence
	 */
	void replaceAll(@NonNull ISequence sequence);
}
