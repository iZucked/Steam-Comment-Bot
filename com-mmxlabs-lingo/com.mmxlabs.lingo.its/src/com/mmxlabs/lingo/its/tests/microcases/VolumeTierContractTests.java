/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.core.runtime.IStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.validation.ValidationTestUtil;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.lng.commercial.validation.VolumeTierContractConstraint;
import com.mmxlabs.models.lng.commercial.validation.VolumeTierSlotParamsConstraint;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * TODO: These tests do not check time intervals are correct.
 */

@ExtendWith(value = ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_EXPOSURES })
public class VolumeTierContractTests extends AbstractMicroTestCase {

	/**
	 * Basic test. Purchase contract loading 2x threshold amount, but same price for
	 * each side of the tier. We expect the blended price to be the same as the
	 * inputs
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicPurchaseContract1() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		Assertions.assertEquals(1, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractExposures1() {

		pricingModelBuilder.makeCommodityDataCurve("lowprice", "$", "mmBTU") //
				.addIndexPoint(YearMonth.of(2022, 10), 2.0) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("highprice", "$", "mmBTU") //
				.addIndexPoint(YearMonth.of(2022, 10), 10.0) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "lowprice", "highprice", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		((LoadSlot) load).setCargoCV(20.0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		Assertions.assertEquals(2 + 1, exposures.size());
		{
			final Optional<ExposureDetail> lowDetail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "lowprice")).findFirst();
			Assertions.assertTrue(lowDetail.isPresent());
			Assertions.assertEquals(-70000 * 20.0, lowDetail.get().getVolumeInMMBTU(), 0.001);
		}
		{
			final Optional<ExposureDetail> highDetail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "highprice")).findFirst();
			Assertions.assertTrue(highDetail.isPresent());
			Assertions.assertEquals(-70000 * 20.0, highDetail.get().getVolumeInMMBTU(), 0.001);
		}

	}

	/**
	 * Exposures with a shift to check this is correctly propogated
	 */

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractExposures2() {

		pricingModelBuilder.makeCommodityDataCurve("lowprice", "$", "mmBTU") //
				.addIndexPoint(YearMonth.of(2022, 10), 2.0) //
				.build();
		pricingModelBuilder.makeCommodityDataCurve("highprice", "$", "mmBTU") //
				.addIndexPoint(YearMonth.of(2022, 10), 10.0) //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "lowprice", "SHIFT(highprice, 1)", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		((LoadSlot) load).setCargoCV(20.0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);

		final List<ExposureDetail> exposures = simpleCargoAllocation.getLoadAllocation().getExposures();
		Assertions.assertEquals(2 + 1, exposures.size());
		{
			final Optional<ExposureDetail> lowDetail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "lowprice")).findFirst();
			Assertions.assertTrue(lowDetail.isPresent());
			Assertions.assertEquals(-70000 * 20.0, lowDetail.get().getVolumeInMMBTU(), 0.001);
			Assertions.assertEquals(YearMonth.of(2018, 6), lowDetail.get().getDate());

		}
		{
			final Optional<ExposureDetail> highDetail = exposures.stream().filter(d -> d.getDealType() == DealType.FINANCIAL).filter(d -> Objects.equals(d.getIndexName(), "highprice")).findFirst();
			Assertions.assertTrue(highDetail.isPresent());
			Assertions.assertEquals(-70000 * 20.0, highDetail.get().getVolumeInMMBTU(), 0.001);
			Assertions.assertEquals(YearMonth.of(2018, 5), highDetail.get().getDate());
		}

	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicPurchaseContract2() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "2", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		// Expect price to be (1 + 2) / 2 as volumes are the same
		Assertions.assertEquals(1.5, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicPurchaseContract3() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "2", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		// Expect price to be (1*70k + 7*35K) / 105k
		Assertions.assertEquals(1.33, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.01);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractWithSlotParams() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "2", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(true);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		load.getExtensions().add(params);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedPrice = (3.0 * 50_000.0 + 4.0 * 55_000.0) / 105_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicPurchaseContractWithDisabledSlotParams() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "2", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(false);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		load.getExtensions().add(params);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		// Expect price to be (1*70k + 7*35K) / 105k
		Assertions.assertEquals(1.33, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.01);
	}

	/**
	 * Basic test. Sales contract loading 2x threshold amount, but same price for
	 * each side of the tier. We expect the blended price to be the same as the
	 * inputs
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicSalesContract1() {

		final SalesContract salesContract = commercialModelBuilder.makeVolumeTierSalesContract("vt1", entity, "5", "5", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "1", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), salesContract, entity, null) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);

		final double expectedPrice = (5.0 * 70_000.0 + 5.0 * 70_000.0) / 140_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
	}

	/**
	 * Basic test. Sales contract loading 2x threshold amount. Different prices with
	 * equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicSalesContract2() {

		final SalesContract salesContract = commercialModelBuilder.makeVolumeTierSalesContract("vt1", entity, "5", "10", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "1", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(140_000, 140_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), salesContract, entity, null) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedPrice = (5.0 * 70_000.0 + 10.0 * 70_000.0) / 140_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicSalesContract3() {

		final SalesContract salesContract = commercialModelBuilder.makeVolumeTierSalesContract("vt1", entity, "5", "10", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "1", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), salesContract, entity, null) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedPrice = (5.0 * 70_000.0 + 10.0 * 35_000.0) / 105_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.01);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSalesContractWithSlotParams() {

		final SalesContract salesContract = commercialModelBuilder.makeVolumeTierSalesContract("vt1", entity, "5", "10", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "1", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), salesContract, entity, null) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(true);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		discharge.getExtensions().add(params);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedPrice = (3.0 * 50_000.0 + 4.0 * 55_000.0) / 105_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);
	}

	/**
	 * Basic test. Purchase contract loading 2x threshold amount. Different prices
	 * with equal volume weighting.
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testBasicSalesContractWithDisabledSlotParams() {

		final SalesContract salesContract = commercialModelBuilder.makeVolumeTierSalesContract("vt1", entity, "5", "10", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "1", 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), salesContract, entity, null) //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(false);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		discharge.getExtensions().add(params);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();

		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		final SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		final double expectedPrice = (5.0 * 70_000.0 + 10.0 * 35_000.0) / 105_000.0;
		Assertions.assertEquals(expectedPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.01);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_Threshold() {

		final PurchaseContract purchaseContract1 = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_THRESHOLD);
			Assertions.assertTrue(children.isEmpty());
		}

		// Error with zero
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setThreshold(0.0);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_THRESHOLD);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(purchaseContract1.getPriceInfo());
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_LowExpression() {

		final PurchaseContract purchaseContract1 = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		// Default case ok
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Null expression is ok.
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setLowExpression(null);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Empty expression is ok.
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setLowExpression("");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// // Invalid not ok
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setLowExpression("NOT VALID");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(purchaseContract1.getPriceInfo());
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_HighExpression() {

		final PurchaseContract purchaseContract1 = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		// Default case ok
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Null expression is ok.
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setHighExpression(null);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Empty expression is ok.
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setHighExpression("");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// // Invalid not ok
		((VolumeTierPriceParameters) purchaseContract1.getPriceInfo()).setHighExpression("NOT VALID");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierContractConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(purchaseContract1.getPriceInfo());
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_SlotParamsThreshold() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(true);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		load.getExtensions().add(params);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_THRESHOLD);
			Assertions.assertTrue(children.isEmpty());
		}

		// Error with zero
		params.setThreshold(0.0);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_THRESHOLD);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_ParamsLowExpression() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);

		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(true);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		load.getExtensions().add(params);

		// Default case ok
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Null expression is not ok.
		params.setLowExpression(null);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// Empty expression is not ok.
		params.setLowExpression("");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// // Invalid not ok
		params.setLowExpression("NOT VALID");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// OK when disabled
		params.setOverrideContract(false);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_LOW_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPurchaseContractValidation_ParamsHighExpression() {

		final PurchaseContract purchaseContract = commercialModelBuilder.makeVolumeTierPurchaseContract("vt1", entity, "1", "1", 70000, VolumeUnits.M3);
		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeDESPurchase("F1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), purchaseContract, entity, null, 22.8, null)//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withVolumeLimits(105_000, 105_000, VolumeUnits.M3) //
				//
				.build() //

				.makeDESSale("D1", LocalDate.of(2018, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_CHITA), null, entity, "5") //
				.build() //

				//
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final VolumeTierSlotParams params = CommercialFactory.eINSTANCE.createVolumeTierSlotParams();
		params.setOverrideContract(true);
		params.setLowExpression("3");
		params.setHighExpression("4");
		params.setThreshold(50000);
		params.setVolumeLimitsUnit(VolumeUnits.M3);

		load.getExtensions().add(params);
		// Default case ok
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}

		// Null expression is not ok.
		params.setHighExpression(null);

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// Empty expression is not ok.
		params.setHighExpression("");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// // Invalid not ok
		params.setHighExpression("NOT VALID");

		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertFalse(children.isEmpty());

			// make sure target is detected.
			boolean foundL1 = false;
			for (final var dscd : children) {
				foundL1 |= dscd.getObjects().contains(params);
			}

			Assertions.assertTrue(foundL1);
		}

		// Disabled case ok
		params.setOverrideContract(false);
		{
			final IStatus statusOrig = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertNotNull(statusOrig);

			// Clear out other validation errors, e.g. validation constraints may fail and
			// get disabled, so do not abort this test.
			final IStatus status = ValidationTestUtil.retainDetailConstraintStatus(statusOrig);

			final List<DetailConstraintStatusDecorator> children = ValidationTestUtil.findStatus(status, VolumeTierSlotParamsConstraint.KEY_HIGH_EXPRESSION);
			Assertions.assertTrue(children.isEmpty());
		}
	}

}
