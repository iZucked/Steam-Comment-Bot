/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Class to create a scenario. Call methods to customise the scenario. When finished get the final scenario using {@link #buildScenario()}. <br>
 * The scenario can probably have more stuff added to it after getting it from {@link #buildScenario()}, but it could also break it, it's untested.
 * 
 * @author Adam Semenenko
 * 
 */
public class CustomScenarioCreator {

	private static final Logger log = LoggerFactory.getLogger(CustomScenarioCreator.class);

	private final CommercialModel commercialModel;
	private final CargoModel cargoModel;
	private final PortModel portModel;
	private final FleetModel fleetModel;
	private final CostModel costModel;
	private final PricingModel pricingModel;
	private final SpotMarketsModel spotMarketsModel;
	private final LNGReferenceModel referenceModel;

	private final ScenarioModelBuilder scenarioModelBuilder;

	final SalesContract sc;
	final PurchaseContract pc;
	final LegalEntity contractEntity;
	final LegalEntity shippingEntity;

	LNGScenarioModel scenario;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private String timeZone = TimeZone.getDefault().getID();

	public CustomScenarioCreator(final float dischargePrice, final String timeZone) {
		this.timeZone = timeZone;

		this.scenarioModelBuilder = ScenarioModelBuilder.instantiate();
		scenario = scenarioModelBuilder.getLNGScenarioModel();

		referenceModel = scenario.getReferenceModel();
		cargoModel = scenario.getCargoModel();

		portModel = referenceModel.getPortModel();
		fleetModel = referenceModel.getFleetModel();
		pricingModel = referenceModel.getPricingModel();
		costModel = referenceModel.getCostModel();
		commercialModel = referenceModel.getCommercialModel();
		spotMarketsModel = referenceModel.getSpotMarketsModel();

		contractEntity = scenarioModelBuilder.getCommercialModelBuilder().makeLegalEntityAndBooks("Third-parties");
		shippingEntity = scenarioModelBuilder.getCommercialModelBuilder().makeLegalEntityAndBooks("Shipping");

		final TaxRate taxRate = CommercialFactory.eINSTANCE.createTaxRate();
		taxRate.setDate(createLocalDate(2000, Calendar.JANUARY, 1));
		shippingEntity.getShippingBook().getTaxRates().add(EcoreUtil.copy(taxRate));
		shippingEntity.getTradingBook().getTaxRates().add(EcoreUtil.copy(taxRate));
		contractEntity.getShippingBook().getTaxRates().add(EcoreUtil.copy(taxRate));
		contractEntity.getTradingBook().getTaxRates().add(EcoreUtil.copy(taxRate));

		commercialModel.getEntities().add(contractEntity);
		commercialModel.getEntities().add(shippingEntity);

		sc = addSalesContract("Sales Contract", dischargePrice);
		pc = addPurchaseContract("Purchase Contract");

		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(RouteOption.DIRECT.getName());
		portModel.getRoutes().add(r);

		// ScenarioUtils.addDefaultSettings(scenario);
	}

	public CustomScenarioCreator(final float dischargePrice) {
		this(dischargePrice, "UTC");
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

		SpotMarketsModelBuilder spotMarketsModelBuilder = new SpotMarketsModelBuilder(spotMarketsModel);

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;

		final double fillCapacity = 1.0;

		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setEquivalenceFactor(equivalenceFactor);
		fleetModel.getBaseFuels().add(baseFuel);

		final BaseFuelCost bfc = ScenarioTools.createBaseFuelCost(baseFuel, baseFuelUnitPrice);
		costModel.getBaseFuelCosts().add(bfc);
		pricingModel.getBaseFuelPrices().add(bfc.getIndex());

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
		vc.setWarmingTime(warmupTime);
		vc.setCoolingVolume(cooldownVolume);
		vc.setMinHeel(minHeelVolume);
		vc.setFillCapacity(fillCapacity);

		final CharterInMarket charterInMarket = spotMarketsModelBuilder.createCharterInMarket("market-" + vc.getName(), vc, "0", spotCharterCount);
		// Make first created market the default one
		if (spotMarketsModel.getCharterInMarkets().size() == 1) {
			spotMarketsModel.setDefaultNominalMarket(charterInMarket);
		}
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
		if (ladenMinSpeed != ladenMaxSpeed) {
			laden.getFuelConsumption().add(ladenMax);
		}
		ballast.getFuelConsumption().add(ballastMin);
		if (ballastMinSpeed != ballastMaxSpeed) {
			ballast.getFuelConsumption().add(ballastMax);
		}

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

			final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();

			fleetModel.getVessels().add(vessel);
			final VesselAvailability availability = CargoFactory.eINSTANCE.createVesselAvailability();
			if (isTimeChartered) {
				availability.setTimeCharterRate("10");
			}
			availability.setVessel(vessel);
			availability.setStartHeel(heelOptions);
			availability.setEntity(shippingEntity);
			cargoModel.getVesselAvailabilities().add(availability);
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
	 * The ports must be created from the method {@link CustomScenarioCreator#createPort(String)} to be correctly added to the scenario.
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

		if (!portModel.getPorts().contains(portA)) {
			portModel.getPorts().add(portA);
			portA.setTimeZone(timeZone);
		}
		if (!portModel.getPorts().contains(portB)) {
			portModel.getPorts().add(portB);
			portB.setTimeZone(timeZone);
		}

		// Assuming we've created the default rout
		final Route r = portModel.getRoutes().get(0);

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
	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final String loadPrice, final String dischargePrice, final float cvValue,
			final LocalDateTime loadWindowStart, final int travelTime) {

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

		cargo.getSlots().add(load);
		cargo.getSlots().add(dis);

		load.setPort(loadPort);
		dis.setPort(dischargePort);
		load.setContract(pc);
		dis.setContract(sc);
		load.setName("load" + cargoID);
		dis.setName("discharge" + cargoID);

		dis.setPriceExpression(dischargePrice);
		load.setPriceExpression(loadPrice);

		load.setMaxQuantity(loadMaxQuantity);
		dis.setMaxQuantity(dischargeMaxQuantity);

		load.setCargoCV(cvValue);

		load.setWindowSize(0);

		final ZoneId dischargeZone = ZoneId.of(dischargePort.getTimeZone() == null || dischargePort.getTimeZone().isEmpty() ? "UTC" : dischargePort.getTimeZone());

		load.setWindowStart(loadWindowStart.toLocalDate());
		load.setWindowStartTime(loadWindowStart.getHour());

		final ZonedDateTime dischargeDate = load.getWindowStartWithSlotOrPortTime().withZoneSameInstant(dischargeZone).plusHours(travelTime);
		dis.setWindowStartTime(dischargeDate.getHour());
		dis.setWindowStart(dischargeDate.toLocalDate());
		dis.setWindowSize(0);

		dis.setPricingEvent(PricingEvent.START_DISCHARGE);

		cargoModel.getLoadSlots().add(load);
		cargoModel.getDischargeSlots().add(dis);
		cargoModel.getCargoes().add(cargo);

		return cargo;
	}

	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final int loadPrice, final float dischargePrice, final float cvValue,
			final LocalDateTime loadWindowStart, final int travelTime) {
		return addCargo(cargoID, loadPort, dischargePort, Float.toString(loadPrice), Float.toString(dischargePrice), cvValue, loadWindowStart, travelTime);
	}

	public DryDockEvent addDryDock(final Port startPort, final LocalDateTime start, final int durationDays) {

		if (!portModel.getPorts().contains(startPort)) {
			log.warn("Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(startPort);
		}

		// Set up dry dock.
		final DryDockEvent dryDock = CargoFactory.eINSTANCE.createDryDockEvent();
		dryDock.setName("Drydock");
		dryDock.setDurationInDays(durationDays);
		dryDock.setPort(startPort);
		// add to scenario's fleet model
		cargoModel.getVesselEvents().add(dryDock);

		// define the start and end time
		dryDock.setStartAfter(start);
		dryDock.setStartBy(start);

		return dryDock;
	}

	public CharterOutEvent addCharterOut(final String id, final Port startPort, final Port endPort, final LocalDateTime startCharterOut, final int heelLimit, final int charterOutDurationDays,
			final float cvValue, final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		final CharterOutEvent charterOut = CargoFactory.eINSTANCE.createCharterOutEvent();

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
		cargoModel.getVesselEvents().add(charterOut);

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
	 * @param canalOption
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
	public static void createCanalAndCost(final LNGScenarioModel scenario, final RouteOption canalOption, final Port A, final Port B, final int distanceAToB, final int distanceBToA,
			final int canalLadenCost, final int canalUnladenCost, final int canalTransitFuelDays, final int canalNBORateDays, final int canalTransitTime) {

		final Route canal = PortFactory.eINSTANCE.createRoute();
		// canal.setCanal(true);
		canal.setRouteOption(canalOption);
		scenario.getReferenceModel().getPortModel().getRoutes().add(canal);
		canal.setName(canalOption.getName());
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

		final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();

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
			scenario.getReferenceModel().getCostModel().getRouteCosts().add(rc2);
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
	public SalesContract addSalesContract(final String name, final float dischargePrice) {

		return scenarioModelBuilder.getCommercialModelBuilder().makeExpressionSalesContract(name, contractEntity, Float.toString(dischargePrice));
	}

	/**
	 * Adds a purchase contract to the model.
	 * 
	 * @author Simon McGregor
	 * @param name
	 * @param dischargePrice
	 * @return
	 */
	public PurchaseContract addPurchaseContract(final String name) {
		return scenarioModelBuilder.getCommercialModelBuilder().makeExpressionPurchaseContract(name, contractEntity, "0");
	}

	public CommodityIndex addCommodityIndex(final String name) {
		final CommodityIndex ci = PricingFactory.eINSTANCE.createCommodityIndex();
		ci.setName(name);
		pricingModel.getCommodityIndices().add(ci);
		final DataIndex<Double> di = PricingFactory.eINSTANCE.createDataIndex();
		ci.setData(di);
		return ci;
	}

	public void addDataToCommodity(final CommodityIndex ci, final YearMonth date, final double value) {
		final DataIndex<Double> di = (DataIndex<Double>) ci.getData();
		final List<IndexPoint<Double>> points = di.getPoints();
		final IndexPoint<Double> ip = PricingFactory.eINSTANCE.createIndexPoint();
		ip.setDate(date);
		ip.setValue(value);
		points.add(ip);
	}

	/**
	 * Sets up a cooldown pricing model including an index
	 */
	public void setupCooldown(final double value) {
		final PricingModel pricingModel = scenario.getReferenceModel().getPricingModel();
		final PortModel portModel = scenario.getReferenceModel().getPortModel();

		final DerivedIndex<Double> result = PricingFactory.eINSTANCE.createDerivedIndex();
		result.setExpression(Double.toString(value));

		final CommodityIndex index = PricingFactory.eINSTANCE.createCommodityIndex();
		index.setName("Cooldown");
		index.setData(result);

		pricingModel.getCommodityIndices().add(index);

		final CooldownPrice price = PricingFactory.eINSTANCE.createCooldownPrice();
		price.setExpression(index.getName());

		for (final Port port : portModel.getPorts()) {
			price.getPorts().add(port);
		}

		costModel.getCooldownCosts().add(price);
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

	public LNGReferenceModel getReferenceModel() {
		return referenceModel;
	}

	public LNGScenarioModel getScenario() {
		return scenario;
	}
}
