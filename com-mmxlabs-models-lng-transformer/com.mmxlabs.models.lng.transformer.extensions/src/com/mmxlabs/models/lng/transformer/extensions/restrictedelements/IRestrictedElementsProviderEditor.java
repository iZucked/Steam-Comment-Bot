/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.restrictedelements;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An editor interface for {@link IRestrictedElementsProvider}
 * 
 * @author Simon Goodall
 */
public interface IRestrictedElementsProviderEditor extends IRestrictedElementsProvider {

	/**
	 * Set the {@link Collection} of elements which may not precede or follow the current element. This will merge with any existing data.
	 * 
	 * @param element
	 * @param restrictedPreceders May be null
	 * @param restrictedFollowers May be null
	 */
	void addRestrictedElements(ISequenceElement element, Collection<ISequenceElement> restrictedPreceders, Collection<ISequenceElement> restrictedFollowers);
}
