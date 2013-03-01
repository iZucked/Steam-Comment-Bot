/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.FixedPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Class to create a scenario programmatically.
 * 
 * The scenario is created by default with 
 *  - one contract legal entity (associated with all contracts)
 *  - one shipping legal entity
 *  - one sales contract
 *  - one vessel class
 *  - one vessel
 *  - three ports
 *  - one purchase contract, and
 *  - one (default) route
 *  
 * The following methods are provided to add further EML objects to the scenario:
 *  - addEntity
 *  - addVessel
 *  - addVesselSimple
 *  - addPorts
 *  - addCargo
 *  - addDryDock
 *  - addCharterOut
 *  - addAllowedVesselsOnCargo
 *  - addInaccessiblePortsOnVesselClass
 *  - addSalesContract
 *  - addPurchaseContract
 *
 * The scenario then needs to be finalised using {@link #buildScenario()} before it can be used.
 * 
 * @author Simon McGregor
 * @since 3.0
 * 
 */
public class DefaultScenarioCreator {

	private static final Logger log = LoggerFactory.getLogger(DefaultScenarioCreator.class);
	public float dischargePrice = 1f;

	// will need at least one sales contract and purchase contract for any completed transactions
	SalesContract salesContract;
	PurchaseContract purchaseContract;
	// will need a contract entity and a shipping entity
	LegalEntity contractEntity;
	LegalEntity shippingEntity;
	
	final DefaultFleetCreator fleetCreator = new DefaultFleetCreator();
	final DefaultPortCreator portCreator = new DefaultPortCreator();
	final DefaultCargoCreator cargoCreator = new DefaultCargoCreator();

	MMXRootObject scenario;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private static final String timeZone = TimeZone.getDefault().getID();

	public DefaultScenarioCreator() {
		scenario = ManifestJointModel.createEmptyInstance(null);
		createMinimalCompleteScenario();
		
		
		// ScenarioUtils.addDefaultSettings(scenario);
	}
	
	/**
	 * Initialises a minimal complete scenario, creating contract and shipping entities,
	 * one vessel class and one vessel, one (default) route, one fixed-price sales contract
	 * and one fixed-price purchase contract.
	 */
	public void createMinimalCompleteScenario() {
		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);
		
		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");
		commercialModel.setShippingEntity(shippingEntity);

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract");
		
		// create a vessel class with default name
		VesselClass vc = fleetCreator.createDefaultVesselClass(null);
		// create a vessel in that class
		fleetCreator.createMultipleDefaultVessels(vc, 1);
		
		// need to create a default route
		addRoute(ScenarioTools.defaultRouteName);		

		// initialise the port creator with a convenient base distance multiplier
		if (vc.getMaxSpeed() == vc.getMinSpeed()) {
			double speed = vc.getMaxSpeed();
			portCreator.baseDistanceMultiplier = speed;
		}
		
		// create three ports (and their distances) all with default settings
		Port [] ports = portCreator.createDefaultPorts(3);
		
		Port loadPort = ports[1];
		Port dischargePort = ports[2];
		int distance = portCreator.getDistance(loadPort, dischargePort, null);
		int duration = (int) (2 * distance / vc.getMaxSpeed());
		
		cargoCreator.createDefaultCargo(null, ports[1], ports[2], null, duration);
	}
	
	/**
	 * @since 3.0
	 */
	public LegalEntity addEntity(String name) {
		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);
		LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(entity);
		return entity;
	}
	
	/**
	 * @since 3.0
	 */
	public Route addRoute(String name) {		
		final PortModel portModel = scenario.getSubModel(PortModel.class);
		Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(name);
		portModel.getRoutes().add(r);
		return r;
	}
	
	public class DefaultVesselStateAttributesCreator {
		int fuelTravelConsumptionPerHour = 10;
		int NBORatePerHour = 5; // should this be converted to per-day?
		int fuelIdleConsumptionPerHour = 5;		
		
		double speed = 10;
		int consumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		double maxSpeed = 10;
		int maxConsumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		
		final int fuelIdleConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int NBOIdleRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBORatePerHour);
		
		
		public VesselStateAttributes createVesselStateAttributes() {
			final VesselStateAttributes result = FleetFactory.eINSTANCE.createVesselStateAttributes();
			
			final FuelConsumption fc = FleetFactory.eINSTANCE.createFuelConsumption();

			fc.setSpeed(speed);
			fc.setConsumption(consumption);
			
			result.getFuelConsumption().add(fc);
			
			result.setIdleBaseRate(fuelIdleConsumptionPerDay);
			result.setIdleNBORate(NBOIdleRatePerDay);
			result.setNboRate(NBORatePerHour);

			return result;
		}

		
	}
	
	public class DefaultFleetCreator {
		// default values
		public static final int spotCharterCount = 0;
		public static final double defaultMinSpeed = 10;
		public static final double defaultMaxSpeed = 10;
		final int baseFuelUnitPrice = 10;
		final int eqivFactor = 1;
		final int speed = 10;
		final int defaultCapacity = 1000000;
		final int consumption = 10;
		final int NBORate = 10;
		final int defaultPilotLightRate = 0;
		final int defaultMinHeelVolume = 500;
		final int defaultCooldownTime = 0;
		final int defaultWarmupTime = Integer.MAX_VALUE;
		final double defaultEquivalenceFactor = 1.0;
		final int cooldownVolume = 0;
		final double defaultFillCapacity = 1.0;		

		public BaseFuel createDefaultBaseFuel() {
			final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
			final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
			final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

			final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
			baseFuel.setName("BASE FUEL");
			baseFuel.setEquivalenceFactor(defaultEquivalenceFactor);
			fleetModel.getBaseFuels().add(baseFuel);

			BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
			bfc.setFuel(baseFuel);
			bfc.setPrice(baseFuelUnitPrice);
			fleetCostModel.getBaseFuelPrices().add(bfc);
			
			return baseFuel;
		}
		
		public VesselClass createDefaultVesselClass(String name) {
			final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
			if (name == null) {
				final int n = fleetModel.getVesselClasses().size();
				name = "Vessel Class " + n;
			}
			
			final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
			fleetModel.getVesselClasses().add(vc);
		
			DefaultVesselStateAttributesCreator dvsac = new DefaultVesselStateAttributesCreator();
			
			CharterCostModel charterCostModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
			charterCostModel.setSpotCharterCount(spotCharterCount);
			charterCostModel.getVesselClasses().add(vc);

			final SpotMarketsModel spotMarketsModel = scenario.getSubModel(SpotMarketsModel.class);
			spotMarketsModel.getCharteringSpotMarkets().add(charterCostModel);
			
			vc.setLadenAttributes(dvsac.createVesselStateAttributes());
			vc.setBallastAttributes(dvsac.createVesselStateAttributes());
			vc.setName(name);
			vc.setBaseFuel(createDefaultBaseFuel());
			vc.setMinSpeed(defaultMinSpeed);
			vc.setMaxSpeed(defaultMaxSpeed);
			vc.setCapacity(defaultCapacity);
			vc.setPilotLightRate(defaultPilotLightRate);
			vc.setCoolingTime(defaultCooldownTime);
			vc.setWarmingTime(defaultWarmupTime);
			vc.setCoolingVolume(cooldownVolume);
			vc.setMinHeel(defaultMinHeelVolume);
			vc.setFillCapacity(defaultFillCapacity);	
			
			return vc;
		}
		
		public Vessel createDefaultVessel(String name, VesselClass vc) {
			final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			vessel.setVesselClass(vc);
			vessel.setName(name);
			
			final VesselAvailability availability = FleetFactory.eINSTANCE.createVesselAvailability();

			vessel.setAvailability(availability);

			HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
			vessel.setStartHeel(heelOptions);

			fleetModel.getVessels().add(vessel);
			
			return vessel;
		}
		
		public Vessel [] createMultipleDefaultVessels(final VesselClass vc, final int num) {
			Vessel[] result = new Vessel[num];
			for (int i = 0; i < num; i++) {
				result[i] = createDefaultVessel(i + "(class " + vc.getName() + ")", vc);
			}
			return result;
		}
		
	}

	
	public class DefaultPortCreator {
		// for auto-generated distances, we can supply a base multiplier so
		// that distances are integer multiples of vessel speeds
		double baseDistanceMultiplier = 1; 
		double defaultCv = 20;
		
		public DefaultPortCreator() {
		}

		public Integer getDistance(Port p1, Port p2, Route r) {
			if (r == null) {
				final PortModel portModel = scenario.getSubModel(PortModel.class);
				r = portModel.getRoutes().get(0);
			}
			
			for (RouteLine line: r.getLines()) {
				if (line.getFrom() == p1 && line.getTo() == p2) {
					return line.getDistance();
				}
			}
			
			return null;
		}
		
		/**
		 * Creates a new DefaultPortCreator whose autogenerated distances will
		 * be multiples of the specified base distance multiplier. If the speeds of
		 * all vessels in the scenario have a common factor, it may be convenient
		 * to set the base multiplier to that common factor, so that travel times
		 * are all integers.
		 *  
		 * 
		 * @param baseDistanceMultiplier
		 */
		public DefaultPortCreator(int baseDistanceMultiplier) {
			this.baseDistanceMultiplier = baseDistanceMultiplier;
		}
		
		/**
		 * Sets the distance between two ports via a specified route to be 
		 * symmetrically equal to the specified value. If route == null, 
		 * the default route is used.
		 * 
		 * @param p1 
		 * @param p2
		 * @param distance
		 * @param r
		 */
		public void setDistance(Port p1, Port p2, int distance, Route r) {
			setOneWayDistance(p1, p2, distance, r);
			setOneWayDistance(p2, p1, distance, r);			
		}
		
		/**
		 * Sets the distance from port p1 to p2 via a specified route to be 
		 * equal to the specified value. If route == null, the default route is used.
		 * The distance from port p2 to p1 is not set.
		 * 
		 * @param p1
		 * @param p2
		 * @param distance
		 * @param r
		 */
		public void setOneWayDistance(Port p1, Port p2, int distance, Route r) {
			// if not specified, set r to route 0 (hopefully the default one)
			if (r == null) {
				final PortModel portModel = scenario.getSubModel(PortModel.class);
				r = portModel.getRoutes().get(0);
			}
			
			final RouteLine distanceLine = PortFactory.eINSTANCE.createRouteLine();
			distanceLine.setFrom(p1);
			distanceLine.setTo(p2);
			distanceLine.setDistance(distance);
			r.getLines().add(distanceLine);
		}
		
		/**
		 * Creates a new port and connects it to existing ports using the
		 * specified 1d array of distances (one for each existing port).
		 * If the name or distances parameters are left null, sensible defaults
		 * will be chosen.  
		 * 
		 * @param name
		 * @param distances
		 * @return
		 */
		public Port createPort(String name, int [] distances) {
			final PortModel portModel = scenario.getSubModel(PortModel.class);
			
			// if not specified, set name to a sensible default value
			if (name == null) {
				int n = portModel.getPorts().size();
				name = "Port " + n;				
			}
			
			final Port result = PortFactory.eINSTANCE.createPort();
			
			result.setTimeZone(timeZone);
			result.setName(name);
			result.setCvValue(defaultCv);
			// TODO: initialise other parameters with explicit defaults

			if (distances == null) {
				distances = createDefaultDistances();
			}
			
			EList<Port> ports = portModel.getPorts();
			ports.add(result);


			for (int i = 0; i < distances.length; i++) {
				setDistance(ports.get(i), result, distances[i], null);
			}

			return result;
		}

		/**
		 * Creates a default list of distances between a new port and existing ports.
		 * The distances are multiples of the base distance multiplier supplied to
		 * the 
		 * 
		 * @return
		 */
		private int [] createDefaultDistances() {
			final PortModel portModel = scenario.getSubModel(PortModel.class);
			EList<Port> ports = portModel.getPorts();
			int [] result = new int [ports.size()];
			
			for (int i = 0; i < result.length; i++) {
				// TODO: make a more meaningful value here
				result[i] = (int) ((i+1) * baseDistanceMultiplier);
				
			}
			return result;
		}
		
		public Port [] createDefaultPorts(int num) {
			Port [] result = new Port [num];
			for (int i = 0; i < num; i++) {
				result[i] = createPort(null, null);
			}
			return result;
		}
	}
	
	public class DefaultCargoCreator {
		int defaultWindowSize = 0;
		
		public void setDefaultSlotWindow(Slot slot, Date time) {
			slot.setWindowSize(defaultWindowSize);
			Port port = slot.getPort();
			final TimeZone zone = TimeZone.getTimeZone(port.getTimeZone() == null || port.getTimeZone().isEmpty() ? "UTC" : port.getTimeZone());

			final Calendar loadCalendar = Calendar.getInstance(zone);
			
			loadCalendar.setTime(time);
			slot.setWindowStartTime(loadCalendar.get(Calendar.HOUR_OF_DAY));
			loadCalendar.set(Calendar.HOUR_OF_DAY, 0);
			loadCalendar.set(Calendar.MINUTE, 0);
			loadCalendar.set(Calendar.SECOND, 0);
			loadCalendar.set(Calendar.MILLISECOND, 0);
			slot.setWindowStart(loadCalendar.getTime());
		}
		
		public Cargo createDefaultCargo(String name, Port loadPort, Port dischargePort, Date loadTime, int travelTimeInHours) {
			Cargo result = CargoFactory.eINSTANCE.createCargo();
			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			
			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = new Date(System.currentTimeMillis());								
			}
			
			// if name is not specified, set it to a sensible default
			if (name == null) {
				String format = "Cargo from %s to %s in %d hrs";
				name = String.format(format, loadPort.getName(), dischargePort.getName(), travelTimeInHours);
			}
			
			result.setLoadSlot(loadSlot);
			result.setDischargeSlot(dischargeSlot);

			loadSlot.setPort(loadPort);
			dischargeSlot.setPort(dischargePort);
			
			loadSlot.setContract(purchaseContract);
			dischargeSlot.setContract(salesContract);
			loadSlot.setName("load");
			dischargeSlot.setName("discharge");
			
			setDefaultSlotWindow(loadSlot, loadTime);
			setDefaultSlotWindow(dischargeSlot, new Date(loadTime.getTime() + (Timer.ONE_HOUR * travelTimeInHours)));

			result.setName(name);

			final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);

			cargoModel.getLoadSlots().add(loadSlot);
			cargoModel.getDischargeSlots().add(dischargeSlot);
			cargoModel.getCargoes().add(result);
			
			return result;
		}
	}
	
	/**
	 * Add a simple vessel to the scenario.
	 * 
	 * @param vesselClassName
	 *            The name of the class of the vessel
	 * @param numOfVesselsToCreate
	 *            The number of identical vessels to create.
	 * @param baseFuelUnitPrice
	 *            The base fuel unit price.
	 * @param speed
	 *            The one speed the vessel can travel at.
	 * @param capacity
	 * @param consumption
	 *            The fuel consumption (constant for all legs/idles) per day
	 * @param NBORate
	 *            The NBO rate (constant for all legs/idles) per day
	 * @param pilotLightRate
	 *            THe pilot light rate
	 * @param minHeelVolume
	 *            The minimum heel volume
	 */
	public Vessel[] addVesselSimple(final String vesselClassName, final int numOfVesselsToCreate, final float baseFuelUnitPrice, final int speed, final int capacity, final int consumption,
			final int NBORate, final int pilotLightRate, final int minHeelVolume, final boolean isTimeChartered) {

		final float equivalenceFactor = 1;
		final int spotCharterCount = 0;

		return addVessel(vesselClassName, numOfVesselsToCreate, spotCharterCount, baseFuelUnitPrice, equivalenceFactor, speed, speed, capacity, speed, consumption, speed, consumption, consumption,
				NBORate, NBORate, speed, consumption, speed, consumption, consumption, NBORate, NBORate, pilotLightRate, minHeelVolume, isTimeChartered);
	}

	/**
	 * Creates a vessel class and adds the specified number of vessels of the created class to the scenario. The attributes of the vessel class and vessel are set using the arguments.
	 */
	public Vessel[] addVessel(final String vesselClassName, final int numOfVesselsToCreate, final int spotCharterCount, final float baseFuelUnitPrice, final float equivalenceFactor,
			final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed, final int ballastMaxConsumption,
			final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption, final int ladenMaxSpeed,
			final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final int pilotLightRate, final int minHeelVolume,
			final boolean isTimeChartered) {

		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;

		final double fillCapacity = 1.0;

		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setEquivalenceFactor(equivalenceFactor);
		fleetModel.getBaseFuels().add(baseFuel);

		BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
		bfc.setFuel(baseFuel);
		bfc.setPrice(baseFuelUnitPrice);
		fleetCostModel.getBaseFuelPrices().add(bfc);

		final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vc.setLadenAttributes(laden);
		vc.setBallastAttributes(ballast);

		fleetModel.getVesselClasses().add(vc);

		vc.setName(vesselClassName);
		vc.setBaseFuel(baseFuel);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(maxSpeed);
		vc.setCapacity(capacity);
		vc.setPilotLightRate(pilotLightRate);
		vc.setCoolingTime(cooldownTime);
		vc.setWarmingTime(warmupTime);
		vc.setCoolingVolume(cooldownVolume);
		vc.setMinHeel(minHeelVolume);
		vc.setFillCapacity(fillCapacity);

		CharterCostModel charterCostModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
		charterCostModel.setSpotCharterCount(spotCharterCount);
		// Costs
		charterCostModel.getVesselClasses().add(vc);

		final SpotMarketsModel spotMarketsModel = scenario.getSubModel(SpotMarketsModel.class);
		spotMarketsModel.getCharteringSpotMarkets().add(charterCostModel);

		final FuelConsumption ladenMin = FleetFactory.eINSTANCE.createFuelConsumption();
		final FuelConsumption ladenMax = FleetFactory.eINSTANCE.createFuelConsumption();

		final FuelConsumption ballastMin = FleetFactory.eINSTANCE.createFuelConsumption();
		final FuelConsumption ballastMax = FleetFactory.eINSTANCE.createFuelConsumption();

		ballastMin.setSpeed(ballastMinSpeed);
		ballastMin.setConsumption(ballastMinConsumption);
		ballastMax.setSpeed(ballastMaxSpeed);
		ballastMax.setConsumption(ballastMaxConsumption);

		ladenMin.setSpeed(ladenMinSpeed);
		ladenMin.setConsumption(ladenMinConsumption);
		ladenMax.setSpeed(ladenMaxSpeed);
		ladenMax.setConsumption(ladenMaxConsumption);

		laden.getFuelConsumption().add(ladenMin);
		laden.getFuelConsumption().add(ladenMax);

		ballast.getFuelConsumption().add(ballastMin);
		ballast.getFuelConsumption().add(ballastMax);

		laden.setIdleBaseRate(ladenIdleConsumptionRate);
		laden.setIdleNBORate(ladenIdleNBORate);
		laden.setNboRate(ladenNBORate);

		ballast.setIdleBaseRate(ballastIdleConsumptionRate);
		ballast.setIdleNBORate(ballastIdleNBORate);
		ballast.setNboRate(ballastNBORate);

		// return a list of all vessels created.
		final Vessel created[] = new Vessel[numOfVesselsToCreate];

		// now create vessels of this class
		for (int i = 0; i < numOfVesselsToCreate; i++) {
			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			vessel.setVesselClass(vc);
			vessel.setName(i + " (class " + vesselClassName + ")");

			if (isTimeChartered) {
				vessel.setTimeCharterRate(10);
			}

			final VesselAvailability availability = FleetFactory.eINSTANCE.createVesselAvailability();

			vessel.setAvailability(availability);

			HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
			vessel.setStartHeel(heelOptions);

			fleetModel.getVessels().add(vessel);
			created[i] = vessel;
		}

		return created;
	}

	// /**
	// * Add a canal to all vessel classes.
	// */
	// public void addCanal(final VesselClassCost canalCost) {
	//
	// canalCostsForAllVesselClasses.add(canalCost);
	// }

	// /**
	// * Add a canal to specific vessel class.
	// */
	// public void addCanal(final VesselClass vc, final VesselClassCost canalCost) {
	// vc.getCanalCosts().add(canalCost);
	// }

	/**
	 * Add two ports to the scenario. There is one distance between the ports.
	 * 
	 * @param portA
	 *            The port to add.
	 * @param portB
	 *            The second port to add.
	 * @param distance
	 *            The single symmetrical distance between the ports.
	 */
	public void addPorts(final Port portA, final Port portB, final int distance) {
		addPorts(portA, portB, new int[] { distance });
	}

	/**
	 * Add two ports to the scenario with one or many distances that are symmetric.
	 * 
	 * @param portA
	 *            The port to add.
	 * @param portB
	 *            The second port to add.
	 * @param distancesSymmetric
	 *            A list of symmetrical distances between the two ports.
	 */
	public void addPorts(final Port portA, final Port portB, final int[] distancesSymmetric) {
		addPorts(portA, portB, distancesSymmetric, distancesSymmetric);
	}

	/**
	 * Add two ports to the scenario and specify the distances from and to each port. <br>
	 * The ports must be created from the method {@link DefaultScenarioCreator#createPort(String)} to be correctly added to the scenario.
	 * 
	 * @param portA
	 *            The port to add.
	 * @param portB
	 *            The second port to add.
	 * @param AtoBDistances
	 *            A list of distances from port A to port B
	 * @param BtoADistances
	 *            A list of distances from port B to port A
	 */
	public void addPorts(final Port portA, final Port portB, final int[] AtoBDistances, final int[] BtoADistances) {

		final PortModel portModel = scenario.getSubModel(PortModel.class);
		if (!portModel.getPorts().contains(portA)) {
			portModel.getPorts().add(portA);
			portA.setTimeZone(timeZone);
		}
		if (!portModel.getPorts().contains(portB)) {
			portModel.getPorts().add(portB);
			portB.setTimeZone(timeZone);
		}

		// Assuming we've created the default rout
		Route r = portModel.getRoutes().get(0);

		for (final int distance : AtoBDistances) {
			final RouteLine distanceLine = PortFactory.eINSTANCE.createRouteLine();
			distanceLine.setFrom(portA);
			distanceLine.setTo(portB);
			distanceLine.setDistance(distance);
			r.getLines().add(distanceLine);
		}

		for (final int distance : BtoADistances) {
			final RouteLine distanceLine = PortFactory.eINSTANCE.createRouteLine();
			distanceLine.setFrom(portB);
			distanceLine.setTo(portA);
			distanceLine.setDistance(distance);
			r.getLines().add(distanceLine);
		}
	}

	
	/**
	 * Add a cargo to the scenario. <br>
	 * Both the load and discharge ports must be added using {@link #addPorts(Port, Port, int[], int[])} to correctly set up distances.
	 */
	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final int loadPrice, final float dischargePrice, final float cvValue, final Date loadWindowStart,
			final int travelTime) {

		final PortModel portModel = scenario.getSubModel(PortModel.class);
		if (!portModel.getPorts().contains(loadPort)) {
			log.warn("Scenario does not contain load port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(loadPort);
		}
		if (!portModel.getPorts().contains(dischargePort)) {
			log.warn("Scenario does not contain discharge port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(dischargePort);
		}

		// final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dis = CargoFactory.eINSTANCE.createDischargeSlot();

		cargo.setLoadSlot(load);
		cargo.setDischargeSlot(dis);

		load.setPort(loadPort);
		dis.setPort(dischargePort);
		load.setContract(purchaseContract);
		dis.setContract(salesContract);
		load.setName("load" + cargoID);
		dis.setName("discharge" + cargoID);

		dis.setPriceExpression(Float.toString(dischargePrice));
		load.setPriceExpression(Float.toString(loadPrice));

		load.setMaxQuantity(loadMaxQuantity);
		dis.setMaxQuantity(dischargeMaxQuantity);

		load.setCargoCV(cvValue);

		load.setWindowSize(0);

		final TimeZone loadZone = TimeZone.getTimeZone(loadPort.getTimeZone() == null || loadPort.getTimeZone().isEmpty() ? "UTC" : loadPort.getTimeZone());

		final TimeZone dischargeZone = TimeZone.getTimeZone(dischargePort.getTimeZone() == null || dischargePort.getTimeZone().isEmpty() ? "UTC" : dischargePort.getTimeZone());

		final Calendar loadCalendar = Calendar.getInstance(loadZone);
		loadCalendar.setTime(loadWindowStart);
		load.setWindowStartTime(loadCalendar.get(Calendar.HOUR_OF_DAY));
		loadCalendar.set(Calendar.HOUR_OF_DAY, 0);
		loadCalendar.set(Calendar.MINUTE, 0);
		loadCalendar.set(Calendar.SECOND, 0);
		loadCalendar.set(Calendar.MILLISECOND, 0);
		load.setWindowStart(loadCalendar.getTime());

		final Date dischargeDate = new Date(loadWindowStart.getTime() + (Timer.ONE_HOUR * travelTime));
		final Calendar dischargeCalendar = Calendar.getInstance(dischargeZone);
		dischargeCalendar.setTime(dischargeDate);
		dis.setWindowStartTime(dischargeCalendar.get(Calendar.HOUR_OF_DAY));

		dischargeCalendar.set(Calendar.HOUR_OF_DAY, 0);
		dischargeCalendar.set(Calendar.MINUTE, 0);
		dischargeCalendar.set(Calendar.SECOND, 0);
		dischargeCalendar.set(Calendar.MILLISECOND, 0);

		dis.setWindowStart(dischargeCalendar.getTime());
		dis.setWindowSize(0);

		cargo.setName(cargoID);

		final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);

		cargoModel.getLoadSlots().add(load);
		cargoModel.getDischargeSlots().add(dis);
		cargoModel.getCargoes().add(cargo);

		return cargo;
	}

	public DryDockEvent addDryDock(final Port startPort, final Date start, final int durationDays) {

		final PortModel portModel = scenario.getSubModel(PortModel.class);
		if (!portModel.getPorts().contains(startPort)) {
			log.warn("Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(startPort);
		}

		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		// Set up dry dock.
		final DryDockEvent dryDock = FleetFactory.eINSTANCE.createDryDockEvent();
		dryDock.setName("Drydock");
		dryDock.setDurationInDays(durationDays);
		dryDock.setPort(startPort);
		// add to scenario's fleet model
		fleetModel.getVesselEvents().add(dryDock);

		// define the start and end time
		dryDock.setStartAfter(start);
		dryDock.setStartBy(start);

		return dryDock;
	}

	public CharterOutEvent addCharterOut(final String id, final Port startPort, final Port endPort, final Date startCharterOut, final int heelLimit, final int charterOutDurationDays,
			final float cvValue, final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		final CharterOutEvent charterOut = FleetFactory.eINSTANCE.createCharterOutEvent();
		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

		// the start and end of the charter out starting-window is 0, for simplicity.
		charterOut.setStartAfter(startCharterOut);
		charterOut.setStartBy(startCharterOut);

		charterOut.setPort(startPort);
		// don't set the end port if both ports are the same - this is equivalent to setting the end port to unset and is a good place to test it works
		if (!startPort.equals(endPort)) {
			charterOut.setRelocateTo(endPort);
		}

		charterOut.setName(id);
		final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		heelOptions.setVolumeAvailable(heelLimit);
		heelOptions.setCvValue(cvValue);
		heelOptions.setPricePerMMBTU(dischargePrice);
		charterOut.setHeelOptions(heelOptions);

		charterOut.setDurationInDays(charterOutDurationDays);

		charterOut.setHireRate(dailyCharterOutPrice);
		charterOut.setRepositioningFee(repositioningFee);
		// add to the scenario's fleet model
		fleetModel.getVesselEvents().add(charterOut);

		return charterOut;
	}

	/**
	 * Finish making the scenario by adding the canals to the vessel classes.
	 * 
	 * @return The finished scenario.
	 */
	public MMXRootObject buildScenario() {

		// Add every canal to every vessel class.
		// for (final VesselClassCost canalCost : this.canalCostsForAllVesselClasses) {
		// for (final VesselClass vc : fleetModel.getVesselClasses()) {
		// addCanal(vc, canalCost);
		// }
		// }

		fixUUIDMisMatches(scenario);

		return scenario;
	}

	/**
	 * When copying a scenario the UUIDs are not copied correctly unless the getter method is called. The method checks whether there is a value and makes a new one if there isn't
	 * 
	 * @param scenario
	 *            The scenario to fix.
	 */
	private static void fixUUIDMisMatches(final MMXRootObject scenario) {

		final TreeIterator<EObject> iterator = scenario.eAllContents();
		while (iterator.hasNext()) {
			final EObject obj = iterator.next();

			if (obj instanceof UUIDObject) {
				((UUIDObject) obj).getUuid();
			}
		}
	}

	/**
	 * Creates a canal and costs. Although this only returns a VesselClassCost for the canal costs, the canal can be retrieved by using {@link VesselClassCost#getCanal()}.
	 * 
	 * @param canalName
	 *            The name of the canal
	 * @param distanceAToB
	 *            Distance along the canal from port A to port B
	 * @param distanceBToA
	 *            Distance along the canal from port B to port A
	 * @param canalLadenCost
	 *            Cost in dollars for a laden vessel
	 * @param canalUnladenCost
	 *            Cost in dollars for a ballast vessel
	 * @param canalTransitFuelDays
	 *            MT of base fuel per day used when in transit
	 * @param canalTransitTime
	 *            Transit time in hours
	 * @return
	 */
	public static void createCanalAndCost(MMXRootObject scenario, final String canalName, Port A, Port B, final int distanceAToB, final int distanceBToA, final int canalLadenCost,
			final int canalUnladenCost, final int canalTransitFuelDays, final int canalNBORateDays, final int canalTransitTime) {

		final Route canal = PortFactory.eINSTANCE.createRoute();
		canal.setCanal(true);
		scenario.getSubModel(PortModel.class).getRoutes().add(canal);
		canal.setName(canalName);
		// add distance lines, as for the main distance model:
		final RouteLine atob = PortFactory.eINSTANCE.createRouteLine();
		atob.setFrom(A);
		atob.setTo(B);
		atob.setDistance(distanceAToB);

		final RouteLine btoa = PortFactory.eINSTANCE.createRouteLine();
		btoa.setFrom(B);
		btoa.setTo(A);
		btoa.setDistance(distanceBToA);

		canal.getLines().add(atob);
		canal.getLines().add(btoa);

		// next do canal costs
		final RouteCost canalCost = PricingFactory.eINSTANCE.createRouteCost();
		canalCost.setRoute(canal);
		canalCost.setLadenCost(canalLadenCost); // cost in dollars for a laden vessel
		canalCost.setBallastCost(canalUnladenCost); // cost in dollars for a ballast vessel

		FleetModel fleetModel = scenario.getSubModel(FleetModel.class);

		VesselClassRouteParameters params = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

		params.setRoute(canal);
		params.setLadenConsumptionRate(canalTransitFuelDays);
		params.setBallastConsumptionRate(canalTransitFuelDays);
		params.setLadenNBORate(canalNBORateDays);
		params.setBallastNBORate(canalNBORateDays);
		params.setExtraTransitTime(canalTransitTime);

		for (VesselClass vc : fleetModel.getVesselClasses()) {
			vc.getRouteParameters().add(EcoreUtil.copy(params));
			final RouteCost rc2 = EcoreUtil.copy(canalCost);
			rc2.setVesselClass(vc);
			scenario.getSubModel(PricingModel.class).getRouteCosts().add(rc2);
		}
	}

	/**
	 * A vessel class has a list of inaccessible ports. This method can add to that list if the given vessel class has already been added to the scenario. Note that that this method does not check to
	 * see if the ports are already in the scenario.
	 * 
	 * @param vc
	 *            The vessel class to add the constraint to.
	 * @param inaccessiblePorts
	 *            The ports to make inaccessible.
	 * @return If the ports are added successfully the method will return true. If the vessel class is not in the scenario it will return false.
	 */
	public boolean addInaccessiblePortsOnVesselClass(final VesselClass vc, final Port[] inaccessiblePorts) {

		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		if (scenarioFleetModelContainsVesselClass(vc)) {

			for (final VesselClass v : fleetModel.getVesselClasses()) {
				if (v.equals(vc)) {
					v.getInaccessiblePorts().addAll(Arrays.asList(inaccessiblePorts));
				}

				return true;
			}
		}
		return false;
	}

	private boolean scenarioFleetModelContainsVesselClass(final VesselClass vc) {

		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		for (final Vessel v : fleetModel.getVessels()) {
			if (v.getVesselClass().equals(vc)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Each cargo has a list of vessels that has all vessels that are allowed to carry the cargo. (If empty there are no restrictions.) This method can add to that list. Note that no checks are made
	 * to see if the given vessels are already in the scenario.
	 * 
	 * @param cargo
	 *            The cargo to add the constraint to.
	 * @param allowedVessels
	 *            The list of vessels to add.
	 * @return True if the vessels were added to the cargo's allowed vessel list. False if the cargo was not in the scenario.
	 */
	public boolean addAllowedVesselsOnCargo(final Cargo cargo, final ArrayList<Vessel> allowedVessels) {

		final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);
		if (cargoModel.getCargoes().contains(cargo)) {

			for (final Cargo c : cargoModel.getCargoes()) {
				if (c.equals(cargo)) {
					c.getAllowedVessels().addAll(allowedVessels);

					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Adds a sales contract to the model.
	 * 
	 * @author Simon McGregor
	 * @param name
	 * @param dischargePrice
	 * @return
	 */
	public SalesContract addSalesContract(String name, float dischargePrice) {
		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);
		SalesContract result = CommercialFactory.eINSTANCE.createSalesContract();
		FixedPriceParameters params = CommercialFactory.eINSTANCE.createFixedPriceParameters();
		result.setName(name);

		result.setEntity(contractEntity);
		params.setPricePerMMBTU(dischargePrice);

		result.setPriceInfo(params);
		commercialModel.getSalesContracts().add(result);

		return result;
	}

	/**
	 * Adds a purchase contract to the model.
	 * 
	 * @author Simon McGregor
	 * @param name
	 * @param dischargePrice
	 * @return
	 * @since 3.0
	 */
	public PurchaseContract addPurchaseContract(String name) {
		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);
		PurchaseContract result = CommercialFactory.eINSTANCE.createPurchaseContract();
		FixedPriceParameters params = CommercialFactory.eINSTANCE.createFixedPriceParameters();

		result.setName(name);

		result.setEntity(contractEntity);
		result.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(result);

		return result;
	}
}
