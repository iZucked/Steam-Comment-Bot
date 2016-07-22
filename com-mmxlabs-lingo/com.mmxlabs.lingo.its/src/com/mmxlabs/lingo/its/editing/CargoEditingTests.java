package com.mmxlabs.lingo.its.editing;

import java.time.LocalDateTime;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoEditingHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

public class CargoEditingTests extends AbstractMicroTestCase {

	@Test
	@Category(MicroTest.class)
	public void createNewCargo() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPort("Point Fortin")).build();

		VesselClass vesselClass = fleetModelFinder.findVesselClass("STEAM-138");
		Vessel vessel = fleetModelBuilder.createVessel("Vessel", vesselClass);
		VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();

		helper.assignVesselEventToVesselAvailability("Assign event", vesselEvent, vesselAvailability);

		Assert.assertSame(vesselAvailability, vesselEvent.getVesselAssignmentType());
	}
}
