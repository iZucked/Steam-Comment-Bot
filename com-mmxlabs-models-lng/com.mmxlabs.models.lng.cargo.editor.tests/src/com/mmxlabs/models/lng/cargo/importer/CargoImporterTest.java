/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultAttributeImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class CargoImporterTest {

	@Test
	public void testLoadSlotImport() {
		// Build Data structure to import
		final Map<String, String> row = new HashMap<>();
		{

			row.put("kind", "");
			row.put("name", "");

			row.put("buy.kind", "LoadSlot");
			row.put("buy." + MMXCorePackage.eINSTANCE.getNamedObject_Name().getName().toLowerCase(), "LoadSlot1");
			row.put("buy." + MMXCorePackage.eINSTANCE.getUUIDObject_Uuid().getName().toLowerCase(), "UUID");
			row.put("buy." + CargoPackage.eINSTANCE.getSlot_Contract().getName().toLowerCase(), "ContractName");
			row.put("buy." + CargoPackage.eINSTANCE.getSlot_Duration().getName().toLowerCase(), "42");
			row.put("buy.min", "1000");
			row.put("buy.max", "2000");
			row.put("buy." + CargoPackage.eINSTANCE.getSlot_Optional().getName().toLowerCase(), "TRUE");
			row.put("buy." + CargoPackage.eINSTANCE.getSlot_Port().getName().toLowerCase(), "PortName");
			row.put("buy.price", "PRICE+1");
			row.put("buy." + CargoPackage.eINSTANCE.getSlot_WindowSize().getName().toLowerCase(), "7");
			row.put("buy.date", "2013-1-1");
			row.put("buy.time", "9");
			row.put("buy.cargoCV", "23.702");
		}
		final CargoImporter cargoImporter = createCargoImporter();
		final IMMXImportContext context = Mockito.mock(IMMXImportContext.class);
		// This is needed otherwise there is no decimal separator!
		Mockito.when(context.getDecimalSeparator()).thenReturn('.');
		final Collection<EObject> importedObjects = cargoImporter.importObject(null, CargoPackage.eINSTANCE.getCargo(), row, context).getCreatedObjects();

		Assertions.assertEquals(2, importedObjects.size());

		boolean foundCargo = false;
		boolean foundLoadSlot = false;

		for (final EObject obj : importedObjects) {
			if (obj instanceof Cargo) {
				Assertions.assertFalse(foundCargo);
				foundCargo = true;

			} else if (obj instanceof LoadSlot) {
				Assertions.assertFalse(foundLoadSlot);
				foundLoadSlot = true;

				final LoadSlot loadSlot = (LoadSlot) obj;

				Assertions.assertEquals("LoadSlot1", loadSlot.getName());
				Assertions.assertEquals("UUID", loadSlot.getUuid());
				// Assertions.assertEquals("ContractName", loadSlot.getContract());
				Assertions.assertEquals(42, loadSlot.getDuration());
				Assertions.assertEquals(1000, loadSlot.getMinQuantity());
				Assertions.assertEquals(2000, loadSlot.getMaxQuantity());
				Assertions.assertTrue(loadSlot.isOptional());
				// Assertions.assertEquals("PortName", loadSlot.getPort());
				Assertions.assertEquals("PRICE+1", loadSlot.getPriceExpression());
				Assertions.assertEquals(7, loadSlot.getWindowSize());
				Assertions.assertEquals(LocalDate.of(2013, 1, 1), loadSlot.getWindowStart());
				Assertions.assertEquals(9, loadSlot.getWindowStartTime());
				Assertions.assertEquals(23.702, loadSlot.getCargoCV(), 0.0001);
			}
		}
		Assertions.assertTrue(foundCargo);
		Assertions.assertTrue(foundLoadSlot);
	}

	@Test
	public void testDischargeSlotImport() {
		// Build Data structure to import
		final Map<String, String> row = new HashMap<>();
		{

			row.put("kind", "");
			row.put("name", "");

			row.put("sell.kind", "DischargeSlot");
			row.put("sell." + MMXCorePackage.eINSTANCE.getNamedObject_Name().getName().toLowerCase(), "DischargeSlot1");
			row.put("sell." + MMXCorePackage.eINSTANCE.getUUIDObject_Uuid().getName().toLowerCase(), "UUID");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Contract().getName().toLowerCase(), "ContractName");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Duration().getName().toLowerCase(), "42");
			row.put("sell.max", "2000");
			row.put("sell.min", "1000");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Optional().getName().toLowerCase(), "TRUE");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Port().getName().toLowerCase(), "PortName");
			row.put("sell.price", "PRICE+1");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_WindowSize().getName().toLowerCase(), "7");
			row.put("sell.date", "2013-1-1");
			row.put("sell.time", "9");
			row.put("sell." + CargoPackage.eINSTANCE.getDischargeSlot_PurchaseDeliveryType().getName().toLowerCase(), "Any");
		}
		final CargoImporter cargoImporter = createCargoImporter();
		final IMMXImportContext context = Mockito.mock(IMMXImportContext.class);
		final Collection<EObject> importedObjects = cargoImporter.importObject(null, CargoPackage.eINSTANCE.getCargo(), row, context).getCreatedObjects();

		Assertions.assertEquals(2, importedObjects.size());

		boolean foundCargo = false;
		boolean foundDischargeSlot = false;

		for (final EObject obj : importedObjects) {
			if (obj instanceof Cargo) {
				Assertions.assertFalse(foundCargo);
				foundCargo = true;

			} else if (obj instanceof DischargeSlot) {
				Assertions.assertFalse(foundDischargeSlot);
				foundDischargeSlot = true;

				final DischargeSlot dischargeSlot = (DischargeSlot) obj;

				Assertions.assertEquals("DischargeSlot1", dischargeSlot.getName());
				Assertions.assertEquals("UUID", dischargeSlot.getUuid());
				// Assertions.assertEquals("ContractName", loadSlot.getContract());
				Assertions.assertEquals(42, dischargeSlot.getDuration());
				Assertions.assertEquals(1000, dischargeSlot.getMinQuantity());
				Assertions.assertEquals(2000, dischargeSlot.getMaxQuantity());
				Assertions.assertTrue(dischargeSlot.isOptional());
				// Assertions.assertEquals("PortName", loadSlot.getPort());
				Assertions.assertEquals("PRICE+1", dischargeSlot.getPriceExpression());
				Assertions.assertEquals(7, dischargeSlot.getWindowSize());
				Assertions.assertEquals(LocalDate.of(2013, 1, 1), dischargeSlot.getWindowStart());
				Assertions.assertEquals(9, dischargeSlot.getWindowStartTime());
				Assertions.assertEquals(CargoDeliveryType.ANY, dischargeSlot.getPurchaseDeliveryType());
			}
		}
		Assertions.assertTrue(foundCargo);
		Assertions.assertTrue(foundDischargeSlot);
	}

	@Test
	public void testImportDataDischargeSlotOnly() throws IOException {

		final List<Map<String, String>> recordData = new ArrayList<>(1);
		// Build Data structure to import
		{

			final Map<String, String> row = new HashMap<>();
			row.put("kind", "");
			row.put("name", "");

			row.put("sell.kind", "DischargeSlot");
			row.put("sell." + MMXCorePackage.eINSTANCE.getNamedObject_Name().getName().toLowerCase(), "DischargeSlot1");
			row.put("sell." + MMXCorePackage.eINSTANCE.getUUIDObject_Uuid().getName().toLowerCase(), "UUID");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Contract().getName().toLowerCase(), "ContractName");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Duration().getName().toLowerCase(), "42");
			row.put("sell.max", "2000");
			row.put("sell.min", "1000");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Optional().getName().toLowerCase(), "TRUE");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_Port().getName().toLowerCase(), "PortName");
			row.put("sell.price", "PRICE+1");
			row.put("sell." + CargoPackage.eINSTANCE.getSlot_WindowSize().getName().toLowerCase(), "7");
			row.put("sell.date", "2013-1-1");
			row.put("sell.time", "9");
			row.put("sell." + CargoPackage.eINSTANCE.getDischargeSlot_PurchaseDeliveryType().getName().toLowerCase(), "Any");

			recordData.add(row);
		}

		final File tmp = File.createTempFile("CargoImporterTest", ".csv");
		try {
			writeCSV(recordData, tmp);
			final CSVReader reader = new FileCSVReader(tmp);
			try {
				final CargoImporter cargoImporter = createCargoImporter();
				final IMMXImportContext context = Mockito.mock(IMMXImportContext.class);
				final Collection<EObject> importedObjects = cargoImporter.importObjects(CargoPackage.eINSTANCE.getCargo(), reader, context);

				Assertions.assertEquals(1, importedObjects.size());

				final EObject obj = importedObjects.iterator().next();
				Assertions.assertTrue(obj instanceof DischargeSlot);

				final DischargeSlot dischargeSlot = (DischargeSlot) obj;

				Assertions.assertEquals("DischargeSlot1", dischargeSlot.getName());
				Assertions.assertEquals("UUID", dischargeSlot.getUuid());
				// Assertions.assertEquals("ContractName", loadSlot.getContract());
				Assertions.assertEquals(42, dischargeSlot.getDuration());
				Assertions.assertEquals(1000, dischargeSlot.getMinQuantity());
				Assertions.assertEquals(2000, dischargeSlot.getMaxQuantity());
				Assertions.assertTrue(dischargeSlot.isOptional());
				// Assertions.assertEquals("PortName", loadSlot.getPort());
				Assertions.assertEquals("PRICE+1", dischargeSlot.getPriceExpression());
				Assertions.assertEquals(7, dischargeSlot.getWindowSize());
				Assertions.assertEquals(LocalDate.of(2013, 1, 1), dischargeSlot.getWindowStart());
				Assertions.assertEquals(9, dischargeSlot.getWindowStartTime());
				Assertions.assertEquals(CargoDeliveryType.ANY, dischargeSlot.getPurchaseDeliveryType());
			} finally {
				// Clean up!
				reader.close();
			}
		} finally {
			// Clean up!
			tmp.delete();
		}
	}

	// //////////////// Methods lifted from ExportCSVWizard

	/**
	 * @param rows
	 * @param outputFile
	 */
	private void writeCSV(final Collection<Map<String, String>> rows, final File outputFile) {
		if (rows.isEmpty())
			return;
		final LinkedHashSet<String> keys = new LinkedHashSet<>();
		for (final Map<String, String> row : rows) {
			keys.addAll(row.keySet());
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			boolean firstKey = true;
			for (final String key : keys) {
				if (!firstKey)
					writer.write(",");
				firstKey = false;
				writer.write(escape(key));
			}
			for (final Map<String, String> row : rows) {
				writer.write("\n");
				firstKey = true;
				for (final String key : keys) {
					if (!firstKey)
						writer.write(",");
					firstKey = false;
					writer.write(escape(row.get(key)));
				}
			}
		} catch (final IOException e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	private String escape(final String key) {
		if (key == null)
			return "";
		final String sub = key.trim().replace("\"", "\\\"");
		return sub.contains(",") ? "\"" + sub + "\"" : sub;
	}

	@NonNull
	private CargoImporter createCargoImporter() {

		final IImporterRegistry registry = new IImporterRegistry() {

			@Inject
			private Injector _injector;

			@Override
			public ISubmodelImporter getSubmodelImporter(final EClass subModelClass) {
				return null;
			}

			@Override
			public Collection<IPostModelImporter> getPostModelImporters() {
				return null;
			}

			@Override
			public Collection<IExtraModelImporter> getExtraModelImporters() {
				return null;
			}

			@Override
			public IClassImporter getClassImporter(final EClass eClass) {
				if (eClass == CargoPackage.Literals.LOAD_SLOT) {
					return _injector.getInstance(LoadSlotImporter.class);
				}
				if (eClass == CargoPackage.Literals.DISCHARGE_SLOT) {
					return _injector.getInstance(DischargeSlotImporter.class);
				}
				return _injector.getInstance(DefaultClassImporter.class);
			}

			@Override
			public IAttributeImporter getAttributeImporter(final EDataType dataType) {

				return _injector.getInstance(DefaultAttributeImporter.class);
			}

			@Override
			public Collection<ISubmodelImporter> getAllSubModelImporters() {
				return null;
			}
		};

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {

				bind(IImporterRegistry.class).toInstance(registry);
			}
		});
		return injector.getInstance(CargoImporter.class);
	}
}
