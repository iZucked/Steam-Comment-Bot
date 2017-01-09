/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.provider.CargoItemProviderAdapterFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

@Ignore
public class CargoImportActionTest {

	/**
	 * Simple test - given nothing to start with and nothing to import, we still have nothing!
	 */
	@Test
	public void testEmptyState() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		}
		final List<EObject> imports = new ArrayList<EObject>();
		{

		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertTrue(imports.isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.canExecute();

		// No change is expected
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

	}

	/**
	 * Simple case - given an empty state, if we import one {@link LoadSlot}, then make sure the load slot is used.
	 */
	@Test
	public void testImportLoadSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot loadSlot;
		{

			loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			loadSlot.setName("Load1");
			imports.add(loadSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getLoadSlots().size());
		final LoadSlot importedLoadSlot = cargoModel.getLoadSlots().get(0);
		Assert.assertEquals(loadSlot, importedLoadSlot);
	}

	/**
	 * Test Case - When the import data contains a LoadSlot with the same ID as an existing LoadSlot - replace it.
	 */
	@Test
	public void testImportUpdateLoadSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final LoadSlot originalLoadSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot.setName("Load1");
			originalLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "TestUUID");
			originalLoadSlot.setDuration(50);

			cargoModel.getLoadSlots().add(originalLoadSlot);
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot;
		{

			newLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot.setName("Load1");
			newLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "NewUUID");
			newLoadSlot.setDuration(100);

			imports.add(newLoadSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getLoadSlots().size());
		final LoadSlot importedLoadSlot = cargoModel.getLoadSlots().get(0);
		// Original slot has been updated
		Assert.assertSame(originalLoadSlot, importedLoadSlot);

		Assert.assertEquals(100, importedLoadSlot.getDuration());
		// Ensure original UUID has been retained
		Assert.assertEquals("TestUUID", importedLoadSlot.getUuid());
	}

	/**
	 * Test Case - When the import data contains a LoadSlot with the a different ID to an existing LoadSlot - add it.
	 */
	@Test
	public void testImportAddLoadSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final LoadSlot originalLoadSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot.setName("Load1");
			originalLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "TestUUID");
			originalLoadSlot.setDuration(50);

			cargoModel.getLoadSlots().add(originalLoadSlot);
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot;
		{

			newLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot.setName("Load2");
			newLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "NewUUID");
			newLoadSlot.setDuration(100);

			imports.add(newLoadSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(2, cargoModel.getLoadSlots().size());
		{

			final LoadSlot importedLoadSlot = cargoModel.getLoadSlots().get(0);
			// Original slot has been replaced
			Assert.assertSame(originalLoadSlot, importedLoadSlot);

			Assert.assertEquals(50, importedLoadSlot.getDuration());
			// Ensure original UUID has been retained
			Assert.assertEquals("TestUUID", importedLoadSlot.getUuid());
		}

		{

			final LoadSlot importedLoadSlot = cargoModel.getLoadSlots().get(1);
			// Original slot has been replaced
			Assert.assertSame(newLoadSlot, importedLoadSlot);

			Assert.assertEquals(100, importedLoadSlot.getDuration());
			// Ensure original UUID has been retained
			Assert.assertEquals("NewUUID", importedLoadSlot.getUuid());
		}
	}

	/**
	 * Simple case - given an empty state, if we import one {@link DischargeSlot}, then make sure the discharge slot is used.
	 */
	@Test
	public void testImportDischargeSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final DischargeSlot dischargeSlot;
		{

			dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			dischargeSlot.setName("Discharge1");
			imports.add(dischargeSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getDischargeSlots().size());
		final DischargeSlot importedDischargeSlot = cargoModel.getDischargeSlots().get(0);
		Assert.assertEquals(dischargeSlot, importedDischargeSlot);
	}

	/**
	 * Test Case - When the import data contains a DischargeSlot with the same ID as an existing DischargeSlot - replace it.
	 */
	@Test
	public void testImportUpdateDischargeSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final DischargeSlot originalDischargeSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot.setName("Discharge1");
			originalDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "TestUUID");
			originalDischargeSlot.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot);
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final DischargeSlot newDischargeSlot;
		{

			newDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot.setName("Discharge1");
			newDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "NewUUID");
			newDischargeSlot.setDuration(100);

			imports.add(newDischargeSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getDischargeSlots().size());
		final DischargeSlot importedDischargeSlot = cargoModel.getDischargeSlots().get(0);
		// Original slot has been updated
		Assert.assertSame(originalDischargeSlot, importedDischargeSlot);

		Assert.assertEquals(100, importedDischargeSlot.getDuration());
		// Ensure original UUID has been retained
		Assert.assertEquals("TestUUID", importedDischargeSlot.getUuid());
	}

	/**
	 * Test Case - When the import data contains a DischargeSlot with the a different ID to an existing DischargeSlot - add it.
	 */
	@Test
	public void testImportAddDischargeSlot() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final DischargeSlot originalDischargeSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot.setName("Discharge1");
			originalDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "TestUUID");
			originalDischargeSlot.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot);
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final DischargeSlot newDischargeSlot;
		{

			newDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot.setName("Discharge2");
			newDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "NewUUID");
			newDischargeSlot.setDuration(100);

			imports.add(newDischargeSlot);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());
		{

			final DischargeSlot importedDischargeSlot = cargoModel.getDischargeSlots().get(0);
			// Original slot has been replaced
			Assert.assertSame(originalDischargeSlot, importedDischargeSlot);

			Assert.assertEquals(50, importedDischargeSlot.getDuration());
			// Ensure original UUID has been retained
			Assert.assertEquals("TestUUID", importedDischargeSlot.getUuid());
		}

		{

			final DischargeSlot importedDischargeSlot = cargoModel.getDischargeSlots().get(1);
			// Original slot has been replaced
			Assert.assertSame(newDischargeSlot, importedDischargeSlot);

			Assert.assertEquals(100, importedDischargeSlot.getDuration());
			// Ensure original UUID has been retained
			Assert.assertEquals("NewUUID", importedDischargeSlot.getUuid());
		}
	}

	/**
	 * Simple Case - Import Single Cargo + slots
	 */
	@Test
	public void testImportCargo() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot;
		final DischargeSlot newDischargeSlot;
		final Cargo newCargo;
		{

			newLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot.setName("Load1");

			newDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot.setName("Discharge2");

			newCargo = CargoFactory.eINSTANCE.createCargo();
			newCargo.getSlots().add(newLoadSlot);
			newCargo.getSlots().add(newDischargeSlot);

			imports.add(newDischargeSlot);
			imports.add(newLoadSlot);
			imports.add(newCargo);
		}

		// These should not fail
		Assert.assertTrue(cargoModel.getCargoes().isEmpty());
		Assert.assertTrue(cargoModel.getLoadSlots().isEmpty());
		Assert.assertTrue(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getCargoes().size());
		Assert.assertEquals(1, cargoModel.getLoadSlots().size());
		Assert.assertEquals(1, cargoModel.getDischargeSlots().size());

		Assert.assertSame(newLoadSlot, cargoModel.getLoadSlots().get(0));
		Assert.assertSame(newDischargeSlot, cargoModel.getDischargeSlots().get(0));
		Assert.assertSame(newCargo, cargoModel.getCargoes().get(0));
		Assert.assertSame(newLoadSlot, cargoModel.getCargoes().get(0).getSlots().get(0));
		Assert.assertSame(newDischargeSlot, cargoModel.getCargoes().get(0).getSlots().get(1));
	}

	/**
	 * Update a Cargo. Same object refs but attributes should be updated
	 */
	@Test
	public void testImportUpdateCargo() {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack);

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final Cargo originalCargo;
		final LoadSlot originalLoadSlot;
		final DischargeSlot originalDischargeSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot.setName("Load1");

			originalDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot.setName("Discharge1");

			originalCargo = CargoFactory.eINSTANCE.createCargo();
			originalCargo.getSlots().add(originalLoadSlot);
			originalCargo.getSlots().add(originalDischargeSlot);

			originalLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID1");
			originalDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID2");
			originalCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID3");

			// Fields to update
			originalCargo.setAllowRewiring(false);
			originalLoadSlot.setDuration(50);
			originalDischargeSlot.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot);
			cargoModel.getLoadSlots().add(originalLoadSlot);
			cargoModel.getCargoes().add(originalCargo);
		}
		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot;
		final DischargeSlot newDischargeSlot;
		final Cargo newCargo;
		{

			newLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot.setName("Load1");

			newDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot.setName("Discharge1");

			newCargo = CargoFactory.eINSTANCE.createCargo();
			newCargo.getSlots().add(newLoadSlot);
			newCargo.getSlots().add(newDischargeSlot);

			newLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID1");
			newDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID2");
			newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID3");

			// Fields to update
			newCargo.setAllowRewiring(true);
			newLoadSlot.setDuration(100);
			newDischargeSlot.setDuration(200);

			imports.add(newDischargeSlot);
			imports.add(newLoadSlot);
			imports.add(newCargo);
		}

		// These should not fail
		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getCargoes().size());
		Assert.assertEquals(1, cargoModel.getLoadSlots().size());
		Assert.assertEquals(1, cargoModel.getDischargeSlots().size());

		Assert.assertSame(originalLoadSlot, cargoModel.getLoadSlots().get(0));
		Assert.assertSame(originalDischargeSlot, cargoModel.getDischargeSlots().get(0));
		Cargo importedCargo = cargoModel.getCargoes().get(0);
		// Cargo reference should NOT have changed
		Assert.assertSame(originalCargo, importedCargo);

		// Slot References should be the newly created imported object
		Slot importedLoadSlot = importedCargo.getSlots().get(0);
		Assert.assertSame(originalLoadSlot, importedLoadSlot);
		Slot importedDischargeSlot = importedCargo.getSlots().get(1);
		Assert.assertSame(originalDischargeSlot, importedDischargeSlot);

		// UUID Fields Should NOT change
		Assert.assertEquals("originalUUID1", importedLoadSlot.getUuid());
		Assert.assertEquals("originalUUID2", importedDischargeSlot.getUuid());
		Assert.assertEquals("originalUUID3", importedCargo.getUuid());

		// Check field updates
		Assert.assertEquals(100, importedLoadSlot.getDuration());
		Assert.assertEquals(200, importedDischargeSlot.getDuration());
		Assert.assertTrue(importedCargo.isAllowRewiring());
	}

	/**
	 * Update and re-wire Cargo. New cargo keeps existing LoadSlot, but new discharge slot. This should result in the original cargo disappearing.
	 */
	@Test
	public void testImportRewireCargo_NewCargo_ExistingLoad() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final Cargo originalCargo;
		final LoadSlot originalLoadSlot;
		final DischargeSlot originalDischargeSlot;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot.setName("Load1");

			originalDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot.setName("Discharge1");

			originalCargo = CargoFactory.eINSTANCE.createCargo();
			originalCargo.getSlots().add(originalLoadSlot);
			originalCargo.getSlots().add(originalDischargeSlot);

			originalLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID1");
			originalDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID2");
			originalCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID3");

			// Fields to update
			originalCargo.setAllowRewiring(false);
			originalLoadSlot.setDuration(50);
			originalDischargeSlot.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot);
			cargoModel.getLoadSlots().add(originalLoadSlot);
			cargoModel.getCargoes().add(originalCargo);
		}

		// Override the editing domain to hook our created objects into the cross-referencing code without requiring a resource set.
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack) {
			@Override
			public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter) {

				if (commandClass == DeleteCommand.class) {
					return new DeleteCommand(this, commandParameter.getCollection()) {

						@Override
						protected Map<EObject, Collection<Setting>> findReferences(Collection<EObject> eObjects) {
							return EcoreUtil.UsageCrossReferencer.findAll(eObjects, cargoModel);
						}
					};
				}
				return super.createCommand(commandClass, commandParameter);
			}
		};

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot;
		final DischargeSlot newDischargeSlot;
		final Cargo newCargo;
		{

			newLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot.setName("Load1");

			newDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot.setName("Discharge2");

			newCargo = CargoFactory.eINSTANCE.createCargo();
			newCargo.getSlots().add(newLoadSlot);
			newCargo.getSlots().add(newDischargeSlot);

			newLoadSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID1");
			newDischargeSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID2");
			newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID3");

			// Fields to update
			newCargo.setAllowRewiring(true);
			newLoadSlot.setDuration(100);
			newDischargeSlot.setDuration(200);

			imports.add(newDischargeSlot);
			imports.add(newLoadSlot);
			imports.add(newCargo);
		}

		// These should not fail
		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(1, cargoModel.getCargoes().size());
		Assert.assertEquals(1, cargoModel.getLoadSlots().size());
		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());

		// Check container state - old cargo and load slot should have gone. New cargo + new load copy and both old and new discharge slots should exist
		Assert.assertSame(originalLoadSlot, cargoModel.getLoadSlots().get(0));
		Assert.assertSame(originalDischargeSlot, cargoModel.getDischargeSlots().get(0));
		Assert.assertSame(newDischargeSlot, cargoModel.getDischargeSlots().get(1));
		Assert.assertSame(newCargo, cargoModel.getCargoes().get(0));

		// / Check obsolete classes
		// Should no longer be contained
		Assert.assertNull(originalCargo.eContainer());
		// Discharge slot should not be connected to a cargo
		Assert.assertFalse(originalCargo.getSlots().contains(originalDischargeSlot));
		Assert.assertNull(originalDischargeSlot.getCargo());

		Cargo importedCargo = cargoModel.getCargoes().get(0);

		Assert.assertEquals(2, importedCargo.getSlots().size());

		Assert.assertTrue(importedCargo.getSlots().contains(cargoModel.getLoadSlots().get(0)));
		Assert.assertTrue(importedCargo.getSlots().contains(cargoModel.getDischargeSlots().get(1)));

		// Assert.assertEquals(100, importedCargo.getLoadSlot().getDuration());
		// Assert.assertEquals(200, importedCargo.getDischargeSlot().getDuration());

		// Slot References should be the newly created imported object
		Slot importedCargoLoadSlot = importedCargo.getSlots().get(0);
		Assert.assertSame(originalLoadSlot, importedCargoLoadSlot);
		Assert.assertSame(importedCargoLoadSlot, cargoModel.getLoadSlots().get(0));

		Slot importedDischargeSlot = importedCargo.getSlots().get(1);
		Assert.assertSame(newDischargeSlot, importedDischargeSlot);

		// Check expected UUID fields - only load slot should have the original UUID
		Assert.assertEquals("originalUUID1", importedCargoLoadSlot.getUuid());
		Assert.assertEquals("newUUID2", importedDischargeSlot.getUuid());
		Assert.assertEquals("newUUID3", importedCargo.getUuid());

		// Check field updates
		Assert.assertTrue(importedCargo.isAllowRewiring());
		Assert.assertEquals(100, importedCargoLoadSlot.getDuration());
		Assert.assertEquals(200, importedDischargeSlot.getDuration());
		Assert.assertEquals(50, originalDischargeSlot.getDuration());
	}

	/**
	 * Update and re-wire Cargoes. Two existing cargoes, but swap discharges
	 */
	@Test
	public void testImportRewireCargo_SwapDischarges() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final Cargo originalCargo1, originalCargo2;
		final LoadSlot originalLoadSlot1, originalLoadSlot2;
		final DischargeSlot originalDischargeSlot1, originalDischargeSlot2;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot1.setName("Load1");

			originalLoadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot2.setName("Load2");

			originalDischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot1.setName("Discharge1");

			originalDischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot2.setName("Discharge2");

			originalCargo1 = CargoFactory.eINSTANCE.createCargo();
			originalCargo1.getSlots().add(originalLoadSlot1);
			originalCargo1.getSlots().add(originalDischargeSlot1);
			originalCargo1.setAllowRewiring(false);

			originalCargo2 = CargoFactory.eINSTANCE.createCargo();
			originalCargo2.getSlots().add(originalLoadSlot2);
			originalCargo2.getSlots().add(originalDischargeSlot2);
			originalCargo2.setAllowRewiring(false);

			originalLoadSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID1");
			originalDischargeSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID2");
			originalCargo1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID3");

			originalLoadSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID4");
			originalDischargeSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID5");
			originalCargo2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID6");

			// Fields to be updated
			originalCargo1.setAllowRewiring(false);
			originalCargo2.setAllowRewiring(false);
			originalLoadSlot1.setDuration(50);
			originalDischargeSlot1.setDuration(50);
			originalLoadSlot2.setDuration(50);
			originalDischargeSlot2.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot1);
			cargoModel.getDischargeSlots().add(originalDischargeSlot2);

			cargoModel.getLoadSlots().add(originalLoadSlot1);
			cargoModel.getLoadSlots().add(originalLoadSlot2);

			cargoModel.getCargoes().add(originalCargo1);
			cargoModel.getCargoes().add(originalCargo2);
		}

		// Override the editing domain to hook our created objects into the cross-referencing code without requiring a resource set.
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack) {
			@Override
			public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter) {

				if (commandClass == DeleteCommand.class) {
					return new DeleteCommand(this, commandParameter.getCollection()) {

						@Override
						protected Map<EObject, Collection<Setting>> findReferences(Collection<EObject> eObjects) {
							return EcoreUtil.UsageCrossReferencer.findAll(eObjects, cargoModel);
						}
					};
				}
				return super.createCommand(commandClass, commandParameter);
			}
		};

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot1, newLoadSlot2;
		final DischargeSlot newDischargeSlot1, newDischargeSlot2;
		final Cargo newCargo1, newCargo2;
		{

			newLoadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot1.setName("Load1");
			newLoadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot2.setName("Load2");

			newDischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot1.setName("Discharge1");
			newDischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot2.setName("Discharge2");

			newCargo1 = CargoFactory.eINSTANCE.createCargo();
			newCargo1.getSlots().add(newLoadSlot1);
			newCargo1.getSlots().add(newDischargeSlot2);
			newCargo1.setAllowRewiring(false);

			newCargo2 = CargoFactory.eINSTANCE.createCargo();
			newCargo2.getSlots().add(newLoadSlot2);
			newCargo2.getSlots().add(newDischargeSlot1);
			newCargo2.setAllowRewiring(false);

			newLoadSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID1");
			newDischargeSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID2");
			newCargo1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID3");
			newLoadSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID4");
			newDischargeSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID5");
			newCargo2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID6");

			// Fields to update
			newCargo1.setAllowRewiring(true);
			newLoadSlot1.setDuration(100);
			newDischargeSlot1.setDuration(200);

			newLoadSlot2.setDuration(300);
			newDischargeSlot2.setDuration(400);

			imports.add(newDischargeSlot1);
			imports.add(newLoadSlot1);
			imports.add(newCargo1);
			imports.add(newDischargeSlot2);
			imports.add(newLoadSlot2);
			imports.add(newCargo2);
		}

		// These should not fail
		Assert.assertEquals(2, cargoModel.getCargoes().size());
		Assert.assertEquals(2, cargoModel.getLoadSlots().size());
		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(2, cargoModel.getCargoes().size());
		Assert.assertEquals(2, cargoModel.getLoadSlots().size());
		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());

		// Check container state - same cargoes should still exist
		Assert.assertSame(originalLoadSlot1, cargoModel.getLoadSlots().get(0));
		Assert.assertSame(originalLoadSlot2, cargoModel.getLoadSlots().get(1));
		Assert.assertSame(originalDischargeSlot1, cargoModel.getDischargeSlots().get(0));
		Assert.assertSame(originalDischargeSlot2, cargoModel.getDischargeSlots().get(1));
		Cargo importedCargo1 = cargoModel.getCargoes().get(0);
		Assert.assertSame(originalCargo1, importedCargo1);
		Cargo importedCargo2 = cargoModel.getCargoes().get(1);
		Assert.assertSame(originalCargo2, importedCargo2);

		// Check new wiring
		Assert.assertSame(originalLoadSlot1, importedCargo1.getSlots().get(0));
		Assert.assertSame(originalDischargeSlot2, importedCargo1.getSlots().get(1));
		Assert.assertSame(originalLoadSlot2, importedCargo2.getSlots().get(0));
		Assert.assertSame(originalDischargeSlot1, importedCargo2.getSlots().get(1));

		// Check expected UUID fields - all original
		Assert.assertEquals("originalUUID1", cargoModel.getLoadSlots().get(0).getUuid());
		Assert.assertEquals("originalUUID2", cargoModel.getDischargeSlots().get(0).getUuid());
		Assert.assertEquals("originalUUID3", cargoModel.getCargoes().get(0).getUuid());
		Assert.assertEquals("originalUUID4", cargoModel.getLoadSlots().get(1).getUuid());
		Assert.assertEquals("originalUUID5", cargoModel.getDischargeSlots().get(1).getUuid());
		Assert.assertEquals("originalUUID6", cargoModel.getCargoes().get(1).getUuid());

		// Check field updates
		Assert.assertEquals(100, cargoModel.getLoadSlots().get(0).getDuration());
		Assert.assertEquals(200, cargoModel.getDischargeSlots().get(0).getDuration());
		Assert.assertEquals(300, cargoModel.getLoadSlots().get(1).getDuration());
		Assert.assertEquals(400, cargoModel.getDischargeSlots().get(1).getDuration());
		Assert.assertTrue(cargoModel.getCargoes().get(0).isAllowRewiring());
		Assert.assertFalse(cargoModel.getCargoes().get(1).isAllowRewiring());
	}

	/**
	 * Update and re-wire Cargoes. Two existing cargoes, but swap loads
	 */
	@Test
	public void testImportRewireCargo_SwapLoads() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		// Build up initial CargoModel
		final CargoModel cargoModel;
		final Cargo originalCargo1, originalCargo2;
		final LoadSlot originalLoadSlot1, originalLoadSlot2;
		final DischargeSlot originalDischargeSlot1, originalDischargeSlot2;
		{
			cargoModel = CargoFactory.eINSTANCE.createCargoModel();

			originalLoadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot1.setName("Load1");

			originalLoadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();
			originalLoadSlot2.setName("Load2");

			originalDischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot1.setName("Discharge1");

			originalDischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();
			originalDischargeSlot2.setName("Discharge2");

			originalCargo1 = CargoFactory.eINSTANCE.createCargo();
			originalCargo1.getSlots().add(originalLoadSlot1);
			originalCargo1.getSlots().add(originalDischargeSlot1);
			originalCargo1.setAllowRewiring(false);

			originalCargo2 = CargoFactory.eINSTANCE.createCargo();
			originalCargo2.getSlots().add(originalLoadSlot2);
			originalCargo2.getSlots().add(originalDischargeSlot2);
			originalCargo2.setAllowRewiring(false);

			originalLoadSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID1");
			originalDischargeSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID2");
			originalCargo1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID3");

			originalLoadSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID4");
			originalDischargeSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID5");
			originalCargo2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "originalUUID6");

			// Fields to be updated
			originalCargo1.setAllowRewiring(false);
			originalCargo2.setAllowRewiring(false);
			originalLoadSlot1.setDuration(50);
			originalDischargeSlot1.setDuration(50);
			originalLoadSlot2.setDuration(50);
			originalDischargeSlot2.setDuration(50);

			cargoModel.getDischargeSlots().add(originalDischargeSlot1);
			cargoModel.getDischargeSlots().add(originalDischargeSlot2);

			cargoModel.getLoadSlots().add(originalLoadSlot1);
			cargoModel.getLoadSlots().add(originalLoadSlot2);

			cargoModel.getCargoes().add(originalCargo1);
			cargoModel.getCargoes().add(originalCargo2);
		}

		// Override the editing domain to hook our created objects into the cross-referencing code without requiring a resource set.
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new CargoItemProviderAdapterFactory(), commandStack) {
			@Override
			public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter) {

				if (commandClass == DeleteCommand.class) {
					return new DeleteCommand(this, commandParameter.getCollection()) {

						@Override
						protected Map<EObject, Collection<Setting>> findReferences(Collection<EObject> eObjects) {
							return EcoreUtil.UsageCrossReferencer.findAll(eObjects, cargoModel);
						}
					};
				}
				return super.createCommand(commandClass, commandParameter);
			}
		};

		final IScenarioEditingLocation location = Mockito.mock(IScenarioEditingLocation.class);
		Mockito.when(location.getEditingDomain()).thenReturn(domain);

		final CargoImportAction action = new CargoImportAction(location, null);

		// Build the MMXRootObject used when replacing references
		{
			final LNGScenarioModel rootObject = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			rootObject.setCargoModel(cargoModel);
			Mockito.when(location.getRootObject()).thenReturn(rootObject);
		}
		final List<EObject> imports = new ArrayList<EObject>();
		final LoadSlot newLoadSlot1, newLoadSlot2;
		final DischargeSlot newDischargeSlot1, newDischargeSlot2;
		final Cargo newCargo1, newCargo2;
		{

			newLoadSlot1 = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot1.setName("Load1");
			newLoadSlot2 = CargoFactory.eINSTANCE.createLoadSlot();
			newLoadSlot2.setName("Load2");

			newDischargeSlot1 = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot1.setName("Discharge1");
			newDischargeSlot2 = CargoFactory.eINSTANCE.createDischargeSlot();
			newDischargeSlot2.setName("Discharge2");

			newCargo1 = CargoFactory.eINSTANCE.createCargo();
			newCargo1.getSlots().add(newLoadSlot2);
			newCargo1.getSlots().add(newDischargeSlot1);
			newCargo1.setAllowRewiring(false);

			newCargo2 = CargoFactory.eINSTANCE.createCargo();
			newCargo2.getSlots().add(newLoadSlot1);
			newCargo2.getSlots().add(newDischargeSlot2);
			newCargo2.setAllowRewiring(false);

			newLoadSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID1");
			newDischargeSlot1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID2");
			newCargo1.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID3");
			newLoadSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID4");
			newDischargeSlot2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID5");
			newCargo2.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), "newUUID6");

			// Fields to update
			newCargo1.setAllowRewiring(true);
			newLoadSlot1.setDuration(100);
			newDischargeSlot1.setDuration(200);

			newLoadSlot2.setDuration(300);
			newDischargeSlot2.setDuration(400);

			imports.add(newDischargeSlot1);
			imports.add(newLoadSlot1);
			imports.add(newCargo1);
			imports.add(newDischargeSlot2);
			imports.add(newLoadSlot2);
			imports.add(newCargo2);
		}

		// These should not fail
		Assert.assertEquals(2, cargoModel.getCargoes().size());
		Assert.assertEquals(2, cargoModel.getLoadSlots().size());
		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());

		final Command cmd = action.mergeImports(cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), imports);

		Assert.assertTrue(cmd.canExecute());
		cmd.execute();

		Assert.assertFalse(cargoModel.getCargoes().isEmpty());
		Assert.assertFalse(cargoModel.getLoadSlots().isEmpty());
		Assert.assertFalse(cargoModel.getDischargeSlots().isEmpty());

		Assert.assertEquals(2, cargoModel.getCargoes().size());
		Assert.assertEquals(2, cargoModel.getLoadSlots().size());
		Assert.assertEquals(2, cargoModel.getDischargeSlots().size());

		// Check container state - same cargoes should still exist
		Assert.assertSame(originalLoadSlot1, cargoModel.getLoadSlots().get(0));
		Assert.assertSame(originalLoadSlot2, cargoModel.getLoadSlots().get(1));
		Assert.assertSame(originalDischargeSlot1, cargoModel.getDischargeSlots().get(0));
		Assert.assertSame(originalDischargeSlot2, cargoModel.getDischargeSlots().get(1));
		Cargo importedCargo1 = cargoModel.getCargoes().get(0);
		Assert.assertSame(originalCargo1, importedCargo1);
		Cargo importedCargo2 = cargoModel.getCargoes().get(1);
		Assert.assertSame(originalCargo2, importedCargo2);

		// Check new wiring
		Assert.assertEquals(2, importedCargo1.getSlots().size());
		Assert.assertEquals(2, importedCargo2.getSlots().size());
		Assert.assertTrue(importedCargo1.getSlots().contains(originalLoadSlot2));
		Assert.assertTrue(importedCargo1.getSlots().contains(originalDischargeSlot1));
		Assert.assertTrue(importedCargo2.getSlots().contains(originalLoadSlot1));
		Assert.assertTrue(importedCargo2.getSlots().contains(originalDischargeSlot2));

		// Check expected UUID fields - all original
		Assert.assertEquals("originalUUID1", cargoModel.getLoadSlots().get(0).getUuid());
		Assert.assertEquals("originalUUID2", cargoModel.getDischargeSlots().get(0).getUuid());
		Assert.assertEquals("originalUUID3", cargoModel.getCargoes().get(0).getUuid());
		Assert.assertEquals("originalUUID4", cargoModel.getLoadSlots().get(1).getUuid());
		Assert.assertEquals("originalUUID5", cargoModel.getDischargeSlots().get(1).getUuid());
		Assert.assertEquals("originalUUID6", cargoModel.getCargoes().get(1).getUuid());

		// Check field updates
		Assert.assertEquals(100, cargoModel.getLoadSlots().get(0).getDuration());
		Assert.assertEquals(200, cargoModel.getDischargeSlots().get(0).getDuration());
		Assert.assertEquals(300, cargoModel.getLoadSlots().get(1).getDuration());
		Assert.assertEquals(400, cargoModel.getDischargeSlots().get(1).getDuration());
		Assert.assertTrue(cargoModel.getCargoes().get(0).isAllowRewiring());
		Assert.assertFalse(cargoModel.getCargoes().get(1).isAllowRewiring());
	}

}
