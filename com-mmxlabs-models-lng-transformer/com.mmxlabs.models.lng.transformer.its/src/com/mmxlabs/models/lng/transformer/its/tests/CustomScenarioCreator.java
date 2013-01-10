/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.FixedPriceContract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
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
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
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
	private final PricingModel pricingModel;
	private final OptimiserModel optimiserModel;

	final SalesContract sc;
	final SalesContract pc;
	final LegalEntity contractEntity;
	final LegalEntity shippingEntity;

	MMXRootObject scenario;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	// private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private static final String timeZone = TimeZone.getDefault().getID();

	public CustomScenarioCreator(final float dischargePrice) {
		scenario = ManifestJointModel.createEmptyInstance(null);

		commercialModel = scenario.getSubModel(CommercialModel.class);
		cargoModel = scenario.getSubModel(CargoModel.class);
		portModel = scenario.getSubModel(PortModel.class);
		fleetModel = scenario.getSubModel(FleetModel.class);
		pricingModel = scenario.getSubModel(PricingModel.class);
		optimiserModel = scenario.getSubModel(OptimiserModel.class);

		contractEntity = CommercialFactory.eINSTANCE.createLegalEntity();
		shippingEntity = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(contractEntity);
		commercialModel.getEntities().add(shippingEntity);
		commercialModel.setShippingEntity(shippingEntity);

		contractEntity.setName("Third-parties");
		shippingEntity.setName("Shipping");

		sc = addSalesContract("Sales Contract", dischargePrice);
		pc = addPurchaseContract("Purchase Contract");

		Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(ScenarioTools.defaultRouteName);
		portModel.getRoutes().add(r);

		// ScenarioUtils.addDefaultSettings(scenario);
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

		FleetCostModel fleetCostModel = pricingModel.getFleetCost();

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

		CharterCostModel charterCostModel = PricingFactory.eINSTANCE.createCharterCostModel();
		charterCostModel.setSpotCharterCount(spotCharterCount);
		// Costs
		charterCostModel.getVesselClasses().add(vc);

		fleetCostModel.getCharterCosts().add(charterCostModel);

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
		load.setContract(pc);
		dis.setContract(sc);
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

		cargoModel.getLoadSlots().add(load);
		cargoModel.getDischargeSlots().add(dis);
		cargoModel.getCargoes().add(cargo);

		return cargo;
	}

	public DryDockEvent addDryDock(final Port startPort, final Date start, final int durationDays) {

		if (!portModel.getPorts().contains(startPort)) {
			log.warn("Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway.", new RuntimeException());
			portModel.getPorts().add(startPort);
		}

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
		FixedPriceContract result = CommercialFactory.eINSTANCE.createFixedPriceContract();
		result.setName(name);

		result.setEntity(contractEntity);
		result.setPricePerMMBTU(dischargePrice);

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
	 */
	public SalesContract addPurchaseContract(String name) {
		FixedPriceContract result = CommercialFactory.eINSTANCE.createFixedPriceContract();
		result.setName(name);

		result.setEntity(contractEntity);

		commercialModel.getPurchaseContracts().add(result);
		
		return result;
	}
}
