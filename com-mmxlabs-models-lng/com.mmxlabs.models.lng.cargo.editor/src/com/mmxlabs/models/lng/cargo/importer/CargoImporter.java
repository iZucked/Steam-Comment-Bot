/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class CargoImporter extends DefaultClassImporter {
	@Override
	protected boolean shouldFlattenReference(EReference reference) {
		return super.shouldFlattenReference(reference) || reference == CargoPackage.eINSTANCE.getCargo_LoadSlot() || reference == CargoPackage.eINSTANCE.getCargo_DischargeSlot();
	}	
}
