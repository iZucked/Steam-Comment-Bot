/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Tests for the {@link MinMaxUnconstrainedVolumeAllocator}.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class MinMaxVolumeAllocatorTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_MaxTransfer_MMBTU() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)
				.build() //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	private void verifyNoCapacityViolations(final SimpleCargoAllocation cargoAllocation) {
		for (final SlotAllocation slotAllocation : cargoAllocation.getCargoAllocation().getSlotAllocations()) {
			Assertions.assertTrue(slotAllocation.getSlotVisit().getViolations().isEmpty());
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_MaxTransfer_M3() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5", 20.0, null) //
				.withVolumeLimits(150_000, 200_000, VolumeUnits.M3)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(175_000, 225_000, VolumeUnits.M3)
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(200_000 * 20, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(200_000 * 20, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_BreakEven() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));
			// Expect max-load for break-even
			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_MinTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(3_500_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(3_500_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)

	public void testDES_Purchase_MinTransfer_withFCL_load() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_MinTransfer_withFCL_discharge() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOB_Sale_MaxTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOB_Sale_BreakEven() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));
			// Expect max-load for break-even
			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOB_Sale_MinTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(3_500_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(3_500_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOB_Sale_MinTransfer_withFCL_load() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testFOB_Sale_MinTransfer_withFCL_discharge() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MaxTransfer() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		// Very large capacity to cover volume limits
		vessel.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", 20.0) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(2_500_000, 3_500_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransfer() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(2_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(2_500_000, 3_500_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assertions.assertEquals(2_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransfer_2() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(10, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_950_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(2_950_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransfer_withFCL_load() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(100_000);
		vessel.setFillCapacity(1.0);
		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(1_800_000, 4_500_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(1_800_000, 3_500_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_000_000 - (vessel.getSafetyHeel() * 20), cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(2_000_000 / 20 - vessel.getSafetyHeel(), cargoAllocation.getLoadAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransfer_withFCL_discharge() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(100_000);
		vessel.setFillCapacity(1.0);
		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 20.0) //
				.withVolumeLimits(1_800_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(1_800_000, 3_500_000, VolumeUnits.MMBTU)//
				.with(s -> s.setFullCargoLot(true))//
				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_000_000 - (vessel.getSafetyHeel() * 20), cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(2_000_000 / 20 - vessel.getSafetyHeel(), cargoAllocation.getLoadAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransfer_WithCVBasedRounding() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 23.7) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(10, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_950_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assertions.assertEquals(Math.floor(2_950_000.0 / 23.7), cargoAllocation.getLoadAllocation().getVolumeTransferred(), 0.01);

			verifyNoCapacityViolations(cargoAllocation);

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testShipped_MinTransferM3_WithCVBasedRounding() throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vessel, entity, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "7", 23.7) //
				.withVolumeLimits(150_000, 200_000, VolumeUnits.M3)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(0, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(150_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assertions.assertEquals(150_000 * 23.7, cargoAllocation.getLoadAllocation().getEnergyTransferred(), 0.0001);

			verifyNoCapacityViolations(cargoAllocation);

		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_CPVolume_MinLoadVolume_Match() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5", 20.0, null)//
				.with(s -> ((LoadSlot) s).setVolumeCounterParty(true))//
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(2_500_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(2_950_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());

			Assertions.assertEquals(2_950_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_CPVolume_MaxLoadVolume_Match() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.with(s -> ((LoadSlot) s).setVolumeCounterParty(true))//
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(2_500_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());

			Assertions.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_CPVolume_MinLoadVolume_UnMatch() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5", 20.0, null) //
				.with(s -> ((LoadSlot) s).setVolumeCounterParty(true))//
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(3_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());

			Assertions.assertEquals(3_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());

			Assertions.assertTrue(verifySomeCapacityViolations(cargoAllocation));
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testDES_Purchase_CPVolume_MaxLoadVolume_UnMatch() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7", 20.0, null) //
				.with(s -> ((LoadSlot) s).setVolumeCounterParty(true))
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "5") //
				.withVolumeLimits(2_500_000, 3_900_000, VolumeUnits.MMBTU)
				.build() //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assertions.assertEquals(3_900_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());

			Assertions.assertEquals(3_900_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());

			Assertions.assertTrue(verifySomeCapacityViolations(cargoAllocation));
		});
	}

	enum LDDCases {
		MAX_D1(true, false), MIN_D1(true, false), MIN_D1_FCL(true, false), MAX_D2(false, true), MIN_D2(false, true), MIN_D2_FCL(false, true);

		private LDDCases(boolean d1range, boolean d2range) {
			this.d1range = d1range;
			this.d2range = d2range;
		}

		private boolean d1range;
		private boolean d2range;

		public boolean isD1Range() {
			return d1range;
		}

		public boolean isD2Range() {
			return d2range;
		}
	}

	/**
	 * Test LDD cargo volume allocation with one floating volume range.
	 * 
	 * @throws Exception
	 */
	@ParameterizedTest
	@EnumSource(LDDCases.class)
	@Tag(TestCategories.MICRO_TEST)
	public void testLDDWithOneDischargeVolumeRange(final LDDCases params) throws Exception {

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_MEGI_176);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vessel, entity).build();

		// final Cargo cargo1 = cargoModelBuilder.makeCargo() //
		final var l1 = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2023, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_BONNY), null, entity, "1", 20.0) //
				.withVolumeLimits(2_000_000, 4_000_000, VolumeUnits.MMBTU)
				.build();

		final var d1 = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2023, 2, 15), portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, entity, "5") //
				.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU)
				.build();

		final var d2 = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2023, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "5") //
				.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU)
				.build();

		// Configure slots for test paramaters
		if (params == LDDCases.MAX_D1 || params == LDDCases.MAX_D2) {
			// Low price to for max purchase
			l1.setPriceExpression("1");
		} else {
			// High price should force min purchase and sales volumes (unless FCL)
			l1.setPriceExpression("10");
		}

		// choose which slot has the floating range
		if (params.isD1Range()) {
			d1.setMaxQuantity(2_000_000);
		}
		if (params.isD2Range()) {
			d2.setMaxQuantity(2_000_000);
		}

		// Set FCL if required
		if (params == LDDCases.MIN_D1_FCL) {
			d1.setFullCargoLot(true);
		}
		if (params == LDDCases.MIN_D2_FCL) {
			d2.setFullCargoLot(true);
		}

		cargoModelBuilder.makeCargo()
				.withSlot(l1)
				.withSlot(d1)
				.withSlot(d2)
				.withVesselAssignment(vesselCharter, 1) //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assertions.assertNotNull(schedule);
			Assertions.assertEquals(1, schedule.getCargoAllocations().size());

			final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);

			final SlotAllocation loadSA = cargoAllocation.getSlotAllocations().get(0);
			final SlotAllocation discharge1SA = cargoAllocation.getSlotAllocations().get(1);
			final SlotAllocation discharge2SA = cargoAllocation.getSlotAllocations().get(2);

			// Assertions.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			switch (params) {
			case MAX_D1, MIN_D1_FCL: {
				Assertions.assertEquals(2_000_000, discharge1SA.getEnergyTransferred());
				Assertions.assertEquals(1_000_000, discharge2SA.getEnergyTransferred());
				break;
			}
			case MAX_D2, MIN_D2_FCL: {
				Assertions.assertEquals(1_000_000, discharge1SA.getEnergyTransferred());
				Assertions.assertEquals(2_000_000, discharge2SA.getEnergyTransferred());
				break;
			}
			case MIN_D1, MIN_D2: {
				Assertions.assertEquals(1_000_000, discharge1SA.getEnergyTransferred());
				Assertions.assertEquals(1_000_000, discharge2SA.getEnergyTransferred());
				break;
			}
			default:
				Assertions.fail();
			}
			Assertions.assertTrue(2_000_000 <= loadSA.getEnergyTransferred());
		});
	}

	private boolean verifySomeCapacityViolations(final SimpleCargoAllocation cargoAllocation) {
		for (final SlotAllocation slotAllocation : cargoAllocation.getCargoAllocation().getSlotAllocations()) {
			if (!slotAllocation.getSlotVisit().getViolations().isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Module to set the required volume allocator code.
	 * 
	 */
	private class MinMaxVolumeModule implements IOptimiserInjectorService {

		@Override
		public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			return null;
		}

		@Override
		public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

			if (moduleType == ModuleType.Module_LNGTransformerModule) {
				return Collections.<@NonNull Module> singletonList(new AbstractModule() {

					@Override
					protected void configure() {
						bind(IVolumeAllocator.class).to(MinMaxUnconstrainedVolumeAllocator.class);
					}
				});
			}

			return null;
		}
	}
}