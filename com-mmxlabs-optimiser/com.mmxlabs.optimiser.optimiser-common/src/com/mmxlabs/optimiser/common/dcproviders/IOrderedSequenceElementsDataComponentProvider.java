/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@Nullable
	ISequenceElement getNextElement(@NonNull ISequenceElement previousElement);

	/**
	 * Returns the element which must precede the given element in a sequence. Returns null if there is no constraint set.
	 */
	@Nullable
	ISequenceElement getPreviousElement(@NonNull ISequenceElement nextElement);
}