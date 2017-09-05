/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

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
 * 
 */
public class DefaultScenarioCreator {

	private static final Logger log = LoggerFactory.getLogger(DefaultScenarioCreator.class);
	public double dischargePrice = 1d;
	public double purchasePrice = 0.5d;

	// will need at least one sales contract and purchase contract for any completed transactions
	public SalesContract salesContract;
	public PurchaseContract purchaseContract;
	// will need a contract entity and a shipping entity
	public final @NonNull LegalEntity contractEntity;
	public final @NonNull LegalEntity shippingEntity;

	public final DefaultFleetCreator fleetCreator = new DefaultFleetCreator();
	public final DefaultPortCreator portCreator = new DefaultPortCreator();
	public final DefaultCargoCreator cargoCreator = new DefaultCargoCreator();
	public final DefaultPricingCreator pricingCreator = new DefaultPricingCreator();
	public final DefaultVesselEventCreator vesselEventCreator = new DefaultVesselEventCreator();

	public final @NonNull LNGScenarioModel scenario;

	public final @NonNull ScenarioModelBuilder scenarioModelBuilder;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	public final String timeZone;
	protected final @NonNull CommercialModelBuilder commercialModelBuilder;
	private final SpotMarketsModelBuilder spotMarketsModelBuilder;
	private final CargoModelBuilder cargoModelBuilder;

	SimpleScenarioDataProvider dataProvider;

	public DefaultScenarioCreator() {
		this("UTC");

	}

