package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;

/***
 * The Editor interface for {@link IAlternativeElementProvider}
 * 
 * @author Simon Goodall
 * @since 2.0
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
