package com.mmxlabs.scheduler.optimiser.lso.guided;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.IGuidedMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertDESPurchaseMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.InsertFOBSaleMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.MoveSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveCargoMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.RemoveLinkedSlotMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapCargoVesselMoveHandler;
import com.mmxlabs.scheduler.optimiser.lso.guided.handlers.SwapSlotMoveHandler;

public class GuidedMoveMapper {

	@Inject
	private Injector injector;

	@Nullable
	public IGuidedMoveHandler getMoveHandler(final @NonNull GuidedMoveTypes moveType) {
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
			return injector.getInstance(SwapCargoVesselMoveHandler.class);
		case Remove_Cargo:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_DES_Purchase:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_FOB_Sale:
			return injector.getInstance(RemoveCargoMoveHandler.class);
		case Remove_Slot:
			// No InsertSlotMove, but is we are leaving partial segments, this may be needed.
			// Insert slot not implemented, so do not used RemoveSlot. Keep to remove cargo
			// return injector.getInstance(RemoveSlotMoveHandler.class);
		case Remove_Vessel_Event:
			break;
		case Swap_Cargo_Vessel:
			return injector.getInstance(SwapCargoVesselMoveHandler.class);
		case Swap_Event_Vessel:
			break;
		case Swap_Slot:
			return injector.getInstance(SwapSlotMoveHandler.class);
		case Move_Slot_NonShipped_Resource:
			return injector.getInstance(MoveSlotMoveHandler.class);
		default:
			break;
		}

		return null;
	}

}
