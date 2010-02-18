package com.acme.optimiser;

/**
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISegment<T> extends Iterable<T> {

	/**
	 * Return size of segment
	 * 
	 * @return
	 */
	int size();

	/**
	 * Return an element within the segment.
	 * 
	 * @param index
	 * @return
	 */
	T get(int index);

	/**
	 * Return segment start index in originating sequence
	 * 
	 * @return
	 */
	int getSequenceStart();

	/**
	 * Return segment end index in originating sequence
	 * 
	 * @return
	 */
	int getSequenceEnd();

	/**
	 * Return originating {@link ISequence} instance.
	 * 
	 * @return
	 */
	ISequence<T> getSequence();

}
