/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class FOBSaleDESPurchaseDurationPricingTests extends AbstractLegacyMicroTestCase {

	private static final String TEST_CURVE_NAME = "TEST_CURVE";

	public static Collection<?> pricingEvents() {
		return Arrays.asList(new Object[][] { { PricingEvent.START_LOAD, 1.0 }, { PricingEvent.END_LOAD, 2.0 }, { PricingEvent.START_DISCHARGE, 1.0 }, { PricingEvent.END_DISCHARGE, 2.0 } });
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("pricingEvents")
	@Tag(TestCategories.MICRO_TEST)
	public void testFOBFOBDurationPricingDates(PricingEvent pricingEvent, Double expectedPrice) {
		// Set up with different price on next contract month.
		final CommodityCurve priceCurve = createPriceCurve();

		final Port pointFortin = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		final PurchaseContract purchaseContract = createPurchaseContract(pointFortin, ContractType.FOB);

		final SalesContract salesContract = createSalesContract(pointFortin, ContractType.FOB);

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		final Cargo cargo = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2019, 7, 31), pointFortin, purchaseContract, entity, null) //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.with(s -> s.setDuration(25)).withPricingEvent(pricingEvent, null).withOptional(true)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2019, 7, 31), pointFortin, salesContract, entity, null, nominatedVessel) //
				.withPricingEvent(pricingEvent, null).build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {
			validateDatesAndPrices(expectedPrice, scenarioRunner);
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("pricingEvents")
	@Tag(TestCategories.MICRO_TEST)
	public void testDESDESDurationPricingDates(PricingEvent pricingEvent, Double expectedPrice) {
		// Set up with different price on next contract month.
		final CommodityCurve priceCurve = createPriceCurve();

		final Port dominionCove = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		final PurchaseContract purchaseContract = createPurchaseContract(dominionCove, ContractType.DES);

		final SalesContract salesContract = createSalesContract(dominionCove, ContractType.DES);

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		final Cargo cargo = cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2019, 7, 31), dominionCove, purchaseContract, entity, null, 22.6, nominatedVessel) //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.with(s -> s.setDuration(25)) //
				.withPricingEvent(pricingEvent, null) //
				.withOptional(true) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 7, 31), dominionCove, salesContract, entity, null) //
				.withPricingEvent(pricingEvent, null).build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			validateDatesAndPrices(expectedPrice, scenarioRunner);
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("pricingEvents")
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertibleFOBFOBDurationPricingDates(PricingEvent pricingEvent, Double expectedPrice) {
		// Set up with different price on next contract month.
		final CommodityCurve priceCurve = createPriceCurve();

		final Port pointFortin = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		final PurchaseContract purchaseContract = createPurchaseContract(pointFortin, ContractType.FOB);

		final Port idkuLNG = portFinder.findPortById(InternalDataConstants.PORT_IDKU);

		final SalesContract salesContract = createSalesContract(idkuLNG, ContractType.FOB);

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		final Cargo cargo = cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2019, 7, 31), pointFortin, purchaseContract, entity, null) //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.with(s -> s.setDuration(25)).withPricingEvent(pricingEvent, null).withOptional(true)//
				.build() //

				.makeFOBSale("D1", FOBSaleDealType.DIVERT_TO_DEST, LocalDate.of(2019, 7, 31), idkuLNG, salesContract, entity, null, nominatedVessel) //
				.with(s -> s.setShippingDaysRestriction(32))//
				.with(s -> s.setDuration(25)).withPricingEvent(pricingEvent, null).build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			validateDatesAndPrices(expectedPrice, scenarioRunner);
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("pricingEvents")
	@Tag(TestCategories.MICRO_TEST)
	public void testDivertibleDESDESDurationPricingDates(PricingEvent pricingEvent, Double expectedPrice) {
		// Set up with different price on next contract month.
		final CommodityCurve priceCurve = createPriceCurve();

		final Port pointFortin = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		final PurchaseContract purchaseContract = createPurchaseContract(pointFortin, ContractType.DES);

		final Port dominionCove = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);

		final SalesContract salesContract = createSalesContract(dominionCove, ContractType.DES);

		// Create the required basic elements
		final Vessel nominatedVessel = fleetModelFinder.findVessel("STEAM-145");

		final Cargo cargo = cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2019, 7, 31), pointFortin, purchaseContract, entity, null, 22.6, nominatedVessel) //
				.with(s -> ((LoadSlot) s).setCargoCV(22.6)) //
				.with(s -> s.setDuration(25)).withPricingEvent(pricingEvent, null).withOptional(true)//
				.build() //

				.makeDESSale("D1", LocalDate.of(2019, 7, 31), dominionCove, salesContract, entity, null) //
				.with(s -> s.setShippingDaysRestriction(32))//
				.with(s -> s.setDuration(25)).withPricingEvent(pricingEvent, null).build() //

				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			validateDatesAndPrices(expectedPrice, scenarioRunner);
		});
	}

	private CommodityCurve createPriceCurve() {
		return pricingModelBuilder.makeCommodityDataCurve(TEST_CURVE_NAME, "$", "mmBtu").addIndexPoint(YearMonth.of(2019, 7), 1.0).addIndexPoint(YearMonth.of(2019, 8), 2.0) // Should use this price.
				.addIndexPoint(YearMonth.of(2019, 9), 3.0).build();
	}

	private SalesContract createSalesContract(final Port port, ContractType contractType) {
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, TEST_CURVE_NAME);
		salesContract.setPreferredPort(port);
		salesContract.setMaxQuantity(3_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		salesContract.setContractType(contractType);
		return salesContract;
	}

	private PurchaseContract createPurchaseContract(final Port port, ContractType contractType) {
		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, TEST_CURVE_NAME);
		purchaseContract.setPreferredPort(port);
		purchaseContract.setMaxQuantity(3_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		purchaseContract.setContractType(contractType);
		return purchaseContract;
	}

	private void validateDatesAndPrices(Double expectedPrice, LNGScenarioRunner scenarioRunner) {
		final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);
		assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
		final Schedule schedule = ScenarioModelUtil.findSchedule(lngScenarioModel);
		assertNotNull(schedule);
		final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);
		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(optCargo1.getLoadName(), schedule);
		final SlotAllocation purchaseAllocation = cargoAllocation.getSlotAllocations().get(0);
		final SlotVisit lsv = purchaseAllocation.getSlotVisit();
		final ZonedDateTime lszdt = lsv.getStart();
		final LocalDate lsPortLocalStartDate = lszdt.toLocalDate();
		final ZonedDateTime lsZdtEnd = lsv.getEnd();
		final LocalDate lsPortLocalEndDate = lsZdtEnd.toLocalDate();

		final SlotAllocation saleAllocation = cargoAllocation.getSlotAllocations().get(1);
		final SlotVisit dsSv = saleAllocation.getSlotVisit();
		final ZonedDateTime dsZdt = dsSv.getStart();
		final LocalDate dsPortLocalStartDate = dsZdt.toLocalDate();
		final ZonedDateTime dsZdtEnd = dsSv.getEnd();
		final LocalDate dsPortLocalEndDate = dsZdtEnd.toLocalDate();

		// Check all above dates are the same for DESDES and FOBFOB cases.
		assertEquals(lsPortLocalStartDate, lsPortLocalEndDate);

		// Check all above dates are the same for DESDES and FOBFOB cases.
		assertEquals(dsPortLocalStartDate, dsPortLocalEndDate);

		// Prices should be next month as price end date should include duration now.
		Assertions.assertEquals(expectedPrice, purchaseAllocation.getPrice(), 0.0001);
	}
}
