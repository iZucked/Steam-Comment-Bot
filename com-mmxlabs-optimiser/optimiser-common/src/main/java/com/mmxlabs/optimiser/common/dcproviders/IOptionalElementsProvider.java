/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.List;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Optional elements DCP which tells you which elements are required to be in sequences and which elements can be left out
 * 
 * @author hinton
 * 
 */
public interface IOptionalElementsProvider extends IDataComponentProvider {
	/**
	 * Returns true if the given element is allowed to be missing from sequences in a solution.
	 * 
	 * This will be much faster than getOptionalElements().contains(element), but should be identical.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementOptional(ISequenceElement element);

	/**
	 * Equivalent to !{@link #isElementOptional(Object)}.
	 * 
	 * This will be much faster than getRequiredElements().contains(element), but should be identical.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementRequired(ISequenceElement element);

	/**
	 * Get all the elements which are optional in the {@link IOptimisationData}.
	 * 
	 * The union of this with {@link #getRequiredElements()} should be all the elements.
	 * 
	 * @return
	 */
	List<ISequenceElement> getOptionalElements();

	/**
	 * Get all the elements which aren't optional in the {@link IOptimisationData}
	 * 
	 * The union of this with {@link #getOptionalElements()} should be all the elements.
	 * 
	 * @return
	 */
	List<ISequenceElement> getRequiredElements();
}
