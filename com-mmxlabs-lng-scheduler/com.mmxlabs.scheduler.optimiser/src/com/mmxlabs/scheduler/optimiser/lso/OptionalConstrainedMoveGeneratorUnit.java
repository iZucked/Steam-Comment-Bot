/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Random;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.moves.handlers.InsertOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.RemoveOptionalElementMoveHandler;

/**
 * A module for the {@link ConstrainedMoveGenerator} which handles moves around optional slots.
 * 
 * @author hinton
 * 
 */
public class OptionalConstrainedMoveGeneratorUnit implements IMoveGenerator {

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private RemoveOptionalElementMoveHandler removeHandler;

	@Inject
	private InsertOptionalElementMoveHandler insertHandler;

	@Override
	public IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager lookupManager, @NonNull Random random) {
		// select an optional element at random
		final ISequenceElement optional = RandomHelper.chooseElementFrom(random, optionalElementsProvider.getOptionalElements());
		final Pair<IResource, Integer> location = lookupManager.lookup(optional);

		if (location.getFirst() == null) {
			return insertHandler.generateAddingMove(optional, location.getSecond(), lookupManager, random);
		} else {
			return removeHandler.generateRemovingMove(optional, location, lookupManager, random);
		}
	}
}
