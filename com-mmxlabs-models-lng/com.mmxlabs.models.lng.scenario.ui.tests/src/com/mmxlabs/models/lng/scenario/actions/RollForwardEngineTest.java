/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.provider.CargoItemProviderAdapterFactory;
import com.mmxlabs.models.lng.fleet.provider.FleetItemProviderAdapterFactory;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.scenario.actions.impl.FixSlotWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FixVesselEventWindowChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.FreezeVesselEventChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveCargoChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveSlotChange;
import com.mmxlabs.models.lng.scenario.actions.impl.RemoveVesselEventChange;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RollForwardEngineTest {

	@Test
	public void examineSlotsAndCargoesTest() {

		final Set<Slot<?>> slotsExpectedToBeRemoved = new HashSet<>();
		final Set<Slot<?>> slotsExpectedToBeFrozen = new HashSet<>();
		final Set<Cargo> cargoesExpectedToBeRemoved = new HashSet<>();
		final Set<Cargo> cargoesExpectedToBeFrozen = new HashSet<>();

		LocalDate freezeCalendar = LocalDate.of(2013, 9, 1);
		LocalDate removeCalendar = LocalDate.of(2013, 8, 1);

		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		// Generate the Data model
		{

			// Load Slot before remove date
			{
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setName("LoadSlot1");
				slot.setWindowStart(removeCalendar.minusDays(2));

				slot.setDuration(24);

				slotsExpectedToBeRemoved.add(slot);

				cargoModel.getLoadSlots().add(slot);
			}

			// Load Slot partially over remove date
			{
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setName("LoadSlot2");
				slot.setWindowStart(removeCalendar.minusDays(1));

				slot.setDuration(48);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getLoadSlots().add(slot);
			}

			// Load Slot before freeze date
			{
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setName("LoadSlot3");
				slot.setWindowStart(freezeCalendar.minusDays(2));

				slot.setDuration(24);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getLoadSlots().add(slot);
			}

			// Load Slot partially over freeze date
			{
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setName("LoadSlot4");
				slot.setWindowStart(freezeCalendar.minusDays(1));

				slot.setDuration(48);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getLoadSlots().add(slot);
			}

			// Load Slot clear of both dates
			{
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setName("LoadSlot5");
				slot.setWindowStart(freezeCalendar.plusDays(1));

				slot.setDuration(48);

				cargoModel.getLoadSlots().add(slot);
			}

			// Discharge Slot before remove date
			{
				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setName("DischargeSlot1");
				slot.setWindowStart(removeCalendar.minusDays(2));

				slot.setDuration(24);

				slotsExpectedToBeRemoved.add(slot);

				cargoModel.getDischargeSlots().add(slot);
			}

			// Discharge Slot partially over remove date
			{
				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setName("DischargeSlot2");
				slot.setWindowStart(removeCalendar.minusDays(1));

				slot.setDuration(48);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getDischargeSlots().add(slot);
			}

			// Discharge Slot before freeze date
			{
				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setName("DischargeSlot3");
				slot.setWindowStart(freezeCalendar.minusDays(2));

				slot.setDuration(24);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getDischargeSlots().add(slot);
			}

			// Discharge Slot partially over freeze date
			{
				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setName("DischargeSlot4");
				slot.setWindowStart(freezeCalendar.minusDays(1));

				slot.setDuration(48);

				slotsExpectedToBeFrozen.add(slot);

				cargoModel.getDischargeSlots().add(slot);
			}

			// Discharge Slot clear of both dates
			{
				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setName("DischargeSlot5");
				slot.setWindowStart(freezeCalendar.plusDays(1));

				slot.setDuration(48);

				cargoModel.getDischargeSlots().add(slot);
			}

			// Cargo before remove date
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot1");

				loadSlot.setWindowStart(removeCalendar.minusDays(4));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);
				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot1");

				dischargeSlot.setWindowStart(removeCalendar.minusDays(2));

				dischargeSlot.setDuration(24);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeRemoved.add(cargo);
			}

			// Cargo one slot before, one slot after remove date
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot2");

				loadSlot.setWindowStart(removeCalendar.minusDays(2));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot2");

				dischargeSlot.setWindowStart(removeCalendar.plusDays(2));

				dischargeSlot.setDuration(24);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before remove date, one slot partially over remove date
			{
				LocalDate dt = removeCalendar;

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot3");

				loadSlot.setWindowStart(removeCalendar.minusDays(2));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot3");
				dischargeSlot.setWindowStart(removeCalendar.minusDays(1));

				dischargeSlot.setDuration(48);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo before freeze date
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot4");

				loadSlot.setWindowStart(freezeCalendar.minusDays(4));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot4");
				dischargeSlot.setWindowStart(freezeCalendar.minusDays(2));

				dischargeSlot.setDuration(24);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before, one slot after freeze date
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot5");

				loadSlot.setWindowStart(freezeCalendar.minusDays(2));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot5");
				dischargeSlot.setWindowStart(freezeCalendar.plusDays(2));

				dischargeSlot.setDuration(24);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo one slot before freeze date, one slot partially over freeze date
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot6");

				loadSlot.setWindowStart(removeCalendar.minusDays(2));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot6");
				dischargeSlot.setWindowStart(removeCalendar.minusDays(1));

				dischargeSlot.setDuration(48);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);

				cargoesExpectedToBeFrozen.add(cargo);
			}

			// Cargo clear of both dates
			{

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				loadSlot.setName("CargoLoadSlot7");

				loadSlot.setWindowStart(freezeCalendar.plusDays(2));

				loadSlot.setDuration(24);

				cargoModel.getLoadSlots().add(loadSlot);

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				dischargeSlot.setName("CargoDischargeSlot7");
				dischargeSlot.setWindowStart(freezeCalendar.plusDays(4));

				dischargeSlot.setDuration(24);

				cargoModel.getDischargeSlots().add(dischargeSlot);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);

				cargoModel.getCargoes().add(cargo);
			}

		}

		final RollForwardEngine engine = new RollForwardEngine();

		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();
		final List<Cargo> cargoesToFreeze = new LinkedList<>();

		engine.examineSlotsAndCargoes(freezeCalendar, removeCalendar, cargoModel, slotsToRemove, slotsToFreeze, cargoesToFreeze, cargoesToRemove);

		// Check lists contain expected items
		Assert.assertEquals(slotsExpectedToBeRemoved, new HashSet<>(slotsToRemove));
		Assert.assertEquals(slotsExpectedToBeFrozen, new HashSet<>(slotsToFreeze));
		Assert.assertEquals(cargoesExpectedToBeRemoved, new HashSet<>(cargoesToRemove));
		Assert.assertEquals(cargoesExpectedToBeFrozen, new HashSet<>(cargoesToFreeze));
	}

	@Test
	public void examineVesselEventsTest() {

		final Set<VesselEvent> eventsExpectedToBeRemoved = new HashSet<>();
		final Set<VesselEvent> eventsExpectedToBeFrozen = new HashSet<>();

		LocalDate freezeCalendar = LocalDate.of(2013, 9, 1);
		LocalDate removeCalendar = LocalDate.of(2013, 8, 1);

		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		// Generate the Data model
		{
			// Event fully before remove and freeze date
			{
				final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
				event.setName("Event1");

				final LocalDateTime dt = removeCalendar.atStartOfDay();
				event.setStartAfter(dt.minusDays(2));
				event.setStartBy(dt.plusDays(1));

				event.setDurationInDays(0);

				eventsExpectedToBeRemoved.add(event);

				cargoModel.getVesselEvents().add(event);
			}

			// Event fully before freeze, but not remove date
			{
				final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
				event.setName("Event2");

				final LocalDateTime dt = freezeCalendar.atStartOfDay();
				event.setStartAfter(dt.minusDays(2));
				event.setStartBy(dt.plusDays(1));

				event.setDurationInDays(2);

				eventsExpectedToBeFrozen.add(event);

				cargoModel.getVesselEvents().add(event);
			}

			// Event partially over remove date
			{
				final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
				event.setName("Event3");

				final LocalDateTime dt = removeCalendar.atStartOfDay();
				event.setStartAfter(dt.minusDays(1));
				event.setStartBy(dt.plusDays(1));

				event.setDurationInDays(1);

				eventsExpectedToBeFrozen.add(event);

				cargoModel.getVesselEvents().add(event);
			}

			// Event partially over freeze date
			{
				final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
				event.setName("Event4");

				final LocalDateTime dt = freezeCalendar.atStartOfDay();
				event.setStartAfter(dt.minusDays(1));
				event.setStartBy(dt.plusDays(1));

				event.setDurationInDays(2);

				eventsExpectedToBeFrozen.add(event);

				cargoModel.getVesselEvents().add(event);
			}

			// Event clear of both dates
			{
				final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
				event.setName("Event5");
				final LocalDateTime dt = freezeCalendar.atStartOfDay();
				event.setStartAfter(dt.plusDays(2));
				event.setStartBy(dt.plusDays(3));

				event.setDurationInDays(1);

				cargoModel.getVesselEvents().add(event);
			}

		}

		final RollForwardEngine engine = new RollForwardEngine();

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();

		engine.examineVesselEvents(freezeCalendar, removeCalendar, cargoModel, eventsToRemove, eventsToFreeze);

		// Check lists contain expected items
		Assert.assertEquals(eventsExpectedToBeRemoved, new HashSet<>(eventsToRemove));
		Assert.assertEquals(eventsExpectedToBeFrozen, new HashSet<>(eventsToFreeze));
	}

	private enum CargoAndEventChangesType {
		EVENT_TO_REMOVE, EVENT_TO_FREEZE, SLOT_TO_REMOVE, SLOT_TO_FREEZE, CARGO_TO_REMOVE, CARGO_TO_FREEZE

	}

	@Test
	public void generateCargoAndEventChangesTest_SlotToRemove() {

		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.SLOT_TO_REMOVE, slot, null);

		Assert.assertEquals(1, changes.size());
		final IRollForwardChange change = changes.get(0);

		Assert.assertTrue(change instanceof RemoveSlotChange);

		final RemoveSlotChange removeSlotChange = (RemoveSlotChange) change;
		Assert.assertSame(slot, removeSlotChange.getChangedObject());
	}

	@Test
	public void generateCargoAndEventChangesTest_LoadSlotToFreeze() {

		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.SLOT_TO_FREEZE, loadSlot, null);

		// We do not freeze slots yet
		Assert.assertEquals(0, changes.size());
	}

	@Test
	public void generateCargoAndEventChangesTest_EventToRemove() {

		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.EVENT_TO_REMOVE, event, null);

		Assert.assertEquals(1, changes.size());
		final IRollForwardChange change = changes.get(0);

		Assert.assertTrue(change instanceof RemoveVesselEventChange);

		final RemoveVesselEventChange removeSlotChange = (RemoveVesselEventChange) change;
		Assert.assertSame(event, removeSlotChange.getChangedObject());
	}

	@Test
	public void generateCargoAndEventChangesTest_EventToFreeze() {

		final VesselEvent event = CargoFactory.eINSTANCE.createDryDockEvent();

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();

		event.setVesselAssignmentType(vessel);

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		event.setPort(p);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		final VesselEventVisit vesselEventVisit = ScheduleFactory.eINSTANCE.createVesselEventVisit();
		vesselEventVisit.setVesselEvent(event);

		sequence.getEvents().add(vesselEventVisit);

		LocalDate dt = LocalDate.of(2014, 9, 4);

		// Create 10 day window, with schedule start halfway
		event.setStartAfter(dt.atTime(0, 0));
		dt = dt.plusDays(5);
		vesselEventVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(1);
		vesselEventVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		dt = dt.plusDays(4);
		event.setStartAfter(dt.atTime(0, 0));

		event.setDurationInDays(1);

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.EVENT_TO_FREEZE, event, schedule);

		Assert.assertEquals(2, changes.size());

		FreezeVesselEventChange freezeChange = null;
		FixVesselEventWindowChange windowChange = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeVesselEventChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeVesselEventChange) change;
			} else if (change instanceof FixVesselEventWindowChange) {
				Assert.assertNull("Only one window change expected", windowChange);
				windowChange = (FixVesselEventWindowChange) change;
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	public void generateCargoAndEventChangesTest_CargoToFreeze() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

		Port p = PortFactory.eINSTANCE.createPort();
		Location l = PortFactory.eINSTANCE.createLocation();
		l.setTimeZone("UTC");
		p.setLocation(l);
		loadSlot.setPort(p);
		dischargeSlot.setPort(p);

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();
		cargo.setVesselAssignmentType(vessel);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		LocalDate dt = LocalDate.of(2013, 9, 4);

		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		{
			schedule.getCargoAllocations().add(cargoAllocation);
		}
		{

			final Slot slot = loadSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}
		dt = dt.plusDays(10);
		{

			final Slot slot = dischargeSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.CARGO_TO_FREEZE, cargo, schedule);

		Assert.assertEquals(3, changes.size());

		FreezeCargoChange freezeChange = null;
		FixSlotWindowChange windowChange1 = null;
		FixSlotWindowChange windowChange2 = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof FreezeCargoChange) {
				Assert.assertNull("Only one freeze change expected", freezeChange);
				freezeChange = (FreezeCargoChange) change;
			} else if (change instanceof FixSlotWindowChange) {
				if (windowChange1 == null) {
					windowChange1 = (FixSlotWindowChange) change;
				} else if (windowChange2 == null) {
					windowChange2 = (FixSlotWindowChange) change;
				} else {
					Assert.fail("Only two window changes expected");
				}
			} else {
				Assert.fail("Unexpected change");
			}
		}
	}

	@Test
	public void generateCargoAndEventChangesTest_CargoToRemove() {

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

		cargo.getSlots().add(loadSlot);
		cargo.getSlots().add(dischargeSlot);

		final VesselAvailability vessel = CargoFactory.eINSTANCE.createVesselAvailability();

		cargo.setVesselAssignmentType(vessel);

		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		schedule.getSequences().add(sequence);

		LocalDate dt = LocalDate.of(2014, 9, 4);

		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		{
			schedule.getCargoAllocations().add(cargoAllocation);
		}
		{

			final Slot slot = loadSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}
		dt = dt.plusDays(10);
		{

			final Slot slot = dischargeSlot;

			final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
			slotAllocation.setSlot(slot);
			cargoAllocation.getSlotAllocations().add(slotAllocation);

			final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();
			slotVisit.setSlotAllocation(slotAllocation);

			schedule.getSlotAllocations().add(slotAllocation);
			sequence.getEvents().add(slotVisit);

			slot.setWindowStart(dt);
			slot.setWindowSize(48);

			dt = dt.plusDays(1);
			slotVisit.setStart(dt.atStartOfDay(ZoneId.of("UTC")));
			slotVisit.setEnd(dt.atStartOfDay(ZoneId.of("UTC")));
		}

		final List<IRollForwardChange> changes = generateCargoAndEventChangesTest(CargoAndEventChangesType.CARGO_TO_REMOVE, cargo, schedule);

		Assert.assertEquals(3, changes.size());

		RemoveSlotChange removeChange1 = null;
		RemoveSlotChange removeChange2 = null;
		RemoveCargoChange removeCargo = null;
		for (final IRollForwardChange change : changes) {
			if (change instanceof RemoveCargoChange) {
				Assert.assertNull("Only one remove cargo change expected", removeCargo);
				removeCargo = (RemoveCargoChange) change;
			} else if (change instanceof RemoveSlotChange) {
				if (removeChange1 == null) {
					removeChange1 = (RemoveSlotChange) change;
				} else if (removeChange2 == null) {
					removeChange2 = (RemoveSlotChange) change;
				} else {
					Assert.fail("Only two remove slot changes expected");
				}
			} else {
				Assert.fail("Unexpected change");
			}
		}
		Assert.assertNotNull(removeCargo);
		Assert.assertNotNull(removeChange1);
		Assert.assertNotNull(removeChange2);
	}

	private @NonNull List<IRollForwardChange> generateCargoAndEventChangesTest(@NonNull final CargoAndEventChangesType type, @NonNull final Object obj, @Nullable final Schedule schedule) {

		final List<VesselEvent> eventsToRemove = new LinkedList<>();
		final List<VesselEvent> eventsToFreeze = new LinkedList<>();
		final List<Slot<?>> slotsToRemove = new LinkedList<>();
		final List<Slot<?>> slotsToFreeze = new LinkedList<>();
		final List<Cargo> cargoesToRemove = new LinkedList<>();
		final List<Cargo> cargoesToFreeze = new LinkedList<>();

		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		scenarioModel.setCargoModel(cargoModel);
		scenarioModel.setScheduleModel(scheduleModel);

		if (schedule != null) {
			scheduleModel.setSchedule(schedule);
		}
		switch (type) {
		case CARGO_TO_FREEZE:
			cargoesToFreeze.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case CARGO_TO_REMOVE:
			cargoesToRemove.add((Cargo) obj);
			cargoModel.getCargoes().add((Cargo) obj);
			for (final Slot<?> slot : ((Cargo) obj).getSlots()) {
				addSlotToModel(cargoModel, slot);
			}
			break;
		case EVENT_TO_FREEZE:
			eventsToFreeze.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case EVENT_TO_REMOVE:
			eventsToRemove.add((VesselEvent) obj);
			cargoModel.getVesselEvents().add((VesselEvent) obj);
			break;
		case SLOT_TO_FREEZE:
			slotsToFreeze.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		case SLOT_TO_REMOVE:
			slotsToRemove.add((Slot) obj);
			addSlotToModel(cargoModel, (Slot) obj);
			break;
		}

		final List<IRollForwardChange> changes = new LinkedList<>();
		final RollForwardEngine engine = new RollForwardEngine();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();
		adapterFactory.addAdapterFactory(new CargoItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new FleetItemProviderAdapterFactory());

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());

		engine.generateCargoAndEventChanges(domain, scenarioModel, changes, cargoesToRemove, cargoesToFreeze, eventsToRemove, eventsToFreeze, slotsToRemove, slotsToFreeze);

		return changes;
	}

	private void addSlotToModel(final CargoModel cargoModel, final Slot<?> slot) {

		if (slot instanceof LoadSlot) {
			cargoModel.getLoadSlots().add((LoadSlot) slot);
		} else {
			cargoModel.getDischargeSlots().add((DischargeSlot) slot);
		}
	}
}
