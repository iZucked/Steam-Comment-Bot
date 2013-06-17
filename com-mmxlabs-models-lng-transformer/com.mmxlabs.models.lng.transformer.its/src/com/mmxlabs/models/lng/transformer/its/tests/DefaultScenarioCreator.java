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

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
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
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
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
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Class to create a scenario programmatically.
 * 
 * The scenario is created by default with - one contract legal entity (associated with all contracts) - one shipping legal entity - one sales contract - one vessel class - one vessel - three ports -
 * one purchase contract, and - one (default) route
 * 
 * The following methods are provided to add further EML objects to the scenario: - addEntity - addVessel - addVesselSimple - addPorts - addCargo - addDryDock - addCharterOut -
 * addAllowedVesselsOnCargo - addInaccessiblePortsOnVesselClass - addSalesContract - addPurchaseContract
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
	public float purchasePrice = 0.5f;

	// will need at least one sales contract and purchase contract for any completed transactions
	SalesContract salesContract;
	PurchaseContract purchaseContract;
	// will need a contract entity and a shipping entity
	LegalEntity contractEntity;
	LegalEntity shippingEntity;

	public final DefaultFleetCreator fleetCreator = new DefaultFleetCreator();
	public final DefaultPortCreator portCreator = new DefaultPortCreator();
	public final DefaultCargoCreator cargoCreator = new DefaultCargoCreator();
	public final DefaultPricingCreator pricingCreator = new DefaultPricingCreator();
	public final DefaultVesselEventCreator vesselEventCreator = new DefaultVesselEventCreator();

	public MinimalScenarioSetup minimalScenarioSetup;

	LNGScenarioModel scenario;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private static final String timeZone = TimeZone.getDefault().getID();

	public DefaultScenarioCreator() {
		scenario = ManifestJointModel.createEmptyInstance(null);
		minimalScenarioSetup = new MinimalScenarioSetup();
	}

	public Date addHours(final Date date, final int hours) {
		return new Date(date.getTime() + Timer.ONE_HOUR * hours);
	}

	public class MinimalScenarioSetup {
		public final VesselClass vc;
		public final Vessel vessel;
		public final VesselAvailability vesselAvailability;

		public final Port originPort;
		public final Port loadPort;
		public final Port dischargePort;

		public final Cargo cargo;

		/**
		 * Initialises a minimal complete scenario, creating: - contract and shipping legal entities - one vessel class and one vessel - one (default) route - one fixed-price sales contract and one
		 * fixed-price purchase contract - three ports (one origin port, one load port and one discharge port) - one cargo The vessel starts at the origin port, must travel to the load port, pick up
		 * the cargo, travel to the discharge port and discharge it. There is enough time at every stage to create some idling at the discharge port.
		 */
		public MinimalScenarioSetup() {
			final CommercialModel commercialModel = scenario.getCommercialModel();
			final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();

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

			/*
			 * determine the time it will take the vessel to travel between load and discharge ports, and create a cargo with enough time to spare
			 */

			final int duration = 2 * getTravelTime(loadPort, dischargePort, null, (int) maxSpeed);

			cargo = cargoCreator.createDefaultCargo(null, ports[1], ports[2], null, duration);

			final Date loadDate = cargo.getSlots().get(0).getWindowStart();
			final Date dischargeDate = cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

			final Date startDate = addHours(loadDate, -2 * getTravelTime(originPort, loadPort, null, (int) maxSpeed));
			final Date endDate = addHours(dischargeDate, 2 * getTravelTime(dischargePort, originPort, null, (int) maxSpeed));

			this.vesselAvailability = fleetCreator.setAvailability(scenarioFleetModel, vessel, originPort, startDate, originPort, endDate);
		}

		/**
		 * Sets up a cooldown pricing model including an index
		 */
		public void setupCooldown(final double value) {
			final PricingModel pricingModel = scenario.getPricingModel();
			final PortModel portModel = scenario.getPortModel();

			final DataIndex<Double> cooldownIndex = pricingCreator.createDefaultCommodityIndex("cooldown", value);

			final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
			price.setIndex(cooldownIndex);

			for (final Port port : portModel.getPorts()) {
				price.getPorts().add(port);
			}

			pricingModel.getCooldownPrices().add(price);
		}
	}

	/**
	 * @since 3.0
	 */
	public LegalEntity addEntity(final String name) {
		final CommercialModel commercialModel = scenario.getCommercialModel();
		final LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(entity);
		return entity;
	}

	/**
	 * @since 3.0
	 */
	public Route addRoute(final String name) {
		final PortModel portModel = scenario.getPortModel();
		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(name);
		portModel.getRoutes().add(r);
		return r;
	}

	public class DefaultVesselStateAttributesCreator {
		public static final int fuelTravelConsumptionPerHour = 15;
		public static final int NBOIdleRatePerHour = 5; // should this be converted to per-day?
		public static final int fuelIdleConsumptionPerHour = 5;

		int consumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		int maxConsumption = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);

		int fuelIdleConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		int NBOIdleRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBOIdleRatePerHour);
		int NBORatePerDay = NBOIdleRatePerDay * 2;

		public VesselStateAttributes createVesselStateAttributes(final double minSpeed, final double maxSpeed) {
			final VesselStateAttributes result = FleetFactory.eINSTANCE.createVesselStateAttributes();

			final double[] speeds = { minSpeed, maxSpeed };

			final int n = (minSpeed == maxSpeed ? 1 : 2);

			for (int i = 0; i < n; i++) {
				final FuelConsumption fc = FleetFactory.eINSTANCE.createFuelConsumption();
				fc.setConsumption(consumption);
				fc.setSpeed(speeds[i]);
				result.getFuelConsumption().add(fc);
			}

			result.setIdleBaseRate(fuelIdleConsumptionPerDay);
			result.setIdleNBORate(NBOIdleRatePerDay);
			result.setNboRate(NBORatePerDay);

			return result;
		}

	}

	public class DefaultVesselClassRouteParametersCreator {
		int defaultConsumptionRatePerHour = 10;
		int defaultNBORatePerHour = 5;
		int defaultConsumptionRatePerDay = TimeUnitConvert.convertPerHourToPerDay(defaultConsumptionRatePerHour);
		int defaultNBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(defaultNBORatePerHour);
		int defaultTransitTimeInHours = 0;

		public VesselClassRouteParameters createVesselClassRouteParameters(final Route route) {
			final VesselClassRouteParameters result = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

			result.setRoute(route);
			result.setLadenConsumptionRate(defaultConsumptionRatePerDay);
			result.setBallastConsumptionRate(defaultConsumptionRatePerDay);
			result.setLadenNBORate(defaultNBORatePerDay);
			result.setBallastNBORate(defaultNBORatePerDay);
			result.setExtraTransitTime(defaultTransitTimeInHours);

			return result;

		}
	}

	public class DefaultFleetCreator {
		// default values
		public static final int spotCharterCount = 0;
		public static final double defaultMinSpeed = 10;
		public static final double defaultMaxSpeed = 10;
		public final static int baseFuelUnitPrice = 10;
		final double eqivFactor = 1;
		final int speed = 10;
		final int defaultCapacity = 10000;
		final int defaultPilotLightRate = 0;
		final int defaultMinHeelVolume = 500;

		final int defaultWarmupTime = Integer.MAX_VALUE;
		final double defaultEquivalenceFactor = 1.0;
		final int cooldownVolume = 0;
		final double defaultFillCapacity = 1.0;
		final int startHeelVolume = 0;
		final int defaultCanalCost = 1;

		/**
		 * Creates a heel options with default settings.
		 * 
		 * @return
		 */
		public HeelOptions createDefaultHeelOptions() {
			final HeelOptions result = FleetFactory.eINSTANCE.createHeelOptions();
			result.setCvValue(portCreator.defaultCv);
			result.setPricePerMMBTU(dischargePrice);
			result.setVolumeAvailable(startHeelVolume);
			return result;
		}

		/**
		 * Creates a base fuel with default settings.
		 * 
		 * @return
		 */
		public BaseFuel createDefaultBaseFuel() {
			final PricingModel pricingModel = scenario.getPricingModel();
			final FleetCostModel fleetCostModel = pricingModel.getFleetCost();
			final FleetModel fleetModel = scenario.getFleetModel();

			final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
			baseFuel.setName("BASE FUEL");
			baseFuel.setEquivalenceFactor(defaultEquivalenceFactor);
			fleetModel.getBaseFuels().add(baseFuel);

			final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
			bfc.setFuel(baseFuel);
			bfc.setPrice(baseFuelUnitPrice);
			fleetCostModel.getBaseFuelPrices().add(bfc);

			return baseFuel;
		}

		/**
		 * Creates a vessel class with default settings. If the name parameter is null, a default name is generated.
		 * 
		 * @param name
		 * @return
		 */
		public VesselClass createDefaultVesselClass(String name) {
			final FleetModel fleetModel = scenario.getFleetModel();
			if (name == null) {
				final int n = fleetModel.getVesselClasses().size();
				name = "Vessel Class " + n;
			}

			final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
			fleetModel.getVesselClasses().add(vc);

			final DefaultVesselStateAttributesCreator dvsac = new DefaultVesselStateAttributesCreator();

			final CharterCostModel charterCostModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
			charterCostModel.setSpotCharterCount(spotCharterCount);
			charterCostModel.getVesselClasses().add(vc);

			final SpotMarketsModel spotMarketsModel = scenario.getSpotMarketsModel();
			spotMarketsModel.getCharteringSpotMarkets().add(charterCostModel);

			vc.setLadenAttributes(dvsac.createVesselStateAttributes(defaultMinSpeed, defaultMaxSpeed));
			vc.setBallastAttributes(dvsac.createVesselStateAttributes(defaultMinSpeed, defaultMaxSpeed));
			vc.setName(name);
			vc.setBaseFuel(createDefaultBaseFuel());
			vc.setMinSpeed(defaultMinSpeed);
			vc.setMaxSpeed(defaultMaxSpeed);
			vc.setCapacity(defaultCapacity);
			vc.setPilotLightRate(defaultPilotLightRate);
			vc.setWarmingTime(defaultWarmupTime);
			vc.setCoolingVolume(cooldownVolume);
			vc.setMinHeel(defaultMinHeelVolume);
			vc.setFillCapacity(defaultFillCapacity);

			return vc;
		}

		/**
		 * Creates a vessel of the specified vessel class with default parameters.
		 * 
		 * @param name
		 * @param vc
		 * @return
		 */
		public Vessel createDefaultVessel(final String name, final VesselClass vc) {
			final FleetModel fleetModel = scenario.getFleetModel();
			final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
			final ScenarioFleetModel scenarioFleetModel = portfolioModel.getScenarioFleetModel();

			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			vessel.setVesselClass(vc);
			vessel.setName(name);

			final VesselAvailability availability = FleetFactory.eINSTANCE.createVesselAvailability();

			availability.setVessel(vessel);
			availability.setStartHeel(createDefaultHeelOptions());

			fleetModel.getVessels().add(vessel);
			scenarioFleetModel.getVesselAvailabilities().add(availability);

			return vessel;
		}

		/**
		 * Creates multiple default vessels of the specified vessel class. Names are automatically generated.
		 * 
		 * @param vc
		 * @param num
		 * @return
		 */
		public Vessel[] createMultipleDefaultVessels(final VesselClass vc, final int num) {
			final Vessel[] result = new Vessel[num];
			for (int i = 0; i < num; i++) {
				result[i] = createDefaultVessel(i + "(class " + vc.getName() + ")", vc);
			}
			return result;
		}

		/**
		 * Creates and sets an availability for the specified vessel.
		 * 
		 * @param vessel
		 * @param startPort
		 * @param startDate
		 * @param endPort
		 * @param endDate
		 */
		public VesselAvailability setAvailability(final ScenarioFleetModel scenarioFleetModel, final Vessel vessel, final Port startPort, final Date startDate, final Port endPort, final Date endDate) {
			for (final VesselAvailability availability : scenarioFleetModel.getVesselAvailabilities()) {
				if (availability.getVessel() == vessel) {
					availability.getStartAt().add(startPort);
					availability.getEndAt().add(endPort);
					availability.setStartAfter(startDate);
					availability.setEndBy(endDate);
					return availability;
				}
			}
			return null;
		}

		public void assignDefaultCanalData(final VesselClass vc, final Route route) {
			assignRouteParameters(vc, route);
			setCanalCost(vc, route, defaultCanalCost, defaultCanalCost);
		}

		/**
		 * Assigns default route parameters to the specified vessel on the specified route.
		 * 
		 * @param vc
		 * @param route
		 * @return
		 */
		public VesselClassRouteParameters assignRouteParameters(final VesselClass vc, final Route route) {
			final VesselClassRouteParameters result = new DefaultVesselClassRouteParametersCreator().createVesselClassRouteParameters(route);
			vc.getRouteParameters().add(result);
			return result;
		}

		public RouteCost setCanalCost(final VesselClass vc, final Route route, final int ladenCost, final int ballastCost) {
			final PricingModel pricingModel = scenario.getPricingModel();

			final RouteCost result = PricingFactory.eINSTANCE.createRouteCost();
			result.setRoute(route);
			result.setLadenCost(ladenCost); // cost in dollars for a laden vessel
			result.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
			result.setVesselClass(vc);
			pricingModel.getRouteCosts().add(result);
			return result;
		}

	}

	public class DefaultPortCreator {
		// for auto-generated distances, we can supply a base multiplier so
		// that distances are integer multiples of vessel speeds
		double baseDistanceMultiplier = 1;
		double defaultCv = 21;

		public DefaultPortCreator() {
		}

		public Route addCanal(final String name) {
			final Route result = addRoute(name);
			result.setCanal(true);
			return result;
		}

		/**
		 * Creates a new DefaultPortCreator whose autogenerated distances will be multiples of the specified base distance multiplier. If the speeds of all vessels in the scenario have a common
		 * factor, it may be convenient to set the base multiplier to that common factor, so that travel times are all integers.
		 * 
		 * 
		 * @param baseDistanceMultiplier
		 */
		public DefaultPortCreator(final int baseDistanceMultiplier) {
			this.baseDistanceMultiplier = baseDistanceMultiplier;
		}

		/**
		 * Sets the distance between two ports via a specified route to be symmetrically equal to the specified value. If route == null, the default route is used.
		 * 
		 * @param p1
		 * @param p2
		 * @param distance
		 * @param r
		 */
		public void setDistance(final Port p1, final Port p2, final int distance, final Route r) {
			setOneWayDistance(p1, p2, distance, r);
			setOneWayDistance(p2, p1, distance, r);
		}

		/**
		 * Sets the distance from port p1 to p2 via a specified route to be equal to the specified value. If route == null, the default route is used. The distance from port p2 to p1 is not set.
		 * 
		 * @param p1
		 * @param p2
		 * @param distance
		 * @param r
		 */
		public void setOneWayDistance(final Port p1, final Port p2, final int distance, Route r) {
			// if not specified, set r to route 0 (hopefully the default one)
			if (r == null) {
				final PortModel portModel = scenario.getPortModel();
				r = portModel.getRoutes().get(0);
			}

			final RouteLine distanceLine = PortFactory.eINSTANCE.createRouteLine();
			distanceLine.setFrom(p1);
			distanceLine.setTo(p2);
			distanceLine.setDistance(distance);
			r.getLines().add(distanceLine);
		}

		/**
		 * Creates a new port and connects it to existing ports using the specified 1d array of distances (one for each existing port). If the name or distances parameters are left null, sensible
		 * defaults will be chosen.
		 * 
		 * @param name
		 * @param distances
		 * @return
		 */
		public Port createPort(String name, int[] distances) {
			final PortModel portModel = scenario.getPortModel();

			// if not specified, set name to a sensible default value
			if (name == null) {
				final int n = portModel.getPorts().size();
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

			final EList<Port> ports = portModel.getPorts();
			ports.add(result);

			for (int i = 0; i < distances.length; i++) {
				setDistance(ports.get(i), result, distances[i], null);
			}

			return result;
		}

		/**
		 * Creates a default list of distances between a new port and existing ports. The distances are multiples of the base distance multiplier supplied to the
		 * 
		 * @return
		 */
		private int[] createDefaultDistances() {
			final PortModel portModel = scenario.getPortModel();
			final EList<Port> ports = portModel.getPorts();
			final int[] result = new int[ports.size()];

			for (int i = 0; i < result.length; i++) {
				// TODO: make a more meaningful value here
				result[i] = (int) ((i + 1) * baseDistanceMultiplier);

			}
			return result;
		}

		public Port[] createDefaultPorts(final int num) {
			final Port[] result = new Port[num];
			for (int i = 0; i < num; i++) {
				result[i] = createPort(null, null);
			}
			return result;
		}

		public void setPortCost(final Port port, final PortCapability capability, final int cost) {
			final PricingModel pricingModel = scenario.getPricingModel();

			final PortCost portCost = PricingFactory.eINSTANCE.createPortCost();
			final PortCostEntry portCostEntry = PricingFactory.eINSTANCE.createPortCostEntry();
			portCostEntry.setActivity(capability);
			portCostEntry.setCost(cost);
			portCost.getEntries().add(portCostEntry);
			portCost.getPorts().add(port);

			pricingModel.getPortCosts().add(portCost);
		}
	}

	public class DefaultCargoCreator {
		int defaultWindowSize = 0;

		public void setDefaultSlotWindow(final Slot slot, final Date time) {
			slot.setWindowSize(defaultWindowSize);
			final Port port = slot.getPort();
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

		public Cargo createDefaultCargo(String name, final Port loadPort, final Port dischargePort, Date loadTime, final int travelTimeInHours) {
			final Cargo result = CargoFactory.eINSTANCE.createCargo();
			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = new Date(System.currentTimeMillis());
			}

			// if name is not specified, set it to a sensible default
			if (name == null) {
				final String format = "Cargo from %s to %s in %d hrs";
				name = String.format(format, loadPort.getName(), dischargePort.getName(), travelTimeInHours);
			}

			result.getSlots().add(loadSlot);
			result.getSlots().add(dischargeSlot);

			loadSlot.setPort(loadPort);
			dischargeSlot.setPort(dischargePort);

			loadSlot.setContract(purchaseContract);
			dischargeSlot.setContract(salesContract);

			loadSlot.setMaxQuantity(Integer.MAX_VALUE);
			loadSlot.setName("load");
			dischargeSlot.setMaxQuantity(Integer.MAX_VALUE);
			dischargeSlot.setName("discharge");

			setDefaultSlotWindow(loadSlot, loadTime);
			setDefaultSlotWindow(dischargeSlot, addHours(loadTime, travelTimeInHours));

			result.setName(name);

			final CargoModel cargoModel = scenario.getPortfolioModel().getCargoModel();

			cargoModel.getLoadSlots().add(loadSlot);
			cargoModel.getDischargeSlots().add(dischargeSlot);
			cargoModel.getCargoes().add(result);

			return result;
		}

		public Cargo createDefaultLddCargo(String name, final Port loadPort, final Port dischargePort1, final Port dischargePort2, Date loadTime, final int travelTimeInHours1,
				final int travelTimeInHours2) {
			final Cargo result = CargoFactory.eINSTANCE.createCargo();
			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			final DischargeSlot dischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = new Date(System.currentTimeMillis());
			}

			// if name is not specified, set it to a sensible default
			if (name == null) {
				final String format = "Cargo from %s to %s in %d hrs to %s in %d hrs";
				name = String.format(format, loadPort.getName(), dischargePort1.getName(), travelTimeInHours1, dischargePort2.getName(), travelTimeInHours2);
			}

			result.getSlots().add(loadSlot);
			result.getSlots().add(dischargeSlot1);
			result.getSlots().add(dischargeSlot2);

			loadSlot.setPort(loadPort);
			dischargeSlot1.setPort(dischargePort1);
			dischargeSlot2.setPort(dischargePort2);

			loadSlot.setContract(purchaseContract);
			dischargeSlot1.setContract(salesContract);
			dischargeSlot2.setContract(salesContract);

			loadSlot.setMaxQuantity(Integer.MAX_VALUE);
			loadSlot.setName("load");
			dischargeSlot1.setMaxQuantity(Integer.MAX_VALUE);
			dischargeSlot1.setName("discharge1");
			dischargeSlot2.setMaxQuantity(Integer.MAX_VALUE);
			dischargeSlot2.setName("discharge2");

			setDefaultSlotWindow(loadSlot, loadTime);
			setDefaultSlotWindow(dischargeSlot1, addHours(loadTime, travelTimeInHours1));
			setDefaultSlotWindow(dischargeSlot2, addHours(loadTime, travelTimeInHours1 + defaultWindowSize + travelTimeInHours2));

			result.setName(name);

			final CargoModel cargoModel = scenario.getPortfolioModel().getCargoModel();

			cargoModel.getLoadSlots().add(loadSlot);
			cargoModel.getDischargeSlots().add(dischargeSlot1);
			cargoModel.getDischargeSlots().add(dischargeSlot2);
			cargoModel.getCargoes().add(result);

			return result;
		}
	}

	public class DefaultPricingCreator {
		public DataIndex<Double> createDefaultCommodityIndex(final String name, final Double value) {
			final DataIndex<Double> result = createIndex(name, value);
			final PricingModel pricingModel = scenario.getPricingModel();

			pricingModel.getCommodityIndices().add(result);

			return result;
		}

		private <T> DataIndex<T> createIndex(final String name, final T value) {
			final IndexPoint<T> startPoint = PricingFactory.eINSTANCE.createIndexPoint();
			final IndexPoint<T> endPoint = PricingFactory.eINSTANCE.createIndexPoint();

			startPoint.setDate(new Date(0));
			startPoint.setValue(value);

			endPoint.setDate(new Date(Long.MAX_VALUE));
			endPoint.setValue(value);

			final DataIndex<T> result = PricingFactory.eINSTANCE.createDataIndex();

			result.getPoints().add(startPoint);
			result.getPoints().add(endPoint);

			return result;
		}

		public CharterCostModel createDefaultCharterCostModel(final VesselClass vc, final Integer minDuration, final Integer price) {
			final CharterCostModel result = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
			result.getVesselClasses().add(vc);
			result.setMinCharterOutDuration(minDuration);
			final String indexName = String.format("Charter-out cost for vessel class %s", vc.getName());
			result.setCharterOutPrice(createIndex(indexName, price));

			final SpotMarketsModel marketModel = scenario.getSpotMarketsModel();
			marketModel.getCharteringSpotMarkets().add(result);

			return result;
		}
		
		public PortCost setPortCost(Port port, PortCapability activity, int cost) {
			final PricingModel pricingModel = scenario.getPricingModel();

			/*
			 * // abortive code checking for an existing port cost based on the port specified
			for (PortCost pc: pricingModel.getPortCosts()) {
				final EList<APortSet<Port>> ports = pc.getPorts();
				if (ports.size() == 1) {
					final APortSet<Port> set = ports.get(0);
					if (set)
				}
			}
			*/
			final PortCost result = PricingFactory.eINSTANCE.createPortCost();
			result.getPorts().add(port);
			pricingModel.getPortCosts().add(result);
			
			final PortCostEntry entry = PricingFactory.eINSTANCE.createPortCostEntry();
			
			entry.setActivity(activity);
			entry.setCost(cost);
			
			result.getEntries().add(entry);
			
			return result;
			
		}
	}

	public class DefaultVesselEventCreator {
		int defaultDurationInDays = 1;

		public void addEventToModel(final VesselEvent event, final String name, final Port port, final Date startByDate, final Date startAfterDate) {
			event.setName(name);
			event.setPort(port);
			event.setDurationInDays(defaultDurationInDays);
			event.setStartBy(startByDate);
			event.setStartAfter(startAfterDate);
			final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();
			scenarioFleetModel.getVesselEvents().add(event);
		}

		public DryDockEvent createDryDockEvent(final String name, final Port port, final Date startByDate, final Date startAfterDate) {
			final DryDockEvent event = FleetFactory.eINSTANCE.createDryDockEvent();
			addEventToModel(event, name, port, startAfterDate, startAfterDate);
			return event;
		}

		public MaintenanceEvent createMaintenanceEvent(final String name, final Port port, final Date startByDate, final Date startAfterDate) {
			final MaintenanceEvent event = FleetFactory.eINSTANCE.createMaintenanceEvent();
			addEventToModel(event, name, port, startAfterDate, startAfterDate);
			return event;
		}

		public CharterOutEvent createCharterOutEvent(final String name, final Port startPort, final Port endPort, final Date startByDate, final Date startAfterDate, final int hireRate) {
			final CharterOutEvent event = FleetFactory.eINSTANCE.createCharterOutEvent();
			addEventToModel(event, name, startPort, startAfterDate, startAfterDate);
			event.setHireRate(hireRate);
			final HeelOptions options = FleetFactory.eINSTANCE.createHeelOptions();
			event.setHeelOptions(options);
			event.setRelocateTo(endPort);
			return event;
		}
	}

	public Integer getTravelTime(final Port p1, final Port p2, final Route r, final int speed) {
		return getDistance(p1, p2, r) / speed;
	}

	public Integer getDistance(final Port p1, final Port p2, Route r) {
		if (r == null) {
			final PortModel portModel = scenario.getPortModel();
			r = portModel.getRoutes().get(0);
		}

		for (final RouteLine line : r.getLines()) {
			if (line.getFrom() == p1 && line.getTo() == p2) {
				return line.getDistance();
			}
		}

		return null;
	}

	public DryDockEvent addDryDock(final Port startPort, final Date start, final int durationDays) {

		final PortModel portModel = scenario.getPortModel();
		if (!portModel.getPorts().contains(startPort)) {
			log.warn("Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(startPort);
		}

		final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();
		// Set up dry dock.
		final DryDockEvent dryDock = FleetFactory.eINSTANCE.createDryDockEvent();
		dryDock.setName("Drydock");
		dryDock.setDurationInDays(durationDays);
		dryDock.setPort(startPort);
		// add to scenario's fleet model
		scenarioFleetModel.getVesselEvents().add(dryDock);

		// define the start and end time
		dryDock.setStartAfter(start);
		dryDock.setStartBy(start);

		return dryDock;
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
	public static void createCanalAndCost(final LNGScenarioModel scenario, final String canalName, final Port A, final Port B, final int distanceAToB, final int distanceBToA,
			final int canalLadenCost, final int canalUnladenCost, final int canalTransitFuelDays, final int canalNBORateDays, final int canalTransitTime) {

		final Route canal = PortFactory.eINSTANCE.createRoute();
		canal.setCanal(true);
		scenario.getPortModel().getRoutes().add(canal);
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

		final FleetModel fleetModel = scenario.getFleetModel();

		final VesselClassRouteParameters params = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

		params.setRoute(canal);
		params.setLadenConsumptionRate(canalTransitFuelDays);
		params.setBallastConsumptionRate(canalTransitFuelDays);
		params.setLadenNBORate(canalNBORateDays);
		params.setBallastNBORate(canalNBORateDays);
		params.setExtraTransitTime(canalTransitTime);

		for (final VesselClass vc : fleetModel.getVesselClasses()) {
			vc.getRouteParameters().add(EcoreUtil.copy(params));
			final RouteCost rc2 = EcoreUtil.copy(canalCost);
			rc2.setVesselClass(vc);
			scenario.getPricingModel().getRouteCosts().add(rc2);
		}
	}

	public CharterOutEvent addCharterOut(final String id, final Port startPort, final Port endPort, final Date startCharterOut, final int heelLimit, final int charterOutDurationDays,
			final float cvValue, final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		final CharterOutEvent charterOut = FleetFactory.eINSTANCE.createCharterOutEvent();
		final ScenarioFleetModel scenarioFleetModel = scenario.getPortfolioModel().getScenarioFleetModel();

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
		scenarioFleetModel.getVesselEvents().add(charterOut);

		return charterOut;
	}

	/**
	 * Finish making the scenario by adding the canals to the vessel classes.
	 * 
	 * @return The finished scenario.
	 */
	public LNGScenarioModel buildScenario() {

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

		final FleetModel fleetModel = scenario.getFleetModel();
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

		final FleetModel fleetModel = scenario.getFleetModel();
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

		final CargoModel cargoModel = scenario.getPortfolioModel().getCargoModel();
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
	public SalesContract addSalesContract(final String name, final float dischargePrice) {
		final CommercialModel commercialModel = scenario.getCommercialModel();
		final SalesContract result = CommercialFactory.eINSTANCE.createSalesContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		result.setName(name);

		result.setEntity(contractEntity);
		params.setPriceExpression(Float.toString(dischargePrice));

		result.setPriceInfo(params);
		commercialModel.getSalesContracts().add(result);

		return result;
	}

	/**
	 * Adds a purchase contract to the model.
	 * 
	 * @author Simon McGregor
	 * @param name
	 * @param purchasePrice
	 * @return
	 * @since 3.0
	 */
	public PurchaseContract addPurchaseContract(final String name, final double purchasePrice) {
		final CommercialModel commercialModel = scenario.getCommercialModel();
		final PurchaseContract result = CommercialFactory.eINSTANCE.createPurchaseContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();

		result.setName(name);

		result.setEntity(contractEntity);
		params.setPriceExpression(Double.toString(purchasePrice));

		result.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(result);

		return result;
	}

	public void checkJourneyGeography(final Journey journey, final Port from, final Port to) {
		Assert.assertEquals(journey.getPort(), from);
		Assert.assertEquals(journey.getDestination(), to);
		final Route route = journey.getRoute();
		Assert.assertEquals(journey.getDistance(), (int) getDistance(from, to, route));
	}

	public RouteCost getRouteCost(final VesselClass vc, final Route route) {
		final PricingModel pricingModel = scenario.getPricingModel();
		for (final RouteCost rc : pricingModel.getRouteCosts()) {
			if (rc.getRoute().equals(route) && rc.getVesselClass().equals(vc)) {
				return rc;
			}
		}
		return null;
	}

	public VesselClassRouteParameters getRouteParameters(final VesselClass vc, final Route route) {
		for (final VesselClassRouteParameters parameters : vc.getRouteParameters()) {
			if (parameters.getRoute().equals(route)) {
				return parameters;
			}
		}
		return null;
	}

}
