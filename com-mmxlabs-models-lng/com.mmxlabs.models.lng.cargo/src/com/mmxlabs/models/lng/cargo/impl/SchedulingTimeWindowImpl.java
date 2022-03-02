/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.TimePeriod;

public class SchedulingTimeWindowImpl implements SchedulingTimeWindow {

	final Slot<?> slot;
	
	public SchedulingTimeWindowImpl(Slot<?> slot) {
		this.slot = slot;
	}

	private int getSlotOrDelegateDuration() {
		return (Integer) slot.eGetWithDefault(CargoPackage.Literals.SLOT__DURATION);
	}
	
	@Override
	public int getDuration() {
		//NB: must reference original slot values.
		if (slot.isWindowCounterParty()) {
			return convertToHours(slot.getWindowSize(), slot.getWindowSizeUnits()) + getSlotOrDelegateDuration();
		}
		else {
			return getSlotOrDelegateDuration();
		}
	}

	private int convertToHours(int windowSize, TimePeriod p) {
		final ZonedDateTime start = getStart();
		ZonedDateTime end = start;
		
		if (windowSize == 0) {
			return 0;
		}

		switch (p) {
		case DAYS:
			end = end.plusDays(windowSize).minusHours(1);
			break;
		case HOURS:
			end = end.plusHours(windowSize) ;
			break;
		case MONTHS:
			end = end.plusMonths(windowSize).minusHours(1);
			break;
		default:
			break;
		}

		return Hours.between(start, end);
	}
	
	@Override
	public TimePeriod getSizeUnits() {
		return (TimePeriod) slot.eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS);
	}

	@Override
	public int getSize() {
		//NB: must reference original slot values.
		if (slot.isWindowCounterParty()) {
			return 0;
		}
		else {
			return (Integer) slot.eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE);
		}
	}	
	
	@Override
	public int getSizeInHours() {
		final TimePeriod p = getSizeUnits();
		final int windowSize = getSize();
		return convertToHours(windowSize, p);
	}

	@Override
	public ZonedDateTime getStart() {
		final LocalDate wStart = slot.getWindowStart();
		if (wStart == null) {
			return null;
		}
		ZonedDateTime dateTime = wStart.atStartOfDay(ZoneId.of(slot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
		final int startTime = (Integer) slot.eGetWithDefault(CargoPackage.eINSTANCE.getSlot_WindowStartTime());
		dateTime = dateTime.withHour(startTime);
		return dateTime;
	}

	@Override
	public ZonedDateTime getEnd() {
		final ZonedDateTime startTime = getStart();
		if (startTime == null) {
			return null;
		}
		
		return startTime.plusHours(getSizeInHours());
	}

	@Override
	public ZonedDateTime getStartWithFlex() {
		ZonedDateTime startTime = getStart();
		if (startTime == null) {
			return null;
		}
		final int slotFlex = slot.getWindowFlex();
		if (slotFlex < 0) {
			final TimePeriod p = slot.getWindowFlexUnits();
			switch (p) {
			case DAYS:
				startTime = startTime.minusDays(slotFlex).plusHours(1);
				break;
			case HOURS:
				startTime = startTime.minusHours(slotFlex) ;
				break;
			case MONTHS:
				startTime = startTime.minusMonths(slotFlex).plusHours(1);
				break;
			default:
				break;
			}
		}
		return startTime;
	}

	@Override
	public ZonedDateTime getEndWithFlex() {
		ZonedDateTime endTime = getEnd();
		if (endTime == null) {
			return null;
		}
		final int slotFlex = slot.getWindowFlex();
		if (slotFlex > 0) {
			final TimePeriod p = slot.getWindowFlexUnits();
			switch (p) {
			case DAYS:
				endTime = endTime.plusDays(slotFlex).minusHours(1);
				break;
			case HOURS:
				endTime = endTime.plusHours(slotFlex) ;
				break;
			case MONTHS:
				endTime = endTime.plusMonths(slotFlex).minusHours(1);
				break;
			default:
				break;
			}
		}
		return endTime;
	}
}
