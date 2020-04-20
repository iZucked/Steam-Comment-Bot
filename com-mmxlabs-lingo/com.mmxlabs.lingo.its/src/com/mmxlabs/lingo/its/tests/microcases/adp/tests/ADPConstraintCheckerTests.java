/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.microcases.adp.AbstractADPAndLightWeightTests;
import com.mmxlabs.lingo.its.tests.microcases.adp.OptimisationEMFTestUtils;
import com.mmxlabs.lingo.its.tests.microcases.adp.TrainingCaseConstants;
import com.mmxlabs.lingo.its.verifier.OptimiserResultVerifier;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.util.TargetCargoesOnVesselConstraintMaker;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.ContractType;
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
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;

public class ADPConstraintCheckerTests extends AbstractADPAndLightWeightTests {
	public static final String CONSTRAINT_CHECKER_TEST_NAME = "CONSTRAINT_CHECKER_TEST";

	@Test
	public void testFeasible() {
		test12Cargoes2Vessels(1,1,1,1,null);
	}
	
	@Test
	public void testInFeasibleNotEnoughSalesSlots() {
		test12Cargoes2Vessels(1,1,2,2,"On sale contract Sales A:\r\n" + "Jan Min:2 Max:2 (Min violated, slots used = 1)");
	}
	
	@Test
	public void testInFeasibleNotEnoughLoadSlots() {
		test12Cargoes2Vessels(2,2,1,1,"On purchase contract Purchase A:\r\n" + "Jan Min:2 Max:2 (Min violated, slots used = 1)");
	}
	
	//@Test
	//public void testInFeasibleNotEnoughLoadSlotsOrSalesSlots() {
	//}
	
	
	/**
	 * Test has two vessels and 12 cargoes. The cargoes have a wide volume, so with no preference weightings we would expect the cargoes to be put on the largest vessel
	 * 
	 * @param contraintWeight
	 * @param targetVessel
	 */
	private void test12Cargoes2Vessels(int minLoads, int maxLoads, int minSales, int maxSales, String inExpectedMessageStr) {
		
		String targetVessel = TrainingCaseConstants.VESSEL_LARGE_SHIP;
		
		// set up special curve for testing
		makeCommodityCurveForQuarters();

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2019, 1), YearMonth.of(2020, 1), defaultCharterInMarket);

		setContractsAndCargoes(12, adpModelBuilder, minLoads, maxLoads, minSales, maxSales);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		// set up fleet constraints
		TargetCargoesOnVesselConstraintMaker.makeTargetCargoesOnVesselConstraint(adpModelBuilder.getADPModel()).withVessel(fleetModelFinder.findVessel(targetVessel)).withTargetNumberOfCargoes(12)
				.withIntervalType(IntervalType.YEARLY).withWeight(5_000_000).build();

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		boolean failedAsExpected = (inExpectedMessageStr == null);
		
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
				for (int i = 0; i < lngScenarioModel.getCargoModel().getLoadSlots().size(); i++) {
					LoadSlot loadSlot = lngScenarioModel.getCargoModel().getLoadSlots().get(i);
					System.out.println(loadSlot.getName());
					verifier.withAnySolutionResultChecker().withUsedLoad(loadSlot.getName()).onFleetVessel(targetVessel).build();
				}
				verifier.withAnySolutionResultChecker().withCargoCount(12, false).build();
				verifier.verifySingleOptimisationResult(result, msg -> Assertions.fail(msg));
			});
		} 
		catch (InfeasibleSolutionException ex) {
			failedAsExpected = ex.getMessage().contains(inExpectedMessageStr);
			ex.printStackTrace();
		}
		finally {
			runnerBuilder.dispose();
		}
		
		assertTrue(failedAsExpected);
	}

	private void setContractsAndCargoes(final int limit, final ADPModelBuilder adpModelBuilder, int minLoads, int maxLoads, int minSales, int maxSales) {
		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(4_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.FOB) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withCargoNumberDistributionModel(12) //
				.build() //
				.addMonthlyMinMaxCargoConstraint(YearMonth.of(2019, 1), 12, minLoads, maxLoads)
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
				.addMonthlyMinMaxCargoConstraint(YearMonth.of(2019, 1), 12, minSales, maxSales)
				//
				.build();
	}

	private CharterInMarket setDefaultVesselsAndContracts() {
		final Vessel vesselMedium = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		final Vessel vesselSmall = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_SMALL_SHIP);
		final Vessel vesselLarge = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_LARGE_SHIP);
		Port pluto = portFinder.findPort(TrainingCaseConstants.PORT_DARWIN);
		Port himeji = portFinder.findPort(TrainingCaseConstants.PORT_HIMEJI);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselMedium, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(true);

		cargoModelBuilder.makeVesselAvailability(vesselSmall, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2020, 1, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();

		cargoModelBuilder.makeVesselAvailability(vesselLarge, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2020, 1, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();
		PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setPreferredPort(pluto);
		purchaseContract.setMaxQuantity(5_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, CONSTRAINT_CHECKER_TEST_NAME);
		salesContract.setPreferredPort(himeji);
		salesContract.setMaxQuantity(5_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		return defaultCharterInMarket;
	}

	private void makeCommodityCurveForQuarters() {
		pricingModelBuilder.makeCommodityDataCurve(CONSTRAINT_CHECKER_TEST_NAME, "$", "mmBTU").addIndexPoint(YearMonth.of(2019, 1), 10.0).addIndexPoint(YearMonth.of(2019, 2), 10.0)
				.addIndexPoint(YearMonth.of(2019, 3), 14.0)

				.addIndexPoint(YearMonth.of(2019, 4), 14.0).addIndexPoint(YearMonth.of(2019, 5), 10.0).addIndexPoint(YearMonth.of(2019, 6), 10.0)

				.addIndexPoint(YearMonth.of(2019, 7), 10.0).addIndexPoint(YearMonth.of(2019, 8), 10.0).addIndexPoint(YearMonth.of(2019, 9), 14.0)

				.addIndexPoint(YearMonth.of(2019, 10), 14.0).addIndexPoint(YearMonth.of(2019, 11), 10.0).addIndexPoint(YearMonth.of(2019, 12), 10.0)

				.build();
	}

	protected OptimisationPlan createOptimisationPlan() {
		// create plan in parent
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(createUserSettings());
		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}

}
