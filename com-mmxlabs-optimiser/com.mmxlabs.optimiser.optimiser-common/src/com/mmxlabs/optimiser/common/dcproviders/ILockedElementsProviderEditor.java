/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Editor interface for locked elements provider
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
public interface ILockedElementsProviderEditor extends ILockedElementsProvider {
	/**
	 * Set whether the given element is locked (true if it is, false if it isn't).
	 * 
	 * Default is false
	 * 
	 * @param element
	 * @param isOptional
	 */
	void setLocked(ISequenceElement element, boolean isLocked);

}
