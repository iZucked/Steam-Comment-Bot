/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * The {@link IAlternativeElementProvider} is a {@link IDataComponentProvider} interface defining the possibility of a mutually exclusive alternative {@link ISequenceElement}. An
 * {@link ISequenceElement} may have at most one alternative element that can be used instead of it.
 * 
 * @author Simon Goodall
 */
public interface IAlternativeElementProvider extends IDataComponentProvider {

	/**
	 * Returns true if this element can be swapped for another alternative element. This could be the original or alternative
	 * 
	 * @param element
	 * @return
	 */
	boolean hasAlternativeElement(@NonNull ISequenceElement element);

	/**
	 * Returns the alternative (or the original) element to the given one.
	 * 
	 * @param element
	 * @return
	 */
	@NonNull
	ISequenceElement getAlternativeElement(@NonNull ISequenceElement element);

	/**
	 * Returns true if this element is an alternative element - not an original.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementAlternative(@NonNull ISequenceElement element);

	/**
	 * Returns true iff this element has been registered with the provider and is the original element.
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementOriginal(@NonNull ISequenceElement element);

	/**
	 * Returns all the elements which are alternatives
	 * 
	 * @return
	 */
	@NonNull
	Collection<@NonNull ISequenceElement> getAllAlternativeElements();
}
