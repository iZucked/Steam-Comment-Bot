package com.acme.optimiser;

/**
 * Interface defining an object to manipulate an {@link IModifiableSequence}.
 * Instances of this interface are likely to replace sequence elements with
 * alternative elements or insert new elements into the sequence. Some examples
 * cases are to change start or end elements based upon the other elements (e.g.
 * change start or end port in a ship scheduling problem) or to replace a group
 * of elements with a single element (e.g. interleaved elements).
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 */
public interface ISequenceManipulator<T> {

	/**
	 * Manipulate the given sequence.
	 * 
	 * @param sequence
	 */
	void manipulate(IModifiableSequence<T> sequence);
}
