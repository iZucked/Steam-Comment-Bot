/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;

/***
 * Note: "run as: JUnit plug-in ITS test" otherwise will not inject all classes needed and will fail.
 */
@ExtendWith(ShiroRunner.class)
public class WeightedAverageCharterRateCharteringCostTests extends AbstractMicroTestCase {
	
	private CharterCurve charterCurve;
	
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoOnVessel() throws Exception {
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
			validateCharterCosts(scenarioRunner, 0);
		});
	}

	private long calculateInternalPnl(LNGScenarioRunner scenarioRunner) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

		@NonNull
		IModifiableSequences initialSequences = new ModifiableSequences(scenarioToOptimiserBridge.getDataTransformer().getInitialSequences());

		long[] metrics = MicroTestUtils.evaluateMetrics(scenarioToOptimiserBridge.getDataTransformer(), initialSequences);

		long internalPnl = metrics[MetricType.PNL.ordinal()];
			
		return internalPnl;
	}

	private void validateCharterCosts(LNGScenarioRunner scenarioRunner, long expectedPnL) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		
		// Check spot index has been updated
		final IScenarioDataProvider optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

		final Schedule schedule = ScenarioModelUtil.getScheduleModel(optimiserScenario).getSchedule();
		
		//Check charter costs for each event correct.
		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				validateEventCharterCost(event);
			}
		}
		
		//Check overall calculated charter cost correct.
		validateOverallCharterCost();
		
		//Check Pnl is as expected.
		//long pnl = calculateInternalPnl(scenarioRunner);
		//Assertions.assertEquals(expectedPnL, pnl);    
	}

	
	
	private void validateOverallCharterCost() {
		//TODO.
	}
	
	private void validateEventCharterCost(Event event) {

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
		long charterCost = 0;
		for (LocalDateTime t = startDate; t.isBefore(endDate); t = t.plus(1, ChronoUnit.HOURS)) {
			Entry<LocalDateTime, Long> charterCostEntry = localDateToCharterCost.floorEntry(t);
			if (charterCostEntry != null) {
				long charterRatePerDay = charterCostEntry.getValue();
				charterCost += charterRatePerDay;
			}
		}
		charterCost /= 24;
		Assertions.assertEquals(charterCost, event.getCharterCost());
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