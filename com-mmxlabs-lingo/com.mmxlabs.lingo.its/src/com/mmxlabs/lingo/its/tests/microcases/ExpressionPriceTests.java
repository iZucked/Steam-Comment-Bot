/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeImpl;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledTimeWindows;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

@SuppressWarnings({ "unused", "null" })
@RunWith(value = ShiroRunner.class)
public class ExpressionPriceTests extends AbstractMicroTestCase {
//	
//	@Override
//	public @NonNull IScenarioDataProvider importReferenceData() throws MalformedURLException {
//		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");
//
//		return scenarioDataProvider;
//	}
//	@Override
//	protected BaseLegalEntity importDefaultEntity() {
//		return commercialModelFinder.findEntity("Entity");
//	}
	
	@Test
	@Category({ MicroTest.class })
	public void test1() {

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		 final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
		 .build();
//		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, "50000", 0);

		pricingModelBuilder.makeCommodityDataCurve("Test", "$", "mmBtu") //
				.addIndexPoint(YearMonth.of(2017, 12), 0.0) //
				.addIndexPoint(YearMonth.of(2018, 1), 10.0) //
				.build();
		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2018, 1, 1), portFinder.findPort("Darwin LNG"), null, entity, "Test")//
				.withWindowStartTime(0) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				//
				.build() //
				.makeDESSale("D1", LocalDate.of(2018, 2, 1), portFinder.findPort("Chita LNG"), null, entity, "Test") //
				.withWindowSize(0, TimePeriod.HOURS) //
				.withWindowStartTime(0) //
				.withPricingEvent(PricingEvent.START_LOAD, null) //
				.build() //
				//
//				.withVesselAssignment(charterInMarket_1, -1, 1) //
				.withVesselAssignment(vesselAvailability,  1) //
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}
		CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(load.getName(), ScenarioModelUtil.findSchedule(scenarioDataProvider));
		SimpleCargoAllocation simpleCargoAllocation = new SimpleCargoAllocation(cargoAllocation);
		double expectedLoadPrice = 10.0;
		Assert.assertEquals(expectedLoadPrice, simpleCargoAllocation.getLoadAllocation().getPrice(), 0.0001);
		Assert.assertEquals(expectedLoadPrice, simpleCargoAllocation.getDischargeAllocation().getPrice(), 0.0001);

	}
}
