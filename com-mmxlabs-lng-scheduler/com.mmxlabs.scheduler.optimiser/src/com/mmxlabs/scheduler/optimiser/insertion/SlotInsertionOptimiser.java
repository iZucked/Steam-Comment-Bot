/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuideMoveGeneratorOptions;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator.MoveResult;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHandlerHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

@NonNullByDefault
public class SlotInsertionOptimiser {

	protected static final Logger LOG = LoggerFactory.getLogger(SlotInsertionOptimiser.class);

	private List<IPairwiseConstraintChecker> constraintCheckers = new LinkedList<>();

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;

	@Inject
	private SequencesHelper sequencesHelper;

	@Inject
	private IMoveHandlerHelper moveHandlerHelper;
	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	public void injectConstraintChecker(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) final ISequences initialRawSequences, final List<IConstraintChecker> injectedConstraintCheckers) {
		this.constraintCheckers = new LinkedList<>();
		for (final IConstraintChecker checker : injectedConstraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				final IPairwiseConstraintChecker constraintChecker = (IPairwiseConstraintChecker) checker;
				constraintCheckers.add(constraintChecker);

				// Prep with initial sequences.
				constraintChecker.checkConstraints(initialRawSequences, null);
			}
		}
	}

	private @Nullable Pair<ISequences, Long> insert(SlotInsertionOptimiserInitialState state, final int seed, final List<ISequenceElement> slots) {

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);

		final GuidedMoveGenerator mg = injector.getInstance(GuidedMoveGenerator.class);

		ISequences currentSequences = state.startingPointRawSequences;

		// // Makes sure target slots are not contained in the solution.
		// {
		// final IModifiableSequences tmp = new ModifiableSequences(currentSequences);
		//
		// for (final ISequenceElement e : tmp.getUnusedElements()) {
		// if (optionalElementsProvider.isElementRequired(e) || optionalElementsProvider.getSoftRequiredElements().contains(e)) {
		// initiallyUnused.add(e);
		// }
		//
		// }
		//
		// for (final ISequenceElement slot : slots) {
		// final LookupManager lookupManager = new LookupManager(tmp);
		// final @Nullable Pair<IResource, Integer> lookup = lookupManager.lookup(slot);
		// if (lookup != null && lookup.getFirst() != null) {
		// @NonNull
		// final IModifiableSequence modifiableSequence = tmp.getModifiableSequence(lookup.getFirst());
		// @NonNull
		// final List<ISequenceElement> segment = moveHandlerHelper.extractSegment(modifiableSequence, slot);
		// for (final ISequenceElement e : segment) {
		// modifiableSequence.remove(e);
		// tmp.getModifiableUnusedElements().add(e);
		// }
		// }
		// }
		// currentSequences = tmp;
		// }

		final long[] initialMetrics = state.initialMetrics;
		{
			// Prepare the initial constraint state.
			evaluationHelper.checkConstraints(manipulator.createManipulatedSequences(currentSequences), null);
		}

		long currentPNL = 0L;
		for (final ISequenceElement slot : slots) {
			if (!currentSequences.getUnusedElements().contains(slot)) {
				// Already inserted, do not try again.
				continue;
			}

			mg.setTargetElements(Collections.singleton(slot));

			// final IMoveHelperImpl helper = injector.getInstance(
			final LookupManager lookupManager = new LookupManager();
			lookupManager.createLookup(currentSequences);

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			final Random optionsRnd = new Random(seed);
			// For seed 0->4095 this will always return true, so kick it now it start introducing "more randomness"..
			optionsRnd.nextBoolean();

			// Set to true to do lateness, P&L checks etc before returning a valid move.
			options.setCheckingMove(true);
			options.setExtendSearch(false);
			// options.setStrictOptional(true);
			options.setStrictOptional(optionsRnd.nextBoolean());
			options.setIgnoreUsedElements(false);
			options.setInsertCanRemove(true);
			options.setNum_tries(10);

			final MoveResult p = mg.generateMove(currentSequences, lookupManager, optionsRnd, Collections.emptyList(), initialMetrics, options);
			if (p == null) {
				return null;
			}

			final IMove move = p.getMove();
			if (move == null) {
				return null;
			}

			// Run through the sequences manipulator of things such as start/end port replacement

			// this will set the return elements to the right places, and remove the start elements.
			final IModifiableSequences mSequences = new ModifiableSequences(currentSequences);
			move.apply(mSequences);

			if (mSequences.getUnusedElements().contains(slot)) {
				System.out.println("Generated move does not include target element");
				return null;
			}

			final long[] metrics = p.getMetrics();
			if (metrics == null) {
				return null;
			}

			currentPNL = metrics[MetricType.PNL.ordinal()];
			currentSequences = new Sequences(mSequences);
		}

		if (true) {

			// Try and remove any hitch-hikers that may have arisen during the search.
			@NonNull
			final ISequences simpleSeq = sequencesHelper.undoUnrelatedChanges(state.originalRawSequences, currentSequences, slots);

			{
				// First check any non-optional input elements have been included. This can happen in a multi slot insertion where subsequent moves undo earlier moves.
				for (final ISequenceElement slot : slots) {
					if (optionalElementsProvider.isElementRequired(slot) || optionalElementsProvider.getSoftRequiredElements().contains(slot)) {
						if (simpleSeq.getUnusedElements().contains(slot)) {
							System.out.println("Generated move does not include target element");
							return null;
						}
					}
				}
			}
			{
				// Make sure we have not swapped unused, compulsory elements
				for (final ISequenceElement e : simpleSeq.getUnusedElements()) {
					if (optionalElementsProvider.isElementRequired(e) || optionalElementsProvider.getSoftRequiredElements().contains(e)) {
						if (!state.initiallyUnused.contains(e)) {
							System.out.println("New required element is in  unused list");
							return null;
						}
					}

				}
			}

			@NonNull
			final IModifiableSequences simpleSeqFull = manipulator.createManipulatedSequences(simpleSeq);

			final long[] metrics = evaluationHelper.evaluateState(simpleSeq, simpleSeqFull, null, null, null);

			return new Pair<>(simpleSeq, metrics[MetricType.PNL.ordinal()]);

		} else {
			return new Pair<>(currentSequences, currentPNL);
		}
	}

	public @Nullable Pair<ISequences, Long> generate(final List<IPortSlot> portSlots, final SlotInsertionOptimiserInitialState state, final int seed) {

		final List<ISequenceElement> elements = portSlots.stream() //
				.map(s -> portSlotProvider.getElement(s)) //
				.collect(Collectors.toList());
		return insert(state, seed, elements);
	}
}
