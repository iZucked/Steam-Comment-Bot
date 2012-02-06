package com.mmxlabs.models.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IReferenceValueProviderFactory {

	IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference,
			MMXRootObject rootObject);

}
