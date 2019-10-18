/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.tests.microcases.adp.AbstractADPAndLightWeightTests;
import com.mmxlabs.lingo.its.tests.microcases.adp.OptimisationEMFTestUtils;
import com.mmxlabs.lingo.its.tests.microcases.adp.TrainingCaseConstants;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;

public class SimpleADPTests extends AbstractADPAndLightWeightTests {

	private VesselAvailability defaultVesselAvailability;

	@Test
	public void testEvaluateDoesNotUpair() {

		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2019, 10));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final PurchaseContract purchaseContract = commercialModelFinder.findPurchaseContract("Purchase A");
		final SalesContract salesContract = commercialModelFinder.findSalesContract("Sales A");
		purchaseContract.setStartDate(YearMonth.of(2018, 10));
		purchaseContract.setEndDate(YearMonth.of(2019, 10));
		purchaseContract.setContractYearStart(10);
		salesContract.setStartDate(YearMonth.of(2018, 10));
		salesContract.setEndDate(YearMonth.of(2019, 10));
		salesContract.setContractYearStart(10);
		
		
		final Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2018, 11, 1), purchaseContract.getPreferredPort(), purchaseContract, null, null).build() //
				.makeDESSale("D1", LocalDate.of(2018, 12, 1), salesContract.getPreferredPort(), salesContract, null, null).build() //
				.withVesselAssignment(defaultVesselAvailability, 1) //
				.build();

		final Slot<?> load = testCargo.getSlots().get(0);
		final Slot<?> discharge = testCargo.getSlots().get(1);

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}

		Assertions.assertNotNull(testCargo.eContainer());
		Assertions.assertNotNull(load.getCargo());
		Assertions.assertNotNull(discharge.getCargo());
	}

	@Test
	public void testBasic12CargoADPOptimisation() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2019, 10));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.withAnySolutionResultChecker().withCargoCount(12, true).build();
				verifier.verifyOptimisationResults(result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	private void setSimple12CargoCase(final ADPModelBuilder adpModelBuilder, final YearMonth start, final YearMonth end) {
		final PurchaseContract purchaseContract = commercialModelFinder.findPurchaseContract("Purchase A");
		final SalesContract salesContract = commercialModelFinder.findSalesContract("Sales A");
		purchaseContract.setStartDate(start);
		purchaseContract.setEndDate(end);
		purchaseContract.setContractYearStart(9);
		salesContract.setStartDate(start);
		salesContract.setEndDate(end);
		salesContract.setContractYearStart(9);
		
		adpModelBuilder.withPurchaseContractProfile(purchaseContract) //
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

		adpModelBuilder.withSalesContractProfile(salesContract) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				//
				.build();
	}

	@Test
	public void tesDESPurchaseADPOptimisation() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final Vessel vesselSmall = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_SMALL_SHIP);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselSmall, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(false);

		final Port darwin = portFinder.findPort(TrainingCaseConstants.PORT_DARWIN);
		final Port himeji = portFinder.findPort(TrainingCaseConstants.PORT_HIMEJI);

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setMaxQuantity(3_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		purchaseContract.setPreferredPort(darwin);
		purchaseContract.setContractType(ContractType.DES);
		purchaseContract.setDesPurchaseDealType(DESPurchaseDealType.DIVERT_FROM_SOURCE);
		purchaseContract.setShippingDaysRestriction(60);
		purchaseContract.setStartDate(YearMonth.of(2018, 10));
		purchaseContract.setEndDate(YearMonth.of(2019, 10));
		purchaseContract.setContractYearStart(10);

		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, "8");
		salesContract.setPreferredPort(himeji);
		salesContract.setMaxQuantity(3_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		salesContract.setContractType(ContractType.DES);
		salesContract.setStartDate(YearMonth.of(2018, 10));
		salesContract.setEndDate(YearMonth.of(2019, 10));
		salesContract.setContractYearStart(10);
		

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withNominatedVessel(vesselSmall) //
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
				//
				.build();
		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		// create plan in parent
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(createUserSettings(true));
		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.withAnySolutionResultChecker().withCargoCount(12, true).build();
				verifier.verifyOptimisationResults(result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	/**
	 * Trim the start of the vessel to start earlier Expect 2 fewer cargoes
	 */
	@Test
	public void testVesselStartTrimming() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();
		final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		vesselAvailability.setStartAfter(vesselAvailability.getStartAfter().plusMonths(2));
		vesselAvailability.setStartBy(vesselAvailability.getStartAfter());
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2019, 10));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.withAnySolutionResultChecker().withCargoCount(10, true).build();
				verifier.verifyOptimisationResults(result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	/**
	 * Trim the end of the vessel to end earlier Expect 2 fewer cargoes
	 */
	@Test
	public void testVesselEndTrimming() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();
		final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		vesselAvailability.setStartAfter(vesselAvailability.getStartAfter().plusMonths(2));
		vesselAvailability.setStartBy(vesselAvailability.getStartAfter());
		vesselAvailability.setEndAfter(vesselAvailability.getEndAfter().minusMonths(2));
		vesselAvailability.setEndBy(vesselAvailability.getEndAfter());
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2019, 10));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 8, result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	/**
	 * Trim the start of the vessel to start later and the end to start earlier Expect 2 fewer cargoes
	 */
	@Test
	public void testVesselStartAndEndTrimming() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();
		final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		vesselAvailability.setEndAfter(vesselAvailability.getEndAfter().minusMonths(2));
		vesselAvailability.setEndBy(vesselAvailability.getEndAfter());
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2019, 10));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 10, result,  Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	/**
	 * Test the contingency.
	 */
	@Test
	public void testContingencyOneVessel() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();
		// final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2018, 11), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2018, 11));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 1, result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	/**
	 * Test the contingency.
	 */
	@Test
	public void testContingencyTwoVessels() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();
		// final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		// final VesselAvailability vesselAvailability = cargoModelFinder.findVesselAvailability(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2018, 12), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder,YearMonth.of(2018, 10), YearMonth.of(2018, 12));

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			System.out.print(ls.getName() + "\n");
		}

		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			System.out.print(ds.getName() + "\n");
		}

		final OptimisationPlan optimisationPlan = createOptimisationPlan();
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiseHint() //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				// Run, get result and store to schedule model for inspection at EMF level if needed
				final IMultiStateResult result = runner.runAndApplyBest();
				// Simple verification, have these slots been used?
				final OptimiserResultVerifier verifier = OptimiserResultVerifier.begin(runner);
				verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 2, result, Assertions::fail);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	private CharterInMarket setDefaultVesselsAndContracts() {
		final Vessel vesselSmall = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_SMALL_SHIP);
		final Vessel vesselMedium = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		final Port darwin = portFinder.findPort(TrainingCaseConstants.PORT_DARWIN);
		final Port himeji = portFinder.findPort(TrainingCaseConstants.PORT_HIMEJI);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselSmall, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(false);

		defaultVesselAvailability = cargoModelBuilder.makeVesselAvailability(vesselMedium, entity) //
				.withStartWindow(LocalDateTime.of(2018, 10, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2019, 10, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setMaxQuantity(3_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		purchaseContract.setPreferredPort(darwin);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, "8");
		salesContract.setPreferredPort(himeji);
		salesContract.setMaxQuantity(3_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		return defaultCharterInMarket;
	}

	protected OptimisationPlan createOptimisationPlan() {
		// create plan in parent
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(createUserSettings());
		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}

}
