/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.adp.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lingo.its.tests.microcases.adp.AbstractADPAndLightWeightTests;
import com.mmxlabs.lingo.its.tests.microcases.adp.OptimisationEMFTestUtils;
import com.mmxlabs.lingo.its.tests.microcases.adp.TrainingCaseConstants;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeConstraintChecker;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;

public class PortShipSizeConstraintADPTest extends AbstractADPAndLightWeightTests {

	private VesselAvailability defaultVesselAvailability;
	
	@Test
	public void testADPOptimisationViolatesPortShipSizeConstraint() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		//create legal medium ship, illegal small ship wrt port ship size constraint.
		//ADP normally will allocate to the nominal small ship, which will violate the port ship size constraint, however the ADP optimiser
		//should not be considering the port ship size constraint, so this is fine.
		final CharterInMarket defaultCharterInMarket = this.setInvalidSmallVesselValidMediumVesselContracts(100000, 150000);
		
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		//true == only run ADP, do not run light weight tabu search.
		final OptimisationPlan optimisationPlan = createOptimisationPlan(true);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null)
				.withOptimisationPlan(optimisationPlan)
				.withOptimiseHint()
				.withThreadCount(1)
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				final IMultiStateResult result = runner.runAndApplyBest();
				final NonNullPair<ISequences, Map<String, Object>> bestSolution = result.getBestSolution();

				//Check failed constraints after adp (we do not want ADP taking into account the Port Ship size constraint as the
				//vessel is not a real vessel...
				List<IConstraintChecker> failedConstraints = MicroTestUtils.validateConstraintCheckers(
						runner.getScenarioToOptimiserBridge().getDataTransformer(), bestSolution.getFirst());
				this.validatePortShipSizeConstraintViolated(failedConstraints);

			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	@Test
	public void testADPAndLightWeightTabuSearchRespectsConstraint() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);

		//create medium ship which violates the port size constraints for all ports, small ship which does not violate the port ship size constraints.
		final CharterInMarket defaultCharterInMarket = this.setInvalidSmallVesselValidMediumVesselContracts(150000,210000);

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);
		
		setSimple12CargoCase(adpModelBuilder);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		//false == Run ADP + light weight tabu search.
		final OptimisationPlan optimisationPlan = createOptimisationPlan(false);
		
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null)
				.withOptimisationPlan(optimisationPlan)
				.withOptimiseHint() 
				.withThreadCount(1) 
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
			runnerBuilder.run(false, runner -> {
				final IMultiStateResult result = runner.runAndApplyBest();
				final NonNullPair<ISequences, Map<String, Object>> bestSolution = result.getBestSolution();
				
				List<IConstraintChecker> failedConstraints = MicroTestUtils.validateConstraintCheckers(
						runner.getScenarioToOptimiserBridge().getDataTransformer(), bestSolution.getFirst());

				//Check no cargos on medium ship after tabu, as would violate the constraints.
				checkNoCargosOnMediumShip(runner.getScenarioToOptimiserBridge().getDataTransformer(), bestSolution.getFirst());
				
				//Tabu search should respect the constraints, so none should fail as everything should be unallocated or on small ship.
				assertNull(failedConstraints);
			});
		} finally {
			runnerBuilder.dispose();
		}
	}

	private void checkNoCargosOnMediumShip(@NonNull final LNGDataTransformer dataTransformer, ISequences sequences) {
		for (IResource res : sequences.getResources()) {
			ISequence seq = sequences.getSequences().get(res);
			IResource mediumShipRes = SequenceHelper.getResource(dataTransformer, defaultVesselAvailability);
			if (res == mediumShipRes) {
				//Only start and end elements.
				assertEquals(2, seq.size());
			}
			else {
				//Should have everything else in it.
				assertTrue(seq.size() > 2);
			}
		}
	}
	
	private void validatePortShipSizeConstraintViolated(final List<IConstraintChecker> failedConstraintCheckers) {
		// Check that port ship size constraint is violated.
		boolean portShipSizeConstraintCheckerViolated = false;
		Assertions.assertNotNull(failedConstraintCheckers);
		for (IConstraintChecker c : failedConstraintCheckers) {
			if (c instanceof PortShipSizeConstraintChecker) {
				portShipSizeConstraintCheckerViolated = true;
			}
		}
		Assertions.assertTrue(portShipSizeConstraintCheckerViolated);
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
				//
				.build();
	}

	private CharterInMarket setInvalidSmallVesselValidMediumVesselContracts(int smallShipCapacity, int mediumShipCapacity) {
		final Vessel vesselSmall = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_SMALL_SHIP);
		vesselSmall.setCapacity(smallShipCapacity);
		final Vessel vesselMedium = fleetModelFinder.findVessel(TrainingCaseConstants.VESSEL_MEDIUM_SHIP);
		vesselMedium.setCapacity(mediumShipCapacity);
		final Port darwin = portFinder.findPort(TrainingCaseConstants.PORT_DARWIN);
		darwin.setMinVesselSize(110000);
		darwin.setMaxVesselSize(200000);
		final Port himeji = portFinder.findPort(TrainingCaseConstants.PORT_HIMEJI);
		himeji.setMinVesselSize(110000);
		himeji.setMaxVesselSize(180000);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselSmall, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true); //Creates 1 small ship, which ADP uses for Pnl evaluation and tabu search then moves to other ships after ADP phase, if sufficient capacity.
		defaultCharterInMarket.setEnabled(true);
		defaultCharterInMarket.setSpotCharterCount(0); //If set to 1, we get another small ship.

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
	
	protected OptimisationPlan createOptimisationPlan(boolean nominalADP) {
		// create plan in parent
		final UserSettings userSettings = createUserSettings();
		userSettings.setNominalOnly(nominalADP);
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(userSettings);
		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}	
}