/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * A copy of {@link IOptimisationData} to store a view of data used on a particular optimisation phase.
 * 
 * @author Alex
 * 
 */
@NonNullByDefault
public interface IPhaseOptimisationData {

	/**
	 * Returns a list of all the sequence elements in the optimisation.
	 * 
	 * @return
	 */

	ImmutableList<ISequenceElement> getSequenceElements();

	/**
	 * Returns a list of all the {@link IResource}s in the optimisation.
	 */
	ImmutableList<IResource> getResources();

	ImmutableList<ISequenceElement> getOptionalElements();

	ImmutableList<ISequenceElement> getConsideredAsOptionalElements();

	ImmutableList<ISequenceElement> getAllElementsConsideredOptional();

	boolean isOptionalElement(ISequenceElement element);

	boolean isConsideredAsOptionalElement(ISequenceElement element);

	boolean isOptionalOrConsideredOptionalElement(ISequenceElement element);

}
