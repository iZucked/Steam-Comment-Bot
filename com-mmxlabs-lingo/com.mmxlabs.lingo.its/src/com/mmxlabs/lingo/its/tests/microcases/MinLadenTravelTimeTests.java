/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@ExtendWith(ShiroRunner.class)
public class MinLadenTravelTimeTests extends AbstractMicroTestCase {

	private static final String VESSEL_SMALL = InternalDataConstants.REF_VESSEL_STEAM_138;

	public IOptimiserInjectorService makeCanalBookingService() {
		return OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming, boolean.class, Boolean.TRUE) //
				.make();
	}

	@NonNull
	private Journey checkAndFindLadenJourney(final @NonNull Port loadPort, final @NonNull Port dischargePort, final @NonNull VesselCharter vesselCharter) {
		final Schedule schedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final VesselCharter vc = sequence.getVesselCharter();
		Assertions.assertEquals(vesselCharter, vc);

		Journey ladenJourney = null;
		for (final Event event : sequence.getEvents()) {
			if (event instanceof Journey journey && journey.isLaden()) {
				Assertions.assertNull(ladenJourney);
				ladenJourney = journey;
			}
		}

		Assertions.assertNotNull(ladenJourney);

		Assertions.assertEquals(loadPort, ladenJourney.getPort());
		Assertions.assertEquals(dischargePort, ladenJourney.getDestination());
		return ladenJourney;
	}

	private int sumTravelAndIdle(final @NonNull Event event) {
		Event currentEvent = event;
		int travelInHours = 0;
		while (currentEvent instanceof Journey || currentEvent instanceof Idle) {
			travelInHours += currentEvent.getDuration();
			currentEvent = currentEvent.getNextEvent();
		}
		return travelInHours;
	}

	@Nullable
	private SlotVisit getNextSlotVisit(final @NonNull Event event) {
		Event nextEvent = event.getNextEvent();
		while (nextEvent != null) {
			if (nextEvent instanceof SlotVisit slotVisit) {
				return slotVisit;
			}
			nextEvent = nextEvent.getNextEvent();
		}
		return null;
	}

	@Test
	public void aaaaa() {

	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPanamaWaitingDays() {

		final int CHOSEN_WAITING_DAYS = 7;
		// extreme value that avoids Panama - used for deriving values to test preconditions for main test.
		final int EXTREME_WAITING_DAYS = 16;

		final Vessel smallShip = fleetModelFinder.findVessel(VESSEL_SMALL);
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(smallShip, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(LocalDate.of(2023, 1, 1), LocalTime.of(0, 0))) //
				.withStartHeel(500, 6_000, 22.6, null) //
				.withEndWindow(LocalDateTime.of(LocalDate.of(2023, 5, 1), LocalTime.of(0, 0))).withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("load", LocalDate.of(2023, 1, 1), loadPort, null, entity, "6", 22.8) //
				.withVisitDuration(36) //
				.withWindowSize(0, TimePeriod.DAYS)
				.build() //
				.makeDESSale("discharge", LocalDate.of(2023, 2, 1), dischargePort, null, entity, "SPLITMONTH(15, 5, 2)") //
				.withWindowSize(2, TimePeriod.DAYS) //
				.withVisitDuration(36) //
				.build() //
				.withVesselAssignment(vesselCharter, -1) //
				.build();

		final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
		Assertions.assertEquals(2, sortedSlots.size());
		Assertions.assertInstanceOf(LoadSlot.class, sortedSlots.get(0));
		Assertions.assertInstanceOf(DischargeSlot.class, sortedSlots.get(1));
		final LoadSlot loadSlot = (LoadSlot) sortedSlots.get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) sortedSlots.get(1);

		cargoModelBuilder.initCanalBookings();
		final VesselGroupCanalParameters defaultVgcp = cargoModelFinder.findVesselGroupCanalParameters(CargoModelBuilder.DEFAULT_VESSEL_GROUP_CANAL_PARAMETERS_NAME);
		Assertions.assertTrue(defaultVgcp.getVesselGroup().isEmpty());
		final List<PanamaSeasonalityRecord> seasonalityRecords = cargoModelFinder.getCargoModel().getCanalBookings().getPanamaSeasonalityRecords();
		Assertions.assertTrue(seasonalityRecords.size() == 1);
		final PanamaSeasonalityRecord psr = seasonalityRecords.get(0);
		Assertions.assertEquals(psr.getVesselGroupCanalParameter(), defaultVgcp);

		final int panamaTravelLadenInHours;
		final int panamaTotalLadenInHours;
		{
			// Pre test 1 - CHOSEN_WAITING_DAYS should choose Panama on laden with no resulting
			// lateness
			psr.setSouthboundWaitingDays(CHOSEN_WAITING_DAYS);
			evaluateTestWith(makeCanalBookingService());

			final @NonNull Journey ladenJourney = checkAndFindLadenJourney(loadPort, dischargePort, vesselCharter);
			Assertions.assertTrue(ladenJourney.getRouteOption() == RouteOption.PANAMA);
			panamaTravelLadenInHours = ladenJourney.getDuration();
			final SlotVisit dischargeSlotVisit = getNextSlotVisit(ladenJourney);
			Assertions.assertNotNull(dischargeSlotVisit);
			Assertions.assertEquals(dischargeSlot, dischargeSlotVisit.getSlotAllocation().getSlot());

			Assertions.assertNull(dischargeSlotVisit.getLateness());
			panamaTotalLadenInHours = sumTravelAndIdle(ladenJourney);
		}
		final int nonPanamaTotalLadenInHours;
		{
			// Pre test 2 - EXTREME_WAITING_DAYS should not choose Panama on laden - alternative route should have some resulting
			// lateness
			psr.setSouthboundWaitingDays(EXTREME_WAITING_DAYS);
			evaluateTestWith(makeCanalBookingService());

			final @NonNull Journey ladenJourney = checkAndFindLadenJourney(loadPort, dischargePort, vesselCharter);
			Assertions.assertTrue(ladenJourney.getRouteOption() != RouteOption.PANAMA);
			final SlotVisit dischargeSlotVisit = getNextSlotVisit(ladenJourney);
			Assertions.assertNotNull(dischargeSlotVisit);
			Assertions.assertEquals(dischargeSlot, dischargeSlotVisit.getSlotAllocation().getSlot());
			Assertions.assertNotNull(dischargeSlotVisit.getLateness());
			nonPanamaTotalLadenInHours = sumTravelAndIdle(ladenJourney);
		}

		final int travelTimeWithoutLateness = Hours.between(loadSlot.getSchedulingTimeWindow().getStart().plusHours(loadSlot.getDuration()), dischargeSlot.getSchedulingTimeWindow().getEnd());

		final int panamaTotalLadenInDaysRoundedUp = (int) Math.ceil(panamaTotalLadenInHours / 24.0);
		final int nonPanamaTotalLadenInDaysRoundedDown = (int) Math.floor(nonPanamaTotalLadenInHours / 24.0);

		final int minimumLadenDays = panamaTotalLadenInDaysRoundedUp;
		/*
		 * Check preconditions for main test
		 */
		// Precondition 1: alternative to Panama should take more days than minimum
		Assertions.assertTrue(minimumLadenDays < nonPanamaTotalLadenInDaysRoundedDown);
		final int minimumLadenDaysInHours = minimumLadenDays * 24;
		// Precondition 2: Minimum laden days should not cause lateness and (by definition) Panama should not cause lateness either.
		Assertions.assertTrue(travelTimeWithoutLateness >= minimumLadenDaysInHours);
		// Precondition 3: If the waiting days are added to minimumLadenDays [incorrect behaviour being tested against], the alternative to Panama (see Pre test 2) should be the better option.
		Assertions.assertTrue(minimumLadenDays + CHOSEN_WAITING_DAYS > nonPanamaTotalLadenInDaysRoundedDown);
		// Precondition 4: Panama laden time is less than minimumLaden - we should see the idle time increase while the journey time stays the same.
		Assertions.assertTrue(panamaTotalLadenInHours < minimumLadenDaysInHours);

		{
			// Main test - expecting Panama for laden journey
			psr.setSouthboundWaitingDays(CHOSEN_WAITING_DAYS);
			loadSlot.setMinLadenTime(minimumLadenDays);
			evaluateTestWith(makeCanalBookingService());

			final @NonNull Journey ladenJourney = checkAndFindLadenJourney(loadPort, dischargePort, vesselCharter);
			Assertions.assertTrue(ladenJourney.getRouteOption() == RouteOption.PANAMA);
			final SlotVisit dischargeSlotVisit = getNextSlotVisit(ladenJourney);
			Assertions.assertNotNull(dischargeSlotVisit);
			Assertions.assertEquals(dischargeSlot, dischargeSlotVisit.getSlotAllocation().getSlot());
			Assertions.assertNull(dischargeSlotVisit.getLateness());
			// This assertion may not hold because of which arrival times pre-test-1 chooses vs the main test.
			// Assertions.assertEquals(panamaTravelLadenInHours, ladenJourney.getDuration());
			final Event expectedIdleEvent = ladenJourney.getNextEvent();
			Assertions.assertInstanceOf(Idle.class, expectedIdleEvent);
			final Idle idle = (Idle) expectedIdleEvent;
			final int totalLadenTime = ladenJourney.getDuration() + idle.getDuration();
			Assertions.assertTrue(totalLadenTime >= minimumLadenDaysInHours);
		}

	}

}
