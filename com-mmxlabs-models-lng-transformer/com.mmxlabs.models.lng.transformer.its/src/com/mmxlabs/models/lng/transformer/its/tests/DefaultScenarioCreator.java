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
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
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
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
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

	LNGScenarioModel scenario;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private static final String timeZone = TimeZone.getDefault().getID();

	public DefaultScenarioCreator() {
		scenario = ManifestJointModel.createEmptyInstance(null);
	}

	public Date addHours(Date date, int hours) {
		return new Date(date.getTime() + Timer.ONE_HOUR * hours);
	}

	/**
	 * @since 3.0
	 */
	public LegalEntity addEntity(String name) {
		final CommercialModel commercialModel = scenario.getCommercialModel();
		LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(entity);
		return entity;
	}

	/**
	 * @since 3.0
	 */
	public Route addRoute(String name) {
		final PortModel portModel = scenario.getPortModel();
		Route r = PortFactory.eINSTANCE.createRoute();
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

		public VesselStateAttributes createVesselStateAttributes(double minSpeed, double maxSpeed) {
			final VesselStateAttributes result = FleetFactory.eINSTANCE.createVesselStateAttributes();

			double[] speeds = { minSpeed, maxSpeed };

			int n = (minSpeed == maxSpeed ? 1 : 2);

			for (int i = 0; i < n; i++) {
				FuelConsumption fc = FleetFactory.eINSTANCE.createFuelConsumption();
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

		public VesselClassRouteParameters createVesselClassRouteParameters(Route route) {
			VesselClassRouteParameters result = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

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
			HeelOptions result = FleetFactory.eINSTANCE.createHeelOptions();
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

			BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
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

			DefaultVesselStateAttributesCreator dvsac = new DefaultVesselStateAttributesCreator();

			CharterCostModel charterCostModel = SpotMarketsFactory.eINSTANCE.createCharterCostModel();
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
		public Vessel createDefaultVessel(String name, VesselClass vc) {
			final FleetModel fleetModel = scenario.getFleetModel();
			LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
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
			Vessel[] result = new Vessel[num];
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
		public VesselAvailability setAvailability(ScenarioFleetModel scenarioFleetModel, Vessel vessel, Port startPort, Date startDate, Port endPort, Date endDate) {
			VesselAvailability availability = FleetFactory.eINSTANCE.createVesselAvailability();
			availability.getStartAt().add(startPort);
			availability.getEndAt().add(endPort);
			availability.setStartAfter(startDate);
			availability.setEndBy(endDate);
			availability.setVessel(vessel);
			scenarioFleetModel.getVesselAvailabilities().add(availability);
			return availability;
		}

		public void assignDefaultCanalData(VesselClass vc, Route route) {
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
		public VesselClassRouteParameters assignRouteParameters(VesselClass vc, Route route) {
			VesselClassRouteParameters result = new DefaultVesselClassRouteParametersCreator().createVesselClassRouteParameters(route);
			vc.getRouteParameters().add(result);
			return result;
		}

		public RouteCost setCanalCost(VesselClass vc, Route route, int ladenCost, int ballastCost) {
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

		public Route addCanal(String name) {
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
		public DefaultPortCreator(int baseDistanceMultiplier) {
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
		public void setDistance(Port p1, Port p2, int distance, Route r) {
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
		public void setOneWayDistance(Port p1, Port p2, int distance, Route r) {
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
		 * Creates a default list of distances between a new port and existing ports. The distances are multiples of the base distance multiplier supplied to the
		 * 
		 * @return
		 */
		private int[] createDefaultDistances() {
			final PortModel portModel = scenario.getPortModel();
			EList<Port> ports = portModel.getPorts();
			int[] result = new int[ports.size()];

			for (int i = 0; i < result.length; i++) {
				// TODO: make a more meaningful value here
				result[i] = (int) ((i + 1) * baseDistanceMultiplier);

			}
			return result;
		}

		public Port[] createDefaultPorts(int num) {
			Port[] result = new Port[num];
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

		public Cargo createDefaultLddCargo(String name, Port loadPort, Port dischargePort1, Port dischargePort2, Date loadTime, int travelTimeInHours1, int travelTimeInHours2) {
			Cargo result = CargoFactory.eINSTANCE.createCargo();
			final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			final DischargeSlot dischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			final DischargeSlot dischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = new Date(System.currentTimeMillis());
			}

			// if name is not specified, set it to a sensible default
			if (name == null) {
				String format = "Cargo from %s to %s in %d hrs to %s in %d hrs";
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
		public DataIndex<Double> createDefaultIndex(String name, double value) {
			final PricingModel pricingModel = scenario.getPricingModel();

			IndexPoint<Double> startPoint = PricingFactory.eINSTANCE.createIndexPoint();
			IndexPoint<Double> endPoint = PricingFactory.eINSTANCE.createIndexPoint();

			startPoint.setDate(new Date(0));
			startPoint.setValue(value);

			endPoint.setDate(new Date(Long.MAX_VALUE));
			endPoint.setValue(value);

			DataIndex<Double> result = PricingFactory.eINSTANCE.createDataIndex();

			result.getPoints().add(startPoint);
			result.getPoints().add(endPoint);

			pricingModel.getCommodityIndices().add(result);

			return result;
		}
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

	public Integer getTravelTime(Port p1, Port p2, Route r, int speed) {
		return getDistance(p1, p2, r) / speed;
	}

	public Integer getDistance(Port p1, Port p2, Route r) {
		if (r == null) {
			final PortModel portModel = scenario.getPortModel();
			r = portModel.getRoutes().get(0);
		}

		for (RouteLine line : r.getLines()) {
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
	public static void createCanalAndCost(LNGScenarioModel scenario, final String canalName, Port A, Port B, final int distanceAToB, final int distanceBToA, final int canalLadenCost,
			final int canalUnladenCost, final int canalTransitFuelDays, final int canalNBORateDays, final int canalTransitTime) {

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

		FleetModel fleetModel = scenario.getFleetModel();

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
			scenario.getPricingModel().getRouteCosts().add(rc2);
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
	public SalesContract addSalesContract(String name, float dischargePrice) {
		final CommercialModel commercialModel = scenario.getCommercialModel();
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
		final CommercialModel commercialModel = scenario.getCommercialModel();
		PurchaseContract result = CommercialFactory.eINSTANCE.createPurchaseContract();
		FixedPriceParameters params = CommercialFactory.eINSTANCE.createFixedPriceParameters();

		result.setName(name);

		result.setEntity(contractEntity);
		result.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(result);

		return result;
	}

	public void checkJourneyGeography(Journey journey, Port from, Port to) {
		Assert.assertEquals(journey.getPort(), from);
		Assert.assertEquals(journey.getDestination(), to);
		Route route = journey.getRoute();
		Assert.assertEquals((int) journey.getDistance(), (int) getDistance(from, to, route));
	}

	public RouteCost getRouteCost(VesselClass vc, Route route) {
		final PricingModel pricingModel = scenario.getPricingModel();
		for (RouteCost rc : pricingModel.getRouteCosts()) {
			if (rc.getRoute().equals(route) && rc.getVesselClass().equals(vc)) {
				return rc;
			}
		}
		return null;
	}

	public VesselClassRouteParameters getRouteParameters(VesselClass vc, Route route) {
		for (VesselClassRouteParameters parameters : vc.getRouteParameters()) {
			if (parameters.getRoute().equals(route)) {
				return parameters;
			}
		}
		return null;
	}

	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldown(double value) {
		final PricingModel pricingModel = scenario.getPricingModel();
		final PortModel portModel = scenario.getPortModel();

		final DataIndex<Double> cooldownIndex = pricingCreator.createDefaultIndex("cooldown", value);

		final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
		price.setIndex(cooldownIndex);

		for (Port port : portModel.getPorts()) {
			price.getPorts().add(port);
		}

		pricingModel.getCooldownPrices().add(price);
	}
}
