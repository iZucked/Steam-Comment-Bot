/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.peaberry.OptimiserInjectorServiceMaker;

@SuppressWarnings("unused")
@ExtendWith(value = ShiroRunner.class)
@RequireFeature(value = { KnownFeatures.FEATURE_CHARTER_LENGTH, KnownFeatures.FEATURE_PURGE, KnownFeatures.FEATURE_CONTINGENCY_IDLE_TIME })
public class CharterLengthTests extends AbstractMicroTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}

	public IOptimiserInjectorService makeCanalBookingService(boolean panama, boolean purge) {

		return OptimiserInjectorServiceMaker.begin() //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming, boolean.class, panama) //
				.withModuleOverrideBindNamedInstance(ModuleType.Module_LNGTransformerModule, SchedulerConstants.Key_SchedulePurges, boolean.class, purge) //
				.make();
	}

	/**
	 * Create a scenario where we expect the charter length to be equivalent to the
	 * idle. Replace with a sandbox test in future where we can force panama route
	 * and fuel choices.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStandardIdleCase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 3, 1, 0, 0, 0)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false);
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);

		final Idle withoutIdle = findIdleEvent(dischargeSlot, withOutSchedule);
		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true);
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);
		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);

		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(withoutIdle.getDuration(), charterLengthEvent.getDuration());

		Assertions.assertEquals(withoutIdle.getCharterCost(), charterLengthEvent.getCharterCost());
		Assertions.assertEquals(withoutIdle.getFuelCost(), charterLengthEvent.getFuelCost());
		Assertions.assertEquals(withoutIdle.getHeelAtEnd(), charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(getPNL(withOutCargo), getPNL(withCargo) + getPNL(charterLengthEvent));
	}

	/**
	 * Test to make sure contengency appears on the charter length.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStandardIdleCaseWithContingency() throws Exception {

		PortModel portModel = portModelBuilder.getPortModel();
		ContingencyMatrix contingencyMatrix = PortFactory.eINSTANCE.createContingencyMatrix();
		ContingencyMatrixEntry line = PortFactory.eINSTANCE.createContingencyMatrixEntry();
		line.setDuration(1);
		line.setFromPort(portFinder.findPortById(InternalDataConstants.PORT_FUTTSU));
		line.setToPort(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN));
		contingencyMatrix.getEntries().add(line);

		portModel.setContingencyMatrix(contingencyMatrix);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 10, 1, 0, 0, 0)) //
				.withCharterRate("80000") //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setSchedulePurge(true)) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2019, 7, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false);
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);

		final Idle withoutIdle = findIdleEvent(dischargeSlot, withOutSchedule);
		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true);
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);
		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);
		final Idle withIdle = findIdleEvent(dischargeSlot, withSchedule);

		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(withoutIdle.getDuration(), charterLengthEvent.getDuration());
		Assertions.assertEquals(24, withoutIdle.getContingencyHours());
		Assertions.assertEquals(24, charterLengthEvent.getContingencyHours());
		Assertions.assertEquals(0, withIdle.getContingencyHours());
	}

	/**
	 * Create a scenario where we expect the charter length to be equivalent to the
	 * idle but excluding panama waiting days
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPanamaWaitIdleCase() throws Exception {

		// Configure panama
		// Set panama waiting days.
		CanalBookings canalBookings = cargoModelBuilder.getCargoModel().getCanalBookings();
		// This should be the default record.
		canalBookings.getPanamaSeasonalityRecords().get(0).setNorthboundWaitingDays(10);
		canalBookings.getPanamaSeasonalityRecords().get(0).setSouthboundWaitingDays(10);

		// Clear Panama cost to encourage panama voyages
		PanamaCanalTariff panamaCanalTariff = costModelBuilder.getCostModel().getPanamaCanalTariff();
		for (var band : panamaCanalTariff.getBands()) {
			band.setLadenTariff(0.0);
			band.setBallastTariff(0.0);
			band.setBallastRoundtripTariff(0.0);
		}

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 5, 1, 0, 0, 0)) //
				.withCharterRate("80000") //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false, makeCanalBookingService(true, false));
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);

		final Journey withoutJourney = findJourneyEvent(dischargeSlot, withOutSchedule);
		Assertions.assertEquals(RouteOption.PANAMA, withoutJourney.getRouteOption());
		final Idle withoutIdle = findIdleEvent(dischargeSlot, withOutSchedule);
		Assertions.assertEquals(10 * 24, withoutIdle.getPanamaHours());

		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true, makeCanalBookingService(true, false));
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);

		final Journey withJourney = findJourneyEvent(dischargeSlot, withSchedule);
		Assertions.assertEquals(RouteOption.PANAMA, withJourney.getRouteOption());

		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);
		final Idle withIdle = findIdleEvent(dischargeSlot, withSchedule);

		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(withoutIdle.getPanamaHours(), withIdle.getDuration());
		Assertions.assertEquals(withoutIdle.getPanamaHours(), withIdle.getPanamaHours());

		Assertions.assertEquals(withoutIdle.getDuration(), charterLengthEvent.getDuration() + withIdle.getDuration());

		Assertions.assertEquals(withoutIdle.getCharterCost(), charterLengthEvent.getCharterCost() + withIdle.getCharterCost());
		Assertions.assertEquals(withoutIdle.getFuelCost(), charterLengthEvent.getFuelCost() + withIdle.getFuelCost());
		Assertions.assertEquals(withoutIdle.getHeelAtEnd(), charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(getPNL(withOutCargo), getPNL(withCargo) + getPNL(charterLengthEvent));
	}

	/**
	 * Create a scenario where we expect the charter length to be equivalent to the
	 * idle but excluding panama waiting days
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testPanamaWaitAndPurgeIdleCase() throws Exception {

		// Configure panama
		// Set panama waiting days.
		CanalBookings canalBookings = cargoModelBuilder.getCargoModel().getCanalBookings();
		// This should be the default record.
		canalBookings.getPanamaSeasonalityRecords().get(0).setNorthboundWaitingDays(10);
		canalBookings.getPanamaSeasonalityRecords().get(0).setSouthboundWaitingDays(10);

		// Clear Panama cost to encourage panama voyages
		PanamaCanalTariff panamaCanalTariff = costModelBuilder.getCostModel().getPanamaCanalTariff();
		for (var band : panamaCanalTariff.getBands()) {
			band.setLadenTariff(0.0);
			band.setBallastTariff(0.0);
			band.setBallastRoundtripTariff(0.0);
		}

		// Make direct crazy long too
		distanceModelBuilder.setPortToPortDistance(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), RouteOption.DIRECT,
				24_000, true);

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);
		vessel.setPurgeTime(24);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 10, 1, 0, 0, 0)) //
				.withCharterRate("80000") //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 3, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L2", LocalDate.of(2019, 5, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.with(s -> ((LoadSlot) s).setSchedulePurge(true)) //
				.build() //
				.makeDESSale("D2", LocalDate.of(2019, 7, 1), portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 2) //
				.withAssignmentFlags(false, false) //
				.build();

		final Slot<?> dischargeSlot = cargo1.getSlots().get(1);

		evaluate(false, makeCanalBookingService(true, true));
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);

		final Journey withoutJourney = findJourneyEvent(dischargeSlot, withOutSchedule);
		Assertions.assertEquals(RouteOption.PANAMA, withoutJourney.getRouteOption());

		final Idle withoutIdle = findIdleEvent(dischargeSlot, withOutSchedule);
		Assertions.assertEquals(10 * 24, withoutIdle.getPanamaHours());
		Purge withoutPurge = (Purge) withoutIdle.getNextEvent();
		Assertions.assertEquals(24, withoutPurge.getDuration());

		final CargoAllocation withOutCargo = findCargoAllocation(dischargeSlot, withOutSchedule);

		evaluate(true, makeCanalBookingService(true, true));
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);

		final Journey withJourney = findJourneyEvent(dischargeSlot, withSchedule);
		Assertions.assertEquals(RouteOption.PANAMA, withJourney.getRouteOption());

		final CharterLengthEvent charterLengthEvent = findCharterLengthEvent(dischargeSlot, withSchedule);
		Purge withPurge = (Purge) charterLengthEvent.getNextEvent();
		Assertions.assertEquals(24, withPurge.getDuration());

		final Idle withIdle = findIdleEvent(dischargeSlot, withSchedule);

		final CargoAllocation withCargo = findCargoAllocation(dischargeSlot, withSchedule);

		Assertions.assertEquals(withoutPurge.getDuration(), withPurge.getDuration());

		Assertions.assertEquals(withoutIdle.getPanamaHours(), withIdle.getDuration());
		Assertions.assertEquals(withoutIdle.getPanamaHours(), withIdle.getPanamaHours());

		Assertions.assertEquals(withoutIdle.getDuration(), charterLengthEvent.getDuration() + withIdle.getDuration());

		Assertions.assertEquals(withoutIdle.getCharterCost(), charterLengthEvent.getCharterCost() + withIdle.getCharterCost());

		// Function to sum mmbtu for LNG fuels and MT for bunker fuels
		BiFunction<Fuel, FuelUsage, Long> func = (fuel, fuelUsage) -> {
			long sum = 0;
			for (var fq : fuelUsage.getFuels()) {
				if (fq.getFuel() == fuel) {
					for (var fa : fq.getAmounts()) {
						if (fuel == Fuel.NBO || fuel == Fuel.FBO) {
							if (fa.getUnit() == FuelUnit.MMBTU) {
								sum += fa.getQuantity();
							}
						} else {
							// (fuel == Fuel.BASE_FUEL || fuel == Fuel.PILOT_LIGHT) {
							if (fa.getUnit() == FuelUnit.MT) {
								sum += fa.getQuantity();
							}
						}
					}
				}
			}
			return sum;
		};

		// Check before and after fuel quantities add up...
		for (Fuel fuel : Fuel.values()) {
			long before = func.apply(fuel, withoutIdle);
			long after = func.apply(fuel, withIdle) + func.apply(fuel, charterLengthEvent);
			Assertions.assertEquals(before, after);
		}
		// ... as LNG fuel cost can differ as the price is zero in the charter length
		// Assertions.assertEquals(withoutIdle.getFuelCost(),
		// charterLengthEvent.getFuelCost() + withIdle.getFuelCost());

		Assertions.assertEquals(0, withPurge.getHeelAtEnd());
		Assertions.assertEquals(withoutPurge.getHeelAtEnd(), withPurge.getHeelAtEnd());
		Assertions.assertEquals(getPNL(withOutCargo), getPNL(withCargo) + getPNL(charterLengthEvent), 1.0);
	}

	/**
	 * Construct a test case where the Charter Length (cooldown rule change really)
	 * we would prefer to heel out and convert idle to charter length where as with
	 * it disabled we are forced to NBO until the end.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testStandardIdleWithCooldownCase() throws Exception {

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 1, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2019, 3, 1, 0, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.build();

		// Construct the cargo scenario

		// Create cargo 1, cargo 2
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2019, 1, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2019, 2, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "20") //
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
	 * Construct a test case where cargoes spanning the period backend are late.
	 * Removing cargoes to fix lateness will introduce charter length, but this
	 * conflicts with end heel changes. Regression test for P!
	 *
	 * @throws Exception
	 */
	@Disabled("Test does not work. Cargo does not get moved")
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testWithLateCargoesAfterPeriod() throws Exception {

		// Create the required basic elements
		final Vessel vessel1 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel1.setSafetyHeel(500);
		final Vessel vessel2 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
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

		Port loadPort = portFinder.findPortById(InternalDataConstants.PORT_ONSLOW);
		Port dischargePort = portFinder.findPortById(InternalDataConstants.PORT_FUTTSU);

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
		// final Slot<?> load2 = cargob2.getSlots().get(0);
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

	public static @NonNull CharterLengthEvent findCharterLengthEvent(final Slot<?> slot, final Schedule schedule) {
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

	private <T> @NonNull T findFirstEventOnSameSequence(final Slot<?> slot, final Schedule schedule, Class<T> cls) {
		final Optional<Event> slotVisit = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit && (((SlotVisit) evt).getSlotAllocation().getSlot() == slot))) //
				.findFirst();

		Assertions.assertTrue(slotVisit.isPresent());
		Event evt = slotVisit.get();
		for (Event e : evt.getSequence().getEvents()) {
			if (cls.isInstance(e)) {
				return cls.cast(e);
			}
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

	private @NonNull Journey findJourneyEvent(final Slot<?> slot, final Schedule schedule) {
		final Optional<Event> discharge = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit && (((SlotVisit) evt).getSlotAllocation().getSlot() == slot))) //
				.findFirst();
		Assertions.assertTrue(discharge.isPresent());
		Event evt = discharge.get();
		evt = evt.getNextEvent();
		if (evt instanceof Journey journey) {
			return journey;
		}

		Assertions.fail("");
		throw new IllegalStateException();
	}

	private @NonNull Idle findIdleEvent(final Slot<?> slot, final Schedule schedule) {
		final Optional<Event> discharge = schedule.getSequences().stream() //
				.flatMap(s -> s.getEvents().stream()) //
				.filter(evt -> (evt instanceof SlotVisit sv && (sv.getSlotAllocation().getSlot() == slot))) //
				.findFirst();
		Assertions.assertTrue(discharge.isPresent());
		Event evt = discharge.get();
		evt = evt.getNextEvent();
		if (evt instanceof Journey) {
			evt = evt.getNextEvent();
		}
		if (evt instanceof Idle idle) {
			return idle;
		}
		Assertions.fail("");
		throw new IllegalStateException();
	}

	private void evaluate(final boolean withCharterLength) {
		evaluate(withCharterLength, null);
	}

	private void evaluate(final boolean withCharterLength, IOptimiserInjectorService s) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		userSettings.setWithCharterLength(withCharterLength);

		final LNGOptimisationBuilder builder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withUserSettings(userSettings) //
				.withThreadCount(1) //
		;
		if (s != null) {
			builder.withOptimiserInjectorService(s);
		}

		final LNGOptimisationRunnerBuilder runnerBuilder = builder.buildDefaultRunner();
		runnerBuilder.evaluateInitialState();
	}

	private Schedule optimise(final boolean withCharterLength, LocalDate periodStart, YearMonth periodEnd) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
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

		runnerBuilder.evaluateInitialState();
		return runnerBuilder.runAndReturnSchedule();
	}

	/**
	 * Based on an extract from a P scenario where the charter length violated the
	 * heel constraints - which should never happen.
	 * 
	 * @throws Exception
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testIdleAfterStart() throws Exception {

		int safetyHeel = 500;

		// Create the required basic elements
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(safetyHeel);

		final VesselAvailability charter_1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2019, 12, 1, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2020, 1, 23, 0, 0, 0), LocalDateTime.of(2020, 2, 9, 0, 0, 0)) //
				.withStartHeel(4_450, 5_000, 23, "") //
				.withEndHeel(0, 0, EVesselTankState.EITHER, "") //
				.withStartPort(portFinder.findPortById(InternalDataConstants.PORT_HIMEJI)) //
				.build();

		// Construct the cargo scenario

		// Create cargo 1
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2020, 1, 3), portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2020, 1, 13), portFinder.findPortById(InternalDataConstants.PORT_INCHEON), null, entity, "7") //
				.build() //
				.withVesselAssignment(charter_1, 1) //
				.withAssignmentFlags(false, false) //
				.build();

		final BaseFuel testHFO = fleetModelBuilder.createBaseFuel("TestHFO", 40.0);
		costModelBuilder.createOrUpdateBaseFuelCost(testHFO, "100");
		vessel.setBaseFuel(testHFO);
		vessel.setIdleBaseFuel(testHFO);

		final Slot<?> slot = cargo1.getSlots().get(0);

		// First evaluate without the charter length enabled
		evaluate(false);
		final Schedule withOutSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withOutSchedule);
		final Idle idle = findFirstEventOnSameSequence(slot, withOutSchedule, Idle.class);

		// Then evaluate with the charter length enabled
		evaluate(true);
		final Schedule withSchedule = ScenarioModelUtil.findSchedule(scenarioDataProvider);
		Assertions.assertNotNull(withSchedule);
		final CharterLengthEvent charterLengthEvent = findFirstEventOnSameSequence(slot, withSchedule, CharterLengthEvent.class);

		// Lets make sure everything matches up - the charter length should have
		// replaced the idle
		Assertions.assertEquals(idle.getHeelAtEnd(), charterLengthEvent.getHeelAtEnd());
		Assertions.assertEquals(idle.getHeelAtStart(), charterLengthEvent.getHeelAtStart());

		Assertions.assertEquals(idle.getDuration(), charterLengthEvent.getDuration());

		// Previous buggy code would report a violation
		Assertions.assertTrue(charterLengthEvent.getViolations().isEmpty());
	}

	private long getPNL(final Object obj) {
		return ScheduleModelUtils.getProfitAndLossContainer(obj).getGroupProfitAndLoss().getProfitAndLoss();
	}
}