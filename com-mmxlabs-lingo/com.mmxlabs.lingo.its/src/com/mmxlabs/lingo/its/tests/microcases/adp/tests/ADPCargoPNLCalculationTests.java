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
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IMultiStateResult;

@SuppressWarnings("null")
public class ADPCargoPNLCalculationTests extends AbstractADPAndLightWeightTests {

	@Test
	public void testBasic1CargoADPOptimisation() {

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple1CargoCase(adpModelBuilder);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final CargoModel cargoModel = cargoModelBuilder.getCargoModel();

		// Check initial conditions are correct
		Assertions.assertTrue(cargoModel.getCargoes().isEmpty());
		Assertions.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assertions.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final OptimisationPlan optimisationPlan = setUpBasicADPSettingsWithNoLSOOrHill();

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
			verifier.verifyCargoCountInOptimisationResultWithoutNominals(0, 2, result, msg -> Assertions.fail(msg));
		});
	}

	private OptimisationPlan setUpBasicADPSettingsWithNoLSOOrHill() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);

		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setMode(OptimisationMode.ADP);
		userSettings.setCleanSlateOptimisation(true);

		OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan);
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);

		return optimisationPlan;
	}

	private void setSimple1CargoCase(final ADPModelBuilder adpModelBuilder) {
		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(3_000_000 * 1, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.FOB) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withCargoNumberDistributionModel(2) //
				.build() //
				//
				.addMaxCargoConstraint(1, IntervalType.MONTHLY) //
				.build();

		adpModelBuilder.withSalesContractProfile(commercialModelFinder.findSalesContract("Sales A")) //
				.withVolume(3_000_000 * 1, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
				.withCargoNumberDistributionModel(2) //
				.build() //
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

		final Port pluto = portFinder.findPortById(InternalDataConstants.PORT_PLUTO);
		final Port himeji = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselEbisu, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(true);

		final CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);

		@SuppressWarnings("unused")
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vesselRogers, entity) //
				.withStartWindow(LocalDateTime.of(2018, 10, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2019, 10, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setPreferredPort(pluto);
		purchaseContract.setMaxQuantity(3_000_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, "8");
		salesContract.setPreferredPort(himeji);
		salesContract.setMaxQuantity(3_000_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.MMBTU);
		return defaultCharterInMarket;
	}
}
