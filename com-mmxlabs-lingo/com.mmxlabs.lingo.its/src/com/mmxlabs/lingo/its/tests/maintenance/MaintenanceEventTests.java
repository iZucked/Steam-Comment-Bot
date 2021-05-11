package com.mmxlabs.lingo.its.tests.maintenance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.VesselAvailabilityMaker;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@ExtendWith(ShiroRunner.class)
public class MaintenanceEventTests extends AbstractMicroTestCase {

	private static final String VESSEL_MEDIUM = InternalDataConstants.REF_VESSEL_STEAM_145;

	private static final String SEQ_ONLY_MAINTENANCE = "s-i-vev-i-e";
	private static final String SEQ_ONLY_MAINTENANCE_W_COOLDOWN_BEFORE_END = "s-i-vev-i-c-e";
	private static final String SEQ_ONLY_MAINTENANCE_W_JOURNEY = "s-j-i-vev-j-i-e";

	private static final List<Class<? extends Event>> HANDLED_EVENT_CLASSES;

	/*
	 * Set of classes for which checkFuelSequence is currently defined,
	 * checkFuelSequence and this set may have to be updated when adding new
	 * tests/changing model.
	 */
	static {
		HANDLED_EVENT_CLASSES = new ArrayList<>();
		HANDLED_EVENT_CLASSES.add(StartEvent.class);
		HANDLED_EVENT_CLASSES.add(Journey.class);
		HANDLED_EVENT_CLASSES.add(Idle.class);
		HANDLED_EVENT_CLASSES.add(VesselEventVisit.class);
		HANDLED_EVENT_CLASSES.add(Cooldown.class);
		HANDLED_EVENT_CLASSES.add(EndEvent.class);
	}

