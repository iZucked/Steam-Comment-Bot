/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;

public abstract class AbstractRegistry<Key, Factory> {
	protected final Map<Key, Factory> cache = new HashMap<Key, Factory>();
	protected final Map<String, Factory> cacheByID = new HashMap<String, Factory>();
	
	protected int getMinimumGenerations(final EClass eClass,
			final String canonicalNameOfSuper) {
		if (canonicalNameOfSuper == null) return Integer.MAX_VALUE;
		if (eClass.getInstanceClass().getCanonicalName()
				.equals(canonicalNameOfSuper)) {
			return 0;
		}
		if (eClass.getESuperTypes().isEmpty()) {
			if (canonicalNameOfSuper.equals(EcorePackage.eINSTANCE.getEObject().getInstanceClass().getCanonicalName())) {
				return 1;
			} else {
				return Integer.MAX_VALUE;
			}
		}
		int result = Integer.MAX_VALUE;
		for (final EClass superClass : eClass.getESuperTypes()) {
			final int d = getMinimumGenerations(superClass,
					canonicalNameOfSuper);
			if (d - 1 < result - 1) {
				result = d + 1;
			}
		}
		return result;
	}
	
	protected boolean factoryExistsForID(final String ID) {
		return cacheByID.containsKey(ID);
	}
	
	protected Factory cacheFactoryForID(final String ID, final Factory factory) {
		cacheByID.put(ID, factory);
		return factory;
	}
	
	protected Factory getFactoryForID(final String ID) {
		return cacheByID.get(ID);
	}
	
	protected abstract Factory match(final Key key);
	
	protected Factory get(final Key key) {
		if (cache.containsKey(key)) return cache.get(key);
		final Factory m = match(key);
		cache.put(key, m);
		return m;
	}
}
