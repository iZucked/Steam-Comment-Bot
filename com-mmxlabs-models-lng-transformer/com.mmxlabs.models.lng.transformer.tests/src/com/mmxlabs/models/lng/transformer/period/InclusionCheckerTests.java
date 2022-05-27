/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.Position;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer.InclusionRecord;

public class InclusionCheckerTests {

	@Test
	public void testSlotInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Slot slot = Mockito.mock(Slot.class);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(slot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(slot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_NoBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Slot slot = Mockito.mock(Slot.class);
		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);

		Mockito.when(slot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(slot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Arrives late
		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10));

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_UpperBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(slot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(slot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Classified as inside
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));

		// One hour later, classified as outside
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_UpperBound_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(slot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();
		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		objectToPortVisitMap.put(slot, portVisit);

		// Arrives late
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		// Classified as inside due to window
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));

		// One hour later, still in due to window
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testSlotInclusion_LowerBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(slot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(slot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Classified as outside
		Mockito.when(slot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));

		// Classified as outside as we use time window
		Mockito.when(slot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, slot, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_LowerBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(loadSlot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final SchedulingTimeWindow stw2 = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow()).thenReturn(stw2);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

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

		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot is late enough to to be in boundary area
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 17));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 17));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 18));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 24));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 25));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);

		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 22));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 24));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 25));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(loadSlot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final SchedulingTimeWindow stw2 = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow()).thenReturn(stw2);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_LowerBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(loadSlot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final SchedulingTimeWindow stw2 = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow()).thenReturn(stw2);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_UpperBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(loadSlot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final SchedulingTimeWindow stw2 = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow()).thenReturn(stw2);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 23);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));
	}

	@Disabled("WIP")
	@Test
	public void testCargoInclusion_UpperBounds_Late() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

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
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Load slot in boundary area
		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 20));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 26));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 27));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Discharge slot in boundary area
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 23);

		Mockito.when(loadPortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(loadPortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));
		Mockito.when(dischargePortVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 20));
		Mockito.when(dischargePortVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 21));
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testCargoInclusion_BothBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Cargo cargo = Mockito.mock(Cargo.class);
		final LoadSlot loadSlot = Mockito.mock(LoadSlot.class);
		final DischargeSlot dischargeSlot = Mockito.mock(DischargeSlot.class);

		final EList<Slot<?>> slotsList = new BasicEList<>();
		slotsList.add(loadSlot);
		slotsList.add(dischargeSlot);
		Mockito.when(cargo.getSlots()).thenReturn(slotsList);
		Mockito.when(cargo.getSortedSlots()).thenReturn(slotsList);

		final SchedulingTimeWindow stw = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(loadSlot.getSchedulingTimeWindow()).thenReturn(stw);
		Mockito.when(loadSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		final SchedulingTimeWindow stw2 = Mockito.mock(SchedulingTimeWindow.class);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow()).thenReturn(stw2);
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getSchedulingTimeWindow().getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();
		final Map<EObject, PortVisit> objectToPortVisitMap = new HashMap<>();

		// Completely out (cargo before period)
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely out (cargo after period)
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Intersect lower bound
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Intersect upper bound
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.After), getObjectInclusionType(checker, cargo, objectToPortVisitMap, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker,cargo, objectToPortVisitMap, periodRecord));

		// Intersect both boundaries
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Both), getObjectInclusionType(checker,cargo, objectToPortVisitMap, periodRecord));
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

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));
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

		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 14);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Before), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 12);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 10);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 11);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Boundary, Position.Both), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));

		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 11, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 12);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 14);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), getObjectInclusionType(checker,event, objectToPortVisitMap, periodRecord));
	}

	@Test
	public void testVesselCharterInclusion_NoVesselCharterBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter,  periodRecord));
	}

	@Test
	public void testVesselCharterInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		final PeriodRecord periodRecord = new PeriodRecord();

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter,  periodRecord));

		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselCharter.getStartBy()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 9, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselCharter.getEndBy()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 9, 0));

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter,  periodRecord));
	}

	@Test
	public void testVesselCharterInclusion_LowerCutoff_EndBySet() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);

		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 4, 0));
		//

		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));
	}

	@Test
	public void testVesselCharterInclusion_LowerCutoff_EndAfterSet() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfter()).thenReturn(PeriodTestUtils.createLocalDateTime(2014, Calendar.JULY, 8, 0));

		// Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		// Mockito.when(vesselCharter.getEndBy()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));
		//
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));
	}

	@Test
	public void testVesselCharterInclusion_Bounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		final PeriodRecord periodRecord = new PeriodRecord();

		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));

		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 15, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 0));

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.After), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));

		// Completely out
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.Out, Position.Before), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));
		// Partially in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 8);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));

		// Partially in
		periodRecord.lowerCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = PeriodTestUtils.createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = PeriodTestUtils.createDate(2014, Calendar.JULY, 19);
		Assertions.assertEquals(new NonNullPair<>(InclusionType.In, Position.Unknown), checker.getVesselCharterInclusionType(vesselCharter, periodRecord));
	}

	@Test
	public void getObjectInVesselCharterRangeTest() {

		final InclusionChecker checker = new InclusionChecker();

		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		final VesselCharter vesselCharter = Mockito.mock(VesselCharter.class);

		// Set some dates
		Mockito.when(portVisit.getStart()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8));
		Mockito.when(portVisit.getEnd()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9));

		// No bounds, so always in
		Assertions.assertEquals(InclusionType.In, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

		// Completely in
		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Assertions.assertEquals(InclusionType.In, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

		// Partially in
		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Assertions.assertEquals(InclusionType.In, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

		// Partially in
		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 6, 0));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 8, 1));

		Assertions.assertEquals(InclusionType.In, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

		// Completely out
		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 5, 0));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 6, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 7, 23));
		Assertions.assertEquals(InclusionType.Out, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

		// Completely out
		Mockito.when(vesselCharter.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getStartAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 9, 1));
		Mockito.when(vesselCharter.getStartByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 10, 0));

		Mockito.when(vesselCharter.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselCharter.getEndAfterAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 11, 0));
		Mockito.when(vesselCharter.getEndByAsDateTime()).thenReturn(PeriodTestUtils.createDate(2014, Calendar.JULY, 12, 0));
		Assertions.assertEquals(InclusionType.Out, checker.getObjectInVesselCharterRange(portVisit, vesselCharter));

	}

	public NonNullPair<InclusionType, Position> getObjectInclusionType(final InclusionChecker checker, @NonNull final Cargo object, @NonNull final Map<EObject, PortVisit> scheduledEventMap,
			@NonNull final PeriodRecord periodRecord) {

		final InclusionRecord r = new InclusionRecord();

		r.object = object;
		r.event = scheduledEventMap;
		return checker.getObjectInclusionType(r, periodRecord);
	}

	public NonNullPair<InclusionType, Position> getObjectInclusionType(final InclusionChecker checker, @NonNull final Slot object, @NonNull final Map<EObject, PortVisit> scheduledEventMap,
			@NonNull final PeriodRecord periodRecord) {

		final InclusionRecord r = new InclusionRecord();

		r.object = object;
		r.event = scheduledEventMap;
		return checker.getObjectInclusionType(r, periodRecord);
	}

	public NonNullPair<InclusionType, Position> getObjectInclusionType(final InclusionChecker checker, @NonNull final VesselEvent object, @NonNull final Map<EObject, PortVisit> scheduledEventMap,
			@NonNull final PeriodRecord periodRecord) {

		final InclusionRecord r = new InclusionRecord();

		r.object = object;
		r.event = scheduledEventMap;
		return checker.getObjectInclusionType(r, periodRecord);
	}

}
