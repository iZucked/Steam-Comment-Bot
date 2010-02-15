package com.acme.optimiser;

/**
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISegment<T> extends Iterable<T> {

	int size();

	T get(int index);

	int getSequenceStart();

	int getSequenceEnd();

	ISequence<T> getSequence();

}
