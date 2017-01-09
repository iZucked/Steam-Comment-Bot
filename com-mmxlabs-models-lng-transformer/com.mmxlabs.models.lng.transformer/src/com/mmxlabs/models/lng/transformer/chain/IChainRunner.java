/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.chain;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;

/**
 * The {@link IChainRunner} contains a series of {@link IChainLink}s to convert the initial state contained in the {@link LNGDataTransformer} by following through all the links to progressively
 * transform {@link IMultiStateResult}s through to a final set of values.
 * 
 * @author Simon Goodall
 *
 */
public interface IChainRunner {

	/**
	 * Shared scenario data. May be used within multiple {@link IChainRunner}s
	 * 
	 * @return
	 */
	@NonNull
	LNGDataTransformer getDataTransformer();

	/**
	 * The initial, evaluated state as defined by the first link in the chain.
	 * 
	 * @return
	 */
	@NonNull
	IMultiStateResult getInitialState();

	/**
	 * Run all links in the chain and return the final result.
	 * 
	 * @param monitor
	 * @return
	 */
	@NonNull
	IMultiStateResult run(@NonNull final IProgressMonitor monitor);
}