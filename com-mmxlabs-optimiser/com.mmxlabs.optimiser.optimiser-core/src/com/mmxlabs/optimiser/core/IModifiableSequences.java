/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Extended version of the {@link ISequences} interface allowing access to
 * {@link IModifiableSequence} instances rather than the read-only
 * {@link ISequence} instances.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public interface IModifiableSequences extends ISequences {
	/**
	 * Returns the {@link IModifiableSequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */

	IModifiableSequence getModifiableSequence(IResource resource);

	/**
	 * Returns the {@link IModifiableSequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */

	IModifiableSequence getModifiableSequence(int index);

	/**
	 * @return
	 */

	Map<IResource, IModifiableSequence> getModifiableSequences();

	/**
	 * @return a modifiable list of unused elements.
	 */

	List<ISequenceElement> getModifiableUnusedElements();
}
