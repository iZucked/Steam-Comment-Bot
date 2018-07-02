/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;

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

		Assertions.assertEquals(0, charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(500, idle.getHeelAtEnd());
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

	private long getPNL(final Object obj) {
		return ScheduleModelUtils.getProfitAndLossContainer(obj).getGroupProfitAndLoss().getProfitAndLoss();
	}
}