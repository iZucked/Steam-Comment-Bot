/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;

import com.mmxlabs.models.lng.port.Route;

public class RouteDistanceLineCacheAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {

		if (adaptableObject instanceof Route) {
			final Route route = (Route) adaptableObject;
			synchronized (route) {
				for (final Adapter eAdapter : route.eAdapters()) {
					if (eAdapter instanceof RouteDistanceLineCache) {
						return (T) eAdapter;
					}
				}
				final RouteDistanceLineCache cache = new RouteDistanceLineCache(route);
				route.eAdapters().add(cache);
				return (T) cache;
			}
		}
		return (T) null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { RouteDistanceLineCache.class };
	}

}
