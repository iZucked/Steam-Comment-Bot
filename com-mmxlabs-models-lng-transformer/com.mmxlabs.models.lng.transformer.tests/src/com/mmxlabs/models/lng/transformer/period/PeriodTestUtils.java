/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

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
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
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
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

public class PeriodTestUtils {

	public static YearMonth createYearMonth(final int year, final int month) {
		return YearMonth.of(year, 1 + month);
	}

	public static ZonedDateTime createDate(final int year, final int month, final int day, final int hour) {
		return ZonedDateTime.of(year, 1 + month, day, hour, 0, 0, 0, ZoneId.of("UTC"));
	}

	public static ZonedDateTime createDate(final String timezoneCode, final int year, final int month, final int day, final int hour) {
		return ZonedDateTime.of(year, 1 + month, day, hour, 0, 0, 0, ZoneId.of(timezoneCode));
	}

	static ZonedDateTime createDate(final int year, final int month, final int day) {
		return ZonedDateTime.of(year, 1 + month, day, 0, 0, 0, 0, ZoneId.of("UTC"));
	}

	static LocalDate createLocalDate(final int year, final int month, final int day) {
		return LocalDate.of(year, 1 + month, day);
	}

	static LocalDateTime createLocalDateTime(final int year, final int month, final int day, int hourOfDay) {
		return LocalDateTime.of(year, 1 + month, day, hourOfDay, 0);
	}

	static ZonedDateTime createDate(final String timezoneCode, final int year, final int month, final int day) {
		return ZonedDateTime.of(year, 1 + month, day, 0, 0, 0, 0, ZoneId.of(timezoneCode));
	}

	public static LNGScenarioModel createBasicScenario() {
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGReferenceModel referenceModel = LNGScenarioFactory.eINSTANCE.createLNGReferenceModel();
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final ScheduleModel scheduleModel = ScheduleFactory.eINSTANCE.createScheduleModel();
		scenarioModel.setReferenceModel(referenceModel);

		referenceModel.setFleetModel(fleetModel);
		referenceModel.setPortModel(portModel);
		scenarioModel.setCargoModel(cargoModel);
		scenarioModel.setScheduleModel(scheduleModel);

		return scenarioModel;
	}

	public static Schedule createSchedule(final LNGScenarioModel scenarioModel) {
		final Schedule schedule = ScheduleFactory.eINSTANCE.createSchedule();
		scenarioModel.getScheduleModel().setSchedule(schedule);

		return schedule;
	}

	public static CargoAllocation createCargoAllocation(final LNGScenarioModel scenarioModel, final Cargo cargo, final SlotAllocation loadAllocation, final SlotAllocation dischargeAllocation,
			final Event... events) {
		final CargoAllocation cargoAllocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
		scenarioModel.getScheduleModel().getSchedule().getCargoAllocations().add(cargoAllocation);

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
		scenarioModel.getScheduleModel().getSchedule().getSlotAllocations().add(slotAllocation);

		slotAllocation.setSlot(slot);

		return slotAllocation;
	}

	public static SlotVisit createSlotVisit(final LNGScenarioModel scenarioModel, final SlotAllocation slotAllocation) {
		final SlotVisit slotVisit = ScheduleFactory.eINSTANCE.createSlotVisit();

		final Slot slot = slotAllocation.getSlot();

		slotVisit.setSlotAllocation(slotAllocation);
		slotVisit.setStart(slot.getWindowStartWithSlotOrPortTime());
		slotVisit.setEnd(slot.getWindowStartWithSlotOrPortTime());

		return slotVisit;
	}

	public static VesselEventVisit createVesselEventVisit(final LNGScenarioModel scenarioModel, final VesselEvent vesselEvent) {
		final VesselEventVisit vesselEventVisit = ScheduleFactory.eINSTANCE.createVesselEventVisit();

		vesselEventVisit.setVesselEvent(vesselEvent);

		vesselEventVisit.setStart(vesselEvent.getStartAfterAsDateTime());

		if (vesselEventVisit.getStart() != null) {
			final ZonedDateTime date = vesselEventVisit.getStart().plusDays(vesselEvent.getDurationInDays());
			vesselEventVisit.setEnd(date);
		}
		return vesselEventVisit;
	}

	public static Vessel createVessel(final LNGScenarioModel scenarioModel, final String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		scenarioModel.getReferenceModel().getFleetModel().getVessels().add(vessel);
		return vessel;
	}

	public static VesselAvailability createVesselAvailability(final LNGScenarioModel scenarioModel, final Vessel vessel) {
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		vesselAvailability.setStartHeel(heelOptions);

		scenarioModel.getCargoModel().getVesselAvailabilities().add(vesselAvailability);
		return vesselAvailability;
	}

