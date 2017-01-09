/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import java.time.ZonedDateTime;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

public class ColourSchemeUtil {

	/**
	 * Checks to see if a journey is "tight", i.e. when idle time after the journey is taken into consideration, is there less than a specified amount of leeway travelling at a vessel-specified
	 * "service speed"?
	 * 
	 * @param journey
	 * @return
	 */
	public static boolean isJourneyTight(final Journey journey, final int leewayInHrs) {
		if (journey == null) {
			return false;
		}

		final int distance = journey.getDistance();
		int journeyPlusIdleTime = journey.getDuration();

		final Sequence sequence = journey.getSequence();
		if (sequence == null) {
			return false;
		}
		VesselClass vesselClass = null;

		// get vessel class directly from the sequence if it is a spot charter
		final CharterInMarket charterInMarket = sequence.getCharterInMarket();
		if (charterInMarket != null) {
			vesselClass = charterInMarket.getVesselClass();
		}
		// otherwise get it from the vessel allocation
		if (vesselClass == null) {
			final VesselAvailability avail = sequence.getVesselAvailability();
			if (avail == null) {
				return false;
			}
			final Vessel vessel = avail.getVessel();
			if (vessel == null) {
				return false;
			}
			vesselClass = vessel.getVesselClass();
		}
		if (vesselClass == null) {
			return false;
		}

		final VesselStateAttributes attr = journey.isLaden() ? vesselClass.getLadenAttributes() : vesselClass.getBallastAttributes();
		final double speed = attr.getServiceSpeed();

		final Event nextEvent = journey.getNextEvent();
		if (nextEvent instanceof Idle) {
			journeyPlusIdleTime += nextEvent.getDuration();
		}

		final double serviceTime = distance / speed;

		return journeyPlusIdleTime - serviceTime < leewayInHrs;
	}

	public static boolean isOutsideTimeWindow(final Event ev) {
		final ZonedDateTime start = ev.getStart();
		if (ev instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) ev;
			if (LatenessUtils.isLateExcludingFlex(vesselEventVisit)) {
				// Only highlight if over 24 hours
				if (LatenessUtils.getLatenessInHours(vesselEventVisit) > 24) {
					return true;
				}
			}
		}

		if (ev instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) ev;
			final Slot slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				if (visit.getStart().isAfter(slot.getWindowEndWithSlotOrPortTime())) {
					return true;
				}
			}
			// if (visit.getStart().before(visit.getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime())) {
			// return true;
			// }
		}
		return false;
	}

	public static boolean isLocked(final Event event, final GanttChartViewer viewer) {
		// Stage 1: Find the cargo
		final Sequence sequence = event.getSequence();
		int index = sequence.getEvents().indexOf(event);
		Cargo cargo = null;
		while (cargo == null && index >= 0) {
			final Object obj = sequence.getEvents().get(index);
			if (obj instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) obj;
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

	/*
	 * static boolean isRiskyVoyage(final Journey journey, final Idle idle, float IdleRisk_speed, float IdleRisk_threshold) {
	 * 
	 * if (journey == null) { return false; }
	 * 
	 * final int distance = journey.getDistance(); int totalTime = journey.getDuration(); if (idle != null) { totalTime += idle.getDuration(); }
	 * 
	 * final int travelTime = Math.round(distance / IdleRisk_speed);
	 * 
	 * return (travelTime / totalTime > IdleRisk_threshold); }
	 */

	public static boolean isSpot(final SlotVisit visit) {
		final Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof SpotSlot) {
			return true;
		}
		return false;
	}

	public static boolean isFOBSaleCargo(final SlotVisit visit) {
		boolean isFOB = false;
		final SlotAllocation slotAllocation = visit.getSlotAllocation();
		if (slotAllocation != null) {
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				if (cargoAllocation != null) {
					final Cargo inputCargo = cargoAllocation.getInputCargo();
					if (inputCargo != null) {
						return inputCargo.getCargoType() == CargoType.FOB;
					}
				}
			} else {
				isFOB = ((DischargeSlot) slot).isFOBSale();
			}
		}
		return isFOB;
	}

	public static boolean isDESPurchaseCargo(final SlotVisit visit) {
		final Slot slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof LoadSlot) {
			return ((LoadSlot) slot).isDESPurchase();
		} else {
			Cargo inputCargo = visit.getSlotAllocation().getCargoAllocation().getInputCargo();
			if (inputCargo != null) {
				return inputCargo.getCargoType() == CargoType.DES;
			}
		}
		return false;
	}
}
