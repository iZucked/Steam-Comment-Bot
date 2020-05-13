/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BuyOption;
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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_SANDBOX })
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
						if (evt instanceof VesselEventVisit) {
							final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
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

		sandboxBuilder.createBaseCaseRow(null, sell1, null);
		sandboxBuilder.createBaseCaseRow(buy1, null, null);

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
}
