/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.sandbox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
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
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

import software.amazon.awssdk.utils.ToString;

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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
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
		Assertions.assertEquals(1, result.getExtraVesselCharters().size());
		Assertions.assertEquals(1, result.getExtraVesselEvents().size());

		// Expecting options in same order as routes were specified.
		for (int i = 0; i < 3; ++i) {

			final SolutionOption option = result.getOptions().get(i);
			final Sequence seq = option.getScheduleModel().getSchedule().getSequences().get(0);
			final VesselEventVisit vev = (VesselEventVisit) seq.getEvents().get(2);

			Assertions.assertEquals(row1.getOptions().getLoadDates().get(i).getDateTime(), vev.getStart().toLocalDateTime());
		}
	}

	record RouteOptions(LocalDateTimeHolder date, RouteOption route, FuelChoice fuel) {
		public static RouteOptions of(LocalDate date, RouteOption route, FuelChoice fuel) {
			LocalDateTimeHolder h = AnalyticsFactory.eINSTANCE.createLocalDateTimeHolder();
			h.setDateTime(date.atStartOfDay());
			return new RouteOptions(h, route, fuel);
		}

		public String toString() {
			return String.format("%s - %s - %s", date.getDateTime(), route, fuel);
		}
	}

	record LDDTestData(RouteOptions laden1, RouteOptions laden2, RouteOptions ballast) {

	}

	public static Collection<LDDTestData> lddTestData() {
		List<LDDTestData> params = new LinkedList<>();
		for (var ro1 : RouteOption.VALUES) {
			for (var ro2 : RouteOption.VALUES) {
				for (var ro3 : RouteOption.VALUES) {
					params.add(new LDDTestData( //
							RouteOptions.of(LocalDate.of(2019, 6, 1), ro1, FuelChoice.NBO_FBO), //
							RouteOptions.of(LocalDate.of(2019, 7, 2), ro2, FuelChoice.NBO_FBO), //
							RouteOptions.of(LocalDate.of(2019, 8, 5), ro3, FuelChoice.NBO_FBO) //
					));
				}
			}
		}
		for (var fc1 : FuelChoice.VALUES) {
			for (var fc2 : FuelChoice.VALUES) {
				for (var fc3 : FuelChoice.VALUES) {
					params.add(new LDDTestData( //
							RouteOptions.of(LocalDate.of(2019, 6, 1), RouteOption.DIRECT, fc1), //
							RouteOptions.of(LocalDate.of(2019, 7, 2), RouteOption.DIRECT, fc2), //
							RouteOptions.of(LocalDate.of(2019, 8, 5), RouteOption.DIRECT, fc3) //
					));
				}
			}
		}
		for (var d1 : new int[] { 0, 1, 2 }) {
			for (var d2 : new int[] { 0, 1, 2 }) {
				for (var d3 : new int[] { 0, 1, 2 }) {
					params.add(new LDDTestData( //
							RouteOptions.of(LocalDate.of(2019, 6, 1).plusDays(d1), RouteOption.DIRECT, FuelChoice.NBO_FBO), //
							RouteOptions.of(LocalDate.of(2019, 7, 5).plusDays(d2), RouteOption.DIRECT, FuelChoice.NBO_FBO), //
							RouteOptions.of(LocalDate.of(2019, 8, 5).plusDays(d3), RouteOption.DIRECT, FuelChoice.NBO_FBO) //
					));
				}
			}
		}

		return params;
	}

	@ParameterizedTest
	@MethodSource("lddTestData")
	public void testLDDCargoRouteOptions(LDDTestData testParams) {

		ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);
		final Port dischargePort1 = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);
		final Port dischargePort2 = portFinder.findPortById(InternalDataConstants.PORT_DAHEJ);

		// Not all port combinations will show canal options, so filter out invalid ones otherise the test will fail.
		// Note, we could assert no solutions too.
		Assumptions.assumeTrue(modelDistanceProvider.hasDistance(loadPort, dischargePort1, testParams.laden1().route()));
		Assumptions.assumeTrue(modelDistanceProvider.hasDistance(dischargePort1, dischargePort2, testParams.laden2().route()));
		Assumptions.assumeTrue(modelDistanceProvider.hasDistance(dischargePort2, loadPort, testParams.ballast().route()));

		final SandboxModelBuilder sandboxBuilder = SandboxModelBuilder.createSandbox(ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider));

		sandboxBuilder.setPortfolioMode(false);
		sandboxBuilder.setPortfolioBreakevenMode(false);
		sandboxBuilder.setManualSandboxMode();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final RoundTripShippingOption shipping = sandboxBuilder.createRoundtripOption(vessel, entity, "80000");

		final BuyOption buy1 = sandboxBuilder.makeBuyOpportunity(false, loadPort, entity, "5") //
				.withDate(LocalDate.of(2019, 6, 1)) //
				.withCV(22.5) //
				.build();

		final SellOption sell1 = sandboxBuilder.makeSellOpportunity(false, dischargePort1, entity, "6") //
				.withDate(LocalDate.of(2019, 7, 5)) //
				.build();
		final SellOption sell2 = sandboxBuilder.makeSellOpportunity(false, dischargePort2, entity, "7") //
				.withVolumeFixed(1_000_000, VolumeUnits.MMBTU) //
				.withDate(LocalDate.of(2019, 8, 1)) //
				.build();

		PartialCaseRowGroup grp = sandboxBuilder.createPartialCaseLDDGroup(buy1, sell1, sell2, shipping);
		grp.getRows().get(0).getOptions().getLadenFuelChoices().add(testParams.laden1().fuel());
		grp.getRows().get(0).getOptions().getLadenRoutes().add(testParams.laden1().route());
		grp.getRows().get(0).getOptions().getLoadDates().add(testParams.laden1().date());

		grp.getRows().get(0).getOptions().getBallastFuelChoices().add(testParams.laden2().fuel());
		grp.getRows().get(0).getOptions().getBallastRoutes().add(testParams.laden2().route());
		grp.getRows().get(0).getOptions().getDischargeDates().add(testParams.laden2().date());

		grp.getRows().get(1).getOptions().getBallastFuelChoices().add(testParams.ballast().fuel());
		grp.getRows().get(1).getOptions().getBallastRoutes().add(testParams.ballast().route());
		grp.getRows().get(1).getOptions().getDischargeDates().add(testParams.ballast().date());

		evaluateSandbox(sandboxBuilder.getOptionAnalysisModel());

		final AbstractSolutionSet result = sandboxBuilder.getOptionAnalysisModel().getResults();
		Assertions.assertNotNull(result);

		// Check expected results size
		Assertions.assertNotNull(result.getBaseOption());
		Assertions.assertEquals(1, result.getOptions().size());

		// Check expected extra data items
		Assertions.assertEquals(3, result.getExtraSlots().size());
		Assertions.assertEquals(1, result.getExtraCharterInMarkets().size());
		Assertions.assertEquals(0, result.getCharterInMarketOverrides().size());
		Assertions.assertEquals(0, result.getExtraVesselCharters().size());
		Assertions.assertEquals(0, result.getExtraVesselEvents().size());

		final SolutionOption option = result.getOptions().get(0);
		final CargoAllocation cargoAllocation = option.getScheduleModel().getSchedule().getCargoAllocations().get(0);
		final SimpleCargoAllocation sca = new SimpleCargoAllocation(cargoAllocation);

		Assertions.assertEquals(testParams.laden1().route(), sca.getLadenLeg().getRouteOption());
		Assertions.assertEquals(testParams.laden2().route(), sca.getBallastLeg().getRouteOption());
		Assertions.assertEquals(testParams.ballast().route(), sca.getBallastLegB().getRouteOption());

		// Allow rounding to nearest day
		Assertions.assertEquals(testParams.laden1().date().getDateTime().toLocalDate(), sca.getLoadAllocation().getSlotVisit().getStart().toLocalDate());
		Assertions.assertEquals(testParams.laden2().date().getDateTime().toLocalDate(), sca.getDischargeAllocation().getSlotVisit().getStart().toLocalDate());
		Assertions.assertEquals(testParams.ballast().date().getDateTime().toLocalDate(), sca.getDischargeAllocationB().getSlotVisit().getStart().toLocalDate());

		// Check fuels
		BiFunction<FuelChoice, Journey, Boolean> f = (c, j) -> {
			for (FuelQuantity fq : j.getFuels()) {
				switch (fq.getFuel()) {
				case BASE_FUEL:
					if (c == FuelChoice.NBO_FBO) {
						return false;
					}
					break;
				case FBO:
					if (c == FuelChoice.NBO_BUNKERS) {
						return false;
					}
					break;
				default:
					break;
				}
			}
			return true;
		};
		Assertions.assertTrue(f.apply(testParams.laden1().fuel(), sca.getLadenLeg()));
		Assertions.assertTrue(f.apply(testParams.laden2().fuel(), sca.getBallastLeg()));
		Assertions.assertTrue(f.apply(testParams.ballast().fuel(), sca.getBallastLegB()));
	}

}
