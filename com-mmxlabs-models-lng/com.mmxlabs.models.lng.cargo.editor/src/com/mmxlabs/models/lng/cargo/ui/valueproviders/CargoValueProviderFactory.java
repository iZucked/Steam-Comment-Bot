package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CargoValueProviderFactory implements
		IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner,
			EReference reference, MMXRootObject rootObject) {
		final CargoModel cm = rootObject.getSubModel(CargoModel.class);
		if (cm == null) return null;
		if (TypesPackage.eINSTANCE.getACargo().isSuperTypeOf(reference.getEReferenceType())) {
			return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_Cargoes());
		} else if (reference.getEReferenceType().isSuperTypeOf(CargoPackage.eINSTANCE.getCargoGroup())) {
			return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
		}
		return null;
	}
}
