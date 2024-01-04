package com.mmxlabs.lingo.its.microcases.emissions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.its.verifier.OptimiserDataMapper;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lingo.its.verifier.SolutionData;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.AdpOptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.EmissionsUtil;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

public class EuEtsTests extends AbstractMicroTestCase {

	private static final String VESSEL_MEDIUM = InternalDataConstants.REF_VESSEL_STEAM_145;
	
	
	@Nested
	class EmissionsResultsTests {

		
		private static PortGroup portGroup;
		private static Port loadPort;
		private static Port dischargePort;
		private static EuEtsModel euEtsModel;
		private static int vesselSpeed = 12;
		private static int journeyDistance = vesselSpeed * 24;
		private static final double M3_LNG_PER_MT = 0.477;
		
		public void setup() {
			// Setup EU ETS parameters
			euEtsModel = lngScenarioModel.getCargoModel().getEuEtsModel();
			loadPort = portFinder.findPortById(InternalDataConstants.PORT_HAMMERFEST);
			dischargePort = portFinder.findPortById(InternalDataConstants.PORT_BARCELONA);
			distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, RouteOption.DIRECT, journeyDistance, true);
			portGroup = portModelBuilder.makePortGroup("EU", List.of(loadPort, dischargePort));
			euEtsModel.setEuaPriceExpression("1.0"); // Sets price to be equal to total emissions
			euEtsModel.setEuPortGroup(portGroup);
			
			Collection<PortCapability> capabilities = List.of(PortCapability.LOAD, PortCapability.DISCHARGE);
			loadPort.getCapabilities().addAll(capabilities);
			dischargePort.getCapabilities().addAll(capabilities);
			loadPort.setLoadDuration(0);
			loadPort.setDischargeDuration(0);
			dischargePort.setLoadDuration(0);
			dischargePort.setDischargeDuration(0);
		}
		
		/**
		 * Test for
		 * 
		 * - sequence: start -> cargo -> maintenance -> end
		 * 
		 * - travel statuses: pre and post maintenance journeys travel direct
		 * 
		 * - LNG status: have enough for entire sequence
		 */
		@Test
		@Tag(TestCategories.MICRO_TEST)
		public void testBothEuPortVisitEmissions() {
			setup();
			
			final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
			vessel.setMinSpeed(vesselSpeed);
			vessel.setMaxSpeed(vesselSpeed);
			

			final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
					.withStartWindow(LocalDateTime.of(2026, 1, 1, 0, 0, 0))
					.withEndWindow(LocalDateTime.of(2026, 1, 4, 0, 0, 0))
					.build();
			
			cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("loadSlot", LocalDate.of(2026, 1, 1), loadPort, null, entity, "1.0") //
					.with(s -> ((LoadSlot) s).setCargoCV(22.8)) //
					.build() //
					.makeDESSale("dischargeSlot", LocalDate.of(2026, 1, 3), dischargePort, null, entity, "1.5") //
					.build() //
					.withVesselAssignment(vesselCharter, 1) //
					.build();

			evaluateTest();

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);

			final List<Sequence> sequences = schedule.getSequences();
			Assertions.assertEquals(1, sequences.size());

			final Sequence sequence = sequences.get(0);
			final List<Event> events = sequence.getEvents();
			
