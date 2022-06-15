/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.types.util.SetUtils;
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

	public final String timeZone;
	protected final @NonNull CommercialModelBuilder commercialModelBuilder;
	private final SpotMarketsModelBuilder spotMarketsModelBuilder;

	SimpleScenarioDataProvider dataProvider;

	private final ModelDistanceProvider distanceProvider;

	public DefaultScenarioCreator() {
		this("UTC");

	}

	public DefaultScenarioCreator(final String timeZone) {
		this.timeZone = timeZone;
		scenarioModelBuilder = ScenarioModelBuilder.instantiate();
		scenario = scenarioModelBuilder.getLNGScenarioModel();
		commercialModelBuilder = scenarioModelBuilder.getCommercialModelBuilder();
		spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");

		dataProvider = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), scenario);
		distanceProvider = ModelDistanceProvider.getOrCreate(scenarioModelBuilder.getPortModelBuilder().getPortModel());
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
		scenarioModelBuilder.getDistanceModelBuilder().createDistanceMatrix(option);
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

			result.setFuelConsumptionOverride(true);
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

	public class DefaultVesselRouteParametersCreator {
		int defaultConsumptionRatePerHour = 10;
		int defaultNBORatePerHour = 5;
		int defaultConsumptionRatePerDay = TimeUnitConvert.convertPerHourToPerDay(defaultConsumptionRatePerHour);
		int defaultNBORatePerDay = TimeUnitConvert.convertPerHourToPerDay(defaultNBORatePerHour);
		int defaultTransitTimeInHours = 0;

		public VesselRouteParameters createVesselRouteParameters(final RouteOption routeOption) {
			final VesselRouteParameters result = FleetFactory.eINSTANCE.createVesselRouteParameters();

			result.setRouteOption(routeOption);
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
		public static final int baseFuelUnitPrice = 10;
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

		/**
		 * Creates a base fuel with default settings.
		 * 
		 * @return
		 */
		public BaseFuel createDefaultBaseFuel() {
			final CostModel costModel = scenario.getReferenceModel().getCostModel();
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();

			final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
			baseFuel.setName("BASE FUEL");
			baseFuel.setEquivalenceFactor(defaultEquivalenceFactor);
			fleetModel.getBaseFuels().add(baseFuel);

			final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
			bfc.setFuel(baseFuel);
			bfc.setExpression(Integer.toString(baseFuelUnitPrice));
			costModel.getBaseFuelCosts().add(bfc);

			return baseFuel;
		}

		/**
		 * Creates a vessel class with default settings. If the name parameter is null, a default name is generated.
		 * 
		 * @param name
		 * @return
		 */
		public Vessel createDefaultVessel(String name) {
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();
			if (name == null) {
				final int n = fleetModel.getVessels().size();
				name = "Vessel " + n;
			}

			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			fleetModel.getVessels().add(vessel);

			final BaseFuel baseFuel = createDefaultBaseFuel();

			final DefaultVesselStateAttributesCreator dvsac = new DefaultVesselStateAttributesCreator();

			spotMarketsModelBuilder.createCharterInMarket("market-" + vessel.getName(), vessel, shippingEntity, "0", spotCharterCount);

			vessel.setLadenAttributes(dvsac.createVesselStateAttributes(defaultMinSpeed, defaultMaxSpeed));
			vessel.setBallastAttributes(dvsac.createVesselStateAttributes(defaultMinSpeed, defaultMaxSpeed));
			vessel.setName(name);

			vessel.setBaseFuel(baseFuel);
			vessel.setInPortBaseFuel(baseFuel);
			vessel.setIdleBaseFuel(baseFuel);
			vessel.setPilotLightBaseFuel(baseFuel);

			vessel.setMinSpeed(defaultMinSpeed);
			vessel.setMaxSpeed(defaultMaxSpeed);
			vessel.setCapacity(defaultCapacity);
			vessel.setPilotLightRate(defaultPilotLightRate);
			vessel.setWarmingTime(defaultWarmupTime);
			vessel.setCoolingVolume(cooldownVolume);
			vessel.setSafetyHeel(defaultSafetyHeelVolume);
			vessel.setFillCapacity(defaultFillCapacity);

			return vessel;
		}

		/**
		 * Creates a vessel of the specified vessel class with default parameters.
		 * 
		 * @param name
		 * @param vc
		 * @return
		 */
		public VesselCharter createDefaultVessel(final @NonNull Vessel vessel, final @NonNull BaseLegalEntity shippingEntity) {

			final VesselCharter availability = scenarioModelBuilder.getCargoModelBuilder() //
					.makeVesselCharter(vessel, shippingEntity) //
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
		public VesselCharter[] createMultipleDefaultVessels(final Vessel vc, final int num, final @NonNull BaseLegalEntity shippingEntity) {
			final VesselCharter[] result = new VesselCharter[num];
			for (int i = 0; i < num; i++) {
				result[i] = createDefaultVessel(vc, shippingEntity);
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
		public VesselCharter setAllVesselCharterWindows(final CargoModel cargoModel, final Vessel vessel, final Port startPort, final LocalDateTime startDate, final Port endPort,
				final LocalDateTime endDate) {
			for (final VesselCharter vesselCharter : cargoModel.getVesselCharters()) {
				if (vesselCharter.getVessel() == vessel) {
					vesselCharter.setStartAt(startPort);
					vesselCharter.getEndAt().add(endPort);
					vesselCharter.setStartAfter(startDate);
					vesselCharter.setEndBy(endDate);
					return vesselCharter;
				}
			}
			return null;
		}

		public void assignDefaultSuezCanalData(final Vessel vc, final RouteOption routeOption) {
			assignRouteParameters(vc, routeOption);
			setCanalCost(vc, routeOption, defaultSuezCanalCost, defaultSuezCanalCost);
		}

		public void assignDefaultPanamaCanalData(final Vessel vc, final RouteOption routeOption) {
			assignRouteParameters(vc, routeOption);
			setCanalCost(vc, routeOption, defaultPanamaCanalCost, defaultPanamaCanalCost);
		}

		/**
		 * Assigns default route parameters to the specified vessel on the specified route.
		 * 
		 * @param vc
		 * @param route
		 * @return
		 */
		public VesselRouteParameters assignRouteParameters(final Vessel vc, final RouteOption routeOption) {
			final VesselRouteParameters result = new DefaultVesselRouteParametersCreator().createVesselRouteParameters(routeOption);
			vc.setRouteParametersOverride(true);
			vc.getRouteParameters().add(result);
			return result;
		}

		public RouteCost setCanalCost(final Vessel vc, final RouteOption routeOption, final int ladenCost, final int ballastCost) {
			final CostModel costModel = scenario.getReferenceModel().getCostModel();

			final RouteCost result = PricingFactory.eINSTANCE.createRouteCost();
			result.setRouteOption(routeOption);
			result.setLadenCost(ladenCost); // cost in dollars for a laden vessel
			result.setBallastCost(ballastCost); // cost in dollars for a ballast vessel
			result.getVessels().add(vc);
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
			location.setTimeZone(timeZone);
			location.setName(name);
			location.setMmxId(name);

			final Port result = PortFactory.eINSTANCE.createPort();
			result.setLocation(location);
			result.setName(name);
			result.setCvValue(defaultCv);
			// TODO: initialise other parameters with explicit defaults

			if (distances == null) {
				distances = createDefaultDistances();
			}

			final EList<Port> ports = portModel.getPorts();
			ports.add(result);

			for (int i = 0; i < distances.length; i++) {
				scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(ports.get(i), result, RouteOption.DIRECT, distances[i], true);
				// setDistance(ports.get(i), result, distances[i], null);
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

		public Cargo createDefaultCargo(@NonNull String prefix, final Port loadPort, final Port dischargePort, LocalDateTime loadTime, final int travelTimeInHours) {

			// if load time is not specified, set it to the current datetime
			if (loadTime == null) {
				loadTime = LocalDateTime.of(2015, 5, 1, 0, 0);
			}

			final LocalDateTime dischargeTime = loadTime.plusHours(travelTimeInHours);
			return scenarioModelBuilder.getCargoModelBuilder() //
					.makeCargo()//
					//
					.makeFOBPurchase(prefix + "load", loadTime.toLocalDate(), loadPort, purchaseContract, null, null) //
					.withWindowStartTime(loadTime.getHour()) //
					.withWindowSize(defaultWindowSize, TimePeriod.HOURS) //
					.withVolumeLimits(0, Integer.MAX_VALUE, VolumeUnits.M3) //
					.build() //
					//
					.makeDESSale(prefix +  "discharge", dischargeTime.toLocalDate(), dischargePort, salesContract, null, null) //
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
				loadTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
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
		public CommodityCurve createDefaultCommodityIndex(final String name, final Double value) {
			final PricingModel pricingModel = scenario.getReferenceModel().getPricingModel();

			final CommodityCurve curve = PricingFactory.eINSTANCE.createCommodityCurve();
			createIndex(curve, value);
			curve.setName(name);

			pricingModel.getCommodityCurves().add(curve);

			return curve;
		}

		private void createIndex(AbstractYearMonthCurve curve, final double value) {
			final YearMonthPoint startPoint = PricingFactory.eINSTANCE.createYearMonthPoint();
			final YearMonthPoint endPoint = PricingFactory.eINSTANCE.createYearMonthPoint();

			startPoint.setDate(YearMonth.of(1000, 1));
			startPoint.setValue(value);

			endPoint.setDate(YearMonth.of(3000, 1));
			endPoint.setValue(value);

			curve.getPoints().add(startPoint);
			curve.getPoints().add(endPoint);

		}

		public CharterOutMarket createDefaultCharterCostModel(final Vessel vessel, final Integer minDuration, final Integer price, final EList<Port> ports) {
			final CharterOutMarket result = SpotMarketsFactory.eINSTANCE.createCharterOutMarket();
			result.getVessels().add(vessel);
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

			event.setAvailableHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
			event.setRequiredHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

			return event;
		}
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
	 * @param vessel
	 *            The vessel class to add the constraint to.
	 * @param inaccessiblePorts
	 *            The ports to make inaccessible.
	 * @return If the ports are added successfully the method will return true. If the vessel class is not in the scenario it will return false.
	 */
	public void addInaccessiblePortsOnVessel(final Vessel vessel, final Port[] inaccessiblePorts) {
		vessel.setInaccessiblePortsOverride(true);
		vessel.getInaccessiblePorts().addAll(Arrays.asList(inaccessiblePorts));
	}

	protected boolean scenarioFleetModelContainsVessel(final Vessel vessel) {
		return scenarioModelBuilder.getFleetModelBuilder().getFleetModel().getVessels().contains(vessel);

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
	public boolean addAllowedVesselsOnCargo(final Cargo cargo, final List<Vessel> allowedVessels) {

		final CargoModel cargoModel = scenario.getCargoModel();
		if (cargoModel.getCargoes().contains(cargo)) {

			for (final Cargo c : cargoModel.getCargoes()) {
				if (c == cargo) {
					for (final Slot<?> s : c.getSlots()) {
						s.getRestrictedVessels().addAll(allowedVessels);
						s.setRestrictedVesselsArePermissive(true);
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
		Assertions.assertEquals(journey.getPort(), from);
		Assertions.assertEquals(journey.getDestination(), to);
		final RouteOption routeOption = journey.getRouteOption();
		Assertions.assertEquals(journey.getDistance(), getDistance(from, to, routeOption));
	}

	private int getDistance(final Port from, final Port to, final RouteOption routeOption) {
		return distanceProvider.getDistance(from, to, routeOption);
	}

	public RouteCost getRouteCost(final Vessel vessel, final RouteOption routeOption) {
		final CostModel costModel = scenario.getReferenceModel().getCostModel();
		for (final RouteCost rc : costModel.getRouteCosts()) {

			if (rc.getRouteOption() == routeOption) {
				for (AVesselSet<Vessel> v : rc.getVessels()) {
					if (v == vessel) {
						return rc;
					}
				}
			}
		}
		for (final RouteCost rc : costModel.getRouteCosts()) {

			if (rc.getRouteOption() == routeOption) {
				for (Vessel v : SetUtils.getObjects(rc.getVessels())) {
					if (v == vessel) {
						return rc;
					}
				}
			}
		}
		return null;
	}

	public VesselRouteParameters getRouteParameters(final Vessel vc, final RouteOption routeOption) {
		for (final VesselRouteParameters parameters : vc.getVesselOrDelegateRouteParameters()) {
			if (parameters.getRouteOption() == routeOption) {
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
