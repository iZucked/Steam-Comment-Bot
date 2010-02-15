package com.acme.optimiser;

import java.util.Map;

/**
 * {@link ISequences} objects define a collection of {@link ISequence} objects
 * and their {@link IResource} allocations.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface ISequences<T> {

	/**
	 * Returns the {@link ISequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */
	ISequence<T> getSequence(IResource resource);

	/**
	 * Return 
	 * @return
	 */
	Map<IResource, ISequence<T>> getSequences();
}
