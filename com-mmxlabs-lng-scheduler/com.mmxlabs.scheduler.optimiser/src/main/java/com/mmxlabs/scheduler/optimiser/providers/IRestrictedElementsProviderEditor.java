package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An editor interface for {@link IRestrictedElementsProvider}
 * 
 * @author Simon Goodall
 * @since 2.0
 */
public interface IRestrictedElementsProviderEditor extends IRestrictedElementsProvider {

	/**
	 * Set the {@link Collection} of elements which may not precede or follow the current element. This will merge with any existing data.
	 * 
	 * @param element
	 * @param restrictedPreceders May be null
	 * @param restrictedFollowers May be null
	 */
	void setRestrictedElements(ISequenceElement element, Collection<ISequenceElement> restrictedPreceders, Collection<ISequenceElement> restrictedFollowers);
}
