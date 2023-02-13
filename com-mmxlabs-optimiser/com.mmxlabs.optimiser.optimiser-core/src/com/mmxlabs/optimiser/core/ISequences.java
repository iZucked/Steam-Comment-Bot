/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * {@link ISequences} objects define a collection of {@link ISequence} objects
 * and their {@link IResource} allocations.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface ISequences {

	/**
	 * Returns the {@link ISequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */

	ISequence getSequence(IResource resource);

	/**
	 * Returns the {@link ISequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */

	ISequence getSequence(int index);

	/**
	 * Return
	 * 
	 * @return
	 */

	Map<IResource, ISequence> getSequences();

	/**
	 * Returns the elements in the solution which are not used anywhere
	 * 
	 * @return
	 */

	List<ISequenceElement> getUnusedElements();

	/**
	 * Returns an indexed list of resources for which resources are keyed off. The
	 * index of each resource can be passed to the {@link #getSequence(int)} method.
	 * 
	 * @return
	 */

	List<IResource> getResources();

	/**
	 * Returns the number of {@link IResource}s / {@link ISequence}s contained in
	 * this object.
	 * 
	 * @return
	 */
	int size();

	ISequencesAttributesProvider getProviders();

}
