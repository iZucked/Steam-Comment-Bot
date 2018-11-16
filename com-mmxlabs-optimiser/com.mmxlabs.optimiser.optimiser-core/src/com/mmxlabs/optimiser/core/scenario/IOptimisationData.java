/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An interface to the optimisation data, comprising accessors for core attributes.
 * 
 * @author proshun, Simon Goodall
 * 
 */
public interface IOptimisationData {

	/**
	 * Returns a list of all the sequence elements in the optimisation.
	 * 
	 * @return
	 */
	@NonNull
	ImmutableList<@NonNull ISequenceElement> getSequenceElements();

	/**
	 * Returns a list of all the {@link IResource}s in the optimisation.
	 */
	@NonNull
	ImmutableList<@NonNull IResource> getResources();

	ImmutableList<ISequenceElement> getConsideredAsOptionalElements();
}
