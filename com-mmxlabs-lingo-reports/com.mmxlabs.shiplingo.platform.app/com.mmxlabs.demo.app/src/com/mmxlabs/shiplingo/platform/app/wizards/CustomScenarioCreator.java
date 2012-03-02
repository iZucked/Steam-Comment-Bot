/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.app.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.UUIDObject;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.ContractFactory;
import scenario.contract.Entity;
import scenario.contract.GroupEntity;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.FleetFactory;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortTimeAndHeel;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.StepwisePriceCurve;
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.lngscheduler.emf.extras.ScenarioUtils;
import com.mmxlabs.shiplingo.platform.app.Activator;

/**
 * Class to create a scenario. Call methods to customise the scenario. When finished get the final scenario using {@link #buildScenario()}. <br>
 * The scenario can probably have more stuff added to it after getting it from {@link #buildScenario()}, but it could also break it, it's untested.
 * 
 * @author Adam Semenenko
 * 
 */
public class CustomScenarioCreator {

	/** The scenario being built */
	private final Scenario scenario;

	final SalesContract sc;
	final PurchaseContract pc;

	/** A list of canal costs that will be added to every class of vessel when the scenario is retrieved for use. */
	private final ArrayList<VesselClassCost> canalCostsForAllVesselClasses = new ArrayList<VesselClassCost>();

	private static final String timeZone = TimeZone.getDefault().getID();

	public CustomScenarioCreator(final float dischargePrice) {

		scenario = ScenarioFactory.eINSTANCE.createScenario();
		scenario.createMissingModels();

		final Entity e = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().getEntities().add(e);
		final GroupEntity s = ContractFactory.eINSTANCE.createGroupEntity();
		scenario.getContractModel().setShippingEntity(s);
		s.setOwnership(1.0);
		s.setTaxRate(0.0);
		s.setTransferOffset(0);

		e.setName("Other");
		s.setName("Shipping");

		sc = ContractFactory.eINSTANCE.createSalesContract();
		pc = ContractFactory.eINSTANCE.createFixedPricePurchaseContract();

		final Index sales = MarketFactory.eINSTANCE.createIndex();
		sales.setName("Sales");
		final StepwisePriceCurve curve = MarketFactory.eINSTANCE.createStepwisePriceCurve();
		sales.setPriceCurve(curve);
		curve.setDefaultValue(dischargePrice);

		scenario.getMarketModel().getIndices().add(sales);

		sc.setEntity(e);
		pc.setEntity(e);
		sc.setIndex(sales);

		scenario.getContractModel().getSalesContracts().add(sc);
		scenario.getContractModel().getPurchaseContracts().add(pc);

		ScenarioUtils.addDefaultSettings(scenario);
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

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;

		final double fillCapacity = 1.0;

		final VesselFuel baseFuel = FleetFactory.eINSTANCE.createVesselFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setUnitPrice(baseFuelUnitPrice);
		baseFuel.setEquivalenceFactor(equivalenceFactor);
		scenario.getFleetModel().getFuels().add(baseFuel);

		final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vc.setLadenAttributes(laden);
		vc.setBallastAttributes(ballast);
		laden.setVesselState(VesselState.LADEN);
		ballast.setVesselState(VesselState.BALLAST);

		scenario.getFleetModel().getVesselClasses().add(vc);

		vc.setName(vesselClassName);
		vc.setBaseFuel(baseFuel);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(maxSpeed);
		vc.setCapacity(capacity);
		vc.setPilotLightRate(pilotLightRate);
		vc.setCooldownTime(cooldownTime);
		vc.setWarmupTime(warmupTime);
		vc.setCooldownVolume(cooldownVolume);
		vc.setMinHeelVolume(minHeelVolume);
		vc.setSpotCharterCount(spotCharterCount);
		vc.setFillCapacity(fillCapacity);

		final FuelConsumptionLine ladenMin = FleetFactory.eINSTANCE.createFuelConsumptionLine();
		final FuelConsumptionLine ladenMax = FleetFactory.eINSTANCE.createFuelConsumptionLine();

		final FuelConsumptionLine ballastMin = FleetFactory.eINSTANCE.createFuelConsumptionLine();
		final FuelConsumptionLine ballastMax = FleetFactory.eINSTANCE.createFuelConsumptionLine();

		ballastMin.setSpeed(ballastMinSpeed);
		ballastMin.setConsumption(ballastMinConsumption);
		ballastMax.setSpeed(ballastMaxSpeed);
		ballastMax.setConsumption(ballastMaxConsumption);

		ladenMin.setSpeed(ladenMinSpeed);
		ladenMin.setConsumption(ladenMinConsumption);
		ladenMax.setSpeed(ladenMaxSpeed);
		ladenMax.setConsumption(ladenMaxConsumption);

		laden.getFuelConsumptionCurve().add(ladenMin);
		laden.getFuelConsumptionCurve().add(ladenMax);

		ballast.getFuelConsumptionCurve().add(ballastMin);
		ballast.getFuelConsumptionCurve().add(ballastMax);

		laden.setIdleConsumptionRate(ladenIdleConsumptionRate);
		laden.setIdleNBORate(ladenIdleNBORate);
		laden.setNboRate(ladenNBORate);

		ballast.setIdleConsumptionRate(ballastIdleConsumptionRate);
		ballast.setIdleNBORate(ballastIdleNBORate);
		ballast.setNboRate(ballastNBORate);

		// return a list of all vessels created.
		final Vessel created[] = new Vessel[numOfVesselsToCreate];

		// now create vessels of this class
		for (int i = 0; i < numOfVesselsToCreate; i++) {
			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			vessel.setClass(vc);
			vessel.setName(i + " (class " + vesselClassName + ")");

			vessel.setTimeChartered(isTimeChartered);

			final PortTimeAndHeel start = FleetFactory.eINSTANCE.createPortTimeAndHeel();
			final PortAndTime end = FleetFactory.eINSTANCE.createPortAndTime();

			vessel.setStartRequirement(start);
			vessel.setEndRequirement(end);

			scenario.getFleetModel().getFleet().add(vessel);
			created[i] = vessel;
		}

		return created;
	}

