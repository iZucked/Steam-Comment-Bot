/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelUnit;
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
	private Journey ballastLegB;
	private final Idle ladenIdle;
	private final Idle ballastIdle;
	private Idle ballastIdleB;
	private final SlotAllocation loadAllocation;
	private final SlotAllocation dischargeAllocation;
	private SlotAllocation dischargeAllocationB;
	private CargoAllocation cargoAllocation;
	private final int eventCount;
	private int startHeel;
	private int endHeel;

	public SimpleCargoAllocation(final CargoAllocation cargoAllocation) {
		this.cargoAllocation = cargoAllocation;
		// Simple processing - assuming single load/discharge pairing
		final EList<Event> events = cargoAllocation.getEvents();
		eventCount = events.size();
		

		if (eventCount != 6 && eventCount != 9) {
			throw new IllegalStateException(
					"Expects Load/Discharge events with two journey and two idle events (6 events) or  Load/Discharge/Discharge events with three journey and three idle events (9 events)");
		}

		loadAllocation = cargoAllocation.getSlotAllocations().get(0);
		dischargeAllocation = cargoAllocation.getSlotAllocations().get(1);
		// LDD Event
		if (eventCount == 9) {
			dischargeAllocationB = cargoAllocation.getSlotAllocations().get(2);
		}

		// Process events list to get full set - which may or may not include nulls. For example we may be missing a Journey leg if there was no journey, or idle time if there was no idle.
		Event[] processedEvents = new Event[eventCount];

		int currentIndex = 0;
		for (Event e : events) {
			if (e instanceof Journey || e instanceof Idle || e instanceof SlotVisit) {
				processedEvents[currentIndex] = e;
				if (currentIndex == 0) {
					startHeel = e.getHeelAtStart();
				}
				if (currentIndex == eventCount - 1) {
					endHeel = e.getHeelAtEnd();
				}
			} else {
				throw new IllegalStateException("Unexpected event type");
			}
			currentIndex++;
		}
		ladenLeg = (Journey) processedEvents[1];
		ladenIdle = (Idle) processedEvents[2];
		ballastLeg = (Journey) processedEvents[4];
		ballastIdle = (Idle) processedEvents[5];
		if (eventCount == 9) {
			ballastLegB = (Journey) processedEvents[7];
			ballastIdleB = (Idle) processedEvents[8];
		}

	}

	public Journey getLadenLeg() {
		return ladenLeg;
	}

	public Journey getBallastLeg() {
		return ballastLeg;
	}

	public Journey getBallastLegB() {
		return ballastLegB;
	}

	public Idle getLadenIdle() {
		return ladenIdle;
	}

	public Idle getBallastIdle() {
		return ballastIdle;
	}

	public Idle getBallastIdleB() {
		return ballastIdleB;
	}

	public SlotAllocation getLoadAllocation() {
		return loadAllocation;
	}

	public SlotAllocation getDischargeAllocation() {
		return dischargeAllocation;
	}

	public SlotAllocation getDischargeAllocationB() {
		return dischargeAllocationB;
	}

	public int getLoadVolume() {
		return loadAllocation.getVolumeTransferred();
	}
	
	public int getPhysicalLoadVolume() {
		return loadAllocation.getPhysicalVolumeTransferred();
	}

	public int getDischargeVolume() {
		return dischargeAllocation.getVolumeTransferred();
	}
	
	public int getPhysicalDischargeVolume() {
		return dischargeAllocation.getPhysicalVolumeTransferred();
	}

	public int getDischargeVolumeB() {
		return dischargeAllocationB.getVolumeTransferred();
	}
	
	public int getPhysicalDischargeVolumeB() {
		return dischargeAllocationB.getPhysicalVolumeTransferred();
	}

	public CargoAllocation getCargoAllocation() {
		return cargoAllocation;
	}

	public EMap<CapacityViolationType, Long> getLoadViolations() {
		return loadAllocation.getSlotVisit().getViolations();
	}

	public EMap<CapacityViolationType, Long> getDischargeViolations() {
		return dischargeAllocation.getSlotVisit().getViolations();
	}

	public EMap<CapacityViolationType, Long> getDischargeViolationsB() {
		return dischargeAllocationB.getSlotVisit().getViolations();
	}

	public int getJourneyIdelFuelVolumeInM3() {
		int journeyIdleFuel = 0;
		journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ladenIdle.getFuels(), FuelUnit.M3);
		journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ladenLeg.getFuels(), FuelUnit.M3);
		journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ballastIdle.getFuels(), FuelUnit.M3);
		journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ballastLeg.getFuels(), FuelUnit.M3);
		if (eventCount == 9) {
			journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ballastIdleB.getFuels(), FuelUnit.M3);
			journeyIdleFuel += ScheduleModelUtils.sumFuelVolumes(ballastLegB.getFuels(), FuelUnit.M3);
		}
		return journeyIdleFuel;
	}

	public int getViolationsCount() {
		int violationCount = 0;
		violationCount += getLoadViolations().size();
		violationCount += getDischargeViolations().size();

		if (eventCount == 9) {
			violationCount += getDischargeViolationsB().size();
		}
		return violationCount;
	}

	public int getStartHeel() {
		return startHeel;
	}

	public int getEndHeel() {
		return endHeel;
	}

	public boolean isLDD() {
		return (eventCount == 9);
	}

	public boolean isLD() {
		return (eventCount == 6);
	}
}
