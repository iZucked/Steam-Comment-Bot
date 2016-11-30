package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.scheduler.optimiser.lso.IConstrainedMoveGeneratorUnit;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.IMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.moves.CompoundMove;
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
	private IGuidedMoveHelper helper;

	@Inject
	private HintManager hintManager;

	@Inject
	private Injector injector;

	@Inject
	private Provider<LookupManager> lookupManagerProvider;

	private ISequences providedSequences;

	private List<@NonNull ISequenceElement> allTargetElements = new LinkedList<>();

	@Inject
	private void findAllTargetElements(@NonNull IOptimisationData optimisationData) {

		allTargetElements = optimisationData.getSequenceElements().stream() //
				.filter(e -> validElementTypes.contains(portTypeProvider.getPortType(e))) //
				.collect(Collectors.toList());
	}

	public void setTargetElements(Collection<ISequenceElement> targetElements) {
		allTargetElements = new ArrayList<>(targetElements);
	}

	@Override
	public void setSequences(final ISequences sequences) {
		this.providedSequences = sequences;
	}

	@Override
	public IMove generateMove() {
		hintManager.reset();

		final int num_tries = 30;

		final List<IMove> discoveredMoves = new LinkedList<>();
		int checkPointIndex = -1;
		IModifiableSequences currentSequences = new ModifiableSequences(providedSequences);

		long existingUnusedCompulsarySlotCount = currentSequences.getUnusedElements().stream() //
				.filter(e -> optionalElementsProvider.isElementRequired(e)) //
				.count();

		for (int i = 0; i < num_tries; ++i) {

			// Generate a move step
			final Pair<IMove, Hints> moveData = getNextMove(currentSequences);
			if (moveData == null) {
				continue;
			}

			final IMove move = moveData.getFirst();
			discoveredMoves.add(move);

			move.apply(currentSequences);
			hintManager.chain(moveData.getSecond());

			// Strictly we remove the original slots from this set and reject the state if there are any slots left rather than just see if there is an overall increase in number as this allows
			// "swimming" slot violations.
			long newUnusedCompulsarySlotCount = currentSequences.getUnusedElements().stream() //
					.filter(e -> optionalElementsProvider.isElementRequired(e)) //
					.count();
			// If the current state passes the constraint checkers, then maybe return it.
			if (
			// hintManager.getOpenCompulsarySlots().isEmpty() && //
			newUnusedCompulsarySlotCount <= existingUnusedCompulsarySlotCount //
					&& helper.doesMovePassConstraints(currentSequences)) {

				// Record this state as valid in case we do not find a valid state later on
				checkPointIndex = i;

				// Sometimes we want to continue searching as the solution may pass constraints, but have other issues - such as increase lateness or other violations.
				// However the extra search can also introduce unnecessary changes.
				// if (helper.getSharedRandom().nextDouble() < 0.05) {
				return new CompoundMove(discoveredMoves);
				// }
			}

		}

		// If we have an empty move, return null
		if (discoveredMoves.isEmpty()) {
			return null;
		}

		if (checkPointIndex != -1) {
			return new CompoundMove(discoveredMoves.subList(0, checkPointIndex));
		}

		// No valid state found, give up
		return null;
	}

	private Pair<IMove, Hints> getNextMove(final @NonNull ISequences sequences) {

		final List<@NonNull ISequenceElement> targetElements = getNextElements();

		Collections.shuffle(targetElements, helper.getSharedRandom());
		final LookupManager lookupManager = lookupManagerProvider.get();
		lookupManager.createLookup(sequences);

		for (final ISequenceElement element : targetElements) {

			final Pair<IResource, Integer> location = lookupManager.lookup(element);
			final List<MoveTypes> moveOptions = new ArrayList<>(moveTypeHelper.getMoveTypes(location.getFirst(), element));

			if (moveOptions.isEmpty()) {
				continue;
			}

			Collections.shuffle(moveOptions, helper.getSharedRandom());
			for (final MoveTypes type : moveOptions) {
				final IMoveHandler handler = getMoveHandler(type);
				if (handler != null) {
					final Pair<IMove, Hints> moveData = handler.handleMove(lookupManager, element, hintManager.getUsedElements());
					if (moveData != null) {
						moveData.getSecond().usedElement(element);
						return moveData;
					}
				}
			}
		}

		return null;

	}

	protected List<@NonNull ISequenceElement> getNextElements() {
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
			} else {
				targetElements.addAll(problemElements);
			}
		}
		
		// Do not try to move "used" elements again
		targetElements.removeAll(hintManager.getUsedElements());
		
		// TODO, this should be filtered up front
		return targetElements;// .stream().filter(e -> validElementTypes.contains(portTypeProvider.getPortType(e))).collect(Collectors.toList());
	}

	@Nullable
	private IMoveHandler getMoveHandler(final @NonNull MoveTypes moveType) {
		switch (moveType) {
		case Insert_Cargo:
			return injector.getInstance(InsertCargoVesselMoveHandler.class);
		case Insert_DES_Purchase:
			return injector.getInstance(InsertDESPurchaseMoveHandler.class);
		case Insert_FOB_Sale:
			return injector.getInstance(InsertFOBSaleMoveHandler.class);
		case Insert_Slot:
			return injector.getInstance(InsertCargoVesselMoveHandler.class);
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
