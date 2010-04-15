package com.mmxlabs.optimiser;

import java.util.List;
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
	 * Returns the {@link ISequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */
	ISequence<T> getSequence(int index);

	/**
	 * Return
	 * 
	 * @return
	 */
	Map<IResource, ISequence<T>> getSequences();

	/**
	 * Returns an indexed list of resources for which resources are keyed off.
	 * The index of each resource can be passed to the {@link #getSequence(int)}
	 * method.
	 * 
	 * @return
	 */
	List<IResource> getResources();

	/**
	 * Returns the number of {@link IResource}s / {@link ISequence}s contained
	 * in this object.
	 * 
	 * @return
	 */
	int size();

}
