/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.insertion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
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

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;

	@Inject
	private SequencesHitchHikerHelper sequencesHelper;

	@Inject
	private SequencesUndoSpotHelper undoSpotHelper;

	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IMoveHandlerHelper moveHandlerHelper;

	@Inject
	private Provider<LookupManager> lookupManagerProvider;

	private @Nullable Pair<ISequences, Long> insert(final SlotInsertionOptimiserInitialState state, final int seed, final List<ISequenceElement> _slots) {

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);

		final GuidedMoveGenerator mg = injector.getInstance(GuidedMoveGenerator.class);

		ISequences currentSequences = state.startingPointRawSequences;

		final long[] initialMetrics = state.initialMetrics;

		// Randomise the insertion order
		final List<ISequenceElement> slots = new ArrayList<>(_slots);
		Collections.shuffle(slots, new Random(seed));
		long currentPNL = 0L;
		for (final ISequenceElement slot : slots) {
			if (!currentSequences.getUnusedElements().contains(slot)) {
				// Already inserted, do not try again.
				continue;
			}

			mg.setTargetElements(Collections.singleton(slot));

			final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

			final Random optionsRnd = new Random(seed);
			// For seed 0->4095 this will always return true, so kick it now it start introducing "more randomness"..
			optionsRnd.nextBoolean();

			// Set to true to do lateness and violation checks etc before returning a valid move.
			options.setCheckingMove(true);
			options.setExtendSearch(false);
			// options.setStrictOptional(true);
			options.setStrictOptional(optionsRnd.nextBoolean());
			options.setCheckEvaluatedState(optionsRnd.nextBoolean());
			options.setIgnoreUsedElements(false);
			options.setInsertCanRemove(true);
			options.setNum_tries(10);

			final MoveResult p = mg.generateMove(currentSequences, optionsRnd, Collections.emptyList(), initialMetrics, options);
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
				// System.out.println("Generated move does not include target element");
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

			// Some check, have the inserted slots done something different?
			{
				boolean sameSolutionBasis = true;

				final ILookupManager beforeManager = state.lookupManager;
				final LookupManager afterManager = lookupManagerProvider.get();
				afterManager.createLookup(currentSequences);

				for (final ISequenceElement slot : slots) {
					IResource afterResource;
					List<ISequenceElement> afterSegment;
					{
						final Pair<IResource, Integer> lookup = afterManager.lookup(slot);
						if (lookup == null || lookup.getFirst() == null) {
							afterResource = null;
							afterSegment = null;
						} else {
							afterResource = lookup.getFirst();
							afterSegment = moveHandlerHelper.extractSegment(currentSequences.getSequence(afterResource), slot);
						}
					}
					IResource beforeResource;
					List<ISequenceElement> beforeSegment;
					{
						final Pair<@Nullable IResource, @NonNull Integer> lookup = beforeManager.lookup(slot);
						if (lookup == null || lookup.getFirst() == null) {
							beforeResource = null;
							beforeSegment = null;
						} else {
							beforeResource = lookup.getFirst();
							assert beforeResource != null;
							beforeSegment = moveHandlerHelper.extractSegment(state.originalRawSequences.getSequence(beforeResource), slot);
						}
					}
					if (beforeResource != afterResource || !Objects.equal(beforeSegment, afterSegment)) {
						sameSolutionBasis = false;
						break;
					}
				}
				if (sameSolutionBasis) {
					// Insertion slots still in the same place as the original solution, reject output.
					return null;
				}
			}

			// Try and remove any hitch-hikers that may have arisen during the search.
			@NonNull
			final ISequences simpleSeq = sequencesHelper.undoUnrelatedChanges(state.originalRawSequences, currentSequences, slots);
			boolean valid = true;
			{
				// First check any non-optional input elements have been included. This can happen in a multi slot insertion where subsequent moves undo earlier moves.
				for (final ISequenceElement slot : slots) {
					if (phaseOptimisationData.isElementRequired(slot) || phaseOptimisationData.getSoftRequiredElements().contains(slot)) {
						if (simpleSeq.getUnusedElements().contains(slot)) {
							// System.out.println("Generated move does not include target element");
							return null;
						}
					}
				}
			}
			{
				// Make sure we have not swapped unused, compulsory elements
				for (final ISequenceElement e : simpleSeq.getUnusedElements()) {
					if (phaseOptimisationData.isElementRequired(e) || phaseOptimisationData.getSoftRequiredElements().contains(e)) {
						if (!state.initiallyUnused.contains(e)) {
							// System.out.println("New required element is in unused list");
							return null;
						}
					}
				}
			}
			long[] metrics = null;
			if (valid) {
				@NonNull
				final IModifiableSequences simpleSeqFull = manipulator.createManipulatedSequences(simpleSeq);

				metrics = evaluationHelper.evaluateState(simpleSeq, simpleSeqFull, null, true, true, null, null);
				if (metrics == null) {

					valid = false;
				}
			}
			if (!valid) {
//				System.err.println("Unable to remove hitch-hikers from solution, returning full solution");

				// Re-check sequences
				{
					// First check any non-optional input elements have been included. This can happen in a multi slot insertion where subsequent moves undo earlier moves.
					for (final ISequenceElement slot : slots) {
						if (phaseOptimisationData.isElementRequired(slot) || phaseOptimisationData.getSoftRequiredElements().contains(slot)) {
							if (currentSequences.getUnusedElements().contains(slot)) {
								System.out.println("Generated move does not include target element");
								return null;
							}
						}
					}
				}
				{
					// Make sure we have not swapped unused, compulsory elements
					for (final ISequenceElement e : currentSequences.getUnusedElements()) {
						if (phaseOptimisationData.isElementRequired(e) || phaseOptimisationData.getSoftRequiredElements().contains(e)) {
							if (!state.initiallyUnused.contains(e)) {
//								System.out.println("New required element is in unused list");
								return null;
							}
						}
					}
				}

				return new Pair<>(currentSequences, currentPNL);
			}
			return new Pair<>(simpleSeq, metrics[MetricType.PNL.ordinal()]);

		} else {
			return new Pair<>(currentSequences, currentPNL);
		}
	}

	public @Nullable Pair<ISequences, Long> generate(final List<IPortSlot> portSlots, final SlotInsertionOptimiserInitialState state, final int seed) {

		assert	SequencesHitchHikerHelper.checkValidSequences(state.originalRawSequences);

		
		final List<ISequenceElement> elements = portSlots.stream() //
				.map(portSlotProvider::getElement) //
				.toList();
		final Pair<ISequences, Long> result = insert(state, seed, elements);
		if (result != null) {
		assert	SequencesHitchHikerHelper.checkValidSequences(result.getFirst());
			result.setFirst(undoSpotHelper.undoSpotMarketSwaps(state.originalRawSequences, result.getFirst()));
		}
		
		if (result != null ) {
			assert	SequencesHitchHikerHelper.checkValidSequences(result.getFirst());
		}
		
		return result;
	}
}
