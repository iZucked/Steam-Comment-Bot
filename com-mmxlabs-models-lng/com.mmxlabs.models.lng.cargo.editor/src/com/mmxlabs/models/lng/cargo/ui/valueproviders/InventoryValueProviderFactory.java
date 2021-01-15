/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class InventoryValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final CargoModel cargoModel = ((LNGScenarioModel) rootObject).getCargoModel();
			if (cargoModel != null) {
				final EClass referenceClass = reference.getEReferenceType();
				if (referenceClass == CargoPackage.eINSTANCE.getInventory()) {
					return new SimpleReferenceValueProvider(cargoModel, CargoPackage.eINSTANCE.getCargoModel_InventoryModels());
				}
			}
		}
		return null;
	}
}