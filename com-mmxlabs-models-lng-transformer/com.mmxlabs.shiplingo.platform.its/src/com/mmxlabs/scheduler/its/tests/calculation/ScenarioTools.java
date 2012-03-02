/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.calculation;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.ScenarioFactory;
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
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;
import com.mmxlabs.lngscheduler.emf.extras.LNGScenarioTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.OptimisationTransformer;
import com.mmxlabs.lngscheduler.emf.extras.ResourcelessModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.ScenarioUtils;
import com.mmxlabs.lngscheduler.emf.extras.contracts.SimpleContractTransformer;
import com.mmxlabs.lngscheduler.emf.extras.export.AnnotatedSolutionExporter;
import com.mmxlabs.lngscheduler.emf.extras.inject.LNGTransformerModule;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;

/**
 * Methods for printing and creating a scenario where a ship travels from port A to port B then back to port A.
 * 
 * @author Adam Semenenko
 * 
 */
public class ScenarioTools {

	private static final Port A = PortFactory.eINSTANCE.createPort();
	private static final Port B = PortFactory.eINSTANCE.createPort();
	static {
		A.setName("A");
		B.setName("B");
	}

	// The default route name of the ocean route between ports.
	public static final String defaultRouteName = IMultiMatrixProvider.Default_Key;

