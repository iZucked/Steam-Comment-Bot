package com.mmxlabs.lingo.its.editing;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoEditingHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

public class CargoEditingTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void assignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPort("Point Fortin"))
				.build();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();

		helper.assignVesselEventToVesselAvailability("Assign event", vesselEvent, vesselAvailability);

		Assert.assertSame(vesselAvailability, vesselEvent.getVesselAssignmentType());
	}

	@Test
	@Category(MicroTest.class)
	public void unassignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();
		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPort("Point Fortin"))//
				.withVesselAssignment(vesselAvailability, 0)//
				.build();

		helper.unassignVesselEventAssignment("Unassign event", vesselEvent);

		Assert.assertNull(vesselEvent.getVesselAssignmentType());
	}

	@Test
	@Category(MicroTest.class)
	public void assignDESPurchase() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null).build();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		helper.assignNominatedVessel("Assign slot", load, vessel);

		Assert.assertSame(vessel, load.getNominatedVessel());
	}

	@Test
	@Category(MicroTest.class)
	public void unassignDESPurchase() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Sakai"), null, entity, "5", 20.0, vessel).build();

		helper.assignNominatedVessel("Unassign slot", load, null);

		Assert.assertNull(load.getNominatedVessel());
	}

	@Test
	@Category(MicroTest.class)
	public void assignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Point Fortin"), null, entity, "5", null).build();

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		helper.assignNominatedVessel("Assign slot", discharge, vessel);

		Assert.assertSame(vessel, discharge.getNominatedVessel());
	}

	@Test
	@Category(MicroTest.class)
	public void unassignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		final Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Point Fortin"), null, entity, "5", vessel).build();

		helper.assignNominatedVessel("Unassign slot", discharge, null);

		Assert.assertNull(discharge.getNominatedVessel());
	}
}
