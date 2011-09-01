/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import java.util.Date;

import javax.management.timer.Timer;

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

	@Test
	public void testLNGSelection() {
		// Create a dummy scenario
		final float baseFuelPrice = 700; // $/MT -- normal
		final float dischargePrice = 1.0f; // $/mmbtu - cheap
		final float cvValue = 22.8f;
		final int travelTime = 24 * 3;

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		print(a);

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
		final float baseFuelPrice = 0; // $/MT -- very cheap, normally around 700
		final float dischargePrice = 7000.0f; // $/mmbtu // very expensive, normally around 7
		final float cvValue = 22.8f;
		final int travelTime = 24 * 3;

		final Scenario scenario = createSimpleScenario(baseFuelPrice, dischargePrice, cvValue, travelTime);
		// evaluate and get a schedule
		final Schedule result = evaluate(scenario);
		// check result is how we expect it to be
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		print(a);

		// on the laden leg we always use NBO; decision time is on the ballast leg
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			Assert.assertTrue("Ballast leg only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}

		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			Assert.assertTrue("Ballast idle only uses base", fq.getQuantity() == 0 || fq.getFuelType() == FuelType.BASE_FUEL);
		}
	}

	/**
	 * @param a
	 */
	private void print(final CargoAllocation a) {
		System.err.println("Allocation " + ((Cargo) a.getLoadSlot().eContainer()).getId());
		System.err.println("Total LNG volume used for fuel: " + a.getFuelVolume() + "M3");
		System.err.println("Laden Leg:");
		for (final FuelQuantity fq : a.getLadenLeg().getFuelUsage()) {
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at " + fq.getTotalPrice());
		}

		System.err.println("Laden Idle:");
		for (final FuelQuantity fq : a.getLadenIdle().getFuelUsage()) {
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at " + fq.getTotalPrice());
		}

		System.err.println("Ballast Leg:");
		for (final FuelQuantity fq : a.getBallastLeg().getFuelUsage()) {
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at " + fq.getTotalPrice());
		}

		System.err.println("Ballast Idle:");
		for (final FuelQuantity fq : a.getBallastIdle().getFuelUsage()) {
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at " + fq.getTotalPrice());
		}
	}

	/**
	 * Evaluate the scenario and create a schedule with costs in it.
	 * 
	 * @param scenario
	 * @return the evaluated schedule
	 */
	private Schedule evaluate(final Scenario scenario) {
		final LNGScenarioTransformer transformer = new LNGScenarioTransformer(scenario);
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
		final Scenario scenario = ScenarioFactory.eINSTANCE.createScenario();
		scenario.createMissingModels();

		final VesselFuel baseFuel = FleetFactory.eINSTANCE.createVesselFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setUnitPrice(baseFuelUnitPrice);
		baseFuel.setEquivalenceFactor(0.5f);

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
		vc.setMinSpeed(12);
		vc.setMaxSpeed(20);
		vc.setCapacity(150000);
		vc.setPilotLightRate(0);
		vc.setCooldownTime(0);
		vc.setWarmupTime(Integer.MAX_VALUE);
		vc.setCooldownVolume(0);
		vc.setMinHeelVolume(0);
		vc.setSpotCharterCount(0);

		final FuelConsumptionLine ladenMin = FleetFactory.eINSTANCE.createFuelConsumptionLine();
		final FuelConsumptionLine ladenMax = FleetFactory.eINSTANCE.createFuelConsumptionLine();

		final FuelConsumptionLine ballastMin = FleetFactory.eINSTANCE.createFuelConsumptionLine();
		final FuelConsumptionLine ballastMax = FleetFactory.eINSTANCE.createFuelConsumptionLine();

		ballastMin.setSpeed(12);
		ballastMin.setConsumption(80);
		ballastMax.setSpeed(20);
		ballastMax.setConsumption(200);

		ladenMin.setSpeed(12);
		ladenMin.setConsumption(100);
		ladenMax.setSpeed(20);
		ladenMax.setConsumption(230);

		laden.getFuelConsumptionCurve().add(ladenMin);
		laden.getFuelConsumptionCurve().add(ladenMax);

		ballast.getFuelConsumptionCurve().add(ballastMin);
		ballast.getFuelConsumptionCurve().add(ballastMax);

		laden.setIdleConsumptionRate(15);
		laden.setIdleNBORate(180);
		laden.setNboRate(200);

		ballast.setIdleConsumptionRate(15);
		ballast.setIdleNBORate(100);
		ballast.setNboRate(180);

		vc.setFillCapacity(1.0);

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
		distance.setDistance(1000);

		// don't forget that distances can be asymmetric, so have to go both ways.
		final DistanceLine distance2 = PortFactory.eINSTANCE.createDistanceLine();
		distance2.setFromPort(B);
		distance2.setToPort(A);
		distance2.setDistance(1000);

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
		load.setFixedPrice(1000);

		load.setMaxQuantity(100000);
		dis.setMaxQuantity(100000);

		load.setCargoCVvalue(cvValue);

		final Date now = new Date();
		load.setWindowStart(new DateAndOptionalTime(now, false));
		final Date then = new Date(now.getTime() + Timer.ONE_HOUR * travelTime);
		dis.setWindowStart(new DateAndOptionalTime(then, false));

		cargo.setId("CARGO");

		scenario.getCargoModel().getCargoes().add(cargo);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
	}
}