	@SuppressWarnings("null")
	private @NonNull VesselAvailabilityMaker populateDefaultVesselAvailablityDetails(final Vessel vessel) {
		return cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 1, 1, 0, 0), LocalDateTime.of(2017, 1, 1, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 8, 1, 0, 0), LocalDateTime.of(2017, 8, 1, 0, 0)) //
				.withCharterRate("70000");
	}

	private MaintenanceEvent makeDefaultMaintenanceEvent(final @NonNull Port maintenancePort, final @NonNull VesselAvailability vesselAvailability) {
		return cargoModelBuilder.makeMaintenanceEvent("Maintenance-145", LocalDateTime.of(2017, 3, 4, 3, 0), LocalDateTime.of(2017, 3, 14, 0, 0), maintenancePort) //
				.withDurationInDays(5).withVesselAssignment(vesselAvailability, 0) //
				.build();
	}

	@SuppressWarnings("null")
	private Schedule fetchSchedule() {
		return ScenarioModelUtil.findSchedule(scenarioDataProvider);
	}

	@NonNull
	private List<Class<? extends Event>> buildExpectedClassSequence(final @NonNull String stringSequence) {
		final List<Class<? extends Event>> expectedClassSequence = new ArrayList<>();
		final String[] splitString = stringSequence.split("-");
		for (final String eventIdentifier : splitString) {
			final Class<? extends Event> expectedClass;
			if (eventIdentifier.equals("s")) {
				expectedClass = StartEvent.class;
			} else if (eventIdentifier.equals("j")) {
				expectedClass = Journey.class;
			} else if (eventIdentifier.equals("i")) {
				expectedClass = Idle.class;
			} else if (eventIdentifier.equals("vev")) {
				expectedClass = VesselEventVisit.class;
			} else if (eventIdentifier.equals("e")) {
				expectedClass = EndEvent.class;
			} else if (eventIdentifier.equals("c")) {
				expectedClass = Cooldown.class;
			} else {
				throw new IllegalStateException(String.format("Unknown event identifier %s.", eventIdentifier));
			}
			expectedClassSequence.add(expectedClass);
		}
		return expectedClassSequence;
	}

	@NonNull
	private Pair<Boolean, Boolean> getBoiloffAndBunkerUse(final FuelUsage fuelUsage) {
		boolean hasLngBoiloff = false;
		boolean hasBunkerUse = false;
		for (final FuelQuantity fuelQuantity : fuelUsage.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			if (fuel == Fuel.NBO || fuel == Fuel.FBO) {
				hasLngBoiloff = true;
			} else if (fuel == Fuel.BASE_FUEL) {
				hasBunkerUse = true;
			}
		}
		return Pair.of(hasLngBoiloff, hasBunkerUse);
	}

	/*
	 * Checks that the fuels used in travelling make sense with respect to presence
	 * of LNG onboard
	 */
	private void checkFuelSequence(final @NonNull List<Event> events) {
		assert !events.isEmpty();
		final Iterator<Event> iterEvent = events.iterator();
		final Event startEvent = iterEvent.next();
		Assertions.assertTrue(startEvent instanceof StartEvent);
		int runningHeel = startEvent.getHeelAtEnd();
		while (iterEvent.hasNext()) {
			final Event nextEvent = iterEvent.next();
			if (!(nextEvent instanceof Cooldown)) {
				Assertions.assertEquals(runningHeel, nextEvent.getHeelAtStart());
			}
			if (nextEvent instanceof Idle || nextEvent instanceof Journey) {
				if (!nextEvent.getStart().equals(nextEvent.getEnd())) {
					final FuelUsage fuelUsage = (FuelUsage) nextEvent;
					final boolean usesNbo = fuelUsage.getFuels().stream().map(FuelQuantity::getFuel).anyMatch(fuel -> fuel == Fuel.NBO || fuel == Fuel.FBO);
					final boolean fuelMismatch = (runningHeel > 0 && !usesNbo) || (runningHeel == 0 && usesNbo);
					Assertions.assertFalse(fuelMismatch);
				}
			} else if (nextEvent instanceof Cooldown) {
				Assertions.assertEquals(0, runningHeel);
			} else if (nextEvent instanceof StartEvent) {
				Assertions.fail("Start event cannot be in the middle of a sequence of events");
			} else if (nextEvent instanceof EndEvent && iterEvent.hasNext()) {
				Assertions.fail("End event cannot be in the middle of a sequence of events");
			} else {
				if (HANDLED_EVENT_CLASSES.stream().noneMatch(handledClass -> handledClass.isInstance(nextEvent))) {
					Assertions.fail(String.format("Unhandled event type: %s.", nextEvent.getClass().getName()));
				}
			}
			runningHeel = nextEvent.getHeelAtEnd();
		}
	}

	/*
	 * Checks that the sequence of classes in expectedSequence is matched by the
	 * sequence of instances in events.
	 */
	private void checkSequence(final @NonNull List<Class<? extends Event>> expectedClassSequence, final @NonNull List<Event> events) {
		Assertions.assertEquals(expectedClassSequence.size(), events.size());
		final Iterator<Event> eventIter = events.iterator();
		for (final Class<? extends Event> expectedClass : expectedClassSequence) {
			Assertions.assertTrue(expectedClass.isInstance(eventIter.next()));
		}
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: enough for entire sequence
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesAllLng() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(15_000, 150_000, 22.67, "0.01") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.build();
		vesselAvailability.setFleet(true);
		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have (strictly positive) heel during all events
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertTrue(event.getHeelAtStart() > 0);
			Assertions.assertTrue(event.getHeelAtEnd() > 0);
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final boolean hasLngBoiloff = idleEvent.getFuels().stream().map(FuelQuantity::getFuel).anyMatch(fuel -> fuel == Fuel.NBO || fuel == Fuel.FBO);
				Assertions.assertTrue(hasLngBoiloff);
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: None available throughout sequence
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesNoLng() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(0, 0, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have zero heel during all events
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertEquals(0, event.getHeelAtStart());
			Assertions.assertEquals(0, event.getHeelAtEnd());
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final boolean hasLngBoiloff = idleEvent.getFuels().stream().map(FuelQuantity::getFuel).anyMatch(fuel -> fuel == Fuel.NBO || fuel == Fuel.FBO);
				Assertions.assertFalse(hasLngBoiloff);
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: runs out before the maintenance event starts
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesLngRunsOutBeforeEvent() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(3_000, 3_000, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have heel before maintenance and none after
		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final boolean hasLngBoiloff = idleEvent.getFuels().stream().map(FuelQuantity::getFuel).anyMatch(fuel -> fuel == Fuel.NBO || fuel == Fuel.FBO);
				if (foundMaintenance) {
					Assertions.assertEquals(0, event.getHeelAtStart());
					Assertions.assertEquals(0, event.getHeelAtEnd());
					Assertions.assertFalse(hasLngBoiloff);
				} else {
					Assertions.assertTrue(event.getHeelAtStart() > 0);
					Assertions.assertTrue(hasLngBoiloff);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: runs out before maintenance event starts
	 * 
	 * - other: end event requires cooldown
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesLngRunsOutBeforeEventEndCold() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(3_000, 3_000, 22.67, "0.01") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_COOLDOWN_BEFORE_END);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have heel before maintenance and none after
		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final boolean hasLngBoiloff = idleEvent.getFuels().stream().map(FuelQuantity::getFuel).anyMatch(fuel -> fuel == Fuel.NBO || fuel == Fuel.FBO);
				if (foundMaintenance) {
					Assertions.assertEquals(0, event.getHeelAtStart());
					Assertions.assertEquals(0, event.getHeelAtEnd());
					Assertions.assertFalse(hasLngBoiloff);
				} else {
					Assertions.assertTrue(event.getHeelAtStart() > 0);
					Assertions.assertTrue(hasLngBoiloff);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: runs out after the maintenance event
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesLngRunsOutAfterEvent() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(18_000, 20_000, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have heel before maintenance and after maintenance.
		// After maintenance NBO time should be less than idle time
		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(event.getHeelAtStart() > 0);
				Assertions.assertTrue(hasLngBoiloff);
				if (foundMaintenance) {
					Assertions.assertEquals(0, event.getHeelAtEnd());
					Assertions.assertTrue(hasBunkerUse);
				} else {
					Assertions.assertTrue(event.getHeelAtEnd() > 0);
					Assertions.assertFalse(hasBunkerUse);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: runs out after maintenance event starts
	 * 
	 * - other: end event requires cooldown
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesLngRunsOutAfterEventEndCold() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(18_000, 20_000, 22.67, "0.01") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_COOLDOWN_BEFORE_END);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// Sequence should have heel before maintenance and after maintenance.
		// After maintenance NBO time should be less than idle time
		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(event.getHeelAtStart() > 0);
				Assertions.assertTrue(hasLngBoiloff);
				if (foundMaintenance) {
					Assertions.assertEquals(0, event.getHeelAtEnd());
					Assertions.assertTrue(hasBunkerUse);
				} else {
					Assertions.assertTrue(event.getHeelAtEnd() > 0);
					Assertions.assertFalse(hasBunkerUse);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: have too much so cannot meet end volume limits
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesTooMuchLng() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final int minAllowedEndHeel = 500;
		final int maxAllowedEndHeel = 1_000;
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(50_000, 150_000, 22.67, "0.01") //
				.withEndHeel(minAllowedEndHeel, maxAllowedEndHeel, EVesselTankState.MUST_BE_COLD, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// All events should start and end with heel
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertTrue(event.getHeelAtStart() > 0);
			Assertions.assertTrue(event.getHeelAtEnd() > 0);
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(hasLngBoiloff);
				Assertions.assertFalse(hasBunkerUse);
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		// Last event end heel should be larger than max allowed
		Assertions.assertTrue(events.get(events.size() - 1).getHeelAtEnd() > maxAllowedEndHeel);
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: idles throughout
	 * 
	 * - LNG status: have too little so we cannot meet end volume limits
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceNoCargoesTooLittleLng() {
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final int minAllowedEndHeel = 500;
		final int maxAllowedEndHeel = 1_000;
		final VesselAvailability vesselAvailability = populateDefaultVesselAvailablityDetails(vessel) //
				.withStartHeel(20_000, 45_000, 22.67, "0.01") //
				.withEndHeel(minAllowedEndHeel, maxAllowedEndHeel, EVesselTankState.MUST_BE_COLD, "") //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// All events should start and end with heel
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertTrue(event.getHeelAtStart() > 0);
			Assertions.assertTrue(event.getHeelAtEnd() > 0);
			if (event instanceof Idle) {
				final Idle idleEvent = (Idle) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(hasLngBoiloff);
				Assertions.assertFalse(hasBunkerUse);
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			}
		}
		// Last event end heel should be larger than max allowed
		Assertions.assertTrue(events.get(events.size() - 1).getHeelAtEnd() < minAllowedEndHeel);
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: pre and post maintenance journeys travel via Panama
	 * 
	 * - LNG status: have enough for entire sequence
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceTravelsViaPanamaAllLng() {
		final Port startPort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final Port endPort = startPort;
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 15, 0, 0), LocalDateTime.of(2017, 2, 15, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 5, 0, 0), LocalDateTime.of(2017, 4, 5, 0, 0)) //
				.withCharterRate("70000") //
				.withStartHeel(15_000, 150_000, 22.67, "0.01") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.withStartPort(startPort) //
				.withEndPort(endPort) //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_JOURNEY);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// All events should start and end with heel
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertTrue(event.getHeelAtStart() > 0);
			Assertions.assertTrue(event.getHeelAtEnd() > 0);
			if (event instanceof Idle) {
				if (!event.getStart().equals(event.getEnd())) {
					final Idle idleEvent = (Idle) event;
					final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
					final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
					final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
					Assertions.assertTrue(hasLngBoiloff);
					Assertions.assertFalse(hasBunkerUse);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			} else if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(journey);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(hasLngBoiloff);
				Assertions.assertFalse(hasBunkerUse);
				// All journeys should be using the Panama canal
				Assertions.assertNotNull(journey.getCanalDateTime());
				Assertions.assertEquals(RouteOption.PANAMA, journey.getRouteOption());
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: pre and post maintenance journeys travel via Panama
	 * 
	 * - LNG status: runs out before maintenance event
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceTravelsViaPanamaRunsOutBeforeEvent() {
		final Port startPort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final Port endPort = startPort;
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 15, 0, 0), LocalDateTime.of(2017, 2, 15, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 5, 0, 0), LocalDateTime.of(2017, 4, 5, 0, 0)) //
				.withCharterRate("70000") //
				.withStartHeel(5_000, 5_000, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "") //
				.withStartPort(startPort) //
				.withEndPort(endPort) //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_JOURNEY);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				if (!event.getStart().equals(event.getEnd())) {
					final Idle idleEvent = (Idle) event;
					final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
					final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
					final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
					if (foundMaintenance) {
						Assertions.assertEquals(0, event.getHeelAtStart());
						Assertions.assertEquals(0, event.getHeelAtEnd());
						Assertions.assertFalse(hasLngBoiloff);
						Assertions.assertTrue(hasBunkerUse);
					} else {
						Assertions.assertTrue(event.getHeelAtStart() >= 0);
						Assertions.assertEquals(0, event.getHeelAtEnd());
						Assertions.assertTrue(hasLngBoiloff);
						Assertions.assertFalse(hasBunkerUse);
					}
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			} else if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(journey);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();

				if (foundMaintenance) {
					Assertions.assertEquals(0, event.getHeelAtStart());
					Assertions.assertEquals(0, event.getHeelAtEnd());
					Assertions.assertFalse(hasLngBoiloff);
					Assertions.assertTrue(hasBunkerUse);
				} else {
					Assertions.assertTrue(event.getHeelAtStart() > 0);
					Assertions.assertTrue(hasLngBoiloff);
					Assertions.assertTrue(hasBunkerUse);
				}
				// All journeys should be using the Panama canal
				Assertions.assertNotNull(journey.getCanalDateTime());
				Assertions.assertEquals(RouteOption.PANAMA, journey.getRouteOption());
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: pre and post maintenance journeys travel via Panama
	 * 
	 * - LNG status: runs out before maintenance event
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceTravelsViaPanamaRunsOutAfterEvent() {
		final Port startPort = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final Port endPort = startPort;
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 01, 0, 0), LocalDateTime.of(2017, 2, 01, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 5, 0, 0), LocalDateTime.of(2017, 4, 5, 0, 0)) //
				.withCharterRate("70000") //
				.withStartHeel(5_000, 12_000, 22.67, "0.01") //
				.withEndHeel(0, 500, EVesselTankState.EITHER, "") //
				.withStartPort(startPort) //
				.withEndPort(endPort) //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_JOURNEY);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		boolean foundMaintenance = false;
		for (final Event event : events) {
			if (event instanceof Idle) {
				if (!event.getStart().equals(event.getEnd())) {
					final Idle idleEvent = (Idle) event;
					final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
					final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
					final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
					if (foundMaintenance) {
						Assertions.assertTrue(event.getHeelAtStart() >= 0);
						Assertions.assertEquals(0, event.getHeelAtEnd());
						Assertions.assertTrue(hasLngBoiloff);
						Assertions.assertTrue(hasBunkerUse);
					} else {
						Assertions.assertTrue(event.getHeelAtStart() > 0);
						Assertions.assertTrue(event.getHeelAtEnd() > 0);
						Assertions.assertTrue(hasLngBoiloff);
						Assertions.assertFalse(hasBunkerUse);
					}
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			} else if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(journey);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();

				if (foundMaintenance) {
					Assertions.assertTrue(hasLngBoiloff);
					Assertions.assertTrue(hasBunkerUse);
				} else {
					Assertions.assertTrue(event.getHeelAtStart() > 0);
					Assertions.assertTrue(event.getHeelAtEnd() > 0);
					Assertions.assertTrue(hasLngBoiloff);
					Assertions.assertFalse(hasBunkerUse);
				}
				// All journeys should be using the Panama canal
				Assertions.assertNotNull(journey.getCanalDateTime());
				Assertions.assertEquals(RouteOption.PANAMA, journey.getRouteOption());
			} else if (event instanceof EndEvent) {
				Assertions.assertEquals(0, event.getHeelAtStart());
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}

	/**
	 * Test for
	 * 
	 * - sequence: start -> maintenance -> end
	 * 
	 * - travel statuses: pre and post maintenance journeys travel via Suez
	 * 
	 * - LNG status: have enough for entire sequence
	 */
	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSingleMaintenanceTravelsViaSuezAllLng() {
		final Port startPort = portFinder.findPortById(InternalDataConstants.PORT_BARCELONA);
		final Port endPort = startPort;
		final Port maintenancePort = portFinder.findPortById(InternalDataConstants.PORT_DAHEJ);

		final Vessel vessel = fleetModelFinder.findVessel(VESSEL_MEDIUM);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2017, 2, 15, 0, 0), LocalDateTime.of(2017, 2, 15, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2017, 4, 3, 0, 0), LocalDateTime.of(2017, 4, 3, 0, 0)) //
				.withCharterRate("70000") //
				.withStartHeel(15_000, 150_000, 22.67, "0.01") //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, "") //
				.withStartPort(startPort) //
				.withEndPort(endPort) //
				.build();
		vesselAvailability.setFleet(true);

		final MaintenanceEvent maintenanceEvent = makeDefaultMaintenanceEvent(maintenancePort, vesselAvailability);

		evaluateTest();

		final Schedule schedule = fetchSchedule();
		Assertions.assertNotNull(schedule);

		final List<Sequence> sequences = schedule.getSequences();
		Assertions.assertEquals(1, sequences.size());

		final Sequence sequence = sequences.get(0);
		final List<Event> events = sequence.getEvents();

		final List<Class<? extends Event>> expectedClassSequence = buildExpectedClassSequence(SEQ_ONLY_MAINTENANCE_W_JOURNEY);
		checkSequence(expectedClassSequence, events);
		checkFuelSequence(events);

		// All events should start and end with heel
		boolean foundMaintenance = false;
		for (final Event event : events) {
			Assertions.assertTrue(event.getHeelAtStart() > 0);
			Assertions.assertTrue(event.getHeelAtEnd() > 0);
			if (event instanceof Idle) {
				if (!event.getStart().equals(event.getEnd())) {
					final Idle idleEvent = (Idle) event;
					final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(idleEvent);
					final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
					final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
					Assertions.assertTrue(hasLngBoiloff);
					Assertions.assertFalse(hasBunkerUse);
				}
			} else if (event instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) event;
				if (vev.getVesselEvent() == maintenanceEvent) {
					foundMaintenance = true;
				}
			} else if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				final Pair<Boolean, Boolean> boiloffAndBunkerUse = getBoiloffAndBunkerUse(journey);
				final boolean hasLngBoiloff = boiloffAndBunkerUse.getFirst();
				final boolean hasBunkerUse = boiloffAndBunkerUse.getSecond();
				Assertions.assertTrue(hasLngBoiloff);
				Assertions.assertFalse(hasBunkerUse);
				// Only the last journey should be using Suez
				if (foundMaintenance) {
					Assertions.assertNotNull(journey.getCanalDateTime());
					Assertions.assertEquals(RouteOption.SUEZ, journey.getRouteOption());
				} else {
					Assertions.assertNull(journey.getCanalDateTime());
					Assertions.assertEquals(RouteOption.DIRECT, journey.getRouteOption());
				}
			}
		}
		Assertions.assertTrue(foundMaintenance);
	}
}
