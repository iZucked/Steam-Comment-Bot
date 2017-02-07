package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.lso.IConstrainedMoveGeneratorUnit;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.IGuidedMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveLinkedSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.CompoundMove;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.moves.util.LookupManager;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class GuidedMoveGenerator implements IConstrainedMoveGeneratorUnit {

	/**
	 * Port types this move generator can handle. TODO: Add in the vessel events
	 */
	public static final Set<PortType> validElementTypes = Sets.newHashSet(PortType.Load, PortType.Discharge);

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private MoveTypeHelper moveTypeHelper;

	@Inject
	private IOptionalElementsProvider optionalElementsProvider;

	@Inject
	private Provider<HintManager> hintManagerProvider;

	@Inject
	private Injector injector;

	@Inject
	private Provider<LookupManager> lookupManagerProvider;

	private ISequences providedSequences;

	@Inject
	private EvaluationHelper evaluationHelper;

	private List<@NonNull ISequenceElement> allTargetElements = new LinkedList<>();

	private GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();

	@Inject
	private void findAllTargetElements(@NonNull final IOptimisationData optimisationData) {

		allTargetElements = optimisationData.getSequenceElements().stream() //
				.filter(e -> validElementTypes.contains(portTypeProvider.getPortType(e))) //
				.collect(Collectors.toList());
	}

	/**
	 * Allow the default set of target elements to be overridden with this set.
	 * 
	 * @param targetElements
	 */
	public void setTargetElements(final Collection<ISequenceElement> targetElements) {
		allTargetElements = new ArrayList<>(targetElements);
	}

	// Normal move generator API.
	@Override
	public IMove generateMove(final ISequences rawSequences, final ILookupManager lookupManager, final Random random) {

		final GuideMoveGeneratorOptions options = GuideMoveGeneratorOptions.createDefault();
		final long[] initialMetrics = evaluationHelper.evaluateState(new ModifiableSequences(providedSequences), null, null, null);
		final MoveResult p = generateMove(rawSequences, lookupManager, random, Collections.emptyList(), initialMetrics, options);
		if (p != null) {
			return p.move;
		}
		return null;
	}

	public static class MoveResult {
		public MoveResult(final IMove move, final HintManager hintManager, final long[] metrics) {
			this.move = move;
			this.hintManager = hintManager;
			this.metrics = metrics;
		}

		public IMove getMove() {
			return move;
		}

		public HintManager getHintManager() {
			return hintManager;
		}

		public long[] getMetrics() {
			return metrics;
		}

		public IMove move;
		public HintManager hintManager;
		public long[] metrics;

	}

	public MoveResult generateMove(final ISequences rawSequences, final ILookupManager lookupManager, final Random random, final List<ISequenceElement> forbidden, final long[] initialMetrics,
			final GuideMoveGeneratorOptions options) {

		this.providedSequences = rawSequences;

		final HintManager hintManager = hintManagerProvider.get();

		if (options.isIgnoreUsedElements()) {
			hintManager.getUsedElements().addAll(forbidden);
		}
		final int num_tries = options.getNum_tries();

		final List<IMove> discoveredMoves = new LinkedList<>();
		int checkPointIndex = -1;
		long[] checkPointMetrics = null;
		final IModifiableSequences currentSequences = new ModifiableSequences(providedSequences);

		final long existingUnusedCompulsarySlotCount = currentSequences.getUnusedElements().stream() //
				.filter(e -> optionalElementsProvider.isElementRequired(e)) //
				.count();

		for (int i = 0; i < num_tries; ++i) {

			// Generate a move step
			final Pair<IMove, Hints> moveData = getNextMove(currentSequences, random, options, hintManager);
			if (moveData == null) {
				continue;
			}

			final IMove move = moveData.getFirst();
			move.apply(currentSequences);

			discoveredMoves.add(move);
			hintManager.chain(moveData.getSecond());

			if (currentSequences.equals(providedSequences)) {
				continue;
			}
			// Strictly we remove the original slots from this set and reject the state if there are any slots left rather than just see if there is an overall increase in number as this allows
			// "swimming" slot violations.
			final long newUnusedCompulsarySlotCount = currentSequences.getUnusedElements().stream() //
					.filter(e -> optionalElementsProvider.isElementRequired(e)) //
					.count();
			// If the current state passes the constraint checkers, then maybe return it.
			if (
			// hintManager.getOpenCompulsarySlots().isEmpty() && //
			newUnusedCompulsarySlotCount <= existingUnusedCompulsarySlotCount //
					&& evaluationHelper.doesMovePassConstraints(currentSequences)) {

				long[] moveMetrics = null;
				if (options.isCheckingMove()) {
					// Check metrics - this return null if we increase lateness, capacity etc (but loss of P&L is ok)
					moveMetrics = evaluationHelper.evaluateState(currentSequences, null, initialMetrics, null);
					if (moveMetrics == null) {
						continue;
					}
				}

				// Record this state as valid in case we do not find a valid state later on
				checkPointIndex = discoveredMoves.size() - 1;
				checkPointMetrics = moveMetrics;

				// -- TODO return a list of valid states? Then caller can apply in order and decide to combine or separate.

				// Sometimes we want to continue searching as the solution may pass constraints, but have other issues - such as increase lateness or other violations.
				// However the extra search can also introduce unnecessary changes.
				if (!options.isExtendSearch() || random.nextDouble() < 0.05) {
					return new MoveResult(new CompoundMove(discoveredMoves), hintManager, moveMetrics);
				}
			}

		}

		// If we have an empty move, return null
		if (discoveredMoves.isEmpty()) {
			return null;
		}

		if (checkPointIndex != -1) {
			return new MoveResult(new CompoundMove(discoveredMoves.subList(0, 1 + checkPointIndex)), hintManager, checkPointMetrics);
		}

		// No valid state found, give up
		return null;
	}

	private Pair<IMove, Hints> getNextMove(final @NonNull ISequences sequences, final @NonNull Random random, final @NonNull GuideMoveGeneratorOptions options, final HintManager hintManager) {

		final List<@NonNull ISequenceElement> targetElements = getNextElements(options, hintManager);

		Collections.shuffle(targetElements, random);
		final LookupManager lookupManager = lookupManagerProvider.get();
		lookupManager.createLookup(sequences);

		for (final ISequenceElement element : targetElements) {

			final Pair<IResource, Integer> location = lookupManager.lookup(element);
			final List<MoveTypes> moveOptions = new ArrayList<>(moveTypeHelper.getMoveTypes(location.getFirst(), element));

			if (moveOptions.isEmpty()) {
				continue;
			}

			Collections.shuffle(moveOptions, random);
			for (final MoveTypes type : moveOptions) {
				final IGuidedMoveHandler handler = getMoveHandler(type);
				if (handler != null) {
					final Pair<IMove, Hints> moveData = handler.handleMove(lookupManager, element, random, options, hintManager.getUsedElements());
					if (moveData != null) {
						return moveData;
					}
				}
			}
		}

		return null;

	}

	protected List<@NonNull ISequenceElement> getNextElements(final GuideMoveGeneratorOptions options, final HintManager hintManager) {
		// Find a set of elements to consider next
		final List<@NonNull ISequenceElement> targetElements = new LinkedList<>();
		{
			final Collection<@NonNull ISequenceElement> problemElements = hintManager.getProblemElements();
			if (problemElements.isEmpty()) {
				final Collection<@NonNull ISequenceElement> suggestedElements = hintManager.getSuggestedElements();
				if (suggestedElements.isEmpty()) {
					targetElements.addAll(allTargetElements);
				} else {
					targetElements.addAll(suggestedElements);
				}
				// Do not try to move "used" elements again (unless marked as a problem element!)
				if (options.isIgnoreUsedElements()) {
					targetElements.removeAll(hintManager.getUsedElements());
				}
			} else {
				targetElements.addAll(problemElements);
			}
		}

		// TODO, this should be filtered up front
		return targetElements;// .stream().filter(e -> validElementTypes.contains(portTypeProvider.getPortType(e))).collect(Collectors.toList());
	}

	@Nullable
	private IGuidedMoveHandler getMoveHandler(final @NonNull MoveTypes moveType) {
		switch (moveType) {
		case Insert_Cargo:
			return injector.getInstance(InsertCargoVesselMoveHandler.class);
		case Insert_DES_Purchase:
			return injector.getInstance(InsertDESPurchaseMoveHandler.class);
		case Insert_FOB_Sale:
			return injector.getInstance(InsertFOBSaleMoveHandler.class);
		case Insert_Slot:
			return injector.getInstance(InsertCargoVesselMoveHandler.class);
		case Remove_Linked_Slot:
			return injector.getInstance(RemoveLinkedSlotMoveHandler.class);
		case Insert_Vessel_Event:
			break;
		case Move_Vessel_Event:
			break;
		case Remove_Cargo:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_DES_Purchase:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_FOB_Sale:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_Slot:
			// return injector.getInstance(RemoveSlotMoveHandler.class);
		case Remove_Vessel_Event:
			break;
		case Swap_Cargo_Vessel:
			return injector.getInstance(SwapCargoVesselMoveHandler.class);
		case Swap_Event_Vessel:
			break;
		case Swap_Slot:
			return injector.getInstance(SwapSlotMoveHandler.class);
		default:
			break;

		}

		return null;
	}

}
