package com.acme.optimiser;

/**
 * The {@link IModifiableSequence} interface extends the {@link ISequence}
 * interface to allow sequence contents to be modified. It is expected that the
 * main users of this interface will be {@link ISequenceManipulator} instances
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
	 * Remove the given element from the sequence.
	 * 
	 * @param element
	 */
	void remove(T element);

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
	 * @param end
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
}
