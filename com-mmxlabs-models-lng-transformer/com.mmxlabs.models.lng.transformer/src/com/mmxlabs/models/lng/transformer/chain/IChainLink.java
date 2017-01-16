/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

/**
 * A {@link IChainLink} represents a single step in a {@link IChainRunner} which may (or may not) transform the data in an {@link IMultiStateResult} object into another {@link IMultiStateResult} by
 * e.g. running an optimisation. These will typically wrap around a {@link ILNGStateTransformerUnit}
 * 
 * {@link IChainLink}s are assumed to be single use.
 * 
 * @author Simon Goodall
 *
 */
public interface IChainLink {
	/**
	 * Returns the number of "ticks" that the {@link IProgressMonitor} in {@link #run(IProgressMonitor)} will be passed
	 * 
	 * @return
	 */
	int getProgressTicks();
////
//	/**
//	 * Prep the link with this {@link IMultiStateResult} as the starting state.
//	 * 
//	 * @param inputState
//	 */
//	void init();

//	/**
//	 * Returns the initial {@link IMultiStateResult} this link uses. This may be different to that passed into {@link #init(IMultiStateResult)}
//	 * 
//	 * @return
//	 */
//	@NonNull
//	IMultiStateResult getInputState();

	/**
	 * Run the operation this link defines and return the result.
	 * 
	 * @param monitor
	 * @return
	 */
	@NonNull
	IMultiStateResult run(@NonNull SequencesContainer initialSequences, @NonNull IMultiStateResult inputState, @NonNull IProgressMonitor monitor);

}
