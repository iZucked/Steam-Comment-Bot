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
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
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

	private @Nullable Pair<ISequences, Long> insert(final ISequences initialRawSequences, final int seed, final List<ISequenceElement> slots) {

		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);

		// TODO: Calculate this once!, not each search step

		final long[] initialMetrics;
		{
			@NonNull
			final IModifiableSequences initialfullSequences = manipulator.createManipulatedSequences(initialRawSequences);

			initialMetrics = evaluationHelper.evaluateState(initialRawSequences, initialfullSequences, null, null, null);
 
			// Set to max value as this is not a concern for us
			initialMetrics[MetricType.COMPULSARY_SLOT.ordinal()] = Integer.MAX_VALUE;
		}
		final GuidedMoveGenerator mg = injector.getInstance(GuidedMoveGenerator.class);

		ISequences currentSequences = initialRawSequences;
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
			// Set to true to do lateness, P&L checks etc before returning a valid move.
			options.setCheckingMove(true);
			options.setExtendSearch(false);
//			options.setStrictOptional(true);
			options.setStrictOptional(new Random(seed).nextBoolean());
			options.setIgnoreUsedElements(false);
			options.setNum_tries(10);

			final MoveResult p = mg.generateMove(currentSequences, lookupManager, new Random(seed), Collections.emptyList(), initialMetrics, options);
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
		return new Pair<>(currentSequences, currentPNL);
	}

	public @Nullable Pair<ISequences, Long> generate(final List<IPortSlot> portSlots, final int seed) {

		final ISequences initialRawSequences = injector.getInstance(Key.get(ISequences.class, Names.named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)));

		final List<ISequenceElement> elements = portSlots.stream() //
				.map(s -> portSlotProvider.getElement(s)) //
				.collect(Collectors.toList());
		return insert(initialRawSequences, seed, elements);
	}
}
