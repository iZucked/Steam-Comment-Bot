/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Long-named class which provides a {@link ReferenceValueProviderCache} for an {@link MMXRootObject} instance.
 * 
 * Previously constructed {@link ReferenceValueProviderCache} instances are held in a {@link WeakHashMap}, to prevent duplicate construction.
 * 
 * @author hinton
 *
 */
public class MMXRootObjectValueProviderCacheAdapterFactory implements IAdapterFactory {
	private final WeakHashMap<MMXRootObject, WeakReference<ReferenceValueProviderCache>> cacheCache = new WeakHashMap<MMXRootObject, WeakReference<ReferenceValueProviderCache>>();

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof MMXRootObject) {
			if (adapterType.isAssignableFrom(ReferenceValueProviderCache.class)) {
				synchronized (this) {
					final MMXRootObject rootObject = (MMXRootObject) adaptableObject;
					final WeakReference<ReferenceValueProviderCache> existingReference = cacheCache.get(rootObject);
					final IReferenceValueProviderProvider existingValue = (existingReference == null) ? null : existingReference.get();
					if (existingValue == null) {
						final ReferenceValueProviderCache newCache = new ReferenceValueProviderCache(rootObject);
						cacheCache.put(rootObject, new WeakReference<ReferenceValueProviderCache>(newCache));
						return adapterType.cast(newCache);
					}
					return adapterType.cast(existingValue);
				}
			}
		}
		return adapterType.cast(null);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { ReferenceValueProviderCache.class };
	}
}
