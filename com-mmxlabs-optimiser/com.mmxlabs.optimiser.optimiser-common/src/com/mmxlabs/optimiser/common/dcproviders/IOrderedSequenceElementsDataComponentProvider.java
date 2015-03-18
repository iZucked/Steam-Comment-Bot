/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * An interface extending {@link IDataComponentProvider} to provide definition of a fixed ordering of elements with a {@link ISequence}.
 * 
 * @author Simon Goodall
 * 
 */
public interface IOrderedSequenceElementsDataComponentProvider extends IDataComponentProvider {

	/**
	 * Returns the element which must follow the given element in a sequence. Returns null if there is no constraint set.
	 * 
	 * @param previousElement
	 * @return
	 */
	ISequenceElement getNextElement(ISequenceElement previousElement);

	/**
	 * Returns the element which must precede the given element in a sequence. Returns null if there is no constraint set.
	 */
	ISequenceElement getPreviousElement(ISequenceElement nextElement);
}