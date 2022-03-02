/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

/**
 * Utility class to create a shared {@link ComposedAdapterFactory} instance with the basic item provider adapter factories. This is used with {@link EcoreLabelProvider} as we cannot do this stuff
 * easily in the constructor.
 * 
 * @author Simon Goodall
 * 
 */
public class EcoreComposedAdapterFactory {
	private static ComposedAdapterFactory instance;

	public static final ComposedAdapterFactory getAdapterFactory() {
		if (instance == null) {
			instance = createNewAdapterFactory();
		}
		return instance;
	}

	private static ComposedAdapterFactory createNewAdapterFactory() {
		return new ComposedAdapterFactory(createFactoryList());
	}

	public static final List<AdapterFactory> createFactoryList() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return factories;
	}
}