	/**
	 * Add a canal to all vessel classes.
	 */
	public void addCanal(final VesselClassCost canalCost) {

		canalCostsForAllVesselClasses.add(canalCost);
	}

	/**
	 * Add a canal to specific vessel class.
	 */
	public void addCanal(final VesselClass vc, final VesselClassCost canalCost) {
		vc.getCanalCosts().add(canalCost);
	}

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
		if (!scenario.getPortModel().getPorts().contains(portA)) {
			scenario.getPortModel().getPorts().add(portA);
			portA.setTimeZone(timeZone);
		}
		if (!scenario.getPortModel().getPorts().contains(portB)) {
			scenario.getPortModel().getPorts().add(portB);
			portB.setTimeZone(timeZone);
		}

		for (final int distance : AtoBDistances) {
			final DistanceLine distanceLine = PortFactory.eINSTANCE.createDistanceLine();
			distanceLine.setFromPort(portA);
			distanceLine.setToPort(portB);
			distanceLine.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distanceLine);
		}

		for (final int distance : BtoADistances) {
			final DistanceLine distanceLine = PortFactory.eINSTANCE.createDistanceLine();
			distanceLine.setFromPort(portB);
			distanceLine.setToPort(portA);
			distanceLine.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distanceLine);
		}
	}

	/**
	 * Add a cargo to the scenario. <br>
	 * Both the load and discharge ports must be added using {@link #addPorts(Port, Port, int[], int[])} to correctly set up distances.
	 */
	public Cargo addCargo(final String cargoID, final Port loadPort, final Port dischargePort, final int loadPrice, final float dischargePrice, final float cvValue, final Date loadWindowStart,
			final int travelTime) {

		if (!scenario.getPortModel().getPorts().contains(loadPort)) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
							"Scenario does not contain load port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway."));
			scenario.getPortModel().getPorts().add(loadPort);
		}
		if (!scenario.getPortModel().getPorts().contains(dischargePort)) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
							"Scenario does not contain discharge port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway."));
			scenario.getPortModel().getPorts().add(dischargePort);
		}

		// final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		final Slot dis = CargoFactory.eINSTANCE.createSlot();

		cargo.setLoadSlot(load);
		cargo.setDischargeSlot(dis);

		load.setPort(loadPort);
		dis.setPort(dischargePort);
		load.setContract(pc);
		dis.setContract(sc);
		load.setId("load");
		dis.setId("discharge");

		dis.setFixedPrice(dischargePrice);
		load.setFixedPrice(loadPrice);

		load.setMaxQuantity(loadMaxQuantity);
		dis.setMaxQuantity(dischargeMaxQuantity);

		load.setCargoCVvalue(cvValue);

		load.setWindowStart(new DateAndOptionalTime(loadWindowStart, false));
		load.setWindowDuration(0);
		final Date dischargeDate = new Date(loadWindowStart.getTime() + (Timer.ONE_HOUR * travelTime));
		dis.setWindowStart(new DateAndOptionalTime(dischargeDate, false));
		dis.setWindowDuration(0);

		cargo.setId(cargoID);

		scenario.getCargoModel().getCargoes().add(cargo);

		return cargo;
	}

	public Drydock addDryDock(final Port startPort, final Date start, final int durationDays) {

		if (!scenario.getPortModel().getPorts().contains(startPort)) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
							"Scenario does not contain start port. Ports should be added using addPorts to correctly set distances. Adding port to scenario anyway."));
			scenario.getPortModel().getPorts().add(startPort);
		}

		// Set up dry dock.
		final Drydock dryDock = FleetFactory.eINSTANCE.createDrydock();
		dryDock.setDuration(durationDays);
		dryDock.setStartPort(startPort);
		// add to scenario's fleet model
		scenario.getFleetModel().getVesselEvents().add(dryDock);

		// define the start and end time
		dryDock.setStartDate(start);
		dryDock.setEndDate(start);

		return dryDock;
	}

	public CharterOut addCharterOut(final String id, final Port startPort, final Port endPort, final Date startCharterOut, final int heelLimit, final int charterOutDurationDays, final float cvValue,
			final float dischargePrice, final int dailyCharterOutPrice, final int repositioningFee) {

		final CharterOut charterOut = FleetFactory.eINSTANCE.createCharterOut();

		// the start and end of the charter out starting-window is 0, for simplicity.
		charterOut.setStartDate(startCharterOut);
		charterOut.setEndDate(startCharterOut);

		charterOut.setStartPort(startPort);
		// don't set the end port if both ports are the same - this is equivalent to setting the end port to unset and is a good place to test it works
		if (!startPort.equals(endPort)) {
			charterOut.setEndPort(endPort);
		}

		charterOut.setId(id);
		charterOut.setHeelLimit(heelLimit);
		charterOut.setDuration(charterOutDurationDays);
		charterOut.setHeelCVValue(cvValue);
		charterOut.setHeelUnitPrice(dischargePrice);
		charterOut.setDailyCharterOutPrice(dailyCharterOutPrice);
		charterOut.setRepositioningFee(repositioningFee);
		// add to the scenario's fleet model
		scenario.getFleetModel().getVesselEvents().add(charterOut);

		return charterOut;
	}

	/**
	 * Finish making the scenario by adding the canals to the vessel classes.
	 * 
	 * @return The finished scenario.
	 */
	public Scenario buildScenario() {

		// Add every canal to every vessel class.
		for (final VesselClassCost canalCost : this.canalCostsForAllVesselClasses) {
			for (final VesselClass vc : scenario.getFleetModel().getVesselClasses()) {
				addCanal(vc, canalCost);
			}
		}

		fixUUIDMisMatches(scenario);

		return scenario;
	}

	/**
	 * When copying a scenario the UUIDs are not copied correctly unless the getter method is called. The method checks whether there is a value and makes a new one if there isn't
	 * 
	 * @param scenario
	 *            The scenario to fix.
	 */
	private static void fixUUIDMisMatches(final Scenario scenario) {

		final TreeIterator<EObject> iterator = scenario.eAllContents();
		while (iterator.hasNext()) {
			final EObject obj = iterator.next();

			if (obj instanceof UUIDObject) {
				((UUIDObject) obj).getUUID();
			}
		}
	}

	public VesselClassCost createCanalCost(final String canalName, final Port portA, final Port portB, final int distanceAToB, final int distanceBToA, final int canalLadenCost,
			final int canalUnladenCost, final int canalTransitFuelDays, final int canalTransitTime) {

		if (!scenario.getPortModel().getPorts().contains(portA)) {
			scenario.getPortModel().getPorts().add(portA);
		}
		if (!scenario.getPortModel().getPorts().contains(portB)) {
			scenario.getPortModel().getPorts().add(portB);
		}

		final Canal canal = PortFactory.eINSTANCE.createCanal();
		canal.setName(canalName);
		final DistanceModel canalDistances = PortFactory.eINSTANCE.createDistanceModel();
		canal.setDistanceModel(canalDistances);
		// add distance lines, as for the main distance model:
		final DistanceLine atob = PortFactory.eINSTANCE.createDistanceLine();
		atob.setFromPort(portA);
		atob.setToPort(portB);
		atob.setDistance(distanceAToB);

		final DistanceLine btoa = PortFactory.eINSTANCE.createDistanceLine();
		btoa.setFromPort(portB);
		btoa.setToPort(portA);
		btoa.setDistance(distanceBToA);

		canalDistances.getDistances().add(atob);
		canalDistances.getDistances().add(btoa);

		// next do canal costs
		final VesselClassCost canalCost = FleetFactory.eINSTANCE.createVesselClassCost();
		canalCost.setCanal(canal);
		canalCost.setLadenCost(canalLadenCost); // cost in dollars for a laden vessel
		canalCost.setUnladenCost(canalUnladenCost); // cost in dollars for a ballast vessel
		canalCost.setTransitFuel(canalTransitFuelDays); // MT of base fuel / day used when in transit
		canalCost.setTransitTime(canalTransitTime); // transit time in hours

		// add the canal cost to the scenario.
		scenario.getCanalModel().getCanals().add(canalCost.getCanal());

		return canalCost;
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

			for (final VesselClass v : scenario.getFleetModel().getVesselClasses()) {
				if (v.equals(vc)) {
					v.getInaccessiblePorts().addAll(Arrays.asList(inaccessiblePorts));
				}

				return true;
			}
		}
		return false;
	}

	private boolean scenarioFleetModelContainsVesselClass(final VesselClass vc) {

		for (final Vessel v : scenario.getFleetModel().getFleet()) {
			if (v.getClass_().equals(vc)) {
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

		if (scenario.getCargoModel().getCargoes().contains(cargo)) {

			for (final Cargo c : scenario.getCargoModel().getCargoes()) {
				if (c.equals(cargo)) {
					c.getAllowedVessels().addAll(allowedVessels);

					return true;
				}
			}
		}
		return false;

	}
}
