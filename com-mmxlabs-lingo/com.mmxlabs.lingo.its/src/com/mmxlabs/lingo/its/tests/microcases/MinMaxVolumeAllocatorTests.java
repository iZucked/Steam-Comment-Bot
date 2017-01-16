/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Tests for the {@link MinMaxUnconstrainedVolumeAllocator}.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class MinMaxVolumeAllocatorTests extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_MaxTransfer_MMBTU() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU).build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU).build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));

			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	private void verifyNoCapacityViolations(final SimpleCargoAllocation cargoAllocation) {
		for (SlotAllocation slotAllocation : cargoAllocation.getCargoAllocation().getSlotAllocations()) {
			Assert.assertTrue(slotAllocation.getSlotVisit().getViolations().isEmpty());
		}
	}

	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_MaxTransfer_M3() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5", 20.0, null) //
				.withVolumeLimits(150_000, 200_000, VolumeUnits.M3).build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(175_000, 225_000, VolumeUnits.M3).build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(200_000 * 20, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(200_000 * 20, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_BreakEven() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU).build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU).build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));
			// Expect max-load for break-even
			Assert.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDES_Purchase_MinTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeDESPurchase("L1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7", 20.0, null) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(3_500_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(3_500_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_MaxTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU).build() //

				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU).build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_BreakEven() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU).build() //

				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU).build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));
			// Expect max-load for break-even
			Assert.assertEquals(4_000_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(4_000_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(4_000_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFOB_Sale_MinTransfer() throws Exception {

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 20.0) //
				.withVolumeLimits(3_000_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeFOBSale("D1", false, LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(3_500_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(3_500_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			Assert.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShipped_MaxTransfer() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		// Very large capacity to cover volume limits
		vesselClass.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0) //
				.withVolumeLimits(3_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(2_500_000, 3_500_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue (ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(3_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(3_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShipped_MinTransfer() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 20.0) //
				.withVolumeLimits(2_500_000, 4_500_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withVolumeLimits(2_500_000, 3_500_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue (ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(2_500_000, cargoAllocation.getDischargeAllocation().getEnergyTransferred());
			Assert.assertEquals(2_500_000 / 20, cargoAllocation.getDischargeAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShipped_MinTransfer_2() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 20.0) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withVolumeLimits(10, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue (ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(2_950_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(2_950_000 / 20, cargoAllocation.getLoadAllocation().getVolumeTransferred());

			verifyNoCapacityViolations(cargoAllocation);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShipped_MinTransfer_WithCVBasedRounding() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 23.7) //
				.withVolumeLimits(2_950_000, 4_000_000, VolumeUnits.MMBTU)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withVolumeLimits(10, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue (ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(2_950_000, cargoAllocation.getLoadAllocation().getEnergyTransferred());
			Assert.assertEquals(Math.floor(2_950_000.0 / 23.7), cargoAllocation.getLoadAllocation().getVolumeTransferred(), 0.01);

			verifyNoCapacityViolations(cargoAllocation);

		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShipped_MinTransferM3_WithCVBasedRounding() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		vesselClass.setCapacity(250_000);

		final CharterInMarket market = spotMarketsModelBuilder.createCharterInMarket("default", vesselClass, "100000", 1);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "7", 23.7) //
				.withVolumeLimits(150_000, 200_000, VolumeUnits.M3)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "5") //
				.withVolumeLimits(0, 4_000_000, VolumeUnits.MMBTU)//

				.build() //
				.withVesselAssignment(market, 0, 0) //
				.build();

		evaluateWithOverrides(new MinMaxVolumeModule(), null, scenarioRunner -> {

			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
			final Schedule schedule = scheduleModel.getSchedule();
			Assert.assertNotNull(schedule);
			Assert.assertEquals(1, schedule.getCargoAllocations().size());

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue (ScheduleModelUtils.matchingSlots(cargo1, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(150_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(150_000 * 23.7, cargoAllocation.getLoadAllocation().getEnergyTransferred(), 0.0);

			verifyNoCapacityViolations(cargoAllocation);

		});
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
						bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(MinMaxUnconstrainedVolumeAllocator.class);
					}
				});
			}

			return null;
		}
	}
}