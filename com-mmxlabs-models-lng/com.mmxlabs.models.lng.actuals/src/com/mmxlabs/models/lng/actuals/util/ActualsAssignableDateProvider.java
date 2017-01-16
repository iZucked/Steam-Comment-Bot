/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.util;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.IAssignableElementDateProvider;

public class ActualsAssignableDateProvider implements IAssignableElementDateProvider {

	private final Map<Slot, SlotActuals> slotActualsMap = new HashMap<>();

	public ActualsAssignableDateProvider(@Nullable final ActualsModel actualsModel) {
		generateMaps(actualsModel);
	}

	private void generateMaps(@Nullable final ActualsModel actualsModel) {
		if (actualsModel != null) {
			for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
				final Cargo cargo = cargoActuals.getCargo();
				if (cargo == null) {
					continue;
				}
				final List<Slot> slots = cargo.getSortedSlots();
				if (slots.isEmpty()) {
					continue;
				}
				for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
					slotActualsMap.put(slotActuals.getSlot(), slotActuals);
				}
			}
		}
	}

	@Override
	public @Nullable ZonedDateTime getSlotWindowStart(@NonNull final Slot slot) {
		if (slotActualsMap.containsKey(slot)) {
			final SlotActuals slotActuals = slotActualsMap.get(slot);
			return slotActuals.getOperationsStartAsDateTime();
		}

		return null;
	}

	@Override
	public @Nullable ZonedDateTime getSlotWindowEnd(@NonNull final Slot slot) {
		if (slotActualsMap.containsKey(slot)) {
			final SlotActuals slotActuals = slotActualsMap.get(slot);
			return slotActuals.getOperationsEndAsDateTime();
		}
		return null;
	}

	@Override
	public @NonNull OptionalInt getSlotDurationInHours(@NonNull Slot slot) {
		if (slotActualsMap.containsKey(slot)) {
			final SlotActuals slotActuals = slotActualsMap.get(slot);
			return OptionalInt.of(Hours.between(slotActuals.getOperationsStartAsDateTime(), slotActuals.getOperationsEndAsDateTime()));
		}
		return OptionalInt.empty();
	}
}
