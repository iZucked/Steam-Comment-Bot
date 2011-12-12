/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Editor interface for optional elements provider
 * 
 * @author hinton
 * 
 */
public interface IOptionalElementsProviderEditor extends IOptionalElementsProvider {
	/**
	 * Set whether the given element is optional (true if it is, false if it is required).
	 * 
	 * If this method is never called for an element, it will be presumed non-optional (default is required).
	 * 
	 * HOWEVER that element will then be missing from the required list, so this isn't a great idea.
	 * 
	 * @param element
	 * @param isOptional
	 */
	void setOptional(ISequenceElement element, boolean isOptional);
}
