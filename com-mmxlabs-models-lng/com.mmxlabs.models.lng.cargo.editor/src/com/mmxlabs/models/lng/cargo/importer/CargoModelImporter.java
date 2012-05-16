/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class CargoModelImporter implements ISubmodelImporter {
	private static final String CARGO_KEY = "CARGO";
	private IClassImporter cargoImporter = Activator.getDefault()
			.getImporterRegistry()
			.getClassImporter(CargoPackage.eINSTANCE.getCargo());
	private HashMap<String, String> inputs = new HashMap<String, String>();
	{
		inputs.put(CARGO_KEY, "Cargoes");
	}
	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs,
			IImportContext context) {
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		
		if (inputs.containsKey(CARGO_KEY)) {
			final CSVReader reader = inputs.get(CARGO_KEY);
			final Collection<EObject> values = cargoImporter.importObjects(CargoPackage.eINSTANCE.getCargo(), reader, context);
			for (final EObject object : values) {
				if (object instanceof Cargo) {
					cargoModel.getCargoes().add((Cargo) object);
				} else if (object instanceof LoadSlot) {
					cargoModel.getLoadSlots().add((LoadSlot) object);
				} else if (object instanceof DischargeSlot) {
					cargoModel.getDischargeSlots().add((DischargeSlot) object);
				}
			}
		}
		
		return cargoModel;
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		output.put(CARGO_KEY, cargoImporter.exportObjects(((CargoModel) model).getCargoes()));
	}
}
