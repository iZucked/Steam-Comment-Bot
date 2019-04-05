package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@ExtendWith(ShiroRunner.class)
@RequireFeature(features = { KnownFeatures.FEATURE_SANDBOX, KnownFeatures.FEATURE_BREAK_EVENS })
public class SandboxTests extends AbstractSandboxTestCase {

	/**
	 * Simple DES - DES deal.
	 */
	@Test
	public void testDESPurchaseSwap() {
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

		final Port port = portFinder.findPort("Futtsu");

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

		final Port port = portFinder.findPort("Futtsu");

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
	 * Test case to check whether we can schedule a cargo OR a vessel event (but not both).
	 */
	@Test
	public void testEventOrCargoCase() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		final ShippingOption shipping1 = sandboxBuilder.makeSimpleCharter(vessel, entity) //
				.withHireCosts("50000") //
				.build();

		final Port port1 = portFinder.findPort("Point Fortin");
		final Port port2 = portFinder.findPort("Futtsu");

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

}
