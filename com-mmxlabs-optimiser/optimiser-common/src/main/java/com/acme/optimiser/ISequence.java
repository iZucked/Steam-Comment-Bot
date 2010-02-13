package com.acme.optimiser;

/**
 * This interface defines an immutable sequence of elements.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
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
	 * the specified range. TODO: Should the ISegment be shared or copied data?
	 * Throws IndexOutOfBoundsException if the start or end does is not a valid
	 * value.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	ISegment<T> getSegment(int start, int end);
}
