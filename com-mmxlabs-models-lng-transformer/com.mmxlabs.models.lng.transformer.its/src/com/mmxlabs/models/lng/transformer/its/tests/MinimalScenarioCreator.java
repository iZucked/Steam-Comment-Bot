/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import org.eclipse.emf.common.util.EList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class MinimalScenarioCreator extends DefaultScenarioCreator {
	public final VesselClass vc;
	public final Vessel vessel;
	public VesselAvailability vesselAvailability;

	public final Port originPort;
	public final Port loadPort;
	public final Port dischargePort;

	public final Cargo cargo;

	/**
	 * Initialises a minimal complete scenario, creating: - contract and shipping legal entities - one vessel class and one vessel - one (default) route - one fixed-price sales contract and one
	 * fixed-price purchase contract - three ports (one origin port, one load port and one discharge port) - one cargo The vessel starts at the origin port, must travel to the load port, pick up the
	 * cargo, travel to the discharge port and discharge it. There is enough time at every stage to create some idling at the discharge port.
	 */
	public MinimalScenarioCreator() {
		super();

		final CommercialModel commercialModel = scenario.getCommercialModel();

		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract", purchasePrice);

		// create a vessel class with default name
		vc = fleetCreator.createDefaultVesselClass(null);
		// create a vessel in that class
		vessel = fleetCreator.createMultipleDefaultVessels(vc, 1, shippingEntity)[0];

		// need to create a default route
		addRoute(ScenarioTools.defaultRouteName);

		final double maxSpeed = vc.getMaxSpeed();

		// initialise the port creator with a convenient base distance multiplier
		if (vc.getMinSpeed() == maxSpeed) {
			portCreator.baseDistanceMultiplier = maxSpeed;
		}

		// create three ports (and their distances) all with default settings
		final Port[] ports = portCreator.createDefaultPorts(3);

		// these are start, load and discharge ports respectively
		originPort = ports[0];
		loadPort = ports[1];
		dischargePort = ports[2];

		cargo = createDefaultCargo(loadPort, dischargePort);

		setDefaultAvailability(originPort, originPort);

	}

	/**
	 * Creates a default cargo from the load port to the discharge port, with enough time to spare between the two ports, and with the start window leaving enough time to travel from the previous
	 * cargo (if any)
	 * 
	 * @param name
	 * @param loadPort
	 * @param dischargePort
	 * @param loadTime
	 * @return
	 */
	public Cargo createDefaultCargo(final Port loadPort, final Port dischargePort) {
		final Pair<Port, DateTime> appointment = getLastAppointment();
		final LocalDateTime loadTime;

		if (appointment == null) {
			loadTime = null;
		} else {
			final DateTime date = appointment.getSecond();
			final Port port = appointment.getFirst();
			loadTime = date.plusHours(getMarginHours(port, loadPort)).withZone(DateTimeZone.forID(loadPort.getTimeZone())).toLocalDateTime();
		}

		return cargoCreator.createDefaultCargo(null, loadPort, dischargePort, loadTime, getMarginHours(loadPort, dischargePort));
	}

	public int getMarginHours(final Port startPort, final Port endPort) {
		return 2 * getTravelTime(loadPort, dischargePort, null, (int) vc.getMaxSpeed());
	}

	public void setDefaultAvailability(final Port startPort, final Port endPort) {

		final Pair<Port, DateTime> firstAppointment = getFirstAppointment();
		final Pair<Port, DateTime> lastAppointment = getLastAppointment();

		final DateTime firstLoadDate = firstAppointment.getSecond();
		final DateTime lastDischargeDate = lastAppointment.getSecond();

		final DateTime startDate = firstLoadDate.minusHours(getMarginHours(startPort, firstAppointment.getFirst()));
		final DateTime endDate = lastDischargeDate.plusHours(getMarginHours(lastAppointment.getFirst(), endPort));

		final CargoModel cargoModel = scenario.getPortfolioModel().getCargoModel();
		this.vesselAvailability = fleetCreator.setAvailability(cargoModel, vessel, originPort, startDate.withZone(DateTimeZone.UTC).toLocalDateTime(), originPort, endDate.withZone(DateTimeZone.UTC)
				.toLocalDateTime());
	}

	public VesselEvent createDefaultMaintenanceEvent(final String name, final Port port, LocalDateTime startDate) {
		if (startDate == null) {
			final Pair<Port, DateTime> last = getLastAppointment();
			startDate = last.getSecond().plusHours(getMarginHours(last.getFirst(), port)).withZone(DateTimeZone.forID(port.getTimeZone())).toLocalDateTime();
		}

		final VesselEvent result = CargoFactory.eINSTANCE.createMaintenanceEvent();
		result.setName(name);
		result.setPort(port);

		result.setStartAfter(startDate);
		result.setStartBy(startDate);

		final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
		final CargoModel cargoModel = portfolioModel.getCargoModel();

		cargoModel.getVesselEvents().add(result);

		return result;
	}

	/*
	 * public Slot getFirstCargoSlot() { final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes(); if (cargoes.isEmpty()) { return null; } final EList<Slot> slots =
	 * cargoes.get(0).getSortedSlots(); return slots.get(0); }
	 * 
	 * public Slot getLastCargoSlot() { final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes(); if (cargoes.isEmpty()) { return null; } final EList<Slot> slots =
	 * cargoes.get(cargoes.size() - 1).getSortedSlots(); return slots.get(slots.size()-1); }
	 */

	public Pair<Port, DateTime> getLastAppointment() {
		DateTime date = null;
		Port port = null;

		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		for (final Cargo cargo : cargoes) {
			final EList<Slot> slots = cargo.getSortedSlots();
			final Slot slot = slots.get(slots.size() - 1);
			final DateTime slotDate = slot.getWindowEndWithSlotOrPortTime();

			if (date == null || date.isBefore(slotDate)) {
				date = slotDate;
				port = slot.getPort();
			}
		}

		final EList<VesselEvent> events = scenario.getPortfolioModel().getCargoModel().getVesselEvents();
		for (final VesselEvent event : events) {
			DateTime eventDate = event.getStartByAsDateTime();
			if (eventDate == null) {
				eventDate = event.getStartAfterAsDateTime();
			}
			if (eventDate != null) {
				eventDate = eventDate.plusDays(event.getDurationInDays());

				if (date == null || date.isBefore(eventDate)) {
					date = eventDate;
					port = event.getPort();
				}
			}
		}

		if (date == null && port == null) {
			return null;
		}

		return new Pair<Port, DateTime>(port, date);
	}

	public Pair<Port, DateTime> getFirstAppointment() {
		DateTime date = null;
		Port port = null;

		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		for (final Cargo cargo : cargoes) {
			final EList<Slot> slots = cargo.getSortedSlots();
			final Slot slot = slots.get(0);
			final DateTime slotDate = slot.getWindowStartWithSlotOrPortTime();

			if (date == null || date.isAfter(slotDate)) {
				date = slotDate;
				port = slot.getPort();
			}
		}

		final EList<VesselEvent> events = scenario.getPortfolioModel().getCargoModel().getVesselEvents();
		for (final VesselEvent event : events) {
			DateTime eventDate = event.getStartAfterAsDateTime();
			if (eventDate == null) {
				eventDate = event.getStartByAsDateTime();
			}
			if (eventDate != null) {
				if (date == null || date.isAfter(eventDate)) {
					date = eventDate;
					port = event.getPort();
				}
			}
		}

		if (date == null && port == null) {
			return null;
		}

		return new Pair<Port, DateTime>(port, date);
	}

	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldown(final double value) {
		final PricingModel pricingModel = scenario.getPricingModel();
		final PortModel portModel = scenario.getPortModel();

		final CommodityIndex cooldownIndex = pricingCreator.createDefaultCommodityIndex("cooldown", value);

		final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
		price.setExpression(cooldownIndex.getName());
		for (final Port port : portModel.getPorts()) {
			price.getPorts().add(port);
		}

		pricingModel.getCooldownPrices().add(price);
	}

	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldownLumpSum(String expression) {
		final PricingModel pricingModel = scenario.getPricingModel();
		final PortModel portModel = scenario.getPortModel();

		final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
		price.setExpression(expression);
		price.setLumpsum(true);
		for (final Port port : portModel.getPorts()) {
			price.getPorts().add(port);
		}

		pricingModel.getCooldownPrices().add(price);
	}

	public CharterOutEvent makeCharterOut(MinimalScenarioCreator msc, MMXRootObject scenario, Port startPort, Port endPort) {
		// change to default: add a charter out event 2-3 hrs after discharge window ends
		final DateTime endLoad = msc.cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();
		final LocalDateTime charterStartByDate = endLoad.plusHours(3).withZone(DateTimeZone.forID(startPort.getTimeZone())).toLocalDateTime();
		final LocalDateTime charterStartAfterDate = endLoad.plusHours(2).withZone(DateTimeZone.forID(startPort.getTimeZone())).toLocalDateTime();
		int charterOutRate = 24;
		CharterOutEvent event = msc.vesselEventCreator.createCharterOutEvent("CharterOut", startPort, endPort, charterStartByDate, charterStartAfterDate, charterOutRate);
		event.getHeelOptions().setVolumeAvailable(0);
		event.getHeelOptions().setCvValue(21);
		event.getHeelOptions().setPricePerMMBTU(1);

		return event;
	}
}
