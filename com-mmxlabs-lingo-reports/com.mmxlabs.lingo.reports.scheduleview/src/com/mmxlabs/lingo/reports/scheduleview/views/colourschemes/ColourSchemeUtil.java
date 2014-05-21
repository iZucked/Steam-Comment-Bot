/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import java.util.Date;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class ColourSchemeUtil {

	public static boolean isOutsideTimeWindow(Event ev) {
		Date start = ev.getStart();
		if ((ev instanceof VesselEventVisit) && start.after(((VesselEventVisit) ev).getVesselEvent().getStartBy())) {
			return true;
		}

		if (ev instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) ev;
			if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
				return true;
			}
			// if (visit.getStart().before(visit.getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime())) {
			// return true;
			// }
		}
		return false;
	}

	public static boolean isLocked(final Event event, GanttChartViewer viewer) {
		// Stage 1: Find the cargo
		final Sequence sequence = event.getSequence();
		int index = sequence.getEvents().indexOf(event);
		Cargo cargo = null;
		while (cargo == null && index >= 0) {
			Object obj = sequence.getEvents().get(index);
			if (obj instanceof SlotVisit) {
				final SlotVisit slotVisit = ((SlotVisit) obj);
				final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				cargo = cargoAllocation.getInputCargo();
			} else if (obj instanceof VesselEventVisit) {
				break;
			}
			--index;
		}

		// Stage 2: Find the input assignment
		if (cargo != null) {
			return cargo.isLocked();
		}
		return false;
	}

	static Idle findIdleForJourney(final Journey journey) {
		final Sequence sequence = journey.getSequence();
		final int index = sequence.getEvents().indexOf(journey);
		if (index != -1 && index + 1 < sequence.getEvents().size()) {
			final Event event = sequence.getEvents().get(index + 1);
			if (event instanceof Idle) {
				return (Idle) event;
			}
		}
		return null;
	}

	static Journey findJourneyForIdle(final Idle idle) {
		final Sequence sequence = idle.getSequence();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof Journey) {
				return (Journey) event;
			}
		}
		return null;
	}

	static boolean isRiskyVoyage(final Journey journey, final Idle idle, float IdleRisk_speed, float IdleRisk_threshold) {

		if (journey == null) {
			return false;
		}

		final int distance = journey.getDistance();
		int totalTime = journey.getDuration();
		if (idle != null) {
			totalTime += idle.getDuration();
		}

		final int travelTime = Math.round(distance / IdleRisk_speed);

		return (travelTime / totalTime > IdleRisk_threshold);
	}

	public static boolean isSpot(final SlotVisit visit) {
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof SpotSlot) {
			return true;
		}
		return false;
	}

	public static boolean isFOBSaleCargo(final SlotVisit visit) {
		boolean isFOB;
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof LoadSlot) {
			return visit.getSlotAllocation().getCargoAllocation().getInputCargo().getCargoType() == CargoType.FOB;
		} else {
			isFOB = ((DischargeSlot) slot).isFOBSale();
		}
		return isFOB;
	}

	public static boolean isDESPurchaseCargo(final SlotVisit visit) {
		Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof LoadSlot) {
			return ((LoadSlot) slot).isDESPurchase();
		} else {
			return visit.getSlotAllocation().getCargoAllocation().getInputCargo().getCargoType() == CargoType.DES;
		}
	}
}
