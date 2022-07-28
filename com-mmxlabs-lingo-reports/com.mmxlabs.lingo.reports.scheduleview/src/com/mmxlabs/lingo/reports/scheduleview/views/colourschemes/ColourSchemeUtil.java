/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
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

	private ColourSchemeUtil() {

	}

	/**
	 * Checks to see if a journey is "tight", i.e. when idle time after the journey
	 * is taken into consideration, is there less than a specified amount of leeway
	 * travelling at a vessel-specified "service speed"?
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
		Vessel vessel = null;

		// get vessel directly from the sequence if it is a spot charter
		final CharterInMarket charterInMarket = sequence.getCharterInMarket();
		if (charterInMarket != null) {
			vessel = charterInMarket.getVessel();
		}
		// otherwise get it from the vessel allocation
		if (vessel == null) {
			final VesselCharter avail = sequence.getVesselCharter();
			if (avail == null) {
				return false;
			}
			vessel = avail.getVessel();

		}
		if (vessel == null) {
			return false;
		}

		final VesselStateAttributes attr = journey.isLaden() ? vessel.getLadenAttributes() : vessel.getBallastAttributes();
		final double speed = attr.getVesselOrDelegateServiceSpeed();

		final Event nextEvent = journey.getNextEvent();
		if (nextEvent instanceof Idle) {
			journeyPlusIdleTime += nextEvent.getDuration();
		}

		final double serviceTime = distance / speed;

		return journeyPlusIdleTime - serviceTime < leewayInHrs;
	}

	public static boolean isOutsideTimeWindow(final Event ev) {
		if (ev instanceof final VesselEventVisit vesselEventVisit) {
			if (LatenessUtils.isLateExcludingFlex(vesselEventVisit)) {
				// Only highlight if over 24 hours
				if (LatenessUtils.getLatenessInHours(vesselEventVisit) > 24) {
					return true;
				}
			}
		}

		if (ev instanceof final SlotVisit visit) {
			final Slot<?> slot = visit.getSlotAllocation().getSlot();
			if (slot != null) {
				if (visit.getStart().isAfter(slot.getSchedulingTimeWindow().getEnd())) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isLocked(final Event event) {
		// Stage 1: Find the cargo
		final Sequence sequence = event.getSequence();
		if (sequence == null) {
			return false;
		}
		int index = sequence.getEvents().indexOf(event);
		Cargo cargo = null;
		while (cargo == null && index >= 0) {
			final Object obj = sequence.getEvents().get(index);
			if (obj instanceof final SlotVisit slotVisit) {
				final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
				if (slotAllocation != null) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot != null) {
						cargo = slot.getCargo();
					}
				}
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
			if (event instanceof final Idle idle) {
				return idle;
			}
		}
		return null;
	}

	static Journey findJourneyForIdle(final Idle idle) {
		final Sequence sequence = idle.getSequence();
		final int index = sequence.getEvents().indexOf(idle);
		if (index != -1 && index - 1 >= 0) {
			final Event event = sequence.getEvents().get(index - 1);
			if (event instanceof final Journey journey) {
				return journey;
			}
		}
		return null;
	}

	/*
	 * static boolean isRiskyVoyage(final Journey journey, final Idle idle, float
	 * IdleRisk_speed, float IdleRisk_threshold) {
	 * 
	 * if (journey == null) { return false; }
	 * 
	 * final int distance = journey.getDistance(); int totalTime =
	 * journey.getDuration(); if (idle != null) { totalTime += idle.getDuration(); }
	 * 
	 * final int travelTime = Math.round(distance / IdleRisk_speed);
	 * 
	 * return (travelTime / totalTime > IdleRisk_threshold); }
	 */

	public static boolean isSpot(final SlotVisit visit) {
		final Slot<?> slot = visit.getSlotAllocation().getSlot();
		return slot instanceof SpotSlot;
	}

	public static boolean isFOBSaleCargo(final SlotVisit visit) {
		boolean isFOB = false;
		final SlotAllocation slotAllocation = visit.getSlotAllocation();
		if (slotAllocation != null) {
			final Slot<?> slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
				if (cargoAllocation != null) {
					return cargoAllocation.getCargoType() == CargoType.FOB;
				}
			} else if (slot instanceof final DischargeSlot ds) {
				isFOB = ds.isFOBSale();
			}
		}
		return isFOB;
	}

	public static boolean isDESPurchaseCargo(final SlotVisit visit) {
		final Slot<?> slot = visit.getSlotAllocation().getSlot();
		if (slot instanceof final LoadSlot ls) {
			return ls.isDESPurchase();
		} else {
			final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
			if (cargoAllocation != null) {
				return cargoAllocation.getCargoType() == CargoType.DES;
			}
		}
		return false;
	}
}
