/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;

/**
 * Test cases to ensure "standard" (not-divertable) FOB or DES cargoes are scheduled with the compatible time window.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class FOBDESTimeWindowsTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testExactDates() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testOverlappingDates1() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 10), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(2, TimePeriod.DAYS) //

				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testOverlappingDates2() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", null) //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 10), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(48, TimePeriod.HOURS) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assert.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assert.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, ZoneId.of(dischargePort.getTimeZone()));

			Assert.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assert.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	/**
	 * In this test the start of the sales window is the end (exclusive!) of the purchase window.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDateOutByOneError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.build();
		//
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2015, 12, 12), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge, loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
	}

	/**
	 * In this test the start of the sales window is just after the end (exclusive!) of the purchase window.
	 */
	@Test
	@Category({ MicroTest.class })
	public void testDateOutByTwoError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPort("Dominion Cove Point LNG");
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
				.withWindowStartTime(0) //
				.withWindowSize(23, TimePeriod.HOURS) //
				.build();
		//
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2015, 12, 12), dischargePort, null, entity, "7") //
				.withWindowStartTime(1) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge, loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assert.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assert.assertEquals(1, failedConstraintCheckers.size());
			Assert.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
	}

}