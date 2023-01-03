/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp.tests;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.microcases.adp.AbstractADPAndLightWeightTests;
import com.mmxlabs.lingo.its.tests.microcases.adp.OptimisationEMFTestUtils;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;

@SuppressWarnings("null")
public class ADPSlotPeriodConstraintTests extends AbstractADPAndLightWeightTests {
	public static final String SALES_TEST_NAME = "SALES_TEST";

	@Test
	public void testBasic12CargoADPOptimisationWithQuarterlyConstraint() {
		// set up special curve for testing
		makeCommodityCurveForQuarters();

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2019, 1), YearMonth.of(2020, 1), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = OptimisationEMFTestUtils.setUpBasicADPSettingsWithNoLSOOrHill(lngScenarioModel);
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();
			// Simple verification, have these slots been used?
			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
			// first verify only 4 slots used
			verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 4, result, Assertions::fail);

			// Next validate solution types exist
			final OptimiserResultVerifier slotVerifier = OptimiserResultVerifier.begin(runner) //
					.withAnySolutionResultChecker().withUsedDischarge("19-SALES A-03").any() // Main sure we have this paring on any kind of vessel
					.withUsedDischarge("19-SALES A-04").any() // Main sure we have this paring on any kind of vessel
					.withUsedDischarge("19-SALES A-09").any() // Main sure we have this paring on any kind of vessel
					.withUsedDischarge("19-SALES A-10").any() // Main sure we have this paring on any kind of vessel
					.build();
		});

	}

	@Test
	public void testBasic12CargoADPOptimisationWithAnnualLimitOf10() {
		final int limit = 10;
		test12CargoesWithYearlyLimit(limit);
	}

	@Test
	public void testBasic12CargoADPOptimisationWithAnnualLimitOf12() {
		final int limit = 12;
		test12CargoesWithYearlyLimit(limit);
	}

	@Test
	public void testBasic12CargoADPOptimisationWithAnnualLimitOf5() {
		final int limit = 5;
		test12CargoesWithYearlyLimit(limit);
	}

	private void test12CargoesWithYearlyLimit(final int limit) {
		// set up special curve for testing
		makeCommodityCurveForQuarters();

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2019, 1), YearMonth.of(2020, 1), defaultCharterInMarket);

		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.FOB) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				//
				.build();

		adpModelBuilder.withSalesContractProfile(commercialModelFinder.findSalesContract("Sales A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				.addMaxCargoConstraint(limit, IntervalType.YEARLY)
				//
				.build();

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = OptimisationEMFTestUtils.setUpBasicADPSettingsWithNoLSOOrHill(lngScenarioModel);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		runnerBuilder.evaluateInitialState();
		runnerBuilder.run(false, runner -> {
			// Run, get result and store to schedule model for inspection at EMF level if
			// needed
			final IMultiStateResult result = runner.runAndApplyBest();
			// Simple verification, have these slots been used?
			final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
			verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, limit, result, msg -> Assertions.fail(msg));
		});
	}

	private void setSimple12CargoCase(final ADPModelBuilder adpModelBuilder) {
		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.FOB) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				//
				.addMaxCargoConstraint(1, IntervalType.MONTHLY) //
				.build();

		adpModelBuilder.withSalesContractProfile(commercialModelFinder.findSalesContract("Sales A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				.addMaxCargoConstraint(1, IntervalType.QUARTERLY)
				//
				.build();
	}

	private CharterInMarket setDefaultVesselsAndContracts() {
		final Vessel vesselEbisu = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vesselEbisu.setName("LNG Ebisu");
		vesselEbisu.setCapacity(147_546);

		final Vessel vesselRogers = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		vesselRogers.setName("Woodside Rogers");
		vesselRogers.setCapacity(159_800);

		Port pluto = portFinder.findPortById(InternalDataConstants.PORT_PLUTO);
		Port himeji = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselEbisu, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(true);

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vesselRogers, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2020, 1, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();
		PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setPreferredPort(pluto);
		purchaseContract.setMaxQuantity(3_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, SALES_TEST_NAME);
		salesContract.setPreferredPort(himeji);
		salesContract.setMaxQuantity(3_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		return defaultCharterInMarket;
	}

	private void makeCommodityCurveForQuarters() {
		pricingModelBuilder.makeCommodityDataCurve(SALES_TEST_NAME, "$", "mmBTU").addIndexPoint(YearMonth.of(2019, 1), 10.0).addIndexPoint(YearMonth.of(2019, 2), 10.0)
				.addIndexPoint(YearMonth.of(2019, 3), 14.0)

				.addIndexPoint(YearMonth.of(2019, 4), 14.0).addIndexPoint(YearMonth.of(2019, 5), 10.0).addIndexPoint(YearMonth.of(2019, 6), 10.0)

				.addIndexPoint(YearMonth.of(2019, 7), 10.0).addIndexPoint(YearMonth.of(2019, 8), 10.0).addIndexPoint(YearMonth.of(2019, 9), 14.0)

				.addIndexPoint(YearMonth.of(2019, 10), 14.0).addIndexPoint(YearMonth.of(2019, 11), 10.0).addIndexPoint(YearMonth.of(2019, 12), 10.0)

				.build();
	}
}
