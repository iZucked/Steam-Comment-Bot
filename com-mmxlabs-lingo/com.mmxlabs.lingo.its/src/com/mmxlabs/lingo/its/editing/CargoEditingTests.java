/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.editing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingHelper;
import com.mmxlabs.models.lng.cargo.util.CargoUtils;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;

public class CargoEditingTests extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void assignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN))
				.build();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();

		helper.assignVesselEventToVesselAvailability("Assign event", vesselEvent, vesselAvailability);

		Assertions.assertSame(vesselAvailability, vesselEvent.getVesselAssignmentType());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignVesselEvent() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vessel, entity).build();
		final DryDockEvent vesselEvent = cargoModelBuilder.makeDryDockEvent("DryDock", LocalDateTime.of(2016, 7, 22, 0, 0), LocalDateTime.of(2016, 7, 22, 0, 0), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN))//
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

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 7, 22), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 20.0, null).build();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		helper.assignNominatedVessel("Assign slot", load, vessel);

		Assertions.assertSame(vessel, load.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignDESPurchase() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final LoadSlot load = cargoModelBuilder.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2016, 7, 22), portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, entity, "5", 20.0, vessel).build();

		helper.assignNominatedVessel("Unassign slot", load, null);

		Assertions.assertNull(load.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void assignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("D1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2016, 7, 22), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null).build();

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		helper.assignNominatedVessel("Assign slot", discharge, vessel);

		Assertions.assertSame(vessel, discharge.getNominatedVessel());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void unassignFOBSale() {

		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();

		final CargoEditingHelper helper = new CargoEditingHelper(editingDomain, lngScenarioModel);

		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);

		final DischargeSlot discharge = cargoModelBuilder.makeFOBSale("L1", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2016, 7, 22), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", vessel).build();

		helper.assignNominatedVessel("Unassign slot", discharge, null);

		Assertions.assertNull(discharge.getNominatedVessel());
	}
	
	/**
	 * Tests CargoUtils.getOpenSlotsIterable() to check that it iterates correctly over
	 * a list of all open slots, and no other slots, in the cargo model. 
	 */
	private void testGetOpenSlotsIterable(CargoModel cargoModel) {
				
		Set<Slot<?>> allSlots = new HashSet<Slot<?>>();
		Set<Slot<?>> openSlots = new HashSet<Slot<?>>();
		Set<Slot<?>> cargoSlots = new HashSet<Slot<?>>();
		
		allSlots.addAll(cargoModel.getLoadSlots());
		allSlots.addAll(cargoModel.getDischargeSlots());
		
		// build a set of all slots that have no cargo associated with them
		for (Slot<?> slot: allSlots) {
			Assertions.assertNotNull(slot);
			if (slot.getCargo() == null) {
				openSlots.add(slot);
			}
		}
		
		// build a set of all slots that are attached to a cargo in the list of cargoes
		for (Cargo cargo: cargoModel.getCargoes()) {
			for (Slot<?> slot: cargo.getSlots()) {
				Assertions.assertNotNull(slot);
				cargoSlots.add(slot);
			}
		}
		
		// sanity check that there is no overlap between the sets		
		HashSet<Slot<?>> intersection = new HashSet<Slot<?>>(openSlots);
		intersection.retainAll(cargoSlots);
		Assertions.assertTrue(intersection.isEmpty(), "Cargo model inconsistency: slots with null cargo in cargo.");
		
		// sanity check that the lists are exhaustive
		HashSet<Slot<?>> union = new HashSet<Slot<?>>(openSlots);
		union.addAll(cargoSlots);
		Assertions.assertTrue(union.equals(allSlots), "Cargo model inconsistency: load & discharge slot lists do not match up with open and closed slot lists.");
				
		// build a set of all slots returned by getOpenSlotsIterable()
		HashSet<Slot<?>> testSet = new HashSet<Slot<?>>();		
		for (Slot<?> slot: CargoUtils.getOpenSlotsIterable(cargoModel)) {
			testSet.add(slot);
		}
		
		// check that it is the same as the set of slots with no cargo attached to them
		Assertions.assertTrue(testSet.equals(openSlots));				
	}
	
	@Test
	/** Tests CargoUtils.getOpenSlotsIterator() against a small toy cargo model.
	 * 
	 */
	public void testGetOpenSlotsIterator() {
		// test structure: create a scenario with load slots, discharge slots and cargoes
		// ensure that there are some open slots (i.e. not attached to cargoes)
		// and some slots that are attached to cargoes
		// 
		// then call getOpenSlotsIterable(), iterate over it, and ensure that it returns the open slots 

		// Setup code copy-pasted from NominalMarketTest
		
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		vessel.setSafetyHeel(500);

		final CharterInMarket charterInMarket_1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 0);
		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 5), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //
				.withVesselAssignment(charterInMarket_1, -1, 1) // -1 is nominal
				.withAssignmentFlags(true, false) //
				.build();
		
		// end copy-paste
		
		// add some open slots to the model
		
		cargoModelBuilder.createFOBPurchase("L2", LocalDate.of(2015, 12, 3), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null);
		cargoModelBuilder.createFOBPurchase("L3", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null);
		cargoModelBuilder.createDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7");

		// run a test on the model

		testGetOpenSlotsIterable(lngScenarioModel.getCargoModel());
		
	}
}
