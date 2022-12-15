/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;

public class RefetchingReferenceValueProviderProvider implements IReferenceValueProviderProvider {

	private Supplier<IReferenceValueProviderProvider> providerProviderProvider;

	public RefetchingReferenceValueProviderProvider(@NonNull final Supplier<IReferenceValueProviderProvider> providerProviderProvider) {
		this.providerProviderProvider = providerProviderProvider;
	}

	@Override
	public IReferenceValueProvider getReferenceValueProvider(EClass owner, EReference reference) {
		return new RefetchingReferenceValueProvider(providerProviderProvider, owner, reference);
	}

	@Override
	public void dispose() {
		providerProviderProvider = null;

	}

}
