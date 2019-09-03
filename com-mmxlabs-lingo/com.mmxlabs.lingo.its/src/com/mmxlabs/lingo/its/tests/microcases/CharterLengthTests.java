/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.data.distances.DataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.TimePeriod;

@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
public class CharterLengthTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("charter-length");
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	/**
	 * Create a scenario where we expect the charter length to be equivalent to the idle
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStandardIdleCase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 3, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false);
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);
		final Idle idle = findIdleEvent(dischargeSlot, withOutSchedule);
		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true);
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);
		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);
		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(idle.getDuration(), charterLengthEvent.getDuration());
		Assertions.assertEquals(idle.getCharterCost(), charterLengthEvent.getCharterCost());
		Assertions.assertEquals(idle.getFuelCost(), charterLengthEvent.getFuelCost());
		Assertions.assertEquals(idle.getHeelAtEnd(), charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(getPNL(withOutCargo), getPNL(withCargo) + getPNL(charterLengthEvent));
	}

	/**
	 * Construct a test case where the Charter Length (cooldown rule change really) we would prefer to heel out and convert idle to charter length where as with it disabled we are forced to NBO until
	 * the end.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStandardIdleWithCooldownCase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel("STEAM-145");
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 3, 1, 0, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 2, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "20") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final BaseFuel testHFO = fleetModelBuilder.createBaseFuel("TestHFO", 40.0);
		costModelBuilder.createOrUpdateBaseFuelCost(testHFO, "100");
		vessel.setBaseFuel(testHFO);
		vessel.setIdleBaseFuel(testHFO);

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false);
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);
		final Idle idle = findIdleEvent(dischargeSlot, withOutSchedule);
		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true);
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);
		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);
		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(500, charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(500, idle.getHeelAtEnd());
	}

	/**
	 * Construct a test case where cargoes spanning the period backend are late. Removing cargoes to fix lateness will introduce charter length, but this conflicts with end heel changes. Regression
	 * test for P!
	 *
	 * @throws Exception
	 */
	@Disabled("Test does not work. Cargo does not get moved")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithLateCargoesAfterPeriod() throws Exception {
		updateDistanceData(scenarioDataProvider, DataConstants.DISTANCES_LATEST_JSON);
		updatePortsData(scenarioDataProvider, DataConstants.PORTS_LATEST_JSON);

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel("STEAM-145");
		vessel1.setSafetyHeel(500);
		final Vessel vessel2 = fleetModelFinder.findVessel("STEAM-138");
		vessel2.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel1, entity) //
				.withCharterRate("80000") //
				.withEndHeel(0, 0, EVesselTankState.EITHER, "") //
				.build();
		final VesselAvailability charter_2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.withCharterRate("80000") //
				.withEndHeel(0, 0, EVesselTankState.EITHER, "") //
				.build();

		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2019, 8, 14), LocalDate.of(2019, 10, 23));
		scenarioModelBuilder.setScheduleHorizon(LocalDate.of(2020, 4, 1));

		Port loadPort = portFinder.findPort("Onslow");
		Port dischargePort = portFinder.findPort("Futtsu");

		final Cargo cargoa1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("La1", LocalDate.of(2019, 11, 5), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.makeDESSale("Da1", LocalDate.of(2019, 11, 24), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargoa2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("La2", LocalDate.of(2020, 1, 8), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Da2", LocalDate.of(2020, 1, 22), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_1, 2) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargoa3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("La3", LocalDate.of(2020, 2, 2), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Da3", LocalDate.of(2020, 2, 1), dischargePort, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(charter_1, 3) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargoa4 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("La4", LocalDate.of(2020, 2, 27), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Da4", LocalDate.of(2020, 3, 12), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_1, 4) //
				.withAssignmentFlags(false, true) //
				.build();

		final Cargo cargob1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb1", LocalDate.of(2019, 11, 27), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db1", LocalDate.of(2019, 12, 11), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_2, 1) //
				.withAssignmentFlags(false, false) //
				.build();
		final Cargo cargob2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb2", LocalDate.of(2019, 12, 6), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db2", LocalDate.of(2020, 12, 01), dischargePort, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS) //

				.build() //
				.withVesselAssignment(charter_2, 2) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargob3 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb3", LocalDate.of(2019, 12, 12), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db3", LocalDate.of(2019, 12, 1), dischargePort, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS) //
				.build() //
				.withVesselAssignment(charter_2, 3) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargob4 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb4", LocalDate.of(2020, 1, 10), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db4", LocalDate.of(2020, 1, 1), dischargePort, null, entity, "7") //
				.withWindowSize(1, TimePeriod.MONTHS) //

				.build() //
				.withVesselAssignment(charter_2, 4) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargob5 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb5", LocalDate.of(2020, 2, 9), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db5", LocalDate.of(2020, 2, 23), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_2, 5) //
				.withAssignmentFlags(false, true) //
				.build();
		final Cargo cargob6 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Lb6", LocalDate.of(2020, 3, 4), loadPort, null, entity, "5") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.makeDESSale("Db6", LocalDate.of(2020, 3, 18), dischargePort, null, entity, "7") //
				.withWindowSize(0, TimePeriod.DAYS) //

				.build() //
				.withVesselAssignment(charter_2, 6) //
				.withAssignmentFlags(false, true) //
				.build();

		final Slot<?> load2 = cargob1.getSlots().get(0);
