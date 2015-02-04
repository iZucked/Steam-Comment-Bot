/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CargoValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cm = lngScenarioModel.getPortfolioModel().getCargoModel();
			if (cm == null) {
				return null;
			}
			if (CargoPackage.eINSTANCE.getCargo().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_Cargoes());
			} else if (reference.getEReferenceType().isSuperTypeOf(CargoPackage.eINSTANCE.getCargoGroup())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
			} else if (CargoPackage.eINSTANCE.getLoadSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots());
			} else if (CargoPackage.eINSTANCE.getDischargeSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots());
			} else if (CargoPackage.eINSTANCE.getSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new MergedReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots());
			}
		}
		return null;
	}
}
