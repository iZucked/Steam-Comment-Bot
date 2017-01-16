/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;

import com.mmxlabs.models.lng.pricing.PricingModel;

public class MarketIndexCacheAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {

		if (adaptableObject instanceof PricingModel) {
			final PricingModel pricingModel = (PricingModel) adaptableObject;
			synchronized (pricingModel) {
				for (final Adapter eAdapter : pricingModel.eAdapters()) {
					if (eAdapter instanceof MarketIndexCache) {
						return (T) eAdapter;
					}
				}
				final MarketIndexCache cache = new MarketIndexCache(pricingModel);
				pricingModel.eAdapters().add(cache);
				return (T) cache;
			}
		}
		return (T) null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { MarketIndexCache.class };
	}

}
