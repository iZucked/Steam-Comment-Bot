/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.moves;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.AnnotatedMoveType;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveHandlerWrapper;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveTypes;
import com.mmxlabs.scheduler.optimiser.moves.handlers.InsertOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.MoveSegmentSequenceMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.RemoveOptionalElementMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.ShuffleElementsMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.SwapSegmentSequenceMoveHandler;
import com.mmxlabs.scheduler.optimiser.moves.handlers.SwapTailsSequenceMoveHandler;

@NonNullByDefault
public class MoveMapper {

	@Inject
	private Injector injector;

	public static final String USE_GUIDED_MOVES = "useGuidedMoves";

	@Inject
	@Named(USE_GUIDED_MOVES)
	private boolean useGuidedMoves;

	private List<MoveTypes> supportedTypes = new LinkedList<>();

	public @Nullable IMoveGenerator getMoveHandler(final MoveTypes moveType) {

		switch (moveType) {
		case Single_Element_Swap:
			break;
		case Insert_Optional_Element:
			return injector.getInstance(InsertOptionalElementMoveHandler.class);
		case Remove_Optional_Element:
			return injector.getInstance(RemoveOptionalElementMoveHandler.class);
		case Swap_Segments:
			return injector.getInstance(SwapSegmentSequenceMoveHandler.class);
		case Move_Segment:
			return injector.getInstance(MoveSegmentSequenceMoveHandler.class);
		case Swap_Tails:
			return injector.getInstance(SwapTailsSequenceMoveHandler.class);
		case Shuffle_Element:
			return injector.getInstance(ShuffleElementsMoveHandler.class);
		case Guided_Move_Generator:
			return injector.getInstance(GuidedMoveGenerator.class);

		case Move_Within_Sequence:
			break;
		case Move_Within_Sequence_Single_Element_Swap:

			break;
		case Rotate_Within_Sequence:
			break;

		// Guided Move types...

		// TODO: Wrap these in an element "finder" class and delegate to the guided helper classes
		case Swap_Slot:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Swap_Slot)));
		case Insert_Cargo:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Insert_Cargo)));
		case Insert_DES_Purchase:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Insert_DES_Purchase)));
		case Insert_FOB_Sale:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Insert_FOB_Sale)));
		case Insert_Slot:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Insert_Slot)));
		case Insert_Vessel_Event:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Insert_Vessel_Event)));
		case Move_Vessel_Event:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Move_Vessel_Event)));
		case Remove_Cargo:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Remove_Cargo)));
		case Remove_DES_Purchase:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Remove_DES_Purchase)));
		case Remove_FOB_Sale:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Remove_FOB_Sale)));
		case Remove_Slot:
			// return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Remove_Slot)));
			break;
		case Remove_Vessel_Event:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Remove_Vessel_Event)));
		case Swap_Cargo_Vessel:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Swap_Cargo_Vessel)));
		case Move_Slot_NonShipped_Resource:
			return injector.getInstance(Key.get(GuidedMoveHandlerWrapper.class, AnnotatedMoveType.annotatedWith(GuidedMoveTypes.Move_Slot_NonShipped_Resource)));

		default:
			break;

		}

		return null;

	}

	@Inject
	public void initSupportedMoveTypes() {

		supportedTypes = new LinkedList<>();

		if (!useGuidedMoves) {
			for (final MoveTypes moveType : MoveTypes.values()) {
				// Skip for now
				if (moveType == MoveTypes.Guided_Move_Generator) {
					continue;
				}
				if (getMoveHandler(moveType) != null) {
					supportedTypes.add(moveType);
				}
			}
		} else {
			supportedTypes.add(MoveTypes.Guided_Move_Generator);
		}

		// // O
		// // 10 % Optional
		// IntStream.range(0, 5).forEach(x -> supportedTypes.add(MoveTypes.Insert_Optional_Element));
		// IntStream.range(0, 5).forEach(x -> supportedTypes.add(MoveTypes.Remove_Optional_Element));
		// // 20 % Shuffle
		// IntStream.range(0, 20).forEach(x -> supportedTypes.add(MoveTypes.Shuffle_Element));
		//
		// IntStream.range(0, 20).forEach(x -> supportedTypes.add(MoveTypes.Swap_Segments));
		// IntStream.range(0, 20).forEach(x -> supportedTypes.add(MoveTypes.Swap_Tails));
		// IntStream.range(0, 20).forEach(x -> supportedTypes.add(MoveTypes.Move_Segment));
	}

	public Collection<MoveTypes> getSupportedMoveTypes() {
		return supportedTypes;
	}
}
