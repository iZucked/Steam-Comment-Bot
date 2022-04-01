/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.microcases.CharterLengthTests;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
@RequireFeature({ KnownFeatures.FEATURE_SANDBOX, KnownFeatures.FEATURE_CHARTER_LENGTH })
public class SandboxTests extends AbstractSandboxTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}

	/**
	 * Simple DES - DES deal.
	 */
	@Test
	public void testDESPurchaseSwap() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "6").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);

		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(3, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // Base state (use price expression as pairing indicator)
			final SolutionOption option = result.getBaseOption();
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("5", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
		}
		{ // Target state (use price expression as pairing indicator)
			final SolutionOption option = result.getOptions().get(0);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("6", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
		}
	}

	@Test
	public void testDESPurchaseSwapWithBE() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		// Leave dates blank. They should be set automatically
		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		// Point-to-point b/e
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "?").withCV(22.5).build();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);

		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(3, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // Base state (use price expression as pairing indicator)
			final SolutionOption option = result.getBaseOption();
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("5", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
		}
		{ // Target state (use price expression as pairing indicator)
			final SolutionOption option = result.getOptions().get(0);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("?", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());

			Assertions.assertEquals(7.0, cargoAllocation.getSlotAllocations().get(0).getPrice(), 0.01);

		}
	}

	@Test
	public void testDESPurchaseSwapWithPortfolioBE() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(true); // Portfolio break even
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		// Leave dates blank. They should be set automatically
		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		// b/e
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "?").withCV(22.5).build();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);

		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(3, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		long pnl;
		{ // Base state (use price expression as pairing indicator)
			final SolutionOption option = result.getBaseOption();
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("5", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
			pnl = cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
		}
		{ // Target state (use price expression as pairing indicator)
			final SolutionOption option = result.getOptions().get(0);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
			Assertions.assertEquals("?", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
			Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());

			Assertions.assertEquals(5.0, cargoAllocation.getSlotAllocations().get(0).getPrice(), 0.01);
			Assertions.assertEquals(pnl, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss(), 1);
		}
	}

	/**
	 * Test case to check whether we can schedule a cargo OR a vessel event (but not
	 * both).
	 */
	@Test
	public void testEventOrCargoCase() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final ShippingOption shipping1 = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("50000") //
				.build();

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, port1, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port2, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		final VesselEventOption event1 = sandboxBuilder.makeCharterOutOpportunity(port1, LocalDate.of(2019, 7, 1), 30).withHireCost(50_000).build();

		sandboxBuilder.makePartialCaseRow() //
				.withShippingOptions(shipping1) //
				.withVesselEventOptions(event1) // Vessel event...
				.withBuyOptions(buy1).withSellOptions(sell1) // ... or a cargo
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(2, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(1, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(1, result.getExtraVesselEvents().size());

		boolean foundCargoSolution = false;
		boolean foundEventSolution = false;
		LOOP_SOLUTIONS: for (final SolutionOption option : result.getOptions()) {
			if (option.getScheduleModel().getSchedule().getCargoAllocations().isEmpty()) {
				for (final Sequence seq : option.getScheduleModel().getSchedule().getSequences()) {
					for (final Event evt : seq.getEvents()) {
						if (evt instanceof VesselEventVisit vesselEventVisit) {
							if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
								foundEventSolution = true;
								continue LOOP_SOLUTIONS;
							}
						}
					}
				}
			} else {
				final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
				Assertions.assertEquals("5", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
				Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
				foundCargoSolution = true;
				for (final Sequence seq : option.getScheduleModel().getSchedule().getSequences()) {
					for (final Event evt : seq.getEvents()) {
						if (evt instanceof VesselEventVisit) {
							Assertions.fail("Vessel event not expected in cargo solution");
						}
					}
				}
			}
		}
		Assertions.assertTrue(foundEventSolution);
		Assertions.assertTrue(foundCargoSolution);
	}

	/**
	 * Regression test: Sandbox optioniser fails as sandbox DES purchase is
	 * transformed twice.
	 */
	@Test
	public void testDESPurchaseOptioniserInPeriod() {

		// Create the portfolio data

		Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		VesselAvailability vesselCharter = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		LoadSlot existingLoad = cargoModelBuilder.createFOBPurchase("l-1", LocalDate.of(2020, 3, 19), portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, entity, "5", 22.8);
		DischargeSlot existingDischarge = cargoModelBuilder.createDESSale("d-1", LocalDate.of(2020, 3, 29), portFinder.findPortById(InternalDataConstants.PORT_DABHOL), null, entity, "7");

		Cargo cargo = cargoModelBuilder.createCargo(existingLoad, existingDischarge);
		cargo.setVesselAssignmentType(vesselCharter);
		cargo.setAllowRewiring(true);

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));
		sandboxBuilder.setPortfolioMode(true);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setOptioniseSandboxMode();

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, portFinder.findPortById(InternalDataConstants.PORT_DABHOL), entity, "5") //
				.withDate(LocalDate.of(2020, 3, 29)) //
				.withCV(22.5) //
				.build();
		final BuyOption buy2 = sandboxBuilder.createBuyReference(existingLoad);

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, portFinder.findPortById(InternalDataConstants.PORT_MAP_TA_PHUT), entity, "5") //
				.withDate(LocalDate.of(2020, 3, 28)) //
				.build();
		final SellOption sell2 = sandboxBuilder.createSellReference(existingDischarge);

		sandboxBuilder.createBaseCaseRow(null, sell1, null).setOptionise(true);
		sandboxBuilder.createBaseCaseRow(buy1, null, null).setOptionise(true);

		UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setPeriodStartDate(LocalDate.of(2020, 3, 15));
		userSettings.setPeriodEnd(YearMonth.of(2020, 6));

		userSettings.setWithCharterLength(false);
		userSettings.setWithSpotCargoMarkets(false);
		userSettings.setGenerateCharterOuts(false);

		((LNGScenarioModel) scenarioDataProvider.getScenario()).setUserSettings(userSettings);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		// Check that we get a result, rather than an exception.
		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertFalse(result.getOptions().isEmpty());
	}

	@Test
	public void testDerive_DESDES() {

		Port futtsu = portFinder.findPortById("L_JP_Futts");
		Port osaka = portFinder.findPortById("L_JP_Osaka");

		LoadSlot dp1 = cargoModelBuilder.createDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2010, 1, 1), futtsu, null, entity, "5", 22.6, null);

		// Valid match
		DischargeSlot ds1 = cargoModelBuilder.createDESSale("DS1", LocalDate.of(2010, 1, 1), futtsu, null, entity, "7");
		// Wrong port
		DischargeSlot ds2 = cargoModelBuilder.createDESSale("DS2", LocalDate.of(2010, 1, 1), osaka, null, entity, "7");
		// Wrong window
		DischargeSlot ds3 = cargoModelBuilder.createDESSale("DS3", LocalDate.of(2010, 2, 1), futtsu, null, entity, "7");

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final BuyOption buy1 = sandboxBuilder.createBuyReference(dp1);

		final SellOption sell1 = sandboxBuilder.createSellReference(ds1);
		final SellOption sell2 = sandboxBuilder.createSellReference(ds2);
		final SellOption sell3 = sandboxBuilder.createSellReference(ds3);

		sandboxBuilder.createBaseCaseRow(buy1, null, null);

		sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1, sell2, sell3) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(0, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // Only valid solution is dp1 to ds1.
			final SolutionOption option = result.getOptions().get(0);

			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			Assertions.assertSame(dp1, cargoAllocation.getSlotAllocations().get(0).getSlot());
			Assertions.assertSame(ds1, cargoAllocation.getSlotAllocations().get(1).getSlot());

		}
	}

	@Test
	public void testDerive_FOBFOB() {

		Port onslow = portFinder.findPortById("L_AU_Onslo");
		Port pluto = portFinder.findPortById("L_AU_Pluto");

		LoadSlot fp1 = cargoModelBuilder.createFOBPurchase("FP1", LocalDate.of(2010, 1, 1), onslow, null, entity, "5", 22.6);

		// Valid match
		DischargeSlot fs1 = cargoModelBuilder.createFOBSale("FS1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2010, 1, 1), onslow, null, entity, "7", null);
		// Wrong port
		DischargeSlot fs2 = cargoModelBuilder.createFOBSale("FS2", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2010, 1, 1), pluto, null, entity, "7", null);
		// Wrong window
		DischargeSlot fs3 = cargoModelBuilder.createFOBSale("FS3", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2010, 2, 1), onslow, null, entity, "7", null);

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final BuyOption buy1 = sandboxBuilder.createBuyReference(fp1);

		final SellOption sell1 = sandboxBuilder.createSellReference(fs1);
		final SellOption sell2 = sandboxBuilder.createSellReference(fs2);
		final SellOption sell3 = sandboxBuilder.createSellReference(fs3);

		sandboxBuilder.createBaseCaseRow(buy1, null, null);

		sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1, sell2, sell3) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(0, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // Only valid solution is fp1 to fs1.
			final SolutionOption option = result.getOptions().get(0);

			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			Assertions.assertSame(fp1, cargoAllocation.getSlotAllocations().get(0).getSlot());
			Assertions.assertSame(fs1, cargoAllocation.getSlotAllocations().get(1).getSlot());

		}
	}

	@Test
	public void testDerive_DESDESDivertFromSource() {

		Port onslow = portFinder.findPortById("L_AU_Onslo");
		Port futtsu = portFinder.findPortById("L_JP_Futts");
		Port osaka = portFinder.findPortById("L_JP_Osaka");

		Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		LoadSlot dp1 = cargoModelBuilder.createDESPurchase("DP1", DESPurchaseDealType.DIVERT_FROM_SOURCE, LocalDate.of(2010, 1, 1), onslow, null, entity, "5", 22.6, vessel);
		dp1.setShippingDaysRestriction(25);

		// Valid but late
		DischargeSlot ds1 = cargoModelBuilder.createDESSale("DS1", LocalDate.of(2010, 1, 1), futtsu, null, entity, "7");
		// Valid but late
		DischargeSlot ds2 = cargoModelBuilder.createDESSale("DS2", LocalDate.of(2010, 1, 1), osaka, null, entity, "7");
		// Outside of shipping days
		DischargeSlot ds3 = cargoModelBuilder.createDESSale("DS3", LocalDate.of(2010, 2, 1), futtsu, null, entity, "7");

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final BuyOption buy1 = sandboxBuilder.createBuyReference(dp1);

		final SellOption sell1 = sandboxBuilder.createSellReference(ds1);
		final SellOption sell2 = sandboxBuilder.createSellReference(ds2);
		final SellOption sell3 = sandboxBuilder.createSellReference(ds3);

		sandboxBuilder.createBaseCaseRow(buy1, null, null);

		sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1, sell2, sell3) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(2, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(0, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		boolean ds1Solution = false;
		boolean ds2Solution = false;
		{
			final SolutionOption option = result.getOptions().get(0);

			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			Assertions.assertSame(dp1, cargoAllocation.getSlotAllocations().get(0).getSlot());
			Slot<?> slot = cargoAllocation.getSlotAllocations().get(1).getSlot();
			ds1Solution |= slot == ds1;
			ds2Solution |= slot == ds2;

		}
		{
			final SolutionOption option = result.getOptions().get(1);

			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			Assertions.assertSame(dp1, cargoAllocation.getSlotAllocations().get(0).getSlot());
			Slot<?> slot = cargoAllocation.getSlotAllocations().get(1).getSlot();
			ds1Solution |= slot == ds1;
			ds2Solution |= slot == ds2;
		}

		Assertions.assertTrue(ds1Solution && ds2Solution);
	}

	/**
	 * Simple Spot buy to spot sell.
	 */
	@Test
	public void testSpotToSpot() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final ShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, importDefaultEntity(), "30000");

		final Port pFuttsu = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);
		final Port pDarwin = portFinder.findPortById(InternalDataConstants.PORT_DARWIN);

		final SpotMarket fobPurchaseAustralia = spotMarketsModelBuilder.makeFOBPurchaseMarket("FOB Purchase Australia", //
				pDarwin, importDefaultEntity(), "5", 23.3).build();
		final SpotMarket desSaleJapan = spotMarketsModelBuilder.makeDESSaleMarket("DES Sale Japan", //
				pFuttsu, importDefaultEntity(), "10").build();

		final BuyMarket buyMarket1 = sandboxBuilder.createBuyMarketOption(fobPurchaseAustralia);
		final SellMarket sellMarket1 = sandboxBuilder.createSellMarketOption(desSaleJapan);

		sandboxBuilder.createPartialCaseRow(buyMarket1, sellMarket1, shipping);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNull(result.getBaseOption());
		Assertions.assertEquals(0, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(0, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());
	}

	/**
	 * Based on fogbugz 5137 where a bug was introduced in the
	 * {@link ScheduleSpecificationTransformer} and the sandbox failed to evaluate.
	 */
	@Test
	public void testPortfolioWithUnusedOptions() {

		// Create the portfolio case first, then create the sandbox after
		final Vessel referenceVessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vessel1 = fleetModelBuilder.createVessel("Vessel1", referenceVessel);
		final Vessel vessel2 = fleetModelBuilder.createVessel("Vessel2", referenceVessel);

		VesselAvailability charter1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity).build();
		VesselAvailability charter2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity).build();

		DESSalesMarket desJKTC = spotMarketsModelBuilder.makeDESSaleMarket("JKTC", portFinder.findPortById(InternalDataConstants.PORT_INCHEON), entity, "7").build();
		DESSalesMarket desUK = spotMarketsModelBuilder.makeDESSaleMarket("UK", portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN), entity, "7").build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2021, 5, 4), portFinder.findPortById(InternalDataConstants.PORT_CAMERON), null, entity, "5") //
				.build() //
				//
				.makeMarketDESSale("D1", desJKTC, YearMonth.of(2021, 6)) //
				.build() //
				//
				.withVesselAssignment(charter1, 1) //
				.build();

		cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2021, 5, 9), portFinder.findPortById(InternalDataConstants.PORT_CAMERON), null, entity, "5", null) //
				.build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L3", LocalDate.of(2021, 7, 1), portFinder.findPortById(InternalDataConstants.PORT_CAMERON), null, entity, "5") //
				.build() //
				//
				.makeMarketDESSale("D3", desJKTC, YearMonth.of(2021, 7)) //
				.build() //
				//
				.withVesselAssignment(charter1, 2) //
				.build();

		cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L4", LocalDate.of(2021, 8, 8), portFinder.findPortById(InternalDataConstants.PORT_CAMERON), null, entity, "5") //
				.build() //
				//
				.makeMarketDESSale("D4", desJKTC, YearMonth.of(2021, 8)) //
				.build() //
				//
				.withVesselAssignment(charter1, 2) //
				.build();

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(true);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		// Create options

		// Buy options
		final BuyOption option2 = sandboxBuilder.createBuyReference(cargoModelFinder.findLoadSlot("L2"));
		final BuyOption option3 = sandboxBuilder.createBuyReference(cargoModelFinder.findLoadSlot("L3"));
		final BuyOption option4 = sandboxBuilder.createBuyReference(cargoModelFinder.findLoadSlot("L4"));

		// Sell Options
		final SellMarket sellMarket1 = sandboxBuilder.createSellMarketOption(desUK);
		final SellMarket sellMarket2 = sandboxBuilder.createSellMarketOption(desUK);
		final SellMarket sellMarket3 = sandboxBuilder.createSellMarketOption(desJKTC);

		// Shipping options
		final ShippingOption shipping1 = sandboxBuilder.createExistingCharter(charter1);
		final ShippingOption shipping2 = sandboxBuilder.createExistingCharter(charter2);

		// Create starting point
		sandboxBuilder.createBaseCaseRow(option2, sellMarket1, shipping1);
		sandboxBuilder.createBaseCaseRow(option4, sellMarket2, shipping1);

		// Create options
		sandboxBuilder.createPartialCaseRow(option2, sellMarket1, shipping1);
		sandboxBuilder.createPartialCaseRow(option4, sellMarket2, shipping2);
		sandboxBuilder.createPartialCaseRow(option3, sellMarket3, shipping1);

		// We want to make sure the evaluation runs without throwing an exception.

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);
	}

	@Test
	public void testBEWithCharterLength() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_BONNY);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, port1, entity, "?") //
				.withDate(LocalDate.of(2021, 1, 1)) //
				.withCV(22.5) //
				.build();

		DESSalesMarket market1 = spotMarketsModelBuilder.makeDESSaleMarket("Market1", port2, entity, "7").withVolumeLimits(0, 4_000_000, VolumeUnits.MMBTU) //
				.build();

		final SellOption sell1 = sandboxBuilder.createSellMarketOption(market1);

		ShippingOption ship1 = sandboxBuilder.makeOptionalSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.with(option -> {
					option.setStart(LocalDate.of(2021, 1, 1));
					option.setEnd(LocalDate.of(2021, 6, 1));
				}).build();

		sandboxBuilder.makePartialCaseRow().withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(ship1) //
				.build();

		// We really need to pass through settings in API, but code will fall back to
		// here otherwise.
		UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();
		userSettings.setWithCharterLength(true);
		scenarioDataProvider.getTypedScenario(LNGScenarioModel.class).setUserSettings(userSettings);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size()); // buy option plus 2x market slots
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(1, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // Target state (use price expression as pairing indicator)
			final SolutionOption option = result.getOptions().get(0);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			// Make sure there is a price - zero indicate b/e did not run
			Assertions.assertNotEquals(0, cargoAllocation.getSlotAllocations().get(0).getPrice());
			// We could have a rounding error of a couple of dollars
			Assertions.assertEquals(0.0, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss(), 2.0);

			CharterLengthEvent charterLength = CharterLengthTests.findCharterLengthEvent(cargoAllocation.getSlotAllocations().get(1).getSlot(), option.getScheduleModel().getSchedule());
			Assertions.assertNotNull(charterLength);
		}
	}

	@Test
	public void testBEWithGCO() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final Port port1 = portFinder.findPortById(InternalDataConstants.PORT_BONNY);
		final Port port2 = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, port1, entity, "?") //
				.withDate(LocalDate.of(2021, 1, 1)) //
				.withCV(22.5) //
				.build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(false, port1, entity, "5") //
				.withDate(LocalDate.of(2021, 1, 1)) //
				.withCV(22.5) //
				.build();

		DESSalesMarket market1 = spotMarketsModelBuilder.makeDESSaleMarket("Market1", port2, entity, "8") //
				.withVolumeLimits(0, 4_000_000, VolumeUnits.MMBTU) //
				.build();

		final SellOption sell1 = sandboxBuilder.createSellMarketOption(market1);

		ShippingOption ship1 = sandboxBuilder.makeOptionalSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.with(option -> {
					option.setStart(LocalDate.of(2021, 1, 1));
					option.setEnd(LocalDate.of(2021, 6, 1));
				}).build();

		sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1, buy2) //
				.withSellOptions(sell1) //
				.withShippingOptions(ship1) //
				.build();

		CharterOutMarket gcoMarket = spotMarketsModelBuilder.createCharterOutMarket("GCO", vessel, "80000", 30);
		gcoMarket.setEnabled(true);

		// We really need to pass through settings in API, but code will fall back to
		// here otherwise.
		UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(true);
		scenarioDataProvider.getTypedScenario(LNGScenarioModel.class).setUserSettings(userSettings);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(2, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(3, result.getExtraSlots().size()); // buy option plus 2x market slots
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(1, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		{ // B/E case - B/E should not have run
			final SolutionOption option = result.getOptions().get(0);

			// We do not expect a GCO - should be disabled with B/E
			List<GeneratedCharterOut> findGCOEvents = findGCOEvents(option.getScheduleModel().getSchedule().getSequences().get(0));
			Assertions.assertEquals(0, findGCOEvents.size());

			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			// Make sure there is a price - zero indicate b/e did not run
			Assertions.assertNotEquals(0, cargoAllocation.getSlotAllocations().get(0).getPrice());
			// We could have a rounding error of a couple of dollars
			Assertions.assertEquals(0.0, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss(), 2.0);

		}
		{ // Non-b/e case - expect GCo to be able to run
			final SolutionOption option = result.getOptions().get(1);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);

			// We do expect to GCO run here
			List<GeneratedCharterOut> findGCOEvents = findGCOEvents(option.getScheduleModel().getSchedule().getSequences().get(0));
			Assertions.assertEquals(1, findGCOEvents.size());

			// Make sure there is a price - zero indicate b/e did not run
			Assertions.assertEquals(5, cargoAllocation.getSlotAllocations().get(0).getPrice());
			// Expect some kind of P&L
			Assertions.assertNotEquals(0.0, cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss());

		}
	}

	private List<GeneratedCharterOut> findGCOEvents(Sequence sequence) {
		List<GeneratedCharterOut> charterOuts = new ArrayList<>();
		for (Event e : sequence.getEvents()) {
			if (e instanceof GeneratedCharterOut gco) {
				charterOuts.add(gco);
			}
		}
		return charterOuts;
	}
}
