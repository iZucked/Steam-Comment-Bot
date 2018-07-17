package com.mmxlabs.lngdataserver.integration.reports.cargo;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;

public class CargoReportUtils {
	public static String getEndBuyerText(DischargeSlot slot) {
		String counterPartyLabel = slot.getSlotOrDelegateCounterparty();
		if (counterPartyLabel != null) {
			return counterPartyLabel;
		} else {
			return "";
		}
	}
	
	public static LocalDate getNextLoadPortDate(CargoAllocation cargoAllocation) {
		if (cargoAllocation == null) {
			return null;
		}
		if (cargoAllocation.getCargoType() == CargoType.FOB) {
			final SlotAllocation loadAllocation = cargoAllocation.getSlotAllocations().get(0);
			final SlotAllocation dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);
			if (loadAllocation.getPort() != dischargeAllocation.getPort()) {
				ZonedDateTime date = dischargeAllocation.getSlot().getWindowStartWithSlotOrPortTime();
				date = date.plusHours(dischargeAllocation.getSlot().getSlotOrDelegateDuration());
				date = date.plusDays(9);
				date = date.withZoneSameInstant(ZoneId.of(cargoAllocation.getSlotAllocations().get(0).getSlot().getTimeZone(CargoPackage.Literals.SLOT__WINDOW_START)));
				return date.toLocalDate();
			}
		} else {
			final PortVisit nextLoad = findLoad(cargoAllocation, true);
			if (nextLoad != null) {
				return nextLoad.getStart().toLocalDate();
			}
		}
		return null;
	}
	
	private static PortVisit findLoad(final CargoAllocation cargoAllocation, final boolean next) {
		Event evt = null;

		if (cargoAllocation.getEvents().size() > 0) {
			if (next) {
				// try to find a load event in the next cargoAllocation
				evt = cargoAllocation.getEvents().get(cargoAllocation.getEvents().size() - 1).getNextEvent();
			} else {
				evt = cargoAllocation.getEvents().get(0);
			}
		}

		while (evt != null) {
			if (evt instanceof SlotVisitImpl) {
				final Slot slot = ((SlotVisitImpl) evt).getSlotAllocation().getSlot();
				if (slot instanceof LoadSlot) {
					return (SlotVisitImpl) evt;
				}
			} else if (evt instanceof EndEvent) {
				return (EndEvent) evt;
			}
			evt = evt.getNextEvent();
		}

		return null;
	}

}
