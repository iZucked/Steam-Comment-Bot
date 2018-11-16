/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Editor interface for optional elements provider
 * 
 * @author hinton
 * 
 */
@NonNullByDefault
public interface IOptionalElementsProviderEditor extends IOptionalElementsProvider {
	/**
	 * Set whether the given element is optional (true if it is, false if it is required).
	 * 
	 * @param element
	 * @param isOptional
	 */
	void setOptional(ISequenceElement element, boolean isOptional);
}
