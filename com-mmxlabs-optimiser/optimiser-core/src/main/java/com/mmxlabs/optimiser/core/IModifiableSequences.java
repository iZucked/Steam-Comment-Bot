/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.Map;

/**
 * Extended version of the {@link ISequences} interface allowing access to
 * {@link IModifiableSequence} instances rather than the read-only
 * {@link ISequence} instances.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IModifiableSequences<T> extends ISequences<T> {
	/**
	 * Returns the {@link IModifiableSequence} for the given {@link IResource}.
	 * 
	 * @param resource
	 * @return
	 */
	IModifiableSequence<T> getModifiableSequence(IResource resource);

	/**
	 * Returns the {@link IModifiableSequence} for the given resource index.
	 * 
	 * @param index
	 * @return
	 */
	IModifiableSequence<T> getModifiableSequence(int index);
	
	/**
	 * @return
	 */
	Map<IResource, IModifiableSequence<T>> getModifiableSequences();
}
