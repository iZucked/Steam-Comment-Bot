/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

import com.mmxlabs.scenario.service.model.provider.ScenarioServiceItemProviderAdapterFactory;

/**
 * Utility class to create a shared {@link ComposedAdapterFactory} instance with the {@link ScenarioServiceItemProviderAdapterFactory} the basic item provider adapter factories.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioServiceComposedAdapterFactory {
	private static ComposedAdapterFactory instance;

	public final static ComposedAdapterFactory getAdapterFactory() {
		if (instance == null) {
			instance = createNewAdapterFactory();
		}
		return instance;
	}

	private static ComposedAdapterFactory createNewAdapterFactory() {
		return new ComposedAdapterFactory(createFactoryList());
	}

	public final static List<AdapterFactory> createFactoryList() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new ScenarioServiceItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return factories;
	}
}
