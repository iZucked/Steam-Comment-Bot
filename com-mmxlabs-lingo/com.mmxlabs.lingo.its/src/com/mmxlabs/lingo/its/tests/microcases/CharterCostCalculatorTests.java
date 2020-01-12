/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.CharterRateToCharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.WeightedAverageCharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;

/***
 * Note: "run as: JUnit plug-in ITS test" otherwise will not inject all classes needed and will fail.
 */
@ExtendWith(ShiroRunner.class)
public class CharterCostCalculatorTests extends AbstractMicroTestCase {
	
	private CharterCurve charterCurve;

	public static Collection<?> getTestParameters() {
		return Arrays.asList(new Object[][] {
			{ WeightedAverageCharterCostCalculator.class },
			{ CharterRateToCharterCostCalculator.class }
		});
	}
	
	
	public void evaluateTest(@Nullable final Consumer<OptimisationPlan> tweaker, @Nullable final Function<LNGScenarioRunner, IRunnerHook> runnerHookFactory,
			@NonNull final Consumer<LNGScenarioRunner> checker, @NonNull Class<? extends @NonNull ICharterCostCalculator> charterCostImplClass) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		IOptimiserInjectorService optimiserInjectorService = OptimiserInjectorServiceMaker.begin().withModuleOverrideBind(ModuleType.Module_LNGTransformerModule, ICharterCostCalculator.class, charterCostImplClass).make();
		
		LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlanCustomiser(tweaker) //
				.withRunnerHookFactory(runnerHookFactory) //
				.withUserSettings(userSettings) //
				.withOptimiserInjectorService(optimiserInjectorService)
				.withThreadCount(getThreadCount()) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, checker);
		} finally {
			runnerBuilder.dispose();
		}
	}	
	
	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCargoSameMonthCharterCost(@NonNull Class<? extends ICharterCostCalculator> charterCostImplClass) throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();
		
		evaluateTest(null, null, scenarioRunner -> {
			validateCharterCosts(scenarioRunner, 0, charterCostImplClass);
		}, charterCostImplClass);
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCargoSameMonthZeroDurationCharterCost(@NonNull Class<? extends ICharterCostCalculator> charterCostImplClass) throws Exception {
		// map into same timezone to make expectations easier
		portModelBuilder.setAllExistingPortsToUTC();

		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Dominion Cove Point LNG");

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 1, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 1), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();
		
		evaluateTest(null, null, scenarioRunner -> {
			validateCharterCosts(scenarioRunner, 0, charterCostImplClass);
		}, charterCostImplClass);
	}
	
	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCargoDifferentMonthTimezoneCharterCost(@NonNull Class<? extends ICharterCostCalculator> charterCostImplClass) throws Exception {
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Ogishima - Tokyo Gas LNG Berth");

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 31, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 31), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();
		
		evaluateTest(null, null, scenarioRunner -> {
			validateCharterCosts(scenarioRunner, 0, charterCostImplClass);
		}, charterCostImplClass);
	}

	@Tag(TestCategories.MICRO_TEST)
	@ParameterizedTest(name = "{0}")
	@MethodSource("getTestParameters")
	public void testCargoZeroDurationDifferentMonthTimezoneCharterCost(@NonNull Class<? extends ICharterCostCalculator> charterCostImplClass) throws Exception {
		@NonNull
		final Port port1 = portFinder.findPort("Point Fortin");

		@NonNull
		final Port port2 = portFinder.findPort("Ogishima - Tokyo Gas LNG Berth");

		// Create the required basic elements
		final CharterInMarket charterInMarket_1 = createChartInMarket();
		
		// Set distance and speed to exact multiple -- quickest travel time is 100 hours
		scenarioModelBuilder.getDistanceModelBuilder().setPortToPortDistance(port1, port2, 1500, 2000, 2000, true);

		final LocalDateTime dischargeDate = LocalDateTime.of(2015, 12, 31, 0, 0, 0).plusHours(24 + 100);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", LocalDate.of(2015, 12, 31), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(charterInMarket_1, 0, 0) 
				.build();
		
		evaluateTest(null, null, scenarioRunner -> {
			validateCharterCosts(scenarioRunner, 0, charterCostImplClass);
		}, charterCostImplClass);
	}
	
	private void validateCharterCosts(LNGScenarioRunner scenarioRunner, long expectedPnL, @NonNull Class<? extends ICharterCostCalculator> charterCostImplClass) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		
		// Check spot index has been updated
		final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule();
		
		//Check charter costs for each event correct.
		long expectedTotalCharterCost = 0;
		ZonedDateTime voyagePlanStart = null;
		
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				if (charterCostImplClass.equals(WeightedAverageCharterCostCalculator.class)) {
					expectedTotalCharterCost += validateWeightedAverageEventCharterCost(event);
				}
				else {
					if (event instanceof SlotVisit) {
						SlotVisit sv = (SlotVisit)event;
						if (sv.getSlotAllocation().getSlot() instanceof LoadSlot) {
							voyagePlanStart = sv.getStart();
						}
					}
					if (voyagePlanStart != null) {
						expectedTotalCharterCost += validateFixedDailyRateEventCharterCost(voyagePlanStart, event);
					}
				}
			}
		}
		
		//Check overall calculated charter cost correct.
		long totalCharterCost = getTotalVoyagePlansCharterCost(scenarioToOptimiserBridge);
		Assertions.assertEquals(expectedTotalCharterCost, totalCharterCost, 1);   
	}
	
	private class CharterCostEvaluator implements BiConsumer<Injector, AnnotatedSolution> {
		long charterCost;
		
		public CharterCostEvaluator() {
			charterCost = 0;
		}
		
		@Override
		public void accept(Injector injector, AnnotatedSolution annotatedSolution) {
			@NonNull
			final IEvaluationState evaluationState = annotatedSolution.getEvaluationState();

			@NonNull
			final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
			Assertions.assertNotNull(volumeAllocatedSequences);
			
			ShippingCostHelper shippingCostHelper = new ShippingCostHelper();
			
			for (VolumeAllocatedSequence vas : volumeAllocatedSequences) {
				var vps = vas.getVoyagePlans();
				for (var vp : vps) {
					charterCost += shippingCostHelper.getHireCosts(vp.getFirst());
				}
			}
		}
		
		public long getCharterCost() {
			return charterCost;
		}
	};
	
	private long getTotalVoyagePlansCharterCost(final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge) {
		final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
		CharterCostEvaluator charterCostEvaluator = new CharterCostEvaluator();
		MicroTestUtils.evaluateState(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences, charterCostEvaluator);
		return OptimiserUnitConvertor.convertToExternalDailyCost(charterCostEvaluator.getCharterCost());
	}
	
	private long validateFixedDailyRateEventCharterCost(ZonedDateTime voyagePlanStart, Event event) {

		//Convert charterCurve to a map.
		List<YearMonthPoint> points = charterCurve.getPoints();
		TreeMap<LocalDateTime, Long> localDateToCharterCost = new TreeMap<LocalDateTime, Long>();
		
		for (YearMonthPoint point : points) {
			int pointMonth = point.getDate().getMonthValue();
			int pointYear = point.getDate().getYear();
			LocalDateTime pointDate = LocalDateTime.of(pointYear,pointMonth,1,0,0,0);
			localDateToCharterCost.put(pointDate, (long)point.getValue());
		}

		//Get localDates for start and end date.
		ZonedDateTime start = event.getStart().withZoneSameInstant(ZoneId.of("UTC"));
		LocalDateTime startDate = start.toLocalDateTime();
		int duration = event.getDuration();
		LocalDateTime endDate = startDate.plus(duration, ChronoUnit.HOURS);
		
		//Compute charter cost hour by hour, by adding charter cost per day per hour and then dividing by 24 at the end to correct amount.
		long expectedCharterCost = 0;
		LocalDateTime charterRatePriceDate = voyagePlanStart.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
		Entry<LocalDateTime, Long> charterCostEntry = localDateToCharterCost.floorEntry(charterRatePriceDate);
		long charterRatePerDay = (charterCostEntry == null ? 0 : charterCostEntry.getValue());
		
		for (LocalDateTime t = startDate; t.isBefore(endDate); t = t.plus(1, ChronoUnit.HOURS)) {
			expectedCharterCost += charterRatePerDay;
		}
		expectedCharterCost /= 24;
		Assertions.assertEquals(expectedCharterCost, event.getCharterCost());
		
		return expectedCharterCost;
	}
	
	
	private long validateWeightedAverageEventCharterCost(Event event) {

		//Convert charterCurve to a map.
		List<YearMonthPoint> points = charterCurve.getPoints();
		TreeMap<LocalDateTime, Long> localDateToCharterCost = new TreeMap<LocalDateTime, Long>();
		
		for (YearMonthPoint point : points) {
			int pointMonth = point.getDate().getMonthValue();
			int pointYear = point.getDate().getYear();
			LocalDateTime pointDate = LocalDateTime.of(pointYear,pointMonth,1,0,0,0);
			localDateToCharterCost.put(pointDate, (long)point.getValue());
		}

		//Get localDates for start and end date.
		ZonedDateTime start = event.getStart().withZoneSameInstant(ZoneId.of("UTC"));
		LocalDateTime startDate = start.toLocalDateTime();
		int duration = event.getDuration();
		LocalDateTime endDate = startDate.plus(duration, ChronoUnit.HOURS);
		
		//Compute charter cost hour by hour, by adding charter cost per day per hour and then dividing by 24 at the end to correct amount.
		long expectedCharterCost = 0;
		for (LocalDateTime t = startDate; t.isBefore(endDate); t = t.plus(1, ChronoUnit.HOURS)) {
			Entry<LocalDateTime, Long> charterCostEntry = localDateToCharterCost.floorEntry(t);
			if (charterCostEntry != null) {
				long charterRatePerDay = charterCostEntry.getValue();
				expectedCharterCost += charterRatePerDay;
			}
		}
		expectedCharterCost /= 24;
		Assertions.assertEquals(expectedCharterCost, event.getCharterCost());
		
		return expectedCharterCost;
	}
	
	private CharterInMarket createChartInMarket() {
		this.charterCurve = this.pricingModelBuilder.makeCharterDataCurve("TestCharterCurve1", "$", "day")
				.addIndexPoint(YearMonth.of(2015, 12), 100000)
				.addIndexPoint(YearMonth.of(2016, 1), 500000)
				.addIndexPoint(YearMonth.of(2016, 2), 1000000).build();
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setMaxSpeed(15.0);
		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "TestCharterCurve1", 1);	
		charterInMarket_1.setNominal(false);
		return charterInMarket_1;
	}
}