/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.Position;

public class InclusionCheckerTests {

	@Test
	public void testSlotInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Slot slot = Mockito.mock(Slot.class);

		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_NoBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Slot slot = Mockito.mock(Slot.class);

		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Arrives late
		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10));

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_UpperBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Classified as inside
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));

		// One hour later, classified as outside
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_UpperBound_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(slot, portVisit);

		// Arrives late
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		// Classified as inside due to window
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));

		// One hour later, still in due to window
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_LowerBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Classified as outside
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));

		// Classified as outside as we use time window
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_LowerBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		final PortVisit loadPortVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(loadSlot, loadPortVisit);

		final PortVisit dischargePortVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(dischargeSlot, dischargePortVisit);

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 20);
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot is late enough to to be in boundary area
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 17));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 17));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 24));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 25));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 22));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 24));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 25));
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_LowerBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_UpperBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 23);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Ignore("WIP")
	@Test
	public void testCargoInclusion_UpperBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		final PortVisit loadPortVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(loadSlot, loadPortVisit);

		final PortVisit dischargePortVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(dischargeSlot, dischargePortVisit);

		// Completely out
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 20);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 22);

		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 22, 1));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 24));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 26));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 27));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 20));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 26));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 27));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 23);

		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 20));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_BothBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot> slotsList = new BasicEList<Slot>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out (cargo before period)
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely out (cargo after period)
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Intersect lower bound
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Intersect upper bound
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));

		// Intersect both boundaries
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Both), checker.getObjectInclusionType(cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselEventInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselEvent event = Mockito.mock(VesselEvent.class);

		Mockito.when(event.getStartAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));
		Mockito.when(event.getStartBy()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 9, 0));
		Mockito.when(event.getDurationInDays()).thenReturn(2);

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselEventInclusion_Bounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselEvent event = Mockito.mock(VesselEvent.class);

		Mockito.when(event.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate("UTC", 2014, Calendar.JULY, 8, 0));
		Mockito.when(event.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate("UTC", 2014, Calendar.JULY, 9, 0));
		Mockito.when(event.getDurationInDays()).thenReturn(2);

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);

		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 14);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 12);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Both), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 11, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 12);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 14);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(event, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_NoVesselAvailabilityBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 9, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 9, 0));

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_LowerCutoff_EndBySet() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		// Assert.assertEquals(new Pair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, periodRecord));

		// Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		// Mockito.when(vesselAvailability.getEndAfter()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));

		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 4, 0));
		//

		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_LowerCutoff_EndAfterSet() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));

		// Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		// Mockito.when(vesselAvailability.getEndBy()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));
		//
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_Bounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0));

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		// Partially in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));

		// Partially in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getObjectInclusionType(vesselAvailability, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void getObjectInVesselAvailabilityRangeTest() {

		final InclusionChecker checker = new InclusionChecker();

		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		// Set some dates
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(portVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		// No bounds, so always in
		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Partially in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Partially in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 6, 0));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely out
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 5, 0));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 6, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely out
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 1));
		Mockito.when(vesselAvailability.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 11, 0));
		Mockito.when(vesselAvailability.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 12, 0));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

	}
}
