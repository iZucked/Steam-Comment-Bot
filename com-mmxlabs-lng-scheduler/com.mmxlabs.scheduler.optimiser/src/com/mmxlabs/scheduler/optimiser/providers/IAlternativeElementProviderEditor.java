/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;

/***
 * The Editor interface for {@link IAlternativeElementProvider}
 * 
 * @author Simon Goodall
 */
public interface IAlternativeElementProviderEditor extends IAlternativeElementProvider {

	/**
	 * Set the alternative elements pair. Each element can only have the other element as an alternative.
	 * 
	 * @param elementA
	 * @param elementB
	 */
	void setAlternativeElements(ISequenceElement elementA, ISequenceElement elementB);
}
