/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RowTypeFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {
		if (object instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
			String type = "Open Slot";
			final Slot slot = openSlotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				type = "Long";
			} else if (slot instanceof DischargeSlot) {
				type = "Short";
			}
			return type;
		} else if (object instanceof SlotVisit || object instanceof CargoAllocation) {
			return "Cargo";
		} else if (object instanceof StartEvent) {
			return "Start";
		} else if (object instanceof EndEvent) {
			return "End";
		} else if (object instanceof GeneratedCharterOut) {
			return "Charter Out (virt)";
		} else if (object instanceof VesselEventVisit) {
			final VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
			if (vesselEvent instanceof DryDockEvent) {
				return "Dry Dock";
			} else if (vesselEvent instanceof MaintenanceEvent) {
				return "Maintenance";
			} else if (vesselEvent instanceof CharterOutEvent) {
				return "Charter Out";
			}
		}
		return "Unknown";
	}
}