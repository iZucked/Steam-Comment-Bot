/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;

public class RelatedSlotAllocationsTest {

	@Test
	public void testUnrelatedSlots() {

		final LoadSlot load1 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge1 = Mockito.mock(DischargeSlot.class);
		final LoadSlot load2 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge2 = Mockito.mock(DischargeSlot.class);

		Mockito.when(load1.getName()).thenReturn("Load1");
		Mockito.when(discharge1.getName()).thenReturn("Discharge1");
		Mockito.when(load2.getName()).thenReturn("Load2");
		Mockito.when(discharge2.getName()).thenReturn("Discharge2");

		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();

		rsa.addRelatedSlot(load1, discharge1);
		rsa.addRelatedSlot(load2, discharge2);

		final CargoAllocation cargoAllocation1 = ScheduleFactory.eINSTANCE.createCargoAllocation();
		final CargoAllocation cargoAllocation2 = ScheduleFactory.eINSTANCE.createCargoAllocation();

		final SlotAllocation loadAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		final SlotAllocation loadAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		cargoAllocation1.getSlotAllocations().add(loadAllocation1);
		cargoAllocation1.getSlotAllocations().add(dischargeAllocation1);

		cargoAllocation2.getSlotAllocations().add(loadAllocation2);
		cargoAllocation2.getSlotAllocations().add(dischargeAllocation2);

		loadAllocation1.setSlot(load1);
		dischargeAllocation1.setSlot(discharge1);

		loadAllocation2.setSlot(load2);
		dischargeAllocation2.setSlot(discharge2);

		final Set<Slot> relatedSetFor1_loads = rsa.getRelatedSetFor(cargoAllocation1, true);
		final Set<Slot> relatedSetFor1_discharges = rsa.getRelatedSetFor(cargoAllocation1, false);
		Assert.assertTrue(relatedSetFor1_loads.contains(load1));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_loads.contains(load2));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge2));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load1));
		Assert.assertTrue(relatedSetFor1_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load2));
		Assert.assertFalse(relatedSetFor1_discharges.contains(discharge2));

		final Set<Slot> relatedSetFor2_loads = rsa.getRelatedSetFor(cargoAllocation2, true);
		final Set<Slot> relatedSetFor2_discharges = rsa.getRelatedSetFor(cargoAllocation2, false);
		Assert.assertFalse(relatedSetFor2_loads.contains(load1));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge1));
		Assert.assertTrue(relatedSetFor2_loads.contains(load2));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge2));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load2));
		Assert.assertTrue(relatedSetFor2_discharges.contains(discharge2));
	}

	@Test
	public void testRelatedSlots() {

		final LoadSlot load1 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge1 = Mockito.mock(DischargeSlot.class);
		final LoadSlot load2 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge2 = Mockito.mock(DischargeSlot.class);

		Mockito.when(load1.getName()).thenReturn("Load");
		Mockito.when(discharge1.getName()).thenReturn("Discharge");
		Mockito.when(load2.getName()).thenReturn("Load");
		Mockito.when(discharge2.getName()).thenReturn("Discharge");

		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();

		rsa.addRelatedSlot(load1, discharge1);
		rsa.addRelatedSlot(load2, discharge2);

		final CargoAllocation cargoAllocation1 = ScheduleFactory.eINSTANCE.createCargoAllocation();
		final CargoAllocation cargoAllocation2 = ScheduleFactory.eINSTANCE.createCargoAllocation();

		final SlotAllocation loadAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		final SlotAllocation loadAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		cargoAllocation1.getSlotAllocations().add(loadAllocation1);
		cargoAllocation1.getSlotAllocations().add(dischargeAllocation1);

		cargoAllocation2.getSlotAllocations().add(loadAllocation2);
		cargoAllocation2.getSlotAllocations().add(dischargeAllocation2);

		loadAllocation1.setSlot(load1);
		dischargeAllocation1.setSlot(discharge1);

		loadAllocation2.setSlot(load2);
		dischargeAllocation2.setSlot(discharge2);

		final Set<Slot> relatedSetFor1_loads = rsa.getRelatedSetFor(cargoAllocation1, true);
		final Set<Slot> relatedSetFor1_discharges = rsa.getRelatedSetFor(cargoAllocation1, false);
		Assert.assertTrue(relatedSetFor1_loads.contains(load1));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge1));
		Assert.assertTrue(relatedSetFor1_loads.contains(load2));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge2));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load1));
		Assert.assertTrue(relatedSetFor1_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load2));
		Assert.assertTrue(relatedSetFor1_discharges.contains(discharge2));

		final Set<Slot> relatedSetFor2_loads = rsa.getRelatedSetFor(cargoAllocation2, true);
		final Set<Slot> relatedSetFor2_discharges = rsa.getRelatedSetFor(cargoAllocation2, false);
		Assert.assertTrue(relatedSetFor2_loads.contains(load1));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge1));
		Assert.assertTrue(relatedSetFor2_loads.contains(load2));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge2));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load1));
		Assert.assertTrue(relatedSetFor2_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load2));
		Assert.assertTrue(relatedSetFor2_discharges.contains(discharge2));
	}

	@Test
	public void testRelatedBySpotLoad() {

		final LoadSlot load1 = Mockito.mock(SpotLoadSlot.class);
		final DischargeSlot discharge1 = Mockito.mock(DischargeSlot.class);
		final LoadSlot load2 = Mockito.mock(SpotLoadSlot.class);
		final DischargeSlot discharge2 = Mockito.mock(DischargeSlot.class);

		Mockito.when(load1.getName()).thenReturn("Load");
		Mockito.when(discharge1.getName()).thenReturn("Discharge1");
		Mockito.when(load2.getName()).thenReturn("Load");
		Mockito.when(discharge2.getName()).thenReturn("Discharge2");

		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();

		rsa.addRelatedSlot(load1, discharge1);
		rsa.addRelatedSlot(load2, discharge2);

		final CargoAllocation cargoAllocation1 = ScheduleFactory.eINSTANCE.createCargoAllocation();
		final CargoAllocation cargoAllocation2 = ScheduleFactory.eINSTANCE.createCargoAllocation();

		final SlotAllocation loadAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		final SlotAllocation loadAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		cargoAllocation1.getSlotAllocations().add(loadAllocation1);
		cargoAllocation1.getSlotAllocations().add(dischargeAllocation1);

		cargoAllocation2.getSlotAllocations().add(loadAllocation2);
		cargoAllocation2.getSlotAllocations().add(dischargeAllocation2);

		loadAllocation1.setSlot(load1);
		dischargeAllocation1.setSlot(discharge1);

		loadAllocation2.setSlot(load2);
		dischargeAllocation2.setSlot(discharge2);

		final Set<Slot> relatedSetFor1_loads = rsa.getRelatedSetFor(cargoAllocation1, true);
		Assert.assertTrue(relatedSetFor1_loads.contains(load1));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge1));
		// Expect other spot instance to be filtered out by the getRelatedSetFor(CargoAllocation) call
		Assert.assertFalse(relatedSetFor1_loads.contains(load2));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge2));

		final Set<Slot> relatedSetFor1_discharges = rsa.getRelatedSetFor(cargoAllocation1, false);
		Assert.assertFalse(relatedSetFor1_discharges.contains(load1));
		Assert.assertTrue(relatedSetFor1_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load2));
		Assert.assertFalse(relatedSetFor1_discharges.contains(discharge2));

		final Set<Slot> relatedSetFor2_loads = rsa.getRelatedSetFor(cargoAllocation2, true);
		// Expect other spot instance to be filtered out by the getRelatedSetFor(CargoAllocation) call
		Assert.assertFalse(relatedSetFor2_loads.contains(load1));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge1));
		Assert.assertTrue(relatedSetFor2_loads.contains(load2));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge2));

		final Set<Slot> relatedSetFor2_discharges = rsa.getRelatedSetFor(cargoAllocation2, false);
		Assert.assertFalse(relatedSetFor2_discharges.contains(load1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load2));
		Assert.assertTrue(relatedSetFor2_discharges.contains(discharge2));
	}

	@Test
	public void testRelatedBySpotDischarge() {

		final LoadSlot load1 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge1 = Mockito.mock(SpotDischargeSlot.class);
		final LoadSlot load2 = Mockito.mock(LoadSlot.class);
		final DischargeSlot discharge2 = Mockito.mock(SpotDischargeSlot.class);

		Mockito.when(load1.getName()).thenReturn("Load1");
		Mockito.when(discharge1.getName()).thenReturn("Discharge");
		Mockito.when(load2.getName()).thenReturn("Load2");
		Mockito.when(discharge2.getName()).thenReturn("Discharge");

		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();

		rsa.addRelatedSlot(load1, discharge1);
		rsa.addRelatedSlot(load2, discharge2);

		final CargoAllocation cargoAllocation1 = ScheduleFactory.eINSTANCE.createCargoAllocation();
		final CargoAllocation cargoAllocation2 = ScheduleFactory.eINSTANCE.createCargoAllocation();

		final SlotAllocation loadAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation1 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		final SlotAllocation loadAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();
		final SlotAllocation dischargeAllocation2 = ScheduleFactory.eINSTANCE.createSlotAllocation();

		cargoAllocation1.getSlotAllocations().add(loadAllocation1);
		cargoAllocation1.getSlotAllocations().add(dischargeAllocation1);

		cargoAllocation2.getSlotAllocations().add(loadAllocation2);
		cargoAllocation2.getSlotAllocations().add(dischargeAllocation2);

		loadAllocation1.setSlot(load1);
		dischargeAllocation1.setSlot(discharge1);

		loadAllocation2.setSlot(load2);
		dischargeAllocation2.setSlot(discharge2);

		final Set<Slot> relatedSetFor1_loads = rsa.getRelatedSetFor(cargoAllocation1, true);
		Assert.assertTrue(relatedSetFor1_loads.contains(load1));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_loads.contains(load2));
		Assert.assertFalse(relatedSetFor1_loads.contains(discharge2));

		final Set<Slot> relatedSetFor1_discharges = rsa.getRelatedSetFor(cargoAllocation1, false);
		Assert.assertFalse(relatedSetFor1_discharges.contains(load1));
		Assert.assertTrue(relatedSetFor1_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor1_discharges.contains(load2));
		// Expect other spot instance to be filtered out by the getRelatedSetFor(CargoAllocation) call
		Assert.assertFalse(relatedSetFor1_discharges.contains(discharge2));

		final Set<Slot> relatedSetFor2_loads = rsa.getRelatedSetFor(cargoAllocation2, true);
		Assert.assertFalse(relatedSetFor2_loads.contains(load1));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge1));
		Assert.assertTrue(relatedSetFor2_loads.contains(load2));
		Assert.assertFalse(relatedSetFor2_loads.contains(discharge2));

		final Set<Slot> relatedSetFor2_discharges = rsa.getRelatedSetFor(cargoAllocation2, false);
		Assert.assertFalse(relatedSetFor2_discharges.contains(load1));
		// Expect other spot instance to be filtered out by the getRelatedSetFor(CargoAllocation) call
		Assert.assertFalse(relatedSetFor2_discharges.contains(discharge1));
		Assert.assertFalse(relatedSetFor2_discharges.contains(load2));
		Assert.assertTrue(relatedSetFor2_discharges.contains(discharge2));
	}

	@Test
	public void testComplexCase1() {
		// Modelled on a real case where groups were incorrectly joined together by spot slots

		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();

		// Case A
		final LoadSlot loadA1 = mockSlot(LoadSlot.class, "load-1");
		final LoadSlot loadA2 = mockSlot(LoadSlot.class, "load-2");
		final LoadSlot loadA3 = mockSlot(SpotLoadSlot.class, "load-3");
		final LoadSlot loadA4 = mockSlot(LoadSlot.class, "load-4");
		final LoadSlot loadA5 = mockSlot(LoadSlot.class, "load-5");
		final LoadSlot loadA6 = mockSlot(SpotLoadSlot.class, "load-6");

		final DischargeSlot dischargeA1 = mockSlot(DischargeSlot.class, "discharge-1");
		final DischargeSlot dischargeA2 = mockSlot(DischargeSlot.class, "discharge-2");
		final DischargeSlot dischargeA3 = mockSlot(DischargeSlot.class, "discharge-3");
		final DischargeSlot dischargeA4 = mockSlot(DischargeSlot.class, "discharge-4");
		final DischargeSlot dischargeA5 = mockSlot(SpotDischargeSlot.class, "discharge-5");
		final DischargeSlot dischargeA6 = mockSlot(DischargeSlot.class, "discharge-6");

		final CargoAllocation cargoAllocationA1 = createCargoAllocation(rsa, loadA1, dischargeA1);
		final CargoAllocation cargoAllocationA2 = createCargoAllocation(rsa, loadA2, dischargeA2);
		final CargoAllocation cargoAllocationA3 = createCargoAllocation(rsa, loadA3, dischargeA3);
		final CargoAllocation cargoAllocationA4 = createCargoAllocation(rsa, loadA4, dischargeA4);
		final CargoAllocation cargoAllocationA5 = createCargoAllocation(rsa, loadA5, dischargeA5);
		final CargoAllocation cargoAllocationA6 = createCargoAllocation(rsa, loadA6, dischargeA6);

		// Case B
		final LoadSlot loadB1 = mockSlot(LoadSlot.class, "load-1");
		final LoadSlot loadB2 = mockSlot(LoadSlot.class, "load-2");
		final LoadSlot loadB3 = mockSlot(SpotLoadSlot.class, "load-3");

		final LoadSlot loadB4 = mockSlot(SpotLoadSlot.class, "load-6");

		final LoadSlot loadB5 = mockSlot(LoadSlot.class, "load-4");
		final LoadSlot loadB6 = mockSlot(LoadSlot.class, "load-5");
		final LoadSlot loadB7 = mockSlot(SpotLoadSlot.class, "load-7");

		final DischargeSlot dischargeB1 = mockSlot(SpotDischargeSlot.class, "discharge-5");
		final DischargeSlot dischargeB2 = mockSlot(DischargeSlot.class, "discharge-1");
		final DischargeSlot dischargeB3 = mockSlot(DischargeSlot.class, "discharge-2");

		final DischargeSlot dischargeB4 = mockSlot(DischargeSlot.class, "discharge-3");

		final DischargeSlot dischargeB5 = mockSlot(SpotDischargeSlot.class, "discharge-7");
		final DischargeSlot dischargeB6 = mockSlot(DischargeSlot.class, "discharge-4");
		final DischargeSlot dischargeB7 = mockSlot(DischargeSlot.class, "discharge-6");

		final CargoAllocation cargoAllocationB1 = createCargoAllocation(rsa, loadB1, dischargeB1);
		final CargoAllocation cargoAllocationB2 = createCargoAllocation(rsa, loadB2, dischargeB2);
		final CargoAllocation cargoAllocationB3 = createCargoAllocation(rsa, loadB3, dischargeB3);
		final CargoAllocation cargoAllocationB4 = createCargoAllocation(rsa, loadB4, dischargeB4);
		final CargoAllocation cargoAllocationB5 = createCargoAllocation(rsa, loadB5, dischargeB5);
		final CargoAllocation cargoAllocationB6 = createCargoAllocation(rsa, loadB6, dischargeB6);
		final CargoAllocation cargoAllocationB7 = createCargoAllocation(rsa, loadB7, dischargeB7);

		// Cycle group 1
		checkRelatedSlots(rsa, cargoAllocationA1, true, loadA1, loadA2, loadB1, loadB2, loadB3);
		checkRelatedSlots(rsa, cargoAllocationA2, true, loadA1, loadA2, loadB1, loadB2, loadB3);
		checkRelatedSlots(rsa, cargoAllocationA1, false, dischargeA1, dischargeA2, dischargeB1, dischargeB2, dischargeB3);
		checkRelatedSlots(rsa, cargoAllocationA2, false, dischargeA1, dischargeA2, dischargeB1, dischargeB2, dischargeB3);

		checkRelatedSlots(rsa, cargoAllocationB1, true, loadA1, loadA2, loadB1, loadB2, loadB3);
		checkRelatedSlots(rsa, cargoAllocationB2, true, loadA1, loadA2, loadB1, loadB2, loadB3);
		checkRelatedSlots(rsa, cargoAllocationB3, true, loadA1, loadA2, loadB1, loadB2, loadB3);
		checkRelatedSlots(rsa, cargoAllocationB1, false, dischargeA1, dischargeA2, dischargeB1, dischargeB2, dischargeB3);
		checkRelatedSlots(rsa, cargoAllocationB2, false, dischargeA1, dischargeA2, dischargeB1, dischargeB2, dischargeB3);
		checkRelatedSlots(rsa, cargoAllocationB3, false, dischargeA1, dischargeA2, dischargeB1, dischargeB2, dischargeB3);

		// Cycle Group 2
		checkRelatedSlots(rsa, cargoAllocationA3, true, loadA3, loadB4);
		checkRelatedSlots(rsa, cargoAllocationA3, false, dischargeA3, dischargeB4);

		checkRelatedSlots(rsa, cargoAllocationB4, true, loadA3, loadB4);
		checkRelatedSlots(rsa, cargoAllocationB4, false, dischargeA3, dischargeB4);

		// Cycle Group 3
		checkRelatedSlots(rsa, cargoAllocationA4, true, loadA4, loadA5, loadB5, loadB6);
		checkRelatedSlots(rsa, cargoAllocationA5, true, loadA4, loadA5, loadB5, loadB6);
		checkRelatedSlots(rsa, cargoAllocationA4, false, dischargeA4, dischargeA5, dischargeB5, dischargeB6);
		checkRelatedSlots(rsa, cargoAllocationA5, false, dischargeA4, dischargeA5, dischargeB5, dischargeB6);

		checkRelatedSlots(rsa, cargoAllocationB5, true, loadA4, loadA5, loadB5, loadB6);
		checkRelatedSlots(rsa, cargoAllocationB6, true, loadA4, loadA5, loadB5, loadB6);
		checkRelatedSlots(rsa, cargoAllocationB5, false, dischargeA4, dischargeA5, dischargeB5, dischargeB6);
		checkRelatedSlots(rsa, cargoAllocationB6, false, dischargeA4, dischargeA5, dischargeB5, dischargeB6);

		// Cycle Group 4
		checkRelatedSlots(rsa, cargoAllocationA6, true, loadA6, loadB7);
		checkRelatedSlots(rsa, cargoAllocationA6, false, dischargeA6, dischargeB7);
		checkRelatedSlots(rsa, cargoAllocationB7, true, loadA6, loadB7);
		checkRelatedSlots(rsa, cargoAllocationB7, false, dischargeA6, dischargeB7);

	}
	
	@Test
	public void testComplexCase2() {

		// A load-1 -> discharge-1
		//
		
		// B load-1 -> discharge-2 
		// B load-2 -> discharge-1 
		
		final RelatedSlotAllocations rsa = new RelatedSlotAllocations();
		
		// Case A
		final LoadSlot loadA1 = mockSlot(LoadSlot.class, "load-1");
		
		final DischargeSlot dischargeA1 = mockSlot(DischargeSlot.class, "discharge-1");
		
		final CargoAllocation cargoAllocationA1 = createCargoAllocation(rsa, loadA1, dischargeA1);
		
		// Case B
		final LoadSlot loadB1 = mockSlot(LoadSlot.class, "load-1");
		final LoadSlot loadB2 = mockSlot(SpotLoadSlot.class, "load-2");
		
		final DischargeSlot dischargeB1 = mockSlot(SpotDischargeSlot.class, "discharge-2");
		final DischargeSlot dischargeB2 = mockSlot(DischargeSlot.class, "discharge-1");
		
		final CargoAllocation cargoAllocationB1 = createCargoAllocation(rsa, loadB1, dischargeB1);
		final CargoAllocation cargoAllocationB2 = createCargoAllocation(rsa, loadB2, dischargeB2);
		
		// Cycle group 1
		checkRelatedSlots(rsa, cargoAllocationA1, true, loadA1, loadB1, loadB2);
		checkRelatedSlots(rsa, cargoAllocationA1, false, dischargeA1, dischargeB1, dischargeB2);
		
		checkRelatedSlots(rsa, cargoAllocationB1, true, loadA1, loadB1, loadB2);
		checkRelatedSlots(rsa, cargoAllocationB2, true, loadA1, loadB1, loadB2);
		checkRelatedSlots(rsa, cargoAllocationB1, false, dischargeA1, dischargeB1, dischargeB2);
		checkRelatedSlots(rsa, cargoAllocationB2, false, dischargeA1, dischargeB1, dischargeB2);
	}

	private <T extends Slot> T mockSlot(final Class<T> cls, final String name) {
		final T slot = Mockito.mock(cls);
		Mockito.when(slot.getName()).thenReturn(name);
		Mockito.when(slot.toString()).thenReturn(name);
		return slot;
	}

	private CargoAllocation createCargoAllocation(RelatedSlotAllocations rsa, final LoadSlot load, final DischargeSlot discharge) {
		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();

		final SlotAllocation loadAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
		loadAllocation.setSlotAllocationType(SlotAllocationType.PURCHASE);
		
		final SlotAllocation dischargeAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
		loadAllocation.setSlotAllocationType(SlotAllocationType.SALE);

		cargoAllocation.getSlotAllocations().add(loadAllocation);
		cargoAllocation.getSlotAllocations().add(dischargeAllocation);

		loadAllocation.setSlot(load);
		dischargeAllocation.setSlot(discharge);

		rsa.addRelatedSlot(load, discharge);

		return cargoAllocation;
	}

	private void checkRelatedSlots(final RelatedSlotAllocations rsa, final CargoAllocation cargoAllocation, final boolean buy, final Slot... slots) {
		final Set<Slot> relatedSet = rsa.getRelatedSetFor(cargoAllocation, buy);
		Assert.assertTrue(relatedSet.size() == slots.length);

		for (final Slot slot : slots) {
			Assert.assertTrue(relatedSet.contains(slot));
		}

	}
}
