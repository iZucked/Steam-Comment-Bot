/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * These test the AssignableElementCommandProvider to ensure the SequenceHint is reset to 0 when data that could influence it's value has changed.
 * 
 * @author Simon Goodall
 *
 */
@ExtendWith(ShiroRunner.class)
public class SequenceHintCommandProviderTest extends AbstractMicroTestCase {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSpotIndex() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, 1)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeAssignmentType() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselAvailability)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotPort() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper
				.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__PORT, portFinder.findPortById(InternalDataConstants.PORT_RAS_LAFFAN))));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotDuration() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__DURATION, 10)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotWindowSize() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE, 10)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotWindowSizeUnits() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS, TimePeriod.DAYS)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotWindowStartTime() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, 10)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotWindowStart() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START, LocalDate.of(2015, 12, 10))));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeSlotWindowCounterParty() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withWindowCounterParty(false) //
				.build() //

				.build();
		cargo1.setSequenceHint(10);
		Slot<?> slot = cargo1.getSlots().get(1);

		Assertions.assertNotEquals(0, cargo1.getSequenceHint());
		Assertions.assertFalse(slot.isWindowCounterParty());

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_COUNTER_PARTY, true)));

		Assertions.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeVesselEventDuration() throws Exception {
		final int newDuration = 10;
		runGenericVesselEventChangeTest(newDuration, VesselEvent::getDurationInDays, CargoPackage.Literals.VESSEL_EVENT__DURATION_IN_DAYS);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeVesselEventPort() throws Exception {
		final Port newPort = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
		runGenericVesselEventChangeTest(newPort, VesselEvent::getPort, CargoPackage.Literals.VESSEL_EVENT__PORT);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeVesselEventStartAfter() throws Exception {
		final LocalDateTime newStartAfter = LocalDateTime.of(2022, 1, 15, 0, 0);
		runGenericVesselEventChangeTest(newStartAfter, VesselEvent::getStartAfter, CargoPackage.Literals.VESSEL_EVENT__START_AFTER);
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void test_ChangeVesselEventStartBy() throws Exception {
		final LocalDateTime newStartBy = LocalDateTime.of(2022, 1, 15, 0, 0);
		runGenericVesselEventChangeTest(newStartBy, VesselEvent::getStartBy, CargoPackage.Literals.VESSEL_EVENT__START_BY);
	}

	private void runGenericVesselEventChangeTest(@NonNull final Object newValue, @NonNull final Function<VesselEvent, Object> valueExtractor, @NonNull final EStructuralFeature emfFeature) {
		final Port vePort = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final LocalDateTime veStartAfter = LocalDateTime.of(2022, 1, 1, 0, 0);
		final LocalDateTime veStartBy = LocalDateTime.of(2022, 2, 1, 0, 0);
		final int veDuration = 5;

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final MaintenanceEvent maintenanceEvent = cargoModelBuilder.makeMaintenanceEvent("M1", veStartAfter, veStartBy, vePort) //
				.withDurationInDays(veDuration) //
				.build();

		final CharterOutEvent charterOutEvent = cargoModelBuilder.makeCharterOutEvent("CO1", veStartAfter, veStartBy, vePort) //
				.withDurationInDays(veDuration) //
				.build();

		final DryDockEvent dryDockEvent = cargoModelBuilder.makeDryDockEvent("DD1", veStartAfter, veStartBy, vePort) //
				.withDurationInDays(veDuration) //
				.build();

		maintenanceEvent.setSequenceHint(10);
		charterOutEvent.setSequenceHint(11);
		dryDockEvent.setSequenceHint(12);

		Assertions.assertNotEquals(0, maintenanceEvent.getSequenceHint());
		Assertions.assertNotEquals(0, charterOutEvent.getSequenceHint());
		Assertions.assertNotEquals(0, dryDockEvent.getSequenceHint());

		Assertions.assertNotEquals(newValue, valueExtractor.apply(maintenanceEvent));
		Assertions.assertNotEquals(newValue, valueExtractor.apply(charterOutEvent));
		Assertions.assertNotEquals(newValue, valueExtractor.apply(dryDockEvent));

		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, maintenanceEvent, emfFeature, newValue)));
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, charterOutEvent, emfFeature, newValue)));
		RunnerHelper.syncExec(() -> domain.getCommandStack().execute(SetCommand.create(domain, dryDockEvent, emfFeature, newValue)));

		Assertions.assertEquals(0, maintenanceEvent.getSequenceHint());
		Assertions.assertEquals(0, charterOutEvent.getSequenceHint());
		Assertions.assertEquals(0, dryDockEvent.getSequenceHint());
	}

	private EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {

		// Create the editing domain with a special command stack.
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource r = new ResourceImpl();
		r.getContents().add(scenarioModel);
		resourceSet.getResources().add(r);

		return ScenarioStorageUtil.initEditingDomain(null, resourceSet, scenarioModel).getFirst();
	}

}