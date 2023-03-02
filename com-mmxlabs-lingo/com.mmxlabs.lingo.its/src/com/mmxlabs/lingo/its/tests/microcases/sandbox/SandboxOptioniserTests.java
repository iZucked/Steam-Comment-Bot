/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@SuppressWarnings("unused")
@ExtendWith(ShiroRunner.class)
public class SandboxOptioniserTests extends AbstractSandboxTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}

	/**
	 * Test tio
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertOptioniseFlag() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null)
				.build();

		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(true);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setOptioniseSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5") //
				.withCV(22.5) //
				.withDate(LocalDate.of(2015, 11, 5)) //
				.build();

		final SellOpportunity sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2015, 12, 5)) //
				.build();

		BaseCaseRow row1 = sandboxBuilder.createBaseCaseRow(null, sell1, null);
		row1.setOptionise(true);
		BaseCaseRow row2 = sandboxBuilder.createBaseCaseRow(buy1, null, null);
		row2.setOptionise(false);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// The transformed buy1/sell1
		Assertions.assertEquals(2, result.getExtraSlots().size());

		Assertions.assertTrue(result instanceof SlotInsertionOptions);

		SlotInsertionOptions resultOptions = (SlotInsertionOptions) result;

		// Expect 1 slot, not the two in the starting point.
		Assertions.assertEquals(1, resultOptions.getSlotsInserted().size());
		// Flaky, but we don't store a mapping between sell1 and the generated object.
		Assertions.assertEquals(sell1.getPort(), resultOptions.getSlotsInserted().get(0).getPort());
		Assertions.assertEquals(result.getExtraSlots().get(1), resultOptions.getSlotsInserted().get(0));
	}

	/**
	 * This code used to throw an assertion error as the buy opportunity was not real and date 0 was based on the sell.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.REGRESSION_TEST)
	public void testOptioniseDateIssue() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(true);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setOptioniseSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_150);

		final ShippingOption shipping = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.build();

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_PLUTO);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);

		final BuyOpportunity buy1 = sandboxBuilder.makeBuyOpportunity(false, dischargePort, entity, "5") //
				.withDate(LocalDate.of(2023, 1, 1)) //
				.withCV(22.8)
				.build();
		//
		// cargoModelBuilder
		// .makeFOBPurchase("FOB_Purchase", LocalDate.of(2023, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5", 22.8)
		// .build();

		// final BuyOption buy1 = sandboxBuilder.createBuyReference(load_FOB1);

		final SellOption sell1 = sandboxBuilder.createSellReference(cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2023, 2, 1), dischargePort, null, entity, "7").build());
		//
		// sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
		// .withDate(LocalDate.of(2023, 2, 1)) //
		// .build();

		BaseCaseRow row1 = sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);
		row1.setOptionise(true);

		// We don't expect any results
		Assertions.assertThrows(UserFeedbackException.class, () -> evaluateSandbox(sandboxBuilder.getOptionAnalysisModel()));

	}

	public static Iterable<Object[]> simpleCharterTestParams() {
		return Arrays.asList(new Object[][] { //
				{ true, true, null, null }, //
				{ false, true, null, null }, //
				{ true, true, LocalDate.of(2022, 12, 1), YearMonth.of(2023, 6) }, //
				{ false, true, LocalDate.of(2022, 12, 1), YearMonth.of(2023, 6) }, //
				{ true, false, null, null }, //
				{ false, false, null, null }, //
				{ true, false, LocalDate.of(2022, 12, 1), YearMonth.of(2023, 6) }, //
				{ false, false, LocalDate.of(2022, 12, 1), YearMonth.of(2023, 6) }, //
		});
	}

	/**
	 * 
	 */
	@ParameterizedTest(name = "Portfolio={0}, openBuySellRow={1}, period={2} -> {3}")
	@MethodSource("simpleCharterTestParams")
	@Tag(TestCategories.MICRO_TEST)
	public void testOptioniseWithSimpleCharterAndPeriod(boolean asPortfolio, boolean withOpenBuySellRow, @Nullable LocalDate periodStart, YearMonth periodEnd) {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(asPortfolio);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setOptioniseSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_150);

		final ShippingOption shipping = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.build();

		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);

		final LoadSlot load_FOB1 = cargoModelBuilder.makeFOBPurchase("FOB_Purchase", LocalDate.of(2023, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_PLUTO), null, entity, "5", 22.8)
				.build();

		final BuyOption buy1 = sandboxBuilder.createBuyReference(load_FOB1);

		final SellOpportunity sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2023, 2, 1)) //
				.build();

		if (withOpenBuySellRow) {
			BaseCaseRow row1 = sandboxBuilder.createBaseCaseRow(null, sell1, null);
			row1.setOptionise(true);
			BaseCaseRow row2 = sandboxBuilder.createBaseCaseRow(buy1, null, null);
			// This can trigger a different code path for shipping lookup in the (Existing)BaseCaseToScheduleSpecification classes
			BaseCaseRow row3 = sandboxBuilder.createBaseCaseRow(sandboxBuilder.createOpenBuy(), sandboxBuilder.createOpenSell(), shipping);
		} else {
			BaseCaseRow row1 = sandboxBuilder.createBaseCaseRow(null, sell1, shipping);
			row1.setOptionise(true);
			BaseCaseRow row2 = sandboxBuilder.createBaseCaseRow(buy1, null, null);
		}

		UserSettings settings = UserSettingsHelper.createDefaultUserSettings();
		if (periodStart != null) {
			settings.setPeriodStartDate(periodStart);
		}
		if (periodEnd != null) {
			settings.setPeriodEnd(periodEnd);
		}

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// The transformed sell1
		Assertions.assertEquals(1, result.getExtraSlots().size());

		Assertions.assertTrue(result instanceof SlotInsertionOptions);

		SlotInsertionOptions resultOptions = (SlotInsertionOptions) result;

		// Expect 1 slot, not the two in the starting point.
		Assertions.assertEquals(1, resultOptions.getSlotsInserted().size());
		// Flaky, but we don't store a mapping between sell1 and the generated object.
		Assertions.assertEquals(sell1.getPort(), resultOptions.getSlotsInserted().get(0).getPort());
		Assertions.assertEquals(result.getExtraSlots().get(0), resultOptions.getSlotsInserted().get(0));
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testInsertOptioniseFlagNothingSelected() throws Exception {

		lngScenarioModel.getCargoModel().getVesselCharters().clear();
		lngScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().clear();

		final LoadSlot load_DES1 = cargoModelBuilder
				.makeDESPurchase("DES_Purchase", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 22.8, null)
				.build();

		final DischargeSlot discharge_DES1 = cargoModelBuilder.makeDESSale("DES_Sale", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "7").build();

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(true);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setOptioniseSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_SAKAI);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5") //
				.withCV(22.5) //
				.withDate(LocalDate.of(2015, 11, 5)) //
				.build();

		final SellOpportunity sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2015, 12, 5)) //
				.build();

		BaseCaseRow row1 = sandboxBuilder.createBaseCaseRow(null, sell1, null);
		row1.setOptionise(false);
		BaseCaseRow row2 = sandboxBuilder.createBaseCaseRow(buy1, null, null);
		row2.setOptionise(false);
		try {
			evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());
		} catch (UserFeedbackException e) {
			// Expect this exception here
			return;
		}
		Assertions.fail("Evaluate was expected to fail");

	}
}