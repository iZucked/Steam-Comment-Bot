/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class HedgeValueTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLoadSlotHedgeValue() throws Exception {

		final LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.withHedgeValue(1000) //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
			// Check single cargo
			Assertions.assertEquals(1, optimiserScenario.getCargoModel().getLoadSlots().size());

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assertions.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assertions.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			Assertions.assertEquals(1000L, ScheduleModelKPIUtils.getHedgeValue(openSlotAllocation));
			Assertions.assertEquals(1000L, ScheduleModelKPIUtils.getElementPNL(openSlotAllocation));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDischargeSlotHedgeValue() throws Exception {

		final DischargeSlot loadSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 9), portFinder.findPort("Sakai"), null, entity, "5") //
				.withHedgeValue(1000) //
				.withOptional(true) //
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assertions.assertNotNull(schedule);

			final List<OpenSlotAllocation> openSlotAllocations = schedule.getOpenSlotAllocations();

			Assertions.assertEquals(1, openSlotAllocations.size());
			final OpenSlotAllocation openSlotAllocation = openSlotAllocations.get(0);

			Assertions.assertEquals(1000L, ScheduleModelKPIUtils.getHedgeValue(openSlotAllocation));
			Assertions.assertEquals(1000L, ScheduleModelKPIUtils.getElementPNL(openSlotAllocation));

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testCargoHedgeValue() throws Exception {

		final Cargo cargo = cargoModelBuilder.makeCargo() //

				.makeDESPurchase("L", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 9), portFinder.findPort("Sakai"), null, entity, "5", null) //
				.withHedgeValue(1000) //
				.build()//

				.makeDESSale("D", LocalDate.of(2015, 12, 9), portFinder.findPort("Sakai"), null, entity, "5") //
				.withHedgeValue(1500) //
				.build() //

				// Make cargo
				.build();

		evaluateTest(null, null, scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assertions.assertNotNull(schedule);

			final List<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();

			Assertions.assertEquals(1, cargoAllocations.size());
			final CargoAllocation cargoAllocation = cargoAllocations.get(0);

			Assertions.assertEquals(1000L + 1500L, ScheduleModelKPIUtils.getHedgeValue(cargoAllocation));

			final long pnl = ScheduleModelKPIUtils.getElementPNL(cargoAllocation);
			Assertions.assertEquals(1000L + 1500L, pnl);

		});
	}

}
