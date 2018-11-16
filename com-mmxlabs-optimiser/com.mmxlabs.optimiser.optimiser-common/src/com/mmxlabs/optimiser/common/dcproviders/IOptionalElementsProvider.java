/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;
import com.google.common.collect.ImmutableList;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Optional elements DCP which tells you which elements are optional
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public interface IOptionalElementsProvider extends IDataComponentProvider {
	/**
	 * Returns true if the given element is allowed to be missing from sequences in a solution.
	 * 
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementOptional(ISequenceElement element);

	/**
	 * Get all the elements which are optional .
	 * 
	 * @return
	 */
	ImmutableList<ISequenceElement> getOptionalElements();
}
