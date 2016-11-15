/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * TODO: {@link #getSegmentStart(Event)} and {@link #getSegmentEnd(Event)} should be replaceable with the new {@link EventGrouping} interface.
 * 
 * @author sg
 *
 */
public class ScheduleModelUtils {

	/**
	 * Returns true if the event is the start of a sequence of events (and thus the prior events sequence ends) For example this could the Load up to the end of the voyage before another load.
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isSegmentStart(final Event event) {
		if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof SlotVisit) {
			if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}

	/**
	 * For the given event, find the segment start.
	 * 
	 * @param event
	 * @return
	 */
	public static Event getSegmentStart(final Event event) {

		Event start = event;
		// Find segment start
		while (start != null && !isSegmentStart(start)) {
			start = start.getPreviousEvent();
		}
		return start;
	}

	/**
	 * For the given event, find the last segment event.
	 * 
	 * @param event
	 * @return
	 */
	public static Event getSegmentEnd(final Event event) {

		Event prevEvent = event;
		Event currentEvent = event.getNextEvent();
		// Find segment start
		while (currentEvent != null && !isSegmentStart(currentEvent)) {
			prevEvent = currentEvent;
			currentEvent = currentEvent.getNextEvent();
		}
		return prevEvent;
	}

	/**
	 * Returns the total duration from start of this event to the start of the next segment.
	 * 
	 * See {@link ScheduleModelUtils#isSegmentStart(Event)}
	 * 
	 * @param event
	 * @return
	 */
	public static int getEventDuration(final Event event) {

		int duration = event.getDuration();
		Event next = event.getNextEvent();
		while (next != null && !isSegmentStart(next)) {
			duration += next.getDuration();
			next = next.getNextEvent();
		}
		return duration;
	}

	/**
	 * Returns the main {@link Event} linked to the input. This could be a {@link LoadSlot} {@link SlotVisit}, or {@link VesselEventVisit} etc.
	 * 
	 * @param scheduleModelObject
	 */
	public static @Nullable PortVisit getMainEvent(EObject scheduleModelObject) {
		if (scheduleModelObject instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) scheduleModelObject;
			scheduleModelObject = cargoAllocation.getSlotAllocations().get(0);
		}
		if (scheduleModelObject instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) scheduleModelObject;
			final Slot slot = slotAllocation.getSlot();
			if (slot instanceof LoadSlot) {
				return slotAllocation.getSlotVisit();
			}
		} else if (scheduleModelObject instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) scheduleModelObject;
			return vesselEventVisit;
		} else if (scheduleModelObject instanceof GeneratedCharterOut) {
			final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) scheduleModelObject;
			return generatedCharterOut;
		} else if (scheduleModelObject instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) scheduleModelObject;
			return endEvent;
		} else if (scheduleModelObject instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) scheduleModelObject;
			return startEvent;
		}
		return null;
	}

	public static @Nullable Journey getLinkedJourneyEvent(PortVisit portVisit) {
		final Event evt = portVisit.getNextEvent();
		if (evt instanceof Journey) {
			return (Journey) evt;
		}
		return null;

	}

	public static @Nullable Idle getLinkedIdleEvent(PortVisit portVisit) {
		Event evt = portVisit.getNextEvent();
		if (evt instanceof Journey) {
			evt = evt.getNextEvent();
		}
		if (evt instanceof Idle) {
			return (Idle) evt;
		}
		return null;

	}
	
	public static int sumFuelVolumes(List<FuelQuantity> fuels, FuelUnit fuelUnit) {
		int fuelTotal = 0;
		for (FuelQuantity fuel : fuels) {
			for (FuelAmount fuelAmount : fuel.getAmounts()) {
				if (fuelAmount.getUnit() == fuelUnit) {
					fuelTotal += fuelAmount.getQuantity();
				}
			}
		}
		return fuelTotal;
	}

}
