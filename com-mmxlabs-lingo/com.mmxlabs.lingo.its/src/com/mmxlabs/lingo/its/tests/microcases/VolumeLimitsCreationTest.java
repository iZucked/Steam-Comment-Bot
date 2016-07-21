/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;

/**
 * Test cases to ensure slot volume limits are created correctly.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class VolumeLimitsCreationTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void testFobPurchase_M3() throws Exception {

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobPurchase_MMBTU() throws Exception {

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_M3() throws Exception {

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null)
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_MMBTU() throws Exception {

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDesSale_M3() throws Exception {

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5")
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDesSale_MMBTU() throws Exception {

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5")//
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobSale_M3() throws Exception {

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", null)//
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobSale_MMBTU() throws Exception {

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", null)//
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobPurchase_M3_Contract() throws Exception {

		final PurchaseContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionPurchaseContract("Contract", entity, "5");
		contract.setMinQuantity(5_000);
		contract.setMaxQuantity(10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.M3);
		contract.setCargoCV(20.0);

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), contract, null, null, null).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobPurchase_MMBTU_Contract() throws Exception {

		final PurchaseContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionPurchaseContract("Contract", entity, "5");
		contract.setMinQuantity(20 * 5_000);
		contract.setMaxQuantity(20 * 10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		contract.setCargoCV(20.0);

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), contract, null, null, null).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_M3_Contract() throws Exception {

		final PurchaseContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionPurchaseContract("Contract", entity, "5");
		contract.setMinQuantity(5_000);
		contract.setMaxQuantity(10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.M3);
		contract.setCargoCV(20.0);

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), contract, null, null, null, null).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDESPurchase_MMBTU_Contract() throws Exception {
		final PurchaseContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionPurchaseContract("Contract", entity, "5");
		contract.setMinQuantity(20 * 5_000);
		contract.setMaxQuantity(20 * 10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		contract.setCargoCV(20.0);
		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), contract, null, null, null, null).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDesSale_M3_Contract() throws Exception {
		final SalesContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionSalesContract("Contract", entity, "5");
		contract.setMinQuantity(5_000);
		contract.setMaxQuantity(10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.M3);

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), contract, null, null) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testDesSale_MMBTU_Contract() throws Exception {
		final SalesContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionSalesContract("Contract", entity, "5");
		contract.setMinQuantity(20 * 5_000);
		contract.setMaxQuantity(20 * 10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.MMBTU);

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), contract, null, null)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobSale_M3_Contract() throws Exception {
		final SalesContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionSalesContract("Contract", entity, "5");
		contract.setMinQuantity(5_000);
		contract.setMaxQuantity(10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.M3);

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), contract, null, null, null)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testFobSale_MMBTU_Contract() throws Exception {
		final SalesContract contract = scenarioModelBuilder.getCommercialModelBuilder().makeExpressionSalesContract("Contract", entity, "5");
		contract.setMinQuantity(20 * 5_000);
		contract.setMaxQuantity(20 * 10_000);
		contract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), contract, null, null, null)//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	///////////////// Existing Spot cargoes

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobPurchase_M3_Existing() throws Exception {

		final FOBPurchasesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBPurchaseMarket("Market", portFinder.findPort("Point Fortin"), entity, "5", 20.0)
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3).build();

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeSpotFOBPurchase("L1", YearMonth.of(2016, 7), market).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobPurchase_MMBTU_Existing() throws Exception {
		final FOBPurchasesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBPurchaseMarket("Market", portFinder.findPort("Point Fortin"), entity, "5", 20.0)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU).build();

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeSpotFOBPurchase("L1", YearMonth.of(2016, 7), market).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDESPurchase_M3_Existing() throws Exception {

		final DESPurchaseMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESPurchaseMarket("Market", Collections.singletonList(portFinder.findPort("Sakai")), entity, "5", 20.0)
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3).build();

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeSpotDESPurchase("L1", YearMonth.of(2016, 7), market, portFinder.findPort("Sakai")).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDESPurchase_MMBTU_Existing() throws Exception {
		final DESPurchaseMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESPurchaseMarket("Market", Collections.singletonList(portFinder.findPort("Sakai")), entity, "5", 20.0)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU).build();

		@NonNull
		final LoadSlot e_loadSlot = cargoModelBuilder.makeSpotDESPurchase("L1", YearMonth.of(2016, 7), market, portFinder.findPort("Sakai")).build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDesSale_M3_Existing() throws Exception {

		final DESSalesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESSaleMarket("Market", portFinder.findPort("Sakai"), entity, "5")
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3).build();

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeSpotDESSale("D1", YearMonth.of(2016, 7), market) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDesSale_MMBTU_Existing() throws Exception {

		final DESSalesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESSaleMarket("Market", portFinder.findPort("Sakai"), entity, "5")
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU).build();

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeSpotDESSale("D1", YearMonth.of(2016, 7), market) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobSale_M3_Existing() throws Exception {

		final FOBSalesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBSaleMarket("Market", Collections.singletonList(portFinder.findPort("Point Fortin")), entity, "5")
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3).build();

		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeSpotFOBSale("D1", YearMonth.of(2016, 7), market, portFinder.findPort("Point Fortin"))//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobSale_MMBTU_Existing() throws Exception {
		final FOBSalesMarket market = scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBSaleMarket("Market", Collections.singletonList(portFinder.findPort("Point Fortin")), entity, "5")
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU).build();
		@NonNull
		final DischargeSlot e_DischargeSlot = cargoModelBuilder.makeSpotFOBSale("D1", YearMonth.of(2016, 7), market, portFinder.findPort("Point Fortin"))//
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	///////////////// Generated Spot cargoes

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobPurchase_M3_Generated() throws Exception {

		scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBPurchaseMarket("Market", portFinder.findPort("Point Fortin"), entity, "5", 20.0).withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5");

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final LoadSlot e_loadSlot = modelEntityMap.getAllModelObjects(SpotLoadSlot.class).iterator().next();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobPurchase_MMBTU_Generated() throws Exception {
		scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBPurchaseMarket("Market", portFinder.findPort("Point Fortin"), entity, "5", 20.0)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5");

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();

			final LoadSlot e_loadSlot = modelEntityMap.getAllModelObjects(SpotLoadSlot.class).iterator().next();

			final ILoadSlot o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadSlot.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDESPurchase_M3_Generated() throws Exception {

		scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESPurchaseMarket("Market", Collections.singletonList(portFinder.findPort("Sakai")), entity, "5", 20.0)
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5");

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final LoadSlot e_loadSlot = modelEntityMap.getAllModelObjects(SpotLoadSlot.class).iterator().next();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertTrue(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDESPurchase_MMBTU_Generated() throws Exception {
		scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESPurchaseMarket("Market", Collections.singletonList(portFinder.findPort("Sakai")), entity, "5", 20.0)
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU) //
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeDESSale("D1", LocalDate.of(2016, 7, 20), portFinder.findPort("Sakai"), null, entity, "5");

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final LoadSlot e_loadSlot = modelEntityMap.getAllModelObjects(SpotLoadSlot.class).iterator().next();

			final ILoadOption o_loadSlot = modelEntityMap.getOptimiserObjectNullChecked(e_loadSlot, ILoadOption.class);

			Assert.assertFalse(o_loadSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_loadSlot.getMinLoadVolume());
			Assert.assertEquals(10_000_000, o_loadSlot.getMaxLoadVolume());
			Assert.assertEquals(20 * 5_000_000, o_loadSlot.getMinLoadVolumeMMBTU());
			Assert.assertEquals(20 * 10_000_000, o_loadSlot.getMaxLoadVolumeMMBTU());
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDesSale_M3_Generated() throws Exception {

		scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESSaleMarket("Market", portFinder.findPort("Sakai"), entity, "5")//
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final DischargeSlot e_DischargeSlot = modelEntityMap.getAllModelObjects(SpotDischargeSlot.class).iterator().next();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());
			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotDesSale_MMBTU_Generated() throws Exception {

		scenarioModelBuilder.getSpotMarketsModelBuilder().makeDESSaleMarket("Market", portFinder.findPort("Sakai"), entity, "5") //
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final DischargeSlot e_DischargeSlot = modelEntityMap.getAllModelObjects(SpotDischargeSlot.class).iterator().next();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobSale_M3_Generated() throws Exception {

		scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBSaleMarket("Market", Collections.singletonList(portFinder.findPort("Point Fortin")), entity, "5")
				.withVolumeLimits(5_000, 10_000, VolumeUnits.M3)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final DischargeSlot e_DischargeSlot = modelEntityMap.getAllModelObjects(SpotDischargeSlot.class).iterator().next();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertTrue(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSpotFobSale_MMBTU_Generated() throws Exception {
		scenarioModelBuilder.getSpotMarketsModelBuilder().makeFOBSaleMarket("Market", Collections.singletonList(portFinder.findPort("Point Fortin")), entity, "5")
				.withVolumeLimits(20 * 5_000, 20 * 10_000, VolumeUnits.MMBTU)//
				.withAvailabilityConstant(1) //
				.build();

		cargoModelBuilder.makeFOBPurchase("L1", LocalDate.of(2016, 7, 20), portFinder.findPort("Point Fortin"), null, entity, "5", 20.0);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			@NonNull
			final LNGDataTransformer dataTransformer = scenarioToOptimiserBridge.getDataTransformer();

			final ModelEntityMap modelEntityMap = dataTransformer.getModelEntityMap();
			final DischargeSlot e_DischargeSlot = modelEntityMap.getAllModelObjects(SpotDischargeSlot.class).iterator().next();

			final IDischargeOption o_dischargeSlot = modelEntityMap.getOptimiserObjectNullChecked(e_DischargeSlot, IDischargeOption.class);

			Assert.assertFalse(o_dischargeSlot.isVolumeSetInM3());

			Assert.assertEquals(5_000_000, o_dischargeSlot.getMinDischargeVolume(20_000_000));
			Assert.assertEquals(10_000_000, o_dischargeSlot.getMaxDischargeVolume(20_000_000));

			Assert.assertEquals(20 * 5_000_000, o_dischargeSlot.getMinDischargeVolumeMMBTU(20_000_000));
			Assert.assertEquals(20 * 10_000_000, o_dischargeSlot.getMaxDischargeVolumeMMBTU(20_000_000));
		});
	}

}