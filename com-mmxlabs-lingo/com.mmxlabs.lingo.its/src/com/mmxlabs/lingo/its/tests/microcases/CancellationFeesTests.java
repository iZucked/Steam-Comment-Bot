/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class CancellationFeesTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testLoadSlotCancellationFee() throws Exception {

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.withCancellationFee("1000") //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assert.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assert.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			final long cancellationFees = ScheduleModelKPIUtils.getCancellationFees(openSlotAllocation);
			Assert.assertEquals(1000L, cancellationFees);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDischargeSlotCancellationFee() throws Exception {

		final DischargeSlot loadSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 9), portFinder.findPort("Sakai"), null, entity, "5") //
				.withCancellationFee("1000") //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assert.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assert.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			final long cancellationFees = ScheduleModelKPIUtils.getCancellationFees(openSlotAllocation);
			Assert.assertEquals(1000L, cancellationFees);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testLoadSlotContractCancellationFee() throws Exception {

		final PurchaseContract contract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase", entity, "5");
		contract.setCancellationExpression("1000");

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPort("Point Fortin"), contract, null, null, null) //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assert.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assert.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			final long cancellationFees = ScheduleModelKPIUtils.getCancellationFees(openSlotAllocation);
			Assert.assertEquals(1000L, cancellationFees);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDischargeSlotContractCancellationFee() throws Exception {

		final SalesContract contract = commercialModelBuilder.makeExpressionSalesContract("Sales", entity, "5");
		contract.setCancellationExpression("1000");

		final DischargeSlot loadSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 9), portFinder.findPort("Sakai"), contract, null, null) //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assert.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assert.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			final long cancellationFees = ScheduleModelKPIUtils.getCancellationFees(openSlotAllocation);
			Assert.assertEquals(1000L, cancellationFees);

		});
	}

}
