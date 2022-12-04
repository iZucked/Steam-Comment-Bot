package com.mmxlabs.models.ui.valueproviders;

import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;

public class RefetchingReferenceValueProvider extends AbstractIdentityReferenceValueProviderWrapper {

	private Supplier<IReferenceValueProviderProvider> providerProviderProvider;
	private EClass owner;
	private EReference reference;

	public RefetchingReferenceValueProvider(@NonNull final Supplier<IReferenceValueProviderProvider> providerProviderProvider, final EClass owner, final EReference reference) {
		this.providerProviderProvider = providerProviderProvider;
		this.owner = owner;
		this.reference = reference;
	}

	@Override
	protected IReferenceValueProvider getReferenceValueProvider() {
		return providerProviderProvider.get().getReferenceValueProvider(owner, reference);
	}

	@Override
	public void dispose() {
		providerProviderProvider = null;
		owner = null;
		reference = null;
	}

}
