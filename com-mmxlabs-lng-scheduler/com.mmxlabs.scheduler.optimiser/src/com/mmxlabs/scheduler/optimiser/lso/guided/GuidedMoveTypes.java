/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso.guided;

public enum GuidedMoveTypes {
	Move_Slot_NonShipped_Resource,
	// Note insert des may remove another cargo
	Insert_DES_Purchase, Remove_DES_Purchase, //
	Insert_FOB_Sale, Remove_FOB_Sale, //
	Insert_Cargo, Remove_Cargo, //
	Swap_Cargo_Vessel, //

	Insert_Slot, Remove_Slot, Swap_Slot,
	// Special move type - remove the desired pairing slot from the solution so we can re-insert it later. E.g. due to lateness it may not be possible to bind otherwise
	Remove_Linked_Slot, //
	Insert_Vessel_Event, Remove_Vessel_Event, Move_Vessel_Event // 
	// Swap_Event_Vessel, <-- Covered by Move_Vessel_Event?
}
