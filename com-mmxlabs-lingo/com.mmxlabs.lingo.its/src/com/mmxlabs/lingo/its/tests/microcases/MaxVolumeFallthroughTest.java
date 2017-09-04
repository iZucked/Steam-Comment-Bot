/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * Test cases to ensure max volume fall through works. Fogbugz: 1024
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class MaxVolumeFallthroughTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_LoadSlotIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("PurchaseContract", entity, "5");
		purchaseContract.setMaxQuantity(14_000);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("SalesContract", entity, "7");
		salesContract.setMaxQuantity(14_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), purchaseContract, null, null, 20.0)//
				.withVolumeLimits(5_000, 15_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), salesContract, null, null) //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			final SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();

			Assert.assertEquals(15_000, loadAllocation.getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_DischargeSlotIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("PurchaseContract", entity, "5");
		purchaseContract.setMaxQuantity(14_000);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("SalesContract", entity, "7");
		salesContract.setMaxQuantity(14_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), purchaseContract, null, null, 20.0)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), salesContract, null, null) //
				.withVolumeLimits(5_000, 15_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			final SlotAllocation discahrgeAllocation = cargoAllocation.getDischargeAllocation();

			Assert.assertEquals(15_000, discahrgeAllocation.getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_LoadContractIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("PurchaseContract", entity, "5");
		purchaseContract.setMaxQuantity(14_000);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("SalesContract", entity, "7");
		salesContract.setMaxQuantity(14_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), purchaseContract, null, null, 20.0)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), salesContract, null, null) //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			final SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();

			Assert.assertEquals(14_000, loadAllocation.getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_DischargeContractIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("PurchaseContract", entity, "5");
		purchaseContract.setMaxQuantity(14_000);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("SalesContract", entity, "7");
		salesContract.setMaxQuantity(14_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), purchaseContract, null, null, 20.0)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), salesContract, null, null) //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(14_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_LoadMarketIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final FOBPurchasesMarket purchaseMarket = spotMarketsModelBuilder.makeFOBPurchaseMarket("PurchaseMarket", portFinder.findPort("Point Fortin"), entity, "5", 20.0) //
				.withVolumeLimits(0, 14_000, VolumeUnits.M3) //
				.build();
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("SalesContract", entity, "7");
		salesContract.setMaxQuantity(14_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeMarketFOBPurchase("L1", purchaseMarket, YearMonth.of(2016, 7), portFinder.findPort("Point Fortin"))//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), salesContract, null, null) //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			final SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();

			Assert.assertEquals(14_000, loadAllocation.getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_DischargeMarketIsLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(500_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("PurchaseContract", entity, "5");
		purchaseContract.setMaxQuantity(14_000);

		final DESSalesMarket salesMarket = spotMarketsModelBuilder.makeDESSaleMarket("SalesMarket", portFinder.findPort("Point Fortin"), entity, "5") //
				.withVolumeLimits(0, 14_000, VolumeUnits.M3) //
				.build();

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), purchaseContract, null, null, 20.0)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeMarketDESSale("D1", salesMarket, YearMonth.of(2016, 8), portFinder.findPort("Sakai")) //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(14_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_VesselIsLoadLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			final SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();

			Assert.assertEquals(15_000, loadAllocation.getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testShippedCargo_VesselIsDischargeLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0), null) //
				.build();

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			// Load volume is clamped.
			Assert.assertEquals(15_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertTrue(15_000 > cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedFOBCargo_VesselIsDischargeLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeFOBSale("D1", false, LocalDate.of(2016, 8, 20), portFinder.findPort("Point Fortin"), null, entity, "7", vessel) //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(15_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(15_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedFOBCargo_VesselIsLoadLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeFOBSale("D1", false, LocalDate.of(2016, 8, 20), portFinder.findPort("Point Fortin"), null, entity, "7", vessel) //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(15_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(15_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedFOBCargo_UseLoadLimit() throws Exception {
		
		final Cargo e_cargo = cargoModelBuilder.makeCargo() //
				
				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, 200_000, VolumeUnits.M3)//
				.build() //
				//
				.makeFOBSale("D1", false, LocalDate.of(2016, 8, 20), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();
		
		evaluateWithLSOTest(scenarioRunner -> {
			
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			
			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();
			
			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));
			
			Assert.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}
	@Test
	@Category({ MicroTest.class })
	public void testNonShippedFOBCargo_UseDischargeLimit() throws Exception {

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeFOBSale("D1", false, LocalDate.of(2016, 8, 20), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withVolumeLimits(5_000, 200_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedFOBCargo_140IsLimit() throws Exception {

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeFOBSale("D1", false, LocalDate.of(2016, 8, 20), portFinder.findPort("Point Fortin"), null, entity, "7", null) //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(140_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(140_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedDESCargo_VesselIsDischargeLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, vessel)//
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(15_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(15_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedDESCargo_VesselIsLoadLimit() throws Exception {

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		final Vessel vessel = fleetModelBuilder.createVessel("My Vessel", vesselClass);
		vessel.setCapacity(15_000);

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, vessel)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, 100_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(15_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(15_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedDESCargo_140IsLimit() throws Exception {
		
		final Cargo e_cargo = cargoModelBuilder.makeCargo() //
				
				.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();
		
		evaluateWithLSOTest(scenarioRunner -> {
			
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
			
			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();
			
			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));
			
			Assert.assertEquals(140_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(140_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}
	@Test
	@Category({ MicroTest.class })
	public void testNonShippedDESCargo_UseDischargeLimit() throws Exception {

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null)//
				.withVolumeLimits(5_000, 200_000, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testNonShippedDESCargo_UseLoadLimit() throws Exception {

		final Cargo e_cargo = cargoModelBuilder.makeCargo() //

				.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null)//
				.withVolumeLimits(5_000, null, VolumeUnits.M3)//
				.build() //
				//
				.makeDESSale("D1", LocalDate.of(2016, 8, 20), portFinder.findPort("Sakai"), null, entity, "7") //
				.withVolumeLimits(5_000, 200_000, VolumeUnits.M3)//
				.build() //
				//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final Schedule schedule = lngScenarioModel.getScheduleModel().getSchedule();

			final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
			Assert.assertTrue(ScheduleModelUtils.matchingSlots(e_cargo, cargoAllocation.getCargoAllocation()));

			Assert.assertEquals(200_000, cargoAllocation.getLoadAllocation().getVolumeTransferred());
			Assert.assertEquals(200_000, cargoAllocation.getDischargeAllocation().getVolumeTransferred());
		});
	}
}