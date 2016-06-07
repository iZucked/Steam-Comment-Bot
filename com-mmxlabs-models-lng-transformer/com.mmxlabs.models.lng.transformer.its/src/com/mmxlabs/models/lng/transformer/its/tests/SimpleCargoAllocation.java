/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

/**
 * 
 * A wrapper around a {@link CargoAllocation} to restore the old API in the case of a two slot cargo - as most of the test cases work on this assumption.
 * 
 */
public class SimpleCargoAllocation {

	private final Journey ladenLeg;
	private final Journey ballastLeg;
	private final Idle ladenIdle;
	private final Idle ballastIdle;
	private final SlotAllocation loadAllocation;
	private final SlotAllocation dischargeAllocation;
	private CargoAllocation cargoAllocation;

	public SimpleCargoAllocation(final CargoAllocation cargoAllocation) {
		this.cargoAllocation = cargoAllocation;
		// Simple processing - assuming single load/discharge pairing
		final EList<Event> events = cargoAllocation.getEvents();

		if (events.size() > 6) {
			throw new IllegalStateException("Expects Simple Cargo Allocation - single load, single discharge  plug Journey and Idle times");
		}
		if (cargoAllocation.getSlotAllocations().size() != 2) {
			throw new IllegalStateException("Expects Simple Cargo Allocation - single load, single discharge  plug Journey and Idle times");
		}

		loadAllocation = cargoAllocation.getSlotAllocations().get(0);
		dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);

		// Process events list to get full set - which may or may not include nulls. For example we may be missing a Journey leg if there was no journey, or idle time if there was no idle.
		Event[] processedEvents = new Event[4];

		int currentIndex = 0;
		for (Event e : events) {
			if (e instanceof Journey) {
				if (currentIndex % 2 == 0) {
					processedEvents[currentIndex] = e;
				} else {
					processedEvents[currentIndex + 1] = e;
					currentIndex++;
				}
			} else if (e instanceof Idle) {
				if (currentIndex % 2 != 0) {
					processedEvents[currentIndex] = e;

				} else {
					processedEvents[currentIndex + 1] = e;
					currentIndex++;
				}
			} else if (e instanceof SlotVisit) {
				// Ignore as already looked up
				continue;
			} else {
				throw new IllegalStateException("Unexpected event type");
			}
			currentIndex++;
		}
		ladenLeg = (Journey) processedEvents[0];
		ladenIdle = (Idle) processedEvents[1];
		ballastLeg = (Journey) processedEvents[2];
		ballastIdle = (Idle) processedEvents[3];
	}

	public Journey getLadenLeg() {
		return ladenLeg;
	}

	public Journey getBallastLeg() {
		return ballastLeg;
	}

	public Idle getLadenIdle() {
		return ladenIdle;
	}

	public Idle getBallastIdle() {
		return ballastIdle;
	}

	public SlotAllocation getLoadAllocation() {
		return loadAllocation;
	}

	public SlotAllocation getDischargeAllocation() {
		return dischargeAllocation;
	}

	public int getLoadVolume() {
		return loadAllocation.getVolumeTransferred();
	}

	public int getDischargeVolume() {
		return dischargeAllocation.getVolumeTransferred();
	}

	public CargoAllocation getCargoAllocation() {
		return cargoAllocation;
	}
}