	/**
	 * Creates a scenario.
	 * 
	 * @param distanceBetweenPorts
	 * @param baseFuelUnitPrice
	 * @param dischargePrice
	 * @param cvValue
	 * @param travelTime
	 * @param equivalenceFactor
	 * @param minSpeed
	 * @param maxSpeed
	 * @param capacity
	 * @param ballastMinSpeed
	 * @param ballastMinConsumption
	 * @param ballastMaxSpeed
	 * @param ballastMaxConsumption
	 * @param ballastIdleConsumptionRate
	 * @param ballastIdleNBORate
	 * @param ballastNBORate
	 * @param ladenMinSpeed
	 * @param ladenMinConsumption
	 * @param ladenMaxSpeed
	 * @param ladenMaxConsumption
	 * @param ladenIdleConsumptionRate
	 * @param ladenIdleNBORate
	 * @param ladenNBORate
	 * @param useDryDock
	 * @param pilotLightRate
	 * @return
	 */
	public static Scenario createScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume) {

		return createScenarioWithCanals(new int[] { distanceBetweenPorts }, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume, null);

	}

	/**
	 * Same as
	 * {@link #createScenarioWithCanal(int[], float, float, float, int, float, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, boolean, int, int, VesselClassCost)}
	 * but only has argument for one distance between the two ports.
	 */
	public static Scenario createScenarioWithCanal(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume, final VesselClassCost canalCost) {
		return createScenarioWithCanals(new int[] { distanceBetweenPorts }, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume, new VesselClassCost[] { canalCost });

	}

	/**
	 * Creates a scenario.
	 * 
	 * @param distancesBetweenPorts
	 *            An array with distances using ocean routes between ports A and B
	 * @param baseFuelUnitPrice
	 * @param dischargePrice
	 * @param cvValue
	 * @param travelTime
	 * @param equivalenceFactor
	 * @param minSpeed
	 * @param maxSpeed
	 * @param capacity
	 * @param ballastMinSpeed
	 * @param ballastMinConsumption
	 * @param ballastMaxSpeed
	 * @param ballastMaxConsumption
	 * @param ballastIdleConsumptionRate
	 *            This will be set to ballastIdleNBORate if it is larger (code can't cope otherwise).
	 * @param ballastIdleNBORate
	 * @param ballastNBORate
	 * @param ladenMinSpeed
	 * @param ladenMinConsumption
	 * @param ladenMaxSpeed
	 * @param ladenMaxConsumption
	 * @param ladenIdleConsumptionRate
	 *            This will be set to ladenIdleNBORate if it is larger (code can't cope otherwise).
	 * @param ladenIdleNBORate
	 * @param ladenNBORate
	 * @param useDryDock
	 *            Use a dry dock, otherwise there is 15 days of ballast idle.
	 * @param canalCost
	 *            If this is not null a canal is added. If it is null no canal is added.
	 * @return
	 */
	public static Scenario createScenarioWithCanals(final int[] distancesBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume, final VesselClassCost[] canalCosts) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		// final int minHeelVolume = 0;
		final int spotCharterCount = 0;
		final double fillCapacity = 1.0;
		// load and discharge prices and quantities
		final int loadPrice = 1000;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		// idle consumption will never be more than the idle NBO rate, so clamp the idle consumptions
		if (ballastIdleConsumptionRate > ballastIdleNBORate) {
			System.err.println("Warning: had to clamp ballast idle consumption rate");
			ballastIdleConsumptionRate = ballastIdleNBORate;
		}
		if (ladenIdleConsumptionRate > ladenIdleNBORate) {
			System.err.println("Warning: had to clamp laden idle consumption rate");
			ladenIdleConsumptionRate = ladenIdleNBORate;
		}

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

		// if given a canals with canal costs, add them to the scenario.
		if (canalCosts != null) {
			for (final VesselClassCost canalCost : canalCosts) {
				if (canalCost != null) {
					// add the canal to the scenario
					scenario.getCanalModel().getCanals().add(canalCost.getCanal());

					vc.getCanalCosts().add(canalCost);
				}
			}
		}

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

		scenario.getPortModel().getPorts().add(A);
		scenario.getPortModel().getPorts().add(B);

		for (final int distance : distancesBetweenPorts) {
			final DistanceLine distanceLine = PortFactory.eINSTANCE.createDistanceLine();
			distanceLine.setFromPort(A);
			distanceLine.setToPort(B);
			distanceLine.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distanceLine);

			// don't forget that distances can be asymmetric, so have to go both ways.
			final DistanceLine distancLineBack = PortFactory.eINSTANCE.createDistanceLine();
			distancLineBack.setFromPort(B);
			distancLineBack.setToPort(A);
			distancLineBack.setDistance(distance);
			scenario.getDistanceModel().getDistances().add(distancLineBack);
		}

		final Entity e = ContractFactory.eINSTANCE.createEntity();
		scenario.getContractModel().getEntities().add(e);
		final GroupEntity s = ContractFactory.eINSTANCE.createGroupEntity();
		scenario.getContractModel().setShippingEntity(s);

		e.setName("Other");
		s.setName("Shipping");
		s.setOwnership(1.0);
		s.setTaxRate(0.0);
		s.setTransferOffset(0);

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
		final Date dischargeDate = new Date(now.getTime() + (Timer.ONE_HOUR * travelTime));
		dis.setWindowStart(new DateAndOptionalTime(dischargeDate, false));
		dis.setWindowDuration(0);

		if (useDryDock) {
			// Set up dry dock.
			final Drydock dryDock = FleetFactory.eINSTANCE.createDrydock();
			dryDock.setDuration(0);
			dryDock.setStartPort(A);
			// add to scenario's fleet model
			scenario.getFleetModel().getVesselEvents().add(dryDock);
			// set the date to be after the discharge date
			final Date thenNext = new Date(dischargeDate.getTime() + (Timer.ONE_HOUR * travelTime));
			dryDock.setStartDate(thenNext);
			dryDock.setEndDate(thenNext);
		}

		cargo.setId("CARGO");

		scenario.getCargoModel().getCargoes().add(cargo);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
	}

	/**
	 * Creates a scenario with a charter out.
	 */
	public static Scenario createCharterOutScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final int pilotLightRate,
			final int charterOutTimeDays, final int heelLimit) {

		// 'magic' numbers that could be set in the arguments.
		// vessel class
		final int cooldownTime = 0;
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final int minHeelVolume = 0;
		final int spotCharterCount = 0;
		final double fillCapacity = 1.0;
		// ports
		final int distanceFromAToB = distanceBetweenPorts;
		final int distanceFromBToA = distanceBetweenPorts;

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
		final GroupEntity s = ContractFactory.eINSTANCE.createGroupEntity();
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

		final Date startCharterOut = new Date();
		final Date endCharterOut = new Date(startCharterOut.getTime() + TimeUnit.DAYS.toMillis(charterOutTimeDays));
		final Date dryDockJourneyStartDate = new Date(endCharterOut.getTime() + TimeUnit.HOURS.toMillis(travelTime));

		final CharterOut charterOut = FleetFactory.eINSTANCE.createCharterOut();
		charterOut.setStartDate(startCharterOut);
		charterOut.setEndDate(startCharterOut);
		// same start and end port.
		charterOut.setStartPort(A);
		charterOut.setEndPort(A);
		charterOut.setId("Charter Out");
		charterOut.setHeelLimit(heelLimit);
		charterOut.setDuration(charterOutTimeDays);
		charterOut.setHeelCVValue(cvValue);
		charterOut.setHeelUnitPrice(dischargePrice);
		charterOut.setDailyCharterOutPrice(0);
		charterOut.setRepositioningFee(0);
		// add to the scenario's fleet model
		scenario.getFleetModel().getVesselEvents().add(charterOut);

		// Set up dry dock to cause journey
		final Drydock dryDockJourney = FleetFactory.eINSTANCE.createDrydock();
		dryDockJourney.setDuration(0);
		dryDockJourney.setStartPort(B);
		// set the date to be after the charter out date
		dryDockJourney.setStartDate(dryDockJourneyStartDate);
		dryDockJourney.setEndDate(dryDockJourneyStartDate);
		// add to scenario's fleet model
		scenario.getFleetModel().getVesselEvents().add(dryDockJourney);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
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
	public static VesselClassCost createCanalAndCost(final String canalName, final int distanceAToB, final int distanceBToA, final int canalLadenCost, final int canalUnladenCost,
			final int canalTransitFuelDays, final int canalTransitTime) {

		final Canal canal = PortFactory.eINSTANCE.createCanal();
		canal.setName(canalName);
		final DistanceModel canalDistances = PortFactory.eINSTANCE.createDistanceModel();
		canal.setDistanceModel(canalDistances);
		// add distance lines, as for the main distance model:
		final DistanceLine atob = PortFactory.eINSTANCE.createDistanceLine();
		atob.setFromPort(A);
		atob.setToPort(B);
		atob.setDistance(distanceAToB);

		final DistanceLine btoa = PortFactory.eINSTANCE.createDistanceLine();
		btoa.setFromPort(B);
		btoa.setToPort(A);
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

		return canalCost;
	}

	/**
	 * Evaluate the scenario and create a schedule with costs in it.
	 * 
	 * @param scenario
	 * @return the evaluated schedule
	 */
	public static Schedule evaluate(final Scenario scenario) {
		// final LNGScenarioTransformer transformer = new LNGScenarioTransformer(scenario);
		final LNGScenarioTransformer lst = LNGTransformerModule.createLNGScenarioTransformer(scenario);

		final ModelEntityMap entities = new ResourcelessModelEntityMap();
		final OptimisationTransformer ot = new OptimisationTransformer(lst.getOptimisationSettings());

		if (!lst.addPlatformTransformerExtensions()) {
			// add extensions manually; TODO improve this later.
			final SimpleContractTransformer sct = new SimpleContractTransformer();
			lst.addTransformerExtension(sct);
			lst.addContractTransformer(sct, sct.getContractEClasses());
		}

		IOptimisationData data;
		try {
			data = lst.createOptimisationData(entities);
		} catch (final IncompleteScenarioException e) {
			// Wrap up exception
			throw new RuntimeException(e);
		}

		final Pair<IOptimisationContext, LocalSearchOptimiser> optAndContext = ot.createOptimiserAndContext(data, entities);

		final IOptimisationContext context = optAndContext.getFirst();
		final LocalSearchOptimiser optimiser = optAndContext.getSecond();

		optimiser.setProgressMonitor(new NullOptimiserProgressMonitor());

		optimiser.init();
		final IAnnotatedSolution startSolution = optimiser.start(context);

		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		// TODO addd trading extension?
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, startSolution);

		return schedule;
	}

	/**
	 * Print a cargo allocation's details.
	 * 
	 * @param testName
	 *            The name of the test being performed (to aid with identification in the console).
	 * @param a
	 *            The cargo allocation to print to console.
	 */
	public static void printCargoAllocation(final String testName, final CargoAllocation a) {
		System.err.println(testName);
		System.err.println("Allocation " + ((Cargo) a.getLoadSlot().eContainer()).getId());
		System.err.println("Total cost: " + a.getTotalCost() + ", Total LNG volume used for fuel: " + a.getFuelVolume() + "M3");

		if (a.getLadenLeg() != null) {
			printJourney("Laden Leg", a.getLadenLeg());
		}
		if (a.getLadenIdle() != null) {
			printIdle("Laden Idle", a.getLadenIdle());
		}
		if (a.getBallastLeg() != null) {
			printJourney("Ballast Leg", a.getBallastLeg());
		}
		if (a.getBallastIdle() != null) {
			printIdle("Ballast Idle", a.getBallastIdle());
		}
	}

	/**
	 * Print a journey, including the name, duration, speed, and fuel usage.
	 * 
	 * @param journeyName
	 * @param journey
	 */
	private static void printJourney(final String journeyName, final Journey journey) {

		System.err.println(journeyName + ":");
		System.err.println("\tRoute: " + journey.getRoute() + ", Distance: " + journey.getDistance() + ", Duration: " + journey.getEventDuration() + ", Speed: " + journey.getSpeed());
		printFuel(journey.getFuelUsage());
		System.err.println("\tRoute cost: $" + journey.getRouteCost() + ", Total cost: $" + journey.getTotalCost());
	}

	/**
	 * Print details of an idle.
	 * 
	 * @param idleName
	 * @param idle
	 */
	private static void printIdle(final String idleName, final Idle idle) {

		System.err.println(idleName + ":");
		System.err.println("\tDuration: " + idle.getEventDuration());
		printFuel(idle.getFuelUsage());
		System.err.println("\tTotal cost: $" + idle.getTotalCost());
	}

	/**
	 * Print the details of fuel used for a section of a voyage.
	 * 
	 * @param fuelQuantities
	 */
	public static void printFuel(final EList<FuelQuantity> fuelQuantities) {

		for (final FuelQuantity fq : fuelQuantities) {
			System.err.println("\t" + fq.getFuelType() + " " + fq.getQuantity() + fq.getFuelUnit() + " at $" + fq.getTotalPrice());
		}
	}

	public static void printSequences(final Schedule result) {

		int i = 1;
		for (final Sequence seq : result.getSequences()) {
			System.err.println("*** Sequence number " + i++ + ":");
			ScenarioTools.printSequence(seq);
		}

	}

	public static void printSequence(final Sequence seq) {

		for (final ScheduledEvent e : seq.getEvents()) {

			if (e instanceof Idle) {

				final Idle i = (Idle) e;

				/**
				 * If you have a vessel which doesn't go anywhere at all and has no start/end conditions it will have an idle event with a null port.
				 */
				final String portName = i.getPort() == null ? "null" : i.getPort().getName();

				System.err.println("Idle:");
				System.err.println("\tvessel state: " + i.getVesselState() + ", Duration: " + i.getEventDuration() + ", port: " + portName);
				ScenarioTools.printFuel(i.getFuelUsage());

			} else if (e instanceof CharterOutVisit) {

				final CharterOutVisit cov = (CharterOutVisit) e;
				System.err.println("Charter Out:");
				System.err.println("\tID: " + cov.getId() + ", end port: " + cov.getPort().getName());
				System.err.println("\tDuration: " + cov.getEventDuration());

			} else if (e instanceof VesselEventVisit) {

				final VesselEventVisit vev = (VesselEventVisit) e;
				System.err.println("VesselEventVisit:");
				System.err.println("\tDuration: " + vev.getEventDuration());

			} else if (e instanceof Journey) {

				final Journey j = (Journey) e;
				System.err.println("Journey:");
				System.err.println("\tDuration: " + j.getEventDuration() + ", distance: " + j.getDistance() + ", destination: " + j.getToPort().getName());

				ScenarioTools.printFuel(j.getFuelUsage());

			} else if (e instanceof SlotVisit) {
				final SlotVisit sv = (SlotVisit) e;
				System.err.println("SlotVisit:");
				System.err.println("\tDuration: " + sv.getEventDuration());
			} else if (e instanceof PortVisit) {
				final PortVisit pv = (PortVisit) e;
				System.err.println("PortVisit:");
				System.err.println("\tDuration: " + pv.getEventDuration());
			} else {
				System.err.println("Unknown:");
				System.err.println("\t" + e.getClass());
			}
		}
	}

	/**
	 * Create a port using {@link PortFactory#eINSTANCE#createPort()} and give it a name.
	 * 
	 * @param name
	 *            The name to give port.
	 * @return The created port.
	 */
	public static Port createPort(final String name) {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);

		return port;
	}
}
