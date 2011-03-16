package com.mmxlabs.rcp.navigator.ecore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

public class EcoreComposedAdapterFactory {
	private static ComposedAdapterFactory instance;

	public final static ComposedAdapterFactory getAdapterFactory() {
		if (instance == null) {
			instance = new ComposedAdapterFactory(createFactoryList());
		}
		return instance;
	}

	public final static List<AdapterFactory> createFactoryList() {
		final List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return factories;
	}
}
