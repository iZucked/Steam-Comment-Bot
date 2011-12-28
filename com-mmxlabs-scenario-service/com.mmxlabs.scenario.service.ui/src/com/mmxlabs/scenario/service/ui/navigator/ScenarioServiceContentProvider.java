package com.mmxlabs.scenario.service.ui.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

import com.mmxlabs.scenario.service.model.provider.ScenarioServiceItemProviderAdapterFactory;

public class ScenarioServiceContentProvider extends AdapterFactoryContentProvider {

	public ScenarioServiceContentProvider() {
		super(new ScenarioServiceItemProviderAdapterFactory());
	}
}
