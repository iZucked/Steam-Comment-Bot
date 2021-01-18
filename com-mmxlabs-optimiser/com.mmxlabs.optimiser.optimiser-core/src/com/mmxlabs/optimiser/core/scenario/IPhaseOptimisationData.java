/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * A copy of {@link IOptimisationData} to store a view of data used on a particular
 * optimisation phase.
 * 
 * @author Alex
 * 
 */
public interface IPhaseOptimisationData {

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
	 * Get all the elements which are treated as optional but are really non-optional. Such elements will typically have a penalty associated with them for non-use. These elements will be considered
	 * as optional and be return in the {@link #getOptionalElements()} method and return true for {@link #isElementOptional(ISequenceElement)}.
	 * 
	 * @return
	 */
	List<@NonNull ISequenceElement> getSoftRequiredElements();

	/**
	 * Get all the elements which are optional in the {@link IPhaseOptimisationData}.
	 * 
	 * The union of this with {@link #getRequiredElements()} should be all the elements.
	 * 
	 * @return
	 */
	List<@NonNull ISequenceElement> getOptionalElements();

	/**
	 * Returns true if the given element is allowed to be missing from sequences in a solution.
	 * 
	 * This will be much faster than getOptionalElements().contains(element), but should be identical.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementOptional(@NonNull ISequenceElement element);

	/**
	 * Equivalent to !{@link #isElementOptional(Object)}.
	 * 
	 * This will be much faster than getRequiredElements().contains(element), but should be identical.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementRequired(@NonNull ISequenceElement element);

	/**
	 * Notify {@link IPhaseOptimisationData} that it is no longer required and clean up internal references.s
	 */
	void dispose();

}
