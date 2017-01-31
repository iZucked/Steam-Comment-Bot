/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNull;

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
	 * This will override any previous setting with {@link #setSoftRequired(ISequenceElement, boolean)}
	 * 
	 * @param element
	 * @param isOptional
	 */
	void setOptional(@NonNull ISequenceElement element, boolean isOptional);

	/**
	 * Set whether the given element is soft required - i.e. it is a required element, but should be treated as optional. This will override any previous setting with
	 * {@link #setOptional(ISequenceElement, boolean)}
	 * 
	 * @param element
	 * @param isSoftRequired
	 * @since 2.0
	 */
	void setSoftRequired(@NonNull ISequenceElement element, boolean isSoftRequired);
}
