/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public final class PNLDeltaUtils {

	private PNLDeltaUtils() {

	}

	public static @Nullable Integer getElementProfitAndLoss(final @Nullable Object object) {

		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation //
				|| object instanceof VesselEventVisit //
				|| object instanceof StartEvent //
				|| object instanceof GeneratedCharterOut //
				|| object instanceof GroupedCharterLengthEvent //
				|| object instanceof GroupedCharterOutEvent //
				|| object instanceof GeneratedCharterLengthEvent //
				|| object instanceof OpenSlotAllocation || object instanceof EndEvent) {
			container = (ProfitAndLossContainer) object;
		}
		if (object instanceof SlotVisit slotVisit) {
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}
		return null;
	}
}
