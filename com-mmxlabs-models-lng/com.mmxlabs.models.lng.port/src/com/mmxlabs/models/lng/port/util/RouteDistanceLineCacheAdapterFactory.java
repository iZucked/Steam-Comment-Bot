/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.util;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;

import com.mmxlabs.models.lng.port.Route;

public class RouteDistanceLineCacheAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Object adaptableObject, final Class adapterType) {

		if (adaptableObject instanceof Route) {
			final Route route = (Route) adaptableObject;
			synchronized (route) {
				for (final Adapter eAdapter : route.eAdapters()) {
					if (eAdapter instanceof RouteDistanceLineCache) {
						return (RouteDistanceLineCache) eAdapter;
					}
				}
				final RouteDistanceLineCache cache = new RouteDistanceLineCache(route);
				route.eAdapters().add(cache);
				return cache;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { RouteDistanceLineCache.class };
	}

}
