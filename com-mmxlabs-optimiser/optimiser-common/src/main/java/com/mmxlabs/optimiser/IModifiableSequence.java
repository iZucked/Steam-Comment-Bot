package com.mmxlabs.optimiser;

/**
 * The {@link IModifiableSequence} interface extends the {@link ISequence}
 * interface to allow sequence contents to be modified. It is expected that the
 * main users of this interface will be {@link ISequencesManipulator} instances
 * and optimiser operators.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IModifiableSequence<T> extends ISequence<T> {

	/**
	 * Replace the element at the specified index with the given element.
	 * 
	 * @param index
	 * @param element
	 */
	void set(int index, T element);

	/**
	 * Remove the element at the specified index
	 * 
	 * @param index
	 */
	void remove(int index);

	/**
	 * Remove the given element from the sequence. Returns false if the element
	 * did not exist or true on success.
	 * 
	 * @param element
	 */
	boolean remove(T element);

	/**
	 * Insert the given element at the specified index.
	 * 
	 * @param index
	 * @param element
	 */
	void insert(int index, T element);

	/**
	 * Add the given element to the end of the sequence.
	 * 
	 * @param element
	 */
	void add(T element);

	/**
	 * Remove a range of elements specified in the given {@link ISegment}
	 * 
	 * @param segment
	 */
	void remove(ISegment<T> segment);

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
	 * Insert a range of elements defined be the given {@link ISegment} at the
	 * specified index.
	 * 
	 * @param index
	 * @param segment
	 */
	void insert(int index, ISegment<T> segment);

	/**
	 * Replaces all entries in the current sequence with those in the specified
	 * sequence.
	 * 
	 * @param sequence
	 */
	void replaceAll(ISequence<T> sequence);
}
