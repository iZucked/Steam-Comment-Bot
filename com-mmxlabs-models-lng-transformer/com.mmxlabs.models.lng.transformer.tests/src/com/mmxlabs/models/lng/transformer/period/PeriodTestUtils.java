/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class PeriodTestUtils {

	public static Date createDate(final int year, final int month, final int day, final int hour) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	public static Date createDate(final String timezoneCode, final int year, final int month, final int day, final int hour) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(timezoneCode));
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	static Date createDate(final int year, final int month, final int day) {
		return PeriodTestUtils.createDate(year, month, day, 0);
	}
	static Date createDate(final String timezoneCode, final int year, final int month, final int day) {
		return PeriodTestUtils.createDate(timezoneCode, year, month, day, 0);
	}

	public static LNGScenarioModel createBasicScenario() {
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGPortfolioModel portfolioModel = LNGScenarioFactory.eINSTANCE.createLNGPortfolioModel();
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();

		scenarioModel.setFleetModel(fleetModel);
		scenarioModel.setPortfolioModel(portfolioModel);
		scenarioModel.setPortModel(portModel);
		portfolioModel.setCargoModel(cargoModel);
		portfolioModel.setScheduleModel(scheduleModel);

		return scenarioModel;
	}

	public static Schedule createSchedule(final LNGScenarioModel scenarioModel) {
		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		scenarioModel.getPortfolioModel().getScheduleModel().setSchedule(schedule);

		return schedule;
	}

	public static CargoAllocation createCargoAllocation(final LNGScenarioModel scenarioModel, final Cargo cargo, final SlotAllocation loadAllocation, final SlotAllocation dischargeAllocation,
			final Event... events) {
		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		scenarioModel.getPortfolioModel().getScheduleModel().getSchedule().getCargoAllocations().add(cargoAllocation);

		cargoAllocation.setInputCargo(cargo);
		cargoAllocation.getSlotAllocations().add(loadAllocation);
		cargoAllocation.getSlotAllocations().add(dischargeAllocation);

		for (final Event event : events) {
			cargoAllocation.getEvents().add(event);
		}

		return cargoAllocation;
	}

	public static SlotAllocation createSlotAllocation(final LNGScenarioModel scenarioModel, final Slot slot) {
		final SlotAllocation slotAllocation = ScheduleFactory.eINSTANCE.createSlotAllocation();
		scenarioModel.getPortfolioModel().getScheduleModel().getSchedule().getSlotAllocations().add(slotAllocation);

		slotAllocation.setSlot(slot);

		return slotAllocation;
	}

	public static SlotVisit createSlotVisit(final LNGScenarioModel scenarioModel, final SlotAllocation slotAllocation) {
		final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();

		final Slot slot = slotAllocation.getSlot();

		slotVisit.setSlotAllocation(slotAllocation);
		slotVisit.setStart(slot.getWindowStart());
		slotVisit.setEnd(slot.getWindowStart());
		slotVisit.setEnd(slot.getWindowStart());

		return slotVisit;
	}

	public static VesselEventVisit createVesselEventVisit(final LNGScenarioModel scenarioModel, final VesselEvent vesselEvent) {
		final VesselEventVisit vesselEventVisit = ScheduleFactory.eINSTANCE.createVesselEventVisit();

		vesselEventVisit.setVesselEvent(vesselEvent);

		vesselEventVisit.setStart(vesselEvent.getStartAfter());

		if (vesselEventVisit.getStart() != null) {
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.setTime(vesselEventVisit.getStart());
			cal.add(Calendar.DATE, vesselEvent.getDurationInDays());
			vesselEventVisit.setEnd(cal.getTime());
		}
		return vesselEventVisit;
	}

	public static Vessel createVessel(final LNGScenarioModel scenarioModel, final String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		scenarioModel.getFleetModel().getVessels().add(vessel);
		return vessel;
	}

	public static VesselAvailability createVesselAvailability(final LNGScenarioModel scenarioModel, final Vessel vessel) {
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		vesselAvailability.setStartHeel(heelOptions);

		scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().add(vesselAvailability);
		return vesselAvailability;
	}

	public static Port createPort(final LNGScenarioModel scenarioModel, final String name) {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);
		port.setTimeZone("Etc/UTC");
		scenarioModel.getPortModel().getPorts().add(port);
		return port;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final String name, final Slot... slots) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getCargoes().add(cargo);

		for (final Slot slot : slots) {
			cargo.getSlots().add(slot);
		}

		return cargo;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final String name, final Port loadPort, final Date loadDate, final Port dischargePort, final Date dischargeDate) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getCargoes().add(cargo);

		final LoadSlot loadSlot = createLoadSlot(scenarioModel, name + "-load");
		loadSlot.setWindowStart(loadDate);
		cargo.getSlots().add(loadSlot);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(loadSlot);

		final DischargeSlot dischargeSlot = createDischargeSlot(scenarioModel, name + "-discharge");
		dischargeSlot.setWindowStart(dischargeDate);
		cargo.getSlots().add(dischargeSlot);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(dischargeSlot);

		return cargo;
	}

	public static CharterOutEvent createCharterOutEvent(final LNGScenarioModel scenarioModel, final String name) {
		final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
		event.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().add(event);

		return event;
	}

	public static VesselEvent createCharterOutEvent(final LNGScenarioModel scenarioModel, final String name, final Port port, final Date date, final int duration) {
		final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
		event.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().add(event);

		event.setStartBy(date);
		event.setStartAfter(date);
		event.setPort(port);
		event.setDurationInDays(duration);

		return event;
	}

	public static LoadSlot createLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static SpotLoadSlot createSpotLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static DischargeSlot createDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static SpotDischargeSlot createSpotDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static PortVisit createPortVisit(final Port port, final Date date) {

		final PortVisit portVisit = ScheduleFactory.eINSTANCE.createPortVisit();
		portVisit.setPort(port);
		portVisit.setStart(date);
		portVisit.setEnd(date);

		return portVisit;
	}

	public static Sequence createSequence(final LNGScenarioModel scenarioModel, final Event... events) {
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		scenarioModel.getPortfolioModel().getScheduleModel().getSchedule().getSequences().add(sequence);

		Event prevEvent = null;
		for (final Event event : events) {
			if (prevEvent != null) {
				prevEvent.setNextEvent(event);
			}
			sequence.getEvents().add(event);
			prevEvent = event;
		}

		return sequence;
	}

	public static StartEvent createStartEvent() {
		return ScheduleFactory.eINSTANCE.createStartEvent();
	}

	public static EndEvent createEndEvent() {
		return ScheduleFactory.eINSTANCE.createEndEvent();
	}

	public static Journey createJourney() {
		return ScheduleFactory.eINSTANCE.createJourney();
	}

	public static Idle createIdle() {
		return ScheduleFactory.eINSTANCE.createIdle();
	}

	public static Cooldown createCooldown() {
		return ScheduleFactory.eINSTANCE.createCooldown();
	}

	public static CollectedAssignment createCollectedAssignment(final VesselAvailability vesselAvailability, final AssignableElement... elements) {

		return new CollectedAssignment(Arrays.asList(elements), vesselAvailability);
	}

	public static EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);

		// Delete commands need a resource set on the editing domain
		final Resource r = new XMIResourceImpl();
		r.getContents().add(scenarioModel);
		editingDomain.getResourceSet().getResources().add(r);
		return editingDomain;
	}

}
