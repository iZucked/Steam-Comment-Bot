/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IImportContext;

public class CargoImporterTest {

	@Test
	public void testLoadSlotImport() {
		// Build Data structure to import
		final Map<String, String> row = new HashMap<String, String>();
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
			row.put("buy." + CargoPackage.eINSTANCE.getLoadSlot_CargoCV().getName().toLowerCase(), "12.7");
		}
		final CargoImporter cargoImporter = new CargoImporter();
		final IImportContext context = Mockito.mock(IImportContext.class);
		final Collection<EObject> importedObjects = cargoImporter.importObject(CargoPackage.eINSTANCE.getCargo(), row, context);

		Assert.assertEquals(2, importedObjects.size());

		boolean foundCargo = false;
		boolean foundLoadSlot = false;

		for (final EObject obj : importedObjects) {
			if (obj instanceof Cargo) {
				Assert.assertFalse(foundCargo);
				foundCargo = true;

			} else if (obj instanceof LoadSlot) {
				Assert.assertFalse(foundLoadSlot);
				foundLoadSlot = true;

				final LoadSlot loadSlot = (LoadSlot) obj;

				Assert.assertEquals("LoadSlot1", loadSlot.getName());
				Assert.assertEquals("UUID", loadSlot.getUuid());
				// Assert.assertEquals("ContractName", loadSlot.getContract());
				Assert.assertEquals(42, loadSlot.getDuration());
				Assert.assertEquals(1000, loadSlot.getMinQuantity());
				Assert.assertEquals(2000, loadSlot.getMaxQuantity());
				Assert.assertTrue(loadSlot.isOptional());
				// Assert.assertEquals("PortName", loadSlot.getPort());
				Assert.assertEquals("PRICE+1", loadSlot.getPriceExpression());
				Assert.assertEquals(7, loadSlot.getWindowSize());
				Assert.assertEquals(new Date(2013 - 1900, 0, 1, 0, 0, 0), loadSlot.getWindowStart());
				Assert.assertEquals(9, loadSlot.getWindowStartTime());
				Assert.assertEquals(12.7, loadSlot.getCargoCV(), 0);
			}
		}
		Assert.assertTrue(foundCargo);
		Assert.assertTrue(foundLoadSlot);
	}

	@Test
	public void testDischargeSlotImport() {
		final List<Map<String, String>> recordData = new ArrayList<Map<String, String>>(1);
		// Build Data structure to import
		final Map<String, String> row = new HashMap<String, String>();
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
			row.put("sell." + CargoPackage.eINSTANCE.getDischargeSlot_PurchaseDeliveryType().getName().toLowerCase(), "ANY");
		}
		final CargoImporter cargoImporter = new CargoImporter();
		final IImportContext context = Mockito.mock(IImportContext.class);
		final Collection<EObject> importedObjects = cargoImporter.importObject(CargoPackage.eINSTANCE.getCargo(), row, context);

		Assert.assertEquals(2, importedObjects.size());

		boolean foundCargo = false;
		boolean foundDischargeSlot = false;

		for (final EObject obj : importedObjects) {
			if (obj instanceof Cargo) {
				Assert.assertFalse(foundCargo);
				foundCargo = true;

			} else if (obj instanceof DischargeSlot) {
				Assert.assertFalse(foundDischargeSlot);
				foundDischargeSlot = true;

				final DischargeSlot dischargeSlot = (DischargeSlot) obj;

				Assert.assertEquals("DischargeSlot1", dischargeSlot.getName());
				Assert.assertEquals("UUID", dischargeSlot.getUuid());
				// Assert.assertEquals("ContractName", loadSlot.getContract());
				Assert.assertEquals(42, dischargeSlot.getDuration());
				Assert.assertEquals(1000, dischargeSlot.getMinQuantity());
				Assert.assertEquals(2000, dischargeSlot.getMaxQuantity());
				Assert.assertTrue(dischargeSlot.isOptional());
				// Assert.assertEquals("PortName", loadSlot.getPort());
				Assert.assertEquals("PRICE+1", dischargeSlot.getPriceExpression());
				Assert.assertEquals(7, dischargeSlot.getWindowSize());
				Assert.assertEquals(new Date(2013 - 1900, 0, 1, 0, 0, 0), dischargeSlot.getWindowStart());
				Assert.assertEquals(9, dischargeSlot.getWindowStartTime());
				Assert.assertEquals(CargoDeliveryType.ANY, dischargeSlot.getPurchaseDeliveryType());
			}
		}
		Assert.assertTrue(foundCargo);
		Assert.assertTrue(foundDischargeSlot);
	}

	@Test
	public void testImportDataDischargeSlotOnly() throws IOException {

		final List<Map<String, String>> recordData = new ArrayList<Map<String, String>>(1);
		// Build Data structure to import
		{

			final Map<String, String> row = new HashMap<String, String>();
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
			row.put("sell." + CargoPackage.eINSTANCE.getDischargeSlot_PurchaseDeliveryType().getName().toLowerCase(), "ANY");

			recordData.add(row);
		}

		final File tmp = File.createTempFile("CargoImporterTest", ".csv");
		try {
			writeCSV(recordData, tmp);
			final CSVReader reader = new CSVReader(tmp);
			try {
				final CargoImporter cargoImporter = new CargoImporter();
				final IImportContext context = Mockito.mock(IImportContext.class);
				final Collection<EObject> importedObjects = cargoImporter.importObjects(CargoPackage.eINSTANCE.getCargo(), reader, context);

				Assert.assertEquals(1, importedObjects.size());

				final EObject obj = importedObjects.iterator().next();
				Assert.assertTrue(obj instanceof DischargeSlot);

				final DischargeSlot dischargeSlot = (DischargeSlot) obj;

				Assert.assertEquals("DischargeSlot1", dischargeSlot.getName());
				Assert.assertEquals("UUID", dischargeSlot.getUuid());
				// Assert.assertEquals("ContractName", loadSlot.getContract());
				Assert.assertEquals(42, dischargeSlot.getDuration());
				Assert.assertEquals(1000, dischargeSlot.getMinQuantity());
				Assert.assertEquals(2000, dischargeSlot.getMaxQuantity());
				Assert.assertTrue(dischargeSlot.isOptional());
				// Assert.assertEquals("PortName", loadSlot.getPort());
				Assert.assertEquals("PRICE+1", dischargeSlot.getPriceExpression());
				Assert.assertEquals(7, dischargeSlot.getWindowSize());
				Assert.assertEquals(new Date(2013 - 1900, 0, 1, 0, 0, 0), dischargeSlot.getWindowStart());
				Assert.assertEquals(9, dischargeSlot.getWindowStartTime());
				Assert.assertEquals(CargoDeliveryType.ANY, dischargeSlot.getPurchaseDeliveryType());
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
		final LinkedHashSet<String> keys = new LinkedHashSet<String>();
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

}
