/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
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
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;

/**
 * Test cases to ensure "standard" (not-divertible) FOB or DES cargoes are scheduled with the compatible time window.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class FOBDESTimeWindowsTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testExactDates() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
				.withWindowStartTime(0) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), dischargePort, null, entity, "7") //
				.withWindowStartTime(0) //
				.build() //
				.withAssignmentFlags(false, false) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final Schedule schedule = scenarioRunner.getSchedule();
			Assertions.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assertions.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, dischargePort.getZoneId());

			Assertions.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assertions.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOverlappingDates1() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 10), dischargePort, null, entity, "5", 22.8, null) //
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
			Assertions.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assertions.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, dischargePort.getZoneId());

			Assertions.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assertions.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testOverlappingDates2() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
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
			Assertions.assertNotNull(schedule);

			CargoAllocation cargoAllocation = null;
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				if (ScheduleModelUtils.matchingSlots(cargo1, ca)) {
					cargoAllocation = ca;
					break;
				}
			}
			Assertions.assertNotNull(cargoAllocation);

			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			final ZonedDateTime transferTime = ZonedDateTime.of(2015, 12, 11, 0, 0, 0, 0, dischargePort.getZoneId());

			Assertions.assertEquals(transferTime, sca.getLoadAllocation().getSlotVisit().getStart());
			Assertions.assertEquals(transferTime, sca.getDischargeAllocation().getSlotVisit().getStart());

		});
	}

	/**
	 * In this test the start of the sales window is the end (exclusive!) of the purchase window.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDateOutByOneError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
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

			ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assertions.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assertions.assertEquals(1, failedConstraintCheckers.size());
			Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
	}

	/**
	 * In this test the start of the sales window is just after the end (exclusive!) of the purchase window.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDateOutByTwoError() throws Exception {
		@NonNull
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		// final Cargo cargo1 =
		LoadSlot loadSlot = cargoModelBuilder // .makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 11), dischargePort, null, entity, "5", 22.8, null) //
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

			ISequences rawSequences = SequenceHelper.createFOBDESSequences(scenarioToOptimiserBridge.getDataTransformer().getInjector(), loadSlot, dischargeSlot);

			// Validate the initial sequences are invalid
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			Assertions.assertNotNull(failedConstraintCheckers);

			// Expect just this one to fail
			Assertions.assertEquals(1, failedConstraintCheckers.size());
			Assertions.assertTrue(failedConstraintCheckers.get(0) instanceof LatenessEvaluatedStateChecker);
		});
	}

}