	public DefaultScenarioCreator(final String timeZone) {
		this.timeZone = timeZone;
		scenarioModelBuilder = ScenarioModelBuilder.instantiate();
		scenario = scenarioModelBuilder.getLNGScenarioModel();
		cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
		commercialModelBuilder = scenarioModelBuilder.getCommercialModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");

		dataProvider = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), scenario);
	}

	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return dataProvider;
	}

	/**
	 */
	public @NonNull LegalEntity addEntity(final @NonNull String name) {

		return commercialModelBuilder.makeLegalEntityAndBooks(name);
	}

	/**
	 */
	public Route addRoute(final RouteOption option) {
		return scenarioModelBuilder.getPortModelBuilder().createRoute(option.getName(), option);
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
		final int defaultSafetyHeelVolume = 0;

		final int defaultWarmupTime = Integer.MAX_VALUE;
		// Same as portcreator default CV
		final double defaultEquivalenceFactor = 21;
		final int cooldownVolume = 0;
		final double defaultFillCapacity = 1.0;
		final int startHeelVolume = 0;
		final int defaultSuezCanalCost = 1;
		final int defaultPanamaCanalCost = 1;

		public void setBaseFuelPrice(final BaseFuelCost bfc, final double price, final YearMonth date) {
			BaseFuelIndex bfi = bfc.getIndex();
			if (bfi == null) {
				bfi = PricingFactory.eINSTANCE.createBaseFuelIndex();
				bfc.setIndex(bfi);
				bfi.setName(bfc.getFuel().getName());
			}
			Index<Double> indexData = bfi.getData();
			if (indexData == null || (indexData instanceof DataIndex) == false) {
				indexData = PricingFactory.eINSTANCE.createDataIndex();
				bfi.setData(indexData);
			}

			final List<IndexPoint<Double>> points = ((DataIndex<Double>) indexData).getPoints();

			if (points.isEmpty()) {
				final IndexPoint<Double> point = PricingFactory.eINSTANCE.createIndexPoint();

				points.add(point);
			}

			points.get(0).setValue(price);
			points.get(0).setDate(date);
		}

		public void setBaseFuelPrice(final BaseFuelCost bfc, final double price) {
			setBaseFuelPrice(bfc, price, YearMonth.of(2000, 1));
		}

		/**
		 * Creates a base fuel with default settings.
		 * 
		 * @return
		 */
		public BaseFuel createDefaultBaseFuel() {
			final PricingModel pricingModel = scenario.getReferenceModel().getPricingModel();
			final CostModel costModel = scenario.getReferenceModel().getCostModel();
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();

			final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
			baseFuel.setName("BASE FUEL");
			baseFuel.setEquivalenceFactor(defaultEquivalenceFactor);
			fleetModel.getBaseFuels().add(baseFuel);

			final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
			bfc.setFuel(baseFuel);
			setBaseFuelPrice(bfc, baseFuelUnitPrice);
			costModel.getBaseFuelCosts().add(bfc);
			pricingModel.getBaseFuelPrices().add(bfc.getIndex());

			return baseFuel;
		}

		/**
		 * Creates a vessel class with default settings. If the name parameter is null, a default name is generated.
		 * 
		 * @param name
		 * @return
		 */
		public VesselClass createDefaultVesselClass(String name) {
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
			if (name == null) {
				final int n = fleetModel.getVesselClasses().size();
				name = "Vessel Class " + n;
			}

			final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
			fleetModel.getVesselClasses().add(vc);

			final DefaultVesselStateAttributesCreator dvsac = new DefaultVesselStateAttributesCreator();

			final CharterInMarket charterInMarket = spotMarketsModelBuilder.createCharterInMarket("market-" + vc.getName(), vc, "0", spotCharterCount);

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
			vc.setMinHeel(defaultSafetyHeelVolume);
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
		public VesselAvailability createDefaultVessel(final @NonNull String name, final @NonNull VesselClass vc, final @NonNull BaseLegalEntity shippingEntity) {
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
			final CargoModel cargoModel = scenario.getCargoModel();

			@NonNull
			final Vessel vessel = scenarioModelBuilder.getFleetModelBuilder().createVessel(name, vc);

			final VesselAvailability availability = scenarioModelBuilder.getCargoModelBuilder() //
					.makeVesselAvailability(vessel, shippingEntity) //
					.withStartHeel(0.0, (double) startHeelVolume, portCreator.defaultCv, Double.toString(dischargePrice)) //
					.build();

			return availability;
		}

		/**
		 * Creates multiple default vessels of the specified vessel class. Names are automatically generated.
		 * 
		 * @param vc
		 * @param num
		 * @return
		 */
		public VesselAvailability[] createMultipleDefaultVessels(final VesselClass vc, final int num, final @NonNull BaseLegalEntity shippingEntity) {
			final VesselAvailability[] result = new VesselAvailability[num];
			for (int i = 0; i < num; i++) {
				result[i] = createDefaultVessel(i + "(class " + vc.getName() + ")", vc, shippingEntity);
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
		public VesselAvailability setAvailability(final CargoModel cargoModel, final Vessel vessel, final Port startPort, final LocalDateTime startDate, final Port endPort,
				final LocalDateTime endDate) {
			for (final VesselAvailability availability : cargoModel.getVesselAvailabilities()) {
				if (availability.getVessel() == vessel) {
					availability.setStartAt(startPort);
					availability.getEndAt().add(endPort);
					availability.setStartAfter(startDate);
					availability.setEndBy(endDate);
					return availability;
				}
			}
			return null;
		}

		public void assignDefaultSuezCanalData(final VesselClass vc, final Route route) {
			assignRouteParameters(vc, route);
			setCanalCost(vc, route, defaultSuezCanalCost, defaultSuezCanalCost);
		}

		public void assignDefaultPanamaCanalData(final VesselClass vc, final Route route) {
			assignRouteParameters(vc, route);
			setCanalCost(vc, route, defaultPanamaCanalCost, defaultPanamaCanalCost);
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
			final CostModel costModel = scenario.getReferenceModel().getCostModel();

			final RouteCost result = PricingFactory.eINSTANCE.createRouteCost();
			result.setRoute(route);
			result.setLadenCost(ladenCost); // cost in dollars for a laden vessel
			result.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
			result.setVesselClass(vc);
			costModel.getRouteCosts().add(result);
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

		public Route addCanal(final RouteOption option) {
			final Route result = addRoute(option);
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
				final PortModel portModel = scenario.getReferenceModel().getPortModel();
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
			final PortModel portModel = scenario.getReferenceModel().getPortModel();

			// if not specified, set name to a sensible default value
			if (name == null) {
				final int n = portModel.getPorts().size();
				name = "Port " + n;
			}

			final Location location = PortFactory.eINSTANCE.createLocation();

			final Port result = PortFactory.eINSTANCE.createPort();
			result.setLocation(location);
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
				scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(ports.get(i), result, RouteOption.DIRECT, distances[i], true);

				setDistance(ports.get(i), result, distances[i], null);
			}

			// Add in all capabilities by default
			result.getCapabilities().add(PortCapability.LOAD);
			result.getCapabilities().add(PortCapability.DISCHARGE);
			result.getCapabilities().add(PortCapability.DRYDOCK);
			result.getCapabilities().add(PortCapability.MAINTENANCE);
			result.getCapabilities().add(PortCapability.TRANSFER);

			return result;
		}

		/**
		 * Creates a default list of distances between a new port and existing ports. The distances are multiples of the base distance multiplier supplied to the
		 * 
		 * @return
		 */
		private int[] createDefaultDistances() {
			final PortModel portModel = scenario.getReferenceModel().getPortModel();
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
			final CostModel costModel = scenario.getReferenceModel().getCostModel();

			final PortCost portCost = PricingFactory.eINSTANCE.createPortCost();
			final PortCostEntry portCostEntry = PricingFactory.eINSTANCE.createPortCostEntry();
			portCostEntry.setActivity(capability);
			portCostEntry.setCost(cost);
			portCost.getEntries().add(portCostEntry);
			portCost.getPorts().add(port);

			costModel.getPortCosts().add(portCost);
		}
	}

	public class DefaultCargoCreator {
		int defaultWindowSize = 0;

		public Cargo createDefaultCargo(final String name, final Port loadPort, final Port dischargePort, LocalDateTime loadTime, final int travelTimeInHours) {

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = LocalDateTime.of(2015, 5, 1, 0, 0);
			}

			final LocalDateTime dischargeTime = loadTime.plusHours(travelTimeInHours);
			return scenarioModelBuilder.getCargoModelBuilder() //
					.makeCargo()//
					//
					.makeFOBPurchase("load", loadTime.toLocalDate(), loadPort, purchaseContract, null, null) //
					.withWindowStartTime(loadTime.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.makeDESSale("discharge", dischargeTime.toLocalDate(), dischargePort, salesContract, null, null) //
					.withWindowStartTime(dischargeTime.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.build();
		}

		public Cargo createDefaultLddCargo(final String name, final Port loadPort, final Port dischargePort1, final Port dischargePort2, LocalDateTime loadTime, final int travelTimeInHours1,
				final int travelTimeInHours2) {

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = LocalDateTime.now();
			}

			final LocalDateTime dischargeTime1 = loadTime.plusHours(travelTimeInHours1);
			final LocalDateTime dischargeTime2 = loadTime.plusHours(travelTimeInHours1 + defaultWindowSize + travelTimeInHours2);
			return scenarioModelBuilder.getCargoModelBuilder() //
					.makeCargo()//
					//
					.makeFOBPurchase("load", loadTime.toLocalDate(), loadPort, purchaseContract, null, null) //
					.withWindowStartTime(loadTime.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.makeDESSale("discharge1", dischargeTime1.toLocalDate(), dischargePort1, salesContract, null, null) //
					.withWindowStartTime(dischargeTime1.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.makeDESSale("discharge2", dischargeTime2.toLocalDate(), dischargePort2, salesContract, null, null) //
					.withWindowStartTime(dischargeTime2.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.build();

		}
	}

	public class DefaultPricingCreator {
		public CommodityIndex createDefaultCommodityIndex(final String name, final Double value) {
			final DataIndex<Double> result = createIndex(value);
			final PricingModel pricingModel = scenario.getReferenceModel().getPricingModel();

			final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
			index.setName(name);
			index.setData(result);

			pricingModel.getCommodityIndices().add(index);

			return index;
		}

		private <T extends Number> DataIndex<T> createIndex(final T value) {
			final IndexPoint<T> startPoint = PricingFactory.eINSTANCE.createIndexPoint();
			final IndexPoint<T> endPoint = PricingFactory.eINSTANCE.createIndexPoint();

			startPoint.setDate(YearMonth.of(1000, 1));
			startPoint.setValue(value);

			endPoint.setDate(YearMonth.of(3000, 1));
			endPoint.setValue(value);

			final DataIndex<T> result = PricingFactory.eINSTANCE.createDataIndex();

			result.getPoints().add(startPoint);
			result.getPoints().add(endPoint);

			return result;
		}

		public CharterOutMarket createDefaultCharterCostModel(final VesselClass vc, final Integer minDuration, final Integer price, final EList<Port> ports) {
			final CharterOutMarket result = SpotMarketsFactory.eINSTANCE.createCharterOutMarket();
			result.setVesselClass(vc);
			result.setMinCharterOutDuration(minDuration);

			result.setCharterOutRate(Integer.toString(price));

			final EList<APortSet<Port>> cPorts = result.getAvailablePorts();
			for (final Port p : ports) {
				if (p.getName().equals("Port 2"))
					cPorts.add(p);
			}

			final SpotMarketsModel marketModel = scenario.getReferenceModel().getSpotMarketsModel();
			marketModel.getCharterOutMarkets().add(result);

			return result;
		}

		public PortCost setPortCost(final Port port, final PortCapability activity, final int cost) {
			final CostModel costModel = scenario.getReferenceModel().getCostModel();

			/*
			 * // abortive code checking for an existing port cost based on the port specified for (PortCost pc: pricingModel.getPortCosts()) { final EList<APortSet<Port>> ports = pc.getPorts(); if
			 * (ports.size() == 1) { final APortSet<Port> set = ports.get(0); if (set) } }
			 */
			final PortCost result = PricingFactory.eINSTANCE.createPortCost();
			result.getPorts().add(port);
			costModel.getPortCosts().add(result);

			final PortCostEntry entry = PricingFactory.eINSTANCE.createPortCostEntry();

			entry.setActivity(activity);
			entry.setCost(cost);

			result.getEntries().add(entry);

			return result;

		}
	}

	public class DefaultVesselEventCreator {
		int defaultDurationInDays = 1;

		public void addEventToModel(final VesselEvent event, final String name, final Port port, final LocalDateTime startByDate, final LocalDateTime startAfterDate) {
			event.setName(name);
			event.setPort(port);
			event.setDurationInDays(defaultDurationInDays);
			event.setStartBy(startByDate);
			event.setStartAfter(startAfterDate);
			final CargoModel cargoModel = scenario.getCargoModel();
			cargoModel.getVesselEvents().add(event);
		}

		public DryDockEvent createDryDockEvent(final String name, final Port port, final LocalDateTime startByDate, final LocalDateTime startAfterDate) {
			final DryDockEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
			addEventToModel(event, name, port, startAfterDate, startAfterDate);
			return event;
		}

		public MaintenanceEvent createMaintenanceEvent(final String name, final Port port, final LocalDateTime startByDate, final LocalDateTime startAfterDate) {
			final MaintenanceEvent event = CargoFactory.eINSTANCE.createMaintenanceEvent();
			addEventToModel(event, name, port, startAfterDate, startAfterDate);
			return event;
		}

		public CharterOutEvent createCharterOutEvent(final String name, final Port startPort, final Port endPort, final LocalDateTime startByDate, final LocalDateTime startAfterDate,
				final int hireRate) {
			final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
			addEventToModel(event, name, startPort, startAfterDate, startAfterDate);
			event.setHireRate(hireRate);
			event.setRelocateTo(endPort);

			event.setAvailableHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
			event.setRequiredHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

			return event;
		}
	}

	public int getTravelTime(final Port p1, final Port p2, final Route r, final int speed) {
		return getDistance(p1, p2, r.getRouteOption()) / speed;
	}

	public int getDistance(final Port p1, final Port p2, RouteOption routeOption) {

		if (p1 == p2) {
			return 0;
		}
		Route r = null;
		final PortModel portModel = scenario.getReferenceModel().getPortModel();
		for (Route route : portModel.getRoutes()) {
			if (route.getRouteOption() == routeOption) {
				r = route;
				break;
			}
		}
		if (r != null) {
			for (final RouteLine line : r.getLines()) {
				if (line.getFrom() == p1 && line.getTo() == p2) {
					return line.getFullDistance();
				}
			}
		}

		throw new NullPointerException();
	}

	public DryDockEvent addDryDock(final Port startPort, final LocalDateTime start, final int durationDays) {

		final PortModel portModel = scenario.getReferenceModel().getPortModel();
		if (!portModel.getPorts().contains(startPort)) {
			log.warn("Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(startPort);
		}

		final DryDockEvent event = scenarioModelBuilder.getCargoModelBuilder()//
				.makeDryDockEvent("Drydock", start, start, startPort) //
				.withDurationInDays(durationDays) //
				.build();

		return event;
	}

	public CharterOutEvent addCharterOut(final String id, final Port startPort, final Port endPort, final LocalDateTime startCharterOut, final int minHeelLimit, final int maxHeelLimit,
			final int charterOutDurationDays, final float cvValue, final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		final CharterOutEvent event = scenarioModelBuilder.getCargoModelBuilder()//
				.makeCharterOutEvent(id, startCharterOut, startCharterOut, startPort) //
				.withDurationInDays(charterOutDurationDays) //
				.withAvailableHeelOptions(minHeelLimit, maxHeelLimit, cvValue, Double.toString(dischargePrice)) //
				.withRepositioningFee(repositioningFee) //
				.withHireRate(dailyCharterOutPrice) //
				.build();

		// // don't set the end port if both ports are the same - this is equivalent to setting the end port to unset and is a good place to test it works
		if (endPort != null && !startPort.equals(endPort)) {
			event.setRelocateTo(endPort);
		}
		return event;
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

		final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
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

		final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
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

		final CargoModel cargoModel = scenario.getCargoModel();
		if (cargoModel.getCargoes().contains(cargo)) {

			for (final Cargo c : cargoModel.getCargoes()) {
				if (c.equals(cargo)) {
					for (final Slot s : c.getSlots()) {
						s.getAllowedVessels().addAll(allowedVessels);
					}
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
	public SalesContract addSalesContract(final String name, final double dischargePrice) {

		return commercialModelBuilder.makeExpressionSalesContract(name, contractEntity, Double.toString(dischargePrice));
	}

	/**
	 * Adds a purchase contract to the model.
	 * 
	 * @author Simon McGregor
	 * @param name
	 * @param purchasePrice
	 * @return
	 */
	public PurchaseContract addPurchaseContract(final String name, final double purchasePrice) {

		return commercialModelBuilder.makeExpressionPurchaseContract(name, contractEntity, Double.toString(purchasePrice));

	}

	public void checkJourneyGeography(final Journey journey, final Port from, final Port to) {
		Assert.assertEquals(journey.getPort(), from);
		Assert.assertEquals(journey.getDestination(), to);
		final Route route = journey.getRoute();
		Assert.assertEquals(journey.getDistance(), (int) getDistance(from, to, route.getRouteOption()));
	}

	public RouteCost getRouteCost(final VesselClass vc, final Route route) {
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		for (final RouteCost rc : costModel.getRouteCosts()) {
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

	public LocalDate createLocalDate(final int year, final int month, final int day) {
		return LocalDate.of(year, 1 + month, day);
	}

	public static LocalDateTime createLocalDateTime(final int year, final int month, final int day, final int hourOfDay) {
		return LocalDateTime.of(year, 1 + month, day, hourOfDay, 0);
	}

	public static YearMonth createYearMonth(final int year, final int month) {
		return YearMonth.of(year, 1 + month);
	}

	public int getTravelTime(final @NonNull Port p1, final @NonNull Port p2, final @NonNull RouteOption r, final int speed) {

		return getDistance(p1, p2, r) / speed;
	}
}
