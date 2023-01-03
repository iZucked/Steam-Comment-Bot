/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.tests;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SchedulingTimeWindowTest {
	
	private TestSlotImpl getTestSlotImpl() {
		LocalDate date = LocalDate.of(2015, 12, 5);
		TestSlotImpl sl = new TestSlotImpl();
		sl.setWindowStart(date);
		sl.setDuration(10);
		return sl;
	}

	//TODO
	private TestSlotImpl getTestSlotImplWithCounterPartyWindowSet() {
		LocalDate date = LocalDate.of(2015, 12, 5);
		TestSlotImpl sl = new TestSlotImpl();
		sl.setWindowStart(date);
		sl.setWindowCounterParty(true);
		//Set add 
		//TODO add duration, window size.
		return sl;
	}

	//TODO
	private TestSlotImpl getTestSlotImplWithManualCounterPartyWindow() {
		LocalDate date = LocalDate.of(2015, 12, 5);
		TestSlotImpl sl = new TestSlotImpl();
		sl.setWindowStart(date);
		sl.setWindowCounterParty(true);
		//TODO add duration, window size.
		return sl;
	}
	
	@Test
	void testGetStart() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getStart(), sl.getWindowStartWithSlotOrPortTime());		
	}
	
	@Test
	void testGetEnd() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getEnd(), sl.getWindowEndWithSlotOrPortTime());		
	}
	
	@Test
	void testGetStartWithFlex() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getStartWithFlex(), sl.getWindowStartWithSlotOrPortTimeWithFlex());				
	}
	
	@Test
	void testGetEndWithFlex() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getEndWithFlex(), sl.getWindowEndWithSlotOrPortTimeWithFlex());
	}
	
	@Test
	void testWithWindowCounterPartyTrue() {
		//TODO
		//test windowsize = 0.
		//test duration = originalWindowSize+originalDuration
	}
	
	@Test
	void testGetSize() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getSize(), sl.getSlotOrDelegateWindowSize());		
	}
	
	@Test
	void testGetSizeUnits() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getSizeUnits(), sl.getSlotOrDelegateWindowSizeUnits());
	}
	
	@Test
	void testGetDuration() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getDuration(), sl.getSlotOrDelegateDuration());		
	}
	
	@Test
	void testGetSizeInHours() {
		TestSlotImpl sl = getTestSlotImpl();
		Assertions.assertEquals(sl.getSchedulingTimeWindow().getSizeInHours(), sl.getWindowSizeInHours());
	}
}
