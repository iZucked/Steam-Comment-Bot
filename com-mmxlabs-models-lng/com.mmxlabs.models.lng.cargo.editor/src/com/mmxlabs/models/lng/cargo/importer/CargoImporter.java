/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CargoImporter extends DefaultClassImporter {
	@Override
	protected boolean shouldFlattenReference(final EReference reference) {
		return super.shouldFlattenReference(reference) || reference == CargoPackage.eINSTANCE.getCargo_LoadSlot() || reference == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	}

	@Override
	public Collection<EObject> importObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		LoadSlot load = null;
		DischargeSlot discharge = null;
		Cargo cargo = null;
		for (final EObject o : result) {
			if (o instanceof Cargo) cargo = (Cargo) o;
			else if (o instanceof LoadSlot) load = (LoadSlot) o;
			else if (o instanceof DischargeSlot) discharge = (DischargeSlot) o;
		}
		
		// fix missing names
		
		
		if (cargo != null && cargo.getName() != null && cargo.getName().trim().isEmpty() == false) {
			final String cargoName = cargo.getName().trim();
			if (load != null && (load.getName() == null || load.getName().trim().isEmpty())) {
				final LoadSlot load2 = load;
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						load2.setName("load-" + cargoName);
						context.registerNamedObject(load2);
					}
					
					@Override
					public int getStage() {
						return -1;
					}
				});
			}
			
			if (discharge != null && (discharge.getName() == null || discharge.getName().trim().isEmpty())) {
				final DischargeSlot discharge2 = discharge;
				context.doLater(new IDeferment() {
					@Override
					public void run(final IImportContext context) {
						discharge2.setName("discharge-" + cargoName);
						context.registerNamedObject(discharge2);
					}
					
					@Override
					public int getStage() {
						return -1;
					}
				});
			}
		}
		
		return result;
	}
}
