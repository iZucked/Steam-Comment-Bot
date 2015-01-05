/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignableElementDateComparator;

public class ActualsAssignableElementComparator extends AssignableElementDateComparator {

	private final Map<AssignableElement, SlotActuals> startDateMap = new HashMap<>();
	private final Map<AssignableElement, SlotActuals> endDateMap = new HashMap<>();

	public ActualsAssignableElementComparator(final ActualsModel actualsModel) {
		generateMaps(actualsModel);
	}

	private void generateMaps(final ActualsModel actualsModel) {
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

	// protected boolean overlaps(final Date start0, final Date end0, final Date start1, final Date end1) {
	// return !(end0.before(start1) || end1.before(start0));
	// }

	@Override
	protected Date getStartDate(final AssignableElement element) {
		if (startDateMap.containsKey(element)) {
			final SlotActuals slotActuals = startDateMap.get(element);
			return slotActuals.getOperationsStart();
		}
		return super.getStartDate(element);
	}

	@Override
	protected Date getEndDate(final AssignableElement element) {
		if (endDateMap.containsKey(element)) {
			final SlotActuals slotActuals = endDateMap.get(element);
			return slotActuals.getOperationsEnd();
		}
		return super.getEndDate(element);
	}
}
