/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;

/**
 * A {@link IChainLink} represents a single step in a {@link IChainRunner} which
 * may (or may not) transform the data in an {@link IMultiStateResult} object
 * into another {@link IMultiStateResult} by e.g. running an optimisation. These
 * will typically wrap around a {@link ILNGStateTransformerUnit}
 * 
 * {@link IChainLink}s are assumed to be single use.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public interface IChainLink {
	/**
	 * Returns the number of "ticks" that the {@link IProgressMonitor} in
	 * {@link #run(IProgressMonitor)} will be passed
	 * 
	 * @return
	 */
	int getProgressTicks();

	/**
	 * Run the operation this link defines and return the result.
	 * 
	 * @param monitor
	 * @return
	 */

	IMultiStateResult run(LNGDataTransformer dataTransformer, SequencesContainer initialSequences, IMultiStateResult inputState, IProgressMonitor monitor);

}
