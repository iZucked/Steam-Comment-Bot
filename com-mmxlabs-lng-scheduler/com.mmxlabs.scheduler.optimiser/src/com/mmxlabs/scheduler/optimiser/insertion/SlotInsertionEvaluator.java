/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertDESPurchaseMove;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.InsertFOBSaleMove;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;

@NonNullByDefault
public class SlotInsertionEvaluator {

	protected static final Logger LOG = LoggerFactory.getLogger(SlotInsertionEvaluator.class);

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;

	@Inject
	private IMoveHelper moveHelper;

	public @Nullable Pair<ISequences, Long> insert(final SlotInsertionOptimiserInitialState state, final ISequenceElement load, ISequenceElement discharge) {

		IMove move = null;
		if (moveHelper.isDESPurchase(load)) {
			final IResource resource = moveHelper.getDESPurchaseResource(load);
			if (!moveHelper.checkResource(discharge, resource)) {
				return null;
			}
			move = InsertDESPurchaseMove.Builder.newMove() //
					.withUnusedDESPurchase(resource, load) //
					.withUnusedDESSale(discharge) //
					.create();
		} else if (moveHelper.isFOBSale(discharge)) {
			final IResource resource = moveHelper.getFOBSaleResource(discharge);
			if (!moveHelper.checkResource(load, resource)) {
				return null;
			}
			move = InsertFOBSaleMove.Builder.newMove() //
					.withUnusedFOBPurchase(load) //
					.withUnusedFOBSale(resource, discharge) //
					.create();
		}
		if (move == null) {
			return null;
		}
		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		ISequences currentSequences = state.startingPointRawSequences;

		// Run through the sequences manipulator of things such as start/end port replacement

		// this will set the return elements to the right places, and remove the start elements.
		final IModifiableSequences mSequences = new ModifiableSequences(currentSequences);
		move.apply(mSequences);

		long[] metrics = null;
		final IModifiableSequences simpleSeqFull = manipulator.createManipulatedSequences(mSequences);
		metrics = evaluationHelper.evaluateState(mSequences, simpleSeqFull, null, true, null, null);
		if (metrics == null) {
			return null;
		}

		return new Pair<>(mSequences, metrics[MetricType.PNL.ordinal()]);
	}
}
