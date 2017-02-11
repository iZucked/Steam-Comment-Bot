/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * Interface for units that plug into the constrained move generator.
 * 
 * @author hinton
 * 
 */
public interface IConstrainedMoveGeneratorUnit {

	/**
	 * Try to generate a move
	 * 
	 * @return
	 */
	IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager lookupManager, @NonNull Random random);
}
