/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class SpotDischargeSlotFactory extends DefaultModelFactory {

	protected EObject constructInstance(EClass eClass) {

		if (CargoPackage.eINSTANCE.getDischargeSlot() == eClass) {
			eClass = CargoPackage.eINSTANCE.getSpotDischargeSlot();
		}

		final EObject object = eClass.getEPackage().getEFactoryInstance().create(eClass);

		// create singly-contained sub objects, by default

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isMany() == false && reference.isContainment() == true) {
				object.eSet(reference, createSubInstance(object, reference));
			}
		}

		return object;
	}

}
