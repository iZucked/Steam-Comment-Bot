/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * TODO: {@link #getSegmentStart(Event)} and {@link #getSegmentEnd(Event)} should be replaceable with the new {@link EventGrouping} interface. 
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
}
