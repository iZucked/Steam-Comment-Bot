/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

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
		A.setLocation(PortFactory.eINSTANCE.createLocation());
		B.setLocation(PortFactory.eINSTANCE.createLocation());
		A.getLocation().setTimeZone("UTC");
		B.getLocation().setTimeZone("UTC");
		A.getLocation().setName("A");
		B.getLocation().setName("B");
		A.getLocation().setMmxId("A");
		B.getLocation().setMmxId("B");
		A.getCapabilities().add(PortCapability.LOAD);
		B.getCapabilities().add(PortCapability.DISCHARGE);

	}

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
	public static @NonNull IScenarioDataProvider createScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue, final int travelTime,
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
	 * 
	 */
	public static @NonNull IScenarioDataProvider createScenarioWithCanal(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue,
			final int travelTime, final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption,
			final int ballastMaxSpeed, final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed,
			final int ladenMinConsumption, final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate,
			final boolean useDryDock, final int pilotLightRate, final int minHeelVolume) {
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
	public static @NonNull IScenarioDataProvider createScenarioWithCanals(final int[] distancesBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue,
			final int travelTime, final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption,
			final int ballastMaxSpeed, final int ballastMaxConsumption, int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed,
			final int ladenMinConsumption, final int ladenMaxSpeed, final int ladenMaxConsumption, int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate,
			final boolean useDryDock, final int pilotLightRate, final int minHeelVolume) {

		// idle consumption will never be more than the idle NBO rate, so clamp the idle consumptions
		if (ballastIdleConsumptionRate > ballastIdleNBORate) {
			System.err.println("Warning: had to clamp ballast idle consumption rate");
			ballastIdleConsumptionRate = ballastIdleNBORate;
		}
		if (ladenIdleConsumptionRate > ladenIdleNBORate) {
			System.err.println("Warning: had to clamp laden idle consumption rate");
			ladenIdleConsumptionRate = ladenIdleNBORate;
		}

		CustomScenarioCreator csc = new CustomScenarioCreator(0.0f);

		PortModel portModel = csc.getScenarioModelBuilder().getPortModelBuilder().getPortModel();
		portModel.getPorts().add(A);
		portModel.getPorts().add(B);

		final Route r = PortFactory.eINSTANCE.createRoute();
		r.setName(RouteOption.DIRECT.getName());
		portModel.getRoutes().add(r);
		csc.getScenarioModelBuilder().getPortModelBuilder().createRoute(RouteOption.DIRECT.getName(), RouteOption.DIRECT);
		csc.getScenarioModelBuilder().getDistanceModelBuilder().createDistanceMatrix(RouteOption.DIRECT);

		// BUG! Overwrite data (old version just added duplicate values...)
		for (final int distance : distancesBetweenPorts) {
			csc.getScenarioModelBuilder().getDistanceModelBuilder().setPortToPortDistance(A, B, RouteOption.DIRECT, distance, true);
		}
		// 'magic' numbers that could be set in the arguments.
		// vessel
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final double fillCapacity = 1.0;

		final int spotCharterCount = 0;

		VesselAvailability[] vessels = csc.addVessel("Vessel Class", 1, spotCharterCount, baseFuelUnitPrice, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed, ballastMinConsumption,
				ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed, ladenMaxConsumption,
				ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, minHeelVolume, false);

		final VesselAvailability availability = vessels[0];

		final PurchaseContract pc = csc.purchaseContract;
		final SalesContract sc = csc.salesContract;

		// load and discharge prices and quantities
		final int loadPrice = 20;
		final int loadMaxQuantity = 100000;
		final int dischargeMaxQuantity = 100000;

		final ZonedDateTime now = LocalDate.now().atStartOfDay(ZoneId.of("UTC"));
		final ZoneId portAZone = A.getZoneId();
		final ZoneId portBZone = B.getZoneId();

		final ZonedDateTime loadDate = now.withZoneSameInstant(portAZone);
		final ZonedDateTime dischargeDate = loadDate.withZoneSameInstant(portBZone).plusHours(travelTime);

		csc.scenarioModelBuilder.getCargoModelBuilder().makeCargo() //
				.makeFOBPurchase("load", loadDate.toLocalDate(), A, pc, null, Float.toString(loadPrice)) //
				.withWindowStartTime(loadDate.getHour())//
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVisitDuration(0) //
				.with(s -> ((LoadSlot) s).setCargoCV(cvValue)) //
				.withVolumeLimits(0, loadMaxQuantity, VolumeUnits.M3) //
				.build() //

				.makeDESSale("discharge", dischargeDate.toLocalDate(), B, sc, null, Float.toString(dischargePrice)) //
				.withWindowStartTime(dischargeDate.getHour())//
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVisitDuration(0) //
				.withVolumeLimits(0, dischargeMaxQuantity, VolumeUnits.M3) //
				.build() //

				.withVesselAssignment(availability, 1) //
				.build();

		if (useDryDock) {
			final LocalDateTime thenNext = dischargeDate.plusHours(travelTime).withZoneSameInstant(portAZone).toLocalDateTime();

			// Set up dry dock to cause journey
			final DryDockEvent dryDockJourney = csc.getScenarioModelBuilder().getCargoModelBuilder() //
					.makeDryDockEvent("DryDock", thenNext, thenNext, A) //
					.withDurationInDays(0) //
					.withVesselAssignment(availability, 2) //
					.build();
		}
		return csc.getScenarioDataProvider();
	}

	/**
	 * Creates a scenario with a charter out.
	 * 
	 */
	public static @NonNull IScenarioDataProvider createCharterOutScenario(final int distanceBetweenPorts, final float baseFuelUnitPrice, final float dischargePrice, final float cvValue,
			final int travelTime, final float equivalenceFactor, final int minSpeed, final int maxSpeed, final int capacity, final int ballastMinSpeed, final int ballastMinConsumption,
			final int ballastMaxSpeed, final int ballastMaxConsumption, final int ballastIdleConsumptionRate, final int ballastIdleNBORate, final int ballastNBORate, final int ladenMinSpeed,
			final int ladenMinConsumption, final int ladenMaxSpeed, final int ladenMaxConsumption, final int ladenIdleConsumptionRate, final int ladenIdleNBORate, final int ladenNBORate,
			final int pilotLightRate, final int charterOutTimeDays, final int heelLimit) {

		CustomScenarioCreator csc = new CustomScenarioCreator(0.0f);

		PortModel portModel = csc.getScenarioModelBuilder().getPortModelBuilder().getPortModel();
		portModel.getPorts().add(A);
		portModel.getPorts().add(B);

		final Route r = csc.getScenarioModelBuilder().getPortModelBuilder().createRoute(RouteOption.DIRECT.getName(), RouteOption.DIRECT);
		csc.getScenarioModelBuilder().getDistanceModelBuilder().createDistanceMatrix(RouteOption.DIRECT);
		csc.getScenarioModelBuilder().getDistanceModelBuilder().setPortToPortDistance(A, B, RouteOption.DIRECT, distanceBetweenPorts, true);

		// 'magic' numbers that could be set in the arguments.
		// vessel
		final int warmupTime = Integer.MAX_VALUE;
		final int cooldownVolume = 0;
		final double fillCapacity = 1.0;

		final int minHeelVolume = 0;
		final int spotCharterCount = 0;

		VesselAvailability[] vessels = csc.addVessel("Vessel Class", 1, spotCharterCount, baseFuelUnitPrice, equivalenceFactor, minSpeed, maxSpeed, capacity, ballastMinSpeed, ballastMinConsumption,
				ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption, ladenMaxSpeed, ladenMaxConsumption,
				ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, minHeelVolume, false);

		final PurchaseContract pc = csc.purchaseContract;
		final SalesContract sc = csc.salesContract;

		final LocalDateTime startCharterOut = LocalDateTime.now();
		final LocalDateTime endCharterOut = startCharterOut.plusDays(charterOutTimeDays);
		final LocalDateTime dryDockJourneyStartDate = endCharterOut.plusHours(travelTime);

		final CharterOutEvent charterOut = csc.getScenarioModelBuilder().getCargoModelBuilder()//
				.makeCharterOutEvent("Charter Out", startCharterOut, startCharterOut, A) //
				.withRelocatePort(A) //
				.withDurationInDays(charterOutTimeDays) //
				.withAvailableHeelOptions(0, heelLimit, cvValue, Double.toString(dischargePrice))//
				.withRepositioningFee(0) //
				.withVesselAssignment(vessels[0], 1) //
				.build();

		// Set up dry dock to cause journey
		final DryDockEvent dryDockJourney = csc.getScenarioModelBuilder().getCargoModelBuilder() //
				.makeDryDockEvent("DryDock", dryDockJourneyStartDate, dryDockJourneyStartDate, B) //
				.withDurationInDays(0) //
				.withVesselAssignment(vessels[0], 2) //
				.build();

		return csc.getScenarioDataProvider();
	}

	/**
	 * Evaluate the scenario and create a schedule with costs in it.
	 * 
	 * @param scenario
	 * @return the evaluated schedule
	 */
	public static Schedule evaluate(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		return evaluate(scenarioDataProvider, false);
	}

	public static Schedule evaluate(@NonNull final IScenarioDataProvider scenarioDataProvider, final boolean withCharterOutGeneration) {

		// Code to dump out the scenario to disk
		if (false) {
			try {
				ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, new File("c:/temp/test.lingo"));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
		userSettings.setGenerateCharterOuts(withCharterOutGeneration);
		final Set<String> hints = LNGTransformerHelper.getHints(userSettings);
		final LNGDataTransformer dataTransformer = new LNGDataTransformer(scenarioDataProvider, null, userSettings, ScenarioUtils.createDefaultSolutionBuilderSettings(), 1, hints,
				LNGTransformerHelper.getOptimiserInjectorServices(new TransformerExtensionTestBootstrapModule(), null));

		Injector evaluationInjector;
		{
			final List<Module> modules = new LinkedList<>();
			modules.add(new InitialSequencesModule(dataTransformer.getInitialSequences()));
			modules.add(new InputSequencesModule(dataTransformer.getInitialSequences()));
			modules.add(new InitialPhaseOptimisationDataModule());
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
					new LNGParameters_EvaluationSettingsModule(dataTransformer.getUserSettings(), ScenarioUtils.createDefaultConstraintAndFitnessSettings()), dataTransformer.getModuleServices(),
					IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
			modules.addAll(
					LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), dataTransformer.getModuleServices(), IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
			evaluationInjector = dataTransformer.getInjector().createChildInjector(modules);
		}

		final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
		// final IAnnotatedSolution startSolution = LNGSchedulerJobUtils.evaluateCurrentState(transformer);

		final Pair<Command, Schedule> p = LNGSchedulerJobUtils.exportSolution(evaluationInjector, scenarioDataProvider.getTypedScenario(LNGScenarioModel.class), dataTransformer.getUserSettings(),
				scenarioDataProvider.getEditingDomain(), modelEntityMap, dataTransformer.getInitialSequences(), null);
		scenarioDataProvider.getCommandStack().execute(p.getFirst());
		return p.getSecond();
	}

	/**
	 * Print a cargo allocation's details.
	 * 
	 * @param testName
	 *            The name of the test being performed (to aid with identification in the console).
	 * @param a
	 *            The cargo allocation to print to console.
	 */
	public static void printCargoAllocation(final String testName, final CargoAllocation ca) {
		System.err.println(testName);
		System.err.println("Allocation " + ca.getName());
		// FIXME: Update for API changes
		// System.err.println("Total cost: " + a.getTotalCost() + ", Total LNG volume used for fuel: " + a.getFuelVolume() + "M3");

		final SimpleCargoAllocation a = new SimpleCargoAllocation(ca);
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
		System.err.println(
				"\tRoute: " + PortModelLabeller.getName(journey.getRouteOption()) + ", Distance: " + journey.getDistance() + ", Duration: " + journey.getDuration() + ", Speed: " + journey.getSpeed());
		printFuel(journey.getFuels());
		// FIXME: Update for API changes
		System.err.println("\tRoute cost: $" + journey.getToll() + ", Total cost: $" + (journey.getToll() + journey.getFuelCost() + journey.getCharterCost()));
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

			for (final FuelAmount fa : fq.getAmounts()) {
				System.err.println("\t" + fq.getFuel() + " " + fa.getQuantity() + fa.getUnit() + " at $" + fq.getCost() + " (unit price: " + fa.getUnitPrice() + ")");
			}
			// System.err.println("\t" + fq.getFuel() + " " + fq.getAmounts(). + fq.getFuelUnit() + " at $" + fq.getTotalPrice());
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

		if (seq.getVesselAvailability() != null) {
			final VesselAvailability va = seq.getVesselAvailability();

			System.err.println(va.getStartAfterAsDateTime());
			System.err.println(va.getEndByAsDateTime());
		}

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
				System.err.println("\tDuration: " + j.getDuration() + ", distance: " + j.getDistance() + ", ports: " + j.getPort().getName() + " -> " + j.getDestination().getName());
				System.err.println("\tDepart: " + j.getStart() + ", Arrive: " + j.getEnd());
				System.err.println("\tFuel Cost: " + j.getFuelCost());

				ScenarioTools.printFuel(j.getFuels());

			} else if (e instanceof SlotVisit) {
				final SlotVisit sv = (SlotVisit) e;
				System.err.println("SlotVisit:");
				final SlotAllocation slotAllocation = sv.getSlotAllocation();
				final Slot slot = slotAllocation.getSlot();
				final String description = (slot instanceof LoadSlot ? "load: " : "discharge: ");
				final int volume = slotAllocation.getVolumeTransferred();

				System.err.println("\tArrive: " + sv.getStart() + ",  Depart: " + sv.getEnd());
				System.err.println(
						"\tWindowStart: " + sv.getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime() + ",  WindowEnd: " + sv.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime());

				System.err.println("\tDuration: " + sv.getDuration() + ", " + description + volume);
				ScenarioTools.printFuel(sv.getFuels());
				if (sv.getPortCost() > 0) {
					System.err.println("\tPort cost: " + sv.getPortCost());

				}
				if (sv.getCharterCost() > 0) {
					System.err.println("\tHire cost: " + sv.getCharterCost());

				}
			} else if (e instanceof Cooldown) {
				final Cooldown cd = (Cooldown) e;
				System.err.println("Cooldown:");
				System.err.println("\tDuration: " + cd.getDuration());
				System.err.println("\tStart: " + cd.getStart() + ", End: " + cd.getEnd());
				ScenarioTools.printFuel(cd.getFuels());

			} else {
				System.err.println("Unknown:");
				System.err.println("\t" + e.getClass());
				System.err.println("\tStart: " + e.getStart() + ", End: " + e.getEnd());
			}

			if (e instanceof Event) {
				Event portVisit = (Event) e;
				System.err.printf("\tHeel: %d -> %d\n", e.getHeelAtStart(), e.getHeelAtEnd());
			}

			// show any capacity violations
			if (e instanceof CapacityViolationsHolder) {
				final EMap<CapacityViolationType, Long> violations = ((CapacityViolationsHolder) e).getViolations();
				if (violations != null && !violations.isEmpty()) {
					String violationString = null;
					for (final Map.Entry<CapacityViolationType, Long> entry : violations) {
						if (violationString == null) {
							violationString = "\tCapacity violations: ";
						} else {
							violationString += ", ";
						}
						violationString += String.format("%s (%d m3)", entry.getKey().getLiteral(), entry.getValue());
					}
					System.err.println(violationString);
				}
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
	public static @NonNull Port createPort(final String name) {

		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);

		port.getCapabilities().add(PortCapability.LOAD);
		port.getCapabilities().add(PortCapability.DISCHARGE);
		port.getCapabilities().add(PortCapability.DRYDOCK);
		port.getCapabilities().add(PortCapability.MAINTENANCE);

		port.setLocation(PortFactory.eINSTANCE.createLocation());
		port.getLocation().setName(name);
		port.getLocation().setMmxId(name);

		return port;
	}

	public static String getVesselStateString(final boolean isLaden) {
		if (isLaden) {
			return "Laden";
		} else {
			return "Ballast";
		}
	}

	// public static void storeToFile(final MMXRootObject instance, final File file, final String versionContext, final int scenarioVersion) throws IOException {
	// final ResourceSetImpl resourceSet = new ResourceSetImpl();
	// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	// final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
	//
	// manifest.setScenarioType("com.mmxlabs.shiplingo.platform.models.manifest.scnfile");
	// manifest.setScenarioVersion(scenarioVersion);
	// manifest.setVersionContext(versionContext);
	// // manifest.setUUID(instance.getUuid());
	// final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/MANIFEST.xmi");
	// final Resource manifestResource = resourceSet.createResource(manifestURI);
	//
	// manifestResource.getContents().add(manifest);
	//
	// final URI relativeURI = URI.createURI("/rootObject.xmi");
	//
	// manifest.getModelURIs().add(relativeURI.toString());
	// final URI resolved = relativeURI.resolve(manifestURI);
	// final Resource r2 = resourceSet.createResource(resolved);
	// r2.getContents().add(instance);
	// r2.save(null);
	// manifestResource.save(null);
	// }

	// public static BaseFuelCost createBaseFuelCost(final BaseFuel baseFuel, final double price) {
	// final BaseFuelCost bfc = PricingFactory.eINSTANCE.createBaseFuelCost();
	//
	// final BaseFuelIndex bfi = PricingFactory.eINSTANCE.createBaseFuelIndex();
	// bfi.setName(baseFuel.getName());
	// final DataIndex<Double> indexData = PricingFactory.eINSTANCE.createDataIndex();
	// bfi.setData(indexData);
	// final IndexPoint<Double> point = PricingFactory.eINSTANCE.createIndexPoint();
	// point.setValue((double) price);
	// point.setDate(YearMonth.now());
	// indexData.getPoints().add(point);
	// bfc.setIndex(bfi);
	//
	// bfc.setFuel(baseFuel);
	// return bfc;
	//
	//
	// }

}
