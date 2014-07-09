package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.InclusionType;
import com.mmxlabs.models.lng.transformer.period.InclusionChecker.PeriodRecord;

public class InclusionCheckerTest {

	@Test
	public void testSlotInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final Slot slot = Mockito.mock(Slot.class);

		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		final PeriodRecord periodRecord = new PeriodRecord();

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(slot, periodRecord));
	}

	@Test
	public void testSlotInclusion_UpperBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		// Classified as inside
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8, 0));
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(slot, periodRecord));

		// One hour later, classified as outside
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8, 1));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(slot, periodRecord));
	}

	@Test
	public void testSlotInclusion_LowerBound() {

		final InclusionChecker checker = new InclusionChecker();

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 8);

		final Slot slot = Mockito.mock(Slot.class);
		Mockito.when(slot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 7, 0));

		// Classified as outside
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 7, 23));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(slot, periodRecord));

		// One hour later, classified as inside
		Mockito.when(slot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8, 0));
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(slot, periodRecord));
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

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(cargo, periodRecord));
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

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		// Completely out
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(cargo, periodRecord));

		// Discharge slot in boundary area
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 18);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Load slot in boundary area
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 10);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(cargo, periodRecord));
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

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		// Completely out
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(cargo, periodRecord));

		// Load slot in boundary area
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Discharge slot in boundary area
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 15, 23);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Completely in
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 16, 0);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(cargo, periodRecord));
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

		Mockito.when(loadSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(loadSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(dischargeSlot.getWindowStartWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 15));
		Mockito.when(dischargeSlot.getWindowEndWithSlotOrPortTime()).thenReturn(createDate(2014, Calendar.JULY, 16));

		final PeriodRecord periodRecord = new PeriodRecord();

		// Completely out (cargo before period)
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(cargo, periodRecord));

		// Completely out (cargo after period)
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(cargo, periodRecord));

		// Intersect both boundaries
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 16);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Intersect lower bound
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 11);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Intersect upper bound
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 15);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 16);
		Assert.assertEquals(InclusionType.Boundary, checker.getObjectInclusionType(cargo, periodRecord));

		// Completely in
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 7);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 8);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 16);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 17);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(cargo, periodRecord));
	}

	@Test
	public void testVesselEventInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselEvent event = Mockito.mock(VesselEvent.class);

		Mockito.when(event.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(event.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 9));
		Mockito.when(event.getDurationInDays()).thenReturn(2);

		final PeriodRecord periodRecord = new PeriodRecord();

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(event, periodRecord));
	}

	@Test
	public void testVesselEventInclusion_Bounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselEvent event = Mockito.mock(VesselEvent.class);

		Mockito.when(event.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(event.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 9));
		Mockito.when(event.getDurationInDays()).thenReturn(2);

		final PeriodRecord periodRecord = new PeriodRecord();

		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(event, periodRecord));

		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 8);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 9);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 14);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(event, periodRecord));

		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 10);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 10);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 10);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 11);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(event, periodRecord));

		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 11, 1);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 12);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 13);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 14);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(event, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_NoVesselAvailabilityBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 7, 23);

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_NoBounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));

		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));
	}

	@Test
	public void testVesselAvailabilityInclusion_Bounds() {

		final InclusionChecker checker = new InclusionChecker();

		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		final PeriodRecord periodRecord = new PeriodRecord();

		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));

		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 9));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 15));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 16));

		// Completely out
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 7, 23);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(vesselAvailability, periodRecord));

		// Completely out
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 16, 1);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(InclusionType.Out, checker.getObjectInclusionType(vesselAvailability, periodRecord));

		// Partially in
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 5);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 6);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 7);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 8);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));

		// Partially in
		periodRecord.lowerCutoff = createDate(2014, Calendar.JULY, 16);
		periodRecord.lowerBoundary = createDate(2014, Calendar.JULY, 17);
		periodRecord.upperBoundary = createDate(2014, Calendar.JULY, 18);
		periodRecord.upperCutoff = createDate(2014, Calendar.JULY, 19);
		Assert.assertEquals(InclusionType.In, checker.getObjectInclusionType(vesselAvailability, periodRecord));
	}

	public void getObjectInVesselAvailabilityRangeTest() {

		final InclusionChecker checker = new InclusionChecker();

		final PortVisit portVisit = Mockito.mock(PortVisit.class);
		final VesselAvailability vesselAvailability = Mockito.mock(VesselAvailability.class);

		// Set some dates
		Mockito.when(portVisit.getStart()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(portVisit.getEnd()).thenReturn(createDate(2014, Calendar.JULY, 9));

		// No bounds, so always in
		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 7));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 8));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 9));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 10));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Partially in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 7, 23));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 8));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 9));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 10));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Partially in
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 6));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 7));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 8));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 8, 1));

		Assert.assertEquals(InclusionType.In, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely out
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 5));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 6));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 7));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 7, 23));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

		// Completely out
		Mockito.when(vesselAvailability.isSetStartAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetStartBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getStartAfter()).thenReturn(createDate(2014, Calendar.JULY, 9, 1));
		Mockito.when(vesselAvailability.getStartBy()).thenReturn(createDate(2014, Calendar.JULY, 10));

		Mockito.when(vesselAvailability.isSetEndAfter()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.isSetEndBy()).thenReturn(Boolean.TRUE);
		Mockito.when(vesselAvailability.getEndAfter()).thenReturn(createDate(2014, Calendar.JULY, 11));
		Mockito.when(vesselAvailability.getEndBy()).thenReturn(createDate(2014, Calendar.JULY, 12));
		Assert.assertEquals(InclusionType.Out, checker.getObjectInVesselAvailabilityRange(portVisit, vesselAvailability));

	}

	private static Date createDate(final int year, final int month, final int day) {
		return createDate(year, month, day, 0);
	}

	private static Date createDate(final int year, final int month, final int day, final int hour) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
}
