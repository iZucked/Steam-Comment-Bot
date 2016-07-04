/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;

public final class SlotContractDetailsHelper {

	@Nullable
	public static <T extends GeneralPNLDetails> T findSlotContractDetails(@NonNull final SlotAllocation slotAllocation, @NonNull final Class<T> detailsCls) {
		final Slot modelSlot = slotAllocation.getSlot();
		final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
		if (modelSlot != null && cargoAllocation != null) {
			return findSlotContractDetails(modelSlot, cargoAllocation, detailsCls);
		}
		return null;
	}

	@Nullable
	public static <T extends GeneralPNLDetails> T findSlotContractDetails(@NonNull final Slot modelSlot, final @NonNull CargoAllocation cargoAllocation, @NonNull final Class<T> detailsCls) {

		SlotPNLDetails slotDetails = null;
		for (final GeneralPNLDetails generalPNLDetails : cargoAllocation.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				if (slotPNLDetails.getSlot() == modelSlot) {
					slotDetails = slotPNLDetails;
				}
			}
		}
		if (slotDetails == null) {
			return null;
		}

		for (final GeneralPNLDetails details : slotDetails.getGeneralPNLDetails()) {
			if (detailsCls.isInstance(details)) {
				return detailsCls.cast(details);
			}
		}
		return null;
	}

	@Nullable
	public static <T extends @NonNull GeneralPNLDetails> T findSlotContractDetails(final @Nullable CargoAllocation cargoAllocation, final Class<T> detailsCls) {

		for (final GeneralPNLDetails generalPNLDetails : cargoAllocation.getGeneralPNLDetails()) {
			if (generalPNLDetails instanceof SlotPNLDetails) {
				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
				for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
					if (detailsCls.isInstance(details)) {
						return detailsCls.cast(details);
					}
				}
			}
		}

		return null;
	}
}
