/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.Date;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

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
	 * fixed-price purchase contract - three ports (one origin port, one load port and one discharge port) - one cargo The vessel starts at the origin port, must travel to the load port, pick up
	 * the cargo, travel to the discharge port and discharge it. There is enough time at every stage to create some idling at the discharge port.
	 */
	public MinimalScenarioCreator() {
		super();
		
		final CommercialModel commercialModel = scenario.getCommercialModel();

		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");
		commercialModel.setShippingEntity(shippingEntity);

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract", purchasePrice);

		// create a vessel class with default name
		vc = fleetCreator.createDefaultVesselClass(null);
		// create a vessel in that class
		vessel = fleetCreator.createMultipleDefaultVessels(vc, 1)[0];

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
	 * Creates a default cargo from the load port to the discharge port, with enough time to spare between the two ports,
	 * and with the start window leaving enough time to travel from the previous cargo (if any) 
	 * 
	 * @param name
	 * @param loadPort
	 * @param dischargePort
	 * @param loadTime
	 * @return
	 */
	public Cargo createDefaultCargo(final Port loadPort, final Port dischargePort) {
		final Pair<Port, Date> appointment = getLastAppointment();
		final Date loadTime;

		if (appointment == null) {
			loadTime = null;
		}
		else {
			Date date = appointment.getSecond();
			Port port = appointment.getFirst();
			loadTime = addHours(date, getMarginHours(port, loadPort));
		}
				
		return cargoCreator.createDefaultCargo(null, loadPort, dischargePort, loadTime, getMarginHours(loadPort, dischargePort));
	}
	
	public int getMarginHours(Port startPort, Port endPort) {
		return 2 * getTravelTime(loadPort, dischargePort, null, (int) vc.getMaxSpeed());
	}	
	
	public void setDefaultAvailability(Port startPort, Port endPort) {
				
		Pair<Port, Date> firstAppointment = getFirstAppointment();
		Pair<Port, Date> lastAppointment = getLastAppointment();

		final Date firstLoadDate = firstAppointment.getSecond();
		final Date lastDischargeDate = lastAppointment.getSecond();
		
		final Date startDate = addHours(firstLoadDate, -getMarginHours(startPort, firstAppointment.getFirst()));
		final Date endDate = addHours(lastDischargeDate, getMarginHours(lastAppointment.getFirst(), endPort));
		
		final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();
		this.vesselAvailability = fleetCreator.setAvailability(scenarioFleetModel, vessel, originPort, startDate, originPort, endDate);
	}

	public VesselEvent createDefaultMaintenanceEvent(final String name, final Port port, Date startDate) {
		if (startDate == null) {
			final Pair<Port, Date> last = getLastAppointment();
			startDate = addHours(last.getSecond(), getMarginHours(last.getFirst(), port));
		}
		
		VesselEvent result = FleetFactory.eINSTANCE.createMaintenanceEvent();
		result.setName(name);
		result.setPort(port);
		
		result.setStartAfter(startDate);
		result.setStartBy(startDate);

		final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
		final ScenarioFleetModel scenarioFleetModel = portfolioModel.getScenarioFleetModel();
		
		scenarioFleetModel.getVesselEvents().add(result);
		
		return result;
	}

	/*
	public Slot getFirstCargoSlot() {
		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		if (cargoes.isEmpty()) {
			return null;
		}
		final EList<Slot> slots = cargoes.get(0).getSortedSlots();
		return slots.get(0);
	}
		
	public Slot getLastCargoSlot() {
		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		if (cargoes.isEmpty()) {
			return null;
		}
		final EList<Slot> slots = cargoes.get(cargoes.size() - 1).getSortedSlots();
		return slots.get(slots.size()-1);
	}
	*/	
	
	public Pair<Port, Date> getLastAppointment() {
		Date date = null;
		Port port = null;
		
		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		for (Cargo cargo: cargoes) {
			final EList<Slot> slots = cargo.getSortedSlots();
			final Slot slot = slots.get(slots.size()-1);
			final Date slotDate = slot.getWindowEndWithSlotOrPortTime();
			
			if (date == null || date.before(slotDate)) {
				date = slotDate;
				port = slot.getPort();
			}
		}
		
		EList<VesselEvent> events = scenario.getPortfolioModel().getScenarioFleetModel().getVesselEvents();
		for (VesselEvent event: events) {
			Date eventDate = event.getStartBy();
			if (eventDate == null) {
				eventDate = event.getStartAfter();
			}
			if (eventDate != null) {
				eventDate = new Date(eventDate.getTime() + Timer.ONE_DAY * event.getDurationInDays());
	
				if (date == null || date.before(eventDate)) {
					date = eventDate;
					port = event.getPort();
				}
			}
		}
		
		if (date == null && port == null) {
			return null;
		}
		
		return new Pair<Port, Date>(port, date);
	}
	
	public Pair<Port, Date> getFirstAppointment() {
		Date date = null;
		Port port = null;
		
		final EList<Cargo> cargoes = scenario.getPortfolioModel().getCargoModel().getCargoes();
		for (Cargo cargo: cargoes) {
			final EList<Slot> slots = cargo.getSortedSlots();
			final Slot slot = slots.get(0);
			final Date slotDate = slot.getWindowStartWithSlotOrPortTime();
			
			if (date == null || date.after(slotDate)) {
				date = slotDate;
				port = slot.getPort();
			}
		}
		
		EList<VesselEvent> events = scenario.getPortfolioModel().getScenarioFleetModel().getVesselEvents();
		for (VesselEvent event: events) {
			Date eventDate = event.getStartAfter();
			if (eventDate == null) {
				eventDate = event.getStartBy();
			}
			if (eventDate != null) {
				if (date == null || date.after(eventDate)) {
					date = eventDate;
					port = event.getPort();
				}
			}
		}
		
		if (date == null && port == null) {
			return null;
		}
		
		return new Pair<Port, Date>(port, date);
	}
	
	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldown(final double value) {
		final PricingModel pricingModel = scenario.getPricingModel();
		final PortModel portModel = scenario.getPortModel();

		final CommodityIndex cooldownIndex = pricingCreator.createDefaultCommodityIndex("cooldown", value);

		final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
		price.setIndex(cooldownIndex);

		for (final Port port : portModel.getPorts()) {
			price.getPorts().add(port);
		}

		pricingModel.getCooldownPrices().add(price);
	}

}
