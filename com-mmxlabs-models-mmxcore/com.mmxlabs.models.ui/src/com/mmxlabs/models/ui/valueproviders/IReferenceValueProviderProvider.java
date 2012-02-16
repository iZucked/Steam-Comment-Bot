package com.mmxlabs.models.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

public interface IReferenceValueProviderProvider {

	public abstract IReferenceValueProvider getReferenceValueProvider(
			final EClass owner, final EReference reference);

}