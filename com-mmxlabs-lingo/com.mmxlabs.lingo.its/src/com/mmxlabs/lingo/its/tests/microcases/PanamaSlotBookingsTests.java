package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.inject.Injector;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.NonSchedulingScheduler;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;

@RunWith(value = ShiroRunner.class)
public class PanamaSlotBookingsTests extends AbstractMicroTestCase {

	@Override
	public LNGScenarioModel importReferenceData() throws MalformedURLException {

		@NonNull
		LNGScenarioModel model = importReferenceData("/referencedata/reference-data-2/");
		
		CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		model.getCargoModel().setCanalBookings(canalBookings);
		
		Optional<Route> potentialPanama = model.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		model.getReferenceModel().getPortModel().getPorts().stream().filter(p -> {
			return "Colon".equals(p.getName()) || "Balboa".equals(p.getName());
		}).forEach(p -> {
			EntryPoint ep = PortFactory.eINSTANCE.createEntryPoint();
			ep.setPort(p);
			if (p.getName().equals("Colon")) {
				ep.setName("EAST");
				panama.setEntryA(ep);
			}else {
				ep.setName("WEST");
				panama.setEntryB(ep);
			}
		});
		
		model.getCargoModel().getCanalBookings().setStrictBoundaryOffsetDays(30);
		model.getCargoModel().getCanalBookings().setRelaxedBoundaryOffsetDays(90);
		model.getCargoModel().getCanalBookings().setFlexibleBookingAmount(0);
		
		return model;
	}
	
	private Cargo createFobDesCargo(final VesselAvailability vesselAvailability, final Port loadPort, final Port dischargePort, final LocalDateTime loadDate, final LocalDateTime dischargeDate) {
		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), loadPort, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), dischargePort, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();
		return cargo;
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotAvailableTest() {
		
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final PanamaSlotsConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, PanamaSlotsConstraintChecker.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge), null);
			assertTrue(checker.checkConstraints(manipulatedSequences, null));
		});
	}

	
	@Test
	@Category({ MicroTest.class })
	public void panamaSlotNoDubleAssignmentTest(){
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		EntryPoint colon = panama.getEntryA();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		
		final Vessel vessel2 = fleetModelBuilder.createVessel("vessel2", vesselClass);
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		
		final Cargo cargo2 = createFobDesCargo(vesselAvailability2, port1, port2, loadDate, dischargeDate);
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = scenarioToOptimiserBridge.getDataTransformer().getInjector();
			
			NonSchedulingScheduler scheduler = injector.getInstance(NonSchedulingScheduler.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability2, cargo2);
			
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNotNull(bookings[0][1]);
			assertNull(bookings[1][1]);
			
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotUnavailableTest() {
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.AUGUST, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(10);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final PanamaSlotsConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, PanamaSlotsConstraintChecker.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge), null);
			assertFalse(checker.checkConstraints(manipulatedSequences, null));
		});
	}
	
	
	@Test
	@Category({ MicroTest.class })
	public void bookingButNoVoyageTest(){
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Barcelona");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = scenarioToOptimiserBridge.getDataTransformer().getInjector();
			
			NonSchedulingScheduler scheduler = injector.getInstance(NonSchedulingScheduler.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNull(bookings[0][1]);
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void nonMatchingBookingTest(){
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();
		
		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 10));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = scenarioToOptimiserBridge.getDataTransformer().getInjector();
			
			NonSchedulingScheduler scheduler = injector.getInstance(NonSchedulingScheduler.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNull(bookings[0][1]);
		});
		
		
	}
	
	@Test
	@Category({ MicroTest.class })
	public void bookingAssignedToSlotTest(){
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		
		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		
		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		d1.setSlot(cargo.getSortedSlots().get(0));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			scenarioToOptimiserBridge.getDataTransformer();
			final Injector injector = scenarioToOptimiserBridge.getDataTransformer().getInjector();
			
			NonSchedulingScheduler scheduler = injector.getInstance(NonSchedulingScheduler.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNotNull(bookings[0][1]);
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void asssignedBookingNotUsedTest(){
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		
		EntryPoint colon = panama.getEntryA();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();
		
		final Vessel vessel2 = fleetModelBuilder.createVessel("vessel2", vesselClass);
		final VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel2, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Quintero");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(13);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);
		
		
		final Cargo cargo2 = createFobDesCargo(vesselAvailability2, port1, port2, loadDate, dischargeDate);
		
		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		d1.setSlot(cargo2.getSortedSlots().get(0));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			
			NonSchedulingScheduler scheduler = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(NonSchedulingScheduler.class);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability2, cargo2);
			
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNull(bookings[0][1]);
			assertNotNull(bookings[1][1]);
			
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void journeyCanBeMadeDirectTest() {
		
		Optional<Route> potentialPanama = lngScenarioModel.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		Route panama = potentialPanama.get();
		EntryPoint colon = panama.getEntryA();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setBookingDate(LocalDate.of(2017, Month.JUNE, 7));
		lngScenarioModel.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-145");
		final Vessel vessel = fleetModelBuilder.createVessel("vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.build();

		@NonNull
		final Port port1 = portFinder.findPort("Sabine Pass");

		@NonNull
		final Port port2 = portFinder.findPort("Chita");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(25);

		final Cargo cargo = createFobDesCargo(vesselAvailability, port1, port2, loadDate, dischargeDate);

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			
			NonSchedulingScheduler scheduler = scenarioToOptimiserBridge.getDataTransformer().getInjector().getInstance(NonSchedulingScheduler.class);
			scheduler.schedule(manipulatedSequences);
			IRouteOptionBooking[][] bookings = scheduler.slotsAssigned();
			
			assertNull(bookings[0][1]);
		});
	}
	
}
