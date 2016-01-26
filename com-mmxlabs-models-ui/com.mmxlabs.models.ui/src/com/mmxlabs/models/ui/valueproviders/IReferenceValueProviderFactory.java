/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IReferenceValueProviderFactory {
	/**
	 * Create a reference value provider suitable for use with the given feature on the given EClass. References will be taken from the root object
	 * @param owner
	 * @param reference
	 * @param rootObject
	 * @return
	 */
	IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference,
			MMXRootObject rootObject);
}
