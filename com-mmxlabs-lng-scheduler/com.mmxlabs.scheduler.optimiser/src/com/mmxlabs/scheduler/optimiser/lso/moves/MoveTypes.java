package com.mmxlabs.scheduler.optimiser.lso.moves;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

public enum MoveTypes {
	// "Legacy" move types
	Move_Segment, Swap_Segments, Swap_Tails, Move_Within_Sequence, Rotate_Within_Sequence, Insert_Optional_Element, Remove_Optional_Element, Shuffle_Element, // <<-- replace with
																																								// Create_NonShipped_Element,
																																								// Remove_NonShipped_Element,

	Single_Element_Swap, Move_Within_Sequence_Single_Element_Swap,

	// Guided Move Generator
	Insert_DES_Purchase, Remove_DES_Purchase, //
	Insert_FOB_Sale, Remove_FOB_Sale, //
	Insert_Cargo, Remove_Cargo, //
	Swap_Cargo_Vessel, //
	Insert_Slot, Remove_Slot, Swap_Slot, //
	Insert_Vessel_Event, Remove_Vessel_Event, Swap_Event_Vessel, Move_Vessel_Event, Guided_Move_Generator; //

	private static final @NonNull MoveTypes @NonNull [] legacyMoveTypes = new @NonNull MoveTypes @NonNull [] { Move_Segment, Swap_Segments, Swap_Tails, Move_Within_Sequence, Rotate_Within_Sequence,
			Insert_Optional_Element, Remove_Optional_Element, Shuffle_Element, Single_Element_Swap, Move_Within_Sequence_Single_Element_Swap };

	public static @NonNull MoveTypes @NonNull [] getLegacyMoveTypes() {
		return legacyMoveTypes;
	}

	private static final @NonNull MoveTypes @NonNull [] guidedMoveTypes = new @NonNull MoveTypes @NonNull [] { Insert_DES_Purchase, Remove_DES_Purchase, //
		Insert_FOB_Sale, Remove_FOB_Sale, //
		Insert_Cargo, Remove_Cargo, //
		Swap_Cargo_Vessel, //
		Insert_Slot, Remove_Slot, Swap_Slot, //
		Insert_Vessel_Event, Remove_Vessel_Event, Swap_Event_Vessel, Move_Vessel_Event};

	public static @NonNull MoveTypes @NonNull [] getGuidedMoveTypes() {
		return guidedMoveTypes;
	}
}
