/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.tests;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.impl.SlotImpl;
import com.mmxlabs.models.lng.types.TimePeriod;

public class TestSlotImpl extends SlotImpl {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getWindowEndWithSlotOrPortTime() {
		final ZonedDateTime startTime = getWindowStartWithSlotOrPortTime();
		if (startTime == null) {
			return null;
		}
		
		return startTime.plusHours(getWindowSizeInHours());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getWindowEndWithSlotOrPortTimeWithFlex() {
		ZonedDateTime endTime = getWindowEndWithSlotOrPortTime();
		if (endTime == null) {
			return null;
		}
		final int slotFlex = getWindowFlex();
		if (slotFlex > 0) {
			final TimePeriod p  = getWindowFlexUnits();
			switch (p) {
			case DAYS:
				endTime = endTime.plusDays(slotFlex).minusHours(1);
				break;
			case HOURS:
				endTime = endTime.plusHours(slotFlex) ;
				break;
			case MONTHS:
				endTime  = endTime.plusMonths(slotFlex).minusHours(1);
				break;
			default:
				break;
			}
		}
		return endTime;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ZonedDateTime getWindowStartWithSlotOrPortTime() {
		final LocalDate wStart = getWindowStart();
		if (wStart == null) {
			return null;
		}
		ZonedDateTime dateTime = wStart.atStartOfDay(ZoneId.of(getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
		final int startTime = (Integer) eGetWithDefault(CargoPackage.eINSTANCE.getSlot_WindowStartTime());
		dateTime = dateTime.withHour(startTime);
		return dateTime;
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public ZonedDateTime getWindowStartWithSlotOrPortTimeWithFlex() {
		ZonedDateTime startTime = getWindowStartWithSlotOrPortTime();
		if (startTime == null) {
			return null;
		}
		final int slotFlex = getWindowFlex();
		if (slotFlex < 0) {
			final TimePeriod p  = getWindowFlexUnits();
			switch (p) {
			case DAYS:
				startTime = startTime.minusDays(slotFlex).plusHours(1);
				break;
			case HOURS:
				startTime = startTime.minusHours(slotFlex) ;
				break;
			case MONTHS:
				startTime  = startTime.minusMonths(slotFlex).plusHours(1);
				break;
			default:
				break;
			}
		}
		return startTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getSlotOrDelegateWindowSize() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TimePeriod getSlotOrDelegateWindowSizeUnits() {
		return (TimePeriod) eGetWithDefault(CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS);
	}

	public int getSlotOrDelegateDuration() {
		return (Integer) eGetWithDefault(CargoPackage.Literals.SLOT__DURATION);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getWindowSizeInHours() {
		final ZonedDateTime start = getWindowStartWithSlotOrPortTime();
		ZonedDateTime end = start;
		final TimePeriod p  = getSlotOrDelegateWindowSizeUnits();
		final int windowSize = getSlotOrDelegateWindowSize();
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
}
