/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Interface used by the {@link RandomMoveGenerator} to generate a {@link IMove} of a particular type. Implementations need to be registered with the {@link RandomMoveGenerator} before they will be
 * used.
 * 
 * @author Simon Goodall
 * 
 */
public interface IRandomMoveGeneratorUnit {

	@Nullable
	IMove generateRandomMove(@NonNull RandomMoveGenerator moveGenerator, @NonNull ISequences sequences);

}
