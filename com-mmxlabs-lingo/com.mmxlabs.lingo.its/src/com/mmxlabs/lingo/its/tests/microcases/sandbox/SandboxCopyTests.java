/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.FragmentCopyHandler;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.EvaluateSolutionSetHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
@RequireFeature(features = { KnownFeatures.FEATURE_SANDBOX, KnownFeatures.FEATURE_BREAK_EVENS })
public class SandboxCopyTests extends AbstractSandboxTestCase {

	/**
	 * Check basic source sandbox data copies across ok.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBasicCopy() throws Exception {

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPort("Futtsu");

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "6").withCV(22.5).build();
		final BuyOption buy3 = sandboxBuilder.createOpenBuy();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		final VesselEventOption event1 = sandboxBuilder.makeCharterOutOpportunity(port, LocalDate.of(2019, 7, 1), 1) //
				.build();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		final ShippingOption shipping1 = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("50000") //
				.build();

		DischargeSlot slot = cargoModelBuilder.makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

		sandboxBuilder.createSellReference(slot);

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);
		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);
		sandboxBuilder.createPartialCaseRow(event1, shipping1);

		OptionAnalysisModel sourceModel = sandboxBuilder.getOptionAnalysisModel();

		// Create a second copy!
		try (IScenarioDataProvider targetSDP = importReferenceData()) {

			// Make equivalent data in new SDP
			new CargoModelBuilder(ScenarioModelUtil.getCargoModel(targetSDP)).makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

			OptionAnalysisModel targetModel = FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP);

			Assertions.assertEquals(sourceModel.getBuys().size(), targetModel.getBuys().size());
			Assertions.assertEquals(sourceModel.getSells().size(), targetModel.getSells().size());
			Assertions.assertEquals(sourceModel.getVesselEvents().size(), targetModel.getVesselEvents().size());
			Assertions.assertEquals(sourceModel.getShippingTemplates().size(), targetModel.getShippingTemplates().size());

			Assertions.assertEquals(sourceModel.getBaseCase().getBaseCase().size(), targetModel.getBaseCase().getBaseCase().size());
			Assertions.assertEquals(sourceModel.getPartialCase().getPartialCase().size(), targetModel.getPartialCase().getPartialCase().size());

			for (int i = 0; i < sourceModel.getBaseCase().getBaseCase().size(); ++i) {

				BaseCaseRow sourceRow = sourceModel.getBaseCase().getBaseCase().get(i);
				BaseCaseRow targetRow = targetModel.getBaseCase().getBaseCase().get(i);

			}

			for (int i = 0; i < sourceModel.getPartialCase().getPartialCase().size(); ++i) {

				PartialCaseRow sourceRow = sourceModel.getPartialCase().getPartialCase().get(i);
				PartialCaseRow targetRow = targetModel.getPartialCase().getPartialCase().get(i);

			}
		}
	}

	@Test
	public void testCopyFailureMissingLoad() throws Exception {

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPort("Futtsu");

		DischargeSlot slot = cargoModelBuilder.makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

		final SellOption sell1 = sandboxBuilder.createSellReference(slot);

		OptionAnalysisModel sourceModel = sandboxBuilder.getOptionAnalysisModel();

		// Create a second copy!
		try (IScenarioDataProvider targetSDP = importReferenceData()) {
			Assertions.assertThrows(IllegalStateException.class, () -> FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP));
		}

	}

	/**
	 * Copy DES - DES deal.
	 */
	@Test
	public void testDESPurchaseSwap() throws Exception {

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPort("Futtsu");

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "6").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);

		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		// Create a second copy!
		try (IScenarioDataProvider targetSDP = importReferenceData()) {

			OptionAnalysisModel targetModel = FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP);

			Assertions.assertEquals(2, targetModel.getBuys().size());
			Assertions.assertEquals(1, targetModel.getSells().size());
			Assertions.assertEquals(0, targetModel.getShippingTemplates().size());

			Assertions.assertEquals(1, targetModel.getBaseCase().getBaseCase().size());
			Assertions.assertEquals(1, targetModel.getPartialCase().getPartialCase().size());

			final AbstractSolutionSet result = targetModel.getResults();

			Consumer<Boolean> checkResult = (expectSchedule) -> {
				//
				Assertions.assertNotNull(result);
				//
				// Check expected results size
				Assertions.assertNotNull(result.getBaseOption());
				Assertions.assertEquals(1, result.getOptions().size());
				//
				// // Check expected extra data items
				Assertions.assertEquals(3, result.getExtraSlots().size());
				Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
				Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
				Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
				Assertions.assertEquals(0, result.getExtraVesselEvents().size());
				//
				{ // Base state (use price expression as pairing indicator)
					final SolutionOption option = result.getBaseOption();
					Assertions.assertNotNull(option.getScheduleSpecification());
					if (expectSchedule) {
						Assertions.assertNotNull(option.getScheduleModel().getSchedule());
						final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
						Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
						Assertions.assertEquals("5", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
						Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
					} else {
						Assertions.assertNull(option.getScheduleModel().getSchedule());
					}
				}
				{ // Target state (use price expression as pairing indicator)
					final SolutionOption option = result.getOptions().get(0);
					Assertions.assertNotNull(option.getScheduleSpecification());
					if (expectSchedule) {
						Assertions.assertNotNull(option.getScheduleModel().getSchedule());

						final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
						Assertions.assertEquals(LocalDate.of(2019, 7, 1), cargoAllocation.getSlotAllocations().get(0).getSlot().getWindowStart());
						Assertions.assertEquals("6", cargoAllocation.getSlotAllocations().get(0).getSlot().getPriceExpression());
						Assertions.assertEquals("7", cargoAllocation.getSlotAllocations().get(1).getSlot().getPriceExpression());
					} else {
						Assertions.assertNull(option.getScheduleModel().getSchedule());

					}
				}
			};

			checkResult.accept(false);

			// Run a synchronous re-evaluation
			EvaluateSolutionSetHelper.recomputeSolution(targetSDP, null, result, true, false, false, false);

			// Check again!
			checkResult.accept(true);
		}
	}
}