//		final Slot<?> load2 = cargob2.getSlots().get(0);
		// Without charter length, all ok
		{
			final Schedule schedule = optimise(false, null, YearMonth.of(2020, 1));
			Assertions.assertNotNull(schedule);
			final CargoAllocation ca = findCargoAllocation(load2, schedule);
			Assertions.assertSame(charter_1, ca.getSequence().getVesselAvailability());
		}
		// With charter length, cannot move
		{
			final Schedule schedule = optimise(true, null, YearMonth.of(2020, 1));
			Assertions.assertNotNull(schedule);
			final CargoAllocation ca = findCargoAllocation(load2, schedule);
			Assertions.assertSame(charter_2, ca.getSequence().getVesselAvailability());

		}

		// Pull in all late cargoes
		// OR charter length should NOT permit empty heel in this case.

	}

	private @NonNull CharterLengthEvent findCharterLengthEvent(final Slot<?> slot, final Schedule schedule) {
		final Optional<Event> discharge = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit && (((SlotVisit) evt).getSlotAllocation().getSlot() == slot))) //
				.findFirst();
		Assertions.assertTrue(discharge.isPresent());
		Event evt = discharge.get();
		evt = evt.getNextEvent();
		if (evt instanceof Journey) {
			evt = evt.getNextEvent();
		}
		if (evt instanceof Idle) {
			// There is still an empty idle event generated.
			evt = evt.getNextEvent();
		}
		if (evt instanceof CharterLengthEvent) {
			return (CharterLengthEvent) evt;
		}
		Assertions.fail("");
		throw new IllegalStateException();
	}

	private @NonNull CargoAllocation findCargoAllocation(final Slot<?> slot, final Schedule schedule) {
		final Optional<Event> discharge = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit && (((SlotVisit) evt).getSlotAllocation().getSlot() == slot))) //
				.findFirst();
		Assertions.assertTrue(discharge.isPresent());
		final Event evt = discharge.get();
		if (evt instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) evt;
			return slotVisit.getSlotAllocation().getCargoAllocation();
		}
		Assertions.fail("");
		throw new IllegalStateException();
	}

	private @NonNull Idle findIdleEvent(final Slot<?> slot, final Schedule schedule) {
		final Optional<Event> discharge = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit && (((SlotVisit) evt).getSlotAllocation().getSlot() == slot))) //
				.findFirst();
		Assertions.assertTrue(discharge.isPresent());
		Event evt = discharge.get();
		evt = evt.getNextEvent();
		if (evt instanceof Journey) {
			evt = evt.getNextEvent();
		}
		if (evt instanceof Idle) {
			return (Idle) evt;
		}
		Assertions.fail("");
		throw new IllegalStateException();
	}

	private void evaluate(final boolean withCharterLength) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setWithCharterLength(withCharterLength);

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withThreadCount(1) //
				.buildDefaultRunner();
		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}
	}

	private Schedule optimise(final boolean withCharterLength, LocalDate periodStart, YearMonth periodEnd) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setWithCharterLength(withCharterLength);

		if (periodStart != null) {
			userSettings.setPeriodStartDate(periodStart);
		}
		if (periodEnd != null) {
			userSettings.setPeriodEnd(periodEnd);
		}

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withThreadCount(1) //
				.withOptimiseHint() //
				.withOptimisationPlanCustomiser(plan -> {
					ScenarioUtils.setLSOStageIterations(plan, 400_000);
					ScenarioUtils.setHillClimbStageIterations(plan, 30_000);
				}).buildDefaultRunner();
		try {
			runnerBuilder.evaluateInitialState();
			return runnerBuilder.runAndReturnSchedule();
		} finally {
			runnerBuilder.dispose();
		}
	}

	private long getPNL(final Object obj) {
		return ScheduleModelUtils.getProfitAndLossContainer(obj).getGroupProfitAndLoss().getProfitAndLoss();
	}
}