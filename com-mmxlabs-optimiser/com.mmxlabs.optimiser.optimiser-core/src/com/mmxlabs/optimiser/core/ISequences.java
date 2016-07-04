/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

/**
 * {@link ISequences} objects define a collection of {@link ISequence} objects and their {@link IResource} allocations.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISequences {

	/**
	 * Returns the {@link ISequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */
	@NonNull
	ISequence getSequence(@NonNull IResource resource);

	/**
	 * Returns the {@link ISequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */
	@NonNull
	ISequence getSequence(int index);

	/**
	 * Return
	 * 
	 * @return
	 */
	@NonNull
	Map<@NonNull IResource, @NonNull ISequence> getSequences();

	/**
	 * Returns the elements in the solution which are not used anywhere
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull ISequenceElement> getUnusedElements();

	/**
	 * Returns an indexed list of resources for which resources are keyed off. The index of each resource can be passed to the {@link #getSequence(int)} method.
	 * 
	 * @return
	 */
	@NonNull
	List<@NonNull IResource> getResources();

	/**
	 * Returns the number of {@link IResource}s / {@link ISequence}s contained in this object.
	 * 
	 * @return
	 */
	int size();
}
