/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.contract.ContractFactory;
import scenario.contract.Entity;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.fleet.FleetFactory;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortTimeAndHeel;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.StepwisePriceCurve;
import scenario.port.DistanceLine;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ResourcelessModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.ScenarioUtils;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * This class contains some whole-system tests which check the fuel choices on a single cargo.
 * 
 * TODO test canal selection; make fuel choice values more precise (find tipping points on paper and write test)
 * 
 * @author hinton
 * 
 */
public class SimpleCalculationTests {

	// Create a dummy scenario
	private static final float baseFuelPriceCheap = 0; // $/MT -- very cheap, normally around 700
	private static final float baseFuelPriceNormal = 700; // $/MT -- normal
	private static final float baseFuelPriceExpensive = 70000; // $/MT
	private static final float dischargePriceCheap = 1.0f; // $/mmbtu - cheap
	private static final float dischargePriceNormal = 7.0f; // $/mmbtu // normal
	private static final float dischargePriceExpensive = 7000.0f; // $/mmbtu // very expensive, normally around 7

	/**
	 * A simple test. Attributes for the laden and ballast legs are identical.
	 */
	@Test
	public void testSimple() {
		// Create a dummy scenario
		final float baseFuelUnitPrice = 1;
		final float dischargePrice = 2;
		final float cvValue = 1;
		final int travelTime = 100;

		final float equivalenceFactor = 1;
		// for simplicity all speeds, fuel and NBO consumptions and rates are equal
		final int speed = 10;
		final int capacity = 1000000;

		final int fuelConsumption = 10 * (int) TimeUnit.DAYS.toHours(1);
		final int NBORate = 10 * (int) TimeUnit.DAYS.toHours(1);

		final int portDistance = 1000;

		final Scenario scenario = createScenario(portDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed, capacity, speed, fuelConsumption, speed,
				fuelConsumption, fuelConsumption, NBORate, NBORate, speed, fuelConsumption, speed, fuelConsumption, fuelConsumption, NBORate, NBORate);
		// evaluate and get a schedule
		final Schedule result = evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		print("testSimple", a);
	}

