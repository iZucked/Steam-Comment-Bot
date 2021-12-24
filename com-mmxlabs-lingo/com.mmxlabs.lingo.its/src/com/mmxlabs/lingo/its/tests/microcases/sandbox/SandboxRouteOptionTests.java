/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.util.SandboxModelBuilder;
import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
@RequireFeature({ KnownFeatures.FEATURE_SANDBOX, KnownFeatures.FEATURE_CHARTER_LENGTH })
public class SandboxRouteOptionTests extends AbstractSandboxTestCase {

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

	@Test
	public void testCargoLadenRouteOptions() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withLadenRouteOptions(RouteOption.DIRECT, RouteOption.SUEZ, RouteOption.PANAMA) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(4, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			Assertions.assertEquals(row.getOptions().getLadenRoutes().get(i), sca.getLadenLeg().getRouteOption());
		}
	}

	@Test
	public void testCargoLadenRouteOptionsMissingRoutes() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_ISLE_OF_GRAIN);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				// SUEZ and PANAMA are not valid routes for this port pair
				.withLadenRouteOptions(RouteOption.DIRECT, RouteOption.SUEZ, RouteOption.PANAMA) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(4, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified. 
		// US -> UK, only expect DIRECT route
		for (int i = 0; i < 1; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			Assertions.assertEquals(row.getOptions().getLadenRoutes().get(i), sca.getLadenLeg().getRouteOption());
			Assertions.assertEquals(RouteOption.DIRECT, sca.getLadenLeg().getRouteOption());
		}
	}
	
	@Test
	public void testCargoBallastRouteOptions() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withBallastRouteOptions(RouteOption.DIRECT, RouteOption.SUEZ, RouteOption.PANAMA) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(4, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			Assertions.assertEquals(row.getOptions().getBallastRoutes().get(i), sca.getBallastLeg().getRouteOption());
		}
	}

	@Test
	public void testCargoLadenFuelChoices() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withLadenFuelChoices(FuelChoice.NBO_BUNKERS, FuelChoice.NBO_FBO) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(2, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(3, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 2; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			FuelChoice choice = row.getOptions().getLadenFuelChoices().get(i);
			if (choice == FuelChoice.NBO_BUNKERS) {
				for (var fq : sca.getLadenLeg().getFuels()) {
					if (fq.getFuel() == Fuel.FBO) {
						Assertions.fail("Unexpected FBO use");
					}
				}
				for (var fq : sca.getLadenIdle().getFuels()) {
					if (fq.getFuel() == Fuel.FBO) {
						Assertions.fail("Unexpected FBO use");
					}
				}
			} else if (choice == FuelChoice.NBO_FBO) {
				for (var fq : sca.getLadenLeg().getFuels()) {
					if (fq.getFuel() == Fuel.BASE_FUEL) {
						Assertions.fail("Unexpected bunker fuel use");
					}
				}
				for (var fq : sca.getLadenIdle().getFuels()) {
					if (fq.getFuel() == Fuel.BASE_FUEL) {
						Assertions.fail("Unexpected bunker fuel use");
					}
				}
			} else {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testCargoBallastFuelChoices() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withBallastFuelChoices(FuelChoice.NBO_BUNKERS, FuelChoice.NBO_FBO) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(2, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(3, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 2; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			FuelChoice choice = row.getOptions().getBallastFuelChoices().get(i);
			if (choice == FuelChoice.NBO_BUNKERS) {
				for (var fq : sca.getBallastLeg().getFuels()) {
					if (fq.getFuel() == Fuel.FBO) {
						Assertions.fail("Unexpected FBO use");
					}
				}
				for (var fq : sca.getBallastIdle().getFuels()) {
					if (fq.getFuel() == Fuel.FBO) {
						Assertions.fail("Unexpected FBO use");
					}
				}
			} else if (choice == FuelChoice.NBO_FBO) {
				for (var fq : sca.getBallastLeg().getFuels()) {
					if (fq.getFuel() == Fuel.BASE_FUEL) {
						Assertions.fail("Unexpected bunker fuel use");
					}
				}
				for (var fq : sca.getBallastIdle().getFuels()) {
					if (fq.getFuel() == Fuel.BASE_FUEL) {
						Assertions.fail("Unexpected bunker fuel use");
					}
				}
			} else {
				Assertions.fail();
			}
		}
	}

	@Test
	public void testEventLadenRouteOptions() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final ShippingOption shipping = sandboxBuilder.makeOptionalSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.build();

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		CharterOutOpportunity event1 = sandboxBuilder.makeCharterOutOpportunity(dischargePort, LocalDate.of(2019, 6, 1), 1).build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		// Two thing on the same manual mode vessel is not really supported. These need
		// to be in date order to evaluate. Event before cargo to get a leg after the
		// event
		final PartialCaseRow row1 = sandboxBuilder.makePartialCaseRow() //
				.withVesselEventOptions(event1) //
				.withShippingOptions(shipping) //
				// "Laden" is also the voyage after the event
				.withLadenRouteOptions(RouteOption.DIRECT, RouteOption.SUEZ, RouteOption.PANAMA) //
				.build();
		final PartialCaseRow row2 = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(1, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(1, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final Sequence seq = option.getScheduleModel().getSchedule().getSequences().get(0);
			final Journey journey = (Journey) seq.getEvents().get(3);

			Assertions.assertEquals(row1.getOptions().getLadenRoutes().get(i), journey.getRouteOption());
		}
	}

	@Test
	public void testCargoLoadDates() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withLoadDates(LocalDateTime.of(2019, 6, 1, 0, 0, 0), LocalDateTime.of(2019, 6, 15, 0, 0, 0), LocalDateTime.of(2019, 7, 1, 0, 0, 0)) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(4, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			Assertions.assertEquals(row.getOptions().getLoadDates().get(i).getDateTime(), sca.getLoadAllocation().getSlotVisit().getStart().toLocalDateTime());
		}
	}

	@Test
	public void testCargoDischargeDates() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withDischargeDates(LocalDateTime.of(2019, 7, 2, 0, 0, 0), LocalDateTime.of(2019, 7, 15, 0, 0, 0), LocalDateTime.of(2019, 8, 1, 0, 0, 0)) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(4, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			Assertions.assertEquals(row.getOptions().getDischargeDates().get(i).getDateTime(), sca.getDischargeAllocation().getSlotVisit().getStart().toLocalDateTime());
		}
	}
	
	@Test
	public void testCargoDischargeDatesDelayed() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		final PartialCaseRow row = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.withDischargeDates(LocalDateTime.of(2019, 6, 1, 0, 0, 0)) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(2, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		{
			int i = 0;
			final SolutionOption option = result.getOptions().get(i);
			final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
			final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

			// Actual date is after requested date as there is not enough time.
			// TODO: Should this case be filtered out as invalid?
			System.out.println(row.getOptions().getDischargeDates().get(i).getDateTime());
			System.out.println(sca.getDischargeAllocation().getSlotVisit().getStart().toLocalDateTime());
			Assertions.assertTrue(row.getOptions().getDischargeDates().get(i).getDateTime().isBefore(sca.getDischargeAllocation().getSlotVisit().getStart().toLocalDateTime()));
		}
	}
	
	@Test
	public void testEventDates() {
		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final ShippingOption shipping = sandboxBuilder.makeOptionalSimpleCharter(vessel, entity) //
				.withHireCosts("80000") //
				.build();

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5").withCV(22.5).build();
		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort, entity, "7") //
				.withDate(LocalDate.of(2019, 7, 1)) //
				.build();

		CharterOutOpportunity event1 = sandboxBuilder.makeCharterOutOpportunity(dischargePort, LocalDate.of(2019, 6, 1), 1).build();

		sandboxBuilder.createBaseCaseRow(buy1, sell1, shipping);

		// Two thing on the same manual mode vessel is not really supported. These need
		// to be in date order to evaluate. Event before cargo to get a leg after the
		// event
		final PartialCaseRow row1 = sandboxBuilder.makePartialCaseRow() //
				.withVesselEventOptions(event1) //
				.withShippingOptions(shipping) //
				// "Load" is also the event
				.withLoadDates(LocalDateTime.of(2019, 6, 1, 0, 0, 0), LocalDateTime.of(2019, 6, 15, 0, 0, 0), LocalDateTime.of(2019, 7, 1, 0, 0, 0)) //

				.build();
		final PartialCaseRow row2 = sandboxBuilder.makePartialCaseRow() //
				.withBuyOptions(buy1) //
				.withSellOptions(sell1) //
				.withShippingOptions(shipping) //
				.build();

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(3, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(2, result.getExtraSlots().size());
		Assertions.assertEquals(0, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(1, result.getExtraVesselAvailabilities().size());
		Assertions.assertEquals(1, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final Sequence seq = option.getScheduleModel().getSchedule().getSequences().get(0);
			final VesselEventVisit vev = (VesselEventVisit) seq.getEvents().get(2);

			Assertions.assertEquals(row1.getOptions().getLoadDates().get(i).getDateTime(),vev.getStart().toLocalDateTime());
		}
	}
}
