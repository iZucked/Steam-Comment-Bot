/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.DateTime;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.AssignableElementDateComparator;

public class ActualsAssignableElementComparator extends AssignableElementDateComparator {

	private final Map<AssignableElement, SlotActuals> startDateMap = new HashMap<>();
	private final Map<AssignableElement, SlotActuals> endDateMap = new HashMap<>();

	public ActualsAssignableElementComparator(@Nullable final ActualsModel actualsModel) {
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
				final Slot firstSlot = slots.get(0);
				final Slot lastSlot = slots.get(slots.size() - 1);
				for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
					if (slotActuals.getSlot() == firstSlot) {
						startDateMap.put(cargo, slotActuals);
					}
					if (slotActuals.getSlot() == lastSlot) {
						endDateMap.put(cargo, slotActuals);
					}
				}
			}
		}
	}

	@Override
	protected DateTime getStartDate(@Nullable final AssignableElement element) {
		if (startDateMap.containsKey(element)) {
			final SlotActuals slotActuals = startDateMap.get(element);
			return slotActuals.getOperationsStartAsDateTime();
		}

		return super.getStartDate(element);
	}

	@Override
	protected DateTime getEndDate(@Nullable final AssignableElement element) {
		if (endDateMap.containsKey(element)) {
			final SlotActuals slotActuals = endDateMap.get(element);
			return slotActuals.getOperationsEndAsDateTime();
		}
		return super.getEndDate(element);
	}
}