	@Test
	public void testLNGSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceNormal; // $/MT -- normal
		final float dischargePrice = dischargePriceCheap; // $/mmbtu - cheap
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		print("testLNGSelection", a);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			Assert.assertTrue("Ballast leg never uses base", fq.getQuantity() == 0 || fq.getFuelType() != FuelType.BASE_FUEL);
		}

		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			Assert.assertTrue("Ballast idle never uses base", fq.getQuantity() == 0 || fq.getFuelType() != FuelType.BASE_FUEL);
		}
	}

	@Test
	public void testBaseSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = baseFuelPriceCheap; // $/MT -- very cheap, normally around 700
		final float dischargePrice = dischargePriceExpensive; // $/mmbtu // very expensive, normally around 7
		final float cvValue = 22.8f;
		final int travelTime = (int) TimeUnit.DAYS.toHours(3);

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		print("testBaseSelection", a);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			Assert.assertTrue("Ballast leg only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}

		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			Assert.assertTrue("Ballast idle only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}
	}
	/**
	 * Evaluate the scenario and create a schedule with costs in it.
	 * 
	 * @param scenario
	 * @return the evaluated schedule
	 */
	private Schedule evaluate(final Scenario scenario) {
		// final LNGScenarioTransformer transformer = new LNGScenarioTransformer(scenario);
		final LNGScenarioTransformer lst = new LNGScenarioTransformer(scenario);

		final ModelEntityMap entities = new ResourcelessModelEntityMap();
		final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

		IOptimisationData<ISequenceElement> data;
		try {
			data = lst.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			// Wrap up exception
			throw new RuntimeException(e);
		}

		final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot.createOptimiserAndContext(data, entities);

		final IOptimisationContext<ISequenceElement> context = optAndContext.getFirst();
		final LocalSearchOptimiser<ISequenceElement> optimiser = optAndContext.getSecond();

		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor<ISequenceElement>());

		optimiser.init();
		final IAnnotatedSolution<ISequenceElement> startSolution = optimiser.start(context);

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, startSolution);

		return schedule;
	}

	/**
	 * Create a simple scenario which contains a vessel + vessel class a couple of ports and a canal, etc.
	 * 
	 * Most tests will call this, and then change some price parameters in the result
	 * 
	 * @param dischargePrice
	 * 
	 * @return
	 */
	private Scenario createSimpleScenario(final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final float equivalenceFactor = 0.5f;
		final int minSpeed = 12;
		final int maxSpeed = 20;
		final int capacity = 150000;
		final int pilotLightRate = 0;
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final int minHeelVolume = 0;
		final int spotCharterCount = 0;
		final double fillCapacity = 1.0;
		// ballast
		final int ballastMinSpeed = 12;
		final int ballastMinConsumption = 80;
		final int ballastMaxSpeed = 20;
		final int ballastMaxConsumption = 200;
		final int ballastIdleConsumptionRate = 15;
		final int ballastIdleNBORate = 100;
		final int ballastNBORate = 180;
		// laden
		final int ladenMinSpeed = 12;
		final int ladenMinConsumption = 100;
		final int ladenMaxSpeed = 20;
		final int ladenMaxConsumption = 230;
		final int ladenIdleConsumptionRate = 15;
		final int ladenIdleNBORate = 180;
		final int ladenNBORate = 200;
		// ports
		final int distanceBetweenPorts = 1000;
		// load and discharge prices and quantities
		final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		return createScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed, ballastMinConsumption,
				ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed, ladenMaxConsumption,
				ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate);
	}

	private Scenario createScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int pilotLightRate = 0;
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final int minHeelVolume = 0;
		final int spotCharterCount = 0;
		final double fillCapacity = 1.0;
		// ports
		final int distanceFromAToB = distanceBetweenPorts;
		final int distanceFromBToA = distanceBetweenPorts;
		// load and discharge prices and quantities
		final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final Scenario scenario = ScenarioFactory.eINSTANCE.createScenario();
		scenario.createMissingModels();

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

		vc.setName("Vessel Class");
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

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setClass(vc);
		vessel.setName("Vessel");

		final PortTimeAndHeel start = FleetFactory.eINSTANCE.createPortTimeAndHeel();
		final PortAndTime end = FleetFactory.eINSTANCE.createPortAndTime();

		vessel.setStartRequirement(start);
		vessel.setEndRequirement(end);

		scenario.getFleetModel().getFleet().add(vessel);

		final Port A = PortFactory.eINSTANCE.createPort();
		final Port B = PortFactory.eINSTANCE.createPort();

		A.setName("A");
		B.setName("B");

		scenario.getPortModel().getPorts().add(A);
		scenario.getPortModel().getPorts().add(B);

		final DistanceLine distance = PortFactory.eINSTANCE.createDistanceLine();
		distance.setFromPort(A);
		distance.setToPort(B);
		distance.setDistance(distanceFromAToB);

		// don't forget that distances can be asymmetric, so have to go both ways.
		final DistanceLine distance2 = PortFactory.eINSTANCE.createDistanceLine();
		distance2.setFromPort(B);
		distance2.setToPort(A);
		distance2.setDistance(distanceFromBToA);

		scenario.getDistanceModel().getDistances().add(distance);
		scenario.getDistanceModel().getDistances().add(distance2);

		final Entity e = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().getEntities().add(e);
		final Entity s = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().setShippingEntity(s);

		e.setName("Other");
		s.setName("Shipping");

		final SalesContract sc = ContractFactory.eINSTANCE.createSalesContract();
		final PurchaseContract pc = ContractFactory.eINSTANCE.createFixedPricePurchaseContract();

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

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		final Slot dis = CargoFactory.eINSTANCE.createSlot();

		cargo.setLoadSlot(load);
		cargo.setDischargeSlot(dis);

		load.setPort(A);
		dis.setPort(B);
		load.setContract(pc);
		dis.setContract(sc);
		load.setId("load");
		dis.setId("discharge");

		dis.setFixedPrice(dischargePrice);
		load.setFixedPrice(loadPrice);

		load.setMaxQuantity(loadMaxQuantity);
		dis.setMaxQuantity(dischargeMaxQuantity);

		load.setCargoCVvalue(cvValue);

		final Date now = new Date();
		load.setWindowStart(new DateAndOptionalTime(now, false));
		load.setWindowDuration(0);
		final Date then = new Date(now.getTime() + Timer.ONE_HOUR * travelTime);
		dis.setWindowStart(new DateAndOptionalTime(then, false));
		dis.setWindowDuration(0);

		cargo.setId("CARGO");

		scenario.getCargoModel().getCargoes().add(cargo);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
	}

	/**
	 * @param a
	 */
	private void print(final String testName, final CargoAllocation a) {
		System.err.println(testName);
		System.err.println("Allocation " + ((Cargo) a.getLoadSlot().eContainer()).getId());
		System.err.println("Total LNG volume used for fuel: " + a.getFuelVolume() + "M3");

		printJourney("Laden Leg", a.getLadenLeg());
		printIdle("Laden Idle", a.getLadenIdle());
		printJourney("Ballast Leg", a.getBallastLeg());
		printIdle("Ballast Idle", a.getBallastIdle());
	}

	private void printJourney(final String journeyName, final Journey journey) {

		System.err.println(journeyName + ":");
		System.err.println("\tDuration:" + journey.getEventDuration());
		System.err.println("\tSpeed:" + journey.getSpeed());

		printFuel(journey.getFuelUsage());
	}

	private void printIdle(final String idleName, final Idle idle) {

		System.err.println(idleName + ":");
		System.err.println("\tDuration:" + idle.getEventDuration());
		printFuel(idle.getFuelUsage());
	}

	private void printFuel(EList<FuelQuantity> fuelQuantities) {

		for (final FuelQuantity fq : fuelQuantities)
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at ¤" + fq.getTotalPrice());
	}

}
