/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;

/**
 * Interface defining an object which can generate {@link IMove} instances to be used within a Local Search Optimisation. In typical use, the caller will use {@code setSequences(...)} to specify the
 * sequences associated with the current state, and then {@code generateMove()} some number of times to get some moves to alter the state; once a move has been applied, the caller will probably call
 * {@code setSequences(...)} again, and so on.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMoveGenerator {

	/**
	 * Returns a {@link IMove} object to change the current solution state.
	 * 
	 * @return
	 */
	@Nullable
	IMove generateMove();

	/**
	 * Returns {@link ISequences} used to generate moves
	 * 
	 * @return
	 */
	@NonNull
	ISequences getSequences();

	/**
	 * Set the {@link ISequences} used to generate moves. This should be whenever the {@link ISequences} have changed.
	 * 
	 * @param sequences
	 */
	void setSequences(@NonNull ISequences sequences);
}