			checkEventsEmissions(events);
		}
		
		@Test
		@Tag(TestCategories.MICRO_TEST)
		public void testOneEuPortVisitEmissions() {
			setup();
			
			// Non EU port
			dischargePort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
			distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, RouteOption.DIRECT, journeyDistance, true);
			dischargePort.getCapabilities().add(PortCapability.DISCHARGE);
			dischargePort.setDischargeDuration(0);
			
			final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
			vessel.setMinSpeed(vesselSpeed);
			vessel.setMaxSpeed(vesselSpeed);
			

			final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
					.withStartWindow(LocalDateTime.of(2026, 1, 1, 0, 0, 0))
					.withEndWindow(LocalDateTime.of(2026, 1, 4, 0, 0, 0))
					.build();
			
			cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("loadSlot", LocalDate.of(2026, 1, 1), loadPort, null, entity, "1.0") //
					.with(s -> ((LoadSlot) s).setCargoCV(22.8)) //
					.build() //
					.makeDESSale("dischargeSlot", LocalDate.of(2026, 1, 3), dischargePort, null, entity, "1.5") //
					.build() //
					.withVesselAssignment(vesselCharter, 1) //
					.build();

			evaluateTest();

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);

			final List<Sequence> sequences = schedule.getSequences();
			Assertions.assertEquals(1, sequences.size());

			final Sequence sequence = sequences.get(0);
			final List<Event> events = sequence.getEvents();
			
			checkEventsEmissions(events);
		}
		
		
		@Test
		@Tag(TestCategories.MICRO_TEST)
		public void testNoneEuPortVisitEmissions() {
		
			setup();
			
			// Non EU port
			loadPort = portFinder.findPortById(InternalDataConstants.PORT_ALTAMIRA);
			dischargePort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
			distanceModelBuilder.setPortToPortDistance(loadPort, dischargePort, RouteOption.DIRECT, journeyDistance, true);
			dischargePort.getCapabilities().add(PortCapability.DISCHARGE);
			dischargePort.setDischargeDuration(0);
			loadPort.getCapabilities().add(PortCapability.LOAD);
			loadPort.setLoadDuration(0);
			
			final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
			vessel.setMinSpeed(vesselSpeed);
			vessel.setMaxSpeed(vesselSpeed);
			

			final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity) //
					.withStartWindow(LocalDateTime.of(2026, 1, 1, 0, 0, 0))
					.withEndWindow(LocalDateTime.of(2026, 1, 4, 0, 0, 0))
					.build();
			
			cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("loadSlot", LocalDate.of(2026, 1, 1), loadPort, null, entity, "1.0") //
					.with(s -> ((LoadSlot) s).setCargoCV(22.8)) //
					.build() //
					.makeDESSale("dischargeSlot", LocalDate.of(2026, 1, 3), dischargePort, null, entity, "1.5") //
					.build() //
					.withVesselAssignment(vesselCharter, 1) //
					.build();

			evaluateTest();

			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);

			final List<Sequence> sequences = schedule.getSequences();
			Assertions.assertEquals(1, sequences.size());

			final Sequence sequence = sequences.get(0);
			final List<Event> events = sequence.getEvents();
			
			checkEventsEmissions(events);
		}
		
		private void checkEventsEmissions(List<Event> events) {
			int expectedTotalEmissions = events.stream().mapToInt(e -> e.getEmissionsCost()).sum();
			int actualTotalEmissions = 0;
			
			double precision = 0.01; // ±1% precision in results to account for rounding errors
			for(Event e : events) {
				double reductionFactor = getReductionFactor(e, euEtsModel, portGroup);
				if(e instanceof Journey journey) {
					double actualJourneyEmissions = Math.floor(getEmissionsFromFuelUsage(journey) * reductionFactor);
					assertWithPrecision(journey.getEmissionsCost(), actualJourneyEmissions, precision);
					actualTotalEmissions += actualJourneyEmissions;
				}
				else if(e instanceof Idle idle) {
					double actualIdleEmissions = Math.floor(getEmissionsFromFuelUsage(idle) * reductionFactor);
					assertWithPrecision(idle.getEmissionsCost(), actualIdleEmissions, precision);
					actualTotalEmissions += actualIdleEmissions;
				}
				else if(e instanceof SlotVisit slotVisit) {
					double actualPortEmissions = Math.floor(getEmissionsFromFuelUsage(slotVisit) * reductionFactor);
					assertWithPrecision(slotVisit.getEmissionsCost(), actualPortEmissions, precision);
					actualTotalEmissions += actualPortEmissions;
				}
			}
			assertWithPrecision(expectedTotalEmissions, actualTotalEmissions, precision);
		}
	}
	
	@Nested
	class OptimisationTests{
		
		private Schedule setup(String euaPriceExpression) {
			EuEtsModel euEtsModel = lngScenarioModel.getCargoModel().getEuEtsModel();
			Port euPort = portFinder.findPortById(InternalDataConstants.PORT_HAMMERFEST);
			Port jpnPort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
			Port usPort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
			distanceModelBuilder.setPortToPortDistance(usPort, euPort, RouteOption.DIRECT, 100, true);
			distanceModelBuilder.setPortToPortDistance(usPort, jpnPort, RouteOption.DIRECT, 100, true);
			PortGroup portGroup = portModelBuilder.makePortGroup("EU", List.of(euPort));
			euEtsModel.setEuaPriceExpression(euaPriceExpression); 
			euEtsModel.setEuPortGroup(portGroup);
			
			int year = 2026;
			EmissionsCoveredTable yearEmissions = CargoFactory.eINSTANCE.createEmissionsCoveredTable();
			yearEmissions.setEmissionsCovered(100);
			yearEmissions.setStartYear(year);
			euEtsModel.getEmissionsCovered().add(yearEmissions);
			
			
			// Unpaired FOB US Purchase
			cargoModelBuilder.makeCargo() //
					.makeFOBPurchase("US_Slot", LocalDate.of(year, 1, 1), usPort, null, entity, "1.0") //
					.build();
			
			// Unpaired DES EU Sale
			cargoModelBuilder.makeCargo() //
					.makeDESSale("EU_SLOT", LocalDate.of(year, 1, 1), euPort, null, entity, "1.0") //
					.withOptional(true) // 
					.build();
			
			// Unpaired DES JPN Sale
			cargoModelBuilder.makeCargo() //
					.makeDESSale("JPN_SLOT", LocalDate.of(year, 1, 1), jpnPort, null, entity, "1.0") //
					.withOptional(true) // 
					.build();
			
			evaluateTest();
			
			final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
			Assertions.assertNotNull(schedule);
			
			return schedule;
			
		}
		
		private @NonNull UserSettings createUserSettings() {
			final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
			userSettings.setGenerateCharterOuts(false);
			userSettings.setMode(OptimisationMode.SHORT_TERM);
			userSettings.setAdpOptimisationMode(AdpOptimisationMode.NON_CLEAN_SLATE);

			userSettings.setShippingOnly(true);
			userSettings.setWithSpotCargoMarkets(true);
			userSettings.setSimilarityMode(SimilarityMode.OFF);
			return userSettings;
		}
		
		private OptimisationPlan createOptimisationPlan(final UserSettings userSettings) {
			final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

			ScenarioUtils.setLSOStageIterations(optimisationPlan, 50_000);
			ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 10_000);
			ScenarioUtils.createOrUpdateAllObjectives(optimisationPlan, NonOptionalSlotFitnessCoreFactory.NAME, true, 24_000_000);

			return optimisationPlan;
		}
		
		@Test
		@Tag(TestCategories.OPTIMISATION_TEST)
		/**
		 * Testing optimisation for unpaired trades where EUA price is cheap
		 * 
		 * Expected Result:
		 * - US trade is paired to EU slot as emissions cost is 0
		 */
		public void testShipping_euaCheap() {
			final UserSettings userSettings = createUserSettings();
			OptimisationPlan optimisationPlan = createOptimisationPlan(userSettings);
			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withOptimisationPlan(optimisationPlan) //
					.withOptimiseHint() //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();
			
			Schedule schedule = setup("0.0");

			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if
				// needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);

				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker() //
						.withUsedDischarge("EU_SLOT") //
						.anyFleetVessel() //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);
			});
		}
		
		@Test
		@Tag(TestCategories.OPTIMISATION_TEST)
		/**
		 * Testing optimisation for unpaired trades where EUA price is cheap
		 * 
		 * Expected Result:
		 * - US trade is paired to JPN slot as emissions cost is 1,000
		 */
		public void testShipping_euaExpensive() {
			final UserSettings userSettings = createUserSettings();
			OptimisationPlan optimisationPlan = createOptimisationPlan(userSettings);
			final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
					.withOptimisationPlan(optimisationPlan) //
					.withOptimiseHint() //
					.withThreadCount(1) //
					.buildDefaultRunner();

			runnerBuilder.evaluateInitialState();
			Schedule schedule = setup("1000.0");
			
			// Solution 1
			runnerBuilder.run(false, runner -> {

				// Run, get result and store to schedule model for inspection at EMF level if
				// needed
				final IMultiStateResult result = runner.runWithProgress(new NullProgressMonitor());
				final LNGScenarioToOptimiserBridge bridge = runnerBuilder.getScenarioRunner().getScenarioToOptimiserBridge();
				final OptimiserDataMapper mapper = new OptimiserDataMapper(bridge);
				final List<SolutionData> solutionDataList = OptimiserResultVerifier.createSolutionData(true, result, mapper);

				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(mapper) //
						.withAnySolutionResultChecker() //
						.withUsedDischarge("JPN_SLOT") //
						.anyFleetVessel() //
						.build();

				final ISequences solution = verifier.verifySolutionExistsInResults(solutionDataList, Assertions::fail);
				Assertions.assertNotNull(solution);
			});
		}
	}
	
	private double getEmissionsFromFuelUsage(FuelUsage fuelUsageEvent) {
		double totalEmissions = 0;
		for(final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
			
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			double emissionsRate = 0;
			if(baseFuel != null)  {
				if(baseFuel.getEmissionReference() != null) {
					emissionsRate = baseFuel.getEmissionReference().getCf();
				}
				else if(baseFuel.getName().equals("HFO")) {
					emissionsRate = 3.114;
				}
				else if(baseFuel.getName().equals("MGO")) {
					emissionsRate = 3.206;
				}
			}
			
			// Calculate emissions rate for fuel used
			switch(fuel) {
			case BASE_FUEL, PILOT_LIGHT:
				// Base Fuel is set to Pilot Light Base Fuel when fuel is Pilot Light
				totalEmissions += fuelAmount.getQuantity() * emissionsRate;
				break;
			case FBO, NBO:
				// Need to define this function for optimiser
				final long quantity = EmissionsUtil.consumedQuantityLNG(fuelQuantity);
				emissionsRate = 2.75;
				totalEmissions +=  quantity * emissionsRate;
				break;
			default:
			}
		}
		
		return totalEmissions;
	}
	
	private double getReductionFactor(Event event, EuEtsModel euEtsModel, PortGroup portGroup) {
		double emissionsFactor = 1.0;
		if(event instanceof Journey journeyEvent) {
			// From EU port -> EU port?
			if(isPortInPortGroup(journeyEvent.getPort(), portGroup) && isPortInPortGroup(journeyEvent.getDestination(), portGroup)) {
				emissionsFactor *= 1.0;
			}
			// From EU port -> non EU port
			else if(isPortInPortGroup(journeyEvent.getPort(), portGroup) ^ isPortInPortGroup(journeyEvent.getDestination(), portGroup)) {
				emissionsFactor *= 0.5;
			}
			// From non EU port -> non EU port
			else if(!isPortInPortGroup(journeyEvent.getPort(), portGroup) && !isPortInPortGroup(journeyEvent.getDestination(), portGroup)){
				emissionsFactor *= 0;
			}
		}
		else if(event instanceof Idle || event instanceof SlotVisit) {
			// Event at EU port?
			if(isPortInPortGroup(event.getPort(), portGroup)) {
				emissionsFactor *= 1.0;
			}
			else {
				emissionsFactor *= 0;
			}
		}
		
		
		// Check event year against ramp up table
		for(int i = 1; i < euEtsModel.getEmissionsCovered().size(); i++) {
			EmissionsCoveredTable entry = euEtsModel.getEmissionsCovered().get(i);
			if(event.getStart().getYear() <  entry.getStartYear()) {
				emissionsFactor *= (euEtsModel.getEmissionsCovered().get(i - 1).getEmissionsCovered() / 100.0);
				break;
			}
		}
		
		return emissionsFactor;
	}
	
	private boolean isPortInPortGroup(Port port, PortGroup portGroup) {
		return SetUtils.getObjects(portGroup).stream().anyMatch(p -> p.getName().equals(port.getName()));
	}
	
	private static void assertWithPrecision(double expected, double actual, double precision) {
        double lowerBound = expected - (expected * precision);
        double upperBound = expected + (expected * precision);

        Assertions.assertTrue(actual >= lowerBound && actual <= upperBound,
                "Value not within expected precision range. Expected: " + expected +
                        ", Actual: " + actual + ", Precision: ±" + precision*100 + "%");
    }
}
