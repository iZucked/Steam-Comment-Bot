package com.mmxlabs.optimiser;

/**
 * This interface defines an immutable sequence of elements. It is assumed that
 * each element appears only once in a given sequence.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISequence<T> extends Iterable<T> {

	/**
	 * Returns the number of elements in this sequence.
	 * 
	 * @return
	 */
	int size();

	/**
	 * Return the element at the specified index. Throws
	 * IndexOutOfBoundsException if the index does not point to a member.
	 * 
	 * @param index
	 * @return
	 */
	T get(int index);

	/**
	 * Return a {@link ISegment} instance from a subset of the sequence using
	 * the specified range. The segment will be independent from the sequence.
	 * Throws IndexOutOfBoundsException if the start or end does is not a valid
	 * value.
	 * 
	 * @param start
	 *            Range start (inclusive)
	 * @param end
	 *            Range end (exclusive)
	 * @return
	 */
	ISegment<T> getSegment(int start, int end);
}
