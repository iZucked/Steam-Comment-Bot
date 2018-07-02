/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.editing;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

public class CargoEditingTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void assignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPort("Point Fortin"))
				.build();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();

		helper.assignVesselEventToVesselAvailability("Assign event", vesselEvent, vesselAvailability);

		Assertions.assertSame(vesselAvailability, vesselEvent.getVesselAssignmentType());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();
		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPort("Point Fortin"))//
				.withVesselAssignment(vesselAvailability, 0)//
				.build();

		helper.unassignVesselEventAssignment("Unassign event", vesselEvent);

		Assertions.assertNull(vesselEvent.getVesselAssignmentType());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void assignDESPurchase() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Sakai"), null, entity, "5", 20.0, null).build();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		helper.assignNominatedVessel("Assign slot", load, vessel);

		Assertions.assertSame(vessel, load.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignDESPurchase() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Sakai"), null, entity, "5", 20.0, vessel).build();

		helper.assignNominatedVessel("Unassign slot", load, null);

		Assertions.assertNull(load.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void assignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("D1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Point Fortin"), null, entity, "5", null).build();

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		helper.assignNominatedVessel("Assign slot", discharge, vessel);

		Assertions.assertSame(vessel, discharge.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel("STEAM-138");

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("L1", false, LocalDate.of(2016, 7, 22), portFinder.findPort("Point Fortin"), null, entity, "5", vessel).build();

		helper.assignNominatedVessel("Unassign slot", discharge, null);

		Assertions.assertNull(discharge.getNominatedVessel());
	}
}
