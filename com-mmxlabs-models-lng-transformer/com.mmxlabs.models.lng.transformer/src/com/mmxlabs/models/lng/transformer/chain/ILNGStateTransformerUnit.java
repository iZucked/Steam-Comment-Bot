/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;

/**
 * A {@link ILNGStateTransformerUnit} defines a transformation between in input solution and an output solution from a shared {@link LNGDataTransformer}
 * 
 * @author Simon Goodall
 *
 */
public interface ILNGStateTransformerUnit {

	/**
	 * Returns the shared transformed data object used by this unit
	 * 
	 * @return
	 */
	@NonNull
	LNGDataTransformer getDataTransformer();

//	/**
//	 * Returns the input state given to this unit
//	 * 
//	 * @return
//	 */
//	@NonNull
//	IMultiStateResult getInputState();

	/**
	 * Execute the transformation operation
	 * 
	 * @param monitor
	 * @return
	 */
	@NonNull
	IMultiStateResult run(@NonNull IProgressMonitor monitor);
}
