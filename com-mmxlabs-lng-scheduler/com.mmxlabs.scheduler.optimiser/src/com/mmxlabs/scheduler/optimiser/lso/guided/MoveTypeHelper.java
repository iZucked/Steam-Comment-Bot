package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class MoveTypeHelper {

	@Inject
	@NonNull
	private IGuidedMoveHelper helper;

	public List<MoveTypes> getMoveTypes(@Nullable final IResource resource, @NonNull final ISequenceElement element) {
		// Is element locked? Then we can not do anything
		final List<MoveTypes> moveTypes = new LinkedList<>();

		// We get here with non-shipped slots!
		if (helper.isLockedToVessel(element) && resource != null && !helper.isOptional(element)) {// && helper.isShipped(element)) {
			// TODO: Strictly we could shift within the sequence
			// moveTypes.add(MoveTypes.Swap_Slot/*_InSequence*/);
			return moveTypes;
		}
		if (helper.isStartOrEndSlot(element)) {
			return moveTypes;
		}
		
		if (resource == null) {
			// Currently unused
			if (helper.isVesselEvent(element)) {
				if (!helper.isRelocatedCharterOutEvent(element)) {
					// If relocated charter event, do nothing.
					moveTypes.add(MoveTypes.Insert_Vessel_Event);
				}
			}
			if (helper.isDESPurchase(element)) {
				moveTypes.add(MoveTypes.Insert_DES_Purchase);
			} else if (helper.isFOBSale(element)) {
				moveTypes.add(MoveTypes.Insert_FOB_Sale);
			} else {
				moveTypes.add(MoveTypes.Swap_Slot);
				moveTypes.add(MoveTypes.Insert_Slot);
				moveTypes.add(MoveTypes.Insert_Cargo);
			}

		} else {
			// Currently used
			if (helper.isVesselEvent(element)) {
				if (!helper.isRelocatedCharterOutEvent(element)) {
					// If relocated charter event, do nothing.
					if (helper.isOptional(element)) {
						moveTypes.add(MoveTypes.Remove_Vessel_Event);
					}
					moveTypes.add(MoveTypes.Swap_Event_Vessel);
					moveTypes.add(MoveTypes.Move_Vessel_Event);
				}
			} else if (helper.isDESPurchase(element) && helper.isOptional(element)) {
				moveTypes.add(MoveTypes.Remove_DES_Purchase);
			} else if (helper.isFOBSale(element) && helper.isOptional(element)) {
				moveTypes.add(MoveTypes.Remove_FOB_Sale);
			} else {
				moveTypes.add(MoveTypes.Swap_Slot);
				moveTypes.add(MoveTypes.Swap_Cargo_Vessel);
				if (helper.isOptional(element)) {
					moveTypes.add(MoveTypes.Remove_Slot);
					moveTypes.add(MoveTypes.Remove_Cargo);
				}
			}
		}
		return moveTypes;
	}
}
