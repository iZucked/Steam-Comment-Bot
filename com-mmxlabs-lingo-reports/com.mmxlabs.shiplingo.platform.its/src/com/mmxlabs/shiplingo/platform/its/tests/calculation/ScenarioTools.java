/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.calculation;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.lng.transformer.export.AnnotatedSolutionExporter;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.NullOptimiserProgressMonitor;
import com.mmxlabs.shiplingo.platform.models.manifest.ManifestJointModel;

/**
 * Methods for printing and creating a scenario where a ship travels from port A to port B then back to port A.
 * 
 * @author Adam Semenenko
 * 
 */
public class ScenarioTools {

	public static final Port A = PortFactory.eINSTANCE.createPort();
	public static final Port B = PortFactory.eINSTANCE.createPort();
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
	public static MMXRootObject createScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume) {

		return createScenarioWithCanals(new int[] { distanceBetweenPorts }, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume);

	}

	/**
	 * Same as
	 * {@link #createScenarioWithCanal(int[], float, float, float, int, float, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, int, boolean, int, int, VesselClassCost)}
	 * but only has argument for one distance between the two ports.
	 */
	public static MMXRootObject createScenarioWithCanal(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume) {
		return createScenarioWithCanals(new int[] { distanceBetweenPorts }, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed,
				ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed,
				ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, useDryDock, pilotLightRate, minHeelVolume);

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
	public static MMXRootObject createScenarioWithCanals(final int[] distancesBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final boolean useDryDock,
			final int pilotLightRate, final int minHeelVolume) {

		final MMXRootObject scenario = ManifestJointModel.createEmptyInstance();

		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

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

		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setEquivalenceFactor(equivalenceFactor);

		final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
		bfc.setFuel(baseFuel);
		bfc.setPrice(baseFuelUnitPrice);
		fleetCostModel.getBaseFuelPrices().add(bfc);

		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		fleetModel.getBaseFuels().add(baseFuel);

		final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vc.setLadenAttributes(laden);
		vc.setBallastAttributes(ballast);

		fleetModel.getVesselClasses().add(vc);

		vc.setName("Vessel Class");
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

		final CharterCostModel charterCostModel = PricingFactory.eINSTANCE.createCharterCostModel();
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

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setVesselClass(vc);
		vessel.setName("Vessel");

		final VesselAvailablility availablility = FleetFactory.eINSTANCE.createVesselAvailablility();

		vessel.setAvailability(availablility);

		HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		vessel.setStartHeel(heelOptions);
		
		fleetModel.getVessels().add(vessel);

		final PortModel portModel = scenario.getSubModel(PortModel.class);
		portModel.getPorts().add(A);
		portModel.getPorts().add(B);

		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName("default");
		portModel.getRoutes().add(r);
		for (final int distance : distancesBetweenPorts) {
			final RouteLine distanceLine = PortFactory.eINSTANCE.createRouteLine();
			distanceLine.setFrom(A);
			distanceLine.setTo(B);
			distanceLine.setDistance(distance);
			r.getLines().add(distanceLine);

			// don't forget that distances can be asymmetric, so have to go both ways.
			final RouteLine distancLineBack = PortFactory.eINSTANCE.createRouteLine();
			distancLineBack.setFrom(B);
			distancLineBack.setTo(A);
			distancLineBack.setDistance(distance);
			r.getLines().add(distancLineBack);
		}

		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);

		final LegalEntity e = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(e);
		final LegalEntity s = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(s);
		commercialModel.setShippingEntity(s);

		e.setName("Other");
		s.setName("Shipping");

		final IndexPriceContract sc = CommercialFactory.eINSTANCE.createIndexPriceContract();
		final PurchaseContract pc = CommercialFactory.eINSTANCE.createFixedPriceContract();

		final DataIndex<Double> sales = PricingFactory.eINSTANCE.createDataIndex();
		sales.setName("Sales");

		pricingModel.getCommodityIndices().add(sales);

		sc.setEntity(e);
		pc.setEntity(e);
		sc.setIndex(sales);

		commercialModel.getSalesContracts().add(sc);
		commercialModel.getPurchaseContracts().add(pc);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot dis = CargoFactory.eINSTANCE.createDischargeSlot();

		cargo.setLoadSlot(load);
		cargo.setDischargeSlot(dis);

		load.setPort(A);
		dis.setPort(B);
		load.setContract(pc);
		dis.setContract(sc);
		load.setName("load");
		dis.setName("discharge");

		dis.setFixedPrice(dischargePrice);
		load.setFixedPrice(loadPrice);

		load.setMaxQuantity(loadMaxQuantity);
		dis.setMaxQuantity(dischargeMaxQuantity);

		load.setCargoCV(cvValue);

		final Date now = new Date();
		load.setWindowStart(now);
		load.setWindowSize(0);
		final Date dischargeDate = new Date(now.getTime() + (Timer.ONE_HOUR * travelTime));
		dis.setWindowStart(dischargeDate);
		dis.setWindowSize(0);

		if (useDryDock) {
			// Set up dry dock.
			final DryDockEvent dryDock = FleetFactory.eINSTANCE.createDryDockEvent();
			dryDock.setDurationInDays(0);
			dryDock.setPort(A);
			// add to scenario's fleet model
			fleetModel.getVesselEvents().add(dryDock);
			// set the date to be after the discharge date
			final Date thenNext = new Date(dischargeDate.getTime() + (Timer.ONE_HOUR * travelTime));
			dryDock.setStartAfter(thenNext);
			dryDock.setStartBy(thenNext);
		}

		cargo.setName("CARGO");

		final CargoModel cargoModel = scenario.getSubModel(CargoModel.class);
		cargoModel.getCargos().add(cargo);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
	}

	/**
	 * Creates a scenario with a charter out.
	 */
	public static MMXRootObject createCharterOutScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
			final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption, final int ballastMaxSpeed,
			final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed, final int ladenMinConsumption,
			final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate, final int pilotLightRate,
			final int charterOutTimeDays, final int heelLimit) {

		final MMXRootObject scenario = ManifestJointModel.createEmptyInstance();
		final PortModel portModel = scenario.getSubModel(PortModel.class);
		final FleetModel fleetModel = scenario.getSubModel(FleetModel.class);
		final PricingModel pricingModel = scenario.getSubModel(PricingModel.class);
		final FleetCostModel fleetCostModel = pricingModel.getFleetCost();

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

		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName("BASE FUEL");
		baseFuel.setEquivalenceFactor(equivalenceFactor);

		final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
		bfc.setFuel(baseFuel);
		bfc.setPrice(baseFuelUnitPrice);
		fleetCostModel.getBaseFuelPrices().add(bfc);

		fleetModel.getBaseFuels().add(baseFuel);
		final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vc.setLadenAttributes(laden);
		vc.setBallastAttributes(ballast);

		fleetModel.getVesselClasses().add(vc);

		vc.setName("Vessel Class");
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

		final CharterCostModel charterCostModel = PricingFactory.eINSTANCE.createCharterCostModel();
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

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setVesselClass(vc);
		vessel.setName("Vessel");

		final VesselAvailablility availability = FleetFactory.eINSTANCE.createVesselAvailablility();

		
		vessel.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());
		
		vessel.setAvailability(availability);

		fleetModel.getVessels().add(vessel);

		portModel.getPorts().add(A);
		portModel.getPorts().add(B);

		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName("default");
		portModel.getRoutes().add(r);

		final RouteLine distance = PortFactory.eINSTANCE.createRouteLine();
		distance.setFrom(A);
		distance.setTo(B);
		distance.setDistance(distanceFromAToB);

		// don't forget that distances can be asymmetric, so have to go both ways.
		final RouteLine distance2 = PortFactory.eINSTANCE.createRouteLine();
		distance2.setFrom(B);
		distance2.setTo(A);
		distance2.setDistance(distanceFromBToA);

		r.getLines().add(distance);
		r.getLines().add(distance2);

		final CommercialModel commercialModel = scenario.getSubModel(CommercialModel.class);
		final LegalEntity e = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(e);
		final LegalEntity s = CommercialFactory.eINSTANCE.createLegalEntity();
		commercialModel.getEntities().add(s);
		commercialModel.setShippingEntity(s);

		e.setName("Other");
		s.setName("Shipping");

		final IndexPriceContract sc = CommercialFactory.eINSTANCE.createIndexPriceContract();
		final PurchaseContract pc = CommercialFactory.eINSTANCE.createFixedPriceContract();

		final DataIndex<Double> sales = PricingFactory.eINSTANCE.createDataIndex();
		sales.setName("Sales");

		pricingModel.getCommodityIndices().add(sales);

		sc.setEntity(e);
		pc.setEntity(e);
		sc.setIndex(sales);

		commercialModel.getSalesContracts().add(sc);
		commercialModel.getPurchaseContracts().add(pc);

		final Date startCharterOut = new Date();
		final Date endCharterOut = new Date(startCharterOut.getTime() + TimeUnit.DAYS.toMillis(charterOutTimeDays));
		final Date dryDockJourneyStartDate = new Date(endCharterOut.getTime() + TimeUnit.HOURS.toMillis(travelTime));

		final CharterOutEvent charterOut = FleetFactory.eINSTANCE.createCharterOutEvent();
		charterOut.setStartAfter(startCharterOut);
		charterOut.setStartBy(startCharterOut);
		// same start and end port.
		charterOut.setPort(A);
		charterOut.setRelocateTo(A);
		charterOut.setName("Charter Out");
		
		final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		heelOptions.setVolumeAvailable(heelLimit);
		heelOptions.setCvValue(cvValue);
		heelOptions.setPricePerMMBTU(dischargePrice);
		charterOut.setHeelOptions(heelOptions);
		

		charterOut.setDurationInDays(charterOutTimeDays);
		// charterOut.setDailyCharterOutPrice(0);
		charterOut.setRepositioningFee(0);
		// add to the scenario's fleet model
		fleetModel.getVesselEvents().add(charterOut);

		// Set up dry dock to cause journey
		final DryDockEvent dryDockJourney = FleetFactory.eINSTANCE.createDryDockEvent();
		dryDockJourney.setDurationInDays(0);
		dryDockJourney.setPort(B);
		// set the date to be after the charter out date
		dryDockJourney.setStartAfter(dryDockJourneyStartDate);
		dryDockJourney.setStartBy(dryDockJourneyStartDate);
		// add to scenario's fleet model
		fleetModel.getVesselEvents().add(dryDockJourney);

		ScenarioUtils.addDefaultSettings(scenario);

		return scenario;
	}

	/**
	 * Evaluate the scenario and create a schedule with costs in it.
	 * 
	 * @param scenario
	 * @return the evaluated schedule
	 */
	public static Schedule evaluate(final MMXRootObject scenario) {

		final LNGTransformer transformer = new LNGTransformer(scenario);
		// final LNGScenarioTransformer transformer = new LNGScenarioTransformer(scenario);
		final LNGScenarioTransformer lst = transformer.getLngScenarioTransformer();

		final ModelEntityMap entities = transformer.getEntities();
		final OptimisationTransformer ot = transformer.getOptimisationTransformer();

		// TODO: This needs fixing up
		if (!lst.addPlatformTransformerExtensions()) {
			// add extensions manually; TODO improve this later.
			final SimpleContractTransformer sct = new SimpleContractTransformer();
			lst.addTransformerExtension(sct);
			lst.addContractTransformer(sct, sct.getContractEClasses());
		}

		final IOptimisationData data = transformer.getOptimisationData();

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
		System.err.println("Allocation " + a.getName());
		// FIXME: Update for API changes
		// System.err.println("Total cost: " + a.getTotalCost() + ", Total LNG volume used for fuel: " + a.getFuelVolume() + "M3");

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
		System.err.println("\tRoute: " + journey.getRoute() + ", Distance: " + journey.getDistance() + ", Duration: " + journey.getDuration() + ", Speed: " + journey.getSpeed());
		printFuel(journey.getFuels());
		// FIXME: Update for API changes
		// System.err.println("\tRoute cost: $" + journey.getRouteCost() + ", Total cost: $" + journey.getFuelCost() + journey.getHireCost());
	}

	/**
	 * Print details of an idle.
	 * 
	 * @param idleName
	 * @param idle
	 */
	private static void printIdle(final String idleName, final Idle idle) {

		System.err.println(idleName + ":");
		System.err.println("\tDuration: " + idle.getDuration());
		printFuel(idle.getFuels());
		// FIXME
		// System.err.println("\tTotal cost: $" + idle.getFuelCost() + idle.getHireCost());
	}

	/**
	 * Print the details of fuel used for a section of a voyage.
	 * 
	 * @param fuelQuantities
	 */
	public static void printFuel(final EList<FuelQuantity> fuelQuantities) {

		for (final FuelQuantity fq : fuelQuantities) {
			// FIXME: Update for API changes
			// System.err.println("\t" + fq.getFuel() + " " + fq.getQuantity() + fq.getFuelUnit() + " at $" + fq.getTotalPrice());
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

		for (final Event e : seq.getEvents()) {

			if (e instanceof Idle) {

				final Idle i = (Idle) e;

				/**
				 * If you have a vessel which doesn't go anywhere at all and has no start/end conditions it will have an idle event with a null port.
				 */
				final String portName = i.getPort() == null ? "null" : i.getPort().getName();

				System.err.println("Idle:");
				System.err.println("\tvessel state: " + getVesselStateString(i.isLaden()) + ", Duration: " + i.getDuration() + ", port: " + portName);
				ScenarioTools.printFuel(i.getFuels());

			} else if (e instanceof VesselEventVisit) {

				final VesselEventVisit vev = (VesselEventVisit) e;
				System.err.println("VesselEventVisit:");
				System.err.println("\tDuration: " + vev.getDuration());

			} else if (e instanceof Journey) {

				final Journey j = (Journey) e;
				System.err.println("Journey:");
				System.err.println("\tDuration: " + j.getDuration() + ", distance: " + j.getDistance() + ", destination: " + j.getPort().getName());

				ScenarioTools.printFuel(j.getFuels());

			} else if (e instanceof SlotVisit) {
				final SlotVisit sv = (SlotVisit) e;
				System.err.println("SlotVisit:");
				System.err.println("\tDuration: " + sv.getDuration());
			} else if (e instanceof SlotVisit) {
				final SlotVisit pv = (SlotVisit) e;
				System.err.println("SlotVisit:");
				System.err.println("\tDuration: " + pv.getDuration());
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

	public static String getVesselStateString(final boolean isLaden) {
		if (isLaden) {
			return "Laden";
		} else {
			return "Ballast";
		}
	}
}
