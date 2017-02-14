package com.mmxlabs.scheduler.optimiser.lso.guided;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.moves.util.IMoveHelper;

public class MoveTypeHelper {

	@Inject
	@NonNull
	private IMoveHelper helper;

	public @NonNull Collection<@NonNull MoveTypes> getMoveTypes(@Nullable final IResource resource, @NonNull final ISequenceElement element) {
		return getMoveTypes(resource, element, true);
	}

	/**
	 * Generate a map of possible move types for all elements in the given {@link IOptimisationData}. (Note this will currently exclude alternative elements). The list of move types should be further
	 * checked once an element has been assigned a {@link IResource} (or to the unused list)
	 * 
	 * @param optimisationData
	 * @return
	 */
	public @NonNull Map<ISequenceElement, @NonNull Collection<@NonNull MoveTypes>> getMoveTypes(@NonNull IOptimisationData optimisationData) {
		Map<ISequenceElement, @NonNull Collection<@NonNull MoveTypes>> map = new HashMap<>();
		for (ISequenceElement element : optimisationData.getSequenceElements()) {
			map.put(element, getMoveTypes(null, element, false));
		}

		return map;
	}

	public @NonNull Collection<@NonNull MoveTypes> getMoveTypes(@Nullable final IResource resource, @NonNull final ISequenceElement element, boolean checkResource) {
		// Is element locked? Then we can not do anything
		final Set<MoveTypes> moveTypes = new LinkedHashSet<>();

		if (checkResource) {
			// We get here with non-shipped slots!
			if (helper.isLockedToVessel(element) && resource != null && !helper.isOptional(element)) {// && helper.isShipped(element)) {
				// TODO: Strictly we could shift within the sequence
				// moveTypes.add(MoveTypes.Swap_Slot/*_InSequence*/);
				return moveTypes;
			}
		}
		if (helper.isStartOrEndSlot(element)) {
			return moveTypes;
		}

		if (!checkResource || resource == null) {
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
				moveTypes.add(MoveTypes.Remove_Linked_Slot);
			}

		}

		if (!checkResource || resource != null) {
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
			} else if (helper.isDESPurchase(element)) {
				if (helper.isOptional(element)) {
					moveTypes.add(MoveTypes.Remove_DES_Purchase);
				}
			} else if (helper.isFOBSale(element)) {
				if (helper.isOptional(element)) {
					moveTypes.add(MoveTypes.Remove_FOB_Sale);
				}
			} else {
				if (resource != null && helper.isNonShippedResource(resource)) {
					moveTypes.add(MoveTypes.Move_Slot_NonShipped_Resource);
				}
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
