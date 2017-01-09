/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.commandproviders;

import java.time.LocalDate;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.MMXAdaptersAwareCommandStack;

/**
 * These test the AssignableElementCommandProvider to ensure the SequenceHint is reset to 0 when data that could influence it's value has changed.
 * 
 * @author Simon Goodall
 *
 */
@RunWith(value = ShiroRunner.class)
public class SequenceHintCommandProviderTest extends AbstractMicroTestCase {

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSpotIndex() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		domain.getCommandStack().execute(SetCommand.create(domain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, 1));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeAssignmentType() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		domain.getCommandStack().execute(SetCommand.create(domain, cargo1, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselAvailability));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotPort() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__PORT, portFinder.findPort("Ras Laffan")));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotDuration() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__DURATION, 10));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotWindowSize() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE, 10));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotWindowSizeUnits() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS, TimePeriod.DAYS));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotWindowStartTime() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START_TIME, 10));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	@Test
	@Category({ MicroTest.class })
	public void test_ChangeSlotWindowStart() throws Exception {

		final EditingDomain domain = createEditingDomain(lngScenarioModel);

		final Cargo cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 1), portFinder.findPort("Point Fortin"), null, entity, "5", null) //
				.build() //

				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //

				.build();

		cargo1.setSequenceHint(10);

		Slot slot = cargo1.getSlots().get(0);

		domain.getCommandStack().execute(SetCommand.create(domain, slot, CargoPackage.Literals.SLOT__WINDOW_START, LocalDate.of(2015, 12, 10)));

		Assert.assertEquals(0, cargo1.getSequenceHint());
	}

	private EditingDomain createEditingDomain(final LNGScenarioModel scenarioModel) {
		final Object lockObject = new Object();
		final ScenarioLock lock = ScenarioServiceFactory.eINSTANCE.createScenarioLock();
		final MMXAdaptersAwareCommandStack commandStack = new MMXAdaptersAwareCommandStack(null, lockObject, lock);
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource r = new ResourceImpl();
		r.getContents().add(scenarioModel);
		resourceSet.getResources().add(r);
		final CommandProviderAwareEditingDomain editingDomain = new CommandProviderAwareEditingDomain(adapterFactory, commandStack, scenarioModel, resourceSet);

		commandStack.setEditingDomain(editingDomain);

		return editingDomain;

	}

}