	public static Port createPort(final LNGScenarioModel scenarioModel, final String name) {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);
		port.setTimeZone("Etc/UTC");
		scenarioModel.getReferenceModel().getPortModel().getPorts().add(port);
		return port;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final Slot... slots) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		scenarioModel.getCargoModel().getCargoes().add(cargo);

		for (final Slot slot : slots) {
			cargo.getSlots().add(slot);
		}

		return cargo;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final String name, final Port loadPort, final LocalDate loadDate, final Port dischargePort, final LocalDate dischargeDate) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		scenarioModel.getCargoModel().getCargoes().add(cargo);

		final LoadSlot loadSlot = createLoadSlot(scenarioModel, name + "-load");
		loadSlot.setWindowStart(loadDate);
		cargo.getSlots().add(loadSlot);
		scenarioModel.getCargoModel().getLoadSlots().add(loadSlot);

		final DischargeSlot dischargeSlot = createDischargeSlot(scenarioModel, name + "-discharge");
		dischargeSlot.setWindowStart(dischargeDate);
		cargo.getSlots().add(dischargeSlot);
		scenarioModel.getCargoModel().getDischargeSlots().add(dischargeSlot);

		return cargo;
	}

	public static CharterOutEvent createCharterOutEvent(final LNGScenarioModel scenarioModel, final String name) {
		final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
		event.setName(name);
		scenarioModel.getCargoModel().getVesselEvents().add(event);

		return event;
	}

	public static VesselEvent createCharterOutEvent(final LNGScenarioModel scenarioModel, final String name, final Port port, final LocalDateTime date, final int duration) {
		final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
		event.setName(name);
		scenarioModel.getCargoModel().getVesselEvents().add(event);

		event.setStartBy(date);
		event.setStartAfter(date);
		event.setPort(port);
		event.setDurationInDays(duration);

		return event;
	}

	public static LoadSlot createLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setName(name);
		scenarioModel.getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static SpotLoadSlot createSpotLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		slot.setName(name);
		scenarioModel.getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static DischargeSlot createDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		slot.setName(name);
		scenarioModel.getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static SpotDischargeSlot createSpotDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		slot.setName(name);
		scenarioModel.getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static SpotMarketsModel createSpotMarkets(final LNGScenarioModel scenarioModel, final String name) {
		final SpotMarketsModel marketsModel = SpotMarketsFactory.eINSTANCE.createSpotMarketsModel();
		final DESSalesMarket desSalesMarket = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
		final DESPurchaseMarket desPurchasesMarket = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
		final FOBSalesMarket fobSalesMarket = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
		final FOBPurchasesMarket fobPurchaseMarket = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();

		for (final SpotMarket market : new SpotMarket[] { desSalesMarket, desPurchasesMarket, fobPurchaseMarket, fobSalesMarket }) {
			setUpSpotMarket(market);
		}
		final SpotMarketGroup dp = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
		dp.getMarkets().add(desPurchasesMarket);
		final SpotMarketGroup ds = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
		ds.getMarkets().add(desSalesMarket);
		final SpotMarketGroup fp = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
		fp.getMarkets().add(fobPurchaseMarket);
		final SpotMarketGroup fs = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
		fs.getMarkets().add(fobSalesMarket);

		marketsModel.setDesPurchaseSpotMarket(dp);
		marketsModel.setDesSalesSpotMarket(ds);
		marketsModel.setFobPurchasesSpotMarket(fp);
		marketsModel.setFobSalesSpotMarket(fs);
		return marketsModel;
	}

	private static void setUpSpotMarket(final SpotMarket market) {
		market.setEnabled(true);
		final SpotAvailability availability = SpotMarketsFactory.eINSTANCE.createSpotAvailability();
		availability.setConstant(2);
		final DataIndex<Integer> curve = PricingFactory.eINSTANCE.createDataIndex();
		for (int i = 4; i < 6; i++) {
			final YearMonth date = DateAndCurveHelper.createYearMonth(2015, i);
			final IndexPoint<Integer> point = PricingFactory.eINSTANCE.createIndexPoint();
			point.setDate(date);
			point.setValue(5);
			curve.getPoints().add(point);
		}
		availability.setCurve(curve);
		market.setAvailability(availability);
	}

	public static PortVisit createPortVisit(final Port port, final ZonedDateTime date) {

		final PortVisit portVisit = ScheduleFactory.eINSTANCE.createPortVisit();
		portVisit.setPort(port);
		portVisit.setStart(date);
		portVisit.setEnd(date);

		return portVisit;
	}

	public static Sequence createSequence(final LNGScenarioModel scenarioModel, final Event... events) {
		final Sequence sequence = ScheduleFactory.eINSTANCE.createSequence();
		scenarioModel.getScheduleModel().getSchedule().getSequences().add(sequence);

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

		return new CollectedAssignment(Arrays.asList(elements), vesselAvailability, null, null);
	}

	@NonNull
	public static EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {
		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		// Delete commands need a resource set on the editing domain
		final Resource r = new XMIResourceImpl();
		r.getContents().add(scenarioModel);
		editingDomain.getResourceSet().getResources().add(r);
		return editingDomain;
	}

}
