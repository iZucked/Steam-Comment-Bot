package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
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
		final Date loadTime;
		final Slot lastSlot = getLastCargoSlot();
		
		if (lastSlot == null) {
			loadTime = null;
		}
		else {
			loadTime = addHours(lastSlot.getWindowEndWithSlotOrPortTime(), getMarginHours(lastSlot.getPort(), loadPort));
		}
				
		return cargoCreator.createDefaultCargo(null, loadPort, dischargePort, loadTime, getMarginHours(loadPort, dischargePort));
	}
	
	public int getMarginHours(Port startPort, Port endPort) {
		return 2 * getTravelTime(loadPort, dischargePort, null, (int) vc.getMaxSpeed());
	}	
	
	public void setDefaultAvailability(Port startPort, Port endPort) {		
		Slot firstSlot = getFirstCargoSlot();
		Slot lastSlot = getLastCargoSlot();
				
		final Date firstLoadDate = firstSlot.getWindowStartWithSlotOrPortTime();
		final Date lastDischargeDate = lastSlot.getWindowEndWithSlotOrPortTime();
		
		final Date startDate = addHours(firstLoadDate, -getMarginHours(startPort, loadPort));
		final Date endDate = addHours(lastDischargeDate, getMarginHours(dischargePort, endPort));
		
		final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();
		this.vesselAvailability = fleetCreator.setAvailability(scenarioFleetModel, vessel, originPort, startDate, originPort, endDate);
	}
	
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
