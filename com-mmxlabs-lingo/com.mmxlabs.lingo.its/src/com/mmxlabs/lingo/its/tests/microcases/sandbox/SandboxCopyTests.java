/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.FragmentCopyHandler;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.analytics.AnalyticsScenarioEvaluator;
import com.mmxlabs.models.lng.transformer.ui.analytics.EvaluateSolutionSetHelper;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxSettings;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxTask;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
@RequireFeature({ KnownFeatures.FEATURE_SANDBOX })
public class SandboxCopyTests extends AbstractSandboxTestCase {

	static {
		final AnalyticsPackage einstance = AnalyticsPackage.eINSTANCE;
	}

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

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "5").withCV(22.5).build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, port, entity, "6").withCV(22.5).build();
		final BuyOption buy3 = sandboxBuilder.createOpenBuy();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, port, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		final VesselEventOption event1 = sandboxBuilder.makeCharterOutOpportunity(port, LocalDate.of(2019, 7, 1), 1) //
				.build();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final ShippingOption shipping1 = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("50000") //
				.build();

		final DischargeSlot slot = cargoModelBuilder.makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

		sandboxBuilder.createSellReference(slot);

		sandboxBuilder.createBaseCaseRow(buy1, sell1, null);
		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);
		sandboxBuilder.createPartialCaseRow(event1, shipping1);

		final OptionAnalysisModel sourceModel = sandboxBuilder.getOptionAnalysisModel();

		// Create a second copy!
		try (IScenarioDataProvider targetSDP = importReferenceData()) {

			// Make equivalent data in new SDP
			new CargoModelBuilder(ScenarioModelUtil.getCargoModel(targetSDP)).makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

			final OptionAnalysisModel targetModel = FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP);
			Assertions.assertNotNull(targetModel);

			Assertions.assertEquals(sourceModel.getBuys().size(), targetModel.getBuys().size());
			Assertions.assertEquals(sourceModel.getSells().size(), targetModel.getSells().size());
			Assertions.assertEquals(sourceModel.getVesselEvents().size(), targetModel.getVesselEvents().size());
			Assertions.assertEquals(sourceModel.getShippingTemplates().size(), targetModel.getShippingTemplates().size());

			Assertions.assertEquals(sourceModel.getBaseCase().getBaseCase().size(), targetModel.getBaseCase().getBaseCase().size());
			Assertions.assertEquals(sourceModel.getPartialCase().getPartialCase().size(), targetModel.getPartialCase().getPartialCase().size());

			for (int i = 0; i < sourceModel.getBaseCase().getBaseCase().size(); ++i) {

				final BaseCaseRow sourceRow = sourceModel.getBaseCase().getBaseCase().get(i);
				final BaseCaseRow targetRow = targetModel.getBaseCase().getBaseCase().get(i);

			}

			for (int i = 0; i < sourceModel.getPartialCase().getPartialCase().size(); ++i) {

				final PartialCaseRow sourceRow = sourceModel.getPartialCase().getPartialCase().get(i);
				final PartialCaseRow targetRow = targetModel.getPartialCase().getPartialCase().get(i);

			}
		}
	}

	/**
	 * Test case to ensure generated solution with a round-trip shipping option works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCopyWithRoundtrip() throws Exception {

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port portFuttsu = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);
		final Port portBonny = portFinder.findPortById(InternalDataConstants.PORT_BONNY);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, portBonny, entity, "5").withCV(22.5).build();
		final BuyOption buy2 = sandboxBuilder.makeBuyOpportunity(true, portFuttsu, entity, "6").withCV(22.5).build();
		final BuyOption buy3 = sandboxBuilder.createOpenBuy();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, portFuttsu, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		final VesselEventOption event1 = sandboxBuilder.makeCharterOutOpportunity(portFuttsu, LocalDate.of(2019, 7, 1), 1) //
				.build();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final ShippingOption shipping1 = sandboxBuilder.createRoundtripOption(vessel, entity, "50000");

		final DischargeSlot slot = cargoModelBuilder.makeDESSale("DES", LocalDate.of(2019, 7, 1), portFuttsu, null, entity, "7").build();

		sandboxBuilder.createSellReference(slot);

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping1);
		sandboxBuilder.createPartialCaseRow(buy2, sell1, null);
		// sandboxBuilder.createPartialCaseRow(event1, shipping1);

		final OptionAnalysisModel sourceModel = sandboxBuilder.getOptionAnalysisModel();

		// Evaluate initial case
		final SandboxJobRunner runner = new SandboxJobRunner();
		runner.withScenario(scenarioDataProvider);
		
		final SandboxSettings settings = new SandboxSettings();
		settings.setSandboxUUID(sourceModel.getUuid());
		settings.setUserSettings(UserSettingsHelper.createDefaultUserSettings());
		runner.withParams(settings);
		
		{
			final AbstractSolutionSet results = runner.run(new NullProgressMonitor());
			Assertions.assertNotNull(results);
			Assertions.assertNotNull(results.getBaseOption());
			Assertions.assertEquals(1, results.getOptions().size());
			sourceModel.setResults(results);
		}

		// Create a second copy!
		try (IScenarioDataProvider targetSDP = importReferenceData()) {

			// Make equivalent data in new SDP
			new CargoModelBuilder(ScenarioModelUtil.getCargoModel(targetSDP)).makeDESSale("DES", LocalDate.of(2019, 7, 1), portFuttsu, null, entity, "7").build();

			final OptionAnalysisModel targetModel = FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP);

			Assertions.assertEquals(sourceModel.getBuys().size(), targetModel.getBuys().size());
			Assertions.assertEquals(sourceModel.getSells().size(), targetModel.getSells().size());
			Assertions.assertEquals(sourceModel.getVesselEvents().size(), targetModel.getVesselEvents().size());
			Assertions.assertEquals(sourceModel.getShippingTemplates().size(), targetModel.getShippingTemplates().size());

			Assertions.assertEquals(sourceModel.getBaseCase().getBaseCase().size(), targetModel.getBaseCase().getBaseCase().size());
			Assertions.assertEquals(sourceModel.getPartialCase().getPartialCase().size(), targetModel.getPartialCase().getPartialCase().size());

			for (int i = 0; i < sourceModel.getBaseCase().getBaseCase().size(); ++i) {

				final BaseCaseRow sourceRow = sourceModel.getBaseCase().getBaseCase().get(i);
				final BaseCaseRow targetRow = targetModel.getBaseCase().getBaseCase().get(i);

			}

			for (int i = 0; i < sourceModel.getPartialCase().getPartialCase().size(); ++i) {

				final PartialCaseRow sourceRow = sourceModel.getPartialCase().getPartialCase().get(i);
				final PartialCaseRow targetRow = targetModel.getPartialCase().getPartialCase().get(i);

			}

			final AbstractSolutionSet results = targetModel.getResults();
			Assertions.assertNotNull(results);
			Assertions.assertNotNull(results.getBaseOption());
			Assertions.assertEquals(1, results.getOptions().size());
		}
	}

	@Test
	public void testCopyFailureMissingLoad() throws Exception {

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final DischargeSlot slot = cargoModelBuilder.makeDESSale("DES", LocalDate.of(2019, 7, 1), port, null, entity, "7").build();

		final SellOption sell1 = sandboxBuilder.createSellReference(slot);

		final OptionAnalysisModel sourceModel = sandboxBuilder.getOptionAnalysisModel();

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

		final Port port = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

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

			final OptionAnalysisModel targetModel = FragmentCopyHandler.copySandboxModelWithJSON(scenarioDataProvider, sandboxBuilder.getOptionAnalysisModel(), targetSDP);

			Assertions.assertEquals(2, targetModel.getBuys().size());
			Assertions.assertEquals(1, targetModel.getSells().size());
			Assertions.assertEquals(0, targetModel.getShippingTemplates().size());

			Assertions.assertEquals(1, targetModel.getBaseCase().getBaseCase().size());
			Assertions.assertEquals(1, targetModel.getPartialCase().getPartialCase().size());

			final AbstractSolutionSet result = targetModel.getResults();

			final Consumer<Boolean> checkResult = expectSchedule -> {
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
				Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
