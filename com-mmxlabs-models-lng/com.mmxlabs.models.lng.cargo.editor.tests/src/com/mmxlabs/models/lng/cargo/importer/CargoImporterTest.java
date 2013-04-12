package com.mmxlabs.models.lng.cargo.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.importer.IImportContext;

public class CargoImporterTest {

	@Test
	public void testLoadSlotImport() {
		// Build Data structure to import
		Map<String, String> row = new HashMap<String, String>();
		{

			row.put("kind", "");
			row.put("name", "");

			row.put("loadslot.kind", "LoadSlot");
			row.put("loadslot." + MMXCorePackage.eINSTANCE.getNamedObject_Name().getName().toLowerCase(), "LoadSlot1");
			row.put("loadslot." + MMXCorePackage.eINSTANCE.getUUIDObject_Uuid().getName().toLowerCase(), "UUID");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_Contract().getName().toLowerCase(), "ContractName");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_Duration().getName().toLowerCase(), "42");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_MaxQuantity().getName().toLowerCase(), "2000");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_MinQuantity().getName().toLowerCase(), "1000");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_Optional().getName().toLowerCase(), "TRUE");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_Port().getName().toLowerCase(), "PortName");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_PriceExpression().getName().toLowerCase(), "PRICE+1");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_WindowSize().getName().toLowerCase(), "7");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_WindowStart().getName().toLowerCase(), "2013-1-1");
			row.put("loadslot." + CargoPackage.eINSTANCE.getSlot_WindowStartTime().getName().toLowerCase(), "9");
			row.put("loadslot." + CargoPackage.eINSTANCE.getLoadSlot_CargoCV().getName().toLowerCase(), "12.7");
		}
		CargoImporter cargoImporter = new CargoImporter();
		IImportContext context = Mockito.mock(IImportContext.class);
		Collection<EObject> importedObjects = cargoImporter.importObject(CargoPackage.eINSTANCE.getCargo(), row, context);

		Assert.assertEquals(1, importedObjects.size());

		EObject obj = importedObjects.iterator().next();
		Assert.assertTrue(obj instanceof LoadSlot);

		LoadSlot loadSlot = (LoadSlot) obj;

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

	@Test
	public void testDischargeSlotImport() {
		List<Map<String, String>> recordData = new ArrayList<Map<String, String>>(1);
		// Build Data structure to import
		Map<String, String> row = new HashMap<String, String>();
		{

			row.put("kind", "");
			row.put("name", "");

			row.put("dischargeslot.kind", "DischargeSlot");
			row.put("dischargeslot." + MMXCorePackage.eINSTANCE.getNamedObject_Name().getName().toLowerCase(), "DischrgeSlot1");
			row.put("dischargeslot." + MMXCorePackage.eINSTANCE.getUUIDObject_Uuid().getName().toLowerCase(), "UUID");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_Contract().getName().toLowerCase(), "ContractName");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_Duration().getName().toLowerCase(), "42");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_MaxQuantity().getName().toLowerCase(), "2000");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_MinQuantity().getName().toLowerCase(), "1000");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_Optional().getName().toLowerCase(), "TRUE");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_Port().getName().toLowerCase(), "PortName");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_PriceExpression().getName().toLowerCase(), "PRICE+1");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_WindowSize().getName().toLowerCase(), "7");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_WindowStart().getName().toLowerCase(), "2013-1-1");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getSlot_WindowStartTime().getName().toLowerCase(), "9");
			row.put("dischargeslot." + CargoPackage.eINSTANCE.getDischargeSlot_PurchaseDeliveryType().getName().toLowerCase(), "ANY");
		}
		CargoImporter cargoImporter = new CargoImporter();
		IImportContext context = Mockito.mock(IImportContext.class);
		Collection<EObject> importedObjects = cargoImporter.importObject(CargoPackage.eINSTANCE.getCargo(), row, context);

		Assert.assertEquals(1, importedObjects.size());

		EObject obj = importedObjects.iterator().next();
		Assert.assertTrue(obj instanceof DischargeSlot);

		DischargeSlot dischargeSlot = (DischargeSlot) obj;

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
