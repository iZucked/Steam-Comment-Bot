/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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
	List<@NonNull ISequenceElement> getSequenceElements();

	/**
	 * Returns a list of all the {@link IResource}s in the optimisation.
	 */
	@NonNull
	List<@NonNull IResource> getResources();

	/**
	 * Notify {@link IOptimisationData} that it is no longer required and clean up internal references.s
	 */
	void dispose();

}
