/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.util.ActualsAssignableDateProvider;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;

/**
 * Test case for the sorting of elements on a Sequence in the EMF data model.
 * 
 * @author Simon Goodall
 *
 */
public class CollectedAssignmentTest {

	@Test
	public void testSimpleCargoOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 03, 01), 24, port, LocalDate.of(2016, 04, 01), 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testSimpleActualsOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 03, 01), 24, port, LocalDate.of(2016, 04, 01), 24);

		final ActualsModel actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
		{

			final CargoActuals cargo2Actuals = ActualsFactory.eINSTANCE.createCargoActuals();
			final LoadActuals load2Actuals = ActualsFactory.eINSTANCE.createLoadActuals();
			final DischargeActuals discharge2Actuals = ActualsFactory.eINSTANCE.createDischargeActuals();

			load2Actuals.setSlot(cargo2.getSlots().get(0));
			discharge2Actuals.setSlot(cargo2.getSlots().get(1));

			cargo2Actuals.setCargo(cargo2);
			cargo2Actuals.getActuals().add(load2Actuals);
			cargo2Actuals.getActuals().add(discharge2Actuals);

			actualsModel.getCargoActuals().add(cargo2Actuals);

			load2Actuals.setOperationsStart(LocalDateTime.of(2015, 12, 1, 0, 0, 0, 0));
			load2Actuals.setOperationsEnd(LocalDateTime.of(2015, 12, 1, 0, 0, 0, 0));

			discharge2Actuals.setOperationsStart(LocalDateTime.of(2015, 12, 10, 0, 0, 0, 0));
			discharge2Actuals.setOperationsEnd(LocalDateTime.of(2015, 12, 10, 0, 0, 0, 0));
		}
		final ActualsAssignableDateProvider dateProvider = new ActualsAssignableDateProvider(actualsModel);
		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		// Actualised date moves cargo 2 before cargo 1
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo1);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, dateProvider);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testSequenceHintCargoOrdering() {

		final @NonNull VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		cargo1.setSequenceHint(1);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		cargo2.setSequenceHint(2);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		for (@NonNull
		final List<@NonNull AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testSimpleEventOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final VesselEvent event1 = createEvent("event1", port, LocalDateTime.of(2016, 01, 01, 0, 0), 24, 1);
		final VesselEvent event2 = createEvent("event2", port, LocalDateTime.of(2016, 03, 01, 0, 0), 24, 1);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(event1);
		expectedSortOrder.add(event2);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(event1);
		assignments.add(event2);
		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testSimpleCargoAndEventOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		final VesselEvent event1 = createEvent("event2", port, LocalDateTime.of(2016, 03, 01, 0, 0), 24, 1);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(event1);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(event1);
		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoAndEventOrdering_1() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 24, port, LocalDate.of(2016, 02, 01), 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 03, 01), 24, port, LocalDate.of(2016, 04, 01), 24);
		final VesselEvent event1 = createEvent("event1", port, LocalDateTime.of(2016, 01, 15, 0, 0), 30 * 24, 1);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(event1);
		expectedSortOrder.add(cargo2);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(event1);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoAndEventOrdering_2() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 01, 01), 2 * 24, port, LocalDate.of(2016, 02, 01), 24 - 1);
		// start event within load window - this should be enough to schedule before cargo
		final VesselEvent event1 = createEvent("event1", port, LocalDateTime.of(2016, 01, 2, 0, 0), 27 * 24 - 1, 1);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(event1);
		expectedSortOrder.add(cargo1);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(event1);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoAndEventOrdering_3() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 1, 10), 0, port, LocalDate.of(2016, 02, 01), 0);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 3, 01), 0, port, LocalDate.of(2016, 03, 2), 0);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2016, 4, 01), 0, port, LocalDate.of(2016, 05, 01), 0);
		// Large event duration forces after cargoes
		final VesselEvent event1 = createEvent("event1", port, LocalDateTime.of(2016, 1, 1, 0, 0), 6 * 31 * 24, 40);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(event1);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(event1);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);
			if (!expectedSortOrder.equals(collectedAssignment.getAssignedObjects())) {

				new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			}
			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoAndEventOrdering_EventDuration() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 2, 1), 0, port, LocalDate.of(2016, 3, 1), 0);
		// Could schedule prior to cargo based on just window, however event duration should flip choice
		final VesselEvent event1 = createEvent("event1", port, LocalDateTime.of(2016, 1, 30, 0, 0), 60 * 24, 10 /* days */);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(event1);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(event1);
		assignments.add(cargo1);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 02, 01), 28 * 24, port, LocalDate.of(2016, 03, 01), 31 * 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 04, 01), 30 * 24, port, LocalDate.of(2016, 04, 01), 30 * 24);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2016, 03, 01), 92 * 24, port, LocalDate.of(2016, 06, 01), 91 * 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoOrdering2() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 6, 01), 92 * 24, port, LocalDate.of(2016, 7, 01), 31 * 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 7, 20), 30 * 24, port, LocalDate.of(2016, 7, 01), 92 * 24);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2016, 6, 01), 92 * 24, port, LocalDate.of(2016, 9, 01), 30 * 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo2);
		assignments.add(cargo1);
		assignments.add(cargo3);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testMoreComplicatedOverlappingCargoOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 02, 01), 28 * 24, port, LocalDate.of(2016, 03, 01), 31 * 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 04, 01), 30 * 24, port, LocalDate.of(2016, 04, 01), 91 * 24);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2016, 03, 01), 92 * 24, port, LocalDate.of(2016, 06, 01), 91 * 24);
		final Cargo cargo4 = createCargo("cargo4", port, LocalDate.of(2016, 06, 01), 30 * 24, port, LocalDate.of(2016, 7, 01), 92 * 24);
		final Cargo cargo5 = createCargo("cargo5", port, LocalDate.of(2016, 06, 01), 92 * 24, port, LocalDate.of(2016, 9, 01), 30 * 24);
		final Cargo cargo6 = createCargo("cargo6", port, LocalDate.of(2016, 9, 01), 30 * 24, port, LocalDate.of(2016, 10, 01), 92 * 24);
		final Cargo cargo7 = createCargo("cargo7", port, LocalDate.of(2016, 10, 01), 31 * 24, port, LocalDate.of(2016, 10, 01), 92 * 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(cargo4);
		expectedSortOrder.add(cargo5);
		expectedSortOrder.add(cargo6);
		expectedSortOrder.add(cargo7);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(cargo4);
		assignments.add(cargo5);
		assignments.add(cargo6);
		assignments.add(cargo7);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);
			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testMoreComplicatedOverlappingCargoOrdering2() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port loadport = PortFactory.eINSTANCE.createPort();
		final Port dischargeport1 = PortFactory.eINSTANCE.createPort();
		final Port dischargeport2 = PortFactory.eINSTANCE.createPort();
		loadport.setTimeZone("America/Chicago");
		dischargeport1.setTimeZone("Asia/Tokyo");
		dischargeport2.setTimeZone("Europe/Paris");
		final Cargo cargo1 = createCargo("cargo1", loadport, LocalDate.of(2016, 6, 1), 2200, dischargeport1, LocalDate.of(2016, 7, 1), 736);
		final Cargo cargo2 = createCargo("cargo2", loadport, LocalDate.of(2016, 7, 20), 736, dischargeport2, LocalDate.of(2016, 7, 01), 2200);
		final Cargo cargo3 = createCargo("cargo3", loadport, LocalDate.of(2016, 6, 1), 2200, dischargeport1, LocalDate.of(2016, 9, 1), 712);
		final Cargo cargo4 = createCargo("cargo4", loadport, LocalDate.of(2016, 10, 01), 736, dischargeport1, LocalDate.of(2016, 11, 01), 712);
		final Cargo cargo5 = createCargo("cargo5", loadport, LocalDate.of(2016, 12, 1), 90 * 24, dischargeport1, LocalDate.of(2017, 2, 01), 28 * 24);
		final Cargo cargo6 = createCargo("cargo6", loadport, LocalDate.of(2017, 2, 1), 28 * 24, dischargeport1, LocalDate.of(2017, 1, 1), 90 * 24);
		final Cargo cargo7 = createCargo("cargo7", loadport, LocalDate.of(2017, 3, 1), 92 * 24, dischargeport1, LocalDate.of(2017, 4, 01), 30 * 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(cargo4);
		expectedSortOrder.add(cargo5);
		expectedSortOrder.add(cargo6);
		expectedSortOrder.add(cargo7);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(cargo4);
		assignments.add(cargo5);
		assignments.add(cargo6);
		assignments.add(cargo7);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);
			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testAmbiguousCargoOrdering() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2016, 02, 01), 28 * 24, port, LocalDate.of(2016, 03, 01), 31 * 24);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2016, 04, 01), 30 * 24, port, LocalDate.of(2016, 04, 01), 91 * 24);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2016, 03, 01), 92 * 24, port, LocalDate.of(2016, 06, 01), 91 * 24);
		final Cargo cargo4 = createCargo("cargo4", port, LocalDate.of(2016, 06, 01), 30 * 24, port, LocalDate.of(2016, 7, 01), 92 * 24);
		final Cargo cargo5 = createCargo("cargo5", port, LocalDate.of(2016, 06, 01), 92 * 24, port, LocalDate.of(2016, 9, 01), 30 * 24);
		final Cargo cargo6 = createCargo("cargo6", port, LocalDate.of(2016, 9, 01), 30 * 24, port, LocalDate.of(2016, 10, 01), 92 * 24);
		final Cargo cargo7 = createCargo("cargo7", port, LocalDate.of(2016, 10, 01), 31 * 24, port, LocalDate.of(2016, 10, 01), 92 * 24);
		final Cargo cargo8 = createCargo("cargo8", port, LocalDate.of(2016, 9, 01), 91 * 24, port, LocalDate.of(2016, 10, 01), 92 * 24);
		final Cargo cargo9 = createCargo("cargo9", port, LocalDate.of(2016, 12, 01), 90 * 24, port, LocalDate.of(2017, 1, 01), 90 * 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(cargo4);
		expectedSortOrder.add(cargo5);
		expectedSortOrder.add(cargo6);
		expectedSortOrder.add(cargo7);
		expectedSortOrder.add(cargo8);
		expectedSortOrder.add(cargo9);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(cargo4);
		assignments.add(cargo5);
		assignments.add(cargo6);
		assignments.add(cargo7);
		assignments.add(cargo8);
		assignments.add(cargo9);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);
			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	private void dumpPermutation(final List<AssignableElement> expectedSortOrder, final List<AssignableElement> permutation, final CollectedAssignment collectedAssignment) {
		if (!expectedSortOrder.equals(collectedAssignment.getAssignedObjects())) {
			{
				final List<String> values = new LinkedList<>();
				for (final AssignableElement e : permutation) {
					if (e instanceof Cargo) {
						values.add(((Cargo) e).getLoadName());
					} else if (e instanceof VesselEvent) {
						values.add(((VesselEvent) e).getName());
					} else {
						Assert.fail("unknown element");
					}
				}
				System.out.println("Input:     " + Joiner.on(", ").join(values));
			}
			{
				final List<String> values = new LinkedList<>();
				for (final AssignableElement e : collectedAssignment.getAssignedObjects()) {
					if (e instanceof Cargo) {
						values.add(((Cargo) e).getLoadName());
					} else if (e instanceof VesselEvent) {
						values.add(((VesselEvent) e).getName());
					} else {
						Assert.fail("unknown element");
					}
				}
				System.out.println("Sorted As: " + Joiner.on(", ").join(values));
			}
			{
				final List<String> values = new LinkedList<>();
				for (final AssignableElement e : expectedSortOrder) {
					if (e instanceof Cargo) {
						values.add(((Cargo) e).getLoadName());
					} else if (e instanceof VesselEvent) {
						values.add(((VesselEvent) e).getName());
					} else {
						Assert.fail("unknown element");
					}
				}
				System.out.println("Expected:  " + Joiner.on(", ").join(values));
			}
		}
	}

	private Cargo createCargo(final String id, @NonNull final Port loadPort, @NonNull final LocalDate loadWindowStart, final int loadWindowSize, @NonNull final Port dischargePort,
			@NonNull final LocalDate dischargeWindowStart, final int dischargeWindowSize) {

		final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
		load.setName(id);
		load.setPort(loadPort);
		load.setWindowStart(loadWindowStart);
		load.setWindowSize(loadWindowSize);

		load.setDuration(24);
		final DischargeSlot discharge = CargoFactory.eINSTANCE.createDischargeSlot();
		discharge.setName(id);
		discharge.setPort(dischargePort);
		discharge.setWindowStart(dischargeWindowStart);
		discharge.setWindowSize(dischargeWindowSize);
		discharge.setDuration(24);

		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.getSlots().add(load);
		cargo.getSlots().add(discharge);

		return cargo;
	}

	private VesselEvent createEvent(final String id, @NonNull final Port loadPort, @NonNull final LocalDateTime loadWindowStart, final int loadWindowSize, final int durationInDays) {

		final DryDockEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
		event.setName(id);
		event.setPort(loadPort);
		event.setStartAfter(loadWindowStart);
		event.setStartBy(loadWindowStart.plusHours(loadWindowSize));
		event.setDurationInDays(durationInDays);

		return event;
	}

	@Test
	public void testOverlappingCargoOrdering_Lateness() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2014, 04, 04), 24, port, LocalDate.of(2014, 05, 02), 24);
		// Spot discharge (note 30th rather than 1st)
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2014, 05, 17), 24, port, LocalDate.of(2014, 04, 30), 744);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2014, 05, 13), 24, port, LocalDate.of(2014, 06, 10), 24);
		final Cargo cargo4 = createCargo("cargo4", port, LocalDate.of(2014, 07, 1), 24, port, LocalDate.of(2014, 07, 11), 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo4);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(cargo4);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoOrdering_Lateness2() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo1 = createCargo("cargo1", port, LocalDate.of(2014, 9, 8), 24, port, LocalDate.of(2014, 9, 1), 720);
		final Cargo cargo2 = createCargo("cargo2", port, LocalDate.of(2014, 9, 15), 24, port, LocalDate.of(2014, 9, 22), 24);
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2014, 9, 28) /* +6 */, 24, port, LocalDate.of(2014, 9, 1), 720);
		final Cargo cargo4 = createCargo("cargo4", port, LocalDate.of(2014, 9, 27), 24, port, LocalDate.of(2014, 9, 1), 720);
		final Cargo cargo5 = createCargo("cargo5", port, LocalDate.of(2014, 10, 11), 24, port, LocalDate.of(2014, 10, 21), 24);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo1);
		expectedSortOrder.add(cargo2);
		expectedSortOrder.add(cargo4);
		expectedSortOrder.add(cargo3);
		expectedSortOrder.add(cargo5);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo1);
		assignments.add(cargo2);
		assignments.add(cargo3);
		assignments.add(cargo4);
		assignments.add(cargo5);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}

	@Test
	public void testOverlappingCargoOrdering_Lateness3() {

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setTimeZone("Etc/UTC");
		final Cargo cargo3 = createCargo("cargo3", port, LocalDate.of(2014, 9, 28) /* +6 */, 24, port, LocalDate.of(2014, 9, 1), 720);
		final Cargo cargo4 = createCargo("cargo4", port, LocalDate.of(2014, 9, 27), 24, port, LocalDate.of(2014, 9, 1), 720);

		final List<AssignableElement> expectedSortOrder = new LinkedList<>();
		expectedSortOrder.add(cargo4);
		expectedSortOrder.add(cargo3);

		final List<AssignableElement> assignments = new LinkedList<>();
		assignments.add(cargo3);
		assignments.add(cargo4);

		for (final List<AssignableElement> permutation : Collections2.permutations(assignments)) {
			final CollectedAssignment collectedAssignment = new CollectedAssignment(new ArrayList<>(permutation), vesselAvailability, null, null);
			Assert.assertNotNull(collectedAssignment);
			Assert.assertSame(vesselAvailability, collectedAssignment.getVesselAvailability());
			Assert.assertNull(collectedAssignment.getCharterInMarket());
			dumpPermutation(expectedSortOrder, permutation, collectedAssignment);

			Assert.assertEquals(expectedSortOrder, collectedAssignment.getAssignedObjects());
		}
	}
}
