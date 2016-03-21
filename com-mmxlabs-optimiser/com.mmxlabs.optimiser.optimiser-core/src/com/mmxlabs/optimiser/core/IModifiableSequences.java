/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Extended version of the {@link ISequences} interface allowing access to {@link IModifiableSequence} instances rather than the read-only {@link ISequence} instances.
 * 
 * @author Simon Goodall
 * 
 */
public interface IModifiableSequences extends ISequences {
	/**
	 * Returns the {@link IModifiableSequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */
	@NonNull
	IModifiableSequence getModifiableSequence(@NonNull IResource resource);

	/**
	 * Returns the {@link IModifiableSequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */
	@NonNull
	IModifiableSequence getModifiableSequence(int index);

	/**
	 * @return
	 */
	@NonNull
	Map<@NonNull IResource, @NonNull IModifiableSequence> getModifiableSequences();

	/**
	 * @return a modifiable list of unused elements.
	 */
	@NonNull
	List<@NonNull ISequenceElement> getModifiableUnusedElements();
}
