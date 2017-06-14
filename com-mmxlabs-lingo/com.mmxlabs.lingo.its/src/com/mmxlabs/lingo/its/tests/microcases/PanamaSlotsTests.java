package com.mmxlabs.lingo.its.tests.microcases;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

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
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.EntryPointImpl;
import com.mmxlabs.models.lng.port.impl.PortModelImpl;
import com.mmxlabs.models.lng.port.provider.EntryPointItemProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.SequenceHelper;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;

@RunWith(value = ShiroRunner.class)
public class PanamaSlotsTests extends AbstractMicroTestCase {

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
		
		EntryPoint colon = panama.getEntryA();
		EntryPoint balboa = panama.getEntryB();

		CanalBookingSlot d1 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d1.setRoute(panama);
		d1.setEntryPoint(colon);
		d1.setSlotDate(LocalDate.of(2017, Month.JUNE, 7));
		model.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d1);
		
		CanalBookingSlot d2 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d2.setRoute(panama);
		d2.setEntryPoint(colon);
		d2.setSlotDate(LocalDate.of(2017, Month.JUNE, 20));
		model.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d2);
		
		CanalBookingSlot d3 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d3.setRoute(panama);
		d3.setEntryPoint(balboa);
		d3.setSlotDate(LocalDate.of(2017, Month.JUNE, 6));
		model.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d3);
		
		CanalBookingSlot d4 = CargoFactory.eINSTANCE.createCanalBookingSlot();
		d4.setRoute(panama);
		d4.setEntryPoint(balboa);
		d4.setSlotDate(LocalDate.of(2017, Month.JULY, 20));
		model.getCargoModel().getCanalBookings().getCanalBookingSlots().add(d4);
		
		model.getCargoModel().getCanalBookings().setStrictBoundaryOffsetDays(30);
		model.getCargoModel().getCanalBookings().setRelaxedBoundaryOffsetDays(90);
		model.getCargoModel().getCanalBookings().setFlexibleSlotAmount(0);
		
		
		return model;
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotAvailableTest() {

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

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

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
		final Port port2 = portFinder.findPort("Chita");

		// map into same timezone to make expectations easier
		port1.setTimeZone("UTC");
		port2.setTimeZone("UTC");

		// Set distance and speed to exact multiple -- quickest travel time is
		// 100 hours
		scenarioModelBuilder.getPortModelBuilder().setPortToPortDistance(port1, port2, 9158, 9158, 4196, true);
		vesselClass.setMaxSpeed(16.0);

		final LocalDateTime loadDate = LocalDateTime.of(2017, Month.JUNE, 1, 0, 0, 0);
		final LocalDateTime dischargeDate = loadDate.plusDays(26);

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();
		
		final Cargo cargo2 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability2, 1) //
				.build();

		evaluateWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			final PanamaSlotsConstraintChecker checker = MicroTestUtils.getChecker(scenarioToOptimiserBridge, PanamaSlotsConstraintChecker.class);
			
			// run with empty sequnces to because of whitelisting in constraint checker
			checker.checkConstraints(SequenceHelper.createSequences(scenarioToOptimiserBridge), null);

			ISequencesManipulator sequencesManipulator = scenarioToOptimiserBridge.getInjector().createChildInjector(new SequencesManipulatorModule()).getInstance(ISequencesManipulator.class);
			@NonNull
			IModifiableSequences manipulatedSequences = sequencesManipulator.createManipulatedSequences(SequenceHelper.createSequences(scenarioToOptimiserBridge, vesselAvailability, cargo));
			SequenceHelper.addSequence(manipulatedSequences, scenarioToOptimiserBridge, vesselAvailability2, cargo2);
			
			assertFalse(checker.checkConstraints(manipulatedSequences, null));
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void panamaSlotUnavailableTest() {
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

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L", loadDate.toLocalDate(), port1, null, entity, "5") //
				.withWindowStartTime(0) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.makeDESSale("D", dischargeDate.toLocalDate(), port2, null, entity, "7") //
				.withWindowStartTime(dischargeDate.toLocalTime().getHour()) //
				.withVisitDuration(24) //
				.withWindowSize(0, TimePeriod.HOURS) //
				.build() //
				//
				.withVesselAssignment(vesselAvailability, 1) //
				.build();

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
}
