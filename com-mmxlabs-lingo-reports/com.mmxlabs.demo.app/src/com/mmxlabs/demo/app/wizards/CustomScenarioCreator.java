/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.demo.app.wizards;

import java.util.ArrayList;
import java.util.Date;

import javax.management.timer.Timer;

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
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.market.Index;
import scenario.market.MarketFactory;
import scenario.market.StepwisePriceCurve;
import scenario.port.DistanceLine;
import scenario.port.Port;
import scenario.port.PortFactory;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.lngscheduler.emf.extras.ScenarioUtils;

/**
 * Class to create a scenario. Methods will customise the scenario.
 * <br>
 * Untested, probably broken currently.
 * 
 * @author Adam Semenenko
 * 
 */
public class CustomScenarioCreator {

	private final Scenario scenario;

	final SalesContract sc;
	final PurchaseContract pc;

	private ArrayList<VesselClass> vesselClasses = new ArrayList<VesselClass>();
	private ArrayList<VesselClassCost> canalCosts = new ArrayList<VesselClassCost>();

	public CustomScenarioCreator(final float dischargePrice) {

		scenario = ScenarioFactory.eINSTANCE.createScenario();
		scenario.createMissingModels();

		final Entity e = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().getEntities().add(e);
		final Entity s = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().setShippingEntity(s);

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

	public void addVesselSimple(final String vesselClassName, final int numOfVesselsToCreate, final float baseFuelUnitPrice, final int speed, final int capacity, final int consumption,
			final int NBORate, final int pilotLightRate, final int minHeelVolume) {
		
		final float equivalenceFactor = 1;

		addVessel(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, equivalenceFactor, speed, speed, capacity, speed, consumption, speed, consumption, consumption, NBORate, NBORate, speed,
				consumption, speed, consumption, consumption, NBORate, NBORate, pilotLightRate, minHeelVolume);
	}

	public void addVessel(final String vesselClassName, final int numOfVesselsToCreate, final float baseFuelUnitPrice, final float equivalenceFactor, final int minSpeed, final int maxSpeed,
			final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed, final int ballastMaxConsumption, int ballastIdleConsumptionRate,
			final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption, final int ladenMaxSpeed, final int ladenMaxConsumption,
			int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final int pilotLightRate, final int minHeelVolume) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;
		final int spotCharterCount = 0;
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

		// now create vessels of this class
		for (int i = 0; i < numOfVesselsToCreate; i++) {
			final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
			vessel.setClass(vc);
			vessel.setName(i + " (class " + vesselClassName + ")");

			// TODO Not sure if this should be here?
			final PortTimeAndHeel start = FleetFactory.eINSTANCE.createPortTimeAndHeel();
			final PortAndTime end = FleetFactory.eINSTANCE.createPortAndTime();

			vessel.setStartRequirement(start);
			vessel.setEndRequirement(end);

			scenario.getFleetModel().getFleet().add(vessel);
		}
	}

	public void addCanal(final VesselClassCost canalCost) {
		if (canalCost != null)
			canalCosts.add(canalCost);
	}

	public void addPorts(final Port portA, final Port portB, final int[] distancesSymmetric) {
		addPorts(portA, portB, distancesSymmetric, distancesSymmetric);
	}

	public void addPorts(final Port portA, final Port portB, final int[] AtoBDistances, final int[] BtoADistances) {
		scenario.getPortModel().getPorts().add(portA);
		scenario.getPortModel().getPorts().add(portB);

		for (int distance : AtoBDistances) {
			final DistanceLine distanceLine = PortFactory.eINSTANCE.createDistanceLine();
			distanceLine.setFromPort(portA);
			distanceLine.setToPort(portB);
			distanceLine.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distanceLine);
		}

		for (int distance : BtoADistances) {
			final DistanceLine distanceLine = PortFactory.eINSTANCE.createDistanceLine();
			distanceLine.setFromPort(portB);
			distanceLine.setToPort(portA);
			distanceLine.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distanceLine);
		}
	}

	public void addCargo(final Port loadPort, final Port dischargePort, final float dischargePrice, final float cvValue, final int travelTime) {

		final int loadPrice = 1000;
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

		final Date now = new Date();
		load.setWindowStart(new DateAndOptionalTime(now, false));
		load.setWindowDuration(0);
		final Date dischargeDate = new Date(now.getTime() + Timer.ONE_HOUR * travelTime);
		dis.setWindowStart(new DateAndOptionalTime(dischargeDate, false));
		dis.setWindowDuration(0);

		cargo.setId("CARGO");

		scenario.getCargoModel().getCargoes().add(cargo);
	}

	public Scenario buildScenario() {

		for (VesselClassCost canalCost : canalCosts) {
			scenario.getCanalModel().getCanals().add(canalCost.getCanal());
			for (VesselClass vc : vesselClasses)
				vc.getCanalCosts().add(canalCost);
		}

		return scenario;
	}
}
