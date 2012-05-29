/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.util.HashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.registries.IReferenceValueProviderFactoryRegistry;

/**
 * Utility class for caching reference value providers for a given root object. If a reference value provider
 * has been created the root object / class / reference it is reused; otherwise gets factories from the factory
 * registry and creates new reference value providers with the root object.
 * 
 * @author hinton
 *
 */
public class ReferenceValueProviderCache implements IReferenceValueProviderProvider {
	private final IReferenceValueProviderFactoryRegistry registry;
	private final MMXRootObject rootObject;
	private final HashMap<Pair<EClass, EReference>, IReferenceValueProvider> cache = 
			new HashMap<Pair<EClass, EReference>, IReferenceValueProvider>();
	
	public ReferenceValueProviderCache(final MMXRootObject root, final IReferenceValueProviderFactoryRegistry registry) {
		this.registry = registry;
		this.rootObject = root;
	}
	
	public ReferenceValueProviderCache(final MMXRootObject root) {
		this(root, Activator.getDefault().getReferenceValueProviderFactoryRegistry());
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider#getReferenceValueProvider(org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public synchronized IReferenceValueProvider getReferenceValueProvider(final EClass owner, final EReference reference) {
		final Pair<EClass, EReference> p = new Pair<EClass, EReference>(owner, reference);
		if (cache.containsKey(p)) {
			return cache.get(p);
		}
		
		final IReferenceValueProviderFactory factory = registry.getValueProviderFactory(owner, reference);
		final IReferenceValueProvider provider = factory == null ? null : factory.createReferenceValueProvider(owner, reference, rootObject);
		cache.put(p, provider);
		
		return provider;
	}
	
	public void dispose() {
		for (final IReferenceValueProvider valueProvider : cache.values()) {
			valueProvider.dispose();
		}
	}
}
